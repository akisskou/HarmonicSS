package criterionManager;

import java.util.ArrayList;

public class Container_of_Criterions {
	public static ArrayList<Criterion> list_of_criterions = new ArrayList<Criterion>();

	public  ArrayList<Criterion> getList_of_criterions() {
		return list_of_criterions;
	}

	public  void setList_of_criterions(ArrayList<Criterion> list_of_criterions) {
		Container_of_Criterions.list_of_criterions = list_of_criterions;
	}


}
