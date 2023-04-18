package aed.individual5;

import es.upm.aedlib.Pair;
import es.upm.aedlib.map.*;


public class TempUtils {

	public static Map<String,Integer> maxTemperatures(long startTime, long endTime, TempData[] tempData) {
		Map<String, Integer> res = new HashTableMap<>();
		for(int i = 0; i<tempData.length ;i++) {
			if(estaEnElRango(startTime,endTime,tempData[i].getTime()) 
					&& (res.get(tempData[i].getLocation()) == null || res.get(tempData[i].getLocation()) <= tempData[i].getTemperature() )) 
				res.put(tempData[i].getLocation(), tempData[i].getTemperature());
		}
		return res;
	}


	public static Pair<String,Integer> maxTemperatureInComunidad(long startTime, long endTime, String region, TempData[] tempData,
			Map<String,String> comunidadMap) {
		Pair<String,Integer> maxTemperatureRes = null;
		for(int i = 0 ; i<tempData.length; i++) {
			if(comunidadMap.get(tempData[i].getLocation()).equals(region) && estaEnElRango(startTime,endTime,tempData[i].getTime()) 
					&&(maxTemperatureRes == null || tempData[i].getTemperature() > maxTemperatureRes.getRight())) 
				maxTemperatureRes = new Pair<>(tempData[i].getLocation(),tempData[i].getTemperature());
		}
		return maxTemperatureRes;
	}

	
	//Metodos Auxiliares

	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @param mesureTime
	 * @return
	 * Comprueba que el tiempo en el que se ha medidio la temperatura este comprendido entre el intervalo valido
	 */


	private static boolean estaEnElRango(long startTime, long endTime, long mesureTime) {
		return mesureTime >= startTime && mesureTime <= endTime;
	}

}
