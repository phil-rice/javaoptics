package one.xingyi.annotations.optics.serialise;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.annotations.processors.PackageAndClass;
import one.xingyi.annotations.utils.IFunctionWithIoException;

import javax.tools.FileObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface IAnnotationProcessorLoader<From, To> {

    To load(From from) throws IOException;
}

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class AnnotationProcessorLoader<From, T> implements IAnnotationProcessorLoader<From, T> {

    private final Function<From, PackageAndClass> fromToClassName;
    private final IFunctionWithIoException<PackageAndClass, FileObject> classNameToFileObject;
    private final BiFunction<From, String, T> parser;


    @Override
    public T load(From from) throws IOException {
        var fileObject = classNameToFileObject.apply(fromToClassName.apply(from));
        try (var reader = new BufferedReader(fileObject.openReader(false))) {
            return parser.apply(from, reader.lines().collect(Collectors.joining("\n")));
        }
    }
}

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class CachedAnnotationProcessorLoader<From, T> implements IAnnotationProcessorLoader<From, T> {

    private final IAnnotationProcessorLoader<From, T> loader;
    private final Function<From, PackageAndClass> fromToClassName;
    private final ConcurrentHashMap<PackageAndClass, T> cache = new ConcurrentHashMap<>();
    private final Object lock = new Object();

    @Override
    public T load(From from) throws IOException {
        var className = fromToClassName.apply(from);
        if (cache.containsKey(className)) return cache.get(className);
        synchronized (lock) {
            //double check locking pattern
            if (cache.containsKey(className)) return cache.get(className);
            var t = loader.load(from);
            cache.put(className, t);
            return t;
        }
    }
}

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class DebuggingAnnotationProcessorLoader<From, T> implements IAnnotationProcessorLoader<From, T> {
    private final IAnnotationProcessorLoader<From, T> loader;
    private final Predicate<From> debugPredicate;
    private final Consumer<String> logger;


    @Override
    public T load(From from) throws IOException {
        try {
            if (debugPredicate.test(from)) logger.accept("Loading " + from);
            return loader.load(from);
        } catch (IOException e) {
            logger.accept("Error loading " + from + " " + e.getMessage());
            throw e;
        }
    }
}