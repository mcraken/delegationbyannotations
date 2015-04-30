# Delegation By Annotations

Ah, the joy of software design. In this tutorial, I will provide a concise guide on how to convert a template or strategy based design into an implementation driven by annotation. Similar and more advanced concepts could be found in an implementation of JEE CDI or any other popular frameworks like Spring. If you are not familiar with design patterns, I encourage you to pick up the catalog of design patterns classic by gang of four.

## Delegation

Delegation as a concept is not exclusive to software. On the contrary, we use delegation as a tool in our daily routines without giving too much thought about it. To better understand delegation as a philosophy or school of though, we first need to understand the context of execution as opposed to the specifics of execution. 

## Traveling Task

Let us say, that someone needs to get from point A to B in a timely manner. The answer to this challenge is location and speed. You need to know the location of B and you need to get there relatively fast. So the context of executing the task at hand is distance and velocity. Up to this point we have not mentioned the specifics of execution which is how to locate and travel to point B. You can locate B using a map which leads to proper calculation of distance. After calculating the distance, you can calculate the velocity on which you need to travel to point B and hence determine your selection of transportation.

### Inhertience v Composition

Now let's propose a solution for the above challenge. First, the context of execution is the travel method which consists of two operations calculate distance and calculate time. 

```java
	// Delegation by inheritence
	public Long travel(Point a, Point b){

		Double distance = calculateDistance(a, b);

		return calculateTime(distance);

	}
	
	protected abstract Long calculateTime(Double distance);

	protected abstract  Double calculateDistance(Point a, Point b);
```
Both of calculate time and distance are template methods. Template design pattern is a form of delegation by inhetitence. Delegation by inheritence postpones the implementation until its picked up by one of the subclasses of the execution context. Here is another solution to the same problem, let us create two interfaces each of which corresponds to one of the template methods as follows:

```java
public interface Locator {
	public Double calculateDistance(Point a, Point b);
}

public interface Transportation {
	public Long calculateTime(Double distance);
}
```

and then we could change the travel method to depend on the two interfaces as follows:

```java
	// Delegation by composition
	public Long travel(Point a, Point b){
		 
		Double distance = locator.calculateDistance(a, b);
		
		return transport.calculateTime(distance);
	}
```

Both locator and vehicle are an actulization of strategy design pattern. The startegy design pattern is a form of delegation by composition. As you can see both of calculate distance and time are moved to its respective interfaces. 

Delegation by composition is more prefered to delegation by inheritence due to the flexability provided by dependency injection. Dependency injection allwos you to inject different implementations to the same refernce at runtime based on the configurations of the system. We have many options from which we can select a vehicle implemenation like a bicycle, a car or a train. We also have different selections of locating tools like a paper or computer based map.

```java
	enum Vehicle{
		BICYCLE(), CAR(), TRAIN();
	}
	
	enum MAP{
		PAPER(), COMPUTERIZED();
	}
```
We can tweak our execution context to cache the available implementations of the delegate references and allow the caller to decide which implementation to use at runtime.

```java
	public Long travel(Point a, Point b, Vehicle vehicle, MAP map){
		
		Locator locator = locators.get(map);
		
		if(locator == null)
			throw new RuntimeException("Unsupported locator");
		
		Transportation transport = vehicles.get(vehicle);
		
		if(transport == null)
			throw new RuntimeException("Unsupported transport");
		
		Double distance = locator.calculateDistance(a, b);
		
		return transport.calculateTime(distance);
	}
```
Up to this point we have three aspects to consider:
- Execution context where a finer grained tasks is oragnized to solve a problem.
- Delegates that provide an implementation for the finer grained tasks.
- Execution configuration which chooses between delegate implementation alternates.  

## Using Annotations
As flexible as the above exmaple is, you have to do different things in order to introduce new functionality inside the system. For exmaple, you have to implement a new Locator or Transportation and add it to the traveling task. It may sound easy for such a trivial system but for more complex systems it may pose some sort of a challenge. It might be easier if we just could find a mechanism to intoduce a new functionality without going through all these steps. 

We could leverage annotations as a reference point of delegation on any method without resorting to implementing any interfaces.
```java
class AnnotatedEntity{

	@TransportationVehicle(vehicle=Vehicle.CAR)
	public Long someMethod(Double distance){
		// Do some calculations
		return timeInSeconds;
	}
	
	@MapLocator(map=Map.COMPUTERIZED)
	public Double someOtherMethod(Point a, Point b){
		// Do some calculations
		return distance;
	}
}
```
And then register whatever entity using the delegate annotation on the delegator like this

```java
AnnotatedEntity someAnnotatedEntity = new AnnotatedEntity();
TravelingTask travelingTask = new TravelingTask();
travelingTask.registerDelegate(someAnnotatedEntity);
```
That it! we could leave the heavy lifting to be taking care of by the delegator. Such as registering the delegate and finding the right methods to call at runtime. The rest of this tutorial I will explain how to implement delegation by annotation. The proposed solution should be taken as a refernece and not as a fully fledged solution.

## Delegation Principal 

A delegation principal applies validation rules on any arbitrary method assigned with a certain annotation, in case of being a good match it register that particular method with the delgator as a future execution refernce. Taking into account that the delegate interface should at least declare one method with a Delegate annotation. Now, let us look at some sequence diagrams that explains how delegation principal works.

### Definition

```java
// D -> Any arbitrary delegate, A -> Any annotation type.
abstract class DelegationPrincipal<D, A extends Annotation> {

	private Class<A> annotationClass;
	
	// Delegation agent should register the delegate with the delegator class
	private DelegationAgent<D, A> delegationAgent;
	
	// Signature of delegate method.
	private Class<?> returnType;
	private Class<?>[] parameters;
```

