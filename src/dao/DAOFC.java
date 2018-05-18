package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tm.BusinessLogicException;
import vos.Persona;
import vos.Propuesta;

public class DAOFC {


	//----------------------------------------------------------------------------------------------------------------------------------
	// CONSTANTES
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Constante para indicar el usuario Oracle del estudiante
	 */

	public final static String USUARIO = "ISIS2304A901810";


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
	public DAOFC() {
		recursos = new ArrayList<Object>();
	}


	//----------------------------------------------------------------------------------------------------------------------------------
	// REQUERIMIENTOS FUNCIONALES DE CONSULTA
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * RFC 1 EL DINERO RECIBIDO POR CADA PROVEEDOR DE ALOJAMIENTO DURANTE EL AÑO ACTUAL Y EL AÑO CORRIDO
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayList<String> dineroRecibido () throws SQLException, Exception {

		String sql = String.format("SELECT PP.*, ASW.ID_PROPUESTA, asw.\"TOTAL GANANCIAS\" FROM (\r\n" + 
				"\r\n" + 
				"SELECT RE.ID_PROPUESTA AS \"ID_PROPUESTA\", SUM(RE.COSTO) AS \"TOTAL GANANCIAS\"\r\n" + 
				"FROM RESERVA RE\r\n" + 
				"WHERE RE.ID_PROPUESTA IN (\r\n" + 
				"    SELECT PT.ID\r\n" + 
				"    FROM PROPUESTA PT \r\n" + 
				"    WHERE PT.ID_PERSONA IN (\r\n" + 
				"        SELECT PEP.ID  \r\n" + 
				"        FROM PERSONA PEP \r\n" + 
				"        WHERE PAPEL = 'OPERADOR'\r\n" + 
				"    )\r\n" + 
				")\r\n" + 
				"GROUP BY RE.ID_PROPUESTA\r\n" + 
				") ASW\r\n" + 
				"INNER JOIN PROPUESTA PU\r\n" + 
				"ON PU.ID = ASW.ID_PROPUESTA\r\n" + 
				"\r\n" + 
				"INNER JOIN PERSONA PP\r\n" + 
				"ON PP.ID = PU.ID_PERSONA\r\n" + 
				"\r\n" + 
				"ORDER BY asw.\"TOTAL GANANCIAS\" DESC", USUARIO);

		System.out.println("Esta es la puta sentencia " + sql);
		ArrayList<String> pagos = new ArrayList<>();
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			pagos.add(rs.getLong("ID") +
					"-"+ rs.getString("NOMBRE")+
					"-"+rs.getString("PAPEL")+
					"-"+rs.getInt("VALOR_MULTA")+
					"-"+rs.getInt("TOTAL GANANCIAS"));
		}
		return pagos;
	}

	/**
	 * RFC2 Retorna las 20 ofertas mas populares
	 * @return
	 */
	public ArrayList<String> ofertasPopulares()  throws SQLException, Exception {

		String sql =String.format( "SELECT  *\r\n" + 
				"FROM \r\n" + 
				"( SELECT ID_PROPUESTA, COUNT(ID_PROPUESTA) AS \"Cantidad Reservas\"  \r\n" + 
				"FROM RESERVA \r\n" + 
				"GROUP BY ID_PROPUESTA\r\n" + 
				"ORDER BY \"Cantidad Reservas\" DESC)\r\n" + 
				"WHERE ROWNUM <= 20", USUARIO);

		ArrayList<String> populares = new ArrayList<String>();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();


		while (rs.next()) {
			populares.add("El id de propuesta es: "+rs.getLong("ID_PROPUESTA") + " con "+rs.getInt("Cantidad Reservas")+" reservas.");
		}
		return populares;

	}

	/**
	 * RFC3 Da el indice de ocupacion de las ofertas de AlohAndes
	 * @return
	 */
	public ArrayList<String> indiceOcupacion()  throws SQLException, Exception {

		String sql =String.format( "SELECT P.ID,(SUM(R.PERSONAS)/ (SUM(P.CAPACIDAD)))*100 AS PORCENTAJE \r\n" + 
				"FROM PROPUESTA P\r\n" + 
				"INNER JOIN RESERVA R \r\n" + 
				"ON P.ID = R.ID_PROPUESTA \r\n" + 
				"GROUP BY P.ID", USUARIO);


		ArrayList<String> indice = new ArrayList<String>();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {

			indice.add("La propuesta con id: "+rs.getLong("ID") + " tiene un porcentaje de " +rs.getInt("PORCENTAJE") );
		}


		return indice;

	}

	/**
	 * RFC4 Da los alojamientos disponibles en rango de fechas con servicios
	 * @param in Fecha inicial en la que se desean buscar
	 * @param fin Fecha final en la que se desean buscar
	 * @param servs Servicios que se desean
	 * @return Lista con los id de los alojamientos disponibles
	 * @throws BusinessLogicException Excepción si se violan reglas de negocio
	 * @throws SQLException Cualquier excepción lanzada en la ejecución de la sentencia
	 */
	public List<Long> alojamientosRango(String in,String fin,String servs) throws BusinessLogicException, SQLException
	{
		List<Long> p1=propuestasNoDisponiblesFecha(in, fin);
		System.out.println("las que no sirven"+p1.size());
		List<Long> p2=propuestasQueSirven(servs);
		System.out.println("las que sirven"+p1.size());
		List<Long> rt = new ArrayList<>();
		for(Long s:p2)
		{
			if(buscarP(s,p1)==false)
			{
				rt.add(s);
			}
		}
		return rt;
	}

	/**
	 * RFC 5 Da el uso de todos los usuarios de alohandes
	 * @return Retorne una lista con el uso de alohandes
	 * @throws BusinessLogicException Violación de reglas de negocio
	 * @throws SQLException Excepcion que se pueda dar durante la ejecución de la sentencia
	 */
	public List<String> usoAlohAndes() throws BusinessLogicException, SQLException
	{
		List<String> retornar = new ArrayList<>();
		String sql = String.format("SELECT TIPO, PAPEL, COSTOTOTAL,TOTALDIAS FROM PERSONA P\r\n" + 
				"INNER JOIN\r\n" + 
				"(SELECT R.ID_PERSONA AS AA, SUM(R.COSTO) AS COSTOTOTAL, SUM(R.DURACION) AS TOTALDIAS\r\n" + 
				"FROM RESERVA R \r\n" + 
				"JOIN PERSONA P ON R.ID_PERSONA = P.ID\r\n" + 
				"GROUP BY R.ID_PERSONA)X\r\n" + 
				"ON P.ID = X.AA", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			retornar.add(rs.getString("TIPO")+
					"-"+rs.getString("PAPEL")+
					"-"+rs.getInt("COSTOTOTAL")+
					"-"+rs.getInt("TOTALDIAS"));
		}
		return retornar;
	}

	/**
	 * RFC 6 Da el uso de un usuario de alohandes
	 * @return Retorna una lista con el uso de alohandes
	 * @throws BusinessLogicException Violación de reglas de negocio
	 * @throws SQLException Excepcion que se pueda dar durante la ejecución de la sentencia
	 */
	public List<String> usoAlohAndesIndividual(Long id) throws BusinessLogicException, SQLException
	{
		List<String> retornar = new ArrayList<>();
		String sql = String.format("SELECT TIPO, PAPEL, COSTOTOTAL,TOTALDIAS FROM PERSONA P\r\n" + 
				"INNER JOIN\r\n" + 
				"(SELECT R.ID_PERSONA AS AA, SUM(R.COSTO) AS COSTOTOTAL, SUM(R.DURACION) AS TOTALDIAS\r\n" + 
				"FROM RESERVA R \r\n" + 
				"JOIN PERSONA P ON R.ID_PERSONA ="+id+ 
				"GROUP BY R.ID_PERSONA)X\r\n" + 
				"ON P.ID = X.AA", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			retornar.add(rs.getString("TIPO")+
					"-"+rs.getString("PAPEL")+
					"-"+rs.getInt("COSTOTOTAL")+
					"-"+rs.getInt("TOTALDIAS"));
		}
		return retornar;
	}

	/**
	 * RF8
	 * @param tipoInmueble
	 * @return
	 * @throws SQLException
	 */
	public List<String> darClienteFrecuente(String tipoInmueble) throws SQLException
	{
		ArrayList<String> res = new ArrayList<>();
		String sql = String.format("SELECT ID_PERSONA, COUNT(ID_PERSONA) \r\n" + 
				"					FROM(  \r\n" + 
				"					SELECT ID_PERSONA \r\n" + 
				"					FROM RESERVA RE  \r\n" + 
				"					WHERE RE.ID_PROPUESTA IN ( \r\n" + 
				"					SELECT ID FROM PROPUESTA\r\n" + 
				"					WHERE TIPO_INMUEBLE = UPPER('"+tipoInmueble+"'))  \r\n" + 
				"					) GROUP BY ID_PERSONA \r\n" + 
				"					HAVING COUNT (ID_PERSONA) >3", USUARIO); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			res.add("El cliente más frecuente en el tipo de inmueble "
					+tipoInmueble+ 
					" es el que tiene el ID: "+ 
					rs.getLong("ID_PERSONA")+" y posee: "+ rs.getInt("COUNT(ID_PERSONA)")+" reservas.");
		}
		return res;
	}
	/**
	 * RFC9
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> propuestasSinDemanda()throws Exception{

		String sql= String.format("SELECT ID, TIPO_INMUEBLE FROM %1$s.PROPUESTA INNER JOIN %1$s.RESERVA"
				+ "ON  PROPUESTA.ID = RESERVA.ID_PROPUESTA"
				+ "WHERE RESERVA.DURACION <= 30", USUARIO);

		ArrayList<String> sinDemanda = new ArrayList<String>();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();


		while (rs.next()) {
			sinDemanda.add(rs.getLong("ID") + " "+rs.getString("TIPO_INMUEBLE"));
		}
		return sinDemanda;
	}


	/**
	 * RFC10 consumo 1 en alhohandes
	 * @param fechaInicio
	 * @param fechaFin
	 * @param idOperador
	 * @param TipoInmueble
	 * @param tipoPersona
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Persona> consultaConsumoQueSeHizo(String fechaInicio, String fechaFin, Long idOperador, String TipoInmueble,
			String tipoPersona)throws SQLException{



		DAOPersona daop= new DAOPersona();
		StringBuilder sql= new StringBuilder();

		sql.append(String.format("SELECT P.*, PER.* , r.*\n" + 
				"FROM (PROPUESTA P INNER JOIN RESERVA R ON P.ID = R.ID_PROPUESTA) INNER JOIN PERSONA PER ON R.ID_PERSONA = PER.ID\n" + 
				"WHERE r.fecha_registro >= TO_DATE('%1s', 'DD-MM-yy') and R.FECHA_REGISTRO <= TO_DATE('%2s', 'DD-MM-yy') AND PER.PAPEL = 'CLIENTE'\n" + 
				"AND PER.ID = %3d\n" + 
				"AND TIPO_INMUEBLE LIKE '%4s'\n" + 
				"AND TIPO LIKE '%5s'\n" + 
				"ORDER BY P.ID;",
				fechaInicio,
				fechaFin,
				idOperador,
				TipoInmueble,
				tipoPersona));

		ArrayList<Persona> resultado= new ArrayList<>();

		PreparedStatement prepStatement= conn.prepareStatement(sql.toString());
		recursos.add(prepStatement);
		ResultSet rs= prepStatement.executeQuery();

		while(rs.next())
			resultado.add(daop.convertResultSetTo_Persona(rs));

		return resultado;

	}



	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<Long> funcionamientoPropuestas() throws SQLException{

		StringBuilder sql= new StringBuilder();
		sql.append(String.format("select lista.semana ,lista.propu , lista.dura\n" + 
				"from \n" + 
				"(\n" + 
				"SELECT TO_CHAR(r.fecha_inicio, 'WW') AS SEMANA,r.id_propuesta as propu,  sum(r.duracion) as Dura\n" + 
				"FROM (PROPUESTA P INNER JOIN RESERVA R ON P.ID= R.ID_PROPUESTA)\n" + 
				"GROUP BY R.ID_PROPUESTA , TO_CHAR(r.fecha_inicio, 'WW')\n" + 
				"order by TO_CHAR(r.fecha_inicio, 'WW') \n" + 
				") lista  \n" + 
				"where lista.dura = (select   max(res.duracion)\n" + 
				"                    FROM PROPUESTA Prop INNER JOIN RESERVA Res ON Prop.ID= Res.ID_PROPUESTA\n" + 
				"                            where TO_CHAR(res.fecha_inicio, 'WW') = lista.semana\n" + 
				"                            GROUP by TO_CHAR(res.fecha_inicio, 'WW')\n" + 
				"                                 )\n" + 
				"\n" + 
				"order by lista.semana"));

		ArrayList<Long> resultado= new ArrayList<>();

		PreparedStatement prepStmt= conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		ResultSet rs= prepStmt.executeQuery();
		while(rs.next())
			resultado.add(rs.getLong("PROPU"));

		return resultado;

	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Propuesta> funcionamientoPropuestasObj() throws Exception{

		DAOPersona dao= new DAOPersona();
		ArrayList<Propuesta> resultado= new ArrayList<>();
		ArrayList<Long> ids= funcionamientoPropuestas();

		for(Long x: ids) {
			dao.setConn(this.conn);
			resultado.add(dao.getPropuestaById(x));
		}

		return resultado;
	}


	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<Long> operadoresMasSolicitados() throws SQLException{

		StringBuilder sql= new StringBuilder();
		sql.append(String.format("select lista.semana ,lista.OPERADOR , lista.CANT\n" + 
				"from \n" + 
				"(\n" + 
				"SELECT TO_CHAR(r.fecha_inicio, 'WW') AS SEMANA,PER.ID as OPERADOR,  COUNT(r.duracion) as CANT\n" + 
				"FROM (PROPUESTA P INNER JOIN RESERVA R ON P.ID= R.ID_PROPUESTA) INNER JOIN PERSONA PER ON P.ID_PERSONA = PER.ID\n" + 
				"GROUP BY PER.ID , TO_CHAR(r.fecha_inicio, 'WW')\n" + 
				"order by TO_CHAR(r.fecha_inicio, 'WW') \n" + 
				") lista  \n" + 
				"where lista.CANT = (select   MAX(COUNT(*))\n" + 
				"                    FROM (PROPUESTA Prop INNER JOIN RESERVA Res ON Prop.ID= Res.ID_PROPUESTA) INNER JOIN PERSONA OPER ON PROP.ID_PERSONA= OPER.ID\n" + 
				"                            where TO_CHAR(res.fecha_inicio, 'WW') = lista.semana\n" + 
				"                            GROUP by TO_CHAR(res.fecha_inicio, 'WW')\n" + 
				"                                 ) \n" + 
				"\n" + 
				"order by lista.semana"));

		ArrayList<Long> resultado= new ArrayList<>();

		PreparedStatement prepStmt= conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		ResultSet rs= prepStmt.executeQuery();
		while(rs.next())
			resultado.add(rs.getLong("OPERADOR"));

		return resultado;
	}
	
	public ArrayList<Persona> operadoresMasSolicitadosObj(Connection conn) throws Exception{
		
		DAOPersona dao= new DAOPersona();
		ArrayList<Persona> resultado = new ArrayList<>();
		ArrayList<Long> ids= operadoresMasSolicitados();
		this.conn = conn;
		
		for(Long x: ids)
		{
			dao.setConn(this.conn);
			resultado.add(dao.findPersonaById(x));
		}
			
		return resultado;
	}


	/**
	 * RFC13
	 * @param cantMes
	 * @param cantMes1
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Persona> buenosClientes(Integer cantMes, Integer cantMes1)throws SQLException{

		DAOPersona daoP= new DAOPersona();
		StringBuilder sql= new StringBuilder();

		sql.append(String.format("SELECT DISTINCT P.*\n" + 
				"FROM (PERSONA P INNER JOIN RESERVA R ON P.ID = R.ID_PERSONA), PROPUESTA PROP, HOTEL H\n" + 
				"WHERE (PROP.ID= R.ID_PROPUESTA AND PROP.ID_HOTEL= H.ID AND  H.TIPO_HABITACION ='Suite' ) OR ((R.COSTO/R.DURACION) >= 150) OR (P.ID IN (\n" + 
				"SELECT DISTINCT PER.ID \n" + 
				"FROM PERSONA PER INNER JOIN RESERVA RES ON PER.ID = RES.ID_PERSONA\n" + 
				"WHERE  (SYSDATE -  RES.FECHA_INICIO) < %1d * 30\n" + 
				"GROUP BY PER.ID\n" + 
				"HAVING COUNT ( PER.ID) >= %2d ;\n" + 
				"))", cantMes, cantMes1));

		ArrayList<Persona> resultado= new ArrayList<>();
		PreparedStatement prepStatement= conn.prepareStatement(sql.toString());
		recursos.add(prepStatement);
		ResultSet rs= prepStatement.executeQuery();

		while(rs.next())
			resultado.add(daoP.convertResultSetTo_Persona(rs));

		return resultado;


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

	//-----------------------------------------------------------------------------------
	//Métodos Auxiliares
	//-----------------------------------------------------------------------------------

	private boolean buscarP(Long id, List<Long>p)
	{
		Boolean rta = false;
		for(Long e:p)
		{
			if( e == id)
			{
				rta = true;
			}
		}
		return rta;
	}
	/**
	 * Dice que propuestas no están disponibles en un rango de fechas
	 * @param in Fecha inicial
	 * @param fin Fecha final
	 * @return Retorna una lista con los id de las propuestas no disponibles
	 * @throws BusinessLogicException Violación de reglas de negocio durante la ejecución del método
	 * @throws SQLException Excepciones durante la ejecución de la sentencia.
	 */
	private List<Long> propuestasNoDisponiblesFecha(String in,String fin) throws BusinessLogicException, SQLException {


		List<Long> este= new ArrayList<>();



		String sql    ="SELECT ID_PROPUESTA  \r\n" + 
				"FROM RESERVA R \r\n" + 
				"WHERE TO_DATE("+"'"+in+"'"+", 'yyyy/mm/dd') BETWEEN R.FECHA_INICIO AND R.FECHA_FINAL\r\n" + 
				"OR TO_DATE("+"'"+fin+"'"+", 'yyyy/mm/dd') BETWEEN R.FECHA_INICIO AND R.FECHA_FINAL";
		System.out.println(sql+ "esta es la sentencia");
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		System.out.println("Esta es la sentencia acá "+ sql);
		ResultSet rs =prepStmt.executeQuery();

		while(rs.next())
		{
			este.add(rs.getLong("ID_PROPUESTA"));
		}
		return este;
	}
	/**
	 * Da la lista de propuestas que tienen los servicios requeridos
	 * @param ser Servicios requeridos, separados por coma
	 * @return Retorna una lista de propuestas que sirven
	 * @throws SQLException Excepciones durante la ejecución del sql
	 */
	private List<Long> propuestasQueSirven(String ser) throws SQLException
	{
		String[] servis = ser.split("-");
		List<Long> retornar = new ArrayList<>();
		String sentencia = new String();
		ArrayList<String> servicios = new ArrayList<>();
		servicios.add("APARTAMENTO");
		servicios.add("HABITACION");
		servicios.add("VIVIENDA_EXPRESS");
		servicios.add("VIVIENDA_UNIVERSITARIA");

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
		for(String serv:servicios) {
			String sql    ="SELECT * "
					+ "FROM PROPUESTA PRO "
					+ "WHERE UPPER(TIPO_INMUEBLE) = UPPER('"+serv+"') "
					+ "AND PRO.ID_"+serv+" "
					+ "IN ( "
					+ "SELECT SERV.ID_"+serv+" "
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
				retornar.add(rs.getLong("ID"));
			}
		}
		return retornar;
	}
}
