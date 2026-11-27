package parcial.dos;

import excepciones.ReglaNegocioException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 las butacas las genera el constructor de Sala
 */
public class SalaService {

    //  filas A a Z
    private static final int MAX_FILAS = 26;

    private final EstadoCine estado;

    public SalaService(EstadoCine estado) {
        this.estado = estado;
    }

    
    public Sala registrarSala(String nombre, int filas, int butacasPorFila)
            throws ReglaNegocioException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre de la sala es obligatorio.");
        }
        if (filas > MAX_FILAS) {
            throw new ReglaNegocioException(
                    "La cantidad de filas no puede superar " + MAX_FILAS + " (filas A a Z).");
        }
        String buscado = nombre.trim();
        for (Sala existente : estado.getSalas().listar()) {
            if (existente.getNombre().equalsIgnoreCase(buscado)) {
                throw new ReglaNegocioException(
                        "Ya existe una sala con el nombre \"" + buscado + "\".");
            }
        }

        
        Sala sala = new Sala(generarCodigo(), nombre, filas, butacasPorFila);
        estado.getSalas().agregar(sala);
        return sala;
    }

    public List<Sala> listarSalas() {
        List<Sala> salas = estado.getSalas().listar();
        Collections.sort(salas, Comparator.comparing(Sala::getCodigo));
        return salas;
    }

    
    private String generarCodigo() {
        int numero = estado.getSalas().cantidad() + 1;
        String codigo = String.format("S%03d", numero);
        while (estado.getSalas().existe(codigo)) {
            numero++;
            codigo = String.format("S%03d", numero);
        }
        return codigo;
    }
}