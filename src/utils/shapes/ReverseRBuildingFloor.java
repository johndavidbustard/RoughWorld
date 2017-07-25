package utils.shapes;

public class ReverseRBuildingFloor extends ParametricShape
{
	private static final long serialVersionUID = 1L;

	public static final String[] parameternames = {"top_width","top_height","bottom_width","bottom_height","ceiling_height"};
	public static final String[] sidenames = {"north","south1","south2","west1","west2","east","floor","ceiling"};
	public String[] getParameterNames() { return parameternames; }
	public String[] getSideNames() { return sidenames; }

	public ReverseRBuildingFloor()
	{
		parameters = new double[]{4.5,3.3,3.0,3.5,2.4};
	}
	
	public ParametricShape copy()
	{
		ReverseRBuildingFloor c = new ReverseRBuildingFloor();
		System.arraycopy(parameters, 0, c.parameters, 0, parameters.length);
		return c;
	}

	public void setCuboidDimensions(double x,double y,double z)
	{
		double hratio1 = parameters[1]/(parameters[1]+parameters[3]);
		double hratio2 = parameters[3]/(parameters[1]+parameters[3]);
		double wratio2 = parameters[2]/parameters[0];
		parameters[0] = x;
		parameters[1] = y*hratio1;
		parameters[2] = x*wratio2;
		parameters[3] = y*hratio2;
		parameters[4] = z;
	}
	public void getCuboidDimensions(double[] xyz)
	{
		xyz[0] = parameters[0];
		xyz[1] = parameters[1]+parameters[3];
		xyz[2] = parameters[2];
	}

}
