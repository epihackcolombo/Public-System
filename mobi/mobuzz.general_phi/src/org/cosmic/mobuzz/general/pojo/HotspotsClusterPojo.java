package org.cosmic.mobuzz.general.pojo;

public class HotspotsClusterPojo {

	Double lat;
	Double lng;
	int rad;
	int toyear;
	int toweek;
	String fromdate;
	String todate;
	String color;

	public HotspotsClusterPojo(Double lat, Double lng, int rad, int toyear, int toweek, String fromdate, String todate, String color) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.rad = rad;
		this.toyear = toyear;
		this.toweek = toweek;
		this.fromdate = fromdate;
		this.todate = todate;
		this.color = color;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public int getRad() {
		return rad;
	}

	public void setRad(int rad) {
		this.rad = rad;
	}

	public int getToyear() {
		return toyear;
	}

	public void setToyear(int toyear) {
		this.toyear = toyear;
	}

	public int getToweek() {
		return toweek;
	}

	public void setToweek(int toweek) {
		this.toweek = toweek;
	}

	public String getFromdate() {
		return fromdate;
	}

	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}

	public String getTodate() {
		return todate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
