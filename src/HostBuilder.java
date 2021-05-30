import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class HostBuilder {
    private class HostInfo {
        public int nPes;
        public int mips;
        public int ram;
        public int storage;
        public int bw;

        public HostInfo(int nPes, int mips, int ram, int storage, int bw) {
            this.nPes = nPes;
            this.mips = mips;
            this.ram = ram;
            this.storage = storage;
            this.bw = bw;
        }
    }

    private AtomicInteger createdHosts;
    private Map<String, HostInfo> cpuTypes;

    public HostBuilder() {
        this.createdHosts = new AtomicInteger();
        cpuTypes = new HashMap<String, HostInfo>();
    }

    public void defineCpuType(String name, int nPes, int mips, int ram, int storage, int bw) {
        cpuTypes.put(name, new HostInfo(nPes, mips, ram, storage, bw));
    }

    public Host buildHost(String type) {
        HostInfo hostInfo = cpuTypes.get(type);
        return buildHost(hostInfo.nPes, hostInfo.mips, hostInfo.ram, hostInfo.storage, hostInfo.bw);
    }

    private Host buildHost(int nPes, int mips, int ram, int storage, int bw) {
        List<Pe> listaCPUs = new ArrayList<Pe>();
        for (int i = 0; i < nPes; i++) {
            listaCPUs.add(new Pe(i, new PeProvisionerSimple(mips)));
        }

        return new Host(createdHosts.getAndIncrement(), new RamProvisionerSimple(ram), new BwProvisionerSimple(bw),
                storage, listaCPUs, new VmSchedulerSpaceShared(listaCPUs));
    }
}
