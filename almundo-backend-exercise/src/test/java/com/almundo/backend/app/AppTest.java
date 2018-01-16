package com.almundo.backend.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(AppTest.class);

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	/**
	 * Simulando 10 llamadas concurrentes y 8 empleados trabajando en este caso
	 * 5 OPERADORES, 2 SUPERVISORES y 1 DIRECTOR. Deberian trabajar todos al
	 * menos en una tarea.
	 * 
	 * @throws InterruptedException
	 * 
	 */
	public void testDispatch10TaskConcurrency() throws InterruptedException {

		log.info("********** Inicio test llamadas y trabajo concurrente asincronico **********.");
		// definimos un alcance para cada cola
		Dispatcher dispatcher = new Dispatcher(5, 2, 1);
		ExecutorService executor = Executors.newFixedThreadPool(20);
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

	/**
	 * Simulamos 10 llamadas concurrentes y asignación de empleados con
	 * diferentes roles, incrementando nuevamente el ingreso de tareas. Las
	 * tareas que exceden la cantidad de empleados disponibles deberían
	 * asignarse a una cola en comun que posteriormente los empleados que se
	 * liberan tomarán cuando se liberen o en este caso
	 * 
	 * @throws InterruptedException
	 * 
	 */
	public void testManyEmployeesTakeTaskOnHold() throws InterruptedException {
		log.info("********** Inicio test muchos empleados tomando trabajos en espera **********.");
		// definimos un alcance para cada cola
		Dispatcher dispatcher = new Dispatcher(5, 2, 1);
		ExecutorService executor = Executors.newFixedThreadPool(24);
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
		// Agregamos diferentes empleados con diferentes roles. Estos empleados
		// superan el alcance de la cola de manera que pueden llegar a tomar
		// trabajos en espera.
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.execute(new ConsumerTask(dispatcher, Role.SUPERVISOR));
		executor.execute(new ConsumerTask(dispatcher, Role.SUPERVISOR));
		executor.execute(new ConsumerTask(dispatcher, Role.DIRECTOR));
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.execute(new ConsumerTask(dispatcher, Role.EMPLOYEE));
		executor.shutdown();
		executor.awaitTermination(2, TimeUnit.MINUTES);

	}

}