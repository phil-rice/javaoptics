package one.focuson.optics.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME) // Need to keep them around for testing
public @interface Traversal {
}
