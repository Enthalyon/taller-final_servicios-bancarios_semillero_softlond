package app.banco.repositorios.interfaces;

import java.util.List;

public interface RepositorioCRUD {
    Object Crear(Object objeto);
    Object ObtenerUno(Object id);
    List<?> ListarTodos();
    String Eliminar(Object id);
    boolean Actualizar(Object id, Object data);
}
