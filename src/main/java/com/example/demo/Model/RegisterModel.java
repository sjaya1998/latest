package com.example.demo.Model;

import java.util.HashSet;
import java.util.Set;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class RegisterModel {
	@Id
	private String email;
	private ObjectId file_id;
	private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;
	private Binary image;
	private int points;
	private  HashSet<Set<Integer>> listOfLists= new HashSet<Set<Integer>>();
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public HashSet<Set<Integer>> getListOfLists() {
		return listOfLists;
	}
	public void setListOfLists(HashSet<Set<Integer>> listOfLists) {
		this.listOfLists = listOfLists;
	}
	
   
    
	public Binary getImage() {
		return image;
	}
	public void setImage(Binary image) {
		this.image = image;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ObjectId getFile_id() {
		return file_id;
	}
	public void setFile_id(ObjectId fileID) {
		this.file_id = fileID;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
   
	}