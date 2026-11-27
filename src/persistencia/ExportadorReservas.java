package persistencia;

import modelo.Entrada;
import modelo.Butaca;
import modelo.Reserva;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportadorReservas {

    public void exportar(List<Reserva> reservas)
            throws IOException {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("reservas.csv"))) {

            bw.write("Codigo;Cliente;Identificacion;"
                    + "Pelicula;Funcion;Sala;"
                    + "FechaHoraFuncion;Butacas;"
                    + "TiposEntrada;ImporteTotal;Estado");

            bw.newLine();

            for (Reserva reserva : reservas) {

                String linea = generarLinea(reserva);

                bw.write(linea);

                bw.newLine();
            }
        }
    }

    private String generarLinea(Reserva reserva) {

        String butacas = "";
        String tiposEntrada = "";

        for (Entrada entrada : reserva.getEntradas()) {

            if (!butacas.isEmpty()) {

                butacas += ",";
                tiposEntrada += ",";
            }

            Butaca butaca = entrada.getButaca();

            butacas += butaca.getFila()
                    + butaca.getNumero();

            tiposEntrada += entrada.getNombreTipo();
        }

        String linea =
                escaparCampo(reserva.getCodigo()) + ";"
                + escaparCampo(reserva.getCliente().getNombre()) + ";"
                + escaparCampo(reserva.getCliente().getIdentificacion()) + ";"
                + escaparCampo(reserva.getFuncion().getPelicula().getTitulo()) + ";"
                + escaparCampo(reserva.getFuncion().getCodigo()) + ";"
                + escaparCampo(reserva.getFuncion().getSala().getCodigo()) + ";"
                + reserva.getFuncion().getFechaHoraInicio() + ";"
                + escaparCampo(butacas) + ";"
                + escaparCampo(tiposEntrada) + ";"
                + reserva.getImporteTotal() + ";"
                + reserva.getEstado();

        return linea;
    }

    private String escaparCampo(String campo) {

        String resultado = campo.replace("\"", "\"\"");

        return "\"" + resultado + "\"";
    }
}