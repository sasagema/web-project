package services;

import java.util.ArrayList;
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

import beans.Item;
import beans.Restaurant;
import beans.User;
import dao.ItemDao;
import dao.RestaurantDao;
import dao.UserDao;
import dto.SearchRestDto;
import enums.RestCateg;
import enums.Role;


@Path("/restaurant")
public class RestaurantService {
	
	@Context
	ServletContext context;
	
	@PostConstruct
	public void init() {
		
		if (context.getAttribute("RestaurantDao") == null) {
	    	String contextPath = context.getRealPath("/");
	    	System.out.println(contextPath);
			context.setAttribute("RestaurantDao", new RestaurantDao(contextPath));
		}
		if (context.getAttribute("ItemDao") == null) {
	    	String contextPath = context.getRealPath("/");
	    	System.out.println(contextPath);
			context.setAttribute("ItemDao", new ItemDao(contextPath));
		}
		if (context.getAttribute("userDao") == null) {
	    	String contextPath = context.getRealPath("/");
	    	System.out.println(contextPath);
			context.setAttribute("userDao", new UserDao(contextPath));
		}
		
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Restaurant> allRestaurants(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		
		RestaurantDao dao = (RestaurantDao) context.getAttribute("RestaurantDao");
		
		return dao.getAll();
		
	}
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Restaurant getOne(@PathParam("id") String id, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		
		RestaurantDao dao = (RestaurantDao) context.getAttribute("RestaurantDao");
		System.out.println("Strigao id="+id);
		Restaurant rest = dao.getById(id);
		System.out.println("vraca ="+rest );
		return rest;
		
	}
	@POST
	@Path("/add-favorite/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addFavorite(@PathParam("id") String id, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		RestaurantDao dao = (RestaurantDao) context.getAttribute("RestaurantDao");
		UserDao userDao = (UserDao) context.getAttribute("userDao");
		User edited = new User();
		System.out.println(user);
		if (user == null || user.getRole() != Role.Kupac) {
		
		return Response.status(401).entity("Morate biti ulogovani.").build();
		
		}else {
			
			Restaurant restToAdd = dao.getById(id);
			edited = userDao.addToFavorite(user, restToAdd);
			if (edited == null) {
				return Response.status(400).entity("Restoran je vec dodat u listu omiljenih.").build();
			}else
				return Response.ok(restToAdd).build();
		}

	}
	@POST
	@Path("/remove-favorite/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeFavorite(@PathParam("id") String id, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		RestaurantDao dao = (RestaurantDao) context.getAttribute("RestaurantDao");
		UserDao userDao = (UserDao) context.getAttribute("userDao");
		User edited = new User();
		System.out.println(user);
		if (user == null || user.getRole() != Role.Kupac) {
		
		return Response.status(401).entity("Morate biti ulogovani.").build();
		
		}else {
			
			Restaurant restToAdd = dao.getById(id);
			edited = userDao.removeFromFavorite(user, restToAdd);
			if (edited == null) {
				return Response.status(400).entity("Restoran nije u listi omiljenih.").build();
			}else
				return Response.ok(restToAdd).build();
		}

	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRestaurants(Restaurant rest, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		RestaurantDao dao = (RestaurantDao) context.getAttribute("RestaurantDao");
		Restaurant added = new Restaurant();
		System.out.println("Dodavanje restorana...");
		System.out.println(rest);
		//kreiranje novog restorana
		/*if (user == null || user.getRole() != Role.ADMIN) {
			
			return Response.status(401).entity("Morate biti ulogovani kao admin.").build();
			
		}else*/
		if (rest.getName().trim().isEmpty() || rest.getAddress().trim().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity("Unesite ispravne podatke.").build();
		}else
			added = dao.addRestaurant(rest);
			if (added != null) {
				return Response.ok(rest).build();
			}else
				return Response.status(Status.BAD_REQUEST) .build();
		
		
	}
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editRestaurant(Restaurant rest, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		Restaurant edited = new Restaurant();
		RestaurantDao dao = (RestaurantDao) context.getAttribute("RestaurantDao");
		//izmena restorana
		/*if (user == null || user.getRole() != Role.ADMIN) {
			
			return Response.status(401).entity("Morate biti ulogovani kao admin.").build();
			
		}else*/
		edited = dao.editRestaurant(rest);	
		
		if (edited == null) {
			return Response.status(Status.BAD_REQUEST) .build();
		}
		
		return Response.ok(rest).build();
		
	}
	@DELETE
	@Path("/{id}")
	public Response deleteRestaurant(@PathParam("id") String id, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		
		RestaurantDao restDao = (RestaurantDao) context.getAttribute("RestaurantDao");
		ItemDao itemDao = (ItemDao) context.getAttribute("ItemDao");
		boolean meals = true;
		boolean drinks = true;
		if (user == null || user.getRole() != Role.Admin) {
			
			return Response.status(401).entity("Morate biti ulogovani kao admin.").build();

		}
		Restaurant rest = restDao.getById(id);
		System.out.println(rest);
		List<Item> mealsForDelete = rest.getMeals();
		List<Item> drinksForDelete = rest.getDrinks();
		if (mealsForDelete.size() > 0) {
			meals =  itemDao.deleteItems(mealsForDelete);
		}
		if (drinksForDelete.size() > 0) {
			drinks = itemDao.deleteItems(drinksForDelete);
		}
		
		
		if (restDao.deleteRestaurant(rest) && meals && drinks) {
				
			return Response.status(200).entity("Restoran je uspesno obrisan.").build();
		}
		
		return Response.status(400).entity("Restoran ne postoji.").build();
		
	}
	@GET
	@Path("/testData")
	@Produces(MediaType.APPLICATION_JSON)
	public Restaurant addRestaurant(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		Restaurant rest = new Restaurant();
		rest.setName("Restporan 1");
		rest.setRestCateg(RestCateg.Domaca_kuhinja);
		rest.setAddress("Adresa 1");
		RestaurantDao dao = (RestaurantDao) context.getAttribute("RestaurantDao");
		dao.addRestaurant(rest);
		

		return rest;
		
	}
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SearchRestDto searchRestaurants(SearchRestDto searchRestDto,  @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		
		String name = searchRestDto.getRestName();
		String address = searchRestDto.getRestAddress();
		RestCateg restCateg;
		if (searchRestDto.getRestCateg() >= 0) {
			restCateg = RestCateg.values()[searchRestDto.getRestCateg()];
		}else {
			restCateg = null;
		}
	
		System.out.println(name.isEmpty());
		System.out.println(address.isEmpty());
		System.out.println(restCateg);
		if (name.isEmpty()) {
			name=null;
		}
		if (address.isEmpty()) {
			address=null;
		}
		RestaurantDao restDao = (RestaurantDao) context.getAttribute("RestaurantDao");
		List<Restaurant> allRestaurants = restDao.getAll();
		List<Restaurant> retList = new ArrayList<Restaurant>();
		List<Restaurant> restName = new ArrayList<Restaurant>();
		List<Restaurant> restAddr = new ArrayList<Restaurant>();
		List<Restaurant> restCategList = new ArrayList<Restaurant>();
		for (Restaurant rest : allRestaurants) {
			if (name != null && rest.getName().toLowerCase().contains(name.toLowerCase())) {
				if (!restName.contains(rest)) {
					restName.add(rest);
				}
			}
			if (address != null && rest.getAddress().toLowerCase().contains(address.toLowerCase())) {
				if (!restAddr.contains(rest)) {
					restAddr.add(rest);
				}
			}
			if (restCateg != null && rest.getRestCateg() == restCateg) {
				if (!restCategList.contains(rest)) {
					restCategList.add(rest);
				}
			}
			
		}
		
			if(name != null && address != null && restCateg != null) {
				if (restName.isEmpty() || restAddr.isEmpty() || restCategList.isEmpty()) {
					System.out.println("ovde1");
					System.out.println(restName.isEmpty());
					System.out.println(restAddr.isEmpty());
					System.out.println(restCategList.isEmpty());
					return searchRestDto;
				}else {
					for (Restaurant r : restName) {
						if (restAddr.contains(r) && restCategList.contains(r)) {
							System.out.println("ovde2");
							retList.add(r);
						}
					}
					searchRestDto.setRestaurants(retList);
					return searchRestDto;
				}
			}
			if (name != null && address != null && restCateg == null) {
				if (restName.isEmpty() || restAddr.isEmpty()) {
					return searchRestDto;
				}else {
					for (Restaurant r : restName) {
						if (restAddr.contains(r)) {
							retList.add(r);
						}
					}
					searchRestDto.setRestaurants(retList);
					return searchRestDto;
				}
			}
			if (name != null && address == null && restCateg != null) {
				if (restName.isEmpty() || restCategList.isEmpty()) {
					return searchRestDto;
				}else {
					for (Restaurant r : restName) {
						if (restCategList.contains(r)) {
							retList.add(r);
						}
					}
					searchRestDto.setRestaurants(retList);
					return searchRestDto;
				}
			}
			if (name == null && address != null && restCateg != null) {
				if (restAddr.isEmpty() || restCategList.isEmpty()) {
					return searchRestDto;
				}else {
					for (Restaurant r : restAddr) {
						if (restCategList.contains(r)) {
							retList.add(r);
						}
					}
					searchRestDto.setRestaurants(retList);
					return searchRestDto;
				}
			}
			if (name != null && address == null && restCateg == null) {
				if (restName.isEmpty()) {
					return searchRestDto;
				}else {				
					searchRestDto.setRestaurants(restName);
					return searchRestDto;
				}
			}
			if (name == null && address != null && restCateg == null) {
				if (restAddr.isEmpty()) {
					return searchRestDto;
				}else {				
					searchRestDto.setRestaurants(restAddr);
					return searchRestDto;
				}
			}
			if (name == null && address == null && restCateg != null) {
				if (restCategList.isEmpty()) {
					return searchRestDto;
				}else {				
					searchRestDto.setRestaurants(restCategList);
					return searchRestDto;
				}
			}
			
		searchRestDto.setRestaurants(retList);
		return searchRestDto;
		
	}
	public List<Restaurant> intersect( List<Restaurant> list1, List<Restaurant> list2){
		List<Restaurant> retList = new ArrayList<Restaurant>();
		
		for (Restaurant rest : list1) {
			if (list2.remove(rest)) {
				retList.add(rest);
			}
		}
		return retList;
	}
}
