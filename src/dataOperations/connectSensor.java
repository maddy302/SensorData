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
		ArrayList<String> listOfDataSources = new ArrayList<String>();
		LocalDateTime sampleDateTime = LocalDateTime.now();
		listOfDataSources.add("https://coastwatch.pfeg.noaa.gov/erddap/tabledap/pmelTaoDySst.csv?"+
				"longitude,latitude,time,station,wmo_platform_code,T_25&time%3E=2015-05-23T12:00:00Z&time%3C=2015-05-31T12:00:00Z");
		
		listOfDataSources.add("https://coastwatch.pfeg.noaa.gov/erddap/tabledap/scrippsGliders.csv?"
				+ "institution%2Cplatform_id%2Cplatform_type%2Cwmo_id%2Ctime%2Clatitude%2Clongitude%2Cpressure%2Ctemperature%2Csalinity%2Cconductivity&"
				+ "time%3E=2018-03-22&time%3C=2018-04-30T16%3A14%3A30Z&"
				+ "latitude%3E=30.5&latitude%3C=40.97715&longitude%3E=-125.6113&longitude%3C=-108"
				+ "&distinct()&orderBy(%22time%22)");
		/*String uri = "https://coastwatch.pfeg.noaa.gov/erddap/tabledap/pmelTaoDySst.csv?"
				+"longitude,latitude,time,station,wmo_platform_code,T_25"+"&time>=2015-05-23T12:00:00Z"
				+"&time<=2015-05-31T12:00:00Z";*/
		//Need to make a repository of urls for data
		String uri2 = "https://coastwatch.pfeg.noaa.gov/erddap/tabledap/pmelTaoDySst.csv?"+
				"longitude,latitude,time,station,wmo_platform_code,T_25&time%3E=2015-05-23T12:00:00Z&time%3C=2015-05-31T12:00:00Z";
		

		try {
			for(int i=0;i<listOfDataSources.size();i++) {
			URL erddap = new URL(listOfDataSources.get(i));
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
				//throw x;
				x.printStackTrace();
			}

		}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
//		return null;

	}
	
	


}
