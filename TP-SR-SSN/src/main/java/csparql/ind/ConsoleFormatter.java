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

          if ("S6".equals(situationName)) {
            System.out.println("Scenario S6 detected : M3 filter obstruction");
          } /*else if ("S1".equals(situationName)) {
            System.out.println("Scenario S1 detected : M1 oil leakage");
          }
            */

          OWLClass Situation = factory.getOWLClass(IRI.create(baseUri + "Situation"));
          OWLIndividual situation = factory.getOWLNamedIndividual(IRI.create(baseUri + "-" + System.currentTimeMillis()));
          OWLClassAssertionAxiom situationType = factory.getOWLClassAssertionAxiom(Situation, situation);
          ontology.add(situationType);

          OWLClass Machine = factory.getOWLClass(IRI.create(baseUri + "Machine"));
          OWLIndividual machineInd = factory.getOWLNamedIndividual(IRI.create(t.get(0)));
          OWLClassAssertionAxiom machineType = factory.getOWLClassAssertionAxiom(Machine, machineInd);
          ontology.add(machineType);

          OWLClass ProductionLine = factory.getOWLClass(IRI.create(baseUri + "Line"));
          OWLIndividual prodLineInd = factory.getOWLNamedIndividual(IRI.create(t.get(1)));
          OWLClassAssertionAxiom prodLineType = factory.getOWLClassAssertionAxiom(ProductionLine, prodLineInd);
          ontology.add(prodLineType);

          OWLObjectProperty concernBy = factory.getOWLObjectProperty(IRI.create(baseUri + "concernBy"));
          OWLObjectPropertyAssertionAxiom concernByAssertMachine = factory.getOWLObjectPropertyAssertionAxiom(concernBy, situation, machineInd);
          ontology.add(concernByAssertMachine);
          OWLObjectPropertyAssertionAxiom concernByAssertProdLine = factory.getOWLObjectPropertyAssertionAxiom(concernBy, situation, prodLineInd);
          ontology.add(concernByAssertProdLine);

          try {
            ontology.saveOntology();
          } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
          }

        });
      }
    }
}