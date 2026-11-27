package modelo;

import repositorios.Repositorio;
import java.io.Serializable;


/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/
public class EstadoCine implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private final Repositorio<Pelicula> peliculas;
    private final Repositorio<Sala> salas;
    private final Repositorio<Funcion> funciones;
    private final Repositorio<Cliente> clientes;
    private final Repositorio<Reserva> reservas;
    
    public EstadoCine() {
        this.peliculas = new Repositorio<>();
        this.salas = new Repositorio<>();
        this.funciones = new Repositorio<>();
        this.clientes = new Repositorio<>();
        this.reservas = new Repositorio<>();
    }

    public Repositorio<Pelicula> getPeliculas() {
        return peliculas;
    }

    public Repositorio<Sala> getSalas() {
        return salas;
    }

    public Repositorio<Funcion> getFunciones() {
        return funciones;
    }

    public Repositorio<Cliente> getClientes() {
        return clientes;
    }

    public Repositorio<Reserva> getReservas() {
        return reservas;
    }

}