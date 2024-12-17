package csparql.ind;

import java.io.File;
import java.util.stream.Stream;

import org.apache.log4j.PropertyConfigurator;
import org.openrdf.query.algebra.Str;
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

			String fileOntology = "Ontology-zoe-charlotte.owl"; // notre ontologie

			//Add the static model
			engine.putStaticNamedModel("http://streamreasoning.org/Ontology-TP",CsparqlUtils.serializeRDFFile(fileOntology));


			String queryS6 = "REGISTER QUERY S6detection AS "
				+ "PREFIX : <http://semanticweb.org/Ontology-TP#> "
				+ "PREFIX sosa: <http://www.w3.org/ns/sosa/> "
				+ "SELECT ?m ?pl "
				+ "FROM STREAM <Stream_S_ConvWaterTemp> [RANGE 15s STEP 5s] "
				+ "FROM STREAM <Stream_S_TransGridTemp> [RANGE 15s STEP 5s] "
				+ "FROM STREAM <Stream_S_GeneratorTemp> [RANGE 15s STEP 5s] "
				+ "FROM <http://streamreasoning.org/Ontology-TP> "
				+ "WHERE { "
				+ "  ?m         :isPartOf        ?pl ."
				+ "  ?m         sosa:hosts       sosa:S_ConvWaterTemp ."
				+ "  ?m 	    sosa:hosts       sosa:S_TransGridTemp ."
				+ "  ?m 	    sosa:hosts       sosa:S_GeneratorTemp ."
				+ "  :S_ConvWaterTemp :madeObservation ?o1 ."
				+ "  :S_TransGridTemp :madeObservation ?o2 ."
				+ "  :S_GeneratorTemp :madeObservation ?o3 ."
				+ "  ?o1        :hasSimpleResult ?v1 ."
				+ "  ?o2        :hasSimpleResult ?v2 ."
				+ "  ?o3        :hasSimpleResult ?v3 ."
				+ " FILTER ( "
				+ " 	?v1 > 60 && "
				+ " 	?v2 < 35 && "
				+ " 	?v3 > 45 "
				+ "     ) "
				+ " } ";

			String queryS10 = "REGISTER QUERY S10detection AS "
				+ "PREFIX : <http://semanticweb.org/Ontology-TP#> "
				+ "PREFIX sosa: <http://www.w3.org/ns/sosa/> "
				+ "SELECT ?m ?pl "
				+ "FROM STREAM <Stream_S_ConvWaterTemp> [RANGE 20s STEP 5s] "
				+ "FROM STREAM <Stream_S_TransGridTemp> [RANGE 20s STEP 5s] "
				+ "FROM STREAM <Stream_S_GeneratorTemp> [RANGE 20s STEP 5s] "
				+ "FROM STREAM <Stream_S_ConverterTemp> [RANGE 20s STEP 5s] "
				+ "FROM <http://streamreasoning.org/Ontology-TP> "
				+ "WHERE { "
				+ "  ?m         :isPartOf        ?pl ."
				+ "  ?m         sosa:hosts       sosa:S_ConvWaterTemp ."
				+ "  ?m         sosa:hosts       sosa:S_TransGridTemp ."
				+ "  ?m         sosa:hosts       sosa:S_GeneratorTemp ."
				+ "  ?m         sosa:hosts       sosa:S_ConverterTemp ."
				+ "  :S_ConvWaterTemp :madeObservation ?o1 ."
				+ "  :S_TransGridTemp :madeObservation ?o2 ."
				+ "  :S_GeneratorTemp :madeObservation ?o3 ."
				+ "  :S_ConverterTemp :madeObservation ?o4 ."
				+ "  ?o1        :hasSimpleResult ?v1 ."
				+ "  ?o2        :hasSimpleResult ?v2 ."
				+ "  ?o3        :hasSimpleResult ?v3 ."
				+ "  ?o4        :hasSimpleResult ?v4 ."
				+ " FILTER ( "
				+ "     ?v1 > 80 && "
				+ "     ?v2 < 35 && "
				+ "     ?v3 > 45 && "
				+ "     ?v4 > 60 "
				+ " ) "
				+ "}";
			

			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory factory = manager.getOWLDataFactory();
			String ontologyURI = "http://semanticweb.org/Ontology-TP";
			String ns = ontologyURI + "#";
			final OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(fileOntology));
			

			// Valeurs min et max de ces 3 streams afin de provoquer la situation S6
			SensorsStreamer Stream_ConvWaterTemp = new SensorsStreamer("Stream_S_ConvWaterTemp",ns,"ConvWaterTemp",2,20,100,ontology,factory);
			SensorsStreamer Stream_TransGridTemp = new SensorsStreamer("Stream_S_TransGridTemp",ns,"TransGridTemp",2,20,100,ontology,factory);
			SensorsStreamer Stream_GeneratorTemp = new SensorsStreamer("Stream_S_GeneratorTemp",ns,"GeneratorTemp",2,20,100,ontology,factory);
			
			SensorsStreamer Stream_ConverterTemp = new SensorsStreamer("Stream_S_ConverterTemp", ns, "ConverterTemp", 2, 20, 100, ontology, factory);

			//Register new streams in the engine
			engine.registerStream(Stream_ConvWaterTemp);
			engine.registerStream(Stream_TransGridTemp);
			engine.registerStream(Stream_GeneratorTemp);
			engine.registerStream(Stream_ConverterTemp);
			
			Thread Stream_ConvWaterTemp_Thread = new Thread(Stream_ConvWaterTemp);
			Thread Stream_TransGridTemp_Thread = new Thread(Stream_TransGridTemp);
			Thread Stream_GeneratorTemp_Thread = new Thread(Stream_GeneratorTemp);
			Thread Stream_ConverterTemp_Thread = new Thread(Stream_ConverterTemp);

			//Register new query in the engine
			CsparqlQueryResultProxy c_S6 = engine.registerQuery(queryS6, false);
			CsparqlQueryResultProxy c_S10 = engine.registerQuery(queryS10, false);

			//Attach a result consumer to the query result proxy to print the results on the console
			c_S6.addObserver(new ConsoleFormatter("S6",ns,ontology,factory));	
			c_S10.addObserver(new ConsoleFormatter("S10", ns, ontology, factory));

			//Start streaming data
			Stream_ConvWaterTemp_Thread.start();
			Stream_TransGridTemp_Thread.start();
			Stream_GeneratorTemp_Thread.start();
			Stream_ConverterTemp_Thread.start();

		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}