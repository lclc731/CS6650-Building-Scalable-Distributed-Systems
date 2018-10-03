package com.example;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("fitbit")
public class Fitbit {

    @POST
    @Path("/{userID}/{day}/{timeInterval}/{stepCount}")
    @Consumes(MediaType.TEXT_PLAIN)
    public String postData(@PathParam("userID") String userID,
                           @PathParam("day") String day,
                           @PathParam("timeInterval") String timeInterval,
                           @PathParam("stepCount") String stepCount) {
        return "post success!";
    }

    @GET
    @Path("/current/{userID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getByUser(@PathParam("userID") String userID) {
        return "By User";
    }

    @GET
    @Path("/single/{userID}/{day}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getByDay(@PathParam("userID") String userID,
                           @PathParam("day") String day) {
        return "By Day";
    }

    @GET
    @Path("/range/{userID}/{startDay}/{numDays}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getByRange(@PathParam("userID") String day,
                             @PathParam("startDay") String timeInterval,
                             @PathParam("numDays") String stepCount) {
        return "By Range";
    }
}
