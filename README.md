
# Industrial stream reasoning system

[![License: MIT](https://img.shields.io/badge/License-MIT-purple.svg)](https://opensource.org/licenses/MIT)

## 🔍 Overview
This project implements a stream reasoning system for real-time detection of abnormal situations in an industrial environment. It uses C-SPARQL to process RDF streams from various sensors in a production line and detect predefined anomalies based on expert knowledge encoded in an ontology.

## 📋 Application scenario
The system monitors a manufacturing production line (PL1) composed of four machines (M1, M2, M3, and M4), each equipped with different sensors measuring properties like temperature, speed, and current. The goal is to detect abnormal situations (like cooling system failures, oil leaks, etc.) that could lead to machine malfunctions or production line failures.

## 🏭 Ontology design
The system utilizes a custom ontology built with the OWL API that models the industrial environment. Key components include:

### Classes
- **Resource**: Parent class for physical components
  - **Machine**: Represents individual machines (M1, M2, M3, M4)
  - **Line**: Represents production lines (PL1)
- **Situation**: Represents abnormal conditions (S6, S10)
- **Sensor**: Represents measurement devices attached to resources
- **ObservableProperty**: Properties that sensors can measure
- **Observation**: Individual sensor readings
- **TemporalEntity**: Time information (Instant, Interval)

### Object Properties
- **isPartOf**: Relates machines to production lines
- **hosts**: Relates resources to their attached sensors
- **madeObservation**: Links sensors to their observations
- **observedProperty**: Links observations to what they measure
- **hasObservableProperty**: Links resources to their observable properties
- **concernBy**: Links resources to situations affecting them
- **isInSituation**: Links observations to detected situations
- **hasTime**: Temporal information for observations

### Data Properties
- **hasSimpleResult**: Stores numerical observation values
- **inXSDDateTimeStamp**: Stores time information

### Ontology Files
- `Ontology-TP-new.owl`: Ontology automatically generated at runtime by the code in the _./demo/_ folder. It models the production environment, machines, sensors, properties, and relationships.

- `Ontology-zoe-charlotte.owl`: A manually renamed copy of _Ontology-TP-new.owl_, used as the base ontology to initialize the C-SPARQL engine during stream reasoning.

## 📐 Project structure
```bash
.
├── demo/
│   └── Ontology generation files
├── TP-SR-SSN/
│   └── Stream reasoning application files
└── README.md
```

## ⚡ How it works
1. The application builds the ontology structure defining resources, sensors, and their relationships
2. The ontology is saved as an OWL file for later use by the C-SPARQL engine
3. The C-SPARQL engine initializes and loads the ontology
4. Sensor streams are registered for various measurements
5. C-SPARQL queries detect abnormal situations based on predefined thresholds
6. When a situation is detected, the ontology is updated to record the anomaly

## 💥 Implemented situations
The implementation focuses on two specific anomaly situations:

1. **S6**: Cooling system failure of M3 machine, detected when:
   * Converter water temperature > 60°C
   * Transformer grid temperature < 35°C
   * Generator temperature > 45°C

2. **S10**: More severe malfunction of M3 machine, detected when:
   * Converter water temperature > 80°C
   * Transformer grid temperature < 35°C
   * Generator temperature > 45°C
   * Converter temperature > 60°C

## 📜 Requirements
* Java 8 or higher
* Maven (for dependency management)
* OWL API
* C-SPARQL engine
* Log4j

## 🎓 Academic context
This project was developed during the second year of the Master’s program in Data Science and Complex Systems at the University of Strasbourg.

## 📝 Documentation
For more detailed information about this project, the [full project report](./resources/project-report-fr.pdf) (in french) and the [original assignment specifications](./resources/project-assignment.pdf) are available in the `./resources` directory.

## 👷‍♂️ Contributors
- Zoé Marquis
- Charlotte Kruzic
