package controladores;

import modelo.EstadoCine;
import modelo.Sala;
import modelo.Butaca;
import servicios.SalaService;
import persistencia.PersistenciaCine;
import excepciones.ReglaNegocioException;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControladorSalas extends VBox {

    private final EstadoCine estado;
    private final SalaService salaService;
    private final PersistenciaCine persistencia;

    private final TextField txtCodigo;
    private final TextField txtNombre;
    private final TextField txtFilas;
    private final TextField txtButacasPorFila;

    private final TableView<Sala> tabla;

    private final GridPane panelButacas;

    public ControladorSalas(EstadoCine estado,
            SalaService salaService,
            PersistenciaCine persistencia) {

        this.estado = estado;
        this.salaService = salaService;
        this.persistencia = persistencia;

        this.txtCodigo = new TextField();
        this.txtNombre = new TextField();
        this.txtFilas = new TextField();
        this.txtButacasPorFila = new TextField();

        this.tabla = new TableView<>();

        this.panelButacas = new GridPane();

        this.setSpacing(15);
        this.setPadding(new Insets(25));

        crearFormulario();
        crearTabla();
        crearPanelButacas();
        actualizarTabla();
    }

    private void crearFormulario() {

        Label titulo = new Label(
                "Administración de salas");

        titulo.setStyle(
                "-fx-font-size: 26px;"
                + "-fx-font-weight: bold;");

        GridPane formulario = new GridPane();

        formulario.setHgap(10);
        formulario.setVgap(10);

        formulario.add(new Label("Código:"), 0, 0);
        formulario.add(txtCodigo, 1, 0);

        formulario.add(new Label("Nombre:"), 0, 1);
        formulario.add(txtNombre, 1, 1);

        formulario.add(new Label("Filas:"), 0, 2);
        formulario.add(txtFilas, 1, 2);

        formulario.add(
                new Label("Butacas por fila:"), 0, 3);

        formulario.add(txtButacasPorFila, 1, 3);

        Button btnRegistrar = new Button("Registrar");
        Button btnLimpiar = new Button("Limpiar");

        btnRegistrar.setOnAction(e -> registrar());
        btnLimpiar.setOnAction(e -> limpiar());

        HBox botones = new HBox(10);

        botones.getChildren().addAll(
                btnRegistrar, btnLimpiar);

        this.getChildren().addAll(
                titulo, formulario, botones);
    }

    private void crearTabla() {

        TableColumn<Sala, String> colCodigo =
                new TableColumn<>("Código");

        colCodigo.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue().getCodigo()));

        TableColumn<Sala, String> colNombre =
                new TableColumn<>("Nombre");

        colNombre.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue().getNombre()));

        TableColumn<Sala, String> colFilas =
                new TableColumn<>("Filas");

        colFilas.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        String.valueOf(
                        datos.getValue().getFilas())));

        TableColumn<Sala, String> colButacas =
                new TableColumn<>("Butacas por fila");

        colButacas.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        String.valueOf(
                        datos.getValue().getButacasPorFila())));

        TableColumn<Sala, String> colCapacidad =
                new TableColumn<>("Capacidad");

        colCapacidad.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        String.valueOf(
                        datos.getValue().getCapacidad())));

        colCodigo.setPrefWidth(80);
        colNombre.setPrefWidth(180);
        colFilas.setPrefWidth(70);
        colButacas.setPrefWidth(120);
        colCapacidad.setPrefWidth(100);

        tabla.getColumns().add(colCodigo);
        tabla.getColumns().add(colNombre);
        tabla.getColumns().add(colFilas);
        tabla.getColumns().add(colButacas);
        tabla.getColumns().add(colCapacidad);

        tabla.setPrefHeight(160);

        tabla.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, actual) -> {

                    if (actual != null) {
                        mostrarButacas(actual);
                    }
                });

        this.getChildren().add(tabla);
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
        panelButacas.setPadding(new Insets(10));
        panelButacas.setAlignment(Pos.CENTER);

        ScrollPane scroll = new ScrollPane(panelButacas);

        scroll.setFitToWidth(true);
        scroll.setPrefHeight(180);

        this.getChildren().addAll(pantalla, scroll);
    }

    private void mostrarButacas(Sala sala) {

        panelButacas.getChildren().clear();

        for (Butaca butaca : sala.getButacas()) {

            int fila = butaca.getFila().charAt(0) - 'A';

            int columna = butaca.getNumero() - 1;

            String identificador =
                    butaca.getFila() + butaca.getNumero();

            Label asiento = new Label(identificador);

            asiento.setPrefSize(40, 35);

            asiento.setAlignment(Pos.CENTER);

            asiento.setStyle(
                    "-fx-background-color: #dbeafe;"
                    + "-fx-border-color: #93c5fd;"
                    + "-fx-background-radius: 5px;"
                    + "-fx-border-radius: 5px;");

            panelButacas.add(asiento, columna, fila);
        }
    }

    private void registrar() {

        try {

            String codigo = txtCodigo.getText();
            String nombre = txtNombre.getText();

            int filas = Integer.parseInt(
                    txtFilas.getText().trim());

            int butacasPorFila = Integer.parseInt(
                    txtButacasPorFila.getText().trim());

            Sala sala = salaService.registrarSala(
                    codigo, nombre, filas, butacasPorFila);

            actualizarTabla();

            if (guardar()) {

                limpiar();

                tabla.getSelectionModel().select(sala);

                mostrarMensaje("Sala registrada.");
            }

        } catch (NumberFormatException e) {

            mostrarError(
                    "Las filas y butacas deben ser números enteros.");

        } catch (ReglaNegocioException e) {

            mostrarError(e.getMessage());
        }
    }

    private void actualizarTabla() {

        tabla.getItems().setAll(
                salaService.listarSalas());
    }

    private void limpiar() {

        txtCodigo.clear();
        txtNombre.clear();
        txtFilas.clear();
        txtButacasPorFila.clear();

        tabla.getSelectionModel().clearSelection();

        panelButacas.getChildren().clear();
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