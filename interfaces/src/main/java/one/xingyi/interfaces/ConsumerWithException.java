package one.xingyi.interfaces;

public interface ConsumerWithException <T>{
    void accept(T t) throws Exception;
}
