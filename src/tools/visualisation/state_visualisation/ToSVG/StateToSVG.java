package tools.visualisation.state_visualisation.ToSVG;

import java.io.PrintWriter;

import utils.FileStoreInterface;
import utils.GeneralMatrixDouble;
import utils.shapes.CSGShape;
import utils.shapes.Cuboid;
import utils.shapes.DeformedCuboid;
import utils.shapes.ModifiedCopyShape;
import utils.shapes.ParametricShape;
import utils.shapes.QuadraticCurvedCuboid;
import utils.shapes.RRoom;
import utils.shapes.RectangularBuildingFloor;
import utils.shapes.RectangularPortal;
import utils.shapes.RectangularRoom;
import utils.shapes.ReverseRBuildingFloor;
import ai.knowledge_representation.state_instance.ObjectLanguageInstance;

public class StateToSVG 
{
	//Take a state description of a room or object etc. and create an SVG image representing it
	
	//Same for sensed and inferred states (add visualisation for uncertainty)

	public static void addShape3D(ParametricShape shape,double x,double y,double z,double scale,int[] origins,PrintWriter pw)
	{
		GeneralMatrixDouble transform = new GeneralMatrixDouble(4,4);
		transform.setIdentity();
		
		addWireframeShape(shape, transform, origins, scale, true, true, pw);

		
//		if(shape instanceof RectangularPortal)
//		{
//			RectangularPortal rbuild = (RectangularPortal)shape;
//			double w = rbuild.parameters[0];
//			double h = rbuild.parameters[1];
//			double d = rbuild.parameters[2];
//
//			double xoff = origins[0];
//			double yoff = origins[1];
//			double xi = x;
//			double yi = y;
//			int wi = (int)(w*scale);
//			int hi = (int)(h*scale);
//			yi += hi;
//			//plan view
//			pw.append("  <polygon points=\""+
//					(xi+xoff)+","+(yi+yoff-hi)+" "       +(xi+xoff+wi)+","+(yi+yoff-hi)+" "+
//					(xi+xoff+wi)+","+(yi+yoff)+" "       +(xi+xoff)+","+(yi+yoff)+" "+
//					"\" style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\""+
//					"/>");
//
//			xoff = origins[2];
//			yoff = origins[3];
//			xi = x;
//			yi = z;
//			wi = (int)(w*scale);
//			hi = (int)(d*scale);
//			//front view
//			pw.append("  <polygon points=\""+
//					(xi+xoff)+","+(yi+yoff-hi)+" "       +(xi+xoff+wi)+","+(yi+yoff-hi)+" "+
//					(xi+xoff+wi)+","+(yi+yoff)+" "       +(xi+xoff)+","+(yi+yoff)+" "+
//					"\" style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\""+
//					"/>");
//
//			xoff = origins[4];
//			yoff = origins[5];
//			xi = y;
//			yi = z;
//			wi = (int)(h*scale);
//			hi = (int)(d*scale);
//			//side view
//			pw.append("  <polygon points=\""+
//					(xi+xoff)+","+(yi+yoff-hi)+" "       +(xi+xoff+wi)+","+(yi+yoff-hi)+" "+
//					(xi+xoff+wi)+","+(yi+yoff)+" "       +(xi+xoff)+","+(yi+yoff)+" "+
//					"\" style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\""+
//					"/>");
//
//		}
//		else
//		if(shape instanceof Cuboid)
//		{
//			Cuboid rbuild = (Cuboid)shape;
//			double w = rbuild.parameters[0];
//			double h = rbuild.parameters[1];
//			double d = rbuild.parameters[2];
//			
//
//			double xoff = origins[0];
//			double yoff = origins[1];
//			double xi = x;
//			double yi = y;
//			int wi = (int)(w*scale);
//			int hi = (int)(h*scale);
//			yi += hi;
//
//			//plan view
//			pw.append("  <polygon points=\""+
//					(xi+xoff)+","+(yi+yoff-hi)+" "       +(xi+xoff+wi)+","+(yi+yoff-hi)+" "+
//					(xi+xoff+wi)+","+(yi+yoff)+" "       +(xi+xoff)+","+(yi+yoff)+" "+
//					"\" style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\""+
//					"/>");
//
//			xoff = origins[2];
//			yoff = origins[3];
//			xi = x;
//			yi = z;
//			wi = (int)(w*scale);
//			hi = (int)(d*scale);
//			//front view
//			pw.append("  <polygon points=\""+
//					(xi+xoff)+","+(yi+yoff-hi)+" "       +(xi+xoff+wi)+","+(yi+yoff-hi)+" "+
//					(xi+xoff+wi)+","+(yi+yoff)+" "       +(xi+xoff)+","+(yi+yoff)+" "+
//					"\" style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\""+
//					"/>");
//
//			xoff = origins[4];
//			yoff = origins[5];
//			xi = y;
//			yi = z;
//			wi = (int)(h*scale);
//			hi = (int)(d*scale);
//			//side view
//			pw.append("  <polygon points=\""+
//					(xi+xoff)+","+(yi+yoff-hi)+" "       +(xi+xoff+wi)+","+(yi+yoff-hi)+" "+
//					(xi+xoff+wi)+","+(yi+yoff)+" "       +(xi+xoff)+","+(yi+yoff)+" "+
//					"\" style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\""+
//					"/>");
//		}

	}
	
	static final int[] cubeEdges = new int[]
	{
		0,1,
		1,3,
		3,2,
		2,0,

		4,5,
		5,7,
		7,6,
		6,4,

		0,4,
		1,5,
		2,6,
		3,7,
	};

//	//edge mid points
//0	"mid_bottom_back",
//1	"mid_top_back",
//2	"left_mid_back",
//3	"right_mid_back",
//
//4	"mid_bottom_front",
//5	"mid_top_front",
//6	"left_mid_front",
//7	"right_mid_front",
//
//8	"left_bottom_mid",
//9	"right_bottom_mid",
//10	"left_top_mid",
//11	"right_top_mid",

	static final int[] quadraticCubeEdgeMidVert = new int[]
	{
//		0,1,
		8+0,
//		1,3,
		8+3,
//		3,2,
		8+1,
//		2,0,
		8+2,
		
//		4,5,
		8+4,
//		5,7,
		8+7,
//		7,6,
		8+5,
//		6,4,
		8+6,

//		0,4,
		8+8,
//		1,5,
		8+9,
//		2,6,
		8+10,
//		3,7,
		8+11,
	};
	
