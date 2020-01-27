package xmlWrapper;

import criterionManager.Criterion;

public interface CriterionManager {
	public boolean canUseCriterion(Criterion crit);
	public int findPatientsIds(String criterion);
	public String getTermsWithNarrowMeaning(String myTerm);
}
