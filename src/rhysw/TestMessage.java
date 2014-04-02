package rhysw;

import java.util.Date;

public class TestMessage 
{
	public static void main(String[] args) {
		Date testDate = new Date();
		TextMessage test = new TextMessage("johnny");
		TextMessage otherTest = new TextMessage("boris");
		HyperlinkMessage hypTest = new HyperlinkMessage("billy");
		HyperlinkMessage otherHypTest = new HyperlinkMessage("george");
		
		test.setContent("testing 1,2,3");
		otherTest.setContent("other test 4,5,6");
		
		hypTest.setContent("www.testing123.com", "testing123");
		otherHypTest.setContent("www.testing456.com", "testing456");
		
		String[] testy = test.getContent();
		for (int i = 0; i < testy.length; i++)
		{
			System.out.println("testy" + testy[i]);		  
		}
		
		String[] testy2 = otherTest.getContent();
		for (int i = 0; i < testy2.length; i++)
		{
			System.out.println("testy2" + testy2[i]);		  
		}
		
		String[] testy3 = hypTest.getContent();
		for (int i = 0; i < testy3.length; i++)
		{
			System.out.println("testy3 " + testy3[i]);	
		}

		String[] testy4 = otherHypTest.getContent();
		for (int i = 0; i < testy4.length; i++)
		{
			System.out.println("testy4 " + testy4[i]);	
		}

	}
}
