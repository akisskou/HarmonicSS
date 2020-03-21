package criterionManager;

public class demographics_occupation extends Criterion {
	
	public String loss_of_work_due_to_pss = "";
	public String count = "";
	
	public String getLoss_of_work_due_to_pss() {
		return loss_of_work_due_to_pss;
	}

	public void setLoss_of_work_due_to_pss(String loss_of_work_due_to_pss) {
		this.loss_of_work_due_to_pss = loss_of_work_due_to_pss;
	}
	

	public String getCount() {
		return count;
	}
	
	public void setCount(String count) {
		this.count = count;
	}

	public demographics_occupation(String criterion_name, String loss_of_work_due_to_pss, String count) {
		super(criterion_name);
		this.loss_of_work_due_to_pss = loss_of_work_due_to_pss;
		this.count = count;
	}

	public demographics_occupation() {
		super("");
		this.loss_of_work_due_to_pss = "";
		this.count = "";
	}

}
