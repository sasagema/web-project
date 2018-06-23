package beans;

import enums.VehicleType;

public class Vehicle {

	private String id;
	private String brand;
	private String model;
	private VehicleType vehicleType;
	private String numberPlate;
	private int prductionYear;
	private boolean occupied;
	private String note;
	private boolean deleted;
	public Vehicle() {
		super();
	}
	public Vehicle(String id, String brand, String model, VehicleType vehicleType, String numberPlate,
			int prductionYear, boolean occupied, String note, boolean deleted) {
		super();
		this.id = id;
		this.brand = brand;
		this.model = model;
		this.vehicleType = vehicleType;
		this.numberPlate = numberPlate;
		this.prductionYear = prductionYear;
		this.occupied = occupied;
		this.note = note;
		this.deleted = deleted;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public VehicleType getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getNumberPlate() {
		return numberPlate;
	}
	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}
	public int getPrductionYear() {
		return prductionYear;
	}
	public void setPrductionYear(int prductionYear) {
		this.prductionYear = prductionYear;
	}
	public boolean isOccupied() {
		return occupied;
	}
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
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
		return "Vehicle [id=" + id + ", brand=" + brand + ", model=" + model + ", vehicleType=" + vehicleType
				+ ", numberPlate=" + numberPlate + ", prductionYear=" + prductionYear + ", occupied=" + occupied
				+ ", note=" + note + ", deleted=" + deleted + "]";
	}
	
	
}
