# Delegation By Annotations

Ah, the joy of software design. In this article, I will provide a concise guide on how to convert a template or strategy based design into an implementation driven by annotation. This concept might be employed in the standard implementation of JEE CDI or any other popular frameworks like Spring. If you are not familiar with design patterns, I encourage you to pick up the catalog of design patterns classic by gang of four.

## Delegation

Delegation as a concept is not exclusive to software. On the contrary, we use delegation as a tool in our daily routines without giving too much thought about it. To better understand delegation as a philosophy or school of though, we first need to understand the context of execution as opposed to the specifics of execution. 

## Traveling Task

Let us say, that someone needs to get from point A to B in a timely manner. The answer to this challenge is location and speed. You need to know the location of B and you need to get there relatively fast. So the context of executing the task at hand is distance and velocity. Up to this point we have not discussed the specifics of execution which is how to locate and travel to point B. You can locate B using a map which leads to proper calculation of distance. After calculating the distance, you can calculate the velocity on which you need to travel to point B and hence determine your selection of transportation.

Now let's propose a solution for the above challenge. First, the context of execution is the travel method which consists of two operations calculate distance and calculate time. 

```java
	// Delegation by inheritence
	public Long travel(Point a, Point b){

		Double distance = calculateDistance(a, b, map);

		return calculateTime(transportation, distance);

	}
	
	protected abstract Long calculateTime(Double distance);

	protected abstract  Double calculateDistance(Point a, Point b);
```
Both of calculate time and distance are template methods. Template methods is a form of delegation by inhetitence. Delegation by inheritence postpones the implementation until its picked up by one of the subclasses of the execution context.
