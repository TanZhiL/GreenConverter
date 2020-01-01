package com.thomas.greenconverter.compiler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.thomas.greenconverter.Converter;
import com.thomas.greenconverter.ListConverter;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ConverterProcessor extends AbstractProcessor {
    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(Converter.class.getCanonicalName());
        annotationTypes.add(ListConverter.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
/*
    public static class FreightDetailConverter implements PropertyConverter<FreightDetail, String> {
        @Override
        public FreightDetail convertToEntityProperty(String databaseValue) {
            return new Gson().fromJson(databaseValue, FreightDetail.class);
        }

        @Override
        public String convertToDatabaseValue(FreightDetail entityProperty) {
            return new Gson().toJson(entityProperty);
        }
    }
 */
        Set<? extends Element> okAspectjElements = roundEnv.getElementsAnnotatedWith(Converter.class);
        for (Element element : okAspectjElements) {
            TypeElement classElement = (TypeElement) element;
            PackageElement packageElement = (PackageElement) element.getEnclosingElement();

            MethodSpec convertToEntityProperty = MethodSpec.methodBuilder("convertToEntityProperty")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(ClassName.get(String.class), "databaseValue")
                    .returns(TypeName.get(classElement.asType()))
                    .addStatement("return new $T().fromJson(databaseValue,"+String.format("%s.class",classElement.getSimpleName())+")", Gson.class)
                    .build();


            MethodSpec convertToDatabaseValue = MethodSpec.methodBuilder("convertToDatabaseValue")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(TypeName.get(classElement.asType()), "entityProperty")
                    .returns(ClassName.get(String.class))
                    .addStatement("  return new $T().toJson(entityProperty)", Gson.class)
                    .build();


            ParameterizedTypeName typeName = ParameterizedTypeName.get(ClassName.get("org.greenrobot.greendao.converter", "PropertyConverter"),TypeName.get(classElement.asType()),ClassName.get(String.class));
            TypeSpec OkAspectj = TypeSpec.classBuilder("Converter")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addSuperinterface(typeName)
                    .addMethod(convertToEntityProperty)
                    .addMethod(convertToDatabaseValue)
                    .build();

            TypeSpec wrapper = TypeSpec.classBuilder(classElement.getSimpleName().toString() + "_Converter")
                    .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                    .addType(OkAspectj)
                    .build();
            JavaFile javaFile = JavaFile.builder(packageElement.getQualifiedName().toString(), wrapper).build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        okAspectjElements = roundEnv.getElementsAnnotatedWith(ListConverter.class);
/*
    public static class BoxDetailConverter implements PropertyConverter<List<BoxDetail>, String> {
        @Override
        public List<BoxDetail> convertToEntityProperty(String databaseValue) {
            if (databaseValue.length()==0) {
                return Collections.emptyList();
            }
            // 先得获得这个，然后再typeToken.getType()，否则会异常
            TypeToken<List<BoxDetail>> typeToken = new TypeToken<List<BoxDetail>>(){};
            return new Gson().fromJson(databaseValue, typeToken.getType());
        }

        @Override
        public String convertToDatabaseValue(List<BoxDetail> arrays) {
            if (arrays == null||arrays.size()==0) {
                return "";
            } else {
                String sb = new Gson().toJson(arrays);
                return sb;
            }
        }
    }
 */
        for (Element element : okAspectjElements) {
            TypeElement classElement = (TypeElement) element;
            PackageElement packageElement = (PackageElement) element.getEnclosingElement();

            ParameterizedTypeName list = ParameterizedTypeName.get(ClassName.get("java.util", "List"), TypeName.get(classElement.asType()));
            MethodSpec convertToEntityProperty = MethodSpec.methodBuilder("convertToEntityProperty")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(ClassName.get(String.class), "databaseValue")
                    .returns(list)
                    .addCode("  if (databaseValue.length()==0) {\n" +
                            "                return $T.emptyList();\n" +
                            "            }", Collections.class)
                    .addCode("$T<$T> typeToken = new $T<$T>(){};", TypeToken.class,list,TypeToken.class,list)
                    .addStatement("return new $T().fromJson(databaseValue,typeToken.getType())", Gson.class)
                    .build();


            MethodSpec convertToDatabaseValue = MethodSpec.methodBuilder("convertToDatabaseValue")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(list, "arrays")
                    .returns(ClassName.get(String.class))
                    .addCode(" if (arrays == null||arrays.size()==0) {\n" +
                            "                return \"\";\n" +
                            "            } else {\n" +
                            "                String sb = new $T().toJson(arrays);\n" +
                            "                return sb;\n" +
                            "            }", Gson.class)
                    .build();


            ParameterizedTypeName typeName = ParameterizedTypeName.get(ClassName.get("org.greenrobot.greendao.converter", "PropertyConverter"),list,ClassName.get(String.class));
            TypeSpec OkAspectj = TypeSpec.classBuilder("ListConverter")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addSuperinterface(typeName)
                    .addMethod(convertToEntityProperty)
                    .addMethod(convertToDatabaseValue)
                    .build();
            TypeSpec wrapper = TypeSpec.classBuilder(classElement.getSimpleName().toString() + "_ListConverter")
                    .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                    .addType(OkAspectj)
                    .build();

            JavaFile javaFile = JavaFile.builder(packageElement.getQualifiedName().toString(), wrapper).build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private String getPackageName(TypeElement type) {
        return mElementUtils.getPackageOf(type).getQualifiedName().toString();
    }
}