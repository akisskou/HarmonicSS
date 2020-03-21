package criterionManager;

public class examination_medical_imaging_test extends Criterion{

	public String test_id="";
	public String assessment ="";
	
	public String test_period_of_time_exact_year = "";		//dt_date				
	public String test_period_of_time_exact_month = "";		//dt_date		
	public String test_period_of_time_exact_day = "";			//dt_date				
	
	public String test_period_of_time_interval_start_year = "";	//dt_date				
	public String test_period_of_time_interval_start_month = "";			//dt_date	
	public String test_period_of_time_interval_start_day = "";	//dt_date				
	
	public String test_period_of_time_interval_end_year = "";		//dt_date			
	public String test_period_of_time_interval_end_month = "";	//dt_date				
	public String test_period_of_time_interval_end_day = "";		//dt_date			
	
	public String test_period_of_time_until_year = "";			//dt_date
	public String test_period_of_time_until_month = "";			//dt_date
	public String test_period_of_time_until_day = "";			//dt_date
	
	public String count = "";
	
	
	public examination_medical_imaging_test(String criterion_name, String test_id, String assessment,
			String test_period_of_time_exact_year, String test_period_of_time_exact_month,
			String test_period_of_time_exact_day, String test_period_of_time_interval_start_year,
			String test_period_of_time_interval_start_month, String test_period_of_time_interval_start_day,
			String test_period_of_time_interval_end_year, String test_period_of_time_interval_end_month,
			String test_period_of_time_interval_end_day, String test_period_of_time_until_year,
			String test_period_of_time_until_month, String test_period_of_time_until_day, String count) {
		super(criterion_name);
		this.test_id = test_id;
		this.assessment = assessment;
		this.test_period_of_time_exact_year = test_period_of_time_exact_year;
		this.test_period_of_time_exact_month = test_period_of_time_exact_month;
		this.test_period_of_time_exact_day = test_period_of_time_exact_day;
		this.test_period_of_time_interval_start_year = test_period_of_time_interval_start_year;
		this.test_period_of_time_interval_start_month = test_period_of_time_interval_start_month;
		this.test_period_of_time_interval_start_day = test_period_of_time_interval_start_day;
		this.test_period_of_time_interval_end_year = test_period_of_time_interval_end_year;
		this.test_period_of_time_interval_end_month = test_period_of_time_interval_end_month;
		this.test_period_of_time_interval_end_day = test_period_of_time_interval_end_day;
		this.test_period_of_time_until_year = test_period_of_time_until_year;
		this.test_period_of_time_until_month = test_period_of_time_until_month;
		this.test_period_of_time_until_day = test_period_of_time_until_day;
		this.count = count;
	}
	
	public examination_medical_imaging_test() {
		super("");
		this.test_id = "";
		this.assessment = "";
		this.test_period_of_time_exact_year = "";
		this.test_period_of_time_exact_month = "";
		this.test_period_of_time_exact_day = "";
		this.test_period_of_time_interval_start_year = "";
		this.test_period_of_time_interval_start_month = "";
		this.test_period_of_time_interval_start_day = "";
		this.test_period_of_time_interval_end_year = "";
		this.test_period_of_time_interval_end_month = "";
		this.test_period_of_time_interval_end_day = "";
		this.test_period_of_time_until_year = "";
		this.test_period_of_time_until_month = "";
		this.test_period_of_time_until_day = "";
		this.count = "";
	}
	
	public String getTest_id() {
		return test_id;
	}
	public void setTest_id(String test_id) {
		this.test_id = test_id;
	}
	public String getAssessment() {
		return assessment;
	}
	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}
	public String getTest_period_of_time_exact_year() {
		return test_period_of_time_exact_year;
	}
	public void setTest_period_of_time_exact_year(String test_period_of_time_exact_year) {
		this.test_period_of_time_exact_year = test_period_of_time_exact_year;
	}
	public String getTest_period_of_time_exact_month() {
		return test_period_of_time_exact_month;
	}
	public void setTest_period_of_time_exact_month(String test_period_of_time_exact_month) {
		this.test_period_of_time_exact_month = test_period_of_time_exact_month;
	}
	public String getTest_period_of_time_exact_day() {
		return test_period_of_time_exact_day;
	}
	public void setTest_period_of_time_exact_day(String test_period_of_time_exact_day) {
		this.test_period_of_time_exact_day = test_period_of_time_exact_day;
	}
	public String getTest_period_of_time_interval_start_year() {
		return test_period_of_time_interval_start_year;
	}
	public void setTest_period_of_time_interval_start_year(String test_period_of_time_interval_start_year) {
		this.test_period_of_time_interval_start_year = test_period_of_time_interval_start_year;
	}
	public String getTest_period_of_time_interval_start_month() {
		return test_period_of_time_interval_start_month;
	}
	public void setTest_period_of_time_interval_start_month(String test_period_of_time_interval_start_month) {
		this.test_period_of_time_interval_start_month = test_period_of_time_interval_start_month;
	}
	public String getTest_period_of_time_interval_start_day() {
		return test_period_of_time_interval_start_day;
	}
	public void setTest_period_of_time_interval_start_day(String test_period_of_time_interval_start_day) {
		this.test_period_of_time_interval_start_day = test_period_of_time_interval_start_day;
	}
	public String getTest_period_of_time_interval_end_year() {
		return test_period_of_time_interval_end_year;
	}
	public void setTest_period_of_time_interval_end_year(String test_period_of_time_interval_end_year) {
		this.test_period_of_time_interval_end_year = test_period_of_time_interval_end_year;
	}
	public String getTest_period_of_time_interval_end_month() {
		return test_period_of_time_interval_end_month;
	}
	public void setTest_period_of_time_interval_end_month(String test_period_of_time_interval_end_month) {
		this.test_period_of_time_interval_end_month = test_period_of_time_interval_end_month;
	}
	public String getTest_period_of_time_interval_end_day() {
		return test_period_of_time_interval_end_day;
	}
	public void setTest_period_of_time_interval_end_day(String test_period_of_time_interval_end_day) {
		this.test_period_of_time_interval_end_day = test_period_of_time_interval_end_day;
	}
	public String getTest_period_of_time_until_year() {
		return test_period_of_time_until_year;
	}
	public void setTest_period_of_time_until_year(String test_period_of_time_until_year) {
		this.test_period_of_time_until_year = test_period_of_time_until_year;
	}
	public String getTest_period_of_time_until_month() {
		return test_period_of_time_until_month;
	}
	public void setTest_period_of_time_until_month(String test_period_of_time_until_month) {
		this.test_period_of_time_until_month = test_period_of_time_until_month;
	}
	public String getTest_period_of_time_until_day() {
		return test_period_of_time_until_day;
	}
	public void setTest_period_of_time_until_day(String test_period_of_time_until_day) {
		this.test_period_of_time_until_day = test_period_of_time_until_day;
	}
	
	public String getCount() {
		return count;
	}
	
	public void setCount(String count) {
		this.count = count;
	}

}
