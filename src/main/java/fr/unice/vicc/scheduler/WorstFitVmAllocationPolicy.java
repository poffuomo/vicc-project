package fr.unice.vicc.scheduler;

import fr.unice.vicc.WorstHostCompare;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arsha on 16-Feb-17.
 * 
 * Scheduler purpose: the aim of this scheduler is to allocate new VMs into (one of) the
 * 		hosts with the highest remaining free resources. In this case the dimensions considered
 * 		for ranking the available free resources are the MIPS and the RAM of the machines.
 * Design choice: in order to consider the two dimensions at the same time, the metric we used
 * 		is to consider a linear combination of the two dimensions but to give more weight to the
 * 		host's available CPU than to available memory, as implemented in {@link WorstHostCompare#compare}
 * 		class. 
 * Worst-case temporal complexity: O(N) where N is the number of hosts since in the worst
 * 		case all the list of hosts has to be checked.
 */
public class WorstFitVmAllocationPolicy extends VmAllocationPolicy {

    /** The map to track the server that host each running VM. */
    private Map<Vm, Host> hoster;

    public WorstFitVmAllocationPolicy(List<? extends Host> hosts) {
        super(hosts);
        hoster = new HashMap<>();
    }

    @Override
    protected void setHostList(List<? extends Host> hostList) {
        super.setHostList(hostList);
        hoster = new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> hosts) {
        // no optimizations here
        return null;
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
    	// get the list of available hosts, ordered with the criterion of "more available resources"
	    List<Host> hosts = getHostList();
	    hosts.sort(new WorstHostCompare());
	    
	    // try to allocate on the hosts given by the ordered list
	    Host best = hosts.get(0);
    	if (best.vmCreate(vm)) {
	        hoster.put(vm, best);
//		        System.out.println("VM " + best.getId() + " allocated");
	        return true;
    	}

        // no appropriate host found!
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
    	// nothing special here
        if (host.vmCreate(vm)) {
            hoster.put(vm, host);
            return true;
        }
        
        // no such allocation possible
        return false;
    }

    @Override
    public void deallocateHostForVm(Vm vm) {
        Host hostToRemove = vm.getHost();
        hostToRemove.vmDestroy(vm);
        hoster.remove(vm, hostToRemove);
    }

    @Override
    public Host getHost(Vm vm) {
        return vm.getHost();
    }

    @Override
    public Host getHost(int vmId, int userId) {
        for (Host h: getHostList()) {
            if (h.getVm(vmId,userId) != null){
                return h;
            }
        }
        
        // no such host
        return null;
    }
}
