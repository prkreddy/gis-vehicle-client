package iiitb.app.geoclientapp.model;

public class VehicleLocation {

	private int vehicleLocationId;

	private double latitude;

	public int getVehicleLocationId() {
		return vehicleLocationId;
	}

	public void setVehicleLocationId(int vehicleLocationId) {
		this.vehicleLocationId = vehicleLocationId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	private double longitude;

	private String time;

}
