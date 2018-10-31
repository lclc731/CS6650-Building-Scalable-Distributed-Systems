package com.example;

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

    public String postStepCount(int userId, int dayId, int interval, int stepCount) {
        String postStepCountURI = REST_URI + "/" + userId + "/" + dayId + "/" + interval + "/" + stepCount;
        return client.target(postStepCountURI)
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.entity("", MediaType.TEXT_PLAIN), String.class);
    }

    public int getSingleDay(int userId, int dayId) {
        String getSingleDayURI = REST_URI + "/single/" + userId + "/" + dayId;
        return client.target(getSingleDayURI)
                .request()
                .get(Integer.class);
    }

    public int getCurrentDay(int userId) {
        String getCurrentDayURI = REST_URI + "/current/" + userId;
        return client.target(getCurrentDayURI)
                .request()
                .get(Integer.class);
    }

    public void deleteTable() {
        String deleteTableURI = REST_URI + "/delete";
        client.target(deleteTableURI)
                .request()
                .delete();
    }
}
