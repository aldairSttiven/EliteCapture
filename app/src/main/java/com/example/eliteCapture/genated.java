package com.example.eliteCapture;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.ControlViews.Cdesplegable;
import com.example.eliteCapture.Config.Util.ControlViews.Cetalf;
import com.example.eliteCapture.Config.Util.ControlViews.Cetnum;
import com.example.eliteCapture.Config.Util.ControlViews.CfilAuto;
import com.example.eliteCapture.Config.Util.ControlViews.Cfiltro;
import com.example.eliteCapture.Config.Util.ControlViews.Cscanner;
import com.example.eliteCapture.Config.Util.ControlViews.Ctextview;
import com.example.eliteCapture.Config.Util.ControlViews.Cconteos;
import com.example.eliteCapture.Config.Util.ControlViews.CradioButton;
import com.example.eliteCapture.Config.Util.Dialogo;
import com.example.eliteCapture.Model.Data.Admin;
import com.example.eliteCapture.Model.Data.Tab.ProcesoTab;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.Model.View.iRespuestas;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;

import static java.lang.String.valueOf;

public class genated extends AppCompatActivity {

	TextView EncabTitulo, contcc, scrollcomplete;
	LinearLayout linearBodypop, linearPrinc;
	ScrollView scrollForm;
	Dialog mypop, popcalificacion;
	Admin adm = null;
	iContenedor iCon = null;
	ContenedorTab contenedor = null;
	SharedPreferences sp = null;

	String path = null;

