package com.example.proyestoandroid.formulario;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.proyestoandroid.R;

import java.util.LinkedHashMap;

public class FieldWrapper {
    private int width = 0;
    private int height = 0;
    public String type;
    public String id;
    public LinkedHashMap<String, View> ui = new LinkedHashMap<>();

    public ConstraintLayout container;

    private FieldListener generalChangeListener;

    /**
     * Infla un layout de field container con el parent especificado, pero sin agregarle nada
     * @param type tipo de input
     * @param parent view al que pertenece el container, layout del form container
     * @param attach si es true, se agrega el container al parent automaticamente
     */
    public FieldWrapper(String type, ViewGroup parent, boolean attach){
        this(type, parent, attach, 400, 20);
    }
    public FieldWrapper(String type, ViewGroup parent, boolean attach, int w, int h){
        this.type = type;
        container = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.field_container, parent, attach);

        buildFields(type);
        setDimensions(w, h);
        requestLayout();
    }
    private LinearLayout.LayoutParams makeLinearParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        return  params;
    }
    public void requestLayout(){
        container.requestLayout();
    }
    public void setDimensions(int w, int h){
        width = w;
        height = h;
        if(ui.size() == 1){
            View v = ui.values().iterator().next();
            v.setMinimumHeight(w);
            v.setMinimumHeight(h);
            v.setLayoutParams(makeLinearParams());

        }else if(type.equalsIgnoreCase("spinner")){
            ui.get("integer").setMinimumWidth((int) (w*0.75));
            ui.get("decimal").setMinimumWidth((int) (w*0.25)-20);
            ui.get("dot").setMinimumWidth(20);

            ui.get("integer").setMinimumHeight(w);
            ui.get("decimal").setMinimumHeight(h);
            ui.get("dot").setMinimumHeight(h);

            ui.get("integer").setLayoutParams(makeLinearParams());
            ui.get("decimal").setLayoutParams(makeLinearParams());
            ui.get("dot").setLayoutParams(makeLinearParams());
        }
    }
    public HorizontalScrollView getView(){
        return container.findViewById(R.id.fieldContainer);
    }
    public LinearLayout getLayout(){
        return container.findViewById(R.id.fieldLayout);
    }
    public Context getCtx(){
        return getLayout().getContext();
    }
    public void appendFields(){
        ui.forEach((id, view)->{
            getLayout().addView(view);
        });
    }
    public void clear(){
        getLayout().removeAllViews();
        ui.clear();
    }
    /**
     * todo configuracion al edittext
     */
    private void addEditText(String id){
        EditText et = new EditText(getCtx());
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        getLayout().addView(et);
        ui.put(id, et);
    }
    private void addEditText_name(String id){
        EditText et = new EditText(getCtx());
        et.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        getLayout().addView(et);
        ui.put(id, et);
    }
    private void addEditNumber(String id){
        EditText et = new EditText(getCtx());
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        getLayout().addView(et);
        ui.put(id, et);
    }
    private static void setMaxLength(EditText t, int length){
        t.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(length)
        });
    }
    private void addEditDate(String id){
        EditText et = new EditText(getCtx());
        et.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        getLayout().addView(et);
        ui.put(id, et);
    }
    private void addEditPhone(String id){
        EditText et = new EditText(getCtx());
        et.setInputType(InputType.TYPE_CLASS_PHONE);
        getLayout().addView(et);
        ui.put(id, et);
    }
    private void addEditEmail(String id){
        EditText et = new EditText(getCtx());
        et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        getLayout().addView(et);
        ui.put(id, et);
    }
    private void addEditDecimal(String idInts, String idDec){
        EditText et = new EditText(getCtx());
        TextView dot = new TextView(getCtx());
        EditText dc = new EditText(getCtx());

        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        dc.setInputType(InputType.TYPE_CLASS_NUMBER);

        FieldWrapper.setMaxLength(et, 10);
        dot.setText(".");
        FieldWrapper.setMaxLength(dc, 2);

        ui.put(idInts, et);
        ui.put("dot", dot);
        ui.put(idDec, dc);

        getLayout().addView(et);
        getLayout().addView(dot);
        getLayout().addView(dc);
    }
    private void addDropdown(String id, FieldAdapterItem[] options){
        if(options == null){
            options = new FieldAdapterItem[]{new FieldAdapterItem("", "Seleccionar...")};
        }
        ArrayAdapter<FieldAdapterItem> adapter = new ArrayAdapter<>(getCtx(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, options);
        Spinner et = new Spinner(getCtx());
        et.setAdapter(adapter);
        getLayout().addView(et);
        ui.put(id, et);
    }
    public void setTextHint(String hint){
        switch (type.toLowerCase()){
            case "text":
            case "number":
            case "date":
            case "phone":
            case "email":
                ((EditText)ui.get(type.toLowerCase())).setHint(hint); break;
            case "decimal":
                ((EditText)ui.get("integer")).setHint(hint); break;
        }
    }
    protected String getValueAsEditText(View v){
        return ((EditText)v).getText().toString();
    }
    public String getValueAsEditText(String v){
        return getValueAsEditText(ui.get(v));
    }
    protected String getValueAsSpinner(View v){
        return ((FieldAdapterItem)((Spinner)v).getSelectedItem()).getKey();
    }
    public String getValueAsSpinner(String v){
        return getValueAsSpinner(ui.get(v));
    }
    protected void setValueAsEditText(View v, String txt){
        ((EditText)v).setText(txt);
    }
    public void setValueAsEditText(String v, String txt){
        setValueAsEditText(ui.get(v), txt);
    }
    protected void setValueAsSpinner(View v, String key){
        Spinner spin = ((Spinner)v);
        spin.setSelection(((ArrayAdapter<FieldAdapterItem>)spin.getAdapter()).getPosition(new FieldAdapterItem(key, null)));
    }
    public void setValueAsSpinner(String v, String key){
        setValueAsSpinner(ui.get(v), key);
    }

    /**
     * Extrae el valor de los campos segun el tipo de input
     * @return
     */
    public String getValue(){
        switch (type.toLowerCase()){
            case "number":
            case "date":
            case "phone":
            case "text": return getValueAsEditText(type.toLowerCase());
            case "decimal":
                String integer = getValueAsEditText("integer");
                String decimal = getValueAsEditText("decimal");
                return integer + "." + decimal;
            case "spinner":
                return getValueAsSpinner("spinner");
        }
        return null;
    }

    /**
     * Establece el valor de los campos
     * @param value
     */
    public void setValue(Object value){
        String str = String.valueOf(value);
        switch (type.toLowerCase()){
            case "text":
            case "number":
            case "date":
            case "phone": setValueAsEditText(type.toLowerCase(), str); break;
            case "decimal":
                String[] number = str.split("\\.");
                if(number.length > 1){
                    setValueAsEditText("decimal", number[1]);
                }
                setValueAsEditText("integer", number[0]);
                break;
            case "spinner": setValueAsSpinner("spinner", str); break;
        }
    }

    public void addTextChangedListener(String v){
        FieldWrapper hook = this;
        EditText et = (EditText)ui.get(v);
        if(et == null) return;
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hook.generalChangeListener.run(hook, v);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    public void addSelectionChangeListener(String v){
        FieldWrapper hook = this;
        Spinner sp = (Spinner)ui.get(v);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hook.generalChangeListener.run(hook, v);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private void buildFields(String type){
        switch (type.toLowerCase()){
            case "text": addEditText("text"); break;
            case "number": addEditNumber("number"); break;
            case "date": addEditDate("date"); break;
            case "phone": addEditPhone("phone"); break;
            case "email": addEditPhone("phone"); break;
            case "decimal": addEditDecimal("integer", "decimal"); break;
            case "spinner": addDropdown("spinner", null); break;
        }
    }

    public void setFieldChangeListener(FieldListener generalChangeListener) {
        this.generalChangeListener = generalChangeListener;
    }
    public void addFieldChangeListener(FieldListener call){
        generalChangeListener = call;
        switch (type.toLowerCase()){
            case "text":
            case "number":
            case "date":
            case "email":
            case "phone": addTextChangedListener(type.toLowerCase()); break;
            case "decimal":
                addTextChangedListener("integer");
                addTextChangedListener("decimal");
                break;
            case "spinner": addSelectionChangeListener("spinner"); break;
        }
    }
    /**
     * Interfaz para callbacks al modificar datos de la ui
     */
    public interface FieldListener {
        void run(FieldWrapper wrapper, String fieldId);
    }
}
