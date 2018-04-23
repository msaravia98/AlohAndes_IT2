package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tm.BusinessLogicException;
import vos.Apartamento;
import vos.Cliente;
import vos.Habitacion;
import vos.Hostal;
import vos.Hotel;
import vos.Persona;
import vos.Propuesta;
import vos.Reserva;
import vos.ReservaColectiva;
import vos.ViviendaExpress;
import vos.ViviendaUniversitaria;

public class DAOReserva {

	//constantes

	/**
	 * constante que contiene el usuario de oracle
	 */
	public final static String USUARIO = "ISIS2304A901810";

	//atributos

	/**
	 * Arraylits de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private  ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private  Connection conn;

	/**
	 * 
	 */
	private DAOPersona persona;

	//constructor

	/**
	 * 
	 */
	public DAOReserva(){

		recursos= new ArrayList<Object>();
		persona= new DAOPersona();
	}




	//metodos

	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public  Reserva getReservaById(Long id) throws SQLException, Exception {
		Reserva reserva = null;

		String sql = String.format("SELECT * FROM %1$s.RESERVA WHERE ID = %2$d", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			reserva = convertResultSetToReserva(rs);
		}

		return reserva;
	}
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayList<Reserva> getReservas() throws SQLException, Exception {
		
		ArrayList<Reserva> res = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.RESERVA", USUARIO); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		while(rs.next())
		{
			res.add(convertResultSetToReserva(rs));
		}
		return res;
	}
	



	/**
	 * Metodo que agregar la informacion de una nueva reserva en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param reserva Reserva que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws BusinessLogicException Si se genera un error dentro del metodo.
	 */
	public void registrarReserva(Reserva reserva) throws SQLException, BusinessLogicException, Exception {

		ArrayList<Reserva> reservasEnFecha = new ArrayList<>();
		if(reserva.getIdColectivo()==null)
		{
			Integer num = 0;
			reserva.setIdColectivo(num.longValue());
		}
		//String sen = String.format("SELECT * FROM %1$d.RESERVA WHERE ID_PERSONA = %2$d AND FECHA_INICIO = %3$d",USUARIO,reserva.getIdCliente(),reserva.getFecha_inicio());


		
		List<Propuesta> propuestR = new ArrayList<>();                                                                          
		String propuestasql = String.format("SELECT * FROM PROPUESTA WHERE ID = %2$d", USUARIO, reserva.getIdPropuesta());
		PreparedStatement prepStmtP= conn.prepareStatement(propuestasql);
		recursos.add(prepStmtP);	
		ResultSet rsp = prepStmtP.executeQuery(); 

		while(rsp.next())
			propuestR.add(convertResultSetTo_Propuesta(rsp));
		
		for(Propuesta este:propuestR) {
		if(este.getSeVaRetirar())
			throw new BusinessLogicException("No se puede realizar la reserva porque la propuesta no esta disponible para más fechas");
		//valido que la propuesta sea vigente

		if(!este.getHabilitada()) throw new BusinessLogicException("La propuesta está deshabilitada");
		}
		//sentencia para insertar la resrva en la base de datos
		String sql = String.format("INSERT INTO %1$s.RESERVA VALUES ("
				+ "%2$s,"
				+ " %3$s,"
				+ " %4$s,"
				+ " '%5$s',"
				+ " '%6$s',"
				+ " '%7$s',"
				+ " %8$s,"
				+ " %9$s,"
				+ "%10$s,"
				+ "%11$s,"
				+ "%12$s)", 
				USUARIO, //1
				reserva.getId(), //2 
				reserva.getIdPropuesta(), //3
				reserva.getIdCliente(), //4
				reserva.getFecha_registro(),  //5
				reserva.getFecha_inicio(),  //6
				reserva.getFecha_cancelacion(),//7
				reserva.getCosto_total(),//8
				reserva.getDuracion(),//9
				reserva.getCantidad_personas(),//10
				reserva.getMulta(),//11
				reserva.getIdColectivo());//12
		
		System.out.println(sql+"esta es la sentencia");

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery(); //se inserta la reserva

	}
	/**
	 * Metodo que actualiza la informacion de la reserva en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param resrva Reserva que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws BusinessLogicException Si se genera un error dentro del metodo.
	 */
	public void cancelarReserva(Reserva reserva) throws SQLException, BusinessLogicException, Exception {

		//Formateando la fecha:
		DateFormat formatoConHora= new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

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

		Reserva reserva1 = getReservaById(reserva.getId());

		Date fechaDeEstadia= formatoConHora.parse(reserva1.getFecha_inicio()); // obtengo la fecha e estadia



		Calendar cal= Calendar.getInstance();

		cal.setTime(fechaDeEstadia);
		cal.add(Calendar.DAY_OF_YEAR, -8);
		Date fechaMaxima= cal.getTime();

		if(fechaActual.before(fechaMaxima)){
			// primera regla de negocio, si esta antes de la fecha máxima de cancelacion se cobra el 10%
			double valorMulta= reserva.getCosto_total();
			reserva.setMulta(valorMulta*0.1);
			reserva.setCosto_total(valorMulta);
			StringBuilder sql = new StringBuilder();
			sql.append(String.format("UPDATE RESERVA SET ", USUARIO));
			sql.append(String.format("FECHA_CANCELACION ",fechaActual.toString()," AND MULTA",reserva.getMulta()));
		}else if(fechaActual.after(fechaMaxima) && fechaActual.before(fechaDeEstadia)){
			//segunda regla de negocio, si se reserva despues de la fecha maxima y antes de la fecha de inicio de la estadia se cobra el 30%
			double valorMulta= reserva.getCosto_total();
			reserva.setMulta(valorMulta*0.3);
			reserva.setCosto_total(valorMulta);
			StringBuilder sql = new StringBuilder();
			sql.append(String.format("UPDATE RESERVA SET ", USUARIO));
			sql.append(String.format("FECHA_CANCELACION = '%1$s' AND MULTA = '%2$s'", fechaActual.toString(), reserva.getMulta()));
		}else{
			//tercera regla de negocio (una parte), si se reserva despues de la fecha de inicio de estadia, se cobra el 50%
			double valorMulta= reserva.getCosto_total();
			reserva.setMulta(valorMulta*0.5);
			reserva.setCosto_total(valorMulta);
			StringBuilder sql = new StringBuilder();
			sql.append(String.format("UPDATE RESERVA SET ", USUARIO));
			sql.append(String.format("FECHA_CANCELACION = '%1$s' MULTA = '%2$s'", fechaActual.toString(),reserva.getMulta()));
		}
	}
	
