//Harry Pham 79422112 David Gonzalez 50765033
package Index;

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
		//Sorted Debug
		int fileNumber = 0;
		int termID = 0;
//		Map<Integer, Map<Integer,Float>> reverseIndex = new TreeMap<Integer, Map<Integer,Float>>();
//		Map<String, Integer> termIDSMap = new TreeMap<String, Integer>();

		
		//Hashmap
		Map<Integer, Map<Integer,Float>> reverseIndex = new HashMap<Integer, Map<Integer,Float>>();
		Map<String, Integer> termIDSMap = new HashMap<String, Integer>();

		for (Entry<String,LinkedTreeMap<String,String>> docEntry : htmlJSON.entrySet()){
			File file = new File("Html/" + docEntry.getValue().get("file"));
//			System.out.println(docEntry.getValue());
			++fileNumber;
			System.out.println(String.format("Indexing file number %d.", fileNumber));
			Map<String,Float> tokens = Utilities.tokenizeFileBodyToMap(file);
			for(Entry<String,Float> wordFreq : tokens.entrySet()){
				if(stopWords.contains(wordFreq.getKey())){
					continue;
				}

//				Integer termID = tokens.get(i).hashCode();
				if(!termIDSMap.containsKey(wordFreq.getKey())){
					termIDSMap.put(wordFreq.getKey(), ++termID);

				}
				Integer wordID = termIDSMap.get(wordFreq.getKey());
				//Treemap
//				reverseIndex.putIfAbsent(wordID, new TreeMap<Integer,Float>());
				//HashMap
				reverseIndex.putIfAbsent(wordID, new HashMap<Integer,Float>());

				Map<Integer, Float> freqMap = reverseIndex.get(wordID);
				freqMap.put(Integer.parseInt(docEntry.getKey()), wordFreq.getValue());				
			}
		}
		double corpusSize = fileNumber;
		for(Entry<Integer, Map<Integer, Float>> postingsList : reverseIndex.entrySet()){
			double docFreq = postingsList.getValue().size();
			float idf = (float) Math.log(corpusSize / docFreq);
			for (Entry<Integer, Float> docEntry : postingsList.getValue().entrySet()){
				float tf = docEntry.getValue();
				float wtf = (float) (1f + Math.log(tf));
				float tf_idf = wtf * idf;
				postingsList.getValue().put(docEntry.getKey(), tf_idf);
			}
		}

//		System.out.println(termIDSMap.get("banging"));
//		System.out.println(reverseIndex.get(110708).toString());
//		
		//Pretty Printing/Larger File Size
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		//Write JSON Object/Smaller File Size
//		Gson gson = new GsonBuilder().create();
		try(Writer writer = new FileWriter("termID.json")){
			gson.toJson(termIDSMap, writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try(Writer writer = new FileWriter("index.json")){
			gson.toJson(reverseIndex, writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Index file created.");
		System.out.println("Number of documents: " + fileNumber);
		System.out.println("Number of unique words: " + termIDSMap.size());
	}
}