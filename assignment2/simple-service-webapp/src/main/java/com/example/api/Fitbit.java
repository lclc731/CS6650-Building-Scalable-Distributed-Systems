package com.example.api;

import com.example.dal.StepCountsDao;
import com.example.model.StepCounts;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("fitbit")
public class Fitbit {
    StepCountsDao stepCountsDao = StepCountsDao.getInstance();

    @POST
    @Path("/{userID}/{dayID}/{timeInterval}/{stepCount}")
    @Consumes(MediaType.TEXT_PLAIN)
    public String postData(@PathParam("userID") String userID,
                           @PathParam("dayID") String dayID,
                           @PathParam("timeInterval") String timeInterval,
                           @PathParam("stepCount") String stepCount) throws SQLException {

        StepCounts stepCounts = new StepCounts(Integer.parseInt(userID),
                Integer.parseInt(dayID),
                Integer.parseInt(timeInterval),
                Integer.parseInt(stepCount));
        stepCountsDao.create(stepCounts);
        return "post success!";
    }


    @GET
    @Path("/current/{userID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getByUser(@PathParam("userID") String userID) throws SQLException {
        int sum = stepCountsDao.getStepCountCurrent(Integer.parseInt(userID));
        return "By User";
    }


    @GET
    @Path("/single/{userID}/{dayID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getByDay(@PathParam("userID") String userID,
                           @PathParam("dayID") String dayID) throws SQLException {
        int sum = stepCountsDao.getStepCountByDay(Integer.parseInt(userID), Integer.parseInt(dayID));
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
