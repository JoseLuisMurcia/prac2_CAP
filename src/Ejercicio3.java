import java.text.DecimalFormat;
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
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

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
        Datacenter centroDeDatos = null;
        try {
            centroDeDatos = new Datacenter(nombre, caracteristicas, new VmAllocationPolicySimple(listaHosts),
                    new LinkedList<Storage>(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Creación del broker
        List<DatacenterBroker> brokers = new ArrayList<DatacenterBroker>();
        for (int u = 0; u < numUsuarios; u++) {
            DatacenterBroker broker = null;
            try {
                broker = new DatacenterBroker("Broker_Ejercicio3_User_" + String.valueOf(u));
                int uid = broker.getId();
                Log.print(uid);

                // Creación de máquina virtuales
                int numberOfVMs = 3;
                List<Vm> vms = new ArrayList<Vm>();
                for (int i = 0; i < numberOfVMs; i++) {
                    Vm vm = new Vm(i, uid, 600, 2, 4096, 1000, 20000, "Xen", new CloudletSchedulerSpaceShared());
                    vms.add(vm);
                }

                broker.submitVmList(vms);

                // Creación de tareas
                int numberOfTasks = 15;
                List<Cloudlet> cloudlets = new ArrayList<Cloudlet>();
                for (int i = 0; i < numberOfTasks; i++) {
                    Cloudlet cl = new Cloudlet(i, 45000, 1, 1024, 1536, new UtilizationModelStochastic(),
                            new UtilizationModelStochastic(), new UtilizationModelStochastic());
                    cl.setUserId(uid);
                    cloudlets.add(cl);
                }

                broker.submitCloudletList(cloudlets);

            } catch (Exception e) {
                e.printStackTrace();
            }

            brokers.add(broker);
        }

        CloudSim.startSimulation();

        for (DatacenterBroker datacenterBroker : brokers) {
            printCloudletList(datacenterBroker.getCloudletReceivedList());
        }
    }

    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID" + indent + "Time"
                + indent + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId()
                        + indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent
                        + dft.format(cloudlet.getExecStartTime()) + indent + indent
                        + dft.format(cloudlet.getFinishTime()));
            }
        }
    }
}
