package interfaz;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import modelo.EstadoCine;
import controladores.ControladorPeliculas;
import servicios.PeliculaService;
import persistencia.PersistenciaCine;
import controladores.ControladorSalas;
import servicios.SalaService;
import controladores.ControladorFunciones;
import servicios.FuncionService;
import controladores.ControladorCartelera;
import servicios.ReservaService;
import modelo.Funcion;
import controladores.ControladorReservas;
import javafx.scene.control.ScrollPane;
import controladores.ControladorConsultaReservas;

public class VentanaPrincipal extends BorderPane {

    private final EstadoCine estado;
    private final PeliculaService peliculaService;
    private final PersistenciaCine persistencia;
    private final SalaService salaService;
    private final FuncionService funcionService;
    private final ReservaService reservaService;

    public VentanaPrincipal(EstadoCine estado,
            PeliculaService peliculaService,
            SalaService salaService,
            FuncionService funcionService,
            ReservaService reservaService,
            PersistenciaCine persistencia) {
        
        this.estado = estado;
        this.peliculaService = peliculaService;
        this.salaService = salaService;
        this.funcionService = funcionService;
        this.reservaService = reservaService;
        this.persistencia = persistencia;

    crearMenu();

    mostrarInicio();
}

    private void crearMenu() {

        VBox menu = new VBox(10);

        menu.setPadding(new Insets(20));

        menu.setPrefWidth(200);

        menu.setStyle("-fx-background-color: #172437;");

        Label titulo = new Label("CINE");

        titulo.setStyle(
                "-fx-text-fill: white;"
                + "-fx-font-size: 26px;"
                + "-fx-font-weight: bold;");

        Button btnInicio = crearBoton("Inicio");

        Button btnCartelera = crearBoton("Cartelera");

        Button btnPeliculas = crearBoton("Películas");

        Button btnSalas = crearBoton("Salas");

        Button btnFunciones = crearBoton("Funciones");

        Button btnReservas = crearBoton("Reservas");

        btnInicio.setOnAction(e -> mostrarInicio());

        btnCartelera.setOnAction(e -> mostrarCartelera());

        btnPeliculas.setOnAction(e -> mostrarPeliculas());

        btnSalas.setOnAction(e -> mostrarSalas());

        btnFunciones.setOnAction(e -> mostrarFunciones());

        btnReservas.setOnAction(e -> mostrarReservas());

        menu.getChildren().addAll(
                titulo,
                btnInicio,
                btnCartelera,
                btnPeliculas,
                btnSalas,
                btnFunciones,
                btnReservas
        );

        this.setLeft(menu);
    }

    private Button crearBoton(String texto) {

        Button boton = new Button(texto);

        boton.setMaxWidth(Double.MAX_VALUE);

        boton.setPrefHeight(40);

        return boton;
    }

    private void mostrarInicio() {

        VBox contenido = new VBox(15);

        contenido.setPadding(new Insets(30));

        contenido.setAlignment(Pos.TOP_LEFT);

        Label titulo = new Label("Bienvenido al sistema de cine");

        titulo.setStyle(
                "-fx-font-size: 26px;"
                + "-fx-font-weight: bold;");

        Label peliculas = new Label(
                "Películas registradas: "
                + estado.getPeliculas().cantidad());

        Label salas = new Label(
                "Salas registradas: "
                + estado.getSalas().cantidad());

        Label funciones = new Label(
                "Funciones registradas: "
                + estado.getFunciones().cantidad());

        Label reservas = new Label(
                "Reservas registradas: "
                + estado.getReservas().cantidad());

        contenido.getChildren().addAll(
                titulo,
                peliculas,
                salas,
                funciones,
                reservas
        );

        this.setCenter(contenido);
    }

    private void mostrarSeccion(String nombre) {

        VBox contenido = new VBox(15);

        contenido.setPadding(new Insets(30));

        Label titulo = new Label(nombre);

        titulo.setStyle(
                "-fx-font-size: 26px;"
                + "-fx-font-weight: bold;");

        Label mensaje = new Label("Sección en desarrollo.");

        contenido.getChildren().addAll(titulo, mensaje);

        this.setCenter(contenido);
    }
    private void mostrarPeliculas() {
        ControladorPeliculas controlador =
            new ControladorPeliculas(
                    estado,
                    peliculaService,
                    persistencia);

    this.setCenter(controlador);
    }
    private void mostrarSalas() {
        ControladorSalas controlador =
            new ControladorSalas(
                    estado,
                    salaService,
                    persistencia);

    this.setCenter(controlador);
    }
    private void mostrarFunciones() {
        ControladorFunciones controlador = new ControladorFunciones(
                    estado,
                    peliculaService,
                    salaService,
                    funcionService,
                    persistencia);
        this.setCenter(controlador);
    }
    private void mostrarCartelera() {
        ControladorCartelera controlador =
            new ControladorCartelera(
                    peliculaService,
                    funcionService,
                    reservaService,
                    funcion -> mostrarNuevaReserva(funcion));
        this.setCenter(controlador);
    }
    private void mostrarNuevaReserva(Funcion funcion) {
        ControladorReservas controlador =
            new ControladorReservas(
                    estado,
                    funcion,
                    reservaService,
                    persistencia,
                    () -> mostrarCartelera());
        ScrollPane scroll = new ScrollPane(controlador);
        
        scroll.setFitToWidth(true);
        
        this.setCenter(scroll);
    }
    private void mostrarReservas() {
        ControladorConsultaReservas controlador =
            new ControladorConsultaReservas(
                    estado,
                    reservaService,
                    persistencia);
    this.setCenter(controlador);
    }
}