package ejercicios;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import builder.BrokerBuilder;
import builder.VmBuilder;
import policy.VmAllocationPolicyRandom;

public class Ejercicio5 {
    public static void launch() {
        int numUsuarios = 1;
        Calendar calendar = Calendar.getInstance();
        boolean trace_flag = false;
        CloudSim.init(numUsuarios, calendar, trace_flag);

        final int NUMERO_HOSTS = 3; // Queremos 3 hosts
        int mips = 1200;
        int ram = 16384; // 16 GB
        long almacenamiento = 1000000; // 1 TB
        long anchoBanda = 10000; // 10 Gbps
        List<Pe>[] listaCPUs = new List[NUMERO_HOSTS];
        Host[] host = new Host[NUMERO_HOSTS];
        List<Host> listaHosts = new ArrayList<Host>();

        for (int i = 0; i < NUMERO_HOSTS; i++) {
            listaCPUs[i] = new ArrayList<Pe>();
            listaCPUs[i].add(new Pe(0, new PeProvisionerSimple(mips)));
            if (i == 1) {
                listaCPUs[i].add(new Pe(1, new PeProvisionerSimple(mips)));
                listaCPUs[i].add(new Pe(2, new PeProvisionerSimple(mips)));
                listaCPUs[i].add(new Pe(3, new PeProvisionerSimple(mips)));
            }
            host[i] = new Host(i, new RamProvisionerSimple(ram), new BwProvisionerSimple(anchoBanda), almacenamiento,
                    listaCPUs[i], new VmSchedulerTimeShared(listaCPUs[i]));
            listaHosts.add(host[i]);
        }

        String arquitectura = "x86";
        String so = "Linux";
        String vmm = "Xen";
        String nombre = "Datacenter_0";
        double zonaHoraria = 3.0;
        double costePorSeg = 0.007;
        double costePorMem = 0.005;
        double costePorAlm = 0.003;
        double costePorBw = 0.002;
        DatacenterCharacteristics caracteristicas = new DatacenterCharacteristics(arquitectura, so, vmm, listaHosts,
                zonaHoraria, costePorSeg, costePorMem, costePorAlm, costePorBw);
        try {
            new Datacenter(nombre, caracteristicas, new VmAllocationPolicyRandom(listaHosts), new LinkedList<Storage>(),
                    0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Creación de las VMs
        VmBuilder vmBuilder = new VmBuilder();
        vmBuilder.defineVmType("VM_A", 1, 400, 2048, 40000, 1000);

        // Creación del broker
        BrokerBuilder brokerBuilder = new BrokerBuilder();
        DatacenterBroker broker = brokerBuilder.buildBroker("Broker_Ejercicio5");
        brokerBuilder.assignVms(broker, 20, vmBuilder, "VM_A");

        CloudSim.startSimulation();
    }
}
