/**-------------------------------------------------------------------
 * ISIS2304 - Sistemas Transaccionales
 * Departamento de Ingenieria de Sistemas
 * Universidad de los Andes
 * Bogota, Colombia
 * 
 * Actividad: Tutorial Parranderos: Arquitectura
 * Autores:
 * 			Santiago Cortes Fernandez	-	s.cortes@uniandes.edu.co
 * 			Juan David Vega Guzman		-	jd.vega11@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package dao;


import java.sql.Connection; 

import dao.DAOReserva;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import jdk.jshell.spi.ExecutionControl.ExecutionControlException;
import tm.BusinessLogicException;
import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicacion
 * 
 * 
 * 
 * 
 * 
 * Data Access Object (DAO)
 * Por medio de la conexioÌ�n que se crea en el Transaction Manager, este componente ejecuta las distintas 
 * sentencias SQL, recibe la informacioÌ�n correspondiente y se encarga de transformar tales resultados 
 * (ResultSets) en objetos que se manipulan posteriormente para atender las peticiones seguÌ�n sea el caso.
 * 
 * 
 * 
 * 
 * 
 */
public class DAOPersona {


	//----------------------------------------------------------------------------------------------------------------------------------
	// CONSTANTES
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Constante para indicar el usuario Oracle del estudiante
	 */
	public final static String USUARIOF = "ISIS2304A901810" ;
	


	//----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Arraylits de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;
		

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE INICIALIZACION
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo constructor de la clase DAOPersona <br/>
	 */
	public DAOPersona() {
		recursos = new ArrayList<Object>();
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE COMUNICACION CON LA BASE DE DATOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo que obtiene la informacion de todos los operadores en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los operadores que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Persona> getOperadores() throws SQLException, Exception {

		ArrayList<Persona> operadores = new ArrayList<Persona>();

		String sql = String.format("SELECT * FROM %1$s.PERSONA WHERE PAPEL = 'Operador'", USUARIOF);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			operadores.add(convertResultSetTo_Persona(rs));
		}
		return operadores;
	}

	/**
	 * Metodo que obtiene la informacion de todos los clientes en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los clientes que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Persona> getClientes() throws SQLException, Exception {

		ArrayList<Persona> clientes = new ArrayList<Persona>();

		String sql = String.format("SELECT * FROM %1$s.PERSONA WHERE PAPEL = 'Cliente'", USUARIOF);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			clientes.add(convertResultSetTo_Persona(rs));
		}
		return clientes;
	}


	/**
	 * Metodo que obtiene la informacion de todos las propuestas en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los clientes que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Propuesta> getPropuestas() throws SQLException, Exception {

		ArrayList<Propuesta> props = new ArrayList<Propuesta>();

		String sql = String.format("SELECT * FROM %1$s.PROPUESTA", USUARIOF);
		System.out.println(sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			props.add(convertResultSetTo_Propuesta(rs));
		}
		return props;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Propuesta getPropuestaById(Long id) throws SQLException, Exception {
		
		Propuesta propuesta = null;

		String sql = String.format("SELECT * FROM %1$s.PROPUESTA WHERE ID = %2$d", USUARIOF, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			propuesta = convertResultSetTo_Propuesta(rs);
		}

		return propuesta;
	}



	/**
	 * Metodo que obtiene la informacion de todos LAS PERSONAS en la Base de Datos que son de TIPO = {estudiante, registrado, empleado, profesor, padre, invitado, empresa}
	 * dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @param tipo de la persona que se quiere buscar = {estudiante, registrado, empleado, profesor, padre, invitado, empresa}
	 * @return lista con la informacion de todos las personas que se encuentran en la Base de Datos que cumplen con los criterios de la sentencia SQL
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */

