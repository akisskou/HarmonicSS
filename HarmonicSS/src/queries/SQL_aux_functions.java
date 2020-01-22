package queries;
public class SQL_aux_functions {

	public static String Make_OR_of_CODES(String field, String codes) {
		
		field = field.replace(" ", "");
		String result = "";
		String[] array_of_codes=codes.replace(" ","").split(",");
		result = "(" + field + "='" + array_of_codes[0] +"'";
		for(int i =1; i<array_of_codes.length; i++) {
			result += " OR " + field + "='" + array_of_codes[i] +"'";		
		}
		return result+")";
	}
	
	public static String Make_null_or_empty_values(Boolean mode, String field, String value) {
		if (mode) { 
			return "("+field+"='' OR "+field+" IS NULL) ";}	//I changed null=>NULL +field+"="+value+" OR "
		else { 
			return field+"="+value +" ";} // mode == false
	}//(dt_date.MONTH=5 OR dt_date.MONTH='' OR dt_date.MONTH IS null) 
	
	
	public static String Make_specific_date_query(Boolean mode, String obj_name_DATE_ID, String dt_date, String specific_year, String specific_month, String specific_day) {
		String query=""; 
		if(mode) { //UNdefined some Elements.   cond_symptom.OBSERVE_DATE_ID
			query += " AND (" +
					"("+obj_name_DATE_ID+"='' OR "+obj_name_DATE_ID+" IS NULL)"+
					" OR ("+obj_name_DATE_ID+"="+dt_date+".ID"+
					" AND( ("+dt_date+".YEAR='' OR "+dt_date+".YEAR IS NULL)";
			if(!specific_month.isEmpty()) query+=" OR ("+dt_date+".YEAR="+specific_year+" AND ("+dt_date+".MONTH='' OR "+dt_date+".MONTH IS NULL) ) ";
			if(!specific_day.isEmpty()) query+=" OR ("+dt_date+".YEAR="+specific_year+" AND "+dt_date+".MONTH="+specific_month+" AND("+dt_date+".DAY='' OR "+dt_date+".DAY IS NULL) ) ";
			query+=")))";	
		
		}
		else {  //Defined all elements mode==false
			query += " AND ("+  Make_null_or_empty_values(mode,obj_name_DATE_ID,dt_date+".ID");//obj_name_DATE_ID+" = dt_date.ID ";
			if(!specific_year.isEmpty()) query += "AND " + Make_null_or_empty_values(mode,dt_date+".YEAR",specific_year);//dt_date.YEAR = '" + crit_demo_pregnancy_obj.CONCEPTION_DATE_YEAR + "' ";
			if(!specific_month.isEmpty()) query += "AND "+ Make_null_or_empty_values(mode,dt_date+".MONTH",specific_month); //dt_date.MONTH = '" + crit_demo_pregnancy_obj.CONCEPTION_DATE_MONTH + "' ";
			if(!specific_day.isEmpty()) query += "AND " + Make_null_or_empty_values(mode,dt_date+".DAY",specific_day); //dt_date.DAY = '" + crit_demo_pregnancy_obj.CONCEPTION_DATE_DAY + "' ";
			query+=")";		
		}
	return query;
	}
	
