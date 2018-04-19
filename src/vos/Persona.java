

package vos;

import java.util.List;

import org.codehaus.jackson.annotate.*;

import tm.BusinessLogicException;

/**
 * Clase que representa una Persona
 * @author sebastian
 *
 */
public class Persona {

	
	
	
	//----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Id de la persona
	 */
	@JsonProperty(value="id")
	private Long id; 

	/**
	 * Nombre de la persona
	 */
	@JsonProperty(value="nombre")
	private String nombre;

	/**
	 * Apellido de la persona
	 */
	@JsonProperty(value="apellido")
	private String apellido;
	
	/**
	 * Tipo de persona. Puede ser : estudiante, registrado, empleado, profesor, padre, invitado, empresa, egresado
	 */
	@JsonProperty(value="tipo")
	private String tipo;
	
	/**
	 * Determina el papel de la persona {cliente, operador}
	 */
	@JsonProperty(value="papel")
	private String papel;
	
	/**
	 * Lista de propuestas de un operador
	 */
	@JsonProperty(value="propuestas")
	private List<Propuesta> propuestas;
	
	/**
	 * Es la multa que puede tener sólo un cliente.
	 */
	@JsonProperty(value="multa")
	private Integer multa;
	
	
	//----------------------------------------------------------------------------------------------------------------------------------
	// METODO CONSTRUCTOR
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo constructor de la clase Persona
	 * <b>post: </b> Crea la persona con los valores que entran por parametro
	 * @param id - Id de la persona.
	 * @param nombre - Nombre de la persona.
	 * @param apellido - Apellido de la persona.
	 * @param tipo - tipo de la persona.
	 */
	public Persona(  
			@JsonProperty(value="id")Long id, 
			@JsonProperty(value="nombre")String nombre , 
			@JsonProperty(  value="apellido")String apellido  ,   
			@JsonProperty(value="tipo")String tipo,
			@JsonProperty(value="papel") String papel,
			@JsonProperty(value="multa") Integer multa ) { 
		
		
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.tipo = tipo;
		this.papel = papel;
		this.multa = multa;
		
	}

	
	
	
	
	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE LA CLASE
	//----------------------------------------------------------------------------------------------------------------------------------
		
		
	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setPresupuesto(String apellido) {
		this.apellido = apellido;
	}

	public String getTipo() {
		return tipo;
	}




	public String getPapel() {
		return papel;
	}





	public void setPapel(String papel) {
		this.papel = papel;
	}





	public Integer getMulta() {
		return multa;
	}





	public void setMulta(Integer multa) {
		this.multa = multa;
	}





	/**
	 * Para agregar una propuesta, se debe ser {operador} y estar asociado con
	 * la universidad {estudiante, empleado, profesor, padre, empresa}
	 * @param propuesta
	 */
	public void addPropuesta ( Propuesta propuesta ) throws BusinessLogicException {
		
		if (this.papel.equalsIgnoreCase("CLIENTE")) 
			throw new BusinessLogicException("Un cliente no puede realizar propuestas de alohamiento. Debe estar registrado como operador y estar asociado con la universidad.");
		if (this.tipo.equalsIgnoreCase("INVITADO") || this.tipo.equalsIgnoreCase("INVITADO")) 
			throw new BusinessLogicException("El usuario no cuenta con los requisitos para realizar una propuesta. Debe estar registrado como operador y estar asociado con la universidad.");
			
		this.propuestas.add(propuesta);
		
	}
	
	
}
