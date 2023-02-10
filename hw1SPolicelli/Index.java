package cs476hw1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

public class Index
{	
	private HashMap<String, Long> map;
	
	public Index()
	{
		this.map = new HashMap<String, Long>();  //initialize the hash map
	}
	
	public Index(String[] tokens)
	{
		this();  //call empty constructor to initialize the hash map
		this.addTokens(tokens);
	}
	
	public void addTokens(String[] tokens)
	{
		for(int i = 0; i < tokens.length; i++)
		{
			//for each token, check if it already exists as a key in the map
			Long l = Long.valueOf(0);
			if (this.map.containsKey(tokens[i]))
			{
				l = this.map.get(tokens[i]);  //if it does exist, get the number of occurrences 
				//otherwise, occurrences is zero
			}
			
			l++; //increment occurrences
			this.map.put(tokens[i], l);  //put this into the map
		}
	}
	
	private ArrayList<Entry<String, Long>> getIndexList()
	{
		//convert the Map to an ArrayList of Entry objects (for iterating over)
		ArrayList<Entry<String, Long>> indexList = new ArrayList<Entry<String, Long>>();
		this.map.entrySet().forEach((entry) -> {
			indexList.add(entry);  //add each entry from the map into this ArrayList (in any order; we will sort this later if/when needed)
		});
		
		return indexList;
	}
	
	private String[] printIndexList(ArrayList<Entry<String, Long>> indexList)
	{
		String printed[] = new String[indexList.size()];
		
		//for each item, in-order, in the ArrayList of Entrys
		for(int i = 0; i < indexList.size(); i++)
		{
			Entry<String, Long> indexEntry = indexList.get(i);
			printed[i] = indexEntry.getKey() + ": " + indexEntry.getValue().toString();  //"<token>: <occurrences>"
		}
		
		return printed;
	}
	
	public String[] printIndexByToken()
	{
		ArrayList<Entry<String, Long>> indexList = this.getIndexList();
		
		//sort the ArrayList by the entry key value (token value) alphabetically, ascending (A-Z)
		Collections.sort(indexList, Comparator.comparing((Entry<String, Long> entry) -> entry.getKey()));
		
		return this.printIndexList(indexList); //print the sorted array
	}
	
	public String[] printIndexByFrequency()
	{
		ArrayList<Entry<String, Long>> indexList = this.getIndexList();
		
		//sort the ArrayList by the number of occurrences, descending (maximum - minimum)
		Collections.sort(indexList, Comparator.comparing((Entry<String, Long> entry) -> -1 * entry.getValue()));
		
		return this.printIndexList(indexList);  //convert sorted array from Entry ArrayList to String[]
	}
}
