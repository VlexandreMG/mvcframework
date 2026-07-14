package com.monframework.model;

import java.util.HashMap;

public class ModelAndView {
    
    private String view;
    private HashMap<String, Object> data = new HashMap<>();
    
    public void setAttribute(String key, Object value) {
        this.data.put(key,value);
    }
    
    public HashMap<String, Object> getData() {
        return data;
    }
    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
