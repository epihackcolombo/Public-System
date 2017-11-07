package org.cosmic.mobuzz.general.pojo;

public class ProfileLoginPojo {

	String status;
	String language;

	public ProfileLoginPojo(String status) {
		super();
		this.status = status;
		this.language = "";
	}
	
	public ProfileLoginPojo(String status, String language) {
		super();
		this.status = status;
		this.language = language;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
