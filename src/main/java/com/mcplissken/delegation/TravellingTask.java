/**
 * 
 */
package com.mcplissken.delegation;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 29, 2015
 */
public class TravellingTask {
	
	private DelegationAgent<Transportation, TransportationVehicle> transportationAgent = new DelegationAgent<Transportation, TransportationVehicle>() {
		
		public void register(TransportationVehicle annotation,
				Transportation delegate) {
			
			addVehicle(annotation.vehicle(), delegate);
		}
	};
	
	private DelegationAgent<Locator, MapLocator> locatorAgent = new DelegationAgent<Locator, MapLocator>() {
		
		public void register(MapLocator annotation, Locator delegate) {
			
			addLocator(annotation.map(), delegate);
		}
	};
	
	public enum Vehicle{
		BICYCLE(), CAR(), TRAIN();
	}
	
	public enum MAP{
		PAPER(), COMPUTERIZED();
	}
	
	private HashMap<Vehicle, Transportation> vehicles;
	private HashMap<MAP, Locator> locators;
	
	private LocatorPrincipal locatorPrincipal;
	private TransportationPrincipal transportationPrincipal;
	
	public TravellingTask() {
		
		locators = new HashMap<TravellingTask.MAP, Locator>();
		
		vehicles = new HashMap<TravellingTask.Vehicle, Transportation>();
		
		locatorPrincipal = new LocatorPrincipal(Locator.class, MapLocator.class, locatorAgent);
		
		transportationPrincipal = new TransportationPrincipal(Transportation.class, TransportationVehicle.class, transportationAgent);
	}
	
	public void registerDelegate(Object receiver){
		
		for(Method method : receiver.getClass().getMethods()){
			
			locatorPrincipal.apply(receiver, method);
			
			transportationPrincipal.apply(receiver, method);
			
		}
		
	}
	
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
	
	public void addVehicle(Vehicle vehicle, Transportation transport){
		vehicles.put(vehicle, transport);
	}
	
	public void addLocator(MAP map, Locator locator){
		locators.put(map, locator);
	}
	
}
