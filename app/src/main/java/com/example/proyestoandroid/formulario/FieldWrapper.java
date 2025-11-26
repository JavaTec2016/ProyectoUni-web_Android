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
    public static final int SIZE_AUTO = -5;
    private int width = 0;
    private int height = 0;
    public String type;
    public String id;
    //agregar por separao
    public TextView label;
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
        this(type, parent, attach, 800, SIZE_AUTO);
    }
    public FieldWrapper(String type, ViewGroup parent, boolean attach, int w, int h){
        this.type = type;
        container = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.field_container, parent, attach);

        buildFields(type);
        setDimensions(w, h);
        requestLayout();
    }
    public void setLabelText(String txt){
        label.setText(txt);
    }
    private LinearLayout.LayoutParams makeLinearParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        return  params;
    }
    private LinearLayout.LayoutParams makeLinearParams(int w, int h){
        int wi=w, he=h;
        if(w==SIZE_AUTO) wi = ViewGroup.LayoutParams.MATCH_PARENT;
        if(h==SIZE_AUTO) he = ViewGroup.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                wi,
                he
        );
        Log.i("PARAMS", "params como: " + wi + " , " + he);
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

            v.setLayoutParams(makeLinearParams(w, h));

        }else if(type.equalsIgnoreCase("decimal")){

            ui.get("integer").setLayoutParams(makeLinearParams((int) (w*0.75), h));
            ui.get("decimal").setLayoutParams(makeLinearParams((int) (w*0.25)-22, h));
            ui.get("dot").setLayoutParams(makeLinearParams(20, h));

        }else if(type.equalsIgnoreCase("date")){

            ui.get("date_y").setLayoutParams(makeLinearParams((int) (w*0.4), h));
            ui.get("date_m").setLayoutParams(makeLinearParams((int) (w*0.4), h));
            ui.get("date_d").setLayoutParams(makeLinearParams((int) (w*0.2), h));
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
        EditText y = new EditText(getCtx());
        Spinner m = makeSpinner(new FieldAdapterItem[]{
                new FieldAdapterItem("", "Mes"),
                new FieldAdapterItem("1", "1 - Enero"),
                new FieldAdapterItem("2", "2 - Febrero"),
                new FieldAdapterItem("3", "3 - Marzo"),
                new FieldAdapterItem("4", "4 - Abril"),
                new FieldAdapterItem("5", "5 - Mayo"),
                new FieldAdapterItem("6", "6 - Junio"),
                new FieldAdapterItem("7", "7 - Julio"),
                new FieldAdapterItem("8", "8 - Agosto"),
                new FieldAdapterItem("9", "9 - Septiembre"),
                new FieldAdapterItem("10", "10- Octubre"),
                new FieldAdapterItem("11", "11- Noviembre"),
                new FieldAdapterItem("12", "12- Diciembre"),
        });
        EditText d = new EditText(getCtx());

        y.setInputType(InputType.TYPE_CLASS_NUMBER);
        d.setInputType(InputType.TYPE_CLASS_NUMBER);
        setMaxLength(y, 4);
        setMaxLength(d, 2);

        getLayout().addView(y);
        getLayout().addView(m);
        getLayout().addView(d);

        ui.put(id+"_y", y);
        ui.put(id+"_m", m);
        ui.put(id+"_d", d);
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
        Spinner et = makeSpinner(options);
        getLayout().addView(et);
        ui.put(id, et);
    }
    private Spinner makeSpinner(FieldAdapterItem[] options){
        if(options == null){
            options = new FieldAdapterItem[]{new FieldAdapterItem("", "Seleccionar...")};
        }
        ArrayAdapter<FieldAdapterItem> adapter = new ArrayAdapter<>(getCtx(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, options);
        Spinner et = new Spinner(getCtx());
        et.setAdapter(adapter);
        return et;
    }
    public void setTextHint(String hint){
        switch (type.toLowerCase()){
            case "text":
            case "number":
            case "phone":
            case "email":
                ((EditText)ui.get(type.toLowerCase())).setHint(hint); break;
            case "decimal":
                ((EditText)ui.get("integer")).setHint(hint);
                ((EditText)ui.get("decimal")).setHint("00");
                break;
            case "date":
                ((EditText)ui.get("date_y")).setHint("Año (Ej. 2002)");
                ((EditText)ui.get("date_d")).setHint("dia (Ej. 31)");
                break;
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
            case "phone":
            case "text": return getValueAsEditText(type.toLowerCase());
            case "decimal":
                String integer = getValueAsEditText("integer");
                String decimal = getValueAsEditText("decimal");
                return integer + "." + decimal;
            case "spinner":
                return getValueAsSpinner("spinner");
            case "date":
                String[] data = {
                        getValueAsEditText("date_y"),
                        getValueAsSpinner("date_m"),
                        getValueAsEditText("date_d")
                };
                return String.join("-", data);
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
            case "phone": setValueAsEditText(type.toLowerCase(), str); break;
            case "decimal":
                String[] number = str.split("\\.");
                if(number.length > 1){
                    setValueAsEditText("decimal", number[1]);
                }
                setValueAsEditText("integer", number[0]);
                break;
            case "spinner": setValueAsSpinner("spinner", str); break;
            case "date":
                String[] ymd = str.split("-");
                if(ymd.length != 3) return;
                setValueAsEditText("date_y", ymd[0]);
                setValueAsSpinner("date_m", ymd[1]);
                setValueAsEditText("date_d", ymd[2]);
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
        label = new TextView(getCtx());
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
            case "email":
            case "phone": addTextChangedListener(type.toLowerCase()); break;
            case "decimal":
                addTextChangedListener("integer");
                addTextChangedListener("decimal");
                break;
            case "spinner": addSelectionChangeListener("spinner"); break;
            case "date":
                addTextChangedListener("date_y");
                addSelectionChangeListener("date_m");
                addTextChangedListener("date_d");
                break;
        }
    }
    /**
     * Interfaz para callbacks al modificar datos de la ui
     */
    public interface FieldListener {
        void run(FieldWrapper wrapper, String fieldId);
    }
}
