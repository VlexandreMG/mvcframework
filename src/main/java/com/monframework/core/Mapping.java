package com.monframework.core;

import java.lang.reflect.Method;

public class Mapping {
    private Class className;
    private Method methode;
    
    
    public Mapping() {}
    
    public Class getClassName() {
        return className;
    }
    public void setClassName(Class className) {
        this.className = className;
    }
    public Method getMethode() {
        return methode;
    }
    public void setMethode(Method methode) {
        this.methode = methode;
    }    
}
