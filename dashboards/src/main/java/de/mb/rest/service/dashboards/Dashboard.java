package de.mb.rest.service.dashboards;

public class Dashboard implements Comparable<Dashboard>
{
	private Long recursoID;
	private Long cantidadHoteles;

	
	public Dashboard(Long ciudadID, Long cantidadHoteles)
	{
		super();
		this.recursoID = ciudadID;
		this.cantidadHoteles = cantidadHoteles;
	}

	public Dashboard()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(Dashboard o)
	{
		if(o.cantidadHoteles==null) return 1;
		
 		return o.cantidadHoteles.compareTo(this.cantidadHoteles) ;
	}

	@Override
	public String toString()
	{
		return "Dashboard [recursoID=" + recursoID + ", cantidadHoteles=" + cantidadHoteles + "]";
	}

	public Long getRecursoID()
	{
		return recursoID;
	}

	public Long getCantidadHoteles()
	{
		return cantidadHoteles;
	}

	public void setRecursoID(Long ciudadID)
	{
		this.recursoID = ciudadID;
	}

	public void setCantidadHoteles(Long cantidadHoteles)
	{
		this.cantidadHoteles = cantidadHoteles;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cantidadHoteles == null) ? 0 : cantidadHoteles.hashCode());
		result = prime * result + ((recursoID == null) ? 0 : recursoID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dashboard other = (Dashboard) obj;
		if (cantidadHoteles == null)
		{
			if (other.cantidadHoteles != null)
				return false;
		}
		else if (!cantidadHoteles.equals(other.cantidadHoteles))
			return false;
		if (recursoID == null)
		{
			if (other.recursoID != null)
				return false;
		}
		else if (!recursoID.equals(other.recursoID))
			return false;
		return true;
	}

}
