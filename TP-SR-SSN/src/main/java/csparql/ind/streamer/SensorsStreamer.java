package csparql.ind.streamer;

import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import eu.larkc.csparql.cep.api.RdfQuadruple;
import eu.larkc.csparql.cep.api.RdfStream;

public class SensorsStreamer extends RdfStream implements Runnable  {

	private long sleepTime;
	private String baseUri;
	private String prop;
	private int Tmin;
	private int Tmax;
	private OWLOntology ontology;
	private OWLDataFactory factory;

	public SensorsStreamer(String iri, String baseUri, String prop,long sleepTime, int Tmin, int Tmax, OWLOntology ontology, OWLDataFactory factory) {
		super(iri);
		this.sleepTime = sleepTime;
		this.baseUri = baseUri;
		this.prop = prop;
		this.Tmin = Tmin;
		this.Tmax = Tmax;
		this.ontology = ontology;
		this.factory = factory;
	}

	public void run() {

		Random random = new Random();
		int result;
		int observationIndex = 0;
		int timeIndex = 0;

		String ontologyURI = "http://semanticweb.org/Ontology-TP";
		String ns = ontologyURI + "#";
		String pre_SOSAOnt = "http://www.w3.org/ns/sosa/";
		String pre_TIME = "http://www.w3.org/2006/time#";

		OWLClass Sensor = factory.getOWLClass(IRI.create(pre_SOSAOnt + "Sensor"));
		OWLClass Observation = factory.getOWLClass(IRI.create(pre_SOSAOnt + "Observation"));
		OWLClass ObservableProperty = factory.getOWLClass(IRI.create(pre_SOSAOnt + "ObservableProperty"));
		OWLClass Instant = factory.getOWLClass(IRI.create(pre_TIME + "Instant"));

		OWLObjectProperty madeObservation = factory.getOWLObjectProperty(IRI.create(pre_SOSAOnt + "madeObservation"));
		OWLObjectProperty observedProperty = factory.getOWLObjectProperty(IRI.create(pre_SOSAOnt + "observedProperty"));
		OWLDataProperty hasSimpleResult = factory.getOWLDataProperty(IRI.create(pre_SOSAOnt + "hasSimpleResult"));
		OWLObjectProperty hasTime = factory.getOWLObjectProperty(IRI.create(ns,"hasTime"));
		OWLDataProperty inXSDDateTimeStamp = factory.getOWLDataProperty(IRI.create(pre_TIME + "inXSDDateTimeStamp"));

		while(true){
			try{
				result = random.nextInt(Tmax - Tmin + 1) + Tmin;
				Timestamp date = new Timestamp(System.currentTimeMillis());

				RdfQuadruple q = new RdfQuadruple(baseUri + "S_" + prop, baseUri + "madeObservation", baseUri + "S_" + prop + "-Obs-" + observationIndex, System.currentTimeMillis());
				System.out.println(q);
				this.put(q);
				q = new RdfQuadruple(baseUri + "S_" + prop + "-Obs-" + observationIndex, baseUri + "observedProperty", baseUri + prop, System.currentTimeMillis());
				System.out.println(q);
				this.put(q);
				q = new RdfQuadruple(baseUri + "S_" + prop + "-Obs-" + observationIndex, baseUri + "hasSimpleResult", result + "^^http://www.w3.org/2001/XMLSchema#integer", System.currentTimeMillis());
				System.out.println(q);
				this.put(q);
				q = new RdfQuadruple(baseUri + "S_" + prop + "-Obs-" + observationIndex, baseUri + "hasTime", baseUri + "t-obs-S_" + prop + "-"+ timeIndex, System.currentTimeMillis());
				System.out.println(q);
				this.put(q);
				q = new RdfQuadruple(baseUri + "t-obs-S_" + prop + "-"+ timeIndex, baseUri + "inXSDDateTime", date + "^^http://www.w3.org/2001/XMLSchema#dateTimeStamp", System.currentTimeMillis());
				System.out.println(q);
				this.put(q);
				
				OWLIndividual sensor = factory.getOWLNamedIndividual(IRI.create(ns,"S_" + prop));
				OWLClassAssertionAxiom sensorType = factory.getOWLClassAssertionAxiom(Sensor, sensor);
				ontology.add(sensorType);
				OWLIndividual obs = factory.getOWLNamedIndividual(IRI.create(ns,"S_" + prop + "-Obs-" + observationIndex));
				OWLClassAssertionAxiom obsType = factory.getOWLClassAssertionAxiom(Observation, obs);
				ontology.add(obsType);
				OWLIndividual property = factory.getOWLNamedIndividual(IRI.create(ns,prop));
				OWLClassAssertionAxiom propType = factory.getOWLClassAssertionAxiom(ObservableProperty, property);
				ontology.add(propType);

				OWLObjectPropertyAssertionAxiom sensormadeobs = factory.getOWLObjectPropertyAssertionAxiom(madeObservation, sensor, obs);
				ontology.add(sensormadeobs);
				OWLObjectPropertyAssertionAxiom observedProp = factory.getOWLObjectPropertyAssertionAxiom(observedProperty, obs, property);
				ontology.add(observedProp);
				
				OWLIndividual time = factory.getOWLNamedIndividual(IRI.create(pre_TIME,"t-obs-S_" + prop + "-"+ timeIndex));        		
				OWLClassAssertionAxiom timeType = factory.getOWLClassAssertionAxiom(Instant, time);
				ontology.add(timeType);
				OWLObjectPropertyAssertionAxiom obshastime = factory.getOWLObjectPropertyAssertionAxiom(hasTime, obs, time);
				ontology.add(obshastime);
				OWLDataPropertyAssertionAxiom timehasdate = factory.getOWLDataPropertyAssertionAxiom(inXSDDateTimeStamp, time, date + "^^http://www.w3.org/2001/XMLSchema#dateTimeStamp");
				ontology.add(timehasdate);
				
				OWLDataPropertyAssertionAxiom obshassimpleresult = factory.getOWLDataPropertyAssertionAxiom(hasSimpleResult, obs, result + "^^http://www.w3.org/2001/XMLSchema#integer");
				ontology.add(obshassimpleresult);

				try {
					ontology.saveOntology();
				} catch (OWLOntologyStorageException e1) {
					e1.printStackTrace();
				}
				
				TimeUnit.SECONDS.sleep(sleepTime);
				observationIndex++;
				timeIndex++;
			} catch(Exception e){
				e.printStackTrace();
			}
		}

	}

}