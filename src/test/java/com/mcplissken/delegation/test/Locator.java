/**
 * 
 */
package com.mcplissken.delegation.test;

import com.mcplissken.delegation.Delegate;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 29, 2015
 */

public interface Locator {
	
	@Delegate
	public Double calculateDistance(Point a, Point b);

}
