package beans;

import enums.ItemType;

public class Item {

	private String id;
	private String name;
	private Double price;
	private String description;
	private String restaurantId;
	private String restaurantName;
	private int quantity;
	private int ordersCounter;
	private boolean deleted;
	private ItemType itemType;
	public Item() {
		super();
	}

	public Item(String id, String name, Double price, String description, int quantity,
			boolean deleted, ItemType itemType, String restaurantId) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.description = description;
		this.quantity = quantity;
		this.deleted = deleted;
		this.itemType = itemType;
		this.restaurantId = restaurantId;
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price=" + price + ", description=" + description
				+ ", quantityGrams=" + quantity +  ", ordersCounter=" + ordersCounter
				+ ", deleted=" + deleted + ", itemType=" + itemType + "]";
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	
	
	
	
	
}
