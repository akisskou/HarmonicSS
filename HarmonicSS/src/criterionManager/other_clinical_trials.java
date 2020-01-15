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
	
	
	public other_clinical_trials(String criterion_name, String period_of_time_exact_year,
			String period_of_time_exact_month, String period_of_time_exact_day,
			String period_of_time_interval_start_year, String period_of_time_interval_start_month,
			String period_of_time_interval_start_day, String period_of_time_interval_end_year,
			String period_of_time_interval_end_month, String period_of_time_interval_end_day,
			String period_of_time_until_year, String period_of_time_until_month, String period_of_time_until_day, String statement) {
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
	
}
