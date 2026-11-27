package pruebas;

import excepciones.ReglaNegocioException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import modelo.Butaca;
import modelo.Entrada;
import modelo.EntradaEstudiante;
import modelo.EntradaGeneral;
import modelo.EstadoCine;
import modelo.Funcion;
import servicios.FuncionService;
import modelo.Pelicula;
import servicios.PeliculaService;
import persistencia.PersistenciaCine;
import modelo.Reserva;
import servicios.ReservaService;
import modelo.Sala;
import servicios.SalaService;

public class PruebaSistema {

    public static void main(String[] args) {
        

        try {

            EstadoCine estado = new EstadoCine();

            PeliculaService peliculaService =
                    new PeliculaService(estado);

            SalaService salaService =
                    new SalaService(estado);

            FuncionService funcionService =
                    new FuncionService(estado);

            ReservaService reservaService =
                    new ReservaService(estado);

            // 1. Registrar películas

            Pelicula pelicula = peliculaService.registrarPelicula(
                    "P01", "Matrix", 120);

            System.out.println("Pelicula registrada: "
                    + pelicula.getTitulo());

            // 2. Registrar sala

            Sala sala = salaService.registrarSala(
                    "S01", "Sala Principal", 4, 5);

            System.out.println("Capacidad de la sala: "
                    + sala.getCapacidad());

            // 3. Registrar función

            LocalDateTime fechaHora =
                    LocalDateTime.now().plusDays(1);

            Funcion funcion = funcionService.registrarFuncion(
                    "F01", "P01", "S01", fechaHora, 2000);

            System.out.println("Funcion registrada: "
                    + funcion.getCodigo());

            // 4. Buscar butacas

            Butaca butacaUno = sala.buscarButaca("A", 1);
            Butaca butacaDos = sala.buscarButaca("A", 2);

            // 5. Crear entradas

            Entrada entradaUno = new EntradaGeneral(butacaUno);

            Entrada entradaDos = new EntradaEstudiante(butacaDos);

            List<Entrada> entradas = new ArrayList<>();

            entradas.add(entradaUno);
            entradas.add(entradaDos);

            // 6. Registrar reserva

            Reserva reserva = reservaService.registrarReserva(
                    "R01", "35456789", "Juan Perez",
                    "F01", entradas);

            System.out.println("Reserva registrada: "
                    + reserva.getCodigo());

            System.out.println("Entrada general: "
                    + entradaUno.getPrecioConfirmado());

            System.out.println("Entrada estudiante: "
                    + entradaDos.getPrecioConfirmado());

            System.out.println("Importe total: "
                    + reserva.getImporteTotal());

            // 7. Verificar ocupación

            System.out.println("Butaca A1 ocupada: "
                    + reservaService.estaOcupada(funcion, butacaUno));

            // 8. Modificar precio

            funcionService.modificarPrecio("F01", 3000);

            System.out.println("Nuevo precio de la funcion: "
                    + funcion.getPrecioBase());

            System.out.println("Importe de la reserva original: "
                    + reserva.getImporteTotal());

            // 9. Cancelar reserva

            reservaService.cancelarReserva("R01");

            System.out.println("Estado de la reserva: "
                    + reserva.getEstado());

            System.out.println("Butaca A1 ocupada: "
                    + reservaService.estaOcupada(funcion, butacaUno));
            PersistenciaCine persistencia = new PersistenciaCine();
            persistencia.guardar(estado);
            System.out.println("Datos guardados correctamente.");

        } catch (ReglaNegocioException e) {

            System.out.println("Error de negocio: "
                    + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("Error de persistencia: "
            + e.getMessage());
        }
    }
}