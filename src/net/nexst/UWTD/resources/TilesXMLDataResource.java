/**
 * 
 */
package net.nexst.UWTD.resources;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * 储存贴图XML信息资源，一般与MapXMLDataResource联动
 * 
 * @see MapXMLDataResource
 * @author LTXOM
 * @version 05/20/2018
 */
public class TilesXMLDataResource extends XMLDataResource
{
	private String tileName;
	private int tileWidth;
	private int tileHeight;

	private int tileCount;
	private int tileColumns;// x
	private int tileRows;// y

	private String imgSourcePath;


	private ImgResource imgResource;

	private CollidersResource colliders;

	private TileImgResource[][] tilesSet;

	/**
	 * @param path 贴图XML文件路径
	 */
	public TilesXMLDataResource(String path)
	{
		super(path);
		init();
	}

	/**
	 * 通过ArrayList构造贴图XML资源对象，一般用于资源对象的子集
	 * @param content 子贴图XML文件内容
	 */
	public TilesXMLDataResource(ArrayList<String> content)
	{
		super(content);
		init();
	}

	private void init()
	{
		int endAt = 0;
		for (int i = 0; i < this.getMatchMap().size(); i++)
		{
			if (this.getMatchMap().get(i).contains("tileset"))
			{
				HashMap<String, String> parametersMap = this.getTagParameters(i);
				tileName = parametersMap.get("name");
				tileWidth = Integer.parseInt(parametersMap.get("tilewidth"));
				tileHeight = Integer.parseInt(parametersMap.get("tileheight"));
				tileCount = Integer.parseInt(parametersMap.get("tilecount"));
				tileColumns = Integer.parseInt(parametersMap.get("columns"));
				tileRows = tileCount / tileColumns;
			} else if (this.getMatchMap().get(i).contains("image"))
			{
				HashMap<String, String> parametersMap = this.getTagParameters(i);
				imgSourcePath = parametersMap.get("source");
//				imgHeight = Integer.parseInt(parametersMap.get("width"));
//				imgWidth = Integer.parseInt(parametersMap.get("height"));
				endAt = i;
				break;
			}
		}

		// init image
		imgResource = new ImgResource("res/mapsData/" + imgSourcePath.substring(3));
		// 这里切割并创建BufferedImage保存到二维数组
		crop();

		// init colliders
		HashMap<Integer, Double> x = new HashMap<Integer, Double>();
		HashMap<Integer, Double> y = new HashMap<Integer, Double>();
		HashMap<Integer, Double> width = new HashMap<Integer, Double>();
		HashMap<Integer, Double> height = new HashMap<Integer, Double>();
		int id = -1;
		for (int i = endAt; i < this.getMatchMap().size(); i++)
		{
			if (this.getMatchMap().get(i).startsWith("tile, 0"))
			{
				id = Integer.parseInt(this.getTagParameter(i, "id"));
			}
			if (this.getMatchMap().get(i).startsWith("object, 1"))
			{
				HashMap<String, String> parametersMap = this.getTagParameters(i);
				x.put(id, Double.parseDouble(parametersMap.get("x")));
				y.put(id, Double.parseDouble(parametersMap.get("y")));
				width.put(id, Double.parseDouble(parametersMap.get("width")));
				height.put(id, Double.parseDouble(parametersMap.get("height")));
			}
		}
		colliders = new CollidersResource(tileName, x, y, width, height);
	}

	private void crop()
	{
		tilesSet = new TileImgResource[tileRows][tileColumns];

		for (int y = 0; y < tileRows; y++)
		{
			for (int x = 0; x < tileColumns; x++)
			{

				tilesSet[y][x] = new TileImgResource(imgResource.getBufferedImage().getSubimage(x * tileWidth,
						y * tileHeight, tileWidth, tileHeight), tileName, x, y);
			}
		}
	}

	/**
	 * @return 返回材质名
	 * */
	public String getTileName()
	{
		return tileName;
	}

	/**
	 * @see CollidersResource
	 * @return 返回封装了该材质所有碰撞器的CollidersResource对象
	 * */
	public CollidersResource getColliders()
	{
		return colliders;
	}

	public TileImgResource[][] getTilesSet()
	{
		return tilesSet;
	}

}
