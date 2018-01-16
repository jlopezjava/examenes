package com.almundo.backend.app;

/**
 * Esta clase representa una llamada, a la 
 * cual le asignamos un id y una cola a la cual
 * fue asignada.
 */
public class Task {
	
	/** The id. */
	int id;
	
	/** The assign to. */
	Role assignTo;
			
	/**
	 * Instantiates a new task.
	 *
	 * @param id the id
	 */
	public Task(int id) {
		super();
		this.id = id;
	}

	/**
	 * Instantiates a new task.
	 *
	 * @param id the id
	 * @param status the status
	 */
	public Task(int id, Role status) {
		super();
		this.id = id;
		this.assignTo = status;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the assign to.
	 *
	 * @return the assign to
	 */
	public Role getAssignTo() {
		return assignTo;
	}

	/**
	 * Sets the assign to.
	 *
	 * @param assignTo the new assign to
	 */
	public void setAssignTo(Role assignTo) {
		this.assignTo = assignTo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Task [id=" + id + ", assignTo=" + assignTo + "]";
	}
	
}
