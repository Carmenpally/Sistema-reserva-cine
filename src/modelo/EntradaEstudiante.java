package modelo;
import excepciones.ReglaNegocioException;

/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/
public class EntradaEstudiante extends Entrada {
    
    private static final long serialVersionUID = 1L;

    private final double DESCUENTO = 0.20;
    
    public EntradaEstudiante(Butaca butaca) throws ReglaNegocioException{
        super(butaca);
    }
    
    @Override
    public double calcularPrecio(double precioBase) {
        double descuento = precioBase * DESCUENTO;
        
        double precioFinal = precioBase - descuento;
        
        return precioFinal;
    }

    @Override
    public String getNombreTipo() {
        return "Estudiante";
    }

}