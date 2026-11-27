package controladores;

import modelo.EstadoCine;
import modelo.Pelicula;
import modelo.Sala;
import modelo.Funcion;

import servicios.PeliculaService;
import servicios.SalaService;
import servicios.FuncionService;

import persistencia.PersistenciaCine;
import excepciones.ReglaNegocioException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControladorFunciones extends VBox {

    private final EstadoCine estado;
    private final PeliculaService peliculaService;
    private final SalaService salaService;
    private final FuncionService funcionService;
    private final PersistenciaCine persistencia;

    private final TextField txtCodigo;
    private final ComboBox<Pelicula> cmbPeliculas;
    private final ComboBox<Sala> cmbSalas;
    private final DatePicker dpFecha;
    private final TextField txtHora;
    private final TextField txtPrecio;

    private final TableView<Funcion> tabla;

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ControladorFunciones(EstadoCine estado,
            PeliculaService peliculaService,
            SalaService salaService,
            FuncionService funcionService,
            PersistenciaCine persistencia) {

        this.estado = estado;
        this.peliculaService = peliculaService;
        this.salaService = salaService;
        this.funcionService = funcionService;
        this.persistencia = persistencia;

        this.txtCodigo = new TextField();
        this.cmbPeliculas = new ComboBox<>();
        this.cmbSalas = new ComboBox<>();
        this.dpFecha = new DatePicker();
        this.txtHora = new TextField();
        this.txtPrecio = new TextField();

        this.tabla = new TableView<>();

        this.setSpacing(15);
        this.setPadding(new Insets(25));

        crearFormulario();
        crearTabla();
        cargarOpciones();
        actualizarTabla();
    }

    private void crearFormulario() {

        Label titulo = new Label(
                "Administración de funciones");

        titulo.setStyle(
                "-fx-font-size: 26px;"
                + "-fx-font-weight: bold;");

        GridPane formulario = new GridPane();

        formulario.setHgap(10);
        formulario.setVgap(10);

        txtHora.setPromptText("HH:mm");

        formulario.add(new Label("Código:"), 0, 0);
        formulario.add(txtCodigo, 1, 0);

        formulario.add(new Label("Película:"), 0, 1);
        formulario.add(cmbPeliculas, 1, 1);

        formulario.add(new Label("Sala:"), 0, 2);
        formulario.add(cmbSalas, 1, 2);

        formulario.add(new Label("Fecha:"), 2, 0);
        formulario.add(dpFecha, 3, 0);

        formulario.add(new Label("Hora:"), 2, 1);
        formulario.add(txtHora, 3, 1);

        formulario.add(new Label("Precio:"), 2, 2);
        formulario.add(txtPrecio, 3, 2);

        Button btnRegistrar = new Button("Registrar");
        Button btnModificar = new Button("Modificar precio");
        Button btnLimpiar = new Button("Limpiar");

        btnRegistrar.setOnAction(e -> registrar());

        btnModificar.setOnAction(e -> modificarPrecio());

        btnLimpiar.setOnAction(e -> limpiar());

        HBox botones = new HBox(10);

        botones.getChildren().addAll(
                btnRegistrar, btnModificar, btnLimpiar);

        this.getChildren().addAll(
                titulo, formulario, botones);
    }

    private void cargarOpciones() {

        for (Pelicula pelicula :
                peliculaService.listarPeliculas()) {

            if (pelicula.isActiva()) {

                cmbPeliculas.getItems().add(pelicula);
            }
        }

        cmbSalas.getItems().setAll(
                salaService.listarSalas());
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

        TableColumn<Funcion, String> colInicio =
                new TableColumn<>("Inicio");

        colInicio.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue().getFechaHoraInicio()
                                .format(formatoFecha)));

        TableColumn<Funcion, String> colFin =
                new TableColumn<>("Finalización");

        colFin.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue().getFechaHoraFinalizacion()
                                .format(formatoFecha)));

        TableColumn<Funcion, String> colPrecio =
                new TableColumn<>("Precio");

        colPrecio.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        String.valueOf(
                        datos.getValue().getPrecioBase())));

        colCodigo.setPrefWidth(70);
        colPelicula.setPrefWidth(140);
        colSala.setPrefWidth(120);
        colInicio.setPrefWidth(140);
        colFin.setPrefWidth(140);
        colPrecio.setPrefWidth(80);

        tabla.getColumns().add(colCodigo);
        tabla.getColumns().add(colPelicula);
        tabla.getColumns().add(colSala);
        tabla.getColumns().add(colInicio);
        tabla.getColumns().add(colFin);
        tabla.getColumns().add(colPrecio);

        tabla.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, actual) -> {

                    if (actual != null) {

                        cargarFormulario(actual);
                    }
                });

        this.getChildren().add(tabla);
    }

    private void registrar() {

        try {

            String codigo = txtCodigo.getText();

            Pelicula pelicula = cmbPeliculas.getValue();

            Sala sala = cmbSalas.getValue();

            LocalDate fecha = dpFecha.getValue();

            if (pelicula == null || sala == null
                    || fecha == null) {

                throw new ReglaNegocioException(
                        "Debe seleccionar película, sala y fecha.");
            }

            LocalTime hora = LocalTime.parse(
                    txtHora.getText().trim());

            LocalDateTime fechaHoraInicio =
                    LocalDateTime.of(fecha, hora);

            double precio = Double.parseDouble(
                    txtPrecio.getText().trim());

            funcionService.registrarFuncion(
                    codigo,
                    pelicula.getCodigo(),
                    sala.getCodigo(),
                    fechaHoraInicio,
                    precio);

            actualizarTabla();

            if (guardar()) {

                limpiar();

                mostrarMensaje("Función registrada.");
            }

        } catch (DateTimeParseException e) {

            mostrarError(
                    "La hora debe tener el formato HH:mm.");

        } catch (NumberFormatException e) {

            mostrarError(
                    "El precio debe ser un número válido.");

        } catch (ReglaNegocioException e) {

            mostrarError(e.getMessage());
        }
    }

    private void modificarPrecio() {

        try {

            Funcion funcion =
                    tabla.getSelectionModel().getSelectedItem();

            if (funcion == null) {

                throw new ReglaNegocioException(
                        "Debe seleccionar una función.");
            }

            double precio = Double.parseDouble(
                    txtPrecio.getText().trim());

            funcionService.modificarPrecio(
                    funcion.getCodigo(), precio);

            actualizarTabla();

            if (guardar()) {

                limpiar();

                mostrarMensaje("Precio modificado.");
            }

        } catch (NumberFormatException e) {

            mostrarError(
                    "El precio debe ser un número válido.");

        } catch (ReglaNegocioException e) {

            mostrarError(e.getMessage());
        }
    }

    private void cargarFormulario(Funcion funcion) {

        txtCodigo.setText(funcion.getCodigo());

        cmbPeliculas.setValue(funcion.getPelicula());

        cmbSalas.setValue(funcion.getSala());

        dpFecha.setValue(
                funcion.getFechaHoraInicio().toLocalDate());

        txtHora.setText(
                funcion.getFechaHoraInicio().toLocalTime()
                        .format(DateTimeFormatter.ofPattern("HH:mm")));

        txtPrecio.setText(
                String.valueOf(funcion.getPrecioBase()));

        txtCodigo.setEditable(false);
        cmbPeliculas.setDisable(true);
        cmbSalas.setDisable(true);
        dpFecha.setDisable(true);
        txtHora.setEditable(false);
    }

    private void actualizarTabla() {

        tabla.getItems().setAll(
                funcionService.listarFunciones());
    }

    private void limpiar() {

        tabla.getSelectionModel().clearSelection();

        txtCodigo.clear();
        cmbPeliculas.setValue(null);
        cmbSalas.setValue(null);
        dpFecha.setValue(null);
        txtHora.clear();
        txtPrecio.clear();

        txtCodigo.setEditable(true);
        cmbPeliculas.setDisable(false);
        cmbSalas.setDisable(false);
        dpFecha.setDisable(false);
        txtHora.setEditable(true);
    }

    private boolean guardar() {

        try {

            persistencia.guardar(estado);

            return true;

        } catch (IOException e) {

            mostrarError(
                    "Los cambios están en memoria, pero "
                    + "no se pudieron guardar. "
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