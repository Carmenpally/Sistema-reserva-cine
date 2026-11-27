package modelo;

import java.util.Locale;
import repositorios.Identificable;
import excepciones.ReglaNegocioException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;


/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/
public class Reserva implements Serializable, Identificable {
    
    private static final long serialVersionUID = 1L;

    private final String codigo;
    private final Cliente cliente;
    private final Funcion funcion;
    private final List<Entrada> entradas;
    private EstadoReserva estado;
    private final double importeTotal;
    private final LocalDateTime fechaHoraReserva;

    public Reserva(String codigo, Cliente cliente, Funcion funcion, List<Entrada> entradas) throws ReglaNegocioException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new ReglaNegocioException(
            "El código de la reserva es obligatorio."
            );
        }
        if (cliente == null){
            throw new ReglaNegocioException(
            "El cliente es obligatorio."
            );
        }
        if (funcion == null){
            throw new ReglaNegocioException(
            "La función es obligatoria."
            );
        }
        if (funcion.comenzo()){
            throw new ReglaNegocioException(
            "La función ya ha comenzado."
            );
        }
        if (entradas == null || entradas.isEmpty()){
            throw new ReglaNegocioException(
            "Debe seleccional al menos una butaca."
            );
        }
        
        validarEntradas(funcion, entradas);
        
        this.codigo = codigo.trim().toUpperCase(Locale.ROOT);
        this.cliente = cliente;
        this.funcion = funcion;
        this.entradas = new ArrayList<>(entradas);
        
        for (Entrada entrada : this.entradas){
            entrada.confirmarPrecio(funcion.getPrecioBase());
        }
        
        this.importeTotal = calcularImporte();
        this.estado = EstadoReserva.CONFIRMADA;
        this.fechaHoraReserva = LocalDateTime.now();
    }
    
    private void validarEntradas(Funcion funcion, List<Entrada> entradas) throws ReglaNegocioException{
        
        Set<Butaca> butacasSeleccionadas = new HashSet<>();
        
        for (Entrada entrada: entradas){
            if (entrada == null){
                throw new ReglaNegocioException(
                "La entrada no puede ser nula."
                );
            }
            if (entrada.isConfirmada()){
                throw new ReglaNegocioException(
                "La entrada ya fue confirmada."
                );
            }
            
            Butaca butaca = entrada.getButaca();
            
            Butaca butacaSala = funcion.getSala().buscarButaca(butaca.getFila(), butaca.getNumero());
            
            if (butacaSala != butaca){
                throw new ReglaNegocioException(
                "La butaca no pertenece a la sala."
                );
            }
            if (!butacasSeleccionadas.add(butaca)){
                throw new ReglaNegocioException(
                "Hay butacas repetidas en la reserva."
                );
            }
        }
    }

    @Override
    public String getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public List<Entrada> getEntradas() {
        return new ArrayList<>(entradas);
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public LocalDateTime getFechaHoraReserva() {
        return fechaHoraReserva;
    }

    public double calcularImporte() {
        
        double total = 0;
        
        for (Entrada entrada : entradas){
            total += entrada.getPrecioConfirmado();
        }
        return total;
    }

    public boolean ocupaButaca(Butaca butaca) {
        
        if (estado == EstadoReserva.CANCELADA){
            return false;
        }
        for (Entrada entrada : entradas){
            if (entrada.getButaca().equals(butaca)){
                return true;
            }
        }
        return false;
    }

    public void cancelar() throws ReglaNegocioException{
        if (estado == EstadoReserva.CANCELADA){
            throw new ReglaNegocioException(
            "La reserva ya fue cancelada."
            );
        }
        this.estado = EstadoReserva.CANCELADA;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (!(obj instanceof Reserva)){
            return false;
        }
        
        Reserva otra = (Reserva) obj;
        
        boolean mismoCodigo = codigo.equals(otra.codigo);
        
        return mismoCodigo;
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

}