package cs476hw1;

import java.util.ArrayList;

public class Tokenizer {

	private String text;
	private String tokens[];
	
	public Tokenizer(String text)
	{
		this.setText(text);  //upon construction, immediately store the text and tokenize
	}
	
	public String getText()
	{
		return this.text;
	}
	
	public void setText(String newText)
	{
		this.text = newText;
		this.tokenize();
	}
	
	public String[] getTokens()
	{
		return this.tokens;
	}
	
	private void tokenize()
	{
		String rawTokens[] = this.text.split("[^a-zA-Z]");  //split the text into a token array (String[]) by splitting at and ignoring non-alphabetical characters
		ArrayList<String> tokenList = new ArrayList<String>();  //since we are removing empty strings, we don't know the length of the remaining array, and we need to use an ArrayList
		
		for(int i = 0; i < rawTokens.length; i++)
		{	
			//if the string length is > 0, and it's not a one-character string except "I" or "a" (two valid words) then add it to the token list
			//otherwise it's junk and we can ignore it
			if (rawTokens[i].length() > 0 && !(rawTokens[i].length() == 1 && !(rawTokens[i].equalsIgnoreCase("i") || rawTokens[i].equalsIgnoreCase("a"))))  //if this array entry is empty, don't add it
				tokenList.add(rawTokens[i].trim().toLowerCase());			
		}
		
		this.tokens = tokenList.toArray(new String[0]);  //convert ArrayList<String> to String[] and return
	}

}
