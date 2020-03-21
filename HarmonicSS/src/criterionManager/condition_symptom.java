package criterionManager;

public class condition_symptom extends Criterion {

	public String sign_coded = "";						//Column: CONDITION_ID voc_symptom_sign Do we also need BROADER_TERM_ID and CATEGORY_ID in our queries?
	public String sign_date_exact_year = "";			//Column: OBSERVE_DATE_ID
	public String sign_date_exact_month = "";			//Column: OBSERVE_DATE_ID
	public String sign_date_exact_day = "";				//Column: OBSERVE_DATE_ID
	public String sign_date_interval_start_year = "";
	public String sign_date_interval_start_month = "";
	public String sign_date_interval_start_day = "";
	public String sign_date_interval_end_year = "";
	public String sign_date_interval_end_month = "";
	public String sign_date_interval_end_day = "";
	public String sign_date_until_year = "";
	public String sign_date_until_month = "";
	public String sign_date_until_day = "";	
	public String statement = "";
	public String count = "";
	
	
	public condition_symptom(String criterion_name, String voc_symptom_sign_CODE, String OBSERVE_DATE_ID_dt_date_YEAR, String OBSERVE_DATE_ID_dt_date_MONTH, 
			String OBSERVE_DATE_ID_dt_date_DAY, 
			String observe_period_begin_year, String observe_period_begin_month, String observe_period_begin_day, String observe_period_end_year,
			String observe_period_end_month, String observe_period_end_day, String observe_until_date_year, String observe_until_date_month,
			String observe_until_date_day, String statement, String count) {
		super(criterion_name);
		this.sign_coded = voc_symptom_sign_CODE;										//Column: CONDITION_ID
		this.sign_date_exact_year = OBSERVE_DATE_ID_dt_date_YEAR;						//Column: OBSERVE_DATE_ID
		this.sign_date_exact_month = OBSERVE_DATE_ID_dt_date_MONTH;						//Column: OBSERVE_DATE_ID
		this.sign_date_exact_day = OBSERVE_DATE_ID_dt_date_DAY;							//Column: OBSERVE_DATE_ID
		this.sign_date_interval_start_year = observe_period_begin_year;
		this.sign_date_interval_start_month = observe_period_begin_month;
		this.sign_date_interval_start_day = observe_period_begin_day;
		this.sign_date_interval_end_year = observe_period_end_year;
		this.sign_date_interval_end_month = observe_period_end_month;
		this.sign_date_interval_end_day = observe_period_end_day;
		this.sign_date_until_year = observe_until_date_year;
		this.sign_date_until_month = observe_until_date_month;
		this.sign_date_until_day = observe_until_date_day;
		this.statement = statement;
		this.count = count;
	}

	public condition_symptom() {
		super("");
		this.sign_coded = "";							//Column: CONDITION_ID
		this.sign_date_exact_year = "";						//Column: OBSERVE_DATE_ID
		this.sign_date_exact_month = "";					//Column: OBSERVE_DATE_ID
		this.sign_date_exact_day = "";						//Column: OBSERVE_DATE_ID
		this.sign_date_interval_start_year = "";
		this.sign_date_interval_start_month = "";
		this.sign_date_interval_start_day = "";
		this.sign_date_interval_end_year = "";
		this.sign_date_interval_end_month = "";
		this.sign_date_interval_end_day = "";
		this.sign_date_until_year = "";
		this.sign_date_until_month = "";
		this.sign_date_until_day = "";
		this.statement = "";
		this.count = "";
	}

	public String getVoc_symptom_sign_CODE() {
		return sign_coded;
	}

	public void setVoc_symptom_sign_CODE(String voc_symptom_sign_CODE) {
		this.sign_coded = voc_symptom_sign_CODE;
	}

	public String getObserve_exact_date_YEAR() {
		return sign_date_exact_year;
	}

	public void setObserve_exact_date_YEAR(String observe_exact_date_YEAR) {
		this.sign_date_exact_year = observe_exact_date_YEAR;
	}

	public String getObserve_exact_date_MONTH() {
		return sign_date_exact_month;
	}

	public void setObserve_exact_date_MONTH(String observe_exact_date_MONTH) {
		this.sign_date_exact_month = observe_exact_date_MONTH;
	}

	public String getObserve_exact_date_DAY() {
		return sign_date_exact_day;
	}

	public void setObserve_exact_date_DAY(String observe_exact_date_DAY) {
		this.sign_date_exact_day = observe_exact_date_DAY;
	}

	public String getObserve_period_begin_year() {
		return sign_date_interval_start_year;
	}

	public void setObserve_period_begin_year(String observe_period_begin_year) {
		this.sign_date_interval_start_year = observe_period_begin_year;
	}

	public String getObserve_period_begin_month() {
		return sign_date_interval_start_month;
	}

	public void setObserve_period_begin_month(String observe_period_begin_month) {
		this.sign_date_interval_start_month = observe_period_begin_month;
	}

	public String getObserve_period_begin_day() {
		return sign_date_interval_start_day;
	}

	public void setObserve_period_begin_day(String observe_period_begin_day) {
		this.sign_date_interval_start_day = observe_period_begin_day;
	}

	public String getObserve_period_end_year() {
		return sign_date_interval_end_year;
	}

	public void setObserve_period_end_year(String observe_period_end_year) {
		this.sign_date_interval_end_year = observe_period_end_year;
	}

	public String getObserve_period_end_month() {
		return sign_date_interval_end_month;
	}

	public void setObserve_period_end_month(String observe_period_end_month) {
		this.sign_date_interval_end_month = observe_period_end_month;
	}

	public String getObserve_period_end_day() {
		return sign_date_interval_end_day;
	}

	public void setObserve_period_end_day(String observe_period_end_day) {
		this.sign_date_interval_end_day = observe_period_end_day;
	}

	public String getObserve_until_date_year() {
		return sign_date_until_year;
	}

	public void setObserve_until_date_year(String observe_until_date_year) {
		this.sign_date_until_year = observe_until_date_year;
	}

	public String getObserve_until_date_month() {
		return sign_date_until_month;
	}

	public void setObserve_until_date_month(String observe_until_date_month) {
		this.sign_date_until_month = observe_until_date_month;
	}

	public String getObserve_until_date_day() {
		return sign_date_until_day;
	}

	public void setObserve_until_date_day(String observe_until_date_day) {
		this.sign_date_until_day = observe_until_date_day;
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
}
