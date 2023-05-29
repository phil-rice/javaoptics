package one.xingyi.optics.annotations.test.seperationOfStructure;

import one.xingyi.optics.annotations.Optics;
import one.xingyi.optics.annotations.PhilsAnnotation;

import java.util.List;

@Optics
@PhilsAnnotation("The Train")
public record Train(List<Wagon> wagons) {
    void x(){
        var x = Train.class.getAnnotation(PhilsAnnotation.class);
        System.out.println(x.value());
    }
}