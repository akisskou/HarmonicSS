package utilities;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import gr.hss.ntua.PatientSelection;

public class Util_Logger {
	//private static final Logger LOGGER = Logger.getLogger(PatientSelection.class.getName());
	static FileHandler fh;  

	public static Logger Initialize_logger(String log_file_name) {
		Logger LOGGER = Logger.getLogger(PatientSelection.class.getName());
		try {
			fh = new FileHandler(log_file_name, true);
		} catch (SecurityException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
        LOGGER.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);		
        return LOGGER;
	}
	
	public static void flush_handler() {
	fh.flush();	
	fh.close();
	}
	//LOGGER.info("Logger Name: "+LOGGER.getName());   
    //LOGGER.warning("Can cause ArrayIndexOutOfBoundsException");
	//LOGGER.info("Logger Name: "+LOGGER.getName());
	//LOGGER.config("index is set to "+150);  
    //LOGGER.warning("Can cause ArrayIndexOutOfBoundsException");
}
