package xmlWrapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONException;

import criterionManager.Criterion;

public interface XMLFileManager {
	public String readXMLbyRequestID(String requestID, String username, String password);
	public void writeXMLResponse(int cohortIndex, boolean createXML);
	public void accessCohorts(String darID, String uesrname, String password, ArrayList<Criterion> list_of_inclusive_criterions, ArrayList<Criterion> list_of_exclusive_criterions) throws MalformedURLException, IOException, JSONException, SQLException, ClassNotFoundException;
}
