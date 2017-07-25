package utils.shapes;

public class DeformedCuboid extends ParametricShape
{
	private static final long serialVersionUID = 1L;

	public static final String[] parameternames = 
		{
		"left_bottom_back_x",
		"left_bottom_back_y",
		"left_bottom_back_z",
		"right_bottom_back_x",
		"right_bottom_back_y",
		"right_bottom_back_z",
		"left_top_back_x",
		"left_top_back_y",
		"left_top_back_z",
		"right_top_back_x",
		"right_top_back_y",
		"right_top_back_z",
		"left_bottom_front_x",
		"left_bottom_front_y",
		"left_bottom_front_z",
		"right_bottom_front_x",
		"right_bottom_front_y",
		"right_bottom_front_z",
		"left_top_front_x",
		"left_top_front_y",
		"left_top_front_z",
		"right_top_front_x",
		"right_top_front_y",
		"right_top_front_z",
		};
	public static final String[] sidenames = {"left","right","back","front","bottom","top"};
	public String[] getParameterNames() { return parameternames; }
	public String[] getSideNames() { return sidenames; }

	public DeformedCuboid()
	{
		parameters = new double[]
				{
				0.0,0.0,0.0,
				1.0,0.0,0.0,
				0.0,1.0,0.0,
				1.0,1.0,0.0,

				0.0,0.0,1.0,
				1.0,0.0,1.0,
				0.0,1.0,1.0,
				1.0,1.0,1.0,
				};
	}
	
	public ParametricShape copy()
	{
		DeformedCuboid c = new DeformedCuboid();
		System.arraycopy(parameters, 0, c.parameters, 0, parameters.length);
		return c;
	}

	public void setCuboidDimensions(double x,double y,double z)
	{
		double minx = Float.MAX_VALUE;
		double miny = Float.MAX_VALUE;
		double minz = Float.MAX_VALUE;
		double maxx = -Float.MAX_VALUE;
		double maxy = -Float.MAX_VALUE;
		double maxz = -Float.MAX_VALUE;
		
		for(int vi=0;vi<8;vi++)
		{
			double vx = parameters[vi*3+0];
			double vy = parameters[vi*3+1];
			double vz = parameters[vi*3+2];
			
			if(vx<minx)
				minx = vx;
			if(vy<miny)
				miny = vy;
			if(vz<minz)
				minz = vz;
			
			if(vx>maxx)
				maxx = vx;
			if(vy>maxy)
				maxy = vy;
			if(vz>maxz)
				maxz = vz;
		}
		
		double dx = maxx-minx;
		double dy = maxy-miny;
		double dz = maxz-minz;
		
		double sx = x/dx;
		double sy = y/dy;
		double sz = z/dz;
		
		for(int vi=0;vi<8;vi++)
		{
			parameters[vi*3+0] *= sx;
			parameters[vi*3+1] *= sy;
			parameters[vi*3+2] *= sz;
		}
	}
	
	public void getCuboidDimensions(double[] xyz)
	{
		double minx = Float.MAX_VALUE;
		double miny = Float.MAX_VALUE;
		double minz = Float.MAX_VALUE;
		double maxx = -Float.MAX_VALUE;
		double maxy = -Float.MAX_VALUE;
		double maxz = -Float.MAX_VALUE;
		
		for(int vi=0;vi<8;vi++)
		{
			double vx = parameters[vi*3+0];
			double vy = parameters[vi*3+1];
			double vz = parameters[vi*3+2];
			
			if(vx<minx)
				minx = vx;
			if(vy<miny)
				miny = vy;
			if(vz<minz)
				minz = vz;
			
			if(vx>maxx)
				maxx = vx;
			if(vy>maxy)
				maxy = vy;
			if(vz>maxz)
				maxz = vz;
		}
		
		xyz[0] = maxx-minx;
		xyz[1] = maxy-miny;
		xyz[2] = maxz-minz;
	}
}