package jsonProcess;

import java.util.Arrays;

public class Intermediate_Layer {

	public Intermediate_Layer() {
		// TODO Auto-generated constructor stub
	}
	
	public static String preProcess_JSON(String inputJSON) {
		String outputJSON = inputJSON.replaceAll("\\s+","");//.replace("[", "[[").replaceFirst("]", "]]");
		
		String[] criterions_list = outputJSON.split("\\{\"criterion\":");  //{\"criterion\":
		criterions_list[0]=criterions_list[0]+"{";
		/*System.out.println("A§"+criterions_list[0]+"B§");
		System.out.println("A§"+criterions_list[1]+"B§");*/
		
		for (int i=1; i<criterions_list.length; i++) {
			String current_criterion = "{\"criterion\":"+criterions_list[i];
			System.out.println("position: "+i+" contents: "+ current_criterion);
			String criterion_name = "";
			if(current_criterion.contains(",")) criterion_name = current_criterion.substring(current_criterion.indexOf("\"criterion\":\""), current_criterion.indexOf("\","));
			else criterion_name = current_criterion.substring(current_criterion.indexOf("\"criterion\":\""), current_criterion.indexOf("\"}"));
			criterion_name=criterion_name.replace("\"criterion\":\"","");
			//System.out.println("The name is: "+criterion_name);
			//System.out.println("lala1: "+current_criterion);
			current_criterion=current_criterion.replace("{\"criterion\"", "\"criterionManager."+criterion_name+"\", {\"criterion\"");
			//System.out.println("A§"+current_criterion+"B§");
			
			switch(criterion_name) 
			{ 
			    case "demographics_pregnancy": 
			        {System.out.println("demographics_pregnancy");
			        current_criterion=auto_fill_JSON_elements_demographics_pregnancy(current_criterion);}
			    break; 
			    case "lifestyle_smoking": 
			    	{System.out.println("lifestyle_smoking"); 
			    	current_criterion=auto_fill_JSON_elements_lifestyle_smoking(current_criterion);}
			    break; 
			    case "condition_symptom": 
			    	{System.out.println("condition_symptom"); 
			    	current_criterion=auto_fill_JSON_elements_condition_symptom(current_criterion);}
			    break; 
			    case "condition_diagnosis": 
			    	{System.out.println("condition_diagnosis");  
			    	current_criterion=auto_fill_JSON_elements_condition_diagnosis(current_criterion);}
			    break; 
			    case "intervention_medication": 
			    	{System.out.println("intervention_medication"); 
			    	current_criterion=auto_fill_JSON_elements_intervention_medication(current_criterion);} 
			    break; 
			    case "intervention_chemotherapy": 
			        {System.out.println("intervention_chemotherapy"); //condition_diagnosis
			        current_criterion=auto_fill_JSON_elements_intervention_chemotherapy(current_criterion);}
			    break; 
			    case "intervention_surgery": 
			        {System.out.println("intervention_surgery"); //condition_diagnosis
			        current_criterion=auto_fill_JSON_elements_intervention_surgery(current_criterion);}			        
			    break;  
			    case "examination_lab_test": 
		        	{System.out.println("examination_lab_test"); //condition_diagnosis
		        	current_criterion=auto_fill_JSON_elements_examination_lab_test(current_criterion);}			        
		        break; //
			    case "examination_biopsy": 
	        		{System.out.println("examination_biopsy"); //condition_diagnosis
	        		current_criterion=auto_fill_JSON_elements_examination_biopsy(current_criterion);}			        
	        	break; 
			    case "examination_medical_imaging_test": 
        			{System.out.println("examination_medical_imaging_test"); 
        			current_criterion=auto_fill_JSON_elements_examination_medical_imaging_test(current_criterion);}			        
        		break; 
			    case "examination_questionnaire_score": 
    				{System.out.println("examination_questionnaire_score"); 
    				current_criterion=auto_fill_JSON_elements_examination_questionnaire_score(current_criterion);}		       
    			break; 
			    case "examination_essdai_domain": 
					{System.out.println("examination_essdai_domain"); 
					current_criterion=auto_fill_JSON_elements_examination_essdai_domain(current_criterion);}       
				break; 
			    case "examination_caci_condition": 
					{System.out.println("examination_caci_condition"); 
					current_criterion=auto_fill_JSON_elements_examination_caci_condition(current_criterion);}       
				break; //other_healthcare_visit
			    case "other_healthcare_visit": 
					{System.out.println("other_healthcare_visit"); 
					current_criterion=auto_fill_JSON_elements_other_healthcare_visit(current_criterion);}       
				break; //other_healthcare_visit
			    case "other_family_history": 
					{System.out.println("other_family_history"); 
					current_criterion=auto_fill_JSON_elements_other_family_history(current_criterion);}       
				break; 
			    case "other_clinical_trials": 
					{System.out.println("other_clinical_trials"); 
					current_criterion=auto_fill_JSON_elements_other_clinical_trials(current_criterion);}       
				break; 
			    case "patient": 
					{System.out.println("patient"); 
					current_criterion=auto_fill_JSON_elements_patient(current_criterion);}       
				break; //patient
			    default: 
			        System.out.println("no match"); 
			} 
			criterions_list[i]="["+current_criterion.substring(0,current_criterion.length()-0)+"]";
			criterions_list[i]=criterions_list[i].replace("},","}");
			//System.out.println("The criterion name is: "+criterion_name);
		}
		String res1=Arrays.toString(criterions_list);
		String res2=res1.substring(1, res1.length()-2).replace("{,", "");//.replaceAll("", "");
		String res3=res2.replace("}]}", "}]]}");//}]}
		//System.out.println("The result is: "+ res3+"END"); //{,
		return res3;
	}


