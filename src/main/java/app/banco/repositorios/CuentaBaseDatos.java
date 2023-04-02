package app.banco.repositorios;

import app.banco.entidades.CuentaAhorro;
import app.banco.entidades.CuentaCorriente;
import app.banco.entidades.abstractas.CuentaBancaria;
import app.banco.repositorios.interfaces.RepositorioCRUD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaBaseDatos implements RepositorioCRUD {
    private String cadenaConexion;

    public CuentaBaseDatos(){
        try {
            DriverManager.registerDriver(new org.sqlite.JDBC());
            this.cadenaConexion = "jdbc:sqlite:banco.db";
        }catch (SQLException e){
            System.err.println("Error de conexión: " + e);
        }
    }

    @Override
    public Object Crear(Object objeto) {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {
            CuentaBancaria cuentaBancaria = (CuentaBancaria) objeto;

            String sentenciaSql =
                    "INSERT INTO CUENTAS (NUMERO_CUENTA, SALDO, TIPO_CUENTA, ID_USUARIO) "
                            + "VALUES('" + cuentaBancaria.getNumeroCuenta() + "', "
                            + "" + cuentaBancaria.getSaldo() + ", "
                            + "'" + cuentaBancaria.getTipoCuenta() + "', "
                            + "" + cuentaBancaria.getIdUsuario() + " " +
                            ");";

            Statement sentencia = conexion.createStatement();

            sentencia.execute(sentenciaSql);
        } catch (SQLException e) {
            return "Error de conexión: " + e;
        } catch (Exception e) {
            return "Error " + e.getMessage();
        }

        return "Transaccion creada con exito";
    }

    @Override
    public boolean Actualizar(Object id, Object saldo) {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {
            double saldoCuenta = (double) saldo;

            String sentenciaSql =
                    "UPDATE CUENTAS SET SALDO = ? WHERE ID = ?";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setDouble(1, saldoCuenta);
            sentencia.setInt(2, (int) id);

            sentencia.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e);
            return false;
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public Object ObtenerUno(Object numeroCuenta) {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {
            String numeroCuentaAEncontrar = (String) numeroCuenta;

            String sentenciaSql =
                    "SELECT * FROM CUENTAS "
                            + "WHERE NUMERO_CUENTA = ?;";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, numeroCuentaAEncontrar);
            ResultSet resultadoConsulta = sentencia.executeQuery();

            if (resultadoConsulta != null && resultadoConsulta.next()) {
                int id = resultadoConsulta.getInt("ID");
                String tipoCuenta = resultadoConsulta.getString("TIPO_CUENTA");

                CuentaBancaria usuarioEncontrado;

                if(tipoCuenta.equalsIgnoreCase("CuentaAhorro")){
                    usuarioEncontrado = new CuentaAhorro(
                            resultadoConsulta.getString("NUMERO_CUENTA"),
                            tipoCuenta,
                            resultadoConsulta.getInt("ID_USUARIO")
                    );
                } else {
                    usuarioEncontrado = new CuentaCorriente(
                            resultadoConsulta.getString("NUMERO_CUENTA"),
                            tipoCuenta,
                            resultadoConsulta.getInt("ID_USUARIO")
                    );
                }

                usuarioEncontrado.setSaldo(resultadoConsulta.getDouble("SALDO"));
                usuarioEncontrado.setId(id);

                return usuarioEncontrado;
            }
        } catch (SQLException e) {
            return "Error de conexión: " + e;
        } catch (Exception e) {
            return "Error " + e.getMessage();
        }

        return null;
    }

    @Override
    public List<?> ListarTodos() {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {
            ArrayList<CuentaBancaria> cuentas = new ArrayList<>();

            String sentenciaSql =
                    "SELECT * FROM CUENTAS";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            ResultSet resultadoConsulta = sentencia.executeQuery();

            if (resultadoConsulta != null ) {
                int id;
                while(resultadoConsulta.next()){
                    id = resultadoConsulta.getInt("ID");
                    String tipoCuenta = resultadoConsulta.getString("TIPO_CUENTA");

                    CuentaBancaria cuentaEncontrada;

                    if(tipoCuenta.equalsIgnoreCase("CuentaAhorro")){
                        cuentaEncontrada = new CuentaAhorro(
                                resultadoConsulta.getString("NUMERO_CUENTA"),
                                tipoCuenta,
                                resultadoConsulta.getInt("ID_USUARIO")
                        );
                    } else {
                        cuentaEncontrada = new CuentaCorriente(
                                resultadoConsulta.getString("NUMERO_CUENTA"),
                                tipoCuenta,
                                resultadoConsulta.getInt("ID_USUARIO")
                        );
                    }
                    cuentaEncontrada.setSaldo(resultadoConsulta.getDouble("SALDO"));
                    cuentaEncontrada.setId(id);

                    cuentas.add(cuentaEncontrada);
                }

                return cuentas;
            }
        } catch (SQLException e) {
            return null;
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    @Override
    public String Eliminar(Object cuenta) {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {
            CuentaBancaria cuentaAEliminar = (CuentaBancaria) cuenta;

            // * Eliminamos las transacciones
            String sentenciaEliminarTransaccionsSql =
                    "DELETE FROM TRANSACCIONES WHERE ID_CUENTA = ?;";
            PreparedStatement sentenciaEliminarTransacciones = conexion.prepareStatement(sentenciaEliminarTransaccionsSql);

            sentenciaEliminarTransacciones.setInt(1, cuentaAEliminar.getId());
            sentenciaEliminarTransacciones.executeUpdate();

            // * Eliminamos las cuentas
            String sentenciaEliminarCuentasSql =
                    "DELETE FROM CUENTAS WHERE ID = ?;";
            PreparedStatement sentenciaEliminarCuentas = conexion.prepareStatement(sentenciaEliminarCuentasSql);
            sentenciaEliminarCuentas.setInt(1, cuentaAEliminar.getId());
            sentenciaEliminarCuentas.executeUpdate();

            return "Cuenta Eliminada";

        } catch (SQLException e) {
            return "Error de conexión: " + e;
        } catch (Exception e) {
            return "Error " + e.getMessage();
        }
    }
}
