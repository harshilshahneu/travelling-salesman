package edu.northeastern.info6205.tspsolver.model;

public class Point implements Payload {
	private String id;
	
	// Real world coordinates
    private double latitude;
    private double longitude;
    
    public Point() {
		
	}
    
	public Point(String id, double latitude, double longitude) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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

}
