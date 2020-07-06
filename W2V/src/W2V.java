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
	
	public static void main(String[] args) throws Exception {
		try {
			InputStream input = new FileInputStream("infos.properties");
			Properties prop = new Properties();
			// load a properties file
			prop.load(input);
			//System.out.println(prop.getProperty("pathToXML"));
			File folder;
			folder = new File(new URI("file:///"+prop.getProperty("pathToXML")));
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
										.getTextContent();
							}
						}
					}
				}

			PrintWriter out = new PrintWriter("raw_text.txt");
			out.println(data);
		

			log.info("Load & Vectorize Sentences....");
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
			/*log.info("Closest Words:");
	        Collection<String> lst = vec.wordsNearestSum(prop.getProperty("word"), 10);
	        log.info("10 Words closest to '"+prop.getProperty("word")+"': {}", lst);*/
	        
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
