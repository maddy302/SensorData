package dataOperations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.apache.commons.lang.math.NumberUtils;

import com.mysql.jdbc.StringUtils;

import sensor.FilePOJO;
import sensor.OceanSensor;

public class FileModel {


	public ArrayList<FilePOJO> getFileNamesFromDir(){
		ArrayList<FilePOJO> fileToProcess = new ArrayList<FilePOJO>();
		String fileName = null;
		String extension = null;
		FilePOJO x = null;
		FileOperations fileOps = new FileOperations();
		boolean fileExists=true;
		try {
			File folder = new File("src/files");
			File[] files = folder.listFiles();

			for(File f : files) {
				if(f.isFile()) {

					int lastIndexOfDot = f.getName().lastIndexOf(".");
					if(lastIndexOfDot>=0) {
						fileName = f.getName().substring(0,lastIndexOfDot);
						extension = f.getName().substring(lastIndexOfDot+1);
						x = new FilePOJO(fileName,extension);
						fileExists = fileOps.checkFileIfExist(x);
						if(!fileExists) {
							fileToProcess.add(x);
						}
					}else {
						System.out.println("File "+fileName+"."+extension+" already exists and is processed");
					}

				}
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		return fileToProcess;
	}


	public int saveFileIntoDB() {
		int status = 0;
		ArrayList<FilePOJO> fileToInsert = new ArrayList<FilePOJO>();
		FileOperations fM = new FileOperations();
		try {


			fileToInsert = getFileNamesFromDir();
			for(FilePOJO f :fileToInsert ) {
				boolean fileInsertStatus = fM.fileInsert(f);
				if(fileInsertStatus)
					status++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return status;

	}
	//form a list of sensor class object to insert into the database
	public int parseData(FilePOJO fileToParseAndInsert) {
		ArrayList<OceanSensor> sensorDataList = new ArrayList<OceanSensor>();
		FileOperations fop = new FileOperations();
		String file_name = fileToParseAndInsert.getName()+"."+fileToParseAndInsert.getExtension();
		BufferedReader br = null;
		String delimiter = ",";
		String line = "";
		int		recordStatus=0;
		int totalRecords = 0;
		int		recordStatus_batch=0;
		int totalRecords_batch = 0;
		try {
			String file = "src/files/"+file_name;
			br = new BufferedReader(new FileReader(file));
			
			while((line=br.readLine())!=null) {
				String[] x = line.split(delimiter);
				if(file_name.contains("pmelTaoDyS")) {
				if(NumberUtils.isNumber(x[0])) {
					OceanSensor ocs = new OceanSensor();
					ocs.setLongitude(x[0]);
					ocs.setLatitude(x[1]);
					ocs.setSensorId(x[3]);
					ocs.setMasterUser(x[4]);
					ocs.setConductivity(0);
					ocs.setPressure(0);
					ocs.setSalinity(0);
					ocs.setRecorded_on(LocalDateTime.now());

					try {
						double d = NumberUtils.createDouble(x[5]);
						if(Double.isNaN(d)) {
						ocs.setT_25(999);}
						else {
							ocs.setT_25(d);
						}
						ocs.setTimeUpdated(LocalDateTime.parse(x[2].substring(0, x[2].length()-1)));
					}catch(Exception e) {
						ocs.setT_25(999);
						ocs.setTimeUpdated(LocalDateTime.now());
					}
					
					recordStatus = fop.persistSensorObject(ocs);
					if(recordStatus>0) {
						totalRecords = totalRecords + recordStatus;
						//System.out.println("Record Inserted Successfully");
					}else {
						System.out.println("Record insertion failed for data"+ocs.toString());
					}
				}else{

				}
			}
				if(file_name.contains("scripps")) {
					if(x[0].equalsIgnoreCase("Scripps")) {
						OceanSensor ocs = new OceanSensor();
						ocs.setLongitude(x[6]);
						ocs.setLatitude(x[5]);
						ocs.setSensorId(x[3]);
						ocs.setMasterUser(x[0]);
						ocs.setRecorded_on(LocalDateTime.parse(x[4].substring(0, x[4].length()-1)));

						try {
							double d = NumberUtils.createDouble(x[8]);
							if(Double.isNaN(d)) {
							ocs.setT_25(999);}
							else {
								ocs.setT_25(d);
							}
							//ocs.setTimeUpdated(LocalDateTime.parse(x[2].substring(0, x[2].length()-1)));
						}catch(Exception e) {
							ocs.setT_25(999);
							//ocs.setTimeUpdated(LocalDateTime.now());
						}
						try {
							double p = NumberUtils.createDouble(x[7]);
							if(Double.isNaN(p)) {
							ocs.setPressure(999);}
							else {
								ocs.setPressure(p);
							}
							//ocs.setTimeUpdated(LocalDateTime.parse(x[2].substring(0, x[2].length()-1)));
						}catch(Exception e) {
							ocs.setPressure(999);
							//ocs.setTimeUpdated(LocalDateTime.now());
						}
						try {
							double s = NumberUtils.createDouble(x[9]);
							if(Double.isNaN(s)) {
							ocs.setSalinity(99);}
							else {
								ocs.setSalinity(s);
							}
							//ocs.setTimeUpdated(LocalDateTime.parse(x[2].substring(0, x[2].length()-1)));
						}catch(Exception e) {
							ocs.setSalinity(99);
							//ocs.setTimeUpdated(LocalDateTime.now());
						}
						try {
							double c = NumberUtils.createDouble(x[10]);
							if(Double.isNaN(c)) {
							ocs.setConductivity(99);}
							else {
								ocs.setConductivity(c);
							}
							//ocs.setTimeUpdated(LocalDateTime.parse(x[2].substring(0, x[2].length()-1)));
						}catch(Exception e) {
							ocs.setConductivity(99);
							//ocs.setTimeUpdated(LocalDateTime.now());
						}
						
						sensorDataList.add(ocs);
						}else{

					}
				}
				
			}
			recordStatus_batch = fop.persistSensorBatchObject(sensorDataList);
			if(recordStatus_batch>0) {
				totalRecords_batch = totalRecords_batch + recordStatus_batch;
				System.out.println("Record batch Inserted Successfully");
			}else {
				System.out.println("Record batch insertion failed for data");
			}
			
			if(totalRecords>0 || totalRecords_batch>0) {
				int updateStatus = fop.updateFileProcessedStatus(fileToParseAndInsert);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return recordStatus;
	}


	public ArrayList<FilePOJO> getFileNamesToElaborate(){
		ArrayList<FilePOJO> fileToProcess = new ArrayList<FilePOJO>();
		String fileName = null;
		String extension = null;
		FilePOJO x = null;
		FileOperations fileOps = new FileOperations();
		boolean fileNotProcessed=true;
		try {
			File folder = new File("src/files");
			File[] files = folder.listFiles();

			for(File f : files) {
				if(f.isFile()) {

					int lastIndexOfDot = f.getName().lastIndexOf(".");
					if(lastIndexOfDot>=0) {
						fileName = f.getName().substring(0,lastIndexOfDot);
						extension = f.getName().substring(lastIndexOfDot+1);
						x = new FilePOJO(fileName,extension);
						fileNotProcessed = fileOps.checkIfFileProcessed(x);
						if(fileNotProcessed) {
							fileToProcess.add(x);
						}
						else {
							System.out.println("File "+fileName+"."+extension+" already exists and is processed");
						}
					}

				}
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		return fileToProcess;
	}

}
