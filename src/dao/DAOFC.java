package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		 * RFC 1
		 * 
		 * EL DINERO RECIBIDO POR CADA PROVEEDOR DE ALOJAMIENTO DURANTE EL AÑO ACTUAL Y EL AÑO CORRIDO
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
					"        WHERE UPPER(PAPEL) = UPPER('operador')\r\n" + 
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
		public ArrayList<String> ofertasPopulares()  throws SQLException, Exception {
					
			String sql =String.format( "SELECT  ID_PROPUESTA, COUNT(ID_PROPUESTA) AS \"Cantidad Reservas\" \n" + 
					"		FROM %1$s.RESERVA \n" + 
					"		GROUP BY ID_PROPUESTA\n" + 
					"		ORDER BY \"Cantidad Reservas\" DESC", USUARIO);
			
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
		
		/**
		 * RFC3
		 * 
		 * Retorna las 20 orfertas mÃ¡s populares 
		 * @return
		 */
		public ArrayList<String> indiceOcupacion()  throws SQLException, Exception {
					
			String sql =String.format( "SELECT  *\r\n" + 
					"FROM \r\n" + 
					"( SELECT ID_PROPUESTA, AVG(PERSONAS)  \r\n" + 
					"FROM RESERVA \r\n" + 
					"GROUP BY ID_PROPUESTA)", USUARIO);
			
			//String sql = "SELECT * FROM " + USUARIO + ".RESERVAS  ";
			
			
			ArrayList<String> indice = new ArrayList<String>();

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			Integer personas = 0;

			while (rs.next()) {
				
				indice.add(rs.getLong("ID_PROPUESTA") + "-" +"rs.getInt(\"SUM(PERSONAS)\")" );
			}
			
			
			return indice;
			
		}
		
		
		/**
		 * RF8
		 * @param tipoInmueble
		 * @return
		 * @throws SQLException
		 */
		public List<Long> darClienteFrecuente(String tipoInmueble) throws SQLException
		{
			ArrayList<Long> res = new ArrayList<>();
			String sql = String.format("SELECT ID_PERSONA, COUNT(ID_PERSONA)" + 
					"FROM(" + 
					"SELECT ID_PERSONA" + 
					"FROM RESERVA RE " + 
					"WHERE RE.ID_PROPUESTA IN (" + 
					"SELECT ID FROM PROPUESTA" + 
					"WHERE TIPO_INMUEBLE = UPPER("+tipoInmueble+"))" + 
					") GROUP BY ID_PERSONA" + 
					"HAVING COUNT (ID_PERSONA) >3", USUARIO); 

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next())
			{
				Long id;
				id = rs.getLong("ID_PERSONA");
				res.add(id);
			}
			return res;
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

}
