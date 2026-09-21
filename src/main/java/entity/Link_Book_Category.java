package entity;

public class Link_Book_Category {

    private String category_id;
    private String isbn;

    public Link_Book_Category(String category_id, String isbn) {
        this.category_id = category_id;
        this.isbn = isbn;
    }



    public Link_Book_Category() {
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
