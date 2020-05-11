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
	public String min_count = "";
	public String max_count = "";
	public String max_year_nested = "";
	public String min_year_nested = "";
	public String years_nested = "";
	public String start_period_year_nested = "";
	public String end_period_year_nested = "";
	
	
	public intervention_surgery(String criterion_name, String DUE_TO_PSS_ID_voc_confirmation_CODE, String surgery_exact_date_year, String surgery_exact_date_month,
			String surgery_exact_date_day, String surgery_period_begin_year, String surgery_period_begin_month, String surgery_period_begin_day, String surgery_period_end_year,
			String surgery_period_end_month, String surgery_period_end_day, String surgery_until_date_year, String surgery_until_date_month, String surgery_until_date_day,
			String statement, String min_count, String max_count, String max_year_nested, String min_year_nested, String years_nested, String start_period_year_nested, String end_period_year_nested) {
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
		this.min_count = min_count;
		this.max_count = max_count;
		this.max_year_nested = max_year_nested;
		this.min_year_nested = min_year_nested;
		this.years_nested = years_nested;
		this.start_period_year_nested = start_period_year_nested;
		this.end_period_year_nested = end_period_year_nested;
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
		this.min_count = "";
		this.max_count = "";
		this.max_year_nested = "";
		this.min_year_nested = "";
		this.years_nested = "";
		this.start_period_year_nested = "";
		this.end_period_year_nested = "";
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
