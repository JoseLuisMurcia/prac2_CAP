import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

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
        Datacenter centroDeDatos = null;
        try {
            centroDeDatos = new Datacenter(nombre, caracteristicas, new VmAllocationPolicyRandom(listaHosts),
                    new LinkedList<Storage>(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker("Broker_Ejercicio5");
            int uid = broker.getId();

            // Creación de máquina virtuales
            int numberOfVMs = 20;
            List<Vm> vms = new ArrayList<Vm>();
            for (int i = 0; i < numberOfVMs; i++) {
                Vm vm = new Vm(i, uid, 400, 1, 2048, 1000, 40000, "Xen", new CloudletSchedulerSpaceShared());
                vms.add(vm);
            }

            broker.submitVmList(vms);

        } catch (Exception e) {
            e.printStackTrace();
        }

        CloudSim.startSimulation();
    }
}
