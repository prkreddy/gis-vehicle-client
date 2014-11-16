package org.iiitb.api.rs.model;

import java.io.InputStream;
import java.util.List;


public class User
{
	String username;
	String name;
	String userType;
	String userId;
	String emailId;
	String password;

	List<String> defaultInterests; 

	InputStream photo;
	private String lastLoggedOn;
	public User()
	{
		
	}
	public User(String username, String password)
	{
		super();
		this.username = username;
		this.password = password;
		
	}

	public List<String> getDefaultInterests() {
		return defaultInterests;
	}
	public void setDefaultInterests(List<String> interests) {
		this.defaultInterests = interests;
	}
	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getEmailId()
	{
		return emailId;
	}

	public void setEmailId(String emailId)
	{
		this.emailId = emailId;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	public InputStream getPhoto()
	{
		return this.photo;
	}
	
	public void setPhoto(InputStream photo)
	{
		this.photo=photo;
	}

	public String getLastLoggedOn()
	{
		return lastLoggedOn;
	}

	public void setLastLoggedOn(String lastLoggedOn)
	{
		this.lastLoggedOn = lastLoggedOn;
	}
}