	public static String old_Preprocess_JSON(String inputJSON) {
		String outputJSON = inputJSON.replace("[", "[[").replaceFirst("]", "]]");
		String criterion_name = inputJSON.substring(inputJSON.indexOf("\"criterion\":\""), inputJSON.indexOf("\","));
		criterion_name=criterion_name.replace("\"criterion\":\"","");
		outputJSON=outputJSON.replace("{\"criterion\"", "\"criterionManager."+criterion_name+"\", {\"criterion\"");
		return outputJSON;
	}
	
	
	public static String old2_Preprocess_JSON(String inputJSON) {
		String outputJSON = inputJSON.replace("[", "[[").replaceFirst("]", "]]");
		String criterion_name = inputJSON.substring(inputJSON.indexOf("\"criterion\":\""), inputJSON.indexOf("\","));
		criterion_name=criterion_name.replace("\"criterion\":\"","");
		outputJSON=outputJSON.replace("{\"criterion\"", "\"criterionManager."+criterion_name+"\", {\"criterion\"");
		switch(criterion_name) 
	    { 
	        case "demographics_pregnancy": 
	            {System.out.println("demographics_pregnancy");
	            outputJSON=auto_fill_JSON_elements_demographics_pregnancy(outputJSON);}
	            break; 
	        case "lifestyle_smoking": 
	        	{System.out.println("lifestyle_smoking"); 
	        	outputJSON=auto_fill_JSON_elements_lifestyle_smoking(outputJSON);}
	            break; 
	        case "condition_symptom": 
	        	{System.out.println("condition_symptom"); 
	        	outputJSON=auto_fill_JSON_elements_condition_symptom(outputJSON);}
	            break; 
	        case "condition_diagnosis": 
	            System.out.println("condition_diagnosis"); 
	            break; 
	        case "intervention_medication": 
	        	{System.out.println("intervention_medication"); 
	        	outputJSON=auto_fill_JSON_elements_intervention_medication(outputJSON);} 
	            break; 
	        case "intervention_chemotherapy": 
	            {System.out.println("intervention_chemotherapy"); //condition_diagnosis
	            outputJSON=auto_fill_JSON_elements_intervention_chemotherapy(outputJSON);}
	            break; 
	        case "intervention_surgery": 
	            {System.out.println("intervention_surgery"); //condition_diagnosis
	            outputJSON=auto_fill_JSON_elements_intervention_surgery(outputJSON);}
	            
	            break;             
	        default: 
	            System.out.println("no match"); 
	    } 
		System.out.println("The criterion name is: "+criterion_name+" The output json fils is: " + outputJSON);
		return outputJSON;
	}
	
	
	public static String auto_fill_JSON_elements_intervention_surgery(String input_JSON) {
		String added_elements="";
		
		if(!input_JSON.contains("\"procedure_date_exact_year\":")) added_elements+="\"procedure_date_exact_year\":\"\",";
		if(!input_JSON.contains("\"procedure_date_exact_month\":")) added_elements+="\"procedure_date_exact_month\":\"\",";
		if(!input_JSON.contains("\"procedure_date_exact_day\":")) added_elements+="\"procedure_date_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"procedure_date_interval_start_year\":")) added_elements+="\"procedure_date_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"procedure_date_interval_start_month\":")) added_elements+="\"procedure_date_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"procedure_date_interval_start_day\":")) added_elements+="\"procedure_date_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"procedure_date_interval_end_year\":")) added_elements+=" \"procedure_date_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"procedure_date_interval_end_month\":")) added_elements+="\"procedure_date_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"procedure_date_interval_end_day\":")) added_elements+="\"procedure_date_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"procedure_date_until_year\":")) added_elements+="\"procedure_date_until_year\":\"\",";
		if(!input_JSON.contains("\"procedure_date_until_month\":")) added_elements+="\"procedure_date_until_month\":\"\",";
		if(!input_JSON.contains("\"procedure_date_until_day\":")) added_elements+="\"procedure_date_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	
	public static String auto_fill_JSON_elements_intervention_chemotherapy(String input_JSON) {
		String added_elements="";
		
		if(!input_JSON.contains("\"period_of_time_exact_year\":")) added_elements+="\"period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_exact_month\":")) added_elements+="\"period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_exact_day\":")) added_elements+="\"period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains(" \"period_of_time_interval_start_year\":")) added_elements+="\"period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_start_month\":")) added_elements+="\"period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_start_day\":")) added_elements+="\"period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains(" \"period_of_time_interval_end_year\":")) added_elements+="\"period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_end_month\":")) added_elements+="\"period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_end_day\":")) added_elements+=" \"period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"period_of_time_until_year\":")) added_elements+="\"period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_until_month\":")) added_elements+="\"period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_until_day\":")) added_elements+="\"period_of_time_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	public static String auto_fill_JSON_elements_intervention_medication(String input_JSON) {
		String added_elements="";
		if(!input_JSON.contains("\"dosage_amount_unit\":")) added_elements+="\"dosage_amount_unit\":\"\",";	
		if(!input_JSON.contains("\"dosage_amount_exact_value\":")) added_elements+="\"dosage_amount_exact_value\":\"\",";
		if(!input_JSON.contains("\"dosage_amount_range_min_value\":")) added_elements+="\"dosage_amount_range_min_value\":\"\",";
		if(!input_JSON.contains("\"dosage_amount_range_max_value\":")) added_elements+="\"dosage_amount_range_max_value\":\"\",";
		
		if(!input_JSON.contains("\"period_of_time_exact_year\":")) added_elements+="\"period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_exact_month\":")) added_elements+="\"period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_exact_day\":")) added_elements+="\"period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"period_of_time_interval_start_year\":")) added_elements+="\"period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_start_month\":")) added_elements+="\"period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_start_day\":")) added_elements+="\"period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"period_of_time_interval_end_year\":")) added_elements+="\"period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_end_month\":")) added_elements+="\"period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_end_day\":")) added_elements+="\"period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"period_of_time_until_year\":")) added_elements+="\"period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_until_month\":")) added_elements+="\"period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_until_day\":")) added_elements+="\"period_of_time_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	
	public static String auto_fill_JSON_elements_condition_symptom(String input_JSON) {
		String added_elements="";
		
		if(!input_JSON.contains("\"sign_date_exact_year\":")) added_elements+="\"sign_date_exact_year\":\"\",";
		if(!input_JSON.contains("\"sign_date_exact_month\":")) added_elements+="\"sign_date_exact_month\":\"\",";
		if(!input_JSON.contains("\"sign_date_exact_day\":")) added_elements+="\"sign_date_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"sign_date_interval_start_year\":")) added_elements+="\"sign_date_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"sign_date_interval_start_month\":")) added_elements+="\"sign_date_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"sign_date_interval_start_day\":")) added_elements+="\"sign_date_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"sign_date_interval_end_year\":")) added_elements+="\"sign_date_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"sign_date_interval_end_month\":")) added_elements+="\"sign_date_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"sign_date_interval_end_day\":")) added_elements+="\"sign_date_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"sign_date_until_year\":")) added_elements+="\"sign_date_until_year\":\"\",";
		if(!input_JSON.contains("\"sign_date_until_month\":")) added_elements+="\"sign_date_until_month\":\"\",";
		if(!input_JSON.contains("\"sign_date_until_day\":")) added_elements+="\"sign_date_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	private static String auto_fill_JSON_elements_condition_diagnosis(String input_JSON) {
		String added_elements="";
		
		if(!input_JSON.contains("\"date_exact_year\":")) added_elements+="\"date_exact_year\":\"\",";
		if(!input_JSON.contains("\"date_exact_month\":")) added_elements+="\"date_exact_month\":\"\",";
		if(!input_JSON.contains("\"date_exact_day\":")) added_elements+="\"date_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"date_interval_start_year\":")) added_elements+="\"date_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"date_interval_start_month\":")) added_elements+="\"date_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"date_interval_start_day\":")) added_elements+="\"date_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"date_interval_end_year\":")) added_elements+="\"date_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"date_interval_end_month\":")) added_elements+="\"date_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"date_interval_end_day\":")) added_elements+="\"date_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"date_until_year\":")) added_elements+="\"date_until_year\":\"\",";
		if(!input_JSON.contains("\"date_until_month\":")) added_elements+="\"date_until_month\":\"\",";
		if(!input_JSON.contains("\"date_until_day\":")) added_elements+="\"date_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	
	public static String auto_fill_JSON_elements_demographics_pregnancy(String input_JSON) {
		System.out.println("auto-fill");
		String added_elements="";
		if(!input_JSON.contains("\"outcome_ss_related\":")) added_elements+="\"outcome_ss_related\":\"\",";
		//if(!input_JSON.contains("\"outcome_coded_value\":")) added_elements+="\"outcome_coded_value\":\"\",";
		if(!input_JSON.contains("\"conception_exact_year\":")) added_elements+="\"conception_exact_year\":\"\",";
		if(!input_JSON.contains("\"conception_exact_month\":")) added_elements+="\"conception_exact_month\":\"\",";
		if(!input_JSON.contains(" \"conception_exact_day\":")) added_elements+="\"conception_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"conception_interval_start_year\":")) added_elements+="\"conception_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"conception_interval_start_month\":")) added_elements+="\"conception_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"conception_interval_start_day\":")) added_elements+=" \"conception_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"conception_interval_end_year\":")) added_elements+="\"conception_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"conception_interval_end_month\":")) added_elements+="\"conception_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"conception_interval_end_day\":")) added_elements+=" \"conception_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"conception_until_year\":")) added_elements+="\"conception_until_year\":\"\",";
		if(!input_JSON.contains("\"conception_until_month\":")) added_elements+="\"conception_until_month\":\"\",";
		if(!input_JSON.contains(" \"conception_until_day\":")) added_elements+="\"conception_until_day\":\"\",";
		
		if(!input_JSON.contains("\"outcome_exact_year\":")) added_elements+="\"outcome_exact_year\":\"\",";
		if(!input_JSON.contains("\"outcome_exact_month\":")) added_elements+="\"outcome_exact_month\":\"\",";
		if(!input_JSON.contains("\"outcome_exact_day\":")) added_elements+="\"outcome_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"outcome_interval_start_year\":")) added_elements+="\"outcome_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"outcome_interval_start_month\":")) added_elements+="\"outcome_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"outcome_interval_start_day\":")) added_elements+="\"outcome_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"outcome_interval_end_year\":")) added_elements+="\"outcome_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"outcome_interval_end_month\":")) added_elements+="\"outcome_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"outcome_interval_end_day\":")) added_elements+="\"outcome_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"outcome_until_year\":")) added_elements+=" \"outcome_until_year\":\"\",";
		if(!input_JSON.contains(" \"outcome_until_month\":")) added_elements+="\"outcome_until_month\":\"\",";
		if(!input_JSON.contains("\"outcome_until_day\":")) added_elements+="\"outcome_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	
	public static String auto_fill_JSON_elements_lifestyle_smoking(String input_JSON) {
		String added_elements="";
		if(!input_JSON.contains("\"amount_unit\":")) added_elements+="\"amount_unit\":\"\",";	
		if(!input_JSON.contains("\"amount_exact_value\":")) added_elements+="\"amount_exact_value\":\"\",";
		if(!input_JSON.contains("\"amount_range_min_value\":")) added_elements+="\"amount_range_min_value\":\"\",";
		if(!input_JSON.contains("\"amount_range_max_value\":")) added_elements+="\"amount_range_max_value\":\"\",";
		
		if(!input_JSON.contains("\"exact_year\":")) added_elements+="\"exact_year\":\"\",";
		if(!input_JSON.contains("\"exact_month\":")) added_elements+="\"exact_month\":\"\",";
		if(!input_JSON.contains("\"exact_day\":")) added_elements+="\"exact_day\":\"\",";
		
		if(!input_JSON.contains("\"interval_start_year\":")) added_elements+="\"interval_start_year\":\"\",";
		if(!input_JSON.contains("\"interval_start_month\":")) added_elements+="\"interval_start_month\":\"\",";
		if(!input_JSON.contains("\"interval_start_day\":")) added_elements+="\"interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"interval_end_year\":")) added_elements+="\"interval_end_year\":\"\",";
		if(!input_JSON.contains("\"interval_end_month\":")) added_elements+="\"interval_end_month\":\"\",";
		if(!input_JSON.contains("\"interval_end_day\":")) added_elements+="\"interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"until_year\":")) added_elements+="\"until_year\":\"\",";
		if(!input_JSON.contains("\"until_month\":")) added_elements+="\"until_month\":\"\",";
		if(!input_JSON.contains("\"until_day\":\"\"")) added_elements+="\"until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	private static String auto_fill_JSON_elements_examination_lab_test(String input_JSON) {
		String added_elements="";
		
		if(!input_JSON.contains("\"sample_period_of_time_exact_year\":")) added_elements+="\"sample_period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"sample_period_of_time_exact_month\":")) added_elements+="\"sample_period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"sample_period_of_time_exact_day\":")) added_elements+="\"sample_period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"sample_period_of_time_interval_start_year\":")) added_elements+="\"sample_period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"sample_period_of_time_interval_start_month\":")) added_elements+="\"sample_period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"sample_period_of_time_interval_start_day\":")) added_elements+="\"sample_period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"sample_period_of_time_interval_end_year\":")) added_elements+=" \"sample_period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"sample_period_of_time_interval_end_month\":")) added_elements+="\"sample_period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"sample_period_of_time_interval_end_day\":")) added_elements+="\"sample_period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"sample_period_of_time_until_year\":")) added_elements+="\"sample_period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"sample_period_of_time_until_month\":")) added_elements+="\"sample_period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"sample_period_of_time_until_day\":")) added_elements+="\"sample_period_of_time_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	private static String auto_fill_JSON_elements_examination_biopsy(String input_JSON) {
		String added_elements="";
