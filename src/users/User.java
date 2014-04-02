package users;

import java.util.ArrayList;
import java.util.Date;

import groupProject.jamie.DatabaseHandler;
import groupProject.jamie.DatabaseHandler.PostDetails;
import groupProject.jamie.DatabaseHandler.UserDetails;
import rhysw.*;

/*
 * Jamie 30/03/14
 */
public class User
{	
	
	public class WrongKindOfUserException extends Exception 
	{
		private static final long serialVersionUID = 1L;
		public WrongKindOfUserException(String name)
		{
			super(name);
		}
	}
	
	public DatabaseHandler dh;
	
	private String username;
	public void setUsername(String l) { username = l; };
	public String getUsername() { return username; };
	
	private String firstname;
	public void setFirstName(String l) { firstname = l; };
	public String getFirstname() { return firstname; };
	
	private String lastname;
	public void setLastName(String l) { lastname = l; };
	public String getLastname() { return lastname; };
	
	private Date DOB;
	public Date getDOB() { return DOB; };
	
	private String city;
	public void setCity(String city) { this.city = city; };
	public String getCity() { return this.city; };
	
	private Date lastLoggedIn;
	public Date getLastLoggedIn() { return lastLoggedIn; };
	
	public String[] followers;
	public String[] requestedFollowers;
	public String[] followees;
	
	public ArrayList<Message> messages;
	
	public User(String username)
	{
		dh = new DatabaseHandler(username);
		
	}
	
	public void setDetails(String firstname, String lastname, String city, Date birthday)
	{
		this.firstname = firstname;
		this.lastname = lastname;
		this.city = city;
		this.DOB = birthday;
	}
	
	public void saveUserDetails()
	{
		dh.setUserDetails(firstname, lastname, city, new java.sql.Date(DOB.getTime()));
	}
	
	public void loadUserDetails()
	{
		UserDetails u = dh.getUserDetails();
		firstname = u.firstname;
		lastname = u.lastname;
		DOB = u.birthday;
		city = u.city;
		lastLoggedIn = u.lli;
	}
	
	public void newMessage(Message m) throws WrongKindOfUserException
	{
		boolean isHyperlink = (m instanceof HyperlinkMessage)? true : false;
		String[] contentArray = m.getContent();
		String content = contentArray[0];
		
		if (contentArray.length == 2)
		{
			content += "<###>"+contentArray[1];
		}
		
		dh.makeNewPost(content, isHyperlink);
	}
	
	public void getMessages()
	{
		ArrayList<PostDetails> pd = dh.getAllPosts();
		ArrayList<Message> messages = new ArrayList<Message>();
		
		for (PostDetails p : pd)
		{
			if (p.isUrl())
			{
				HyperlinkMessage hm = new HyperlinkMessage(p.getOwner(), p.getDate());
				String[] content = p.getContent().split("<###>");
				hm.setContent(content[0], content[1]);
				messages.add(hm);
			}
			else
			{
				TextMessage tm = new TextMessage(p.getOwner(), p.getDate());
				tm.setContent(p.getContent());
				messages.add(tm);
			}
		}
		this.messages = messages;
	}
	
	public void getFollowers() throws WrongKindOfUserException
	{
		followers = dh.getFollowers();
	}
	
	public void getRequestsFollowers() throws WrongKindOfUserException
	{
		requestedFollowers = dh.getFollowRequests();
	}
	
	public void acceptFollowers(String[] usernames) throws WrongKindOfUserException
	{
		dh.acceptFollowerRequests(usernames);
	}
	
	public void getFollowees() throws WrongKindOfUserException
	{
		followees = dh.getFollowedUsers();
	}
	
	public void followUser(String uname) throws WrongKindOfUserException
	{
		dh.requestFollow(uname);
	}
	
	public void unfollowUser(String uname) throws WrongKindOfUserException
	{
		dh.stopFollow(uname);
	}
	
}
