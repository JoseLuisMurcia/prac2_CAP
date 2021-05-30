package builder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModelStochastic;

public class CloudletBuilder {
    private class CloudletInfo {
        public int nInstructions;
        public int nPes;
        public int inputSize;
        public int outputSize;

        public CloudletInfo(int nInstructions, int nPes, int inputSize, int outputSize) {
            this.nInstructions = nInstructions;
            this.nPes = nPes;
            this.inputSize = inputSize;
            this.outputSize = outputSize;
        }
    }

    private AtomicInteger createdCloudlets;
    private Map<String, CloudletInfo> cloudletTypes;

    public CloudletBuilder() {
        createdCloudlets = new AtomicInteger();
        cloudletTypes = new HashMap<String, CloudletInfo>();
    }

    public void defineCloudletType(String name, int nInstructions, int nPes, int inputSize, int outputSize) {
        cloudletTypes.put(name, new CloudletInfo(nInstructions, nPes, inputSize, outputSize));
    }

    public Cloudlet buildCloudlet(String type) {
        CloudletInfo cloudletInfo = cloudletTypes.get(type);
        return buildCloudlet(cloudletInfo.nInstructions, cloudletInfo.nPes, cloudletInfo.inputSize,
                cloudletInfo.outputSize);
    }

    private Cloudlet buildCloudlet(int nInstructions, int nPes, int inputSize, int outputSize) {
        return new Cloudlet(createdCloudlets.getAndIncrement(), nInstructions, nPes, inputSize, outputSize,
                new UtilizationModelStochastic(), new UtilizationModelStochastic(), new UtilizationModelStochastic());
    }
}
