package criterionManager;

public class Crit_exam_lab_test extends Criterion { //check if it will be deleted

	public String test_id = "";									//Column TEST_ID voc_lab_test
	
	public String outcome_amount_unit = "";							//Column OUTCOME_AMOUNT_ID dt_amount_VALUE
	public String OUTCOME_AMOUNT_ID_dt_amount_UNIT_ID_voc_unit_CODE = "";			//Column OUTCOME_AMOUNT_ID dt_amount_UNIT_ID voc_unit
	public String OUTCOME_ASSESSMENT_ID_voc_assessment_CODE = "";					//Column OUTCOME_ASSESSMENT_ID_voc_assessment
	
	public String NORMAL_RANGE_ID_dt_amount_range_VALUE1 = "";						//Column NORMAL_RANGE_ID dt_amount_range
	public String NORMAL_RANGE_ID_dt_amount_range_VALUE2 = "";						//Column NORMAL_RANGE_ID dt_amount_range
	public String NORMAL_RANGE_ID_dt_amount_range_UNIT_ID_voc_unit_CODE = "";		//Column NORMAL_RANGE_ID dt_amount_range UNIT_ID voc_unit
//	public String OUTCOME_TERM_ID=""; 												// There is not definition in the schema of DB.
	
	public String START_DATE_dt_date_YEAR = "";										//Column: PERIOD_ID START_DATE
	public String START_DATE_dt_date_MONTH = "";									//Column: PERIOD_ID START_DATE
	public String START_DATE_dt_date_DAY = "";										//Column: PERIOD_ID START_DATE
	public String START_DATE_dt_date_BEFORE_DATE_ID = "";							//Column: PERIOD_ID START_DATE
	public String START_DATE_voc_direction_CODE = "";								//Column: PERIOD_ID START_DATE
	
	public Crit_exam_lab_test(String criterion_name, String TEST_ID_voc_lab_test_CODE, String OUTCOME_AMOUNT_ID_dt_amount_VALUE, 
			String OUTCOME_AMOUNT_ID_dt_amount_UNIT_ID_voc_unit_CODE, String OUTCOME_ASSESSMENT_ID_voc_assessment_CODE,
			String NORMAL_RANGE_ID_dt_amount_range_VALUE1, String NORMAL_RANGE_ID_dt_amount_range_VALUE2,
			String NORMAL_RANGE_ID_dt_amount_range_UNIT_ID_voc_unit_CODE, String START_DATE_dt_date_YEAR, String START_DATE_dt_date_MONTH,
			String START_DATE_dt_date_DAY, String START_DATE_dt_date_BEFORE_DATE_ID, String START_DATE_voc_direction_CODE) {
		super(criterion_name);

		this.test_id = TEST_ID_voc_lab_test_CODE;																//Column TEST_ID voc_lab_test
		this.outcome_amount_unit = OUTCOME_AMOUNT_ID_dt_amount_VALUE;												//Column OUTCOME_AMOUNT_ID dt_amount_VALUE
		this.OUTCOME_AMOUNT_ID_dt_amount_UNIT_ID_voc_unit_CODE = OUTCOME_AMOUNT_ID_dt_amount_UNIT_ID_voc_unit_CODE;				//Column OUTCOME_AMOUNT_ID dt_amount_UNIT_ID voc_unit
		this.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE = OUTCOME_ASSESSMENT_ID_voc_assessment_CODE;								//Column OUTCOME_ASSESSMENT_ID_voc_assessment
		this.NORMAL_RANGE_ID_dt_amount_range_VALUE1 = NORMAL_RANGE_ID_dt_amount_range_VALUE1;									//Column NORMAL_RANGE_ID dt_amount_range
		this.NORMAL_RANGE_ID_dt_amount_range_VALUE2 = NORMAL_RANGE_ID_dt_amount_range_VALUE2;									//Column NORMAL_RANGE_ID dt_amount_range
		this.NORMAL_RANGE_ID_dt_amount_range_UNIT_ID_voc_unit_CODE = NORMAL_RANGE_ID_dt_amount_range_UNIT_ID_voc_unit_CODE;		//Column NORMAL_RANGE_ID dt_amount_range UNIT_ID voc_unit
		
		this.START_DATE_dt_date_YEAR = START_DATE_dt_date_YEAR;																	//Column: PERIOD_ID START_DATE
		this.START_DATE_dt_date_MONTH = START_DATE_dt_date_MONTH;																//Column: PERIOD_ID START_DATE
		this.START_DATE_dt_date_DAY = START_DATE_dt_date_DAY;																	//Column: PERIOD_ID START_DATE
		this.START_DATE_dt_date_BEFORE_DATE_ID = START_DATE_dt_date_BEFORE_DATE_ID;												//Column: PERIOD_ID START_DATE
		this.START_DATE_voc_direction_CODE = START_DATE_voc_direction_CODE;														//Column: PERIOD_ID START_DATE
	}

