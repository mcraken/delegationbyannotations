/**
 * 
 */
package com.mcplissken.delegation.test;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 29, 2015
 */
public abstract class TemplateTravelingTask {


	public Long travel(Point a, Point b){

		Double distance = calculateDistance(a, b);

		return calculateTime(distance);

	}

	protected abstract Long calculateTime(Double distance);

	protected abstract  Double calculateDistance(Point a, Point b);
}
