package modelo;

import java.util.Locale;
import repositorios.Identificable;
import excepciones.ReglaNegocioException;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/
public class Funcion implements Serializable, Identificable {
    
    private static final long serialVersionUID = 1L;

    private final String codigo;
    private Pelicula pelicula;
    private Sala sala;
    private LocalDateTime fechaHoraInicio;
    private double precioBase;

    public Funcion(String codigo, Pelicula pelicula, Sala sala, LocalDateTime fechaHoraInicio, double precioBase) throws ReglaNegocioException {
        if (codigo == null || codigo.trim().isEmpty()){
            throw new ReglaNegocioException(
            "El código de la función es obligatorio."
            );
        }
        if (pelicula == null){
            throw new ReglaNegocioException(
            "La película es obligatoria."
            );
        }
        if (sala == null){
            throw new ReglaNegocioException(
            "La sala es obligatoria."
            );
        }
        if (fechaHoraInicio == null){
            throw new ReglaNegocioException(
            "La fecha y hora son obligatorias."
            );
        }
        if (!fechaHoraInicio.isAfter(LocalDateTime.now())){
            throw new ReglaNegocioException(
            "La función debe programarse en el futuro."
            );
        }
        if (precioBase <= 0 || !Double.isFinite(precioBase)){
            throw new ReglaNegocioException(
            "El precio debe ser válido y mayor que cero."
            );
        }
        this.codigo = codigo.trim().toUpperCase(Locale.ROOT);
        this.pelicula = pelicula;
        this.sala = sala;
        this.fechaHoraInicio = fechaHoraInicio;
        this.precioBase = precioBase;
    }

    @Override
    public String getCodigo() {
        return codigo;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public LocalDateTime getFechaHoraFinalizacion() {
        int duracion = pelicula.getDuracionMinutos();
        
        LocalDateTime fechaHoraFinalizacion = fechaHoraInicio.plusMinutes(duracion);
        
        return fechaHoraFinalizacion;
    }

    public boolean comenzo() {
        LocalDateTime ahora = LocalDateTime.now();
        
        boolean comenzo = !fechaHoraInicio.isAfter(ahora);
        return comenzo;
    }
    public void modificarPrecio(double precioBase) throws ReglaNegocioException {
        if (precioBase <= 0 || !Double.isFinite(precioBase)){
            throw new ReglaNegocioException(
                    "El precio debe ser válido y mayor que cero."
            );
        }
        this.precioBase = precioBase;
    }

    public boolean seSuperponeCon(Funcion otra) {
        if (!sala.equals(otra.getSala())){
            return false;
        }
        
        boolean condicionUno = fechaHoraInicio.isBefore(otra.getFechaHoraFinalizacion());
        
        boolean condicionDos = getFechaHoraFinalizacion().isAfter(otra.getFechaHoraInicio());
        
        return condicionUno && condicionDos;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (!(obj instanceof Funcion)){
            return false;
        }
        
        Funcion otra = (Funcion) obj;
        
        boolean mismoCodigo = codigo.equals(otra.codigo);
        
        return mismoCodigo;
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

}