package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

public class ETN_ETA {

    Context context;
    RespuestasTab rt;
    String ubicacion, path;
    boolean vacio, initial;
    
    TextView respuestaPonderado;
    EditText camp;
    LinearLayout contenedorCamp;
    containerAdmin ca;
    preguntaPonderado pp;

    public ETN_ETA(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {//CONSTRUCTOR
        this.context = context;
        this.rt = rt;
        this.ubicacion = ubicacion;
        this.path = path;
        this.vacio = rt.getRespuesta() != null;
        this.initial = initial;

        ca = new containerAdmin(context);
        pp = new preguntaPonderado(context, ubicacion, rt, path);

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? "Resultado : "+rt.getPonderado() : "Resultado :");
    }

    public View crear(){//GENERA EL CONTENEDOR DEL ITEM
        contenedorCamp = ca.container();
        contenedorCamp.setOrientation(LinearLayout.VERTICAL);
        contenedorCamp.setPadding(10, 0, 10, 0);
        contenedorCamp.setGravity(Gravity.CENTER_HORIZONTAL);

        contenedorCamp.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado
        contenedorCamp.addView(campo());
        pp.validarColorContainer(contenedorCamp, vacio, initial);//pinta el contenedor del item si esta vacio o no

        return contenedorCamp;
    }

    public EditText campo(){//CAMPO DE USUARIO
        camp = (EditText) pp.campoEdtable("Edit");

        LinearLayout.LayoutParams llparams = ca.params();
        llparams.weight = 1;
        llparams.setMargins(5,2,5,10);

        //para limitar cantidad de digitos segun la regla
        if (rt.getReglas() != 0) camp.setFilters(
                new InputFilter[]{
                        new InputFilter.LengthFilter(rt.getReglas())
                });

        camp.setText(vacio ? rt.getRespuesta() : "");
        camp.setLayoutParams(llparams);

        if(rt.getTipo().equals("ETN")) camp.setRawInputType(Configuration.KEYBOARD_QWERTY);

        funCamp();
        return camp;
    }

    public void funCamp(){//FUNCION DEL CAMPO
        camp.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                    String rta = camp.getText().toString();
                    registro(!rta.isEmpty() ? rta : null, !rta.isEmpty() ? rt.getPonderado()+"" : null);
                    respuestaPonderado.setText(!rta.isEmpty() ? "Resultado : "+rt.getPonderado() : "Resultado :");
                    contenedorCamp.setBackgroundResource(R.drawable.bordercontainer);
            }
        });
    }

    public void registro(String rta, String valor) {//REGISTRO
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, String.valueOf(valor), null, rt.getReglas());
    }
}