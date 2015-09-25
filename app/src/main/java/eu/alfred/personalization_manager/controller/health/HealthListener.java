package eu.alfred.personalization_manager.controller.health;

import java.util.List;
import java.util.Map;

import eu.alfred.internal.wrapper.healthmonitor.resource.Resource;

/**
 * Created by Arturo.Brotons on 18/08/2015.
 */
public interface HealthListener {
    public void notification(List<Resource> results);
    public void addResource(Resource value);
    public void setTotal(int total);
    public void end(Boolean success);
    public void start();
    public Map<Resource, String> getResourceValues();
}
