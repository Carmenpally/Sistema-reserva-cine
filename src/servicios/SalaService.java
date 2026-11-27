package servicios;

import excepciones.ReglaNegocioException;
import java.util.List;
import modelo.EstadoCine;
import modelo.Sala;

public class SalaService {

    private final EstadoCine estado;

    public SalaService(EstadoCine estado) {
        this.estado = estado;
    }

    public Sala registrarSala(String codigo, String nombre,
            int filas, int butacasPorFila)
            throws ReglaNegocioException {

        Sala sala = new Sala(codigo, nombre,
                filas, butacasPorFila);

        estado.getSalas().agregar(sala);

        return sala;
    }

    public Sala buscarSala(String codigo)
            throws ReglaNegocioException {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new ReglaNegocioException(
                    "El código de la sala es obligatorio.");
        }

        Sala sala = estado.getSalas().buscar(codigo.trim());

        if (sala == null) {
            throw new ReglaNegocioException(
                    "La sala no fue encontrada.");
        }

        return sala;
    }

    public List<Sala> listarSalas() {

        return estado.getSalas().listar();
    }
}