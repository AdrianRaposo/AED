package aed.delivery;

import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.graph.DirectedGraph;
import es.upm.aedlib.graph.DirectedAdjacencyListGraph;
import es.upm.aedlib.graph.Vertex;
import es.upm.aedlib.indexedlist.ArrayIndexedList;
import es.upm.aedlib.indexedlist.IndexedList;
import es.upm.aedlib.graph.Edge;
import es.upm.aedlib.map.HashTableMap;
import es.upm.aedlib.map.Map;
import es.upm.aedlib.set.HashTableMapSet;
import es.upm.aedlib.set.Set;
import java.util.Iterator;

public class Delivery<V> {

	// Construct a graph out of a series of vertices and an adjacency matrix.
	// There are 'len' vertices. A negative number means no connection. A non-negative
	// number represents distance between nodes.
	private DirectedGraph<V,Integer> graph ;
	private PositionList<Vertex<V>> vertices;
	private Integer[][] gmat;
	private Map<Vertex<V>, Integer> mapa;

	public Delivery(V[] places, Integer[][] gmat) {
		this.graph = new DirectedAdjacencyListGraph<>();
		this.vertices = new NodePositionList<>();
		this.mapa= new HashTableMap<>();
		this.gmat=gmat;
		for(int i = 0; i< gmat.length; i++) {
			Vertex<V> v = this.graph.insertVertex(places[i]);
			this.vertices.addLast(v);
			this.mapa.put(v, i);
		}
		Position<Vertex<V>>cursori= vertices.first();
		for(int i = 0; i< gmat.length; i++) {
			Position<Vertex<V>>cursorj= vertices.first();
			for(int j = 0; j< gmat[i].length; j++) {
				if(gmat[i][j]!=null)
					this.graph.insertDirectedEdge(cursori.element(), cursorj.element(), gmat[i][j]);
				cursorj=vertices.next(cursorj);
			}
			cursori=vertices.next(cursori);
		}
	}

	// Just return the graph that was constructed
	public DirectedGraph<V, Integer> getGraph() {
		return graph;
	}

	// Return a Hamiltonian path for the stored graph, or null if there is noe.
	// The list containts a series of vertices, with no repetitions (even if the path
	// can be expanded to a cycle).
	public PositionList <Vertex<V>> tour() {
		PositionList<Vertex<V>> hamiltonianCycle=null;
		Position<Vertex<V>> cursor = vertices.first();
		boolean existe = false;
		while(cursor!=null && !existe) {
			hamiltonianCycle= new NodePositionList<>();
			hamiltonianCycle.addLast(cursor.element());
			hamiltonianPath(hamiltonianCycle,cursor.element());
			if(hamiltonianCycle.size() == vertices.size()) {
				existe=true; 
			}
			cursor = vertices.next(cursor);
		}
		if(!existe)
			return null;
		return hamiltonianCycle;
	}



	public int length(PositionList<Vertex<V>> path) {
		int sum= 0;
		Position<Vertex<V>> cursor = path.first();
		while(cursor!=path.last()) {
			sum+=gmat[mapa.get(cursor.element())][mapa.get(path.next(cursor).element())];
			cursor= path.next(cursor);
		}
		return sum;
	}

	public String toString() {
		return "Delivery";
	}

	private static<V>  boolean contains(PositionList<Vertex<V>> path, 
			Vertex<V> v) {
		for (Vertex<V> pathVertex : path) {
			if (v == pathVertex) return true;
		}
		return false;
	}
	private void hamiltonianPath(PositionList<Vertex<V>> hamiltonianPath,Vertex<V> v) {
		if(hamiltonianPath.size()==graph.numVertices()) {
			return;
		}
		Position<Vertex<V>> cursor = vertices.first();

		while(cursor!=null) {
			if(gmat[mapa.get(v)][mapa.get(cursor.element())]!=null && !contains(hamiltonianPath,cursor.element()) ) {
				hamiltonianPath.addLast(cursor.element());
				hamiltonianPath(hamiltonianPath,cursor.element());
			}
			cursor = vertices.next(cursor);
		}

		if(hamiltonianPath.size()<graph.numVertices()) {
			hamiltonianPath.remove(hamiltonianPath.last());
		}


	}

}

