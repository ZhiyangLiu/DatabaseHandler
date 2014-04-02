package groupProject.jamie;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import rhysw.Message;
import rhysw.TextMessage;
import users.User;
import users.User.WrongKindOfUserException;

public class basicMain
{
	public static void main(String[] args)
	{
		DatabaseHandler.setDatabasesDirectory("databases/");
		User u = new User("Bob");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date bday =
		null;
		try
		{
			bday = sdf.parse("28-04-1989");
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		u.setDetails("Robert", "Bobson", "Sydney", bday);
		u.saveUserDetails();
		u.loadUserDetails();
		TextMessage m = new TextMessage("Bob", new java.util.Date());
		m.setContent("Hi there, this is a first message!");
		try
		{
			u.newMessage(m);
		}
		catch (WrongKindOfUserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		u.getMessages();
		
		for (Message mess : u.messages)
		{
			System.out.println(mess.getContent()[0]);
		}
	}
	
	/*
	public static void main(String[] args)
	{
		DatabaseHandler.setDatabasesDirectory("databases/");
		
		DatabaseHandler dH=new DatabaseHandler("Bob");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date bday =
		null;
		try
		{
			bday = sdf.parse("28-04-1989");
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dH.setUserDetails("Robert", "Smith", "Sydney", new Date(bday.getTime()));
		
		DatabaseHandler.UserDetails uD = dH.getUserDetails();
		System.out.println(uD.city + " " + uD.birthday.toString());
		
		DatabaseHandler dH2 = new DatabaseHandler("Johnny");
		
		
		String[] johnnyFollowers = dH2.getFollowers();
		System.out.println("Now following Johnny:");
		for(String s : johnnyFollowers)
		{
			System.out.println(s);
		}
		
		dH.requestFollow("Johnny");
		String[] johnnyRequests = dH2.getFollowRequests();
		System.out.println("Requesting to be friends with Johnny:");
		for(String s : johnnyRequests)
		{
			System.out.println(s);
		}
		
		dH2.acceptFollowerRequests(johnnyRequests);
		
		johnnyFollowers = dH2.getFollowers();
		System.out.println("Now following Johnny:");
		for(String s : johnnyFollowers)
		{
			System.out.println(s);
		}
		
		String[] bobisFollowing = dH.getFollowedUsers();
		
		System.out.println("Bob is following:");
		
		for (String s : bobisFollowing)
		{
			System.out.println(s);
		}
		
		dH2.makeNewPost("Hi this is my first post!", false);
		
		
		ArrayList<DatabaseHandler.PostDetails> johnnyPosts = dH2.getAllPosts();
		System.out.println();
		System.out.println("All posts seen by Johnny:");
		for (DatabaseHandler.PostDetails pd : johnnyPosts)
		{
			System.out.println(pd.getOwner() + " said '" + pd.getContent() + "' at " + pd.getDate() );
			dH2.removePost(pd.getPrimaryKey());
		}
		
		
		ArrayList<DatabaseHandler.PostDetails> bobPosts = dH.getAllPosts();
		System.out.println();
		System.out.println("All posts seen by Bob:");
		for (DatabaseHandler.PostDetails pd : bobPosts)
		{
			System.out.println(pd.getOwner() + " said '" + pd.getContent() + "' at " + pd.getDate() );
		}
		
		System.out.println("End program");
		
	}
	*/
}
