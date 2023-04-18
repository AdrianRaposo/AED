package aed.filter;


import java.util.Iterator;
import java.util.function.Predicate;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.positionlist.PositionList;




public class Utils {

	public static <E> Iterable<E> filter(Iterable<E> d, Predicate<E> pred) {
		if(d == null) {
			throw new IllegalArgumentException();
		}
		PositionList <E> r = new NodePositionList<E>(); 
		Iterator <E> iter=d.iterator();	  
		while(iter.hasNext()) {
			E x= iter.next();
			if(x!=null && pred.test(x)) {
				r.addLast( x);
			}

		}
		return r;
	}
}

