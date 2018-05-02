package rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.AlohandesTransactionManager;
import vos.Reserva;
import vos.ReservaColectiva;

@Path("funcionalesConsulta")
public class RequerimientosService {
	
	//atributos
	
	/**
	 * 
	 */
	@Context
	private ServletContext context;

	
	//inicializaciones
	
	/**
	 * 
	 * @return
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}
	
	/**
	 * 
	 * @param e
	 * @return
	 */
	private String doErrorMessage(Exception e) {
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	
	/**
	 * 
	 * @param reserva
	 * @return
	 */
	@GET
	@Produces( MediaType.APPLICATION_JSON)
	@Path("RFC1")
	public Response darDineroRecibido() {
		
		try {
			AlohandesTransactionManager tm= new AlohandesTransactionManager(getPath());
			List<String> dinero = tm.dineroRecibido();
			return Response.status(200).entity(dinero).build();
			}catch( Exception e ){
				return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
			}
	}
	
	/**
	 * 
	 * @param reserva
	 * @return
	 */
	@GET
	@Produces( MediaType.APPLICATION_JSON)
	@Path("RFC2")
	public Response ofertasPopulares() {
		
		try {
			AlohandesTransactionManager tm= new AlohandesTransactionManager(getPath());
			List<String> ofertas = tm.PropuestasPopulares();
			return Response.status(200).entity(ofertas).build();
			}catch( Exception e ){
				return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
			}
	}
	
	/**
	 * Requerimiento funcional de consulta 3, que saca el indice de ocupación de cada propuesta
	 * @param reserva 
	 * @return
	 */
	@GET
	@Produces( MediaType.APPLICATION_JSON)
	@Path("RFC3")
	public Response indiceOcupacion() {
		
		try {
			AlohandesTransactionManager tm= new AlohandesTransactionManager(getPath());
			List<String> ofertas = tm.IndiceOcupacion();
			return Response.status(200).entity(ofertas).build();
			}catch( Exception e ){
				return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
			}
	}
	
	/**
	 * Requerimiento funcional de consulta 3, que saca el indice de ocupación de cada propuesta
	 * @param reserva 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("RFC4")
	public Response alojamientosDisponibles(@QueryParam("in") String in,@QueryParam("fin") String fin,@QueryParam("servs") String servs) {
		
		try {
			AlohandesTransactionManager tm= new AlohandesTransactionManager(getPath());
			List<Long> ofertas = tm.darPropuestasDisponibles(in, fin, servs);
			return Response.status(200).entity(ofertas).build();
			}catch( Exception e ){
				return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
			}
	}
	
	/**
	 * Requerimiento funcional de consulta 3, que saca el indice de ocupación de cada propuesta
	 * @param reserva 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("RFC5")
	public Response darUsoTotalAlohAndes() {
		
		try {
			AlohandesTransactionManager tm= new AlohandesTransactionManager(getPath());
			List<String> uso = tm.darUsoAlohAndes();
			return Response.status(200).entity(uso).build();
			}catch( Exception e ){
				return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
			}
	}
	
	/**
	 * Requerimiento funcional de consulta 3, que saca el indice de ocupación de cada propuesta
	 * @param reserva 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("RFC6")
	public Response darUsoDeUnUsuario(@QueryParam ("id") Long id) {
		
		try {
			AlohandesTransactionManager tm= new AlohandesTransactionManager(getPath());
			List<String> uso = tm.darUsoIndividualAlohAndes(id);
			return Response.status(200).entity(uso).build();
			}catch( Exception e ){
				return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
			}
	}
	
	
	/**
	 * 
	 * @param reserva
	 * @return
	 */
	@GET
	@Produces( MediaType.APPLICATION_JSON)
	@Path("RFC8")
	public Response darFrecuentes(@QueryParam("tipo")String tipo) {
		
		try {
			AlohandesTransactionManager tm= new AlohandesTransactionManager(getPath());
			List<String> clien = tm.ClientesFrecuentes(tipo);
			return Response.status(200).entity(clien).build();
			}catch( Exception e ){
				return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
			}
	}
	
	/**
	 * Requerimiento funcional de consulta 3, que saca el indice de ocupación de cada propuesta
	 * @param reserva 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("RFC9")
	public Response ofertasSinDemanda() {
		
		try {
			AlohandesTransactionManager tm= new AlohandesTransactionManager(getPath());
			List<String> uso = tm.propuestasSinDemandas();
			return Response.status(200).entity(uso).build();
			}catch( Exception e ){
				return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
			}
	}

	
	
}
