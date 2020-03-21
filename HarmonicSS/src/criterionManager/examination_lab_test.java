package criterionManager;

public class examination_lab_test extends Criterion{
	
	public String test_id=""; //voc_lab_test
	public String outcome_amount_unit = "";		//dt_amount		
	public String outcome_amount_exact_value = "";	//dt_amount	
	public String outcome_amount_range_min_value = "";	//dt_amount	
	public String outcome_amount_range_max_value = "";	//dt_amount	
	public String outcome_assessment = "";		//voc_assessment Boolean
	public String outcome_term = ""; 			//given by the user
	public String normal_range_value = "";			//dt_amout_range
	public String sample_period_of_time_exact_year = "";		//dt_date				
	public String sample_period_of_time_exact_month = "";		//dt_date		
	public String sample_period_of_time_exact_day = "";			//dt_date				
	public String sample_period_of_time_interval_start_year = "";	//dt_date				
	public String sample_period_of_time_interval_start_month = "";			//dt_date	
	public String sample_period_of_time_interval_start_day = "";	//dt_date				
	public String sample_period_of_time_interval_end_year = "";		//dt_date			
	public String sample_period_of_time_interval_end_month = "";	//dt_date				
	public String sample_period_of_time_interval_end_day = "";		//dt_date			
	public String sample_period_of_time_until_year = "";			//dt_date
	public String sample_period_of_time_until_month = "";			//dt_date
	public String sample_period_of_time_until_day = "";				//dt_date
	public String count = "";
	
	
	public examination_lab_test(String criterion_name, String test_id, String outcome_amount_unit, String outcome_amount_exact_value,
			String outcome_amount_range_min_value, String outcome_amount_range_max_value, String outcome_assessment,
			String outcome_term, String normal_range_value, String sample_period_of_time_exact_year,
			String sample_period_of_time_exact_month, String sample_period_of_time_exact_day,
			String sample_period_of_time_interval_start_year, String sample_period_of_time_interval_start_month,
			String sample_period_of_time_interval_start_day, String sample_period_of_time_interval_end_year,
			String sample_period_of_time_interval_end_month, String sample_period_of_time_interval_end_day,
			String sample_period_of_time_until_year, String sample_period_of_time_until_month,
			String sample_period_of_time_until_day, String count) {
		super(criterion_name);
		this.test_id = test_id;
		this.outcome_amount_unit = outcome_amount_unit;
		this.outcome_amount_exact_value = outcome_amount_exact_value;
		this.outcome_amount_range_min_value = outcome_amount_range_min_value;
		this.outcome_amount_range_max_value = outcome_amount_range_max_value;
		this.outcome_assessment = outcome_assessment;
		this.outcome_term = outcome_term;
		this.normal_range_value = normal_range_value;
		this.sample_period_of_time_exact_year = sample_period_of_time_exact_year;
		this.sample_period_of_time_exact_month = sample_period_of_time_exact_month;
		this.sample_period_of_time_exact_day = sample_period_of_time_exact_day;
		this.sample_period_of_time_interval_start_year = sample_period_of_time_interval_start_year;
		this.sample_period_of_time_interval_start_month = sample_period_of_time_interval_start_month;
		this.sample_period_of_time_interval_start_day = sample_period_of_time_interval_start_day;
		this.sample_period_of_time_interval_end_year = sample_period_of_time_interval_end_year;
		this.sample_period_of_time_interval_end_month = sample_period_of_time_interval_end_month;
		this.sample_period_of_time_interval_end_day = sample_period_of_time_interval_end_day;
		this.sample_period_of_time_until_year = sample_period_of_time_until_year;
		this.sample_period_of_time_until_month = sample_period_of_time_until_month;
		this.sample_period_of_time_until_day = sample_period_of_time_until_day;
		this.count = count;
	}

