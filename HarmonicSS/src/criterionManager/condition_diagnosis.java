package criterionManager;

public class condition_diagnosis extends Criterion{
	
	public String organ = "";						//Column: CONDITION_ID voc_symptom_sign Do we also need BROADER_TERM_ID and CATEGORY_ID in our queries?
	public String condition = "";
	public String stage="";  
	public String performance_status="";
	public String date_exact_year = "";				//Column: OBSERVE_DATE_ID
	public String date_exact_month = "";			//Column: OBSERVE_DATE_ID
	public String date_exact_day = "";				//Column: OBSERVE_DATE_ID
	public String date_interval_start_year = "";
	public String date_interval_start_month = "";
	public String date_interval_start_day = "";
	public String date_interval_end_year = "";
	public String date_interval_end_month = "";
	public String date_interval_end_day = "";
	public String date_until_year = "";
	public String date_until_month = "";
	public String date_until_day = "";
	public String statement = "";


	public condition_diagnosis(String criterion_name, String condition, String organ, String stage, String performance_status,
			String date_exact_year, String date_exact_month, String date_exact_day, String date_interval_start_year,
			String date_interval_start_month, String date_interval_start_day, String date_interval_end_year,
			String date_interval_end_month, String date_interval_end_day, String date_until_year,
			String date_until_month, String date_until_day, String statement) {
		super(criterion_name);
		this.condition = condition;
		this.organ = organ;
		this.stage = stage;
		this.performance_status = performance_status;
		this.date_exact_year = date_exact_year;
		this.date_exact_month = date_exact_month;
		this.date_exact_day = date_exact_day;
		this.date_interval_start_year = date_interval_start_year;
		this.date_interval_start_month = date_interval_start_month;
		this.date_interval_start_day = date_interval_start_day;
		this.date_interval_end_year = date_interval_end_year;
		this.date_interval_end_month = date_interval_end_month;
		this.date_interval_end_day = date_interval_end_day;
		this.date_until_year = date_until_year;
		this.date_until_month = date_until_month;
		this.date_until_day = date_until_day;
		this.statement = statement;
	}
	
	public condition_diagnosis() {
		super("");
		this.condition = "";
		this.organ = "";
		this.stage = "";
		this.performance_status = "";
		this.date_exact_year = "";
		this.date_exact_month = "";
		this.date_exact_day = "";
		this.date_interval_start_year = "";
		this.date_interval_start_month = "";
		this.date_interval_start_day = "";
		this.date_interval_end_year = "";
		this.date_interval_end_month = "";
		this.date_interval_end_day = "";
		this.date_until_year = "";
		this.date_until_month = "";
		this.date_until_day = "";
		this.statement = "";
	}
	
	public String getCondition() {
		return condition;
	}
	
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public String getOrgan() {
		return organ;
	}
	
	public void setOrgan(String organ) {
		this.organ = organ;
	}
	
	public String getStage() {
		return stage;
	}
	
	public void setStage(String stage) {
		this.stage = stage;
	}
	
	public String getPerformance_status() {
		return performance_status;
	}
	
	public void setPerformance_status(String performance_status) {
		this.performance_status = performance_status;
	}
	
	public String getDate_exact_year() {
		return date_exact_year;
	}
	
	public void setDate_exact_year(String date_exact_year) {
		this.date_exact_year = date_exact_year;
	}
	
	public String getDate_exact_month() {
		return date_exact_month;
	}
	
	public void setDate_exact_month(String date_exact_month) {
		this.date_exact_month = date_exact_month;
	}
	
	public String getDate_exact_day() {
		return date_exact_day;
	}
	
	public void setDate_exact_day(String date_exact_day) {
		this.date_exact_day = date_exact_day;
	}
	
	public String getDate_interval_start_year() {
		return date_interval_start_year;
	}
	
	public void setDate_interval_start_year(String date_interval_start_year) {
		this.date_interval_start_year = date_interval_start_year;
	}
	
	public String getDate_interval_start_month() {
		return date_interval_start_month;
	}
	
	public void setDate_interval_start_month(String date_interval_start_month) {
		this.date_interval_start_month = date_interval_start_month;
	}
	
	public String getDate_interval_start_day() {
		return date_interval_start_day;
	}
	
	public void setDate_interval_start_day(String date_interval_start_day) {
		this.date_interval_start_day = date_interval_start_day;
	}
	
	public String getDate_interval_end_year() {
		return date_interval_end_year;
	}
	
	public void setDate_interval_end_year(String date_interval_end_year) {
		this.date_interval_end_year = date_interval_end_year;
	}
	
	public String getDate_interval_end_month() {
		return date_interval_end_month;
	}
	
	public void setDate_interval_end_month(String date_interval_end_month) {
		this.date_interval_end_month = date_interval_end_month;
	}
	
	public String getDate_interval_end_day() {
		return date_interval_end_day;
	}
	
	public void setDate_interval_end_day(String date_interval_end_day) {
		this.date_interval_end_day = date_interval_end_day;
	}
	
	public String getDate_until_year() {
		return date_until_year;
	}
	
	public void setDate_until_year(String date_until_year) {
		this.date_until_year = date_until_year;
	}
	
	public String getDate_until_month() {
		return date_until_month;
	}
	
	public void setDate_until_month(String date_until_month) {
		this.date_until_month = date_until_month;
	}
	
	public String getDate_until_day() {
		return date_until_day;
	}
	
	public void setDate_until_day(String date_until_day) {
		this.date_until_day = date_until_day;
	}	
	
	public String getStatement() {
		return statement;
	}
	
	public void setStatement(String statement) {
		this.statement = statement;
	}
}