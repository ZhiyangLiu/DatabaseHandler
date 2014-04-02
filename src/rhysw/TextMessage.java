package rhysw;

import java.util.Date;

public class TextMessage extends Message {
	
	public TextMessage(String identity) {
		super(identity);
	}
	
	public TextMessage(String identity, Date date) {
		super(identity, date);
	}
}
