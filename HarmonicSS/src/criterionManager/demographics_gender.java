package criterionManager;

public class demographics_gender extends Criterion {

	public String gender; //'SEX-01' or 'SEX-02'
	
	public String getVoc_sex_CODE() {
		return gender;
	}

	public void setVoc_sex_CODE(String voc_sex_CODE) {
		this.gender = voc_sex_CODE;
	}

	public demographics_gender(String criterion_name, String voc_sex_CODE)  { 
		super(criterion_name);
		this.gender = voc_sex_CODE;
	}
	
	public demographics_gender()  { 
		super("");
		this.gender = "";
	}

}
