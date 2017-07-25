package tools.validation;

import utils.ArrayUtils;
import utils.FileStoreInterface;
import utils.GeneralMatrixDouble;
import utils.GeneralMatrixFloat;
import ai.knowledge_representation.state_instance.ObjectLanguageInstance;

public class SpatialValidation 
{
	//Given an object
	//Validate that all the contained objects are within the container
	//And that the transforms match
	
	public static void validateMap(ObjectLanguageInstance o,FileStoreInterface fs)
	{
		boolean fix = true;
		
		double[] ctrans = o.physicalRepresentation.contains_metric_transform;
		for(int i=0;i<o.contains.length;i++)
		{
			String cid = o.contains[i];
			
			String cpath = ObjectLanguageInstance.uniqueIdToPath(cid);
			ObjectLanguageInstance c = ObjectLanguageInstance.loadObject(fs, cpath+"/data.txt");
			
			//Location in map
			if(c.type.equalsIgnoreCase("room"))
			{
				//Location of portal relative to map
				int coff = i*12;
				GeneralMatrixDouble roomRelativeToMaptran = new GeneralMatrixDouble(4,4);
				roomRelativeToMaptran.setIdentity();
				for(int ti=0;ti<3;ti++)
					roomRelativeToMaptran.value[ti] = ctrans[coff+ti];
				for(int ti=0;ti<3;ti++)
					roomRelativeToMaptran.value[4+ti] = ctrans[coff+3+ti];
				for(int ti=0;ti<3;ti++)
					roomRelativeToMaptran.value[8+ti] = ctrans[coff+6+ti];
				roomRelativeToMaptran.set3DTransformPosition(ctrans[coff+9], ctrans[coff+10], ctrans[coff+11]);

				double[] cctrans = c.physicalRepresentation.contains_metric_transform;
				for(int cci=0;cci<c.contains.length;cci++)
				{
					String ccid = c.contains[cci];
					String ccpath = ObjectLanguageInstance.uniqueIdToPath(ccid);
					ObjectLanguageInstance cc = ObjectLanguageInstance.loadObject(fs, ccpath+"/data.txt");
					if(cc.type.equalsIgnoreCase("portal"))
					{
						int ccoff = cci*12;
						GeneralMatrixDouble portalRelativeToRoomtran = new GeneralMatrixDouble(4,4);
						portalRelativeToRoomtran.setIdentity();
						for(int ti=0;ti<3;ti++)
							portalRelativeToRoomtran.value[ti] = cctrans[ccoff+ti];
						for(int ti=0;ti<3;ti++)
							portalRelativeToRoomtran.value[4+ti] = cctrans[ccoff+3+ti];
						for(int ti=0;ti<3;ti++)
							portalRelativeToRoomtran.value[8+ti] = cctrans[ccoff+6+ti];
						portalRelativeToRoomtran.set3DTransformPosition(cctrans[ccoff+9], cctrans[ccoff+10], cctrans[ccoff+11]);
						
						int oContainsccind = ArrayUtils.getIndex(o.contains,cc.uniqueID);

						if(oContainsccind==-1)
						{
							System.out.println("Error");
							
						}
						
						int occoff = oContainsccind*12;
						GeneralMatrixDouble portalRelativeToMaptran = new GeneralMatrixDouble(4,4);
						portalRelativeToMaptran.setIdentity();
						for(int ti=0;ti<3;ti++)
							portalRelativeToMaptran.value[ti] = ctrans[occoff+ti];
						for(int ti=0;ti<3;ti++)
							portalRelativeToMaptran.value[4+ti] = ctrans[occoff+3+ti];
						for(int ti=0;ti<3;ti++)
							portalRelativeToMaptran.value[8+ti] = ctrans[occoff+6+ti];
						portalRelativeToMaptran.set3DTransformPosition(ctrans[occoff+9], ctrans[occoff+10], ctrans[occoff+11]);
						
						if(
								(portalRelativeToRoomtran.value[4*3+0]<-0.36)||
								(portalRelativeToRoomtran.value[4*3+1]<-0.36)||
								(portalRelativeToRoomtran.value[4*3+2]<-0.36)
						  )
						{
							cctrans[ccoff+9] = portalRelativeToMaptran.value[4*3+0]-roomRelativeToMaptran.value[4*3+0];
							cctrans[ccoff+10] = portalRelativeToMaptran.value[4*3+1]-roomRelativeToMaptran.value[4*3+1];
							cctrans[ccoff+11] = portalRelativeToMaptran.value[4*3+2]-roomRelativeToMaptran.value[4*3+2];

							if(fix)
							{
								ObjectLanguageInstance.saveObject(c, fs, cpath+"/data.txt");
							}
							System.out.println("Err");
						}
						
						int ccWithincind = ArrayUtils.getIndex(cc.within,c.uniqueID);
						
						//portal that is contained in a room doesn't stop within
						if(ccWithincind==-1)
						{
							System.out.println("test:"+ccWithincind);
							
							if(fix)
							{
								cc.within = ArrayUtils.add(cc.within, c.uniqueID);
								cc.within_type = ArrayUtils.add(cc.within_type,c.type);
								cc.physicalRepresentation.within_metric_transform = 
										ArrayUtils.add(cc.physicalRepresentation.within_metric_transform,new double[]
										{
												cctrans[ccoff+0],cctrans[ccoff+1],cctrans[ccoff+2],
												cctrans[ccoff+3],cctrans[ccoff+4],cctrans[ccoff+5],
												cctrans[ccoff+6],cctrans[ccoff+7],cctrans[ccoff+8],
												cctrans[ccoff+9],cctrans[ccoff+10],cctrans[ccoff+11],
										}
								);
								
								ObjectLanguageInstance.saveObject(cc, fs, ccpath+"/data.txt");
							}
						}
					}
				}
			}
			else
			if(c.type.equalsIgnoreCase("portal"))
			{
				//Location of portal relative to map
				int coff = i*12;
				GeneralMatrixDouble portalRelativeToMaptran = new GeneralMatrixDouble(4,4);
				portalRelativeToMaptran.setIdentity();
				for(int ti=0;ti<3;ti++)
					portalRelativeToMaptran.value[ti] = ctrans[coff+ti];
				for(int ti=0;ti<3;ti++)
					portalRelativeToMaptran.value[4+ti] = ctrans[coff+3+ti];
				for(int ti=0;ti<3;ti++)
					portalRelativeToMaptran.value[8+ti] = ctrans[coff+6+ti];
				portalRelativeToMaptran.set3DTransformPosition(ctrans[coff+9], ctrans[coff+10], ctrans[coff+11]);
				
				//double[] cwtrans = c.physicalRepresentation.within_metric_transform;
				for(int wi=0;wi<c.within.length;wi++)
				{
					String wid = c.within[wi];
					if(wid.equalsIgnoreCase(o.uniqueID))
					{
						//check the within transform is valid
						
						
					}
					else
					{
						String wpath = ObjectLanguageInstance.uniqueIdToPath(wid);
						ObjectLanguageInstance w = ObjectLanguageInstance.loadObject(fs, wpath+"/data.txt");
						
						//the rooms index relative to the map
						int roomRelativeToMapind = ArrayUtils.getIndex(o.contains, wid);
						
						if(roomRelativeToMapind==-1)
						{
							System.out.println("Error?");
							continue;
						}
						
						
						//transform of the room relative to the map
						int roomRelativeToMapoff = roomRelativeToMapind*12;
						GeneralMatrixDouble roomRelativeToMaptran = new GeneralMatrixDouble(4,4);
						roomRelativeToMaptran.setIdentity();
						for(int ti=0;ti<3;ti++)
							roomRelativeToMaptran.value[ti] = ctrans[roomRelativeToMapoff+ti];
						for(int ti=0;ti<3;ti++)
							roomRelativeToMaptran.value[4+ti] = ctrans[roomRelativeToMapoff+3+ti];
						for(int ti=0;ti<3;ti++)
							roomRelativeToMaptran.value[8+ti] = ctrans[roomRelativeToMapoff+6+ti];
						roomRelativeToMaptran.set3DTransformPosition(ctrans[roomRelativeToMapoff+9], ctrans[roomRelativeToMapoff+10], ctrans[roomRelativeToMapoff+11]);
						
						
						GeneralMatrixDouble mapRelativeToRoomtran = new GeneralMatrixDouble(4,4);
						GeneralMatrixDouble.invertTransform(roomRelativeToMaptran, mapRelativeToRoomtran);
						
//						for(int ti=0;ti<3;ti++)
//							wtran.value[ti] = wtrans[woff+ti];
//						for(int ti=0;ti<3;ti++)
//							wtran.value[4+ti] = wtrans[woff+3+ti];
//						for(int ti=0;ti<3;ti++)
//							wtran.value[8+ti] = ctrans[woff+6+ti];
//						wtran.set3DTransformPosition(wtrans[woff+9], wtrans[woff+10], wtrans[woff+11]);

						//index of portal relative to the room
						int portalRelativeToRoomInd = ArrayUtils.getIndex(w.contains, c.uniqueID);
						
						if(portalRelativeToRoomInd==-1)
						{
							System.out.println("Error");
						}
						
						double[] wctrans = w.physicalRepresentation.contains_metric_transform;
						int portalRelativeToRoomOff = portalRelativeToRoomInd*12;
						GeneralMatrixDouble portalRelativeToRoomtran = new GeneralMatrixDouble(4,4);
						portalRelativeToRoomtran.setIdentity();
						for(int ti=0;ti<3;ti++)
							portalRelativeToRoomtran.value[ti] = wctrans[portalRelativeToRoomOff+ti];
						for(int ti=0;ti<3;ti++)
							portalRelativeToRoomtran.value[4+ti] = wctrans[portalRelativeToRoomOff+3+ti];
						for(int ti=0;ti<3;ti++)
							portalRelativeToRoomtran.value[8+ti] = wctrans[portalRelativeToRoomOff+6+ti];
						portalRelativeToRoomtran.set3DTransformPosition(wctrans[portalRelativeToRoomOff+9],wctrans[portalRelativeToRoomOff+10],wctrans[portalRelativeToRoomOff+11]);

						System.out.println("test");
					}
					
				}
			}
			
			
			
		}
	}
}
