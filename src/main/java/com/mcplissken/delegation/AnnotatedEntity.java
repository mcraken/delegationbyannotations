/**
 * 
 */
package com.mcplissken.delegation;


import com.mcplissken.delegation.TravellingTask.MAP;
import com.mcplissken.delegation.TravellingTask.Vehicle;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 30, 2015
 */
public class AnnotatedEntity {
	
	 @TransportationVehicle(vehicle=Vehicle.CAR)
	    public Long someMethod(Double distance){
		 	System.out.println("Calculating Time");
	        return 0L;
	    }

	    @MapLocator(map=MAP.COMPUTERIZED)
	    public Double someOtherMethod(Point a, Point b){
	        System.out.println("Calculating Distance");
	        return 0D;
	    }
}
