package aed.hashtable;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Arrays;

import es.upm.aedlib.Entry;
import es.upm.aedlib.EntryImpl;
import es.upm.aedlib.map.Map;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.InvalidKeyException;


/**
 * A hash table implementing using open addressing to handle key collisions.
 */
public class HashTable<K,V> implements Map<K,V> {
	Entry<K,V>[] buckets;
	int size;

	public HashTable(int initialSize) {
		this.buckets = createArray(initialSize);
		this.size = 0;
	}

	/**
	 * Add here the method necessary to implement the Map api, and
	 * any auxilliary methods you deem convient.
	 */

	// Examples of auxilliary methods: IT IS NOT REQUIRED TO IMPLEMENT THEM

	@SuppressWarnings("unchecked") 
	private Entry<K,V>[] createArray(int size) {
		Entry<K,V>[] buckets = (Entry<K,V>[]) new Entry[size];
		return buckets;
	}

	// Returns the bucket index of an object
	private int index(Object obj) {
		return Math.abs(obj.hashCode())%buckets.length;
	}

	// Returns the index where an entry with the key is located,
	// or if no such entry exists, the "next" bucket with no entry,
	// or if all buckets stores an entry, -1 is returned.
	private int search(Object obj) {
		boolean encontrado = false;
		int res=-1;
		int pos = index(obj);
		boolean started= false;
		while(!encontrado && (!started|| pos!= index(obj))) {
			if(buckets[pos]==null || buckets[pos].getKey().equals(obj)) {
				res= pos;
				encontrado = true;
			}
			pos= (pos+1)%buckets.length;
			started = true;
		}


		return res;

	}

	// Doubles the size of the bucket array, and inserts all entries present
	// in the old bucket array into the new bucket array, in their correct
	// places. Remember that the index of an entry will likely change in
	// the new array, as the size of the array changes.
	private void rehash() {

		Entry<K,V>[] aux= buckets;
		this.buckets = createArray(buckets.length*2);
		this.size= 0;
		for(int i  = 0 ; i<aux.length ; i++) {
			put(aux[i].getKey(),aux[i].getValue());
		}
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return entries().iterator();
	}

	@Override
	public boolean containsKey(Object key) throws InvalidKeyException {
		if(key== null) throw new InvalidKeyException();
		int pos =  search(key); 
		return pos>=0 && buckets[pos]!=null;
	}

	@Override
	public Iterable<Entry<K, V>> entries() {
		PositionList<Entry<K, V>>res= new NodePositionList<>();
		for(int i = 0; i < buckets.length; i++) {
			if(buckets[i]!=null)
				res.addLast(buckets[i]);
		}
		return res;
	}

	@Override
	public V get(K key) throws InvalidKeyException {
		if(key==null) throw new InvalidKeyException();
		V res = null;
		if(containsKey(key))
			res=  buckets[search(key)].getValue();
		return res;
	}

	@Override
	public boolean isEmpty() {
		return size()==0;
	}

	@Override
	public Iterable<K> keys() {
		PositionList<K>res= new NodePositionList<>();
		for(int i = 0; i < buckets.length; i++) {
			if(buckets[i]!=null)
				res.addLast(buckets[i].getKey());
		}
		return res;
	}

	@Override
	public V put(K key, V value) throws InvalidKeyException {
		if(key== null) throw new InvalidKeyException();
		V res = null;
		int pos = search(key);
		if(pos == -1) {
			rehash();
			pos= search(key);
		}
		if(buckets[pos]==null ) {
			this.size++;
		}
		else {
			res= buckets[pos].getValue();
		}
		buckets[pos]=new EntryImpl<K,V>(key,value);
		return res;
	}

	@Override
	public V remove(K key) throws InvalidKeyException {
		if(key== null) throw new InvalidKeyException();
		V res = null;
		int pos= search(key);
		if(pos>=0 && buckets[pos]!= null) {
			res = buckets[pos].getValue();
			buckets[pos]= null;
			int cursor = (pos+1) % buckets.length;
			while(pos!= cursor && buckets[cursor]!=null) {
				int posiblelugar = search(buckets[cursor].getKey());
				if(buckets[posiblelugar]==null) {
					buckets[posiblelugar]  =buckets[cursor];
					buckets[cursor]=null;
				}
				cursor= (cursor+1)%buckets.length;
			}
			this.size--;
		}
		return res;
	}

	@Override
	public int size() {
		return this.size;
	}

}

