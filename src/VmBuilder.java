import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Vm;

public class VmBuilder {
    private class VmInfo {
        public int nPes;
        public int mips;
        public int ram;
        public int storage;
        public int bw;

        public VmInfo(int nPes, int mips, int ram, int storage, int bw) {
            this.nPes = nPes;
            this.mips = mips;
            this.ram = ram;
            this.storage = storage;
            this.bw = bw;
        }
    }

    private AtomicInteger createdVms;
    private Map<String, VmInfo> vmTypes;

    public VmBuilder() {
        createdVms = new AtomicInteger();
        vmTypes = new HashMap<String, VmInfo>();
    }

    public void defineVmType(String name, int nPes, int mips, int ram, int storage, int bw) {
        vmTypes.put(name, new VmInfo(nPes, mips, ram, storage, bw));
    }

    public Vm buildVm(String type, int brokerID) {
        VmInfo hostInfo = vmTypes.get(type);
        return buildVm(hostInfo.nPes, hostInfo.mips, hostInfo.ram, hostInfo.storage, hostInfo.bw, brokerID);
    }

    private Vm buildVm(int nPes, int mips, int ram, int storage, int bw, int brokerID) {
        return new Vm(createdVms.getAndIncrement(), brokerID, mips, nPes, ram, bw, storage, "Xen",
                new CloudletSchedulerSpaceShared());
    }
}
