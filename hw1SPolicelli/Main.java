package cs476hw1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Indexing inputted files...");
		long start = System.currentTimeMillis();  //start the timer
		
		if (args.length < 2)
		{
			System.out.println("2 arguments expected (paths to input and output directories). Exiting.");
			return;
		}
		
		//first argument is input directory, second is output directory
		String inputDir = args[0];
		String outputDir = args[1];
		
		//make necessary directories for program output
		new File(outputDir + "/tokenized").mkdirs();
		
		Index index = new Index();
		
		//for all files in the input directory:
		File htmlFiles[] = new File(inputDir).listFiles();
		
		System.out.println("Initialization took " + (System.currentTimeMillis() - start) + " ms.");
		
		for(int i = 0; i < htmlFiles.length; i++)
		{
			Document doc;
			String textContent = "";
			try
			{
				doc = Jsoup.parse(htmlFiles[i]);  //Jsoup parse the HTML document
				Element html = doc.getElementsByTag("html").first();  //get the HTML tag
				Document textAndBadTags = Jsoup.parse(html.text());  //in document 107, some bad tags aren't parsed correctly by Jsoup, so it parses the HTML text one more time to clean it up
				textContent = textAndBadTags.text();
				/*
				if (htmlFiles[i].getPath().contains("045")) //045, 107 have no BODY tag and very little text
					System.out.println(textAndBadTags.text());
				//*/
			}
			catch (IOException e)
			{
				System.out.println("IO Error in File " + htmlFiles[i].getPath());
				e.printStackTrace();
			}
			catch(Exception e)
			{
				System.out.println("Error parsing text of " + htmlFiles[i].getPath());
				e.printStackTrace();
			}
			
			//write a file with the raw list of all tokens in it
			String inputFilename = htmlFiles[i].getName();
			String tokenizedFilepath = outputDir + "/tokenized/tokenized" + inputFilename.substring(0, inputFilename.length() - 5) + ".txt";
			//System.out.println(tokenizedFilepath);
			FileWriter tokenizedFile;
			
			//tokenize all text content from the raw text string
			Tokenizer t = new Tokenizer(textContent);
			String tokens[] = t.getTokens();
			
			try
			{
				new File(tokenizedFilepath).delete();  //delete the file if exists
				tokenizedFile = new FileWriter(tokenizedFilepath, true);
				for(int j = 0; j < tokens.length; j++)
				{
					tokenizedFile.write(tokens[j] + "\n");  //write each token
				}
				tokenizedFile.close();
			}
			catch(Exception e)
			{
				System.out.println("Error writing to " + tokenizedFilepath);
				e.printStackTrace();
			}
			
			//add tokens to index data structure
			index.addTokens(tokens);
			if (i % 50 == 0)
				System.out.println("" + (i+1) + " files indexed... " + (System.currentTimeMillis() - start) + " ms elapsed.");
		}
		
		//write index files, one sorted by token alphabetically, the other by frequency
		FileWriter tokenIndexFile, freqIndexFile;
		String tokenFilepath = outputDir + "/tokensTokenSorted.txt";
		String freqFilepath = outputDir + "/tokensFreqSorted.txt";
		
		try
		{
			new File(tokenFilepath).delete();  //delete file if already exists
			tokenIndexFile = new FileWriter(tokenFilepath, true);  //open for writing
			String tokensTokenSorted[] = index.printIndexByToken(); //get a string array of tokens and their occurrences (sorted by the token value alphabetically)
			for(int j = 0; j < tokensTokenSorted.length; j++)
			{
				tokenIndexFile.write(tokensTokenSorted[j] + "\n");  //write each token entry
			}
			tokenIndexFile.close();
			
			new File(freqFilepath).delete();  //delete file if already exists
			freqIndexFile = new FileWriter(freqFilepath, true);
			String tokensFreqSorted[] = index.printIndexByFrequency();  //get a string array of tokens and their occurrences (sorted by the number of occurrences, high to low)
			for(int j = 0; j < tokensFreqSorted.length; j++)
			{
				freqIndexFile.write(tokensFreqSorted[j] + "\n");  //write each token entry
			}
			freqIndexFile.close();
		}
		catch(Exception e)
		{
			System.out.println("Error writing to " + tokenFilepath + " and/or " + freqFilepath);
			e.printStackTrace();
		}
		
		System.out.println("Tokenizing and indexing took " + (System.currentTimeMillis() - start) + " ms.");
	}
}
