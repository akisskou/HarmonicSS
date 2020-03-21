package criterionManager;

public class examination_biopsy extends Criterion{

	public String biopsy_type="";
	public String test_id="";
	
	public String outcome_amount_unit = "";		//dt_amount		
	public String outcome_amount_exact_value = "";	//dt_amount	
	public String outcome_amount_range_min_value = "";	//dt_amount	
	public String outcome_amount_range_max_value = "";	//dt_amount	
	
	
	public String  assessment ="";		//voc_assessment Boolean
	public String  outcome_check =""; 			//given by the user
	
	public String  normal_range_value ="";			//dt_amout_range
	//public String  normal_range_val2 ="";			//dt_amout_range
	
	
	public String biopsy_period_of_time_exact_year = "";		//dt_date				
	public String biopsy_period_of_time_exact_month = "";		//dt_date		
	public String biopsy_period_of_time_exact_day = "";			//dt_date				
	
	public String biopsy_period_of_time_interval_start_year = "";	//dt_date				
	public String biopsy_period_of_time_interval_start_month = "";			//dt_date	
	public String biopsy_period_of_time_interval_start_day = "";	//dt_date				
	
	public String biopsy_period_of_time_interval_end_year = "";		//dt_date			
	public String biopsy_period_of_time_interval_end_month = "";	//dt_date				
	public String biopsy_period_of_time_interval_end_day = "";		//dt_date			
	
	public String biopsy_period_of_time_until_year = "";			//dt_date
	public String biopsy_period_of_time_until_month = "";			//dt_date
	public String biopsy_period_of_time_until_day = "";				//dt_date
	
	public String count = "";
	
	
	public examination_biopsy(String criterion_name, String biopsy_id, String test_id, String outcome_amount_unit,
			String outcome_amount_exact_value, String outcome_amount_range_min_value,
			String outcome_amount_range_max_value, String assessment, String outcome_check, String normal_range_val1, 
			String biopsy_period_of_time_exact_year, String biopsy_period_of_time_exact_month,
			String biopsy_period_of_time_exact_day, String biopsy_period_of_time_interval_start_year,
			String biopsy_period_of_time_interval_start_month, String biopsy_period_of_time_interval_start_day,
			String biopsy_period_of_time_interval_end_year, String biopsy_period_of_time_interval_end_month,
			String biopsy_period_of_time_interval_end_day, String biopsy_period_of_time_until_year,
			String biopsy_period_of_time_until_month, String biopsy_period_of_time_until_day, String count) {
		super(criterion_name);
		this.biopsy_type = biopsy_id;
		this.test_id = test_id;
		this.outcome_amount_unit = outcome_amount_unit;
		this.outcome_amount_exact_value = outcome_amount_exact_value;
		this.outcome_amount_range_min_value = outcome_amount_range_min_value;
		this.outcome_amount_range_max_value = outcome_amount_range_max_value;
		this.assessment = assessment;
		this.outcome_check = outcome_check;
		this.normal_range_value = normal_range_val1;
		//this.normal_range_val2 = normal_range_val2;
		this.biopsy_period_of_time_exact_year = biopsy_period_of_time_exact_year;
		this.biopsy_period_of_time_exact_month = biopsy_period_of_time_exact_month;
		this.biopsy_period_of_time_exact_day = biopsy_period_of_time_exact_day;
		this.biopsy_period_of_time_interval_start_year = biopsy_period_of_time_interval_start_year;
		this.biopsy_period_of_time_interval_start_month = biopsy_period_of_time_interval_start_month;
		this.biopsy_period_of_time_interval_start_day = biopsy_period_of_time_interval_start_day;
		this.biopsy_period_of_time_interval_end_year = biopsy_period_of_time_interval_end_year;
		this.biopsy_period_of_time_interval_end_month = biopsy_period_of_time_interval_end_month;
		this.biopsy_period_of_time_interval_end_day = biopsy_period_of_time_interval_end_day;
		this.biopsy_period_of_time_until_year = biopsy_period_of_time_until_year;
		this.biopsy_period_of_time_until_month = biopsy_period_of_time_until_month;
		this.biopsy_period_of_time_until_day = biopsy_period_of_time_until_day;
		this.count = count;
	}
	
	public examination_biopsy() {
		super("");
		this.biopsy_type = "";
		this.test_id = "";
		this.outcome_amount_unit = "";
		this.outcome_amount_exact_value = "";
		this.outcome_amount_range_min_value = "";
		this.outcome_amount_range_max_value = "";
		this.assessment = "";
		this.outcome_check = "";
		this.normal_range_value = "";
		this.biopsy_period_of_time_exact_year = "";
		this.biopsy_period_of_time_exact_month = "";
		this.biopsy_period_of_time_exact_day = "";
		this.biopsy_period_of_time_interval_start_year = "";
		this.biopsy_period_of_time_interval_start_month = "";
		this.biopsy_period_of_time_interval_start_day = "";
		this.biopsy_period_of_time_interval_end_year = "";
		this.biopsy_period_of_time_interval_end_month = "";
		this.biopsy_period_of_time_interval_end_day = "";
		this.biopsy_period_of_time_until_year = "";
		this.biopsy_period_of_time_until_month = "";
		this.biopsy_period_of_time_until_day = "";
		this.count = "";
	}
	
