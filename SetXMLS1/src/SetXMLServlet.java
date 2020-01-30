

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * Servlet implementation class SetXMLServlet
 */
@WebServlet("/SetXMLServlet")
public class SetXMLServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetXMLServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		try{
			NewXMLReq myxml = new Gson().fromJson(request.getReader(), NewXMLReq.class);
			URL url = new URL("https://private.harmonicss.eu/hcloud/index.php/apps/coh/api/1.0/s1");
			String name = "hrexpert";
	        String password = "1hrexpert2!";

	        String authString = name + ":" + password;
	        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	        String authStringEnc = new String(authEncBytes);
			JSONObject params = new JSONObject();
			params.put("darId", myxml.darId);
		    params.put("serviceConfig", myxml.serviceConfig); //"&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt; &lt;iccs:PatientsSelectionRequest sessionID=&quot;101010203920&quot; requestDate=&quot;2019-10-16&quot; xsi:schemaLocation=&quot;http://www.ntua.org/criteria PatientsSelection-EligibilityCriteria-Schema-v.1.3.xsd&quot; xmlns:iccs=&quot;http://www.ntua.org/criteria&quot; xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;&gt; &lt;UserID&gt;HARM-USER-15&lt;/UserID&gt; &lt;CohortID&gt;COHORT-ID-01&lt;/CohortID&gt; &lt;CohortID&gt;COHORT-ID-17&lt;/CohortID&gt; &lt;RequestTitle&gt;Patient Selection request for clinical study XYZ&lt;/RequestTitle&gt; &lt;RequestID&gt;ELIGIBLE_PATIENTS_NUMBER&lt;/RequestID&gt; &lt;EligibilityCriteria&gt; &lt;InclusionCriteria&gt; &lt;!-- Demographics Criterion --&gt; &lt;Criterion UID=&quot;CRIT-ID-01&quot; Name=&quot;-&quot;&gt; &lt;Description&gt;Received Glucocorticoids&lt;/Description&gt; &lt;FormalExpression Language=&quot;JSON&quot; ID=&quot;CRIT-ID-01-FE&quot; Origin=&quot;-&quot; InfoLoss=&quot;false&quot; ProducedBy=&quot;HarmonicSS_GUI&quot; Model=&quot;HarmonicSS_ReferenceModel&quot;&gt; &lt;BooleanExpression&gt; {&quot;criterion&quot;:&quot;condition_diagnosis&quot;,&quot;condition&quot;:&quot;COND-101000&quot;} &lt;/BooleanExpression&gt; &lt;/FormalExpression&gt; &lt;/Criterion&gt; &lt;!-- Blood Test Criterion --&gt; &lt;!--Criterion UID=&quot;CRIT-ID-03&quot; Name=&quot;-&quot;&gt; &lt;Description&gt;Diagnosed with cancer&lt;/Description&gt; &lt;FormalExpression Language=&quot;JSON&quot; ID=&quot;CRIT-ID-03-FE&quot; Origin=&quot;-&quot; InfoLoss=&quot;false&quot; ProducedBy=&quot;HarmonicSS_GUI&quot; Model=&quot;HarmonicSS_ReferenceModel&quot;&gt; &lt;BooleanExpression&gt; {&quot;criterion&quot;:&quot;condition_diagnosis&quot;,&quot;condition&quot;:&quot;COND-140000&quot;} &lt;/BooleanExpression&gt; &lt;/FormalExpression&gt; &lt;/Criterion--&gt; &lt;/InclusionCriteria&gt; &lt;ExclusionCriteria&gt; &lt;!-- Medication Criterion --&gt; &lt;Criterion UID=&quot;CRIT-ID-04&quot; Name=&quot;-&quot;&gt; &lt;Description&gt;Anti-Ro/SSA was true (normal)&lt;/Description&gt; &lt;FormalExpression Language=&quot;JSON&quot; ID=&quot;CRIT-ID-04-FE&quot; Origin=&quot;-&quot; InfoLoss=&quot;false&quot; ProducedBy=&quot;HarmonicSS_GUI&quot; Model=&quot;HarmonicSS_ReferenceModel&quot;&gt; &lt;BooleanExpression&gt; {&quot;criterion&quot;:&quot;examination_lab_test&quot;,&quot;test_id&quot;:&quot;BLOOD-530&quot;} &lt;/BooleanExpression&gt; &lt;/FormalExpression&gt; &lt;/Criterion&gt; &lt;/ExclusionCriteria&gt; &lt;/EligibilityCriteria&gt; &lt;/iccs:PatientsSelectionRequest&gt;");
		    String postData = params.toString();
		    //System.out.println(postData);
		    byte[] postDataBytes = postData.getBytes("UTF-8");
		    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    conn.setRequestMethod("PUT");
		    conn.setRequestProperty("Content-Type", "application/json");
		    conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
		    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		    conn.setDoOutput(true);
		    conn.getOutputStream().write(postDataBytes);
		    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    for (int c; (c = in.read()) >= 0;)
		        sb.append((char)c);
		    String resp = sb.toString();
		    System.out.println(resp);
		    //JSONObject newRequest = new JSONObject(resp);
		    response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.print(resp);
			pw.close();
		}
	
		
		catch (Exception e) {
   			System.out.println(e);
   		}
	}

}
