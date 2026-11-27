package parcial.dos;

import excepciones.ReglaNegocioException;
import java.io.Serializable;

/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/
public abstract class Entrada implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private final Butaca butaca;
    private double precioConfirmado;
    private boolean confirmada;

    public Entrada(Butaca butaca) throws ReglaNegocioException {
        if (butaca == null){
            throw new ReglaNegocioException(
            "La butaca es obligatoria."
            );
        }
        
        this.butaca = butaca;
        this.precioConfirmado = 0;
        this.confirmada = false;
        
    }

    public Butaca getButaca() {
        return butaca;
    }
    public boolean isConfirmada(){
        return confirmada;
    }

    public double getPrecioConfirmado() {
        return precioConfirmado;
    }
    public abstract String getNombreTipo();

    public void confirmarPrecio(double precioBase) throws ReglaNegocioException{
        if (confirmada){
            throw new ReglaNegocioException(
            "El precio de la entrada ya fue confirmado."
            );
        }
        if (precioBase <= 0 || !Double.isFinite(precioBase)){
            throw new ReglaNegocioException(
            "El precio debe ser válido y mayor que cero."
            );
        }
        this.precioConfirmado = calcularPrecio(precioBase);
        this.confirmada = true;
    }

    public abstract double calcularPrecio(double precioBase);
} 