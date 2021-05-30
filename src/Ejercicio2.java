import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.UtilizationModelPlanetLabInMemory;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

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
        Datacenter centroDeDatos;
        try {
            centroDeDatos = new Datacenter(nombre, caracteristicas, new VmAllocationPolicySimple(listaHosts),
                    new LinkedList<Storage>(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Creación del broker
        try {
            DatacenterBroker broker = new DatacenterBroker("Broker_Ejercicio2");
            int uid = broker.getId();

            // Creación de máquina virtuales
            int numberOfVMs = 2;
            List<Vm> vms = new ArrayList<Vm>();
            for (int i = 0; i < numberOfVMs; i++) {
                Vm vm = new Vm(i, uid, 200, 2, 1024, 100, 6144, "Xen", new CloudletSchedulerSpaceShared());
                vms.add(vm);
                host.vmCreate(vm);
            }

            broker.submitVmList(vms);

            // Creación de tareas
            int numberOfTasks = 12;
            List<Cloudlet> cloudlets = new ArrayList<Cloudlet>();
            for (int i = 0; i < numberOfTasks; i++) {
                Cloudlet cl = new Cloudlet(i, 10000, 1, 2048, 2560, new UtilizationModelStochastic(),
                        new UtilizationModelStochastic(), new UtilizationModelStochastic());
                cl.setUserId(uid);
                cloudlets.add(cl);
            }

            broker.submitCloudletList(cloudlets);

        } catch (Exception e) {
            e.printStackTrace();
        }

        CloudSim.startSimulation();
    }
}
