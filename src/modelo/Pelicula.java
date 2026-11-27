package modelo;
import repositorios.Identificable;
import excepciones.ReglaNegocioException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Locale;

/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/
public class Pelicula implements Serializable, Identificable {

    private static final long serialVersionUID = 1L;
    private String codigo;
    private String titulo;
    private int duracionMinutos;
    private boolean activa;

    public Pelicula(String codigo, String titulo, int duracionMinutos) throws ReglaNegocioException{
        
        if (codigo == null || codigo.trim().isEmpty()){
            throw new ReglaNegocioException(
            "El código de la película es obligatorio.");
        }
        if (titulo == null|| titulo.trim().isEmpty()){
            throw new ReglaNegocioException(
                    "El título de la película es obligatorio."
            );
        }
        if (duracionMinutos <= 0){
            throw new ReglaNegocioException(
                    "La duración debe ser mayor que cero."
            );
        }
        
        this.codigo = codigo.trim().toUpperCase(Locale.ROOT);
        this.titulo = titulo.trim();
        this.duracionMinutos = duracionMinutos;
        this.activa = true; 
    }
    @Override
    public String toString() {
        return codigo + " - " + titulo;
    }

    @Override
    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public boolean isActiva() {
        return activa;
    }

    public void modificar(String titulo, int duracionMinutos) throws ReglaNegocioException {
        if (titulo == null || titulo.trim().isEmpty()){
            throw new ReglaNegocioException(
                    "El título de la película es obligatorio."
            );
        }
        if (duracionMinutos <= 0){
            throw new ReglaNegocioException(
            "La duración debe ser mayor que cero."
            );
        }
        this.titulo = titulo.trim();
        this.duracionMinutos = duracionMinutos;
    }

    public void desactivar() {
        this.activa = false;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
        return true;
        }
        if (!(obj instanceof Pelicula)){
            return false;
        }
        
        Pelicula otra = (Pelicula) obj;
        
        boolean mismoCodigo = codigo.equals(otra.codigo);
        
        return mismoCodigo;
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }
}