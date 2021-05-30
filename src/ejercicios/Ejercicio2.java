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
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import builder.BrokerBuilder;
import builder.CloudletBuilder;
import builder.VmBuilder;
import logger.Logger;

public class Ejercicio2 {
    public static void launch() {
        int numUsuarios = 1;
        Calendar calendar = Calendar.getInstance();
        boolean trace_flag = false;
        CloudSim.init(numUsuarios, calendar, trace_flag);

        List<Pe> listaCPUs = new ArrayList<Pe>();
        int mips = 500;
        listaCPUs.add(new Pe(0, new PeProvisionerSimple(mips)));
        listaCPUs.add(new Pe(1, new PeProvisionerSimple(mips)));
        int hostId = 0;
        int ram = 4096;
        long almacenamiento = 20000;
        long anchoBanda = 1000;

        Host host = new Host(hostId, new RamProvisionerSimple(ram), new BwProvisionerSimple(anchoBanda), almacenamiento,
                listaCPUs, new VmSchedulerTimeShared(listaCPUs));

        List<Host> listaHosts = new ArrayList<Host>();
        listaHosts.add(host);

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

        // Creación de las VMs
        VmBuilder vmBuilder = new VmBuilder();
        vmBuilder.defineVmType("VM_A", 2, 200, 1024, 6000, 100);

        // Creación de los Cloudlets
        CloudletBuilder cloudletBuilder = new CloudletBuilder();
        cloudletBuilder.defineCloudletType("CL_A", 10000, 1, 2048, 2560);

        // Creación del broker
        BrokerBuilder brokerBuilder = new BrokerBuilder();
        DatacenterBroker broker = brokerBuilder.buildBroker("Broker_Ejercicio2");
        brokerBuilder.assignVms(broker, 2, vmBuilder, "VM_A");
        brokerBuilder.assignCloudlets(broker, 12, cloudletBuilder, "CL_A");

        CloudSim.startSimulation();

        Logger.printCloudletList(broker.getCloudletReceivedList());
    }
}
