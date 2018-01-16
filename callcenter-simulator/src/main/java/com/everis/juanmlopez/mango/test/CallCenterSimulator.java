package com.everis.juanmlopez.mango.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallCenterSimulator {

	/**
	 * La idea del ejercicio es en base a un vector (queue) son personas -producer-  esperan ser atendidas estos "Atenders/Consumers"
	 * tiene que retornar el tiempo maximo de trabajo del agente despues de atender todas las personas, las personas tienen que ser atendidas a medida que llegan FIFO
	 * @param tiempoDeCliente
	 * @param agentes
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public int tiempoMaximoDeTrabajoParaCajero(int[] tiempoDeCliente, int agentes)
			throws InterruptedException, ExecutionException {

		for (int j = 0; j < tiempoDeCliente.length; j++) {
			CallQueue.queueCall(tiempoDeCliente[j]);
		}

		ExecutorService executor = Executors.newFixedThreadPool(agentes);
		List<Future<Integer>> agentesList = new ArrayList<Future<Integer>>();

		for (int j = 0; j < agentes; j++) {
			agentesList.add(executor.submit(new ServiceAgent(j)));

		}

		List<Integer> tiempos = new ArrayList<>();
		for (Iterator<Future<Integer>> iterator = agentesList.iterator(); iterator.hasNext();) {
			Future<Integer> future = iterator.next();
			tiempos.add(future.get());
		}

		executor.shutdownNow();
		return tiempos.stream().mapToInt(i -> i).max().getAsInt();

	}

	public static void main(String... args) throws InterruptedException, ExecutionException {

		CallCenterSimulator callTest = new CallCenterSimulator();
		int[] tiempos = { 4, 2, 2, 3,3 };
		System.out.println(callTest.tiempoMaximoDeTrabajoParaCajero(tiempos, 4));
	}
}
