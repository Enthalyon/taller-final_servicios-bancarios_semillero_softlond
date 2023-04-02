package app.banco.repositorios;

import app.banco.entidades.Usuario;
import app.banco.repositorios.interfaces.RepositorioCRUD;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioBaseDatos implements RepositorioCRUD {
    private String cadenaConexion;

    public UsuarioBaseDatos(){
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
            Usuario usuario = (Usuario) objeto;

            String sentenciaSql =
                    "INSERT INTO USUARIOS (NOMBRE, APELLIDO, CEDULA) "
                    + "VALUES('" + usuario.getNombre() + "', "
                    + "'" + usuario.getApellido() + "', "
                    + "'" + usuario.getCedula() + "' " +
                    ");";

            Statement sentencia = conexion.createStatement();

            sentencia.execute(sentenciaSql);
        } catch (SQLException e) {
            return "Error de conexión: " + e;
        } catch (Exception e) {
            return "Error " + e.getMessage();
        }

        return "Usuario creado con exito";
    }

    @Override
    public Object ObtenerUno(Object cedula) {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {
            String CedulaUsuario = (String) cedula;

            String sentenciaSql =
                    "SELECT * FROM USUARIOS "
                            + "WHERE CEDULA = ?";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, CedulaUsuario);
            ResultSet resultadoConsulta = sentencia.executeQuery();

            if (resultadoConsulta != null && resultadoConsulta.next()) {
                int id = resultadoConsulta.getInt("ID");

                Usuario usuarioEncontrado = new Usuario(
                        resultadoConsulta.getString("NOMBRE"),
                        resultadoConsulta.getString("APELLIDO"),
                        resultadoConsulta.getString("CEDULA")
                );
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
            ArrayList<Usuario> usuarios = new ArrayList<>();

            String sentenciaSql =
                    "SELECT * FROM USUARIOS";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            ResultSet resultadoConsulta = sentencia.executeQuery();

            if (resultadoConsulta != null ) {
                int id;
                while(resultadoConsulta.next()){
                    id = resultadoConsulta.getInt("ID");
                    Usuario usuarioEncontrado = new Usuario(
                            resultadoConsulta.getString("NOMBRE"),
                            resultadoConsulta.getString("APELLIDO"),
                            resultadoConsulta.getString("CEDULA")
                    );
                    usuarioEncontrado.setId(id);
                    usuarios.add(usuarioEncontrado);
                }

                return usuarios;
            }
        } catch (SQLException e) {
            return null;
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    @Override
    public String Eliminar(Object usuario) {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {
            Usuario usuarioAEliminar = (Usuario) usuario;

            // * Obtención de Cuentas
            String sentenciaCuentasSql =
                    "SELECT ID FROM CUENTAS WHERE ID_USUARIO = ?";
            PreparedStatement sentenciaCuentas = conexion.prepareStatement(sentenciaCuentasSql);
            sentenciaCuentas.setInt(1, usuarioAEliminar.getId());
            ResultSet resultadoConsultaCuentas = sentenciaCuentas.executeQuery();

            // * Eliminamos las transacciones
            int tempIdCuenta = 0, numeroCuentasEliminar = 0;
            String sentenciaEliminarTransaccionsSql =
                    "DELETE FROM TRANSACCIONES WHERE ID_CUENTA = ?";
            PreparedStatement sentenciaEliminarTransacciones;

            if(resultadoConsultaCuentas != null){
                while(resultadoConsultaCuentas.next()){
                    tempIdCuenta = resultadoConsultaCuentas.getInt("ID");
                    if(tempIdCuenta != 0){
                        numeroCuentasEliminar++;
                        sentenciaEliminarTransacciones = conexion.prepareStatement(sentenciaEliminarTransaccionsSql);
                        sentenciaEliminarTransacciones.setInt(1, tempIdCuenta);
                        sentenciaEliminarTransacciones.executeUpdate();
                    }
                }
            }

            // * Eliminamos las cuentas
            if(numeroCuentasEliminar > 0){
                String sentenciaEliminarCuentasSql =
                        "DELETE FROM CUENTAS WHERE ID_USUARIO = ?";
                PreparedStatement sentenciaEliminarCuentas = conexion.prepareStatement(sentenciaEliminarCuentasSql);
                sentenciaEliminarCuentas.setInt(1, usuarioAEliminar.getId());
                sentenciaEliminarCuentas.executeUpdate();
            }

            // * Eliminación de usuario
            String sentenciaSql =
                    "DELETE FROM USUARIOS WHERE ID = ?";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, usuarioAEliminar.getId());
            sentencia.executeUpdate();

            return "Usuario Eliminado";

        } catch (SQLException e) {
            return "Error de conexión: " + e;
        } catch (Exception e) {
            return "Error " + e.getMessage();
        }
    }

    @Override
    public boolean Actualizar(Object id, Object data) {
        return false;
    }
}
