package app.banco.servicios;

import app.banco.entidades.Usuario;
import app.banco.repositorios.UsuarioBaseDatos;
import app.banco.repositorios.interfaces.RepositorioCRUD;
import app.banco.servicios.interfaces.Servicio;

import java.util.List;
import java.util.Map;

public class ServicioUsuario implements Servicio {
    private RepositorioCRUD repositorio;

    public ServicioUsuario() {
        repositorio = new UsuarioBaseDatos();
    }

    @Override
    public Object Crear(Map datosUsuario) {
        String nombre = (String) datosUsuario.get("nombre");
        String apellido = (String) datosUsuario.get("apellido");
        String cedula = (String) datosUsuario.get("cedula");

        if(nombre == null || nombre == "" ||
                apellido == null || apellido == "" ||
                cedula == null || cedula == "")
        {
            throw new RuntimeException("No se enviaron todos los campos");
        }

        Usuario newPerson = new Usuario(nombre, apellido, cedula);

        return repositorio.Crear(newPerson);
    }

    @Override
    public Object ObtenerUno(Object id) {
        return repositorio.ObtenerUno(id);
    }

    @Override
    public List<?> ListarTodos() {
        return repositorio.ListarTodos();
    }

    @Override
    public String Eliminar(Object cedula) {

        Usuario usuarioAEliminar = (Usuario) repositorio.ObtenerUno(cedula);

        if(usuarioAEliminar == null){
            throw new RuntimeException("El usuario indicado no existe");
        }

        return repositorio.Eliminar(usuarioAEliminar);
    }
}
