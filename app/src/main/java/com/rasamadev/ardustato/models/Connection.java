package com.rasamadev.ardustato.models;

public class Connection {
    public String id;
    public String connectionname;
    public String ip;
    public String userid;

    public Connection(String id, String connectionname, String ip, String userid) {
        this.id = id;
        this.connectionname = connectionname;
        this.ip = ip;
        this.userid = userid;
    }

    public Connection(String connectionname, String ip, String userid) {
        this.connectionname = connectionname;
        this.ip = ip;
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConnectionname() {
        return connectionname;
    }

    public void setConnectionname(String connectionname) {
        this.connectionname = connectionname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
