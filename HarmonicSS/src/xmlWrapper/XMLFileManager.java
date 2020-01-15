package xmlWrapper;

public interface XMLFileManager {
	public String readXMLbyRequestID(String requestID);
	public void writeXMLResponse();
	public String[] getCohortsAccessByRequestID(String requestID);
}
