/**
 * 
 */
package com.mcplissken.delegation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 29, 2015
 */
public abstract class DelegationPrincipal<D, A extends Annotation> {

	private Class<A> annotationClass;
	private DelegationAgent<D, A> delegationAgent;

	private Class<?> returnType;
	
	private Class<?>[] parameters;
	
	public DelegationPrincipal(
			Class<D> delegateClass,
			Class<A> annotationClass,
			DelegationAgent<D, A> delegationAgent) {
		
		this.annotationClass = annotationClass;
		this.delegationAgent = delegationAgent;
		
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

	public void apply(Object receiver, Method target){

		A annotation = getTargetAnnotation(target);

		if(annotation != null ){

			isMethodValid(target, annotation);

			D delegate = apply(receiver, target, annotation);
			
			delegationAgent.register(annotation, delegate);
		}

	}

	private A getTargetAnnotation(Method target) {
		A annotation = target.getAnnotation(annotationClass);
		return annotation;
	}

	private void isMethodValid(Method target, A annotation){
		
		matchReturnType(target);
		
		matchParametersLength(target);
		
		matchParametersOrder(target);
	}

	private void matchParametersOrder(Method target) {
		
		for(int i = 0; i < parameters.length; i++){
			
			if(parameters[i] != target.getParameterTypes()[i]){
				throw new RuntimeException("Invalid order of parameters");
			}
				
		}
	}

	private void matchParametersLength(Method target) {
		
		if(target.getParameterTypes().length != parameters.length){

			throw new RuntimeException("Invalid number of parameters");
		}
	}

	private void matchReturnType(Method target) {
		Class<?> targetReturnType = target.getReturnType();
		
		if(targetReturnType != returnType){
			throw new RuntimeException("Invalid return type");
		}
	}

	protected abstract D apply(Object receiver, Method target, A annotation);
}
