package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.util.ArrayList;
import java.util.List;

public class AUT_DES_CBX {
    Context context;
    String ubicacion, path;
    RespuestasTab rt;
    boolean vacio, initial;

    containerAdmin ca;
    GIDGET pp;
    textAdmin ta;

    TextView respuestaPonderado;
    LinearLayout contenedorcampAut, LineRespuesta;
    AutoCompleteTextView campAut;
    Spinner campSpin;

    iDesplegable iDesp;

    public AUT_DES_CBX(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        this.context = context;
        this.ubicacion = ubicacion;
        this.rt = rt;
        this.path = path;
        this.initial = initial;
        this.vacio = rt.getRespuesta() != null;

        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);
        ta = new textAdmin(context);

        iDesp = new iDesplegable(null, path);

        LineRespuesta = (LinearLayout) pp.resultadoFiltro();

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? " Resultado : "+rt.getPonderado() : " Resultado : ");
    }

    public View crear(){
        contenedorcampAut = ca.container();
        contenedorcampAut.setOrientation(LinearLayout.VERTICAL);
        contenedorcampAut.setPadding(10, 0, 10, 5);
        contenedorcampAut.setGravity(Gravity.CENTER_HORIZONTAL);

        contenedorcampAut.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado
        contenedorcampAut.addView(pintarRespuesta(rt.getRespuesta()));
        contenedorcampAut.addView(camp());
        pp.validarColorContainer(contenedorcampAut, vacio, initial);//pinta el contenedor del item si esta vacio o no

        return contenedorcampAut;
    }

    public View camp(){
            LinearLayout.LayoutParams params = ca.params();
            params.setMargins(5, 2, 5, 5);

            View v = null;
            switch (rt.getTipo()) {
                case "AUT":
                    campAut = (AutoCompleteTextView) pp.campoEdtable("Auto", "grisClear");
                    campAut.setText((vacio ? rt.getCausa() : ""));
                    campAut.setAdapter(getAdapter(getDesp()));
                    FunAut(campAut);
                    v = campAut;
                    break;
                case "DES":
                case "CBX":
                    campSpin = new Spinner(context);
                    campSpin.setAdapter(getAdapter(getDesp()));
                    campSpin.setSelection((vacio ? getDesp().indexOf(rt.getCausa()) : 0));
                    campSpin.setBackgroundResource(R.drawable.myspinner);
                    FunsDesp(campSpin);
                    v = campSpin;
                    break;
            }
            v.setLayoutParams(params);
            return v;
    }

    public List<String> getDesp(){
        try {
            List<String> Loptions = new ArrayList<>();
            iDesp.nombre = rt.getDesplegable();
            if(rt.getTipo().equals("DES") || rt.getTipo().equals("CBX")) Loptions.add("Selecciona");
            for (DesplegableTab desp : iDesp.all()) {
                Loptions.add(desp.getOpcion());
            }
            return Loptions;
        }catch (Exception e){
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public ArrayAdapter<String> getAdapter(List<String> listaCargada){
        int resource = rt.getTipo().equals("DES") || rt.getTipo().equals("CBX") ? R.layout.items_des : R.layout.items_aut;
        ArrayAdapter<String> autoArray = new ArrayAdapter<>(context,resource , listaCargada);
        return autoArray;
    }

    public void FunAut(final AutoCompleteTextView etdauto) {
        etdauto.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {

                DesplegableTab dt = filtroDesplegable(campAut.getText().toString());
                registro(dt != null ? dt.getCodigo() : null, dt != null ? String.valueOf(rt.getPonderado()) : null, dt != null ? dt.getOpcion() + "" : null);
                respuestaPonderado.setText(dt != null ? "Resultado : " + rt.getPonderado() : "Resultado :");
                contenedorcampAut.setBackgroundResource(R.drawable.bordercontainer);
                pintarRespuesta(dt != null ? dt.getCodigo() : null);
            }
        });
    }

    public void FunsDesp(final Spinner spn) {
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                DesplegableTab dt = filtroDesplegable(spn.getItemAtPosition(position).toString());
                registro(dt != null ? dt.getCodigo() : null, dt != null ? String.valueOf(rt.getPonderado()) : null, dt != null ? dt.getOpcion() + "" : null);
                respuestaPonderado.setText(dt != null ? "Resultado : " + rt.getPonderado() : "Resultado :");
                pintarRespuesta(dt != null ? dt.getCodigo() : null);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void registro(String rta, String valor, String causa) {//REGISTRO
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, String.valueOf(valor), causa, rt.getReglas());
    }

    public DesplegableTab  filtroDesplegable(String rta){
        DesplegableTab dt = null;
        if(rt.getDesplegable() != null){
            DesplegableTab des = pp.busqueda(rta);
            if(des != null) {
                dt = des;
            }
        }
        return dt;
    }

    public View pintarRespuesta(String causa){//PINTA LA RESPUESTA DE BUSQUEDA DEL JSON SI SE REQUIERE
        if (LineRespuesta.getChildCount() > 0 || causa == null) LineRespuesta.removeAllViews();
        if (causa != null) {
            if (!causa.isEmpty())
                LineRespuesta.addView(ta.textColor(causa, "verde", 15, "l"));
                contenedorcampAut.setBackgroundResource(causa != null ? R.drawable.bordercontainer : R.drawable.bordercontainerred);
        }

        return LineRespuesta;
    }
}