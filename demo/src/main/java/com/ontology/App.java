package com.ontology;

import java.io.File;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

public class App {

	public static void main(String[] args) throws OWLOntologyCreationException {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();

		String ontologyURI = "http://semanticweb.org/Ontology-TP";
		String ns = ontologyURI + "#";

		OntologyAssistant oa = new OntologyAssistant();

		OWLOntology ontology = manager.createOntology(IRI.create(ontologyURI));

		String pre_TIME = "http://www.w3.org/2006/time#";
		String pre_SOSAOnt = "http://www.w3.org/ns/sosa/";

		// OWLDatatype floatDatatype = factory.getFloatOWLDatatype();
		// OWLDatatype doubleDatatype = factory.getDoubleOWLDatatype();
		// OWLDatatype booleanDatatype = factory.getBooleanOWLDatatype();
		OWLDatatype integerDatatype = factory.getIntegerOWLDatatype();
		
		OWLDatatype doubleDatatype = factory.getDoubleOWLDatatype();
		OWLDatatype dateTimeStamp = factory.getOWLDatatype("https://www.w3.org/TR/xmlschema11-2/#dateTimeStamp");
		//http://www.w3.org/2001/XMLSchema#dateTimeStamp");
		OWLDatatype strDatatype = factory.getStringOWLDatatype();

        
        /* 
         * OWL Classes 
        */
		OWLClass Resource = factory.getOWLClass(IRI.create(ns, "Resource"));
		OWLClass Machine = factory.getOWLClass(IRI.create(ns, "Machine"));
        OWLClass Line = factory.getOWLClass(IRI.create(ns, "Line"));
        ontology.add(factory.getOWLDeclarationAxiom(Machine));
		ontology.add(factory.getOWLDeclarationAxiom(Resource));
		ontology.add(factory.getOWLSubClassOfAxiom(Machine, Resource));
        ontology.add(factory.getOWLSubClassOfAxiom(Line, Resource));

		OWLClass Situation = factory.getOWLClass(IRI.create(ns, "Situation"));
		ontology.add(factory.getOWLDeclarationAxiom(Situation));
		
		OWLClass Situation_S1 = factory.getOWLClass(IRI.create(ns, "Situation-S1"));
		ontology.add(factory.getOWLDeclarationAxiom(Situation_S1));
		ontology.add(factory.getOWLSubClassOfAxiom(Situation_S1, Situation));

		OWLClass Sensor = factory.getOWLClass(IRI.create(pre_SOSAOnt + "Sensor"));
		OWLClass ObservableProperty = factory.getOWLClass(IRI.create(pre_SOSAOnt + "ObservableProperty"));
		OWLClass Observation = factory.getOWLClass(IRI.create(pre_SOSAOnt + "Observation"));
		ontology.add(factory.getOWLDeclarationAxiom(Sensor));
		ontology.add(factory.getOWLDeclarationAxiom(ObservableProperty));
		ontology.add(factory.getOWLDeclarationAxiom(Observation));

		OWLClass TemporalEntity = factory.getOWLClass(IRI.create(pre_TIME + "TemporalEntity"));
		OWLClass Instant = factory.getOWLClass(IRI.create(pre_TIME + "Instant"));
		OWLClass Interval = factory.getOWLClass(IRI.create(pre_TIME + "Interval"));
		ontology.add(factory.getOWLDeclarationAxiom(TemporalEntity));
		ontology.add(factory.getOWLSubClassOfAxiom(Instant, TemporalEntity));
		ontology.add(factory.getOWLSubClassOfAxiom(Interval, TemporalEntity));


        /* 
         * OWL Object Properties 
        */
        OWLObjectProperty isPartOf = factory.getOWLObjectProperty(IRI.create(ns, "isPartOf"));
		OWLObjectPropertyDomainAxiom isPartOfdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(isPartOf,
        Resource);
		OWLObjectPropertyRangeAxiom isPartOfrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(isPartOf,
        Resource);
		ontology.add(factory.getOWLDeclarationAxiom(isPartOf));
		ontology.add(isPartOfdomainAxiom);
		ontology.add(isPartOfrangeAxiom);
		
		OWLObjectProperty madeObservation = factory.getOWLObjectProperty(IRI.create(pre_SOSAOnt + "madeObservation"));
		OWLObjectPropertyDomainAxiom domainAxiom = factory.getOWLObjectPropertyDomainAxiom(madeObservation, Sensor);
		OWLObjectPropertyRangeAxiom rangeAxiom = factory.getOWLObjectPropertyRangeAxiom(madeObservation, Observation);
		ontology.add(factory.getOWLDeclarationAxiom(madeObservation));
		ontology.add(domainAxiom);
		ontology.add(rangeAxiom);

		OWLObjectProperty observedProperty = factory.getOWLObjectProperty(IRI.create(pre_SOSAOnt + "observedProperty"));
		OWLObjectPropertyDomainAxiom observedPropertydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(observedProperty,
				Observation);
		OWLObjectPropertyRangeAxiom observedPropertyrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(observedProperty,
				ObservableProperty);
		ontology.add(factory.getOWLDeclarationAxiom(observedProperty));
		ontology.add(observedPropertydomainAxiom);
		ontology.add(observedPropertyrangeAxiom);

		OWLObjectProperty hosts = factory.getOWLObjectProperty(IRI.create(pre_SOSAOnt + "hosts"));
		OWLObjectPropertyDomainAxiom hostsdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hosts, Resource);
		OWLObjectPropertyRangeAxiom hostsrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hosts, Sensor);
		ontology.add(factory.getOWLDeclarationAxiom(hosts));
		ontology.add(hostsdomainAxiom);
		ontology.add(hostsrangeAxiom);

		OWLObjectProperty concernBy = factory.getOWLObjectProperty(IRI.create(ns, "concernBy"));
		OWLObjectPropertyDomainAxiom concernBydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(concernBy, Resource);
		OWLObjectPropertyRangeAxiom concernByrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(concernBy, Situation);
		ontology.add(factory.getOWLDeclarationAxiom(concernBy));
		ontology.add(concernBydomainAxiom);
		ontology.add(concernByrangeAxiom);
		
		OWLObjectProperty hasObservableProperty = factory.getOWLObjectProperty(IRI.create(ns, "hasObservableProperty"));
		OWLObjectPropertyDomainAxiom hasObservablePropertydomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasObservableProperty, Resource);
		OWLObjectPropertyRangeAxiom hasObservablePropertyrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasObservableProperty, ObservableProperty);
		ontology.add(factory.getOWLDeclarationAxiom(hasObservableProperty));
		ontology.add(hasObservablePropertydomainAxiom);
		ontology.add(hasObservablePropertyrangeAxiom);

		OWLObjectProperty hasTime = factory.getOWLObjectProperty(IRI.create(ns, "hasTime"));
		OWLObjectPropertyDomainAxiom hasTimedomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasTime, Observation);
		OWLObjectPropertyRangeAxiom hasTimerangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasTime, TemporalEntity);
		ontology.add(factory.getOWLDeclarationAxiom(hasTime));
		ontology.add(hasTimedomainAxiom);
		ontology.add(hasTimerangeAxiom);
		
		OWLObjectProperty isInSituation = factory.getOWLObjectProperty(IRI.create(ns + "isInSituation"));
		OWLObjectPropertyDomainAxiom isInSituationdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(isInSituation,
				Observation);
		OWLObjectPropertyRangeAxiom isInSituationrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(isInSituation,
				Situation);
		ontology.add(factory.getOWLDeclarationAxiom(isInSituation));
		ontology.add(isInSituationdomainAxiom);
		ontology.add(isInSituationrangeAxiom);
		
		OWLObjectProperty hasObservation = factory.getOWLObjectProperty(IRI.create(ns + "hasObservation"));
		OWLObjectPropertyDomainAxiom hasObservationdomainAxiom = factory.getOWLObjectPropertyDomainAxiom(hasObservation,
				Situation);
		OWLObjectPropertyRangeAxiom hasObservationrangeAxiom = factory.getOWLObjectPropertyRangeAxiom(hasObservation,
				Observation);
		ontology.add(factory.getOWLDeclarationAxiom(hasObservation));
		ontology.add(hasObservationdomainAxiom);
		ontology.add(hasObservationrangeAxiom);

		OWLObjectProperty situationTime = factory.getOWLObjectProperty(IRI.create(ns + "situationTime"));
		OWLObjectPropertyDomainAxiom situationTimedomainAxiom = factory.getOWLObjectPropertyDomainAxiom(situationTime,
				Situation);
		OWLObjectPropertyRangeAxiom situationTimerangeAxiom = factory.getOWLObjectPropertyRangeAxiom(situationTime,
				Interval);
		ontology.add(factory.getOWLDeclarationAxiom(situationTime));
		ontology.add(situationTimedomainAxiom);
		ontology.add(situationTimerangeAxiom);

        OWLDataProperty hasSimpleResult = factory.getOWLDataProperty(IRI.create(pre_SOSAOnt + "hasSimpleResult"));
		OWLDataPropertyDomainAxiom hasSimpleResultdomainAxiom = factory.getOWLDataPropertyDomainAxiom(hasSimpleResult,
				Observation);
		OWLDataPropertyRangeAxiom hasSimpleResultrangeAxiom = factory.getOWLDataPropertyRangeAxiom(hasSimpleResult,
				integerDatatype);
		ontology.add(factory.getOWLDeclarationAxiom(hasSimpleResult));
		ontology.add(hasSimpleResultdomainAxiom);
		ontology.add(hasSimpleResultrangeAxiom);


        /* 
		 * OWL Data Properties
		 */
		OWLDataProperty inXSDDateTimeStamp = factory.getOWLDataProperty(IRI.create(pre_TIME + "inXSDDateTimeStamp"));
		OWLDataPropertyDomainAxiom inXSDDateTimeStampdomainAxiom = factory.getOWLDataPropertyDomainAxiom(inXSDDateTimeStamp,
				Instant);
		OWLDataPropertyRangeAxiom inXSDDateTimeStamprangeAxiom = factory.getOWLDataPropertyRangeAxiom(inXSDDateTimeStamp,
				dateTimeStamp);
		ontology.add(factory.getOWLDeclarationAxiom(inXSDDateTimeStamp));
		ontology.add(inXSDDateTimeStampdomainAxiom);
		ontology.add(inXSDDateTimeStamprangeAxiom);


		/* 
		 * Individuals
		 */
		OWLIndividual PL1 = factory.getOWLNamedIndividual(IRI.create(ns,"PL1"));
		OWLClassAssertionAxiom prodLinePL1 = factory.getOWLClassAssertionAxiom(Line, PL1);
		ontology.add(prodLinePL1);

		OWLIndividual M1 = factory.getOWLNamedIndividual(IRI.create(ns,"M1"));
		OWLClassAssertionAxiom machineM1 = factory.getOWLClassAssertionAxiom(Machine, M1);
		ontology.add(machineM1);

		oa.relateIndividuals(ontology, manager, factory, isPartOf, M1, PL1);
		
		OWLIndividual S_OilTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_OilTemp"));
		OWLClassAssertionAxiom sensorS_OilTemp = factory.getOWLClassAssertionAxiom(Sensor, S_OilTemp);
		ontology.add(sensorS_OilTemp);

		oa.relateIndividuals(ontology, manager, factory, hosts, M1, S_OilTemp);

		OWLIndividual OilTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"OilTemp"));
		OWLClassAssertionAxiom OP_OilTemp = factory.getOWLClassAssertionAxiom(ObservableProperty, OilTemp);
		ontology.add(OP_OilTemp);
		
		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M1, OilTemp);

		File fileformated = new File("/PATH.../Ontology-TP-new.owl");

		try {
			// ontology.saveOntology(System.out);
			ontology.saveOntology(IRI.create(fileformated.toURI()));
		} catch (OWLOntologyStorageException e1) {
			e1.printStackTrace();
		}

		/*
		 * Consistency check
		 */ 

		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory(); 
		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor(); 
		OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor); 
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);
		reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS); 
		
		if (reasoner.isConsistent()) 
			System.out.println ("The ontology is Consistent.");
		else 
			System.out.println ("ERROR");

	}
}