import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.nexst.UWTD.resources.MapXMLDataResource;

public class Tester
{

	public static void main(String[] args) throws IOException
	{
		int layer = 1;
		int x = 9;
		int y = 15;
		MapXMLDataResource mxdr = new MapXMLDataResource("res/mapsData/Maps/Demo.tmx");

		File outputfile = new File("res/image.png");
		ImageIO.write(mxdr.getLayer(layer).getTileImgResource(x, y).getBufferedImage(), "png", outputfile);

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
