package utils.shapes.skinned;

import java.io.Serializable;

import utils.GeneralMatrixDouble;
import utils.GeneralMatrixInt;
import utils.GeneralMatrixString;

public class Skeleton implements Serializable
{
	private static final long serialVersionUID = 1L;

	//the hierarchy of bones of a skeleton
	public GeneralMatrixString boneNames = new GeneralMatrixString(1);
	public GeneralMatrixInt boneParents = new GeneralMatrixInt(1);
	
	//the joints of a skeleton
	public GeneralMatrixString jointNames = new GeneralMatrixString(1);
	public GeneralMatrixInt boneJoints = new GeneralMatrixInt(1);

	//Details for DAZ characters
	public GeneralMatrixString boneLabels = new GeneralMatrixString(1);
	public GeneralMatrixInt rotationOrder = new GeneralMatrixInt(1); //
	
	//runtime visualisation of the skeleton
	public GeneralMatrixDouble vpos = new GeneralMatrixDouble(3);
	public GeneralMatrixDouble bmats = new GeneralMatrixDouble(9);
	public GeneralMatrixDouble bonelengths = new GeneralMatrixDouble(1);
	public GeneralMatrixDouble localbindbmats = new GeneralMatrixDouble(9);

	//runtime definition of a pose (local orientations of bones)
	public GeneralMatrixDouble lpos = new GeneralMatrixDouble(3);

	public GeneralMatrixDouble tvpos = new GeneralMatrixDouble(3);
	public GeneralMatrixDouble tbmats = new GeneralMatrixDouble(3);
	
}
