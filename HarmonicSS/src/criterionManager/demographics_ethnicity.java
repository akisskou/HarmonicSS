package criterionManager;

public class demographics_ethnicity extends Criterion {

	public String ethnicity = ""; //'ETHN-01', 'ETHN-02', 'ETHN-03'

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public demographics_ethnicity(String criterion_name, String ethnicity) {
		super(criterion_name);
		this.ethnicity = ethnicity;
	}
	
	public demographics_ethnicity() {
		super("");
		this.ethnicity = "";
	}
	
}
