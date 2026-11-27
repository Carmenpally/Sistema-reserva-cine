package parcial.dos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PersistenciaCine {

    private static final String RUTA_POR_DEFECTO = "datos/estado_cine.ser";

    public static EstadoCine cargarOInicializar(String rutaArchivo) {
        Path ruta = Paths.get(rutaArchivo);
        if (!Files.exists(ruta)) {
            return crearEstadoInicial();
        }

        try (ObjectInputStream estado = new ObjectInputStream(Files.newInputStream(ruta))) {
            Object obj = estado.readObject();
            if (!(obj instanceof EstadoCine)) {
                System.out.println("El archivo '" + rutaArchivo + "' no contiene un EstadoCine válido.");
                return crearEstadoInicial();
            }
            return (EstadoCine) obj;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se pudo leer el archivo '" + rutaArchivo + "': " + e.getMessage());
            return crearEstadoInicial();
        }
    }

    public static boolean guardar(String rutaArchivo, EstadoCine estado) {
        if (estado == null) {
            System.out.println("Error al guardar: el estado del cine es nulo.");
            return false;
        }

        Path destino = Paths.get(rutaArchivo);
        try {
            Path carpetaPrincipal = destino.getParent();
            if (carpetaPrincipal != null) {
                Files.createDirectories(carpetaPrincipal);
            }

            Path temporal = destino.resolveSibling(destino.getFileName().toString() + ".tmp");
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(temporal))) {
                oos.writeObject(estado);
            }

            Files.move(temporal, destino, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.out.println("Error al guardar el estado en '" + rutaArchivo + "': " + e.getMessage());
            return false;
        }
    }

    private static EstadoCine crearEstadoInicial() {
        return new EstadoCine();
    }

    public static String getRutaPorDefecto() {
        return RUTA_POR_DEFECTO;
    }
}
