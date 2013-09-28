package com.teamsierra.csc191.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User: scott
 * Date: 9/8/13
 * Time: 8:26 PM
 */

@Document(collection = "appointments")
public class Appointment {

    @Id
    private String id;
    private String clientID;
    private String stylistID;
    private Integer startTime;
    private Integer endTime;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getStylistID() {
        return stylistID;
    }

    public void setStylistID(String stylistID) {
        this.stylistID = stylistID;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    @Override
//    public String toString() {
//        return "{" +
//                    "\"clientID\":"+ getClientID() +","+
//                    "\"stylistID\":"+ getStylistID() +","+
//                    "\"startTime\":"+ getStartTime() +","+
//                    "\"endTime\":"+ getEndTime() +","+
//                    "\"status\":\""+ getStatus() +"\""+
//                "}";
//    }
}
