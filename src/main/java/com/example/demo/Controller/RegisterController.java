package com.example.demo.Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.bson.BsonBinarySubType;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Model.LoadFile;
import com.example.demo.Model.RegisterModel;
import com.example.demo.Repository.RegisterRepo;
import com.example.demo.Service.RegisterService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;


@Controller
//@RequestMapping
public class RegisterController {
	@Autowired 
	private RegisterRepo ru;
	@Autowired
    private GridFsTemplate template;
	@Autowired
	public RegisterService rs;
	@Autowired
    MongoClient mongoClient;
    @Autowired
    private GridFsOperations operations;
    
	@GetMapping("/")
	public String homepage() {
		return "home";
	}
	
	@RequestMapping("/register")
	public String registerpage() {
		return "newuser";
	}
	
	@GetMapping("/login")
	public String loginpage() {
		return "table";
	}
	
	 @PostMapping("/register_user")
	    public String register_user(@RequestParam("email") String email,@RequestParam("file") MultipartFile file,@RequestParam("inputId") int points,@RequestParam("sets") String set) throws IOException {
		 System.out.println(set);
		 set=set.replace(" ","");
		 String[] s=set.split(",");
		 System.out.println(s.length);
		 for(int i=0;i<s.length;i++)
		 {
			 System.out.println(s[i]+"i");
		 }
		 System.out.println(set+"register");
		 HashSet<Set<Integer>> listOfLists= new HashSet<Set<Integer>>();
		 for(int i=1;i<s.length-1;i=i+2)
		 {
			 Set<Integer> list1= new HashSet<Integer>();
			 list1.add(Integer.valueOf(s[i+1]));
	        list1.add(Integer.valueOf(s[i]));
	       
	        listOfLists.add(list1);
		 }
		 System.out.println(listOfLists);
	        
//		 rs.addFile(email, file, points, listOfLists);
	     return "home";
	    }
	
	 
	    @RequestMapping("/fetch")
	    public String download(Model model,@Param("email") String email) throws IOException {
	    	System.out.println("------------"+email);
	    	RegisterModel rm=rs.getFile(email);
	        System.out.println(rm);
	        if(rm!=null)
	        {
	        	model.addAttribute("email",rm.getEmail());
		        model.addAttribute("image",Base64.getEncoder().encodeToString(rm.getImage().getData()));
		        model.addAttribute("co-ordinates",rm.getListOfLists());
		        model.addAttribute("points",rm.getPoints());
		        return "login";  
	        }
	        	return "home";
	        
	           
	    }
	    @PostMapping("/check_user")
	    public String check_user(Model model,@RequestParam ("email1") String email,@RequestParam("points") int points,@RequestParam("sets") String set) throws IOException {
	    	boolean flag=true;
	    	 String[] s=set.split(",");
			int[][] login_value=new int[points][2];
			System.out.println(set);
			int z=1;
			 for(int i=0;i<login_value.length;i++)
			 {
					login_value[i][0]=Integer.valueOf(s[z]);
					login_value[i][1]=Integer.valueOf(s[z+1]);
				    z=z+2;
				 
			 }
	    	 RegisterModel rm=rs.getFile(email);
	    	 System.out.println(rm.getEmail());
			 HashSet<Set<Integer>> l2= rm.getListOfLists();
			 List<ArrayList<Integer>> originalList = new ArrayList<ArrayList<Integer>>();
			 Iterator value = l2.iterator();
		        while (value.hasNext()) {
		        	List<Integer> l1=new ArrayList<>();
		        	String ss=value.next().toString();
		        	ss=ss.replace('[', ',');
		        	ss=ss.replace(']', ',');
		        	ss=ss.replace(" ","");
		        	String[] split=ss.split(",");
	        		l1.add(Integer.valueOf(split[1]));
	        		l1.add(Integer.valueOf(split[2]));
	        		originalList.add((ArrayList<Integer>) l1);
		        }
		        for(int i=0;i<login_value.length;i++)
		        {
		        	for(int j=0;j<login_value[i].length;j++)
		        	{
		        		System.out.print(login_value[i][j]+" ");
		        	}
		        	System.out.println("");
		        }
		        System.out.println("originalList");
		        for(int i=0;i<originalList.size();i++)
		        {
		        	for(int j=0;j<2;j++)
		        	{
		        		System.out.print(originalList.get(i).get(j)+" ");
		        	}
		        	System.out.println("");
		        }
		        
		        
		        
//		        for(int i=0;i<login_value.length;i++)
//		        {
//		        	
//		        	int x=login_value[i][0];
//		        	int y=login_value[i][1];
//		        	int a=0;
//		        	while(a<originalList.size())
//		        	{
//		        		int ovx=originalList.get(a).get(0);
//		        		int ovy=originalList.get(a).get(1);
//		        		System.out.println("x" + x +"ovx" + ovx);
//		        		System.out.println("y" + y +"ovy" +ovy);
//		        		if(x>=ovx-15 && x<=ovx+15)
//		        		{
//		        			if(y>=ovy-15 && y<=ovy+15)
//		        			{
//		        				System.out.println("entering");
//		        				originalList.remove(a);
//		        				break;
//		        			}
//		        		}
//		        		else
//		        			a++;
//		      
//		        	}
//		        }
//		        System.out.println(originalList.size());
//		        if(originalList==null)
//		        	return "dashboard" ;
	    	    return "home";
	    }
	    @RequestMapping("/check_user/change_password")
	    public String change()
	    {
	    	return "change_password";
	    }
	    @RequestMapping("/dashboard")
	    public String dashboard(Model model,@Param("email") String email)
	    {   
//	    	model.addAttribute("email", email); 
//	    	return "Welcome to dashboard"
//	    			+ " "+email;
	    	return "dashboard";
	    }

private ObjectId q(String string, ObjectId id) {
	
	GridFSFile gridFsFile = template.findOne(new Query(Criteria.where(string).is(id)));
	return gridFsFile.getObjectId();
}
	@DeleteMapping("/delete")
	public ResponseEntity<String> delete(@RequestBody RegisterModel r)
	{
		rs.delete(r);
		return new ResponseEntity<String>("Record Deleted",HttpStatus.ACCEPTED);
	}
	
	//PROFILE CONTROLLER 
	@RequestMapping("/dashboard/profile")
	public String profilepage() {
		return "profile";
	}
	
	@PostMapping("/postprofile")
	public String addprofile(@RequestParam("email")String email,@RequestParam("f_name") String f_name,@RequestParam("l_name")String l_name,@RequestParam("country")String country,@RequestParam("user_name")String user_name)
	  {
			rs.addprofile(email,f_name,l_name,country,user_name);
			return "home";
	  }

}
