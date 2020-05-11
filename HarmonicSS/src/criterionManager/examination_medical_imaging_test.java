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
	
	public String min_count = "";
	public String max_count = "";
	public String max_year_nested = "";
	public String min_year_nested = "";
	public String years_nested = "";
	public String start_period_year_nested = "";
	public String end_period_year_nested = "";
	
	
	public examination_medical_imaging_test(String criterion_name, String test_id, String assessment,
			String test_period_of_time_exact_year, String test_period_of_time_exact_month,
			String test_period_of_time_exact_day, String test_period_of_time_interval_start_year,
			String test_period_of_time_interval_start_month, String test_period_of_time_interval_start_day,
			String test_period_of_time_interval_end_year, String test_period_of_time_interval_end_month,
			String test_period_of_time_interval_end_day, String test_period_of_time_until_year,
			String test_period_of_time_until_month, String test_period_of_time_until_day, String min_count, String max_count, String max_year_nested, String min_year_nested, String years_nested, String start_period_year_nested, String end_period_year_nested) {
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
		this.min_count = min_count;
		this.max_count = max_count;
		this.max_year_nested = max_year_nested;
		this.min_year_nested = min_year_nested;
		this.years_nested = years_nested;
		this.start_period_year_nested = start_period_year_nested;
		this.end_period_year_nested = end_period_year_nested;
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
		this.min_count = "";
		this.max_count = "";
		this.max_year_nested = "";
		this.min_year_nested = "";
		this.years_nested = "";
		this.start_period_year_nested = "";
		this.end_period_year_nested = "";
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
	
	public String getMinCount() {
		return min_count;
	}
	
	public void setMinCount(String min_count) {
		this.min_count = min_count;
	}
	
	public String getMaxCount() {
		return max_count;
	}
	
	public void setMaxCount(String max_count) {
		this.max_count = max_count;
	}

	public String getMaxNested() {
		return max_year_nested;
	}
	
	public void setMaxNested(String max_year_nested) {
		this.max_year_nested = max_year_nested;
	}
	
	public String getMinNested() {
		return min_year_nested;
	}
	
	public void setMinNested(String min_year_nested) {
		this.min_year_nested = min_year_nested;
	}
	
	public String getYearsNested() {
		return years_nested;
	}
	
	public void setYearsNested(String years_nested) {
		this.years_nested = years_nested;
	}
	
	public void setStartPeriodNested(String start_period_year_nested) {
		this.start_period_year_nested = start_period_year_nested;
	}
	
	public String getStartPeriodNested() {
		return start_period_year_nested;
	}
	
	public void setEndPeriodNested(String end_period_year_nested) {
		this.end_period_year_nested = end_period_year_nested;
	}
	
	public String getEndPeriodNested() {
		return end_period_year_nested;
	}
}
