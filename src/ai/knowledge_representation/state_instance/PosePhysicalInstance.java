package ai.knowledge_representation.state_instance;

public class PosePhysicalInstance 
{
	//poses modify the shape properties and the transform of objects

	//only a certain set of poses are valid
	//a pose model is needed to create valid pose instances for an object
	//the models reflect how the object can move and typically include
	//constraints due to other interacting objects
	
	//Joint angles to pose the bones of an object
	public double[] pose_transforms = new double[]{};	

}
