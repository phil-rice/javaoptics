package one.xingyi.optics.annotations.test;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.optics.annotations.Optics;

@Optics(debug = true)
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Broken {
    final String name;
    final String withoutConstructor;
    Broken(String name, String withoutConstructor) {
    	this.name = name;
    	this.withoutConstructor = withoutConstructor;
    }
}
