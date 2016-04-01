package com.luxoft.mpp.entity.model.enumeration;

/**
 * Created by iivaniv on 18.03.2016.
 *
 */
public enum EtlStatus {

    SUCCESS("SUCCESS"),
    FAIL("FAIL");


    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    EtlStatus(String status) {
        this.status = status;

    }
}
