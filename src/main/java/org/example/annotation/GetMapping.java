package org.example.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@RequestMapping(requestMethod=RequestMethod.GET)
public @interface GetMapping {

    @AliasFor(annotation=RequestMapping.class)
    String value() default "";
}