### Creation

![create delegation principal](https://cloud.githubusercontent.com/assets/6278849/7410334/1be9d0d0-ef2e-11e4-9652-1a23fae3fd49.jpg)

```java
	public DelegationPrincipal(
			Class<D> delegateClass,
			Class<A> annotationClass,
			DelegationAgent<D, A> delegationAgent) {
		
		this.annotationClass = annotationClass;
		this.delegationAgent = delegationAgent;
		
		// Use delegate class to find a method annotated with Delegate annotation
		Method delegateMethod = findDelegateMethod(delegateClass);
		
		this.returnType = delegateMethod.getReturnType();
		this.parameters = delegateMethod.getParameterTypes();
	}
	
	private Method findDelegateMethod(Class<D> delegateClass) {
		
		Method delegateMethod = null;
		
		for(Method method : delegateClass.getMethods()){
			if(method.getAnnotation(Delegate.class) != null){
				delegateMethod = method;
				break;
			}
		}
		
		if(delegateMethod == null)
			throw new RuntimeException("Could find any method annotated with delegate");
		
		return delegateMethod;
	}
```
### Apply
![apply](https://cloud.githubusercontent.com/assets/6278849/7410720/d40f464c-ef31-11e4-96f8-a41b4a54f114.jpg)

```java
	public void apply(Object receiver, Method target){
		
		// find the if method is assigned with target annotation.
		A annotation = getTargetAnnotation(target);

		if(annotation != null ){
			
			// checks against method signature to gurantee full match
			// throws runtime exception in case of failure
			isMethodValid(target, annotation);
			
			// Returns an instance of delegate class,
			// apply is a template method and 
			// should be implemented by child classes
			D delegate = apply(receiver, target, annotation);
			
			// Delegation agent should be aware of how to register with the delegator
			delegationAgent.register(annotation, delegate);
		}
	
	protected abstract D apply(Object receiver, Method target, A annotation);
	
	}
```

## Implementing Principals
The following code snippets explain how to extend delegation principal in order to create two new principals one for the Transportation interface and another one for the Locator.

### Locator

Start with the annotation

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MapLocator {
	MAP map();
}
```
Add @Delegate annotation to calculateDistance as follows
```java
	@Delegate	
	public Double calculateDistance(Point a, Point b);
```
Now, create the LocatorPrincipal class

```java

// D -> Locator and A -> MapLocator annotation. 
class LocatorPrincipal extends DelegationPrincipal<Locator, MapLocator> {

	public LocatorPrincipal(Class<Locator> delegateClass,
			Class<MapLocator> annotationClass,
			DelegationAgent<Locator, MapLocator> delegationAgent) {
		
		super(delegateClass, annotationClass, delegationAgent);
	}
	
	@Override
	protected Locator apply(final Object receiver, final Method target,
			final MapLocator annotation) {
		
		// Create new locator delegate instance to register with the delgator class.
		return new Locator() {
			
			public Double calculateDistance(Point a, Point b) {
				
				try {
					// invoke target method to calculate the distance				
					return (Double) target.invoke(receiver, a, b);
					
				} catch (Exception e) {
					
					throw new RuntimeException(annotation.map() + " locator failed", e);
				}
			}
		};
	}
}
```

### Transportation

Again, Start with the annotation

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TransportationVehicle {
	Vehicle vehicle();
}
```

Add @Delegate annotation to calculateTime as follows

```java
	@Delegate
	public Long calculateTime(Double distance);
```

Now, create the TransportationPrincipal class

```java
// D -> Transportation, A -> TransportationVehicle annotation.
class TransportationPrincipal extends DelegationPrincipal<Transportation, TransportationVehicle> {

	public TransportationPrincipal(
			Class<Transportation> delegateClass,
			Class<TransportationVehicle> annotationClass,
			DelegationAgent<Transportation, TransportationVehicle> delegationAgent) {
		
		super(delegateClass, annotationClass, delegationAgent);
	}

	@Override
	protected Transportation apply(final Object receiver, final Method target,
			final TransportationVehicle annotation) {
		
		return new Transportation() {
			
			public Long calculateTime(Double distance) {
				
				try {
					
					// invoke time calculation method
					return (Long) target.invoke(receiver, distance);
					
				} catch (Exception e) {
					
				throw new RuntimeException(annotation.vehicle() +  " transportation delegate has failed", e);
				
				} 
			}
		};
	}

}
```

### Traveling Task Revisted

Now, let us implement the delegation agents and registerDelegate method inside the TravelingTask. The follwoing code snippet shows selected parts of the TravelingTask

```java
class TravellingTask {
	
	// Create inner DelegationAgent class to register a transportation delegate
	private DelegationAgent<Transportation, TransportationVehicle> transportationAgent = new DelegationAgent<Transportation, TransportationVehicle>() {
		
		// This method should be called by TransporationPrincipal class while
		// applying the prinicipal to the traveling task
		public void register(TransportationVehicle annotation,
				Transportation delegate) {
			
			addVehicle(annotation.vehicle(), delegate);
		}
	};
	
	// Create inner DelegationAgent class to register a locator delegate
	private DelegationAgent<Locator, MapLocator> locatorAgent = new DelegationAgent<Locator, MapLocator>() {
		
		// This method should be called by LocatorPrincipal class while
		// applying the prinicipal to the traveling task
		public void register(MapLocator annotation, Locator delegate) {
			
			addLocator(annotation.map(), delegate);
		}
	};
}
```
