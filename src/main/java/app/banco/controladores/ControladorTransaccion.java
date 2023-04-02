package app.banco.controladores;

import app.banco.entidades.Transaccion;
import app.banco.entidades.Usuario;
import app.banco.servicios.ServicioTransaccion;
import app.banco.servicios.ServicioUsuario;
import app.banco.servicios.interfaces.Servicio;
import app.banco.utils.MapearRespuesta;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.management.InvalidAttributeValueException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ControladorTransaccion extends HttpServlet {
    private Servicio servicioTransaccion;
    private ObjectMapper mapper;

    public ControladorTransaccion() {
        servicioTransaccion = new ServicioTransaccion();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
            try {
                switch (request.getPathInfo()) {
                    case "/obtener-uno":
                        String idTransaccion = request.getParameter("idTransaccion");
                        if(idTransaccion == null || idTransaccion.equals("")){
                            throw new InvalidAttributeValueException("El parametro idTransaccion es obligatorio");
                        }

                        Transaccion transaccion = (Transaccion)servicioTransaccion.ObtenerUno(idTransaccion);

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("application/json");
                        response.getWriter().println(MapearRespuesta.ObtenerJson(transaccion));
                        break;
                    case "/listar-todos":
                        ArrayList<Transaccion> transacciones = (ArrayList<Transaccion>)servicioTransaccion.ListarTodos();

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("application/json");
                        response.getWriter().println(MapearRespuesta.ObtenerJson(transacciones));
                        break;
                    default:
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encuentra el recurso");
                        break;
                }
            } catch (Exception e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().println(MapearRespuesta.Error(e.getMessage()));
            }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        if(request.getContentType().equals("application/json")){
            try {
                switch (request.getPathInfo()) {
                    case "/crear":
                        Map<String, Object> transaccionMap = mapper.readValue(request.getInputStream(), HashMap.class);
                        String createdResponse = (String)servicioTransaccion.Crear(transaccionMap);

                        response.setStatus(HttpServletResponse.SC_CREATED);
                        response.setContentType("application/json");
                        response.getWriter().println(MapearRespuesta.Exito(createdResponse));
                        break;
                    default:
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encuentra el recurso");
                        break;
                }
            } catch (Exception e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().println(MapearRespuesta.Error(e.getMessage()));
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            response.setContentType("application/json");
            response.getWriter().println(MapearRespuesta.Error("El contenido debe ser JSON"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
            try {
                switch (request.getPathInfo()) {
                    case "/eliminar":
                        String idTransaccion = request.getParameter("idTransaccion");
                        if(idTransaccion == null || idTransaccion.equals("")){
                            throw new InvalidAttributeValueException("El parametro idTransaccion es obligatorio");
                        }

                        String respuestaEliminacion = servicioTransaccion.Eliminar(idTransaccion);

                        response.setStatus(HttpServletResponse.SC_CREATED);
                        response.setContentType("application/json");
                        response.getWriter().println(MapearRespuesta.Exito(respuestaEliminacion));
                        break;
                    default:
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encuentra el recurso");
                        break;
                }
            } catch (Exception e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().println(MapearRespuesta.Error(e.getMessage()));
            }
    }
}
