package com.example;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 * Created by ChangLiu on 9/26/18.
 */
public class ClientEndPoint {
    private String REST_URI;
    private Client client = ClientBuilder.newClient();

    public ClientEndPoint(String uri) {
        REST_URI = uri;
    }

    public String getStatus() throws ClientErrorException {
        return client
                .target(REST_URI)
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
    }

    public <T> T postText(Object requestEntity, Class<T> responseType) throws
            ClientErrorException {
        return client
                .target(REST_URI)
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.entity(requestEntity, MediaType.TEXT_PLAIN), responseType);
    }
}
