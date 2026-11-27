package parcial.dos;

import excepciones.ReglaNegocioException;

/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/
public class EntradaGeneral extends Entrada {
    
    private static final long serialVersionUID = 1L;
    
    public EntradaGeneral(Butaca butaca) throws ReglaNegocioException{
        super(butaca);
    }
    
    @Override
    public double calcularPrecio(double precioBase){
        return precioBase;
    }
    
    @Override
    public String getNombreTipo(){
        return "General";
    }
}