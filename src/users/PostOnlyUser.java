package users;

import users.User.WrongKindOfUserException;

public class PostOnlyUser extends User
{
	
	public PostOnlyUser(String username)
	{
		super(username);
	}

	@Override
	public void getFollowees() throws WrongKindOfUserException
	{
		throw new WrongKindOfUserException("Post only users can't follow");
	}

	@Override
	public void followUser(String uname) throws WrongKindOfUserException
	{
		throw new WrongKindOfUserException("Post only users can't follow");
	}

	@Override
	public void unfollowUser(String uname) throws WrongKindOfUserException
	{
		throw new WrongKindOfUserException("Post only users can't follow");
	}
}
