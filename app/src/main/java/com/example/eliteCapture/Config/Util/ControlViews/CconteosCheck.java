package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class CconteosCheck {

    View ControlView;

    Context c;
    String path, ubicacion;
    RespuestasTab rT;
    int id;
    boolean vacio, inicial;

    String respuestaSpin;

    CheckBox cB;

    ControlGnr Cgnr;

    public CconteosCheck(Context context, String path, String ubicacion, RespuestasTab rt, Boolean inicial) {
        this.c = context;
        this.path = path;
        this.ubicacion = ubicacion;
        this.rT = rt;
        this.id = rt.getId().intValue() + 1;
        this.vacio = rt.getRespuesta() != null;
        this.inicial = inicial;
    }

    //metodo que crea el control del conteo
    public View Cconteo() {

        LinearLayout.LayoutParams llparamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 2.3;

        LinearLayout.LayoutParams llparamsTextpo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 0.7;

        final TextView tvp = new TextView(c);
        tvp.setId(rT.getId().intValue());
        tvp.setText(id + ". " + rT.getPregunta() + "\nponderado: " + rT.getPonderado());
        tvp.setTextColor(Color.parseColor("#979A9A"));
        tvp.setPadding(5, 5, 5, 5);
        tvp.setTypeface(null, Typeface.BOLD);
        tvp.setLayoutParams(llparamsText);

        String valor;
        if(rT.getValor()==null){
            valor = "Resultado : \n";
        }else {
            if(rT.getValor().equals("-1")){
                valor = "Resultado :\nNA";
            }else {
                valor = "Resultado : \n"+rT.getValor();
            }
        }

        final TextView tvpor = new TextView(c);
        tvpor.setId(rT.getId().intValue());
        tvpor.setText((rT.getValor() == null || rT.getValor().equals("null") ? "Resultado : \n" : valor));
        tvpor.setTextColor(Color.parseColor("#979A9A"));
        tvpor.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        tvpor.setPadding(10, 10, 10, 10);
        tvpor.setTypeface(null, Typeface.BOLD);
        tvpor.setLayoutParams(llparamsTextpo);

        Log.i("getvalor", "Conteos "+rT.getValor());

        Cgnr = new ControlGnr(c, rT.getId(), pp(tvp, tvpor), Check() ,Cbotones(tvpor) , "vx3");
        ControlView = Cgnr.Contenedor(vacio, inicial, rT.getTipo());

        return ControlView;
    }

    //metodo que crea los botones de conteos
    public View Cbotones(final TextView tvpor) {
        View viewTotal;

        LinearLayout llbtn = new LinearLayout(c);
        llbtn.setWeightSum(3);
        llbtn.setOrientation(LinearLayout.HORIZONTAL);
        llbtn.setVerticalGravity(Gravity.CENTER);
        llbtn.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        LinearLayout.LayoutParams LLcmpweight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LLcmpweight.weight = (float) 2;

        final TextView tvr = new TextView(c);
        tvr.setText((rT.getRespuesta() == null) ? "-1" : rT.getRespuesta());
        tvr.setId(rT.getId().intValue());
        tvr.setTextSize(40);
        tvr.setTextColor(Color.parseColor("#5d6d7e"));
        tvr.setTypeface(null, Typeface.BOLD);
        tvr.setGravity(Gravity.CENTER);
        tvr.setLayoutParams(LLcmpweight);

        LinearLayout.LayoutParams LLbtnweight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LLbtnweight.weight = (float) 0.5;

        Button bntres = new Button(c);
        bntres.setId(rT.getId().intValue());
        bntres.setText("-");
        bntres.setTextSize(20);
        bntres.setTextColor(Color.parseColor("#ffffff"));
        bntres.setBackgroundResource(R.drawable.btnres);
        bntres.setLayoutParams(LLbtnweight);

        Button bntsum = new Button(c);
        bntsum.setId(rT.getId().intValue());
        bntsum.setText("+");
        bntsum.setTextSize(20);
        bntsum.setTextColor(Color.parseColor("#ffffff"));
        bntsum.setBackgroundResource(R.drawable.btnsum);
        bntsum.setLayoutParams(LLbtnweight);

        llbtn.addView(bntres);
        llbtn.addView(tvr);
        llbtn.addView(bntsum);

        viewTotal = llbtn;

        int data = Integer.parseInt(tvr.getText().toString());
        ResSum(data, rT.getRespuesta() == null, tvr);

        //btn de suma
        bntsum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(tvr.getText().toString());
                if (n < rT.getReglas()) {
                    int rta = n + 1;
                    String data = ResSum(rta, false, tvr);
                    tvr.setText(data);

                    String vlr = valor(rta);
                    tvpor.setText((rta < 0) ? "Resultado: " : "Resultado: \n" + vlr);

                    try {
                        registro(data, vlr, respuestaSpin);
                    } catch (Exception e) {
                        Log.i("Error_Crs", e.toString());
                    }
                }

            }
        });

        //btn de resta
        bntres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(tvr.getText().toString());
                if (n >= 0) {
                    int rta = n - 1;
                    String data = ResSum(rta, false, tvr);
                    tvr.setText(data);

                    String vlr = valor(rta);
                    tvpor.setText((rta < 0) ? "Resultado: \n NA" : "Resultado: \n" + vlr);
                    try {
                        registro(data, (rta < 0) ? "-1" : vlr, respuestaSpin);
                    } catch (Exception e) {
                        Log.i("Error_Crs", e.toString());
                    }
                }

                //valida si no aplica
                if(tvr.getVisibility()==View.INVISIBLE && n<0){

                    tvr.setVisibility(View.VISIBLE);
                    tvpor.setText("Resultado: \n NA");

                    int rta = n;
                    String vlr = valor(rta);
                    String data = ResSum(rta, false, tvr);
                    try {
                        registro(data, (rta < 0) ? "-1" : vlr, respuestaSpin);
                    } catch (Exception e) {
                        Log.i("Error_Crs", e.toString());
                    }

                }else{}

            }

        });


        return viewTotal;
    }

    public String ResSum(int rta, boolean inicial, TextView tvr) {
        String data = String.valueOf(rta);
        try {
            if (inicial && data.equals("-1")) {
                tvr.setVisibility(View.INVISIBLE);
            } else if (!inicial && rta > -1) {
                tvr.setVisibility(View.VISIBLE);
            } else if (!inicial && rta <= 0) {
                tvr.setVisibility(View.VISIBLE);
                registro("-1", "-1", respuestaSpin);
            }
        } catch (Exception ex) {
            Toast.makeText(c, "" + ex.toString(), Toast.LENGTH_SHORT).show();
        }

        return data;
    }

    public View Check(){

        for(int i = 0 ; i < soloOpciones(rT.getDesplegable()).size(); i++){
            cB = new CheckBox(c);
            cB.setText(i);
        }

        return cB;
    }

    //opciones del desplegable
    public ArrayList soloOpciones(String opcion) {
        try {
            ArrayList<String> opc = new ArrayList<>();

            iDesplegable iDesp = new iDesplegable(null, path);
            iDesp.nombre = opcion;
            opc.add("Selecciona");
            for (DesplegableTab des : iDesp.all()) {
                opc.add(des.getOpcion());
            }
            return opc;
        } catch (Exception ex) {
            Toast.makeText(c, "" + ex, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public String valor(int rta) {
        try {
            DecimalFormatSymbols separator = new DecimalFormatSymbols();
            separator.setDecimalSeparator('.');

            DecimalFormat decimalFormat = new DecimalFormat("#.##",separator);
            float operacion = Float.parseFloat((rta < 0) ? "-1" : String.valueOf((rT.getPonderado() / rT.getReglas()) * (rT.getReglas() - rta)));

            return String.valueOf(decimalFormat.format(operacion));
        }catch (Exception ex){
            Toast.makeText(c, ""+ex.toString(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    public void registro(String rta, String valor, String causa) throws Exception {
        new iContenedor(path).editarTemporal(ubicacion, rT.getId().intValue(), rta, String.valueOf(valor), causa, rT.getReglas());
    }

    //retorna el layout del encabezado pregunta porcentaje
    public LinearLayout pp(View v1, View v2) {
        LinearLayout.LayoutParams llparamspregunta = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        LinearLayout llpregunta = new LinearLayout(c);
        llpregunta.setLayoutParams(llparamspregunta);
        llpregunta.setWeightSum(3);
        llpregunta.setOrientation(LinearLayout.HORIZONTAL);
        llpregunta.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        llpregunta.addView(v1); //retorna pregunta
        llpregunta.addView(v2); //retorna pocentaje

        return llpregunta;
    }
}