package one.xingyi.profile.pathmap;

import lombok.AllArgsConstructor;
import one.xingyi.helpers.MapHelpers;
import one.xingyi.interfaces.Function3;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public interface IPathMap<V> {
    static <V> IPathMap<V> make(Supplier<V> defaultValue) {return new PathMap<>("", defaultValue, defaultValue.get());}
    String path();
    V get();
    void put(V value);
    IPathMap<V> child(String pathOffset);
    Set<String> childPaths();
    <Acc> Acc fold(Acc zero, Function3<Acc, String, V, Acc> foldFn);

}
@AllArgsConstructor
class PathMap<V> implements IPathMap<V> {
    final String path;
    final Supplier<V> defaultValue;
    V value;
    final Map<String, IPathMap<V>> children = new ConcurrentHashMap<>();
    @Override
    public String path() {return path;}
    @Override
    public void put(V value) {this.value = value;}
    @Override
    public V get() {return value;}
    @Override
    public IPathMap<V> child(String pathOffset) {return MapHelpers.getOrAdd(children, pathOffset, () -> new PathMap<>(path + (path.isEmpty() ? "" : ".") + pathOffset, defaultValue, defaultValue.get()));}
    @Override
    public Set<String> childPaths() {return children.keySet();}
    @Override
    public <Acc> Acc fold(Acc zero, Function3<Acc, String, V, Acc> foldFn) {
        Acc acc = foldFn.apply(zero, path, value);
        SortedSet<String> keys = new TreeSet<>(children.keySet());
        for (String key : keys)
            acc = children.get(key).fold(acc, foldFn);
        return acc;
    }


}



