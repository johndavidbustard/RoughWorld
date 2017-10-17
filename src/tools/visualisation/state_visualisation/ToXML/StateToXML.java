package tools.visualisation.state_visualisation.ToXML;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ai.knowledge_representation.state_instance.ObjectLanguageInstance;
import ai.knowledge_representation.story_instance.StoryInstance;
import ai.model_construction.state_model.PoseLanguageModel;

public class StateToXML 
{
	public static void objectToXMLFile(ObjectLanguageInstance o)
	{
		String concept = o.uniqueID;
		int ind = concept.indexOf("/");
		String instance = concept.substring(ind+1);
		concept = concept.substring(0,ind);

		String instancepath = "Concepts/"+concept+"/instances/"+instance;		
		String datapath = instancepath+"/data.xml";

		try {
            JAXBContext context = JAXBContext.newInstance(ObjectLanguageInstance.class);
            Marshaller m = context.createMarshaller();
            //for pretty-print XML in JAXB
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Write to System.out for debugging
            // m.marshal(emp, System.out);

            // Write to File
            m.marshal(o, new File(datapath));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
	}
	
	public static void objectToXMLFile(StoryInstance o)
	{
		String concept = o.uniqueID;
		int ind = concept.indexOf("/");
		String instance = concept.substring(ind+1);
		concept = concept.substring(0,ind);

		String instancepath = "Concepts/"+concept+"/instances/"+instance;		
		String datapath = instancepath+"/data.xml";

		try {
            JAXBContext context = JAXBContext.newInstance(StoryInstance.class);
            Marshaller m = context.createMarshaller();
            //for pretty-print XML in JAXB
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Write to System.out for debugging
            // m.marshal(emp, System.out);

            // Write to File
            m.marshal(o, new File(datapath));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
	}
	
	public static void objectToXMLFile(PoseLanguageModel o)
	{
		String concept = o.uniqueID;
		int ind = concept.indexOf("/");
		String instance = concept.substring(ind+1);
		concept = concept.substring(0,ind);

		String instancepath = "Concepts/"+concept+"/models/"+instance;		
		String datapath = instancepath+"/data.xml";

		try {
            JAXBContext context = JAXBContext.newInstance(PoseLanguageModel.class);
            Marshaller m = context.createMarshaller();
            //for pretty-print XML in JAXB
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Write to System.out for debugging
            // m.marshal(emp, System.out);

            // Write to File
            m.marshal(o, new File(datapath));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
	}
}
