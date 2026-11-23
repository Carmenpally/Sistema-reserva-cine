package parcial.dos;

import java.io.Serializable;

/**
 * Entrada de una butaca dentro de una reserva.
 * Es abstracta: no existe una entrada "sin tipo". Cada tipo (general, estudiante)
 * define cómo calcula su precio, y el resto del sistema solo llama a
 * calcularPrecio() sin preguntar de qué tipo es (polimorfismo).
 */
public abstract class Entrada implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Butaca butaca;
    private double precioConfirmado;

    public Entrada(Butaca butaca) {
        // faltan las validaciones y excepcioes
        this.butaca = butaca;
    }

    public Butaca getButaca() {
        return butaca;
    }

    public double getPrecioConfirmado() {
        return precioConfirmado;
    }

    
    //  no cambia aunque después cambie el precio de la función 
    // "El importe confirmado y los precios de las entradas deberán conservarse aunque posteriormente
    //cambie el precio de la función"
    public void confirmarPrecio(double precioBase) {
        this.precioConfirmado = calcularPrecio(precioBase);
    }

    
    public abstract double calcularPrecio(double precioBase);

    
    public abstract String getNombreTipo();
}