package beans;

import java.util.List;

import enums.RestCateg;

public class Restaurant {

	private String id;
	private String name;
	private String addressa;
	private RestCateg restCateg;
	private List<Item> meals;
	private List<Item> drinks;
	private boolean deleted;
	
}
