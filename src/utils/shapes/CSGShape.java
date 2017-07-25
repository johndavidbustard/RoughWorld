package utils.shapes;

import utils.ArrayUtils;
import utils.GeneralMatrixDouble;

public class CSGShape extends ParametricShape
{
 	private static final long serialVersionUID = 1L;

 	public String[] parameternames = new String[]{};
 	public String[] sidenames = new String[]{};
 	public String[] getParameterNames() { return parameternames; }
 	public String[] getSideNames() { return sidenames; }

	public static final int ADD = 0;
	public static final int SUBTRACT = 1;
	
	public String[] shapeNames = new String[]{};
	public double[] shapeTransforms = new double[]{};
	public ParametricShape[] shapes = new ParametricShape[]{};
	public int[] operations = new int[]{};
	
 	public CSGShape()
 	{
 	}
 	
	public ParametricShape copy()
	{
		CSGShape c = new CSGShape();
		c.shapeNames = new String[shapeNames.length];
		System.arraycopy(shapeNames, 0, c.shapeNames, 0, shapeNames.length);		
		c.shapeTransforms = new double[shapeTransforms.length];
		System.arraycopy(shapeTransforms, 0, c.shapeTransforms, 0, shapeTransforms.length);
		c.operations = new int[operations.length];
		System.arraycopy(operations, 0, c.operations, 0, operations.length);

		c.shapes = new ParametricShape[shapes.length];
		for(int i=0;i<shapes.length;i++)
		{
			c.shapes[i] = shapes[i].copy();
		}
		c.buildParametersAndSides();
		return c;
	}

	public void parametersUpdated() 
	{
		int pind = 0;
		for(int si=0;si<shapes.length;si++)
		{
			for(int pi=0;pi<shapes[si].parameters.length;pi++)
			{
				shapes[si].parameters[pi] = parameters[pind];
				pind++;
			}			
			shapes[si].parametersUpdated();
			for(int ti=0;ti<6;ti++)
			{
				shapeTransforms[si*6+ti] = parameters[pind];
				pind++;
			}
		}
	}

	public static ParametricShape[] remove(ParametricShape[] v, int ind,int len)
	{
		ParametricShape[] nv = new ParametricShape[v.length-len];
		if(ind!=0)
			System.arraycopy(v, 0, nv, 0, ind);
		if((v.length-(ind+len))>=0)
			System.arraycopy(v, ind+len, nv, ind, v.length-(ind+len));
		return nv;
	}

	public void removeSubShape(int ind)
	{
		shapeNames = ArrayUtils.remove(shapeNames, ind, 1);
		shapeTransforms = ArrayUtils.remove(shapeTransforms, ind*6, 6);
		shapes = remove(shapes, ind, 1);
		operations = ArrayUtils.remove(operations, ind, 1);

		buildParametersAndSides();
	}
	
	public void buildParametersAndSides()
	{
		int numps = 0;
		int numss = 0;
		for(int si=0;si<shapes.length;si++)
		{
			String[] pn = shapes[si].getParameterNames();
			String[] sn = shapes[si].getSideNames();
			numps += pn.length+6;
			numss += sn.length;
		}
		
		double[] np = new double[numps];
		String[] npn = new String[numps];
		String[] nsn = new String[numss];
		int npnind = 0;
		int nsnind = 0;
		for(int si=0;si<shapes.length;si++)
		{
			String[] pn = shapes[si].getParameterNames();
			String[] sn = shapes[si].getSideNames();
			for(int pni=0;pni<pn.length;pni++)
			{
				npn[npnind] = shapeNames[si]+"_"+pn[pni];
				np[npnind] = shapes[si].parameters[pni];
				npnind++;
			}
			npn[npnind] = shapeNames[si]+"_X"; 
			np[npnind] = shapeTransforms[si*6+0];
			npnind++;
			npn[npnind] = shapeNames[si]+"_Y"; 
			np[npnind] = shapeTransforms[si*6+1];
			npnind++;
			npn[npnind] = shapeNames[si]+"_Z"; 
			np[npnind] = shapeTransforms[si*6+2];
			npnind++;
			npn[npnind] = shapeNames[si]+"_Xr"; 
			np[npnind] = shapeTransforms[si*6+3];
			npnind++;
			npn[npnind] = shapeNames[si]+"_Yr"; 
			np[npnind] = shapeTransforms[si*6+4];
			npnind++;
			npn[npnind] = shapeNames[si]+"_Zr"; 
			np[npnind] = shapeTransforms[si*6+5];
			npnind++;
			for(int sni=0;sni<sn.length;sni++)
			{
				nsn[nsnind] = shapeNames[si]+"_"+sn[sni];
				nsnind++;
			}
		}
	
		parameters = np;
		parameternames = npn;
		sidenames = nsn;
	}
	
