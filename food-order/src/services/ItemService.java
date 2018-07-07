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
import dto.SearchItemDto;
import dto.SearchRestDto;
import enums.ItemType;
import enums.RestCateg;
import enums.Role;



@Path("/item")
public class ItemService {

	@Context
	ServletContext context;
	
	@PostConstruct
	public void init() {
		
		if (context.getAttribute("ItemDao") == null) {
	    	String contextPath = context.getRealPath("/");
	    	System.out.println(contextPath);
			context.setAttribute("ItemDao", new ItemDao(contextPath));
		}
		if (context.getAttribute("RestaurantDao") == null) {
	    	String contextPath = context.getRealPath("/");
	    	System.out.println(contextPath);
			context.setAttribute("RestaurantDao", new RestaurantDao(contextPath));
		}
		
		
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> allItems(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		
		ItemDao dao = (ItemDao) context.getAttribute("ItemDao");
		System.out.println("ARTIKLI: " +dao.getAll());
		return dao.getAll();
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addItem(Item item, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		ItemDao itemDao = (ItemDao) context.getAttribute("ItemDao");
		RestaurantDao restDao = (RestaurantDao) context.getAttribute("RestaurantDao");
		Item added = new Item();
		//kreiranje novog artikla
		if (user == null || user.getRole() != Role.Admin) {
			
			return Response.status(401).entity("Morate biti ulogovani kao admin.").build();
			
		}
		 if(item.getName().trim().isEmpty() || item.getDescription().trim().isEmpty() || (item.getPrice() <= 0 || item.getRestaurantId() == null)) {
			return Response.status(Status.BAD_REQUEST).entity("Unesite ispravne podatke.").build();
		}
			added = itemDao.addItem(item);
			
			if (added != null) {
				restDao.addItemToRest(added);
				return Response.ok(item).build();
			}else
				return Response.status(Status.BAD_REQUEST) .build();
		
		
	}
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editItem(Item item, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		Item edited = new Item();
		ItemDao dao = (ItemDao) context.getAttribute("ItemDao");
		//izmena item
		if (user == null || user.getRole() != Role.Admin) {
			
			return Response.status(401).entity("Morate biti ulogovani kao admin.").build();
			
		}
		if(item.getName().trim().isEmpty() || item.getDescription().trim().isEmpty() || (item.getPrice() <= 0 || item.getRestaurantId() == null)) {
			return Response.status(Status.BAD_REQUEST).entity("Unesite ispravne podatke.").build();
		}
		edited = dao.editItem(item);	
		
		if (edited == null) {
			return Response.status(Status.BAD_REQUEST) .build();
		}
		
		return Response.ok(item).build();
		
	}
	@DELETE
	@Path("/{id}")

	public Response deleteItem(@PathParam("id") String id, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		
		ItemDao dao = (ItemDao) context.getAttribute("ItemDao");
		RestaurantDao restDao = (RestaurantDao) context.getAttribute("RestaurantDao");
		if (user == null || user.getRole() != Role.Admin) {
			
			return Response.status(401).entity("Morate biti ulogovani kao admin.").build();

		} 
		Item item = dao.getById(id);
		Restaurant rest = restDao.getById(item.getRestaurantId());
		if (item != null && rest != null) {
			
			
				if (item.getItemType() == ItemType.MEAL) {
					rest.getMeals().remove(item);
				}else {
					rest.getDrinks().remove(item);
				}
			
	
				
			dao.deleteItem(item);
			restDao.saveRestaurants();
			return Response.status(200).entity("Artikal je uspesno obrisan.").build();
		}
		
		return Response.status(201).entity("Artikal ne postoji.").build();
		
	}
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> getPopular(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		
		ItemDao dao = (ItemDao) context.getAttribute("ItemDao");
		
		return dao.getPopularItems();
		
	}
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Item getOne(@PathParam("id") String id, @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		
		ItemDao dao = (ItemDao) context.getAttribute("ItemDao");
		System.out.println("Strigao id="+id);
		Item item = dao.getById(id);
		System.out.println("vraca ="+item );
		return item;
		
	}
	@GET
	@Path("/popularItems")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> popularItems(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		
		ItemDao dao = (ItemDao) context.getAttribute("ItemDao");
		System.out.println("ARTIKLI: " +dao.getPopularItems());
		return dao.getPopularItems();
		
	}
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SearchItemDto searchRestaurants(SearchItemDto searchItemDto,  @Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		ItemDao itemDao = (ItemDao) context.getAttribute("ItemDao");
		RestaurantDao restDao = (RestaurantDao) context.getAttribute("RestaurantDao");
		String itemName = searchItemDto.getItemName();
		ItemType itemType;
		if (searchItemDto.getItemType() >= 0) {
			itemType = ItemType.values()[searchItemDto.getItemType()];
		}else {
			itemType = null;
		}
		double itemPriceMin = searchItemDto.getItemPriceMin();
		double itemPriceMax = searchItemDto.getItemPriceMax();
		System.out.println(itemPriceMin);
		System.out.println(itemPriceMax);
		if (itemPriceMin < 0 || itemPriceMin > itemPriceMax || itemPriceMax > 10000) {
			return null;
		}
		String restId = searchItemDto.getItemRestId();
		Restaurant rest = restDao.getById(searchItemDto.getItemRestId()); 
		List<Item> restItems = new ArrayList<Item>();
		if (rest != null) {
			restItems.addAll(rest.getDrinks());
			restItems.addAll(rest.getMeals());
		}
		List<Item> retList = new ArrayList<Item>();
		List<Item> allItems = itemDao.getAll();
		List<Item> itemNames = new ArrayList<Item>();
		List<Item> itemPrices = new ArrayList<Item>();
		List<Item> itemTypes = new ArrayList<Item>();
		List<Item> itemsFromRest = new ArrayList<Item>();
		for (Item item : allItems) {
			if (!itemName.isEmpty() && item.getName().toLowerCase().contains(itemName.toLowerCase())) {
				itemNames.add(item);
			}
			if (itemType != null && item.getItemType() == itemType) {
				itemTypes.add(item);
			}
			if (item.getPrice() >= itemPriceMin && item.getPrice() <= itemPriceMax ) {
				itemPrices.add(item);
			}
			if (!restItems.contains(item)) {
				itemsFromRest.add(item);
			}
			
		}
		if (!itemName.isEmpty() && itemPriceMax == 0 && rest == null && itemType == null) {
			searchItemDto.setItems(itemNames);
			return searchItemDto;
		}
		if (itemName.isEmpty() && itemPriceMax != 0 && rest == null && itemType == null) {
			searchItemDto.setItems(itemPrices);
			return searchItemDto;
		}
		if (itemName.isEmpty() && itemPriceMax == 0 && rest != null && itemType == null) {
			searchItemDto.setItems(itemsFromRest);
			return searchItemDto;
		}
		if (itemName.isEmpty() && itemPriceMax == 0 && rest == null && itemType != null) {
			searchItemDto.setItems(itemTypes);
			return searchItemDto;
		}
		if (!itemName.isEmpty() && itemPriceMax != 0 && rest != null && itemType != null) {
			searchItemDto.setItems(intersect(itemNames, intersect(itemPrices, intersect(itemsFromRest, itemTypes))));
			return searchItemDto;
		}
		
		return searchItemDto;
		
	}
	public List<Item> intersect( List<Item> list1, List<Item> list2){
		List<Item> retList = new ArrayList<Item>();
		
		for (Item rest : list1) {
			if (list2.remove(rest)) {
				retList.add(rest);
			}
		}
		return retList;
	}
}
