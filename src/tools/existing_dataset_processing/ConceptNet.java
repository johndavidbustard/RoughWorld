package tools.existing_dataset_processing;

import java.util.ArrayList;

import web.WebClient;

public class ConceptNet 
{
	public static void main(String[] commandLineParameters) throws Exception 
	{
		
	}
	
	public static void getRooms()
	{
		//get all the concept net entries that isa room
		try
		{
			WebClient checkIP = new WebClient("http://conceptnet.io/c/en/room");
			checkIP.post("");
			long startIP = System.currentTimeMillis();
			ArrayList<ArrayList<String> > checkq = new ArrayList<ArrayList<String> >();
			while(true)
			{
				checkIP.getQueue(checkq);
				boolean processedsomething = false;
				for (int i = 0; i < checkq.size(); i++) 
				{
					ArrayList<String> resultlines = checkq.remove(i);
					
					
					resultlines.get(0);
					break;
				}
				if(processedsomething)
					break;
				
				Thread.sleep(16);
				
				long time = System.currentTimeMillis();
				if((time-startIP)>1000)
				{
					//timeout
					break;
				}
			}	
		}
		catch(Exception e)
		{
			
		}
	}
}