/*		if(!input_JSON.contains("\"outcome_amount_unit\":")) added_elements+="\"outcome_amount_unit\":\"\",";
		if(!input_JSON.contains("\"outcome_amount_exact_value\":")) added_elements+="\"outcome_amount_exact_value\":\"\",";
		if(!input_JSON.contains("\"outcome_amount_range_min_value\":")) added_elements+="\"outcome_amount_range_min_value\":\"\",";
		if(!input_JSON.contains("\"outcome_amount_range_max_value\":")) added_elements+="\"outcome_amount_range_max_value\":\"\",";
		if(!input_JSON.contains("\"outcome_assessment\":")) added_elements+="\"outcome_assessment\":\"\",";
		if(!input_JSON.contains("\"outcome_check\":")) added_elements+="\"outcome_check\":\"\",";
		if(!input_JSON.contains("\"normal_range\":")) added_elements+="\"normal_range\":\"\",";*/

		if(!input_JSON.contains("\"biopsy_period_of_time_exact_year\":")) added_elements+="\"biopsy_period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"biopsy_period_of_time_exact_month\":")) added_elements+="\"biopsy_period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"biopsy_period_of_time_exact_day\":")) added_elements+="\"biopsy_period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"biopsy_period_of_time_interval_start_year\":")) added_elements+="\"biopsy_period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"biopsy_period_of_time_interval_start_month\":")) added_elements+="\"biopsy_period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"biopsy_period_of_time_interval_start_day\":")) added_elements+="\"biopsy_period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"biopsy_period_of_time_interval_end_year\":")) added_elements+=" \"biopsy_period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"biopsy_period_of_time_interval_end_month\":")) added_elements+="\"biopsy_period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"biopsy_period_of_time_interval_end_day\":")) added_elements+="\"biopsy_period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"biopsy_period_of_time_until_year\":")) added_elements+="\"biopsy_period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"biopsy_period_of_time_until_month\":")) added_elements+="\"biopsy_period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"biopsy_period_of_time_until_day\":")) added_elements+="\"biopsy_period_of_time_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	private static String auto_fill_JSON_elements_examination_medical_imaging_test(String input_JSON) {
		String added_elements="";
		if(!input_JSON.contains("\"test_period_of_time_exact_year\":")) added_elements+="\"test_period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"test_period_of_time_exact_month\":")) added_elements+="\"test_period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"test_period_of_time_exact_day\":")) added_elements+="\"test_period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"test_period_of_time_interval_start_year\":")) added_elements+="\"test_period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"test_period_of_time_interval_start_month\":")) added_elements+="\"test_period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"test_period_of_time_interval_start_day\":")) added_elements+="\"test_period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"test_period_of_time_interval_end_year\":")) added_elements+=" \"test_period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"test_period_of_time_interval_end_month\":")) added_elements+="\"test_period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"test_period_of_time_interval_end_day\":")) added_elements+="\"test_period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"test_period_of_time_until_year\":")) added_elements+="\"test_period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"test_period_of_time_until_month\":")) added_elements+="\"test_period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"test_period_of_time_until_day\":")) added_elements+="\"test_period_of_time_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	private static String auto_fill_JSON_elements_examination_questionnaire_score(String input_JSON) {
		String added_elements="";
		/*
		if(!input_JSON.contains("\"score_id\":")) added_elements+="\"score_id\":\"\",";
		if(!input_JSON.contains("\"value\":")) added_elements+="\"value\":\"\",";
		if(!input_JSON.contains("\"assessment\":")) added_elements+="\"assessment\":\"\",";
		if(!input_JSON.contains("\"normal_range_val1\":")) added_elements+="\"normal_range_val1\":\"\",";
		if(!input_JSON.contains("\"normal_range_val2\":")) added_elements+="\"normal_range_val2\":\"\",";
		if(!input_JSON.contains("\"other_term\":")) added_elements+="\"other_term\":\"\",";*/
		
		
		if(!input_JSON.contains("\"questionnaire_period_of_time_exact_year\":")) added_elements+="\"questionnaire_period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_exact_month\":")) added_elements+="\"questionnaire_period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_exact_day\":")) added_elements+="\"questionnaire_period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_start_year\":")) added_elements+="\"questionnaire_period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_start_month\":")) added_elements+="\"questionnaire_period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_start_day\":")) added_elements+="\"questionnaire_period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_end_year\":")) added_elements+=" \"questionnaire_period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_end_month\":")) added_elements+="\"questionnaire_period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_end_day\":")) added_elements+="\"questionnaire_period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"questionnaire_period_of_time_until_year\":")) added_elements+="\"questionnaire_period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_until_month\":")) added_elements+="\"questionnaire_period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_until_day\":")) added_elements+="\"questionnaire_period_of_time_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	private static String auto_fill_JSON_elements_examination_essdai_domain(String input_JSON) {
		String added_elements="";
		
		if(!input_JSON.contains("\"questionnaire_period_of_time_exact_year\":")) added_elements+="\"questionnaire_period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_exact_month\":")) added_elements+="\"questionnaire_period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_exact_day\":")) added_elements+="\"questionnaire_period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_start_year\":")) added_elements+="\"questionnaire_period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_start_month\":")) added_elements+="\"questionnaire_period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_start_day\":")) added_elements+="\"questionnaire_period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_end_year\":")) added_elements+=" \"questionnaire_period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_end_month\":")) added_elements+="\"questionnaire_period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_end_day\":")) added_elements+="\"questionnaire_period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"questionnaire_period_of_time_until_year\":")) added_elements+="\"questionnaire_period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_until_month\":")) added_elements+="\"questionnaire_period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_until_day\":")) added_elements+="\"questionnaire_period_of_time_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	private static String auto_fill_JSON_elements_examination_caci_condition(String input_JSON) {
		String added_elements="";
		
		if(!input_JSON.contains("\"questionnaire_period_of_time_exact_year\":")) added_elements+="\"questionnaire_period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_exact_month\":")) added_elements+="\"questionnaire_period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_exact_day\":")) added_elements+="\"questionnaire_period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_start_year\":")) added_elements+="\"questionnaire_period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_start_month\":")) added_elements+="\"questionnaire_period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_start_day\":")) added_elements+="\"questionnaire_period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_end_year\":")) added_elements+=" \"questionnaire_period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_end_month\":")) added_elements+="\"questionnaire_period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_interval_end_day\":")) added_elements+="\"questionnaire_period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"questionnaire_period_of_time_until_year\":")) added_elements+="\"questionnaire_period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_until_month\":")) added_elements+="\"questionnaire_period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"questionnaire_period_of_time_until_day\":")) added_elements+="\"questionnaire_period_of_time_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	private static String auto_fill_JSON_elements_other_healthcare_visit(String input_JSON) {
		String added_elements="";
		
		if(!input_JSON.contains("\"period_of_time_exact_year\":")) added_elements+="\"period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_exact_month\":")) added_elements+="\"period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_exact_day\":")) added_elements+="\"period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"period_of_time_interval_start_year\":")) added_elements+="\"period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_start_month\":")) added_elements+="\"period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_start_day\":")) added_elements+="\"period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"period_of_time_interval_end_year\":")) added_elements+=" \"period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_end_month\":")) added_elements+="\"period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_end_day\":")) added_elements+="\"period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"period_of_time_until_year\":")) added_elements+="\"period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_until_month\":")) added_elements+="\"period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_until_day\":")) added_elements+="\"period_of_time_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	private static String auto_fill_JSON_elements_other_family_history(String input_JSON) {
		String added_elements="";
		
		if(!input_JSON.contains("\"medical_condition\":")) added_elements+="\"medical_condition\":\"\",";
		if(!input_JSON.contains("\"relative_degree\":")) added_elements+="\"relative_degree\":\"\",";
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	private static String auto_fill_JSON_elements_other_clinical_trials(String input_JSON) {
		String added_elements="";
		
		if(!input_JSON.contains("\"period_of_time_exact_year\":")) added_elements+="\"period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_exact_month\":")) added_elements+="\"period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_exact_day\":")) added_elements+="\"period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"period_of_time_interval_start_year\":")) added_elements+="\"period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_start_month\":")) added_elements+="\"period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_start_day\":")) added_elements+="\"period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"period_of_time_interval_end_year\":")) added_elements+=" \"period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_end_month\":")) added_elements+="\"period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_interval_end_day\":")) added_elements+="\"period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"period_of_time_until_year\":")) added_elements+="\"period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"period_of_time_until_month\":")) added_elements+="\"period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"period_of_time_until_day\":")) added_elements+="\"period_of_time_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
	private static String auto_fill_JSON_elements_patient(String input_JSON) {
		String added_elements="";
		
		if(!input_JSON.contains("\"exact_age\":")) added_elements+="\"exact_age\":\"\",";
		if(!input_JSON.contains("\"min_age\":")) added_elements+="\"min_age\":\"\",";
		if(!input_JSON.contains("\"max_age\":")) added_elements+="\"max_age\":\"\",";
		
		if(!input_JSON.contains("\"exact_age_of_cohort_inclusion\":")) added_elements+="\"exact_age_of_cohort_inclusion\":\"\",";
		if(!input_JSON.contains("\"min_age_of_cohort_inclusion\":")) added_elements+="\"min_age_of_cohort_inclusion\":\"\",";
		if(!input_JSON.contains("\"max_age_of_cohort_inclusion\":")) added_elements+="\"max_age_of_cohort_inclusion\":\"\",";
		
		if(!input_JSON.contains("\"exact_age_of_diagnosis\":")) added_elements+="\"exact_age_of_diagnosis\":\"\",";
		if(!input_JSON.contains("\"min_age_of_diagnosis\":")) added_elements+="\"min_age_of_diagnosis\":\"\",";
		if(!input_JSON.contains("\"max_age_of_diagnosis\":")) added_elements+="\"max_age_of_diagnosis\":\"\",";
		
		if(!input_JSON.contains("\"exact_age_of_sign\":")) added_elements+="\"exact_age_of_sign\":\"\",";
		if(!input_JSON.contains("\"min_age_of_sign\":")) added_elements+="\"min_age_of_sign\":\"\",";
		if(!input_JSON.contains("\"max_age_of_sign\":")) added_elements+="\"max_age_of_sign\":\"\",";
		
		if(!input_JSON.contains("\"birth_period_of_time_exact_year\":")) added_elements+="\"birth_period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"birth_period_of_time_exact_month\":")) added_elements+="\"birth_period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"birth_period_of_time_exact_day\":")) added_elements+="\"birth_period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"birth_period_of_time_interval_start_year\":")) added_elements+="\"birth_period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"birth_period_of_time_interval_start_month\":")) added_elements+="\"birth_period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"birth_period_of_time_interval_start_day\":")) added_elements+="\"birth_period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"birth_period_of_time_interval_end_year\":")) added_elements+=" \"birth_period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"birth_period_of_time_interval_end_month\":")) added_elements+="\"birth_period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"birth_period_of_time_interval_end_day\":")) added_elements+="\"birth_period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"birth_period_of_time_until_year\":")) added_elements+="\"birth_period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"birth_period_of_time_until_month\":")) added_elements+="\"birth_period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"birth_period_of_time_until_day\":")) added_elements+="\"birth_period_of_time_until_day\":\"\",";
		
		
		if(!input_JSON.contains("\"symptoms_onset_period_of_time_exact_year\":")) added_elements+="\"symptoms_onset_period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"symptoms_onset_period_of_time_exact_month\":")) added_elements+="\"symptoms_onset_period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"symptoms_onset_period_of_time_exact_day\":")) added_elements+="\"symptoms_onset_period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"symptoms_onset_period_of_time_interval_start_year\":")) added_elements+="\"symptoms_onset_period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"symptoms_onset_period_of_time_interval_start_month\":")) added_elements+="\"symptoms_onset_period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"symptoms_onset_period_of_time_interval_start_day\":")) added_elements+="\"symptoms_onset_period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"symptoms_onset_period_of_time_interval_end_year\":")) added_elements+=" \"symptoms_onset_period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"symptoms_onset_period_of_time_interval_end_month\":")) added_elements+="\"symptoms_onset_period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"symptoms_onset_period_of_time_interval_end_day\":")) added_elements+="\"symptoms_onset_period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"symptoms_onset_period_of_time_until_year\":")) added_elements+="\"symptoms_onset_period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"symptoms_onset_period_of_time_until_month\":")) added_elements+="\"symptoms_onset_period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"symptoms_onset_period_of_time_until_day\":")) added_elements+="\"symptoms_onset_period_of_time_until_day\":\"\",";
		
		
		if(!input_JSON.contains("\"diagnosis_period_of_time_exact_year\":")) added_elements+="\"diagnosis_period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"diagnosis_period_of_time_exact_month\":")) added_elements+="\"diagnosis_period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"diagnosis_period_of_time_exact_day\":")) added_elements+="\"diagnosis_period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"diagnosis_period_of_time_interval_start_year\":")) added_elements+="\"diagnosis_period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"diagnosis_period_of_time_interval_start_month\":")) added_elements+="\"diagnosis_period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"diagnosis_period_of_time_interval_start_day\":")) added_elements+="\"diagnosis_period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"diagnosis_period_of_time_interval_end_year\":")) added_elements+=" \"diagnosis_period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"diagnosis_period_of_time_interval_end_month\":")) added_elements+="\"diagnosis_period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"diagnosis_period_of_time_interval_end_day\":")) added_elements+="\"diagnosis_period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"diagnosis_period_of_time_until_year\":")) added_elements+="\"diagnosis_period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"diagnosis_period_of_time_until_month\":")) added_elements+="\"diagnosis_period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"diagnosis_period_of_time_until_day\":")) added_elements+="\"diagnosis_period_of_time_until_day\":\"\",";
		
		
		
		if(!input_JSON.contains("\"cohort_inclusion_period_of_time_exact_year\":")) added_elements+="\"cohort_inclusion_period_of_time_exact_year\":\"\",";
		if(!input_JSON.contains("\"cohort_inclusion_period_of_time_exact_month\":")) added_elements+="\"cohort_inclusion_period_of_time_exact_month\":\"\",";
		if(!input_JSON.contains("\"cohort_inclusion_period_of_time_exact_day\":")) added_elements+="\"cohort_inclusion_period_of_time_exact_day\":\"\",";
		
		if(!input_JSON.contains("\"cohort_inclusion_period_of_time_interval_start_year\":")) added_elements+="\"cohort_inclusion_period_of_time_interval_start_year\":\"\",";
		if(!input_JSON.contains("\"cohort_inclusion_period_of_time_interval_start_month\":")) added_elements+="\"cohort_inclusion_period_of_time_interval_start_month\":\"\",";
		if(!input_JSON.contains("\"cohort_inclusion_eriod_of_time_interval_start_day\":")) added_elements+="\"cohort_inclusion_period_of_time_interval_start_day\":\"\",";
		
		if(!input_JSON.contains("\"cohort_inclusion_period_of_time_interval_end_year\":")) added_elements+=" \"cohort_inclusion_period_of_time_interval_end_year\":\"\",";
		if(!input_JSON.contains("\"cohort_inclusion_period_of_time_interval_end_month\":")) added_elements+="\"cohort_inclusion_period_of_time_interval_end_month\":\"\",";
		if(!input_JSON.contains("\"cohort_inclusion_period_of_time_interval_end_day\":")) added_elements+="\"cohort_inclusion_period_of_time_interval_end_day\":\"\",";
		
		if(!input_JSON.contains("\"cohort_inclusion_period_of_time_until_year\":")) added_elements+="\"cohort_inclusion_period_of_time_until_year\":\"\",";
		if(!input_JSON.contains("\"cohort_inclusion_period_of_time_until_month\":")) added_elements+="\"cohort_inclusion_period_of_time_until_month\":\"\",";
		if(!input_JSON.contains("\"cohort_inclusion_period_of_time_until_day\":")) added_elements+="\"cohort_inclusion_period_of_time_until_day\":\"\",";
		
		String result= input_JSON.replace("\"}", "\","+added_elements+"}");
		return result.replaceAll(",}", "}");
	}
	
}
