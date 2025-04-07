import java.util.Random;
import javax.swing.JPanel;

public class Jugador {
    private final int TOTAL_CARTAS = 10;
    private final int MARGEN = 10;
    private final int DISTANCIA = 40;

    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Random r = new Random();

    public void repartir() {
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();

        int posicion = MARGEN + (TOTAL_CARTAS - 1) * DISTANCIA;
        for (Carta carta : cartas) {
            carta.mostrar(pnl, posicion, MARGEN);
            posicion -= DISTANCIA;
        }

        pnl.repaint();
    }

    public String getGrupos() {
        String mensaje = "No se encontraron grupos\n";
        
        int[] contadores = new int[NombreCarta.values().length];
        for (Carta carta : cartas) {
            contadores[carta.getNombre().ordinal()]++;
        }
        
        boolean hayGrupos = false;
        for (int contador : contadores) {
            if (contador >= 2) {
                hayGrupos = true;
                break;
            }
        }
        
        if (hayGrupos) {
            mensaje = "Se encontraron los siguientes grupos:\n";
            for (int i = 0; i < contadores.length; i++) {
                if (contadores[i] >= 2) {
                    mensaje += contadores[i] + " de " + NombreCarta.values()[i] + "\n";
                }
            }
        }

        mensaje += getEscaleras();
        return mensaje;
    }
    
    public String getEscaleras() {
        String mensaje = "";
        
        for (Pinta pinta : Pinta.values()) {
            int[] valores = new int[NombreCarta.values().length];
            for (Carta carta : cartas) {
                if (carta.getPinta() == pinta) {
                    valores[carta.getNombre().ordinal()] = 1;
                }
            }
            
            int contador = 0;
            int maxContador = 0;
            for (int i = 0; i < valores.length; i++) {
                if (valores[i] == 1) {
                    contador++;
                    if (contador > maxContador) {
                        maxContador = contador;
                    }
                } else {
                    contador = 0;
                }
            }
            
            if (maxContador >= 3) {
                mensaje += "Escalera de " + pinta + " con " + maxContador + " cartas\n";
            }
        }
        
        return mensaje;
    }

    public int calcularPuntaje() {
        boolean[] enGrupoOEscalera = new boolean[TOTAL_CARTAS];

        int[] contadores = new int[NombreCarta.values().length];
        for (Carta carta : cartas) {
            contadores[carta.getNombre().ordinal()]++;
        }
        
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            if (contadores[cartas[i].getNombre().ordinal()] >= 2) {
                enGrupoOEscalera[i] = true;
            }
        }
        
        for (Pinta pinta : Pinta.values()) {
            int[] valores = new int[NombreCarta.values().length];
            for (int i = 0; i < TOTAL_CARTAS; i++) {
                if (cartas[i].getPinta() == pinta) {
                    valores[cartas[i].getNombre().ordinal()] = 1;
                }
            }
            
            int contador = 0;
            for (int i = 0; i < valores.length; i++) {
                if (valores[i] == 1) {
                    contador++;
                    if (contador >= 3) {                        for (int j = 0; j < TOTAL_CARTAS; j++) {
                            if (cartas[j].getPinta() == pinta && 
                                cartas[j].getNombre().ordinal() <= i && 
                                cartas[j].getNombre().ordinal() > i - contador) {
                                enGrupoOEscalera[j] = true;
                            }
                        }
                    }
                } else {
                    contador = 0;
                }
            }
        }

        int puntaje = 0;
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            if (!enGrupoOEscalera[i]) {
                NombreCarta nombre = cartas[i].getNombre();
                switch (nombre) {
                    case AS: case JACK: case QUEEN: case KING:
                        puntaje += 10;
                        break;
                    default:
                        puntaje += nombre.ordinal() + 1;
                }
            }
        }
        return puntaje;
    } 
}
