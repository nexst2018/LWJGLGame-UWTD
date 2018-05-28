package net.nexst.UWTD.resources.map;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.nexst.UWTD.resources.MapXMLDataResource;

/**
 * 整理渲染地图时所需要用到的数据，用于向渲染器传递数据
 * @author LTXOM
 * @version 5/28/2018
 * */
public class MapRendererHelper
{
	private MapXMLDataResource mxdr;
	private BufferedImage[][] images;
	private HashMap<BufferedImage, Integer> imageIdMap;

	/**
	 * 构造器需要传入MapXMLDataResource对象，例如：
	 * 
	 * new MapRendererHelper(new MapXMLDataResource("res/mapsData/Maps/Demo.tmx"));
	 * 
	 * @see MapXMLDataResource
	 * 
	 * */
	public MapRendererHelper(MapXMLDataResource mxdr)
	{
		this.mxdr = mxdr;
		imageIdMap = new HashMap<BufferedImage, Integer>();
		images = new BufferedImage[this.getLayerSize()][];
		init();
	}

	private void init()
	{
		for (int i = 0; i < this.getLayerSize(); i++)
		{
			HashSet<Integer> usedIds = mxdr.getLayer(i).getUsedIds();

			images[i] = new BufferedImage[usedIds.contains(0) ? usedIds.size() - 1 : usedIds.size()];
			int counter = 0;
			for (int next : usedIds)
			{
				if (next == 0)
					continue;

				images[i][counter] = mxdr.getMapTilesImgManager().getImg(next).getBufferedImage();
				imageIdMap.put(images[i][counter], next);
				counter++;
			}
		}

	}

	/**
	 * @return 得到指定图层用到的BufferedImage对象(不重复)
	 * */
	public BufferedImage[] getBufferedImage(int layer)
	{
		return images[layer];
	}

	/**
	 * 得到指定图层的指定BufferedImage对象所有出现在地图上的坐标
	 * @param bi 指定的BufferedImage对象(必须是TileImgResource引用的BufferedImage对象,或该类getBufferedImage(int layer)中的BufferedImage对象)
	 * @param layer 指定图层
	 * @return 返回的数组为八的倍数，每八个元素(以几何坐标系)储存四个顶点坐标，顺序为左下、左上、右上、右下
	 * */
	public float[] getVertex(BufferedImage bi, int layer)
	{
		ArrayList<String> coorList = null;
		for (int i = 0; i < images[layer].length; i++)
		{
			if (bi.equals(images[layer][i]))
			{
				coorList = mxdr.getLayer(layer).getTileCoordMap()
						.get(mxdr.getMapTilesImgManager().getImg(imageIdMap.get(images[layer][i])));
				break;
			}
		}

		if (coorList != null)
		{
			float[] result = new float[coorList.size() * 8];
			double width = mxdr.getLayer(layer).getLayerWidth();
			double height = mxdr.getLayer(layer).getLayerHeight();
			int x, y;
			int counter = 0;
			for (int i = 0; i < coorList.size(); i++)
			{
				String[] temp = coorList.get(i).split(",");
				x = Integer.parseInt(temp[0]);
				y = Integer.parseInt(temp[1]);
				// a: X & Y
				result[counter++] = (float) (x / width);
				result[counter++] = (float) ((height - 1f - y) / height);

				// b: X & Y
				result[counter++] = (float) (x / width);
				result[counter++] = (float) ((height - y) / height);

				// c: X & Y
				result[counter++] = (float) ((x + 1f) / width);
				result[counter++] = (float) ((height - y) / height);

				// d: X & Y
				result[counter++] = (float) ((x + 1f) / width);
				result[counter++] = (float) ((height - 1f - y) / height);

			}
			System.out.println(coorList);
			return result;
		} else
		{
			return null;
		}
	}

	/**
	 * @return 得到当前地图的图层数量
	 * */
	public int getLayerSize()
	{
		return mxdr.getLayerSize();
	}
}
