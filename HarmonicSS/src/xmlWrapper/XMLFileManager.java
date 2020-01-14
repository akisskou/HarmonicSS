package xmlWrapper;

public interface XMLFileManager {
	public void readXMLbyRequestID(String requestID);
	public String[] getCohortsAccessByRequestID(String requestID);
}
