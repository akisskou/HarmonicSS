package xmlWrapper;

public interface XMLFileManager {
	public void readXMLbyRequestID(String requestID);
	public void writeXMLResponse();
	public String[] getCohortsAccessByRequestID(String requestID);
}
