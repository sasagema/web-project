package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Item;
import beans.Order;
import beans.OrderItem;
import beans.User;
import dao.ItemDao;
import dao.OrderDao;
import dao.UserDao;
import dto.OrderDto;
import enums.OrderStatus;
import enums.Role;

@Path("/order")
public class OrderService {

	@Context
	ServletContext context;
	
	@PostConstruct
	public void init() {
	
		if (context.getAttribute("OrderDao") == null) {
			String contextPath = context.getRealPath("/");
			System.out.println(contextPath);
			context.setAttribute("OrderDao", new OrderDao(contextPath));
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
	public Response getAll(@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		OrderDao orderDao = (OrderDao) context.getAttribute("OrderDao");
		if (user == null) {
			return Response.status(401).entity("Morate biti prijavljeni.").build();
		}
		return Response.ok(orderDao.getAll()).build();
	}
	@GET
	@Path("/open")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOpenOrders(@Context HttpServletRequest request){
		System.out.println("USAO2");
		User user = (User) request.getSession().getAttribute("user");
		OrderDao orderDao = (OrderDao) context.getAttribute("OrderDao");
		if (user == null) {
			return Response.status(401).entity("Morate biti prijavljeni.").build();
		}
		List<Order> retList = new ArrayList<>();
		retList = orderDao.findOpenOrders();
		System.out.println(retList);
		return Response.ok(orderDao.findOpenOrders()).build();
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOrder(OrderDto orderDto,  @Context HttpServletRequest request) {
		User buyer = (User) request.getSession().getAttribute("user");
		OrderDao orderDao = (OrderDao) context.getAttribute("OrderDao");
		ItemDao itemDao = (ItemDao) context.getAttribute("ItemDao");
		UserDao userDao = (UserDao) context.getAttribute("userDao");
		double totalPrice = 0.0;
		int points = orderDto.getPoints();
		double discount = 0.0;
		if (buyer == null || buyer.getRole() == Role.Dostavljac) {
			return Response.status(401).entity("Morate biti prijavljeni.").build();
		}

		if ( orderDto.getOrderItems().size() <= 0) {
			return Response.status(400).entity("Izaberite stavke porudzbine.").build();
		}
		if (orderDto.getAddress().isEmpty()) {
			return Response.status(400).entity("Unesite adresu isporuke.").build();
		}
		Order order = new Order();
		order.setOdredItems(new ArrayList<OrderItem>());
		for (OrderItem oi : orderDto.getOrderItems()) {
			order.getOrderedItems().add(new OrderItem(oi.getId(), itemDao.getById(oi.getId()), oi.getQuantity()));
			Item item = itemDao.ordered(oi.getId());
			totalPrice += item.getPrice() * oi.getQuantity();
		}
		//order.setBuyer(buyer);
		order.setBuyerId(buyer.getId());
		order.setBuyerUsername(buyer.getUsername());
		order.setAddress(orderDto.getAddress());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date nowDateTime = new Date();
		String date = dateFormat.format(nowDateTime).split(" ")[0];
		String time = dateFormat.format(nowDateTime).split(" ")[1];
		order.setOrderDate(date);
		order.setOrderTime(time);
		order.setNote(orderDto.getNote());
		if (points < 0 || points > 10) {
			return Response.status(201).entity("Doslo je do greske.").build();
		}
		if (points > 0) {
			discount = 1 - (3.0 * points)/100;
			totalPrice = totalPrice * discount;
		}
		order.setDiscount(points * 3);
		order.setTotalPrice(totalPrice);
		order.setOrderStatus(OrderStatus.Poruceno);
		Order retOrder = orderDao.addOrder(order);
		if (retOrder == null) {
			return Response.status(201).entity("Doslo je do greske.").build();
		}
		System.out.println(orderDto);
		System.out.println(order);
		userDao.addOrder(buyer.getId(), retOrder);
		userDao.removeDiscountPoints(buyer.getId(), orderDto.getPoints());
		return Response.ok(retOrder).build();
	}
	
	
}
