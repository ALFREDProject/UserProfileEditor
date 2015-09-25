package eu.alfred.personalization_manager.controller.health;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Map;

import eu.alfred.internal.wrapper.healthmonitor.HealthMonitorServerException;
import eu.alfred.internal.wrapper.healthmonitor.HealthMonitorServerWrapper;
import eu.alfred.internal.wrapper.healthmonitor.resource.Resource;
import eu.alfred.internal.wrapper.healthmonitor.resource.Value;
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

    public void createInfo(User user) {
        HealthCreateAttempt createAttempt = new HealthCreateAttempt();
        createAttempt.execute(user);
    }

    public void update(User user) {
        Map<Resource, String> resourceValues = caller.getResourceValues();
        HealthUpdateResources updateResources = new HealthUpdateResources(user);
        updateResources.execute(resourceValues);
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
        protected void onPreExecute() {
            Log.d(TAG, "Health onPreExecute()");
            caller.start();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Log.d(TAG, "Health onPostExecute()");
            caller.end(success);
        }
    }


    class HealthCreateAttempt extends AsyncTask<User, Object, Boolean> {

        @Override
        protected Boolean doInBackground(User... params) {
            boolean success = true;

            try {
                User user = params[0];
                Log.d(TAG, "Health info for user: " + user);
                HealthMonitorServerWrapper wrapper = new HealthMonitorServerWrapper(user.getUserId(), user.getAccessToken());
                Log.d(TAG, "Creating default resources");
                wrapper.createDefaultResourcesForCurrentUser();
                Thread.sleep(100);
                List<String> resourceList = wrapper.getResourceList(null);
                Log.d(TAG, "Health info: " + resourceList.size() + " resources.");
                Integer total = resourceList.size();
                publishProgress(total);
                for (String key : resourceList) {
                    try {
                        Log.d(TAG, "Health resource, grabbing: " + key + ".");
                        Resource resource = wrapper.getResource(key, null);
                        Log.d(TAG, "Health resource, grabbed: " + key + ".");
                        String s = resource.getResourceName();
                        Value value = null;
                        if (s.equals("birthdate")) {
                            value = new Value<String>("19/02/1972");
                        } else if (s.equals("temperature")) {
                            value = new Value<Integer>(37);
                        } else if (s.equals("respiratoryRate")) {
                            value = new Value<Integer>(21);
                        } else if (s.equals("heartrate")) {
                            value = new Value<Integer>(95);
                        } else if (s.equals("walkingspeed")) {
                            value = new Value<Integer>(40);
                        } else if (s.equals("position")) {
                            value = new Value<String>("Resting");
                        }
                        if (value != null) {
                            wrapper.updateResource(resource, value);
                            Thread.sleep(100);
                        }
                        Resource updatedResource = wrapper.getResource(key, null);
                        publishProgress(updatedResource);
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
        protected void onPreExecute() {
            Log.d(TAG, "Health onPreExecute()");
            caller.start();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Log.d(TAG, "Health onPostExecute()");
            caller.end(success);
        }
    }

    private class HealthUpdateResources extends AsyncTask<Map<Resource, String>, Object, Boolean> {
        private final User user;

        public HealthUpdateResources(User user) {
            this.user = user;
        }

        @Override
        protected Boolean doInBackground(Map<Resource, String>... params) {
            boolean success = true;
            try {
                Map<Resource, String> resourceValues = params[0];
                Log.d(TAG, "Health info for user: " + user);
                HealthMonitorServerWrapper wrapper = new HealthMonitorServerWrapper(user.getUserId(), user.getAccessToken());
                Log.d(TAG, "Creating default resources");
                for (Resource resource : resourceValues.keySet()) {
                    String rawText = resourceValues.get(resource);
                    String units = getUnits(rawText);
                    String rawValue = getRawValue(rawText);
                    Value val = null;
                    if (units != null && !units.isEmpty()) {
                        resource.setUnit(units);
                    }
                    try {
                        Integer intValue = Integer.valueOf(rawValue);
                        val = new Value<Integer>(intValue);
                    } catch (NumberFormatException e) {
                        val = new Value<String>(rawValue);
                    }
                    Log.d(TAG, "Updating resource " + resource.getResourceName()
                            + " with value: " + val.getStringValue()
                            + " and units: " + resource.getUnit());
                    Resource result = wrapper.updateResource(resource, val);
                    Log.d(TAG, "Result: " + result.toJsonString());
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
            }
            return success;
        }
    }

    private String getUnits(String rawText) {
        String units = null;
        String[] split = rawText.split(" ");
        if (split.length > 1) {
            units = split[split.length - 1];
        }

        Log.d(TAG, "rawText: '" + rawText + "' units: '" + units + "'");
        return units;
    }

    private String getRawValue(String rawText) {
        String rawValue = null;
        int i = rawText.lastIndexOf(' ');
        if (i == -1) {
            rawValue = rawText;
        } else {
            rawValue = rawText.substring(0, i);
        }
        Log.d(TAG, "rawText: '" + rawText + "' rawValue: '" + rawValue + "'");
        return rawValue;
    }
}
