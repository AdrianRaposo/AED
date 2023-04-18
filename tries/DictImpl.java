package aed.tries;

import java.util.Arrays;
import es.upm.aedlib.Pair;
import es.upm.aedlib.Position;
import es.upm.aedlib.fifo.FIFO;
import es.upm.aedlib.fifo.FIFOList;
import es.upm.aedlib.tree.GeneralTree;
import es.upm.aedlib.tree.LinkedGeneralTree;
import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.positionlist.NodePositionList;public class DictImpl implements Dictionary {
	// A boolean because we need to know if a word ends in a node or not
	GeneralTree<Pair<Character,Boolean>> tree;
	public DictImpl() {
		tree = new LinkedGeneralTree<>();
		tree.addRoot(new Pair<Character,Boolean>(null,false));
	}
	public void add(String word) throws IllegalArgumentException{
		if(word==null ||word.length()==0)
			throw new IllegalArgumentException();
		Position<Pair<Character,Boolean>> cursor=tree.root();
		Position<Pair<Character,Boolean>> hijo=null;
		boolean valor=false;
		for (int i=0;i<word.length();i++) {
			char ch=word.charAt(i);
			hijo=searchChildLabelledBy(ch,cursor);
			if(hijo!=null && i==word.length()-1) {
				hijo.element().setRight(!valor);
			}
			else if (hijo==null ) {
				if(i==word.length()-1)
					valor=true;
				cursor=addChildAlphabetically(new Pair<>(ch,valor),cursor);
			}
			else
				cursor=hijo;
		}
	}
	public void delete(String word) throws IllegalArgumentException {
		if(word==null || word.length()==0)
			throw new IllegalArgumentException();
		Position <Pair<Character,Boolean>> res=findPos(word);
		if(res!=null && res.element().getRight()) {
			res.element().setRight(false);
		}
	}
	public boolean isIncluded(String word) throws IllegalArgumentException {
		if(word==null|| word.length()==0)
			throw new IllegalArgumentException();
		Position <Pair<Character,Boolean>> res=findPos(word);
		return (res!=null && res.element().getRight());
	}
	public PositionList<String> wordsBeginningWithPrefix(String prefix) {
		PositionList<String> result = new NodePositionList<>();
		Position <Pair<Character,Boolean>> cursor = findPos(prefix);
		if(prefix.length()==0)
			cursor= tree.root();
		if(cursor!=null) {
			addPreorder(result,cursor,prefix);
		}
		return result;
	}
	// METODOS AUX
	private void addPreorder(PositionList<String>result,Position<Pair<Character, Boolean>> v ,String word){
		if( v.element().getRight())
			result.addLast(word);
		for ( Position <Pair<Character, Boolean>> w : tree.children(v) ) {
			addPreorder(result, w, word+""+w.element().getLeft()) ;
		}
	}
	private Position<Pair<Character,Boolean>> searchChildLabelledBy(char ch, Position<Pair<Character,Boolean>> pos) {
		FIFO < Position<Pair<Character,Boolean>>> fifo = new FIFOList <Position<Pair<Character,Boolean>>>();
		for ( Position <Pair<Character,Boolean>> w : tree.children(pos)) {
			fifo.enqueue(w);
		}
		while (! fifo.isEmpty()) {
			Position <Pair<Character,Boolean>> v = fifo.dequeue();
			if (v.element().getLeft().equals(ch))
				return v;
		}
		return null;
	}

	private Position<Pair<Character,Boolean>>addChildAlphabetically(Pair<Character,Boolean> pair,Position<Pair<Character,Boolean>> pos) {
		FIFO < Position<Pair<Character,Boolean>>> fifo = new FIFOList <Position<Pair<Character,Boolean>>>();
		Position <Pair<Character,Boolean>> res=null;
		for ( Position <Pair<Character,Boolean>> w : tree.children(pos)) {
			fifo.enqueue(w);
		}
		while (! fifo.isEmpty()) {
			Position <Pair<Character,Boolean>> v = fifo.dequeue();
			if (pair.getLeft().compareTo(v.element().getLeft())<0)
				res=tree.insertSiblingBefore(v,pair);
		}
		if(res==null)
			res= tree.addChildLast(pos, pair);
		return res;
	}

	private Position<Pair<Character,Boolean>> findPos(String prefix) {
		Position<Pair<Character,Boolean>> res = tree.root();
		for(int i = 0; i<prefix.length() && res!=null ;i++) {
			res=searchChildLabelledBy(prefix.charAt(i),res);

		}
		return res;
	}

}


