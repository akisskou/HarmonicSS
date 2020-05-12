package criterionManager;

import java.sql.Timestamp;
import java.util.Date;

public class patient extends Criterion{

	public String exact_age = "";
	public String min_age = "";
	public String max_age = "";
	public String exact_age_of_cohort_inclusion = "";
	public String min_age_of_cohort_inclusion = "";
	public String max_age_of_cohort_inclusion = "";
	public String exact_age_of_diagnosis = "";
	public String min_age_of_diagnosis = "";
	public String max_age_of_diagnosis = "";
	public String exact_age_of_sign = "";
	public String min_age_of_sign = "";
	public String max_age_of_sign = "";
	
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
	
	public String max_birth_year_nested = "";
	public String years_birth_nested = "";
	public String max_symptoms_onset_year_nested = "";
	public String min_symptoms_onset_year_nested = "";
	public String years_symptoms_onset_nested = "";
	public String max_diagnosis_year_nested = "";
	public String min_diagnosis_year_nested = "";
	public String years_diagnosis_nested = "";
	public String max_cohort_inclusion_year_nested = "";
	public String min_cohort_inclusion_year_nested = "";
	public String years_cohort_inclusion_nested = "";
	public String type_nested = "";
	public String start_period_symptoms_onset_year_nested = "";
	public String end_period_symptoms_onset_year_nested = "";
	public String start_period_diagnosis_year_nested = "";
	public String end_period_diagnosis_year_nested = "";
	public String start_period_cohort_inclusion_year_nested = "";
	public String end_period_cohort_inclusion_year_nested = "";
	
	
	
	public patient(String criterion_name, String exact_age, String min_age, String max_age, String exact_age_of_cohort_inclusion, String min_age_of_cohort_inclusion, String max_age_of_cohort_inclusion, String birth_period_of_time_exact_year,
			String exact_age_of_diagnosis, String min_age_of_diagnosis, String max_age_of_diagnosis,
			String exact_age_of_sign, String min_age_of_sign, String max_age_of_sign,
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
			String cohort_inclusion_period_of_time_until_month, String cohort_inclusion_period_of_time_until_day, String max_birth_year_nested, String years_birth_nested, String max_symptoms_onset_year_nested, String min_symptoms_onset_year_nested, String years_symptoms_onset_nested, String max_diagnosis_year_nested, String min_diagnosis_year_nested, String years_diagnosis_nested, String max_cohort_inclusion_year_nested, String min_cohort_inclusion_year_nested, String years_cohort_inclusion_nested, String type_nested, String start_period_symptoms_onset_year_nested, String end_period_symptoms_onset_year_nested, String start_period_diagnosis_year_nested, String end_period_diagnosis_year_nested, String start_period_cohort_inclusion_year_nested, String end_period_cohort_inclusion_year_nested) {
		super(criterion_name);
		this.exact_age = exact_age;
		this.min_age = min_age;
		this.max_age = max_age;
		this.exact_age_of_cohort_inclusion = exact_age_of_cohort_inclusion;
		this.min_age_of_cohort_inclusion = min_age_of_cohort_inclusion;
		this.max_age_of_cohort_inclusion = max_age_of_cohort_inclusion;
		this.exact_age_of_diagnosis = exact_age_of_diagnosis;
		this.min_age_of_diagnosis = min_age_of_diagnosis;
		this.max_age_of_diagnosis = max_age_of_diagnosis;
		this.exact_age_of_sign = exact_age_of_sign;
		this.min_age_of_sign = min_age_of_sign;
		this.max_age_of_sign = max_age_of_sign;
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
		this.max_birth_year_nested = max_birth_year_nested;
		this.years_birth_nested = years_birth_nested;
		this.max_symptoms_onset_year_nested = max_symptoms_onset_year_nested;
		this.min_symptoms_onset_year_nested = min_symptoms_onset_year_nested;
		this.years_symptoms_onset_nested = years_symptoms_onset_nested;
		this.max_diagnosis_year_nested = max_diagnosis_year_nested;
		this.min_diagnosis_year_nested = min_diagnosis_year_nested;
		this.years_diagnosis_nested = years_diagnosis_nested;
		this.max_cohort_inclusion_year_nested = max_cohort_inclusion_year_nested;
		this.min_cohort_inclusion_year_nested = min_cohort_inclusion_year_nested;
		this.years_cohort_inclusion_nested = years_cohort_inclusion_nested;
		this.type_nested = type_nested;
		this.start_period_symptoms_onset_year_nested = start_period_symptoms_onset_year_nested;
		this.end_period_symptoms_onset_year_nested = end_period_symptoms_onset_year_nested;
		this.start_period_diagnosis_year_nested = start_period_diagnosis_year_nested;
		this.end_period_diagnosis_year_nested = end_period_diagnosis_year_nested;
		this.start_period_cohort_inclusion_year_nested = start_period_cohort_inclusion_year_nested;
		this.end_period_cohort_inclusion_year_nested = end_period_cohort_inclusion_year_nested;
	}
	
	
	public patient() {
		super("");
		this.exact_age = "";
		this.min_age = "";
		this.max_age = "";
		this.exact_age_of_cohort_inclusion = "";
		this.min_age_of_cohort_inclusion = "";
		this.max_age_of_cohort_inclusion = "";
		this.exact_age_of_diagnosis = "";
		this.min_age_of_diagnosis = "";
		this.max_age_of_diagnosis = "";
		this.exact_age_of_sign = "";
		this.min_age_of_sign = "";
		this.max_age_of_sign = "";
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
		this.max_birth_year_nested = "";
		this.years_birth_nested = "";
		this.max_symptoms_onset_year_nested = "";
		this.min_symptoms_onset_year_nested = "";
		this.years_symptoms_onset_nested = "";
		this.max_diagnosis_year_nested = "";
		this.min_diagnosis_year_nested = "";
		this.years_diagnosis_nested = "";
		this.max_cohort_inclusion_year_nested = "";
		this.min_cohort_inclusion_year_nested = "";
		this.years_cohort_inclusion_nested = "";
		this.type_nested = "";
		this.start_period_symptoms_onset_year_nested = "";
		this.end_period_symptoms_onset_year_nested = "";
		this.start_period_diagnosis_year_nested = "";
		this.end_period_diagnosis_year_nested = "";
		this.start_period_cohort_inclusion_year_nested = "";
		this.end_period_cohort_inclusion_year_nested = "";
	}
	
