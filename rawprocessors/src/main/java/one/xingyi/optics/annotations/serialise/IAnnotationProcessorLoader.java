package one.xingyi.optics.annotations.serialise;

import lombok.*;
import one.xingyi.optics.annotations.processors.PackageAndClass;
import one.xingyi.optics.annotations.interfaces.IFunctionWithException;

import javax.tools.FileObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    private final IFunctionWithException<PackageAndClass, FileObject> classNameToFileObject;
    private final BiFunction<From, String, T> parser;


    @Override
    public T load(From from) throws IOException {
        var fileObject = classNameToFileObject.apply(fromToClassName.apply(from));
        var file = new File(fileObject.toUri());

        try (var reader = new FileReader(file)) {
            var bufferedReader = new BufferedReader(reader);
            return parser.apply(from, bufferedReader.lines().collect(Collectors.joining("\n")));
        }
    }
}

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
class CachedAnnotationProcessorLoader<From, T> implements IAnnotationProcessorLoader<From, T> {

    private final IAnnotationProcessorLoader<From, T> loader;
    private final ConcurrentHashMap<PackageAndClass, T> cache;
    private final Function<From, PackageAndClass> fromToClassName;

    @Override
    public T load(From from) throws IOException {
        var className = fromToClassName.apply(from);
        if (cache.containsKey(className)) return cache.get(className);
        synchronized (cache) {
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
            T result = loader.load(from);
            if (debugPredicate.test(from)) logger.accept("Loaded " + result + " from " + from);
            return result;
        } catch (IOException | RuntimeException e) {
            logger.accept("Error loading " + from + " " + e.getMessage());
            throw e;
        }
    }
}