package servicios;

import excepciones.ReglaNegocioException;
import java.util.ArrayList;
import java.util.List;
import modelo.Butaca;
import modelo.Cliente;
import modelo.Entrada;
import modelo.EstadoCine;
import modelo.Funcion;
import modelo.Reserva;

public class ReservaService {

    private final EstadoCine estado;

    public ReservaService(EstadoCine estado) {
        this.estado = estado;
    }

    public Reserva registrarReserva(String codigo,
            String identificacion, String nombre,
            String codigoFuncion, List<Entrada> entradas)
            throws ReglaNegocioException {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new ReglaNegocioException(
                    "El código de la reserva es obligatorio.");
        }

        if (estado.getReservas().existe(codigo.trim())) {
            throw new ReglaNegocioException(
                    "El código de la reserva ya existe.");
        }

        Funcion funcion =
                estado.getFunciones().buscar(codigoFuncion);

        if (funcion == null) {
            throw new ReglaNegocioException(
                    "La función no fue encontrada.");
        }

        validarDisponibilidad(funcion, entradas);

        Cliente cliente = new Cliente(identificacion, nombre);

        Cliente existente = estado.getClientes().buscar(
                cliente.getIdentificacion());

        if (existente != null) {
            cliente = existente;
        }

        Reserva reserva = new Reserva(
                codigo, cliente, funcion, entradas);

        registrarCliente(cliente);

        estado.getReservas().agregar(reserva);

        return reserva;
    }

    private void registrarCliente(Cliente cliente)
            throws ReglaNegocioException {

        String identificacion = cliente.getIdentificacion();

        if (!estado.getClientes().existe(identificacion)) {

            estado.getClientes().agregar(cliente);
        }
    }

    public boolean estaOcupada(Funcion funcion, Butaca butaca) {

        List<Reserva> reservas =
                estado.getReservas().listar();

        for (Reserva reserva : reservas) {

            boolean mismaFuncion =
                    reserva.getFuncion().equals(funcion);

            if (mismaFuncion && reserva.ocupaButaca(butaca)) {

                return true;
            }
        }

        return false;
    }

    private void validarDisponibilidad(Funcion funcion,
            List<Entrada> entradas)
            throws ReglaNegocioException {

        if (entradas == null || entradas.isEmpty()) {
            throw new ReglaNegocioException(
                    "Debe seleccionar al menos una butaca.");
        }

        for (Entrada entrada : entradas) {

            if (entrada == null) {
                throw new ReglaNegocioException(
                        "La entrada no puede ser nula.");
            }

            Butaca butaca = entrada.getButaca();

            if (estaOcupada(funcion, butaca)) {

                throw new ReglaNegocioException(
                        "La butaca "
                        + butaca.getFila()
                        + butaca.getNumero()
                        + " ya está ocupada.");
            }
        }
    }

    public Reserva buscarReserva(String codigo)
            throws ReglaNegocioException {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new ReglaNegocioException(
                    "El código de la reserva es obligatorio.");
        }

        Reserva reserva =
                estado.getReservas().buscar(codigo.trim());

        if (reserva == null) {
            throw new ReglaNegocioException(
                    "La reserva no fue encontrada.");
        }

        return reserva;
    }

    public List<Reserva> buscarPorCliente(String identificacion)
            throws ReglaNegocioException {

        if (identificacion == null
                || identificacion.trim().isEmpty()) {

            throw new ReglaNegocioException(
                    "La identificación es obligatoria.");
        }

        List<Reserva> resultado = new ArrayList<>();

        for (Reserva reserva : listarReservas()) {

            String identificacionCliente =
                    reserva.getCliente().getIdentificacion();
            
            if (identificacionCliente.equalsIgnoreCase(identificacion.trim())) {
                resultado.add(reserva);
            }
        }

        return resultado;
    }

    public List<Reserva> listarReservas() {

        return estado.getReservas().listar();
    }

    public void cancelarReserva(String codigo)
            throws ReglaNegocioException {

        Reserva reserva = buscarReserva(codigo);

        reserva.cancelar();
    }
}