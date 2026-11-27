package servicios;

import excepciones.ReglaNegocioException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import modelo.EstadoCine;
import modelo.Funcion;
import modelo.Pelicula;
import modelo.Sala;

public class FuncionService {

    private final EstadoCine estado;

    public FuncionService(EstadoCine estado) {
        this.estado = estado;
    }

    public Funcion registrarFuncion(String codigo,
            String codigoPelicula, String codigoSala,
            LocalDateTime fechaHoraInicio, double precioBase)
            throws ReglaNegocioException {

        Pelicula pelicula =
                estado.getPeliculas().buscar(codigoPelicula);

        if (pelicula == null) {
            throw new ReglaNegocioException(
                    "La película no fue encontrada.");
        }

        Sala sala =
                estado.getSalas().buscar(codigoSala);

        if (sala == null) {
            throw new ReglaNegocioException(
                    "La sala no fue encontrada.");
        }

        Funcion funcion = new Funcion(
                codigo, pelicula, sala,
                fechaHoraInicio, precioBase);

        validarSuperposicion(funcion);

        estado.getFunciones().agregar(funcion);

        return funcion;
    }

    private void validarSuperposicion(Funcion funcion)
            throws ReglaNegocioException {

        List<Funcion> funciones =
                estado.getFunciones().listar();

        for (Funcion otra : funciones) {

            if (funcion.seSuperponeCon(otra)) {

                throw new ReglaNegocioException(
                        "La función se superpone con otra "
                        + "función programada en la misma sala.");
            }
        }
    }

    public Funcion buscarFuncion(String codigo)
            throws ReglaNegocioException {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new ReglaNegocioException(
                    "El código de la función es obligatorio.");
        }

        Funcion funcion =
                estado.getFunciones().buscar(codigo.trim());

        if (funcion == null) {
            throw new ReglaNegocioException(
                    "La función no fue encontrada.");
        }

        return funcion;
    }

    public List<Funcion> listarFunciones() {

        return estado.getFunciones().listar();
    }

    public List<Funcion> buscarPorPelicula(String codigoPelicula)
            throws ReglaNegocioException {

        Pelicula pelicula =
                estado.getPeliculas().buscar(codigoPelicula);

        if (pelicula == null) {
            throw new ReglaNegocioException(
                    "La película no fue encontrada.");
        }

        List<Funcion> resultado = new ArrayList<>();

        for (Funcion funcion : listarFunciones()) {

            if (funcion.getPelicula().equals(pelicula)) {

                resultado.add(funcion);
            }
        }

        return resultado;
    }

    public List<Funcion> buscarPorFecha(LocalDate fecha)
            throws ReglaNegocioException {

        if (fecha == null) {
            throw new ReglaNegocioException(
                    "La fecha es obligatoria.");
        }

        List<Funcion> resultado = new ArrayList<>();

        for (Funcion funcion : listarFunciones()) {

            LocalDate fechaFuncion =
                    funcion.getFechaHoraInicio().toLocalDate();

            if (fechaFuncion.equals(fecha)) {

                resultado.add(funcion);
            }
        }

        return resultado;
    }

    public void modificarPrecio(String codigo, double precioBase)
            throws ReglaNegocioException {

        Funcion funcion = buscarFuncion(codigo);

        funcion.modificarPrecio(precioBase);
    }
}