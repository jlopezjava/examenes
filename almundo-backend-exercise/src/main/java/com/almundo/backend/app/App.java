package com.almundo.backend.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almundo.backend.app.Dispatcher;
import com.almundo.backend.app.ProducerTask;

/**
 * The Class App.
 */
public class App {
	
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(App.class);

	/**
	 * The main method.
	 * Solo contiene un ejemplo, el resto estan en la clase {@link AppTest}
	 *
	 * @param args the arguments
	 * @throws InterruptedException the interrupted exception
	 */
	public static void main(String[] args) throws InterruptedException {
		
		log.info("Inicio ejemplo llamadas y trabajo concurrente asincronico 2.");
		// definimos un alcance para cada cola
		Dispatcher dispatcher = new Dispatcher(5, 2, 1);
		ExecutorService executor = Executors.newFixedThreadPool(18);
		// Creamos un productor que simula 10 llamados concurrentes
		executor.execute(new ProducerTask(dispatcher, 1));
		executor.execute(new ProducerTask(dispatcher, 2));
		executor.execute(new ProducerTask(dispatcher, 3));
		executor.execute(new ProducerTask(dispatcher, 4));
		executor.execute(new ProducerTask(dispatcher, 5));
		executor.execute(new ProducerTask(dispatcher, 6));
		executor.execute(new ProducerTask(dispatcher, 7));
		executor.execute(new ProducerTask(dispatcher, 8));
		executor.execute(new ProducerTask(dispatcher, 9));
		executor.execute(new ProducerTask(dispatcher, 10));
		// Agregamos diferentes empleados con diferentes roles.
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.execute(new ConsumerTask(dispatcher, Role.SUPERVISOR));
		executor.execute(new ConsumerTask(dispatcher, Role.SUPERVISOR));
		executor.execute(new ConsumerTask(dispatcher, Role.DIRECTOR));
		executor.shutdown();
		executor.awaitTermination(2, TimeUnit.MINUTES);

	}

}