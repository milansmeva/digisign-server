package com.infosys.digitalsignage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.digitalsignage.DAL.UserDAL;
import com.infosys.digitalsignage.Models.UserDetails;

@RestController
@RequestMapping("user")
public class UserController {
	
	@RequestMapping(path="adduser",method=RequestMethod.POST)
	public boolean AddUser(@RequestBody UserDetails user_det) {
		try {
			UserDAL userDAL= new UserDAL();
			return userDAL.AddUser(user_det);
		}
		catch(Exception ex) {
			
		}
		return false;
	}
	
	@RequestMapping(path="authenticateuser",method=RequestMethod.POST)
	public boolean AuthenticateUser(@RequestBody UserDetails user_det) {
		try {
			UserDAL userDAL= new UserDAL();
			return userDAL.AuthenticateUser(user_det);
		}
		catch(Exception ex) {
			
		}
		return false;
	}
}
