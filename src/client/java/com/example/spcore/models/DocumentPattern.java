package com.example.spcore.models;

import java.util.List;

public class DocumentPattern {
    public String pattentName;
    public String description;
    public List<Field> fields;
    public List<String> template;
    public static class Field {
        public String name;
        public String displayName;
        public String description;
    }
}