	public static String Make_begin_end_period_query(Boolean mode, String obj_name_Period_ID, String dt_date1, String dt_date2, String begin_year, String begin_month, String begin_day, String end_year, String end_month,
			String end_day) { // obj_name_Period_ID = lifestyle_smoking.PERIOD_ID
		String 	query=""; //" AND ("+obj_name_Period_ID+" = dt_period_of_time.ID" +
				  //" AND dt_period_of_time.START_DATE_ID = "+dt_date1+".ID" + 
				  //" AND dt_period_of_time.END_DATE_ID = "+dt_date2+".ID)";
		if(mode) { //UNdefined some Elements.
			
			query+=" AND ("+Make_null_or_empty_values(mode,obj_name_Period_ID,"") +
				   " OR ("+obj_name_Period_ID+" = dt_period_of_time.ID AND "+Make_null_or_empty_values(mode,"dt_period_of_time.START_DATE_ID","")+" AND "+Make_null_or_empty_values(mode,"dt_period_of_time.END_DATE_ID","")+")" +
				   " OR ("+" dt_period_of_time.START_DATE_ID = "+dt_date1+".ID" +" AND dt_period_of_time.END_DATE_ID = "+dt_date2+".ID) AND"
				   + "(";

			if(!(begin_year).isEmpty()) {query+=" ("+Make_null_or_empty_values(mode,"dt_period_of_time.END_DATE_ID","")+" AND ("+dt_date1+".YEAR < "+begin_year+"))";}
			if(!(end_year).isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.END_DATE_ID","")+" AND ("+dt_date1+".YEAR < "+end_year+"))";}
			
			if(!(begin_year).isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.START_DATE_ID","")+" AND ("+begin_year+" < "+dt_date2+".YEAR))";}
			if(!(end_year).isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.START_DATE_ID","")+" AND ("+end_year+" < "+dt_date2+".YEAR))";}
			
			//new1
			if(!(begin_month).isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.END_DATE_ID","")+" AND ("+dt_date1+".YEAR = "+begin_year+") AND ("+dt_date1+".MONTH < "+begin_month+"))";}
			else {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.END_DATE_ID","")+" AND ("+dt_date1+".YEAR = "+begin_year+"))";}
			
			if(!(end_month).isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.END_DATE_ID","")+" AND ("+dt_date1+".YEAR = "+end_year+") AND ("+dt_date1+".MONTH < "+end_month+"))";}
			else {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.END_DATE_ID","")+" AND ("+dt_date1+".YEAR = "+end_year+"))";}
			
			if(!(begin_month).isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.START_DATE_ID","")+" AND ("+begin_year+" = "+dt_date2+".YEAR) AND ("+dt_date2+".MONTH > "+begin_month+"))";}
			else {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.START_DATE_ID","")+" AND ("+begin_year+" = "+dt_date2+".YEAR))";}
			
			if(!(end_month).isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.START_DATE_ID","")+" AND ("+end_year+" = "+dt_date2+".YEAR) AND ("+dt_date2+".MONTH > "+end_month+"))";}
			else {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.START_DATE_ID","")+" AND ("+end_year+" = "+dt_date2+".YEAR))";}
			
			
			//new2
			if(!(begin_day).isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.END_DATE_ID","")+" AND ("+dt_date1+".YEAR = "+begin_year+") AND ("+dt_date1+".MONTH = "+begin_month+") AND ("+dt_date1+".DAY <= "+begin_day+"))";}
			else if(!begin_month.isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.END_DATE_ID","")+" AND ("+dt_date1+".YEAR = "+begin_year+") AND ("+dt_date1+".MONTH = "+begin_month+"))";}
			
			if(!(end_day).isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.END_DATE_ID","")+" AND ("+dt_date1+".YEAR = "+end_year+") AND ("+dt_date1+".MONTH = "+end_month+") AND ("+dt_date1+".DAY <= "+end_day+"))";}
			else if(!end_month.isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.END_DATE_ID","")+" AND ("+dt_date1+".YEAR = "+end_year+") AND ("+dt_date1+".MONTH = "+end_month+"))";}
			
			if(!(begin_day).isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.START_DATE_ID","")+" AND ("+begin_year+" = "+dt_date2+".YEAR) AND ("+dt_date2+".MONTH = "+begin_month+") AND ("+dt_date2+".DAY <= "+begin_day+"))";}
			else if(!begin_month.isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.START_DATE_ID","")+" AND ("+begin_year+" = "+dt_date2+".YEAR) AND ("+dt_date2+".MONTH = "+begin_month+"))";}
			
			if(!(end_day).isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.START_DATE_ID","")+" AND ("+end_year+" = "+dt_date2+".YEAR) AND ("+dt_date2+".MONTH = "+end_month+") AND ("+dt_date2+".DAY <= "+end_day+"))";}
			else if(!end_month.isEmpty()) {query+=" OR ("+Make_null_or_empty_values(mode,"dt_period_of_time.START_DATE_ID","")+" AND ("+end_year+" = "+dt_date2+".YEAR) AND ("+dt_date2+".MONTH = "+end_month+"))";}
	
			query+="))";
		}
		else { //Defined all elements 
			
			query+= " AND ("+obj_name_Period_ID+" = dt_period_of_time.ID" +
					  " AND dt_period_of_time.START_DATE_ID = "+dt_date1+".ID" + 
					  " AND dt_period_of_time.END_DATE_ID = "+dt_date2+".ID)";
			
		query+=" AND (("+begin_year+" < dt_date1.YEAR AND dt_date1.YEAR < "+end_year+")" + 		
				" OR ("+begin_year+" > dt_date1.YEAR  AND dt_date2.YEAR > "+end_year+")" + 				
				" OR ("+begin_year+" < dt_date2.YEAR AND dt_date2.YEAR < "+end_year+")"  + 				 
				" OR ("+begin_year+" < dt_date1.YEAR AND dt_date2.YEAR < "+end_year+")";
					
		if(!end_month.isEmpty()) query += " OR ("+end_year+" = dt_date1.YEAR AND dt_date1.MONTH < "+end_month+")"; else {query += " OR ("+end_year+" = dt_date1.YEAR)";} //1
		if(!begin_month.isEmpty()) query += " OR ("+begin_year+" = dt_date1.YEAR AND dt_date1.MONTH > "+begin_month+")"; else {query += " OR ("+begin_year+" = dt_date1.YEAR)";} //2 not sure if needed.
		
		if(!end_month.isEmpty()) query +=" OR ("+end_year+" = dt_date2.YEAR AND dt_date2.MONTH > "+end_month+")"; else {query +=" OR ("+end_year+" = dt_date2.YEAR)";} //3 not sure if needed.
		if(!begin_month.isEmpty()) query += " OR ("+begin_year+" = dt_date2.YEAR AND dt_date2.MONTH > "+begin_month+")"; else {query += " OR ("+begin_year+" = dt_date2.YEAR)";}
		
		
		if(!(end_day).isEmpty()) query += " OR ("+end_year+" = dt_date1.YEAR AND dt_date1.MONTH = "+end_month+ " AND dt_date1.DAY <= "+end_day+")"; 
		else if(!end_month.isEmpty()) {query += " OR ("+end_year+" = dt_date1.YEAR AND dt_date1.MONTH = "+end_month+")";} 
		else {query += " OR ("+end_year+" = dt_date1.YEAR)";} //1
		
		if(!(begin_day).isEmpty()) query += " OR ("+begin_year+" = dt_date1.YEAR AND dt_date1.MONTH = "+begin_month+" AND dt_date1.DAY >= "+begin_day+")"; 
		else if(!begin_month.isEmpty()) {query += " OR ("+begin_year+" = dt_date1.YEAR AND dt_date1.MONTH = "+begin_month+")";} 
		else { query += " OR ("+begin_year+" = dt_date1.YEAR)";} //2 not sure if needed.
		
		if(!(end_day).isEmpty()) query += " OR ("+end_year+" = dt_date2.YEAR AND dt_date2.MONTH = "+end_month+" AND dt_date2.DAY >= "+end_day+")"; else if(!end_month.isEmpty()) {query += " OR ("+end_year+" = dt_date2.YEAR AND dt_date2.MONTH = "+end_month+")";} else {query += " OR ("+end_year+" = dt_date2.YEAR)";} //3 not sure if needed.
		if(!(begin_day).isEmpty()) query += " OR ("+begin_year+" = dt_date2.YEAR AND dt_date2.MONTH = "+begin_month+" AND dt_date2.DAY >= "+begin_day+")"; else if(!begin_month.isEmpty()) {query += " OR ("+begin_year+" = dt_date2.YEAR AND dt_date2.MONTH = "+begin_month+")";} else {query += " OR ("+begin_year+" = dt_date2.YEAR)";} //4
		
		query+=")";
		}
		return query;
	}
	
	
	public static String Make_begin_end_date_query(Boolean mode, String obj_name_DATE_ID, String dt_date, String begin_year, String begin_month, String begin_day, String end_year, String end_month,
			String end_day) { 
		String query="";
		if(mode) { //UNdefined some Elements.
			query += " AND (("+obj_name_DATE_ID+" IS NULL) OR " +
		"("+obj_name_DATE_ID+" = "+dt_date+".ID " + 
		"AND (" + 
			"("+dt_date+".YEAR ='' OR "+dt_date+".YEAR IS NULL) OR  "+
		    "("+dt_date+".YEAR = "+begin_year+" AND  ("+dt_date+".MONTH='' OR "+dt_date+".MONTH IS NULL) )  OR "+
		    "("+dt_date+".YEAR = "+end_year+" AND ("+dt_date+".MONTH='' OR "+dt_date+".MONTH IS NULL) ) " +
		")))";

/*			query += " AND (("+obj_name_DATE_ID+" IS NULL) OR " +
					"("+obj_name_DATE_ID+" = "+dt_date+".ID " + 
					"AND (" + 
						"("+dt_date+".YEAR ='' OR "+dt_date+".YEAR IS NULL) OR  "+
					
						"("+dt_date+".YEAR > "+begin_year+" AND "+dt_date+".YEAR < " + end_year  + ") OR "+
						
					    "("+dt_date+".YEAR = "+begin_year+" AND  ("+dt_date+".MONTH='' OR "+dt_date+".MONTH IS NULL) )  OR "+
					    "("+dt_date+".YEAR = "+end_year+" AND ("+dt_date+".MONTH='' OR "+dt_date+".MONTH IS NULL) ) " +
					")))";*/
			

			//query += " AND demo_pregnancy_data.CONCEPTION_DATE_ID = dt_date.ID " +" AND ((dt_date.YEAR ='' OR dt_date.YEAR IS NULL)";
			//query += " OR( (dt_date.YEAR = "+begin_year+" OR dt_date.YEAR = "+end_year+") AND (dt_date.MONTH='' OR dt_date.MONTH IS NULL)";
			
			//if(!(end_month).isEmpty())query += " OR((dt_date.YEAR = "+begin_year+" AND dt_date.MONTH = "+begin_month+") OR (dt_date.YEAR = "+end_year+" AND dt_date.MONTH = "+end_month+") ) "
			//				+ " AND dt_date.DAY='' OR dt_date.DAY IS NULL) )";
			}
		
		else { //ALLdefined 
			//System.out.println("");
			query += " AND "+obj_name_DATE_ID+" = "+dt_date+".ID AND "+obj_name_DATE_ID+"  IS NOT NULL AND ((" +
					begin_year + "< "+dt_date+".YEAR AND "+dt_date+".YEAR < " + end_year+")";
			if(!begin_month.isEmpty()) query += " OR ("+dt_date+".YEAR = " + begin_year + " AND "+dt_date+".MONTH > " + begin_month +")"; 
				else{ query += " OR "+dt_date+".YEAR = " + begin_year;}
			if(!end_month.isEmpty()) query += " AND ("+dt_date+".YEAR = " + end_year + " AND "+dt_date+".MONTH < " + end_month +")"; 
				else{ query += " OR "+dt_date+".YEAR = " + begin_year;}
		
			if(!begin_day.isEmpty()) {query += " OR ("+dt_date+".YEAR = "+ begin_year +" AND "+dt_date+".MONTH = "+begin_month+" AND "+dt_date+".DAY >= " +begin_day+")";} 
				else if(!begin_month.isEmpty()) {query += " OR ("+dt_date+".YEAR = "+ begin_year +" AND "+dt_date+".MONTH = "+begin_month+")";}
				else {query += " OR ("+dt_date+".YEAR = "+ begin_year+")";}
			if(!end_day.isEmpty()) {query += " OR ("+dt_date+".YEAR = "+ end_year +" AND "+dt_date+".MONTH = "+  end_month + " AND "+dt_date+".DAY <= "+end_day+")";} 
				else if(!begin_month.isEmpty()) {query += " OR ("+dt_date+".YEAR = "+ begin_year +" AND "+dt_date+".MONTH = "+begin_month+")";}		
				else {query += " OR ("+dt_date+".YEAR = "+ begin_year +")";}
			query +=")";
		}
		
		return query;
	}
	public static String Make_begin_end_date_query_period_OR(Boolean mode, String obj_name_DATE_ID, String dt_date, String begin_year, String begin_month, String begin_day, String end_year, String end_month,
			String end_day) { 
		String query="";
		if(mode) { //UNdefined some Elements.
			query += " OR (("+obj_name_DATE_ID+" IS NULL) OR " +
					"("+obj_name_DATE_ID+" = "+dt_date+".ID " + 
					"AND (" + 
						"("+dt_date+".YEAR ='' OR "+dt_date+".YEAR IS NULL) OR  "+
					    "("+dt_date+".YEAR = "+begin_year+" AND  ("+dt_date+".MONTH='' OR "+dt_date+".MONTH IS NULL) )  OR "+
					    "("+dt_date+".YEAR = "+end_year+" AND ("+dt_date+".MONTH='' OR "+dt_date+".MONTH IS NULL) ) " +
					")))";
			
			
			//query += " AND demo_pregnancy_data.CONCEPTION_DATE_ID = dt_date.ID " +" AND ((dt_date.YEAR ='' OR dt_date.YEAR IS NULL)";
			//query += " OR( (dt_date.YEAR = "+begin_year+" OR dt_date.YEAR = "+end_year+") AND (dt_date.MONTH='' OR dt_date.MONTH IS NULL)";
			
			//if(!(end_month).isEmpty())query += " OR((dt_date.YEAR = "+begin_year+" AND dt_date.MONTH = "+begin_month+") OR (dt_date.YEAR = "+end_year+" AND dt_date.MONTH = "+end_month+") ) "
			//				+ " AND dt_date.DAY='' OR dt_date.DAY IS NULL) )";
			}
		
		else { //ALLdefined 
			query += " OR "+obj_name_DATE_ID+" = "+dt_date+".ID AND "+obj_name_DATE_ID+"  IS NOT NULL AND ((" +
					begin_year + "< "+dt_date+".YEAR AND "+dt_date+".YEAR < " + end_year+")";
			if(!begin_month.isEmpty()) query += " OR ("+dt_date+".YEAR = " + begin_year + " AND "+dt_date+".MONTH > " + begin_month +")"; 
				else{ query += " OR "+dt_date+".YEAR = " + begin_year;}
			if(!end_month.isEmpty()) query += " AND ("+dt_date+".YEAR = " + end_year + " AND "+dt_date+".MONTH < " + end_month +")"; 
				else{ query += " OR "+dt_date+".YEAR = " + begin_year;}
		
			if(!begin_day.isEmpty()) {query += " OR ("+dt_date+".YEAR = "+ begin_year +" AND "+dt_date+".MONTH = "+begin_month+" AND "+dt_date+".DAY >= " +begin_day+")";} 
				else if(!begin_month.isEmpty()) {query += " OR ("+dt_date+".YEAR = "+ begin_year +" AND "+dt_date+".MONTH = "+begin_month+")";}
				else {query += " OR ("+dt_date+".YEAR = "+ begin_year+")";}
			if(!end_day.isEmpty()) {query += " OR ("+dt_date+".YEAR = "+ end_year +" AND "+dt_date+".MONTH = "+  end_month + " AND "+dt_date+".DAY <= "+end_day+")";} 
				else if(!begin_month.isEmpty()) {query += " OR ("+dt_date+".YEAR = "+ begin_year +" AND "+dt_date+".MONTH = "+begin_month+")";}		
				else {query += " OR ("+dt_date+".YEAR = "+ begin_year +")";}
			query +=")";
		}
		
		return query;
	}
	
	
}
