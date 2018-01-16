package com.almundo.backend.app;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase permite manejar la asignacion recurrentes de tareas (llamadas) y
 * asignar las mismas a los responsables correspondientes
 * (OPERADOR/SUPERVISOR/DIRECTOR).
 */
public class Dispatcher {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

	/** The task. */
	Task task;

	/** The common queue. */
	Queue<Task> commonQueue = new ConcurrentLinkedQueue<Task>();

	/** The employee queue. */
	LinkedBlockingQueue<Task> employeeQueue;

	/** The director queue. */
	LinkedBlockingQueue<Task> directorQueue;

	/** The supervisor queue. */
	LinkedBlockingQueue<Task> supervisorQueue;

	/** Default total employee. */
	int totalEmployee = 5;

	/** Default The total supervisor. */
	int totalSupervisor = 2;

	/** Default total director. */
	int totalDirector = 1;

	/**
	 * Inicializa un nuevo dispatcher.
	 */
	Dispatcher() {
		employeeQueue = new LinkedBlockingQueue<Task>(totalEmployee);
		directorQueue = new LinkedBlockingQueue<Task>(totalSupervisor);
		supervisorQueue = new LinkedBlockingQueue<Task>(totalDirector);
	}

	/**
	 * Inicializar un nuevo dispatcher indicando el total de empleados,
	 * supervisores y directores que existen.
	 *
	 * @param totalEmployee
	 *            the total employees
	 * @param totalSupervisor
	 *            the total supervisors
	 * @param totalDirector
	 *            the total director
	 */
	Dispatcher(int totalEmployee, int totalSupervisor, int totalDirector) {
		this.totalEmployee = totalEmployee;
		this.totalSupervisor = totalSupervisor;
		this.totalDirector = totalDirector;
		employeeQueue = new LinkedBlockingQueue<Task>(totalEmployee);
		directorQueue = new LinkedBlockingQueue<Task>(totalSupervisor);
		supervisorQueue = new LinkedBlockingQueue<Task>(totalDirector);
	}

	/**
	 * Get task by role.
	 *
	 * @param role
	 *            the role
	 * @param time
	 *            the time
	 * @return the by role
	 */
	public void getByRole(Role role, Long time) {
		switch (role) {
		case EMPLOYEE:
			if (employeeQueue.isEmpty())
				takeTaskOnHold(role);
			else
				log.info("Consumer " + role + " tomando la tarea: " + employeeQueue.poll() + " - Tiempo: " + time);
			break;
		case SUPERVISOR:
			if (supervisorQueue.isEmpty())
				takeTaskOnHold(role);
			else
				log.info("Consumer " + role + " tomando la tarea: " + supervisorQueue.poll() + " - Tiempo: " + time
						+ " - Pendientes: " + supervisorQueue.size());
			break;
		case DIRECTOR:
			if (directorQueue.isEmpty())
				takeTaskOnHold(role);
			else
				log.info("Consumer " + role + " tomando la tarea: " + directorQueue.poll() + " - Tiempo: " + time
						+ " - Pendientes: " + directorQueue.size());
			break;
		default:
			log.info("Aguante un instante, todos nuestros representantes estan ocupados");
			break;
		}
		if (log.isDebugEnabled()) {
			log.debug("\nPendientes operadores: " + employeeQueue.size() + "\n" + " Pendientes supervisores: "
					+ supervisorQueue.size() + "\n" + " Pendientes directores: " + directorQueue.size());
		}
	}

	/**
	 * Take task on hold.
	 *
	 * @param role
	 *            the role
	 */
	private void takeTaskOnHold(Role role) {
		if (!commonQueue.isEmpty()) {
			log.info("Consumer " + role + " tomando la tarea en espera: " + commonQueue.poll() + " - Pendientes: "
					+ commonQueue.size());
		}
	}

	/**
	 * Este metodo se encarga de asignar a los empleados disponibles las tareas
	 * (llamadas).
	 *
	 * @param task
	 *            the task
	 */
	public void dispatchCall(Task task) {
		addTask2EmployeeQueue(task);
	}

	/**
	 * Adds the task to employee queue
	 *
	 * @param queue
	 *            the queue
	 * @param task
	 *            the task
	 */
	private void addTask2EmployeeQueue(Task task) {
		try {
			this.task = task;
			this.task.setAssignTo(Role.EMPLOYEE);
			employeeQueue.add(task);
			log.info("Asignando la tarea: " + task.getId() + " a la cola de operadores.");
		} catch (IllegalStateException ex) {
			log.info(
					"En este momento todos nuestros operadores se encuentran ocupados, Se asigna la tarea a la cola de SUPERVISORES.");
			addTask2SupervisorQueue(task);
		}
	}

