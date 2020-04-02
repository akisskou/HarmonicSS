package criterionManager;

public class demographics_pregnancy extends Criterion {
	
	public String conception_exact_year = "";						//Column: CONCEPTION_DATE_ID
	public String conception_exact_month = "";						//Column: CONCEPTION_DATE_ID
	public String conception_exact_day = "";						//Column: CONCEPTION_DATE_ID
	public String conception_interval_start_year = "";
	public String conception_interval_start_month = "";
	public String conception_interval_start_day = "";
	public String conception_interval_end_year = "";
	public String conception_interval_end_month = "";
	public String conception_interval_end_day = "";
	public String conception_until_year = "";
	public String conception_until_month = "";
	public String conception_until_day = "";
	public String outcome_exact_year = "";							//Column: OUTCOME_DATE_ID
	public String outcome_exact_month = "";							//Column: OUTCOME_DATE_ID
	public String outcome_exact_day = "";							//Column: OUTCOME_DATE_ID
	public String outcome_interval_start_year = "";
	public String outcome_interval_start_month = "";
	public String outcome_interval_start_day = "";
	public String outcome_interval_end_year = "";
	public String outcome_interval_end_month = "";
	public String outcome_interval_end_day = "";
	public String outcome_until_year = "";
	public String outcome_until_month = "";
	public String outcome_until_day = "";
	public String outcome_coded_value = ""; 					//Column: OUTCOME_ID
	public String outcome_ss_related = ""; 						//Column: SS_CONCORDANT_ID
	public String statement = "";
	public String count = "";
	public String max_outcome_nested = "";
	public String min_outcome_nested = "";
	public String years_outcome_nested = "";
	public String max_conception_nested = "";
	public String min_conception_nested = "";
	public String years_conception_nested = "";
	public String type_nested = "";
	

	public demographics_pregnancy(String criterion_name, String CONCEPTION_DATE_YEAR, String CONCEPTION_DATE_MONTH, String CONCEPTION_DATE_DAY,  
			String OUTCOME_DATE_YEAR, String OUTCOME_DATE_MONTH, String OUTCOME_DATE_DAY, 
			 String voc_pregnancy_outcome_CODE, String voc_confirmation_CODE,
			String CONCEPTION_period_begin_year, String CONCEPTION_period_begin_month, String CONCEPTION_period_begin_day,
			String CONCEPTION_period_end_year, String CONCEPTION_period_end_month, String CONCEPTION_period_end_day,
			String CONCEPTION_until_year, String CONCEPTION_until_date_month, String CONCEPTION_until_date_day,
			
			String OUTCOME_period_begin_year, String OUTCOME_period_begin_month, String OUTCOME_period_begin_day,
			String OUTCOME_period_end_year, String OUTCOME_period_end_month, String OUTCOME_period_end_day,
			String OUTCOME_until_year, String OUTCOME_until_month, String OUTCOME_until_day, String statement, String count, String max_outcome_nested, String min_outcome_nested, String years_outcome_nested, String max_conception_nested, String min_conception_nested, String years_conception_nested, String type_nested) {
		super(criterion_name);
		this.conception_exact_year = CONCEPTION_DATE_YEAR;						//Column: CONCEPTION_DATE_ID
		this.conception_exact_month = CONCEPTION_DATE_MONTH;						//Column: CONCEPTION_DATE_ID
		this.conception_exact_day = CONCEPTION_DATE_DAY;							//Column: CONCEPTION_DATE_ID
		this.conception_interval_start_year = "";
		this.conception_interval_start_month = "";
		this.conception_interval_start_day = "";	
		this.conception_interval_end_year = "";
		this.conception_interval_end_month = "";
		this.conception_interval_end_day = "";	
		this.conception_until_year = "";
		this.conception_until_month = "";
		this.conception_until_day = "";
		this.outcome_exact_year = OUTCOME_DATE_YEAR;								//Column: OUTCOME_DATE_ID
		this.outcome_exact_month = OUTCOME_DATE_MONTH;							//Column: OUTCOME_DATE_ID
		this.outcome_exact_day = OUTCOME_DATE_DAY;								//Column: OUTCOME_DATE_ID
		this.outcome_interval_start_year = "";
		this.outcome_interval_start_month = "";
		this.outcome_interval_start_day = "";
		this.outcome_interval_end_year = "";
		this.outcome_interval_end_month = "";
		this.outcome_interval_end_day = "";
		this.outcome_until_year = "";
		this.outcome_until_month = "";
		this.outcome_until_day = "";
		this.outcome_coded_value = voc_pregnancy_outcome_CODE; 			//Column: OUTCOME_ID
		this.outcome_ss_related = voc_confirmation_CODE;						//Column: SS_CONCORDANT_ID
		this.statement = statement;
		this.count = count;
		this.max_outcome_nested = max_conception_nested;
		this.min_outcome_nested = min_conception_nested;
		this.years_outcome_nested = years_conception_nested;
		this.max_conception_nested = max_conception_nested;
		this.min_conception_nested = min_conception_nested;
		this.years_conception_nested = years_conception_nested;
		this.type_nested = type_nested;
	}

