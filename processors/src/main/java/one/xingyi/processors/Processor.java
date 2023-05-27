package one.xingyi.processors;

import one.xingyi.optics.annotations.Optics;
import org.stringtemplate.v4.STGroupFile;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SupportedAnnotationTypes("one.xingyi.optics.annotations.Optics")
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
        log("Processing Optics annotations" + annotations.size());
        for (TypeElement annotation : annotations) {
            log("Processing Optics annotation " + annotation);
            if (annotation.getQualifiedName().toString().equals(Optics.class.getName())) {
                processOpticsAnnotation(roundEnv, annotation);
            }
            return true;
        }
        return false;
    }

    public String opticsClassName(RecordOpticsWithTraversals details) {
        return details.getPackageName() + "." + details.getClassName() + "Optics";
    }

    Stream<RecordOpticsDetails> fromAnnotation(RoundEnvironment roundEnv, TypeElement annotation) {
        annotation.getTypeParameters().forEach(t -> log("Type parameter " + t));
        return roundEnv.getElementsAnnotatedWith(annotation).stream().map(RecordOpticsDetails::fromElement);
    }

    private void processOpticsAnnotation(RoundEnvironment roundEnv, TypeElement annotation) {
        List<RecordOpticsDetails> recordOpticsDetails = fromAnnotation(roundEnv, annotation).toList();

        recordOpticsDetails.stream()
                .map(rod -> RecordOpticsWithTraversals.from(recordOpticsDetails, rod))
                .map(this::makeRecordOpticsFileDefn).forEach(this::makeSourceFile);
    }

    private FileDefn makeRecordOpticsFileDefn(RecordOpticsWithTraversals d) {
        log("Details " + d);
        String rendered = render(d, "recordOptic");
        var className = opticsClassName(d);
        return new FileDefn(className, rendered);
    }

    private void makeSourceFile(FileDefn fileDefn) {
        try {
            JavaFileObject builderFile = filer.createSourceFile(fileDefn.className());
            try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                out.println(fileDefn.content());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String render(RecordOpticsWithTraversals details, String templateName) {
        var record = stringTemplate.getInstanceOf(templateName);
        record.add("details", details);
        return record.render();
    }

}
