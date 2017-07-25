package ai.knowledge_representation.state_instance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import tools.visualisation.state_visualisation.ToXML.StateToXML;
import utils.ArrayUtils;
import utils.FileStoreInterface;

//Most things are objects
//This class is intentionally as simple as possible so that it can be serialised to an from a database efficiently
//The assumption is that only part of an instance will be loaded at any one time
//References to other objects/rooms etc. take the form of uniqueids
//There is probably a nicer way of representing this but this will get us started
@XmlRootElement
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

	public static String uniqueIdToPath(String uid)
	{
		int ind = uid.indexOf("/");
		String concept = uid.substring(0,ind);
		String instance = uid.substring(ind+1);
		return "Concepts/"+concept+"/instances/"+instance;
	}
	
	public static ObjectLanguageInstance loadObject(FileStoreInterface fs,String path)
	{
		if(fs.exists(path))
		{
	      try {
		         FileInputStream fileIn = new FileInputStream(path);
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         ObjectLanguageInstance o = (ObjectLanguageInstance) in.readObject();
		         in.close();
		         fileIn.close();
		         
		         StateToXML.objectToXMLFile(o);
		         
		         return o;
		      }catch(IOException i) {
		         i.printStackTrace();
		         return null;
		      }catch(ClassNotFoundException c) {
		         System.out.println("Employee class not found");
		         c.printStackTrace();
		         return null;
		      }
		}
		else
		{
			return null;
		}
	}

	public static void saveObject(ObjectLanguageInstance o,FileStoreInterface fs,String path)
	{
		try {
			fs.rename(path, "~"+path);
//			 File fout = new File(path);
//			 if(fout.exists())
//				 fout.delete();
	         FileOutputStream fileOut =
	         new FileOutputStream(path);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(o);
	         out.close();
	         fileOut.close();
	         
	         StateToXML.objectToXMLFile(o);
	      }
		catch(IOException i) 
	    {
	         i.printStackTrace();
	    }
	}

	public void add(ObjectLanguageInstance i)
	{
		i.within = ArrayUtils.add(i.within,uniqueID);
		i.within_type = ArrayUtils.add(i.within_type,type);
		contains = ArrayUtils.add(contains,i.uniqueID);
		contains_type = ArrayUtils.add(contains_type,i.type);
	}
	public void remove(ObjectLanguageInstance i)
	{		
		int ind = ArrayUtils.getIndex(i.within,uniqueID);
		int cind = ArrayUtils.getIndex(contains,i.uniqueID);

		physicalRepresentation.remove(i.physicalRepresentation, ind, cind);
		
		i.within = ArrayUtils.remove(i.within,ind,1);
		i.within_type = ArrayUtils.remove(i.within,ind,1);

		contains = ArrayUtils.remove(contains,cind,1);
		contains_type = ArrayUtils.remove(contains_type,cind,1);
	}

}