	public demographics_pregnancy() {
		this.conception_exact_year = "";							//Column: CONCEPTION_DATE_ID
		this.conception_exact_month = "";						//Column: CONCEPTION_DATE_ID
		this.conception_exact_day = "";							//Column: CONCEPTION_DATE_ID
		this.conception_interval_start_year = "";
		this.conception_interval_start_month = "";
		this.conception_interval_start_day = "";
		this.conception_interval_end_year = "";
		this.conception_interval_end_month = "";
		this.conception_interval_end_day = "";
		this.conception_until_year = "";
		this.conception_until_month = "";
		this.conception_until_day = "";
		this.outcome_exact_year = "";							//Column: OUTCOME_DATE_ID
		this.outcome_exact_month = "";							//Column: OUTCOME_DATE_ID
		this.outcome_exact_day = "";								//Column: OUTCOME_DATE_ID
		this.outcome_interval_start_year = "";
		this.outcome_interval_start_month = "";
		this.outcome_interval_start_day = "";	
		this.outcome_interval_end_year = "";
		this.outcome_interval_end_month = "";
		this.outcome_interval_end_day = "";
		this.outcome_until_year = "";
		this.outcome_until_month = "";
		this.outcome_until_day = "";
		this.outcome_coded_value = ""; 					//Column: OUTCOME_ID
		this.outcome_ss_related = "";						//Column: SS_CONCORDANT_ID
		this.statement = "";
		this.count = "";
		this.max_outcome_nested = "";
		this.min_outcome_nested = "";
		this.years_outcome_nested = "";
		this.max_conception_nested = "";
		this.min_conception_nested = "";
		this.years_conception_nested = "";
		this.type_nested = "";
	}
	
	public String getCONCEPTION_DATE_YEAR() {
		return conception_exact_year;
	}

	public void setCONCEPTION_DATE_YEAR(String cONCEPTION_DATE_YEAR) {
		conception_exact_year = cONCEPTION_DATE_YEAR;
	}

	public String getCONCEPTION_DATE_MONTH() {
		return conception_exact_month;
	}

	public void setCONCEPTION_DATE_MONTH(String cONCEPTION_DATE_MONTH) {
		conception_exact_month = cONCEPTION_DATE_MONTH;
	}

	public String getCONCEPTION_DATE_DAY() {
		return conception_exact_day;
	}

	public void setCONCEPTION_DATE_DAY(String cONCEPTION_DATE_DAY) {
		conception_exact_day = cONCEPTION_DATE_DAY;
	}


	public String getOUTCOME_DATE_YEAR() {
		return outcome_exact_year;
	}

	public void setOUTCOME_DATE_YEAR(String oUTCOME_DATE_YEAR) {
		outcome_exact_year = oUTCOME_DATE_YEAR;
	}

	public String getOUTCOME_DATE_MONTH() {
		return outcome_exact_month;
	}

	public void setOUTCOME_DATE_MONTH(String oUTCOME_DATE_MONTH) {
		outcome_exact_month = oUTCOME_DATE_MONTH;
	}

	public String getOUTCOME_DATE_DAY() {
		return outcome_exact_day;
	}

	public void setOUTCOME_DATE_DAY(String oUTCOME_DATE_DAY) {
		outcome_exact_day = oUTCOME_DATE_DAY;
	}

	public String getOutcome_coded_value() {
		return outcome_coded_value;
	}

	public void setOutcome_coded_value(String voc_pregnancy_outcome_CODE) {
		this.outcome_coded_value = voc_pregnancy_outcome_CODE;
	}

	public String getOutcome_ss_related() {
		return outcome_ss_related;
	}

	public void setOutcome_ss_related(String voc_confirmation_CODE) {
		this.outcome_ss_related = voc_confirmation_CODE;
	}

	public String getCONCEPTION_period_begin_year() {
		return conception_interval_start_year;
	}

	public void setCONCEPTION_period_begin_year(String cONCEPTION_period_begin_year) {
		conception_interval_start_year = cONCEPTION_period_begin_year;
	}

	public String getCONCEPTION_period_begin_month() {
		return conception_interval_start_month;
	}

	public void setCONCEPTION_period_begin_month(String cONCEPTION_period_begin_month) {
		conception_interval_start_month = cONCEPTION_period_begin_month;
	}

	public String getCONCEPTION_period_begin_day() {
		return conception_interval_start_day;
	}

	public void setCONCEPTION_period_begin_day(String cONCEPTION_period_begin_day) {
		conception_interval_start_day = cONCEPTION_period_begin_day;
	}

	public String getCONCEPTION_period_end_year() {
		return conception_interval_end_year;
	}

	public void setCONCEPTION_period_end_year(String cONCEPTION_period_end_year) {
		conception_interval_end_year = cONCEPTION_period_end_year;
	}

