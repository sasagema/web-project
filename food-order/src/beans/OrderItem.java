package beans;

public class OrderItem {
	
	private String id;
	
	private Item item;
	private int quantity;
	
	public OrderItem() {
		super();
	}
	public OrderItem(String id, Item item, int quantity) {
		super();
		this.id = id;
		this.item = item;
		this.quantity = quantity;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", item=" + item + ", quantity=" + quantity + "]";
	}
	
	
	
}
