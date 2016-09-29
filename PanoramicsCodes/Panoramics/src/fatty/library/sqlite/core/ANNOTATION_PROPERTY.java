package fatty.library.sqlite.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Property
 * 
 * @author Fatty
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ANNOTATION_PROPERTY {
	public String column() default "";
	public String defaultValue() default "";
}
