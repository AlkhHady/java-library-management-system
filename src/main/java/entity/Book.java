package entity;

import java.time.LocalDate;

public class Book {

    private String isbn;
    private String title;
    private String publisher_id;
    private String public_date;
    private Integer page;
    private String description;
    private Integer price;
    private Integer quantity;

    public Book(String isbn, String title, String publisher_id, String public_date, Integer page, String description, Integer price, Integer quantity) {
        this.isbn = isbn;
        this.title = title;
        this.publisher_id = publisher_id;
        this.public_date = public_date;
        this.page = page;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Book(String isbn, String title, String public_date, Integer page, String description, Integer price, Integer quantity) {
        this.isbn = isbn;
        this.title = title;
        this.public_date = public_date;
        this.page = page;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Book() {
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getPublic_date() {
        return public_date;
    }

    public void setPublic_date(String public_date) {
        this.public_date = public_date;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
