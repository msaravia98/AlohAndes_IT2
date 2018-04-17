package vos;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import tm.BusinessLogicException;


/**
 * Representa la propuesta de un operador de alohandes
 * @author sebastian
 *
 */
public class Propuesta {


	//----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Representa el tipo de inmueble de la propuesta
	 * @author sebastian
	 *
	 */
	public enum TIPO_INMUEBLE {
		APARTAMENTO,
		HABITACION,
		HOSTEL,
		HOTEL,
		VIVIENDA_EXPRESS,
		VIVIENDA_UNIVERSITARIA;
	}
	
	/**
	 * Representa el tipo de inmueble de la propuesta
	 */
	@JsonProperty(value="tipo_inmueble")
	private String tipo_inmueble;
	
	/**
	 * Id de la PROPUESTA
	 */
	@JsonProperty(value="id")
	private Long id; 
	
	/**
	 * Representa el tipo de inmuble apartamento de la propuesta
	 */
	@JsonProperty(value="apartamento")
	private Apartamento apartamento;
	
	/**
	 * Representa el tipo de inmuble habitacion de la propuesta
	 */
	@JsonProperty(value="habitacion")
	private Habitacion habitacion;
	
	/**
	 * Representa el tipo de inmuble hostel de la propuesta
	 */
	@JsonProperty(value="hostal")
	private Hostal hostal;
	
	/**
	 * Representa el tipo de inmuble hotel de la propuesta
	 */
	@JsonProperty(value="hotel")
	private Hotel hotel;
	
	/**
	 * Representa el tipo de inmuble vivienda express de la propuesta
	 */
	@JsonProperty(value="vivienda_express")
	private ViviendaExpress vivienda_express;
	
	/**
	 * Representa el tipo de inmuble vivienda universitaria de la propuesta
	 */
	@JsonProperty(value="vivienda_universitaria")
	private ViviendaUniversitaria vivienda_universitarias;
	
	/**
	 * atributo que define si la propuesta se debe retirar
	 */
	@JsonProperty(value="seRetira")
	private Boolean seRetira;
	
	@JsonProperty(value="capacidad")
	private Integer capacidad;
	
	@JsonProperty(value="costo")
	private Integer costo;
	
	/**
	 * 
	 */
	@JsonProperty(value="habilitada")
	private Boolean habilitada;
	
	@JsonProperty(value="fechaDeshabilitacionInicial")
	private String fechaDeshabilitacionInicial;
	
	@JsonProperty(value="fechaDeshabilitacionFinal")
	private String fechaDeshabilitacionFinal;


	//----------------------------------------------------------------------------------------------------------------------------------
	// METODO CONSTRUCTOR
	//----------------------------------------------------------------------------------------------------------------------------------

	public Propuesta ( @JsonProperty(value="id") Long id,
			@JsonProperty(value="id") String tipo_inmueble ) {
		this.id = id;
		this.tipo_inmueble = tipo_inmueble;
		this.apartamento = null;
		this.habitacion = null;
		this.hostal = null;
		this.hotel = null;
		this.vivienda_express = null;
		this.vivienda_universitarias = null;
		this.seRetira= false;
		
		
		this.habilitada= true;
		this.fechaDeshabilitacionInicial= null;
		this.fechaDeshabilitacionFinal= null;
	}
	

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE LA CLASE
	//----------------------------------------------------------------------------------------------------------------------------------

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Apartamento getApartamento() {
		return apartamento;
	}

	public void setApartamento(Apartamento apartamento) {
		this.apartamento = apartamento;
	}

	public Habitacion getHabitacion() {
		return habitacion;
	}

	public void setHabitacion(Habitacion habitacion) {
		this.habitacion = habitacion;
	}

	public Hostal getHostal() {
		return hostal;
	}

	public void setHostal(Hostal hostel) {
		this.hostal = hostel;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public ViviendaExpress getVivienda_express() {
		return vivienda_express;
	}

	public void setVivienda_express(ViviendaExpress vivienda_express) {
		this.vivienda_express = vivienda_express;
	}

	public ViviendaUniversitaria getVivienda_universitarias() {
		return vivienda_universitarias;
	}

	public void setVivienda_universitarias(ViviendaUniversitaria vivienda_universitarias) {
		this.vivienda_universitarias = vivienda_universitarias;
	}

	public String getTipo_inmueble() {
		return tipo_inmueble;
	}

	public void setTipo_inmueble(String tipo_inmueble) {
		this.tipo_inmueble = tipo_inmueble;
	}


	/**
	 * @return the seVaRetirar
	 */
	public Boolean getSeVaRetirar() {
		return seRetira;
	}


	/**
	 * @param seVaRetirar the seVaRetirar to set
	 */
	public void setSeVaRetirar(Boolean seVaRetirar) {
		this.seRetira = seVaRetirar;
	}


	/**
	 * @return the habilitada
	 */
	public Boolean getHabilitada() {
		return habilitada;
	}


	/**
	 * @param habilitada the habilitada to set
	 */
	public void setHabilitada(Boolean habilitada) {
		this.habilitada = habilitada;
	}
	
	public void habilitar() throws ParseException, BusinessLogicException {
		
		DateFormat formatoConHora= new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
        
        //Fecha actual desglosada:
        Calendar fecha = Calendar.getInstance();
        int anio = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH) + 1;
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        int segundo = fecha.get(Calendar.SECOND);
        String actualDate= ""+anio+"-"+mes+"-"+dia+" "+hora+":"+minuto+":"+segundo;
        
		Date fechaActual= formatoConHora.parse(actualDate);
		Date fechaFin= formatoConHora.parse(fechaDeshabilitacionFinal);
		if(fechaActual.after(fechaFin))
			throw new BusinessLogicException();
		
		if(fechaFin.after(fechaActual))
			setHabilitada(true);
		
		
	}


	/**
	 * @return the fechaDeshabilitacionInicial
	 */
	public String getFechaDeshabilitacionInicial() {
		return fechaDeshabilitacionInicial;
	}


	/**
	 * @param fechaDeshabilitacionInicial the fechaDeshabilitacionInicial to set
	 */
	public void setFechaDeshabilitacionInicial(String fechaDeshabilitacionInicial) {
		this.fechaDeshabilitacionInicial = fechaDeshabilitacionInicial;
	}


	/**
	 * @return the fechaDeshabilitacionFinal
	 */
	public String getFechaDeshabilitacionFinal() {
		return fechaDeshabilitacionFinal;
	}


	/**
	 * @param fechaDeshabilitacionFinal the fechaDeshabilitacionFinal to set
	 */
	public void setFechaDeshabilitacionFinal(String fechaDeshabilitacionFinal) {
		this.fechaDeshabilitacionFinal = fechaDeshabilitacionFinal;
	}


	
	

	
}
