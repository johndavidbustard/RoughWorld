package tools.existing_dataset_processing;

import java.util.ArrayList;

import web.WebClient;

public class ConceptNet 
{
	public static void main(String[] commandLineParameters) throws Exception 
	{
		getRoomsHTML();
	}
	
	public static void getRoomConcept(String concept)
	{
		//We want to process a concept net room concept and construct a folder in the Concepts folder
		
	}
	
	public static void getRoomsAPI()
	{
		//get all the concept net entries that isa room
		try
		{
			WebClient checkIP = new WebClient("http://api.conceptnet.io/c/en/room/n?offset=0&limit=1000");
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
					
					String x = null;
					String relation = null;
					String relationweight = null;
					String y = null;

					for(int li=0;li<resultlines.size();li++)
					{		
						processedsomething = true;
						
						String line = resultlines.get(li);
						line = line.trim();			
						
						if(line.startsWith("\"@id\": \"/a/["))
						{
							line = line.substring("\"@id\": \"/a/[".length());
							int ind = line.indexOf(",");
							relation = line.substring(0,ind);
							line = line.substring(ind+1);
							ind = line.indexOf(",");
							x = line.substring(0,ind);
							line = line.substring(ind+1);
							ind = line.indexOf("]");
							y = line.substring(0,ind);
						}
						if(line.startsWith("\"weight\": "))
						{
							relationweight = line.substring("\"weight\": ".length());
							
							//can now process the edge information
							//we only want the things that are a room not things a room is
							if(y.equalsIgnoreCase("c/en/room/n"))
							{
								System.out.println(x+" "+relation+" "+y);
							}
						}
					}
					
				}
				if(processedsomething)
					break;
				
				Thread.sleep(16);
				
				long time = System.currentTimeMillis();
//				if((time-startIP)>1000)
//				{
//					//timeout
//					break;
//				}
			}	
		}
		catch(Exception e)
		{
			
		}
	}
	
	//the website is blocking html requests which is a pity as the api doesn't contain all the data available i.e. references to external sites is not included
	public static void getRoomsHTML()
	{
		//get all the concept net entries that isa room
		try
		{
			WebClient checkIP = new WebClient("http://conceptnet.io/c/en/room/n?rel=/r/IsA&limit=100000");
			checkIP.post("");
			long startIP = System.currentTimeMillis();
			
			final int OUTSIDE = 0;
			final int INEDGESTART = 1;
			final int INEDGEREL = 2;
			final int INEDGEEND = 3;
			int state = OUTSIDE;
			
			ArrayList<ArrayList<String> > checkq = new ArrayList<ArrayList<String> >();
			while(true)
			{
				checkIP.getQueue(checkq);
				boolean processedsomething = false;
				for (int i = 0; i < checkq.size(); i++) 
				{
					ArrayList<String> resultlines = checkq.remove(i);
					
					String x = null;
					String relation = null;
					String relationweight = null;
					String y = null;

					for(int li=0;li<resultlines.size();li++)
					{		
						processedsomething = true;
						
						String line = resultlines.get(li);
						line = line.trim();
						
						if(state==OUTSIDE)
						{
							if(line.startsWith("<td class=\"edge-start\">"))
							{
								state = INEDGESTART;
							}
						}
						else
						if(state==INEDGESTART)
						{
							if(line.startsWith("<a href=\""))
							{
								line = line.substring("<a href=\"".length());
								int ind = line.indexOf("\"");
								line = line.substring(0,ind);
								x = line;
							}
							else
							if(line.startsWith("<td class=\"edge-rel\">"))
							{
								state = INEDGEREL;
							}
						}
						else
						if(state==INEDGEREL)
						{
							if(line.startsWith("―&nbsp;<span class=\"rel-label\">"))
							{
								line = line.substring("―&nbsp;<span class=\"rel-label\">".length());
								int ind = line.indexOf("<");
								line = line.substring(0,ind);
								relation = line;
							}
							else
							if(line.startsWith("Weight: "))
							{
								line = line.substring("Weight: ".length());
								relationweight = line;
							}
							else
							if(line.startsWith("<td class=\"edge-end\">"))
							{
								state = INEDGEEND;
							}
						}
						else
						if(state==INEDGEEND)
						{
							//if(line.startsWith("<a href=\""))
							//Grab the supplementary definition so we get the sense of the word i.e. a noun not a verb
							if(line.startsWith("(<a href=\""))
							{
								line = line.substring("(<a href=\"".length());
								int ind = line.indexOf("\"");
								line = line.substring(0,ind);
								y = line;
								
								//can now process the edge information
								//we only want the things that are a room not things a room is
								if(y.equalsIgnoreCase("/c/en/room/n"))
								{
									System.out.println(x+" "+relation+" "+y);
								}
							}
							else
							if(line.startsWith("</sup>"))
							{
								state = OUTSIDE;
							}

						}
					}
					
				}
				if(processedsomething)
					break;
				
				Thread.sleep(16);
				
				long time = System.currentTimeMillis();
//				if((time-startIP)>1000)
//				{
//					//timeout
//					break;
//				}
			}	
		}
		catch(Exception e)
		{
			
		}
	}
}
