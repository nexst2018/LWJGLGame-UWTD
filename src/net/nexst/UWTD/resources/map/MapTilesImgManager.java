package net.nexst.UWTD.resources.map;

import java.util.ArrayList;
import java.util.HashMap;

import net.nexst.UWTD.resources.TileImgResource;
import net.nexst.UWTD.resources.TilesXMLDataResource;

/**
 * 每一个MapTilesImgManager负责管理一个地图的材质，
 * 该类实质是封装多个TileImgResource对象
 * @see TileImgResource
 * 
 * */
public class MapTilesImgManager
{
	private HashMap<Integer, TileImgResource> imgMap;

	public MapTilesImgManager(ArrayList<TilesXMLDataResource> tilesXMLDataResourceList)
	{

		imgMap = new HashMap<Integer, TileImgResource>();
		int counter = 1;
		for (int i = 0; i < tilesXMLDataResourceList.size(); i++)
		{
			TileImgResource[][] tilesSet = tilesXMLDataResourceList.get(i).getTilesSet();
			for (int y = 0; y < tilesSet.length; y++)
			{
				for (int x = 0; x < tilesSet[0].length; x++)
				{
					imgMap.put(counter++, tilesSet[y][x]);
				}
			}
		}
	}

	/**
	 * @param id 
	 * @return 返回TileImgResource材质对象
	 * */
	public TileImgResource getImg(int id)
	{	
		return imgMap.get(id);
	}
}
