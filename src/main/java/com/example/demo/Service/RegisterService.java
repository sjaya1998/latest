package com.example.demo.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.bson.types.Binary;

import com.example.demo.Model.LoadFile;
import com.example.demo.Model.ProfileModel;
import com.example.demo.Model.RegisterModel;
import com.example.demo.Repository.ProfileRepo;
import com.example.demo.Repository.RegisterRepo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;



@Service
public class RegisterService {
	@Autowired 
	private RegisterRepo ru; 
	
//	public void add(String email, MultipartFile file) throws IOException{
//		RegisterModel rm= new RegisterModel();
//		rm.setEmail(email);
//		System.out.println("");
//		byte [] byteArr=file.getBytes();
//		InputStream inputStream = new ByteArrayInputStream(byteArr);
////		for(int i=0;i<inputStream.length();i++) {
////			System.out.println(bytes[i]);
////		}
////		System.out.println(bytes+"44444");
//		rm.setImage(inputStream.toString());
//		ru.save(rm);
//		
////		String filename = StringUtils.cleanPath(file.getOriginalFilename());
////		String ext1 = FilenameUtils.getExtension(filename);
////		System.out.println("11111111111111111111111111111111"+ext1);
////		String regex= "([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp))$)";
////		Pattern p = Pattern.compile(regex);
////        java.util.regex.Matcher m = p.matcher(ext1);
////		if(m.matches())
////		{
////			try {
////				
////			}
////			catch(IOException e)
////			{
////				e.printStackTrace();
////			}
//			
////		}
////		else {
////			System.out.println("00000000000000000000000000000000000000000000");
////		}
//		
//		
////		rm.setImage(new Binary(BsonBinarySubType.BINARY,file.getBytes()));
////		rm=ru.insert(rm);
////		return rm.getEmail();
//	}
	@Autowired
    private GridFsTemplate template;
	
	@Autowired
	private MongoTemplate mongotemplate;

    @Autowired
    private GridFsOperations operations;
    RegisterModel rm=new RegisterModel();


  public void addFile(String email,MultipartFile image,int points,HashSet<Set<Integer>> co_ordinates) throws IOException{
	  rm.setEmail(email);
	  rm.setImage(new Binary(BsonBinarySubType.BINARY,image.getBytes()));
	  rm.setPoints(points);
	  rm.setListOfLists(co_ordinates);
		ru.save(rm) ; 
  }
  
    
  public RegisterModel getFile(String email)throws IOException{
	  boolean flag=ru.findById(email).isPresent();
	  if(flag)
		  return ru.findById(email).get();
	  return null;
  		
  }
    

 

	public RegisterModel getPhoto(String id) {
		return ru.findById(id).get();
	}
	public RegisterModel saveOrUpdate(RegisterModel r)
	{
		return ru.save(r);
	}
	public List<RegisterModel> findAll(){
		return ru.findAll();
	}
	public void delete(RegisterModel r)
	{ 
		ru.delete(r);
	}
	
	
	//PROFILE MODULES SERVICE
	

	@Autowired
	private ProfileRepo pu;
	 ProfileModel pm=new ProfileModel();
		public void addprofile(String email,String f_name,String l_name,String country,String user_name)
		  {
			  System.out.println("rrrrrrrrrr"+email+f_name+l_name+country+user_name);
			  pm.setCountry(country);
			  pm.setEmail(email);
			  pm.setF_name(f_name);
			  pm.setL_name(l_name);
			  pm.setUser_name(user_name);
			  pu.save(pm);
			  return;
			  
		  }
	

}
