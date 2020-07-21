package com.example.samaritan_yourneedmate;

public class UserInformation {
    String name;
    String phno;
    String dob;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public UserInformation() {
    }

    public UserInformation(String name, String phno, String dob) {
        this.name = name;
        this.phno = phno;
        this.dob = dob;
    }
}
