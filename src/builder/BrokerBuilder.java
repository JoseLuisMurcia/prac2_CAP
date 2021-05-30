package builder;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
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
        List<Vm> vms = new ArrayList<Vm>();
        for (int i = 0; i < nVms; i++) {
            vms.add(builder.buildVm((type), broker.getId()));
        }
        broker.submitVmList(vms);
    }

    public void assignCloudlets(DatacenterBroker broker, int nCloudlets, CloudletBuilder builder, String type) {
        List<Cloudlet> cloudlets = new ArrayList<Cloudlet>();
        for (int i = 0; i < nCloudlets; i++) {
            Cloudlet cl = builder.buildCloudlet(type);
            cl.setUserId(broker.getId());
            cloudlets.add(cl);
        }
        broker.submitCloudletList(cloudlets);
    }
}
