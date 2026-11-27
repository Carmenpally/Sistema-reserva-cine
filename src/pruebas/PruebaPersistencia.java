package pruebas;

import excepciones.ReglaNegocioException;
import java.io.IOException;
import modelo.Butaca;
import modelo.Entrada;
import modelo.EstadoCine;
import persistencia.ExportadorReservas;
import modelo.Funcion;
import servicios.FuncionService;
import persistencia.PersistenciaCine;
import modelo.Reserva;
import servicios.ReservaService;
import modelo.Sala;
import servicios.SalaService;

public class PruebaPersistencia {

    public static void main(String[] args) {

        try {

            PersistenciaCine persistencia =
                    new PersistenciaCine();

            EstadoCine estado = persistencia.cargar();

            System.out.println("Datos recuperados correctamente.");

            // 1. Verificar cantidad de entidades

            System.out.println("Peliculas: "
                    + estado.getPeliculas().cantidad());

            System.out.println("Salas: "
                    + estado.getSalas().cantidad());

            System.out.println("Funciones: "
                    + estado.getFunciones().cantidad());

            System.out.println("Clientes: "
                    + estado.getClientes().cantidad());

            System.out.println("Reservas: "
                    + estado.getReservas().cantidad());

            // 2. Recuperar servicios

            ReservaService reservaService =
                    new ReservaService(estado);

            FuncionService funcionService =
                    new FuncionService(estado);

            SalaService salaService =
                    new SalaService(estado);

            // 3. Recuperar reserva

            Reserva reserva =
                    reservaService.buscarReserva("R01");

            System.out.println("\nDatos de la reserva:");

            System.out.println("Codigo: "
                    + reserva.getCodigo());

            System.out.println("Cliente: "
                    + reserva.getCliente().getNombre());

            System.out.println("Pelicula: "
                    + reserva.getFuncion()
                            .getPelicula().getTitulo());

            System.out.println("Importe total: "
                    + reserva.getImporteTotal());

            System.out.println("Estado: "
                    + reserva.getEstado());

            // 4. Recuperar entradas

            for (Entrada entrada : reserva.getEntradas()) {

                Butaca butaca = entrada.getButaca();

                System.out.println("Butaca: "
                        + butaca.getFila()
                        + butaca.getNumero()
                        + " - " + entrada.getNombreTipo()
                        + " - $" + entrada.getPrecioConfirmado());
            }

            // 5. Verificar precio actual

            Funcion funcion =
                    funcionService.buscarFuncion("F01");

            System.out.println("\nPrecio actual de la funcion: "
                    + funcion.getPrecioBase());

            // 6. Verificar disponibilidad

            Sala sala = salaService.buscarSala("S01");

            Butaca butaca = sala.buscarButaca("A", 1);

            System.out.println("Butaca A1 ocupada: "
                    + reservaService.estaOcupada(funcion, butaca));

            // 7. Verificar referencias

            boolean mismaFuncion =
                    reserva.getFuncion() == funcion;

            boolean mismaSala =
                    funcion.getSala() == sala;

            boolean mismaButaca =
                    reserva.getEntradas().get(0).getButaca()
                    == butaca;

            System.out.println("\nVerificacion de referencias:");

            System.out.println("Misma funcion: " + mismaFuncion);

            System.out.println("Misma sala: " + mismaSala);

            System.out.println("Misma butaca: " + mismaButaca);

            // 8. Exportar CSV

            ExportadorReservas exportador =
                    new ExportadorReservas();

            exportador.exportar(
                    reservaService.listarReservas());

            System.out.println("\nCSV exportado correctamente.");

        } catch (ReglaNegocioException e) {

            System.out.println("Error de negocio: "
                    + e.getMessage());

        } catch (IOException e) {

            System.out.println("Error de persistencia: "
                    + e.getMessage());

        } catch (ClassNotFoundException e) {

            System.out.println("Error de deserializacion: "
                    + e.getMessage());
        }
    }
}