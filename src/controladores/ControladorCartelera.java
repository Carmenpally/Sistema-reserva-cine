package controladores;

import modelo.Pelicula;
import modelo.Funcion;
import modelo.Butaca;

import servicios.PeliculaService;
import servicios.FuncionService;
import servicios.ReservaService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControladorCartelera extends VBox {

    private final PeliculaService peliculaService;
    private final FuncionService funcionService;
    private final ReservaService reservaService;

    private final Consumer<Funcion> accionReservar;

    private final ComboBox<Pelicula> cmbPeliculas;
    private final DatePicker dpFecha;
    private final TableView<Funcion> tabla;

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ControladorCartelera(
            PeliculaService peliculaService,
            FuncionService funcionService,
            ReservaService reservaService,
            Consumer<Funcion> accionReservar) {

        this.peliculaService = peliculaService;
        this.funcionService = funcionService;
        this.reservaService = reservaService;
        this.accionReservar = accionReservar;

        this.cmbPeliculas = new ComboBox<>();
        this.dpFecha = new DatePicker();
        this.tabla = new TableView<>();

        this.setSpacing(15);
        this.setPadding(new Insets(25));

        crearFiltros();
        crearTabla();
        crearBotones();
        cargarPeliculas();
        actualizarTabla();
    }

    private void crearFiltros() {

        Label titulo = new Label("Cartelera");

        titulo.setStyle(
                "-fx-font-size: 26px;"
                + "-fx-font-weight: bold;");

        cmbPeliculas.setPromptText("Todas las películas");

        dpFecha.setPromptText("Todas las fechas");
        dpFecha.setEditable(false);

        Button btnBuscar = new Button("Buscar");

        Button btnLimpiar = new Button("Limpiar filtros");

        btnBuscar.setOnAction(e -> actualizarTabla());

        btnLimpiar.setOnAction(e -> limpiarFiltros());

        HBox filtros = new HBox(10);

        filtros.getChildren().addAll(
                cmbPeliculas,
                dpFecha,
                btnBuscar,
                btnLimpiar
        );

        this.getChildren().addAll(titulo, filtros);
    }

    private void cargarPeliculas() {

        cmbPeliculas.getItems().setAll(
                peliculaService.listarPeliculas());
    }

    private void crearTabla() {

        TableColumn<Funcion, String> colCodigo =
                new TableColumn<>("Código");

        colCodigo.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue().getCodigo()));

        TableColumn<Funcion, String> colPelicula =
                new TableColumn<>("Película");

        colPelicula.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue().getPelicula().getTitulo()));

        TableColumn<Funcion, String> colSala =
                new TableColumn<>("Sala");

        colSala.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue().getSala().getNombre()));

        TableColumn<Funcion, String> colFecha =
                new TableColumn<>("Fecha y hora");

        colFecha.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue().getFechaHoraInicio()
                                .format(formatoFecha)));

        TableColumn<Funcion, String> colPrecio =
                new TableColumn<>("Precio");

        colPrecio.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        String.valueOf(
                        datos.getValue().getPrecioBase())));

        TableColumn<Funcion, String> colDisponibles =
                new TableColumn<>("Disponibles");

        colDisponibles.setCellValueFactory(datos -> {

            Funcion funcion = datos.getValue();

            int disponibles = contarDisponibles(funcion);

            return new SimpleStringProperty(
                    String.valueOf(disponibles));
        });

        colCodigo.setPrefWidth(75);
        colPelicula.setPrefWidth(140);
        colSala.setPrefWidth(120);
        colFecha.setPrefWidth(140);
        colPrecio.setPrefWidth(80);
        colDisponibles.setPrefWidth(100);

        tabla.getColumns().add(colCodigo);
        tabla.getColumns().add(colPelicula);
        tabla.getColumns().add(colSala);
        tabla.getColumns().add(colFecha);
        tabla.getColumns().add(colPrecio);
        tabla.getColumns().add(colDisponibles);

        this.getChildren().add(tabla);
    }

    private void actualizarTabla() {

        tabla.getItems().clear();

        Pelicula peliculaSeleccionada =
                cmbPeliculas.getValue();

        LocalDate fechaSeleccionada =
                dpFecha.getValue();

        for (Funcion funcion :
                funcionService.listarFunciones()) {

            if (funcion.comenzo()) {
                continue;
            }

            if (peliculaSeleccionada != null) {

                if (!funcion.getPelicula()
                        .equals(peliculaSeleccionada)) {

                    continue;
                }
            }

            if (fechaSeleccionada != null) {

                LocalDate fechaFuncion =
                        funcion.getFechaHoraInicio()
                                .toLocalDate();

                if (!fechaFuncion.equals(fechaSeleccionada)) {

                    continue;
                }
            }

            tabla.getItems().add(funcion);
        }
    }

    private int contarDisponibles(Funcion funcion) {

        int disponibles = 0;

        for (Butaca butaca :
                funcion.getSala().getButacas()) {

            if (!reservaService.estaOcupada(funcion, butaca)) {

                disponibles++;
            }
        }

        return disponibles;
    }

    private void limpiarFiltros() {

        cmbPeliculas.setValue(null);
        dpFecha.setValue(null);

        actualizarTabla();
    }

    private void crearBotones() {

        Button btnReservar =
                new Button("Reservar butacas");

        btnReservar.setOnAction(e -> reservar());

        this.getChildren().add(btnReservar);
    }

    private void reservar() {

        Funcion funcion =
                tabla.getSelectionModel().getSelectedItem();

        if (funcion == null) {

            mostrarError("Debe seleccionar una función.");

            return;
        }

        if (funcion.comenzo()) {

            mostrarError("La función ya ha comenzado.");

            actualizarTabla();

            return;
        }

        if (contarDisponibles(funcion) == 0) {

            mostrarError("No hay butacas disponibles.");

            actualizarTabla();

            return;
        }

        accionReservar.accept(funcion);
    }

    private void mostrarError(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }
}