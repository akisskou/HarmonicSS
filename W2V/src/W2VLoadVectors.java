import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Properties;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class W2VLoadVectors {

	private static Logger log = LoggerFactory.getLogger(W2VLoadVectors.class);
	
	public static void main(String[] args) throws Exception {
		InputStream input = new FileInputStream("infos.properties");
		Properties prop = new Properties();
		// load a properties file
		prop.load(input);
		log.info("Loading word vectors from text file....");
		Word2Vec vec = WordVectorSerializer.readWord2VecModel("word_vectors.txt");
		log.info("Closest Words:");
        Collection<String> lst = vec.wordsNearestSum(prop.getProperty("word"), Integer.valueOf(prop.getProperty("count")));
        log.info(prop.getProperty("count")+" Words closest to '"+prop.getProperty("word")+"': {}", lst);
        PrintWriter out = new PrintWriter(prop.getProperty("word")+".txt");
        String data = prop.getProperty("count")+" Words closest to '"+prop.getProperty("word")+"': "+lst.toString();
		out.println(data);
		out.close();
	}
}
