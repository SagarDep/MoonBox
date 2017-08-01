package net.qiujuer.jumper.compiler;

import net.qiujuer.jumper.annotation.JumpType;
import net.qiujuer.jumper.annotation.JumpUiThread;
import net.qiujuer.jumper.annotation.JumpWorkerThread;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

//https://github.com/google/auto
@SupportedAnnotationTypes({"net.qiujuer.jumper.annotation.JumpUiThread", "net.qiujuer.jumper.annotation.JumpWorkerThread"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class JumperAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> uiElements = roundEnvironment.getElementsAnnotatedWith(JumpUiThread.class);

        Set<? extends Element> workerElements = roundEnvironment.getElementsAnnotatedWith(JumpWorkerThread.class);

        if (uiElements != null && !uiElements.isEmpty()) {
            processUiAnnotation(uiElements);
        }

        if (workerElements != null && !workerElements.isEmpty()) {
            processWorkerAnnotation(workerElements);
        }

        // 创建一个文件输出
        // processingEnv.getFiler().createSourceFile("net.qiujuer.jumper.apt.GeneratedClass");
        /*
        //对于Element直接强转
        */


        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // a.b.c$d
        // a.b.c.d
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add(JumpUiThread.class.getCanonicalName());
        hashSet.add(JumpWorkerThread.class.getCanonicalName());
        return hashSet;
    }

    private void processUiAnnotation(Set<? extends Element> uiElements) {
        for (Element element : uiElements) {
            // ExecutableElement、PackageElement、TypeElement、TypeParameterElement、VariableElement
            // 方法Element
            ExecutableElement executableElement = (ExecutableElement) element;
            JumpUiThread annotation = element.getAnnotation(JumpUiThread.class);
            // 如果是异步运行，检查返回类型
            if (annotation.value() == JumpType.ASYNC) {
                TypeMirror returnType = executableElement.getReturnType();
                if (returnType.getKind() != TypeKind.VOID) {
                    // 如果不为void返回类型
                    printWaring(executableElement, "JumpUiThread ASYNC 不支持返回数据，建议设置为void！");
                }
            }
        }
    }

    private void processWorkerAnnotation(Set<? extends Element> workerElements) {
        for (Element element : workerElements) {
            ExecutableElement executableElement = (ExecutableElement) element;
            TypeMirror returnType = executableElement.getReturnType();
            if (returnType.getKind() != TypeKind.VOID) {
                // 如果不为void返回类型
                printError(executableElement, "JumpWorkerThread 不支持返回数据，必须为void！");
            }

            List<? extends VariableElement> parameters = executableElement.getParameters();
            if (parameters != null && parameters.size() > 0) {
                // 如果有参数输入
                printError(executableElement, "JumpWorkerThread 暂不支持输入参数！");
            }
        }
    }


    @SuppressWarnings("SameParameterValue")
    private void printWaring(ExecutableElement element, String message) {
        String className = element.getEnclosingElement().toString();
        String methodName = element.toString();
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, className + "#" + methodName + " " + message);
    }

    private void printError(ExecutableElement element, String message) {
        String className = element.getEnclosingElement().toString();
        String methodName = element.toString();
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, className + "#" + methodName + " " + message, element);
    }

}