	public String getBiopsy_type() {
		return biopsy_type;
	}
	public void setBiopsy_type(String biopsy_id) {
		this.biopsy_type = biopsy_id;
	}
	public String getTest_id() {
		return test_id;
	}
	public void setTest_id(String test_id) {
		this.test_id = test_id;
	}
	public String getOutcome_amount_unit() {
		return outcome_amount_unit;
	}
	public void setOutcome_amount_unit(String outcome_amount_unit) {
		this.outcome_amount_unit = outcome_amount_unit;
	}
	public String getOutcome_amount_exact_value() {
		return outcome_amount_exact_value;
	}
	public void setOutcome_amount_exact_value(String outcome_amount_exact_value) {
		this.outcome_amount_exact_value = outcome_amount_exact_value;
	}
	public String getOutcome_amount_range_min_value() {
		return outcome_amount_range_min_value;
	}
	public void setOutcome_amount_range_min_value(String outcome_amount_range_min_value) {
		this.outcome_amount_range_min_value = outcome_amount_range_min_value;
	}
	public String getOutcome_amount_range_max_value() {
		return outcome_amount_range_max_value;
	}
	public void setOutcome_amount_range_max_value(String outcome_amount_range_max_value) {
		this.outcome_amount_range_max_value = outcome_amount_range_max_value;
	}
	public String getAssessment() {
		return assessment;
	}
	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}
	public String getOutcome_check() {
		return outcome_check;
	}
	public void setOutcome_check(String outcome_check) {
		this.outcome_check = outcome_check;
	}
	public String getNormal_range_value() {
		return normal_range_value;
	}
	public void setNormal_range_value(String normal_range_value) {
		this.normal_range_value = normal_range_value;
	}
	
/*	public String getNormal_range_val2() {
		return normal_range_val2;
	}
	public void setNormal_range_val2(String normal_range_val2) {
		this.normal_range_val2 = normal_range_val2;
	}*/
	
	public String getBiopsy_period_of_time_exact_year() {
		return biopsy_period_of_time_exact_year;
	}
	public void setBiopsy_period_of_time_exact_year(String biopsy_period_of_time_exact_year) {
		this.biopsy_period_of_time_exact_year = biopsy_period_of_time_exact_year;
	}
	public String getBiopsy_period_of_time_exact_month() {
		return biopsy_period_of_time_exact_month;
	}
	public void setBiopsy_period_of_time_exact_month(String biopsy_period_of_time_exact_month) {
		this.biopsy_period_of_time_exact_month = biopsy_period_of_time_exact_month;
	}
	public String getBiopsy_period_of_time_exact_day() {
		return biopsy_period_of_time_exact_day;
	}
	public void setBiopsy_period_of_time_exact_day(String biopsy_period_of_time_exact_day) {
		this.biopsy_period_of_time_exact_day = biopsy_period_of_time_exact_day;
	}
	public String getBiopsy_period_of_time_interval_start_year() {
		return biopsy_period_of_time_interval_start_year;
	}
	public void setBiopsy_period_of_time_interval_start_year(String biopsy_period_of_time_interval_start_year) {
		this.biopsy_period_of_time_interval_start_year = biopsy_period_of_time_interval_start_year;
	}
	public String getBiopsy_period_of_time_interval_start_month() {
		return biopsy_period_of_time_interval_start_month;
	}
	public void setBiopsy_period_of_time_interval_start_month(String biopsy_period_of_time_interval_start_month) {
		this.biopsy_period_of_time_interval_start_month = biopsy_period_of_time_interval_start_month;
	}
	public String getBiopsy_period_of_time_interval_start_day() {
		return biopsy_period_of_time_interval_start_day;
	}
	public void setBiopsy_period_of_time_interval_start_day(String biopsy_period_of_time_interval_start_day) {
		this.biopsy_period_of_time_interval_start_day = biopsy_period_of_time_interval_start_day;
	}
	public String getBiopsy_period_of_time_interval_end_year() {
		return biopsy_period_of_time_interval_end_year;
	}
	public void setBiopsy_period_of_time_interval_end_year(String biopsy_period_of_time_interval_end_year) {
		this.biopsy_period_of_time_interval_end_year = biopsy_period_of_time_interval_end_year;
	}
	public String getBiopsy_period_of_time_interval_end_month() {
		return biopsy_period_of_time_interval_end_month;
	}
	public void setBiopsy_period_of_time_interval_end_month(String biopsy_period_of_time_interval_end_month) {
		this.biopsy_period_of_time_interval_end_month = biopsy_period_of_time_interval_end_month;
	}
	public String getBiopsy_period_of_time_interval_end_day() {
		return biopsy_period_of_time_interval_end_day;
	}
	public void setBiopsy_period_of_time_interval_end_day(String biopsy_period_of_time_interval_end_day) {
		this.biopsy_period_of_time_interval_end_day = biopsy_period_of_time_interval_end_day;
	}
	public String getBiopsy_period_of_time_until_year() {
		return biopsy_period_of_time_until_year;
	}
	public void setBiopsy_period_of_time_until_year(String biopsy_period_of_time_until_year) {
		this.biopsy_period_of_time_until_year = biopsy_period_of_time_until_year;
	}
	public String getBiopsy_period_of_time_until_month() {
		return biopsy_period_of_time_until_month;
	}
	public void setBiopsy_period_of_time_until_month(String biopsy_period_of_time_until_month) {
		this.biopsy_period_of_time_until_month = biopsy_period_of_time_until_month;
	}
	public String getBiopsy_period_of_time_until_day() {
		return biopsy_period_of_time_until_day;
	}
	public void setBiopsy_period_of_time_until_day(String biopsy_period_of_time_until_day) {
		this.biopsy_period_of_time_until_day = biopsy_period_of_time_until_day;
	}
	public String getCount() {
		return count;
	}
	
	public void setCount(String count) {
		this.count = count;
	}
}