	//-----------------------------------------------------------------------------------------
	// RESERVA COLECTIVA-----------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayList<Reserva> getReservasColectivas() throws SQLException, Exception {
		
		ArrayList<Reserva> res = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.RESERVA WHERE ID_COLECTIVO > 0", USUARIO); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		while(rs.next())
		{
			res.add(convertResultSetToReserva(rs));
		}
		return res;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Reserva> getReservaByIdColectivo(Long id) throws SQLException, Exception {
		List<Reserva> reserva = new ArrayList<>();

		String sql = String.format("SELECT * FROM %1$s.RESERVA WHERE ID_COLECTIVO = %2$d", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			reserva.add(convertResultSetToReserva(rs));
		}

		return reserva;
	}
	public void registrarReservaColectiva(ReservaColectiva reserva) throws Exception
	{
		List<Propuesta> props = propuestasQueSirven(reserva);
		Reserva nueva = null;
		//Filtros que necesito
		Integer cant = reserva.getCantidadInmuebles();
		Double cost = reserva.getCosto();
		Integer dur = reserva.getDuracion();
		Double mul = reserva.getMulta();
		String in = reserva.getFechaInicio();
		String can = reserva.getFechaCancelacion();
		String reg = reserva.getFechaRegistro();
		Long col = reserva.getIdColectivo();
		Long clien = reserva.getCliente();

		for(Integer i =0;i<cant;i++)
		{
			Propuesta p = props.get(i);
			Long id = i.longValue();
			nueva = new Reserva(id, 
					reg, 
					can, 
					in, 
					dur, 
					cost, 
					cant, 
					mul, 
					col, 
					p.getId(), 
					clien);
			registrarReserva(nueva);
		}
	}


