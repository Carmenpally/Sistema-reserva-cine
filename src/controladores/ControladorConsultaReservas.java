package controladores;

import modelo.EstadoCine;
import modelo.Reserva;
import modelo.Entrada;
import modelo.Butaca;
import modelo.EstadoReserva;

import servicios.ReservaService;
import persistencia.PersistenciaCine;
import persistencia.ExportadorReservas;
import excepciones.ReglaNegocioException;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControladorConsultaReservas extends VBox {

    private final EstadoCine estado;
    private final ReservaService reservaService;
    private final PersistenciaCine persistencia;

    private final ComboBox<String> cmbBusqueda;
    private final TextField txtBusqueda;

    private final TableView<Reserva> tabla;

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ControladorConsultaReservas(
            EstadoCine estado,
            ReservaService reservaService,
            PersistenciaCine persistencia) {

        this.estado = estado;
        this.reservaService = reservaService;
        this.persistencia = persistencia;

        this.cmbBusqueda = new ComboBox<>();
        this.txtBusqueda = new TextField();

        this.tabla = new TableView<>();

        this.setSpacing(15);
        this.setPadding(new Insets(25));

        crearFormulario();
        crearTabla();
        crearBotones();
        actualizarTabla();
    }

    private void crearFormulario() {

        Label titulo = new Label("Consulta de reservas");

        titulo.setStyle(
                "-fx-font-size: 26px;"
                + "-fx-font-weight: bold;");

        cmbBusqueda.getItems().addAll(
                "Código de reserva",
                "Identificación del cliente");

        cmbBusqueda.setValue("Código de reserva");

        txtBusqueda.setPromptText("Ingrese el código");

        cmbBusqueda.setOnAction(e -> {

            if (cmbBusqueda.getValue()
                    .equals("Código de reserva")) {

                txtBusqueda.setPromptText(
                        "Ingrese el código");

            } else {

                txtBusqueda.setPromptText(
                        "Ingrese la identificación");
            }
        });

        Button btnBuscar = new Button("Buscar");

        Button btnMostrarTodas =
                new Button("Mostrar todas");

        btnBuscar.setOnAction(e -> buscar());

        btnMostrarTodas.setOnAction(e -> {

            txtBusqueda.clear();

            actualizarTabla();
        });

        HBox formulario = new HBox(10);

        formulario.getChildren().addAll(
                cmbBusqueda,
                txtBusqueda,
                btnBuscar,
                btnMostrarTodas);

        this.getChildren().addAll(
                titulo, formulario);
    }

    private void crearTabla() {

        TableColumn<Reserva, String> colCodigo =
                new TableColumn<>("Código");

        colCodigo.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue().getCodigo()));

        TableColumn<Reserva, String> colCliente =
                new TableColumn<>("Cliente");

        colCliente.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue()
                                .getCliente().getNombre()));

        TableColumn<Reserva, String> colPelicula =
                new TableColumn<>("Película");

        colPelicula.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue()
                                .getFuncion()
                                .getPelicula()
                                .getTitulo()));

        TableColumn<Reserva, String> colFuncion =
                new TableColumn<>("Función");

        colFuncion.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue()
                                .getFuncion().getCodigo()));

        TableColumn<Reserva, String> colFecha =
                new TableColumn<>("Fecha y hora");

        colFecha.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue()
                                .getFuncion()
                                .getFechaHoraInicio()
                                .format(formatoFecha)));

        TableColumn<Reserva, String> colTotal =
                new TableColumn<>("Total");

        colTotal.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        String.format("$%.2f",
                                datos.getValue()
                                        .getImporteTotal())));

        TableColumn<Reserva, String> colEstado =
                new TableColumn<>("Estado");

        colEstado.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue()
                                .getEstado().toString()));

        colCodigo.setPrefWidth(65);
        colCliente.setPrefWidth(120);
        colPelicula.setPrefWidth(120);
        colFuncion.setPrefWidth(65);
        colFecha.setPrefWidth(135);
        colTotal.setPrefWidth(90);
        colEstado.setPrefWidth(110);

        tabla.getColumns().add(colCodigo);
        tabla.getColumns().add(colCliente);
        tabla.getColumns().add(colPelicula);
        tabla.getColumns().add(colFuncion);
        tabla.getColumns().add(colFecha);
        tabla.getColumns().add(colTotal);
        tabla.getColumns().add(colEstado);

        this.getChildren().add(tabla);
    }

    private void buscar() {

        try {

            String busqueda = txtBusqueda.getText();

            String tipo = cmbBusqueda.getValue();

            if (tipo.equals("Código de reserva")) {

                Reserva reserva =
                        reservaService.buscarReserva(busqueda);

                tabla.getItems().setAll(reserva);

            } else {

                tabla.getItems().setAll(
                        reservaService.buscarPorCliente(
                                busqueda));

                if (tabla.getItems().isEmpty()) {

                    mostrarMensaje(
                            "No se encontraron reservas.");
                }
            }

        } catch (ReglaNegocioException e) {

            mostrarError(e.getMessage());
        }
    }

    private void actualizarTabla() {

        tabla.getItems().setAll(
                reservaService.listarReservas());
    }

    private void crearBotones() {

        Button btnComprobante =
                new Button("Ver comprobante");

        Button btnCancelar =
                new Button("Cancelar reserva");

        Button btnExportar =
                new Button("Exportar CSV");

        btnComprobante.setOnAction(e ->
                verComprobante());

        btnCancelar.setOnAction(e ->
                cancelarReserva());

        btnExportar.setOnAction(e ->
                exportarReservas());

        HBox botones = new HBox(10);

        botones.getChildren().addAll(
                btnComprobante,
                btnCancelar,
                btnExportar);

        this.getChildren().add(botones);
    }

    private Reserva obtenerReservaSeleccionada()
            throws ReglaNegocioException {

        Reserva reserva =
                tabla.getSelectionModel().getSelectedItem();

        if (reserva == null) {

            throw new ReglaNegocioException(
                    "Debe seleccionar una reserva.");
        }

        return reserva;
    }

    private void verComprobante() {

        try {

            Reserva reserva =
                    obtenerReservaSeleccionada();

            mostrarComprobante(reserva);

        } catch (ReglaNegocioException e) {

            mostrarError(e.getMessage());
        }
    }

    private void mostrarComprobante(Reserva reserva) {

        String comprobante =
                "Código: " + reserva.getCodigo()
                + "\nCliente: "
                + reserva.getCliente().getNombre()
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
                        .format(formatoFecha)
                + "\nFecha de reserva: "
                + reserva.getFechaHoraReserva()
                        .format(formatoFecha)
                + "\nEstado: " + reserva.getEstado()
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
        alerta.setHeaderText("Detalle de la reserva");
        alerta.setContentText(comprobante);

        alerta.showAndWait();
    }

    private void cancelarReserva() {

        try {

            Reserva reserva =
                    obtenerReservaSeleccionada();

            if (reserva.getEstado()
                    == EstadoReserva.CANCELADA) {

                throw new ReglaNegocioException(
                        "La reserva ya está cancelada.");
            }

            Alert confirmacion = new Alert(
                    Alert.AlertType.CONFIRMATION);

            confirmacion.setTitle("Cancelar reserva");

            confirmacion.setHeaderText(
                    "¿Está seguro de cancelar la reserva?");

            confirmacion.setContentText(
                    "Reserva: " + reserva.getCodigo()
                    + "\nLas butacas volverán a estar disponibles.");

            ButtonType respuesta =
                    confirmacion.showAndWait()
                            .orElse(ButtonType.CANCEL);

            if (respuesta != ButtonType.OK) {

                return;
            }

            reservaService.cancelarReserva(
                    reserva.getCodigo());

            tabla.refresh();

            if (guardar()) {

                mostrarMensaje(
                        "Reserva cancelada correctamente.");
            }

        } catch (ReglaNegocioException e) {

            mostrarError(e.getMessage());
        }
    }

    private void exportarReservas() {

        try {

            ExportadorReservas exportador =
                    new ExportadorReservas();

            exportador.exportar(
                    reservaService.listarReservas());

            mostrarMensaje(
                    "Las reservas fueron exportadas "
                    + "al archivo reservas.csv.");

        } catch (IOException e) {

            mostrarError(
                    "No se pudieron exportar las reservas. "
                    + e.getMessage());
        }
    }

    private boolean guardar() {

        try {

            persistencia.guardar(estado);

            return true;

        } catch (IOException e) {

            mostrarError(
                    "Los cambios están en memoria, "
                    + "pero no se pudieron guardar. "
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

    private void mostrarMensaje(String mensaje) {

        Alert alerta = new Alert(
                Alert.AlertType.INFORMATION);

        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }
}
