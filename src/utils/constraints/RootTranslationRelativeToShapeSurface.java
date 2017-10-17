package utils.constraints;

import ai.knowledge_representation.state_instance.ObjectLanguageInstance;
import utils.FileStoreInterface;
import utils.GeneralMatrixDouble;
import utils.GeneralMatrixObject;
import utils.GeneralMatrixString;
import utils.shapes.HumanShape;
import utils.shapes.ParametricShape;

public class RootTranslationRelativeToShapeSurface extends PhysicalConstraint
{
	private static final long serialVersionUID = 1L;

	//how is the position determined
	//relative position in ShapeX (on surface, zero is left, 1.0 is right)
	//relative position in ShapeY (on surface)
	//relative position in ShapeZ (away from surface)

	//absolute delta in ShapeX (on surface, zero is left, 1.0 is right)
	//absolute delta in ShapeY (on surface)
	//absolute delta in ShapeZ (away from surface)

	//absolute delta in X
	//absolute delta in Y
	//absolute delta in Z

	//absolute override X weight
	//absolute override X
	//absolute override Y weight
	//absolute override Y
	//absolute override Z weight
	//absolute override Z

	public static final String[] parameternames = {
		"CRelSX","CRelSY","CRelSZ",
		"CAbsDSX","CAbsDSY","CAbsDSZ",
		"CAbsDX","CAbsDY","CAbsDZ",
		"CAbsXw","CAbsYw","CAbsZw",
		"CAbsX","CAbsY","CAbsZ",
		"ORelSX","ORelSY","ORelSZ",
		"OAbsDSX","OAbsDSY","OAbsDSZ",
		"OAbsDX","OAbsDY","OAbsDZ",
		"OAbsXw","OAbsYw","OAbsZw",
		"OAbsX","OAbsY","OAbsZ",
};
	public static final String[] constraineenames = {"Person","Object"};
	public static final String[] shapenames = {"CharacterShape","ObjectShape"};
	public static final String[] sidenames = {"CharacterShapeFace","ObjectShapeFace"};

	public String[] getShapeNames() {return shapenames;}
	public String[] getShapeSideNames() {return sidenames;}

	public String[] getParameterNames() { return parameternames; }
	public String[] getConstraineeNames() { return constraineenames; }
	public int numConstrainees() { return 2; }
	public int numErrorTerms() { return 3; }

	public RootTranslationRelativeToShapeSurface()
	{
		parameters = new double[]{
				0.0,0.0,0.0,
				0.0,0.0,0.0,
				0.0,0.0,0.0,
				0.0,0.0,0.0,
				0.0,0.0,0.0,

				0.0,0.0,0.0,
				0.0,0.0,0.0,
				0.0,0.0,0.0,
				0.0,0.0,0.0,
				0.0,0.0,0.0,
			};
		constrainees = new int[]{0,-1};
		shapes = new String[2][1];
		shape_side = new String[2];
		shape_side[0] = ParametricShape.allsidenames[0];
		shape_side[1] = ParametricShape.allsidenames[0];
	}
	
