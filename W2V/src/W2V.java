import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class W2V {
	
	private static Logger log = LoggerFactory.getLogger(W2V.class);
	
	private static String stopwords1 = "<=,>=,_,=,<,>,+,%, -,- , - ,—,•,…,/,#,$,&,*,\\,^,{,},~,£,§,®,°,±,³,·,½,™";

	private static String stopwords2 = "i,me,my,myself,we,our,ours,ourselves,you,your,yours,yourself,yourselves,he,him,his,himself,she,her,hers,herself,it,its,itself,they,them,their,theirs,themselves,what,which,who,whom,never,this,that,these,those,am,is,are,was,were,be,been,being,have,has,had,having,do,does,did,doing,a,an,the,and,but,if,kung,or,because,as,until,while,of,at,by,for,with,about,against,between,into,through,during,before,after,above,below,to,from,up,down,in,out,on,off,over,under,again,further,then,once,here,there,when,where,why,how,long,all,any,both,each,few,more,delivering,most,other,some,such,no,nor,not,only,own,same,so,than,too,cry,very,s,t,can,lite,will,just,don,should,now";
	
	public static void main(String[] args) throws Exception {
		try {
			InputStream input = new FileInputStream("infos.properties");
			Properties prop = new Properties();
			// load a properties file
			prop.load(input);
			//System.out.println(prop.getProperty("pathToXML"));
			File folder;
			folder = new File(new URI("file:///"+prop.getProperty("pathToXML")));
			log.info("Checking for files...");
			File[] files = folder.listFiles(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.isFile();
				}
			});
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			String data = "";
			
				String field="";
				log.info("Reading all files...");
				for (int i = 0; i < files.length; i++) {
					Document doc = dBuilder.parse(files[i]);
					doc.getDocumentElement().normalize();
					for(int j=0; j<2; j++) {
						//if(j==0) field="brief_summary";
						if(j==0) field="detailed_description";
						else field="criteria";
						//System.out.println(field);
						NodeList nList = doc.getElementsByTagName(field);
						if(nList.getLength()>0) {
							Node nNode = nList.item(0);
							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								Element eElement = (Element) nNode;
								data += eElement
										.getElementsByTagName("textblock")
										.item(0)
										.getTextContent().toLowerCase();
							}
						}
					}
				}

			String[] sw1 = stopwords1.split(",");	
			String[] sw2 = stopwords2.split(",");
			
			log.info("Replacing stop words...");
			
			for(int i=0; i<sw1.length; i++) data = data.replace(sw1[i], " ");
			for(int i=0; i<sw2.length; i++) data = data.replace(" "+sw2[i]+" ", " ");
			//if(stopwords.contains(","+myKeywordString.toLowerCase()+","))	
				
			log.info("Writing data in new file...");
			PrintWriter out = new PrintWriter("raw_text.txt");
			out.println(data);
		

			/*log.info("Load & Vectorize Sentences....");
			// Strip white space before and after for each line
			SentenceIterator iter = new BasicLineIterator("raw_text.txt");
			// Split on white spaces in the line to get words
			TokenizerFactory t = new DefaultTokenizerFactory();
			t.setTokenPreProcessor(new CommonPreprocessor());
			
			log.info("Building model....");
			Word2Vec vec = new Word2Vec.Builder()
					.minWordFrequency(5)
					.iterations(1)
					.layerSize(100)
					.seed(42)
					.windowSize(5)
					.iterate(iter)
					.tokenizerFactory(t)
					.build();
			
			log.info("Fitting Word2Vec model....");
			vec.fit();
			
			log.info("Writing word vectors to text file....");
			WordVectorSerializer.writeWordVectors(vec, "word_vectors.txt");
			*/
	        
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
