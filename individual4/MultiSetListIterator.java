package aed.individual4;

import java.util.Iterator;
import java.util.NoSuchElementException;

import es.upm.aedlib.Pair;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.PositionList;

public class MultiSetListIterator<E> implements Iterator<E> {

	PositionList<Pair<E,Integer>> list;
	Position<Pair<E,Integer>> cursor;
	int counter;
	Position<Pair<E,Integer>> prevCursor;



	public MultiSetListIterator(PositionList<Pair<E,Integer>> list) {
		if(list == null) throw new IllegalArgumentException();
		this.list = list;
		this.cursor = list.first();
		this.counter = 0;
	}

	public boolean hasNext() {
		return cursor != null && (cursor.element().getRight()>counter || list.next(cursor)!=null);
	}

	public E next() {
		if( !hasNext() ) throw new NoSuchElementException();
		if(counter<cursor.element().getRight()) {
			this.counter++;
		}else {
			cursor =list.next(cursor);
			this.counter=1;
		}
		this.prevCursor=cursor;
		return cursor.element().getLeft();
	}

	@Override
	public void remove() {

		if( counter == 0 ) throw new IllegalStateException();
		if(cursor.element().getRight()==1) {
			cursor=list.next(cursor);
			list.remove(prevCursor);
			counter=0;
		}else {
			cursor.element().setRight(cursor.element().getRight()-1);
			counter--;
		}

	}
}
