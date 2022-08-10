package org.khasanof.core;

import org.khasanof.config.PropertyConfig;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

public class SchemaCore {

    private String schemaValue;
    private String nameValue;

    public SchemaCore() {
    }

    private SchemaCore(String schemaValue, String nameValue) {
        this.schemaValue = schemaValue;
        this.nameValue = nameValue;
    }

    public void annotationValueInitialize(Class<?> aClass) {
        Annotation[] annotations = aClass.getAnnotations();
        for (Annotation annotation1 : annotations) {
            if (annotation1.annotationType().getSimpleName().equals(Table.class.getSimpleName())) {
                List<String> tokens1 = getTokens(annotation1.toString(), "(");
                String s = tokens1.get(1).substring(0, tokens1.get(1).length() - 1);
                List<String> tokens = getTokens(s, ",");
                String name = tokens.get(0).substring(6, tokens.get(0).length() - 1);
                setNameValue(name);
                String schema = tokens.get(1).substring(9, tokens.get(1).length() - 1);
                setSchemaValue(schema);
            }
        }
    }

    public boolean annotationPresent(Class<?> aClass) {
        return aClass.isAnnotationPresent(Table.class);
    }

    public boolean annotationPresent(Object o, Annotation annotation) {
        Class<?> aClass = o.getClass();
        return aClass.isAnnotationPresent(annotation.annotationType());
    }

    private List<String> getTokens(String str, String token) {
        return Collections.list(new StringTokenizer(str, token)).stream()
                .map(tokens -> (String) tokens)
                .collect(Collectors.toList());
    }

    public String getSchemaValue() {
        return schemaValue;
    }

    public void setSchemaValue(String schemaValue) {
        this.schemaValue = schemaValue;
    }

    public String getNameValue() {
        return nameValue;
    }

    public void setNameValue(String nameValue) {
        this.nameValue = nameValue;
    }


}
