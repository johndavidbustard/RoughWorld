package utils.shapes;

public class WireShape extends ParametricShape implements Deformer
{ 
	private static final long serialVersionUID = 1L;

	public String[] parameternames = {"0_x","0_y","0_z","0_r"};
	public static final String[] sidenames = {"start","end","left","right","down","up"};
	public String[] getParameterNames() { return parameternames; }
	public String[] getSideNames() { return sidenames; }

	//optional shape can repeat along wire length e.g. spring, thread, shower segments
	public boolean deformSegment = true;
	public ParametricShape wireSegment = null; 
	public double[] precomputedWireLengths = new double[]{};
	
	public WireShape()
	{
		parameters = new double[]{0.0,0.0,0.0,0.0};
	}
	
	public void deform(double[] vs,int off,int len,double[] dvs,int doff,int dlen)
	{
		//the z of the shape determines the position along the wire
		
		//use the points to determine the wire shape
		
	}

	public ParametricShape copy()
	{
		WireShape c = new WireShape();
		c.parameters = new double[parameters.length];
		System.arraycopy(parameters, 0, c.parameters, 0, parameters.length);
		c.buildParameterNames();
		return c;
	}
	
	public void buildParameterNames()
	{
		parameternames = new String[parameters.length];
		for(int i=0;i<parameters.length/4;i++)
		{
			parameternames[i*4+0] = ""+i+"_x";
			parameternames[i*4+1] = ""+i+"_y";
			parameternames[i*4+2] = ""+i+"_z";
			parameternames[i*4+3] = ""+i+"_r";
		}
	}
	
	public void parametersUpdated()
	{
		//calculate the line length of each part of the line
		
		//
	}	
	
	public void setCuboidDimensions(double x,double y,double z)
	{
		double[] xyz = new double[3];
		getCuboidDimensions(xyz);
		
		double sx = x/xyz[0];
		double sy = y/xyz[1];
		double sz = z/xyz[2];
		
		for(int i=0;i<parameters.length/4;i++)
		{
			parameters[i*4+0] *= sx;
			parameters[i*4+1] *= sy;
			parameters[i*4+2] *= sz;
		}
		
	}
	public void getCuboidDimensions(double[] xyz)
	{
		double minx = Double.MAX_VALUE;
		double miny = Double.MAX_VALUE;
		double minz = Double.MAX_VALUE;
		double maxx = -Double.MAX_VALUE;
		double maxy = -Double.MAX_VALUE;
		double maxz = -Double.MAX_VALUE;

		for(int i=0;i<parameters.length/4;i++)
		{
			double px = parameters[i*4+0];
			double py = parameters[i*4+1];
			double pz = parameters[i*4+2];
			if(px<minx)
				minx = px;
			if(py<miny)
				miny = py;
			if(pz<minz)
				minz = pz;
			if(px>maxx)
				maxx = px;
			if(py>maxy)
				maxy = py;
			if(pz>maxz)
				maxz = pz;
		}

		if(parameters.length==4)
		{
			xyz[0] = 1.0;
			xyz[1] = 1.0;
			xyz[2] = 1.0;
		}
		else
		{
			xyz[0] = maxx-minx;
			xyz[1] = maxy-miny;
			xyz[2] = maxz-minz;
		}
	}

}
