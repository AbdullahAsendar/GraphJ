/**
 * 
 */
package com.asendar.model.ui;


import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * mehtods annotated with this will be called on {@link AppView} reset controller
 * 
 * 
 * @author Asendar
 *
 */
@Target({ METHOD })
@Retention(RUNTIME)
@Documented
public @interface OnReset {}