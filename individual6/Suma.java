package aed.individual6;

import es.upm.aedlib.graph.Edge;
import es.upm.aedlib.graph.Vertex;
import es.upm.aedlib.graph.DirectedGraph;
import es.upm.aedlib.map.Map;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.map.HashTableMap;


public class Suma {
	public static <E> Map<Vertex<Integer>,Integer> sumVertices(DirectedGraph<Integer,E> g) {
		Map<Vertex<Integer>,Integer> res = new HashTableMap<>();
		for(Vertex<Integer> v : g.vertices()) {
			res.put(v,sumarVerticesAlcanzables(v,g));
		}
		return res;
	}
	private static<E> Integer sumarVerticesAlcanzables(Vertex<Integer> v, DirectedGraph<Integer, E> g) {
		 PositionList<Vertex<Integer>> listaVerticesAlcanzables = new NodePositionList<>();
		 listaVerticesAlcanzables.addLast(v);
		 verticesAlcanzables(v,g,listaVerticesAlcanzables);
		 int suma=0;
		 for(Vertex<Integer> vertice : listaVerticesAlcanzables) {
			 suma+=vertice.element().intValue();
		 }
		return suma;
	}
	private static<E> void  verticesAlcanzables(Vertex<Integer> v, DirectedGraph<Integer, E> g , PositionList<Vertex<Integer>> listaVerticesAlcanzables) {
		for(Edge<E> e : g.outgoingEdges(v)){
			if(!contains(listaVerticesAlcanzables,g.endVertex(e)) && g.endVertex(e)!=v){
				listaVerticesAlcanzables.addLast(g.endVertex(e));
				verticesAlcanzables(g.endVertex(e),g,listaVerticesAlcanzables);
			}
		}
	}
	private static  boolean contains(PositionList<Vertex<Integer>> aux, 
			Vertex<Integer> v) {
		for (Vertex<Integer> pathVertex : aux) {
			if (v == pathVertex) return true;
		}
		return false;
	}
}