	public ArrayList<Persona> getPersonas_Por_Tipo ( String tipo ) throws SQLException, Exception{

		ArrayList<Persona> personas = new ArrayList<Persona>();

		String sql = String.format("SELECT * FROM %1$s.PERSONA WHERE TIPO = '%2$s'", USUARIOF, tipo);
		PreparedStatement prepStmt = conn.prepareStatement(sql);

		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			personas.add(convertResultSetTo_Persona(rs));
		}
		return personas;
	}

	/**
	 * Metodo que obtiene la informacion del bebedor en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador de la persona
	 * @return la informacion de la persona que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe la persona con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Persona findPersonaById ( Long id ) throws SQLException, Exception 
	{
		Persona pep = null;

		String sql = String.format("SELECT * FROM %1$s.PERSONA WHERE ID = %2$d", USUARIOF, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			pep = convertResultSetTo_Persona(rs);
		}

		return pep;
	}

	/**
	 * REQUERIMIENTO 1
	 * 
	 * Metodo que agregar la informacion de un nuevo persona en la Base de Datos a partir del parametro ingresado<br/>
	 * Se define el rol de la persona {cliente, operador}
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param bebedor Bebedor que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addPersona (Persona persona) throws SQLException, Exception {

		String sql = 
				String.format(
						"INSERT INTO %1$s.PERSONA (ID, NOMBRE, APELLIDO,TIPO,PAPEL,VALOR_MULTA) "
								+ "VALUES (%2$s, '%3$s', '%4$s', '%5$s', '%6$s', '%7$s')", 
								USUARIOF, 
								persona.getId(), 
								persona.getNombre(),
								persona.getApellido(),
								persona.getTipo(),
								persona.getPapel(),
								persona.getMulta()
						);
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	/**
	 * Agrega la informacion de una propuestas a la base de datos
	 *  <b>Precondicion: </b> la conexion ha sido inicializada y la propuesta no puede existir sin una persona operador que la maneje <br/>  
	 * @param persona
	 * @param propuesta
	 * @throws SQLException
	 * @throws Exception
	 */
	public void addPropuesta ( Persona persona, Propuesta propuesta ) throws SQLException, Exception, BusinessLogicException {

		if ( this.findPersonaById(persona.getId()) == null ) {
			System.out.println(String.format("Actualmente la persona con id = {%1$s} {%2$s %3$s} no esta registrada. Debe estar registrado como operador y estar relacionado con la universidad para poder registrar una propuesta.", 
					persona.getId(), persona.getNombre(), persona.getApellido()));
			return;
		}

		// ESTA PARTE ASEGURA QUE LA PERSONA QUE ESTA HCIENDO LA PROPUESTA, SEA UN OPERADOR RELACIONADO A LA UNIVERSIDAD
		try {
			persona.addPropuesta(propuesta);
		} catch ( BusinessLogicException e ) {
			throw new BusinessLogicException(e.getMessage());
		} 

		String sql =
				String.format("INSERT INTO %1$s.PROPUESTAS(ID, ID_PERSONA, ID_HOSTEL, ID_HOTEL, ID_VIVIENDA_EXPRESS, ID_APARTAMENTO"
						+ ", ID_VIVIENDA_UNIVERSITARIA, ID_HABITACION, SE_VA_RETIRAR, HABILITADA, FECHA_INICIO_DESHABILITADA, FECHA_FIN_DESHABILITADA)"
						+ " VALUES ( %2$d, %3$d, %4$d, %5$d, %6$d, %7$d, %8$d, %9$d, %10$d, %11$d, %12$s, %13$s  )",
						propuesta.getId(),
						persona.getId(),
						propuesta.getHostal().getId(),
						propuesta.getHotel().getId(),
						propuesta.getVivienda_express().getId(),
						propuesta.getApartamento().getId(),
						propuesta.getVivienda_universitarias().getId(),
						propuesta.getHabitacion().getId(),
						(propuesta.getSeVaRetirar()==false)? 0 : 1,
						(propuesta.getHabilitada()==false)? 0 : 1,
						propuesta.getFechaDeshabilitacionInicial(),
						propuesta.getFechaDeshabilitacionFinal());

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	
	/**
	 * 
	 * @param propuesta
	 * @throws SQLException
	 * @throws Exception
	 * @throws BusinessLogicException
	 */
	public void retirarPropuesta(Propuesta propuesta)throws SQLException, Exception, BusinessLogicException{
		
		//Formateando la fecha:
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
		
		
		//necesitio las reservas que tengan esa propuesta
		ArrayList<Reserva> reservasConPropuesta = new ArrayList<>();
		
		String reservitas= String.format("SELECT * FROM RESERVAS WHERE ID_PROPUESTA = %2$d", USUARIOF, propuesta.getId());
		PreparedStatement prepStmt1= conn.prepareStatement(reservitas);
		recursos.add(prepStmt1);	
		ResultSet rs = prepStmt1.executeQuery();
		DAOReserva dao= new DAOReserva();
		
		while(rs.next())
			reservasConPropuesta.add(dao.convertResultSetToReserva(rs));
		//obtengo las reservas con la propuesta dada
		
		Date lastDate= new Date("1500-01-01 00:00:00");
		for(Reserva res: reservasConPropuesta) {
			Date temp= res.getFechaFinal();
			if(temp.after(lastDate)) {
				lastDate= temp;
			}
		} //obtengo la fecha de la ultima reserva que se acaba
		
		if(fechaActual.after(lastDate)) {
			propuesta.setSeVaRetirar(true);
			StringBuilder sql = new StringBuilder();
			sql.append(String.format("UPDATE PROPUESTAS SET ", USUARIOF));
			sql.append(String.format("SE_VA_RETIRAR = '%1$s' ", propuesta.getSeVaRetirar()));
			PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
			recursos.add(prepStmt);
			prepStmt.executeQuery();
			
		}
		else {
			
			throw new BusinessLogicException("No se pueden retirar propuestas hasta que se terminen las reservas");
		}
		
		
	}
	
	/**
	 * 
	 * @param propuesta
	 * @param numDias
	 * @throws Exception
	 * @throws BusinessLogicException
	 */
	public void deshabilitarPropuesta(Propuesta propuesta, int numDias) throws Exception, BusinessLogicException{
		
		
		if(propuesta.getSeVaRetirar()) throw new BusinessLogicException("La propuesta se va retirar, por ende no se puede deshabilitar");
		if(!propuesta.getHabilitada()) throw new BusinessLogicException("La propuesta ya está deshabilitada");
		
		//Formateando la fecha:
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
		
		
		//necesitio las reservas que tengan esa propuesta
		ArrayList<Reserva> reservasConPropuesta = new ArrayList<>();
		
		String reservitas= String.format("SELECT * FROM RESERVAS WHERE ID_PROPUESTA = %2$d", USUARIOF, propuesta.getId());
		PreparedStatement prepStmt1= conn.prepareStatement(reservitas);
		recursos.add(prepStmt1);	
		ResultSet rs = prepStmt1.executeQuery();
		DAOReserva dao= new DAOReserva();
		
		while(rs.next())
			reservasConPropuesta.add(dao.convertResultSetToReserva(rs));
		//obtengo las reservas con la propuesta dada
		
		Date lastDate= new Date("1500-01-01 00:00:00");
		for(Reserva res: reservasConPropuesta) {
			Date temp= res.getFechaFinal();
			if(temp.after(lastDate)) {
				lastDate= temp;
			}
		}//fecha de ultima reserva que se acaba
		
		
		
		
		//tomo las propuestas que esten en esta fecha
		ArrayList<Propuesta> propuestasDisponibles=getPropuestasByTipoInmueble(propuesta.getTipo_inmueble());
		
		propuesta.setHabilitada(false);//se deshabilita la propuesta

		
		//busco el tipo de inmueble que buscan que este disponible
		boolean fin= false;
		int n= propuestasDisponibles.size();
		Propuesta propuestaCambio= null;
		for(int i=0; i<n && !fin; i++) {
			if(propuestasDisponibles.get(i).getSeVaRetirar()== false && propuestasDisponibles.get(i).getHabilitada()== true) {
				propuestaCambio= propuestasDisponibles.get(i);
			}
		}
		
		int nr= reservasConPropuesta.size();
		for(int i=0; i<nr; i++) 
			reservasConPropuesta.get(i).setPropuesta(propuestaCambio);
		
		dao.registrarReservaColectiva(reservasConPropuesta);//le asigno la propuesta determinada a la reserva que estaba
		
		
		propuesta.setFechaDeshabilitacionInicial(actualDate);		
		        
		Calendar cal= Calendar.getInstance();
		cal.setTime(fechaActual);
		cal.add(Calendar.DAY_OF_YEAR, numDias);
		Date fechaFin= cal.getTime();
		String fechaFinal= fechaFin.toString();
		
		propuesta.setFechaDeshabilitacionFinal(fechaFinal);
		
		updatePropuesta(propuesta);
		
		
	}
	
	
	public void rehabilitarPropuesta(Propuesta propuesta) throws BusinessLogicException, SQLException {
		
		if(propuesta.getHabilitada()) throw new BusinessLogicException("Tiene que estar deshabilitada la propuesta");
		
		try {
			propuesta.habilitar();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		updatePropuesta(propuesta);
		
	}
	
	/**
	 * 
	 * @param tipoInmueble
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Propuesta> getPropuestasByTipoInmueble(String tipoInmueble) throws SQLException{
		
		ArrayList<Propuesta> props = new ArrayList<Propuesta>();
		
		

		String sql = String.format("SELECT * FROM %1$s.PROPUESTAS WHERE TIPO_INMUEBLE = %2$s", USUARIOF, tipoInmueble);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			props.add(convertResultSetTo_Propuesta(rs));
		}
		return props;
		
	}
	
	public void updatePropuesta(Propuesta propuesta) throws SQLException {
		
		
		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE PROPUESTAS SET ", USUARIOF));
		sql.append(String.format("TIPO_INMUEBLE = '%1$s' AND ID_HOTEL = '%2$s' AND ID_HOSTEL = '%3$s'"
				+ "AND ID_VIVIENDA_EXPRESS = '%4$s' AND ID_APARTAMENTO = '%5$s' AND ID_VIVIENDA_UNIVERSITARIA = '%6$s'"
				+ "AND ID_HABITACION = '%7$s' AND SE_VA_RETIRAR = '%8$s' AND HABILITADA = '%9$s' AND FECHA_INICIO_DESHABILITADA = '%10$s'"
				+ "AND FECHA_FIN_DESHABILITADA = '%11$s'", 
				propuesta.getTipo_inmueble(),
				propuesta.getHotel().getId(),
				propuesta.getHostal().getId(),
				propuesta.getVivienda_express().getId(),
				propuesta.getApartamento().getId(),
				propuesta.getVivienda_universitarias().getId(),
				propuesta.getHabitacion().getId(),
				(propuesta.getSeVaRetirar()==true)? 1:0,
				(propuesta.getHabilitada()==true)? 1:0,
				propuesta.getFechaDeshabilitacionInicial(),
				propuesta.getFechaDeshabilitacionFinal()));
		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que actualiza la informacion de LA PERSONA en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param persona Persona que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updatePersona ( Persona persona ) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.PERSONA SET ", USUARIOF));
		sql.append(String.format("NOMBRE = '%1$s' AND APELLIDO = '%2$s' AND TIPO = '%3$s' "
				+ "AND PAPEL = '%4$s' AND VALOR_MULTA = '%5$s'",
				persona.getNombre(), 
				persona.getApellido(), 
				persona.getTipo(),
				persona.getPapel(),
				persona.getMulta()
				));

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina una Persona de la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param persona Persona que desea eliminar de la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deletePersona ( Persona persona ) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.PERSONAS WHERE ID = %2$d", USUARIOF, persona.getId());

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}



	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS AUXILIARES
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo encargado de inicializar la conexion del DAO a la Base de Datos a partir del parametro <br/>
	 * <b>Postcondicion: </b> el atributo conn es inicializado <br/>
	 * @param connection la conexion generada en el TransactionManager para la comunicacion con la Base de Datos
	 */
	public void setConn(Connection connection){
		this.conn = connection;
	}

	/**
	 * Metodo que cierra todos los recursos que se encuentran en el arreglo de recursos<br/>
	 * <b>Postcondicion: </b> Todos los recurso del arreglo de recursos han sido cerrados.
	 */
	public void cerrarRecursos() {
		for(Object ob : recursos){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	/**
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla PERSONAS) en una instancia de la clase PERSONA.
	 * @param resultSet ResultSet con la informacion de un bebedor que se obtuvo de la base de datos.
	 * @return Persona cuyos atributos corresponden a los valores asociados a un registro particular de la tabla PERSONAS.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Persona convertResultSetTo_Persona(ResultSet resultSet) throws SQLException {
		//Tenga en cuenta los nombres de las columnas de la Tabla en la Base de Datos 
		//(ID, NOMBRE, APELLIDO, TIPO, CEDULA, ROL, NIT, EMAIL)

		Long id = resultSet.getLong("ID");
		String nombre = resultSet.getString("NOMBRE");
		String apellido = resultSet.getString("APELLIDO");
		String tipo = resultSet.getString("TIPO");
		String papel = resultSet.getString("PAPEL");
		Integer multa = resultSet.getInt("VALOR_MULTA");

		Persona pep = new Operador(id, nombre, apellido, tipo, papel, multa);

		return pep;
	}

	public Propuesta convertResultSetTo_Propuesta(ResultSet resultSet) throws SQLException {

		long id = resultSet.getLong("ID");
		String tipo_inmueble = resultSet.getString("TIPO_INMUEBLE");

		Propuesta prop = new Propuesta(id, tipo_inmueble);

		if ( Propuesta.TIPO_INMUEBLE.APARTAMENTO.toString().equalsIgnoreCase(tipo_inmueble) ) {
			String sql = String.format("SELECT * FROM %1$s.APARTAMENTO WHERE ID = %2$d", USUARIOF, resultSet.getLong("ID_APARTAMENTO"));
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				prop.setApartamento(new Apartamento(rs.getLong("ID"), rs.getInt("AMOBLADO")  , rs.getDouble("COSTO_ADMINISTRACION")));
			}
		} 

		if ( Propuesta.TIPO_INMUEBLE.HABITACION.toString().equalsIgnoreCase(tipo_inmueble) ) {
			String sql = String.format("SELECT * FROM %1$s.HABITACION WHERE ID = %2$d", USUARIOF, resultSet.getLong("ID_HABITACION"));
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				prop.setHabitacion( new Habitacion(rs.getLong("ID"), rs.getInt("ESPECIAL"), rs.getString("TIPO_HABITACION")) );
			}
		}

		if ( Propuesta.TIPO_INMUEBLE.HOSTAL.toString().equalsIgnoreCase(tipo_inmueble) ) {
			String sql = String.format("SELECT * FROM %1$s.HOSTAL WHERE ID = %2$d", USUARIOF, resultSet.getLong("ID_HOSTAL"));
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				prop.setHostal( new Hostal(rs.getLong("ID"), rs.getString("CAMARA_COMERCIO"), rs.getString("SUPERINTENDENCIA"), rs.getString("TIPO_HABITACION"), rs.getString("UBICACION"), rs.getInt("APERTURA"), rs.getInt("CIERRE")) );
			}
		}

		if ( Propuesta.TIPO_INMUEBLE.HOTEL.toString().equalsIgnoreCase(tipo_inmueble) ) {
			String sql = String.format("SELECT * FROM %1$s.HOTEL WHERE ID = %2$d", USUARIOF, resultSet.getLong("ID_HOTEL"));
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				prop.setHotel( new Hotel(rs.getLong("ID"), rs.getString("CAMARA_COMERCIO"), rs.getString("SUPERINTENDENCIA"), rs.getString("TIPO_HABITACION"), rs.getString("UBICACION") ));
			}
		}

		if ( Propuesta.TIPO_INMUEBLE.VIVIENDA_EXPRESS.toString().equalsIgnoreCase(tipo_inmueble) ) {
			String sql = String.format("SELECT * FROM %1$s.VIVIENDA_EXPRESS WHERE ID = %2$d", USUARIOF, resultSet.getLong("ID_VIVIENDA_EXPRESS"));
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				prop.setVivienda_express( new ViviendaExpress(rs.getLong("ID"), rs.getString("MENAJE"), rs.getString("UBICACION")) );
			}
		}

		if ( Propuesta.TIPO_INMUEBLE.VIVIENDA_UNIVERSITARIA.toString().equalsIgnoreCase(tipo_inmueble) ) {
			String sql = String.format("SELECT * FROM %1$s.VIVIENDA_UNIVERSITARIA WHERE ID = %2$d", USUARIOF, resultSet.getLong("ID_VIVIENDA_UNIVERSITARIA"));
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				prop.setVivienda_universitarias( new ViviendaUniversitaria(rs.getLong("ID"), rs.getString("UBICACION"), rs.getString("MENAJE"), rs.getString("DESCRIPCION"), rs.getString("TIPO_HABITACION"), rs.getInt("MENSUAL") == 0 ? false : true) );
			}
		}

		int retiro= resultSet.getInt("SE_RETIRA");
		Boolean seVaRetirar= (retiro == 1)? true: false;
		prop.setSeVaRetirar(seVaRetirar);

		/**int habilitada= resultSet.getInt("HABLILITADA");
		Boolean estaHabilitada= (habilitada==1)? true: false;
		prop.setHabilitada(estaHabilitada);
		*/
		return prop;
	}





















	//----------------------------------------------------------------------------------------------------------------------------------
	// REQUERIMIENTOS FUNCIONALES DE CONSULTA
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * RFC 1
	 * 
	 * EL DINERO RECIBIDO POR CADA PROVEEDOR DE ALOJAMIENTO DURANTE EL AÑO ACTUAL Y EL AÑO CORRIDO
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayList<String> _dinero_recibido () throws SQLException, Exception {
		
		String sql = "SELECT PP.*, ASW.ID_PROPUESTA, asw.\"TOTAL GANANCIAS\" FROM (\n" + 
				"\n" + 
				"SELECT RE.ID_PROPUESTA AS \"ID_PROPUESTA\", SUM(RE.COSTO_TOTAL) AS \"TOTAL GANANCIAS\"\n" + 
				"FROM RESERVAS RE\n" + 
				"WHERE RE.ID_PROPUESTA IN (\n" + 
				"    SELECT PT.ID\n" + 
				"    FROM PROPUESTAS PT \n" + 
				"    WHERE PT.ID_PERSONA IN (\n" + 
				"        SELECT PEP.ID  \n" + 
				"        FROM PERSONAS PEP \n" + 
				"        WHERE ROL = 'operador'\n" + 
				"    )\n" + 
				")\n" + 
				"GROUP BY RE.ID_PROPUESTA\n" + 
				") ASW\n" + 
				"INNER JOIN PROPUESTAS PU\n" + 
				"ON PU.ID = ASW.ID_PROPUESTA\n" + 
				"\n" + 
				"INNER JOIN PERSONAS PP\n" + 
				"ON PP.ID = PU.ID_PERSONA\n" + 
				"\n" + 
				"ORDER BY asw.\"TOTAL GANANCIAS\" DESC\n";
		
		
		ArrayList<String> pagos = new ArrayList<>();
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			pagos.add(rs.getString("ID") + "\t" + rs.getString("NOMBRE") + "\t" + rs.getString("ASW.ID_PROPUESTA") + "\t" + rs.getString("TOTAL GANANCIAS"));
		}
		return pagos;
	}

	/**
	 * RFC2
	 * 
	 * Retorna las 20 orfertas mÃ¡s populares 
	 * @return
	 */
	public ArrayList<String> _20_ofertas_mas_populares ()  throws SQLException, Exception {
				
		String sql =String.format( "SELECT  ID_PROPUESTA, COUNT(ID_PROPUESTA) AS \"Cantidad Reservas\" \n" + 
				"		FROM %1$s.RESERVAS \n" + 
				"		GROUP BY ID_PROPUESTA\n" + 
				"		ORDER BY \"Cantidad Reservas\" DESC", USUARIOF);
		
		//String sql = "SELECT * FROM " + USUARIO + ".RESERVAS  ";
		
		
		ArrayList<String> populares = new ArrayList<String>();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		

		while (rs.next()) {
			populares.add(rs.getLong("ID_PROPUESTA") + "");
		}
		return populares;
		
	}

	
	

	





















}
