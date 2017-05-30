package ai.knowledge_representation.story_instance;

public class StoryInstance 
{
	//Initial state
	String[] objectInstances = new String[]{};
	String[] phonemeInstances = new String[]{};
	String[] soundInstances = new String[]{};
	String[] poseInstances = new String[]{};
	
	//Timed changes to state (which may occur in parallel)
	String[] actionInstances = new String[]{};
	double[] actionStartAndEnd = new double[]{};
	
	//the physical representation of a story comes from the physical information in actions and state
}
