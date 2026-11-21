package parcial.dos;

import java.time.LocalDateTime;
import java.util.List;

/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/
public class Reserva {

    private String codigo;
    private Cliente cliente;
    private Funcion funcion;
    private List<Entrada> entradas;
    private EstadoReserva estado;
    private double importeTotal;
    private LocalDateTime fechaHoraReserva;

    public Reserva(String codigo, Cliente cliente, Funcion funcion, List<Entrada> entradas) {
        // Constructor a resolver...
    }

    public String getCodigo() {
        return codigo;
    }
    
    public Cliente getCliente() {
        return this.cliente;
    }

    public Funcion getFuncion() {
        return ;
    }

    public List<Entrada> getEntradas() {
        // Método a resolver...
        return null;
    }

    public EstadoReserva getEstado() {
        // Método a resolver...
        return null;
    }

    public double getImporteTotal() {
        // Método a resolver...
        return 0;
    }

    public LocalDateTime getFechaHoraReserva() {
        // Método a resolver...
        return null;
    }

    public double calcularImporte() {
        // Método a resolver...
        return 0;
    }

    public boolean ocupaButaca(Butaca butaca) {
        // Método a resolver...
        return false;
    }

    public void cancelar() {
        // Método a resolver...
    }

    public boolean equals(Object obj) {
        // Método a resolver...
        return false;
    }

    public int hashCode() {
        // Método a resolver...
        return 0;
    }

}