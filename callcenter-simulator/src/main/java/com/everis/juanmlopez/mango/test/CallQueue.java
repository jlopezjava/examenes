package com.everis.juanmlopez.mango.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

public class CallQueue {

	private static CallQueue instance;
	private int counter;
	private SimpleDateFormat formatter;
	private LinkedBlockingQueue<Call> queue;

	
	public static void queueCall(int duration) {
		try {
			Call call = new Call(getInstance().counter++, duration);
			log("Colocando persona " + call.getNumber() + " con un tiempo de " + call.getDuration() + " seconds");
			getInstance().getQueue().put(call);
		} catch (InterruptedException e) {
			log("Opa paso algo a insertar en la cola");
		}
	}

	public static Call retrieveCall() {
		Call call = getInstance().getQueue().poll();
		if (call != null) {
			log("Atendiendo persona " + call.getNumber());
		}
		return call;
	}

	private static CallQueue getInstance() {
		if (instance == null) {
			instance = new CallQueue();
		}
		return instance;
	}
	
	public static boolean isEmpty() {
	
		return getInstance().getQueue().isEmpty();
	}

	private static void log(String s) {
		System.out.println("[" + getInstance().formatter.format(new Date()) + "][EsperaQueue] " + s);
	}


	private CallQueue() {
		this.queue = new LinkedBlockingQueue<Call>();
		this.counter = 1;
		this.formatter = new SimpleDateFormat("HH:mm:ss");
	}

	public LinkedBlockingQueue<Call> getQueue() {
		return queue;
	}

	public void setQueue(LinkedBlockingQueue<Call> queue) {
		this.queue = queue;
	}
}
