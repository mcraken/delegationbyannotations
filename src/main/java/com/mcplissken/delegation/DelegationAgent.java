/**
 * 
 */
package com.mcplissken.delegation;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 29, 2015
 */
public interface DelegationAgent<T, A> {
	
	public void register(A annotation, T delegate);
}
