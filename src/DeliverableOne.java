import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DeliverableOne {
	
	public static void main(String[] args) {
		CreateIndex("testFiles");
	}
	
	public static void CreateIndex(String filePath) {
		File inputFolder = new File(filePath);
		//Map<String, ArrayList<Pair<String,Integer>>> reverseIndex = new HashMap<String, ArrayList<Pair<String,Integer>>>();
		//Sorted Debug
		Map<String, TreeMap<String,Integer>> reverseIndex = new TreeMap<String, TreeMap<String,Integer>>();
		//Hashmap
//		Map<String, HashMap<String,Integer>> reverseIndex = new HashMap<String, HashMap<String,Integer>>();
		
		
		for (File f : inputFolder.listFiles()){
			ArrayList<String> tokens = Utilities.tokenizeFile(f);
			for(int i = 0; i < tokens.size(); ++i){
				//Treemap
				reverseIndex.putIfAbsent(tokens.get(i), new TreeMap<String, Integer>());
				TreeMap<String, Integer> freqMap = reverseIndex.get(tokens.get(i));
				freqMap.putIfAbsent(f.getName(), 0);
				Integer freq = freqMap.get(f.getName());
				freqMap.put(f.getName(), ++freq);
//				
				//Hashmap
//				reverseIndex.putIfAbsent(tokens.get(i), new HashMap<String, Integer>());
//				HashMap<String, Integer> freqMap = reverseIndex.get(tokens.get(i));
//				freqMap.putIfAbsent(f.getName(), 0);
//				Integer freq = freqMap.get(f.getName());
//				freqMap.put(f.getName(), ++freq);
				
				
			}
			
//			
//			for(Entry<String, Integer> entry : freqMap.entrySet()) {
//				reverseIndex.putIfAbsent(entry.getKey(), new ArrayList<Pair<String,Integer>>());
//				ArrayList<Pair<String,Integer>> docList = reverseIndex.get(entry.getKey());
//				
//				docList.add(new Pair<String,Integer>(f.getName(), entry.getValue()));
//			}
//			
//			Set<Entry<String,ArrayList<Pair<String,Integer>>>> reverseSet = reverseIndex.entrySet();
			
		}
		
		//Pretty Printing
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(reverseIndex));
		
		//Write JSON Object
//		Gson gson = new GsonBuilder().create();
//		try(Writer writer = new FileWriter("index.json")){
//			gson.toJson(reverseIndex, writer);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}