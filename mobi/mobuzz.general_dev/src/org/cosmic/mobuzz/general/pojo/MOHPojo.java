package org.cosmic.mobuzz.general.pojo;

public class MOHPojo {
	
	int id;
	String district_id, 
	     	district_name, 
			district_moh, 
			moh_address, 
			moh_telephone, 
			moh_doctor, 
			moh_doctor_telephone, 
			moh_location;
	
	
	public MOHPojo(int id, String district_id, String district_name, String district_moh, String moh_address, String moh_telephone, String moh_doctor, String moh_doctor_telephone, String moh_location) {
		super();
		this.id = id;
		this.district_id = district_id;
		this.district_name = district_name;
		this.district_moh = district_moh;
		this.moh_address = moh_address;
		this.moh_telephone = moh_telephone;
		this.moh_doctor = moh_doctor;
		this.moh_doctor_telephone = moh_doctor_telephone;
		this.moh_location = moh_location;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getDistrict_id() {
		return district_id;
	}


	public void setDistrict_id(String district_id) {
		this.district_id = district_id;
	}


	public String getDistrict_name() {
		return district_name;
	}


	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}


	public String getDistrict_moh() {
		return district_moh;
	}


	public void setDistrict_moh(String district_moh) {
		this.district_moh = district_moh;
	}


	public String getMoh_address() {
		return moh_address;
	}


	public void setMoh_address(String moh_address) {
		this.moh_address = moh_address;
	}


	public String getMoh_telephone() {
		return moh_telephone;
	}


	public void setMoh_telephone(String moh_telephone) {
		this.moh_telephone = moh_telephone;
	}


	public String getMoh_doctor() {
		return moh_doctor;
	}


	public void setMoh_doctor(String moh_doctor) {
		this.moh_doctor = moh_doctor;
	}


	public String getMoh_doctor_telephone() {
		return moh_doctor_telephone;
	}


	public void setMoh_doctor_telephone(String moh_doctor_telephone) {
		this.moh_doctor_telephone = moh_doctor_telephone;
	}


	public String getMoh_location() {
		return moh_location;
	}


	public void setMoh_location(String moh_location) {
		this.moh_location = moh_location;
	}
	
	
}
