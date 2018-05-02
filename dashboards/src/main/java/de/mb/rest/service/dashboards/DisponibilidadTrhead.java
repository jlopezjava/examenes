package de.mb.rest.service.dashboards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.zip.GZIPInputStream;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.mb.rest.service.exceptions.NotFoundException;

public class DisponibilidadTrhead implements Callable<Dashboard>
{

	private String destino = new String();

	public DisponibilidadTrhead(String destino)
	{
		super();
		this.destino = destino;

	}

	@Override
	public String toString()
	{
		return "DisponibilidadTrhead [destino=" + destino + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	@Override
	public Dashboard call()
	{
		Dashboard destino = null;
		try
		{
			destino = busquedaYObtencionDeDestinoYDisponibilidad();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.call();
			throw new NotFoundException("Scope Hubo un problema al buscar el destino");
		}
		return destino;
	}

	/**
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 * @throws JSONException
	 */
	private Dashboard busquedaYObtencionDeDestinoYDisponibilidad() throws MalformedURLException, IOException, ProtocolException, JSONException
	{
		String inputStream;
		inputStream = this.recursoHotelesDisponible(destino);
		JSONObject obj = new JSONObject(inputStream);
		Long valueOf = Long.valueOf(obj.getJSONObject("paging").get("total").toString());
		return new Dashboard(Long.valueOf(destino), valueOf);
	}

	public String recursoHotelesDisponible(String ciudad) throws MalformedURLException, IOException, ProtocolException, JSONException
	{
		// Solo necesito el total de hoteles por ciudad entonces con obtener solo el paginador y el total por ciudad es suficiente , para el socpe ciudad
		URL url = new URL("https://api.despegar.com/v3/hotels/prices?country=" + this.getCodePaisDeCiudad(ciudad) + "&destinations=" + ciudad
				+ "&distribution=2");
		return connectionRead(url);
	}

	public static String connectionRead(URL url) throws IOException, ProtocolException
	{
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("X-ApiKey", "c6f65de19e5749acbff0cebe37a43174");
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

	public static String getCodePaisDeCiudad(String id) throws MalformedURLException, IOException, ProtocolException, JSONException
	{
		// Se que esto no lo tendria que hacer y buscar en un snapshot total estos datos como ciudad y pais no cambian
		JSONObject objC = new JSONObject(connectionRead(new URL("https://api.despegar.com/v3/cities/" + id)));
		JSONObject objP = new JSONObject(connectionRead(new URL("https://api.despegar.com/v3/countries/" + objC.getString("country_id"))));
		return objP.getString("code");
	}

	public String getDestino()
	{
		return destino;
	}

	public void setDestino(String destino)
	{
		this.destino = destino;
	}

}
