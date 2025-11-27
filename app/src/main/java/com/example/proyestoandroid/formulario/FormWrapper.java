package com.example.proyestoandroid.formulario;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.proyestoandroid.R;

import java.util.LinkedHashMap;
import java.util.Objects;

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
        field.id = id;
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
    public FieldWrapper makeNewField(String id, String type, String hint, String label){
        FieldWrapper f = new FieldWrapper(type, getLayout(), false);
        f.setTextHint(hint);
        f.setLabelText(label);
        addLabel(f.label);
        addField(id, f);
        return f;
    }
    public void markLabelError(String fieldId, String error){
        FieldWrapper fld = ui.get(fieldId);
        TextView txt = fld.label;
        txt.setTextColor(Color.RED);
        String err = fld.labelText+"("+error+")";
        txt.setText(err);
    }
    public void clearLabelError(String fieldId){
        FieldWrapper fld = ui.get(fieldId);
        TextView txt = fld.label;
        txt.setTextColor(Color.BLACK);
        String err = fld.labelText;
        txt.setText(err);
    }
    /**
     * Extrae los datos de todos los campos registrados en el formulario a un mapa
     * @return
     */
    public LinkedHashMap<String, Object> getFormData(){
        LinkedHashMap<String , Object> out = new LinkedHashMap<>();
        ui.forEach((id, field)->{
            out.put(id, field.getValue());
        });
        return out;
    }

    /**
     * coloca los datos del mapa en todos los campos del formulario cuyas ids coincidan
     * @param datos
     */
    public void setFormData(LinkedHashMap<String, Object> datos){
        datos.forEach((id, value)->{
            if(!ui.containsKey(id)) return;
            ui.get(id).setValue(value);
        });
    }
    public void clearForm(){
        ui.forEach((id, field)->{
            field.setValue("");
        });
    }
    public void onFieldsChange(FieldWrapper.FieldListener listener){
        ui.forEach((key, field)->{
            field.addFieldChangeListener(listener);
        });
    }

}
