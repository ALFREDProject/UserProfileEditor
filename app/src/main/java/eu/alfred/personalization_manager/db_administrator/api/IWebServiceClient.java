package eu.alfred.personalization_manager.db_administrator.api;

import java.util.List;

/**
 * Created by Arturo.Brotons on 26/01/2015.
 */
public interface IWebServiceClient {
    List<Object> doGetRequest(String urlToCall, Class entityClass);

    String doPostRequestToCreate(String urlToPost, String jsonToPost);

    List<Object> doPostRequestToRetrieve(String urlToPost, String jsonToPost, Class entityClass);

    String doPutRequest(String urlToPut, String jsonToPut);

    String doDeleteRequest(String urlToDelete);
}
