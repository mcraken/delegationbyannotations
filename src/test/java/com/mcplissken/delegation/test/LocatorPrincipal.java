/**
 * 
 */
package com.mcplissken.delegation.test;

import java.lang.reflect.Method;

import com.mcplissken.delegation.DelegationAgent;
import com.mcplissken.delegation.DelegationPrincipal;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 30, 2015
 */
public class LocatorPrincipal extends DelegationPrincipal<Locator, MapLocator> {

	public LocatorPrincipal(Class<Locator> delegateClass,
			Class<MapLocator> annotationClass,
			DelegationAgent<Locator, MapLocator> delegationAgent) {
		
		super(delegateClass, annotationClass, delegationAgent);
	}

	/* (non-Javadoc)
	 * @see com.mcplissken.delegation.DelegationPrincipal#apply(java.lang.Object, java.lang.reflect.Method, java.lang.annotation.Annotation)
	 */
	@Override
	protected Locator apply(final Object receiver, final Method target,
			final MapLocator annotation) {
		
		return new Locator() {
			
			public Double calculateDistance(Point a, Point b) {
				
				try {
					
					return (Double) target.invoke(receiver, a, b);
					
				} catch (Exception e) {
					
					throw new RuntimeException(annotation.map() + " locator failed", e);
				}
			}
		};
	}
}
