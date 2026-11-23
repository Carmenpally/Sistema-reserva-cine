package parcial.dos;

/**
 * Entrada de estudiante: precio base menos descuento 20%.
 * 
 */
public class EntradaEstudiante extends Entrada {

    private static final long serialVersionUID = 1L;

    
    private static final double DESCUENTO = 0.20;

    public EntradaEstudiante(Butaca butaca) {
        super(butaca);
    }

    @Override
    public double calcularPrecio(double precioBase) {
        return precioBase - (precioBase * DESCUENTO);
    }

    @Override
    public String getNombreTipo() {
        return "Estudiante";
    }
}