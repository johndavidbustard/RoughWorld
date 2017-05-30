package ai.knowledge_representation.state_instance;

import java.io.Serializable;

public class PoseLanguageInstance implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String uniqueID;
	public String name;

	public String[] properties_name = new String[]{};
	public String[] properties_value = new String[]{};

	public PosePhysicalInstance physicalRepresentation = null;
	
}
