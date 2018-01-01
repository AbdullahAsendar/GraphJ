/**
 * 
 */
package com.asendar.model.ui;


import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * custom inject method that looks the exact same as {@link javax.inject.Inject}
 * to be used with {@link DependencyInjector} and not to be conflicted with spring
 * {@link Autowired}
 * 
 * 
 * @author Asendar
 *
 */
@Target({ METHOD, CONSTRUCTOR, FIELD })
@Retention(RUNTIME)
@Documented
public @interface Inject {}