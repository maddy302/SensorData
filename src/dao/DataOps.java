package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import exceptions.SNSRCloudGenericException;
import helper.SNSRDbQuery;
import helper.SensorCloudDataConnection;
import sensor.FilePOJO;
import sensor.OceanSensor;



public class DataOps {
	// LOGGER for handling all transaction messages in VISITORDAO
		private static Logger log = Logger.getLogger(DataOps.class);
	//JDBC API classes for data persistence
		private Connection connection = null;
		private PreparedStatement statement = null;
		private ResultSet resultSet = null;
		private SNSRDbQuery query;
		
		public DataOps() {
			ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
			query = (SNSRDbQuery) context.getBean("SqlBean");
		}
	public int insertFile(FilePOJO x) throws Exception {
		int status = 0;
		try {
			connection = SensorCloudDataConnection.createDbConnection();
			Statement selectAll = connection.createStatement();
			statement = connection.prepareStatement(query.getInsertFile());
			resultSet = selectAll.executeQuery(query.getGetFileList());
			while(resultSet.next()) {
				if((resultSet.getString("FILE_NAME")+resultSet.getString("EXTENSION")+resultSet.getString("ID")).equals(x.getName()+x.getExtension()+x.getId())) {
					status = -1;
					log.info(x.getName()+x.getExtension()+x.getId()+" already exists");
					break;
				}
			}
			if (status>=0) {
				statement.setString(1, x.getName());
				statement.setString(2, x.getExtension());
				statement.setString(3, x.getDescription());
				status = statement.executeUpdate();
				
				if(status<0) {
					throw new SNSRCloudGenericException("Records not inserted properly",new Exception());
				}
			}
		}
		finally {
			if(resultSet!=null)
			resultSet.close();
			SensorCloudDataConnection.closeConnection();
		}
		
		
		
		return status;
	}
	
	public boolean checkIfFileExists(FilePOJO x) throws Exception {
		boolean status = false;
		try {
			connection = SensorCloudDataConnection.createDbConnection();
			statement = connection.prepareStatement(query.getGetFile());
			statement.setString(1,x.getName());
			statement.setString(2, x.getExtension());
			//statement = connection.prepareStatement(query.getInsertFile());
			resultSet = statement.executeQuery();
			while(resultSet!=null && resultSet.next()) {
					status = true;
					log.info(x.getName()+x.getExtension()+x.getId()+" already exists");
					break;
			}
			
		}finally {
			resultSet.close();
			SensorCloudDataConnection.closeConnection();
		}
		return status;
	}
	
	public boolean persistSensorData(OceanSensor x) throws Exception{
		boolean insertStatus = false;
		int status = 0;
		
		try {
			connection = SensorCloudDataConnection.createDbConnection();
			statement = connection.prepareStatement(query.getInsertSensorData());
			statement.setString(1, x.getLongitude());
			statement.setString(2, x.getLatitude());
			statement.setString(3, x.getSensorId());
			statement.setDouble(4, x.getT_25());
			Timestamp timest = Timestamp.valueOf(x.getTimeUpdated());
			statement.setTimestamp(5, timest);
			
			status = statement.executeUpdate();
			if(status<0) {
				throw new SNSRCloudGenericException("Records not inserted properly",new Exception());
			}
		}finally {
			SensorCloudDataConnection.closeConnection();

		}
		if(status>0)
		return true;
		else
			return false;
		
	}
	
	public boolean checkIfFileProcessed(FilePOJO x) throws Exception {
		boolean status = false;
		try {
			connection = SensorCloudDataConnection.createDbConnection();
			statement = connection.prepareStatement(query.getGetFile());
			statement.setString(1,x.getName());
			statement.setString(2, x.getExtension());
			//statement = connection.prepareStatement(query.getInsertFile());
			resultSet = statement.executeQuery();
			while(resultSet!=null && resultSet.next()) {
				String processed = resultSet.getString("PROCESSED");
				if(processed.equalsIgnoreCase("N"))
				status = true;
			}
			
		}finally {
			resultSet.close();
			SensorCloudDataConnection.closeConnection();
		}
		return status;
	}
	
	public int updateFileProcessedStatus(FilePOJO x) throws Exception{
		int status = 0;
		try {
			connection = SensorCloudDataConnection.createDbConnection();
			statement = connection.prepareStatement(query.getUpdateProcessedFlag());
			statement.setString(1, "Y");
			statement.setString(2, x.getName());
			statement.setString(3, x.getExtension());
			status = statement.executeUpdate();
		}finally {
			SensorCloudDataConnection.closeConnection();
		}
		
		return status;
	}
}
