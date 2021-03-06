package criterionManager;

public class lifestyle_smoking extends Criterion {

	public String status_coded_value = "";					//Column: STATUS_ID					
	public String amount_unit = "";					//Column: AMOUNT_ID Do we also need the column: EXPRESSION ?
	public String amount_exact_value = "";	//dt_amount	
	public String amount_range_min_value = "";	//dt_amount	
	public String amount_range_max_value = "";	//dt_amount	
	public String exact_year = "";						
	public String exact_month = "";					
	public String exact_day = "";						
	public String interval_start_year = "";					
	public String interval_start_month = "";				
	public String interval_start_day = "";					
	public String interval_end_year = "";					
	public String interval_end_month = "";					
	public String interval_end_day = "";					
	public String until_year = "";
	public String until_month = "";
	public String until_day = "";	
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
	
	
	public lifestyle_smoking(String criterion_name, String voc_smoking_status_CODE, String dt_amount_exact_value, String amount_range_min_value,
			String amount_range_max_value,String dt_amount_voc_unit_CODE,
			String START_DATE_dt_date_YEAR, String START_DATE_dt_date_MONTH, String START_DATE_dt_date_DAY, 
			String END_DATE_dt_date_YEAR, String END_DATE_dt_date_MONTH, String END_DATE_dt_date_DAY,
			 String smoking_exact_date_year, String smoking_exact_date_month, String smoking_exact_date_day,
			String smoking_until_date_year, String smoking_until_date_month, String smoking_until_date_day, String statement, String min_count, String max_count, String end_before_start_year_nested, String end_before_end_year_nested, String end_after_start_year_nested, String end_after_end_year_nested, String start_before_start_year_nested, String start_before_end_year_nested, String start_after_start_year_nested, String start_after_end_year_nested, String years_nested) {	
		super(criterion_name);
		this.status_coded_value = voc_smoking_status_CODE;							//Column: STATUS_ID
		this.amount_exact_value = dt_amount_exact_value;							//Column: AMOUNT_ID
		this.amount_range_min_value = amount_range_min_value;
		this.amount_range_max_value = amount_range_max_value;						//dt_amount
		this.amount_unit = dt_amount_voc_unit_CODE;									//Column: AMOUNT_ID Do we also need the column: EXPRESSION ?
		this.exact_year = smoking_exact_date_year;						
		this.exact_month = smoking_exact_date_month;					
		this.exact_day = smoking_exact_date_day;	
		this.interval_start_year = START_DATE_dt_date_YEAR;							//Column: PERIOD_ID START_DATE
		this.interval_start_month = START_DATE_dt_date_MONTH;						//Column: PERIOD_ID START_DATE
		this.interval_start_day = START_DATE_dt_date_DAY;							//Column: PERIOD_ID START_DATE	
		this.interval_end_year = END_DATE_dt_date_YEAR;								//Column: PERIOD_ID END_DATE
		this.interval_end_month = END_DATE_dt_date_MONTH;							//Column: PERIOD_ID END_DATE
		this.interval_end_day = END_DATE_dt_date_DAY;								//Column: PERIOD_ID END_DATE
		this.until_year = smoking_until_date_year;
		this.until_month = smoking_until_date_month;
		this.until_day = smoking_until_date_day;
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

	public lifestyle_smoking() {
		super("");
		this.status_coded_value = "";					//Column: STATUS_ID
		this.amount_exact_value = "";					//Column: AMOUNT_ID
		this.amount_range_min_value = "";
		this.amount_range_max_value = "";				//dt_amount
		this.amount_unit = "";							//Column: AMOUNT_ID Do we also need the column: EXPRESSION ?
		this.exact_year = "";						
		this.exact_month = "";					
		this.exact_day = "";
		this.interval_start_year = "";					//Column: PERIOD_ID START_DATE
		this.interval_start_month = "";					//Column: PERIOD_ID START_DATE
		this.interval_start_day = "";					//Column: PERIOD_ID START_DATE
		this.interval_end_year = "";					//Column: PERIOD_ID END_DATE
		this.interval_end_month = "";					//Column: PERIOD_ID END_DATE
		this.interval_end_day = "";						//Column: PERIOD_ID END_DATE
		this.until_year = "";
		this.until_month = "";
		this.until_day = "";
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
	
	public String getAmount_exact_value() {
		return amount_exact_value;
	}

	public void setAmount_exact_value(String amount_exact_value) {
		this.amount_exact_value = amount_exact_value;
	}

	public String getAmount_range_min_value() {
		return amount_range_min_value;
	}

	public void setAmount_range_min_value(String amount_range_min_value) {
		this.amount_range_min_value = amount_range_min_value;
	}

	public String getAmount_range_max_value() {
		return amount_range_max_value;
	}

	public void setAmount_range_max_value(String amount_range_max_value) {
		this.amount_range_max_value = amount_range_max_value;
	}

	public String getVoc_smoking_status_CODE() {
		return status_coded_value;
	}

	public void setVoc_smoking_status_CODE(String voc_smoking_status_CODE) {
		this.status_coded_value = voc_smoking_status_CODE;
	}

	public String getDt_amount_voc_unit_CODE() {
		return amount_unit;
	}

	public void setDt_amount_voc_unit_CODE(String dt_amount_voc_unit_CODE) {
		this.amount_unit = dt_amount_voc_unit_CODE;
	}

	public String getSmoking_exact_date_year() {
		return exact_year;
	}

	public void setSmoking_exact_date_year(String smoking_exact_date_year) {
		this.exact_year = smoking_exact_date_year;
	}

	public String getSmoking_exact_date_month() {
		return exact_month;
	}

	public void setSmoking_exact_date_month(String smoking_exact_date_month) {
		this.exact_month = smoking_exact_date_month;
	}

	public String getSmoking_exact_date_day() {
		return exact_day;
	}

	public void setSmoking_exact_date_day(String smoking_exact_date_day) {
		this.exact_day = smoking_exact_date_day;
	}

	public String getSmoking_period_begin_year() {
		return interval_start_year;
	}

	public void setSmoking_period_begin_year(String smoking_period_begin_year) {
		this.interval_start_year = smoking_period_begin_year;
	}

	public String getSmoking_period_begin_month() {
		return interval_start_month;
	}

	public void setSmoking_period_begin_month(String smoking_period_begin_month) {
		this.interval_start_month = smoking_period_begin_month;
	}

	public String getSmoking_period_begin_day() {
		return interval_start_day;
	}

	public void setSmoking_period_begin_day(String smoking_period_begin_day) {
		this.interval_start_day = smoking_period_begin_day;
	}

	public String getSmoking_period_end_year() {
		return interval_end_year;
	}

	public void setSmoking_period_end_year(String smoking_period_end_year) {
		this.interval_end_year = smoking_period_end_year;
	}

	public String getSmokimg_period_end_month() {
		return interval_end_month;
	}

	public void setSmokimg_period_end_month(String smokimg_period_end_month) {
		this.interval_end_month = smokimg_period_end_month;
	}

	public String getSmoking_period_end_day() {
		return interval_end_day;
	}

	public void setSmoking_period_end_day(String smoking_period_end_day) {
		this.interval_end_day = smoking_period_end_day;
	}

	public String getSmoking_until_date_year() {
		return until_year;
	}

	public void setSmoking_until_date_year(String smoking_until_date_year) {
		this.until_year = smoking_until_date_year;
	}

	public String getSmoking_until_date_month() {
		return until_month;
	}

	public void setSmoking_until_date_month(String smoking_until_date_month) {
		this.until_month = smoking_until_date_month;
	}

	public String getSmoking_until_date_day() {
		return until_day;
	}

	public void setSmoking_until_date_day(String smoking_until_date_day) {
		this.until_day = smoking_until_date_day;
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
