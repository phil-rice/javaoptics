package one.xingyi.annotations.optics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME) // Need to keep them around for testing
public @interface Optics {
    String[] traversals() default {};
    boolean addListTraversal() default false;

    boolean debug() default false;
}
