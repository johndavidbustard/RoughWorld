package ai.model_construction.state_model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import tools.visualisation.state_visualisation.ToXML.StateToXML;
import utils.FileStoreInterface;
import ai.knowledge_representation.state_instance.ObjectLanguageInstance;
import ai.knowledge_representation.state_instance.ObjectPhysicalInstance;

@XmlRootElement
public class PoseLanguageModel implements Serializable
{
	//Used to create an appropriate instance of a pose
	//Most poses should be quite straightforwardly like an instance example
	//Where any relations with other objects can be replaced based on the requirements 
	//for the instance that is being generated
	private static final long serialVersionUID = 1L;

	public String uniqueID;
	public String name;
	public String type;


	//Objects can be added with the least amount of information possible
	//The ultimate goal is that a physical representation is present but initially
	//the object will simply have a name
	public PosePhysicalModel physicalRepresentation = null;
	
	//The state of an object (property value)
	public String[] properties_name = new String[]{};
	public String[] properties_value = new String[]{};

	//The things that define a pose
	public String[] character_concepts = new String[]{};
	public String[] object_concepts = new String[]{};
	
	public static String uniqueIdToPath(String uid)
	{
		int ind = uid.indexOf("/");
		String concept = uid.substring(0,ind);
		String instance = uid.substring(ind+1);
		return "Concepts/"+concept+"/models/"+instance;
	}
	
	public static PoseLanguageModel loadObject(FileStoreInterface fs,String path)
	{
		if(fs.exists(path))
		{
	      try {
		         FileInputStream fileIn = new FileInputStream(path);
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         PoseLanguageModel o = (PoseLanguageModel) in.readObject();
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

	public static void saveObject(PoseLanguageModel o,FileStoreInterface fs,String path)
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


}
