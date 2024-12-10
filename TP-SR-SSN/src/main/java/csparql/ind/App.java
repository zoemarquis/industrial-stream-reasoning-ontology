package csparql.ind;

import java.io.File;
import org.apache.log4j.PropertyConfigurator;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.larkc.csparql.common.utils.CsparqlUtils;
import eu.larkc.csparql.core.engine.CsparqlEngineImpl;
import eu.larkc.csparql.core.engine.CsparqlQueryResultProxy;
import csparql.ind.streamer.SensorsStreamer;

public class App {

	private static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		try{
			//Configure log4j logger for the csparql engine
			PropertyConfigurator.configure("log4j_configuration/csparql_readyToGoPack_log4j.properties");

			//Create csparql engine instance
			CsparqlEngineImpl engine = new CsparqlEngineImpl();
			//Initialize the engine instance
			//The initialization creates the static engine (SPARQL) and the stream engine (CEP)
			engine.initialize(true);

			String fileOntology = "Ontology-TP.owl";

			//Add the static model
			engine.putStaticNamedModel("http://streamreasoning.org/Ontology-TP",CsparqlUtils.serializeRDFFile(fileOntology));
			

			String queryS = "REGISTER QUERY S6detection AS "
				+ "PREFIX : <http://semanticweb.org/Ontology-TP#> "
				+ "PREFIX sosa: <http://www.w3.org/ns/sosa/> "
				+ "SELECT ?m ?pl "
				+ "FROM STREAM <Stream_S_OilTemp> [RANGE 15s STEP 5s] "
				+ "FROM <http://streamreasoning.org/Ontology-TP> "
				+ "WHERE { "
				+ "{ ?m         :isPartOf        ?pl ."
				+ "  ?m         sosa:hosts       sosa:S_OilTemp ." 
				+ "  :S_OilTemp :madeObservation ?o1 ."
				+ "  ?o1        :hasSimpleResult ?v1 ."
				+ " FILTER ( "
				+ "		?v1 > 80.0 ) . }"
				+ " } ";


			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory factory = manager.getOWLDataFactory();
			String ontologyURI = "http://semanticweb.org/Ontology-TP";
			String ns = ontologyURI + "#";
			final OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(fileOntology));
			
			SensorsStreamer Stream_OilTemp = new SensorsStreamer("Stream_S_OilTemp",ns,"OilTemp",2,50,90,ontology,factory);

			//Register new streams in the engine
			engine.registerStream(Stream_OilTemp);

			Thread Stream_OilTemp_Thread = new Thread(Stream_OilTemp);

			//Register new query in the engine
			CsparqlQueryResultProxy c_S = engine.registerQuery(queryS, false);

			//Attach a result consumer to the query result proxy to print the results on the console
			c_S.addObserver(new ConsoleFormatter("S",ns,ontology,factory));	

			//Start streaming data
			Stream_OilTemp_Thread.start();

		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}