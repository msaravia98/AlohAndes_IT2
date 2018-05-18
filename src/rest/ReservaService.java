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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.AlohandesTransactionManager;
import vos.Reserva;
import vos.ReservaColectiva;

@Path("reservas")
public class ReservaService {
	
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
	
	//metodos rest
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getReservas() {
		
		try {
			AlohandesTransactionManager tm= new AlohandesTransactionManager(getPath());
			
			List<Reserva> reserva = tm.getAllReservas() ;
			return Response.status(200).entity(reserva).build();
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("{id: \\d+}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getReservaById(@PathParam("id") Long id) {
		
		try {
			AlohandesTransactionManager tm= new AlohandesTransactionManager(getPath());
			
			Reserva reserva = tm.getReservaById(id);
			return Response.status(200).entity(reserva).build();
		}catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
		
	}
	
	/**
	 * 
	 * @param reserva
	 * @return
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response registrarReserva(Reserva reserva) {
		
		try {
			AlohandesTransactionManager tm= new AlohandesTransactionManager(getPath());
			tm.registrarReserva(reserva);
			return Response.status(200).entity(reserva).build();
			}catch( Exception e ){
				return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
			}
	}

	
	
	
	
	/**
	 * 
	 * @param reserva
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id: \\d+}")
	public Response cancelarReserva(@PathParam("id") Long id) {
		
		try {
			AlohandesTransactionManager tm= new AlohandesTransactionManager(getPath());
			Reserva reserva = tm.getReservaById(id);
			tm.cancelarReserva(reserva);
			return Response.status( 200 ).entity(reserva).build();
		} catch (Exception e) {
			return Response.status( 500 ).entity(doErrorMessage(e)).build();
		}
	}

	
	
}