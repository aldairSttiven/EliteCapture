package com.example.eliteCapture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eliteCapture.Model.Data.Admin;
import com.example.eliteCapture.Model.Data.Tab.ProcesoTab;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;
import com.example.eliteCapture.Model.View.iContador;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.Model.View.iHistorico;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Index extends AppCompatActivity {
    LinearLayout linearCheck, titulodata;
    TextView txtPaneluser, txtELige, txtUpdateData;
    RelativeLayout contenUser;

    public String path = null;

    SharedPreferences sp = null;
    UsuarioTab usu;

    Admin admin = null;
    iContenedor contenedor = null;

    iHistorico historico = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_index);
        linearCheck = findViewById(R.id.LinearCheck);
        titulodata = findViewById(R.id.titulodata);
        contenUser = findViewById(R.id.contenUser);
        txtPaneluser = findViewById(R.id.txtPaneluser);
        txtELige = findViewById(R.id.txtELige);

        path = getExternalFilesDir(null) + File.separator;
        sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

        try {

            admin = new Admin(null, path);
            contenedor = new iContenedor(path);
            historico = new iHistorico(path);

            traerDataUser();
            traerFechaUpDate();
            CargaMenu();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error \n" + ex, Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void clearHistory(View v){
        historico.limpiarXfecha(this);
    }

    private void traerFechaUpDate() {
        String fechaUp = sp.getString("fechaUpDate", "");
    }

    public void traerDataUser() {
        try {
            usu = new Gson().fromJson(sp.getString("usuario", ""), new TypeToken<UsuarioTab>() {
            }.getType());

            txtPaneluser.setText("Usuario : " + usu.getNombre_usuario()+"\n idUsuario : "+usu.getId_usuario());
        } catch (Exception ex) {
            Toast.makeText(this, "Se genero un error al traer los datos del usuario \n \n" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void CargaMenu() {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(30, 10, 30, 0);

        try {
            iContador contar = new iContador(path);
            for (final ProcesoTab c : orderMenu()) {

                LinearLayout.LayoutParams layoutParamsbtn = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                int cuenta = contar.getCantidad(usu.getId_usuario(), c.getCodigo_proceso());
                Button btn = new Button(getApplicationContext());
                btn.setText(c.getNombre_proceso() + (cuenta == 0 ? "" : " (" + cuenta + ")"));
                btn.setId(c.getCodigo_proceso());
                btn.setTextColor(Color.parseColor("#ffffff"));
                btn.setTextSize(15);
                btn.setBackgroundColor(Color.parseColor("#2e2d33"));
                btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                btn.setLayoutParams(layoutParamsbtn);

                //agregando check dinamicos
                linearCheck.addView(btn, layoutParams);

                btn.setOnClickListener(view -> {
                    SharedPreferences.Editor edit = sp.edit();
                    try {
                        edit.putString("proceso", admin.getProceso().json(c));
                        edit.apply();
                    } catch (Exception e) {
                        Log.i("Error onClick, Index", e.toString());
                    }

                    Intent intent = new Intent(getApplicationContext(), genated.class);
                    startActivity(intent);
                });
            }
        } catch (Exception e) {

            Log.i("Error PinProceso, Index", e.toString());
            e.printStackTrace();
        }

    }

    public List<ProcesoTab> orderMenu(){
        try {
            List<ProcesoTab> listProceso = admin.getProceso().procesosUsuario(usu.getProcesos());
            Collections.sort(listProceso, new Comparator<ProcesoTab>() {
                @Override
                public int compare(ProcesoTab o1, ProcesoTab o2) {
                    return o1.getNombre_proceso().compareTo(o2.getNombre_proceso());
                }
            });
            return listProceso;
        }catch (Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void Salir(View v) {

        SharedPreferences.Editor edit = sp.edit();
        edit.putString("usuario", "");

        edit.apply();

        Intent i = new Intent(Index.this, Login.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void onActualizar(View v) {
        Intent i = new Intent(Index.this, splash_activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("class", "Index");
        i.putExtra("idUsuario", usu.getId_usuario());
        i.putExtra("redireccion", 2);
        i.putExtra("carga", "BajarDatos");

        startActivity(i);
        linearCheck.removeAllViews();
    }

    public void onBackPressed() {
    }
}
