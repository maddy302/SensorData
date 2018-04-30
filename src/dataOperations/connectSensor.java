package dataOperations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;

import sensor.FilePOJO;


public class connectSensor {

	public static void main(String[] args) {
		LocalDateTime x = LocalDateTime.now();
		getSensorData(x);
		FileModel fModel = new FileModel();
		int statusSaveFileData=0;
	int statusSaveFile = fModel.saveFileIntoDB();
	ArrayList<FilePOJO> listFile = fModel.getFileNamesToElaborate();
	if(!listFile.isEmpty()) {
	for(FilePOJO fileToParseAndInsert : listFile) {
		int k = fModel.parseData(fileToParseAndInsert);
		statusSaveFileData = statusSaveFileData+k;
	}
	
	}
	
	}
	
	public static void getSensorData(LocalDateTime date)
	{

		try {
			LocalDateTime sampleDateTime = LocalDateTime.now();
			/*String uri = "https://coastwatch.pfeg.noaa.gov/erddap/tabledap/pmelTaoDySst.csv?"
					+"longitude,latitude,time,station,wmo_platform_code,T_25"+"&time>=2015-05-23T12:00:00Z"
					+"&time<=2015-05-31T12:00:00Z";*/
			String uri2 = "https://coastwatch.pfeg.noaa.gov/erddap/tabledap/pmelTaoDySst.csv?"+
					"longitude,latitude,time,station,wmo_platform_code,T_25&time%3E=2015-05-23T12:00:00Z&time%3C=2015-05-31T12:00:00Z";
			URL erddap = new URL(uri2);
			HttpURLConnection connectionToErddap = (HttpURLConnection)erddap.openConnection();

			/*InputStreamReader inpStrRdr = new InputStreamReader(connectionToErddap.getInputStream());
			BufferedReader br = new BufferedReader(inpStrRdr);*/
			int responseCode = connectionToErddap.getResponseCode();

			//FileUtils.copyURLToFile(erddap,);

			//check response code of the request
			if(responseCode==HttpsURLConnection.HTTP_OK) {

				String fileName = "";
				String contentDisposition = connectionToErddap.getHeaderField("Content-Disposition");
				String contentType = connectionToErddap.getContentType();
				int contentLength = connectionToErddap.getContentLength();
				if(contentDisposition!=null) {
					//extract file names
					int index = contentDisposition.indexOf("filename=");
					if (index > 0) {
						fileName = contentDisposition.substring(index + 9,
								contentDisposition.length() );
					}
				}
				else {
					// extracts file name from URL
					fileName = uri2.substring(uri2.lastIndexOf("/") + 1,
							uri2.lastIndexOf("?"));

				}
				System.out.println("Content-Type = " + contentType);
				System.out.println("Content-Disposition = " + contentDisposition);
				System.out.println("Content-Length = " + contentLength);
				System.out.println("fileName = " + contentLength);
			
				//Creating an input stream
				InputStream inputStream = connectionToErddap.getInputStream();
				//String pathToSave = "files/"+ File.separator+fileName;
				String pathToSave = "src/files/"+File.separator+fileName;
				
				//outstream to save the file
				FileOutputStream outStream = new FileOutputStream(pathToSave);
				int bytesRead = -1;
	            byte[] buffer = new byte[4096];
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	            	outStream.write(buffer, 0, bytesRead);
	            }
	 
	            outStream.close();
	            inputStream.close();
	 
	            System.out.println("File downloaded");
			
			}
			else {
				Error x = new Error("The website to fetch data isn't responding");
				throw x;
			}

		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
//		return null;

	}
	
	


}
