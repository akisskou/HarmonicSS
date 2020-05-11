package criterionManager;

public class other_clinical_trials extends Criterion{

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
	public String statement = "";
	public String min_count = "";
	public String max_count = "";
	public String end_before_start_year_nested = "";
	public String end_before_end_year_nested = "";
	public String end_after_start_year_nested = "";
	public String end_after_end_year_nested = "";
	public String start_before_start_year_nested = "";
	public String start_before_end_year_nested = "";
	public String start_after_start_year_nested = "";
	public String start_after_end_year_nested = "";
	public String years_nested = "";
	
	
	public other_clinical_trials(String criterion_name, String period_of_time_exact_year,
			String period_of_time_exact_month, String period_of_time_exact_day,
			String period_of_time_interval_start_year, String period_of_time_interval_start_month,
			String period_of_time_interval_start_day, String period_of_time_interval_end_year,
			String period_of_time_interval_end_month, String period_of_time_interval_end_day,
			String period_of_time_until_year, String period_of_time_until_month, String period_of_time_until_day, String statement, String min_count, String max_count, String max_year_nested, String min_year_nested, String end_before_start_year_nested, String end_before_end_year_nested, String end_after_start_year_nested, String end_after_end_year_nested, String start_before_start_year_nested, String start_before_end_year_nested, String start_after_start_year_nested, String start_after_end_year_nested, String years_nested) {
		super(criterion_name);
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
		this.statement = statement;
		this.min_count = min_count;
		this.max_count = max_count;
		this.end_before_start_year_nested = end_before_start_year_nested;
		this.end_before_end_year_nested = end_before_end_year_nested;
		this.end_after_start_year_nested = end_after_start_year_nested;
		this.end_after_end_year_nested = end_after_end_year_nested;
		this.start_before_start_year_nested = start_before_start_year_nested;
		this.start_before_end_year_nested = start_before_end_year_nested;
		this.start_after_start_year_nested = start_after_start_year_nested;
		this.start_after_end_year_nested = start_after_end_year_nested;
		this.years_nested = years_nested;
	}
	
	public other_clinical_trials() {
		super("");
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
		this.statement = "";
		this.min_count = "";
		this.max_count = "";
		this.end_before_start_year_nested = "";
		this.end_before_end_year_nested = "";
		this.end_after_start_year_nested = "";
		this.end_after_end_year_nested = "";
		this.start_before_start_year_nested = "";
		this.start_before_end_year_nested = "";
		this.start_after_start_year_nested = "";
		this.start_after_end_year_nested = "";
		this.years_nested = "";
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
	
	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
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
	
	public String getEndBeforeStartNested() {
		return end_before_start_year_nested;
	}
	
	public void setEndBeforeStartNested(String end_before_start_year_nested) {
		this.end_before_start_year_nested = end_before_start_year_nested;
	}
	
	public String getEndBeforeEndNested() {
		return end_before_end_year_nested;
	}
	
	public void setEndBeforeEndNested(String end_before_end_year_nested) {
		this.end_before_end_year_nested = end_before_end_year_nested;
	}
	
	public String getEndAfterStartNested() {
		return end_after_start_year_nested;
	}
	
	public void setEndAfterStartNested(String end_after_start_year_nested) {
		this.end_after_start_year_nested = end_after_start_year_nested;
	}
	
	public String getEndAfterEndNested() {
		return end_after_end_year_nested;
	}
	
	public void setEndAfterEndNested(String end_after_end_year_nested) {
		this.end_after_end_year_nested = end_after_end_year_nested;
	}
	
	public String getStartBeforeStartNested() {
		return start_before_start_year_nested;
	}
	
	public void setStartBeforeStartNested(String start_before_start_year_nested) {
		this.start_before_start_year_nested = start_before_start_year_nested;
	}
	
	public String getStartBeforeEndNested() {
		return start_before_end_year_nested;
	}
	
	public void setStartBeforeEndNested(String start_before_end_year_nested) {
		this.start_before_end_year_nested = start_before_end_year_nested;
	}
	
	public String getStartAfterStartNested() {
		return start_after_start_year_nested;
	}
	
	public void setStartAfterStartNested(String start_after_start_year_nested) {
		this.start_after_start_year_nested = start_after_start_year_nested;
	}
	
	public String getStartAfterEndNested() {
		return start_after_end_year_nested;
	}
	
	public void setStartAfterEndNested(String start_after_end_year_nested) {
		this.start_after_end_year_nested = start_after_end_year_nested;
	}
	
	public String getYearsNested() {
		return years_nested;
	}
	
	public void setYearsNested(String years_nested) {
		this.years_nested = years_nested;
	}
}
