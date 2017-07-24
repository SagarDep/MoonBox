package net.qiujuer.jumper.compiler;

import net.qiujuer.jumper.annotation.JumpUiThread;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;


//https://github.com/google/auto

@SupportedAnnotationTypes("net.qiujuer.jumper.annotation.JumpUiThread")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class JumperAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        int i = 1;
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "=========== " + i++);
        for (TypeElement typeElement : set) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, typeElement.toString());
        }


        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "=========== " + i++);
        for (Element element : roundEnvironment.getRootElements()) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, element.toString());
        }
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "=========== " + i);

        StringBuilder builder = new StringBuilder()
                .append("package net.qiujuer.jumper.apt;\n\n")
                .append("public class GeneratedClass {\n\n") // open class
                .append("\tpublic String getMessage() {\n") // open method
                .append("\t\treturn \"");

        // for each javax.lang.model.element.Element annotated with the CustomAnnotation
        for (Element element : roundEnvironment.getElementsAnnotatedWith(JumpUiThread.class)) {
            String objectType = element.getSimpleName().toString();

            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, element.getKind().toString());

            // this is appending to the return statement
            builder.append(objectType).append(" says hello!\\n");


            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "错误来了1111");
        }

        builder.append("\";\n") // end return
                .append("\t}\n") // close method
                .append("}\n"); // close class


        try { // write the file
            JavaFileObject source = processingEnv.getFiler().createSourceFile("net.qiujuer.jumper.apt.GeneratedClass");


            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(JumpUiThread.class.getCanonicalName());
    }
}
