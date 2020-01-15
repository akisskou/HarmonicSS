package criterionManager;

public class demographics_education extends Criterion{
	
	public String education_level = ""; // 'EDU-LEV-03'

	public String getEducation_level() {
		return education_level;
	}

	public void setEducation_level(String voc_education_level_CODE) {
		this.education_level = voc_education_level_CODE;
	}

	public demographics_education(String criterion_name, String education_level) {
		super(criterion_name);
		this.education_level = education_level;
	}
	
	public demographics_education() {
		super("");
		this.education_level = "";
	}

}
