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

import com.example.demo.Model.FileListModel;
import com.example.demo.Model.FileModel;
import com.example.demo.Model.ProfileModel;
import com.example.demo.Model.RegisterModel;
import com.example.demo.Repository.FileRepo;
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
	@Autowired
    private GridFsTemplate template;
	@Autowired
	private ProfileRepo pr;
	
	@Autowired
	private MongoTemplate mongotemplate;

    @Autowired
    private GridFsOperations operations;
    RegisterModel rm=new RegisterModel();
    ProfileModel pmm=new ProfileModel();
    
    public boolean already_user(String email)
    {
    	return ru.existsById(email);
    }


  public void addFile(String email,MultipartFile image,int points,HashSet<List<Integer>> co_ordinates) throws IOException{
	  rm.setEmail(email);
	  rm.setImage(new Binary(BsonBinarySubType.BINARY,image.getBytes()));
	  rm.setPoints(points);
	  rm.setListOfLists(co_ordinates);
	  pmm.setEmail(email);
	  pr.save(pmm);
	  ru.save(rm);
	  
  }
 
  public RegisterModel getFile(String email)throws IOException{
	  boolean flag=ru.findById(email).isPresent();
	  if(flag)
		  return ru.findById(email).get();
	  return null;
  		
  }
  
  public ProfileModel getProfile(String email)
  {
	  return pr.findById(email).get();
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
			  
		  }

	//FILE UPLOAD MODULE SERVICE
		@Autowired
		private FileRepo fu;
		FileListModel fm=new FileListModel();
		public void addFileList(String email,List<ObjectId> filelist)
		{
			fm.setEmail(email);
			fm.setId(filelist);
			fu.save(fm);
			
		}
		@Autowired
	    private GridFsTemplate gridFsTemplate;
		public void save(String email,MultipartFile upload) throws IOException {
			// TODO Auto-generated method stub
//			 try {
//				 System.out
//			      Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
//			    } catch (Exception e) {
//			      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
//			    }
			 DBObject metadata = new BasicDBObject();
		        metadata.put("fileSize", upload.getSize());
		        ObjectId fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);
		        System.out.println("fileid"+fileID);
			       FileListModel flm=fu.findById(email).get();
			       List<ObjectId> ol=flm.getId();
			       List<String> fname=flm.getFname();
		        ol.add(fileID);
		        fname.add(upload.getOriginalFilename());
		        fm.setEmail(email);
		        fm.setId(ol);
		        fm.setFname(fname);
		        fu.save(fm);
		}
		
		public List<GridFSFile> fetch_file(String email){
			FileListModel flm=fu.findById(email).get();
		    List<ObjectId> id=flm.getId();
			List<GridFSFile> filename=new ArrayList<>();
			for(int i=0;i<id.size();i++)
			{
				
				GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id.get(i))));
				filename.add(file);
					
			}
			return filename;		
		}
		
		
		//HOW TO GET THE OBJECT ID FOR RETREIVE A FILE

		

}
