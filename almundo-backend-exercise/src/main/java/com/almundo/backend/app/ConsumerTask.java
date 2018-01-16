package com.almundo.backend.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almundo.backend.app.config.*;

/**
 * Esta clase representa a un empleado independientemente de su rol
 * (EMPLEADO/SUPERVISOR/DIRECTOR)
 */
public class ConsumerTask implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

	/** The dispatcher. */
	Dispatcher dispatcher;
	
	/** The role. */
	Role role;


	/**
	 * Instantiates a new consumer tark.
	 *
	 * @param dispatcher the dispatcher
	 * @param role the role
	 */
	ConsumerTask(Dispatcher dispatcher, Role role) {
		this.dispatcher = dispatcher;
		this.role = role;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {		
			Long time = Config.asignRandomTimeToTask(); 
			dispatcher.getByRole(role, time);
			Thread.sleep(time);
		} catch (InterruptedException ex) {
			log.error(ex.getMessage());
		}
	}

}
