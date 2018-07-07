package beans;

import java.io.Serializable;
import java.util.List;

import enums.Role;

public class User  {


	
	private String id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String telephone;
	private String email;
	private String registrDate;
	private Role role;
	private boolean deleted;
	private int discountPoints;
	private Vehicle vehicle;
	
	private List<Order> orders;
	private List<Vehicle> vehicles;	
	private List<Restaurant> favoriteRest;
	private List<Order> deliveryOrders;
	
	public User() {
		super();
	}

	public User(String id, String firstName, String lastName, String username, String password, String telephone,
			String email, String registrDate, Role role, boolean deleted, int discountPoints, List<Order> orders,
			List<Vehicle> vehicles, List<Restaurant> favoriteRest) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.telephone = telephone;
		this.email = email;
		this.registrDate = registrDate;
		this.role = role;
		this.deleted = deleted;
		this.discountPoints = discountPoints;
		this.orders = orders;
		this.vehicles = vehicles;
		this.favoriteRest = favoriteRest;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegistrDate() {
		return registrDate;
	}

	public void setRegistrDate(String registrDate) {
		this.registrDate = registrDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int getDiscountPoints() {
		return discountPoints;
	}

	public void setDiscountPoints(int discountPoints) {
		this.discountPoints = discountPoints;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public List<Restaurant> getFavoriteRest() {
		return favoriteRest;
	}

	public void setFavoriteRest(List<Restaurant> favoriteRest) {
		this.favoriteRest = favoriteRest;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
				+ ", password=" + password + ", telephone=" + telephone + ", email=" + email + ", registrDate="
				+ registrDate + ", role=" + role + ", deleted=" + deleted + ", discountPoints=" + discountPoints
				+ ", orders=" + orders + ", vehicles=" + vehicles + ", favoriteRest=" + favoriteRest + "]";
	}

	public List<Order> getDeliveryOrders() {
		return deliveryOrders;
	}

	public void setDeliveryOrders(List<Order> deliveryOders) {
		this.deliveryOrders = deliveryOders;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	
	
	
	
}
