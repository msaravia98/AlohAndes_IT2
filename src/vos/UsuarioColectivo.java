package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class UsuarioColectivo {
	
	@JsonProperty(value="idPersona")
	private Long idPersona;
	
	@JsonProperty(value="idReservaColectiva")
	private Long idReservaColectiva;
	
	public UsuarioColectivo(
			@JsonProperty(value="idPersona")Long id,
			@JsonProperty(value="idPersona")Long res) {
			
		this.idPersona = id;
		this.idReservaColectiva = res;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Long getIdReservaColectiva() {
		return idReservaColectiva;
	}

	public void setIdReservaColectiva(Long idReservaColectiva) {
		this.idReservaColectiva = idReservaColectiva;
	}
	
	

}
