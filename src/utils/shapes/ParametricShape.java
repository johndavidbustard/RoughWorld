package utils.shapes;

import java.io.Serializable;

public abstract class ParametricShape implements Serializable 
{
	private static final long serialVersionUID = 1L;

	public static final String[] buildingShapes = {"RectangularBuildingFloor","ReverseRBuildingFloor"};
	public static final String[] roomShapes = {"RectangularRoom","RRoom"};
	public static final String[] portalShapes = {"RectangularPortal"};
	public static final String[] objectShapes = {"Cuboid","CSGShape","DeformedCuboid","QuadraticCurvedCuboid","ModifiedCopyShape"};

	public static String[] getShapesByType(String type)
	{
		if(type.equalsIgnoreCase("Building"))
		{
			return buildingShapes;
		}
		else
		if(type.equalsIgnoreCase("Room"))
		{
			return roomShapes;
		}
		else
		if(type.equalsIgnoreCase("Portal"))
		{
			return portalShapes;
		}
		else
		if(type.equalsIgnoreCase("Object"))
		{
			return objectShapes;
		}
		return null;
	}
	
	public double[] parameters;
	
	public abstract String[] getParameterNames();
	public abstract String[] getSideNames();
	
	public abstract void setCuboidDimensions(double x,double y,double z);
	public abstract void getCuboidDimensions(double[] xyz);
	
	public abstract ParametricShape copy();
	
	public void parametersUpdated() {}
	
	public void makeSimilarSize(ParametricShape oldShape)
	{
		double[] xyz = new double[3];
		oldShape.getCuboidDimensions(xyz);
		setCuboidDimensions(xyz[0], xyz[1], xyz[2]);
	}

	public static ParametricShape get(String name)
	{
		if(name.equalsIgnoreCase("ModifiedCopyShape"))
		{
			return new ModifiedCopyShape();
		}
		else
		if(name.equalsIgnoreCase("QuadraticCurvedCuboid"))
		{
			return new QuadraticCurvedCuboid();
		}
		else
		if(name.equalsIgnoreCase("DeformedCuboid"))
		{
			return new DeformedCuboid();
		}
		else
		if(name.equalsIgnoreCase("CSGShape"))
		{
			return new CSGShape();
		}
		else
		if(name.equalsIgnoreCase("Cuboid"))
		{
			return new Cuboid();
		}
		else
		if(name.equalsIgnoreCase("RectangularPortal"))
		{
			return new RectangularPortal();
		}
		else
		if(name.equalsIgnoreCase("RectangularRoom"))
		{
			return new RectangularRoom();
		}
		else
		if(name.equalsIgnoreCase("RRoom"))
		{
			return new RRoom();
		}
		else
		if(name.equalsIgnoreCase("RectangularBuildingFloor"))
		{
			return new RectangularBuildingFloor();
		}
		else
		if(name.equalsIgnoreCase("ReverseRBuildingFloor"))
		{
			return new ReverseRBuildingFloor();
		}
		return null;
	}
}
