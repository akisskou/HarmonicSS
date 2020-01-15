package criterionManager;

public class intervention_chemotherapy extends Criterion {
	
	public String reason = ""; 					//Column: DUE_TO_PSS_ID
	public String period_of_time_exact_year = "";						
	public String period_of_time_exact_month = "";					
	public String period_of_time_exact_day = "";						
	public String period_of_time_interval_start_year = "";					
	public String period_of_time_interval_start_month = "";				
	public String period_of_time_interval_start_day = "";					
	public String period_of_time_interval_end_year = "";					
	public String period_of_time_interval_end_month = "";					
	public String period_of_time_interval_end_day = "";					
	public String period_of_time_until_year = "";
	public String period_of_time_until_month = "";
	public String period_of_time_until_day = "";	
	public String statement = "";
	
	
	public intervention_chemotherapy(String criterion_name, String voc_confirmation_CODE, String chem_exact_date_year, String chem_exact_date_month, String chem_exact_date_day,
			String chem_period_begin_year, String chem_period_begin_month, String chem_period_begin_day, String chem_period_end_year, String chem_period_end_month, 
			String chem_period_end_day, String chem_until_date_year, String chem_until_date_month, String chem_until_date_day, String statement) {
		super(criterion_name);
		this.reason = voc_confirmation_CODE; 						//Column: DUE_TO_PSS_ID
		this.period_of_time_exact_year = chem_exact_date_year;						
		this.period_of_time_exact_month = chem_exact_date_month;					
		this.period_of_time_exact_day = chem_exact_date_day;						
		this.period_of_time_interval_start_year = chem_period_begin_year;					
		this.period_of_time_interval_start_month = chem_period_begin_month;				
		this.period_of_time_interval_start_day = chem_period_begin_day;					
		this.period_of_time_interval_end_year = chem_period_end_year;					
		this.period_of_time_interval_end_month = chem_period_end_month;					
		this.period_of_time_interval_end_day = chem_period_end_day;					
		this.period_of_time_until_year = chem_until_date_year;
		this.period_of_time_until_month = chem_until_date_month;
		this.period_of_time_until_day = chem_until_date_day;
		this.statement=statement;
	}

	public intervention_chemotherapy() {
		super("");
		this.reason = ""; 				//Column: DUE_TO_PSS_ID
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

	public String getReason() {
		return reason;
	}

	public void setReason(String voc_confirmation_CODE) {
		this.reason = voc_confirmation_CODE;
	}

	public String getChem_exact_date_year() {
		return period_of_time_exact_year;
	}

	public void setChem_exact_date_year(String chem_exact_date_year) {
		this.period_of_time_exact_year = chem_exact_date_year;
	}

	public String getChem_exact_date_month() {
		return period_of_time_exact_month;
	}

	public void setChem_exact_date_month(String chem_exact_date_month) {
		this.period_of_time_exact_month = chem_exact_date_month;
	}

	public String getChem_exact_date_day() {
		return period_of_time_exact_day;
	}

	public void setChem_exact_date_day(String chem_exact_date_day) {
		this.period_of_time_exact_day = chem_exact_date_day;
	}

	public String getChem_period_begin_year() {
		return period_of_time_interval_start_year;
	}

	public void setChem_period_begin_year(String chem_period_begin_year) {
		this.period_of_time_interval_start_year = chem_period_begin_year;
	}

	public String getChem_period_begin_month() {
		return period_of_time_interval_start_month;
	}

	public void setChem_period_begin_month(String chem_period_begin_month) {
		this.period_of_time_interval_start_month = chem_period_begin_month;
	}

	public String getChem_period_begin_day() {
		return period_of_time_interval_start_day;
	}

	public void setChem_period_begin_day(String chem_period_begin_day) {
		this.period_of_time_interval_start_day = chem_period_begin_day;
	}

	public String getChem_period_end_year() {
		return period_of_time_interval_end_year;
	}

	public void setChem_period_end_year(String chem_period_end_year) {
		this.period_of_time_interval_end_year = chem_period_end_year;
	}

	public String getChem_period_end_month() {
		return period_of_time_interval_end_month;
	}

	public void setChem_period_end_month(String chem_period_end_month) {
		this.period_of_time_interval_end_month = chem_period_end_month;
	}

	public String getChem_period_end_day() {
		return period_of_time_interval_end_day;
	}

	public void setChem_period_end_day(String chem_period_end_day) {
		this.period_of_time_interval_end_day = chem_period_end_day;
	}

	public String getChem_until_date_year() {
		return period_of_time_until_year;
	}

	public void setChem_until_date_year(String chem_until_date_year) {
		this.period_of_time_until_year = chem_until_date_year;
	}

	public String getChem_until_date_month() {
		return period_of_time_until_month;
	}

	public void setChem_until_date_month(String chem_until_date_month) {
		this.period_of_time_until_month = chem_until_date_month;
	}

	public String getChem_until_date_day() {
		return period_of_time_until_day;
	}

	public void setChem_until_date_day(String chem_until_date_day) {
		this.period_of_time_until_day = chem_until_date_day;
	}
	
	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
}
