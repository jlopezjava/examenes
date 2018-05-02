package de.mb.rest.service.dashboards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public class DisponibilidadPool
{
	public Map<String, Object> buscarDisponibilidadParaDestinos(List<String> destinos, String scope) throws InterruptedException, ExecutionException, TimeoutException
	{
		Map<String, Object> respuesta = new HashMap<String, Object>();
		TreeSet<Dashboard> criterioYCantidadDeHotelesDisponibles = new TreeSet<Dashboard>();
		List<String> criterioSinResultados = new ArrayList<String>();
		ExecutorService executor = Executors.newFixedThreadPool(destinos.size());
		List<DisponibilidadTrhead> futureList = new ArrayList<DisponibilidadTrhead>();
		List<Future<Dashboard>> listaResultado = new ArrayList<Future<Dashboard>>();

	
		for (String destino : destinos)
		{
			futureList.add(new DisponibilidadTrhead(destino));		
		}
		listaResultado  = executor.invokeAll(futureList);

		for (Future<Dashboard> future : listaResultado)
		{
			Dashboard d = null;
			try
			{
				d = future.get();
			}
			catch (Exception e) {
			    // interrupts if there is any possible error
				future.cancel(true);
			}
			if (d.getCantidadHoteles().equals(0l))
			{
				criterioSinResultados.add(d.getRecursoID().toString());
			}
			else
			{
				criterioYCantidadDeHotelesDisponibles.add(d);
			}
		}
		executor.shutdown();
		while (!executor.isTerminated())
		{
		}
		respuesta.put("unavailables", criterioSinResultados);
		respuesta.put("availables", criterioYCantidadDeHotelesDisponibles);
		return respuesta;
	}
}
