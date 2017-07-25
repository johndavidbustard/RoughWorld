package ai.knowledge_representation.state_instance;

import java.io.Serializable;

import utils.ArrayUtils;
import utils.GeneralMatrixDouble;
import utils.shapes.ParametricShape;

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
	public double[] contains_metric_transform = new double[]{};	
	//The approximate 3d spatial dimensions of the object
	public double[] approximation_cuboid = null;
	//A parametric shape that is more accurate than the approximation cuboid
	public ParametricShape shape = null;
	
	
	
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
		contains_metric_transform = ArrayUtils.add(contains_metric_transform,new double[]
				{
				1.0,0.0,0.0,
				0.0,1.0,0.0,
				0.0,0.0,1.0,

				x,y,z
				}
		);
	}
	
	public void add(ObjectPhysicalInstance i,double x,double y,double z,double zr)
	{
		GeneralMatrixDouble zrot = new GeneralMatrixDouble(4,4);
		zrot.set3DTransformRotation(0.0, 0.0, zr*Math.PI/180.0);
		i.within_metric_transform = ArrayUtils.add(i.within_metric_transform,new double[]
				{
				zrot.value[4*0+0],zrot.value[4*0+1],zrot.value[4*0+2],
				zrot.value[4*1+0],zrot.value[4*1+1],zrot.value[4*1+2],
				zrot.value[4*2+0],zrot.value[4*2+1],zrot.value[4*2+2],

				x,y,z
				}
		);
		contains_metric_transform = ArrayUtils.add(contains_metric_transform,new double[]
				{
				zrot.value[4*0+0],zrot.value[4*0+1],zrot.value[4*0+2],
				zrot.value[4*1+0],zrot.value[4*1+1],zrot.value[4*1+2],
				zrot.value[4*2+0],zrot.value[4*2+1],zrot.value[4*2+2],

				x,y,z
				}
		);
	}

	public void add(ObjectPhysicalInstance i,double x,double y,double z,
			double xr,double yr,double zr)
	{
		GeneralMatrixDouble zrot = new GeneralMatrixDouble(4,4);
		zrot.set3DTransformRotation(xr*Math.PI/180.0, yr*Math.PI/180.0, zr*Math.PI/180.0);
		i.within_metric_transform = ArrayUtils.add(i.within_metric_transform,new double[]
				{
				zrot.value[4*0+0],zrot.value[4*0+1],zrot.value[4*0+2],
				zrot.value[4*1+0],zrot.value[4*1+1],zrot.value[4*1+2],
				zrot.value[4*2+0],zrot.value[4*2+1],zrot.value[4*2+2],

				x,y,z
				}
		);
		contains_metric_transform = ArrayUtils.add(contains_metric_transform,new double[]
				{
				zrot.value[4*0+0],zrot.value[4*0+1],zrot.value[4*0+2],
				zrot.value[4*1+0],zrot.value[4*1+1],zrot.value[4*1+2],
				zrot.value[4*2+0],zrot.value[4*2+1],zrot.value[4*2+2],

				x,y,z
				}
		);
	}

	public void remove(ObjectPhysicalInstance i,int wind,int cind)
	{
		i.within_metric_transform = ArrayUtils.remove(i.within_metric_transform,wind*12,12);
		contains_metric_transform = ArrayUtils.remove(contains_metric_transform,cind*12,12);
	}
}
