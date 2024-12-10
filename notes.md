Concepts :
- Machines (M1, M2, M3, M4)
- Ligne de production
- Propriétés observables (Oil_temps, Trans_temp, Rotor_speed...)
- Sensor
- Obs

Relations :
Machines --isPartOf--> Ligne
Machines --hosts--> Sensor
Ligne --hosts--> Sensor
Sensor --measure--> Propriétés observables
Sensor --makeObs--> Obs
Obs ----> Propriétés observables
Obs ----> value