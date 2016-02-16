import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DeliverableOne {
	
	public static void main(String[] args) {
		CreateIndex("testFiles");
	}
	
	public static String CreateIndex(String filePath) {
		File inputFolder = new File(filePath);
		//Map<String, ArrayList<Pair<String,Integer>>> reverseIndex = new HashMap<String, ArrayList<Pair<String,Integer>>>();
		Map<String, HashMap<String,Integer>> reverseIndex = new HashMap<String, HashMap<String,Integer>>();
		
		
		for (File f : inputFolder.listFiles()){
			ArrayList<String> tokens = Utilities.tokenizeFile(f);
//			Map<String, Integer> freqMap = new HashMap<String, Integer>();
			for(int i = 0; i < tokens.size(); ++i){
//				Integer frequency = freqMap.get(tokens.get(i));
				//freqMap.put(tokens.get(i), frequency == null ? 1 : frequency + 1);
				reverseIndex.putIfAbsent(tokens.get(i), new HashMap<String, Integer>());
				HashMap<String, Integer> freqMap = reverseIndex.get(tokens.get(i));
				freqMap.putIfAbsent(f.getName(), 0);
				Integer freq = freqMap.get(f.getName());
				freqMap.put(f.getName(), ++freq);
				
				
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
		System.out.println(reverseIndex.toString());
		return "";
	}
}