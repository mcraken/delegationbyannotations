/**
 * 
 */
package com.mcplissken.delegation.test;

import org.junit.Test;

import com.mcplissken.delegation.test.TravellingTask.MAP;
import com.mcplissken.delegation.test.TravellingTask.Vehicle;



/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 30, 2015
 */
public class TestTravellingTask {
	
	@Test
	public void testTravel(){
		
		AnnotatedEntity someAnnotatedEntity = new AnnotatedEntity();
		TravellingTask travelingTask = new TravellingTask();
		travelingTask.registerDelegate(someAnnotatedEntity);
		
		travelingTask.travel(null, null, Vehicle.CAR, MAP.COMPUTERIZED);
	}
}
