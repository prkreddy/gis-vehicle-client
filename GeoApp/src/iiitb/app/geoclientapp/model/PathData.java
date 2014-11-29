package iiitb.app.geoclientapp.model;

import java.util.ArrayList;
import java.util.List;

public class PathData {

	/**
	 * 
	 */
	public PathData() {
		super();
		vehicles = new ArrayList<Vehicle>();
		locations = new ArrayList<Geofence>();
	}

	private int pathId;

	private String pathName;

	private int pathLen;

	private List<Geofence> locations;

	private List<Vehicle> vehicles;

	public int getPathId() {
		return pathId;
	}

	public void setPathId(int pathId) {
		this.pathId = pathId;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public int getPathLen() {
		return pathLen;
	}

	public void setPathLen(int pathLen) {
		this.pathLen = pathLen;
	}

	public List<Geofence> getLocations() {
		return locations;
	}

	public void setLocations(List<Geofence> locations) {
		this.locations = locations;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

}
