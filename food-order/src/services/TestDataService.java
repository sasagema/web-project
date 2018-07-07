/*package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.catalina.deploy.ContextService;

import beans.Item;
import beans.Restaurant;
import beans.User;
import beans.Vehicle;
import dao.ItemDao;
import dao.RestaurantDao;
import dao.UserDao;
import dao.VehicleDao;
import enums.ItemType;
import enums.RestCateg;
import enums.Role;
import enums.VehicleType;

@Path("/testdata")
public class TestDataService {

	@Context
	ServletContext context;
	
	@PostConstruct
	public void init() {
		if (context.getAttribute("RestaurantDao") == null) {
			String contextPath = context.getRealPath("/");
			context.setAttribute("RestaurantDao", new RestaurantDao(contextPath));
			
		}
		if (context.getAttribute("ItemDao") == null) {
			String contextPath = context.getRealPath("/");
			context.setAttribute("ItemDao", new ItemDao(contextPath));
		}
		if (context.getAttribute("VehicleDao") == null) {
			String contextPath = context.getRealPath("/");
			context.setAttribute("VehicleDao", new VehicleDao(contextPath));
		}
		if (context.getAttribute("UserDao") == null) {
			String contextPath = context.getRealPath("/");
			context.setAttribute("UserDao", new UserDao(contextPath));
		}
		
	}
	
	public Response addTestData(@Context HttpServletRequest request) {
		
		UserDao userDao = (UserDao) context.getAttribute("UserDao");
		RestaurantDao restaurantDao = (RestaurantDao) context.getAttribute("RestaurantDao");
		ItemDao itemDao = (ItemDao) context.getAttribute("ItemDao");
		VehicleDao vehicleDao = (VehicleDao) context.getAttribute("VehicleDao");
		
		//User pera je ADMIN
		User user1 = new User();
		user1.setFirstName("Pera");
		user1.setLastName("Peric");
		user1.setEmail("pera@pera.com");
		user1.setUsername("pera");
		user1.setPassword("pera");
		user1.setRegistrDate("12/3/2011");
		user1.setTelephone("061123123");
		user1.setRole(Role.ADMIN);
		user1.setDeleted(false);
		
		//User mika je USER tj. kupac
		User user2 = new User();
		user2.setFirstName("Mika");
		user2.setLastName("Mikic");
		user2.setEmail("mika@mika.com");
		user2.setUsername("mika");
		user2.setPassword("mika");
		user2.setRegistrDate("12/3/2014");
		user2.setTelephone("061143223");
		user2.setRole(Role.USER);
		user2.setDeleted(false);
		
		//User zika je Dostavljac 
		User user3 = new User();
		user3.setFirstName("Zika");
		user3.setLastName("Zikic");
		user3.setEmail("zika@zika.com");
		user3.setUsername("zika");
		user3.setPassword("zika");
		user3.setRegistrDate("12/3/2016");
		user3.setTelephone("061143000");
		user3.setRole(Role.DELIVERY);
		user3.setDeleted(false);
		
		Restaurant rest1 = new Restaurant();
		rest1.setName("Kukuriku Novi Sad");
		rest1.setAddress("Bul. Despota Stefana 5");
		rest1.setRestCateg(RestCateg.Domaca_kuhinja);
		
		Restaurant rest2 = new Restaurant();
		rest2.setName("Lazin salas");
		rest2.setAddress("Laze Teleckog 5");
		rest2.setRestCateg(RestCateg.Domaca_kuhinja);
		
		Restaurant rest3 = new Restaurant();
		rest3.setName("Big Pizza");
		rest3.setAddress("Jovana Subotica 18");
		rest3.setRestCateg(RestCateg.Picerija);
		
		Restaurant rest4 = new Restaurant();
		rest4.setName("Gojko's Pizza");
		rest4.setAddress("Bul. Slobodana Jovanovica 12");
		rest4.setRestCateg(RestCateg.Picerija);
		
		Restaurant rest5 = new Restaurant();
		rest5.setName("QiQi");
		rest5.setAddress("Ognjena Price 5");
		rest5.setRestCateg(RestCateg.Kineski_restoran);
		
		Restaurant rest6 = new Restaurant();
		rest6.setName("Kineski zmaj");
		rest6.setAddress("Janka Veselinovica 29");
		rest6.setRestCateg(RestCateg.Kineski_restoran);
		
		Restaurant rest7 = new Restaurant();
		rest7.setName("Real India ");
		rest7.setAddress("Ulica Modene bb");
		rest7.setRestCateg(RestCateg.Indijski_restoran);
		
		Restaurant rest8 = new Restaurant();
		rest8.setName("Surabaya");
		rest8.setAddress("Primorska 26");
		rest8.setRestCateg(RestCateg.Indijski_restoran);
		
		Restaurant rest9 = new Restaurant();
		rest9.setName("Rostilj Dva Kneza");
		rest9.setAddress("Futoska 1");
		rest9.setRestCateg(RestCateg.Rostilj);
		
		Restaurant rest10 = new Restaurant();
		rest10.setName("Rostiljnica Obelix");
		rest10.setAddress("Futoski put 59");
		rest10.setRestCateg(RestCateg.Rostilj);
		
		Restaurant rest11 = new Restaurant();
		rest11.setName("Dizni");
		rest11.setAddress("Petra Drapsina 59");
		rest11.setRestCateg(RestCateg.Poslasticarnica);

		Restaurant rest12 = new Restaurant();
		rest12.setName("Vremeplov");
		rest12.setAddress("Bul. Oslobodjenja 59");
		rest12.setRestCateg(RestCateg.Poslasticarnica);
		
		Vehicle vehicle1 = new Vehicle();
		vehicle1.setBrand("Fiat");
		vehicle1.setModel("Punto");
		vehicle1.setNumberPlate("NS123NN");
		vehicle1.setProductionYear(2002);
		vehicle1.setNote("");
		vehicle1.setVehicleType(VehicleType.CAR);
		
		Vehicle vehicle2 = new Vehicle();
		vehicle2.setBrand("Opel");
		vehicle2.setModel("Corsa");
		vehicle2.setNumberPlate("NS321DD");
		vehicle2.setProductionYear(2010);
		vehicle2.setNote("");
		vehicle2.setVehicleType(VehicleType.CAR);
		
		Vehicle vehicle3 = new Vehicle();
		vehicle3.setBrand("Cross");
		vehicle3.setModel("Picnic");
		vehicle3.setNumberPlate("Zenski bicikl");
		vehicle3.setProductionYear(2016);
		vehicle3.setNote("");
		vehicle3.setVehicleType(VehicleType.BIKE);
		
		Vehicle vehicle4 = new Vehicle();
		vehicle4.setBrand("Cross");
		vehicle4.setModel("Urban");
		vehicle4.setNumberPlate("");
		vehicle4.setProductionYear(2017);
		vehicle4.setNote("Muski bicikl");
		vehicle4.setVehicleType(VehicleType.BIKE);
		
		Vehicle vehicle5 = new Vehicle();
		vehicle5.setBrand("Piaggio");
		vehicle5.setModel("Free");
		vehicle5.setNumberPlate("NS12312");
		vehicle5.setProductionYear(2000);
		vehicle5.setNote("Ima veliki sanduk.");
		vehicle5.setVehicleType(VehicleType.MOPED);
		
		Vehicle vehicle6 = new Vehicle();
		vehicle6.setBrand("Vespa");
		vehicle6.setModel("Elettrica ");
		vehicle6.setNumberPlate("NS32133");
		vehicle6.setProductionYear(2018);
		vehicle6.setNote("Skuter na elektricni pogon");
		vehicle6.setVehicleType(VehicleType.MOPED);
		
		Item item1 = new Item();
		item1.setName("Piletina sa kikirikijem");
		item1.setDescription("Piletina na tradicionalan nacin");
		item1.setPrice(400.0);
		item1.setItemType(ItemType.MEAL);
		item1.setQuantityGrams(300);

		Item item2 = new Item();
		item2.setName("Svinjetina sa kikirikijem");
		item2.setDescription("Svinjetina na tradicionalan nacin");
		item2.setPrice(500.0);
		item2.setItemType(ItemType.MEAL);
		item2.setQuantityGrams(300);
	
		Item item3 = new Item();
		item3.setName("Capriciossa");
		item3.setDescription("Velika porodicna");
		item3.setPrice(790.0);
		item3.setItemType(ItemType.MEAL);
		item3.setQuantityGrams(500);
		item3.setOrdersCounter(5);
		
		Item item4 = new Item();
		item4.setName("Margarita");
		item4.setDescription("Velika porodicna");
		item4.setPrice(800.0);
		item4.setItemType(ItemType.MEAL);
		item4.setQuantityGrams(500);
	
		Item item5 = new Item();
		item5.setName("Sarma");
		item5.setDescription("Velika porcija");
		item5.setPrice(500.0);
		item5.setItemType(ItemType.MEAL);
		item5.setQuantityGrams(500);
	
		Item item6 = new Item();
		item6.setName("Musaka");
		item6.setDescription("Kajmak gratis");
		item6.setPrice(300.0);
		item6.setItemType(ItemType.MEAL);
		item6.setQuantityGrams(400);
	
		Item item7 = new Item();
		item7.setName("Velika pljeskavica");
		item7.setDescription("Govedina");
		item7.setPrice(200.0);
		item7.setItemType(ItemType.MEAL);
		item7.setQuantityGrams(250);;
		
		Item item13 = new Item();
		item13.setName("Gurmanska pljeskavica");
		item13.setDescription("Govedina, slanina");
		item13.setPrice(250.0);
		item13.setItemType(ItemType.MEAL);
		item13.setQuantityGrams(200);;
		
		Item item8 = new Item();
		item8.setName("Coca-Cola");
		item8.setDescription("Gazirani sok");
		item8.setPrice(150.0);
		item8.setItemType(ItemType.DRINK);
		item8.setQuantityMl(330);
		item8.setOrdersCounter(10);
		
		Item item9 = new Item();
		item9.setName("Rosa");
		item9.setDescription("Negazirana voda");
		item9.setPrice(100.0);
		item9.setItemType(ItemType.DRINK);
		item9.setQuantityMl(500);
	
		Item item10 = new Item();
		item10.setName("Knjaz Milos");
		item10.setDescription("Mineralna voda");
		item10.setPrice(130.0);
		item10.setItemType(ItemType.DRINK);
		item10.setQuantityMl(330);
	
		Item item11 = new Item();
		item11.setName("Fanta");
		item11.setDescription("Gazirani sok");
		item11.setPrice(150.0);
		item11.setItemType(ItemType.DRINK);
		item11.setQuantityMl(330);
	
		Item item12 = new Item();
		item12.setName("Djus");
		item12.setDescription("Sveze cedjen");
		item12.setPrice(200.0);
		item12.setItemType(ItemType.DRINK);
		item12.setQuantityMl(500);
		
		
		rest1.setMeals(new ArrayList<Item>());
		rest1.setDrinks(new ArrayList<Item>());
		rest1.getMeals().add(item5);
		rest1.getMeals().add(item6);
		rest1.getDrinks().add(item8);
		rest1.getDrinks().add(item9);
		rest1.getDrinks().add(item10);
		rest1.getDrinks().add(item11);
		rest1.getDrinks().add(item12);
		
		rest2.setMeals(new ArrayList<Item>());
		rest2.setDrinks(new ArrayList<Item>());
		rest2.getMeals().add(item5);
		rest2.getDrinks().add(item8);
		rest2.getDrinks().add(item9);
		rest2.getDrinks().add(item10);
		rest2.getDrinks().add(item11);
		rest2.getDrinks().add(item12);
		
		rest3.setMeals(new ArrayList<Item>());
		rest3.setDrinks(new ArrayList<Item>());
		rest3.getMeals().add(item3);
		rest3.getMeals().add(item4);
		rest3.getDrinks().add(item8);
		rest3.getDrinks().add(item9);
		rest3.getDrinks().add(item10);
		rest3.getDrinks().add(item11);
		rest3.getDrinks().add(item12);
		
		rest4.setMeals(new ArrayList<Item>());
		rest4.setDrinks(new ArrayList<Item>());
		rest4.getMeals().add(item3);
		rest4.getDrinks().add(item8);
		rest4.getDrinks().add(item9);
		rest4.getDrinks().add(item10);
		rest4.getDrinks().add(item11);
		rest4.getDrinks().add(item12);
	
		rest5.setMeals(new ArrayList<Item>());
		rest5.setDrinks(new ArrayList<Item>());
		rest5.getMeals().add(item1);
		rest5.getMeals().add(item2);
		rest5.getDrinks().add(item8);
		rest5.getDrinks().add(item9);
		rest5.getDrinks().add(item10);
		rest5.getDrinks().add(item11);
		rest5.getDrinks().add(item12);
		
		rest6.setMeals(new ArrayList<Item>());
		rest6.setDrinks(new ArrayList<Item>());
		rest6.getMeals().add(item2);
		rest6.getDrinks().add(item8);
		rest6.getDrinks().add(item9);
		rest6.getDrinks().add(item10);
		rest6.getDrinks().add(item11);
		rest6.getDrinks().add(item12);
		
		rest7.setMeals(new ArrayList<Item>());
		rest7.setDrinks(new ArrayList<Item>());
		rest7.getMeals().add(item5);
		rest7.getMeals().add(item6);
		rest7.getDrinks().add(item8);
		rest7.getDrinks().add(item9);
		rest7.getDrinks().add(item10);
		rest7.getDrinks().add(item11);
		rest7.getDrinks().add(item12);
		
		rest8.setMeals(new ArrayList<Item>());
		rest8.setDrinks(new ArrayList<Item>());
		rest8.getMeals().add(item5);
		rest8.getMeals().add(item6);
		rest8.getDrinks().add(item8);
		rest8.getDrinks().add(item9);
		rest8.getDrinks().add(item10);
		rest8.getDrinks().add(item11);
		rest8.getDrinks().add(item12);
		
		rest9.setMeals(new ArrayList<Item>());
		rest9.setDrinks(new ArrayList<Item>());
		rest9.getMeals().add(item7);
		rest9.getDrinks().add(item8);
		rest9.getDrinks().add(item9);
		rest9.getDrinks().add(item10);
		rest9.getDrinks().add(item11);
		rest9.getDrinks().add(item12);
		
		rest10.setMeals(new ArrayList<Item>());
		rest10.setDrinks(new ArrayList<Item>());
		rest10.getMeals().add(item7);
		rest10.getMeals().add(item13);
		rest10.getDrinks().add(item8);
		rest10.getDrinks().add(item9);

		rest11.setMeals(new ArrayList<Item>());
		rest11.setDrinks(new ArrayList<Item>());
		rest11.getDrinks().add(item8);

		rest12.setMeals(new ArrayList<Item>());
		rest12.setDrinks(new ArrayList<Item>());
		rest12.getDrinks().add(item8);
		
		user2.setFavoriteRest(new ArrayList<>());
		user2.getFavoriteRest().add(rest1);
		user2.getFavoriteRest().add(rest2);
		user2.getFavoriteRest().add(rest3);
		
		return Response.ok().build();
		
	}
}
*/