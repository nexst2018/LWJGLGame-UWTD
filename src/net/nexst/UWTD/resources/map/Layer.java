package net.nexst.UWTD.resources.map;

import static net.nexst.UWTD.util.Terminal.PREFIX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
	private HashMap<TileImgResource, ArrayList<String>> coordMap;
	private HashSet<Integer> usedId;

	public Layer(MapTilesImgManager mapTilesImgManager, ArrayList<TilesXMLDataResource> tilesXMLDataResourceList,
			ArrayList<Integer> startIndex, ArrayList<String> layerContent, String layerName, int layerWidth,
			int layerHeight)
	{
		usedId = new HashSet<Integer>();

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

		coordMap = new HashMap<TileImgResource, ArrayList<String>>();

		for (int y = 0; y < layerHeight; y++)
		{
			String line = layerContent.get(y);
			String[] strIds = line.split(",");

			for (int x = 0; x < layerWidth; x++)
			{
				if (!coordMap.containsKey(mapTilesImgManager.getImg(Integer.parseInt(strIds[x]))))
				{
					coordMap.put(mapTilesImgManager.getImg(Integer.parseInt(strIds[x])), new ArrayList<String>());
				}
				coordMap.get(mapTilesImgManager.getImg(Integer.parseInt(strIds[x]))).add(x + "," + y);
				usedId.add(Integer.parseInt(strIds[x]));
			}
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

	/**
	 * @return 返回贴图以及其对应的所有坐标：HashMap<TileImgResource, ArrayList<String>>
	 * */
	public HashMap<TileImgResource, ArrayList<String>> getTileCoordMap()
	{
		return coordMap;
	}

	public HashSet<Integer> getUsedIds()
	{
		return usedId;
	}
}
