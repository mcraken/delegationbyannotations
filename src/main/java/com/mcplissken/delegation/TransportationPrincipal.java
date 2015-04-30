/**
 * 
 */
package com.mcplissken.delegation;

import java.lang.reflect.Method;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 30, 2015
 */
public class TransportationPrincipal extends DelegationPrincipal<Transportation, TransportationVehicle> {

	public TransportationPrincipal(
			Class<Transportation> delegateClass,
			Class<TransportationVehicle> annotationClass,
			DelegationAgent<Transportation, TransportationVehicle> delegationAgent) {
		
		super(delegateClass, annotationClass, delegationAgent);
	}

	/* (non-Javadoc)
	 * @see com.mcplissken.delegation.DelegationPrincipal#apply(java.lang.Object, java.lang.reflect.Method, java.lang.annotation.Annotation)
	 */
	@Override
	protected Transportation apply(final Object receiver, final Method target,
			final TransportationVehicle annotation) {
		
		return new Transportation() {
			
			public Long calculateTime(Double distance) {
				
				try {
					
					return (Long) target.invoke(receiver, distance);
					
				} catch (Exception e) {
					
					throw new RuntimeException(annotation.vehicle() +  " transportation delegate has failed", e);
				} 
			}
		};
	}

}
