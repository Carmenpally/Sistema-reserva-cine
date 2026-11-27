package controladores;

import modelo.EstadoCine;
import modelo.Funcion;
import modelo.Butaca;
import modelo.Entrada;
import modelo.EntradaGeneral;
import modelo.EntradaEstudiante;
import modelo.Reserva;

import servicios.ReservaService;
import persistencia.PersistenciaCine;
import excepciones.ReglaNegocioException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControladorReservas extends VBox {

    private final EstadoCine estado;
    private final Funcion funcion;
    private final ReservaService reservaService;
    private final PersistenciaCine persistencia;

    private final Runnable accionVolver;

    private final TextField txtCodigo;
    private final TextField txtIdentificacion;
    private final TextField txtNombre;

    private final GridPane panelButacas;
    private final VBox panelEntradas;
    private final Label lblTotal;

    private final Map<Butaca, Button> botonesButacas;

    private final Map<Butaca, ComboBox<String>> seleccionadas;

    public ControladorReservas(
            EstadoCine estado,
            Funcion funcion,
            ReservaService reservaService,
            PersistenciaCine persistencia,
            Runnable accionVolver) {

        this.estado = estado;
        this.funcion = funcion;
        this.reservaService = reservaService;
        this.persistencia = persistencia;
        this.accionVolver = accionVolver;

        this.txtCodigo = new TextField();
        this.txtIdentificacion = new TextField();
        this.txtNombre = new TextField();

        this.panelButacas = new GridPane();
        this.panelEntradas = new VBox(5);

        this.lblTotal = new Label("Total: $0.00");

        this.botonesButacas = new LinkedHashMap<>();
        this.seleccionadas = new LinkedHashMap<>();

        this.setSpacing(15);
        this.setPadding(new Insets(25));

        crearEncabezado();
        crearPanelButacas();
        crearFormulario();
        crearResumen();
        crearBotones();
    }

    private void crearEncabezado() {

        Label titulo = new Label("Nueva reserva");

        titulo.setStyle(
                "-fx-font-size: 26px;"
                + "-fx-font-weight: bold;");

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Label informacion = new Label(
                funcion.getPelicula().getTitulo()
                + " - "
                + funcion.getSala().getNombre()
                + " - "
                + funcion.getFechaHoraInicio().format(formato));

        Label precio = new Label(
                "Precio base: $" + funcion.getPrecioBase());

        this.getChildren().addAll(
                titulo, informacion, precio);
    }

    private void crearPanelButacas() {

        Label pantalla = new Label("PANTALLA");

        pantalla.setMaxWidth(Double.MAX_VALUE);
        pantalla.setAlignment(Pos.CENTER);

        pantalla.setStyle(
                "-fx-background-color: #dce1e7;"
                + "-fx-padding: 8px;"
                + "-fx-font-weight: bold;");

        panelButacas.setHgap(5);
        panelButacas.setVgap(5);
        panelButacas.setAlignment(Pos.CENTER);
        panelButacas.setPadding(new Insets(10));

        for (Butaca butaca :
                funcion.getSala().getButacas()) {

            String identificador =
                    butaca.getFila() + butaca.getNumero();

            Button boton = new Button(identificador);

            boton.setPrefSize(50, 35);

            int fila =
                    butaca.getFila().charAt(0) - 'A';

            int columna = butaca.getNumero() - 1;

            botonesButacas.put(butaca, boton);

            boton.setOnAction(e ->
                    seleccionarButaca(butaca));

            panelButacas.add(boton, columna, fila);

            actualizarBotonButaca(butaca);
        }

        ScrollPane scroll = new ScrollPane(panelButacas);

        scroll.setFitToWidth(true);
        scroll.setPrefHeight(180);

        Label disponible = new Label("Disponible");

        disponible.setStyle(
                "-fx-background-color: #bbf7d0;"
                + "-fx-padding: 5px;");

        Label seleccionada = new Label("Seleccionada");

        seleccionada.setStyle(
                "-fx-background-color: #93c5fd;"
                + "-fx-padding: 5px;");

        Label ocupada = new Label("Ocupada");

        ocupada.setStyle(
                "-fx-background-color: #fecaca;"
                + "-fx-padding: 5px;");

        HBox leyenda = new HBox(10);

        leyenda.getChildren().addAll(
                disponible, seleccionada, ocupada);

        this.getChildren().addAll(
                pantalla, scroll, leyenda);
    }

    private void actualizarBotonButaca(Butaca butaca) {

        Button boton = botonesButacas.get(butaca);

        if (reservaService.estaOcupada(funcion, butaca)) {

            boton.setStyle(
                    "-fx-background-color: #fecaca;"
                    + "-fx-opacity: 1;");

            boton.setDisable(true);

        } else if (seleccionadas.containsKey(butaca)) {

            boton.setStyle(
                    "-fx-background-color: #93c5fd;");

            boton.setDisable(false);

        } else {

            boton.setStyle(
                    "-fx-background-color: #bbf7d0;");

            boton.setDisable(false);
        }
    }

    private void seleccionarButaca(Butaca butaca) {

        if (reservaService.estaOcupada(funcion, butaca)) {

            seleccionadas.remove(butaca);

            actualizarBotonButaca(butaca);
            actualizarResumen();

            mostrarError("La butaca está ocupada.");

            return;
        }

        if (seleccionadas.containsKey(butaca)) {

            seleccionadas.remove(butaca);

        } else {

            ComboBox<String> tipo = new ComboBox<>();

            tipo.getItems().addAll(
                    "General", "Estudiante");

            tipo.setValue("General");

            tipo.setOnAction(e -> actualizarTotal());

            seleccionadas.put(butaca, tipo);
        }

        actualizarBotonButaca(butaca);

        actualizarResumen();
    }

    private void crearFormulario() {

        Label titulo = new Label("Datos del cliente");

        titulo.setStyle("-fx-font-weight: bold;");

        GridPane formulario = new GridPane();

        formulario.setHgap(10);
        formulario.setVgap(10);

        formulario.add(
                new Label("Código de reserva:"), 0, 0);

        formulario.add(txtCodigo, 1, 0);

        formulario.add(
                new Label("Identificación:"), 0, 1);

        formulario.add(txtIdentificacion, 1, 1);

        formulario.add(
                new Label("Nombre:"), 0, 2);

        formulario.add(txtNombre, 1, 2);

        this.getChildren().addAll(titulo, formulario);
    }

    private void crearResumen() {

        Label titulo = new Label("Entradas seleccionadas");

        titulo.setStyle("-fx-font-weight: bold;");

        lblTotal.setStyle(
                "-fx-font-size: 20px;"
                + "-fx-font-weight: bold;");

        this.getChildren().addAll(
                titulo, panelEntradas, lblTotal);
    }

    private void actualizarResumen() {

        panelEntradas.getChildren().clear();

        for (Butaca butaca : seleccionadas.keySet()) {

            String identificador =
                    butaca.getFila() + butaca.getNumero();

            Label etiqueta = new Label(identificador);

            etiqueta.setPrefWidth(50);

            ComboBox<String> tipo =
                    seleccionadas.get(butaca);

            HBox fila = new HBox(10);

            fila.setAlignment(Pos.CENTER_LEFT);

            fila.getChildren().addAll(etiqueta, tipo);

            panelEntradas.getChildren().add(fila);
        }

        actualizarTotal();
    }

    private Entrada crearEntrada(Butaca butaca, String tipo)
            throws ReglaNegocioException {

        Entrada entrada;

        if (tipo.equals("Estudiante")) {

            entrada = new EntradaEstudiante(butaca);

        } else {

            entrada = new EntradaGeneral(butaca);
        }

        return entrada;
    }

    private void actualizarTotal() {

        try {

            double total = 0;

            for (Butaca butaca : seleccionadas.keySet()) {

                String tipo =
                        seleccionadas.get(butaca).getValue();

                Entrada entrada = crearEntrada(butaca, tipo);

                double precio =
                        entrada.calcularPrecio(
                                funcion.getPrecioBase());

                total += precio;
            }

            lblTotal.setText(
                    String.format("Total: $%.2f", total));

        } catch (ReglaNegocioException e) {

            lblTotal.setText("Total: no disponible");

            mostrarError(e.getMessage());
        }
    }

    private void crearBotones() {

        Button btnConfirmar =
                new Button("Confirmar reserva");

        Button btnVolver =
                new Button("Volver a cartelera");

        btnConfirmar.setOnAction(e -> confirmarReserva());

        btnVolver.setOnAction(e -> accionVolver.run());

        HBox botones = new HBox(10);

        botones.getChildren().addAll(
                btnConfirmar, btnVolver);

        this.getChildren().add(botones);
    }

    private void confirmarReserva() {

        try {

            if (seleccionadas.isEmpty()) {

                throw new ReglaNegocioException(
                        "Debe seleccionar al menos una butaca.");
            }

            String codigo = txtCodigo.getText();

            String identificacion =
                    txtIdentificacion.getText();

            String nombre = txtNombre.getText();

            List<Entrada> entradas = new ArrayList<>();

            for (Butaca butaca : seleccionadas.keySet()) {

                String tipo =
                        seleccionadas.get(butaca).getValue();

                Entrada entrada =
                        crearEntrada(butaca, tipo);

                entradas.add(entrada);
            }

            Reserva reserva =
                    reservaService.registrarReserva(
                            codigo,
                            identificacion,
                            nombre,
                            funcion.getCodigo(),
                            entradas);

            if (guardar()) {

                mostrarComprobante(reserva);

                accionVolver.run();

            } else {

                seleccionadas.clear();

                for (Butaca butaca : botonesButacas.keySet()) {

                    actualizarBotonButaca(butaca);
                }

                actualizarResumen();

                txtCodigo.clear();
            }

        } catch (ReglaNegocioException e) {

            mostrarError(e.getMessage());
        }
    }

    private void mostrarComprobante(Reserva reserva) {

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String comprobante =
                "Código: " + reserva.getCodigo()
                + "\nCliente: " + reserva.getCliente().getNombre()
                + "\nIdentificación: "
                + reserva.getCliente().getIdentificacion()
                + "\nPelícula: "
                + reserva.getFuncion().getPelicula().getTitulo()
                + "\nFunción: "
                + reserva.getFuncion().getCodigo()
                + "\nSala: "
                + reserva.getFuncion().getSala().getNombre()
                + "\nFecha y hora: "
                + reserva.getFuncion().getFechaHoraInicio()
                        .format(formato)
                + "\n\nEntradas:\n";

        for (Entrada entrada : reserva.getEntradas()) {

            Butaca butaca = entrada.getButaca();

            comprobante +=
                    butaca.getFila() + butaca.getNumero()
                    + " - " + entrada.getNombreTipo()
                    + " - $" + entrada.getPrecioConfirmado()
                    + "\n";
        }

        comprobante +=
                "\nTotal: $" + reserva.getImporteTotal();

        Alert alerta = new Alert(
                Alert.AlertType.INFORMATION);

        alerta.setTitle("Comprobante de reserva");
        alerta.setHeaderText("Reserva confirmada");
        alerta.setContentText(comprobante);

        alerta.showAndWait();
    }

    private boolean guardar() {

        try {

            persistencia.guardar(estado);

            return true;

        } catch (IOException e) {

            mostrarError(
                    "La reserva se registró en memoria, "
                    + "pero no se pudo guardar en el archivo. "
                    + e.getMessage());

            return false;
        }
    }

    private void mostrarError(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }
}