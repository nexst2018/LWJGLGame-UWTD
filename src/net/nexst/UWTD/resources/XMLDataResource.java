package net.nexst.UWTD.resources;

import static net.nexst.UWTD.util.Terminal.PREFIX;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 继承DataResource，储存XML类型的数据资源，封装了基本XML解析功能
 * 
 * @author LTXOM
 * @version 5/20/2018
 */
public class XMLDataResource extends DataResource
{
	private String version;
	private String encoding;

	private ArrayList<String> matchMap;

	// 找到首标签后但尾标签不在当前行会将
	// 首标签坐标数据放入waitList中
	private ArrayList<String> waitList;

	/**
	 * 通过本地文件构造XML资源对象
	 * */
	public XMLDataResource(String path)
	{
		super(path);

		matchMap = new ArrayList<String>();
		waitList = new ArrayList<String>();
		analyzeXML();
	}

	/**
	 * 通过ArrayList构造XML资源对象，一般用于本地文件资源对象的子集
	 * */
	public XMLDataResource(ArrayList<String> content)
	{
		super(content);

		matchMap = new ArrayList<String>();
		waitList = new ArrayList<String>();
		analyzeXML();
	}

	private void analyzeXML()
	{
		analyzeMatchMap();
	}

	private void analyzeMatchMap()
	{
		// 首先读取版本信息
		int start = 0;
		for (int i = 0; i < this.getSource().size(); i++)
		{

			String line = this.getSource().get(i);
			if (line.startsWith("<?") && line.contains("xml version=\""))
			{
				String tempVersion = line.substring(line.indexOf("version=\"") + "version=\"".length());
				this.version = tempVersion.substring(0, tempVersion.indexOf("\""));

				String tempEncoding = line.substring(line.indexOf("encoding=\"") + "encoding=\"".length());
				this.encoding = tempEncoding.substring(0, tempEncoding.indexOf("\""));
				start = i + 1;
				break;
			}
		}

		// 填充matchMap列表
		int matchMapIndex = 0;

		a: for (int i = start; i < this.getSource().size(); i++)
		{

			// 在实际上文件中的行数:
			int index = i + 1;
			String line = this.getSource().get(i);
			String prefix;
			if (line.contains("<") && line.contains(">") && !line.trim().startsWith("</"))
			{

				prefix = line.substring(line.indexOf("<") + 1,
						line.substring(line.indexOf("<")).indexOf(" ") + line.indexOf("<"));
				if (line.endsWith("/>"))
				{
					matchMap.add(prefix + ", 1, " + index + ", " + index);
					matchMapIndex++;
					continue a;
				} else if (line.substring(line.indexOf(">")).contains("<"))
				{
					prefix = line.substring(line.indexOf("<") + 1, line.indexOf(">"));
					matchMap.add(prefix + ", 0, " + index + ", " + index);
					matchMapIndex++;
					continue a;
				} else if (!line.contains("</"))
				{
					if (line.substring(line.indexOf("<")).indexOf(" ") > line.indexOf(">"))
					{
						prefix = line.substring(line.indexOf("<"), line.indexOf(">"));
					}
					waitList.add(prefix + "," + matchMapIndex);
					matchMap.add(prefix + ", 0, " + index);
					matchMapIndex++;
					continue a;
				}
			} else if (line.contains("</"))
			{
				prefix = line.substring(line.indexOf("</") + 2, line.indexOf(">"));
				for (int j = waitList.size() - 1; j >= 0; j--)
				{
					String[] arr = waitList.get(j).split(",");
					if (arr[0].equals(prefix))
					{
						matchMap.set(Integer.parseInt(arr[1]), matchMap.get(Integer.parseInt(arr[1])) + ", " + index);
						waitList.remove(j);
						continue a;
					}
				}
			}
		}
	}

	/**
	 * @return 返回当前XML版本
	 * */
	public String getVersion()
	{
		return version;
	}

	/**
	 * @return 返回当前XML编码格式
	 * */
	public String getEncoding()
	{
		return encoding;
	}

