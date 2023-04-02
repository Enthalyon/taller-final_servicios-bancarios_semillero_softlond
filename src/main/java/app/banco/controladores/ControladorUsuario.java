package app.banco.controladores;

import app.banco.entidades.Usuario;
import app.banco.servicios.ServicioUsuario;
import app.banco.servicios.interfaces.Servicio;
import app.banco.utils.MapearRespuesta;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ControladorUsuario extends HttpServlet {
    private Servicio servicioUsuario;
    private ObjectMapper mapper;

    public ControladorUsuario() {
        servicioUsuario = new ServicioUsuario();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
            try {
                switch (request.getPathInfo()) {
                    case "/obtener-uno":
                        String cedula = request.getParameter("cedula");

                        Usuario usuario = (Usuario)servicioUsuario.ObtenerUno(cedula);

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("application/json");
                        response.getWriter().println(MapearRespuesta.ObtenerJson(usuario));
                        break;
                    case "/listar-todos":
                        ArrayList<Usuario> usuarios = (ArrayList<Usuario>)servicioUsuario.ListarTodos();

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("application/json");
                        response.getWriter().println(MapearRespuesta.ObtenerJson(usuarios));
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
                        Map<String, Object> usuarioMap = mapper.readValue(request.getInputStream(), HashMap.class);
                        String createdResponse = (String)servicioUsuario.Crear(usuarioMap);

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
                        String cedula = request.getParameter("cedula");

                        String respuestaEliminacion = servicioUsuario.Eliminar(cedula);

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
