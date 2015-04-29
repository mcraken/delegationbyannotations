# Delegation By Annotations

Ah, the joy of software design. In this article, I will provide a concise guide on how to convert a template or strategy based design into an implementation driven by annotation. This concept might be employed in the standard implementation of JEE CDI or any other popular frameworks like Spring. If you are not familiar with design patterns, I encourage you to pick up the catalog of design patterns classic by gang of four.

## Delegation

Delegation as a concept is not exclusive to software. On the contrary, we use delegation as a tool in our daily routines without giving too much thought about it. To better understand delegation as a philosophy or school of though, we first need to understand the context of execution as opposed to the specifics of execution. 

Let us say, that someone needs to get from point A to B in a timely manner. The answer to this challenge is location and speed. You need to know the location of B and you need to get there relatively fast. So the context of executing the task at hand is distance and velocity. Up to this point we have not discussed the specifics of execution which is how to locate and travel to point B. You can locate B using a map which leads to proper calculation of distance. After calculating the distance, you can calculate the velocity on which you need to travel to point B and hence determine you selection of transportation.

``java
    class TravelingTask{

      private Locator locator;
      private Vehicle vehicle; 

      public TravelingTask(Locator locator, Vehicle vehicle){
        this.locator = locator;
        this.vehicle = vehicle;
      } 

      public void travel(Point a, Point b, Time time){
        Double distance = locator.calculate(a, b);
        vehicle.go(distance, time);
      }
    }
``
      
