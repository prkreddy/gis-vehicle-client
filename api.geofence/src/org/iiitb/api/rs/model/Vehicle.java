package org.iiitb.api.rs.model;

public class Vehicle {

	private int vehicleId;

	private String vehicleName;

	private String vehicleRegNo;

	private boolean alarmflag;

	private int pathId;

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

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
