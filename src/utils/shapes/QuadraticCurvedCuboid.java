package utils.shapes;

public class QuadraticCurvedCuboid extends ParametricShape
{
	private static final long serialVersionUID = 1L;

	public static final String[] parameternames = 
		{
		//cuboid points
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
		
		//edge mid points
		"mid_bottom_back_x",
		"mid_bottom_back_y",
		"mid_bottom_back_z",
		"mid_top_back_x",
		"mid_top_back_y",
		"mid_top_back_z",
		"left_mid_back_x",
		"left_mid_back_y",
		"left_mid_back_z",
		"right_mid_back_x",
		"right_mid_back_y",
		"right_mid_back_z",

		"mid_bottom_front_x",
		"mid_bottom_front_y",
		"mid_bottom_front_z",
		"mid_top_front_x",
		"mid_top_front_y",
		"mid_top_front_z",
		"left_mid_front_x",
		"left_mid_front_y",
		"left_mid_front_z",
		"right_mid_front_x",
		"right_mid_front_y",
		"right_mid_front_z",

		"left_bottom_mid_x",
		"left_bottom_mid_y",
		"left_bottom_mid_z",
		"right_bottom_mid_x",
		"right_bottom_mid_y",
		"right_bottom_mid_z",
		"left_top_mid_x",
		"left_top_mid_y",
		"left_top_mid_z",
		"right_top_mid_x",
		"right_top_mid_y",
		"right_top_mid_z",

		//face mid points
		"left_mid_mid_x",
		"left_mid_mid_y",
		"left_mid_mid_z",
		"right_mid_mid_x",
		"right_mid_mid_y",
		"right_mid_mid_z",
		"mid_bottom_mid_x",
		"mid_bottom_mid_y",
		"mid_bottom_mid_z",
		"mid_top_mid_x",
		"mid_top_mid_y",
		"mid_top_mid_z",
		"mid_mid_back_x",
		"mid_mid_back_y",
		"mid_mid_back_z",
		"mid_mid_front_x",
		"mid_mid_front_y",
		"mid_mid_front_z",
		};
	public static final String[] sidenames = {"left","right","back","front","bottom","top"};
	public String[] getParameterNames() { return parameternames; }
	public String[] getSideNames() { return sidenames; }

	public QuadraticCurvedCuboid()
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
				
				//edge mid points
//				"mid_bottom_back",
//				"mid_top_back",
//				"left_mid_back",
//				"right_mid_back",
				0.5,0.0,0.0,
				0.5,1.0,0.0,
				0.0,0.5,0.0,
				1.0,0.5,0.0,
//
//				"mid_bottom_front",
//				"mid_top_front",
//				"left_mid_front",
//				"right_mid_front",
				0.5,0.0,1.0,
				0.5,1.0,1.0,
				0.0,0.5,1.0,
				1.0,0.5,1.0,
//
//				"left_bottom_mid",
//				"right_bottom_mid",
//				"left_top_mid",
//				"right_top_mid",
				0.0,0.0,0.5,
				1.0,0.0,0.5,
				0.0,1.0,0.5,
				1.0,1.0,0.5,
				
//				"left_mid_mid",
//				"right_mid_mid",
//				"mid_bottom_mid",
//				"mid_top_mid",
//				"mid_mid_back",
//				"mid_mid_front",
				0.0,0.5,0.5,
				1.0,0.5,0.5,
				0.5,0.0,0.5,
				0.5,1.0,0.5,
				0.5,0.5,0.0,
				0.5,0.5,1.0,
			};
	}
	
	
	public ParametricShape copy()
	{
		QuadraticCurvedCuboid c = new QuadraticCurvedCuboid();
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
		
		for(int vi=0;vi<26;vi++)
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
		
		for(int vi=0;vi<26;vi++)
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
		
		for(int vi=0;vi<26;vi++)
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