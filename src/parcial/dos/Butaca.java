package parcial.dos;

import excepciones.ReglaNegocioException;
import java.io.Serializable;
import java.util.Objects;

/**
 * Ubicación física de una sala, identificada por fila y número
 * Es inmutable: una vez creada no cambia. Que esté libre u ocupada NO se guarda
 * aquí, porque eso depende de la función (lo resuelven las reservas).
 */
public class Butaca implements Serializable {

    private static final long serialVersionUID = 1L;


    private final String fila;
    private final int numero;

    public Butaca(String fila, int numero) throws ReglaNegocioException {
        if (fila == null || fila.trim().isEmpty()) {
            throw new ReglaNegocioException("La fila de la butaca es obligatoria.");
        }
        if (numero <= 0) {
            throw new ReglaNegocioException("El número de la butaca debe ser mayor que cero.");
        }
        this.fila = fila.trim();
        this.numero = numero;
    }

    public String getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Butaca otra = (Butaca) obj;
        return numero == otra.numero && fila.equals(otra.fila);
    }

    
    @Override
    public int hashCode() {
        return Objects.hash(fila, numero);
    }
}
