package com.example.demo.Model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class FileListModel {
	@Id
	private String email;
	private List<ObjectId> id=new ArrayList<>();
	private List<String> fname=new ArrayList<>();
	public List<String> getFname() {
		return fname;
	}
	public void setFname(List<String> fname) {
		this.fname = fname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<ObjectId> getId() {
		return id;
	}
	public void setId(List<ObjectId> id) {
		this.id = id;
	}
	
	

}
