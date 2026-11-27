package parcial.dos;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ExportadorReservaCsv {

    private static final String RUTA_CSV_POR_DEFECTO = "datos/reservas_consulta.csv";
    private static final DateTimeFormatter FMT_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean exportar(String rutaArchivo, EstadoCine estado) {
        if (estado == null) {
            System.err.println("Error al exportar: el estado es nulo.");
            return false;
        }

        Path destino = Paths.get(rutaArchivo);
        try {
            Path carpetaPadre = destino.getParent();
            if (carpetaPadre != null) {
                Files.createDirectories(carpetaPadre);
            }

            List<Reserva> reservas = estado.getReservas().listar();
            try (BufferedWriter bw = Files.newBufferedWriter(destino, StandardCharsets.UTF_8)) {
                bw.write("codigo_reserva;estado;fecha_hora_reserva;cliente_id;cliente_nombre;codigo_funcion;pelicula;sala;fecha_hora_funcion;butacas;cantidad_entradas;importe_total");
                bw.newLine();

                for (Reserva r : reservas) {
                    String butacasTexto = r.getEntradas().stream()
                            .map(e -> e.getButaca().getFila() + e.getButaca().getNumero() + "(" + e.getNombreTipo() + ")")
                            .collect(Collectors.joining(" | "));

                    String linea = String.join(";",
                            escapar(r.getCodigo()),
                            escapar(r.getEstado().name()),
                            escapar(r.getFechaHoraReserva().format(FMT_FECHA_HORA)),
                            escapar(r.getCliente().getIdentificacion()),
                            escapar(r.getCliente().getNombre()),
                            escapar(r.getFuncion().getCodigo()),
                            escapar(r.getFuncion().getPelicula().getTitulo()),
                            escapar(r.getFuncion().getSala().getNombre()),
                            escapar(r.getFuncion().getFechaHoraInicio().format(FMT_FECHA_HORA)),
                            escapar(butacasTexto),
                            String.valueOf(r.getEntradas().size()),
                            String.format(java.util.Locale.US, "%.2f", r.getImporteTotal())
                    );
                    bw.write(linea);
                    bw.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al exportar reservas a CSV '" + rutaArchivo + "': " + e.getMessage());
            return false;
        }
    }

    private static String escapar(String valor) {
        if (valor == null) {
            return "";
        }
        String limpio = valor.replace("\"", "\"\"");
        if (limpio.contains(";") || limpio.contains("\"") || limpio.contains("\n")) {
            return "\"" + limpio + "\"";
        }
        return limpio;
    }

    public static String getRutaCsvPorDefecto() {
        return RUTA_CSV_POR_DEFECTO;
    }
    
    
}