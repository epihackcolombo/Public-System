package org.cosmic.mobuzz.general.pojo;

import org.cosmic.mobuzz.general.util.GlobalMethods;

public class HistoryPojo {
	
	int image = 0;
	String id = null;
	String gps = null;
	String address = null;
	String date = null;
	String ward = null;
	String remarks = null;
	String assessment = null;
	String cmcmessage = null;	
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

		if(GlobalMethods.validateString(id))
		{
			return id;
		}
		else
		{
			return "-1";
		}
		
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGps() {

		if(GlobalMethods.validateString(gps))
		{
			return gps;
		}
		else
		{
			return " ";
		}
		
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public String getAddress() {

		if(GlobalMethods.validateString(address))
		{
			return address;
		}
		else
		{
			return " ";
		}
		
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDate() {

		if(GlobalMethods.validateString(date))
		{
			return date;
		}
		else
		{
			return " ";
		}
		
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getWard() {

		if(GlobalMethods.validateString(ward))
		{
			return ward;
		}
		else
		{
			return " ";
		}
		
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getRemarks() {

		if(GlobalMethods.validateString(remarks))
		{
			return remarks;
		}
		else
		{
			return " ";
		}
		
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAssessment() {

		if(GlobalMethods.validateString(assessment))
		{
			return assessment;
		}
		else
		{
			return " ";
		}
		
	}

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}	
	
	public String getCmcMessage() {

		if(GlobalMethods.validateString(cmcmessage))
		{
			return cmcmessage;
		}
		else
		{
			return " ";
		}
		
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

	public String getCmcmessage() {
		return cmcmessage;
	}

	public void setCmcmessage(String cmcmessage) {
		this.cmcmessage = cmcmessage;
	}

	public String getImagepath() {
		return imagepath;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}

}
