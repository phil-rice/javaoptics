package one.xingyi.tuples;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor
public class Tuple4<T1, T2, T3, T4> {

    public final T1 t1;
    public final T2 t2;
    public final T3 t3;
    public final T4 t4;
    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 t1, T2 t2, T3 t3, T4 t4) {
        return new Tuple4<>(t1, t2, t3, t4);
    }
}
