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

                try {
                    OWLClass Situation = factory.getOWLClass(IRI.create(baseUri + "Situation"));
                    OWLIndividual situationInstance = factory.getOWLNamedIndividual(IRI.create(baseUri + situationName + "-" + System.currentTimeMillis()));
                    OWLClassAssertionAxiom situationType = factory.getOWLClassAssertionAxiom(Situation, situationInstance);
                    ontology.add(situationType);

                    OWLClass Machine = factory.getOWLClass(IRI.create(baseUri + "Machine"));
                    OWLIndividual machineInstance = factory.getOWLNamedIndividual(IRI.create(machine));
                    OWLClassAssertionAxiom machineType = factory.getOWLClassAssertionAxiom(Machine, machineInstance);
                    ontology.add(machineType);

                    OWLClass ProductionLine = factory.getOWLClass(IRI.create(baseUri + "Line"));
                    OWLIndividual productionLineInstance = factory.getOWLNamedIndividual(IRI.create(prodline));
                    OWLClassAssertionAxiom productionLineType = factory.getOWLClassAssertionAxiom(ProductionLine, productionLineInstance);
                    ontology.add(productionLineType);

                    OWLObjectProperty concernBy = factory.getOWLObjectProperty(IRI.create(baseUri + "concernsBy"));
                    OWLObjectPropertyAssertionAxiom situationConcernsMachine = factory.getOWLObjectPropertyAssertionAxiom(concernBy, machineInstance, situationInstance);
                    ontology.add(situationConcernsMachine);

                    OWLObjectPropertyAssertionAxiom situationConcernsProductionLine = factory.getOWLObjectPropertyAssertionAxiom(concernBy, productionLineInstance, situationInstance);
                    ontology.add(situationConcernsProductionLine);

                    ontology.saveOntology();
                    System.out.println("Ontology saved successfully for " + situationName);

                } catch (OWLOntologyStorageException e) {
                    System.err.println("Error saving ontology: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }
}