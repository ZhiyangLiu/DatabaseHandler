package groupProject.jamie;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;


/**
 * 
 * @author Jamiebuckley
 * @version 1.1
 */
public class DatabaseHandler 
{
	public class PostDetails
	{
		int primaryKey;
		private String owner;
		private boolean isUrl;
		private String content;
		private String date;
		
		public PostDetails(int PK, String owner, boolean isUrl, String date, String content)
		{
			this.primaryKey = PK;
			this.owner = owner;
			this.isUrl = isUrl;
			this.date = date;
			this.content = content;
		}
		
		public String getOwner()
		{
			return owner;
		}
		public boolean isUrl()
		{
			return isUrl;
		}
		public String getContent()
		{
			return content;
		}
		
		public String getDate()
		{
			return date;
		}
		
		public int getPrimaryKey()
		{
			return primaryKey;
		}
	}
	
	
  private final String connectedUser;
  private static String databasesDirectory = ".";
  
  /**
   * Creates an instance of type DatabaseHandler.  Takes a String representing a logged in user.  Certain commands
   * can only be perfomed on the logged in user, for security reasons.
   * @param username The username (as a string) that represents the logged in user.
   */
  public DatabaseHandler(final String username)
  {
	  try
	  {
		Class.forName("org.sqlite.JDBC");
	  } 
	  catch (ClassNotFoundException e)
	  {
		e.printStackTrace();
	  }
	  
	  this.connectedUser = username;
	  
	  if (!doesUsernameExist(username))
	  {
		  createNewUser(username);  
	  }
  }
  
  /**
   * Sets the database directory string that will be added to database names.  The format of this string is Unix directory traversal from the root of the program's executing folder.  Therefore, a folder stored inside that root folder would be passed in the form "./<foldername>/"
   * @param directory The directory that the databases will be stored in.  Represented by standard Unix directory movement.  Default is "."
   */
  public static void setDatabasesDirectory(final String directory)
  {
	  databasesDirectory=directory;
  }

  /**
   * 
   * @return A string array of all users that exist in the system.
   */
  public static String[] getAllUsers() 
  {
	  File folder = new File(databasesDirectory);
	  File[] listFiles = folder.listFiles();
	  String[] results = new String[listFiles.length];
	  int count = 0;
	  
	  for (File f : listFiles)
	  {
		  if (!f.isFile())
		  {
			  continue;
		  }
		  String n = f.getName();
		  int i = -1;
		  if ((i = (n.lastIndexOf('.'))) <= 0)
		  {
			  continue;
		  }
		  if (n.substring(i+1).equals("db"))
		  {
			  results[count]=n.substring(0, i);
		  }
		  count++;
	  }
	  
	  String[] trimResults = new String[count];
	  System.arraycopy(results, 0, trimResults, 0, count);
	  return trimResults;
  }

  /**
   * @param Username The username to check the existence of, as a string.
   * @return A boolean value of 1 (Exists) or 0 (Does not exist).
   */
  public static Boolean doesUsernameExist(String Username) 
  {
	  File f = new File(databasesDirectory+Username+".db");
	  return (f.exists() && !f.isDirectory());
  }
  
 
  //********************************
  //Code relating to logged in user
  //*******************************
  
  public void setUserDetails(String city, int birthday, int birthmonth, int birthyear)
  {
	  
	  String updateDetailsSQL = "INSERT OR REPLACE INTO user_data (ID, CITY, BIRTHDAY, BIRTHMONTH, BIRTHYEAR) VALUES (0, '"
	  		+ city + "'," + birthday + "," + birthmonth + "," + birthyear + ");";
	  connectAndExecute(connectedUser, updateDetailsSQL);
  }
  
  public class UserDetails
  {
	  public final String city;
	  public final int birthday;
	  public final int birthmonth;
	  public final int birthyear;
	  
	  public UserDetails(String city, int birthday, int birthmonth, int birthyear)
	  {
		  this.city = new String(city);
		  this.birthday = birthday;
		  this.birthmonth = birthmonth;
		  this.birthyear = birthyear;
	  }
  }
  
