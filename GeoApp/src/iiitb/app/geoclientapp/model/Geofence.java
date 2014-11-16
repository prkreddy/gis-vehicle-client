package iiitb.app.geoclientapp.model;

public class Geofence {

	private int geofenceId;

	private double latitude;

	private double longitude;

	private int fenceRadius;

	private int expireDuration;

	public int getGeofenceId() {
		return geofenceId;
	}

	public void setGeofenceId(int geofenceId) {
		this.geofenceId = geofenceId;
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

	public int getFenceRadius() {
		return fenceRadius;
	}

	public void setFenceRadius(int fenceRadius) {
		this.fenceRadius = fenceRadius;
	}

	public int getExpireDuration() {
		return expireDuration;
	}

	public void setExpireDuration(int expireDuration) {
		this.expireDuration = expireDuration;
	}

	public int getPathId() {
		return pathId;
	}

	public void setPathId(int pathId) {
		this.pathId = pathId;
	}

	private int pathId;

}
