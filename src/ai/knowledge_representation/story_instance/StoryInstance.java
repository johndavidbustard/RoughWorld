package ai.knowledge_representation.story_instance;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import ai.knowledge_representation.story_instance.changes.ChangeState;
import tools.visualisation.state_visualisation.ToXML.StateToXML;
import utils.FileStoreInterface;

@XmlRootElement
public class StoryInstance implements Serializable 
{
	private static final long serialVersionUID = 1L;

	public String uniqueID;
	public String name;
	public String type;

	public String[] properties_name = new String[]{};
	public String[] properties_value = new String[]{};

	//maps and characters
	public String[] objects = new String[]{};
	public String[] poses = new String[]{};

	//objects will move into other objects and out again
	//this is the highest parent, from which the state of the world is drawn
	public String initialRootObject = null;
	
	//poses at times
	//where characters are at times
	public ChangeState[] changes = new ChangeState[]{};
	public double[] timeOfChange = new double[]{};
	
	
//	//Initial state
//	String[] objectInstances = new String[]{};
//	String[] phonemeInstances = new String[]{};
//	String[] soundInstances = new String[]{};
//	String[] poseInstances = new String[]{};
//		
	//the physical representation of a story comes from the physical information in actions and state

	public static String uniqueIdToPath(String uid)
	{
		int ind = uid.indexOf("/");
		String concept = uid.substring(0,ind);
		String instance = uid.substring(ind+1);
		return "Concepts/"+concept+"/instances/"+instance;
	}
	
	public static StoryInstance loadObject(FileStoreInterface fs,String path)
	{
		if(fs.exists(path))
		{
	      try {
		         FileInputStream fileIn = new FileInputStream(path);
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         StoryInstance o = (StoryInstance) in.readObject();
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

	public static void saveObject(StoryInstance o,FileStoreInterface fs,String path)
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
