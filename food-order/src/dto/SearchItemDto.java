package dto;

import java.util.List;

import beans.Item;
import enums.ItemType;

public class SearchItemDto {

	private String searchItemId;
	private String itemName;
	private int itemType;
	private double itemPriceMin;
	private double itemPriceMax;
	private String itemRestId;
	
	private List<Item> items;

	public SearchItemDto() {
		super();
	}

	public String getSearchItemId() {
		return searchItemId;
	}

	public void setSearchItemId(String searchItemId) {
		this.searchItemId = searchItemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public double getItemPriceMin() {
		return itemPriceMin;
	}

	public void setItemPriceMin(double itemPriceMin) {
		this.itemPriceMin = itemPriceMin;
	}

	public double getItemPriceMax() {
		return itemPriceMax;
	}

	public void setItemPriceMax(double itemPriceMax) {
		this.itemPriceMax = itemPriceMax;
	}

	public String getItemRestId() {
		return itemRestId;
	}

	public void setItemRestId(String itemRestId) {
		this.itemRestId = itemRestId;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	
}