	/**
	 * Adds the task to supervisor queue
	 *
	 * @param queue
	 *            the queue
	 * @param task
	 *            the task
	 */
	private void addTask2SupervisorQueue(Task task) {
		try {
			this.task = task;
			this.task.setAssignTo(Role.SUPERVISOR);
			supervisorQueue.add(task);
			log.info("Asignando la tarea: " + task.getId() + " a la cola de supervisores.");
		} catch (IllegalStateException ex) {
			log.info(
					"En este momento todos nuestros SUPERVISORES se encuentran ocupados, Se asigna la tarea a la cola de DIRECTORES.");
			addTask2DirectorQueue(task);
		}
	}

	/**
	 * Adds the task to director queue
	 *
	 * @param queue
	 *            the queue
	 * @param task
	 *            the task
	 */
	private void addTask2DirectorQueue(Task task) {
		try {
			this.task = task;
			this.task.setAssignTo(Role.DIRECTOR);
			directorQueue.add(task);
			log.info("Asignando la tarea: " + task.getId() + " a la cola de directores.");
		} catch (IllegalStateException ex) {
			log.info(
					"En este momento todos nuestros DIRECTORES se encuentran ocupados, Se asigna la tarea a la cola de ESPERA...");
			addTask2CommonQueue(task);
		}
	}

	/**
	 * Adds the task to common queue
	 *
	 * @param queue
	 *            the queue
	 * @param task
	 *            the task
	 */
	private void addTask2CommonQueue(Task task) {
		log.info("Asignando la tarea: " + task.getId() + " a la cola de ESPERA.");
		this.task = task;
		this.task.setAssignTo(Role.ON_HOLD);
		commonQueue.add(task);

	}

	/**
	 * Gets the common queue.
	 *
	 * @return the common queue
	 */
	public Queue<Task> getCommonQueue() {
		return commonQueue;
	}

	/**
	 * Sets the common queue.
	 *
	 * @param commonQueue
	 *            the new common queue
	 */
	public void setCommonQueue(Queue<Task> commonQueue) {
		this.commonQueue = commonQueue;
	}

	/**
	 * Gets the employee queue.
	 *
	 * @return the employee queue
	 */
	public LinkedBlockingQueue<Task> getEmployeeQueue() {
		return employeeQueue;
	}

	/**
	 * Sets the employee queue.
	 *
	 * @param employeeQueue
	 *            the new employee queue
	 */
	public void setEmployeeQueue(LinkedBlockingQueue<Task> employeeQueue) {
		this.employeeQueue = employeeQueue;
	}

	/**
	 * Gets the director queue.
	 *
	 * @return the director queue
	 */
	public LinkedBlockingQueue<Task> getDirectorQueue() {
		return directorQueue;
	}

	/**
	 * Sets the director queue.
	 *
	 * @param directorQueue
	 *            the new director queue
	 */
	public void setDirectorQueue(LinkedBlockingQueue<Task> directorQueue) {
		this.directorQueue = directorQueue;
	}

	/**
	 * Gets the supervisor queue.
	 *
	 * @return the supervisor queue
	 */
	public LinkedBlockingQueue<Task> getSupervisorQueue() {
		return supervisorQueue;
	}

	/**
	 * Sets the supervisor queue.
	 *
	 * @param supervisorQueue
	 *            the new supervisor queue
	 */
	public void setSupervisorQueue(LinkedBlockingQueue<Task> supervisorQueue) {
		this.supervisorQueue = supervisorQueue;
	}

	/**
	 * Gets the total employee.
	 *
	 * @return the total employee
	 */
	public int getTotalEmployee() {
		return totalEmployee;
	}

	/**
	 * Sets the total employee.
	 *
	 * @param totalEmployee
	 *            the new total employee
	 */
	public void setTotalEmployee(int totalEmployee) {
		this.totalEmployee = totalEmployee;
	}

	/**
	 * Gets the total supervisor.
	 *
	 * @return the total supervisor
	 */
	public int getTotalSupervisor() {
		return totalSupervisor;
	}

	/**
	 * Sets the total supervisor.
	 *
	 * @param totalSupervisor
	 *            the new total supervisor
	 */
	public void setTotalSupervisor(int totalSupervisor) {
		this.totalSupervisor = totalSupervisor;
	}

	/**
	 * Gets the total director.
	 *
	 * @return the total director
	 */
	public int getTotalDirector() {
		return totalDirector;
	}

	/**
	 * Sets the total director.
	 *
	 * @param totalDirector
	 *            the new total director
	 */
	public void setTotalDirector(int totalDirector) {
		this.totalDirector = totalDirector;
	}

}