  public UserDetails getUserDetails()
  {
	  String getDetailsSQL = "SELECT CITY, BIRTHDAY, BIRTHMONTH, BIRTHYEAR from user_data WHERE ID=0;";
	  ResultSet dSet = connectExecuteAndGet(connectedUser, getDetailsSQL);
	  
	  UserDetails details = null;
	  try 
	  {
		details = new UserDetails(dSet.getString("CITY"), dSet.getInt("BIRTHDAY"), dSet.getInt("BIRTHMONTH"), dSet.getInt("BIRTHYEAR"));
	  } 
	  catch (SQLException e) 
	  {
		e.printStackTrace();
	  }
	  try 
	  {
		dSet.close();	
	  } 
	  catch (SQLException e) 
	  {
		e.printStackTrace();
	  }
	  return details;
  }
  
  /**
   * 
   * @return A string array representing all users that have requested to follow the logged in user (passed in constructor).
   */
  public String[] getFollowRequests()
  {
	  String followRequestSQL = "SELECT * FROM user_followers WHERE ACCEPTED=0;";
	  ResultSet followRequests = connectExecuteAndGet(connectedUser, followRequestSQL);
	  ArrayList<String> results = new ArrayList<String>();
	  try
	  {
		  while(followRequests.next())
		  {
			  String username = followRequests.getString("USER_NAME");
			  results.add(username);
		  }
	  } 
	  catch (SQLException e)
	  {
		  e.printStackTrace();
		  return null;
	  }
	  String[] resultArray = new String[results.size()];
	  resultArray = results.toArray(resultArray);
	  return resultArray;
  }

  /**
   * 
   * @return A string array representing all users that are currently following the logged in user.
   */
  public String[] getFollowers() 
  {
	  String followRequestSQL = "SELECT USER_NAME FROM user_followers WHERE ACCEPTED=1;";
	  ResultSet followRequests = connectExecuteAndGet(connectedUser, followRequestSQL);
	  ArrayList<String> results = new ArrayList<String>();
	  try
	  {
		  while(followRequests.next())
		  {
			  String username = followRequests.getString("USER_NAME");
			  results.add(username);
		  }
	  } 
	  catch (SQLException e)
	  {
		  e.printStackTrace();
		  return null;
	  }
	  String[] resultArray = new String[results.size()];
	  resultArray = results.toArray(resultArray);
	  return resultArray;
  }
  
  /**
   * 
   * @return A string array representing the users that the logged in user is following.
   */
  public String[] getFollowedUsers()
  {
	  String followRequestSQL = "SELECT USER_NAME FROM user_followed_users WHERE ACCEPTED=1;";
	  ResultSet followRequests = connectExecuteAndGet(connectedUser, followRequestSQL);
	  ArrayList<String> results = new ArrayList<String>();
	  try
	  {
		  while(followRequests.next())
		  {
			  String username = followRequests.getString("USER_NAME");
			  results.add(username);
		  }
	  } 
	  catch (SQLException e)
	  {
		  e.printStackTrace();
		  return null;
	  }
	  String[] resultArray = new String[results.size()];
	  resultArray = results.toArray(resultArray);
	  return resultArray;
  }

  /**
   * 
   * @param usernames A string array of all the users to grant a follower relationship to.
   */
  public void acceptFollowerRequests(String[] usernames) 
  {
	  if (usernames.length == 0)
	  {
		  return;
	  }
	  
	  String followAcceptSQL = "UPDATE user_followers SET ACCEPTED=1 WHERE ACCEPTED=0 AND ";
	  
	  for (int i = 0; i < usernames.length; i++)
	  {
		  followAcceptSQL += "USER_NAME='"+usernames[i]+"'";
		  if (i < usernames.length-1)
			  followAcceptSQL += " OR ";
	  }
	  followAcceptSQL += ";";
	  
	  connectAndExecute(connectedUser, followAcceptSQL);
	  
	  for (String s : usernames)
	  {
		  String followerUpdateSQL = "UPDATE user_followed_users SET ACCEPTED=1 WHERE ACCEPTED=0 AND USER_NAME='"+connectedUser+"';";
		  connectAndExecute(s, followerUpdateSQL);
	  }
  }

