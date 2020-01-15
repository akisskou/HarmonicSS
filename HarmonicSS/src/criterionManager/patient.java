package criterionManager;

public class patient extends Criterion{

	public String birth_period_of_time_exact_year = "";				//dt_date				
	public String birth_period_of_time_exact_month = "";				//dt_date		
	public String birth_period_of_time_exact_day = "";				//dt_date				
	
	public String birth_period_of_time_interval_start_year = "";		//dt_date				
	public String birth_period_of_time_interval_start_month = "";		//dt_date	
	public String birth_period_of_time_interval_start_day = "";		//dt_date				
	
	public String birth_period_of_time_interval_end_year = "";		//dt_date			
	public String birth_period_of_time_interval_end_month = "";		//dt_date				
	public String birth_period_of_time_interval_end_day = "";			//dt_date			
	
	public String birth_period_of_time_until_year = "";				//dt_date
	public String birth_period_of_time_until_month = "";				//dt_date
	public String birth_period_of_time_until_day = "";				//dt_date
	
	
	public String symptoms_onset_period_of_time_exact_year = "";				//dt_date				
	public String symptoms_onset_period_of_time_exact_month = "";				//dt_date		
	public String symptoms_onset_period_of_time_exact_day = "";				//dt_date				
	
	public String symptoms_onset_period_of_time_interval_start_year = "";		//dt_date				
	public String symptoms_onset_period_of_time_interval_start_month = "";		//dt_date	
	public String symptoms_onset_period_of_time_interval_start_day = "";		//dt_date				
	
	public String symptoms_onset_period_of_time_interval_end_year = "";		//dt_date			
	public String symptoms_onset_period_of_time_interval_end_month = "";		//dt_date				
	public String symptoms_onset_period_of_time_interval_end_day = "";			//dt_date			
	
	public String symptoms_onset_period_of_time_until_year = "";				//dt_date
	public String symptoms_onset_period_of_time_until_month = "";				//dt_date
	public String symptoms_onset_period_of_time_until_day = "";				//dt_date

	
	public String diagnosis_period_of_time_exact_year = "";				//dt_date				
	public String diagnosis_period_of_time_exact_month = "";				//dt_date		
	public String diagnosis_period_of_time_exact_day = "";				//dt_date				
	
	public String diagnosis_period_of_time_interval_start_year = "";		//dt_date				
	public String diagnosis_period_of_time_interval_start_month = "";		//dt_date	
	public String diagnosis_period_of_time_interval_start_day = "";		//dt_date				
	
	public String diagnosis_period_of_time_interval_end_year = "";		//dt_date			
	public String diagnosis_period_of_time_interval_end_month = "";		//dt_date				
	public String diagnosis_period_of_time_interval_end_day = "";			//dt_date			
	
	public String diagnosis_period_of_time_until_year = "";				//dt_date
	public String diagnosis_period_of_time_until_month = "";				//dt_date
	public String diagnosis_period_of_time_until_day = "";				//dt_date

	
	public String cohort_inclusion_period_of_time_exact_year = "";				//dt_date				
	public String cohort_inclusion_period_of_time_exact_month = "";				//dt_date		
	public String cohort_inclusion_period_of_time_exact_day = "";				//dt_date				
	
	public String cohort_inclusion_period_of_time_interval_start_year = "";		//dt_date				
	public String cohort_inclusion_period_of_time_interval_start_month = "";		//dt_date	
	public String cohort_inclusion_period_of_time_interval_start_day = "";		//dt_date				
	
	public String cohort_inclusion_period_of_time_interval_end_year = "";		//dt_date			
	public String cohort_inclusion_period_of_time_interval_end_month = "";		//dt_date				
	public String cohort_inclusion_period_of_time_interval_end_day = "";			//dt_date			
	
