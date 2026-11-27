package servicios;

import excepciones.ReglaNegocioException;
import java.time.LocalDateTime;
import java.util.List;
import modelo.EstadoCine;
import modelo.Funcion;
import modelo.Pelicula;

/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/

public class PeliculaService {

    private final EstadoCine estado;

    public PeliculaService(EstadoCine estado) {
        this.estado = estado;
    }

    public Pelicula registrarPelicula(String codigo,
            String titulo, int duracionMinutos)
            throws ReglaNegocioException {

        Pelicula pelicula = new Pelicula(
                codigo, titulo, duracionMinutos);

        estado.getPeliculas().agregar(pelicula);

        return pelicula;
    }

    public void modificarPelicula(String codigo,
            String titulo, int duracionMinutos)
            throws ReglaNegocioException {

        Pelicula pelicula = buscarPelicula(codigo);

        if (duracionMinutos <= 0) {
            throw new ReglaNegocioException(
                    "La duración debe ser mayor que cero.");
        }

        if (pelicula.getDuracionMinutos() != duracionMinutos) {

            validarNuevaDuracion(pelicula, duracionMinutos);
        }

        pelicula.modificar(titulo, duracionMinutos);
    }

    private void validarNuevaDuracion(Pelicula pelicula,
            int nuevaDuracion)
            throws ReglaNegocioException {

        List<Funcion> funciones = estado.getFunciones().listar();

        for (Funcion funcion : funciones) {

            if (funcion.getPelicula().equals(pelicula)
                    && !funcion.comenzo()) {

                LocalDateTime nuevoFin =
                        funcion.getFechaHoraInicio()
                                .plusMinutes(nuevaDuracion);

                for (Funcion otra : funciones) {

                    if (funcion.equals(otra)) {
                        continue;
                    }

                    if (!funcion.getSala().equals(otra.getSala())) {
                        continue;
                    }

                    LocalDateTime finOtra;

                    if (otra.getPelicula().equals(pelicula)) {

                        finOtra = otra.getFechaHoraInicio()
                                .plusMinutes(nuevaDuracion);

                    } else {

                        finOtra = otra.getFechaHoraFinalizacion();
                    }

                    boolean condicionUno =
                            funcion.getFechaHoraInicio()
                                    .isBefore(finOtra);

                    boolean condicionDos =
                            nuevoFin.isAfter(
                                    otra.getFechaHoraInicio());

                    if (condicionUno && condicionDos) {

                        throw new ReglaNegocioException(
                                "La nueva duración genera "
                                + "una superposición de funciones.");
                    }
                }
            }
        }
    }

    public void desactivarPelicula(String codigo)
            throws ReglaNegocioException {

        Pelicula pelicula = buscarPelicula(codigo);

        pelicula.desactivar();
    }

    public Pelicula buscarPelicula(String codigo)
            throws ReglaNegocioException {

        if (codigo == null || codigo.trim().isEmpty()) {

            throw new ReglaNegocioException(
                    "El código de la película es obligatorio.");
        }

        Pelicula pelicula =
                estado.getPeliculas().buscar(codigo.trim());

        if (pelicula == null) {

            throw new ReglaNegocioException(
                    "La película no fue encontrada.");
        }

        return pelicula;
    }

    public List<Pelicula> listarPeliculas() {

        return estado.getPeliculas().listar();
    }
}