	static final int[] tEdges = new int[]
	{
		0,1,
		1,3,
		3,5,
		5,7,
		7,6,
		6,4,
		4,2,
		2,0,
		
		0+8,1+8,
		1+8,3+8,
		3+8,5+8,
		5+8,7+8,
		7+8,6+8,
		6+8,4+8,
		4+8,2+8,
		2+8,0+8,
		
		0,0+8,
		1,1+8,
		2,2+8,
		3,3+8,
		4,4+8,
		5,5+8,
		6,6+8,
		7,7+8,
	};

	
	public static void addWireframeT(GeneralMatrixDouble transform,int[] origin,double scale,boolean asLocation, 
			double x1,double w1,double h1,
			double x2,double w2,double h2,
			double d,
			boolean as3D,
			PrintWriter pw)
	{
		double[] localverts = new double[4*16];
		double[] worldverts = new double[4*16];
		
		localverts[0*4+0] = x1+0*w1; localverts[0*4+1] = 0*h1; localverts[0*4+2] = 0*d; localverts[0*4+3] = 1.0;
		localverts[1*4+0] = x1+1*w1; localverts[1*4+1] = 0*h1; localverts[1*4+2] = 0*d; localverts[1*4+3] = 1.0;
		localverts[2*4+0] = x1+0*w1; localverts[2*4+1] = 1*h1; localverts[2*4+2] = 0*d; localverts[2*4+3] = 1.0;
		localverts[3*4+0] = x1+1*w1; localverts[3*4+1] = 1*h1; localverts[3*4+2] = 0*d; localverts[3*4+3] = 1.0;

		localverts[4*4+0*4+0] = x2+0*w2; localverts[4*4+0*4+1] = h1+0*h2; localverts[4*4+0*4+2] = 0*d; localverts[4*4+0*4+3] = 1.0;
		localverts[4*4+1*4+0] = x2+1*w2; localverts[4*4+1*4+1] = h1+0*h2; localverts[4*4+1*4+2] = 0*d; localverts[4*4+1*4+3] = 1.0;
		localverts[4*4+2*4+0] = x2+0*w2; localverts[4*4+2*4+1] = h1+1*h2; localverts[4*4+2*4+2] = 0*d; localverts[4*4+2*4+3] = 1.0;
		localverts[4*4+3*4+0] = x2+1*w2; localverts[4*4+3*4+1] = h1+1*h2; localverts[4*4+3*4+2] = 0*d; localverts[4*4+3*4+3] = 1.0;

		localverts[8*4+0*4+0] = x1+0*w1; localverts[8*4+0*4+1] = 0*h1; localverts[8*4+0*4+2] = 1*d; localverts[8*4+0*4+3] = 1.0;
		localverts[8*4+1*4+0] = x1+1*w1; localverts[8*4+1*4+1] = 0*h1; localverts[8*4+1*4+2] = 1*d; localverts[8*4+1*4+3] = 1.0;
		localverts[8*4+2*4+0] = x1+0*w1; localverts[8*4+2*4+1] = 1*h1; localverts[8*4+2*4+2] = 1*d; localverts[8*4+2*4+3] = 1.0;
		localverts[8*4+3*4+0] = x1+1*w1; localverts[8*4+3*4+1] = 1*h1; localverts[8*4+3*4+2] = 1*d; localverts[8*4+3*4+3] = 1.0;

		localverts[12*4+0*4+0] = x2+0*w2; localverts[12*4+0*4+1] = h1+0*h2; localverts[12*4+0*4+2] = 1*d; localverts[12*4+0*4+3] = 1.0;
		localverts[12*4+1*4+0] = x2+1*w2; localverts[12*4+1*4+1] = h1+0*h2; localverts[12*4+1*4+2] = 1*d; localverts[12*4+1*4+3] = 1.0;
		localverts[12*4+2*4+0] = x2+0*w2; localverts[12*4+2*4+1] = h1+1*h2; localverts[12*4+2*4+2] = 1*d; localverts[12*4+2*4+3] = 1.0;
		localverts[12*4+3*4+0] = x2+1*w2; localverts[12*4+3*4+1] = h1+1*h2; localverts[12*4+3*4+2] = 1*d; localverts[12*4+3*4+3] = 1.0;


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
	    	worldverts[vi*4+0] = transx;
	    	worldverts[vi*4+1] = transy;
	    	worldverts[vi*4+2] = transz;
	    	worldverts[vi*4+3] = transh;
		}
		
