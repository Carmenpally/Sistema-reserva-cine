package parcial.dos;

public class Principal {

    public static void main(String[] args) {
        EstadoCine estado = PersistenciaCine.cargarOInicializar(PersistenciaCine.getRutaPorDefecto());

        PeliculaService peliculaService = new PeliculaService(estado);
        SalaService salaService = new SalaService(estado);
        FuncionService funcionService = new FuncionService(estado);
        ReservaService reservaService = new ReservaService(estado);
        
        
       if (PersistenciaCine.guardar(PersistenciaCine.getRutaPorDefecto(), estado)) {
            System.out.println("Estado guardado correctamente.");
       }
       
       if (ExportadorReservaCsv.exportar(ExportadorReservaCsv.getRutaCsvPorDefecto(), estado)) {
            System.out.println("Reservas exportadas a CSV correctamente.");
        }
    }

}