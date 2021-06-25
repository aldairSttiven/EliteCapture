package com.example.eliteCapture.Model.View.Tab;


import java.io.Serializable;

public class RespuestasTab implements Serializable {
    private Long id;
	private int codigo;
	private int idProceso;
	private Long idPregunta;
	private String tipo;
	private String pregunta;
	private float ponderado;
	private String respuesta;
	private String valor;
	private String causa;
	private String desplegable;
	private int reglas;
	private String tip;
	private String desde_hasta;
	private int decimales;
	private int obligatorio;

	public RespuestasTab(Long id, int codigo, int idProceso, Long idPregunta, String tipo, String pregunta, float ponderado, String respuesta, String valor, String causa, String desplegable, int reglas, String tip, String desde_hasta, int decimales, int obligatorio) {
		this.id = id;
		this.codigo = codigo;
		this.idProceso = idProceso;
		this.idPregunta = idPregunta;
		this.tipo = tipo;
		this.pregunta = pregunta;
		this.ponderado = ponderado;
		this.respuesta = respuesta;
		this.valor = valor;
		this.causa = causa;
		this.desplegable = desplegable;
		this.reglas = reglas;
		this.tip = tip;
		this.desde_hasta = desde_hasta;
		this.decimales = decimales;
		this.obligatorio = obligatorio;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(int idProceso) {
		this.idProceso = idProceso;
	}

	public Long getIdPregunta() {
		return idPregunta;
	}

	public void setIdPregunta(Long idPregunta) {
		this.idPregunta = idPregunta;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public float getPonderado() {
		return ponderado;
	}

	public void setPonderado(float ponderado) {
		this.ponderado = ponderado;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public String getDesplegable() {
		return desplegable;
	}

	public void setDesplegable(String desplegable) {
		this.desplegable = desplegable;
	}

	public int getReglas() {
		return reglas;
	}

	public void setReglas(int reglas) {
		this.reglas = reglas;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getDesde_hasta() {
		return desde_hasta;
	}

	public void setDesde_hasta(String desde_hasta) {
		this.desde_hasta = desde_hasta;
	}

	public int getDecimales() {
		return decimales;
	}

	public void setDecimales(int decimales) {
		this.decimales = decimales;
	}

	public int getObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(int obligatorio) {
		this.obligatorio = obligatorio;
	}
}
