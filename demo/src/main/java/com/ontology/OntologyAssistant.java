package com.ontology;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

	public class OntologyAssistant {
		public OWLClass createClass(OWLOntology o, OWLOntologyManager m, OWLDataFactory f, String nam) {
			m.addAxiom(o, f.getOWLDeclarationAxiom(f.getOWLClass(IRI.create(nam))));
			return f.getOWLClass(IRI.create(nam));
		}
		
		public void addAnnotationComment(OWLOntology o, OWLOntologyManager m, OWLDataFactory f, OWLClass c,String com) {
			OWLAnnotation comment = f.getOWLAnnotation(f.getRDFSComment(), f.getOWLLiteral(com, "en"));
			OWLAxiom commentAx = f.getOWLAnnotationAssertionAxiom(c.getIRI(), comment);
			m.addAxiom(o, commentAx);	
		}
		
		public void subClass(OWLOntology o, OWLOntologyManager m, OWLDataFactory f, OWLClass c1, OWLClass c2) {
			OWLAxiom subClassAx = f.getOWLSubClassOfAxiom(c1, c2);
			m.addAxiom(o, subClassAx);		
		}

		public OWLObjectProperty createObjectProperty(OWLOntology o, OWLOntologyManager m, OWLDataFactory f, String prop, OWLClass dom, OWLClass ran) {
			OWLObjectPropertyDomainAxiom domAx = f.getOWLObjectPropertyDomainAxiom(f.getOWLObjectProperty(IRI.create(prop)), dom);
			OWLObjectPropertyRangeAxiom ranAx = f.getOWLObjectPropertyRangeAxiom(f.getOWLObjectProperty(IRI.create(prop)), ran);
			m.addAxiom(o, domAx);
			m.addAxiom(o, ranAx);
			return f.getOWLObjectProperty(IRI.create(prop));
		}
		
		public OWLIndividual createIndividual(OWLOntology o, OWLOntologyManager m, OWLDataFactory f, String ind, OWLClass c) {
		    m.applyChange(new AddAxiom(o, f.getOWLClassAssertionAxiom(c, f.getOWLNamedIndividual(IRI.create(ind)))));
		    return f.getOWLNamedIndividual(IRI.create(ind));
		}
		
		public void relateIndividuals(OWLOntology o, OWLOntologyManager m, OWLDataFactory f, OWLObjectProperty prop, OWLIndividual ind1, OWLIndividual ind2) {
			OWLObjectPropertyAssertionAxiom axiom = f.getOWLObjectPropertyAssertionAxiom(prop, ind1, ind2);
		    m.applyChange(new AddAxiom(o, axiom));	
		}

		public OWLDataProperty createDataProperty(OWLOntology o, OWLOntologyManager m, OWLDataFactory f, String prop, OWLClass dom, OWLDatatype ran) {
			OWLDataPropertyDomainAxiom domAx = f.getOWLDataPropertyDomainAxiom(f.getOWLDataProperty(IRI.create(prop)), dom);
			OWLDataPropertyRangeAxiom ranAx = f.getOWLDataPropertyRangeAxiom(f.getOWLDataProperty(IRI.create(prop)), ran);
			m.addAxiom(o, domAx);
			m.addAxiom(o, ranAx);
			return f.getOWLDataProperty(IRI.create(prop));
		}

		public void assignValueToDataTypeProperty(OWLOntology o, OWLOntologyManager m, OWLDataFactory f, OWLDataProperty dataProp, OWLIndividual ind, OWLLiteral dat) {
			OWLAxiom axiom = f.getOWLDataPropertyAssertionAxiom(dataProp, ind, dat);
			m.applyChange(new AddAxiom(o, axiom));	
		}
	

}