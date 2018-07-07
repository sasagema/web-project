package beans;

import java.util.List;

import enums.RestCateg;

public class Restaurant {

	private String id;
	private String name;
	private String address;
	private RestCateg restCateg;
	private List<Item> meals;
	private List<Item> drinks;
	private boolean deleted;
	private int ordersCounter;
	public Restaurant() {
		super();
	}
	public Restaurant(String id, String name, String address, RestCateg restCateg, List<Item> meals, List<Item> drinks,
			boolean deleted) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.restCateg = restCateg;
		this.meals = meals;
		this.drinks = drinks;
		this.deleted = deleted;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public RestCateg getRestCateg() {
		return restCateg;
	}
	public void setRestCateg(RestCateg restCateg) {
		this.restCateg = restCateg;
	}
	public List<Item> getMeals() {
		return meals;
	}
	public void setMeals(List<Item> meals) {
		this.meals = meals;
	}
	public List<Item> getDrinks() {
		return drinks;
	}
	public void setDrinks(List<Item> drinks) {
		this.drinks = drinks;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public int getOrdersCounter() {
		return ordersCounter;
	}
	public void setOrdersCounter(int ordersCounter) {
		this.ordersCounter = ordersCounter;
	}
	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", name=" + name + ", address=" + address + ", restCateg=" + restCateg
				+ ", meals=" + meals + ", drinks=" + drinks + ", deleted=" + deleted + "]";
	}
	
	
	
}
