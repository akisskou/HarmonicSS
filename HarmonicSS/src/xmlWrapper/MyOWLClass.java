package xmlWrapper;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLClass;

public class MyOWLClass {
	OWLClass name;
	String id;
	List<MyOWLClass> subClasses = new ArrayList<MyOWLClass>();
	boolean isSubClass = false;
}
