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
import beans.Restaurant;
import beans.User;
import enums.ItemType;


public class RestaurantDao {

	private HashMap<String, Restaurant> restaurants = new HashMap<>();
	private String contextPath;
	
	public RestaurantDao() {
		
	}
	public RestaurantDao(String contextPath) {
		loadRestaurants(contextPath);
		this.contextPath = contextPath;
	}
	
	public void loadRestaurants(String contextPath) {
		BufferedReader in = null;
		ObjectMapper mapper = new ObjectMapper();
		List<Restaurant> restorFromFile = new ArrayList<>();
		try {
			File file = new File (contextPath + "/restorani.json");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));

			if (in != null) {
				restorFromFile = mapper.readValue(in, new TypeReference<List<Restaurant>>(){});
				restaurants.clear();
				
				for (Restaurant rest : restorFromFile) {
					restaurants.put(rest.getId(), rest);
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
	
	public void saveRestaurants() {
		ObjectMapper mapper = new ObjectMapper();
		List<Restaurant> restList = new ArrayList<Restaurant>();
		restList.addAll(restaurants.values());
		System.out.println("Restorani "+restList);

		try {
			File file = new File (this.contextPath + "/restorani.json");
			System.out.println(file.getCanonicalPath());
			mapper.writerWithDefaultPrettyPrinter().writeValue(file , restList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//vrace sve restorane koji nisu obrisani
	public List<Restaurant> getAll(){
		List<Restaurant> list = new ArrayList<>();
		for (Restaurant restaurant : restaurants.values()) {
			if (!restaurant.isDeleted()) {
				list.add(restaurant);
			}
		}		
		return list;
	}
	public Restaurant addRestaurant(Restaurant rest) {
		
		Integer maxId = 0;
		for (String id : restaurants.keySet()) {
			int idNum =Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		rest.setId(maxId.toString());
		rest.setDrinks(new ArrayList<Item>());
		rest.setDeleted(false);
		rest.setMeals(new ArrayList<Item>());
		System.out.println(rest);
		restaurants.put(rest.getId(), rest);
		System.out.println("Dodao ga je");
		System.out.println(restaurants);

		saveRestaurants();
		return restaurants.get(rest.getId());
	}
	public boolean deleteRestaurant(Restaurant rest) {
		Restaurant forDelete = restaurants.get(rest.getId());
		if(forDelete != null && !forDelete.isDeleted()) {
			forDelete.setDeleted(true);
			saveRestaurants();
			return true;
		}else
			return false;
	}
	public Restaurant editRestaurant(Restaurant rest) {
		Restaurant forEdit = restaurants.get(rest.getId());
		if (forEdit != null && !forEdit.isDeleted()) {
			forEdit.setAddress(rest.getAddress());
			forEdit.setName(rest.getName());
			forEdit.setRestCateg(rest.getRestCateg());
			restaurants.put(forEdit.getId(), forEdit);
			
			saveRestaurants();
			return forEdit;
			
		} else {
			return null;
		}
	}
	public Restaurant addItemToRest(Item item) {
		Restaurant rest = restaurants.get(item.getRestaurantId()); 
		if (rest != null) {
			if (item.getItemType() == ItemType.DRINK) {
				rest.getDrinks().add(item);
			}else
				if (item.getItemType() == ItemType.MEAL) {
					rest.getMeals().add(item);
				}
			restaurants.replace(rest.getId(), rest);
			saveRestaurants();
			return rest;
		}
		return null;
	}
	public Restaurant getById(String id) {
		System.out.println(restaurants);
		System.out.println("trazi se " +id+"id");
		System.out.println(restaurants.containsKey(id));
		Restaurant rest = restaurants.containsKey(id) ? restaurants.get(id) : null;
		if (rest != null && !rest.isDeleted()) {
			return rest;
		} else {
			return null;
		}
	}
	public Restaurant findByName(String name) {
		
		for (Restaurant r : restaurants.values()) {
			if (r.getName().equals(name)  && !r.isDeleted()) {
				return r;
			}
		}
		return null;
	}
}