	public Crit_exam_lab_test() {
		super("");
		
		this.test_id = "";									//Column TEST_ID voc_lab_test
		this.outcome_amount_unit = "";							//Column OUTCOME_AMOUNT_ID dt_amount_VALUE
		this.OUTCOME_AMOUNT_ID_dt_amount_UNIT_ID_voc_unit_CODE = "";			//Column OUTCOME_AMOUNT_ID dt_amount_UNIT_ID voc_unit
		this.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE = "";					//Column OUTCOME_ASSESSMENT_ID_voc_assessment
		this.NORMAL_RANGE_ID_dt_amount_range_VALUE1 = "";						//Column NORMAL_RANGE_ID dt_amount_range
		this.NORMAL_RANGE_ID_dt_amount_range_VALUE2 = "";						//Column NORMAL_RANGE_ID dt_amount_range
		this.NORMAL_RANGE_ID_dt_amount_range_UNIT_ID_voc_unit_CODE = "";		//Column NORMAL_RANGE_ID dt_amount_range UNIT_ID voc_unit
//		this.OUTCOME_TERM_ID=""; 												// There is not definition in the schema of DB.
		
		this.START_DATE_dt_date_YEAR = "";										//Column: PERIOD_ID START_DATE
		this.START_DATE_dt_date_MONTH = "";										//Column: PERIOD_ID START_DATE
		this.START_DATE_dt_date_DAY = "";										//Column: PERIOD_ID START_DATE
		this.START_DATE_dt_date_BEFORE_DATE_ID = "";							//Column: PERIOD_ID START_DATE
		this.START_DATE_voc_direction_CODE = "";								//Column: PERIOD_ID START_DATE
	}

	public String getTEST_ID_voc_lab_test_CODE() {
		return test_id;
	}

	public void setTEST_ID_voc_lab_test_CODE(String tEST_ID_voc_lab_test_CODE) {
		test_id = tEST_ID_voc_lab_test_CODE;
	}

	public String getOUTCOME_AMOUNT_ID_dt_amount_VALUE() {
		return outcome_amount_unit;
	}

	public void setOUTCOME_AMOUNT_ID_dt_amount_VALUE(String oUTCOME_AMOUNT_ID_dt_amount_VALUE) {
		outcome_amount_unit = oUTCOME_AMOUNT_ID_dt_amount_VALUE;
	}

	public String getOUTCOME_AMOUNT_ID_dt_amount_UNIT_ID_voc_unit_CODE() {
		return OUTCOME_AMOUNT_ID_dt_amount_UNIT_ID_voc_unit_CODE;
	}

	public void setOUTCOME_AMOUNT_ID_dt_amount_UNIT_ID_voc_unit_CODE(
			String oUTCOME_AMOUNT_ID_dt_amount_UNIT_ID_voc_unit_CODE) {
		OUTCOME_AMOUNT_ID_dt_amount_UNIT_ID_voc_unit_CODE = oUTCOME_AMOUNT_ID_dt_amount_UNIT_ID_voc_unit_CODE;
	}

