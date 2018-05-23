package net.nexst.UWTD.resources;

import static net.nexst.UWTD.util.Terminal.PREFIX;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 储存数据资源
 * 
 * @author LTXOM
 * @version 5/19/2018
 */
public class DataResource extends Resource
{
	private ArrayList<String> sourceList;

	/**
	 * 资源文件必须是数据文件，构造器将读取其内容存放至sourceList中
	 */
	public DataResource(String path)
	{
		super(path, ResourceType.DATA);
		sourceList = new ArrayList<String>();
		BufferedReader bfr = null;
		try
		{
			bfr = new BufferedReader(new FileReader(this.getFile()));
			String line;
			while ((line = bfr.readLine()) != null)
			{
				sourceList.add(line);
			}
		} catch (IOException e)
		{
			System.err.println(PREFIX + "无法读取文件：" + this.getPath());
			e.printStackTrace();
		} finally
		{
			try
			{
				bfr.close();
			} catch (IOException e)
			{
				System.err.println(PREFIX + "无法关闭文件流，以下是错误信息");
				e.printStackTrace();
			}
		}

	}

	/**
	 * 也可以从ArrayList中读取数据
	 */
	public DataResource(ArrayList<String> sourceList)
	{
		super(ResourceType.DATA);
		this.sourceList = sourceList;

	}

	/**
	 * @return 得到数据资源的内容
	 */
	public ArrayList<String> getSource()
	{
		return sourceList;
	}
}
