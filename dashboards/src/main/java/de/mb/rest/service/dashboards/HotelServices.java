package de.mb.rest.service.dashboards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.mb.rest.service.exceptions.NotFoundException;

@Path("/")
public class HotelServices
{

	@GET
	@Path("cities")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getCities()
	{
		List<String> ciudades = new ArrayList(); //this.getListaDeRecursos(recursoCiudades);
		ciudades.add("Barcelona");
		ciudades.add("Madrid");
		ciudades.add("Valencia");
		return ciudades;
	}
	@GET
	@Path("hotels")
	@Produces(MediaType.APPLICATION_JSON)
	public TreeSet<Dashboard> getHotels(@QueryParam("scope") String scope)
	{
		DashboardsService dbS = new DashboardsService();
		TreeSet<Dashboard> criterioYCantidadDeHoteles = new TreeSet<Dashboard>();
		try
		{
			if ("cities".equals(scope))
			{
				criterioYCantidadDeHoteles = dbS.popularHotelesEnCriterio(dbS.buscarCiudades());
			}
			else if ("countries".equals(scope))
			{
				criterioYCantidadDeHoteles = dbS.popularHotelesEnCriterioPaises();
			}
			else if ("continents".equals(scope))
			{
				criterioYCantidadDeHoteles = dbS.popularHotelesEnCriterioContinentes();
			}
			else
			{
				throw new NotFoundException("Scope " + scope + ", el scope es incorrecto");
			}

		}
		catch (Throwable e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NotFoundException("Upss , ocurrio un problema al utilizar el servicio");
		}
		
		return criterioYCantidadDeHoteles;
	}
	
	/**
	 * availabilities
	 * @param scope
	 * @return
	 */
	@GET
	@Path("availabilities")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object>  getAvailabilities(@QueryParam("destinations")String destinoMultiple,@QueryParam("scope") String scope)
	{
		DisponibilidadPool dbS = new DisponibilidadPool();
		List destinos = new ArrayList<String>();
		for (String des : destinoMultiple.split(",")) { 
			destinos.add(des);
		}
		Map<String, Object> popularHotelesHabilitados;
		try
		{
			popularHotelesHabilitados = dbS.buscarDisponibilidadParaDestinos(destinos, scope);
			System.out.println(popularHotelesHabilitados);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NotFoundException("Upss , ocurrio un problema al utilizar el servicio");
		}
		System.out.println(popularHotelesHabilitados);
		return popularHotelesHabilitados;
	}

}
