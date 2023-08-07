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
public class Child {
    private final String name;
    private final int age;
    
    Child(String name, int age) {
    	this.name = name;
    	this.age = age;
    }

}

