package app.banco.servicios;

import app.banco.entidades.CuentaAhorro;
import app.banco.entidades.CuentaCorriente;
import app.banco.entidades.Transaccion;
import app.banco.entidades.abstractas.CuentaBancaria;
import app.banco.repositorios.CuentaBaseDatos;
import app.banco.repositorios.TransaccionBaseDatos;
import app.banco.repositorios.interfaces.RepositorioCRUD;
import app.banco.servicios.interfaces.Servicio;

import java.util.List;
import java.util.Map;

public class ServicioTransaccion implements Servicio {
    private RepositorioCRUD repositorioTransaccion;
    private RepositorioCRUD repositorioCuenta;

    public ServicioTransaccion() {
        repositorioTransaccion = new TransaccionBaseDatos();
        repositorioCuenta = new CuentaBaseDatos();
    }

    @Override
    public Object Crear(Map datosTransaccion) {
        String tipoTransaccion = (String) datosTransaccion.get("tipoTransaccion");
        double monto = Double.parseDouble(datosTransaccion.get("monto").toString());
        String numeroCuenta = (String) datosTransaccion.get("numeroCuenta");
        String tipoCuentaDestino = (String) datosTransaccion.get("tipoCuentaDestino");

        // Validamos los campos
        if(tipoTransaccion == null || tipoTransaccion == "" ||
                monto <= 0 ||
                numeroCuenta == null || numeroCuenta == "")
        {
            throw new RuntimeException("No se enviaron todos los campos");
        }

        if(!tipoTransaccion.equals("depositar") &&
                !tipoTransaccion.equals("retirar") &&
                !tipoTransaccion.equals("transferir"))
        {
            throw new RuntimeException("El tipo de transaccion indicado no es valido, deber ser: " +
                    "depositar, retirar o transferir");
        }

        if(tipoTransaccion.equals("transferencia") && tipoCuentaDestino == null || tipoCuentaDestino   == ""){
            throw new RuntimeException("Si el tipo de transaccion es transferencia, la cuenta de destino no puede ser nula");
        }

        CuentaBancaria cuenta = (CuentaBancaria) repositorioCuenta.ObtenerUno(numeroCuenta);

        if(cuenta == null) {
            throw new RuntimeException("La cuenta indicada no existe");
        }

        Transaccion transaccion = new Transaccion(tipoTransaccion, monto, cuenta.getId(), tipoCuentaDestino);

        try {
            if(cuenta.getTipoCuenta().equals("CuentaAhorro")){
                CuentaAhorro CA = (CuentaAhorro) cuenta;
                cuenta = doTransactionAhorro(CA, transaccion);
            } else {
                CuentaCorriente CC = (CuentaCorriente) cuenta;
                cuenta = doTransactionCorriente(CC, transaccion);
            }
        }
        catch (Exception e){
            throw e;
        }

        boolean saldoActualizado = repositorioCuenta.Actualizar(cuenta.getId(), cuenta.getSaldo());

        if(!saldoActualizado){
            throw new RuntimeException("Ha ocurrido un error al actualizar el saldo de la cuenta");
        }

        return repositorioTransaccion.Crear(transaccion);
    }

    @Override
    public Object ObtenerUno(Object id) {
        return repositorioTransaccion.ObtenerUno(id);
    }

    @Override
    public List<?> ListarTodos() {
        return repositorioTransaccion.ListarTodos();
    }

    @Override
    public String Eliminar(Object idTransaccion) {

        Transaccion transaccionAEliminar = (Transaccion) repositorioTransaccion.ObtenerUno(idTransaccion);

        if(transaccionAEliminar == null){
            throw new RuntimeException("La transaccion indicada no existe");
        }

        return repositorioTransaccion.Eliminar(transaccionAEliminar);
    }

    public CuentaBancaria doTransactionAhorro(CuentaAhorro cuenta, Transaccion transaccion){
        switch (transaccion.getTipoTransaccion()){
            case "depositar":
                cuenta.depositar(transaccion.getMonto());
                break;
            case "retirar":
                cuenta.retirar(transaccion.getMonto());
                break;
            case "transferir":
                cuenta.transferir(transaccion.getTipoCuentaDestino(), transaccion.getMonto());
                break;
        }
        return cuenta;
    }

    public CuentaBancaria doTransactionCorriente(CuentaCorriente cuenta, Transaccion transaccion){
        switch (transaccion.getTipoTransaccion()){
            case "depositar":
                cuenta.depositar(transaccion.getMonto());
                break;
            case "retirar":
                cuenta.retirar(transaccion.getMonto());
                break;
            case "transferir":
                cuenta.transferir(transaccion.getTipoCuentaDestino(), transaccion.getMonto());
                break;
        }
        return cuenta;
    }
}
