package utils.constraints;

import java.io.Serializable;

import ai.knowledge_representation.state_instance.ObjectLanguageInstance;
import utils.FileStoreInterface;
import utils.GeneralMatrixDouble;
import utils.GeneralMatrixObject;
import utils.GeneralMatrixString;
import utils.shapes.Cuboid;
import utils.shapes.DeformedCuboid;
import utils.shapes.HumanShape;
import utils.shapes.ParametricShape;
import utils.shapes.human.HumanDeformedCuboids;

public abstract class PhysicalConstraint implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String[] empty = {};
	public static final String[] constraintnames = {"AbsoluteTransform","AbsoluteLocalBoneRotation","RootTranslationRelativeToShapeSurface","IKRelativeToShapeSurface","AbsoluteWorldBoneFacingRelativeToBindMatrix","IKRelativeToSelf","AbsoluteRootTranslation"};

	public double[] parameters;
	public abstract String[] getParameterNames();
	public int[] constrainees;
	public abstract String[] getConstraineeNames();
	public int[] bones;
	public String[] getBoneNames() {return empty;}
	public String[][] shapes;
	public String[] getShapeNames() {return empty;}
	public String[] shape_side;
	public String[] getShapeSideNames() {return empty;}
//	public String[] shape_corner;
//	public String[] getShapeCornerNames() {return empty;}
	
	public abstract int numConstrainees();
	public abstract int numErrorTerms();
	public abstract void calcErrors(FileStoreInterface fs,double[] e,int offset,GeneralMatrixObject characters,GeneralMatrixObject objects,GeneralMatrixDouble transforms);
	public abstract void minimiseErrors(FileStoreInterface fs,GeneralMatrixObject characters,GeneralMatrixObject objects,GeneralMatrixDouble transforms);
	
	public abstract void minimiseErrors(GeneralMatrixString objectpaths,GeneralMatrixObject objects,GeneralMatrixString posepaths,GeneralMatrixObject poses,
			GeneralMatrixObject pcharacters,GeneralMatrixObject pobjects,GeneralMatrixDouble transforms);			
	
	public void parametersUpdated() {}
	
	public static PhysicalConstraint[] add(PhysicalConstraint[] v, PhysicalConstraint i)
	{
		PhysicalConstraint[] nv = new PhysicalConstraint[v.length+1];
		System.arraycopy(v, 0, nv, 0, v.length);
		nv[v.length] = i;
		return nv;
	}
	public static PhysicalConstraint[] remove(PhysicalConstraint[] v, int ind,int len)
	{
		PhysicalConstraint[] nv = new PhysicalConstraint[v.length-len];
		if(ind!=0)
			System.arraycopy(v, 0, nv, 0, ind);
		if((v.length-(ind+len))>=0)
			System.arraycopy(v, ind+len, nv, ind, v.length-(ind+len));
		return nv;
	}

	public static PhysicalConstraint get(String name)
	{
		if(name.equalsIgnoreCase("AbsoluteTransform"))
			return new AbsoluteTransform();
		if(name.equalsIgnoreCase("AbsoluteLocalBoneRotation"))
			return new AbsoluteLocalBoneRotation();
		if(name.equalsIgnoreCase("RootTranslationRelativeToShapeSurface"))
			return new RootTranslationRelativeToShapeSurface();
		if(name.equalsIgnoreCase("IKRelativeToShapeSurface"))
			return new IKRelativeToShapeSurface();
		if(name.equalsIgnoreCase("AbsoluteWorldBoneFacingRelativeToBindMatrix"))
			return new AbsoluteWorldBoneFacingRelativeToBindMatrix();
		if(name.equalsIgnoreCase("IKRelativeToSelf"))
			return new IKRelativeToSelf();
		if(name.equalsIgnoreCase("AbsoluteRootTranslation"))
			return new AbsoluteRootTranslation();
		return null;
	}
	
	public static void getShapeTransform(
			double[] deformedCuboidVerts,
			double rx,double ry,double rz,
			GeneralMatrixDouble surfaceTransform)
	{
		//Back
		//Front
		double dx,dy,dz;
		double xdx=0,xdy=0,xdz=0;
		double ydx=0,ydy=0,ydz=0;
		double zdx=0,zdy=0,zdz=0;
		
		double[] zprojectCorners = new double[4*3];
		for(int i=0;i<4;i++)
		{
			dx = (deformedCuboidVerts[3*i+0+3*4]-deformedCuboidVerts[3*i+0])*rz;
			dy = (deformedCuboidVerts[3*i+1+3*4]-deformedCuboidVerts[3*i+1])*rz;
			dz = (deformedCuboidVerts[3*i+2+3*4]-deformedCuboidVerts[3*i+2])*rz;

			zdx += dx;
			zdy += dy;
			zdz += dz;
			
			zprojectCorners[i*3+0] = dx+deformedCuboidVerts[3*i+0];
			zprojectCorners[i*3+1] = dy+deformedCuboidVerts[3*i+1];
			zprojectCorners[i*3+2] = dz+deformedCuboidVerts[3*i+2];
		}
		
		
		double[] yprojectCorners = new double[2*3];
		for(int i=0;i<2;i++)
		{
			dx = (zprojectCorners[i*3+0+3*2]-zprojectCorners[i*3+0])*ry;
			dy = (zprojectCorners[i*3+1+3*2]-zprojectCorners[i*3+1])*ry;
			dz = (zprojectCorners[i*3+2+3*2]-zprojectCorners[i*3+2])*ry;
			
			ydx += dx;
			ydy += dy;
			ydz += dz;
			
			yprojectCorners[i*3+0] = dx+zprojectCorners[3*i+0];
			yprojectCorners[i*3+1] = dy+zprojectCorners[3*i+1];
			yprojectCorners[i*3+2] = dz+zprojectCorners[3*i+2];
		}
		
		dx = (yprojectCorners[0+3]-yprojectCorners[0])*rx;
		dy = (yprojectCorners[1+3]-yprojectCorners[1])*rx;
		dz = (yprojectCorners[2+3]-yprojectCorners[2])*rx;
		
		xdx += dx;
		xdy += dy;
		xdz += dz;

		surfaceTransform.set3DTransformPosition(dx+yprojectCorners[0], 
				dy+yprojectCorners[1], 
				dz+yprojectCorners[2]);
	}
	
	static final int[] humanCuboidToDeformedCuboid = {
//		6,7,
//		2,3,
//		4,5,
//		0,1,
		0,1,
		2,3,
		4,5,
		6,7,
	};
	
	
	
	public static void getShapeTransform(GeneralMatrixString objectpaths,GeneralMatrixObject wobjects,
			ObjectLanguageInstance o,String[] relativeShape,int offset,String side,
			double rx,double ry,double rz,
			GeneralMatrixDouble surfaceTransform)
	{
		String cmd = relativeShape[offset];
		if(cmd.equalsIgnoreCase("contains"))
		{
			offset++;
			cmd = relativeShape[offset];
			double[] ctrans = o.physicalRepresentation.contains_metric_transform;
			for(int i=0;i<o.contains.length;i++)
			{
				String cid = o.contains[i];
				int cind = objectpaths.find(cid);
				//String cpath = ObjectLanguageInstance.uniqueIdToPath(cid);
				ObjectLanguageInstance c = (ObjectLanguageInstance)wobjects.value[cind];//ObjectLanguageInstance.loadObject(fs, cpath+"/data.txt");

				if(c.name.equalsIgnoreCase(cmd))
				{
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
					
					GeneralMatrixDouble.mult(surfaceTransform, containedRelativeToParenttran, containedRelativeToWorld);

					surfaceTransform.set(containedRelativeToWorld);
					getShapeTransform(objectpaths, wobjects, c, relativeShape, offset+1, side, rx, ry, rz, surfaceTransform);
					break;
				}
			}
		}
		else
		{
			double dx = 0.0;
			double dy = 0.0;
			double dz = 0.0;
			ParametricShape shape = o.physicalRepresentation.shape;
			if(shape instanceof HumanShape)
			{
				HumanShape s = (HumanShape)shape;
				for(int si=0;si<HumanDeformedCuboids.cuboid_names.length;si++)
				{
					if(relativeShape[offset].equalsIgnoreCase(HumanDeformedCuboids.cuboid_names[si]))
					{
						double nrx = rx;
						double nry = ry;
						double nrz = rz;
						if(side.equalsIgnoreCase("Left"))
						{
							nrx = -rz;
							nry = ry;
							nrz = rx;
						}
						else
						if(side.equalsIgnoreCase("Right"))
						{
							nrx = (rz+1.0);
							nry = ry;
							nrz = rx;
						}
						else
						if(side.equalsIgnoreCase("Bottom"))
						{
							nrx = rx;
							nry = -rz;
							nrz = ry;
						}
						else
						if(side.equalsIgnoreCase("Top"))
						{
							nrx = rx;
							nry = rz+1.0;
							nrz = ry;
						}
						else
						if(side.equalsIgnoreCase("Back"))
						{
							nrx = rx;
							nry = ry;
							nrz = -rz;
						}
						else
						if(side.equalsIgnoreCase("Front"))
						{
							nrx = rx;
							nry = ry;
							nrz = rz+1.0;
						}

						double[] cuboidcorners = new double[8*3];
						int[] cs = HumanDeformedCuboids.cuboid[si];
						for(int vi=0;vi<cs.length;vi++)
						{
							int vind = cs[vi];
							int cvi = humanCuboidToDeformedCuboid[vi];
							cuboidcorners[cvi*3+0] = s.pos.value[vind*3+0];
							cuboidcorners[cvi*3+1] = s.pos.value[vind*3+1];
							cuboidcorners[cvi*3+2] = s.pos.value[vind*3+2];
						}
						
						double[] worldverts = new double[3*8];

						for(int vi=0;vi<8;vi++)
						{
							double px = cuboidcorners[vi*3+0];
							double py = cuboidcorners[vi*3+1];
							double pz = cuboidcorners[vi*3+2];
							double ph = 1.0;
					    	double transx = (px*surfaceTransform.value[4*0+0])+(py*surfaceTransform.value[4*1+0])+(pz*surfaceTransform.value[4*2+0])+(ph*surfaceTransform.value[4*3+0]);
					    	double transy = (px*surfaceTransform.value[4*0+1])+(py*surfaceTransform.value[4*1+1])+(pz*surfaceTransform.value[4*2+1])+(ph*surfaceTransform.value[4*3+1]);
					    	double transz = (px*surfaceTransform.value[4*0+2])+(py*surfaceTransform.value[4*1+2])+(pz*surfaceTransform.value[4*2+2])+(ph*surfaceTransform.value[4*3+2]);
					    	worldverts[vi*3+0] = transx;
					    	worldverts[vi*3+1] = transy;
					    	worldverts[vi*3+2] = transz;
						}

						getShapeTransform(worldverts, nrx, nry, nrz, surfaceTransform);
						break;
					}
				}
			}
			else
			if(shape instanceof Cuboid)
			{
				double nrx = rx;
				double nry = ry;
				double nrz = rz;
				if(side.equalsIgnoreCase("Left"))
				{
					nrx = -rz;
					nry = ry;
					nrz = rx;
				}
				else
				if(side.equalsIgnoreCase("Right"))
				{
					nrx = (rz+1.0);
					nry = ry;
					nrz = rx;
				}
				else
				if(side.equalsIgnoreCase("Bottom"))
				{
					nrx = rx;
					nry = -rz;
					nrz = ry;
				}
				else
				if(side.equalsIgnoreCase("Top"))
				{
					nrx = rx;
					nry = rz+1.0;
					nrz = ry;
				}
				else
				if(side.equalsIgnoreCase("Back"))
				{
					nrx = rx;
					nry = ry;
					nrz = -rz;
				}
				else
				if(side.equalsIgnoreCase("Front"))
				{
					nrx = rx;
					nry = ry;
					nrz = rz+1.0;
				}
				
				Cuboid s = (Cuboid)shape;
				
				dx += surfaceTransform.value[4*0+0]*nrx*s.parameters[0]
						+surfaceTransform.value[4*1+0]*nry*s.parameters[1]
						+surfaceTransform.value[4*2+0]*nrz*s.parameters[2];
				dy += surfaceTransform.value[4*0+1]*nrx*s.parameters[0]
						+surfaceTransform.value[4*1+1]*nry*s.parameters[1]
						+surfaceTransform.value[4*2+1]*nrz*s.parameters[2];
				dz += surfaceTransform.value[4*0+2]*nrx*s.parameters[0]
						+surfaceTransform.value[4*1+2]*nry*s.parameters[1]
						+surfaceTransform.value[4*2+2]*nrz*s.parameters[2];

				surfaceTransform.value[4*3+0] += dx;
				surfaceTransform.value[4*3+1] += dy;
				surfaceTransform.value[4*3+2] += dz;
			}
			else
			{
				surfaceTransform.setIdentity();
			}
		}
	}
	
	public static void getShapeTransform(FileStoreInterface fs,ObjectLanguageInstance o,String[] relativeShape,int offset,String side,
			double rx,double ry,double rz,
			GeneralMatrixDouble surfaceTransform)
	{
		String cmd = relativeShape[offset];
		if(cmd.equalsIgnoreCase("contains"))
		{
			offset++;
			cmd = relativeShape[offset];
			double[] ctrans = o.physicalRepresentation.contains_metric_transform;
			for(int i=0;i<o.contains.length;i++)
			{
				String cid = o.contains[i];
				String cpath = ObjectLanguageInstance.uniqueIdToPath(cid);
				ObjectLanguageInstance c = ObjectLanguageInstance.loadObject(fs, cpath+"/data.txt");

				if(c.name.equalsIgnoreCase(cmd))
				{
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
					
					GeneralMatrixDouble.mult(surfaceTransform, containedRelativeToParenttran, containedRelativeToWorld);

					surfaceTransform.set(containedRelativeToWorld);
					getShapeTransform(fs, c, relativeShape, offset+1, side, rx, ry, rz, surfaceTransform);
					break;
				}
			}
		}
		else
		{
			double dx = 0.0;
			double dy = 0.0;
			double dz = 0.0;
			ParametricShape shape = o.physicalRepresentation.shape;
			if(shape instanceof HumanShape)
			{
				HumanShape s = (HumanShape)shape;
				for(int si=0;si<HumanDeformedCuboids.cuboid_names.length;si++)
				{
					if(relativeShape[offset].equalsIgnoreCase(HumanDeformedCuboids.cuboid_names[si]))
					{
						double nrx = rx;
						double nry = ry;
						double nrz = rz;
						if(side.equalsIgnoreCase("Left"))
						{
							nrx = -rz;
							nry = ry;
							nrz = rx;
						}
						else
						if(side.equalsIgnoreCase("Right"))
						{
							nrx = (rz+1.0);
							nry = ry;
							nrz = rx;
						}
						else
						if(side.equalsIgnoreCase("Bottom"))
						{
							nrx = rx;
							nry = -rz;
							nrz = ry;
						}
						else
						if(side.equalsIgnoreCase("Top"))
						{
							nrx = rx;
							nry = rz+1.0;
							nrz = ry;
						}
						else
						if(side.equalsIgnoreCase("Back"))
						{
							nrx = rx;
							nry = ry;
							nrz = -rz;
						}
						else
						if(side.equalsIgnoreCase("Front"))
						{
							nrx = rx;
							nry = ry;
							nrz = rz+1.0;
						}

						double[] cuboidcorners = new double[8*3];
						int[] cs = HumanDeformedCuboids.cuboid[si];
						for(int vi=0;vi<cs.length;vi++)
						{
							int vind = cs[vi];
							int cvi = humanCuboidToDeformedCuboid[vi];
							cuboidcorners[cvi*3+0] = s.pos.value[vind*3+0];
							cuboidcorners[cvi*3+1] = s.pos.value[vind*3+1];
							cuboidcorners[cvi*3+2] = s.pos.value[vind*3+2];
						}
						
						double[] worldverts = new double[3*8];

						for(int vi=0;vi<8;vi++)
						{
							double px = cuboidcorners[vi*3+0];
							double py = cuboidcorners[vi*3+1];
							double pz = cuboidcorners[vi*3+2];
							double ph = 1.0;
					    	double transx = (px*surfaceTransform.value[4*0+0])+(py*surfaceTransform.value[4*1+0])+(pz*surfaceTransform.value[4*2+0])+(ph*surfaceTransform.value[4*3+0]);
					    	double transy = (px*surfaceTransform.value[4*0+1])+(py*surfaceTransform.value[4*1+1])+(pz*surfaceTransform.value[4*2+1])+(ph*surfaceTransform.value[4*3+1]);
					    	double transz = (px*surfaceTransform.value[4*0+2])+(py*surfaceTransform.value[4*1+2])+(pz*surfaceTransform.value[4*2+2])+(ph*surfaceTransform.value[4*3+2]);
					    	worldverts[vi*3+0] = transx;
					    	worldverts[vi*3+1] = transy;
					    	worldverts[vi*3+2] = transz;
						}

						getShapeTransform(worldverts, nrx, nry, nrz, surfaceTransform);
						break;
					}
				}
			}
			else
			if(shape instanceof Cuboid)
			{
				double nrx = rx;
				double nry = ry;
				double nrz = rz;
				if(side.equalsIgnoreCase("Left"))
				{
					nrx = -rz;
					nry = ry;
					nrz = rx;
				}
				else
				if(side.equalsIgnoreCase("Right"))
				{
					nrx = (rz+1.0);
					nry = ry;
					nrz = rx;
				}
				else
				if(side.equalsIgnoreCase("Bottom"))
				{
					nrx = rx;
					nry = -rz;
					nrz = ry;
				}
				else
				if(side.equalsIgnoreCase("Top"))
				{
					nrx = rx;
					nry = rz+1.0;
					nrz = ry;
				}
				else
				if(side.equalsIgnoreCase("Back"))
				{
					nrx = rx;
					nry = ry;
					nrz = -rz;
				}
				else
				if(side.equalsIgnoreCase("Front"))
				{
					nrx = rx;
					nry = ry;
					nrz = rz+1.0;
				}
				
				Cuboid s = (Cuboid)shape;
				
				dx += surfaceTransform.value[4*0+0]*nrx*s.parameters[0]
						+surfaceTransform.value[4*1+0]*nry*s.parameters[1]
						+surfaceTransform.value[4*2+0]*nrz*s.parameters[2];
				dy += surfaceTransform.value[4*0+1]*nrx*s.parameters[0]
						+surfaceTransform.value[4*1+1]*nry*s.parameters[1]
						+surfaceTransform.value[4*2+1]*nrz*s.parameters[2];
				dz += surfaceTransform.value[4*0+2]*nrx*s.parameters[0]
						+surfaceTransform.value[4*1+2]*nry*s.parameters[1]
						+surfaceTransform.value[4*2+2]*nrz*s.parameters[2];

				surfaceTransform.value[4*3+0] += dx;
				surfaceTransform.value[4*3+1] += dy;
				surfaceTransform.value[4*3+2] += dz;
			}
			else
			{
				surfaceTransform.setIdentity();
			}
		}
	}
}
