package com.almundo.backend.app;

import org.junit.Assert;
import org.junit.Test;

/**
 * The Class DispatcherTest.
 * 
 */
public class DispatcherTest {

	// asignamos un total de 5 operadores, 3 supervisores y 1 director.
	//@InjectMocks
	Dispatcher dispatcher = new Dispatcher(5, 3, 1);;	
	Task task1 = new Task(1);
	Task task2 = new Task(2);
	Task task3 = new Task(3);
	Task task4 = new Task(4);
	Task task5 = new Task(5);
	Task task6 = new Task(6);
	Task task7 = new Task(7);
	Task task8 = new Task(8);
	Task task9 = new Task(9);
	Task task10 = new Task(10);


	/**
	 * Testeamos la prioridad de asignacion de tareas. En este caso si la cola
	 * de OPERADORES esta vacia, la tarea debe asignarse por prioridad a dicha
	 * cola.
	 */
	@Test
	public void dispatchTask2EmployeeQueue() {
		// vaciamos la cola para asegurarnos qeu cuan
		dispatcher.getEmployeeQueue().clear();
		dispatcher.dispatchCall(task1);
		Assert.assertEquals(1, dispatcher.getEmployeeQueue().size());
	}

	/**
	 * Testeamos la prioridad de asignacion de tareas. En este caso si la cola
	 * de OPERADORES esta llena, la tarea debe asignarse a la cola de
	 * SUPERVISORES.
	 */
	@Test
	public void dispatchTask2SupervisorQueue() {
		// vaciamos la cola para asegurarnos qeu cuan
		dispatcher.getSupervisorQueue().clear();
		dispatcher.dispatchCall(task1);
		dispatcher.dispatchCall(task2);
		dispatcher.dispatchCall(task3);
		dispatcher.dispatchCall(task4);
		dispatcher.dispatchCall(task5);
		//todos los operadores estan ocupados
		dispatcher.dispatchCall(task6);
		Assert.assertEquals(1, dispatcher.getSupervisorQueue().size());
	}

	/**
	 * Testeamos la prioridad de asignacion de tareas. En este caso si la cola
	 * de SUPERVISORES esta llena, la tarea debe asignarse a la cola de
	 * DIRECTORES.
	 */
	@Test
	public void dispatchTask2DirectorQueue() {
		// vaciamos la cola para asegurarnos qeu cuan
		dispatcher.getEmployeeQueue().clear();
		dispatcher.getSupervisorQueue().clear();
		dispatcher.getDirectorQueue().clear();	
		dispatcher.dispatchCall(task1);
		dispatcher.dispatchCall(task2);
		dispatcher.dispatchCall(task3);
		dispatcher.dispatchCall(task4);
		dispatcher.dispatchCall(task5);
		//todos los operadores estan ocupados
		dispatcher.dispatchCall(task6);
		dispatcher.dispatchCall(task7);
		dispatcher.dispatchCall(task8);
		//todos los supervisores estan ocupados
		dispatcher.dispatchCall(task9);
		Assert.assertEquals(1, dispatcher.getDirectorQueue().size());
	}

	/**
	 * Testeamos como los hilos van a ir consumiendo las tareas en base a su
	 * rol, en este caso OPERADOR y si su cola esta vacia chequea si la cola en
	 * comun, tiene tareas en espera.
	 */
	@Test
	public void testGetByRoleEmployee() {
		// vaciamos la cola de tareas.
		dispatcher.getEmployeeQueue().clear();
		// asignamos una tarea en la cola de empleados
		dispatcher.dispatchCall(task1);
		Role role = Role.EMPLOYEE;
		// con este parametro se simula la demora de la llamada.
		Long time = 5000L;
		// verificamos que consuma la tarea
		Assert.assertEquals(1, dispatcher.getEmployeeQueue().size());
		dispatcher.getByRole(role, time);
		Assert.assertEquals(0, dispatcher.getEmployeeQueue().size());
	}

	/**
	 * Testeamos como los hilos van a ir consumiendo las tareas en base a su
	 * rol, en este caso SUPERVISOR y si su cola esta vacia chequea si la cola
	 * en comun, tiene tareas en espera.
	 */
	@Test
	public void testGetByRoleSupervisor() {
		// vaciamos la cola de tareas.
		dispatcher.getEmployeeQueue().clear();
		dispatcher.getSupervisorQueue().clear();
		dispatcher.dispatchCall(task1);
		dispatcher.dispatchCall(task2);
		dispatcher.dispatchCall(task3);
		dispatcher.dispatchCall(task4);
		dispatcher.dispatchCall(task5);
		//todos los operadores estan ocupados
		dispatcher.dispatchCall(task6);
		//todos los supervisores estan ocupados
		Role role = Role.SUPERVISOR;
		// con este parametro se simula la demora de la llamada.
		Long time = 5000L;
		// verificamos que consuma la tarea
		Assert.assertEquals(1, dispatcher.getSupervisorQueue().size());
		dispatcher.getByRole(role, time);
		Assert.assertEquals(0, dispatcher.getSupervisorQueue().size());
	}

	/**
	 * Testeamos como los hilos van a ir consumiendo las tareas en base a su
	 * rol, en este caso DIRECTOR y si su cola esta vacia chequea si la cola en
	 * comun, tiene tareas en espera.
	 */
	@Test
	public void testGetByRoleDirector() {
		// vaciamos la cola de tareas.
		dispatcher.getEmployeeQueue().clear();
		dispatcher.getSupervisorQueue().clear();
		dispatcher.getDirectorQueue().clear();	
		dispatcher.dispatchCall(task1);
		dispatcher.dispatchCall(task2);
		dispatcher.dispatchCall(task3);
		dispatcher.dispatchCall(task4);
		dispatcher.dispatchCall(task5);
		//todos los operadores estan ocupados
		dispatcher.dispatchCall(task6);
		dispatcher.dispatchCall(task7);
		dispatcher.dispatchCall(task8);
		//todos los supervisores estan ocupados
		dispatcher.dispatchCall(task9);
		Role role = Role.DIRECTOR;
		// con este parametro se simula la demora de la llamada.
		Long time = 5000L;
		// verificamos que consuma la tarea
		Assert.assertEquals(1, dispatcher.getDirectorQueue().size());
		dispatcher.getByRole(role, time);
		Assert.assertEquals(0, dispatcher.getDirectorQueue().size());
	}

}
