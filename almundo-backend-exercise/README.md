# almundo-backend-exercise

Ejercicio backend para el examen de ingreso de almundo.com 

## Consigna/Requerimientos/Extras



Existe un call center donde hay 3 tipos de empleados: operador, supervisor y director. El proceso de la atención de una llamada
telefónica en primera instancia debe ser atendida por un operador, si no hay ninguno libre debe ser atendida por un supervisor, y de no
haber tampoco supervisores libres debe ser atendida por un director.

1. Diseñar el modelado de clases y diagramas UML necesarios para documentar y comunicar el diseño.
2. Debe existir una clase Dispatcher encargada de manejar las llamadas, y debe contener el método dispatchCall para que las
asigne a los empleados disponibles.
3. La clase Dispatcher debe tener la capacidad de poder procesar 10 llamadas al mismo tiempo (de modo concurrente). Cada llamada puede durar un tiempo aleatorio entre 5 y 10 segundos.
4. Debe tener un test unitario donde lleguen 10 llamadas.


* Dar alguna solución sobre qué pasa con una llamada cuando no hay ningún empleado libre.
* Dar alguna solución sobre qué pasa con una llamada cuando entran más de 10 llamadas concurrentes.
* Agregar los tests unitarios que se crean convenientes.
* Agregar documentación de código.

## Modelado

![alt text](https://raw.githubusercontent.com/ewatemberg/almundo-backend-exercise/master/Diseño.jpg)

![alt text](https://raw.githubusercontent.com/ewatemberg/almundo-backend-exercise/master/Diagrama.jpg)


## Descripción

Algunas consideraciones de la solución:
- Se opto por manejar las asignaciones con colas, limitadas y priorizadas.
- Las mismas permiten el acceso concurrente para ambas operaciones.
- Se definió crear una cola comun del tipo "ConcurrentLinkedQueue" sin un límite apara asignar las tareas (llamadas) ON HOLD, es decir, para encolar las tareas cuando todos los empleados se encuentran con un trabajo en curso.
- Se agregaron configuraciones para asignar a cada tarea un tiempo aproximado al solicitado en el enunciado.

## Build

    mvn clean install


## Demo

Para correr la aplicacion, Run> App.java. 
Este ejecutable, solo contiene un ejemplo. Similar al caso 1 de testing.

### Testing 

Se crearon 2 test unitarios que permiten probar el 2 casos que contemplan la resolución de los puntos extras:

* Caso 1:
Permite la ejecucion concurrente de 10 llamadas (task). Estas son atendidas por los empleados (OPERADOR/SUPERVISOR/DIRECTOR) jerarquicamente. 
En la medida que los empleados van quedando asignados e ingresan llamados, las asignaciones van cambiando en base al rol del empleado. Es decir, primero se asignan los OPERADORES, luego los SUPERVISORES y por último los DIRECTORES.
Por otro lado si no existen empleados disponibles se asignan a una cola de espera que deberá ser tomada por los mismo en la medida que se liberen (feature a desarrollar => hacer que los hilos trabajen hasta que las colas queden vacias.)
Como excepción a la prueba solo admite una sola corrida, pero dado que es recurrente se puede evaluar el comportamiento de las asignaciones.

* Caso 2: 
Permite la ejecución concunrrentes de 10 llamadas (task). Estas al igual que el test anterior son atendidas por los empleados. Para simular la asignación de tareas ON_HOLD, se definió un alcance o limite de asignaciones para cada rol. Cuando agregamos mas empleados que atiendan el trabajo, empezaran a tomar los mismos en la medida que no tengan asignaciones correspondientes a sus roles. En este caso al estar desasignados tomarán tareas de la cola ON_HOLD.



 

