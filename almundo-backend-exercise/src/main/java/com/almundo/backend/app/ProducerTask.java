package com.almundo.backend.app;

/**
 * Esta clase permite simular la concurrencia de llamadas.
 * 
 * Comparte la clase dispatcher, que contiene la cola de asignación de tareas.
 */
public class ProducerTask implements Runnable {

	/** The dispatcher. */
	Dispatcher dispatcher;
	int id = 0;
	int total = 0;
	boolean preloadedQueues = false;

	/**
	 * Inicializa un nuevo producer task con un id
	 *
	 * @param dispatcher
	 *            the dispatcher
	 */
	ProducerTask(Dispatcher dispatcher, boolean preloadedQueues, int total) {
		this.dispatcher = dispatcher;
		this.total = total;
		this.preloadedQueues = preloadedQueues;
	}

	/**
	 * Inicializa un nuevo producer task con un id
	 *
	 * @param dispatcher
	 *            the dispatcher
	 */
	ProducerTask(Dispatcher dispatcher, int id) {
		this.dispatcher = dispatcher;
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if (!preloadedQueues)
			dispatcher.dispatchCall(new Task(id));
		else {
			for (int i = 0; i < total; i++) {
				dispatcher.dispatchCall(new Task(i));
			}
		}

	}
}
