package helper;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.log4j.Logger;

/*A helper class to create a data connection*/

public class SensorCloudDataConnection {
	
	private static Connection conn = null;

	private static Logger log = Logger.getLogger(SensorCloudDataConnection.class);
	/**
	 * <br/>
	 * METHOD DESCRIPTION: <br/>
	 * 
	 * Open connection to access the underlying database. <br/>
	 * 
	 * @return Connection
	 *  
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * 
	 */
	
	public static Connection createDbConnection() throws Exception {
		
		
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/SENSOR_CLOUD","root","bsrihari09");
			log.info("-----Connection established with mySQL");
		
		return conn;
	}
	
	public static void closeConnection() throws Exception{
		conn.close();
		log.info("------Connection to mySQL closed");
	}
}
