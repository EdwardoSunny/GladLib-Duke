import edu.duke.*;
import java.util.*;
//new

public class GladLib {
    private HashMap<String, ArrayList<String>> myMap = new HashMap<String, ArrayList<String>>();
    
    private ArrayList<String> usedWords = new ArrayList<String>();
    String currentLabel = "";
    private ArrayList<String> categoriesUsing = new ArrayList<String>();
    
    private Random myRandom = new Random();
    
    private static String dataSourceURL = "http://dukelearntoprogram.com/course3/data";
    private static String dataSourceDirectory = "datalong";
    
    public GladLib(){
        initializeFromSource(dataSourceDirectory);
        myRandom = new Random();
    }
    
    
    public GladLib(String source){
        initializeFromSource(source);
        myRandom = new Random();
    }
    
    private void initializeFromSource(String source) {
        String[] labels = {"country", "noun", "animal", "adjective", "name", "color", "timeframe"};
        
        for(String s : labels){
            ArrayList<String> temp = readIt(source+"/"+s+".txt");
            myMap.put(s, temp);
        }
    }
    
    private String randomFrom(ArrayList<String> source){
        ArrayList<String> sourcetemp = source;
        int index = myRandom.nextInt(sourcetemp.size());
        return source.get(index);
    }
    
    private String getSubstitute(String label) {
        if (label.equals("number")){
            return "" + myRandom.nextInt(50) + 5;
        }
        
        return randomFrom(myMap.get(label));
    }
    
    private String processWord(String w){
        int first = w.indexOf("<");
        int last = w.indexOf(">",first);
        if (first == -1 || last == -1){
            return w;
        }
        String prefix = w.substring(0,first);
        String suffix = w.substring(last+1);
        String label = w.substring(first+1,last);
        String sub = getSubstitute(label);

        
        String finalWord = sub;
        
        
        if (usedWords.contains(sub)){
            finalWord = getSubstitute(label);
        }else{
            usedWords.add(finalWord);
            categoriesUsing.add(label);
        }
        
        
        
        return finalWord;
        
        
    }
    
    private void printOut(String s, int lineWidth){
        int charsWritten = 0;
        for(String w : s.split("\\s+")){
            if (charsWritten + w.length() > lineWidth){
                System.out.println();
                charsWritten = 0;
            }
            System.out.print(w+" ");
            charsWritten += w.length() + 1;
        }
    }
    
    private String fromTemplate(String source){
        String story = "";
        if (source.startsWith("http")) {
            URLResource resource = new URLResource(source);
            for(String word : resource.words()){
                story = story + processWord(word) + " ";
                
            }
        }
        else {
            FileResource resource = new FileResource(source);
            for(String word : resource.words()){
                story = story + processWord(word) + " ";
                
            }
        }
        return story;
    }
    
    private ArrayList<String> readIt(String source){
        ArrayList<String> list = new ArrayList<String>();
        if (source.startsWith("http")) {
            URLResource resource = new URLResource(source);
            for(String line : resource.lines()){
                list.add(line);
            }
        }
        else {
            FileResource resource = new FileResource(source);
            for(String line : resource.lines()){
                list.add(line);
            }
        }
        return list;
    }
    
    private int totalWordsINMap(){
        int total = 0;
        for (String s : myMap.keySet()){
            ArrayList<String> current = myMap.get(s);
            total = total + current.size();
        }
        
        return total;
    }
    
    private int totalWordsConsidered(){
        int total = 0;
        for (int i = 0; i < categoriesUsing.size(); i++){
            
            System.out.println(categoriesUsing);
            total = total + myMap.get(categoriesUsing.get(i)).size();
        }
        return total;
    }
    
    public void makeStory(){
        //usedWords.clear();
        System.out.println("\n");
        String story = fromTemplate("data/madtemplate2.txt");
        printOut(story, 60);
        System.out.println("\n");
        System.out.println("Total words chosen from: " + totalWordsINMap());
        System.out.println(categoriesUsing);
        System.out.println(usedWords);
        System.out.println(totalWordsConsidered());
    }
    


}