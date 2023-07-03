# Comprehensions

The Java implementation of List Comprehensions is perhaps the worst in any modern program language

* There is no support for lambdas that throw checked exceptions. This means that you have to write a lot of boiler plate code to do anything useful.
* They only work on streams, so you constantly have to convert between lists and streams
  * And streams are the wrong thing to use: The essence of a stream is laziness. They are not reusable
  * Our core code wants the essence of 'sequence' and a stream is a very poor model for sequence
* The collector pattern is a lot of boilerplate code, and we want to remove boilerplate code from our systems

This library lets us do list comprehensions on code that returns exceptions reasonably simply

It is not designed to do 'a.map.flatmap.filter.map....' It's designed for the 90% of code that is just a map or a fold