package parcial.dos;

/**
 * Entrada general: precio base 
 */
public class EntradaGeneral extends Entrada {

    private static final long serialVersionUID = 1L;

    public EntradaGeneral(Butaca butaca) {
        super(butaca);
    }

    @Override
    public double calcularPrecio(double precioBase) {
        return precioBase;
    }

    @Override
    public String getNombreTipo() {
        return "General";
    }
}