package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Item;
import beans.Order;
import beans.OrderItem;
import beans.User;
import beans.Vehicle;
import dao.ItemDao;
import dao.OrderDao;
import dao.UserDao;
import dao.VehicleDao;
import enums.OrderStatus;
import enums.Role;
import enums.VehicleStatus;

@Path("/delivery")
public class DeliveryService {

	
	@Context
	ServletContext context;
	
	@PostConstruct
	public void init() {
		
		if (context.getAttribute("OrderDao") == null) {
			String contextPath = context.getRealPath("/");
			context.setAttribute("OrderDao", new OrderDao(contextPath));
		}
		if (context.getAttribute("userDao") == null) {
			String contextPath = context.getRealPath("/");
			context.setAttribute("userDao", new UserDao(contextPath));
		}
		if (context.getAttribute("ItemDao") == null) {
			String contextPath = context.getRealPath("/");
			context.setAttribute("ItemDao", new ItemDao(contextPath));
		}
		if (context.getAttribute("VehicleDao") == null) {
			String contextPath = context.getRealPath("/");
			context.setAttribute("VehicleDao", new VehicleDao(contextPath));
		}
		
	}
	
	@POST
	@Path("/accept/{orderId}/{vehicleId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response acceptDelivery(@PathParam("orderId") String orderId, @PathParam("vehicleId") String vehicleId,  @Context HttpServletRequest request) {
		
		UserDao userDao = (UserDao) context.getAttribute("userDao");
		ItemDao itemDao = (ItemDao) context.getAttribute("ItemDao");
		OrderDao orderDao = (OrderDao) context.getAttribute("OrderDao");
		VehicleDao vehicleDao = (VehicleDao) context.getAttribute("VehicleDao");

		User user = (User) request.getSession().getAttribute("user");
		Vehicle vehicle = vehicleDao.getById(vehicleId);
		Order order = orderDao.findById(orderId);
		System.out.println("order" + order);
		System.out.println("vehicle" + vehicle);
		System.out.println("user" + user);
		if (user == null || order == null || vehicle == null || user.getRole() != Role.Dostavljac) {
			return Response.status(400).build();
		}
		for (Order o : user.getDeliveryOrders()) {
			if (o.getOrderStatus() == OrderStatus.Dostava_u_toku) {
				return Response.status(400).entity("Imate dostave koje su u toku.").build();
			}
		}
		orderDao.addDeliveryToOrder(user, order.getId());
		userDao.addOrderToDelivery(user.getId(), order);
		vehicleDao.addToDelivery(vehicleId, user);
		userDao.addDeliveryVehicle(user.getId(), vehicle);
		
		return Response.ok(order).build();
	}
	@POST
	@Path("/complete/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response acceptDelivery(@PathParam("orderId") String orderId, @Context HttpServletRequest request) {
		
		UserDao userDao = (UserDao) context.getAttribute("userDao");
		ItemDao itemDao = (ItemDao) context.getAttribute("ItemDao");
		OrderDao orderDao = (OrderDao) context.getAttribute("OrderDao");
		VehicleDao vehicleDao = (VehicleDao) context.getAttribute("VehicleDao");

		User user = (User) request.getSession().getAttribute("user");
		System.out.println("Da li su isti useri"+(user == userDao.getById(user.getId())));
		Order order = orderDao.findById(orderId);
		if (user == null || order == null || user.getRole() != Role.Dostavljac) {
		return Response.status(400).build();
		}
		Vehicle vehicle = vehicleDao.getById(user.getVehicle().getId());
		vehicle.setOccupiedById(null);
		vehicle.setOccupiedByUsername(null);
		vehicle.setStatus(VehicleStatus.Slobodan);
		user.setVehicle(null);
		User buyer = userDao.getById(order.getBuyerId());
		if (order.getTotalPrice() > 500) {
			int points = buyer.getDiscountPoints();
			buyer.setDiscountPoints(points + 1);
		}
		List<OrderItem> orderedItems = order.getOrderedItems();
		order.setOrderStatus(OrderStatus.Dostavljeno);
		for (OrderItem oi : orderedItems) {
			Item item = itemDao.getById(oi.getItem().getId());
			int orderNum = item.getOrdersCounter();
			item.setOrdersCounter(orderNum + 1);
		}
		userDao.saveUsers();
		orderDao.saveOrders();
		vehicleDao.saveVehicles();
		itemDao.saveItems();
		return Response.ok(order).build();
	}
}
