package vos;




import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa una Rerserva que es la representacion de un Contrato en
 * el caso de estudio
 * 
 * @author sebastian
 *
 */
public class Reserva {

	// ----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	// ----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Id de la reserva
	 */
	@JsonProperty(value = "id")
	private Long id;

	/**
	 * Fecha en la que se registró la reserva en el sistema
	 */
	@JsonProperty(value = "fecha_registro")
	private String fecha_registro;

	/**
	 * Fecha en la que se canceló la reserva
	 */
	@JsonProperty(value = "fecha_cancelacion")
	private String fecha_cancelacion;

	/**
	 * Fecha en la que la persona empieza a hacer uso del inmueble
	 */
<<<<<<< HEAD
	@JsonProperty(value = "fecha_inicio")
=======
	@JsonProperty(value="fecha_inicio")
>>>>>>> refs/remotes/origin/master
	private String fecha_inicio;

	/**
	 * Duracion de la estadia en DIAS
	 */
	@JsonProperty(value = "duracion")
	private Integer duracion;

	/**
	 * Costo total de la reserva
	 */
	@JsonProperty(value = "costo_total")
	private Double costo_total;

	/**
	 * Determina la cantidad de personas que ocuparan un inmueble por medio de esta
	 * reserva
	 */
	@JsonProperty(value = "cantidad_personas")
	private Integer cantidad_personas;
<<<<<<< HEAD

	/**
	 * el costo se hay multa
	 */
	@JsonProperty(value = "multa")
	private Double multa;

	@JsonProperty(value = "idColectivo")
	private Long idColectivo;

=======
	
	
	/**
	 * el costo se hay multa
	 */
	@JsonProperty(value="multa")
	private Double multa;
	
	
	@JsonProperty(value="IdColectivo")
	private Long IdColectivo;
	
>>>>>>> refs/remotes/origin/master
	/**
	 * propuesta de la reserva
	 */
	@JsonProperty(value = "idPropuesta")
	private Long idPropuesta;

	/**
	 * cliente que le corresponde a cada reserva
	 */
	@JsonProperty(value = "idCliente")
	private Long idCliente;

	// ----------------------------------------------------------------------------------------------------------------------------------
	// METODO CONSTRUCTOR
	// ----------------------------------------------------------------------------------------------------------------------------------

	/**
<<<<<<< HEAD
	 * Metodo constructor de la clase Reserva <b>post: </b> Crea la reserva con los
	 * valores que entran por parametro
	 * 
=======
>>>>>>> refs/remotes/origin/master
	 * @param id
	 * @param fecha_registro
	 * @param fecha_cancelacion
	 * @param fecha_inicio_estadia
	 * @param duracion
	 * @param costo_total
	 * @param cantidad_personas
	 * @param valorMulta
	 * @param IDColectivo
	 * @param propuesta
	 * @param cliente
	 */
<<<<<<< HEAD
	public Reserva(@JsonProperty(value = "id") Long id, 
			@JsonProperty(value = "fecha_registro") String fecha_registro,
			@JsonProperty(value = "fecha_cancelacion") String fecha_cancelacion,
			@JsonProperty(value = "fecha_inicio_estadia") String fecha_inicio,
			@JsonProperty(value = "duracion") Integer duracion, 
			@JsonProperty(value = "costo_total") Double costo_total,
			@JsonProperty(value = "cantidad_personas") Integer cantidad_personas,
			@JsonProperty(value = "valorMulta") Double valorMulta,
			@JsonProperty(value = "idColectivo") Long idColectivo, 
			@JsonProperty(value = "idPropuesta") Long idPropuesta,
			@JsonProperty(value = "idCliente") Long idCliente) {
		this.id = id;
		this.fecha_registro = fecha_registro;
		this.fecha_cancelacion = fecha_cancelacion;
		this.fecha_inicio = fecha_inicio;
		this.duracion = duracion;
		this.costo_total = costo_total;
		this.cantidad_personas = cantidad_personas;
		this.multa = valorMulta;
		this.idColectivo = idColectivo;
		this.idPropuesta = idPropuesta;
		this.idCliente = idCliente;
=======
	public Reserva(
			@JsonProperty(value="id") Long id,
			@JsonProperty(value="fecha_registro") String fecha_registro,
			@JsonProperty(value="fecha_cancelacion") String fecha_cancelacion,
			@JsonProperty(value="fecha_inicio_estadia") String fecha_inicio_estadia,
			@JsonProperty(value="duracion") Integer duracion,
			@JsonProperty(value="costo_total") Double costo_total,
			@JsonProperty(value="cantidad_personas") Integer cantidad_personas,
			@JsonProperty(value="valorMulta") Double valorMulta,
			@JsonProperty(value="IDColectivo") Long IDColectivo,
			@JsonProperty(value= "propuesta") Propuesta propuesta,
			@JsonProperty(value= "cliente") Cliente cliente) {
		this.id = id;
		this.fecha_registro = fecha_registro;
		this.fecha_cancelacion = fecha_cancelacion;
		this.fecha_inicio = fecha_inicio_estadia;
		this.duracion = duracion;
		this.costo_total = costo_total;
		this.cantidad_personas = cantidad_personas;
		this.multa= valorMulta;
		//TODO inizialicar propuesta y cliente
		
		this.propuesta= propuesta;
		this.cliente=cliente;
		
	}
>>>>>>> refs/remotes/origin/master

	}

