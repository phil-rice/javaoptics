package one.xingyi.optics.annotations.serialise;

import lombok.*;
import one.xingyi.optics.annotations.processors.PackageAndClass;
import one.xingyi.optics.annotations.utils.IFunctionWithIoException;

import javax.tools.FileObject;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class CachedAnnotationProcessorStorer<From, To> implements IAnnotationProcessorStorer<From, To> {
    private final IAnnotationProcessorStorer<From, To> storer;
    private final ConcurrentHashMap<PackageAndClass, To> cache;
    private final Function<From, PackageAndClass> fromToClassName;

    @Override
    public void store(From from, To to) throws IOException {
        var className = fromToClassName.apply(from);
        cache.put(className, to);
        storer.store(from, to);

    }
}

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class DebuggingAnnotationProcessorStorer<From, To> implements IAnnotationProcessorStorer<From, To> {
    private final IAnnotationProcessorStorer<From, To> storer;
    private final Predicate<From> debugPredicate;
    private final Consumer<String> logger;

    @Override
    public void store(From from, To to) throws IOException {
        try {
            if (debugPredicate.test(from)) logger.accept("Stored " + from + " giving " + to);
            storer.store(from, to);
            if (debugPredicate.test(from)) logger.accept("Stored " + from);
        } catch (IOException | RuntimeException e) {
            logger.accept("Error storing " + from + ", " + to + e.getMessage());
            throw e;
        }
    }
}