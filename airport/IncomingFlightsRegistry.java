package aed.airport;


import es.upm.aedlib.Entry;
import es.upm.aedlib.EntryImpl;
import es.upm.aedlib.Pair;
import es.upm.aedlib.priorityqueue.*;
import es.upm.aedlib.map.*;
import es.upm.aedlib.positionlist.*;


/**
 * A registry which organizes information on airplane arrivals.
 */
public class IncomingFlightsRegistry {
	private PriorityQueue< Long,  String> registro;
	private Map<String,Entry<Long, String>> mapaVuelos;


	/**
	 * Constructs an class instance.
	 */
	public IncomingFlightsRegistry() {
		this.registro= new HeapPriorityQueue<>();
		this.mapaVuelos = new HashTableMap<>();
	}

	/**
	 * 
	 * A flight is predicted to arrive at an arrival time (in seconds).
	 */
	public void arrivesAt(String flight, long time) {
		if(this.mapaVuelos.containsKey(flight))
			flightDiverted(flight);
		this.mapaVuelos.put(flight, this.registro.enqueue(time, flight));

	}

	/**
	 * A flight has been diverted, i.e., will not arrive at the airport.
	 */
	public void flightDiverted(String flight) {
		if(this.mapaVuelos.containsKey(flight)) {
			this.registro.remove(this.mapaVuelos.remove(flight));
		}
	}

	/**
	 * Returns the arrival time of the flight.
	 * @return the arrival time for the flight, or null if the flight is not predicted
	 * to arrive.
	 */
	public Long arrivalTime(String flight) {
		if(this.mapaVuelos.containsKey(flight))
			return this.mapaVuelos.get(flight).getKey();
		return null;
	}

	/**
	 * Returns a list of "soon" arriving flights, i.e., if any 
	 * is predicted to arrive at the airport within nowTime+180
	 * then adds the predicted earliest arriving flight to the list to return, 
	 * and removes it from the registry.
	 * Moreover, also adds to the returned list, in order of arrival time, 
	 * any other flights arriving withinfirstArrivalTime+120; these flights are 
	 * also removed from the queue of incoming flights.
	 * @return a list of soon arriving flights.
	 */
	public PositionList<FlightArrival> arriving(long nowTime) {
		PositionList<FlightArrival> res = new NodePositionList<>();
		if(!this.registro.isEmpty()) {
			Entry<Long,String> aux = this.registro.first();
			if(aux.getKey()<=nowTime+180) {
				this.registro.dequeue();
				FlightArrival avion = new FlightArrival(aux.getValue(),aux.getKey());
				res.addLast(avion);
				this.mapaVuelos.remove(aux.getValue());
				vuelosConflicto(avion,res);
			}
		}
		return res;
	}
	//METODO AUX
	private void vuelosConflicto(FlightArrival vuelo, PositionList<FlightArrival> res) {
		boolean conflicto= true;
		while(!this.registro.isEmpty() && conflicto) {
			Entry<Long,String> aux = this.registro.first();
			if(aux.getKey()<=vuelo.arrivalTime()+120) {
				this.registro.dequeue();
				FlightArrival avion = new FlightArrival(aux.getValue(),aux.getKey());
				res.addLast(avion);
				this.mapaVuelos.remove(aux.getValue());
			}else
				conflicto = false;
		}
	}

}
