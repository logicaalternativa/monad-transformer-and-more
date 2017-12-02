# Workshop sobre ¿_Monad transformer_ o es otra cosa mejor? #

Este es un _**taller**_ donde vamos a empezar con un acercamiento a las 
monadas desde **Java** intentando solucionar un hipotético problema que 
tenemos: intentar combinar dos efectos el Futuro y el Either, cuando 
simulamos llamadas a servicios reactivos implementado a partir de 
actores de Akka  y que devuleven `Future<Either>`. Parece que la 
solución va a ser implementar una clase de utilidades que nos ayude a 
gestionar todo esto.

Por otra parte imaginemos que un cliente para lo que tiene no necesita
esa complejidad y quiere un servicio "_como los de toda la vida_". Para 
ello tenemos los mismos servicios con la misma funcionalidad y que 
retornan nuestro propio contenedor que no es "nada sospechoso" de ser 
una monada.

Primero nos daremos cuenta que la lógica de negocio cambiará si 
elegimos uno u otro tipo de servicios y entonces es cuando nos 
preguntamos ¿Por qué tengo que cambiar mi lógica de negocio si cambio el
tipo que devuelve el resultado?


## Primer enfoque ##

Vemos que con nuestra propia implementación de la MonadError podemos 
solucionar el problema tanto de la nuestra "_Monad transformer_" como
de nuestro contenedor y empezamos a tener la sensación de estar
utilizando un **DSL**. Pero (siempre hay un pero) Nos obliga a tener 
código repetido y nos gustaría tener nuestra lógica de una manera
más clara y declarativa


## La solución ##

Llega **Scala** al rescate con la '_magia_' de sus '_constructores de 
tipos_' y sus '_implícitos_' y su poder de genera DSLs de manera 
elegante. Veremos que se puede tener la lógica definida de manera 
declarativa de una manera elegante independiente de si se trabaja de 
manera síncrona o asíncrona, y con perfecta integración con código 
**Java**.

# Esquema de composición de la información #

![Esquema]( ./doc/secuence.plantuml.svg )
