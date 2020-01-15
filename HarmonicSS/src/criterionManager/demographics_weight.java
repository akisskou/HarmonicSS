package criterionManager;

public class demographics_weight extends Criterion {
	
	public String body_mass_index = ""; //BMI-02
	
	public String getBody_mass_index() {
		return body_mass_index;
	}

	public void setBody_mass_index(String body_mass_index) {
		this.body_mass_index = body_mass_index;
	}

	public demographics_weight(String criterion_name, String body_mass_index) {
		super(criterion_name);
		this.body_mass_index = body_mass_index;
	}

	public demographics_weight() {
		super("");
		this.body_mass_index = "";
	}

}
