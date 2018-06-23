package beans;

import java.util.List;

import enums.OrderStatus;

public class Order {
	
	private String id;
	private List<OrderItem> odredItems;
	private User buyer;
	private User delivery;
	private String orderDate;
	private String orderTime;
	private OrderStatus orderStatus;
	private String note;
	private boolean deleted;
	
	public Order() {
		super();
	}

	public Order(String id, List<OrderItem> odredItems, User buyer, User delivery, String orderDate, String orderTime,
			OrderStatus orderStatus, String note, boolean deleted) {
		super();
		this.id = id;
		this.odredItems = odredItems;
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

	public List<OrderItem> getOdredItems() {
		return odredItems;
	}

	public void setOdredItems(List<OrderItem> odredItems) {
		this.odredItems = odredItems;
	}

	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}

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

	@Override
	public String toString() {
		return "Order [id=" + id + ", odredItems=" + odredItems + ", buyer=" + buyer + ", delivery=" + delivery
				+ ", orderDate=" + orderDate + ", orderTime=" + orderTime + ", orderStatus=" + orderStatus + ", note="
				+ note + ", deleted=" + deleted + "]";
	}
	
	
}
