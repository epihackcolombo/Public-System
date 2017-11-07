package org.cosmic.mobuzz.general.pojo;

public class ResponsePojo {

	public int responseCode;
	public String responseContent;

	public ResponsePojo(int responseCode, String responseContent) {
		this.responseCode = responseCode;
		this.responseContent = responseContent;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

}