	public examination_lab_test() {	
		super("");
		this.test_id = "";
		this.outcome_amount_unit = "";
		this.outcome_amount_exact_value = "";
		this.outcome_amount_range_min_value = "";
		this.outcome_amount_range_max_value = "";
		this.outcome_assessment = "";
		this.outcome_term = "";
		this.normal_range_value = "";
		this.sample_period_of_time_exact_year = "";
		this.sample_period_of_time_exact_month = "";
		this.sample_period_of_time_exact_day = "";
		this.sample_period_of_time_interval_start_year = "";
		this.sample_period_of_time_interval_start_month = "";
		this.sample_period_of_time_interval_start_day = "";
		this.sample_period_of_time_interval_end_year = "";
		this.sample_period_of_time_interval_end_month = "";
		this.sample_period_of_time_interval_end_day = "";
		this.sample_period_of_time_until_year = "";
		this.sample_period_of_time_until_month = "";
		this.sample_period_of_time_until_day = "";
		this.count = "";
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

	public String getOutcome_assessment() {
		return outcome_assessment;
	}

	public void setOutcome_assessment(String outcome_assessment) {
		this.outcome_assessment = outcome_assessment;
	}

	public String getOutcome_term() {
		return outcome_term;
	}

	public void setOutcome_term(String outcome_term) {
		this.outcome_term = outcome_term;
	}

	public String getNormal_range_value() {
		return normal_range_value;
	}

	public void setNormal_range_value(String normal_range_value) {
		this.normal_range_value = normal_range_value;
	}

	public String getSample_period_of_time_exact_year() {
		return sample_period_of_time_exact_year;
	}

	public void setSample_period_of_time_exact_year(String sample_period_of_time_exact_year) {
		this.sample_period_of_time_exact_year = sample_period_of_time_exact_year;
	}

	public String getSample_period_of_time_exact_month() {
		return sample_period_of_time_exact_month;
	}

	public void setSample_period_of_time_exact_month(String sample_period_of_time_exact_month) {
		this.sample_period_of_time_exact_month = sample_period_of_time_exact_month;
	}

	public String getSample_period_of_time_exact_day() {
		return sample_period_of_time_exact_day;
	}

	public void setSample_period_of_time_exact_day(String sample_period_of_time_exact_day) {
		this.sample_period_of_time_exact_day = sample_period_of_time_exact_day;
	}

	public String getSample_period_of_time_interval_start_year() {
		return sample_period_of_time_interval_start_year;
	}

	public void setSample_period_of_time_interval_start_year(String sample_period_of_time_interval_start_year) {
		this.sample_period_of_time_interval_start_year = sample_period_of_time_interval_start_year;
	}

	public String getSample_period_of_time_interval_start_month() {
		return sample_period_of_time_interval_start_month;
	}

	public void setSample_period_of_time_interval_start_month(String sample_period_of_time_interval_start_month) {
		this.sample_period_of_time_interval_start_month = sample_period_of_time_interval_start_month;
	}

	public String getSample_period_of_time_interval_start_day() {
		return sample_period_of_time_interval_start_day;
	}

	public void setSample_period_of_time_interval_start_day(String sample_period_of_time_interval_start_day) {
		this.sample_period_of_time_interval_start_day = sample_period_of_time_interval_start_day;
	}

	public String getSample_period_of_time_interval_end_year() {
		return sample_period_of_time_interval_end_year;
	}

	public void setSample_period_of_time_interval_end_year(String sample_period_of_time_interval_end_year) {
		this.sample_period_of_time_interval_end_year = sample_period_of_time_interval_end_year;
	}

	public String getSample_period_of_time_interval_end_month() {
		return sample_period_of_time_interval_end_month;
	}

	public void setSample_period_of_time_interval_end_month(String sample_period_of_time_interval_end_month) {
		this.sample_period_of_time_interval_end_month = sample_period_of_time_interval_end_month;
	}

	public String getSample_period_of_time_interval_end_day() {
		return sample_period_of_time_interval_end_day;
	}

	public void setSample_period_of_time_interval_end_day(String sample_period_of_time_interval_end_day) {
		this.sample_period_of_time_interval_end_day = sample_period_of_time_interval_end_day;
	}

	public String getSample_period_of_time_until_year() {
		return sample_period_of_time_until_year;
	}

	public void setSample_period_of_time_until_year(String sample_period_of_time_until_year) {
		this.sample_period_of_time_until_year = sample_period_of_time_until_year;
	}

	public String getSample_period_of_time_until_month() {
		return sample_period_of_time_until_month;
	}

	public void setSample_period_of_time_until_month(String sample_period_of_time_until_month) {
		this.sample_period_of_time_until_month = sample_period_of_time_until_month;
	}

	public String getSample_period_of_time_until_day() {
		return sample_period_of_time_until_day;
	}

	public void setSample_period_of_time_until_day(String sample_period_of_time_until_day) {
		this.sample_period_of_time_until_day = sample_period_of_time_until_day;
	}
	
	public String getCount() {
		return count;
	}
	
	public void setCount(String count) {
		this.count = count;
	}
}
