package dto;

import java.util.List;

import beans.OrderItem;

public class OrderDto {

	private String id;	
	private List<OrderItem> orderItems;
	private int points;
	private String note;
	private String date;
	private String buyerId;
	private String deliverId;
	private String address;
	private double price;
 
 
 
	public OrderDto() {
		super();
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public List<OrderItem> getOrderItems() {
		return orderItems;
	}



	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}



	public int getPoints() {
		return points;
	}



	public void setPoints(int points) {
		this.points = points;
	}



	public String getNote() {
		return note;
	}



	public void setNote(String note) {
		this.note = note;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getBuyerId() {
		return buyerId;
	}



	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}



	public String getDeliverId() {
		return deliverId;
	}



	public void setDeliverId(String deliverId) {
		this.deliverId = deliverId;
	}



	public double getPrice() {
		return price;
	}



	public void setPrice(double price) {
		this.price = price;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
	
	
	 
 
}
