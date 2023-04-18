package aed.recursion;

import es.upm.aedlib.Pair;
import es.upm.aedlib.Position;
import es.upm.aedlib.indexedlist.*;
import es.upm.aedlib.positionlist.*;


public class Utils {

	public static int multiply(int a, int b) {
		if(a<0) 
			return -1* multiplyAux( a , b , 0);
		return multiplyAux(a, b , 0);
	}

	public static <E extends Comparable<E>> int findBottom(IndexedList<E> l) {
		return findBottomAux(l,-1);
	}



	public static <E extends Comparable<E>> NodePositionList<Pair<E,Integer>>
	joinMultiSets(NodePositionList<Pair<E,Integer>> l1,
			NodePositionList<Pair<E,Integer>> l2) {
		NodePositionList<Pair<E,Integer>> res   = new NodePositionList<>();
		return joinMultisetAux(l1,l2,res,l1.first(),l2.first());
	}




	//Metodos Auxiliares
	private static int multiplyAux(int a, int b, int sum) {
		if(a%2!=0)
			sum+=b;
		if(a==0){
			return sum;
		}else
			return multiplyAux(a/2,b*2,sum);
	}
	private static <E extends Comparable<E> >int findBottomAux(IndexedList<E> l, int res) {
		if(l.isEmpty())
			return res; 
		return findBottomAux2(l, 0, l.size()-1);
	}

	private static <E extends Comparable<E> >int findBottomAux2(IndexedList<E> list, int starts, int ends) {
		if(starts==ends) {
			return ends;
		}else if(ends - starts == 1 ) {
			if(list.get(starts).compareTo(list.get(ends))<= 0)
				return starts;
			return ends;
		}
		else  {
			if(list.get(puntoMedio(starts , ends)).compareTo(list.get(puntoMedio(starts,ends)+1))<=0 && list.get(puntoMedio(starts,ends)).compareTo(list.get(puntoMedio(starts,ends)-1))<=0 )
				return puntoMedio(starts,ends);
			else if(list.get(puntoMedio(starts,ends)).compareTo(list.get(puntoMedio(starts,ends)+1))>=0 ) {
				return findBottomAux2(list, puntoMedio(starts,ends) ,ends);
			}
			else
				return findBottomAux2(list,starts,puntoMedio(starts,ends));
		}
	}
	private static <E extends Comparable<E>> NodePositionList<Pair<E, Integer>> joinMultisetAux(NodePositionList<Pair<E, Integer>> l1,
			NodePositionList<Pair<E, Integer>> l2, NodePositionList<Pair<E, Integer>> res, Position<Pair<E, Integer>> cursorl1,
			Position<Pair<E, Integer>> cursorl2) {
		if(cursorl1==null && cursorl2 == null)
			return res;
		if(cursorl1==null) {
			cursorl2 = actualizar(l2,res,cursorl2);
		}
		else if(cursorl2==null){
			cursorl1 = actualizar(l1,res,cursorl1);
		}else {
			if(compareTo(cursorl1,cursorl2)<0) {
				cursorl1 = actualizar(l1,res,cursorl1);
			}else if(compareTo(cursorl1,cursorl2)>0) {
				cursorl2 = actualizar(l2,res,cursorl2);
			}
			else {
				cursorl1.element().setRight(cursorl1.element().getRight()+ cursorl2.element().getRight());
				cursorl1=actualizar(l1,res,cursorl1);
				cursorl2 =l2.next(cursorl2);

			}

		}
		return joinMultisetAux(l1,l2,res,cursorl1,cursorl2);
	}

	private static <E> Position<Pair<E, Integer>> actualizar(NodePositionList<Pair<E, Integer>> l, NodePositionList<Pair<E, Integer>> res,
			Position<Pair<E, Integer>> cursor) {
		res.addLast(cursor.element());
		return (cursor = l.next(cursor));
	}
	private static <E extends Comparable<E>> int compareTo(Position<Pair<E,Integer>> cursor1,Position<Pair<E,Integer>> cursor2) {
		return cursor1.element().getLeft().compareTo(cursor2.element().getLeft());
	}
	private static int puntoMedio(int a ,int b) {
		return (a+b)/2;

	}


}
