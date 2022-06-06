package com.example.demo.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class ProfileModel {
	@Id
	private String email;
	private String f_name;
    private String l_name;
    private String user_name;
    private String country;
    
    
    
    public ProfileModel() {
	
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getF_name() {
		return f_name;
	}
	public void setF_name(String f_name) {
		this.f_name = f_name;
	}
	public String getL_name() {
		return l_name;
	}
	public void setL_name(String l_name) {
		this.l_name = l_name;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public ProfileModel(String email, String f_name, String l_name, String user_name, String country) {
		super();
		this.email = email;
		this.f_name = f_name;
		this.l_name = l_name;
		this.user_name = user_name;
		this.country = country;
	}

}
