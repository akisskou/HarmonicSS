package criterionManager;

public class intervention_medication extends Criterion {
	
	public String pharmacological_drug = ""; 			//Column: MEDICATION_ID voc_pharm_drug
	public String dosage_amount_unit = "";				//Column: DOSAGE_ID dt_amount	
	public String dosage_amount_exact_value = "";		//Column: DOSAGE_ID dt_amount voc_unit Do we need the NAME and DESCRIPTION ?
	public String dosage_amount_range_min_value="";
	public String dosage_amount_range_max_value="";
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


	public intervention_medication(String criterion_name, String voc_pharm_drug_CODE, 
			String dosage_amount_exact_value, String dosage_amount_range_min_value, String dosage_amount_range_max_value,
			String DOSAGE_ID_dt_amount_UNIT_ID_CODE, String dosage_amount_unit,
			String medication_exact_date_year, String medication_exact_date_month, String medication_exact_date_day, String medication_period_begin_year,
			String medication_period_begin_month, String medication_period_begin_day, String medication_period_end_year, String medication_period_end_month,
			String medication_period_end_day, String medication_until_date_year, String medication_until_date_month, String medication_until_date_day, String statement, String min_count, String max_count) {
		super(criterion_name);
		this.pharmacological_drug = voc_pharm_drug_CODE; 												//Column: MEDICATION_ID voc_pharm_drug
		this.dosage_amount_unit = dosage_amount_unit;						//Column: DOSAGE_ID dt_amount	
		this.dosage_amount_exact_value = dosage_amount_exact_value;		//Column: DOSAGE_ID dt_amount voc_unit Do we need the NAME and DESCRIPTION ?
		this.dosage_amount_range_min_value=dosage_amount_range_min_value;
		this.dosage_amount_range_max_value=dosage_amount_range_max_value;
		this.period_of_time_exact_year = medication_exact_date_year;						
		this.period_of_time_exact_month = medication_exact_date_month;					
		this.period_of_time_exact_day = medication_exact_date_day;						
		this.period_of_time_interval_start_year = medication_period_begin_year;					
		this.period_of_time_interval_start_month = medication_period_begin_month;				
		this.period_of_time_interval_start_day = medication_period_begin_day;					
		this.period_of_time_interval_end_year = medication_period_end_year;					
		this.period_of_time_interval_end_month = medication_period_end_month;					
		this.period_of_time_interval_end_day = medication_period_end_day;					
		this.period_of_time_until_year = medication_until_date_year;
		this.period_of_time_until_month = medication_until_date_month;
		this.period_of_time_until_day = medication_until_date_day;
		this.statement=statement;
		this.min_count = min_count;
		this.max_count = max_count;
	}

	public intervention_medication() {
		super("");
		this.pharmacological_drug = ""; 							//Column: MEDICATION_ID voc_pharm_drug
		this.dosage_amount_unit = "";					//Column: DOSAGE_ID dt_amount	
		this.dosage_amount_exact_value = "";			//Column: DOSAGE_ID dt_amount voc_unit Do we need the NAME and DESCRIPTION ?
		this.dosage_amount_range_min_value="";
		this.dosage_amount_range_max_value="";			//Column: DOSAGE_ID dt_amount voc_unit Do we need the NAME and DESCRIPTION ?
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
		this.statement="";
		this.min_count = "";
		this.max_count = "";
	}

	public String getDosage_amount_exact_value() {
		return dosage_amount_exact_value;
	}

	public void setDosage_amount_exact_value(String dosage_amount_exact_value) {
		this.dosage_amount_exact_value = dosage_amount_exact_value;
	}

	public String getDosage_amount_range_min_value() {
		return dosage_amount_range_min_value;
	}

	public void setDosage_amount_range_min_value(String dosage_amount_range_min_value) {
		this.dosage_amount_range_min_value = dosage_amount_range_min_value;
	}

	public String getDosage_amount_range_max_value() {
		return dosage_amount_range_max_value;
	}

	public void setDosage_amount_range_max_value(String dosage_amount_range_max_value) {
		this.dosage_amount_range_max_value = dosage_amount_range_max_value;
	}
	
	public String getVoc_pharm_drug_CODE() {
		return pharmacological_drug;
	}

	public void setVoc_pharm_drug_CODE(String voc_pharm_drug_CODE) {
		this.pharmacological_drug = voc_pharm_drug_CODE;
	}

	public String getDOSAGE_ID_dt_amount_VALUE() {
		return dosage_amount_unit;
	}

	public void setDOSAGE_ID_dt_amount_VALUE(String dOSAGE_ID_dt_amount_VALUE) {
		dosage_amount_unit = dOSAGE_ID_dt_amount_VALUE;
	}

	public String getMedication_exact_date_year() {
		return period_of_time_exact_year;
	}

	public void setMedication_exact_date_year(String medication_exact_date_year) {
		this.period_of_time_exact_year = medication_exact_date_year;
	}

	public String getMedication_exact_date_month() {
		return period_of_time_exact_month;
	}

	public void setMedication_exact_date_month(String medication_exact_date_month) {
		this.period_of_time_exact_month = medication_exact_date_month;
	}

	public String getMedication_exact_date_day() {
		return period_of_time_exact_day;
	}

	public void setMedication_exact_date_day(String medication_exact_date_day) {
		this.period_of_time_exact_day = medication_exact_date_day;
	}

	public String getMedication_period_begin_year() {
		return period_of_time_interval_start_year;
	}

	public void setMedication_period_begin_year(String medication_period_begin_year) {
		this.period_of_time_interval_start_year = medication_period_begin_year;
	}

	public String getMedication_period_begin_month() {
		return period_of_time_interval_start_month;
	}

	public void setMedication_period_begin_month(String medication_period_begin_month) {
		this.period_of_time_interval_start_month = medication_period_begin_month;
	}

	public String getMedication_period_begin_day() {
		return period_of_time_interval_start_day;
	}

	public void setMedication_period_begin_day(String medication_period_begin_day) {
		this.period_of_time_interval_start_day = medication_period_begin_day;
	}

	public String getMedication_period_end_year() {
		return period_of_time_interval_end_year;
	}

	public void setMedication_period_end_year(String medication_period_end_year) {
		this.period_of_time_interval_end_year = medication_period_end_year;
	}

	public String getMedication_period_end_month() {
		return period_of_time_interval_end_month;
	}

	public void setMedication_period_end_month(String medication_period_end_month) {
		this.period_of_time_interval_end_month = medication_period_end_month;
	}

	public String getMedication_period_end_day() {
		return period_of_time_interval_end_day;
	}

	public void setMedication_period_end_day(String medication_period_end_day) {
		this.period_of_time_interval_end_day = medication_period_end_day;
	}

	public String getMedication_until_date_year() {
		return period_of_time_until_year;
	}

	public void setMedication_until_date_year(String medication_until_date_year) {
		this.period_of_time_until_year = medication_until_date_year;
	}

	public String getMedication_until_date_month() {
		return period_of_time_until_month;
	}

	public void setMedication_until_date_month(String medication_until_date_month) {
		this.period_of_time_until_month = medication_until_date_month;
	}

	public String getMedication_until_date_day() {
		return period_of_time_until_day;
	}

	public void setMedication_until_date_day(String medication_until_date_day) {
		this.period_of_time_until_day = medication_until_date_day;
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
}
