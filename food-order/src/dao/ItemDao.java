package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Item;
import beans.Order;
import beans.Restaurant;


public class ItemDao {

	private HashMap<String, Item> items = new HashMap<>();
	private String contextPath;
	
	public ItemDao() {
		
	}
	public ItemDao(String contextPath) {
		loadItems(contextPath);
		this.contextPath = contextPath;
	}
	
	public void loadItems(String contextPath) {
		BufferedReader in = null;
		ObjectMapper mapper = new ObjectMapper();
		List<Item> itemsFromFile = new ArrayList<>();
		try {
			File file = new File (contextPath + "/artikli.json");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));

			if (in != null) {
				itemsFromFile = mapper.readValue(in, new TypeReference<List<Item>>(){});
				items.clear();
				
				for (Item item : itemsFromFile) {
					items.put(item.getId(), item);
				}
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
	}
	
	public void saveItems() {
		ObjectMapper mapper = new ObjectMapper();
		List<Item> itemList = new ArrayList<Item>();
		itemList.addAll(items.values());
		System.out.println("Artikli "+itemList);

		try {
			File file = new File (this.contextPath + "/artikli.json");
			System.out.println(file.getCanonicalPath());
			mapper.writerWithDefaultPrettyPrinter().writeValue(file , itemList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public List<Item> getAll(){
		List<Item> list = new ArrayList<>();
		for (Item item : items.values()) {
			if (!item.isDeleted()) {
				list.add(item);
			}
		}		
		return list;
	}
	public Item findByName(String name) {
		
		for (Item i : items.values()) {
			if (i.getName().equals(name) && !i.isDeleted()) {
				return i;
			}
		}
		return null;
	}
	public Item addItem(Item item) {
		
		Integer maxId = 0;
		for (String id : items.keySet()) {
			int idNum =Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		
		item.setId(maxId.toString());
		item.setDeleted(false);
		items.put(item.getId(), item);
		saveItems();
		if (items.get(item.getId()) == null) {
			return null;
		}
		return items.get(item.getId());
	}
	public boolean deleteItem(Item item) {
		Item forDelete = items.get(item.getId());
		if(forDelete != null && !forDelete.isDeleted()) {
			forDelete.setDeleted(true);
			saveItems();
			return true;
		}else
			return false;
	}
	public Item editItem(Item item) {
		Item forEdit = items.get(item.getId());
		if (forEdit != null && !forEdit.isDeleted()) {
			forEdit.setDescription(item.getDescription());
			forEdit.setName(item.getName());
			forEdit.setPrice(item.getPrice());
			forEdit.setQuantity(item.getQuantity());
			items.put(forEdit.getId(), forEdit);
			saveItems();
			return forEdit;
			
		} else {
			return null;
		}
	}
	public List<Item> getPopularItems(){
		
		List<Item> itemList = getAll();
	
		Comparator<Item> itemOrdersComparator = (Item item1, Item item2)-> Integer.valueOf(item1.getOrdersCounter()).compareTo(item2.getOrdersCounter());

		itemList.sort(itemOrdersComparator.reversed());
		
		System.out.println("Sortirana lista desc: "+ itemList);
		
		
		//deset komada sa najvise porudzbina
		if (itemList.size() >= 10) {
			return itemList.subList(0, 10);
		} else {
			return itemList;
		}
		
		
	}
	public Item getById(String id) {
		System.out.println(items);
		System.out.println("trazi se " +id+"id");
		System.out.println(items.containsKey(id));
		Item item =  items.containsKey(id) ? items.get(id) : null;
		if (item != null && !item.isDeleted()) {
			return item;
		} else {
			return null;
		}
	}
	public boolean deleteItems(List<Item> itemsForDelete) {
		int removed = 0;
		System.out.println(itemsForDelete);
		for (Item item : itemsForDelete) {
			if (items.containsKey(item.getId())) {
				items.get(item.getId()).setDeleted(true);
				removed++;
			}
		}
		if (removed == itemsForDelete.size()) {
			saveItems();
			return true;
		}else {
			return false;
		}
		
	}
	public Item ordered(String id) {
		Item item = getById(id);
		if (item == null) {
			return null;
		} 
		int counter = item.getOrdersCounter();
		item.setOrdersCounter(counter + 1);
		saveItems();
		return item;		
	}
}
