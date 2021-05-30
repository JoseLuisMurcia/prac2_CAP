import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

public class BrokerBuilder {
    public DatacenterBroker buildBroker(String name) {
        DatacenterBroker broker = null;

        try {
            broker = new DatacenterBroker(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return broker;
    }

    public void assignVms(DatacenterBroker broker, int nVms, VmBuilder builder, String type) {
        int numberOfVMs = nVms;
        List<Vm> vms = new ArrayList<Vm>();
        for (int i = 0; i < numberOfVMs; i++) {
            vms.add(builder.buildVm((type), broker.getId()));
        }
        broker.submitVmList(vms);
    }
}
