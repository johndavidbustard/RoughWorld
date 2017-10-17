package utils.shapes;

public class UniformlySkinnedShape 
{
	//Set of transforms at regular points within a shape
	//to smoothly deform it
	//useful for cloth, or soft objects like pillows

	double distanceBetweenSamples = 0.1;
	ParametricShape shapeToDeform = null;

	int[] dimensions = new int[]{1,1,1};
	
	public void baseShapeChanged()
	{
		
	}
	
	public void calcTransform(double x,double y,double z,double[] transform)
	{
		//a quadratic bezier volume constructed from the control points
		//this determines the new location of the points after the deformation
	}
}
