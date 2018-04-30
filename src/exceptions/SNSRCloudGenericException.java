package exceptions;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class SNSRCloudGenericException extends Exception {

	private static Logger log = Logger.getLogger(SNSRCloudGenericException.class);

	public SNSRCloudGenericException(String message, Throwable object) {
		super(message, object);
		log.info("Exception Message is :" + message);
	}
	
}
