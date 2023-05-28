package one.xingyi.annotations.optics.serialise;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.annotations.processors.PackageAndClass;
import one.xingyi.annotations.processors.RecordedTraversals;
import one.xingyi.annotations.processors.WithDebug;
import one.xingyi.annotations.utils.IFunctionWithIoException;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IAnnotationProcessorStore<From, To> extends IAnnotationProcessorLoader<From, To>, IAnnotationProcessorStorer<From, To> {
    static IFunctionWithIoException<PackageAndClass, FileObject> classNameToFileObjectForExtension(Filer filer, String extension) {
        return pckAndClass -> filer.createResource(StandardLocation.CLASS_OUTPUT, pckAndClass.getPackageName(), pckAndClass.getClassName() + "." + extension);
    }

    static <From, To> IAnnotationProcessorStore<From, To> defaultStore(Filer filer,
                                                                       Function<From, PackageAndClass> fromToClassName,
                                                                       String extension,
                                                                       BiFunction<From, String, To> parser,
                                                                       Function<To, String> printer,
                                                                       Consumer<String> logger,
                                                                       Predicate<From> debug) {
        var classNameToFileObject = classNameToFileObjectForExtension(filer, extension);
        var storer = new AnnotationProcessorStorer<From, To>(fromToClassName, classNameToFileObject, printer);
        var loader = new DebuggingAnnotationProcessorLoader<From, To>(
                new CachedAnnotationProcessorLoader<From, To>(
                        new AnnotationProcessorLoader<From, To>(fromToClassName, classNameToFileObject, parser),
                        fromToClassName),
                debug, logger);
        return new DefaultAnnotationProcessorStore<>(storer, loader);
    }
}

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class DefaultAnnotationProcessorStore<From, To> implements IAnnotationProcessorStore<From, To> {
    private final IAnnotationProcessorStorer<From, To> storer;
    private final IAnnotationProcessorLoader<From, To> loader;

    @Override
    public To load(From from) throws IOException {
        return loader.load(from);
    }

    @Override
    public void store(From from, To to) throws IOException {
        storer.store(from, to);
    }
}


