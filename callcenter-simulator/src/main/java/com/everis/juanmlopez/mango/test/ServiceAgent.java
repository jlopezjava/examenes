package com.everis.juanmlopez.mango.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

public class ServiceAgent implements  Callable<Integer> {

	private long callExpiration;
	private SimpleDateFormat formatter;
	private int id;
	private int duration;
	private boolean running = true;
	private ServiceAgentStatus status;


	public ServiceAgent(int id) {
		this.id = id;
		this.status = ServiceAgentStatus.FREE;
		formatter = new SimpleDateFormat("HH:mm:ss");
	}

	@Override
	public Integer call() {
		while (running) {
			if (status == ServiceAgentStatus.FREE) {
				Call call = CallQueue.retrieveCall();
				if (call != null) {
					log("Contestando llamada " + call.getNumber());
					callExpiration = System.currentTimeMillis() + (call.getDuration() * 1000);
					status = ServiceAgentStatus.IN_A_CALL;
					duration += call.getDuration();
				}
			} else {
				if (System.currentTimeMillis() > callExpiration) {
					status = ServiceAgentStatus.FREE;
					if (CallQueue.isEmpty()) this.stop();
				}
			}
		}
		return duration ;
	}


	public void stop() {
		running = false;
	}

	private void log(String s) {
		System.out.println("[" + formatter.format(new Date()) + "][ServiceAgent][Agent " + id + "] " + s);
	}



}