	/**
	 * Metodo que actualiza la informacion de la reserva en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param resrva Reserva que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws BusinessLogicException Si se genera un error dentro del metodo.
	 */
	public void cancelarReservaColectiva(List<Reserva> reserva) throws SQLException, BusinessLogicException, Exception {
		Double multa = 0.0;
		for(Reserva re:reserva)
		{
			cancelarReserva(re);
			multa+=re.getMulta();
			re.setMulta(multa);
		}
		
	}
	/**
	 * Retorna las propuestas que sirven, segun tipo de propuesta y los servicios requeridos
	 * @param reserva Reserva Colectiva con los filtros que se desean aplicar
	 * @return Retorna una lista con las Propuestas que cumplen los filtros dados
	 * @throws BusinessLogicException Si se violan reglas de negocio
	 * @throws SQLException En la ejecución de las sentencias.
	 */
	private List<Propuesta> propuestasQueSirven(ReservaColectiva reserva) throws BusinessLogicException, SQLException {

		Integer cant = reserva.getCantidadInmuebles();
		String tipoIn = reserva.getTipoInmueble();
		List<Propuesta> retornar = new ArrayList<>();
		String servs = reserva.getServiciosDeseados();
		String[] servis = servs.split("-");
		Integer dur = reserva.getDuracion();
		String sentencia ="";


		if(servis.length>0)
		{
			String cadServ ="(";
			for(String servi:servis)
			{
				cadServ+="'"+servi+"',";
			}
			cadServ = cadServ.substring(0,cadServ.length()-2);
			cadServ+="')";

			sentencia = " WHERE TI.NOMBRE IN" + cadServ +"";
		}
		String sql    ="SELECT * "
				+ "FROM PROPUESTA PRO "
				+ "WHERE UPPER(TIPO_INMUEBLE) = UPPER('"+tipoIn+"') "
				+ "AND PRO.ID_"+tipoIn+" "
				+ "IN ( "
				+ "SELECT SERV.ID_"+tipoIn+" "
				+ "FROM SERVICIO_BASICO SERV "
				+ "INNER JOIN "
				+ "TIPO_SERVICIO TI "
				+ "ON TI.ID = SERV.ID_TIPO_SERVICIO"
				+ sentencia
				+")"
				+"";
		System.out.println(sql+ "esta es la sentencia");
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs =prepStmt.executeQuery();

		while(rs.next())
		{
			retornar.add(convertResultSetTo_Propuesta(rs));
		}
		return retornar;
	}




	public static Reserva convertResultSetToReserva(ResultSet resultSet) throws SQLException {


		Long id = resultSet.getLong("ID");
		Long idPropuesta= resultSet.getLong("ID_PROPUESTA");
		Long idCliente= resultSet.getLong("ID_PERSONA");
		Long IdColectivo = resultSet.getLong("ID_COLECTIVO");
		
		String fecha_registro = resultSet.getString("FECHA_REGISTRO");
		String fecha_inicio = resultSet.getString("FECHA_INICIO");
		String fecha_cancelacion = resultSet.getString("FECHA_CANCELACION");
		Double costo= resultSet.getDouble("COSTO");
		Integer duracion= resultSet.getInt("DURACION");
		Integer personas= resultSet.getInt("PERSONAS");
		Double valorMulta= resultSet.getDouble("MULTA");
		
		Reserva retornar = new Reserva(id,
				fecha_registro,
				fecha_cancelacion,
				fecha_inicio,
				duracion,
				costo,
				personas,
				valorMulta,
				IdColectivo,
				idPropuesta,
				idCliente);
		return retornar;

	}

	public  Propuesta convertResultSetTo_Propuesta(ResultSet resultSet) throws SQLException {

		long id = resultSet.getLong("ID");
		String tipo_inmueble = resultSet.getString("TIPO_INMUEBLE");

		Propuesta prop = new Propuesta(id, tipo_inmueble);

		if ( Propuesta.TIPO_INMUEBLE.APARTAMENTO.toString().equalsIgnoreCase(tipo_inmueble) ) {
			String sql = String.format("SELECT * FROM %1$s.APARTAMENTO WHERE ID = %2$d", USUARIO, resultSet.getLong("ID_APARTAMENTO"));
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				prop.setApartamento(new Apartamento(rs.getLong("ID"), rs.getInt("AMOBLADO")  , rs.getDouble("COSTO_ADMINISTRACION")));
			}
		} 

