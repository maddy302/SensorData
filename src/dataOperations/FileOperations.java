package dataOperations;

import java.io.File;
import java.util.ArrayList;

import dao.DataOps;
import sensor.FilePOJO;
import sensor.OceanSensor;

public class FileOperations {

	
	//log all the file names and ids into the db
/*	private FilePOJO x = null;
	FileOperations(String file_name,String extension, String description){
		x = new FilePOJO(file_name,extension);
		//x.setName(file_name);
		//x.setExtension(extension);
		x.setDescription(description);
	}*/
	
	public boolean checkFileIfExist(FilePOJO y) {
		boolean status = false;
		try {
		DataOps d = new DataOps();
		status = d.checkIfFileExists(y);
		}
		catch(Exception e) {
			e.printStackTrace();
			}
		//boolean status;
		return status;
	
}
	public boolean fileInsert(FilePOJO x) {
		int status = 0;
		boolean statB =false;
		try {
			DataOps d = new DataOps();
			status = d.insertFile(x);
			 statB =status>0?true:false;
			if(statB) {
				System.out.println("File inseted successfully");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return statB;
	}
	
	public int persistSensorObject(OceanSensor x) {
		int records=0;
		
		//ArrayList<String> fileNames = getFileNamesFromDir();
		DataOps d = new DataOps();
		try {
			boolean status = d.persistSensorData(x);
			if(status)
				records=1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return records;
		
	} 
	
	public int persistSensorBatchObject(ArrayList<OceanSensor> x) {
		int status=0;
		
		//ArrayList<String> fileNames = getFileNamesFromDir();
		DataOps d = new DataOps();
		try {
			 status = d.bacthPersist(x);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return status;
		
	} 
	public boolean checkIfFileProcessed(FilePOJO x) {
		// TODO Auto-generated method stub
		boolean stat = false;
		DataOps d = new DataOps();
		try {
			 stat = d.checkIfFileProcessed(x);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stat;
	}
	
	public int updateFileProcessedStatus(FilePOJO x) {
		int status = 0;
		DataOps d = new DataOps();
		try {
			status = d.updateFileProcessedStatus(x);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return status;
		
		
	}
	
	
}
