package one.xingyi.optics.annotations.serialise;

import lombok.*;
import one.xingyi.optics.annotations.processors.PackageAndClass;
import one.xingyi.optics.annotations.interfaces.IFunctionWithException;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IAnnotationProcessorStore<From, To> extends IAnnotationProcessorLoader<From, To>, IAnnotationProcessorStorer<From, To> {
    static IFunctionWithException<PackageAndClass, FileObject> classNameToFileObjectForExtension(Filer filer, String extension) {
        return pckAndClass -> filer.createResource(StandardLocation.SOURCE_OUTPUT, pckAndClass.getPackageName() + ".storeFor" + extension, pckAndClass.getClassName() + "." + extension);
    }

    static <From, To> IAnnotationProcessorStore<From, To> defaultStore(Filer filer,
                                                                       Function<From, PackageAndClass> fromToClassName,
                                                                       String extension,
                                                                       BiFunction<From, String, To> parser,
                                                                       Function<To, String> printer,
                                                                       Consumer<String> logger,
                                                                       Predicate<From> debug) {
        var cache = new ConcurrentHashMap<PackageAndClass, To>();
        var classNameToFileObject = classNameToFileObjectForExtension(filer, extension);
        var storer = new CachedAnnotationProcessorStorer<From, To>(
                new DebuggingAnnotationProcessorStorer<From, To>(new AnnotationProcessorStorer<From, To>(fromToClassName, classNameToFileObject, printer),
                        debug, logger), cache, fromToClassName);
        var loader = new DebuggingAnnotationProcessorLoader<From, To>(
                new CachedAnnotationProcessorLoader<From, To>(
                        new AnnotationProcessorLoader<From, To>(fromToClassName, classNameToFileObject, parser),
                        cache, fromToClassName),
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


