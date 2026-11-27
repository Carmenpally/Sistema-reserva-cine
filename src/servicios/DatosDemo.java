package servicios;

import excepciones.ReglaNegocioException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import modelo.Entrada;
import modelo.EntradaEstudiante;
import modelo.EntradaGeneral;
import modelo.EstadoCine;
import modelo.Sala;

/**
 
 * 2 salas, 3 películas, 6 funciones y reservas.

 */
public class DatosDemo {

    private final EstadoCine estado;
    private final PeliculaService peliculaService;
    private final SalaService salaService;
    private final FuncionService funcionService;
    private final ReservaService reservaService;

    public DatosDemo(EstadoCine estado,
            PeliculaService peliculaService,
            SalaService salaService,
            FuncionService funcionService,
            ReservaService reservaService) {
        this.estado = estado;
        this.peliculaService = peliculaService;
        this.salaService = salaService;
        this.funcionService = funcionService;
        this.reservaService = reservaService;
    }

   
    public boolean cargarSiEstaVacio() throws ReglaNegocioException {

        boolean hayDatos = estado.getPeliculas().cantidad() > 0
                || estado.getSalas().cantidad() > 0
                || estado.getFunciones().cantidad() > 0
                || estado.getReservas().cantidad() > 0;

        if (hayDatos) {
            return false;
        }

        cargarSalas();
        cargarPeliculas();
        cargarFunciones();
        cargarReservas();

        return true;
    }

    private void cargarSalas() throws ReglaNegocioException {
        salaService.registrarSala("S01", "Sala 1", 5, 8);
        salaService.registrarSala("S02", "Sala 2", 4, 6);
    }

    private void cargarPeliculas() throws ReglaNegocioException {
        peliculaService.registrarPelicula("P01", "El viaje de Luna", 110);
        peliculaService.registrarPelicula("P02", "Noche en la ciudad", 95);
        peliculaService.registrarPelicula("P03", "Aventura extrema", 130);
    }

    private void cargarFunciones() throws ReglaNegocioException {

        LocalDate manana = LocalDate.now().plusDays(1);
        LocalDate pasado = LocalDate.now().plusDays(2);

        // Sala 1
        funcionService.registrarFuncion("F01", "P01", "S01",
                LocalDateTime.of(manana, LocalTime.of(14, 0)), 4000);
        funcionService.registrarFuncion("F02", "P02", "S01",
                LocalDateTime.of(manana, LocalTime.of(17, 0)), 4500);
        funcionService.registrarFuncion("F03", "P03", "S01",
                LocalDateTime.of(manana, LocalTime.of(20, 0)), 5000);

        // Sala 2
        funcionService.registrarFuncion("F04", "P01", "S02",
                LocalDateTime.of(manana, LocalTime.of(16, 0)), 4000);
        funcionService.registrarFuncion("F05", "P02", "S02",
                LocalDateTime.of(pasado, LocalTime.of(18, 0)), 4500);
        funcionService.registrarFuncion("F06", "P03", "S02",
                LocalDateTime.of(pasado, LocalTime.of(21, 0)), 5000);
    }

    private void cargarReservas() throws ReglaNegocioException {

        Sala sala1 = salaService.buscarSala("S01");
        Sala sala2 = salaService.buscarSala("S02");

        
        List<Entrada> entradasR01 = new ArrayList<>();
        entradasR01.add(new EntradaGeneral(sala1.buscarButaca("A", 1)));
        entradasR01.add(new EntradaEstudiante(sala1.buscarButaca("A", 2)));
        reservaService.registrarReserva("R01", "30111222",
                "Ana Gómez", "F01", entradasR01);

        
        List<Entrada> entradasR02 = new ArrayList<>();
        entradasR02.add(new EntradaGeneral(sala2.buscarButaca("A", 1)));
        reservaService.registrarReserva("R02", "28999888",
                "Luis Pérez", "F04", entradasR02);

        // reserva que se cancela 
        List<Entrada> entradasR03 = new ArrayList<>();
        entradasR03.add(new EntradaEstudiante(sala1.buscarButaca("C", 4)));
        reservaService.registrarReserva("R03", "40222333",
                "Carla Díaz", "F02", entradasR03);
        reservaService.cancelarReserva("R03");
    }
}