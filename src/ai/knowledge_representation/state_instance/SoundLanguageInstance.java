package ai.knowledge_representation.state_instance;

import java.io.Serializable;

public class SoundLanguageInstance implements Serializable 
{
	private static final long serialVersionUID = 1L;

	public String uniqueID;
	public String name;
	public String type;

	//Objects can be added with the least amount of information possible
	//The ultimate goal is that a physical representation is present but initially
	//The object will simply have a name
	public SoundPhysicalInstance physicalRepresentation = null;
	
	//The state of a sound (property value)
	public String[] properties_name = new String[]{};
	public String[] properties_value = new String[]{};
	
	//Sounds are a type of physical object (a disturbance in a medium)
	//They are also a point in a changing event 
	//i.e. they are like how poses are an instance of an action
}
