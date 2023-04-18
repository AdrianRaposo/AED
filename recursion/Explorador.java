package aed.recursion;

import java.util.Iterator;

import es.upm.aedlib.Pair;
import es.upm.aedlib.positionlist.*;


public class Explorador {

	public static Pair<Object,PositionList<Lugar>> explora(Lugar inicialLugar) {
		PositionList<Lugar> res= new NodePositionList<>();
		inicialLugar.marcaSueloConTiza();
		return  exploraAux(inicialLugar,res);

	}

	private static Pair<Object, PositionList<Lugar>> exploraAux(Lugar lugar, PositionList<Lugar> res) {
		res.addLast(lugar);
		Lugar siguiente = siguienteLugar(lugar);
		if(siguiente!=null) {
			if(siguiente.tieneTesoro()) {
				res.addLast(siguiente);
				return new Pair<>(siguiente.getTesoro(),res);
			}
			else
				return exploraAux(siguiente,res);
		}
		else {
			res.remove(res.last());
			siguiente = res.last().element();
			res.remove(res.last());
			if(res.first()==null)
				return new Pair<>(null,res);
			else
				return exploraAux(siguiente,res);
		}
	}

	private static Lugar siguienteLugar(Lugar posicion) { 
		Iterator<Lugar> it = posicion.caminos().iterator();
		Lugar siguiente= it.next();
		if(siguiente==null)
			return null;
		else {
			if(!siguiente.sueloMarcadoConTiza()) {
				siguiente.marcaSueloConTiza();
				return siguiente;
			}
			while (it.hasNext()) {
				if(siguiente.sueloMarcadoConTiza()) 
					siguiente= it.next();

				else {
					siguiente.marcaSueloConTiza();
					return siguiente;
				}
			}
			if(!siguiente.sueloMarcadoConTiza()) {
				siguiente.marcaSueloConTiza();
				return siguiente;
			}
			else
				return null;
		}
	}
}
