package rhysw;

import java.util.Date;


public class HyperlinkMessage extends Message {
	
	private String hyperDescription = "";
	
	public HyperlinkMessage(String identity) {
		super(identity);
	}
	
	public HyperlinkMessage(String identity, Date date) {
		super(identity, date);
	}
	
	public void setContent(String content, String hyperDescription) {
		super.setContent(content);
		this.hyperDescription = hyperDescription;
	}
	public String[] getContent() {
		String[] arr = super.getContent();
		arr[1] = this.hyperDescription;
		return arr;
	}
	
}

