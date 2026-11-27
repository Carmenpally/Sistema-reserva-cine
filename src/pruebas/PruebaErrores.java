package pruebas;

import excepciones.ReglaNegocioException;
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
import servicios.PeliculaService;
import servicios.ReservaService;
import modelo.Sala;
import servicios.SalaService;

public class PruebaErrores {

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

            // Preparar datos

            peliculaService.registrarPelicula(
                    "P01", "Matrix", 120);

            Sala sala = salaService.registrarSala(
                    "S01", "Sala Principal", 4, 5);

            Sala otraSala = salaService.registrarSala(
                    "S02", "Sala Secundaria", 4, 5);

            LocalDateTime fechaHora =
                    LocalDateTime.now().plusDays(1);

            Funcion funcion = funcionService.registrarFuncion(
                    "F01", "P01", "S01", fechaHora, 2000);

            Butaca butaca = sala.buscarButaca("A", 1);

            // PRUEBA 1: Código duplicado

            try {

                peliculaService.registrarPelicula(
                        "P01", "Titanic", 194);

                System.out.println("Prueba 1: FALLIDA");

            } catch (ReglaNegocioException e) {

                System.out.println("Prueba 1: OK - "
                        + e.getMessage());
            }

            // PRUEBA 2: Funciones superpuestas

            try {

                funcionService.registrarFuncion(
                        "F02", "P01", "S01",
                        fechaHora.plusMinutes(30), 2000);

                System.out.println("Prueba 2: FALLIDA");

            } catch (ReglaNegocioException e) {

                System.out.println("Prueba 2: OK - "
                        + e.getMessage());
            }

            // Crear una reserva válida

            List<Entrada> entradas = new ArrayList<>();

            entradas.add(new EntradaGeneral(butaca));

            reservaService.registrarReserva(
                    "R01", "35456789", "Juan Perez",
                    "F01", entradas);

            // PRUEBA 3: Butaca ocupada

            try {

                List<Entrada> nuevasEntradas =
                        new ArrayList<>();

                nuevasEntradas.add(new EntradaGeneral(butaca));

                reservaService.registrarReserva(
                        "R02", "38945621", "Maria Lopez",
                        "F01", nuevasEntradas);

                System.out.println("Prueba 3: FALLIDA");

            } catch (ReglaNegocioException e) {

                System.out.println("Prueba 3: OK - "
                        + e.getMessage());
            }

            // PRUEBA 4: Butacas repetidas

            try {

                Butaca butacaDos = sala.buscarButaca("A", 2);

                List<Entrada> repetidas = new ArrayList<>();

                repetidas.add(new EntradaGeneral(butacaDos));

                repetidas.add(new EntradaEstudiante(butacaDos));

                reservaService.registrarReserva(
                        "R03", "35456789", "Juan Perez",
                        "F01", repetidas);

                System.out.println("Prueba 4: FALLIDA");

            } catch (ReglaNegocioException e) {

                System.out.println("Prueba 4: OK - "
                        + e.getMessage());
            }

            // PRUEBA 5: Butaca de otra sala

            try {

                Butaca butacaIncorrecta =
                        otraSala.buscarButaca("B", 1);

                List<Entrada> incorrectas = new ArrayList<>();

                incorrectas.add(
                        new EntradaGeneral(butacaIncorrecta));

                reservaService.registrarReserva(
                        "R04", "35456789", "Juan Perez",
                        "F01", incorrectas);

                System.out.println("Prueba 5: FALLIDA");

            } catch (ReglaNegocioException e) {

                System.out.println("Prueba 5: OK - "
                        + e.getMessage());
            }

            // PRUEBA 6: Función en el pasado

            try {

                funcionService.registrarFuncion(
                        "F03", "P01", "S01",
                        LocalDateTime.now().minusDays(1), 2000);

                System.out.println("Prueba 6: FALLIDA");

            } catch (ReglaNegocioException e) {

                System.out.println("Prueba 6: OK - "
                        + e.getMessage());
            }

            // Cancelar la reserva existente

            reservaService.cancelarReserva("R01");

            // PRUEBA 7: Cancelación doble

            try {

                reservaService.cancelarReserva("R01");

                System.out.println("Prueba 7: FALLIDA");

            } catch (ReglaNegocioException e) {

                System.out.println("Prueba 7: OK - "
                        + e.getMessage());
            }

        } catch (ReglaNegocioException e) {

            System.out.println("Error durante la preparación: "
                    + e.getMessage());
        }
    }
}