		for(int li=0;li<24;li++)
		{
			int i = tEdges[li*2+0];
			int j = tEdges[li*2+1];
			
			if(as3D)
			{
				{
					double x_1 = origin[0]+worldverts[i*4+0]*scale;
					double y1 = origin[1]+worldverts[i*4+1]*scale;
					double x_2 = origin[0]+worldverts[j*4+0]*scale;
					double y2 = origin[1]+worldverts[j*4+1]*scale;			
					pw.append("<line x1=\""+x_1+"\" y1=\""+y1+"\" x2=\""+x_2+"\" y2=\""+y2+"\" "
							+ "style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				}
				{
					double x_1 = origin[2]+worldverts[i*4+0]*scale;
					double y1 = origin[3]-worldverts[i*4+2]*scale;
					double x_2 = origin[2]+worldverts[j*4+0]*scale;
					double y2 = origin[3]-worldverts[j*4+2]*scale;			
					pw.append("<line x1=\""+x_1+"\" y1=\""+y1+"\" x2=\""+x_2+"\" y2=\""+y2+"\" "
							+ "style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				}
				{
					double x_1 = origin[4]+worldverts[i*4+1]*scale;
					double y1 = origin[5]-worldverts[i*4+2]*scale;
					double x_2 = origin[4]+worldverts[j*4+1]*scale;
					double y2 = origin[5]-worldverts[j*4+2]*scale;			
					pw.append("<line x1=\""+x_1+"\" y1=\""+y1+"\" x2=\""+x_2+"\" y2=\""+y2+"\" "
							+ "style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				}
			}
			else
			{
				double x_1 = origin[0]+worldverts[i*4+0]*scale;
				double y1 = origin[1]+worldverts[i*4+1]*scale;
				double x_2 = origin[0]+worldverts[j*4+0]*scale;
				double y2 = origin[1]+worldverts[j*4+1]*scale;			
				pw.append("<line x1=\""+x_1+"\" y1=\""+y1+"\" x2=\""+x_2+"\" y2=\""+y2+"\" "
						+ "style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
			}
		}
	}

	
	
	public static void addWireframeQuadraticCurvedCube(GeneralMatrixDouble transform,int[] origin,double scale,boolean asLocation, 
			double[] localverts,
			boolean as3D,
			PrintWriter pw)
	{
		double[] worldverts = new double[4*26];

		for(int vi=0;vi<26;vi++)
		{
			double px = localverts[vi*3+0];
			double py = localverts[vi*3+1];
			double pz = localverts[vi*3+2];
			double ph = 1.0;
	    	double transx = (px*transform.value[4*0+0])+(py*transform.value[4*1+0])+(pz*transform.value[4*2+0])+(ph*transform.value[4*3+0]);
	    	double transy = (px*transform.value[4*0+1])+(py*transform.value[4*1+1])+(pz*transform.value[4*2+1])+(ph*transform.value[4*3+1]);
	    	double transz = (px*transform.value[4*0+2])+(py*transform.value[4*1+2])+(pz*transform.value[4*2+2])+(ph*transform.value[4*3+2]);
	    	double transh = (px*transform.value[4*0+3])+(py*transform.value[4*1+3])+(pz*transform.value[4*2+3])+(ph*transform.value[4*3+3]);
	    	worldverts[vi*4+0] = transx;
	    	worldverts[vi*4+1] = transy;
	    	worldverts[vi*4+2] = transz;
	    	worldverts[vi*4+3] = transh;
		}
		
		for(int li=0;li<12;li++)
		{
			int i = cubeEdges[li*2+0];
			int j = cubeEdges[li*2+1];
			int k = quadraticCubeEdgeMidVert[li];
			if(as3D)
			{
				{
					double x1 = origin[0]+worldverts[i*4+0]*scale;
					double y1 = origin[1]+worldverts[i*4+1]*scale;
					double x2 = origin[0]+worldverts[j*4+0]*scale;
					double y2 = origin[1]+worldverts[j*4+1]*scale;
					double cx = origin[0]+worldverts[k*4+0]*scale;
					double cy = origin[1]+worldverts[k*4+1]*scale;
					
					//calculate the bezier control point so that the centre point is hit
					cx = 2.0*(cx-(0.25*x1+0.25*x2));
					cy = 2.0*(cy-(0.25*y1+0.25*y2));
					
					//					
					pw.append("<path d=\"M"+x1+" "+y1+" Q "+cx+" "+cy+", "+x2+" "+y2+"\" "
							+ "style=\"fill:transparent;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				}
				{
					double x1 = origin[2]+worldverts[i*4+0]*scale;
					double y1 = origin[3]-worldverts[i*4+2]*scale;
					double x2 = origin[2]+worldverts[j*4+0]*scale;
					double y2 = origin[3]-worldverts[j*4+2]*scale;			
					double cx = origin[2]+worldverts[k*4+0]*scale;
					double cy = origin[3]-worldverts[k*4+2]*scale;
					
					//calculate the bezier control point so that the centre point is hit
					cx = 2.0*(cx-(0.25*x1+0.25*x2));
					cy = 2.0*(cy-(0.25*y1+0.25*y2));
					
					pw.append("<path d=\"M"+x1+" "+y1+" Q "+cx+" "+cy+", "+x2+" "+y2+"\" "
							+ "style=\"fill:transparent;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				}
				{
					double x1 = origin[4]+worldverts[i*4+1]*scale;
					double y1 = origin[5]-worldverts[i*4+2]*scale;
					double x2 = origin[4]+worldverts[j*4+1]*scale;
					double y2 = origin[5]-worldverts[j*4+2]*scale;			
					double cx = origin[4]+worldverts[k*4+1]*scale;
					double cy = origin[5]-worldverts[k*4+2]*scale;
					
					//calculate the bezier control point so that the centre point is hit
					cx = 2.0*(cx-(0.25*x1+0.25*x2));
					cy = 2.0*(cy-(0.25*y1+0.25*y2));
					
					pw.append("<path d=\"M"+x1+" "+y1+" Q "+cx+" "+cy+", "+x2+" "+y2+"\" "
							+ "style=\"fill:transparent;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				}
			}
			else
			{
				double x1 = origin[0]+worldverts[i*4+0]*scale;
				double y1 = origin[1]+worldverts[i*4+1]*scale;
				double x2 = origin[0]+worldverts[j*4+0]*scale;
				double y2 = origin[1]+worldverts[j*4+1]*scale;			
				double cx = origin[0]+worldverts[k*4+0]*scale;
				double cy = origin[1]+worldverts[k*4+1]*scale;
				
				//calculate the bezier control point so that the centre point is hit
				cx = 2.0*(cx-(0.25*x1+0.25*x2));
				cy = 2.0*(cy-(0.25*y1+0.25*y2));
				
				pw.append("<path d=\"M"+x1+" "+y1+" Q "+cx+" "+cy+", "+x2+" "+y2+"\" "
						+ "style=\"fill:transparent;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				
			}
		}
	}
	public static void addWireframeDeformedCube(GeneralMatrixDouble transform,int[] origin,double scale,boolean asLocation, 
			double[] localverts,
			boolean as3D,
			PrintWriter pw)
	{
		double[] worldverts = new double[4*8];

		for(int vi=0;vi<8;vi++)
		{
			double px = localverts[vi*3+0];
			double py = localverts[vi*3+1];
			double pz = localverts[vi*3+2];
			double ph = 1.0;
	    	double transx = (px*transform.value[4*0+0])+(py*transform.value[4*1+0])+(pz*transform.value[4*2+0])+(ph*transform.value[4*3+0]);
	    	double transy = (px*transform.value[4*0+1])+(py*transform.value[4*1+1])+(pz*transform.value[4*2+1])+(ph*transform.value[4*3+1]);
	    	double transz = (px*transform.value[4*0+2])+(py*transform.value[4*1+2])+(pz*transform.value[4*2+2])+(ph*transform.value[4*3+2]);
	    	double transh = (px*transform.value[4*0+3])+(py*transform.value[4*1+3])+(pz*transform.value[4*2+3])+(ph*transform.value[4*3+3]);
	    	worldverts[vi*4+0] = transx;
	    	worldverts[vi*4+1] = transy;
	    	worldverts[vi*4+2] = transz;
	    	worldverts[vi*4+3] = transh;
		}
		
		for(int li=0;li<12;li++)
		{
			int i = cubeEdges[li*2+0];
			int j = cubeEdges[li*2+1];
			
			if(as3D)
			{
				{
					double x1 = origin[0]+worldverts[i*4+0]*scale;
					double y1 = origin[1]+worldverts[i*4+1]*scale;
					double x2 = origin[0]+worldverts[j*4+0]*scale;
					double y2 = origin[1]+worldverts[j*4+1]*scale;			
					pw.append("<line x1=\""+x1+"\" y1=\""+y1+"\" x2=\""+x2+"\" y2=\""+y2+"\" "
							+ "style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				}
				{
					double x1 = origin[2]+worldverts[i*4+0]*scale;
					double y1 = origin[3]-worldverts[i*4+2]*scale;
					double x2 = origin[2]+worldverts[j*4+0]*scale;
					double y2 = origin[3]-worldverts[j*4+2]*scale;			
					pw.append("<line x1=\""+x1+"\" y1=\""+y1+"\" x2=\""+x2+"\" y2=\""+y2+"\" "
							+ "style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				}
				{
					double x1 = origin[4]+worldverts[i*4+1]*scale;
					double y1 = origin[5]-worldverts[i*4+2]*scale;
					double x2 = origin[4]+worldverts[j*4+1]*scale;
					double y2 = origin[5]-worldverts[j*4+2]*scale;			
					pw.append("<line x1=\""+x1+"\" y1=\""+y1+"\" x2=\""+x2+"\" y2=\""+y2+"\" "
							+ "style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				}
			}
			else
			{
				double x1 = origin[0]+worldverts[i*4+0]*scale;
				double y1 = origin[1]+worldverts[i*4+1]*scale;
				double x2 = origin[0]+worldverts[j*4+0]*scale;
				double y2 = origin[1]+worldverts[j*4+1]*scale;			
				pw.append("<line x1=\""+x1+"\" y1=\""+y1+"\" x2=\""+x2+"\" y2=\""+y2+"\" "
						+ "style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				
			}
		}
	}
	
	public static void addWireframeCube(GeneralMatrixDouble transform,int[] origin,double scale,boolean asLocation, 
			double w,double h,double d,
			boolean as3D,
			PrintWriter pw)
	{
		double[] localverts = new double[4*8];
		double[] worldverts = new double[4*8];
		
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
	    	worldverts[vi*4+0] = transx;
	    	worldverts[vi*4+1] = transy;
	    	worldverts[vi*4+2] = transz;
	    	worldverts[vi*4+3] = transh;
		}
		
		for(int li=0;li<12;li++)
		{
			int i = cubeEdges[li*2+0];
			int j = cubeEdges[li*2+1];
			
			if(as3D)
			{
				{
					double x1 = origin[0]+worldverts[i*4+0]*scale;
					double y1 = origin[1]+worldverts[i*4+1]*scale;
					double x2 = origin[0]+worldverts[j*4+0]*scale;
					double y2 = origin[1]+worldverts[j*4+1]*scale;			
					pw.append("<line x1=\""+x1+"\" y1=\""+y1+"\" x2=\""+x2+"\" y2=\""+y2+"\" "
							+ "style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				}
				{
					double x1 = origin[2]+worldverts[i*4+0]*scale;
					double y1 = origin[3]-worldverts[i*4+2]*scale;
					double x2 = origin[2]+worldverts[j*4+0]*scale;
					double y2 = origin[3]-worldverts[j*4+2]*scale;			
					pw.append("<line x1=\""+x1+"\" y1=\""+y1+"\" x2=\""+x2+"\" y2=\""+y2+"\" "
							+ "style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				}
				{
					double x1 = origin[4]+worldverts[i*4+1]*scale;
					double y1 = origin[5]-worldverts[i*4+2]*scale;
					double x2 = origin[4]+worldverts[j*4+1]*scale;
					double y2 = origin[5]-worldverts[j*4+2]*scale;			
					pw.append("<line x1=\""+x1+"\" y1=\""+y1+"\" x2=\""+x2+"\" y2=\""+y2+"\" "
							+ "style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				}
			}
			else
			{
				double x1 = origin[0]+worldverts[i*4+0]*scale;
				double y1 = origin[1]+worldverts[i*4+1]*scale;
				double x2 = origin[0]+worldverts[j*4+0]*scale;
				double y2 = origin[1]+worldverts[j*4+1]*scale;			
				pw.append("<line x1=\""+x1+"\" y1=\""+y1+"\" x2=\""+x2+"\" y2=\""+y2+"\" "
						+ "style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\"/>");
				
			}
		}
	}

	public static void addWireframeShape(ParametricShape shape,GeneralMatrixDouble transform,int[] origin,double scale,boolean asLocation,boolean as3D, PrintWriter pw)
	{
		if(shape instanceof ModifiedCopyShape)
		{
			ModifiedCopyShape mcs = (ModifiedCopyShape)shape;
			ParametricShape ms = mcs.modifiedCopy;
			addWireframeShape(ms, transform, origin, scale, asLocation, as3D, pw);
		}
		else
		if(shape instanceof QuadraticCurvedCuboid)
		{
			QuadraticCurvedCuboid dc = (QuadraticCurvedCuboid)shape;
			addWireframeQuadraticCurvedCube(transform, origin, scale, asLocation, dc.parameters, as3D, pw);
		}
		else
		if(shape instanceof DeformedCuboid)
		{
			DeformedCuboid dc = (DeformedCuboid)shape;
			addWireframeDeformedCube(transform, origin, scale, asLocation, dc.parameters, as3D, pw);
		}
		else
		if(shape instanceof CSGShape)
		{
			CSGShape csg = (CSGShape)shape;
			
			final double DEG2RAD = Math.PI/180.0;
			for(int si=0;si<csg.shapes.length;si++)
			{
				ParametricShape ss = csg.shapes[si];
				GeneralMatrixDouble stran = new GeneralMatrixDouble(4,4);
				stran.setIdentity();
				stran.set3DTransformPosition(csg.shapeTransforms[si*6+0], csg.shapeTransforms[si*6+1], csg.shapeTransforms[si*6+2]);
				stran.set3DTransformRotation(csg.shapeTransforms[si*6+3]*DEG2RAD, csg.shapeTransforms[si*6+4]*DEG2RAD, csg.shapeTransforms[si*6+5]*DEG2RAD);
				
				GeneralMatrixDouble containedRelativeToWorld = new GeneralMatrixDouble(4,4);
				GeneralMatrixDouble.mult(stran, transform, containedRelativeToWorld);
				addWireframeShape(ss, containedRelativeToWorld, origin, scale, asLocation, as3D, pw);
			}
		}
		else
		if(shape instanceof Cuboid)
		{
			Cuboid rbuild = (Cuboid)shape;
			double w = rbuild.parameters[0];
			double h = rbuild.parameters[1];
			double d = rbuild.parameters[2];
			
			addWireframeCube(transform, origin, scale, asLocation, w, h, d, as3D, pw);
		}
		else
		if(shape instanceof RectangularPortal)
		{
			RectangularPortal rbuild = (RectangularPortal)shape;
			double w = rbuild.parameters[0];
			double h = rbuild.parameters[1];
			double d = rbuild.parameters[2];
			
			addWireframeCube(transform, origin, scale, asLocation, w, h, d, as3D, pw);
		}
		else
		if(shape instanceof RectangularRoom)
		{
			RectangularRoom rbuild = (RectangularRoom)shape;
			double w = rbuild.parameters[0];
			double h = rbuild.parameters[1];
			double d = rbuild.parameters[2];
			
			addWireframeCube(transform, origin, scale, asLocation, w, h, d, as3D, pw);
		}
		else
		if(shape instanceof RRoom)
		{
			RRoom rbuild = (RRoom)shape;
			
			addWireframeT(transform, origin, scale, asLocation, 0.0, rbuild.parameters[0], rbuild.parameters[1], 
					0.0, rbuild.parameters[2], rbuild.parameters[3], 
					rbuild.parameters[4], as3D, pw);
		}
		else
		if(shape instanceof RectangularBuildingFloor)
		{
			RectangularBuildingFloor rbuild = (RectangularBuildingFloor)shape;

			addWireframeT(transform, origin, scale, asLocation, 0.0, rbuild.parameters[0], rbuild.parameters[1], 
					rbuild.parameters[0]-rbuild.parameters[2], rbuild.parameters[2], rbuild.parameters[3], 
					rbuild.parameters[4], as3D, pw);
		}
	}
	
	public static void addShape(ParametricShape shape,double x,double y,double scale,boolean asLocation, PrintWriter pw)
	{
		if(shape instanceof Cuboid)
		{
			Cuboid rbuild = (Cuboid)shape;
			double w = rbuild.parameters[0];
			double h = rbuild.parameters[1];
			int wi = (int)(w*scale);
			int hi = (int)(h*scale);
			
			pw.append("  <polygon points=\""+
					x+","+y+" "                 +(x+wi)+","+y+" "+
					(x+wi)+","+(y+hi)+" "       +(x)+","+(y+hi)+" "+
					"\" style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\""+
					"/>");
		}
		else
		if(shape instanceof RectangularPortal)
		{
			RectangularPortal rbuild = (RectangularPortal)shape;
			double w = rbuild.parameters[0];
			double h = rbuild.parameters[1];
			int wi = (int)(w*scale);
			int hi = (int)(h*scale);
			
			pw.append("  <polygon points=\""+
					x+","+y+" "                 +(x+wi)+","+y+" "+
					(x+wi)+","+(y+hi)+" "       +(x)+","+(y+hi)+" "+
					"\" style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\""+
					"/>");
		}
		else
		if(shape instanceof RectangularRoom)
		{
			RectangularRoom rbuild = (RectangularRoom)shape;
			double w = rbuild.parameters[0];
			double h = rbuild.parameters[1];
			int wi = (int)(w*scale);
			int hi = (int)(h*scale);
			
			if(asLocation)
			{
				pw.append("  <polygon points=\""+
						x+","+y+" "                 +(x+wi)+","+y+" "+
						(x+wi)+","+(y+hi)+" "       +(x)+","+(y+hi)+" "+
						"\" style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\""+
						"/>");
			}
			else
			{
				pw.append("  <polygon points=\""+
						x+","+y+" "                 +(x+wi)+","+y+" "+
						(x+wi)+","+(y+hi)+" "       +(x)+","+(y+hi)+" "+
						"\" style=\"fill:white;stroke:black;stroke-width:0;fill-rule:evenodd;\""+
						"/>");
			}
		}
		else
		if(shape instanceof RRoom)
		{
			RRoom rbuild = (RRoom)shape;
			
			double w = rbuild.parameters[0];
			double h = rbuild.parameters[1]+rbuild.parameters[3];
			
			int wi = (int)(w*scale);
			int hi1 = (int)(rbuild.parameters[1]*scale);
			int hi2 = (int)(rbuild.parameters[3]*scale);
			int hi = hi1+hi2;
			int wi2 = (int)(rbuild.parameters[2]*scale);
			if(asLocation)
			{
				pw.append("  <polygon points=\""+
						x+","+y+" "                 +(x+wi)+","+y+" "+					
						(x+wi)+","+(y+hi1)+" "       +(x+wi2)+","+(y+hi1)+" "+
						(x+wi2)+","+(y+hi)+" "+(x)+","+(y+hi)+" "+
						"\" style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\""+
						"/>");
			}
			else
			{
				pw.append("  <polygon points=\""+
						x+","+y+" "                 +(x+wi)+","+y+" "+					
						(x+wi)+","+(y+hi1)+" "       +(x+wi2)+","+(y+hi1)+" "+
						(x+wi2)+","+(y+hi)+" "+(x)+","+(y+hi)+" "+
						"\" style=\"fill:white;stroke:black;stroke-width:0;fill-rule:evenodd;\""+
						"/>");
			}
		}
		else
		if(shape instanceof RectangularBuildingFloor)
		{
			RectangularBuildingFloor rbuild = (RectangularBuildingFloor)shape;
			double w = rbuild.parameters[0];
			double h = rbuild.parameters[1];
			int wi = (int)(w*scale);
			int hi = (int)(h*scale);
			
			pw.append("  <polygon points=\""+
					x+","+y+" "                 +(x+wi)+","+y+" "+
					(x+wi)+","+(y+hi)+" "       +(x)+","+(y+hi)+" "+
					"\" style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\""+
					//"\" style=\"fill:black;stroke:black;stroke-width:0;fill-rule:evenodd;\""+
					"/>");

		}
		else
		if(shape instanceof ReverseRBuildingFloor)
		{
			ReverseRBuildingFloor rbuild = (ReverseRBuildingFloor)shape;
			double w = rbuild.parameters[0];
			double h = rbuild.parameters[1]+rbuild.parameters[3];
			
			int wi = (int)(w*scale);
			int hi1 = (int)(rbuild.parameters[1]*scale);
			int hi2 = (int)(rbuild.parameters[3]*scale);
			int hi = hi1+hi2;
			int wi2 = (int)(rbuild.parameters[2]*scale);
			pw.append("  <polygon points=\""+
					x+","+y+" "                 +(x+wi)+","+y+" "+
					(x+wi)+","+(y+hi)+" "       +(x+(wi-wi2))+","+(y+hi)+" "+
					(x+(wi-wi2))+","+(y+hi1)+" "+(x)+","+(y+hi1)+" "+
					"\" style=\"fill:white;stroke:black;stroke-width:1;fill-rule:evenodd;\""+
					//"\" style=\"fill:black;stroke:black;stroke-width:0;fill-rule:evenodd;\""+
					"/>");
		}
	}
	
	public static void startSVG3D(ParametricShape shape,int inset,int ceiling_height,double scale,int[] origins,PrintWriter pw)
	{
		double[] dims = new double[3];
		shape.getCuboidDimensions(dims);
//		if(shape instanceof RectangularPortal)
//		{
//			RectangularPortal rbuild = (RectangularPortal)shape;
//			double w = rbuild.parameters[0];
//			double h = rbuild.parameters[1];
//			double d = rbuild.parameters[2];
			double w = dims[0];
			double h = dims[1];
			double d = dims[2];
			int wi = (int)(w*scale);
			int hi = (int)(h*scale);
			int di = (int)(d*scale);

			int width = (wi+hi+inset*3);
			int height = (hi+ceiling_height+inset);
			
			origins[0] = inset;
			origins[1] = inset;
			
			origins[2] = inset;
			origins[3] = inset+(hi+inset)+ceiling_height;

			origins[4] = inset+(wi+inset*3);
			origins[5] = inset+(hi+inset)+ceiling_height;

			
			pw.append("<svg width=\""+(width+inset*2)+"\" height=\""+(height+inset*2)+"\">");
			
//		}
//		else
//		if(shape instanceof Cuboid)
//		{
//			Cuboid rbuild = (Cuboid)shape;
//			double w = rbuild.parameters[0];
//			double h = rbuild.parameters[1];
//			double d = rbuild.parameters[2];
//			int wi = (int)(w*scale);
//			int hi = (int)(h*scale);
//			int di = (int)(d*scale);
//
//			int width = (wi+hi+inset*3);
//			int height = (hi+ceiling_height+inset);
//			
//			origins[0] = inset;
//			origins[1] = inset;
//			
//			origins[2] = inset;
//			origins[3] = inset+(hi+inset)+ceiling_height;
//
//			origins[4] = inset+(wi+inset*3);
//			origins[5] = inset+(hi+inset)+ceiling_height;
//
//			
//			pw.append("<svg width=\""+(width+inset*2)+"\" height=\""+(height+inset*2)+"\">");
//			
//		}
	}
	
	public static void startSVG(ParametricShape shape,int inset,double scale,PrintWriter pw)
	{
		if(shape instanceof RectangularRoom)
		{
			RectangularRoom rbuild = (RectangularRoom)shape;
			double w = rbuild.parameters[0];
			double h = rbuild.parameters[1];
			int wi = (int)(w*scale);
			int hi = (int)(h*scale);
			
			pw.append("<svg width=\""+(wi+inset*2)+"\" height=\""+(hi+inset*2)+"\">");
		}
		else
		if(shape instanceof RRoom)
		{
			RRoom rbuild = (RRoom)shape;
			
			double w = rbuild.parameters[0];
			double h = rbuild.parameters[1]+rbuild.parameters[3];
			
			int wi = (int)(w*scale);
			int hi1 = (int)(rbuild.parameters[1]*scale);
			int hi2 = (int)(rbuild.parameters[3]*scale);
			int hi = hi1+hi2;
			
			pw.append("<svg width=\""+(wi+inset*2)+"\" height=\""+(hi+inset*2)+"\">");
		}
		else
		if(shape instanceof RectangularBuildingFloor)
		{
			RectangularBuildingFloor rbuild = (RectangularBuildingFloor)shape;
			double w = rbuild.parameters[0];
			double h = rbuild.parameters[1];
			int wi = (int)(w*scale);
			int hi = (int)(h*scale);
			
			pw.append("<svg width=\""+(wi+inset*2)+"\" height=\""+(hi+inset*2)+"\">");
		}
		else
		if(shape instanceof ReverseRBuildingFloor)
		{
			ReverseRBuildingFloor rbuild = (ReverseRBuildingFloor)shape;
			double w = rbuild.parameters[0];
			double h = rbuild.parameters[1]+rbuild.parameters[3];
			
			int wi = (int)(w*scale);
			int hi1 = (int)(rbuild.parameters[1]*scale);
			int hi2 = (int)(rbuild.parameters[3]*scale);
			int hi = hi1+hi2;
			int wi2 = (int)(rbuild.parameters[2]*scale);
			pw.append("<svg width=\""+(wi+inset*2)+"\" height=\""+(hi+inset*2)+"\">");
		}
	}

	public static void objectToSVG(PrintWriter pw,FileStoreInterface fs,ObjectLanguageInstance o,double ceiling_height)
	{
		int[] origins = new int[3*2];
		int inset = 40;
		double scale = 400;
		ParametricShape shape = o.physicalRepresentation.shape;
		if(shape!=null)
		{
			startSVG3D(shape,inset,(int)(ceiling_height*scale),scale,origins,pw);
			addShape3D(shape,0,0,0,scale,origins, pw);
		}

		GeneralMatrixDouble parentRelativeToWorldtran = new GeneralMatrixDouble(4,4);
		parentRelativeToWorldtran.setIdentity();
		containsToSVG(pw, fs, o, origins, inset, scale, true, parentRelativeToWorldtran, 2, true);

		//containsToSVG3D(pw, fs, o, origins, inset, scale, true, parentRelativeToWorldtran, 2);

		pw.append("</svg>");
	}
	
	public static void containsToSVG3D(PrintWriter pw,FileStoreInterface fs,ObjectLanguageInstance o,
			int[] origins,int inset,double scale,boolean printtext,
			GeneralMatrixDouble parentRelativeToWorldtran,
			int remainingDepth)
	{
		double[] dimensions = new double[3];
		double[] ctrans = o.physicalRepresentation.contains_metric_transform;
		for(int i=0;i<o.contains.length;i++)
		{
			String cid = o.contains[i];
			
			String cpath = ObjectLanguageInstance.uniqueIdToPath(cid);
			ObjectLanguageInstance c = ObjectLanguageInstance.loadObject(fs, cpath+"/data.txt");
			
			ParametricShape cshape = c.physicalRepresentation.shape;
			if(cshape!=null)
			{
				//compose the transforms
				int coff = i*12;
				GeneralMatrixDouble containedRelativeToParenttran = new GeneralMatrixDouble(4,4);
				containedRelativeToParenttran.setIdentity();
				for(int ti=0;ti<3;ti++)
					containedRelativeToParenttran.value[ti] = ctrans[coff+ti];
				for(int ti=0;ti<3;ti++)
					containedRelativeToParenttran.value[4+ti] = ctrans[coff+3+ti];
				for(int ti=0;ti<3;ti++)
					containedRelativeToParenttran.value[8+ti] = ctrans[coff+6+ti];
				containedRelativeToParenttran.set3DTransformPosition(ctrans[coff+9], ctrans[coff+10], ctrans[coff+11]);

				GeneralMatrixDouble containedRelativeToWorld = new GeneralMatrixDouble(4,4);
				
				GeneralMatrixDouble.mult(parentRelativeToWorldtran, containedRelativeToParenttran, containedRelativeToWorld);
				
				double x = containedRelativeToWorld.value[4*3+0];
				double y = containedRelativeToWorld.value[4*3+1];
				double z = containedRelativeToWorld.value[4*3+2];
				
				addShape3D(cshape,x*scale,y*scale,-z*scale, scale, origins, pw);
				
				if((remainingDepth>0)&&(c.contains.length>0))
				{
					containsToSVG3D(pw, fs, c, origins, inset, scale, false, containedRelativeToWorld, remainingDepth-1);
				}
				
				if(printtext)
				{
					cshape.getCuboidDimensions(dimensions);
					
					if(dimensions[0]<0.5)
					{
						pw.append("<g writing-mode=\"tb-rl\" font-size=\"14px\" font-family=\"Verdana\"\">");
					    //<text y="50px" text-anchor="middle">Direction</text>
						pw.append("<text fill=\"#000000\" x=\""+(7+inset+(x)*scale)+"\" y=\""+(inset+1+(y)*scale)+"\">"+c.name+"</text>");
						pw.append("</g>");
					}
					else
					{
						pw.append("<g writing-mode=\"lr-tb\" font-size=\"14px\" font-family=\"Verdana\"\">");
						pw.append("<text fill=\"#000000\" x=\""+(inset+x*scale)+"\" y=\""+(inset-1+(y+dimensions[1])*scale)+"\">"+c.name+"</text>");
						pw.append("</g>");
					}
				}	
			}
			
		}

	}
	
	public static void portalToSVG(PrintWriter pw,FileStoreInterface fs,ObjectLanguageInstance o,double ceiling_height)
	{
		int[] origins = new int[3*2];
		int inset = 20;
		double scale = 200;
		ParametricShape shape = o.physicalRepresentation.shape;
		if(shape!=null)
		{
			startSVG3D(shape,inset,(int)(ceiling_height*scale),scale,origins,pw);
			addShape3D(shape,0,0,0,scale,origins, pw);
		}

		GeneralMatrixDouble parentRelativeToWorldtran = new GeneralMatrixDouble(4,4);
		parentRelativeToWorldtran.setIdentity();
		containsToSVG(pw, fs, o, origins, inset, scale, true, parentRelativeToWorldtran, 2, true);

		//containsToSVG3D(pw, fs, o, origins, inset, scale, true, parentRelativeToWorldtran, 2);
		pw.append("</svg>");
	}
	
	public static void containsToSVG(PrintWriter pw,FileStoreInterface fs,ObjectLanguageInstance o,
			int[] origin,int inset,double scale,boolean printtext,
			GeneralMatrixDouble parentRelativeToWorldtran,
			int remainingDepth,
			boolean as3D)
	{
		double[] dimensions = new double[3];
		double[] ctrans = o.physicalRepresentation.contains_metric_transform;
		for(int i=0;i<o.contains.length;i++)
		{
			String cid = o.contains[i];
			
			String cpath = ObjectLanguageInstance.uniqueIdToPath(cid);
			ObjectLanguageInstance c = ObjectLanguageInstance.loadObject(fs, cpath+"/data.txt");
			
			ParametricShape cshape = c.physicalRepresentation.shape;
			if(cshape!=null)
			{
				//compose the transforms
				int coff = i*12;
				GeneralMatrixDouble containedRelativeToParenttran = new GeneralMatrixDouble(4,4);
				containedRelativeToParenttran.setIdentity();
				for(int ti=0;ti<3;ti++)
					containedRelativeToParenttran.value[ti] = ctrans[coff+ti];
				for(int ti=0;ti<3;ti++)
					containedRelativeToParenttran.value[4+ti] = ctrans[coff+3+ti];
				for(int ti=0;ti<3;ti++)
					containedRelativeToParenttran.value[8+ti] = ctrans[coff+6+ti];
				containedRelativeToParenttran.set3DTransformPosition(ctrans[coff+9], ctrans[coff+10], ctrans[coff+11]);

				GeneralMatrixDouble containedRelativeToWorld = new GeneralMatrixDouble(4,4);
				
				//GeneralMatrixDouble.mult(parentRelativeToWorldtran, containedRelativeToParenttran, containedRelativeToWorld);
				
				GeneralMatrixDouble.mult(containedRelativeToParenttran, parentRelativeToWorldtran, containedRelativeToWorld);
				
				double x = containedRelativeToWorld.value[4*3+0];
				double y = containedRelativeToWorld.value[4*3+1];
				double z = containedRelativeToWorld.value[4*3+2];
				
				//addShape(cshape,origin[0]+x*scale,origin[1]+y*scale, scale,false, pw);
				addWireframeShape(cshape,containedRelativeToWorld, origin, scale, false, as3D, pw);
				
				if((remainingDepth>0)&&(c.contains.length>0))
				{
					containsToSVG(pw, fs, c, origin, inset, scale, false, containedRelativeToWorld, remainingDepth-1, as3D);
				}
				
				if(printtext)
				{
					cshape.getCuboidDimensions(dimensions);
					
					if(dimensions[0]<0.5)
					{
						pw.append("<g writing-mode=\"tb-rl\" font-size=\"14px\" font-family=\"Verdana\"\">");
					    //<text y="50px" text-anchor="middle">Direction</text>
						pw.append("<text fill=\"#000000\" x=\""+(7+inset+(x)*scale)+"\" y=\""+(inset+1+(y)*scale)+"\">"+c.name+"</text>");
						pw.append("</g>");
					}
					else
					{
						pw.append("<g writing-mode=\"lr-tb\" font-size=\"14px\" font-family=\"Verdana\"\">");
						pw.append("<text fill=\"#000000\" x=\""+(inset+x*scale)+"\" y=\""+(inset-1+(y+dimensions[1])*scale)+"\">"+c.name+"</text>");
						pw.append("</g>");
					}
				}	
			}
			
		}

	}
	
	public static void roomToSVG(PrintWriter pw,FileStoreInterface fs,ObjectLanguageInstance o)
	{
		double scale = 200;
		int inset = 10+(int)(0.4*scale);
		
		int[] origin = new int[]{inset,inset};
		
		ParametricShape shape = o.physicalRepresentation.shape;
		if(shape!=null)
		{
			startSVG(shape,inset,scale,pw);
			addShape(shape,inset,inset,scale,true, pw);
		}

		GeneralMatrixDouble parentRelativeToWorldtran = new GeneralMatrixDouble(4,4);
		parentRelativeToWorldtran.setIdentity();
		containsToSVG(pw, fs, o, origin, inset, scale, true, parentRelativeToWorldtran, 2, false);
		
//		double[] dimensions = new double[3];
//		double[] ctrans = o.physicalRepresentation.contains_metric_transform;
//		for(int i=0;i<o.contains.length;i++)
//		{
//			String cid = o.contains[i];
//			
//			String cpath = ObjectLanguageInstance.uniqueIdToPath(cid);
//			ObjectLanguageInstance c = ObjectLanguageInstance.loadObject(fs, cpath+"/data.txt");
//			
//			ParametricShape cshape = c.physicalRepresentation.shape;
//			if(cshape!=null)
//			{
//				double x = ctrans[i*12+ 9];
//				double y = ctrans[i*12+10];
//				double z = ctrans[i*12+11];
//				addShape(cshape,inset+x*scale,inset+y*scale, scale,false, pw);
//				cshape.getCuboidDimensions(dimensions);
//				
//				if(dimensions[0]<0.5)
//				{
//					pw.append("<g writing-mode=\"tb-rl\" font-size=\"14px\" font-family=\"Verdana\"\">");
//				    //<text y="50px" text-anchor="middle">Direction</text>
//					pw.append("<text fill=\"#000000\" x=\""+(7+inset+(x)*scale)+"\" y=\""+(inset+1+(y)*scale)+"\">"+c.name+"</text>");
//					pw.append("</g>");
//				}
//				else
//				{
//					pw.append("<g writing-mode=\"lr-tb\" font-size=\"14px\" font-family=\"Verdana\"\">");
//					pw.append("<text fill=\"#000000\" x=\""+(inset+x*scale)+"\" y=\""+(inset-1+(y+dimensions[1])*scale)+"\">"+c.name+"</text>");
//					pw.append("</g>");
//				}
//			}
//			
//		}
		
		pw.append("</svg>");
	}
	
	public static void mapToSVG(PrintWriter pw,FileStoreInterface fs,ObjectLanguageInstance o)
	{
		int inset = 20;
		double scale = 100;
		
		int[] origin = new int[]{inset,inset};

		ParametricShape shape = o.physicalRepresentation.shape;
		if(shape!=null)
		{
			startSVG(shape,inset,scale,pw);
			addShape(shape,inset,inset,scale,true, pw);
		}
		
		GeneralMatrixDouble parentRelativeToWorldtran = new GeneralMatrixDouble(4,4);
		parentRelativeToWorldtran.setIdentity();
		containsToSVG(pw, fs, o, origin, inset, scale, true, parentRelativeToWorldtran, 2, false);

//		double[] dimensions = new double[3];
//		double[] ctrans = o.physicalRepresentation.contains_metric_transform;
//		for(int i=0;i<o.contains.length;i++)
//		{
//			String cid = o.contains[i];
//			
//			String cpath = ObjectLanguageInstance.uniqueIdToPath(cid);
//			ObjectLanguageInstance c = ObjectLanguageInstance.loadObject(fs, cpath+"/data.txt");
//			
//			ParametricShape cshape = c.physicalRepresentation.shape;
//			if(cshape!=null)
//			{
//				double x = ctrans[i*12+ 9];
//				double y = ctrans[i*12+10];
//				double z = ctrans[i*12+11];
//				addShape(cshape,inset+x*scale,inset+y*scale, scale,false, pw);
//				cshape.getCuboidDimensions(dimensions);
//				
//				if(dimensions[0]<0.5)
//				{
//					pw.append("<g writing-mode=\"tb-rl\" font-size=\"14px\" font-family=\"Verdana\"\">");
//				    //<text y="50px" text-anchor="middle">Direction</text>
//					pw.append("<text fill=\"#000000\" x=\""+(7+inset+(x)*scale)+"\" y=\""+(inset+1+(y)*scale)+"\">"+c.name+"</text>");
//					pw.append("</g>");
//				}
//				else
//				{
//					pw.append("<g writing-mode=\"lr-tb\" font-size=\"14px\" font-family=\"Verdana\"\">");
//					pw.append("<text fill=\"#000000\" x=\""+(inset+x*scale)+"\" y=\""+(inset-1+(y+dimensions[1])*scale)+"\">"+c.name+"</text>");
//					pw.append("</g>");
//				}
//			}
//			
//		}
		
		pw.append("</svg>");

	}

}
