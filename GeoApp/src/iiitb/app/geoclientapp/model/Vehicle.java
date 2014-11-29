package iiitb.app.geoclientapp.model;

public class Vehicle {

	private VehicleLocation vehicleLocation;

	public VehicleLocation getVehicleLocation() {
		return vehicleLocation;
	}

	public void setVehicleLocation(VehicleLocation vehicleLocation) {
		this.vehicleLocation = vehicleLocation;
	}

	private int vehicleId;

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	private String vehicleName;

	private String vehicleRegNo;

	private boolean alarmflag;

	private int pathId;

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getVehicleRegNo() {
		return vehicleRegNo;
	}

	public void setVehicleRegNo(String vehicleRegNo) {
		this.vehicleRegNo = vehicleRegNo;
	}

	public boolean isAlarmflag() {
		return alarmflag;
	}

	public void setAlarmflag(boolean alarmflag) {
		this.alarmflag = alarmflag;
	}

	public int getPathId() {
		return pathId;
	}

	public void setPathId(int pathId) {
		this.pathId = pathId;
	}

}
