package com.monframework.core;

public class Mapping {
    private String className;  // Ex: "com.monapp.controller.EmpController"
    private String method;     // Ex: "listeEmployes"

    public Mapping(String className, String method) {
        this.className = className;
        this.method = method;
    }

    // Getters et Setters
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}