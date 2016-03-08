package SearchEngine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;

import Index.Utilities;

public class DeliverableTwo {
	
	private static Map<String, LinkedTreeMap<String,String>> htmlJSON = null;
	private static Map<String, Double> termsIDSMap = null;
	private static Map<String, LinkedTreeMap<String, Double>> reverseIndex = null;

	private static void readJSON(){
		Gson gson = new GsonBuilder().create();
		JsonReader reader = null;
		try {
			reader = new JsonReader(new FileReader("html_files.json"));
			htmlJSON = gson.fromJson(reader, HashMap.class);
			reader.close();
			reader = new JsonReader(new FileReader("termID.json"));
			termsIDSMap = gson.fromJson(reader, TreeMap.class);
			reader.close();
			reader = new JsonReader(new FileReader("index.json"));
			reverseIndex = gson.fromJson(reader, HashMap.class);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private static ArrayList<Double> cosineScore(ArrayList<String> query){
		ArrayList<Double> relevantDocs = new ArrayList<Double>();
		Map<String, Double> scores = new HashMap<String, Double>();
		for(String term : query){
			Integer termID = termsIDSMap.get(term).intValue();
			LinkedTreeMap<String, Double> postingsList = reverseIndex.get(termID.toString());
			int docFreq = postingsList.size();
			//Weight in query later
			for (Entry<String, Double> docEntry : postingsList.entrySet()){
				String docID = docEntry.getKey();
				Double tf_idf = docEntry.getValue();
				scores.putIfAbsent(docID, 0d);
				Double score = scores.get(docID);
				scores.put(docID, score + tf_idf);
			}
		}
		for(Entry<String, Double> entry : scores.entrySet()){
			System.out.println(entry.toString());
		}
		//normalize with magnitude later
		return relevantDocs;
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		readJSON();
		while (true){
			System.out.print("Enter search query: ");
			String query = s.nextLine();
			ArrayList<String> tokens = Utilities.tokenizeQuery(query);
			System.out.println(tokens.toString());
			cosineScore(tokens);
		}
//		s.close();
	}
}