package app.banco.entidades;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaccion {
    private int id;
    private String fecha;
    private String hora;
    private String tipoTransaccion;
    private double monto;
    private int idCuenta;
    private String tipoCuentaDestino;

    public Transaccion(String tipoTransaccion, double monto, int idCuenta, String tipoCuentaDestino) {
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.idCuenta = idCuenta;
        this.tipoCuentaDestino = tipoCuentaDestino;

        this.fecha = LocalDate.now().toString();
        this.hora = LocalTime.now().toString();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getTipoCuentaDestino() {
        return tipoCuentaDestino;
    }

    public void setTipoCuentaDestino(String tipoCuentaDestino) {
        this.tipoCuentaDestino = tipoCuentaDestino;
    }
}