 	public void addShape(ParametricShape s,String name,int operation,
 			double x,double y,double z,
 			double xr,double yr,double zr)
 	{
 		if(shapeTransforms==null)
 		{
 			shapeNames = new String[]{};
 			shapeTransforms = new double[]{};
 			shapes = new ParametricShape[]{};
 			operations = new int[]{};
 		}
// 		GeneralMatrixDouble transform = new GeneralMatrixDouble(4,4);
// 		transform.setIdentity();
// 		transform.set3DTransformPosition(x, y, z);
// 		transform.set3DTransformRotation(xr, yr, zr);
 		{
			double[] nv = new double[shapeTransforms.length+6];
			System.arraycopy(shapeTransforms, 0, nv, 0, shapeTransforms.length);

			nv[shapeTransforms.length+0] = x;
			nv[shapeTransforms.length+1] = y;
			nv[shapeTransforms.length+2] = z;
			nv[shapeTransforms.length+3] = xr;
			nv[shapeTransforms.length+4] = yr;
			nv[shapeTransforms.length+5] = zr;
			
//			if(shapeTransforms.length>0)
//				System.arraycopy(shapeTransforms, 0, nv, 0, shapeTransforms.length);
//			System.arraycopy(transform.value, 4*0, nv, 
//					shapeTransforms.length+3*0, 3);
//			System.arraycopy(transform.value, 4*1, nv, 
//					shapeTransforms.length+3*1, 3);
//			System.arraycopy(transform.value, 4*2, nv, 
//					shapeTransforms.length+3*2, 3);
//			System.arraycopy(transform.value, 4*3, nv, 
//					shapeTransforms.length+3*3, 3);
			shapeTransforms = nv;
 		}
 		
 		shapeNames = ArrayUtils.add(shapeNames, name);

		{
			ParametricShape[] nv = new ParametricShape[shapes.length+1];
			System.arraycopy(shapes, 0, nv, 0, shapes.length);
			nv[shapes.length] = s;
			shapes = nv;
		}		
		operations = ArrayUtils.add(operations,operation);
		
		buildParametersAndSides();
 	}
 	
 	public void setCuboidDimensions(double x,double y,double z)
 	{
// 		parameters[0] = x;
// 		parameters[1] = y;
// 		parameters[2] = z;
 	}
 	public void getCuboidDimensions(double[] xyz)
 	{
 		if(shapes.length>0)
 		{
 			//Calc the cuboid of each shape
 			//for each calc the verts and transform with the shape transform
 			//determine the max and min in each dimension
 			double maxx = 0.0;
 			double maxy = 0.0;
 			double maxz = 0.0;
			final double DEG2RAD = Math.PI/180.0;
 			double[] sdim = new double[3];
 			for(int si=0;si<shapes.length;si++)
 			{
 				GeneralMatrixDouble transform = new GeneralMatrixDouble(4,4);
 				transform.setIdentity();
 				transform.set3DTransformPosition(shapeTransforms[si*6+0], shapeTransforms[si*6+1], shapeTransforms[si*6+2]);
 				transform.set3DTransformRotation(shapeTransforms[si*6+3]*DEG2RAD, shapeTransforms[si*6+4]*DEG2RAD, shapeTransforms[si*6+5]*DEG2RAD);
 				
 				shapes[si].getCuboidDimensions(sdim);
 				
 				double[] localverts = new double[4*8];
 				
 				double w = sdim[0];
 				double h = sdim[1];
 				double d = sdim[2];
 				
 				localverts[0*4+0] = 0*w; localverts[0*4+1] = 0*h; localverts[0*4+2] = 0*d; localverts[0*4+3] = 1.0;
 				localverts[1*4+0] = 1*w; localverts[1*4+1] = 0*h; localverts[1*4+2] = 0*d; localverts[1*4+3] = 1.0;
 				localverts[2*4+0] = 0*w; localverts[2*4+1] = 1*h; localverts[2*4+2] = 0*d; localverts[2*4+3] = 1.0;
 				localverts[3*4+0] = 1*w; localverts[3*4+1] = 1*h; localverts[3*4+2] = 0*d; localverts[3*4+3] = 1.0;

 				localverts[4*4+0*4+0] = 0*w; localverts[4*4+0*4+1] = 0*h; localverts[4*4+0*4+2] = 1*d; localverts[4*4+0*4+3] = 1.0;
 				localverts[4*4+1*4+0] = 1*w; localverts[4*4+1*4+1] = 0*h; localverts[4*4+1*4+2] = 1*d; localverts[4*4+1*4+3] = 1.0;
 				localverts[4*4+2*4+0] = 0*w; localverts[4*4+2*4+1] = 1*h; localverts[4*4+2*4+2] = 1*d; localverts[4*4+2*4+3] = 1.0;
 				localverts[4*4+3*4+0] = 1*w; localverts[4*4+3*4+1] = 1*h; localverts[4*4+3*4+2] = 1*d; localverts[4*4+3*4+3] = 1.0;

 				for(int vi=0;vi<localverts.length/4;vi++)
 				{
 					double px = localverts[vi*4+0];
 					double py = localverts[vi*4+1];
 					double pz = localverts[vi*4+2];
 					double ph = localverts[vi*4+3];
 			    	double transx = (px*transform.value[4*0+0])+(py*transform.value[4*1+0])+(pz*transform.value[4*2+0])+(ph*transform.value[4*3+0]);
 			    	double transy = (px*transform.value[4*0+1])+(py*transform.value[4*1+1])+(pz*transform.value[4*2+1])+(ph*transform.value[4*3+1]);
 			    	double transz = (px*transform.value[4*0+2])+(py*transform.value[4*1+2])+(pz*transform.value[4*2+2])+(ph*transform.value[4*3+2]);
 			    	double transh = (px*transform.value[4*0+3])+(py*transform.value[4*1+3])+(pz*transform.value[4*2+3])+(ph*transform.value[4*3+3]);
// 			    	worldverts[vi*4+0] = transx;
// 			    	worldverts[vi*4+1] = transy;
// 			    	worldverts[vi*4+2] = transz;
// 			    	worldverts[vi*4+3] = transh;

 			    	if(transx>maxx)
 			    	{
 			    		maxx = transx;
 			    	}
 			    	if(transy>maxy)
 			    	{
 			    		maxy = transy;
 			    	}
 			    	if(transz>maxz)
 			    	{
 			    		maxz = transz;
 			    	}
 				}
 				
 			}
 			xyz[0] = maxx;
 			xyz[1] = maxy;
 			xyz[2] = maxz;
 		}
 		else
 		{
	 		xyz[0] = 1.0;
	 		xyz[1] = 1.0;
	 		xyz[2] = 1.0;
 		}
 	}
}
