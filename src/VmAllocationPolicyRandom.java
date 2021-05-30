import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;

public class VmAllocationPolicyRandom extends VmAllocationPolicySimple {
    public VmAllocationPolicyRandom(List<? extends Host> list) {
        super(list);
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        int requiredPes = vm.getNumberOfPes();
        boolean result = false;
        Set<Integer> triedHosts = new HashSet<Integer>();

        if (!getVmTable().containsKey(vm.getUid())) { // if this vm was not created
            do {// we still trying until we find a host or until we try all of them
                int idx = new Random().nextInt(getHostList().size());

                if (triedHosts.contains(idx)) {
                    continue;
                }
                triedHosts.add(idx);

                Host host = getHostList().get(idx);

                result = host.vmCreate(vm);
                if (result) { // if vm were succesfully created in the host
                    getVmTable().put(vm.getUid(), host);
                    getUsedPes().put(vm.getUid(), requiredPes);
                    result = true;
                    break;
                }

            } while (!result && triedHosts.size() < getHostList().size());
        }
        return result;
    }
}