  /**
   * 
   * @param usernames A string array of all the users to deny a follower relationship to.
   */
  public void denyFollowerRequests(String[] usernames) 
  {
	  String followAcceptSQL = "DELETE FROM user_followers WHERE ACCEPTED=0 AND ";
	  
	  for (int i = 0; i < usernames.length; i++)
	  {
		  followAcceptSQL += "USER_NAME='"+usernames[i]+"'";
		  if (i < usernames.length-1)
			  followAcceptSQL += " OR ";
	  }
	  followAcceptSQL += ";";
	  
	  connectAndExecute(connectedUser, followAcceptSQL);
	  
	  for (String s : usernames)
	  {
		  String followerUpdateSQL = "DELETE FROM user_followed_users WHERE ACCEPTED=0 AND USER_NAME='"+connectedUser+"';";
		  connectAndExecute(s, followerUpdateSQL);
	  }
  }

  /**
   * 
   * @param username Causes the connected user to request a follower relationship with the user designated by username.
   */
  public void requestFollow(String username) 
  {
	  String otherUserSQL = "INSERT OR IGNORE INTO user_followers (USER_NAME, ACCEPTED) VALUES ('" + connectedUser + "'," + "0);";
	  connectAndExecute(username, otherUserSQL);
	  
	  String thisUserSQL = "INSERT OR IGNORE INTO user_followed_users (USER_NAME, ACCEPTED) VALUES ('" + username + "'," + "0);";
	  connectAndExecute(connectedUser, thisUserSQL);
  }

  /**
   * 
   * @param username The username of the user to stop following.  Does nothing if user is not already being followed.
   */
  public void stopFollow(String username) 
  {
	  String stopFollowSQL = "DELETE FROM user_followed_users WHERE ACCEPTED=1 AND USER_NAME='"+username+"';";
	  connectAndExecute(connectedUser, stopFollowSQL);
	  String otherUserStopFollow = "DELETE FROM user_followers WHERE ACCEPTED=1 AND USER_NAME='"+connectedUser+"';";
	  connectAndExecute(username, otherUserStopFollow);
  }

  /**
   * 
   * @param postContent The content of the post, as a string.
   * @param isLink True if the post is to be styled as an HTML link, false otherwise.
   */
  public void makeNewPost(String postContent, boolean isLink) 
  {
	  makeNewPost(postContent, getFollowers(), isLink);
  }

  
  /**
   * 
   * @param postContent tThe content of the post, as a string.
   * @param followers An array of usernames to send this post to.
   * @param isLink True if the post is to be styled as an HTML link, false otherwise.
   */
  public void makeNewPost(String postContent, String[] followers, boolean isLink) 
  {
	  int link=(isLink)?1:0;
	  String PostSQL = "INSERT INTO user_honks (OWNER, HYPERLINK, CONTENT) VALUES ('"+connectedUser+"',"+link+",'"+postContent+"');";
	  ArrayList<Integer> keys = connectExecuteAndGetKeys(connectedUser, PostSQL);
	  int postKey = keys.get(0);
	  String otherUserPostSQL = "INSERT INTO user_followed_user_honks (OWNER, FOREIGNKEY) VALUES ('"+connectedUser+"',"+postKey+");";
	  
	  for (String s : followers)
	  {
		  connectAndExecute(s, otherUserPostSQL);
	  } 
  }

  /**
   * 
   * @return An array of PostDetails of all the posts visible by the connectedUser
   */
  public ArrayList<PostDetails> getAllPosts() 
  {
	  ArrayList<PostDetails> postResults = new ArrayList<PostDetails>();
	  
	  String[] followedUsers = getFollowedUsers();
	  
	  for (String s : followedUsers)
	  {
		  ArrayList<PostDetails> userPosts = getAllPosts(s);
		  postResults.addAll(userPosts);
	  }
	  postResults.addAll(getAllPosts(connectedUser));
	  return postResults;
  }

 /**
  * 
  * @param username The username to scan for posts.
  * @return All posts that have been made by the passed in user.
  */
  public ArrayList<PostDetails> getAllPosts(String username) 
  {
	  ArrayList<PostDetails> postResults = new ArrayList<PostDetails>();
	  String userPostsSQL = "SELECT ID,OWNER,HYPERLINK,CONTENT FROM user_honks;";
	  ResultSet userPosts = connectExecuteAndGet(username, userPostsSQL);
	  try
	  {
		  while(userPosts.next())
		  {
			  int PK = userPosts.getInt("ID");
			  String owner = userPosts.getString("OWNER");
			  Boolean isLink = userPosts.getBoolean("HYPERLINK");
			  String content = userPosts.getString("CONTENT");
			  String date = "NOT DONE YET";
			  PostDetails pd = new PostDetails(PK, owner, isLink, date, content);
			  postResults.add(pd);
		  }
		  return postResults;
	  }
	  catch (SQLException e)
	  {
		  return null;
	  }
  }

