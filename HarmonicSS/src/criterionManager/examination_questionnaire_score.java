package criterionManager;

public class examination_questionnaire_score extends Criterion{

	public String score= "";
	public String value = "";
	public String exact_value = "";	
	public String range_min_value="";
	public String range_max_value="";
	public String assessment = "";
	public String normal_range_value = "";
	public String unit= "";
	public String other_term ="";
	
	public String questionnaire_period_of_time_exact_year = "";				//dt_date				
	public String questionnaire_period_of_time_exact_month = "";			//dt_date		
	public String questionnaire_period_of_time_exact_day = "";				//dt_date				
	
	public String questionnaire_period_of_time_interval_start_year = "";	//dt_date				
	public String questionnaire_period_of_time_interval_start_month = "";	//dt_date	
	public String questionnaire_period_of_time_interval_start_day = "";		//dt_date				
	
	public String questionnaire_period_of_time_interval_end_year = "";		//dt_date			
	public String questionnaire_period_of_time_interval_end_month = "";		//dt_date				
	public String questionnaire_period_of_time_interval_end_day = "";		//dt_date			
	
	public String questionnaire_period_of_time_until_year = "";				//dt_date
	public String questionnaire_period_of_time_until_month = "";			//dt_date
	public String questionnaire_period_of_time_until_day = "";				//dt_date
	
	public String min_count = "";
	public String max_count = "";
	public String max_year_nested = "";
	public String min_year_nested = "";
	public String years_nested = "";
	public String start_period_year_nested = "";
	public String end_period_year_nested = "";
	
	
	public examination_questionnaire_score(String criterion_name, String score_id, String value, String exact_value, String range_min_value, String range_max_value, String assessment,
			String normal_range_value, String normal_range_unit, String other_term,
			String questionnaire_period_of_time_exact_year, String questionnaire_period_of_time_exact_month,
			String questionnaire_period_of_time_exact_day, String questionnaire_period_of_time_interval_start_year,
			String questionnaire_period_of_time_interval_start_month,
			String questionnaire_period_of_time_interval_start_day,
			String questionnaire_period_of_time_interval_end_year,
			String questionnaire_period_of_time_interval_end_month,
			String questionnaire_period_of_time_interval_end_day, String questionnaire_period_of_time_until_year,
			String questionnaire_period_of_time_until_month, String questionnaire_period_of_time_until_day, String min_count, String max_count, String max_year_nested, String min_year_nested, String years_nested, String start_period_year_nested, String end_period_year_nested) {
		super(criterion_name);
		this.score = score_id;
		this.exact_value = exact_value;
		this.range_max_value = range_max_value;
		this.range_min_value = range_min_value;
		this.value = value;		
		this.assessment = assessment;
		this.normal_range_value = normal_range_value;
		this.unit = normal_range_unit;
		this.other_term = other_term;
		this.questionnaire_period_of_time_exact_year = questionnaire_period_of_time_exact_year;
		this.questionnaire_period_of_time_exact_month = questionnaire_period_of_time_exact_month;
		this.questionnaire_period_of_time_exact_day = questionnaire_period_of_time_exact_day;
		this.questionnaire_period_of_time_interval_start_year = questionnaire_period_of_time_interval_start_year;
		this.questionnaire_period_of_time_interval_start_month = questionnaire_period_of_time_interval_start_month;
		this.questionnaire_period_of_time_interval_start_day = questionnaire_period_of_time_interval_start_day;
		this.questionnaire_period_of_time_interval_end_year = questionnaire_period_of_time_interval_end_year;
		this.questionnaire_period_of_time_interval_end_month = questionnaire_period_of_time_interval_end_month;
		this.questionnaire_period_of_time_interval_end_day = questionnaire_period_of_time_interval_end_day;
		this.questionnaire_period_of_time_until_year = questionnaire_period_of_time_until_year;
		this.questionnaire_period_of_time_until_month = questionnaire_period_of_time_until_month;
		this.questionnaire_period_of_time_until_day = questionnaire_period_of_time_until_day;
		this.min_count = min_count;
		this.max_count = max_count;
		this.max_year_nested = max_year_nested;
		this.min_year_nested = min_year_nested;
		this.years_nested = years_nested;
		this.start_period_year_nested = start_period_year_nested;
		this.end_period_year_nested = end_period_year_nested;
	}
	
