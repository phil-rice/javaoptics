package one.xingyi.annotations.optics.serialise;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.annotations.processors.PackageAndClass;
import one.xingyi.annotations.utils.IFunctionWithIoException;

import javax.tools.FileObject;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * For traversals we need to be able to walk to the next class and find its traversals
 */
public interface IAnnotationProcessorStorer<From, To> {
    void store(From from, To to) throws IOException;

}

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class AnnotationProcessorStorer<From, To> implements IAnnotationProcessorStorer<From, To> {

    private final Function<From, PackageAndClass> fromToClassName;
    private final IFunctionWithIoException<PackageAndClass, FileObject> classNameToFileObject;
    private final Function<To, String> printer;

    @Override
    public void store(From from, To to) throws IOException {
        var fileObject = classNameToFileObject.apply(fromToClassName.apply(from));
        try (var writer = fileObject.openWriter()) {
            writer.write(printer.apply(to));
        }
    }
}
