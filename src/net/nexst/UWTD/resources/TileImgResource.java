package net.nexst.UWTD.resources;

import java.awt.image.BufferedImage;

/**
 * 用于储存单个材质图像，使用父类getBufferedImage()方法返回图像缓冲
 * */
public class TileImgResource extends ImgResource
{
	private String tileName;
	private int x;
	private int y;

	public TileImgResource(BufferedImage image, String tileName, int x, int y)
	{
		super(image);
		this.x = x;
		this.y = y;
		this.tileName = tileName;
	}

	/**
	 * @return 返回当前(单片)材质相对应贴图的名字
	 * */
	public String getTileName()
	{
		return tileName;
	}

	/**
	 * @return 返回当前(单片)材质相对于贴图的X坐标
	 * */
	public int getXLocation()
	{
		return x;
	}

	/**
	 * @return 返回当前(单片)材质相对于贴图的Y坐标
	 * */
	public int getYLocation()
	{
		return y;
	}

}
