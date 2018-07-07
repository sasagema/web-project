package beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import enums.OrderStatus;

public class Order {
	
	private String id;
	private List<OrderItem> orderedItems;
	@JsonIgnore
	private User buyer;
	@JsonIgnore
	private User delivery;
	private String buyerId;
	private String buyerUsername;
	private String deliveryId;
	private String deliveryUsername;
	private String orderDate;
	private String orderTime;
	private OrderStatus orderStatus;
	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerUsername() {
		return buyerUsername;
	}

	public void setBuyerUsername(String buyerUsername) {
		this.buyerUsername = buyerUsername;
	}

	private String address;
	private String note;
	private boolean deleted;
	private double totalPrice;
	private double discount;
	public Order() {
		super();
	}

	public Order(String id, List<OrderItem> orderedItems, User buyer, User delivery, String orderDate, String orderTime,
			OrderStatus orderStatus, String note, boolean deleted) {
		super();
		this.id = id;
		this.orderedItems = orderedItems;
		this.buyer = buyer;
		this.delivery = delivery;
		this.orderDate = orderDate;
		this.orderTime = orderTime;
		this.orderStatus = orderStatus;
		this.note = note;
		this.deleted = deleted;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<OrderItem> getOrderedItems() {
		return orderedItems;
	}

	public void setOdredItems(List<OrderItem> odredItems) {
		this.orderedItems = odredItems;
	}
	@JsonIgnore
	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}
	@JsonIgnore
	public User getDelivery() {
		return delivery;
	}

	public void setDelivery(User delivery) {
		this.delivery = delivery;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}



	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}



	public String getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getDeliveryUsername() {
		return deliveryUsername;
	}

	public void setDeliveryUsername(String deliveryUsername) {
		this.deliveryUsername = deliveryUsername;
	}

	public void setOrderedItems(List<OrderItem> orderedItems) {
		this.orderedItems = orderedItems;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderedItems=" + orderedItems + ", buyer=" + buyer + ", delivery=" + delivery
				+ ", buyerId=" + buyerId + ", buyerUsername=" + buyerUsername + ", deliveryId=" + deliveryId
				+ ", deliveryUsername=" + deliveryUsername + ", orderDate=" + orderDate + ", orderTime=" + orderTime
				+ ", orderStatus=" + orderStatus + ", address=" + address + ", note=" + note + ", deleted=" + deleted
				+ ", totalPrice=" + totalPrice + ", discount=" + discount + "]";
	}


	
	
}
