package app.banco.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GenerateDB {
    public static void generar(){
        try {
            DriverManager.registerDriver(new org.sqlite.JDBC());
            String cadenaConexion = "jdbc:sqlite:banco.db";
            Connection conexion = DriverManager.getConnection(cadenaConexion);
            Statement sentencia = conexion.createStatement();

            String usuarioSql = "CREATE TABLE USUARIOS(\n" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "NOMBRE TEXT NOT NULL,\n" +
                    "APELLIDO TEXT NOT NULL,\n" +
                    "CEDULA TEXT NOT NULL UNIQUE\n" +
                    ");\n";

            String cuentasSql = "CREATE TABLE CUENTAS(\n" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "NUMERO_CUENTA TEXT NOT NULL UNIQUE,\n" +
                    "SALDO REAL NOT NULL,\n" +
                    "TIPO_CUENTA TEXT NOT NULL,\n" +
                    "ID_USUARIO INTEGER NOT NULL,\n" +
                    "FOREIGN KEY(ID_USUARIO) REFERENCES USUARIOS(ID)\n" +
                    ");\n";

            String transaccionesSql = "CREATE TABLE TRANSACCIONES(\n" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "FECHA TEXT NOT NULL,\n" +
                    "HORA TEXT NOT NULL,\n" +
                    "TIPO_TRANSACCION TEXT NOT NULL,\n" +
                    "MONTO REAL NOT NULL,\n" +
                    "ID_CUENTA INTEGER NOT NULL,\n" +
                    "TIPO_CUENTA_DESTINO TEXT,\n" +
                    "FOREIGN KEY(ID_CUENTA) REFERENCES CUENTAS(ID)\n" +
                    ");\n\n";

            sentencia.execute(usuarioSql);
            sentencia.execute(cuentasSql);
            sentencia.execute(transaccionesSql);

        } catch (SQLException e) {
            System.err.println("Error de conexión con la base de datos: " + e);
        }
    }
}
