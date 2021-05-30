public class Launcher {
    static int exercise = 3;

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
        }
    }
}
