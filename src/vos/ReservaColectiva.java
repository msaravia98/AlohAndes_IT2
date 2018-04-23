package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReservaColectiva  {
	
	@JsonProperty (value = "idColectivo")
	private Long idColectivo;
	
	@JsonProperty (value = "cliente")
	private Long cliente;
	
	@JsonProperty (value = "serviciosDeseados")
	private String serviciosDeseados;
	
	@JsonProperty (value = "fechaInicio")
	private String fechaInicio;
	
	@JsonProperty (value = "fechaRegistro")
	private String fechaRegistro;
	
	@JsonProperty (value = "fechaCancelacion")
	private String fechaCancelacion;
	
	@JsonProperty (value = "cantidadInmuebles")
	private Integer cantidadInmuebles;
	
	@JsonProperty (value = "Duracion")
	private Integer Duracion;
	
	@JsonProperty (value = "Costo")
	private Double Costo;
	
	@JsonProperty (value = "Multa")
	private Double Multa;
	
	@JsonProperty(value ="TipoInmueble")
	private String TipoInmueble;
	
	@JsonProperty (value = "IDReservas")
	private List<Reserva> IDReservas;
	
	@JsonProperty (value = "IDPropuestas")
	private List<Long> IDPropuestas;
	

	/**
	 * 
	 * @param idColectivo
	 * @param idCliente
	 * @param cantidadInmbuebles
	 * @param serviciosDeseados
	 * @param inicio
	 * @param fechaRegistro
	 * @param fechaCancelacion
	 * @param CantidadInmuebles
	 * @param Duracion
	 * @param costo
	 * @param multa
	 * @param tipoInmueble
	 */
	public ReservaColectiva(
			@JsonProperty(value="idColectivo") Long idColectivo,
			@JsonProperty(value="idCliente") Long idCliente,
			@JsonProperty(value="cantidadInmuebles") Integer cantidadInmbuebles,
			@JsonProperty(value="serviciosDeseados") String serviciosDeseados,
			@JsonProperty(value="fechaInicio") String inicio,
			@JsonProperty(value="fechaRegistro") String fechaRegistro,
			@JsonProperty(value="fechaCancelacion") String fechaCancelacion,
			@JsonProperty(value="Duracion") Integer Duracion,
			@JsonProperty(value= "costo") Double costo,
			@JsonProperty(value="multa")Double multa,
			@JsonProperty(value= "tipoInmueble") String tipoInmueble) {
		
		this.idColectivo= idColectivo;
		this.cliente = idCliente;
		this.cantidadInmuebles = cantidadInmbuebles;
		this.serviciosDeseados = serviciosDeseados;
		this.fechaInicio = inicio;
		this.fechaRegistro = fechaRegistro;
		this.fechaCancelacion = fechaCancelacion;
		this.cantidadInmuebles = cantidadInmbuebles;
		this.Duracion = Duracion;
		this.Costo = costo;
		this.Multa= multa;
		this.TipoInmueble = tipoInmueble;
	}

	public Double getMulta() {
		return Multa;
	}

	public void setMulta(Double multa) {
		Multa = multa;
	}

	public String getTipoInmueble() {
		return TipoInmueble;
	}

	public void setTipoInmueble(String tipoInmueble) {
		TipoInmueble = tipoInmueble;
	}

	public Long getIdColectivo() {
		return idColectivo;
	}

	public void setIdColectivo(Long idColecti) {
		idColectivo = idColecti;
	}

	public Long getCliente() {
		return this.cliente;
	}

	public void setIdCliente(Long idCliente) {

		this.cliente = idCliente;
	}

	public Integer getCantidadInmuebles() {
		return cantidadInmuebles;
	}

	public void setCantidadInmuebles(Integer cantidadInmuebles) {
		this.cantidadInmuebles = cantidadInmuebles;
	}

	public String getServiciosDeseados() {
		return serviciosDeseados;
	}

	public void setServiciosDeseados(String serviciosDeseados) {
		this.serviciosDeseados = serviciosDeseados;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(String fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}


	public Integer getDuracion() {
		return Duracion;
	}

	public void setDuracion(Integer duracion) {
		Duracion = duracion;
	}

	public Double getCosto() {
		return Costo;
	}

	public void setCosto(Double costo) {
		Costo = costo;
	}

	public List<Reserva> getIDReservas() {
		return IDReservas;
	}

	public void setIDReservas(List<Reserva> iDReservas) {
		IDReservas = iDReservas;
	}

	public List<Long> getIDPropuestas() {
		return IDPropuestas;
	}

	public void setIDPropuestas(List<Long> iDPropuestas) {
		IDPropuestas = iDPropuestas;
	}
	
}
