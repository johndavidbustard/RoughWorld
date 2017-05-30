package ai.knowledge_representation.state_instance;

import java.io.Serializable;

import utils.ArrayUtils;

//Most things are objects
//This class is intentionally as simple as possible so that it can be serialised to an from a database efficiently
//The assumption is that only part of an instance will be loaded at any one time
//References to other objects/rooms etc. take the form of uniqueids
//There is probably a nicer way of representing this but this will get us started
public class ObjectLanguageInstance implements Serializable 
{
	private static final long serialVersionUID = 1L;

	public String uniqueID;
	public String name;
	public String type;

	//Objects can be added with the least amount of information possible
	//The ultimate goal is that a physical representation is present but initially
	//the object will simply have a name
	public ObjectPhysicalInstance physicalRepresentation = null;
	
	//The state of an object (property value)
	public String[] properties_name = new String[]{};
	public String[] properties_value = new String[]{};

	//An object's pose can consist of many separate poses e.g. head pose, arm pose, face expression etc.
	//Each pose can have name value properties
	//This refers to 
	public String[] poses = new String[]{};

	//Relations between objects (typically only refers to objects near to this object (as could be deduced by sensing the object)
	//For example
	//Relative position in space
	public String[] relations_with = new String[]{};
	public String[] relations_with_type = new String[]{};
	public String[][] relations_properties = new String[][]{};
	
	//Objects can have named sub parts (they are not necessarily exclusive e.g. front of person, torso, rib cage,
	public String[] parts = new String[]{};
		
	//For example: 
	//Relative position in space
	//Supported by
	//Directed at
	public String[] parts_relations_with = new String[]{};
	public String[] parts_relations_with_type = new String[]{};
	//e.g. feet resting on the top of a bed
	public String[] parts_relations_part = new String[]{};
	public String[][] parts_relations_properties = new String[][]{};
	

	//Special relations relating to how space is defined
	//important for prioritising what to show or reason about
		//The object we are within (usually a room)
		public String[] within = new String[]{};
		public String[] within_type = new String[]{};
	
		//for objects like clothes, furniture, vehicles and rooms which have objects within them 
		public String[] contains = new String[]{};
		public String[] contains_type = new String[]{};

		//objects, such as buildings, can have objects within them, such as rooms, that themselves
		//contain objects
		public String[] portals_to_internal_objects = null;

		
	public void add(ObjectLanguageInstance i)
	{
		i.within = ArrayUtils.add(i.within,uniqueID);
		i.within_type = ArrayUtils.add(i.within_type,type);
		contains = ArrayUtils.add(contains,i.uniqueID);
		contains_type = ArrayUtils.add(contains_type,i.type);
	}
	public void remove(ObjectLanguageInstance i)
	{
		int ind = i.getWithinIndex(uniqueID);
		i.within = ArrayUtils.remove(i.within,ind,1);
		i.within_type = ArrayUtils.remove(i.within,ind,1);

		ind = getContainsIndex(i.uniqueID);
		contains = ArrayUtils.remove(contains,ind,1);
	}

	public int getWithinIndex(String w)
	{
		if(within==null)
			return -1;
		for(int i=0;i<within.length;i++)
		{
			if(within[i].equalsIgnoreCase(w))
			{
				return i;
			}
		}
		return -1;
	}
	public int getContainsIndex(String w)
	{
		if(contains==null)
			return -1;
		for(int i=0;i<contains.length;i++)
		{
			if(contains[i].equalsIgnoreCase(w))
			{
				return i;
			}
		}
		return -1;
	}
	

}