	public String cohort_inclusion_period_of_time_until_year = "";				//dt_date
	public String cohort_inclusion_period_of_time_until_month = "";				//dt_date
	public String cohort_inclusion_period_of_time_until_day = "";				//dt_date
	
	
	
	
	public patient(String criterion_name, String birth_period_of_time_exact_year,
			String birth_period_of_time_exact_month, String birth_period_of_time_exact_day,
			String birth_period_of_time_interval_start_year, String birth_period_of_time_interval_start_month,
			String birth_period_of_time_interval_start_day, String birth_period_of_time_interval_end_year,
			String birth_period_of_time_interval_end_month, String birth_period_of_time_interval_end_day,
			String birth_period_of_time_until_year, String birth_period_of_time_until_month,
			String birth_period_of_time_until_day, String symptoms_onset_period_of_time_exact_year,
			String symptoms_onset_period_of_time_exact_month, String symptoms_onset_period_of_time_exact_day,
			String symptoms_onset_period_of_time_interval_start_year,
			String symptoms_onset_period_of_time_interval_start_month,
			String symptoms_onset_period_of_time_interval_start_day,
			String symptoms_onset_period_of_time_interval_end_year,
			String symptoms_onset_period_of_time_interval_end_month,
			String symptoms_onset_period_of_time_interval_end_day, String symptoms_onset_period_of_time_until_year,
			String symptoms_onset_period_of_time_until_month, String symptoms_onset_period_of_time_until_day,
			String diagnosis_period_of_time_exact_year, String diagnosis_period_of_time_exact_month,
			String diagnosis_period_of_time_exact_day, String diagnosis_period_of_time_interval_start_year,
			String diagnosis_period_of_time_interval_start_month, String diagnosis_period_of_time_interval_start_day,
			String diagnosis_period_of_time_interval_end_year, String diagnosis_period_of_time_interval_end_month,
			String diagnosis_period_of_time_interval_end_day, String diagnosis_period_of_time_until_year,
			String diagnosis_period_of_time_until_month, String diagnosis_period_of_time_until_day,
			String cohort_inclusion_period_of_time_exact_year, String cohort_inclusion_period_of_time_exact_month,
			String cohort_inclusion_period_of_time_exact_day,
			String cohort_inclusion_period_of_time_interval_start_year,
			String cohort_inclusion_period_of_time_interval_start_month,
			String cohort_inclusion_period_of_time_interval_start_day,
			String cohort_inclusion_period_of_time_interval_end_year,
			String cohort_inclusion_period_of_time_interval_end_month,
			String cohort_inclusion_period_of_time_interval_end_day, String cohort_inclusion_period_of_time_until_year,
			String cohort_inclusion_period_of_time_until_month, String cohort_inclusion_period_of_time_until_day) {
		super(criterion_name);
		this.birth_period_of_time_exact_year = birth_period_of_time_exact_year;
		this.birth_period_of_time_exact_month = birth_period_of_time_exact_month;
		this.birth_period_of_time_exact_day = birth_period_of_time_exact_day;
		this.birth_period_of_time_interval_start_year = birth_period_of_time_interval_start_year;
		this.birth_period_of_time_interval_start_month = birth_period_of_time_interval_start_month;
		this.birth_period_of_time_interval_start_day = birth_period_of_time_interval_start_day;
		this.birth_period_of_time_interval_end_year = birth_period_of_time_interval_end_year;
		this.birth_period_of_time_interval_end_month = birth_period_of_time_interval_end_month;
		this.birth_period_of_time_interval_end_day = birth_period_of_time_interval_end_day;
		this.birth_period_of_time_until_year = birth_period_of_time_until_year;
		this.birth_period_of_time_until_month = birth_period_of_time_until_month;
		this.birth_period_of_time_until_day = birth_period_of_time_until_day;
		this.symptoms_onset_period_of_time_exact_year = symptoms_onset_period_of_time_exact_year;
		this.symptoms_onset_period_of_time_exact_month = symptoms_onset_period_of_time_exact_month;
		this.symptoms_onset_period_of_time_exact_day = symptoms_onset_period_of_time_exact_day;
		this.symptoms_onset_period_of_time_interval_start_year = symptoms_onset_period_of_time_interval_start_year;
		this.symptoms_onset_period_of_time_interval_start_month = symptoms_onset_period_of_time_interval_start_month;
		this.symptoms_onset_period_of_time_interval_start_day = symptoms_onset_period_of_time_interval_start_day;
		this.symptoms_onset_period_of_time_interval_end_year = symptoms_onset_period_of_time_interval_end_year;
		this.symptoms_onset_period_of_time_interval_end_month = symptoms_onset_period_of_time_interval_end_month;
		this.symptoms_onset_period_of_time_interval_end_day = symptoms_onset_period_of_time_interval_end_day;
		this.symptoms_onset_period_of_time_until_year = symptoms_onset_period_of_time_until_year;
		this.symptoms_onset_period_of_time_until_month = symptoms_onset_period_of_time_until_month;
		this.symptoms_onset_period_of_time_until_day = symptoms_onset_period_of_time_until_day;
		this.diagnosis_period_of_time_exact_year = diagnosis_period_of_time_exact_year;
		this.diagnosis_period_of_time_exact_month = diagnosis_period_of_time_exact_month;
		this.diagnosis_period_of_time_exact_day = diagnosis_period_of_time_exact_day;
		this.diagnosis_period_of_time_interval_start_year = diagnosis_period_of_time_interval_start_year;
		this.diagnosis_period_of_time_interval_start_month = diagnosis_period_of_time_interval_start_month;
		this.diagnosis_period_of_time_interval_start_day = diagnosis_period_of_time_interval_start_day;
		this.diagnosis_period_of_time_interval_end_year = diagnosis_period_of_time_interval_end_year;
		this.diagnosis_period_of_time_interval_end_month = diagnosis_period_of_time_interval_end_month;
		this.diagnosis_period_of_time_interval_end_day = diagnosis_period_of_time_interval_end_day;
		this.diagnosis_period_of_time_until_year = diagnosis_period_of_time_until_year;
		this.diagnosis_period_of_time_until_month = diagnosis_period_of_time_until_month;
		this.diagnosis_period_of_time_until_day = diagnosis_period_of_time_until_day;
		this.cohort_inclusion_period_of_time_exact_year = cohort_inclusion_period_of_time_exact_year;
		this.cohort_inclusion_period_of_time_exact_month = cohort_inclusion_period_of_time_exact_month;
		this.cohort_inclusion_period_of_time_exact_day = cohort_inclusion_period_of_time_exact_day;
		this.cohort_inclusion_period_of_time_interval_start_year = cohort_inclusion_period_of_time_interval_start_year;
		this.cohort_inclusion_period_of_time_interval_start_month = cohort_inclusion_period_of_time_interval_start_month;
		this.cohort_inclusion_period_of_time_interval_start_day = cohort_inclusion_period_of_time_interval_start_day;
		this.cohort_inclusion_period_of_time_interval_end_year = cohort_inclusion_period_of_time_interval_end_year;
		this.cohort_inclusion_period_of_time_interval_end_month = cohort_inclusion_period_of_time_interval_end_month;
		this.cohort_inclusion_period_of_time_interval_end_day = cohort_inclusion_period_of_time_interval_end_day;
		this.cohort_inclusion_period_of_time_until_year = cohort_inclusion_period_of_time_until_year;
		this.cohort_inclusion_period_of_time_until_month = cohort_inclusion_period_of_time_until_month;
		this.cohort_inclusion_period_of_time_until_day = cohort_inclusion_period_of_time_until_day;
	}
	
	
	public patient() {
		super("");
		this.birth_period_of_time_exact_year = "";
		this.birth_period_of_time_exact_month = "";
		this.birth_period_of_time_exact_day = "";
		this.birth_period_of_time_interval_start_year = "";
		this.birth_period_of_time_interval_start_month = "";
		this.birth_period_of_time_interval_start_day = "";
		this.birth_period_of_time_interval_end_year = "";
		this.birth_period_of_time_interval_end_month = "";
		this.birth_period_of_time_interval_end_day = "";
		this.birth_period_of_time_until_year = "";
		this.birth_period_of_time_until_month = "";
		this.birth_period_of_time_until_day = "";
		this.symptoms_onset_period_of_time_exact_year = "";
		this.symptoms_onset_period_of_time_exact_month = "";
		this.symptoms_onset_period_of_time_exact_day = "";
		this.symptoms_onset_period_of_time_interval_start_year = "";
		this.symptoms_onset_period_of_time_interval_start_month = "";
		this.symptoms_onset_period_of_time_interval_start_day = "";
		this.symptoms_onset_period_of_time_interval_end_year = "";
		this.symptoms_onset_period_of_time_interval_end_month = "";
		this.symptoms_onset_period_of_time_interval_end_day = "";
		this.symptoms_onset_period_of_time_until_year = "";
		this.symptoms_onset_period_of_time_until_month = "";
		this.symptoms_onset_period_of_time_until_day = "";
		this.diagnosis_period_of_time_exact_year = "";
		this.diagnosis_period_of_time_exact_month = "";
		this.diagnosis_period_of_time_exact_day = "";
		this.diagnosis_period_of_time_interval_start_year = "";
		this.diagnosis_period_of_time_interval_start_month = "";
		this.diagnosis_period_of_time_interval_start_day = "";
		this.diagnosis_period_of_time_interval_end_year = "";
		this.diagnosis_period_of_time_interval_end_month = "";
		this.diagnosis_period_of_time_interval_end_day = "";
		this.diagnosis_period_of_time_until_year = "";
		this.diagnosis_period_of_time_until_month = "";
		this.diagnosis_period_of_time_until_day = "";
		this.cohort_inclusion_period_of_time_exact_year = "";
		this.cohort_inclusion_period_of_time_exact_month = "";
		this.cohort_inclusion_period_of_time_exact_day = "";
		this.cohort_inclusion_period_of_time_interval_start_year = "";
		this.cohort_inclusion_period_of_time_interval_start_month = "";
		this.cohort_inclusion_period_of_time_interval_start_day = "";
		this.cohort_inclusion_period_of_time_interval_end_year = "";
		this.cohort_inclusion_period_of_time_interval_end_month = "";
		this.cohort_inclusion_period_of_time_interval_end_day = "";
		this.cohort_inclusion_period_of_time_until_year = "";
		this.cohort_inclusion_period_of_time_until_month = "";
		this.cohort_inclusion_period_of_time_until_day = "";
	}
	
