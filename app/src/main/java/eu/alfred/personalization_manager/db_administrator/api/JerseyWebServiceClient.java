package eu.alfred.personalization_manager.db_administrator.api;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.service.ServiceFinder;

import eu.alfred.personalization_manager.db_administrator.api.temp.AndroidServiceIteratorProvider;


public class JerseyWebServiceClient implements IWebServiceClient {

	@Override
    public List<Object> doGetRequest(String urlToCall, Class entityClass) {
		List<Object> resultList = null;
		
		 try {
                ServiceFinder.setIteratorProvider(new AndroidServiceIteratorProvider());
	            Client client = Client.create();
	            WebResource webResource = client.resource(urlToCall);  

	            // GET method
	            ClientResponse response = webResource.accept("application/json")
	            		.type("application/x-www-form-urlencoded").get(ClientResponse.class);

	            // check response status code
	            if (response.getStatus() != 200) {
	                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	            }

	            // display response
	            String resultOfRequest = response.getEntity(String.class);
	            ObjectMapper mapper = new ObjectMapper();
	            resultList = mapper.readValue(resultOfRequest,
	            		TypeFactory.defaultInstance().constructCollectionType(List.class, entityClass));
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 
		 return resultList;
	}
	
	@Override
    public String doPostRequestToCreate(String urlToPost, String jsonToPost) {
		String resultOfRequest = null;
		
		try {
            ServiceFinder.setIteratorProvider(new AndroidServiceIteratorProvider());
            Client client = Client.create();
            WebResource webResource = client.resource(urlToPost);  

            // POST method
            ClientResponse response = webResource.accept("application/json")
            		.type("application/x-www-form-urlencoded").type("application/json").post(ClientResponse.class, jsonToPost);

            // check response status code
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            // display response
            resultOfRequest = response.getEntity(String.class);
                        
            System.out.println("Output from Server .... ");
            System.out.println(resultOfRequest + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return resultOfRequest;
	}
	
	@Override
    public List<Object> doPostRequestToRetrieve(String urlToPost, String jsonToPost, Class entityClass) {
		List<Object> resultList = null;
		
		try {
            ServiceFinder.setIteratorProvider(new AndroidServiceIteratorProvider());

            Client client = Client.create();
            WebResource webResource = client.resource(urlToPost);  

            // POST method
            ClientResponse response = webResource.accept("application/json")
            		.type("application/x-www-form-urlencoded").type("application/json").post(ClientResponse.class, jsonToPost);

            // check response status code
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            // display response
            String resultOfRequest = response.getEntity(String.class);
            ObjectMapper mapper = new ObjectMapper();
            resultList = mapper.readValue(resultOfRequest,
            		TypeFactory.defaultInstance().constructCollectionType(List.class, entityClass));   

        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return resultList;
	}
	
	@Override
    public String doPutRequest(String urlToPut, String jsonToPut) {
		String resultOfRequest = null;
		
		try {
            ServiceFinder.setIteratorProvider(new AndroidServiceIteratorProvider());
            Client client = Client.create();
            WebResource webResource = client.resource(urlToPut);  

            // PUT method
            ClientResponse response = webResource.accept("application/json")
                    .type("application/x-www-form-urlencoded").type("application/json").put(ClientResponse.class, jsonToPut);

            // check response status code
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            // display response
            resultOfRequest = response.getEntity(String.class);
            System.out.println("Output from Server .... ");
            System.out.println(resultOfRequest + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return resultOfRequest;
	}
	
	@Override
    public String doDeleteRequest(String urlToDelete) {
		String resultOfRequest = null;
		
		try {
            ServiceFinder.setIteratorProvider(new AndroidServiceIteratorProvider());
            Client client = Client.create();
            WebResource webResource = client.resource(urlToDelete);  

            // DELETE method
            ClientResponse response = webResource.accept("application/json")
            		.type("application/x-www-form-urlencoded").delete(ClientResponse.class);

            // check response status code
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            // display response
            resultOfRequest = response.getEntity(String.class);
            System.out.println("Output from Server .... ");
            System.out.println(resultOfRequest + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
		return resultOfRequest;
	}
}
