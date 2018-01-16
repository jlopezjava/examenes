package com.everis.juanmlopez.mango.test;

import java.io.Serializable;

public class Call implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int duration;
	private int number;


	public Call(int number, int duration) {
		this.number = number;
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public int getNumber() {
		return number;
	}
}
