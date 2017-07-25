package website;

import java.util.ArrayList;

import utils.FileStoreInterface;
import web.WebClient;
import web.WebInterface;
import web.WebRequest;
import web.WebResponse;
import website.views.DynamicWebPage;
import website.views.EditMapInstance;
import website.views.EditObjectInstance;
import website.views.EditPortalInstance;
import website.views.EditRoomInstance;
import website.views.EditUtils;
import website.views.InternalIndex;
import website.views.ViewMapConcept;
import website.views.ViewObjectConcept;
import website.views.ViewRoomConcept;

//An internal website for experimentation and curation of the dataset
public class Internal 
{
	String externalip = null;
	WebInterface webInterface = null;
	FileStoreInterface fs = new FileStoreInterface("");
	
	ArrayList<DynamicWebPage> pages = new ArrayList<DynamicWebPage>();
	
	boolean keepRunning = true;
	
	//This is a main function, it is the first function that is called when a java program starts
	//The array of strings named commandLineParameters are extra information that can be
	//passed to a program when it is run using the command line
	//this is just one of many ways of varying a programs behaviour by passing information to it
		//for example some programs have config files, which are usually simple text files with name=value settings on each line
		//programs with databases (like this one) can store their settings in the database
		//some programs even read properties stored by the operating system in system variables
	public static void main(String[] commandLineParameters) throws Exception 
	{
		Internal rw = new Internal();
		rw.init();
		rw.run();
	}
	
	public void init()
	{
	
		try
		{
			if(externalip==null)
			{
				//get the world ip address
				//login to otidae
				//upload the ip address
				WebClient checkIP = new WebClient("http://icanhazip.com/");
				checkIP.post("");
				long startIP = System.currentTimeMillis();
				ArrayList<ArrayList<String> > checkq = new ArrayList<ArrayList<String> >();
				while(true)
				{
					checkIP.getQueue(checkq);
					for (int i = 0; i < checkq.size(); i++) 
					{
						ArrayList<String> resultlines = checkq.remove(i);
						externalip = resultlines.get(0);
						break;
					}
					if(externalip!=null)
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
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
		//Creates a *WebInterface* object
		//when this object is created it will attempt to connect to a network port on the computer
		//and will wait for requests to be made to that port
		//if the connection is successful the URL of the webserver will be printed out to the console
		//when this program is running you can connect to this webserver by typing the URL into a webbrowser on your machine
		//depending on the settings of your firewall you may be able to connect to the webserver from 
		//any other machine connected to the same network 
		//(e.g. other computers in the Lab or on your home network if you share the same wifi connection)
	//Port 80 is the default port for serving webpages
		//when you type "http://127.0.0.1/" as a URL into a webbrowser it will try to access port 80 on your local machine
		//sometimes this port is used by other applications
		//to deal with this you can run your webserver on a different port e.g. 8080
		//then you can access the webserver using "http://127.0.0.1:8080/"
		int port = 8080;
		webInterface = new WebInterface(port);

		InternalIndex index = new InternalIndex(fs);
		pages.add(index);
		ViewMapConcept vmi = new ViewMapConcept(fs);
		pages.add(vmi);
		ViewObjectConcept voi = new ViewObjectConcept(fs);
		pages.add(voi);
		ViewRoomConcept vri = new ViewRoomConcept(fs);
		pages.add(vri);
		EditMapInstance emi = new EditMapInstance(fs);
		pages.add(emi);
		EditObjectInstance eoi = new EditObjectInstance(fs);
		pages.add(eoi);
		EditRoomInstance eri = new EditRoomInstance(fs);
		pages.add(eri);
		EditPortalInstance epi = new EditPortalInstance(fs);
		pages.add(epi);
		
		EditUtils.findShapesToCopy(fs);
	}
	
	public void run()
	{
		ArrayList<WebRequest> requestToProcess = new ArrayList<WebRequest>();

		while(keepRunning)
		{
			webInterface.getQueue(requestToProcess);

			for(int i = 0; i < requestToProcess.size(); i++) 
			{
		        WebRequest toProcess = requestToProcess.remove(i);

		        if(toProcess.path.length()==0)
		        	toProcess.path = "Index.html";
		        
		        boolean processed = false;
		        for(int pi = 0; pi<pages.size(); pi++)
		        {
		        	DynamicWebPage p = pages.get(pi);
		        	if(p.process(toProcess))
		        	{
		        		processed = true;
		        		if(toProcess.r==null)
		        			toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, p, null );		        	
		        		break;
		        	}
		        }
		        
		        if(!processed)
		        {
			        String asFilepath = fs.decodeFilePath(toProcess.path);
			        if((asFilepath!=null)&&fs.exists(asFilepath))
			        {
			        	toProcess.r = WebResponse.serveFile(toProcess.parms, asFilepath);
			        }
			        else
			        {
			        	toProcess.r = new WebResponse( WebResponse.HTTP_NOTFOUND, WebResponse.MIME_PLAINTEXT,
								 "Error 404, file not found." );
			        }
		        }
		        //create a new thread that will send the response to the webbrowser
				Thread t = new Thread( toProcess );
				//daemon means that even if the program is closed this thread will continue until
				//it is finished
				t.setDaemon( true );
				//this starts the thread running
				t.start();

			}
			//This command pauses the update loop for 10 milliseconds
			//the goal is to have the total time to calculate each loop be under 16.7 milliseconds
			//if this performance goal is reached then the server will be able to respond to
			//changes faster than the eye can detect changes 
			//if a graphical user interface were added to this application 
			//it would appear to be responding instantaneously
			try
			{
				Thread.sleep(10);
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
		}
	}
}
