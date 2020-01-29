

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Servlet implementation class GetXMLServlet
 */
@WebServlet("/GetXMLServlet")
public class GetXMLServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetXMLServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		try {
	        String webPage = "https://private.harmonicss.eu/index.php/apps/coh/api/1.0/s2?darId=59";
	        String name = "hrexpert";
	        String password = "1hrexpert2!";

	        String authString = name + ":" + password;
	        System.out.println("auth string: " + authString);
	        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	        String authStringEnc = new String(authEncBytes);
	        System.out.println("Base64 encoded auth string: " + authStringEnc);

	        URL url = new URL(webPage);
	        URLConnection urlConnection = url.openConnection();
	        urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
	        InputStream is = urlConnection.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is);

	        int numCharsRead;
	        char[] charArray = new char[1024];
	        StringBuffer sb = new StringBuffer();
	        while ((numCharsRead = isr.read(charArray)) > 0) {
	            sb.append(charArray, 0, numCharsRead);
	        }
	        String result = sb.toString();
	        JSONArray jsonarray = new JSONArray(result);
		    String myXML = jsonarray.getJSONObject(0).get("serviceConfig").toString();
	        response.setContentType("text/xml;charset=UTF-8");
	        PrintWriter pw = response.getWriter();
			pw.print(myXML);
			pw.close();
	        
	        System.out.println("*** BEGIN ***");
	        System.out.println(result);
	        System.out.println("*** END ***");
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
	}

}
