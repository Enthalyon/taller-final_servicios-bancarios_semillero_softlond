package app.banco.servicios;

import app.banco.entidades.CuentaAhorro;
import app.banco.entidades.CuentaCorriente;
import app.banco.entidades.Usuario;
import app.banco.entidades.abstractas.CuentaBancaria;
import app.banco.repositorios.CuentaBaseDatos;
import app.banco.repositorios.UsuarioBaseDatos;
import app.banco.repositorios.interfaces.RepositorioCRUD;
import app.banco.servicios.interfaces.Servicio;

import java.util.List;
import java.util.Map;

public class ServicioCuenta implements Servicio {
    private RepositorioCRUD repositorioCuenta;
    private RepositorioCRUD repositorioUsuario;

    public ServicioCuenta() {
        repositorioCuenta = new CuentaBaseDatos();
        repositorioUsuario = new UsuarioBaseDatos();
    }

    @Override
    public Object Crear(Map datosCuenta) {
        String numeroCuenta = (String) datosCuenta.get("numeroCuenta");
        String cedula = (String) datosCuenta.get("cedula");
        String TipoCuenta = (String) datosCuenta.get("TipoCuenta");

        if(numeroCuenta == null || numeroCuenta == "" ||
                cedula == null || cedula == "" ||
                TipoCuenta == null || TipoCuenta == "")
        {
            throw new RuntimeException("No se enviaron todos los campos");
        }

        if(!TipoCuenta.equals("CuentaAhorro") && !TipoCuenta.equals("CuentaCorriente")){
            throw new RuntimeException("El tipo de cuenta indicado no es valido, deber ser " +
                    "CuentaAhorro o CuentaCorriente");
        }

        Usuario usuario = (Usuario)repositorioUsuario.ObtenerUno(cedula);

        if (usuario == null){
            throw new RuntimeException("El usuario con la cedula ingresada no existe");
        }

        CuentaBancaria cuenta;
        if(TipoCuenta.equals("CuentaAhorro")){
            cuenta = new CuentaAhorro(numeroCuenta, TipoCuenta, usuario.getId());
        } else {
            cuenta = new CuentaCorriente(numeroCuenta, TipoCuenta, usuario.getId());
        }

        return repositorioCuenta.Crear(cuenta);
    }

    @Override
    public Object ObtenerUno(Object id) {
        return repositorioCuenta.ObtenerUno(id);
    }

    @Override
    public List<?> ListarTodos() {
        return repositorioCuenta.ListarTodos();
    }

    @Override
    public String Eliminar(Object numeroCuenta) {

        CuentaBancaria cuentaAEliminar = (CuentaBancaria) repositorioCuenta.ObtenerUno(numeroCuenta);

        if(cuentaAEliminar == null){
            throw new RuntimeException("La cuenta indicada no existe");
        }

        return repositorioCuenta.Eliminar(cuentaAEliminar);
    }
}
