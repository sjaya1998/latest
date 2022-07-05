package com.example.demo.Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.Model.FileListModel;
import com.example.demo.Model.ProfileModel;
import com.example.demo.Message.ResponseMessage;
import com.example.demo.Model.RegisterModel;
import com.example.demo.Repository.FileRepo;
import com.example.demo.Repository.ProfileRepo;
import com.example.demo.Repository.RegisterRepo;
import com.example.demo.Service.*;
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
	private ProfileRepo puu;
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
		return "login";
	}
	//REGISTER USER
	 @PostMapping("/register_user")
	    public String register_user(Model model,@RequestParam("email") String email,@RequestParam("file") MultipartFile file,@RequestParam("inputId") int points,@RequestParam("sets") String set) throws IOException {
		 boolean flag=rs.already_user(email);
		 if(flag)
		 {
			 model.addAttribute("emailError","emailError"); 
			 return "newuser";
		 }
		 set=set.replace(" ","");
		 String[] s=set.split(",");
		 HashSet<List<Integer>> listOfLists= new HashSet<List<Integer>>();
		 for(int i=1;i<s.length-1;i=i+2)
		 {
			 List<Integer> list1= new ArrayList<Integer>();
	        list1.add(Integer.valueOf(s[i]));
	        list1.add(Integer.valueOf(s[i+1]));
	        listOfLists.add(list1);
		 }
		 System.out.println(listOfLists);
	        
		 rs.addFile(email, file, points, listOfLists);
		 String fname="";
		 String lname="";
		 String country="";
		 String uname="";
		 rs.addprofile(email, fname, lname, country, uname);
		 List<ObjectId> id=new ArrayList<>();
		 rs.addFileList(email,id);
		 RedirectView redirectView = new RedirectView();
		 redirectView.setContextRelative(true);
		 redirectView.setUrl("/");
		 return "login";
	    }
	
	 	//LOGIN USER
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
	        
	        model.addAttribute("emailError","emailError");
            return "login";
	      }
	    
	    @PostMapping("/check_user")
	    public String check_user(Model model,@RequestParam ("email1") String email,@RequestParam("points") int points,@RequestParam("sets") String set,RedirectAttributes redirectAttributes) throws IOException {
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
			 HashSet<List<Integer>> l2= rm.getListOfLists();
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
		                
		        for(int i=0;i<login_value.length;i++)
		        {
		        	
		        	int x=login_value[i][0];
		        	int y=login_value[i][1];
		        	int a=0;
		        	while(a<originalList.size())
		        	{
		        		int ovx=originalList.get(a).get(0);
		        		int ovy=originalList.get(a).get(1);
		        		System.out.println("x" + x +"ovx" + ovx);
		        		System.out.println("y" + y +"ovy" +ovy);
		        		if(x>=ovx-15 && x<=ovx+15)
		        		{
		        			if(y>=ovy-15 && y<=ovy+15)
		        			{
		        				System.out.println("entering");
		        				originalList.remove(a);
		        				break;
		        			}
		        			else
		        				a++;
		        		}
		        		else
		        			a++;
		      
		        	}
		        }
		        System.out.println(originalList.size());
		        if(originalList.size()==0)
		        {
		        	
		        	RegisterModel rmm=rs.getFile(email);
		        	ProfileModel pm=rs.getProfile(email);
		        	//model.addAttribute("", redirectAttributes)
		        	redirectAttributes.addFlashAttribute("email", rm.getEmail());
		        	redirectAttributes.addFlashAttribute("fname",pm.getF_name());
		        	redirectAttributes.addFlashAttribute("lname",pm.getL_name());
		        	redirectAttributes.addFlashAttribute("country",pm.getCountry());
		        	redirectAttributes.addFlashAttribute("uname",pm.getUser_name());	
		        	return "redirect:/dashboard";
		        }
		        	
		        model.addAttribute("logError","logError");
	            return "login";
	    }
	   //DASHBOARD
	    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	    public String dashboard(HttpServletRequest request,Model model,RedirectAttributes redirectAttributes)
	    {   

	    	String e=(String) model.asMap().get("email");
	    		
	    	return "dashboard";
	    }
	    //UPDATE PROFILE
	    @RequestMapping(value="/dashboard/profile", method = RequestMethod.GET)
	    public void display_profile(Model model,@Param("email") String email) throws IOException {
	    	System.out.println("email++++++"+email);
	    	
	    }
	    
	    @PostMapping("/dashboard/update_profile")
	    public String update_profile(@RequestParam("email") String email,@RequestParam("fname") String fname,@RequestParam("lname") String lname,@RequestParam("uname") String uname,@RequestParam("country") String country,RedirectAttributes redirectAttributes ) throws IOException{
	    
//	    	ProfileModel pm=rs.getProfile(email);
        	//model.addAttribute("", redirectAttributes)
	    	System.out.println("7777777777777777");
//        	redirectAttributes.addFlashAttribute("email", pm.getEmail());
//        	return "redirect:/dashboard";
	    	 rs.addprofile(email, fname, lname, country, uname);
	    	 ProfileModel pm=rs.getProfile(email);
	        	//model.addAttribute("", redirectAttributes)
	        	redirectAttributes.addFlashAttribute("email", pm.getEmail());
	        	redirectAttributes.addFlashAttribute("fname",pm.getF_name());
	        	redirectAttributes.addFlashAttribute("lname",pm.getL_name());
	        	redirectAttributes.addFlashAttribute("country",pm.getCountry());
	        	redirectAttributes.addFlashAttribute("uname",pm.getUser_name());
	    	 return "redirect:/dashboard";
	        
	    	
	    	
	    }
	    @RequestMapping("/fetch_profile")
	    public String fetch_profile(Model model,@Param("email") String email) throws IOException {
	    	System.out.println("------------"+email);
	    	ProfileModel rm=rs.getProfile(email);
	        System.out.println(rm);
	       
	        	model.addAttribute("email",rm.getEmail());
		        model.addAttribute("fname",rm.getF_name());
		        model.addAttribute("lname", rm.getL_name());
		        model.addAttribute("country",rm.getCountry());
		        model.addAttribute("uname",rm.getUser_name());
	        	return "dashboard";
	        
	           
	    }


	    //UPDATE USER 
	    @PostMapping("/dashboard/update_user")
	    public String update_users(@RequestParam("email") String email,@RequestParam("file") MultipartFile file,@RequestParam("inputId") int points,@RequestParam("sets") String set,RedirectAttributes redirectAttributes) throws IOException {
		 System.out.println(set);
		 set=set.replace(" ","");
		 String[] s=set.split(",");
		 HashSet<List<Integer>> listOfLists= new HashSet<List<Integer>>();
		 for(int i=1;i<s.length-1;i=i+2)
		 {
			 List<Integer> list1= new ArrayList<Integer>();
	        list1.add(Integer.valueOf(s[i]));
	        list1.add(Integer.valueOf(s[i+1]));
	        listOfLists.add(list1);
		 }
		 System.out.println(listOfLists);
	        
		 rs.addFile(email, file, points, listOfLists);
		 RegisterModel rmm=rs.getFile(email);
     	 redirectAttributes.addFlashAttribute("email", rmm.getEmail());
     	 return "redirect:/dashboard";
	    }
	    
	    //UPLOAD FILE
	    
	   
	    
	    @PostMapping("/upload")
	    public String uploadFiles(Model model,@RequestParam("email") String email,@RequestParam("files") MultipartFile[] files) throws IOException {
	      String message = "";
	      boolean flag=false;
	      System.out.println("files"+files);
	      try {
	        List<String> fileNames = new ArrayList<>();
	        Arrays.asList(files).stream().forEach(file -> {
	        	System.out.println("files....."+file.getOriginalFilename());
	          try {
				rs.save(email,file);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          fileNames.add(file.getOriginalFilename());
	          
	        });
	        flag=true;
	        
	      } catch (Exception e) {
	              
	      }
	      model.addAttribute("email",email);
	      if(flag)
	      {
	    	  message="Uploaded the files successfully: ";
	    	  model.addAttribute("message",  message);
	    	  System.out.println("flag.."+flag);
	      }	 
	      else
	      {
	    	  message="Fail to upload files!";
	    	  model.addAttribute("message", message);
	    	  System.out.println("flag"+flag);
	      }
	    	 
	        
      	return "dashboard";
	     
	    }
	 
	    @Autowired
		private FileRepo fu;
		FileListModel fm=new FileListModel();
	    @RequestMapping("/dashboard/fetch_file")
	    public ModelAndView fetch_file(Model model,@Param("email") String email) throws IOException {
	    	System.out.println("------------"+email);
//	    	List<GridFSFile> rm=rs.fetch_file(email);
//	    	System.out.println(rm.size());
//	    	return "dashboard";
	    	ModelAndView mav=new ModelAndView("dashboard");
	    	mav.addObject("files", fu.findById(email).get());
	    	return mav;
	    }
	    
	  
	    
	   


}
