package entity;

import java.time.LocalDate;

public class Loan {

    private String username;
    private String book;
    private LocalDate date_loan;
    private Integer lastDate_loan;

    public Loan(String username, String isbn, LocalDate date_loan, Integer lastDate_loan) {
        this.username = username;
        this.book = isbn;
        this.date_loan = date_loan;
        this.lastDate_loan = lastDate_loan;
    }

    public Loan(String username, String isbn, Integer lastDate_loan) {
        this.username = username;
        this.book = isbn;
        this.lastDate_loan = lastDate_loan;
    }

    public Loan(String isbn, Integer lastDate_loan) {
        this.book = isbn;
        this.lastDate_loan = lastDate_loan;
    }

    public Loan() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public LocalDate getDate_loan() {
        return date_loan;
    }

    public void setDate_loan(LocalDate date_loan) {
        this.date_loan = date_loan;
    }

    public Integer getLastDate_loan() {
        return lastDate_loan;
    }

    public void setLastDate_loan(Integer lastDate_loan) {
        this.lastDate_loan = lastDate_loan;
    }
}
