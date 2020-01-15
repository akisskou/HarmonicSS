package criterionManager;

public class other_family_history extends Criterion{

	public String medical_condition ="";
	public String relative_degree ="";
	public String statement = "";
		
	
	public other_family_history(String criterion_name, String medical_condition, String relative_degree, String statement) {
		super(criterion_name);
		this.medical_condition = medical_condition;
		this.relative_degree = relative_degree;
		this.statement = statement;
	}
	
	public other_family_history() {
		super("");
		this.medical_condition = "";
		this.relative_degree = "";
		this.statement = "";
	}
	
	public String getMedical_condition() {
		return medical_condition;
	}
	
	public void setMedical_condition(String medical_condition) {
		this.medical_condition = medical_condition;
	}
	
	public String getRelative_degree() {
		return relative_degree;
	}
	
	public void setRelative_degree(String relative_degree) {
		this.relative_degree = relative_degree;
	}
	
	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
}
