import ejercicios.Ejercicio1;
import ejercicios.Ejercicio2;
import ejercicios.Ejercicio3;
import ejercicios.Ejercicio4;
import ejercicios.Ejercicio5;
import ejercicios.Ejercicio6;

public class Launcher {
    static int exercise = 6;

    public static void main(String[] args) {
        switch (exercise) {
            case 1:
                Ejercicio1.launch();
                break;
            case 2:
                Ejercicio2.launch();
                break;
            case 3:
                Ejercicio3.launch();
                break;
            case 4:
                Ejercicio4.launch();
                break;
            case 5:
                Ejercicio5.launch();
                break;
            case 6:
                Ejercicio6.launch();
                break;
        }
    }
}
