package parcial.dos;

/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/
public abstract class Entrada {

    private Butaca butaca;
    private double precioConfirmado;

    public Entrada(Butaca butaca) {
        // Constructor a resolver...
    }

    public Butaca getButaca() {
        // Método a resolver...
        return null;
    }

    public double getPrecioConfirmado() {
        // Método a resolver...
        return 0;
    }

    public void confirmarPrecio(double precioBase) {
        // Método a resolver...
    }

    public abstract double calcularPrecio(double precioBase);

    public abstract String getNombreTipo();

}