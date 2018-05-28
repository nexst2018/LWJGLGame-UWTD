package net.nexst.UWTD.resources;

import java.util.ArrayList;

import net.nexst.UWTD.resources.map.*;

/**
 * 储存地图XML信息资源，一般与TilesXMLDataResource联动
 * 
 * @see TilesXMLDataResource
 * @author LTXOM
 * @version 05/21/2018
 */
public class MapXMLDataResource extends XMLDataResource
{
	private ArrayList<TilesXMLDataResource> tilesXMLDataResourceList;
	private ArrayList<Layer> layersList;
	private ArrayList<Integer> startIndex;
	private MapTilesImgManager mapTilesImgManager;

	/**
	 * @param filePath 地图XML文件路径
	 */
	public MapXMLDataResource(String filePath)
	{
		super(filePath);
		init();
	}

	private void init()
	{
		tilesXMLDataResourceList = new ArrayList<TilesXMLDataResource>();
		layersList = new ArrayList<Layer>();
		startIndex = new ArrayList<Integer>();

		int endAt = 0;
		for (int i = 0; i < this.getMatchMap().size(); i++)
		{
			if (this.getMatchMap().get(i).startsWith("tileset, 1"))
			{
				String path = "res/mapsData/" + this.getTagParameter(i, "source").substring(3);
				tilesXMLDataResourceList.add(new TilesXMLDataResource(path));
				startIndex.add(Integer.parseInt(this.getTagParameter(i, "firstgid")));
			} else if (this.getMatchMap().get(i).startsWith("layer, 0"))
			{
				endAt = i;
				break;

			}
		}
		mapTilesImgManager = new MapTilesImgManager(tilesXMLDataResourceList);

		for (int i = endAt; i < this.getMatchMap().size(); i++)
		{
			if (this.getMatchMap().get(i).startsWith("layer, 0"))
			{
				Layer layer = new Layer(mapTilesImgManager, tilesXMLDataResourceList, startIndex,
						this.getContent(i + 1), this.getTagParameter(i, "name"),
						Integer.parseInt(this.getTagParameter(i, "width")),
						Integer.parseInt(this.getTagParameter(i, "height")));
				layersList.add(layer);
			}
		}
	}

	public ArrayList<TilesXMLDataResource> getTilesXMLDataResourceList()
	{
		return tilesXMLDataResourceList;
	}

	public Layer getLayer(int layerIndex)
	{
		return layersList.get(layerIndex);
	}

	public int getLayerSize()
	{
		return layersList.size();
	}
	
	public MapTilesImgManager getMapTilesImgManager() {
		return mapTilesImgManager;
	}
}