	public String get_exact_age_of_diagnosis() {
		return exact_age_of_diagnosis;
	}
	public String get_min_age_of_diagnosis() {
		return min_age_of_diagnosis;
	}
	public String get_max_age_of_diagnosis() {
		return max_age_of_diagnosis;
	}
	public String get_exact_age() {
		return exact_age;
	}
	public String get_min_age() {
		return min_age;
	}
	public String get_max_age() {
		return max_age;
	}
	public String get_exact_age_of_cohort_inclusion() {
		return exact_age_of_cohort_inclusion;
	}
	public String get_min_age_of_cohort_inclusion() {
		return min_age_of_cohort_inclusion;
	}
	public String get_max_age_of_cohort_inclusion() {
		return max_age_of_cohort_inclusion;
	}
	public String get_exact_age_of_sign() {
		return exact_age_of_sign;
	}
	public String get_min_age_of_sign() {
		return min_age_of_sign;
	}
	public String get_max_age_of_sign() {
		return max_age_of_sign;
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
	
	public String get_max_birth_year_nested() {
		return max_birth_year_nested;
	}
	public void set_max_birth_year_nested(String max_birth_year_nested) {
		this.max_birth_year_nested = max_birth_year_nested;
	}
	
	public String get_years_birth_nested() {
		return years_birth_nested;
	}
	public void set_years_birth_nested(String years_birth_nested) {
		this.years_birth_nested = years_birth_nested;
	}
	
	public String get_max_symptoms_onset_year_nested() {
		return max_symptoms_onset_year_nested;
	}
	public void set_max_symptoms_onset_year_nested(String max_symptoms_onset_year_nested) {
		this.max_symptoms_onset_year_nested = max_symptoms_onset_year_nested;
	}
	
	public String get_min_symptoms_onset_year_nested() {
		return min_symptoms_onset_year_nested;
	}
	public void set_min_symptoms_onset_year_nested(String min_symptoms_onset_year_nested) {
		this.min_symptoms_onset_year_nested = min_symptoms_onset_year_nested;
	}
	
	public String get_years_symptoms_onset_nested() {
		return years_symptoms_onset_nested;
	}
	public void set_years_symptoms_onset_nested(String years_symptoms_onset_nested) {
		this.years_symptoms_onset_nested = years_symptoms_onset_nested;
	}

	public String get_max_diagnosis_year_nested() {
		return max_diagnosis_year_nested;
	}
	public void set_max_diagnosis_year_nested(String max_diagnosis_year_nested) {
		this.max_diagnosis_year_nested = max_diagnosis_year_nested;
	}
	
	public String get_min_diagnosis_year_nested() {
		return min_diagnosis_year_nested;
	}
	public void set_min_diagnosis_year_nested(String min_diagnosis_year_nested) {
		this.min_diagnosis_year_nested = min_diagnosis_year_nested;
	}
	
	public String get_years_diagnosis_nested() {
		return years_diagnosis_nested;
	}
	public void set_years_diagnosis_nested(String years_diagnosis_nested) {
		this.years_diagnosis_nested = years_diagnosis_nested;
	}
	
	public String get_max_cohort_inclusion_year_nested() {
		return max_cohort_inclusion_year_nested;
	}
	public void set_max_cohort_inclusion_year_nested(String max_cohort_inclusion_year_nested) {
		this.max_cohort_inclusion_year_nested = max_cohort_inclusion_year_nested;
	}
	
	public String get_min_cohort_inclusion_year_nested() {
		return min_cohort_inclusion_year_nested;
	}
	public void set_min_cohort_inclusion_year_nested(String min_cohort_inclusion_year_nested) {
		this.min_cohort_inclusion_year_nested = min_cohort_inclusion_year_nested;
	}
	
	public String get_years_cohort_inclusion_nested() {
		return years_cohort_inclusion_nested;
	}
	public void set_years_cohort_inclusion_nested(String years_cohort_inclusion_nested) {
		this.years_cohort_inclusion_nested = years_cohort_inclusion_nested;
	}
	
	public String get_type_nested() {
		return type_nested;
	}
	public void set_type_nested(String type_nested) {
		this.type_nested = type_nested;
	}
	
	public String get_start_period_symptoms_onset_year_nested() {
		return start_period_symptoms_onset_year_nested;
	}
	public void set_start_period_symptoms_onset_year_nested(String start_period_symptoms_onset_year_nested) {
		this.start_period_symptoms_onset_year_nested = start_period_symptoms_onset_year_nested;
	}
	
	public String get_end_period_symptoms_onset_year_nested() {
		return end_period_symptoms_onset_year_nested;
	}
	public void set_end_period_symptoms_onset_year_nested(String end_period_symptoms_onset_year_nested) {
		this.end_period_symptoms_onset_year_nested = end_period_symptoms_onset_year_nested;
	}
	
	public String get_start_period_diagnosis_year_nested() {
		return start_period_diagnosis_year_nested;
	}
	public void set_start_period_diagnosis_year_nested(String start_period_diagnosis_year_nested) {
		this.start_period_diagnosis_year_nested = start_period_diagnosis_year_nested;
	}
	
	public String get_end_period_diagnosis_year_nested() {
		return end_period_diagnosis_year_nested;
	}
	public void set_end_period_diagnosis_year_nested(String end_period_diagnosist_year_nested) {
		this.end_period_diagnosis_year_nested = end_period_diagnosis_year_nested;
	}
	
	public String get_start_period_cohort_inclusion_year_nested() {
		return start_period_cohort_inclusion_year_nested;
	}
	public void set_start_period_cohort_inclusion_year_nested(String start_period_cohort_inclusion_year_nested) {
		this.start_period_cohort_inclusion_year_nested = start_period_cohort_inclusion_year_nested;
	}
	
	public String get_end_period_cohort_inclusion_year_nested() {
		return end_period_cohort_inclusion_year_nested;
	}
	public void set_end_period_cohort_inclusion_year_nested(String end_period_cohort_inclusion_year_nested) {
		this.end_period_cohort_inclusion_year_nested = end_period_cohort_inclusion_year_nested;
	}
}
