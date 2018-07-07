package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Item;
import beans.User;
import beans.Vehicle;
import enums.VehicleStatus;

public class VehicleDao {

	private HashMap<String, Vehicle> vehicles = new HashMap<>();
	private String contextPath;
	
	public VehicleDao() {
		
	}
	public VehicleDao(String contextPath) {
		loadVehicles(contextPath);
		this.contextPath = contextPath;
	}
	
	public void loadVehicles(String contextPath) {
		BufferedReader in = null;
		ObjectMapper mapper = new ObjectMapper();
		List<Vehicle> vehiclesFromFile = new ArrayList<>();
		try {
			File file = new File (contextPath + "/vozila.json");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));

			if (in != null) {
				vehiclesFromFile = mapper.readValue(in, new TypeReference<List<Vehicle>>(){});
				vehicles.clear();
				
				for (Vehicle vehicle : vehiclesFromFile) {
					vehicles.put(vehicle.getId(), vehicle);
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
	
	public void saveVehicles() {
		ObjectMapper mapper = new ObjectMapper();
		List<Vehicle> vehicleList = new ArrayList<Vehicle>();
		vehicleList.addAll(vehicles.values());
		System.out.println("Artikli "+vehicleList);

		try {
			File file = new File (this.contextPath + "/vozila.json");
			System.out.println(file.getCanonicalPath());
			mapper.writerWithDefaultPrettyPrinter().writeValue(file , vehicleList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public List<Vehicle> getAll(){
		List<Vehicle> list = new ArrayList<>();
		for (Vehicle vehicle : vehicles.values()) {
			if (!vehicle.isDeleted()) {
				list.add(vehicle);
			}
		}		
		return list;
	}
	public Vehicle addVehicle(Vehicle vehicle) {
		
		Integer maxId = 0;
		for (String id : vehicles.keySet()) {
			int idNum =Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		vehicle.setId(maxId.toString());
		vehicle.setDeleted(false);
		vehicle.setOccupied(false);
		vehicle.setOccupiedById(null);
		vehicle.setOccupiedByUsername(null);
		vehicle.setStatus(VehicleStatus.Slobodan);
		vehicles.put(vehicle.getId(), vehicle);
		saveVehicles();
		if (vehicles.get(vehicle.getId()) == null) {
			return null;
		}
		return vehicles.get(vehicle.getId());
	}
	public boolean deleteVehicle(Vehicle vehicle) {
		Vehicle forDelete = vehicles.get(vehicle.getId());
		if(forDelete != null && !forDelete.isDeleted()) {
			forDelete.setDeleted(true);
			saveVehicles();
			return true;
		}else
			return false;
	}
	public Vehicle editVehicle(Vehicle vehicle) {
		Vehicle forEdit = vehicles.get(vehicle.getId());
		if (forEdit != null && !forEdit.isDeleted()) {
			forEdit.setBrand(vehicle.getBrand());
			forEdit.setModel(vehicle.getModel());
			forEdit.setNote(vehicle.getNote());
			forEdit.setNumberPlate(vehicle.getNumberPlate());
			forEdit.setProductionYear(vehicle.getProductionYear());
			forEdit.setVehicleType(vehicle.getVehicleType());
			vehicles.put(forEdit.getId(), forEdit);
			saveVehicles();
			return forEdit;
			
		} else {
			return null;
		}
	}
	public Vehicle getById(String id) {
		System.out.println(vehicles);
		System.out.println("trazi se " +id+"id");
		System.out.println(vehicles.containsKey(id));
		Vehicle vehicle =  vehicles.containsKey(id) ? vehicles.get(id) : null;
		if (vehicle != null && !vehicle.isDeleted()) {
			return vehicle;
		} else {
			return null;
		}
	}
	public Vehicle findByNumPlate(String numPlate) {
		
		for (Vehicle v : getAll()) {
			if (v.getNumberPlate().toUpperCase().equals(numPlate.toUpperCase()) && !v.isDeleted()) {
				return v;
			}
		}
		return null;
	}
	public Vehicle addToDelivery(String vehicleId, User user) {
		Vehicle vehicle = getById(vehicleId);
		if (vehicle != null && !vehicle.isOccupied() && vehicle.getStatus() == VehicleStatus.Slobodan) {
			vehicle.setStatus(VehicleStatus.Zauzet);
			vehicle.setOccupied(true);
			vehicle.setOccupiedByUsername(user.getUsername());
			vehicle.setOccupiedById(user.getId());
			saveVehicles();
			return vehicle;
		} else {
			return null;
		}
	}
	public List<Vehicle> getVehiclesForDelivery(){
		List<Vehicle> retList = new ArrayList<Vehicle>();
		for (Vehicle vehicle : getAll()) {
			if (vehicle.getStatus() == VehicleStatus.Slobodan) {
				retList.add(vehicle);
			}
		}
		return retList;
	}
}
