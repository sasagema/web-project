package services;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
import javax.ws.rs.core.Response.Status;

import beans.Vehicle;
import beans.Restaurant;
import beans.User;
import dao.RestaurantDao;
import dao.VehicleDao;
import enums.Role;
import enums.VehicleStatus;
import enums.VehicleType;

@Path("/vehicle")
public class VehicleService {

	@Context
	ServletContext context;
	
	@PostConstruct
	public void init() {
		
		if (context.getAttribute("VehicleDao") == null) {
	    	String contextPath = context.getRealPath("/");
	    	System.out.println(contextPath);
			context.setAttribute("VehicleDao", new VehicleDao(contextPath));
		}
		
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Vehicle> allVehicles(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		
		VehicleDao dao = (VehicleDao) context.getAttribute("VehicleDao");
		
		return dao.getAll();
		
	}
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Vehicle getOne(@PathParam("id") String id, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		
		VehicleDao dao = (VehicleDao) context.getAttribute("VehicleDao");
		System.out.println("Strigao id="+id);
		Vehicle vehicle = dao.getById(id);
		System.out.println("vraca ="+vehicle );
		return vehicle;
		
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addVehicle(Vehicle vehicle, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		VehicleDao dao = (VehicleDao) context.getAttribute("VehicleDao");
		Vehicle added = new Vehicle();
		//kreiranje novog vozila
		if (user == null || user.getRole() != Role.Admin) {
			
			return Response.status(401).entity("Morate biti ulogovani kao admin.").build();
			
		}
		Date datum = new Date();
		int godina = datum.getYear() + 1900;
		System.out.println("Godina " + godina);
		
		if (vehicle.getBrand().trim().isEmpty() || vehicle.getModel().trim().isEmpty() || vehicle.getNumberPlate().trim().isEmpty() || (vehicle.getProductionYear() <= 1930 || vehicle.getProductionYear() > godina)) {
			return Response.status(Status.BAD_REQUEST).entity("Unesite ispravne podatke.").build();
		}
		
		if(vehicle.getVehicleType() == VehicleType.BIKE) {
			vehicle.setNumberPlate("nema");
			added = dao.addVehicle(vehicle);
			if (added != null) { 
				return Response.ok(added).build();
			}
		}else {
			
			if( dao.findByNumPlate(vehicle.getNumberPlate()) == null) {
				added = dao.addVehicle(vehicle);
				if (added != null) { 
					return Response.ok(added).build();
				}
			}else {
				
				return Response.status(Status.BAD_REQUEST).entity("Postoji vozilo sa istom registracijom.").build();
			}
		
		}
			
		return Response.status(Status.BAD_REQUEST).entity("Greska.").build();	
	}
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editVehicle(Vehicle vehicle, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		Vehicle edited = new Vehicle();
		VehicleDao dao = (VehicleDao) context.getAttribute("VehicleDao");
		//izmena vehicleorana
		if (user == null || user.getRole() != Role.Admin) {
			
			return Response.status(401).entity("Morate biti ulogovani kao admin.").build();
			
		}
		Date datum = new Date();
		int godina = datum.getYear() + 1900;
		if (vehicle.getBrand().trim().isEmpty() || vehicle.getModel().trim().isEmpty() || vehicle.getNumberPlate().trim().isEmpty() || (vehicle.getProductionYear() <= 1930 || vehicle.getProductionYear() > godina)) {
			return Response.status(Status.BAD_REQUEST).entity("Unesite ispravne podatke.").build();
		}
		edited = dao.editVehicle(vehicle);	
		
		if (edited == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		return Response.ok(vehicle).build();
		
	}
	@DELETE
	@Path("/{id}")
	public Response deleteVehicle(@PathParam("id") String id, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		
		VehicleDao dao = (VehicleDao) context.getAttribute("VehicleDao");
		Vehicle vehicle = dao.getById(id);
		if (user == null || user.getRole() != Role.Admin) {
			
			return Response.status(401).entity("Morate biti ulogovani kao admin.").build();

		}
		if (vehicle.getOccupiedById() != null && vehicle.getOccupiedByUsername() != null && vehicle.getStatus() != VehicleStatus.Slobodan) {
			return Response.status(400).entity("Vozilo nije slobodno.").build();
		}else {
			
			dao.deleteVehicle(vehicle);
				
			return Response.status(200).entity("Vozilo je uspesno obrisano.").build();
			
		}
		
	}
	@GET
	@Path("/forDelivery")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehiclesForDelivery( @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
			if (user == null || user.getRole() == Role.Kupac) {
			
			return Response.status(401).entity("Morate biti ulogovani kao admin.").build();
			}
		VehicleDao dao = (VehicleDao) context.getAttribute("VehicleDao");
		List<Vehicle> vehicles = dao.getVehiclesForDelivery();
		
		return Response.ok(vehicles).build();
		
	}
}
