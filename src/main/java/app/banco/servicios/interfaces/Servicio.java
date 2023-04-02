package app.banco.servicios.interfaces;

import java.util.List;
import java.util.Map;

public interface Servicio {
    Object Crear(Map objeto);
    Object ObtenerUno(Object id);
    List<?> ListarTodos();
    String Eliminar(Object id);
}
