package dto;

import java.util.List;

import beans.Restaurant;
import enums.RestCateg;

public class SearchRestDto {

	private String searchRestId;
	private String restName;
	private String restAddress;
	private int restCateg;
	
	private List<Restaurant> restaurants;

	public SearchRestDto() {
		super();
	}

	public String getSearchRestId() {
		return searchRestId;
	}

	public void setSearchRestId(String searchRestId) {
		this.searchRestId = searchRestId;
	}

	public String getRestName() {
		return restName;
	}

	public void setRestName(String restName) {
		this.restName = restName;
	}

	public String getRestAddress() {
		return restAddress;
	}

	public void setRestAddress(String restAddress) {
		this.restAddress = restAddress;
	}

	public int getRestCateg() {
		return restCateg;
	}

	public void setRestCateg(int restCateg) {
		this.restCateg = restCateg;
	}

	public List<Restaurant> getRestaurants() {
		return restaurants;
	}

	public void setRestaurants(List<Restaurant> restaurants) {
		this.restaurants = restaurants;
	}
	
	
	
}
