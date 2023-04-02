package app.banco.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class MapearRespuesta {

    public static String ObtenerJson(Object objecto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        if(objecto == null){
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "usuario no encontrado");
            return mapper.writeValueAsString(respuesta);
        }

        return mapper.writeValueAsString(objecto);
    }

    public static String Exito(String mensaje) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", mensaje);
        return mapper.writeValueAsString(respuesta);
    }

    public static String Error(String mensaje) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", mensaje);
        return mapper.writeValueAsString(respuesta);
    }
}
