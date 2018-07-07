package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.NEW;

import beans.Order;
import beans.User;
import beans.Vehicle;
import enums.OrderStatus;
import beans.Order;

public class OrderDao {

	private HashMap<String, Order> orders = new HashMap<>();
	private String contextPath;

	public OrderDao() {
	
	}
	public OrderDao(String contextPath) {
		this.contextPath = contextPath;
		loadOrders(contextPath);
	}
	
	public void loadOrders(String contextPath) {
		BufferedReader in = null;
		ObjectMapper mapper = new ObjectMapper();
		List<Order> ordersFromFile = new ArrayList<>();
		try {
			File file = new File (contextPath + "/porudzbine.json");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));

			if (in != null) {
				ordersFromFile = mapper.readValue(in, new TypeReference<List<Order>>(){});
				orders.clear();
				
				for (Order order : ordersFromFile) {
					orders.put(order.getId(), order);
				}
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
	}
	
	public void saveOrders() {
		ObjectMapper mapper = new ObjectMapper();
		List<Order> orderList = new ArrayList<Order>();
		orderList.addAll(orders.values());
		System.out.println("Porudzbine "+orderList);

		try {
			File file = new File (this.contextPath + "/porudzbine.json");
			System.out.println(file.getCanonicalPath());
			mapper.writerWithDefaultPrettyPrinter().writeValue(file , orderList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public List<Order> getAll(){
		List<Order> list = new ArrayList<Order>();
		for (Order order : orders.values()) {
			if (!order.isDeleted()) {
				list.add(order);
			}
		}		
		return list;
	}
	public List<Order> findOpenOrders(){
		List<Order> list = new ArrayList<>();
		List<Order> retList = new ArrayList<>();
		list = getAll();
		
		for (Order order : list) {
			if (order.getDeliveryId() == null && order.getOrderStatus() == OrderStatus.Poruceno) {
				retList.add(order);
			}
		}
		return retList;
	}
	public Order addOrder(Order order) {
		
		Integer maxId = 0;
		for (String id : orders.keySet()) {
			int idNum =Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		order.setId(maxId.toString());
		order.setDeleted(false);
		orders.put(order.getId(), order);

		if (orders.get(order.getId()) == null) {
			return null;
		}
		saveOrders();
		return orders.get(order.getId());
	}
	public Order findById(String id) {

		Order order =  orders.containsKey(id) ? orders.get(id) : null;
		if (order != null && !order.isDeleted()) {
			return order;
		} else {
			return null;
		}
	}
	public Order addDeliveryToOrder(User user,  String orderId) {

		Order order =  findById(orderId);
		if (order != null && order.getOrderStatus() == OrderStatus.Poruceno) {
			order.setDeliveryId(user.getId());
			order.setDeliveryUsername(user.getUsername());
			order.setOrderStatus(OrderStatus.Dostava_u_toku);
			saveOrders();
			return order;
		} else {
			return null;
		}
	}
}
