package policy;

import java.util.HashMap;
import java.util.Map;

import ejercicios.Ejercicio6;

public class PolicyFailTester {
    public static int currentFailCount = 0;
    private static Map<Integer, Integer> failData = new HashMap<>();

    private static void printData() {
        for (Map.Entry<Integer, Integer> entry : failData.entrySet()) {
            System.out.println(entry.getKey() + " VMs failed -> " + entry.getValue() + " times.");
        }
    }

    public static void testPolicy(int nTests) {
        for (int i = 0; i < nTests; i++) {
            currentFailCount = 0;
            Ejercicio6.launch(); // EJERCICIO A TESTEAR
            if (failData.containsKey(currentFailCount)) {
                failData.put(currentFailCount, failData.get(currentFailCount) + 1);
            } else {
                failData.put(currentFailCount, 0);
            }
        }
        printData();
    }
}
