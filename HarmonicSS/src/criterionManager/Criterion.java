package criterionManager;
/*
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(
		  use = JsonTypeInfo.Id.NAME, 
		  include = JsonTypeInfo.As.PROPERTY, 
		  property = "type")
		@JsonSubTypes({ 
		  @Type(value = Crit_demo_sex_data.class, name = "Crit_demo_sex_data"), 
		  @Type(value = Crit_demo_ethnicity_data.class, name = "Crit_demo_ethnicity_data"), 
		  @Type(value = Crit_demo_education_level_data.class, name = "Crit_demo_education_level_data") 
})*/
public abstract class Criterion {
	public String criterion="";

	public String getCriterion() {
		return criterion;
	}
	public void setCriterion(String criterion_name) {
		this.criterion = criterion_name;
	}
	public Criterion(String criterion_name) {this.criterion=criterion_name;}
	public Criterion() {this.criterion="";}

}
