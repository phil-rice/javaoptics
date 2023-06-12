package one.xingyi.tuples;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
public class Tuple3<T1, T2, T3> {
    public final T1 t1;
    public final T2 t2;
    public final T3 t3;


    public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
        return new Tuple3<>(t1, t2, t3);
    }
}
