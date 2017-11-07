package org.cosmic.mobuzz.general.pojo;

public class HistoryPojo {
	
	int image;
	String id;
	String gps;
	String address;
	String date;
	String ward;
	String remarks;
	String assessment;
	String cmcmessage;
	String imagepath = null;

	
	public HistoryPojo() {
		
	}

	public HistoryPojo(int image, String id, String gps, String address, String date, String ward, String remarks, String assessment, String cmcmessage) {
		super();
		this.image = image;
		this.id = id;
		this.gps = gps;
		this.address = address;
		this.date = date;
		this.ward = ward;
		this.remarks = remarks;
		this.assessment = assessment;
		this.cmcmessage = cmcmessage;
	}

	public HistoryPojo(int image, String id, String gps, String address, String date, String ward, String remarks, String assessment, String cmcmessage, String imagepath) {
		super();
		this.image = image;
		this.id = id;
		this.gps = gps;
		this.address = address;
		this.date = date;
		this.ward = ward;
		this.remarks = remarks;
		this.assessment = assessment;
		this.cmcmessage = cmcmessage;
		this.imagepath = imagepath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAssessment() {
		return assessment;
	}

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}	
	
	public String getCmcMessage() {
		return cmcmessage;
	}

	public void setCmcMessage(String cmcMessage) {
		this.cmcmessage = cmcMessage;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getImagepath() {
		return imagepath;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}

}
