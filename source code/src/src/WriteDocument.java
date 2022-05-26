package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import util.CsvReader;

public class WriteDocument {
	private ArrayList<String[]> Fields;
	private Analyzer analyzer;
	private Directory index;
	private String query;
	private int counter=0;
	private Path output;
	private String convsort;
	private File file = new File("Autosuggest.txt");
	private Set<String> set;
	
	public WriteDocument(String query,String sortby) {
		set = new HashSet<>();
		this.query = query;
		String inputpath = "inputFiles\\TMDb_updated.CSV";
		output = Paths.get("outputFiles\\");
		CsvReader c = new CsvReader(inputpath);
		Fields = c.ReadCsv();
		chechSort(sortby);
		initialize();
		
	
			
	}
	
	public void initialize() {
		
		try {
			analyzer = new StandardAnalyzer();
			index = FSDirectory.open(output);
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			
			iwc.setOpenMode(OpenMode.CREATE);
			IndexWriter writer = new IndexWriter(index,iwc);
			for(String[] index1: Fields) {
				addDoc(writer,index1);
			}
			
			writer.close();
			
		}catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void addDoc(IndexWriter w,String[] fields) throws IOException {
		
		String s="";
		Document doc = new Document();
		if(fields.length>6) {
			for(int i=2; i<fields.length-3; i++) {
				s+= fields[i];
			}
			doc.add(new TextField("overview",s,Field.Store.YES));
		}else {
			doc.add(new TextField("overview",fields[2],Field.Store.YES));
		}
		
		doc.add(new TextField("title",fields[1],Field.Store.YES));
		doc.add(new StringField("vote_average",fields[fields.length-1],Field.Store.YES));
		doc.add(new SortedDocValuesField("title", new BytesRef(fields[1])));
		doc.add(new SortedDocValuesField("vote_average", new BytesRef(fields[fields.length-1])));

		w.addDocument(doc);
		
	}
	
	
	public String search() throws  IOException {
		String s1,s2,plot,title,s = "";
		String[] fields = {"title","overview"};
		try {
			Query q = new MultiFieldQueryParser(fields, analyzer).parse(query);
			int hitsPerPage = 10000;
			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);
			Sort sort = new Sort(new SortField(convsort, SortField.Type.STRING, true));
			TopDocs docs = searcher.search(q, hitsPerPage,sort);
			ScoreDoc[] hits = docs.scoreDocs;
			SimpleHTMLFormatter yellow_highlight = new SimpleHTMLFormatter("<b style=\"background-color:yellow\">", "</b>");
			QueryScorer scorer = new QueryScorer(q,fields[0]);
			Highlighter highlighter = new Highlighter(yellow_highlight,scorer);
			QueryScorer scorer1 = new QueryScorer(q,fields[1]);
			Highlighter highlighter1 = new Highlighter(yellow_highlight,scorer1);
			
			for(int i=0;i<hits.length;++i) {
			    int docId = hits[i].doc;
			    Document d = searcher.doc(docId);
			    counter++;
			    
			    s1 = d.get("title");
			    String[] fragTitle = highlightText(highlighter,scorer,reader,docId,fields[0],s1);
			    s2 = d.get("overview");
			    String[] fragPlot = highlightText(highlighter1,scorer1,reader,docId,fields[1],s2);
			    
			    if(fragTitle.length == 0) {
			    	title = s1;
			    }else {
			    	title="";
				    for(int j=0; j<fragTitle.length; j++) {
				    	title += fragTitle[j];
				    }
			    }
			    if(fragPlot.length == 0) {
			    	plot = s2;
			    }else {
			    	plot="";
				    for(int j=0; j<fragPlot.length; j++) {
				    	plot += fragPlot[j];
				    }
			    } 
			    
			    s+=((counter) + ". " + "OverView: "+plot.replace("\"\"","")+ "<br>" +"Title: "+
			    		title.replace("\"\"","")+"<br>"+"Vote_average: "+ d.get("vote_average").replace(";","").replace("\"", "")+"<br><br>");
			}
			reader.close();
			
		} catch ( IOException| ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return s;
	}
	
	public void chechSort(String sortby) {
		if(sortby == "None") {
			convsort = "";
		}
		else if(sortby == "Rating") {
			convsort = "vote_average";
		}
		else if(sortby == "Title") {
			convsort = "title";
		}
	}

	
	@SuppressWarnings("deprecation")
	public String[] highlightText(Highlighter highlight,QueryScorer scorer,IndexReader reader,int docId,String field,String s1) {
		
		TokenStream stream;
		String[] fragment = null;
		try {
			stream = TokenSources.getAnyTokenStream(reader, docId, field, analyzer);
			Fragmenter fragmenter1 = new SimpleSpanFragmenter(scorer);
		    highlight.setTextFragmenter(fragmenter1);
		    fragment = highlight.getBestFragments(stream, s1,100);
		    
		} catch (IOException | InvalidTokenOffsetsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    return fragment;
	}
	
	public void writeFile(String data) {
        FileWriter fr = null;
        try {
            fr = new FileWriter(file,true);
            fr.append(data+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	public ArrayList<String> readFile(){
		ArrayList<String> h = new ArrayList<>();
		Scanner sc;
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String input = sc.nextLine();
				if(set.add(input)) {
					h.add(input);
				}
		    }
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return h;
	}
	
	public void EmptyFile() {
		if (file.delete()) { 
		      System.out.println("Deleted the file: " + file.getName());
		    } else {
		      System.out.println("Failed to delete the file.");
		    }
	}
}