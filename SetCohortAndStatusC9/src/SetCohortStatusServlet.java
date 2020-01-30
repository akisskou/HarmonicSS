

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
 * Servlet implementation class SetCohortStatusServlet
 */
@WebServlet("/SetCohortStatusServlet")
public class SetCohortStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetCohortStatusServlet() {
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
			NewCohort coh = new Gson().fromJson(request.getReader(), NewCohort.class);
			URL url = new URL("https://private.harmonicss.eu/index.php/apps/coh/api/1.0/p6");
			String name = "hrexpert";
	        String password = "1hrexpert2!";

	        String authString = name + ":" + password;
	        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	        String authStringEnc = new String(authEncBytes);
			JSONObject params = new JSONObject();
			params.put("darId", coh.darId);
		    params.put("cohortId", coh.cohortId);
		    params.put("statusId", coh.statusId);
		    params.put("statusDate", coh.statusDate);
		    params.put("validDate", coh.validDate);
		    params.put("remarks", coh.remarks);
		    params.put("filename", coh.filename);
		    String postData = params.toString();
		    //System.out.println(postData);
		    byte[] postDataBytes = postData.getBytes("UTF-8");
		    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    conn.setRequestMethod("POST");
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