	public String getBirth_period_of_time_exact_year() {
		return birth_period_of_time_exact_year;
	}
	public void setBirth_period_of_time_exact_year(String birth_period_of_time_exact_year) {
		this.birth_period_of_time_exact_year = birth_period_of_time_exact_year;
	}
	public String getBirth_period_of_time_exact_month() {
		return birth_period_of_time_exact_month;
	}
	public void setBirth_period_of_time_exact_month(String birth_period_of_time_exact_month) {
		this.birth_period_of_time_exact_month = birth_period_of_time_exact_month;
	}
	public String getBirth_period_of_time_exact_day() {
		return birth_period_of_time_exact_day;
	}
	public void setBirth_period_of_time_exact_day(String birth_period_of_time_exact_day) {
		this.birth_period_of_time_exact_day = birth_period_of_time_exact_day;
	}
	public String getBirth_period_of_time_interval_start_year() {
		return birth_period_of_time_interval_start_year;
	}
	public void setBirth_period_of_time_interval_start_year(String birth_period_of_time_interval_start_year) {
		this.birth_period_of_time_interval_start_year = birth_period_of_time_interval_start_year;
	}
	public String getBirth_period_of_time_interval_start_month() {
		return birth_period_of_time_interval_start_month;
	}
	public void setBirth_period_of_time_interval_start_month(String birth_period_of_time_interval_start_month) {
		this.birth_period_of_time_interval_start_month = birth_period_of_time_interval_start_month;
	}
	public String getBirth_period_of_time_interval_start_day() {
		return birth_period_of_time_interval_start_day;
	}
	public void setBirth_period_of_time_interval_start_day(String birth_period_of_time_interval_start_day) {
		this.birth_period_of_time_interval_start_day = birth_period_of_time_interval_start_day;
	}
	public String getBirth_period_of_time_interval_end_year() {
		return birth_period_of_time_interval_end_year;
	}
	public void setBirth_period_of_time_interval_end_year(String birth_period_of_time_interval_end_year) {
		this.birth_period_of_time_interval_end_year = birth_period_of_time_interval_end_year;
	}
	public String getBirth_period_of_time_interval_end_month() {
		return birth_period_of_time_interval_end_month;
	}
	public void setBirth_period_of_time_interval_end_month(String birth_period_of_time_interval_end_month) {
		this.birth_period_of_time_interval_end_month = birth_period_of_time_interval_end_month;
	}
	public String getBirth_period_of_time_interval_end_day() {
		return birth_period_of_time_interval_end_day;
	}
	public void setBirth_period_of_time_interval_end_day(String birth_period_of_time_interval_end_day) {
		this.birth_period_of_time_interval_end_day = birth_period_of_time_interval_end_day;
	}
	public String getBirth_period_of_time_until_year() {
		return birth_period_of_time_until_year;
	}
	public void setBirth_period_of_time_until_year(String birth_period_of_time_until_year) {
		this.birth_period_of_time_until_year = birth_period_of_time_until_year;
	}
	public String getBirth_period_of_time_until_month() {
		return birth_period_of_time_until_month;
	}
	public void setBirth_period_of_time_until_month(String birth_period_of_time_until_month) {
		this.birth_period_of_time_until_month = birth_period_of_time_until_month;
	}
	public String getBirth_period_of_time_until_day() {
		return birth_period_of_time_until_day;
	}
	public void setBirth_period_of_time_until_day(String birth_period_of_time_until_day) {
		this.birth_period_of_time_until_day = birth_period_of_time_until_day;
	}
	public String getSymptoms_onset_period_of_time_exact_year() {
		return symptoms_onset_period_of_time_exact_year;
	}
	public void setSymptoms_onset_period_of_time_exact_year(String symptoms_onset_period_of_time_exact_year) {
		this.symptoms_onset_period_of_time_exact_year = symptoms_onset_period_of_time_exact_year;
	}
	public String getSymptoms_onset_period_of_time_exact_month() {
		return symptoms_onset_period_of_time_exact_month;
	}
	public void setSymptoms_onset_period_of_time_exact_month(String symptoms_onset_period_of_time_exact_month) {
		this.symptoms_onset_period_of_time_exact_month = symptoms_onset_period_of_time_exact_month;
	}
	public String getSymptoms_onset_period_of_time_exact_day() {
		return symptoms_onset_period_of_time_exact_day;
	}
	public void setSymptoms_onset_period_of_time_exact_day(String symptoms_onset_period_of_time_exact_day) {
		this.symptoms_onset_period_of_time_exact_day = symptoms_onset_period_of_time_exact_day;
	}
	public String getSymptoms_onset_period_of_time_interval_start_year() {
		return symptoms_onset_period_of_time_interval_start_year;
	}
	public void setSymptoms_onset_period_of_time_interval_start_year(
			String symptoms_onset_period_of_time_interval_start_year) {
		this.symptoms_onset_period_of_time_interval_start_year = symptoms_onset_period_of_time_interval_start_year;
	}
	public String getSymptoms_onset_period_of_time_interval_start_month() {
		return symptoms_onset_period_of_time_interval_start_month;
	}
	public void setSymptoms_onset_period_of_time_interval_start_month(
			String symptoms_onset_period_of_time_interval_start_month) {
		this.symptoms_onset_period_of_time_interval_start_month = symptoms_onset_period_of_time_interval_start_month;
	}
	public String getSymptoms_onset_period_of_time_interval_start_day() {
		return symptoms_onset_period_of_time_interval_start_day;
	}
	public void setSymptoms_onset_period_of_time_interval_start_day(
			String symptoms_onset_period_of_time_interval_start_day) {
		this.symptoms_onset_period_of_time_interval_start_day = symptoms_onset_period_of_time_interval_start_day;
	}
	public String getSymptoms_onset_period_of_time_interval_end_year() {
		return symptoms_onset_period_of_time_interval_end_year;
	}
	public void setSymptoms_onset_period_of_time_interval_end_year(String symptoms_onset_period_of_time_interval_end_year) {
		this.symptoms_onset_period_of_time_interval_end_year = symptoms_onset_period_of_time_interval_end_year;
	}
	public String getSymptoms_onset_period_of_time_interval_end_month() {
		return symptoms_onset_period_of_time_interval_end_month;
	}
	public void setSymptoms_onset_period_of_time_interval_end_month(
			String symptoms_onset_period_of_time_interval_end_month) {
		this.symptoms_onset_period_of_time_interval_end_month = symptoms_onset_period_of_time_interval_end_month;
	}
	public String getSymptoms_onset_period_of_time_interval_end_day() {
		return symptoms_onset_period_of_time_interval_end_day;
	}
	public void setSymptoms_onset_period_of_time_interval_end_day(String symptoms_onset_period_of_time_interval_end_day) {
		this.symptoms_onset_period_of_time_interval_end_day = symptoms_onset_period_of_time_interval_end_day;
	}
	public String getSymptoms_onset_period_of_time_until_year() {
		return symptoms_onset_period_of_time_until_year;
	}
	public void setSymptoms_onset_period_of_time_until_year(String symptoms_onset_period_of_time_until_year) {
		this.symptoms_onset_period_of_time_until_year = symptoms_onset_period_of_time_until_year;
	}
	public String getSymptoms_onset_period_of_time_until_month() {
		return symptoms_onset_period_of_time_until_month;
	}
	public void setSymptoms_onset_period_of_time_until_month(String symptoms_onset_period_of_time_until_month) {
		this.symptoms_onset_period_of_time_until_month = symptoms_onset_period_of_time_until_month;
	}
	public String getSymptoms_onset_period_of_time_until_day() {
		return symptoms_onset_period_of_time_until_day;
	}
	public void setSymptoms_onset_period_of_time_until_day(String symptoms_onset_period_of_time_until_day) {
		this.symptoms_onset_period_of_time_until_day = symptoms_onset_period_of_time_until_day;
	}
	public String getDiagnosis_period_of_time_exact_year() {
		return diagnosis_period_of_time_exact_year;
	}
	public void setDiagnosis_period_of_time_exact_year(String diagnosis_period_of_time_exact_year) {
		this.diagnosis_period_of_time_exact_year = diagnosis_period_of_time_exact_year;
	}
	public String getDiagnosis_period_of_time_exact_month() {
		return diagnosis_period_of_time_exact_month;
	}
	public void setDiagnosis_period_of_time_exact_month(String diagnosis_period_of_time_exact_month) {
		this.diagnosis_period_of_time_exact_month = diagnosis_period_of_time_exact_month;
	}
	public String getDiagnosis_period_of_time_exact_day() {
		return diagnosis_period_of_time_exact_day;
	}
	public void setDiagnosis_period_of_time_exact_day(String diagnosis_period_of_time_exact_day) {
		this.diagnosis_period_of_time_exact_day = diagnosis_period_of_time_exact_day;
	}
	public String getDiagnosis_period_of_time_interval_start_year() {
		return diagnosis_period_of_time_interval_start_year;
	}
	public void setDiagnosis_period_of_time_interval_start_year(String diagnosis_period_of_time_interval_start_year) {
		this.diagnosis_period_of_time_interval_start_year = diagnosis_period_of_time_interval_start_year;
	}
	public String getDiagnosis_period_of_time_interval_start_month() {
		return diagnosis_period_of_time_interval_start_month;
	}
	public void setDiagnosis_period_of_time_interval_start_month(String diagnosis_period_of_time_interval_start_month) {
		this.diagnosis_period_of_time_interval_start_month = diagnosis_period_of_time_interval_start_month;
	}
	public String getDiagnosis_period_of_time_interval_start_day() {
		return diagnosis_period_of_time_interval_start_day;
	}
	public void setDiagnosis_period_of_time_interval_start_day(String diagnosis_period_of_time_interval_start_day) {
		this.diagnosis_period_of_time_interval_start_day = diagnosis_period_of_time_interval_start_day;
	}
	public String getDiagnosis_period_of_time_interval_end_year() {
		return diagnosis_period_of_time_interval_end_year;
	}
	public void setDiagnosis_period_of_time_interval_end_year(String diagnosis_period_of_time_interval_end_year) {
		this.diagnosis_period_of_time_interval_end_year = diagnosis_period_of_time_interval_end_year;
	}
	public String getDiagnosis_period_of_time_interval_end_month() {
		return diagnosis_period_of_time_interval_end_month;
	}
	public void setDiagnosis_period_of_time_interval_end_month(String diagnosis_period_of_time_interval_end_month) {
		this.diagnosis_period_of_time_interval_end_month = diagnosis_period_of_time_interval_end_month;
	}
	public String getDiagnosis_period_of_time_interval_end_day() {
		return diagnosis_period_of_time_interval_end_day;
	}
	public void setDiagnosis_period_of_time_interval_end_day(String diagnosis_period_of_time_interval_end_day) {
		this.diagnosis_period_of_time_interval_end_day = diagnosis_period_of_time_interval_end_day;
	}
	public String getDiagnosis_period_of_time_until_year() {
		return diagnosis_period_of_time_until_year;
	}
	public void setDiagnosis_period_of_time_until_year(String diagnosis_period_of_time_until_year) {
		this.diagnosis_period_of_time_until_year = diagnosis_period_of_time_until_year;
	}
	public String getDiagnosis_period_of_time_until_month() {
		return diagnosis_period_of_time_until_month;
	}
	public void setDiagnosis_period_of_time_until_month(String diagnosis_period_of_time_until_month) {
		this.diagnosis_period_of_time_until_month = diagnosis_period_of_time_until_month;
	}
	public String getDiagnosis_period_of_time_until_day() {
		return diagnosis_period_of_time_until_day;
	}
	public void setDiagnosis_period_of_time_until_day(String diagnosis_period_of_time_until_day) {
		this.diagnosis_period_of_time_until_day = diagnosis_period_of_time_until_day;
	}
	public String getCohort_inclusion_period_of_time_exact_year() {
		return cohort_inclusion_period_of_time_exact_year;
	}
	public void setCohort_inclusion_period_of_time_exact_year(String cohort_inclusion_period_of_time_exact_year) {
		this.cohort_inclusion_period_of_time_exact_year = cohort_inclusion_period_of_time_exact_year;
	}
	public String getCohort_inclusion_period_of_time_exact_month() {
		return cohort_inclusion_period_of_time_exact_month;
	}
	public void setCohort_inclusion_period_of_time_exact_month(String cohort_inclusion_period_of_time_exact_month) {
		this.cohort_inclusion_period_of_time_exact_month = cohort_inclusion_period_of_time_exact_month;
	}
	public String getCohort_inclusion_period_of_time_exact_day() {
		return cohort_inclusion_period_of_time_exact_day;
	}
	public void setCohort_inclusion_period_of_time_exact_day(String cohort_inclusion_period_of_time_exact_day) {
		this.cohort_inclusion_period_of_time_exact_day = cohort_inclusion_period_of_time_exact_day;
	}
	public String getCohort_inclusion_period_of_time_interval_start_year() {
		return cohort_inclusion_period_of_time_interval_start_year;
	}
	public void setCohort_inclusion_period_of_time_interval_start_year(
			String cohort_inclusion_period_of_time_interval_start_year) {
		this.cohort_inclusion_period_of_time_interval_start_year = cohort_inclusion_period_of_time_interval_start_year;
	}
	public String getCohort_inclusion_period_of_time_interval_start_month() {
		return cohort_inclusion_period_of_time_interval_start_month;
	}
	public void setCohort_inclusion_period_of_time_interval_start_month(
			String cohort_inclusion_period_of_time_interval_start_month) {
		this.cohort_inclusion_period_of_time_interval_start_month = cohort_inclusion_period_of_time_interval_start_month;
	}
	public String getCohort_inclusion_period_of_time_interval_start_day() {
		return cohort_inclusion_period_of_time_interval_start_day;
	}
	public void setCohort_inclusion_period_of_time_interval_start_day(
			String cohort_inclusion_period_of_time_interval_start_day) {
		this.cohort_inclusion_period_of_time_interval_start_day = cohort_inclusion_period_of_time_interval_start_day;
	}
	public String getCohort_inclusion_period_of_time_interval_end_year() {
		return cohort_inclusion_period_of_time_interval_end_year;
	}
	public void setCohort_inclusion_period_of_time_interval_end_year(
			String cohort_inclusion_period_of_time_interval_end_year) {
		this.cohort_inclusion_period_of_time_interval_end_year = cohort_inclusion_period_of_time_interval_end_year;
	}
	public String getCohort_inclusion_period_of_time_interval_end_month() {
		return cohort_inclusion_period_of_time_interval_end_month;
	}
	public void setCohort_inclusion_period_of_time_interval_end_month(
			String cohort_inclusion_period_of_time_interval_end_month) {
		this.cohort_inclusion_period_of_time_interval_end_month = cohort_inclusion_period_of_time_interval_end_month;
	}
	public String getCohort_inclusion_period_of_time_interval_end_day() {
		return cohort_inclusion_period_of_time_interval_end_day;
	}
	public void setCohort_inclusion_period_of_time_interval_end_day(
			String cohort_inclusion_period_of_time_interval_end_day) {
		this.cohort_inclusion_period_of_time_interval_end_day = cohort_inclusion_period_of_time_interval_end_day;
	}
	public String getCohort_inclusion_period_of_time_until_year() {
		return cohort_inclusion_period_of_time_until_year;
	}
	public void setCohort_inclusion_period_of_time_until_year(String cohort_inclusion_period_of_time_until_year) {
		this.cohort_inclusion_period_of_time_until_year = cohort_inclusion_period_of_time_until_year;
	}
	public String getCohort_inclusion_period_of_time_until_month() {
		return cohort_inclusion_period_of_time_until_month;
	}
	public void setCohort_inclusion_period_of_time_until_month(String cohort_inclusion_period_of_time_until_month) {
		this.cohort_inclusion_period_of_time_until_month = cohort_inclusion_period_of_time_until_month;
	}
	public String getCohort_inclusion_period_of_time_until_day() {
		return cohort_inclusion_period_of_time_until_day;
	}
	public void setCohort_inclusion_period_of_time_until_day(String cohort_inclusion_period_of_time_until_day) {
		this.cohort_inclusion_period_of_time_until_day = cohort_inclusion_period_of_time_until_day;
	}

	
}
