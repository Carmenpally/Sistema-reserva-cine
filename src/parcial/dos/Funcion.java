package parcial.dos;

import java.time.LocalDateTime;

public class Funcion {

    private String codigo;
    private Pelicula pelicula;
    private Sala sala;
    private LocalDateTime fechaHora;
    private double precioBase;
    private String horaFinal;

    public Funcion(String codigo, Pelicula pelicula, Sala sala, LocalDateTime fechaHora, double precioBase) {
        // Constructor a resolver...
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Pelicula getPelicula() {
        return this.pelicula;
    }

    public Sala getSala() {
        return this.sala;
    }

    public int getFechaHora() {
        return this.fechaHora.getDayOfMonth();
    }

    public double getPrecioBase() {
        return this.precioBase;
    }    

    public boolean comenzo() {
        // Método a resolver...
        return false;
    }

    public boolean seSuperponeCon(Funcion otra) {
        // Método a resolver...
        return false;
    }

    public boolean equals(Object obj) {
        // Método a resolver...
        return false;
    }

    public int hashCode() {
        // Método a resolver...
        return 0;
    }

    @Override
    public String toString(){
        return "Funcion: " +
                "\nCodigo: " + this.codigo +
                "\nPelicula: " + this.pelicula.getTitulo() +
                "\nSala: " + this.sala.getNombre() +
                "\nHorario: " + this.fechaHora +
                "\nPrecio " + this.precioBase;
    }
}