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
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import builder.BrokerBuilder;
import builder.CloudletBuilder;
import builder.VmBuilder;
import logger.Logger;

public class Ejercicio3 {
    public static void launch() {
        int numUsuarios = 10;
        Calendar calendar = Calendar.getInstance();
        boolean trace_flag = false;
        CloudSim.init(numUsuarios, calendar, trace_flag);

        List<Pe> listaCPUs = new ArrayList<Pe>();
        int mips = 1200;
        listaCPUs.add(new Pe(0, new PeProvisionerSimple(mips)));
        listaCPUs.add(new Pe(1, new PeProvisionerSimple(mips)));
        listaCPUs.add(new Pe(2, new PeProvisionerSimple(mips)));
        listaCPUs.add(new Pe(3, new PeProvisionerSimple(mips)));

        int ram = 24576; // 24 GB
        long almacenamiento = 2000000; // 2 TB
        long anchoBanda = 10000; // 10 Gbps

        final int NUMERO_HOSTS = 5; // Queremos 5 hosts
        Host[] host = new Host[NUMERO_HOSTS];
        List<Host> listaHosts = new ArrayList<Host>();
        for (int i = 0; i < NUMERO_HOSTS; i++) {
            host[i] = new Host(i, new RamProvisionerSimple(ram), new BwProvisionerSimple(anchoBanda), almacenamiento,
                    listaCPUs, new VmSchedulerSpaceShared(listaCPUs));
            listaHosts.add(host[i]);
        }

        String arquitectura = "x86";
        String so = "Linux";
        String vmm = "Xen";
        String nombre = "Datacenter_0";
        double zonaHoraria = 2.0;
        double costePorSeg = 0.01;
        double costePorMem = 0.005;
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

        // Creación de las VMs
        VmBuilder vmBuilder = new VmBuilder();
        vmBuilder.defineVmType("VM_A", 2, 600, 4096, 20000, 1000);

        // Creación de los Cloudlets
        CloudletBuilder cloudletBuilder = new CloudletBuilder();
        cloudletBuilder.defineCloudletType("CL_A", 45000, 1, 1024, 1536);

        List<DatacenterBroker> brokers = new ArrayList<DatacenterBroker>();
        for (int u = 0; u < numUsuarios; u++) {
            // Creación de los broker
            BrokerBuilder brokerBuilder = new BrokerBuilder();
            DatacenterBroker broker = brokerBuilder.buildBroker("Broker_Ejercicio3");
            brokerBuilder.assignVms(broker, 3, vmBuilder, "VM_A");
            brokerBuilder.assignCloudlets(broker, 15, cloudletBuilder, "CL_A");

            brokers.add(broker);
        }

        CloudSim.startSimulation();

        for (DatacenterBroker datacenterBroker : brokers) {
            Logger.printCloudletList(datacenterBroker.getCloudletReceivedList());
        }
    }
}
