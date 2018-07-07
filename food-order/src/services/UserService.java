package services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
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

import beans.Item;
import beans.User;
import dao.ItemDao;
import dao.UserDao;
import enums.Role;

@Path("/user")
public class UserService {
	
	
	@Context
	ServletContext context;
	
	public UserService() {
	}
	
	@PostConstruct
	public void init() {

		if (context.getAttribute("userDao") == null) {
	    	String contextPath = context.getRealPath("/");
	    	System.out.println(contextPath);
			context.setAttribute("userDao", new UserDao(contextPath));
		}
	}
	
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logIn(User user, @Context HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		if (session.getAttribute("user") != null) {
			
			return Response.status(400).entity("Vec ste prijavljeni.").build();
		}
		if (user.getUsername().trim().isEmpty() || user.getPassword().trim().isEmpty() ) {
			return Response.status(400).entity("Unesite ispravno korisnicko ime i lozinku.").build();
		}
		UserDao dao = (UserDao) context.getAttribute("userDao");
		User newUser = dao.getByUsername(user.getUsername());
		
		if (newUser != null && newUser.getPassword().equals(user.getPassword())) {
			
			session.setAttribute("user", newUser);
			//return Response.status(200).entity("Uspesno ste se prijavili.").build();
			User retUser = new User();
			retUser.setUsername(newUser.getUsername());
			retUser.setRole(newUser.getRole());
			return Response.ok(retUser).build();
			
		} else {
			
			return Response.status(400).entity("Neispravno korisnicko ime i/ili lozinka.").build();
		}
		
	}
	
	@POST
	@Path("/registration")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registracija(User user) {
		user.setRole(Role.Kupac);

		//SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy HH:mm");
		DateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
		String date = dateFormat.format(new Date());
		user.setRegistrDate(date);
		
		System.out.println("Datum string" + date);
		if (user.getFirstName().trim().isEmpty() || user.getLastName().trim().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getTelephone().isEmpty()) {
			return Response.status(400).entity("Popunite polja za registraciju.").build();

		}
		UserDao dao = (UserDao) context.getAttribute("userDao");
		User newUser = dao.registerUser(user);
		
		if (newUser == null) {
			
			return Response.status(400).entity("Korisnicko ime ili email vec postoje.").build();
		
		}else 
			
			return Response.status(200).build();
		
		
		
	}
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@Context HttpServletRequest request) {
		request.getSession().invalidate();
		return Response.status(200).build();
	}
	@GET
	@Path("/currentUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User currentUser(@Context HttpServletRequest request) {
		return (User) request.getSession().getAttribute("user");
		
	}
	@GET
	@Path("/tests")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User test(@Context HttpServletRequest request) {
		return (User) request.getSession().getAttribute("user");
		
	}
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> test(){
		UserDao dao = (UserDao) context.getAttribute("userDao");
		return dao.getAll();
	}
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editUser(User user, @Context HttpServletRequest request) {
		User admin = (User) request.getSession().getAttribute("user");
	
		User edited = new User();
		UserDao dao = (UserDao) context.getAttribute("userDao");
		//izmena korisnika
		if (admin == null || admin.getRole() != Role.Admin) {
			
			return Response.status(401).entity("Morate biti ulogovani kao admin.").build();
			
		}else
		edited = dao.editUser(user);	
		
		if (edited == null) {
			return Response.status(Status.BAD_REQUEST) .build();
		}
		
		return Response.ok(edited).build();
		
	}
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("id") String id, @Context HttpServletRequest request){
		UserDao dao = (UserDao) context.getAttribute("userDao");
		return dao.getById(id);
	}
}
