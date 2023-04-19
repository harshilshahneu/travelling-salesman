package edu.northeastern.info6205.tspsolver.model;

import java.util.Objects;

public class Point {
	private String id;
	private String placeId;
	
	// Real world coordinates
    private double latitude;
    private double longitude;
    
    public Point() {
		
	}
    
	public Point(String id, double latitude, double longitude) {

		if (latitude < -90.0 || latitude > 90.0 || longitude > 180.0 || longitude < -180.0){
			throw new IllegalArgumentException("Illegal coordinates provided.");
		};

		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
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
		if (latitude < -90.0 || latitude > 90.0){
			throw new IllegalArgumentException("Illegal coordinates provided.");
		}
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		if (longitude > 180.0 || longitude < -180.0){
			throw new IllegalArgumentException("Illegal coordinates provided.");
		}
		this.longitude = longitude;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, latitude, longitude);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		return Objects.equals(id, other.id)
				&& Double.doubleToLongBits(latitude) == Double.doubleToLongBits(other.latitude)
				&& Double.doubleToLongBits(longitude) == Double.doubleToLongBits(other.longitude);
	}

	@Override
	public String toString() {
		return "Point [id=" + id + ", placeId=" + placeId + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
}
