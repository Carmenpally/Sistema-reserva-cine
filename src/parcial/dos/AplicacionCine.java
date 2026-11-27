package parcial.dos;

import excepciones.ReglaNegocioException;
import servicios.DatosDemo;
import persistencia.PersistenciaCine;
import modelo.EstadoCine;
import interfaz.VentanaPrincipal;
import servicios.FuncionService;
import servicios.ReservaService;
import servicios.PeliculaService;
import servicios.SalaService;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
* Código generado por la app UXFtoJava by Charly Cimino
* @see https://github.com/CharlyCimino/uxf-to-java
*/

public class AplicacionCine extends Application {

    private EstadoCine estado;
    private PersistenciaCine persistencia;
    private PeliculaService peliculaService;
    private SalaService salaService;
    private FuncionService funcionService;
    private ReservaService reservaService;

    @Override
    public void start(Stage stage) {

        persistencia = new PersistenciaCine();

        try {

            estado = persistencia.cargar();

        } catch (IOException | ClassNotFoundException e) {

            Alert alerta = new Alert(Alert.AlertType.ERROR);

            alerta.setTitle("Error de persistencia");

            alerta.setHeaderText(
                    "No se pudieron recuperar los datos.");

            alerta.setContentText(e.getMessage());

            alerta.showAndWait();

            return;
        }

        peliculaService = new PeliculaService(estado);
        salaService = new SalaService(estado);
        funcionService = new FuncionService(estado);
        reservaService = new ReservaService(estado);
        DatosDemo datosDemo = new DatosDemo(estado, peliculaService,
                salaService, funcionService, reservaService);

        try {
            if (datosDemo.cargarSiEstaVacio()) {
                persistencia.guardar(estado);
            }
        } catch (ReglaNegocioException | IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Datos de demostración");
            alerta.setHeaderText(
                    "No se pudieron cargar los datos de demostración.");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }

        VentanaPrincipal ventana = new VentanaPrincipal(
        estado,
        peliculaService,
        salaService,
        funcionService,
        reservaService,
        persistencia);
        
        Scene scene = new Scene(ventana, 1000, 650);

        stage.setTitle("Sistema de Reservas de Cine");
        stage.setScene(scene);
        stage.show();
    }
}