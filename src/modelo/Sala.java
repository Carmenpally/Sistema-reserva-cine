package modelo;
import repositorios.Identificable;
import excepciones.ReglaNegocioException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/
public class Sala implements Serializable, Identificable {
    
    private static final long serialVersionUID = 1L;
    private String codigo;
    private String nombre;
    private int filas;
    private int butacasPorFila;
    private List<Butaca> butacas;

    public Sala (String codigo, String nombre, int filas, int butacasPorFila)throws ReglaNegocioException {
        if (codigo == null|| codigo.trim().isEmpty()){
            throw new ReglaNegocioException("El código de la sala es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()){
            throw new ReglaNegocioException("El nombre de la sala es obligatorio.");
        }
        if (filas <= 0){
            throw new ReglaNegocioException("La cantidad de filas debe ser mayor que cero.");
        }
        if (filas > 26) {
            throw new ReglaNegocioException(
            "La sala no puede tener más de 26 filas.");
        }
        if (butacasPorFila <=0){
            throw new ReglaNegocioException("La cantidad de butacas por fila debe ser mayor que cero");
        }
        
        this.codigo = codigo.trim().toUpperCase(Locale.ROOT);
        this.nombre = nombre.trim();
        this.filas = filas;
        this.butacasPorFila = butacasPorFila;
        int capacidad = filas * butacasPorFila;
        this.butacas = new ArrayList<>(capacidad); 
        
        generarButacas();
    }
    private void generarButacas()throws ReglaNegocioException {
        for (int i = 0; i < filas; i++){
            char letraFila = (char) ('A' + i );
            String fila = "" + letraFila;
            
            for (int numero = 1; numero <= butacasPorFila; numero++){
                Butaca butaca = new Butaca(fila, numero);
                
                butacas.add(butaca);
            }
        }
    }

    @Override
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getFilas() {
        return filas;
    }

    public int getButacasPorFila() {
        return butacasPorFila;
    }

    public int getCapacidad() {
        return filas*butacasPorFila;
    }

    public List<Butaca> getButacas() {
        return butacas;
    }

    public Butaca buscarButaca(String fila, int numero) {
        for (Butaca butaca : butacas){
            
            boolean mismaFila = butaca.getFila().equals(fila);
            boolean mismoNumero = butaca.getNumero() == numero;
            if (mismaFila && mismoNumero){
                return butaca;
            }
        }
        return null;
    }

    public boolean contieneButaca(Butaca butaca) {
        return butacas.contains(butaca);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        Sala otra = (Sala) obj;
        
        return codigo.equals(otra.codigo);
    }
    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }

    @Override
    public int hashCode(){
        return codigo.hashCode();
    }
}