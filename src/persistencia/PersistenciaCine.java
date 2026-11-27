package persistencia;

import modelo.EstadoCine;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PersistenciaCine {

    private final Path archivo;

    public PersistenciaCine() {

        this.archivo = Paths.get("cine.dat");
    }

    public void guardar(EstadoCine estado)
            throws IOException {

        Path temporal = Paths.get("cine.tmp");

        try (FileOutputStream fos =
                new FileOutputStream(temporal.toFile());

             ObjectOutputStream oos =
                new ObjectOutputStream(fos)) {

            oos.writeObject(estado);
        }

        Files.move(temporal, archivo,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
    }

    public EstadoCine cargar()
            throws IOException, ClassNotFoundException {

        if (Files.notExists(archivo)) {

            return new EstadoCine();
        }

        try (FileInputStream fis =
                new FileInputStream(archivo.toFile());

             ObjectInputStream ois =
                new ObjectInputStream(fis)) {

            Object objeto = ois.readObject();

            if (!(objeto instanceof EstadoCine)) {

                throw new IOException(
                        "El archivo no contiene un estado válido.");
            }

            EstadoCine estado = (EstadoCine) objeto;

            return estado;
        }
    }
}