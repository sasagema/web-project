package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Item;
import beans.Order;
import beans.Restaurant;
import beans.User;
import beans.Vehicle;

public class UserDao {
	
	private HashMap<String, User> users = new HashMap<String, User>();
	private String contextPath;
	public UserDao() {
		super();
	}
	
	public UserDao(String contextPath) {
		loadUsers(contextPath);
		this.contextPath = contextPath;
	}

	public void loadUsers(String contextPath) {
		BufferedReader in = null;
		ObjectMapper mapper = new ObjectMapper();
		List<User> usersFromFile = new ArrayList<>();
		try {
			File file = new File (contextPath + "/korisnici.json");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));

			if (in != null) {
				usersFromFile = mapper.readValue(in, new TypeReference<List<User>>(){});
				users.clear();
				
				for (User user : usersFromFile) {
					users.put(user.getId(), user);
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
	
	public void saveUsers() {
		BufferedWriter out = null;
		ObjectMapper mapper = new ObjectMapper();
		List<User> userList = new ArrayList<User>();
		userList.addAll(users.values());
		System.out.println("Korisnici "+userList);

		try {
			File file = new File (this.contextPath + "/korisnici.json");
			System.out.println(file.getCanonicalPath());

			mapper.writerWithDefaultPrettyPrinter().writeValue(file , userList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public User addUser(User user) {
		Integer maxId = 0;
		for (String id : users.keySet()) {
			int idNum =Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		user.setFavoriteRest(new ArrayList<>());
		user.setOrders(new ArrayList<>());
		user.setFavoriteRest(new ArrayList<>());
		user.setVehicles(new ArrayList<>());
		user.setDeliveryOrders(new ArrayList<>());
		user.setId(maxId.toString());
		users.put(user.getId(), user);
		saveUsers();
		return user;
	}
	public List<User> getAll(){
		
		List<User> list = new ArrayList<>();
		list.addAll(users.values());
		return list;
	}
	public User getById(String id) {
		User user = users.get(id);
		if (user != null && !user.isDeleted()) {
			return user;
		} else {
			return null;
		}
		
	}
	public User registerUser(User user) {
		List<User> userList = new ArrayList<>();
		userList.addAll(users.values());
		
		for (User u : userList) {
			
			if (u.getEmail().toLowerCase().equals(user.getEmail().toLowerCase()) || u.getUsername().toLowerCase().equals(user.getUsername().toLowerCase())) 
				return null;
			
							
		}
		return addUser(user);
	}
	public User getByUsername(String username) {
		for (User user : users.values()) {
			
			if (user.getUsername().equals(username)) {				
				
				return user;
			
			}
		}
		return null;
		
	}

	public User editUser(User user) {
		System.out.println(users.containsKey(user.getId()));
		User forEdit = users.containsKey(user.getId()) ? users.get(user.getId()) : null;
		if (forEdit != null) {
			forEdit.setFirstName(user.getFirstName());
			forEdit.setLastName(user.getLastName());
			forEdit.setRole(user.getRole());
			users.put(forEdit.getId(), forEdit);
			saveUsers();
			user.setEmail(forEdit.getEmail());
			user.setTelephone(forEdit.getTelephone());
			user.setUsername(forEdit.getUsername());
			user.setDiscountPoints(forEdit.getDiscountPoints());
			user.setRegistrDate(forEdit.getRegistrDate());
			return user;
		} else {
			return null;
		}
		
	}
	public boolean deleteUser(User user) {
		User forDelete = users.get(user.getId());
		if(forDelete != null && !forDelete.isDeleted()) {
			forDelete.setDeleted(true);
			saveUsers();
			return true;
		}else
			return false;
	}

	public User addToFavorite(User user, Restaurant rest) {
		System.out.println(users.containsKey(user.getId()));
		User userToAdd = users.get(user.getId());
		
		for (Restaurant r : userToAdd.getFavoriteRest()) {
			if (r.getId().equals(rest.getId())) {
				return null;
			}
		}

		userToAdd.getFavoriteRest().add(rest);
		saveUsers();
		return userToAdd;
	}
	public User removeFromFavorite(User user, Restaurant rest) {
		System.out.println(users.containsKey(user.getId()));
		User userToAdd = users.get(user.getId());
		
		for (Restaurant r : userToAdd.getFavoriteRest()) {
			if (r.getId().equals(rest.getId())) {
				int index = userToAdd.getFavoriteRest().indexOf(r);
				userToAdd.getFavoriteRest().remove(index);
				saveUsers();
				return userToAdd;
			}
		}
		return null;
	}
	public boolean addOrder(String id, Order order) {
		User user = users.get(id);
		if(user != null && !user.isDeleted()) {
			user.getOrders().add(order);
			saveUsers();
			return true;
		}else
			return false;
	}
	public boolean addDiscountPoints(String id, int points) {
		User user = users.get(id);
		if(user != null && !user.isDeleted() && user.getDiscountPoints() < 10) {
			int discPoints = user.getDiscountPoints();
			user.setDiscountPoints(discPoints + points); 
			saveUsers();
			return true;
		}else
			return false;
	}
	public boolean removeDiscountPoints(String id, int points) {
		User user = users.get(id);
		if(user != null && !user.isDeleted()) {
			int discPoints = user.getDiscountPoints();
			if (discPoints < points) {
				return false;
			}
			user.setDiscountPoints(discPoints - points); 
			saveUsers();
			return true;
		}else
			return false;
	}
	
	public User addOrderToDelivery(String userId, Order order) {
		User user = getById(userId);
		if (user != null ) {
			user.getDeliveryOrders().add(order);
			saveUsers();
			return user;
		} else {
			return null;
		}
	}
	public User addDeliveryVehicle(String userId, Vehicle vehicle) {
		User user = getById(userId);
		if (user != null || vehicle == null) {
			user.setVehicle(vehicle);
			saveUsers();
			return user;
		} else {
			return null;
		}
	}
}
