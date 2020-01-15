package criterionManager;

import java.io.IOException;
//import java.util.List;
//import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.map.SerializationConfig.Feature;

//import java.io.File;
//import java.nio.file.Path;
//import java.nio.file.Paths;

//import org.codehaus.jackson.type.TypeReference;

public class Criterions {


	public static ArrayList<Criterion> list_of_criterions = new ArrayList<Criterion>();
	public Criterions() {
	}
	
	public static void create_random_JSON_file() {
		demographics_gender crit1 = new demographics_gender("demo_sex","SEX-01");
		demographics_ethnicity crit2 = new demographics_ethnicity("demo_ethnicity","ETHN-05");
		demographics_education crit3 = new demographics_education("demo_education","EDU-LEV-03");
		ArrayList<Criterion> cur_list_of_criterions = new ArrayList<Criterion>();
		cur_list_of_criterions.add(crit1);
		cur_list_of_criterions.add(crit2);
		cur_list_of_criterions.add(crit3);

		Container_of_Criterions obj = new Container_of_Criterions();
		obj.setList_of_criterions(cur_list_of_criterions);
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.disable(Feature.FAIL_ON_EMPTY_BEANS);
		try {
			String res=mapper.writeValueAsString(obj);
			System.out.println("The new json file that we created is: "+ res);
		} catch (JsonGenerationException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	
	}
	static void main(String[] args){
		create_random_JSON_file();		
	}
	
	public static Container_of_Criterions From_JSON_String_to_Criterion_ArrayList(String inputJSON) throws JsonParseException, JsonMappingException, IOException {
		Container_of_Criterions results=null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		
		//try {
		System.out.println("SOS Look: "+inputJSON);
			Container_of_Criterions obj = mapper.readValue(inputJSON, Container_of_Criterions.class);
			results=obj;
		/*} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		
		return results;
	}
	//	public static Set Execute_Queries() {return null;	}
}
