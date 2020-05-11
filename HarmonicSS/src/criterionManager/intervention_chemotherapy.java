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
	
	
	public intervention_chemotherapy(String criterion_name, String voc_confirmation_CODE, String chem_exact_date_year, String chem_exact_date_month, String chem_exact_date_day,
			String chem_period_begin_year, String chem_period_begin_month, String chem_period_begin_day, String chem_period_end_year, String chem_period_end_month, 
			String chem_period_end_day, String chem_until_date_year, String chem_until_date_month, String chem_until_date_day, String statement, String min_count, String max_count, String end_before_start_year_nested, String end_before_end_year_nested, String end_after_start_year_nested, String end_after_end_year_nested, String start_before_start_year_nested, String start_before_end_year_nested, String start_after_start_year_nested, String start_after_end_year_nested, String years_nested) {
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
