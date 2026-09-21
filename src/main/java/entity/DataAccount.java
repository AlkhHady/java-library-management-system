package entity;

import java.time.LocalDate;

public class DataAccount {

    private String username;
    private String addres;
    private String number;
    private String email;
    private LocalDate date_created;

    public DataAccount(String username, String addres, String number, String email, LocalDate date_created) {
        this.username = username;
        this.addres = addres;
        this.number = number;
        this.email = email;
        this.date_created = date_created;
    }

    public DataAccount(String addres, String number, String email) {
        this.addres = addres;
        this.number = number;
        this.email = email;
    }

    public DataAccount() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDate_created() {
        return date_created;
    }

    public void setDate_created(LocalDate date_created) {
        this.date_created = date_created;
    }
}
