package xmlWrapper;

import java.io.IOException;

/*import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;*/
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Result_UIDs {
//return "{\"UIDs_defined_ALL_elements\":["+results[0]+"],\"UIDs_UNdefined_some_elements\":["+results[1]+"]}";
	//public static ArrayList<String> UIDs_defined_ALL_elements = new ArrayList<String>();  
	//public static ArrayList<String> UIDs_UNdefined_some_elements = new ArrayList<String>();  
	
	public String[] UIDs_defined_ALL_elements = null; // new ArrayList<String>();  
	public String[] UIDs_UNdefined_some_elements = null; //new ArrayList<String>();  
	
	public Result_UIDs() {
		this.UIDs_defined_ALL_elements = null;
		this.UIDs_UNdefined_some_elements = null;
	}
	public  void Input_UIDs(Boolean mode, String UIDs) {
		
		if(!UIDs.isEmpty()) {
			if (mode) {
				this.UIDs_UNdefined_some_elements=(UIDs.substring(1)).split(" ");		
			} else {
				this.UIDs_defined_ALL_elements=(UIDs.substring(1)).split(" ");
			}
		} else {
			
			if (mode) {
				this.UIDs_UNdefined_some_elements=new String[1]; UIDs_UNdefined_some_elements[0]="";		
			} else {
				this.UIDs_defined_ALL_elements=new String[1]; UIDs_defined_ALL_elements[0]="";	
			}
		}
		
	}
	public  String Output_JSON_UIDs() {
		String result ="";
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		//mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		//mapper.disable(Feature.FAIL_ON_EMPTY_BEANS);
		try {
			 result=mapper.writeValueAsString(this);
		} catch (JsonGenerationException e) {
			result ="JsonGenerationException Failed to construct the output JSON file.";
			e.printStackTrace();
		/*} catch (JsonMappingException e) {
			result ="JsonMappingException Failed to construct the output JSON file.";
			e.printStackTrace();*/
		} catch (IOException e) {
			result ="IOException Failed to construct the output JSON file.";
			e.printStackTrace();
		}
		return result;
	}
	/*public String[] getUIDs_defined_ALL_elements() {
		return UIDs_defined_ALL_elements;
	}
	public void setUIDs_defined_ALL_elements(String[] uIDs_defined_ALL_elements) {
		UIDs_defined_ALL_elements = uIDs_defined_ALL_elements;
	}
	public String[] getUIDs_UNdefined_some_elements() {
		return UIDs_UNdefined_some_elements;
	}
	public void setUIDs_UNdefined_some_elements(String[] uIDs_UNdefined_some_elements) {
		UIDs_UNdefined_some_elements = uIDs_UNdefined_some_elements;
	}*/

	
}
