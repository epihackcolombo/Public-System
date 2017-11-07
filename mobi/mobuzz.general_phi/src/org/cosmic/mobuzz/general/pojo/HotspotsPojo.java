package org.cosmic.mobuzz.general.pojo;

public class HotspotsPojo {

	Double lat;
	Double lng;
	int week;
	String start;
	String end;
	
	
	public HotspotsPojo() {

	}

	public HotspotsPojo(Double lat, Double lng, int week, String start,	String end) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.week = week;
		this.start = start;
		this.end = end;
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

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	

}
