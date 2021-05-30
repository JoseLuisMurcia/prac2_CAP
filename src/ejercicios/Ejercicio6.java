package ejercicios;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.core.CloudSim;

import builder.BrokerBuilder;
import builder.HostBuilder;
import builder.VmBuilder;

public class Ejercicio6 {
    public static void launch() {
        int numUsuarios = 3;
        Calendar calendar = Calendar.getInstance();
        boolean trace_flag = false;
        CloudSim.init(numUsuarios, calendar, trace_flag);

        HostBuilder hostBuilder = new HostBuilder();

        hostBuilder.defineCpuType("Type1", 2, 2000, 8192, 1000000, 10000);
        hostBuilder.defineCpuType("Type2", 4, 2400, 24576, 2000000, 10000);

        List<Host> listaHosts = new ArrayList<Host>();
        for (int i = 0; i < 16; i++) {
            listaHosts.add(hostBuilder.buildHost("Type1"));
        }
        for (int i = 0; i < 4; i++) {
            listaHosts.add(hostBuilder.buildHost("Type2"));
        }

        String arquitectura = "x86";
        String so = "Linux";
        String vmm = "Xen";
        String nombre = "Datacenter_0";
        double zonaHoraria = 4.0;
        double costePorSeg = 0.01;
        double costePorMem = 0.01;
        double costePorAlm = 0.003;
        double costePorBw = 0.005;

        DatacenterCharacteristics caracteristicas = new DatacenterCharacteristics(arquitectura, so, vmm, listaHosts,
                zonaHoraria, costePorSeg, costePorMem, costePorAlm, costePorBw);
        try {
            new Datacenter(nombre, caracteristicas, new VmAllocationPolicySimple(listaHosts), new LinkedList<Storage>(),
                    0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        VmBuilder vmBuilder = new VmBuilder();
        vmBuilder.defineVmType("A", 1, 2400, 3072, 120000, 1000);
        vmBuilder.defineVmType("B", 1, 2000, 2048, 80000, 1000);
        vmBuilder.defineVmType("C", 1, 1800, 1024, 60000, 1000);

        // Creación del broker
        BrokerBuilder brokerBuilder = new BrokerBuilder();

        DatacenterBroker broker_0 = brokerBuilder.buildBroker("Broker_Ejercicio6_User_0");
        brokerBuilder.assignVms(broker_0, 8, vmBuilder, "A");

        DatacenterBroker broker_1 = brokerBuilder.buildBroker("Broker_Ejercicio6_User_1");
        brokerBuilder.assignVms(broker_1, 16, vmBuilder, "B");

        DatacenterBroker broker_2 = brokerBuilder.buildBroker("Broker_Ejercicio6_User_2");
        brokerBuilder.assignVms(broker_2, 24, vmBuilder, "C");

        CloudSim.startSimulation();
    }
}
