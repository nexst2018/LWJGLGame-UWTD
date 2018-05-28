import java.io.IOException;

import net.nexst.UWTD.resources.MapXMLDataResource;
import net.nexst.UWTD.resources.map.MapRendererHelper;

public class Tester
{

	public static void main(String[] args) throws IOException
	{
		int layer = 1;
		int x = 9;
		int y = 15;
		MapXMLDataResource mxdr = new MapXMLDataResource("res/mapsData/Maps/Demo.tmx");
		MapRendererHelper mrh = new MapRendererHelper(mxdr);

		System.out.println(mxdr.getLayer(0).getLayerWidth());
		float[] result = mrh.getVertex(mrh.getBufferedImage(1)[0], 1);
		for (int i = 0; i < result.length; i++)
		{
			if (i % 8 == 0)
				System.out.println();
			System.out.println(result[i]);

		}

		// BufferedImage[] imageArr = mrh.getBufferedImage(1);

		// for (int i = 0; i < imageArr.length; i++)
		// {
		// File outputfile = new File("res/image" + i + ".png");
		// ImageIO.write(imageArr[i], "png", outputfile);

		// }
		// TilesXMLDataResource tmdr = new
		// TilesXMLDataResource("res/mapsData/TilesData/OutsideBlocks1.tsx");
		// System.out.println();
		// 贴图切割测试
		// for (int y = 0; y < 16; y++)
		// {
		// for (int x = 0; x < 8; x++)
		// {
		// File outputfile = new File("res/image-x" + (x + 1) + "-y" + (y + 1) +
		// ".png");
		// ImageIO.write(tmdr.getTilesSet()[y][x].getBufferedImage(), "png",
		// outputfile);
		//
		// }
		// }

	}

}
