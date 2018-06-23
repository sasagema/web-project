package beans;

public class Item {

	private String id;
	private String name;
	private Double price;
	private String description;
	private int quantityGrams;
	private int quantityMl;
	private boolean deleted;
	
	public Item() {
		super();
	}

	public Item(String id, String name, Double price, String description, int quantityGrams, int quantityMl,
			boolean deleted) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.description = description;
		this.quantityGrams = quantityGrams;
		this.quantityMl = quantityMl;
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

	public int getQuantityGrams() {
		return quantityGrams;
	}

	public void setQuantityGrams(int quantityGrams) {
		this.quantityGrams = quantityGrams;
	}

	public int getQuantityMl() {
		return quantityMl;
	}

	public void setQuantityMl(int quantityMl) {
		this.quantityMl = quantityMl;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price=" + price + ", description=" + description
				+ ", quantityGrams=" + quantityGrams + ", quantityMl=" + quantityMl + ", deleted=" + deleted + "]";
	}
	
	
	
}
