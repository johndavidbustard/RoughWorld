package utils.shapes;

public class ModifiedCopyShape extends ParametricShape
{
 	private static final long serialVersionUID = 1L;

 	public String copyPath = null;
 	public ParametricShape baseCopy = null;
 	public ParametricShape modifiedCopy = null;

 	public String[] parameternames = new String[]{};
 	public String[] sidenames = new String[]{};
 	public String[] getParameterNames() { return parameternames; }
 	public String[] getSideNames() { return sidenames; }

 	public ModifiedCopyShape()
 	{
 		
 	}
 	
	public ParametricShape copy()
	{
		ModifiedCopyShape c = new ModifiedCopyShape();
		c.copyPath = ""+copyPath;
		c.baseCopy = baseCopy.copy();
		c.baseUpdated();
		System.arraycopy(parameters, 0, c.parameters, 0, parameters.length);
		c.parametersUpdated();
		return c;
	}

 	public void baseUpdated()
 	{
 		parameters = new double[baseCopy.parameters.length]; 		
 		modifiedCopy = baseCopy.copy();
 		buildParametersAndSides();
 	}
 	
	public void parametersUpdated() 
	{
		int pind = 0;
		for(int pi=0;pi<modifiedCopy.parameters.length;pi++)
		{
			modifiedCopy.parameters[pi] = baseCopy.parameters[pi]+parameters[pind];
			pind++;
		}			
		modifiedCopy.parametersUpdated();
	}
	
	public void buildParametersAndSides()
	{
		int numps = 0;
		int numss = 0;

		{
			String[] pn = modifiedCopy.getParameterNames();
			String[] sn = modifiedCopy.getSideNames();
			numps += pn.length;
			numss += sn.length;
		}
		
		double[] np = new double[numps];
		String[] npn = new String[numps];
		String[] nsn = new String[numss];
		int npnind = 0;
		int nsnind = 0;
		{
			String[] pn = modifiedCopy.getParameterNames();
			String[] sn = modifiedCopy.getSideNames();
			for(int pni=0;pni<pn.length;pni++)
			{
				npn[npnind] = "d_"+pn[pni];
				np[npnind] = modifiedCopy.parameters[pni];
				npnind++;
			}
			for(int sni=0;sni<sn.length;sni++)
			{
				nsn[nsnind] = modifiedCopy+"_"+sn[sni];
				nsnind++;
			}
		}
	
		parameters = np;
		parameternames = npn;
		sidenames = nsn;
	}
	
 	public void setCuboidDimensions(double x,double y,double z)
 	{
 		//modifiedCopy.setCuboidDimensions(x, y, z);
 		
 		//update parameters
 	}
 	public void getCuboidDimensions(double[] xyz)
 	{
 		modifiedCopy.getCuboidDimensions(xyz);
 	}
}