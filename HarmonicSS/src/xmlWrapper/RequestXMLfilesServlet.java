package xmlWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Properties;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class RequestXMLfilesServlet
 */
public class RequestXMLfilesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequestXMLfilesServlet() {
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
			InputStream input = new FileInputStream(getServletContext().getRealPath("/WEB-INF/infos.properties"));
	    	Properties prop = new Properties();
            // load a properties file
            prop.load(input);
			/*Scanner s = new Scanner(new BufferedReader(new FileReader(getServletContext().getRealPath("/WEB-INF/properties.txt"))));
			String[] line1 = s.nextLine().split(",");
			File folder = new File(new URI("file:///"+line1[1].trim()));*/
            File folder = new File(new URI("file:///"+prop.getProperty("pathToXML")));
			File[] files = folder.listFiles(new FileFilter() {
			    @Override
			    public boolean accept(File f) {
			        return f.isFile();
			    }
			});
			String[] fileNames = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				fileNames[i] = files[i].getName().split(".xml")[0];
			}
			JSONObject requestXMLfiles = new JSONObject();
			requestXMLfiles.put("requestXMLfiles", fileNames);
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.print(requestXMLfiles.toString());
			pw.close();
		} catch (Exception e) {
			System.out.println("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
