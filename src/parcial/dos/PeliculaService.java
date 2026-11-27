package parcial.dos;

import excepciones.ReglaNegocioException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PeliculaService {

    private final EstadoCine estado;

    public PeliculaService(EstadoCine estado) {
        this.estado = estado;
    }

    
    public Pelicula registrarPelicula(String titulo, int duracionMinutos)
            throws ReglaNegocioException {
        Pelicula pelicula = new Pelicula(generarCodigo(), titulo, duracionMinutos);
        estado.getPeliculas().agregar(pelicula);
        return pelicula;
    }

    
    public void modificarPelicula(String codigo, String titulo, int duracionMinutos)
            throws ReglaNegocioException {
        Pelicula pelicula = obtenerPelicula(codigo);
        String tituloAnterior = pelicula.getTitulo();
        int duracionAnterior = pelicula.getDuracionMinutos();

        pelicula.modificar(titulo, duracionMinutos); 

        try {
            verificarSinSuperposiciones(pelicula);
        } catch (ReglaNegocioException e) {
            pelicula.modificar(tituloAnterior, duracionAnterior); 
            throw e;
        }
    }

    
    public void desactivarPelicula(String codigo) throws ReglaNegocioException {
        Pelicula pelicula = obtenerPelicula(codigo);
        if (!pelicula.isActiva()) {
            throw new ReglaNegocioException("La película ya está desactivada.");
        }
        pelicula.desactivar();
    }

    
    public Pelicula buscarPelicula(String codigo) {
        return estado.getPeliculas().buscar(codigo);
    }

    public List<Pelicula> listarPeliculas() {
        List<Pelicula> peliculas = estado.getPeliculas().listar();
        ordenarPorCodigo(peliculas);
        return peliculas;
    }

    
    public List<Pelicula> listarPeliculasActivas() {
        List<Pelicula> activas = new ArrayList<>();
        for (Pelicula pelicula : listarPeliculas()) {
            if (pelicula.isActiva()) {
                activas.add(pelicula);
            }
        }
        return activas;
    }

    private Pelicula obtenerPelicula(String codigo) throws ReglaNegocioException {
        Pelicula pelicula = buscarPelicula(codigo);
        if (pelicula == null) {
            throw new ReglaNegocioException("No existe la película " + codigo + ".");
        }
        return pelicula;
    }

    
    private void verificarSinSuperposiciones(Pelicula pelicula) throws ReglaNegocioException {
        List<Funcion> funciones = estado.getFunciones().listar();
        for (Funcion propia : funciones) {
            if (propia.getPelicula().equals(pelicula) && !propia.comenzo()) {
                for (Funcion otra : funciones) {
                    if (!propia.equals(otra) && propia.seSuperponeCon(otra)) {
                        throw new ReglaNegocioException(
                                "No se puede modificar la película: la función "
                                + propia.getCodigo() + " se superpondría con la función "
                                + otra.getCodigo() + " en " + propia.getSala().getNombre() + ".");
                    }
                }
            }
        }
    }

    
    private String generarCodigo() {
        int numero = estado.getPeliculas().cantidad() + 1;
        String codigo = String.format("P%03d", numero);
        while (estado.getPeliculas().existe(codigo)) {
            numero++;
            codigo = String.format("P%03d", numero);
        }
        return codigo;
    }

    
    private void ordenarPorCodigo(List<Pelicula> peliculas) {
        Collections.sort(peliculas, Comparator.comparing(Pelicula::getCodigo));
    }
}