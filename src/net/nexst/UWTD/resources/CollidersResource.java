package net.nexst.UWTD.resources;

import java.util.HashMap;

/**
 * 本类用来存储单个贴图的碰撞器坐标信息，
 * 每个TilesXMLDataResource都对应一个CollidersResource对象
 * */
public class CollidersResource extends Resource
{
	private String tileName;
	private HashMap<Integer, Double> x;
	private HashMap<Integer, Double> y;
	private HashMap<Integer, Double> width;
	private HashMap<Integer, Double> height;

	/**
	 * 一般不直接创建该类对象，通过TitlesXMLDataResource类中的getColliders()方法调用
	 * @see TitlesXMLDataResource
	 * 
	 * */
	public CollidersResource(String tileName, HashMap<Integer, Double> x, HashMap<Integer, Double> y,
			HashMap<Integer, Double> width, HashMap<Integer, Double> height)
	{
		super(ResourceType.COLLIDER);
		this.tileName = tileName;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * @return 返回材质名
	 * */
	public String getTileName()
	{
		return tileName;
	}

	/**
	 * @return 返回当前材质指定id碰撞器的x坐标
	 * */
	public double getX(int id)
	{
		return x.get(id);
	}

	/**
	 * @return 返回当前材质指定id碰撞器的y坐标
	 * */
	public double getY(int id)
	{
		return y.get(id);
	}

	/**
	 * @return 返回当前材质指定id碰撞器的宽度
	 * */
	public double getWidth(int id)
	{
		return width.get(id);
	}

	/**
	 * @return 返回当前材质指定id碰撞器的高度
	 * */
	public double getHeight(int id)
	{
		return height.get(id);
	}
}
