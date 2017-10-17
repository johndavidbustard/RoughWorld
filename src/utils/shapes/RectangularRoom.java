package utils.shapes;

import utils.GeneralMatrixString;

public class RectangularRoom extends ParametricShape
{
	private static final long serialVersionUID = 1L;

	public static final String[] parameternames = {"width","height","ceiling_height"};
	public static final String[] sidenames = {"north","south","west","east","floor","ceiling"};
	public String[] getParameterNames() { return parameternames; }
	public String[] getSideNames() { return sidenames; }

	public void getShapePaths(String prefix,GeneralMatrixString paths)
	{
		paths.push_back(prefix+"/RectangularRoom");
	}

	public RectangularRoom()
	{
		parameters = new double[]{4.5,3.5,2.4};
	}
	
	public ParametricShape copy()
	{
		RectangularRoom c = new RectangularRoom();
		System.arraycopy(parameters, 0, c.parameters, 0, parameters.length);
		return c;
	}

	public void setCuboidDimensions(double x,double y,double z)
	{
		parameters[0] = x;
		parameters[1] = y;
		parameters[2] = z;
	}
	public void getCuboidDimensions(double[] xyz)
	{
		xyz[0] = parameters[0];
		xyz[1] = parameters[1];
		xyz[2] = parameters[2];
	}

}