	public void calcErrors(FileStoreInterface fs,double[] e,int offset,GeneralMatrixObject characters,GeneralMatrixObject objects,GeneralMatrixDouble transforms)
	{
		GeneralMatrixDouble surfaceTransform = new GeneralMatrixDouble(4,4);
		surfaceTransform.setIdentity();
		
		{
			int oind = (-constrainees[1])-1;
			ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[oind];
			PhysicalConstraint.getShapeTransform(fs,o,shapes[1],2,shape_side[1],
					parameters[15+0],parameters[15+1],parameters[15+2],
					surfaceTransform);
		}
		
		double tx = surfaceTransform.value[4*3+0];
		double ty = surfaceTransform.value[4*3+1];
		double tz = surfaceTransform.value[4*3+2];
		
//		"AbsDSX","AbsDSY","AbsDSZ",
//		"AbsDX","AbsDY","AbsDZ",
//		"AbsXw","AbsYw","AbsZw",
//		"AbsX","AbsY","AbsZ"};

		
		int cind = constrainees[0];
		ObjectLanguageInstance o = (ObjectLanguageInstance)characters.value[cind];
		if(o.physicalRepresentation.shape instanceof HumanShape)
		{
			HumanShape s = (HumanShape)o.physicalRepresentation.shape;
			s.parametersUpdated();
			PhysicalConstraint.getShapeTransform(fs,o,shapes[0],2,shape_side[0],
					parameters[0],parameters[1],parameters[2],
					surfaceTransform);
			double ex = tx-surfaceTransform.value[4*3+0];//s.parameters[0];
			double ey = ty-surfaceTransform.value[4*3+1];//s.parameters[1];
			double ez = tz-surfaceTransform.value[4*3+2];//s.parameters[2];
			e[offset+0] = ex;
			e[offset+1] = ey;
			e[offset+2] = ez;
		}

	}
	
	
	public void minimiseErrors(FileStoreInterface fs,GeneralMatrixObject characters,GeneralMatrixObject objects,GeneralMatrixDouble transforms)
	{
		GeneralMatrixDouble objectsurfaceTransform = new GeneralMatrixDouble(4,4);
		objectsurfaceTransform.setIdentity();
		
		if(shapes[1][0]!=null)
		{
			int oind = (-constrainees[1])-1;
			ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[oind];
			PhysicalConstraint.getShapeTransform(fs,o,shapes[1],2,shape_side[1],
					parameters[15+0],parameters[15+1],parameters[15+2],
					objectsurfaceTransform);
			objectsurfaceTransform.value[4*3+0] += parameters[15+3*2+0];
			objectsurfaceTransform.value[4*3+1] += parameters[15+3*2+1];
			objectsurfaceTransform.value[4*3+2] += parameters[15+3*2+2];
		}
		
		double tx = objectsurfaceTransform.value[4*3+0];
		double ty = objectsurfaceTransform.value[4*3+1];
		double tz = objectsurfaceTransform.value[4*3+2];
		
//		"AbsDSX","AbsDSY","AbsDSZ",
//		"AbsDX","AbsDY","AbsDZ",
//		"AbsXw","AbsYw","AbsZw",
//		"AbsX","AbsY","AbsZ"};

		GeneralMatrixDouble charactersurfaceTransform = new GeneralMatrixDouble(4,4);
		charactersurfaceTransform.setIdentity();

		
		int cind = constrainees[0];
		ObjectLanguageInstance o = (ObjectLanguageInstance)characters.value[cind];
		if((o.physicalRepresentation.shape instanceof HumanShape)&&(shapes[0][0]!=null))
		{
			HumanShape s = (HumanShape)o.physicalRepresentation.shape;
			s.parametersUpdated();
			PhysicalConstraint.getShapeTransform(fs,o,shapes[0],2,shape_side[0],
					parameters[0],parameters[1],parameters[2],
					charactersurfaceTransform);
			charactersurfaceTransform.value[4*3+0] += parameters[3*2+0];
			charactersurfaceTransform.value[4*3+1] += parameters[3*2+1];
			charactersurfaceTransform.value[4*3+2] += parameters[3*2+2];
			double ex = tx-charactersurfaceTransform.value[4*3+0];//s.parameters[0];
			double ey = ty-charactersurfaceTransform.value[4*3+1];//s.parameters[1];
			double ez = tz-charactersurfaceTransform.value[4*3+2];//s.parameters[2];
			s.parameters[0]+=ex;
			s.parameters[1]+=ey;
			s.parameters[2]+=ez;
		}

	}
	
	public void minimiseErrors(GeneralMatrixString objectpaths,GeneralMatrixObject wobjects,GeneralMatrixString posepaths,GeneralMatrixObject poses,
			GeneralMatrixObject characters,GeneralMatrixObject objects,GeneralMatrixDouble transforms)
	{
		GeneralMatrixDouble objectsurfaceTransform = new GeneralMatrixDouble(4,4);
		objectsurfaceTransform.setIdentity();
		
		if(shapes[1][0]!=null)
		{
			int oind = (-constrainees[1])-1;
			ObjectLanguageInstance o = (ObjectLanguageInstance)objects.value[oind];
			PhysicalConstraint.getShapeTransform(objectpaths,wobjects,
					o,shapes[1],2,shape_side[1],
					parameters[15+0],parameters[15+1],parameters[15+2],
					objectsurfaceTransform);
			objectsurfaceTransform.value[4*3+0] += parameters[15+3*2+0];
			objectsurfaceTransform.value[4*3+1] += parameters[15+3*2+1];
			objectsurfaceTransform.value[4*3+2] += parameters[15+3*2+2];
		}
		
		double tx = objectsurfaceTransform.value[4*3+0];
		double ty = objectsurfaceTransform.value[4*3+1];
		double tz = objectsurfaceTransform.value[4*3+2];
		
//		"AbsDSX","AbsDSY","AbsDSZ",
//		"AbsDX","AbsDY","AbsDZ",
//		"AbsXw","AbsYw","AbsZw",
//		"AbsX","AbsY","AbsZ"};

		GeneralMatrixDouble charactersurfaceTransform = new GeneralMatrixDouble(4,4);
		charactersurfaceTransform.setIdentity();

		
		int cind = constrainees[0];
		ObjectLanguageInstance o = (ObjectLanguageInstance)characters.value[cind];
		if((o.physicalRepresentation.shape instanceof HumanShape)&&(shapes[0][0]!=null))
		{
			HumanShape s = (HumanShape)o.physicalRepresentation.shape;
			s.parametersUpdated();
			PhysicalConstraint.getShapeTransform(objectpaths,wobjects,
					o,shapes[0],2,shape_side[0],
					parameters[0],parameters[1],parameters[2],
					charactersurfaceTransform);
			charactersurfaceTransform.value[4*3+0] += parameters[3*2+0];
			charactersurfaceTransform.value[4*3+1] += parameters[3*2+1];
			charactersurfaceTransform.value[4*3+2] += parameters[3*2+2];
			double ex = tx-charactersurfaceTransform.value[4*3+0];//s.parameters[0];
			double ey = ty-charactersurfaceTransform.value[4*3+1];//s.parameters[1];
			double ez = tz-charactersurfaceTransform.value[4*3+2];//s.parameters[2];
			s.parameters[0]+=ex;
			s.parameters[1]+=ey;
			s.parameters[2]+=ez;
		}

	}
}
