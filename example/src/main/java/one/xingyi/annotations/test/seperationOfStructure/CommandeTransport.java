package one.xingyi.annotations.test.seperationOfStructure;

import one.xingyi.optics.annotations.Optics;

import java.util.List;

@Optics
public record CommandeTransport(List<TronconTransport> tronconTransportList) {

}
