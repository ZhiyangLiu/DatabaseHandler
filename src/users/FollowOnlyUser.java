package users;

import rhysw.Message;

public class FollowOnlyUser extends User
{

	public FollowOnlyUser(String username)
	{
		super(username);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void newMessage(Message m) throws WrongKindOfUserException
	{
		throw new WrongKindOfUserException("Follow only users can't send messages");
	}

}
