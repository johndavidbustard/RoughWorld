package ai.knowledge_representation.state_instance;

import java.io.Serializable;

import utils.ArrayUtils;

public class ObjectPhysicalInstance implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String iconImage;
	//There may be multiple photographs of an instance of an object
	public String[] photographs;
	//There may be a 3d model 
	//typically an obj file with mtl and texture for rigid objects
	//or a collada file for a skinned poseable object
	public String model;

	//The transform defines the back left bottom of the cubic approximation of the object in the room it is within
	//If objects are in multiple rooms simultaniously then each is defined here
	public double[] within_metric_transform = new double[]{};	
	//The approximate 3d spatial dimensions of the object
	public double[] approximation_cuboid = null;
	
	//These are 3d matrix transforms stored in 12 doubles 3x3 for rotation and 3 for translation
	public double[] parts_metric_transform = new double[]{};
	//These are an approximate cuboid volume, the volume extends from the top, left, back defined by the metric transform
	public double[] parts_approximation_cuboid = new double[]{};	

	public void add(ObjectPhysicalInstance i,double x,double y,double z)
	{
		i.within_metric_transform = ArrayUtils.add(i.within_metric_transform,new double[]
				{
				1.0,0.0,0.0,
				0.0,1.0,0.0,
				0.0,0.0,1.0,

				x,y,z
				}
		);
	}

	public void remove(ObjectPhysicalInstance i,int ind)
	{
		i.within_metric_transform = ArrayUtils.remove(i.within_metric_transform,ind*12,12);
	}
}
