package parcial.dos;

import excepciones.ReglaNegocioException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ReservaService {

    private final EstadoCine estado;

    public ReservaService(EstadoCine estado) {
        this.estado = estado;
    }

    
    public Set<Butaca> obtenerButacasOcupadas(Funcion funcion) {
        Set<Butaca> ocupadas = new HashSet<>();
        for (Reserva reserva : estado.getReservas().listar()) {
            if (reserva.getFuncion().equals(funcion)
                    && reserva.getEstado() == EstadoReserva.CONFIRMADA) {
                for (Entrada entrada : reserva.getEntradas()) {
                    ocupadas.add(entrada.getButaca());
                }
            }
        }
        return ocupadas;
    }

    
    public Reserva confirmarReserva(Funcion funcion, String identificacion, String nombre,
                                    List<Entrada> entradas) throws ReglaNegocioException {
        if (funcion == null) {
            throw new ReglaNegocioException("La función es obligatoria.");
        }
        if (!estado.getFunciones().existe(funcion.getCodigo())) {
            throw new ReglaNegocioException("La función no existe en el sistema.");
        }
        if (funcion.comenzo()) {
            throw new ReglaNegocioException("La función ya ha comenzado.");
        }
        if (entradas == null || entradas.isEmpty()) {
            throw new ReglaNegocioException("Debe seleccionar al menos una butaca.");
        }

        
        Set<Butaca> ocupadas = obtenerButacasOcupadas(funcion);
        for (Entrada entrada : entradas) {
            if (entrada == null) {
                throw new ReglaNegocioException("La entrada no puede ser nula.");
            }
            Butaca butaca = entrada.getButaca();
            if (ocupadas.contains(butaca)) {
                throw new ReglaNegocioException("La butaca " + butaca.getFila()
                        + butaca.getNumero() + " ya está ocupada para esta función.");
            }
        }

        
        Cliente cliente = resolverCliente(identificacion, nombre);
        Reserva reserva = new Reserva(generarCodigo(), cliente, funcion, entradas);

        if (!estado.getClientes().existe(cliente.getCodigo())) {
            estado.getClientes().agregar(cliente);
        }
        estado.getReservas().agregar(reserva);
        return reserva;
    }

    public void cancelarReserva(String codigo) throws ReglaNegocioException {
        Reserva reserva = buscarReserva(codigo);
        if (reserva == null) {
            throw new ReglaNegocioException("No existe la reserva " + codigo + ".");
        }
        reserva.cancelar(); // rechaza si ya estaba cancelada
    }

    
    public Reserva buscarReserva(String codigo) {
        if (codigo == null) {
            return null;
        }
        String buscado = codigo.trim();
        for (Reserva reserva : estado.getReservas().listar()) {
            if (reserva.getCodigo().equalsIgnoreCase(buscado)) {
                return reserva;
            }
        }
        return null;
    }

    
    public List<Reserva> buscar(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return listarReservas();
        }
        String buscado = criterio.trim();
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva reserva : estado.getReservas().listar()) {
            boolean porCodigo = reserva.getCodigo().equalsIgnoreCase(buscado);
            boolean porCliente = reserva.getCliente().getIdentificacion().equals(buscado);
            if (porCodigo || porCliente) {
                resultado.add(reserva);
            }
        }
        ordenarPorCodigo(resultado);
        return resultado;
    }

    public List<Reserva> listarReservas() {
        List<Reserva> reservas = estado.getReservas().listar();
        ordenarPorCodigo(reservas);
        return reservas;
    }

    
    private Cliente resolverCliente(String identificacion, String nombre)
            throws ReglaNegocioException {
        Cliente nuevo = new Cliente(identificacion, nombre);
        Cliente existente = estado.getClientes().buscar(nuevo.getCodigo());
        if (existente != null) {
            return existente;
        }
        return nuevo;
    }

    
    private String generarCodigo() {
        int numero = estado.getReservas().cantidad() + 1;
        String codigo = String.format("CIN-%03d", numero);
        while (estado.getReservas().existe(codigo)) {
            numero++;
            codigo = String.format("CIN-%03d", numero);
        }
        return codigo;
    }

    
    private void ordenarPorCodigo(List<Reserva> reservas) {
        Collections.sort(reservas, Comparator.comparing(Reserva::getCodigo));
    }
}