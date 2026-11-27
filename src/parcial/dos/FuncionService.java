package parcial.dos;

import excepciones.ReglaNegocioException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FuncionService {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final EstadoCine estado;

    public FuncionService(EstadoCine estado) {
        this.estado = estado;
    }

    
    public Funcion programarFuncion(String codigoPelicula, String codigoSala,
                                    LocalDateTime fechaHoraInicio, double precioBase)
            throws ReglaNegocioException {
        Pelicula pelicula = estado.getPeliculas().buscar(codigoPelicula);
        if (pelicula == null) {
            throw new ReglaNegocioException("La película seleccionada no existe.");
        }
        if (!pelicula.isActiva()) {
            throw new ReglaNegocioException(
                    "La película está desactivada y no admite nuevas funciones.");
        }
        Sala sala = estado.getSalas().buscar(codigoSala);
        if (sala == null) {
            throw new ReglaNegocioException("La sala seleccionada no existe.");
        }

        Funcion nueva = new Funcion(generarCodigo(), pelicula, sala, fechaHoraInicio, precioBase);
        validarSinSuperposicion(nueva);

        estado.getFunciones().agregar(nueva);
        return nueva;
    }

    
    public Funcion buscarFuncion(String codigo) {
        return estado.getFunciones().buscar(codigo);
    }

    
    public List<Funcion> listarFunciones() {
        List<Funcion> funciones = estado.getFunciones().listar();
        Collections.sort(funciones, Comparator
                .comparing(Funcion::getFechaHoraInicio)
                .thenComparing(Funcion::getCodigo));
        return funciones;
    }

    
    public List<Funcion> buscarCartelera(String codigoPelicula, LocalDate fecha) {
        boolean filtraPelicula = codigoPelicula != null && !codigoPelicula.trim().isEmpty();
        List<Funcion> resultado = new ArrayList<>();

        for (Funcion funcion : listarFunciones()) {
            boolean noComenzo = !funcion.comenzo();
            boolean peliculaActiva = funcion.getPelicula().isActiva();
            boolean coincidePelicula = !filtraPelicula
                    || funcion.getPelicula().getCodigo().equals(codigoPelicula.trim());
            boolean coincideFecha = fecha == null
                    || funcion.getFechaHoraInicio().toLocalDate().equals(fecha);

            if (noComenzo && peliculaActiva && coincidePelicula && coincideFecha) {
                resultado.add(funcion);
            }
        }
        return resultado;
    }

    
    private void validarSinSuperposicion(Funcion nueva) throws ReglaNegocioException {
        for (Funcion existente : estado.getFunciones().listar()) {
            if (nueva.seSuperponeCon(existente)) {
                throw new ReglaNegocioException("La sala ya tiene una función entre las "
                        + existente.getFechaHoraInicio().format(HORA) + " y las "
                        + existente.getFechaHoraFinalizacion().format(HORA) + ".");
            }
        }
    }

    
    private String generarCodigo() {
        int numero = estado.getFunciones().cantidad() + 1;
        String codigo = String.format("F%03d", numero);
        while (estado.getFunciones().existe(codigo)) {
            numero++;
            codigo = String.format("F%03d", numero);
        }
        return codigo;
    }
}