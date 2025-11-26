package com.example.proyestoandroid.formulario;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FieldAdapterItem {
    String key;
    String value;

    public FieldAdapterItem(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof FieldAdapterItem)) return false;
        FieldAdapterItem item = (FieldAdapterItem) obj;
        return (item.getKey().equals(key));
    }
}