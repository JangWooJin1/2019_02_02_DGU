import java.io.PrintWriter;
import java.util.*;

public class TypeMap extends HashMap<Variable, Type> { 

// TypeMap is implemented as a Java HashMap.  
// Plus a 'display' method to facilitate experimentation.
	//
	public void display () {
	    System.out.print("{ ");
	    String sep = "";
	    for (Variable key : keySet() ) {
	        System.out.print(sep + "<" + key + ", " + get(key).toString() + ">");
	        sep = ", ";
	    }
	    System.out.println(" }");
	  }
	  
	  public void display (Functions f, TypeMap fm) {
			String sep = "  ";
			String sep2 = "";
			for (Variable key : keySet() ) {
	                    
	                    if (f != null){
	                        int funcloc = -1;
	                        for(int i  = 0; i < f.size(); i++){
	                            if (key.toString().equals(f.get(i).id))funcloc = i;
	                        }
	                        if (!(funcloc == -1)){
	                            System.out.print(sep + "<" + key +"(");

	                            Function func = f.get(funcloc);
	                            if (func.params != null) {
	                                    for (int a = 0; a < func.params.size(); a++) {
	                                        System.out.print(sep2 + "<" + func.params.get(a).v + ", " + func.params.get(a).t.toString() + ">");
	                                        sep2 = ", ";
	                                    }
	                            }

	                            System.out.print("), " + get(key).toString() + ">");
	                        }else{
	                            System.out.print(sep + "<" + key + ", " + get(key).toString() + ">");
	                        } 
	                    } else {
	                        System.out.print(sep + "<" + key + ", " + get(key).toString() + ">");
	                    }
	                    sep = ",\n  ";
	                    sep2 = "";
			}

			System.out.println("\n}\n");
		}
	  
	//
	
}
