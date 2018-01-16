package com.almundo.backend.app.config;

import java.util.Random;

/**
 * Esta clase permite configurar algunas propiedades de la aplicacion.
 * 
 */
public class Config {
	
	public static int TIME_MIN = 5000;
	public static int TIME_MAX = 10000;
	
	/**
	 * Este metodo permite asignar aleatoriamente un valor 
	 * entre 5 y 10 segundos.
	 *
	 * @return the long
	 */
	public static Long asignRandomTimeToTask(){
		Random randomGenerator = new Random();
		int value = randomGenerator.nextInt(TIME_MAX - TIME_MIN + 1) + TIME_MIN;
		return new Long(value);
	}

}
