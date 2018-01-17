package de.mb.rest.service.dashboards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DashboardsService
{

	public static void main(String[] args)
	{
		try
		{
			TreeSet<Dashboard> criterioYCantidadDeHoteles = new TreeSet<Dashboard>();

			DashboardsService ds = new DashboardsService();

			System.out.println("Dashboard por ciudades");
//			criterioYCantidadDeHoteles = ds.popularHotelesEnCriterio(ds.buscarCiudades());

			System.out.println(criterioYCantidadDeHoteles.toString());

			// ahora voy a buscar hoteles por paises
			System.out.println("Dashboard por paises");
			// criterioYCantidadDeHoteles = ds.popularHotelesEnCriterioPaises();

			// ahora voy a buscar hoteles por Continentes -- Aguanta!!
			/**
			 * Voy a limitar a N paises por continentes porque sino no me va a dar las conexiones cuando busque los hoteles
			 */
			System.out.println("Dashboard por continente");
			// criterioYCantidadDeHoteles = ds.popularHotelesEnCriterioContinentes();

			System.out.println(criterioYCantidadDeHoteles.toString());

			System.out.println("Dashboard por si esta habilitado o no");
			List destinos = new ArrayList<String>();
			destinos.add("982");
			destinos.add("983");
			destinos.add("984");
			destinos.add("9");
			String scope = new String("cities");
			
			DisponibilidadPool dp = new DisponibilidadPool();
			dp.buscarDisponibilidadParaDestinos(destinos, scope);
//			System.out.println(ds.popularHotelesHabilitados(destinos, scope));

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}




	/**
	 * @param criterioYCantidadDeHoteles
	 * @return
	 * @throws JSONException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 */
	public List<String> buscarCiudades() throws JSONException, MalformedURLException, IOException, ProtocolException
	{
//		String recursoCiudades = recursoCiudades(null);
		List<String> ciudades = new ArrayList(); //this.getListaDeRecursos(recursoCiudades);
		ciudades.add("Barcelona");
		ciudades.add("Madrid");
		ciudades.add("Valencia");
		return ciudades;
	}

	public String buscarCiudades(String pais) throws JSONException, MalformedURLException, IOException, ProtocolException
	{
		String recursoCiudades = recursoCiudades(pais);
		StringBuilder result = new StringBuilder();
		List<String> listaDeRecursos = new ArrayList(); //this.getListaDeRecursos(recursoCiudades);
		listaDeRecursos.add("Barcelona");
		listaDeRecursos.add("Madrid");
		listaDeRecursos.add("Valencia");

		for (Iterator iterator = listaDeRecursos.iterator(); iterator.hasNext();)
		{
			result.append((String) iterator.next());
			if (iterator.hasNext())
			{
				result.append(",");
			}
		}
		return result.toString();
	}

	public List<Pais> buscarPaises(String continente) throws JSONException, MalformedURLException, IOException, ProtocolException
	{
		String recursoPaises = recursoPaises(continente);
		List<Pais> paises = getListaDeRecursosPais(recursoPaises);
		return paises;
	}

	public List<String> buscarContinentes() throws JSONException, MalformedURLException, IOException, ProtocolException
	{
		String inputStream = recursoContinentes();
		List<String> continentes = this.getListaDeRecursos(inputStream);
		return continentes;
	}

	/**
	 * @param inputStream
	 * @return
	 * @throws JSONException
	 */
	private List<String> getListaDeRecursos(String inputStream) throws JSONException
	{
		JSONObject obj = new JSONObject(inputStream);
		JSONArray jsonArrayItems = obj.getJSONArray("items");
		String key = "id";
		List<String> listaDeRecursos = new ArrayList<String>();
		for (int i = 0; i < jsonArrayItems.length(); i++)
		{
			JSONObject jRecurso = (JSONObject) jsonArrayItems.get(i);
			listaDeRecursos.add(jRecurso.getString(key));
		}
		return listaDeRecursos;
	}

	private List<Pais> getListaDeRecursosPais(String inputStream) throws JSONException
	{
		JSONObject obj = new JSONObject(inputStream);
		JSONArray jsonArrayItems = obj.getJSONArray("items");

		List<Pais> listaDeRecursos = new ArrayList<Pais>();
		for (int i = 0; i < jsonArrayItems.length(); i++)
		{
			JSONObject jRecurso = (JSONObject) jsonArrayItems.get(i);
			listaDeRecursos.add(new Pais(jRecurso.getString("id"), jRecurso.getString("code")));
		}
		return listaDeRecursos;
	}

	public TreeSet<Dashboard> popularHotelesEnCriterio(List ciudades) throws JSONException, MalformedURLException, IOException, ProtocolException
	{
		TreeSet<Dashboard> criterioYCantidadDeHoteles = new TreeSet<Dashboard>();
		for (Iterator<String> iterator = ciudades.iterator(); iterator.hasNext();)
		{
			String cities = iterator.next();
			JSONObject obj = new JSONObject(recursoHotelesByCriterio(cities));
			Dashboard d = new Dashboard(Long.valueOf(cities), Long.valueOf(obj.getJSONObject("paging").get("total").toString()));

			criterioYCantidadDeHoteles.add(d);
		}
		return criterioYCantidadDeHoteles;
	}

	public TreeSet<Dashboard> popularHotelesEnCriterioPaises() throws JSONException, MalformedURLException, IOException, ProtocolException
	{
		TreeSet<Dashboard> criterioYCantidadDeHoteles = new TreeSet<Dashboard>();
		List<Pais> buscarPaises = this.buscarPaises(null);
		for (Pais pais1 : buscarPaises)
		{
			JSONObject obj = new JSONObject(recursoHotelesByCriterio(this.buscarCiudades(pais1.getIdPais().toString())));
			Dashboard d = new Dashboard(Long.valueOf(pais1.getIdPais()), Long.valueOf(obj.getJSONObject("paging").get("total").toString()));
			criterioYCantidadDeHoteles.add(d);
			System.out.println(criterioYCantidadDeHoteles);
		}
		return criterioYCantidadDeHoteles;
	}

	public TreeSet<Dashboard> popularHotelesEnCriterioContinentes() throws JSONException, MalformedURLException, IOException, ProtocolException
	{
		TreeSet<Dashboard> criterioYCantidadDeHoteles = new TreeSet<Dashboard>();
		List<String> listaContinentesIds = this.buscarContinentes();
		for (String cont : listaContinentesIds)
		{
			Long cantidadDeHoteles = 0l;
			for (Iterator iterator = this.buscarPaises(cont).iterator(); iterator.hasNext();)
			{
				// No funciona el restque me trae mas de una ciudad por pais
				Pais pais = (Pais) iterator.next();
				JSONObject obj = new JSONObject(recursoHotelesByCriterio(this.buscarCiudades(pais.getIdPais())));
				cantidadDeHoteles += Long.valueOf(obj.getJSONObject("paging").get("total").toString());
			}
			;
			Dashboard d = new Dashboard(Long.valueOf(cont), Long.valueOf(cantidadDeHoteles));
			criterioYCantidadDeHoteles.add(d);
			System.out.println(criterioYCantidadDeHoteles);
		}
		return criterioYCantidadDeHoteles;
	}

	public static String recursoContinentes() throws MalformedURLException, IOException, ProtocolException
	{
		// Si se quiere leer el snapshot volcarlo a un
		// file y crear un iterator para ir leyendo de a
		// secciones
		String inputStream = "{\"items\":" + connectionRead(new URL("https://api.despegar.com/v3/continents")) + "}";
		return inputStream;
	}

	/**
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 */
	public static String recursoCiudades(String pais) throws MalformedURLException, IOException, ProtocolException
	{
		// Si se quiere leer el snapshot volcarlo a un
		// file y crear un iterator para ir leyendo de a
		// secciones
		URL url = new URL("https://api.despegar.com/v3/cities?" + ((pais != null) ? "country_id=" + pais + "&" : "")
				+ "product=HOTELS&language=ES&offset=10&limit=5");
		return connectionRead(url);
	}

	public static String getCodePaisDeCiudad(String id) throws MalformedURLException, IOException, ProtocolException, JSONException
	{
		// Se que esto no lo tendria que hacer y buscar en un snapshot total estos datos como ciudad y pais no cambian
		JSONObject objC = new JSONObject(connectionRead(new URL("https://api.despegar.com/v3/cities/" + id)));
		JSONObject objP = new JSONObject(connectionRead(new URL("https://api.despegar.com/v3/countries/" + objC.getString("country_id"))));
		return objP.getString("code");
	}

	public static String recursoPaises(String continente) throws MalformedURLException, IOException, ProtocolException
	{
		// Si se quiere leer el snapshot volcarlo a un
		// file y crear un iterator para ir leyendo de a
		// secciones
		URL url = new URL("https://api.despegar.com/v3/countries?language=es&product=HOTELS" + ((continente != null) ? "&continent_id=" + continente : "")
				+ "&offset=0&limit=10");
		return connectionRead(url);
	}

	public static String recursoHotelesByCriterio(String ciudad) throws MalformedURLException, IOException, ProtocolException
	{
		// TODO REVISAR ESTO!
		// Solo necesito el total de hoteles por ciudad entonces con obtener solo el paginador y el total por ciudad es suficiente , para el socpe ciudad
		URL url = new URL("https://api.despegar.com/v3/hotels?offset=0&limit=20" + ((ciudad != null && !ciudad.isEmpty()) ? "&cities=" + ciudad : ""));
		return connectionRead(url);
	}

	/**
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ProtocolException
	 */
	public static String connectionRead(URL url) throws IOException, ProtocolException
	{
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("X-ApiKey", "aa58b8708cf44b5893a0de0b2e410343");
		conn.setRequestProperty("Accept-Encoding", "gzip,deflat");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200)
		{
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((new GZIPInputStream(conn.getInputStream()))));

		StringBuilder responseStrBuilder = new StringBuilder();
		String inputStr;
		while ((inputStr = br.readLine()) != null)
		{
			responseStrBuilder.append(inputStr);
		}

		conn.disconnect();
		return responseStrBuilder.toString();
	}
}
