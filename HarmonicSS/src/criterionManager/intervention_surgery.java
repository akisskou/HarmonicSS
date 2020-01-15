package criterionManager;

public class intervention_surgery extends Criterion {

	public String reason = "";						//Column: DUE_TO_PSS_ID 
	public String procedure_date_exact_year = "";						
	public String procedure_date_exact_month = "";					
	public String procedure_date_exact_day = "";	
	public String procedure_date_interval_start_year = "";					
	public String procedure_date_interval_start_month = "";				
	public String procedure_date_interval_start_day = "";					
	public String procedure_date_interval_end_year = "";					
	public String procedure_date_interval_end_month = "";					
	public String procedure_date_interval_end_day = "";					
	public String procedure_date_until_year = "";
	public String procedure_date_until_month = "";
	public String procedure_date_until_day = "";
	public String statement = "";
	
	
	public intervention_surgery(String criterion_name, String DUE_TO_PSS_ID_voc_confirmation_CODE, String surgery_exact_date_year, String surgery_exact_date_month,
			String surgery_exact_date_day, String surgery_period_begin_year, String surgery_period_begin_month, String surgery_period_begin_day, String surgery_period_end_year,
			String surgery_period_end_month, String surgery_period_end_day, String surgery_until_date_year, String surgery_until_date_month, String surgery_until_date_day,
			String statement) {
		super(criterion_name);
		this.reason = DUE_TO_PSS_ID_voc_confirmation_CODE;		
		this.procedure_date_exact_year = surgery_exact_date_year;						
		this.procedure_date_exact_month = surgery_exact_date_month;					
		this.procedure_date_exact_day = surgery_exact_date_day;	
		this.procedure_date_interval_start_year = surgery_period_begin_year;					
		this.procedure_date_interval_start_month = surgery_period_begin_month;				
		this.procedure_date_interval_start_day = surgery_period_begin_day;					
		this.procedure_date_interval_end_year = surgery_period_end_year;					
		this.procedure_date_interval_end_month = surgery_period_end_month;					
		this.procedure_date_interval_end_day = surgery_period_end_day;					
		this.procedure_date_until_year = surgery_until_date_year;
		this.procedure_date_until_month = surgery_until_date_month;
		this.procedure_date_until_day = surgery_until_date_day;	
		this.statement = statement;
	}

	public intervention_surgery() {
		super("");
		this.reason = "";		
		this.procedure_date_exact_year = "";						
		this.procedure_date_exact_month = "";					
		this.procedure_date_exact_day = "";	
		this.procedure_date_interval_start_year = "";					
		this.procedure_date_interval_start_month = "";				
		this.procedure_date_interval_start_day = "";					
		this.procedure_date_interval_end_year = "";					
		this.procedure_date_interval_end_month = "";					
		this.procedure_date_interval_end_day = "";					
		this.procedure_date_until_year = "";
		this.procedure_date_until_month = "";
		this.procedure_date_until_day = "";
		this.statement = "";
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String dUE_TO_PSS_ID_voc_confirmation_CODE) {
		reason = dUE_TO_PSS_ID_voc_confirmation_CODE;
	}

	public String getSurgery_exact_date_year() {
		return procedure_date_exact_year;
	}

	public void setSurgery_exact_date_year(String surgery_exact_date_year) {
		this.procedure_date_exact_year = surgery_exact_date_year;
	}

	public String getSurgery_exact_date_month() {
		return procedure_date_exact_month;
	}

	public void setSurgery_exact_date_month(String surgery_exact_date_month) {
		this.procedure_date_exact_month = surgery_exact_date_month;
	}

	public String getSurgery_exact_date_day() {
		return procedure_date_exact_day;
	}

	public void setSurgery_exact_date_day(String surgery_exact_date_day) {
		this.procedure_date_exact_day = surgery_exact_date_day;
	}

	public String getSurgery_period_begin_year() {
		return procedure_date_interval_start_year;
	}

	public void setSurgery_period_begin_year(String surgery_period_begin_year) {
		this.procedure_date_interval_start_year = surgery_period_begin_year;
	}

	public String getSurgery_period_begin_month() {
		return procedure_date_interval_start_month;
	}

	public void setSurgery_period_begin_month(String surgery_period_begin_month) {
		this.procedure_date_interval_start_month = surgery_period_begin_month;
	}

	public String getSurgery_period_begin_day() {
		return procedure_date_interval_start_day;
	}

	public void setSurgery_period_begin_day(String surgery_period_begin_day) {
		this.procedure_date_interval_start_day = surgery_period_begin_day;
	}

	public String getSurgery_period_end_year() {
		return procedure_date_interval_end_year;
	}

	public void setSurgery_period_end_year(String surgery_period_end_year) {
		this.procedure_date_interval_end_year = surgery_period_end_year;
	}

	public String getSurgery_period_end_month() {
		return procedure_date_interval_end_month;
	}

	public void setSurgery_period_end_month(String surgery_period_end_month) {
		this.procedure_date_interval_end_month = surgery_period_end_month;
	}

	public String getSurgery_period_end_day() {
		return procedure_date_interval_end_day;
	}

	public void setSurgery_period_end_day(String surgery_period_end_day) {
		this.procedure_date_interval_end_day = surgery_period_end_day;
	}

	public String getSurgery_until_date_year() {
		return procedure_date_until_year;
	}

	public void setSurgery_until_date_year(String surgery_until_date_year) {
		this.procedure_date_until_year = surgery_until_date_year;
	}

	public String getSurgery_until_date_month() {
		return procedure_date_until_month;
	}

	public void setSurgery_until_date_month(String surgery_until_date_month) {
		this.procedure_date_until_month = surgery_until_date_month;
	}

	public String getSurgery_until_date_day() {
		return procedure_date_until_day;
	}

	public void setSurgery_until_date_day(String surgery_until_date_day) {
		this.procedure_date_until_day = surgery_until_date_day;
	}
	
	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
}
