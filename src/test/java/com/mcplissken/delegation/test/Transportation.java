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
public interface Transportation {
	
	@Delegate
	public Long calculateTime(Double distance);

}
