package parcial.dos;
import java.util.ArrayList;
import java.util.List;

/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/
public class Sala {

    private String codigo;
    private String nombre;
    private int filas;
    private int butacasPorFila;
    private List<Butaca> butacas;

    public Sala(String codigo, String nombre, int filas, int butacasPorFila) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.filas = filas;
        this.butacasPorFila = butacasPorFila;
        this.butacas = new ArrayList<>(); 
        
        generarButacas();
    }
    private void generarButacas(){
        for (int i = 0; i < filas; i++){
            String fila = String.
        }

    public String getCodigo() {
        // Método a resolver...
        return "";
    }

    public String getNombre() {
        // Método a resolver...
        return "";
    }

    public int getFilas() {
        // Método a resolver...
        return 0;
    }

    public int getButacasPorFila() {
        // Método a resolver...
        return 0;
    }

    public int getCapacidad() {
        // Método a resolver...
        return 0;
    }

    public List<Butaca> getButacas() {
        // Método a resolver...
        return null;
    }

    public Butaca buscarButaca(String fila, int numero) {
        // Método a resolver...
        return null;
    }

    public boolean contieneButaca(Butaca butaca) {
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

}