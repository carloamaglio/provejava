package dataLogger;


public class DataLoggerController {

	FileDataLogger logger;

	public DataLoggerController(String filename, int slot, String[] variables) {
		logger = FileDataLogger.create(filename);
		CMonitor.addMonitorSlotListener(slot, this);
	}

}
