package de.mb.rest.service.dashboards;

public class Pais
{
	private String idPais;
	private String codePais;

	public Pais(String idPais, String codePais)
	{
		super();
		this.idPais = idPais;
		this.codePais = codePais;
	}

	public String getIdPais()
	{
		return idPais;
	}

	public String getCodePais()
	{
		return codePais;
	}

	public void setIdPais(String idPais)
	{
		this.idPais = idPais;
	}

	public void setCodePais(String codePais)
	{
		this.codePais = codePais;
	}

}