	public String getCONCEPTION_period_end_month() {
		return conception_interval_end_month;
	}

	public void setCONCEPTION_period_end_month(String cONCEPTION_period_end_month) {
		conception_interval_end_month = cONCEPTION_period_end_month;
	}

	public String getCONCEPTION_period_end_day() {
		return conception_interval_end_day;
	}

	public void setCONCEPTION_period_end_day(String cONCEPTION_period_end_day) {
		conception_interval_end_day = cONCEPTION_period_end_day;
	}

	public String getCONCEPTION_until_date_year() {
		return conception_until_year;
	}

	public void setCONCEPTION_until_date_year(String cONCEPTION_until_year) {
		conception_until_year = cONCEPTION_until_year;
	}

	public String getCONCEPTION_until_date_month() {
		return conception_until_month;
	}

	public void setCONCEPTION_until_date_month(String cONCEPTION_until_date_month) {
		conception_until_month = cONCEPTION_until_date_month;
	}

	public String getCONCEPTION_until_date_day() {
		return conception_until_day;
	}

	public void setCONCEPTION_until_date_day(String cONCEPTION_until_date_day) {
		conception_until_day = cONCEPTION_until_date_day;
	}

	public String getOUTCOME_period_begin_year() {
		return outcome_interval_start_year;
	}

	public void setOUTCOME_period_begin_year(String oUTCOME_period_begin_year) {
		outcome_interval_start_year = oUTCOME_period_begin_year;
	}

	public String getOUTCOME_period_begin_month() {
		return outcome_interval_start_month;
	}

	public void setOUTCOME_period_begin_month(String oUTCOME_period_begin_month) {
		outcome_interval_start_month = oUTCOME_period_begin_month;
	}

	public String getOUTCOME_period_begin_day() {
		return outcome_interval_start_day;
	}

	public void setOUTCOME_period_begin_day(String oUTCOME_period_begin_day) {
		outcome_interval_start_day = oUTCOME_period_begin_day;
	}

	public String getOUTCOME_period_end_year() {
		return outcome_interval_end_year;
	}

	public void setOUTCOME_period_end_year(String oUTCOME_period_end_year) {
		outcome_interval_end_year = oUTCOME_period_end_year;
	}

	public String getOUTCOME_period_end_month() {
		return outcome_interval_end_month;
	}

	public void setOUTCOME_period_end_month(String oUTCOME_period_end_month) {
		outcome_interval_end_month = oUTCOME_period_end_month;
	}

	public String getOUTCOME_period_end_day() {
		return outcome_interval_end_day;
	}

	public void setOUTCOME_period_end_day(String oUTCOME_period_end_day) {
		outcome_interval_end_day = oUTCOME_period_end_day;
	}

	public String getOUTCOME_until_date_year() {
		return outcome_until_year;
	}

	public void setOUTCOME_until_date_year(String oUTCOME_until_year) {
		outcome_until_year = oUTCOME_until_year;
	}

	public String getOUTCOME_until_date_month() {
		return outcome_until_month;
	}

	public void setOUTCOME_until_date_month(String oUTCOME_until_date_month) {
		outcome_until_month = oUTCOME_until_date_month;
	}

	public String getOUTCOME_until_date_day() {
		return outcome_until_day;
	}

	public void setOUTCOME_until_date_day(String oUTCOME_until_date_day) {
		outcome_until_day = oUTCOME_until_date_day;
	}
	
	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getCount() {
		return count;
	}
	
	public void setCount(String count) {
		this.count = count;
	}
	
	public String getOutcomeMaxNested() {
		return max_outcome_nested;
	}
	
	public void setOutcomeMaxNested(String max_outcome_nested) {
		this.max_outcome_nested = max_outcome_nested;
	}
	
	public String getOutcomeMinNested() {
		return min_outcome_nested;
	}
	
	public void setOutcomeMinNested(String min_outcome_nested) {
		this.min_outcome_nested = min_outcome_nested;
	}
	public String getOutcomeYearsNested() {
		return years_outcome_nested;
	}
	
	public void setOutcomeYearsNested(String years_outcome_nested) {
		this.years_outcome_nested = years_outcome_nested;
	}
	
	public String getConceptionMaxNested() {
		return max_conception_nested;
	}
	
	public void setConceptionMaxNested(String max_conception_nested) {
		this.max_conception_nested = max_conception_nested;
	}
	
	public String getConceptionMinNested() {
		return min_conception_nested;
	}
	
	public void setConceptionMinNested(String min_conception_nested) {
		this.min_conception_nested = min_conception_nested;
	}
	public String getConceptionYearsNested() {
		return years_conception_nested;
	}
	
	public void setConceptionYearsNested(String years_conception_nested) {
		this.years_conception_nested = years_conception_nested;
	}
	public String getTypeNested() {
		return type_nested;
	}
	
	public void setTypeNested(String type_nested) {
		this.type_nested = type_nested;
	}
}