	int contConsec = 1;
	ProcesoTab pro = null;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_genated);
		getSupportActionBar().hide();


		sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

		try {
			path = getExternalFilesDir(null) + File.separator; // 1

			adm = new Admin(null, path);

			getcodProceso();

			iCon = new iContenedor(path);

			contenedor = validarTemporal();

			Log.i("Contenedor", iCon.json(contenedor));


			mypop = new Dialog(this);
			popcalificacion = new Dialog(this);

			insView();

			CrearHeader(contenedor.getHeader());

			CrearForm(contenedor.getQuestions());
			// Validar como evitar re proceso.
			iCon.crearTemporal(contenedor);


			//ENCABEZADO ASIGNA EL NOMBRE DEL PROCESO
			EncabTitulo.setText(pro.getNombre_proceso());

			contcc.setText("1");

			mypop.show();


		} catch (Exception ex) {
			Log.i("Error_onCreate", ex.toString());
		}

	}

	public ContenedorTab validarTemporal() {

		try {
			ContenedorTab conTemp = iCon.optenerTemporal();
			if (conTemp != null) {
				Log.i("Contenedor_Temporal", "Existe temporal");
				if (conTemp.getIdProceso() == pro.getCodigo_proceso()) {
					Dialogo dlg = new Dialogo(this);

					boolean continuar = dlg.DialogConfirm("Tienes un formulario pendiente, \n ¿deseas continuar?.");

					Log.i("Contenedor_Temporal", "Dialogo: " + continuar);
					if (continuar) {
						return conTemp;
					}
				}
			}
			return iCon.generarContenedor(getcodUsuario(), adm.getDetalles().forDetalle(pro.getCodigo_proceso()));
		} catch (Exception e) {
			Log.i("Contenedor_Error", e.toString());
			return null;
		}
	}


	//CREA LOS CONTROLES DEL HEADER EN EL POP
	public void CrearHeader(List<RespuestasTab> header) {
		try {
			for (RespuestasTab r : header) {

				switch (r.getTipo()) {
					case "TV":
						Ctextview ct = new Ctextview();
						linearBodypop.addView(ct.textview(genated.this, r.getId(), r.getPregunta()));
						break;
					case "ETN":
						Cetnum cen = new Cetnum(genated.this, r.getId(), r.getPregunta());
						linearBodypop.addView(cen.tnumerico());
						break;
					case "ETA":
						Cetalf cal = new Cetalf(genated.this, r.getId(), r.getPregunta());
						linearBodypop.addView(cal.talfanumerico());
						break;
					case "CBX":
						Cdesplegable cd = new Cdesplegable(genated.this, r.getId(), r.getPregunta(), r.getDesplegable());
						linearBodypop.addView(cd.desplegable());
						break;
					case "FIL":
						Cfiltro cf = new Cfiltro(genated.this, r.getId(), r.getPregunta(), r.getDesplegable());
						linearBodypop.addView(cf.filtro());
						break;
					case "SCA":
						Cscanner cs = new Cscanner(genated.this, r.getId(), r.getPregunta());
						linearBodypop.addView(cs.scanner());
						break;
					case "AUT":
						CfilAuto ca = new CfilAuto(genated.this, r.getId(), r.getPregunta(), r.getDesplegable());
						linearBodypop.addView(ca.autocompletado());
						break;
					default:
						Toast.makeText(this, "ocurrio un error al crear ", Toast.LENGTH_SHORT).show();
						break;
				}

			}
		} catch (Exception exception) {
			Toast.makeText(this, "exception en generated \n \n" + exception.toString(), Toast.LENGTH_SHORT).show();
			Log.i("Excepcreate", exception.toString());
		}

	}

	//CREA CONTROLES DEL FORMULARIO
	public void CrearForm(List<RespuestasTab> questions) throws Exception {
		scrollForm.fullScroll(View.FOCUS_UP); //funcion que sube el scroll al inicio

		for (RespuestasTab r : questions) {

			Log.i("Error_Crs", "" + new iRespuestas().json(r));


			switch (r.getTipo()) {
				case "RS":
					Cconteos cc = new Cconteos(genated.this, path, "Q", r);
					linearPrinc.addView(cc.Cconteo());
					break;
				case "RB":
					CradioButton cb = new CradioButton(genated.this, path, "Q", r.getId(), r.getPregunta(), r.getPonderado(), r.getDesplegable());
					linearPrinc.addView(cb.Tradiobtn());
					break;
			}
		}
	}


	//CALIFICACIÓN POP
	public void onCalificar(View v) {
		popcalificacion.show();
	}

	// metodo para extraer del SharedPreferences el id del proceso
	public void getcodProceso() {
		pro = new Gson().fromJson(sp.getString("proceso", ""), new TypeToken<ProcesoTab>() {
		}.getType());
		Log.i("ProContenedor:", sp.getString("proceso", ""));

	}

	// metodo para extraer del SharedPreferences el id del usuario
	public int getcodUsuario() {
		return sp.getInt("codigo", 0);
	}

	//INSTANCIA CONTROLES VIEWS
	public void insView() {
		mypop.setContentView(R.layout.popupcumstom);
		popcalificacion.setContentView(R.layout.popupcalificacion);
		linearPrinc = findViewById(R.id.LinearCheck);
		EncabTitulo = findViewById(R.id.EncabTitulo);
		scrollForm = findViewById(R.id.scrollForm);
		contcc = findViewById(R.id.contcc);
		///editarEncab = findViewById(R.id.);
		linearBodypop = mypop.findViewById(R.id.linearbodypop);
		scrollcomplete = findViewById(R.id.complete);

		Window window = mypop.getWindow();
		window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	}

	//MUESTRA EL POP CON LOS CAMPOS DEL HEADER
	public void Showpop(View v) {
		mypop.show();
	}

	//OCULTA EL POP CON LOS CAMPOS DEL HEADER
	public void ocultarPop(View v) {
		mypop.dismiss();
	}


	//SI OPRIME EL BOTON DE RETROCESO
	public void onBackPressed() {
		Intent i = new Intent(this, Index.class);
		startActivity(i);
	}

	public void onBackPressedform(View v) {
		Intent i = new Intent(this, Index.class);
		startActivity(i);
	}

	//trastear estos datos------------------------------------------------------------------------------
	//trastear estos datos------------------------------------------------------------------------------
	//trastear estos datos------------------------------------------------------------------------------
	//trastear estos datos------------------------------------------------------------------------------
	public void contador1_5() {
		if (Integer.parseInt(contcc.getText().toString()) < 6) {
			contConsec++;
			contcc.setText(valueOf(contConsec));
			contcc.setTextColor(Color.parseColor("#aab7b8"));
		}
		if (Integer.parseInt(contcc.getText().toString()) == 6) {
			contConsec = 1;
			contcc.setText(valueOf(contConsec));
		}
		if (Integer.parseInt(contcc.getText().toString()) == 5) {
			contcc.setTextColor(Color.parseColor("#ec7063"));
		}
	}

	//trastear estos datos------------------------------------------------------------------------------
	//trastear estos datos------------------------------------------------------------------------------
	//trastear estos datos------------------------------------------------------------------------------
	//elimina los controles dentro del linear del formulario
	public void eliminarHijos() {
		if (linearPrinc.getChildCount() > 0) {
			linearPrinc.removeAllViews();
		}
	}

	//trastear estos datos------------------------------------------------------------------------------
	//trastear estos datos------------------------------------------------------------------------------
	//trastear estos datos------------------------------------------------------------------------------
	public void DesabilitarTeclado(View v) {

		v.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});

	}


	//METODOS PARA OBTENER DATOS
	public String getPhoneName() {
		BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
		String deviceName = myDevice.getName();
		return deviceName;
	}

}