	/**
	 * matchMap用来储存分析到的XML标签对
	 * 比如下列XML：
	 * 1:<nodeA>
	 * 2: <nodeB>
	 * 3: abcdf
	 * 4: </nodeB>
	 * 5: <nodeC />
	 * 6: <nodeD>abc</nodeD>
	 * 7: <nodeB>
	 * 8: 123
	 * 9: </nodeB>
	 * 10:</nodeA>
	 * 
	 * 在matchMap中将会以String列表存储四个信息
	 * 第一个是node名称，第二个是连接状态，0是分开标签，1是单独标签
	 * 第三第四个是开始/结束位置
	 * 例如以下信息：
	 * 0: "nodeA, 0, 1, 10"
	 * 1: "nodeB, 0, 2, 4"
	 * 2: "nodeC, 1, 5, 5"
	 * 3: "nodeD, 0, 6, 6"
	 * 4: "nodeB, 0, 7, 9"
	 * @return 返回matchMap
	 * */
	public ArrayList<String> getMatchMap()
	{
		return matchMap;
	}

	/**
	 * 得到指定标签的参数值
	 * 
	 * @param matchMapIndex 要得到的标签在matchMap中的索引位置
	 * @param parameter 想要得到的参数
	 * 
	 * @return 若不存在参数，将会返回null
	 * */
	public String getTagParameter(int matchMapIndex, String parameter)
	{
		String result = null;
		String[] tagIndexArr = matchMap.get(matchMapIndex).split(",");
		int startAt = Integer.parseInt(tagIndexArr[2].trim()) - 1;
		String tagName = tagIndexArr[0].trim();
		String line = this.getSource().get(startAt);

		if (!line.contains(" " + parameter + "="))
		{
			System.err.println(PREFIX + "XML资源文件" + this.getPath() + "第" + (startAt + 1) + "行的" + tagName + "中不存在"
					+ parameter + "属性，请检查");
			return null;
		}

		String halfResult = line.substring(line.indexOf(parameter) + (parameter + "=\"").length());
		result = halfResult.substring(0, halfResult.indexOf("\""));

		return result;
	}

	/**
	 * 得到指定标签的所有参数
	 * @param matchMapIndex 要得到的标签在matchMap中的位置
	 * @return 返回数据以HashMap键值对存储
	 * */
	public HashMap<String, String> getTagParameters(int matchMapIndex)
	{
		HashMap<String, String> result = new HashMap<String, String>();

		String[] tagIndexArr = matchMap.get(matchMapIndex).split(",");
		int startAt = Integer.parseInt(tagIndexArr[2].trim()) - 1;
		String tagName = tagIndexArr[0].trim();
		String line = this.getSource().get(startAt);

		if (!line.contains("="))
		{
			System.err.println(
					PREFIX + "XML资源文件" + this.getPath() + "第" + (startAt + 1) + "行的" + tagName + "中不存在" + "任何属性，请检查");
			return null;
		}
		line = line.trim();

		while (line.contains("="))
		{
			String firstHalf = line.substring(0, line.indexOf("="));
			String secondHalf = line.substring(line.indexOf("="));

			result.put(firstHalf.substring(firstHalf.indexOf(" ") + 1),
					secondHalf.substring(2, secondHalf.substring(2).indexOf("\"") + 2));

			line = line.substring(line.indexOf("=") + 1, line.length());

		}

		return result;
	}

	/**
	 * 得到两个标签之间所有的内容
	 * @param matchMapIndex 要得到的内容在matchMap中的位置
	 * @return 若标签只有属性没有内容，则会返回null
	 * */
	public ArrayList<String> getContent(int matchMapIndex)
	{
		ArrayList<String> result = new ArrayList<String>();
		String[] tagIndexArr = matchMap.get(matchMapIndex).split(",");
		int tageType = Integer.parseInt(tagIndexArr[1].trim());
		int startAt = Integer.parseInt(tagIndexArr[2].trim()) - 1;
		int endAt = Integer.parseInt(tagIndexArr[3].trim()) - 1;

		String tagName = tagIndexArr[0].trim();

		if (tageType == 1)
		{
			System.err.println(PREFIX + "标签" + tagName + "是属性标签，没有数据！");
			return null;
		}
		if (startAt == endAt)
		{
			String temp = this.getSource().get(startAt);
			temp = temp.substring(temp.indexOf(">") + 1);
			temp = temp.substring(0, temp.indexOf("<"));
			result.add(temp);
			return result;
		}

		for (int i = startAt + 1; i < endAt; i++)
		{
			result.add(this.getSource().get(i));

		}

		return result;
	}

}
