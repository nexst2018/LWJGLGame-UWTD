package net.nexst.UWTD.resources.map;

import static net.nexst.UWTD.util.Terminal.PREFIX;

import java.util.ArrayList;
import java.util.HashMap;

import net.nexst.UWTD.resources.TileImgResource;
import net.nexst.UWTD.resources.TilesXMLDataResource;

/**
 * Layer类将贴图坐标转换并将贴图与材质文件对齐
 * 
 * */
public class Layer
{
	private String layerName;
	private int layerWidth;
	private int layerHeight;
	private HashMap<Integer, HashMap<Integer, TileImgResource>> imgList;

	public Layer(MapTilesImgManager mapTilesImgManager, ArrayList<TilesXMLDataResource> tilesXMLDataResourceList,
			ArrayList<Integer> startIndex, ArrayList<String> layerContent, String layerName, int layerWidth,
			int layerHeight)
	{

		if (tilesXMLDataResourceList.size() != startIndex.size())
		{
			System.err.println(PREFIX + "初始化图层时关键错误，这是由于贴图索引数量与贴图数量不一致导致的");
			throw new RuntimeException("Fatal ERROR!");
		} else if (layerHeight != layerContent.size())
		{
			System.err.println(PREFIX + "初始化图层时关键错误，这是由于预设的贴图高度与实际贴图高度不一致导致的");
			throw new RuntimeException("Fatal ERROR!");
		}

		this.layerName = layerName;
		this.layerHeight = layerHeight;
		this.layerWidth = layerWidth;

		imgList = new HashMap<Integer, HashMap<Integer, TileImgResource>>();
		HashMap<Integer, TileImgResource> subList;
		for (int y = 0; y < layerHeight; y++)
		{
			String line = layerContent.get(y);
			String[] strIds = line.split(",");
			subList = new HashMap<Integer, TileImgResource>();

			for (int x = 0; x < layerWidth; x++)
			{
				subList.put(x, mapTilesImgManager.getImg(Integer.parseInt(strIds[x])));
			}
			imgList.put(y, subList);
		}

	}

	public String getLayerName()
	{
		return layerName;
	}

	public int getLayerWidth()
	{
		return layerWidth;
	}

	public int getLayerHeight()
	{
		return layerHeight;
	}

	public TileImgResource getTileImgResource(int x, int y)
	{
		return imgList.get(y + 1).get(x + 1);
	}
}
