package criterionManager;

public class other_healthcare_visit extends Criterion{

	public String specialist ="";
	
	public String period_of_time_exact_year = "";				//dt_date				
	public String period_of_time_exact_month = "";				//dt_date		
	public String period_of_time_exact_day = "";				//dt_date				
	
	public String period_of_time_interval_start_year = "";		//dt_date				
	public String period_of_time_interval_start_month = "";		//dt_date	
	public String period_of_time_interval_start_day = "";		//dt_date				
	
	public String period_of_time_interval_end_year = "";		//dt_date			
	public String period_of_time_interval_end_month = "";		//dt_date				
	public String period_of_time_interval_end_day = "";			//dt_date			
	
	public String period_of_time_until_year = "";				//dt_date
	public String period_of_time_until_month = "";				//dt_date
	public String period_of_time_until_day = "";				//dt_date
	
	public String count = "";
	public String max_nested = "";
	public String min_nested = "";
	public String years_nested = "";
	
	
	public other_healthcare_visit(String criterion_name, String specialist, String period_of_time_exact_year,
			String period_of_time_exact_month, String period_of_time_exact_day,
			String period_of_time_interval_start_year, String period_of_time_interval_start_month,
			String period_of_time_interval_start_day, String period_of_time_interval_end_year,
			String period_of_time_interval_end_month, String period_of_time_interval_end_day,
			String period_of_time_until_year, String period_of_time_until_month, String period_of_time_until_day, String count, String max_nested, String min_nested, String years_nested) {
		super(criterion_name);
		this.specialist = specialist;
		this.period_of_time_exact_year = period_of_time_exact_year;
		this.period_of_time_exact_month = period_of_time_exact_month;
		this.period_of_time_exact_day = period_of_time_exact_day;
		this.period_of_time_interval_start_year = period_of_time_interval_start_year;
		this.period_of_time_interval_start_month = period_of_time_interval_start_month;
		this.period_of_time_interval_start_day = period_of_time_interval_start_day;
		this.period_of_time_interval_end_year = period_of_time_interval_end_year;
		this.period_of_time_interval_end_month = period_of_time_interval_end_month;
		this.period_of_time_interval_end_day = period_of_time_interval_end_day;
		this.period_of_time_until_year = period_of_time_until_year;
		this.period_of_time_until_month = period_of_time_until_month;
		this.period_of_time_until_day = period_of_time_until_day;
		this.count = count;
		this.max_nested = max_nested;
		this.min_nested = min_nested;
		this.years_nested = years_nested;
	}
	
	public other_healthcare_visit() {
		super("");
		this.specialist = "";
		this.period_of_time_exact_year = "";
		this.period_of_time_exact_month = "";
		this.period_of_time_exact_day = "";
		this.period_of_time_interval_start_year = "";
		this.period_of_time_interval_start_month = "";
		this.period_of_time_interval_start_day = "";
		this.period_of_time_interval_end_year = "";
		this.period_of_time_interval_end_month = "";
		this.period_of_time_interval_end_day = "";
		this.period_of_time_until_year = "";
		this.period_of_time_until_month = "";
		this.period_of_time_until_day = "";
		this.count = "";
		this.max_nested = "";
		this.min_nested = "";
		this.years_nested = "";
	}
	
	public String getSpecialist() {
		return specialist;
	}
	public void setSpecialist(String specialist) {
		this.specialist = specialist;
	}
	public String getPeriod_of_time_exact_year() {
		return period_of_time_exact_year;
	}
	public void setPeriod_of_time_exact_year(String period_of_time_exact_year) {
		this.period_of_time_exact_year = period_of_time_exact_year;
	}
	public String getPeriod_of_time_exact_month() {
		return period_of_time_exact_month;
	}
	public void setPeriod_of_time_exact_month(String period_of_time_exact_month) {
		this.period_of_time_exact_month = period_of_time_exact_month;
	}
	public String getPeriod_of_time_exact_day() {
		return period_of_time_exact_day;
	}
	public void setPeriod_of_time_exact_day(String period_of_time_exact_day) {
		this.period_of_time_exact_day = period_of_time_exact_day;
	}
	public String getPeriod_of_time_interval_start_year() {
		return period_of_time_interval_start_year;
	}
	public void setPeriod_of_time_interval_start_year(String period_of_time_interval_start_year) {
		this.period_of_time_interval_start_year = period_of_time_interval_start_year;
	}
	public String getPeriod_of_time_interval_start_month() {
		return period_of_time_interval_start_month;
	}
	public void setPeriod_of_time_interval_start_month(String period_of_time_interval_start_month) {
		this.period_of_time_interval_start_month = period_of_time_interval_start_month;
	}
	public String getPeriod_of_time_interval_start_day() {
		return period_of_time_interval_start_day;
	}
	public void setPeriod_of_time_interval_start_day(String period_of_time_interval_start_day) {
		this.period_of_time_interval_start_day = period_of_time_interval_start_day;
	}
	public String getPeriod_of_time_interval_end_year() {
		return period_of_time_interval_end_year;
	}
	public void setPeriod_of_time_interval_end_year(String period_of_time_interval_end_year) {
		this.period_of_time_interval_end_year = period_of_time_interval_end_year;
	}
	public String getPeriod_of_time_interval_end_month() {
		return period_of_time_interval_end_month;
	}
	public void setPeriod_of_time_interval_end_month(String period_of_time_interval_end_month) {
		this.period_of_time_interval_end_month = period_of_time_interval_end_month;
	}
	public String getPeriod_of_time_interval_end_day() {
		return period_of_time_interval_end_day;
	}
	public void setPeriod_of_time_interval_end_day(String period_of_time_interval_end_day) {
		this.period_of_time_interval_end_day = period_of_time_interval_end_day;
	}
	public String getPeriod_of_time_until_year() {
		return period_of_time_until_year;
	}
	public void setPeriod_of_time_until_year(String period_of_time_until_year) {
		this.period_of_time_until_year = period_of_time_until_year;
	}
	public String getPeriod_of_time_until_month() {
		return period_of_time_until_month;
	}
	public void setPeriod_of_time_until_month(String period_of_time_until_month) {
		this.period_of_time_until_month = period_of_time_until_month;
	}
	public String getPeriod_of_time_until_day() {
		return period_of_time_until_day;
	}
	public void setPeriod_of_time_until_day(String period_of_time_until_day) {
		this.period_of_time_until_day = period_of_time_until_day;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getMaxNested() {
		return max_nested;
	}
	
	public void setMaxNested(String max_nested) {
		this.max_nested = max_nested;
	}
	
	public String getMinNested() {
		return min_nested;
	}
	
	public void setMinNested(String min_nested) {
		this.min_nested = min_nested;
	}
	
	public String getYearsNested() {
		return years_nested;
	}
	
	public void setYearsNested(String years_nested) {
		this.years_nested = years_nested;
	}
}
