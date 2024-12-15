package csparql.ind;

import eu.larkc.csparql.common.RDFTable;
import eu.larkc.csparql.core.ResultFormatter;
import java.util.Observable;

import org.apache.jena.dboe.sys.Sys;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.hp.hpl.jena.vocabulary.OWL;

public class ConsoleFormatter extends ResultFormatter {
    
    private String situationName;
    private String baseUri;
    private OWLOntology ontology;
    private OWLDataFactory factory;

	  public ConsoleFormatter(String  situationName, String baseUri, OWLOntology ontology, OWLDataFactory factory) {
      this.situationName = situationName;
      this.baseUri = baseUri;
      this.ontology = ontology;
      this.factory = factory;
    }

    @Override
    public void update(Observable o, Object arg) {

      RDFTable rdfTable = (RDFTable)arg;
      System.out.println();
      
      if (rdfTable.size()==0)
        System.out.println("NO RESULT");
      else {
        System.out.println(situationName + " DETECTED. "+ rdfTable.size() + " result at SystemTime: "+System.currentTimeMillis());
        rdfTable.stream().forEach((t) -> {
          String machine = t.get(0);
          String prodline = t.get(1);

          System.out.println(machine + " and " + prodline + " are involved in situation " + situationName);

          OWLClass Situation = factory.getOWLClass(IRI.create(baseUri + "Situation"));
          OWLIndividual situation = factory.getOWLNamedIndividual(IRI.create(baseUri, situationName + "-" + System.currentTimeMillis()));
          OWLClassAssertionAxiom situationType = factory.getOWLClassAssertionAxiom(Situation, situation);
          ontology.add(situationType);

          OWLClass Machine = factory.getOWLClass(IRI.create(baseUri + "Machine"));
          OWLIndividual M3 = factory.getOWLNamedIndividual(IRI.create(t.get(0)));
          OWLClassAssertionAxiom machineM3 = factory.getOWLClassAssertionAxiom(Machine, M3);
          ontology.add(machineM3);

          OWLClass Line = factory.getOWLClass(IRI.create(baseUri + "Line"));
          OWLIndividual PL1 = factory.getOWLNamedIndividual(IRI.create(t.get(1)));
          OWLClassAssertionAxiom prodPL1 = factory.getOWLClassAssertionAxiom(Line, PL1);
          ontology.add(prodPL1);

          OWLObjectProperty concernBy = factory.getOWLObjectProperty(IRI.create(baseUri + "concernBy"));
          OWLObjectPropertyAssertionAxiom concernByAssertM3 = factory.getOWLObjectPropertyAssertionAxiom(concernBy, M3, situation);
          ontology.add(concernByAssertM3);
          OWLObjectPropertyAssertionAxiom concernByAssertPL1 = factory.getOWLObjectPropertyAssertionAxiom(concernBy, PL1, situation);
          ontology.add(concernByAssertPL1);

          // if ("S6".equals(situationName)) {
          //   System.out.println("Scenario S6 detected : M3 filter obstruction");

          //   OWLClass Situation_S6 = factory.getOWLClass(IRI.create(baseUri + "Situation-S6"));
          //   OWLIndividual situation_S6 = factory.getOWLNamedIndividual(IRI.create(baseUri + "-" + System.currentTimeMillis()));
          //   OWLClassAssertionAxiom situationType_S6 = factory.getOWLClassAssertionAxiom(Situation_S6, situation_S6);
          //   ontology.add(situationType_S6);

          //   OWLObjectProperty concernBy = factory.getOWLObjectProperty(IRI.create(baseUri + "concernBy"));
          //   OWLObjectPropertyAssertionAxiom concernByAssertMachine = factory.getOWLObjectPropertyAssertionAxiom(concernBy, situation_S6, machineInd);
          //   ontology.add(concernByAssertMachine);
          //   OWLObjectPropertyAssertionAxiom concernByAssertProdLine = factory.getOWLObjectPropertyAssertionAxiom(concernBy, situation_S6, prodLineInd);
          //   ontology.add(concernByAssertProdLine);

          // } else if ("S1".equals(situationName)) {
          //   System.out.println("Scenario S1 detected : M1 oil leakage");

          //   OWLClass Situation_S1 = factory.getOWLClass(IRI.create(baseUri + "Situation-S1"));
          //   OWLIndividual situation_S1 = factory.getOWLNamedIndividual(IRI.create(baseUri + "-" + System.currentTimeMillis()));
          //   OWLClassAssertionAxiom situationType_S1 = factory.getOWLClassAssertionAxiom(Situation_S1, situation_S1);
          //   ontology.add(situationType_S1);

          //   OWLObjectProperty concernBy = factory.getOWLObjectProperty(IRI.create(baseUri + "concernBy"));
          //   OWLObjectPropertyAssertionAxiom concernByAssertMachine = factory.getOWLObjectPropertyAssertionAxiom(concernBy, situation_S1, machineInd);
          //   ontology.add(concernByAssertMachine);
          //   OWLObjectPropertyAssertionAxiom concernByAssertProdLine = factory.getOWLObjectPropertyAssertionAxiom(concernBy, situation_S1, prodLineInd);
          //   ontology.add(concernByAssertProdLine);
          // }

          try {
            ontology.saveOntology();
            System.out.println("Ontology saved");
          } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
          }

        });
      }
    }
}