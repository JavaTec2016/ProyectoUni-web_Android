package com.example.proyestoandroid.formulario;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.proyestoandroid.R;

import java.util.LinkedHashMap;

public class FormWrapper {

    public ScrollView container;
    LinkedHashMap<String, FieldWrapper> ui = new LinkedHashMap<>();

    public FormWrapper(ViewGroup parent, boolean attach){
        container = (ScrollView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.form_container, parent, attach);
    }
    public FormWrapper(ScrollView container){
        this.container = container;
    }
    public LinearLayout getLayout(){
        return container.findViewById(R.id.formLayout);
    }
    public Context getCtx(){
        return getLayout().getContext();
    }
    public void addField(String id, FieldWrapper field){
        ui.put(id, field);
        getLayout().addView(field.container);
        //getLayout().requestLayout();
        Log.i("DEBUGEO", "items " + getLayout().getChildCount());
    }
    public void addLabel(TextView lbl){
        getLayout().addView(lbl);
    }
    public void makeNewField(String id, String type, String hint){
        FieldWrapper f = new FieldWrapper(type, getLayout(), false);
        f.setTextHint(hint);
        addField(id, f);
    }
    public void makeNewField(String id, String type, String hint, String label){
        FieldWrapper f = new FieldWrapper(type, getLayout(), false);
        f.setTextHint(hint);
        f.setLabelText(label);
        addLabel(f.label);
        addField(id, f);
    }
    public void onFieldsChange(FieldWrapper.FieldListener listener){
        ui.forEach((key, field)->{
            field.addFieldChangeListener(listener);
        });
    }

}
