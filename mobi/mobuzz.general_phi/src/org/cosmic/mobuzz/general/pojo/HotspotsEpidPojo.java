package org.cosmic.mobuzz.general.pojo;

public class HotspotsEpidPojo {

	//To support CMC-PHI-Epid reports
	
	Double lat;
	Double lng;

	int rad;
	String tag;
	String fill;
	String stroke;
	
	public HotspotsEpidPojo() {

	}
	
	public HotspotsEpidPojo(Double lat, Double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}
	
	public HotspotsEpidPojo(Double lat, Double lng, int rad, String tag, String fill, String stroke) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.rad = rad;
		this.tag = tag;
		this.fill = fill;
		this.stroke = stroke;
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getFill() {
		return fill;
	}

	public void setFill(String fill) {
		this.fill = fill;
	}

	public String getStroke() {
		return stroke;
	}

	public void setStroke(String stroke) {
		this.stroke = stroke;
	}


}
