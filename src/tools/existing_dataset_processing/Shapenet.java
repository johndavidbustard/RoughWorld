package tools.existing_dataset_processing;

import utils.FileStoreInterface;
import utils.TextImportExport;

public class Shapenet 
{

	//Create instances from a list of 
	
	public static void main(String[] commandLineParameters) throws Exception 
	{
		processFolder();
	}
	
	public static void processFolder()
	{
		FileStoreInterface fs = new FileStoreInterface(".");
		String instances = TextImportExport.loadTextFile("RelatedDatasets/Shapenet/02818832/instances.txt");
		String prefix = "object_cn_bed_wn31_102814024";
		String[] instancea = instances.split("\n");
		for(int i=0;i<instancea.length;i++)
		{
			fs.createDirectory("Concepts/"+prefix+"/instances/"+prefix+"_sn_"+instancea[i]+"/");
		}
		System.out.println("test");
	}
}
