import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;

public class DeliverableOne {
	
	public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
		CreateIndex("testFiles");
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(String.format("Program runtime: %d hours, %d minutes, %d seconds", 
        		TimeUnit.MILLISECONDS.toHours(totalTime),
        		TimeUnit.MILLISECONDS.toMinutes(totalTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalTime)),
        		TimeUnit.MILLISECONDS.toSeconds(totalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime))));
	}
	
	public static void CreateIndex(String filePath) {
		Gson gsonHTML = new GsonBuilder().create();
		HashMap<String,LinkedTreeMap<String,String>> htmlJSON = null;
		JsonReader reader = null;
		try {
			reader = new JsonReader(new FileReader("html_files.json"));
			htmlJSON = gsonHTML.fromJson(reader, HashMap.class);
		} catch (FileNotFoundException e) {
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
		
		ArrayList<String> stopWords = Utilities.tokenizeFile(new File("stopwords.txt"));
		//Map<String, ArrayList<Pair<String,Integer>>> reverseIndex = new HashMap<String, ArrayList<Pair<String,Integer>>>();
		//Sorted Debug
		int fileNumber = 0;
		int termID = 0;
		Map<Integer, TreeMap<Integer,Integer>> reverseIndex = new TreeMap<Integer, TreeMap<Integer,Integer>>();
		Map<String, Integer> termIDSMap = new TreeMap<String, Integer>();

		
		//Hashmap
//		Map<String, HashMap<Integer,Integer>> reverseIndex = new HashMap<String, HashMap<String,Integer>>();
//		Map<Integer, String> termIDs = new HashMap<Integer, String>();
		for (Entry<String,LinkedTreeMap<String,String>> docEntry : htmlJSON.entrySet()){
			File file = new File("Html/" + docEntry.getValue().get("file"));
			++fileNumber;
			Map<String,Integer> tokens = Utilities.tokenizeFileToMap(file);
			for(Entry<String,Integer> wordFreq : tokens.entrySet()){
				if(stopWords.contains(wordFreq.getKey())){
					continue;
				}

//				Integer termID = tokens.get(i).hashCode();
				if(!termIDSMap.containsKey(wordFreq.getKey())){
					termIDSMap.put(wordFreq.getKey(), ++termID);

				}
				//Treemap
//				if(termID == 110708){
//					System.out.println(tokens.toString());
//					System.out.println(wordFreq.toString());
//					break;
//				}
				Integer wordID = termIDSMap.get(wordFreq.getKey());
				reverseIndex.putIfAbsent(wordID, new TreeMap<Integer,Integer>());
				TreeMap<Integer, Integer> freqMap = reverseIndex.get(wordID);
				freqMap.put(Integer.parseInt(docEntry.getKey()), wordFreq.getValue());
//				TreeMap<Integer, Integer> freqMap = reverseIndex.get(termID);
//				freqMap.putIfAbsent(Integer.parseInt(entry.getKey()), 0);
//				Integer freq = freqMap.get(Integer.parseInt(entry.getKey()));
//				freqMap.put(Integer.parseInt(entry.getKey()), ++freq);
				
				//Hashmap
//				reverseIndex.putIfAbsent(tokens.get(i), new HashMap<String, Integer>());
//				HashMap<String, Integer> freqMap = reverseIndex.get(tokens.get(i));
//				freqMap.putIfAbsent(entry.getKey(), 0);
//				Integer freq = freqMap.get(entry.getKey());
//				freqMap.put(entry.getKey(), ++freq);
				
				
			}
			System.out.println("File #" + fileNumber + " completed.");
//			for(Entry<String, Integer> entry : freqMap.entrySet()) {
//				reverseIndex.putIfAbsent(entry.getKey(), new ArrayList<Pair<String,Integer>>());
//				ArrayList<Pair<String,Integer>> docList = reverseIndex.get(entry.getKey());
//				
//				docList.add(new Pair<String,Integer>(f.getName(), entry.getValue()));
//			}
//			
//			Set<Entry<String,ArrayList<Pair<String,Integer>>>> reverseSet = reverseIndex.entrySet();
			
		}
//		System.out.println(termIDSMap.get("banging"));
//		System.out.println(reverseIndex.get(110708).toString());
//		
		//Pretty Printing/Larger File Size
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		System.out.println(gson.toJson(reverseIndex));
		
		//Write JSON Object/Smaller File Size
//		Gson gson = new GsonBuilder().create();
		try(Writer writer = new FileWriter("termID.json")){
			gson.toJson(termIDSMap, writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try(Writer writer = new FileWriter("indexReadable.json")){
			gson.toJson(reverseIndex, writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Index file created.");
	}
}