		if ( Propuesta.TIPO_INMUEBLE.HABITACION.toString().equalsIgnoreCase(tipo_inmueble) ) {
			String sql = String.format("SELECT * FROM %1$s.HABITACION WHERE ID = %2$d", USUARIO, resultSet.getLong("ID_HABITACION"));
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				prop.setHabitacion( new Habitacion(rs.getLong("ID"), rs.getInt("ESPECIAL"), rs.getString("TIPO_HABITACION")) );
			}
		}

		if ( Propuesta.TIPO_INMUEBLE.HOSTAL.toString().equalsIgnoreCase(tipo_inmueble) ) {
			String sql = String.format("SELECT * FROM %1$s.HOSTAL WHERE ID = %2$d", USUARIO, resultSet.getLong("ID_HOSTAL"));
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				prop.setHostal( new Hostal(rs.getLong("ID"), rs.getString("CAMARA_COMERCIO"), rs.getString("SUPERINTENDENCIA"), rs.getString("TIPO_HABITACION"), rs.getString("UBICACION"), rs.getInt("APERTURA"), rs.getInt("CIERRE")) );
			}
		}

		if ( Propuesta.TIPO_INMUEBLE.HOTEL.toString().equalsIgnoreCase(tipo_inmueble) ) {
			String sql = String.format("SELECT * FROM %1$s.HOTEL WHERE ID = %2$d", USUARIO, resultSet.getLong("ID_HOTEL"));
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				prop.setHotel( new Hotel(rs.getLong("ID"), rs.getString("CAMARA_COMERCIO"), rs.getString("SUPERINTENDENCIA"), rs.getString("TIPO_HABITACION"), rs.getString("UBICACION") ));
			}
		}

		if ( Propuesta.TIPO_INMUEBLE.VIVIENDA_EXPRESS.toString().equalsIgnoreCase(tipo_inmueble) ) {
			String sql = String.format("SELECT * FROM %1$s.VIVIENDA_EXPRESS WHERE ID = %2$d", USUARIO, resultSet.getLong("ID_VIVIENDA_EXPRESS"));
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				prop.setVivienda_express( new ViviendaExpress(rs.getLong("ID"), rs.getString("MENAJE"), rs.getString("UBICACION")) );
			}
		}

		if ( Propuesta.TIPO_INMUEBLE.VIVIENDA_UNIVERSITARIA.toString().equalsIgnoreCase(tipo_inmueble) ) {
			String sql = String.format("SELECT * FROM %1$s.VIVIENDA_UNIVERSITARIA WHERE ID = %2$d", USUARIO, resultSet.getLong("ID_VIVIENDA_UNIVERSITARIA"));
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
	/**
		
		int habilitada= resultSet.getInt("HABLILITADA");
		Boolean estaHabilitada= (habilitada==1)? true: false;
		prop.setHabilitada(estaHabilitada);*/
		
		return prop;
	}



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



	public static void main(String[] args){

		Date fechaActual = new Date();
		System.out.println(fechaActual);
		System.out.println("---------------------------------------------");

		//Formateando la fecha:
		DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
		DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println("Son las: "+formatoHora.format(fechaActual)+" de fecha: "+formatoFecha.format(fechaActual));

		//Fecha actual desglosada:
		Calendar fecha = Calendar.getInstance();
		int año = fecha.get(Calendar.YEAR);
		int mes = fecha.get(Calendar.MONTH) + 1;
		int dia = fecha.get(Calendar.DAY_OF_MONTH);
		int hora = fecha.get(Calendar.HOUR_OF_DAY);
		int minuto = fecha.get(Calendar.MINUTE);
		int segundo = fecha.get(Calendar.SECOND);



		System.out.println("Fecha Actual: "+ dia + "/" + (mes) + "/" + año);
		System.out.printf("Hora Actual: %02d:%02d:%02d %n", hora, minuto, segundo);
		System.out.println("-------------Fecha desglosada----------------");
		System.out.println("El año es: "+ año);
		System.out.println("El mes es: "+ mes);
		System.out.println("El día es: "+ dia);
		System.out.printf("La hora es: %02d %n", hora);
		System.out.printf("El minuto es: %02d %n", minuto);
		System.out.printf("El segundo es: %02d %n", segundo);

		Calendar cal = Calendar.getInstance();
		String date1= "17/03/2018";
		Date date;
		try {
			date = formatoFecha.parse(date1);
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_YEAR, -8);
			System.out.println("la resta es -----> "+cal.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}









}
