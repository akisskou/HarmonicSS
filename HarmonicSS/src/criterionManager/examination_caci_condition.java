package criterionManager;

public class examination_caci_condition extends Criterion {

	public String caci ="";
	public String value ="";
	
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
	
	public String count = "";
	
	
	public examination_caci_condition(String criterion_name, String caci, String value,
			String questionnaire_period_of_time_exact_year, String questionnaire_period_of_time_exact_month,
			String questionnaire_period_of_time_exact_day, String questionnaire_period_of_time_interval_start_year,
			String questionnaire_period_of_time_interval_start_month,
			String questionnaire_period_of_time_interval_start_day,
			String questionnaire_period_of_time_interval_end_year,
			String questionnaire_period_of_time_interval_end_month,
			String questionnaire_period_of_time_interval_end_day, String questionnaire_period_of_time_until_year,
			String questionnaire_period_of_time_until_month, String questionnaire_period_of_time_until_day, String count) {
		super(criterion_name);
		this.caci = caci;
		this.value = value;
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
		this.count = count;
	}
	
	public examination_caci_condition() {
		super("");
		this.caci = "";
		this.value = "";
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
		this.count = "";
	}
	
	public String getCaci() {
		return caci;
	}
	public void setCaci(String caci) {
		this.caci = caci;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
	public String getCount() {
		return count;
	}
	
	public void setCount(String count) {
		this.count = count;
	}
	
}
