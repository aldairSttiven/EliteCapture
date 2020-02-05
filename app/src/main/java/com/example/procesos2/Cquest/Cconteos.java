package com.example.procesos2.Cquest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procesos2.Model.iTemporal;
import com.example.procesos2.Model.tab.TemporalTab;
import com.example.procesos2.R;

import java.util.ArrayList;

import static java.lang.String.valueOf;

public class Cconteos {
    View ControlView;

    int idres = 0;
    iTemporal iT = null;

    //metodo que crea el control del conteo
    public View Cconteo(Context context, Long id, String contenido, Float porcentaje, final String path, final String nompro, final int idpro, final  int codusu ){
        try {
            iT = new iTemporal(path);
        }catch (Exception ex){
            Toast.makeText(context, "Exc en el paht \n"+ex.toString(), Toast.LENGTH_SHORT).show();
        }
            iT.path = path;
            iT.nombre = nompro;
            final TemporalTab tt = new TemporalTab();


        int i;
        for(i = 0; i<=1; i++){
            ArrayList<consconteos> lista = new ArrayList<>();
            lista.add(new consconteos(context,id,contenido));

            for(consconteos cc : lista){

                //ORGANIZA LOS CONTROLES INTEGRADOS
                LinearLayout.LayoutParams llparamsTotal = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                llparamsTotal.setMargins(0,0,0,10);

                LinearLayout LLtotal = new LinearLayout(context);
                LLtotal.setLayoutParams(llparamsTotal);
                LLtotal.setWeightSum(2);
                LLtotal.setOrientation(LinearLayout.VERTICAL);
                LLtotal.setPadding(10,30,10,10);
                LLtotal.setGravity(Gravity.CENTER_HORIZONTAL);
                LLtotal.setBackgroundResource(R.drawable.bordercontainer);


                LinearLayout.LayoutParams llparamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llparamsText.weight = (float) 2.3;

                LinearLayout.LayoutParams llparamsTextpo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llparamsText.weight = (float) 0.7;

                TextView tvItem = new TextView(context);
                tvItem.setText(""+id.intValue());
                tvItem.setTextColor(Color.parseColor("#58d68d"));
                tvItem.setVisibility(View.INVISIBLE);
                tvItem.setTextSize(5);
                tvItem.setTypeface(null,Typeface.BOLD);

                final TextView tvp = new TextView(context);
                tvp.setId(id.intValue());
                tvp.setText(contenido+"\nponderado: "+porcentaje.toString());
                tvp.setTextColor(Color.parseColor("#979A9A"));
                tvp.setPadding(5, 5, 5, 5);
                tvp.setBackgroundColor(Color.parseColor("#ffffff"));
                tvp.setTypeface(null, Typeface.BOLD);
                tvp.setLayoutParams(llparamsText);

                final TextView tvpor = new TextView(context);
                tvpor.setId(idres++);
                tvpor.setText("resultado: ");
                tvpor.setTextColor(Color.parseColor("#979A9A"));
                tvpor.setBackgroundColor(Color.parseColor("#ffffff"));
                tvpor.setPadding(10, 10, 10, 10);
                tvpor.setTypeface(null, Typeface.BOLD);
                tvpor.setLayoutParams(llparamsTextpo);

                LLtotal.addView(LLPREGUNTA(context,tvp,tvpor));
                LLtotal.addView(tvItem);
                LLtotal.addView(Cbotones(context,id,nompro,codusu));

                ControlView = LLtotal;
            }
        }
        return ControlView;
    }

    //metodo que crea los botones de conteos
    public View Cbotones(final Context context, final Long id, final  String nompro, final int codusu){
        View viewTotal;

        LinearLayout llbtn = new LinearLayout(context);
        llbtn.setWeightSum(4);
        llbtn.setOrientation(LinearLayout.HORIZONTAL);
        llbtn.setVerticalGravity(Gravity.CENTER);
        llbtn.setPadding(10,10,10,10);

        LinearLayout.LayoutParams LLcmpweight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LLcmpweight.weight = 3;

        final TextView tvr = new TextView(context);
        tvr.setText("-1");
        tvr.setId(id.intValue());
        tvr.setTextSize(40);
        tvr.setTextColor(Color.parseColor("#5d6d7e"));
        tvr.setTypeface(null, Typeface.BOLD);
        tvr.setGravity(Gravity.CENTER);
        tvr.setLayoutParams(LLcmpweight);

        LinearLayout.LayoutParams LLbtnweight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LLbtnweight.weight =(float) 0.5;

        Button bntres = new Button(context);
        bntres.setId(id.intValue());
        bntres.setText("-");
        bntres.setTextSize(30);
        bntres.setTextColor(Color.parseColor("#ffffff"));
        bntres.setBackgroundResource(R.drawable.btnres);
        bntres.setLayoutParams(LLbtnweight);

        Button bntsum = new Button(context);
        bntsum.setId(id.intValue());
        bntsum.setText("+");
        bntsum.setTextSize(30);
        bntsum.setTextColor(Color.parseColor("#ffffff"));
        bntsum.setBackgroundResource(R.drawable.btnsum);
        bntsum.setLayoutParams(LLbtnweight);

        llbtn.addView(bntres);
        llbtn.addView(tvr);
        llbtn.addView(bntsum);

        viewTotal = llbtn;

        final TemporalTab tt = new TemporalTab();

            bntsum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int n = Integer.parseInt(tvr.getText().toString());
                    if (n < 9) {
                        tvr.setText(valueOf(n + 1));
                        try {
                            iT.nombre = "Temp"+nompro;
                            tt.setUsuario(codusu);
                            tt.setProceso(tt.getProceso());
                            tt.setItem(id.intValue());
                            tt.setRespuesta(tvr.getText().toString());
                            tt.setPorcentaje(0.0);
                            iT.update(id.intValue(),tt);
                        }catch (Exception ex){
                            Toast.makeText(context, "Exc al actualizar \n"+ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            bntres.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int n = Integer.parseInt(tvr.getText().toString());
                    if (n > -1) {
                        tvr.setText(valueOf(n - 1));
                        try{
                            tt.setRespuesta(tvr.getText().toString());
                            Toast.makeText(context, "" + iT.update(id.intValue(), tt), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            Toast.makeText(context, "Exc al actualizar \n"+ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        return viewTotal;
    }

    //retorna view de pregunta y porcentaje
    public View LLPREGUNTA(Context c,View v1, View v2){

        LinearLayout.LayoutParams llparamspregunta = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout llpregunta = new LinearLayout(c);
        llpregunta.setLayoutParams(llparamspregunta);
        llpregunta.setWeightSum(3);
        llpregunta.setOrientation(LinearLayout.HORIZONTAL);

        llpregunta.addView(v1); //retorna pregunta
        llpregunta.addView(v2); //retorna pocentaje

        return llpregunta;
    }

    //constructor
    class consconteos{
        Context context;
        Long id;
        String contenido;

        public consconteos(Context context, Long id, String contenido) {
            this.context = context;
            this.id = id;
            this.contenido = contenido;
        }
    }
}