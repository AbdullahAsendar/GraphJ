/**
 * 
 */
package com.asendar.model.ui;



import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Bean;

/**
 * 
 * Tells {@link FXMLView} to create {@link Bean} 
 * using the name instead of the class type, why? 
 * because using inheritance spring can not deal with classes
 * 
 * 
 * @author Asendar
 *
 */
@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Documented
public @interface DefineByName {}