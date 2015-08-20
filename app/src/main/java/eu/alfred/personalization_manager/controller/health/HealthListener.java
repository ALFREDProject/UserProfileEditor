package eu.alfred.personalization_manager.controller.health;

import java.util.List;

import eu.alfred.internal.wrapper.healthmonitor.resource.Resource;

/**
 * Created by Arturo.Brotons on 18/08/2015.
 */
public interface HealthListener {
    public void notification(List<Resource> results);

    void addResource(Resource value);

    void setTotal(int total);

    void end(Boolean success);
}
