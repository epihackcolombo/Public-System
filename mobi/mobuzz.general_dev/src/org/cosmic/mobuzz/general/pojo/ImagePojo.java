package org.cosmic.mobuzz.general.pojo;

public class ImagePojo {
	
	String img_type;
	String img_data;
	String img_name;
	
	public ImagePojo(String img_type, String img_data) {
		super();
		this.img_type = img_type;
		this.img_data = img_data;
	}

	public ImagePojo(String img_type, String img_data, String img_name) {
		super();
		this.img_type = img_type;
		this.img_data = img_data;
		this.img_name = img_name;
	}

	public String getImg_type() {
		return img_type;
	}

	public void setImg_type(String img_type) {
		this.img_type = img_type;
	}

	public String getImg_data() {
		return img_data;
	}

	public void setImg_data(String img_data) {
		this.img_data = img_data;
	}

	public String getImg_name() {
		return img_name;
	}

	public void setImg_name(String img_name) {
		this.img_name = img_name;
	}

}
