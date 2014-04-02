package rhysw;

import java.util.Date;

public abstract class Message {

	public final String identity;
	public final Date date;
	private String content;
	
	public Message(String identity) 
	{
		this.identity = identity;
		this.date = new Date();
	}
	
	public Message(String identity, Date date)
	{
		this.identity = identity;
		this.date = date;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String[] getContent() {
		String[] arr = new String[1];
		arr[0] = this.content;
		return arr;
	}
}
