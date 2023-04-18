
package aed.orderedmap;
import java.util.Comparator;
import es.upm.aedlib.Entry;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.positionlist.NodePositionList;public class PositionListOrderedMap<K,V> implements OrderedMap<K,V> {

	
	
	private Comparator<K> cmp;
	private PositionList<Entry<K,V>> elements; /* Acabar de codificar el constructor */
	public PositionListOrderedMap(Comparator<K> cmp) {
		this.cmp = cmp;
		this.elements =new NodePositionList<>();
	} 
	/* Ejemplo de un posible método auxiliar: */ /* If key is in the map, return the position of the corresponding
	 * entry. Otherwise, return the position of the entry which
	 * should follow that of key. If that entry is not in the map,
	 * return null. Examples: assume key = 2, and l is the list of
	 * keys in the map. For l = [], return null; for l = [1], return
	 * null; for l = [2], return a ref. to '2'; for l = [3], return a
	 * reference to [3]; for l = [0,1], return null; for l = [2,3],
	 * return a reference to '2'; for l = [1,3], return a reference to
	 * '3'. */ 
	private Position<Entry<K,V>> findKeyPlace(K key) {
		ilegal(key);
		Position<Entry<K,V>> res = elements.first();
		boolean encontrado = false;
		while(!encontrado && res!=null ) {
			if(cmp.compare(key, res.element().getKey())<=0) {
				encontrado= true;
			}
			else
				res = elements.next(res);
		} return res;
	} 
	private Position<Entry<K,V>> keyPosition(K key){
		ilegal(key);
		Position<Entry<K,V>> res = elements.first();
		boolean encontrado = false;
		while(!encontrado && res!=null ) {
			if(res.element().getKey().equals(key)) {
				encontrado= true;
			}
			else
				res = elements.next(res);
		}
		return res;
	}
	private void ilegal(K key) {
		if(key==null)
			throw new IllegalArgumentException();
	}
	/* Podéis añadir más métodos auxiliares */ 


	public boolean containsKey(K key) {

		return keyPosition(key)!=null;

	} 
	public V get(K key) {
		V res = null;
		if(containsKey(key)) {
			res= keyPosition(key).element().getValue();
		}
		return res;
	}  
	public V put(K key, V value) {
		V res = null;
		EntryImpl<K, V> e = new EntryImpl<>(key, value);
		if(containsKey(key)) {
			res=get(key);
			elements.set(keyPosition(key), e);
		}
		else if(findKeyPlace(key)!=null) {
			elements.addBefore(findKeyPlace(key), e );
		}else {
			elements.addLast(e);
		}
		return res;
	} 
	public V remove(K key) {
		V res = null;
		if(containsKey(key)) {
			res= get(key);
			elements.remove(keyPosition(key));
		}
		return res; } 
	public int size() {
		return elements.size();
	}
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	public Entry<K,V> floorEntry(K key) {
		Entry<K,V> res = null;
		Position<Entry<K,V>> cursor = findKeyPlace(key);
		if(cursor==null && !isEmpty() ) {
			cursor = elements.last();
			res = cursor.element();
		}
		else if(cursor!=elements.first() && cursor!=keyPosition(key) && !isEmpty()) {
			cursor = elements.prev(cursor);
			res = cursor.element();
		}else if(cursor==keyPosition(key)&& !isEmpty()) {
			res = cursor.element();
		}
		return res;
	}
	public Entry<K,V> ceilingEntry(K key) {
		Entry<K,V> res = null;
		if(findKeyPlace(key)!=null) {
			res=findKeyPlace(key).element();
		}
		return res;
	}
	public Iterable<K> keys() {
		Position<Entry<K,V>> cursor = elements.first();
		PositionList<K> keys =new NodePositionList<>();
		while(cursor!=null){
			keys.addLast(cursor.element().getKey());
			cursor = elements.next(cursor);
		}
		return keys;
	}
	public String toString() {
		return elements.toString();
	}
}

