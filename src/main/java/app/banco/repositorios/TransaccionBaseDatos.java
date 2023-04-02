package app.banco.repositorios;
import app.banco.entidades.Transaccion;
import app.banco.repositorios.interfaces.RepositorioCRUD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaccionBaseDatos implements RepositorioCRUD {
    private String cadenaConexion;

    public TransaccionBaseDatos(){
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
            Transaccion transaccion = (Transaccion) objeto;

            String sentenciaSql =
                    "INSERT INTO TRANSACCIONES (FECHA, HORA, TIPO_TRANSACCION, MONTO, ID_CUENTA, TIPO_CUENTA_DESTINO) "
                            + "VALUES('" + transaccion.getFecha() + "', "
                            + "'" + transaccion.getHora() + "', "
                            + "'" + transaccion.getTipoTransaccion() + "', "
                            + "" + transaccion.getMonto() + ", "
                            + "" + transaccion.getIdCuenta() + ", "
                            + "'" + transaccion.getTipoCuentaDestino() + "' " +
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
    public Object ObtenerUno(Object idTransaccion) {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {
            int idTransaccionAEncontrar = Integer.parseInt((String)idTransaccion);

            String sentenciaSql =
                    "SELECT * FROM TRANSACCIONES "
                            + "WHERE ID = ?";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idTransaccionAEncontrar);
            ResultSet resultadoConsulta = sentencia.executeQuery();

            if (resultadoConsulta != null && resultadoConsulta.next()) {
                int id = resultadoConsulta.getInt("ID");

                Transaccion transaccion;

                transaccion = new Transaccion(
                        resultadoConsulta.getString("TIPO_TRANSACCION"),
                        resultadoConsulta.getDouble("MONTO"),
                        resultadoConsulta.getInt("ID_CUENTA"),
                        resultadoConsulta.getString("TIPO_CUENTA_DESTINO")
                );

                transaccion.setId(id);

                return transaccion;
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
            ArrayList<Transaccion> transacciones = new ArrayList<>();

            String sentenciaSql =
                    "SELECT * FROM TRANSACCIONES";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            ResultSet resultadoConsulta = sentencia.executeQuery();

            if (resultadoConsulta != null ) {
                int id;
                while(resultadoConsulta.next()){
                    id = resultadoConsulta.getInt("ID");
                    Transaccion transaccion;

                    transaccion = new Transaccion(
                            resultadoConsulta.getString("TIPO_TRANSACCION"),
                            resultadoConsulta.getDouble("MONTO"),
                            resultadoConsulta.getInt("ID_CUENTA"),
                            resultadoConsulta.getString("TIPO_CUENTA_DESTINO")
                    );

                    transaccion.setId(id);

                    transacciones.add(transaccion);
                }

                return transacciones;
            }
        } catch (SQLException e) {
            return null;
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    @Override
    public String Eliminar(Object transaccion) {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {
            Transaccion transaccionAEliminar = (Transaccion) transaccion;

            // * Eliminamos las transacciones
            String sentenciaEliminarTransaccionsSql =
                    "DELETE FROM TRANSACCIONES WHERE ID = ?;";
            PreparedStatement sentenciaEliminarTransacciones = conexion.prepareStatement(sentenciaEliminarTransaccionsSql);

            sentenciaEliminarTransacciones.setInt(1, transaccionAEliminar.getId());
            sentenciaEliminarTransacciones.executeUpdate();

            return "transaccion Eliminada";

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