	public examination_questionnaire_score() {
		super("");
		this.score = "";
		this.value = "";
		this.exact_value = "";
		this.range_min_value = "";
		this.range_max_value = "";
		this.unit = "";
		this.assessment = "";
		this.normal_range_value = "";
		this.other_term = "";
		this.questionnaire_period_of_time_exact_year = "";
		this.questionnaire_period_of_time_exact_month = "";
		this.questionnaire_period_of_time_exact_day = "";
		this.questionnaire_period_of_time_interval_start_year = "";
		this.questionnaire_period_of_time_interval_start_month = "";
		this.questionnaire_period_of_time_interval_start_day = "";
		this.questionnaire_period_of_time_interval_end_year = "";
		this.questionnaire_period_of_time_interval_end_month = "";
		this.questionnaire_period_of_time_interval_end_day = "";
		this.questionnaire_period_of_time_until_year = "";
		this.questionnaire_period_of_time_until_month = "";
		this.questionnaire_period_of_time_until_day = "";
		this.min_count = "";
		this.max_count = "";
		this.max_year_nested = "";
		this.min_year_nested = "";
		this.years_nested = "";
		this.start_period_year_nested = "";
		this.end_period_year_nested = "";
	}
	
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getExactValue() {
		return exact_value; 
	}
	public void setExactValue(String exact_value) {
		this.exact_value = exact_value;
	}
	public String getRangeMinValue() {
		return range_min_value; 
	}
	public void setRangeMinValue(String range_min_value) {
		this.range_min_value = range_min_value;
	}
	public String getRangeMaxValue() {
		return range_max_value; 
	}
	public void setRangeMaxValue(String range_max_value) {
		this.range_max_value = range_max_value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String normal_range_unit) {
		this.unit = normal_range_unit;
	}
	//public String normal_range_val2 ="";
	public String getAssessment() {
		return assessment;
	}
	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}
	public String getNormal_range_value() {
		return normal_range_value;
	}
	public void setNormal_range_value(String normal_range_val1) {
		this.normal_range_value = normal_range_val1;
	}

	public String getOther_term() {
		return other_term;
	}
	public void setOther_term(String other_term) {
		this.other_term = other_term;
	}
	public String getQuestionnaire_period_of_time_exact_year() {
		return questionnaire_period_of_time_exact_year;
	}
	public void setQuestionnaire_period_of_time_exact_year(String questionnaire_period_of_time_exact_year) {
		this.questionnaire_period_of_time_exact_year = questionnaire_period_of_time_exact_year;
	}
	public String getQuestionnaire_period_of_time_exact_month() {
		return questionnaire_period_of_time_exact_month;
	}
	public void setQuestionnaire_period_of_time_exact_month(String questionnaire_period_of_time_exact_month) {
		this.questionnaire_period_of_time_exact_month = questionnaire_period_of_time_exact_month;
	}
	public String getQuestionnaire_period_of_time_exact_day() {
		return questionnaire_period_of_time_exact_day;
	}
	public void setQuestionnaire_period_of_time_exact_day(String questionnaire_period_of_time_exact_day) {
		this.questionnaire_period_of_time_exact_day = questionnaire_period_of_time_exact_day;
	}
	public String getQuestionnaire_period_of_time_interval_start_year() {
		return questionnaire_period_of_time_interval_start_year;
	}
	public void setQuestionnaire_period_of_time_interval_start_year(
			String questionnaire_period_of_time_interval_start_year) {
		this.questionnaire_period_of_time_interval_start_year = questionnaire_period_of_time_interval_start_year;
	}
	public String getQuestionnaire_period_of_time_interval_start_month() {
		return questionnaire_period_of_time_interval_start_month;
	}
	public void setQuestionnaire_period_of_time_interval_start_month(
			String questionnaire_period_of_time_interval_start_month) {
		this.questionnaire_period_of_time_interval_start_month = questionnaire_period_of_time_interval_start_month;
	}
	public String getQuestionnaire_period_of_time_interval_start_day() {
		return questionnaire_period_of_time_interval_start_day;
	}
	public void setQuestionnaire_period_of_time_interval_start_day(String questionnaire_period_of_time_interval_start_day) {
		this.questionnaire_period_of_time_interval_start_day = questionnaire_period_of_time_interval_start_day;
	}
	public String getQuestionnaire_period_of_time_interval_end_year() {
		return questionnaire_period_of_time_interval_end_year;
	}
	public void setQuestionnaire_period_of_time_interval_end_year(String questionnaire_period_of_time_interval_end_year) {
		this.questionnaire_period_of_time_interval_end_year = questionnaire_period_of_time_interval_end_year;
	}
	public String getQuestionnaire_period_of_time_interval_end_month() {
		return questionnaire_period_of_time_interval_end_month;
	}
	public void setQuestionnaire_period_of_time_interval_end_month(String questionnaire_period_of_time_interval_end_month) {
		this.questionnaire_period_of_time_interval_end_month = questionnaire_period_of_time_interval_end_month;
	}
	public String getQuestionnaire_period_of_time_interval_end_day() {
		return questionnaire_period_of_time_interval_end_day;
	}
	public void setQuestionnaire_period_of_time_interval_end_day(String questionnaire_period_of_time_interval_end_day) {
		this.questionnaire_period_of_time_interval_end_day = questionnaire_period_of_time_interval_end_day;
	}
	public String getQuestionnaire_period_of_time_until_year() {
		return questionnaire_period_of_time_until_year;
	}
	public void setQuestionnaire_period_of_time_until_year(String questionnaire_period_of_time_until_year) {
		this.questionnaire_period_of_time_until_year = questionnaire_period_of_time_until_year;
	}
	public String getQuestionnaire_period_of_time_until_month() {
		return questionnaire_period_of_time_until_month;
	}
	public void setQuestionnaire_period_of_time_until_month(String questionnaire_period_of_time_until_month) {
		this.questionnaire_period_of_time_until_month = questionnaire_period_of_time_until_month;
	}
	public String getQuestionnaire_period_of_time_until_day() {
		return questionnaire_period_of_time_until_day;
	}
	public void setQuestionnaire_period_of_time_until_day(String questionnaire_period_of_time_until_day) {
		this.questionnaire_period_of_time_until_day = questionnaire_period_of_time_until_day;
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
