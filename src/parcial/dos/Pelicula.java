package parcial.dos;

/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/
public class Pelicula {

    private String codigo;
    private String titulo;
    private int duracionMinutos;
    private boolean activa;

    public Pelicula(String codigo, String titulo, int duracionMinutos) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.duracionMinutos = duracionMinutos;
        this.activa = true; 
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public boolean isActiva() {
        return activa;
    }

    public void modificar(String titulo, int duracionMinutos) {
        this.titulo = titulo;
        this.duracionMinutos = duracionMinutos;
    }

    public void desactivar() {
        this.activa = false;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
        return true;
        }
        if (!(obj instanceof Pelicula)){
            return false;
        }
        
        Pelicula otra = (Pelicula) obj;
        
        boolean mismoCodigo = codigo.equals(otra.codigo);
        
        return mismoCodigo;
    }
}