	public String getOUTCOME_ASSESSMENT_ID_voc_assessment_CODE() {
		return OUTCOME_ASSESSMENT_ID_voc_assessment_CODE;
	}

	public void setOUTCOME_ASSESSMENT_ID_voc_assessment_CODE(String oUTCOME_ASSESSMENT_ID_voc_assessment_CODE) {
		OUTCOME_ASSESSMENT_ID_voc_assessment_CODE = oUTCOME_ASSESSMENT_ID_voc_assessment_CODE;
	}

	public String getNORMAL_RANGE_ID_dt_amount_range_VALUE1() {
		return NORMAL_RANGE_ID_dt_amount_range_VALUE1;
	}

	public void setNORMAL_RANGE_ID_dt_amount_range_VALUE1(String nORMAL_RANGE_ID_dt_amount_range_VALUE1) {
		NORMAL_RANGE_ID_dt_amount_range_VALUE1 = nORMAL_RANGE_ID_dt_amount_range_VALUE1;
	}

	public String getNORMAL_RANGE_ID_dt_amount_range_VALUE2() {
		return NORMAL_RANGE_ID_dt_amount_range_VALUE2;
	}

	public void setNORMAL_RANGE_ID_dt_amount_range_VALUE2(String nORMAL_RANGE_ID_dt_amount_range_VALUE2) {
		NORMAL_RANGE_ID_dt_amount_range_VALUE2 = nORMAL_RANGE_ID_dt_amount_range_VALUE2;
	}

	public String getNORMAL_RANGE_ID_dt_amount_range_UNIT_ID_voc_unit_CODE() {
		return NORMAL_RANGE_ID_dt_amount_range_UNIT_ID_voc_unit_CODE;
	}

	public void setNORMAL_RANGE_ID_dt_amount_range_UNIT_ID_voc_unit_CODE(
			String nORMAL_RANGE_ID_dt_amount_range_UNIT_ID_voc_unit_CODE) {
		NORMAL_RANGE_ID_dt_amount_range_UNIT_ID_voc_unit_CODE = nORMAL_RANGE_ID_dt_amount_range_UNIT_ID_voc_unit_CODE;
	}

	public String getSTART_DATE_dt_date_YEAR() {
		return START_DATE_dt_date_YEAR;
	}

	public void setSTART_DATE_dt_date_YEAR(String sTART_DATE_dt_date_YEAR) {
		START_DATE_dt_date_YEAR = sTART_DATE_dt_date_YEAR;
	}

	public String getSTART_DATE_dt_date_MONTH() {
		return START_DATE_dt_date_MONTH;
	}

	public void setSTART_DATE_dt_date_MONTH(String sTART_DATE_dt_date_MONTH) {
		START_DATE_dt_date_MONTH = sTART_DATE_dt_date_MONTH;
	}

	public String getSTART_DATE_dt_date_DAY() {
		return START_DATE_dt_date_DAY;
	}

	public void setSTART_DATE_dt_date_DAY(String sTART_DATE_dt_date_DAY) {
		START_DATE_dt_date_DAY = sTART_DATE_dt_date_DAY;
	}

	public String getSTART_DATE_dt_date_BEFORE_DATE_ID() {
		return START_DATE_dt_date_BEFORE_DATE_ID;
	}

	public void setSTART_DATE_dt_date_BEFORE_DATE_ID(String sTART_DATE_dt_date_BEFORE_DATE_ID) {
		START_DATE_dt_date_BEFORE_DATE_ID = sTART_DATE_dt_date_BEFORE_DATE_ID;
	}

	public String getSTART_DATE_voc_direction_CODE() {
		return START_DATE_voc_direction_CODE;
	}

	public void setSTART_DATE_voc_direction_CODE(String sTART_DATE_voc_direction_CODE) {
		START_DATE_voc_direction_CODE = sTART_DATE_voc_direction_CODE;
	}

}
