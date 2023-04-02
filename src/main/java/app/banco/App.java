package app.banco;

import app.banco.controladores.ControladorCuenta;
import app.banco.controladores.ControladorTransaccion;
import app.banco.controladores.ControladorUsuario;
import app.banco.utils.GenerateDB;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class App {
    public static void main(String[] args) {
        // Descomentar esta linea para generar la base de datos
        //GenerateDB.generar();

        Server server = new Server(3500);
        server.setHandler(new DefaultHandler());

        ServletContextHandler context = new ServletContextHandler();

        context.setContextPath("/api/");
        context.addServlet(ControladorUsuario.class, "/usuario/*");
        context.addServlet(ControladorCuenta.class, "/cuenta/*");
        context.addServlet(ControladorTransaccion.class, "/transaccion/*");

        server.setHandler(context);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}