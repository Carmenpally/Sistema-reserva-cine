/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

import modelo.EstadoCine;
import modelo.Pelicula;
import servicios.PeliculaService;
import persistencia.PersistenciaCine;
import excepciones.ReglaNegocioException;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
/**
 *
 * @author max_p
 */

public class ControladorPeliculas extends VBox {

    private final EstadoCine estado;
    private final PeliculaService peliculaService;
    private final PersistenciaCine persistencia;

    private final TextField txtCodigo;
    private final TextField txtTitulo;
    private final TextField txtDuracion;

    private final TableView<Pelicula> tabla;

    public ControladorPeliculas(EstadoCine estado,
            PeliculaService peliculaService,
            PersistenciaCine persistencia) {

        this.estado = estado;
        this.peliculaService = peliculaService;
        this.persistencia = persistencia;

        this.txtCodigo = new TextField();
        this.txtTitulo = new TextField();
        this.txtDuracion = new TextField();

        this.tabla = new TableView<>();

        this.setSpacing(15);
        this.setPadding(new Insets(25));

        crearFormulario();

        crearTabla();

        actualizarTabla();
    }

    private void crearFormulario() {

        Label titulo = new Label(
                "Administración de películas");

        titulo.setStyle(
                "-fx-font-size: 26px;"
                + "-fx-font-weight: bold;");

        GridPane formulario = new GridPane();

        formulario.setHgap(10);
        formulario.setVgap(10);

        formulario.add(new Label("Código:"), 0, 0);
        formulario.add(txtCodigo, 1, 0);

        formulario.add(new Label("Título:"), 0, 1);
        formulario.add(txtTitulo, 1, 1);

        formulario.add(new Label("Duración:"), 0, 2);
        formulario.add(txtDuracion, 1, 2);

        Button btnRegistrar = new Button("Registrar");
        Button btnModificar = new Button("Modificar");
        Button btnDesactivar = new Button("Desactivar");
        Button btnLimpiar = new Button("Limpiar");

        btnRegistrar.setOnAction(e -> registrar());

        btnModificar.setOnAction(e -> modificar());

        btnDesactivar.setOnAction(e -> desactivar());

        btnLimpiar.setOnAction(e -> limpiar());

        HBox botones = new HBox(10);

        botones.getChildren().addAll(
                btnRegistrar,
                btnModificar,
                btnDesactivar,
                btnLimpiar
        );

        this.getChildren().addAll(
                titulo, formulario, botones);
    }

    private void crearTabla() {

        TableColumn<Pelicula, String> colCodigo =
                new TableColumn<>("Código");

        colCodigo.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue().getCodigo()));

        TableColumn<Pelicula, String> colTitulo =
                new TableColumn<>("Título");

        colTitulo.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        datos.getValue().getTitulo()));

        TableColumn<Pelicula, String> colDuracion =
                new TableColumn<>("Duración");

        colDuracion.setCellValueFactory(datos ->
                new SimpleStringProperty(
                        String.valueOf(
                        datos.getValue().getDuracionMinutos())));

        TableColumn<Pelicula, String> colEstado =
                new TableColumn<>("Estado");

        colEstado.setCellValueFactory(datos -> {

            Pelicula pelicula = datos.getValue();

            String texto;

            if (pelicula.isActiva()) {
                texto = "Activa";
            } else {
                texto = "Inactiva";
            }

            return new SimpleStringProperty(texto);
        });

        colCodigo.setPrefWidth(100);
        colTitulo.setPrefWidth(250);
        colDuracion.setPrefWidth(100);
        colEstado.setPrefWidth(100);

        tabla.getColumns().add(colCodigo);
        tabla.getColumns().add(colTitulo);
        tabla.getColumns().add(colDuracion);
        tabla.getColumns().add(colEstado);

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
            String titulo = txtTitulo.getText();

            int duracion = Integer.parseInt(
                    txtDuracion.getText().trim());

            peliculaService.registrarPelicula(
                    codigo, titulo, duracion);

            actualizarTabla();

            if (guardar()) {
                limpiar();
                mostrarMensaje("Película registrada.");
            }

        } catch (NumberFormatException e) {

            mostrarError(
                    "La duración debe ser un número entero.");

        } catch (ReglaNegocioException e) {

            mostrarError(e.getMessage());
        }
    }

    private void modificar() {

        try {

            Pelicula seleccionada =
                    tabla.getSelectionModel().getSelectedItem();

            if (seleccionada == null) {
                throw new ReglaNegocioException(
                        "Debe seleccionar una película.");
            }

            String codigo = seleccionada.getCodigo();
            String titulo = txtTitulo.getText();

            int duracion = Integer.parseInt(
                    txtDuracion.getText().trim());

            peliculaService.modificarPelicula(
                    codigo, titulo, duracion);

            actualizarTabla();

            if (guardar()) {
                limpiar();
                mostrarMensaje("Película modificada.");
            }

        } catch (NumberFormatException e) {

            mostrarError(
                    "La duración debe ser un número entero.");

        } catch (ReglaNegocioException e) {

            mostrarError(e.getMessage());
        }
    }

    private void desactivar() {

        try {

            Pelicula seleccionada =
                    tabla.getSelectionModel().getSelectedItem();

            if (seleccionada == null) {
                throw new ReglaNegocioException(
                        "Debe seleccionar una película.");
            }

            peliculaService.desactivarPelicula(
                    seleccionada.getCodigo());

            actualizarTabla();

            if (guardar()) {
                limpiar();
                mostrarMensaje("Película desactivada.");
            }

        } catch (ReglaNegocioException e) {

            mostrarError(e.getMessage());
        }
    }

    private void cargarFormulario(Pelicula pelicula) {

        txtCodigo.setText(pelicula.getCodigo());

        txtTitulo.setText(pelicula.getTitulo());

        txtDuracion.setText(
                String.valueOf(pelicula.getDuracionMinutos()));

        txtCodigo.setEditable(false);
    }

    private void actualizarTabla() {

        tabla.getItems().setAll(
                peliculaService.listarPeliculas());
    }

    private void limpiar() {

        tabla.getSelectionModel().clearSelection();

        txtCodigo.clear();
        txtTitulo.clear();
        txtDuracion.clear();

        txtCodigo.setEditable(true);
    }

    private boolean guardar() {

        try {

            persistencia.guardar(estado);

            return true;

        } catch (IOException e) {

            mostrarError(
                    "Los cambios están en memoria, pero no "
                    + "se pudieron guardar en el archivo. "
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
