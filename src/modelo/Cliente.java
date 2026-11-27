package modelo;

import repositorios.Identificable;
import excepciones.ReglaNegocioException;
import java.io.Serializable;
import java.util.Locale;

/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/
public class Cliente implements Serializable, Identificable{
    
    private static final long serialVersionUID = 1L;

    private final String identificacion;
    private String nombre;

    public Cliente(String identificacion, String nombre) throws ReglaNegocioException {
        if (identificacion == null || identificacion.trim().isEmpty()){
            throw new ReglaNegocioException(
                    "La identificación del cliente es obligatoria."
            );
        }
        
        if (nombre == null || nombre.trim().isEmpty()){
            throw new ReglaNegocioException(
            "El nombre del cliente es obligatorio."
            );
        }
        this.identificacion = identificacion.trim()
        .toUpperCase(Locale.ROOT);
        this.nombre = nombre.trim();
    }

    public String getIdentificacion() {
        return identificacion;
    }
    
    @Override
    public String getCodigo(){
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (!(obj instanceof Cliente)){
            return false;
        }
        Cliente otro = (Cliente) obj;
        
        boolean mismaIdentificacion = identificacion.equals(otro.identificacion);
        return mismaIdentificacion;
    }

    @Override
    public int hashCode() {
        return identificacion.hashCode();
    }

}