package com.ontology;

import java.io.File;

import org.apache.jena.vocabulary.OWL;
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

		OWLClass Situation_S6 = factory.getOWLClass(IRI.create(ns, "Situation-S6"));
		ontology.add(factory.getOWLDeclarationAxiom(Situation_S6));
		ontology.add(factory.getOWLSubClassOfAxiom(Situation_S6, Situation));

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

		OWLIndividual M2 = factory.getOWLNamedIndividual(IRI.create(ns,"M2"));
		OWLClassAssertionAxiom machineM2 = factory.getOWLClassAssertionAxiom(Machine, M2);
		ontology.add(machineM2);

		oa.relateIndividuals(ontology, manager, factory, isPartOf, M2, PL1);

		OWLIndividual M3 = factory.getOWLNamedIndividual(IRI.create(ns,"M3"));
		OWLClassAssertionAxiom machineM3 = factory.getOWLClassAssertionAxiom(Machine, M3);
		ontology.add(machineM3);

		oa.relateIndividuals(ontology, manager, factory, isPartOf, M3, PL1);

		OWLIndividual M4 = factory.getOWLNamedIndividual(IRI.create(ns,"M4"));
		OWLClassAssertionAxiom machineM4 = factory.getOWLClassAssertionAxiom(Machine, M4);
		ontology.add(machineM4);

		oa.relateIndividuals(ontology, manager, factory, isPartOf, M4, PL1);
		
		OWLIndividual S_OilTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_OilTemp"));
		OWLClassAssertionAxiom sensorS_OilTemp = factory.getOWLClassAssertionAxiom(Sensor, S_OilTemp);
		ontology.add(sensorS_OilTemp);

		oa.relateIndividuals(ontology, manager, factory, hosts, M1, S_OilTemp);

		OWLIndividual OilTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"OilTemp"));
		OWLClassAssertionAxiom OP_OilTemp = factory.getOWLClassAssertionAxiom(ObservableProperty, OilTemp);
		ontology.add(OP_OilTemp);
		
		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M1, OilTemp);

		OWLIndividual S_TransformerTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_TransformerTemp"));
		OWLClassAssertionAxiom sensorS_TransformerTemp = factory.getOWLClassAssertionAxiom(Sensor, S_TransformerTemp);
		ontology.add(sensorS_TransformerTemp);

		oa.relateIndividuals(ontology, manager, factory, hosts, M1, S_TransformerTemp);

		OWLIndividual TransformerTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"TransformerTemp"));
		OWLClassAssertionAxiom OP_TransformerTemp = factory.getOWLClassAssertionAxiom(ObservableProperty, TransformerTemp);
		ontology.add(OP_TransformerTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M1, TransformerTemp);

		OWLIndividual S_ControlerTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_ControlerTemp"));
		OWLClassAssertionAxiom sensorS_ControlerTemp = factory.getOWLClassAssertionAxiom(Sensor, S_ControlerTemp);
		ontology.add(sensorS_ControlerTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M1, S_ControlerTemp);

		OWLIndividual ControlerTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"ControlerTemp"));
		OWLClassAssertionAxiom OP_ControlerTemp = factory.getOWLClassAssertionAxiom(ObservableProperty, ControlerTemp);
		ontology.add(OP_ControlerTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M1, ControlerTemp);

		OWLIndividual S_GeneratorCurr = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_GeneratorCurr"));
		OWLClassAssertionAxiom sensorS_GeneratorCurr = factory.getOWLClassAssertionAxiom(Sensor, S_GeneratorCurr);
		ontology.add(sensorS_GeneratorCurr);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M1, S_GeneratorCurr);

		OWLIndividual GeneratorCurr = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"GeneratorCurr"));
		OWLClassAssertionAxiom OP_GeneratorCurr = factory.getOWLClassAssertionAxiom(ObservableProperty, GeneratorCurr);
		ontology.add(OP_GeneratorCurr);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M1, GeneratorCurr);

		OWLIndividual S_PlatformTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_PlatformTemp"));
		OWLClassAssertionAxiom sensorS_PlatformTemp = factory.getOWLClassAssertionAxiom(Sensor, S_PlatformTemp);
		ontology.add(sensorS_PlatformTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, PL1, S_PlatformTemp);

		OWLIndividual PlatformTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"PlatformTemp"));
		OWLClassAssertionAxiom OP_PlatformTemp = factory.getOWLClassAssertionAxiom(ObservableProperty, PlatformTemp);
		ontology.add(OP_PlatformTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, PL1, PlatformTemp);

		OWLIndividual S_GearboxTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_GearboxTemp"));
		OWLClassAssertionAxiom sensorS_GearboxTemp = factory.getOWLClassAssertionAxiom(Sensor, S_GearboxTemp);
		ontology.add(sensorS_GearboxTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M2, S_GearboxTemp);

		OWLIndividual GearboxTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"GearboxTemp"));
		OWLClassAssertionAxiom OP_GearboxTemp = factory.getOWLClassAssertionAxiom(ObservableProperty, GearboxTemp);
		ontology.add(OP_GearboxTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M2, GearboxTemp);

		OWLIndividual S_GeneratorSpeed = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_GeneratorSpeed"));
		OWLClassAssertionAxiom sensorS_GeneratorSpeed = factory.getOWLClassAssertionAxiom(Sensor, S_GeneratorSpeed);
		ontology.add(sensorS_GeneratorSpeed);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M2, S_GeneratorSpeed);

		OWLIndividual GeneratorSpeed = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"GeneratorSpeed"));
		OWLClassAssertionAxiom OP_GeneratorSpeed = factory.getOWLClassAssertionAxiom(ObservableProperty, GeneratorSpeed);
		ontology.add(OP_GeneratorSpeed);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M2, GeneratorSpeed);

		OWLIndividual S_EnvironmentTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_EnvironmentTemp"));
		OWLClassAssertionAxiom sensorS_EnvironmentTemp = factory.getOWLClassAssertionAxiom(Sensor, S_EnvironmentTemp);
		ontology.add(sensorS_EnvironmentTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, PL1, S_EnvironmentTemp);

		OWLIndividual EnvironmentTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"EnvironmentTemp"));
		OWLClassAssertionAxiom OP_EnvironmentTemp = factory.getOWLClassAssertionAxiom(ObservableProperty, EnvironmentTemp);
		ontology.add(OP_EnvironmentTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, PL1, EnvironmentTemp);

		OWLIndividual S_PowerOutput = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_PowerOutput"));
		OWLClassAssertionAxiom sensorS_PowerOutput = factory.getOWLClassAssertionAxiom(Sensor, S_PowerOutput);
		ontology.add(sensorS_PowerOutput);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, PL1, S_PowerOutput);

		OWLIndividual PowerOutput = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"PowerOutput"));
		OWLClassAssertionAxiom OP_PowerOutput = factory.getOWLClassAssertionAxiom(ObservableProperty, PowerOutput);
		ontology.add(OP_PowerOutput);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, PL1, PowerOutput);

		OWLIndividual S_ConvWaterTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_ConvWaterTemp"));
		OWLClassAssertionAxiom sensorS_ConvWaterTemp = factory.getOWLClassAssertionAxiom(Sensor, S_ConvWaterTemp);
		ontology.add(sensorS_ConvWaterTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M3, S_ConvWaterTemp);

		OWLIndividual ConvWaterTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"ConvWaterTemp"));
		OWLClassAssertionAxiom OP_ConvWaterTemp = factory.getOWLClassAssertionAxiom(ObservableProperty, ConvWaterTemp);
		ontology.add(OP_ConvWaterTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M3, ConvWaterTemp);

		OWLIndividual S_TransGridTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_TransGridTemp"));
		OWLClassAssertionAxiom sensorS_TransGridTemp = factory.getOWLClassAssertionAxiom(Sensor, S_TransGridTemp);
		ontology.add(sensorS_TransGridTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M3, S_TransGridTemp);

		OWLIndividual TransGridTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"TransGridTemp"));
		OWLClassAssertionAxiom OP_TransGridTemp = factory.getOWLClassAssertionAxiom(ObservableProperty, TransGridTemp);
		ontology.add(OP_TransGridTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M3, TransGridTemp);

		OWLIndividual S_GeneratorTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_GeneratorTemp"));
		OWLClassAssertionAxiom sensorS_GeneratorTemp = factory.getOWLClassAssertionAxiom(Sensor, S_GeneratorTemp);
		ontology.add(sensorS_GeneratorTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M3, S_GeneratorTemp);

		OWLIndividual GeneratorTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"GeneratorTemp"));
		OWLClassAssertionAxiom OP_GeneratorTemp = factory.getOWLClassAssertionAxiom(ObservableProperty, GeneratorTemp);
		ontology.add(OP_GeneratorTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M3, GeneratorTemp);

		OWLIndividual S_ConverterTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_ConverterTemp"));
		OWLClassAssertionAxiom sensorS_ConverterTemp = factory.getOWLClassAssertionAxiom(Sensor, S_ConverterTemp);
		ontology.add(sensorS_ConverterTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M3, S_ConverterTemp);

		OWLIndividual ConverterTemp = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"ConverterTemp"));
		OWLClassAssertionAxiom OP_ConverterTemp = factory.getOWLClassAssertionAxiom(ObservableProperty, ConverterTemp);
		ontology.add(OP_ConverterTemp);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M3, ConverterTemp);

		OWLIndividual S_RotorSpeed = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_RotorSpeed"));
		OWLClassAssertionAxiom sensorS_RotorSpeed = factory.getOWLClassAssertionAxiom(Sensor, S_RotorSpeed);
		ontology.add(sensorS_RotorSpeed);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M4, S_RotorSpeed);

		OWLIndividual RotorSpeed = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"RotorSpeed"));
		OWLClassAssertionAxiom OP_RotorSpeed = factory.getOWLClassAssertionAxiom(ObservableProperty, RotorSpeed);
		ontology.add(OP_RotorSpeed);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M4, RotorSpeed);

		OWLIndividual S_RotorPitchAngle = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"S_RotorPitchAngle"));
		OWLClassAssertionAxiom sensorS_RotorPitchAngle = factory.getOWLClassAssertionAxiom(Sensor, S_RotorPitchAngle);
		ontology.add(sensorS_RotorPitchAngle);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M4, S_RotorPitchAngle);

		OWLIndividual RotorPitchAngle = factory.getOWLNamedIndividual(IRI.create(pre_SOSAOnt,"RotorPitchAngle"));
		OWLClassAssertionAxiom OP_RotorPitchAngle = factory.getOWLClassAssertionAxiom(ObservableProperty, RotorPitchAngle);
		ontology.add(OP_RotorPitchAngle);

		oa.relateIndividuals(ontology, manager, factory, hasObservableProperty, M4, RotorPitchAngle);







		File fileformated = new File("Ontology-TP-new.owl");

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