package com.ecoentorno.ecomobile.model;

import java.io.Serializable;

/**
 * Created by Cristhian on 04/10/2015.
 */
public class Client implements Serializable{

    private long nit;
    private int verificationDigit;
    private String socialName;
    private String address;
    private long telephone;
    private String city;
    private char group;

    public Client(long nit, int verificationDigit, String socialName, String address, long telephone, String city, char group) {
        this.nit = nit;
        this.verificationDigit = verificationDigit;
        this.socialName = socialName;
        this.address = address;
        this.telephone = telephone;
        this.city = city;
        this.group = group;
    }

    public Client(String[] info){
        long nit = Long.parseLong(info[0]);
        int verificationDigit = Integer.parseInt(info[1]);
        String socialName = info[2];
        String address = info[3];
        long telephone = Long.parseLong(info[4]);
        String city = info[5];
        char group = info[6].charAt(0);
        this.nit = nit;
        this.verificationDigit = verificationDigit;
        this.socialName = socialName;
        this.address = address;
        this.telephone = telephone;
        this.city = city;
        this.group = group;
    }

    public long getNit() {
        return nit;
    }

    public void setNit(long nit) {
        this.nit = nit;
    }

    public int getVerificationDigit() {
        return verificationDigit;
    }

    public void setVerificationDigit(int verificationDigit) {
        this.verificationDigit = verificationDigit;
    }

    public String getSocialName() {
        return socialName;
    }

    public void setSocialName(String socialName) {
        this.socialName = socialName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTelephone() {
        return telephone;
    }

    public void setTelephone(long telephone) {
        this.telephone = telephone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGroup() {
        if(group=='G' || group=='g')
            return "GRAN CONTRIBUYENTE";
        else if(group=='P' || group=='p')
            return "PEQUENO CONTRIBUYENTE";
        else
            return "-";
    }

    public void setGroup(char group) {
        this.group = group;
    }
}
