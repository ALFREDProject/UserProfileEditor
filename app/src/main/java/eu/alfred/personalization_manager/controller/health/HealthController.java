package eu.alfred.personalization_manager.controller.health;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import eu.alfred.internal.wrapper.healthmonitor.HealthMonitorServerException;
import eu.alfred.internal.wrapper.healthmonitor.HealthMonitorServerWrapper;
import eu.alfred.internal.wrapper.healthmonitor.resource.Resource;
import eu.alfred.personalization_manager.controller.auth.User;

/**
 * Created by Arturo.Brotons on 20/07/2015.
 */
public class HealthController {

    final public String TAG = "HealthController";

    HealthAttempt current;
    final HealthListener caller;

    public HealthController(HealthListener caller) {
        this.caller = caller;
        current = null;
    }

    public void getInfo(User user) {
        Log.d(TAG, "Health info before execute");
        current = new HealthAttempt();
        current.execute(user);
    }

    class HealthAttempt extends AsyncTask<User, Object, Boolean> {

        @Override
        protected Boolean doInBackground(User... params) {
            boolean success = true;

            try {
                User user = params[0];
                Log.d(TAG, "Health info for user: " + user);
                HealthMonitorServerWrapper wrapper = new HealthMonitorServerWrapper(user.getUserId(), user.getAccessToken());
                List<String> resourceList = wrapper.getResourceList(null);
                Log.d(TAG, "Health info: " + resourceList.size() + " resources.");
                Integer total = resourceList.size();
                publishProgress(total);
                for (String key : resourceList) {
                    try {
                        Log.d(TAG, "Health resource, grabbing: " + key + ".");
                        Resource resource = wrapper.getResource(key, null);
                        Log.d(TAG, "Health resource, grabbed: " + key + ".");
                        publishProgress(resource);
                        Log.d(TAG, "Health resource, published: " + key + ".");
                        Thread.sleep(100);

                    } catch (HealthMonitorServerException e) {
                        Log.w(TAG, e.getMessage());
                        success = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return success;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            Object value = values[0];
            if (value instanceof Integer) {
                Integer total = (Integer) value;
                Log.d(TAG, "onProgressUpdate" + total);
                caller.setTotal(total);
            } else if(value instanceof Resource) {
                Resource res = (Resource) value;
                Log.d(TAG, "onProgressUpdate" + res.toJsonString());
                caller.addResource(res);
            } else {
                Log.w(TAG, "onProgressUpdate incorrect instance " + value.toString());
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Log.d(TAG, "Health onPostExecute()");
            caller.end(success);
        }
    }

}