	// ----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE LA CLASE
	// ----------------------------------------------------------------------------------------------------------------------------------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public String getFecha_registro() {
		return fecha_registro;
	}

	public void setFecha_registro(String fecha_registro) {
		this.fecha_registro = fecha_registro;
	}

	public String getFecha_cancelacion() {
		return fecha_cancelacion;
	}

	public void setFecha_cancelacion(String fecha_cancelacion) {
		this.fecha_cancelacion = fecha_cancelacion;
	}

<<<<<<< HEAD

=======
	public String getFecha_inicio_estadia() {
		return fecha_inicio;
	}

	public void setFecha_inicio_estadia(String fecha_inicio_estadia) {
		this.fecha_inicio = fecha_inicio_estadia;
	}
>>>>>>> refs/remotes/origin/master

	/**
	 * EN DIAS
	 * 
	 * @return
	 */
	public Integer getDuracion() {
		return duracion;
	}

	/**
	 * EN DIAS
	 * 
	 * @param duracion
	 */
	public void setDuracion(Integer duracion) {
		this.duracion = duracion;
	}

	public Double getCosto_total() {
		return costo_total;
	}

	public void setCosto_total(Double costo_total) {
		this.costo_total = costo_total;
	}

	public Integer getCantidad_personas() {
		return cantidad_personas;
	}

	public void setCantidad_personas(Integer cantidad_personas) {
		this.cantidad_personas = cantidad_personas;
	}

	

	public Long getIdPropuesta() {
		return idPropuesta;
	}

	public void setIdPropuesta(Long idPropuesta) {
		this.idPropuesta = idPropuesta;
	}

	public Long getIdCliente() {
		return idCliente;
	}
<<<<<<< HEAD

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Double getMulta() {
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public Long getIdColectivo() {
		return idColectivo;
=======
	
	public Date getFechaFinal() throws Exception{
		
        DateFormat formato= new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
		Date fechaInicio;
		
		fechaInicio = formato.parse(fecha_inicio);
        
		Calendar cal= Calendar.getInstance();
		
		cal.setTime(fechaInicio);
		cal.add(Calendar.DAY_OF_YEAR, duracion);
		Date fechaFin= cal.getTime();
		
		return fechaFin;
	}




	public Double getMulta() {
		return multa;
	}




	public void setMulta(Double multa) {
		this.multa = multa;
	}




	public Long getIdColectivo() {
		return IdColectivo;
	}




	public void setIdColectivo(Long idColectivo) {
		IdColectivo = idColectivo;
>>>>>>> refs/remotes/origin/master
	}

	public void setIdColectivo(Long idColectivo) {
		this.idColectivo = idColectivo;
	}

}
