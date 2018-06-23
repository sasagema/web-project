package beans;

import java.io.Serializable;
import java.util.List;

import enums.Role;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
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
	
	
	private List<Order> orders;
	private List<Vehicle> vehicles;	
	private List<Restaurant> favoriteRest;
	

	
	
	
}
