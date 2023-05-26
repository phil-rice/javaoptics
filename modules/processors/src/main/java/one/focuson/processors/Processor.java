package one.focuson.processors;

import lombok.SneakyThrows;
import org.stringtemplate.v4.STGroupFile;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes("one.focuson.optics.annotations.Optics")
@SupportedSourceVersion(SourceVersion.RELEASE_16)
public class Processor extends AbstractProcessor {

    private Messager messager;
    private ProcessingEnvironment pEnv;
    private Filer filer;


    protected void log(String message) {
        messager.printMessage(javax.tools.Diagnostic.Kind.NOTE, message);
    }

    private STGroupFile stringTemplate;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
//        throw new RuntimeException("Init  Optics annotations");
        messager = processingEnv.getMessager();
        this.pEnv = processingEnv;
        this.filer = pEnv.getFiler();

        log("Init  Optics annotations");
        stringTemplate = new STGroupFile("record.stg");

    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            log("Processing Optics annotations" + annotations.size());
            for (TypeElement annotation : annotations) {
                log("Processing Optics annotation " + annotation);
                if (annotation.getQualifiedName().toString().equals("one.focuson.optics.annotations.Optics")) {
                    processOpticsAnnotation(roundEnv, annotation);
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String opticsClassName(RecordOpticsDetails details) {
        return details.getPackageName() + "." + details.getClassName() + "Optics";
    }


    private void processOpticsAnnotation(RoundEnvironment roundEnv, TypeElement annotation) throws IOException {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
        log("Found Optics annotations " + elements.size());
        for (var element : elements) {
            var recordDetails = RecordOpticsDetails.fromElement(element);
            log("Found Optics annotation " + recordDetails);
            var record = stringTemplate.getInstanceOf("recordOptic");
            log("Record" + record);
            record.add("details", recordDetails);
            var lensClassName = opticsClassName(recordDetails);
            log("lensClassName [" + lensClassName + "]");
            String rendered = record.render();
            JavaFileObject builderFile = filer.createSourceFile(lensClassName);
            try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                out.println(rendered);
            }
            log("With string template" + rendered);
        }
    }


}
