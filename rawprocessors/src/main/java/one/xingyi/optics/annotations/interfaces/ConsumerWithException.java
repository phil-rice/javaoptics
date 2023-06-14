package one.xingyi.optics.annotations.interfaces;

public interface ConsumerWithException <T>{
    void accept(T t) throws Exception;
}