  /**
   * Removes the post indicated by thePostKey
   * @param thePostKey The post key (Accessible from PostDetails) to remove.
   */
  public void removePost(int thePostKey) 
  {
	  String deletePostSQL = "DELETE FROM user_honks WHERE ID="+thePostKey+";";
	  connectAndExecute(connectedUser, deletePostSQL);
  }
  
  /**
   * Connects to a database indicated by username, and executes the passed in SQL
   * @param username The username of the database
   * @param SQL The SQL to execute
   */
  private void connectAndExecute(String username, String SQL)
  {
	  Connection connection = null;
	  Statement smnt = null;
	  
	  try
	  {
		  connection = DriverManager.getConnection("jdbc:sqlite:" + databasesDirectory + username + ".db");
		  smnt = connection.createStatement();
		  smnt.executeUpdate(SQL);
	  }
	  catch(SQLException e)
	  {
		  e.printStackTrace();
		  return;
	  } 
	  finally
	  {
		  
	  }
  }
  
  /**
   * 
   * @param username The username of the database
   * @param SQL The SQL to execute
   * @return Gets the keys of the affected cells
   */
  private ArrayList<Integer> connectExecuteAndGetKeys(String username, String SQL)
  {
	  Connection connection = null;
	  Statement smnt = null;
	  ResultSet res;
	  
	  try
	  {
		  connection = DriverManager.getConnection("jdbc:sqlite:" + databasesDirectory + username + ".db");
		  smnt = connection.createStatement();
		  smnt.executeUpdate(SQL);
		  res = smnt.executeQuery("SELECT last_insert_rowid();");
		  ArrayList<Integer> resultKeys = new ArrayList<Integer>();
		  while(res.next())
		  {
			  resultKeys.add(res.getInt(1));
		  }
		  return resultKeys;
	  }
	  catch(SQLException e)
	  {
		  e.printStackTrace();
		  return null;
	  }  
  }
  
  /**
   * 
   * @param username The username of the database
   * @param SQL The SQL to execute
   * @return The resultset returned by the SQL query.
   */
  private ResultSet connectExecuteAndGet(String username, String SQL)
  {
	  Connection connection = null;
	  Statement smnt = null;
	  ResultSet res = null;
	  
	  try
	  {
		  connection = DriverManager.getConnection("jdbc:sqlite:" + databasesDirectory + username + ".db");
		  smnt = connection.createStatement();
		  res = smnt.executeQuery(SQL);
	  }
	  catch(SQLException e)
	  {
		  e.printStackTrace();
		  return null;
	  }  
	  return res;
  }

  /**
   * Creates all tables necessary for a new user.
   * @param username The username for the database that will be created as <username>.db
   */
  private void createNewUser(final String username)
  {	  
	  String createUserSQL = "CREATE TABLE user_data (ID INTEGER PRIMARY KEY, CITY TEXT, BIRTHDAY INTEGER, BIRTHMONTH INTEGER, BIRTHYEAR INTEGER);";
	  String createUserPostsSQL = "CREATE TABLE user_honks (ID INTEGER PRIMARY KEY, OWNER TEXT NOT NULL, HYPERLINK INTEGER NOT NULL, CONTENT TEXT);";
	  String createUserFollowedPostsSQL = "CREATE TABLE user_followed_user_honks (ID INTEGER PRIMARY KEY, OWNER TEXT NOT NULL, FOREIGNKEY INTEGER NOT NULL);";
	  String followedUsersSQL = "CREATE TABLE user_followed_users(ID INTEGER PRIMARY KEY, USER_NAME TEXT NOT NULL UNIQUE, ACCEPTED INTEGER NOT NULL);";
	  String userFollowersSQL = "CREATE TABLE user_followers(ID INTEGER PRIMARY KEY, USER_NAME TEXT NOT NULL UNIQUE, ACCEPTED INTEGER NOT NULL);";
	  connectAndExecute(username, createUserSQL+createUserPostsSQL+createUserFollowedPostsSQL+followedUsersSQL+userFollowersSQL);
	  
  }
}