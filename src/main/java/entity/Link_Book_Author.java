package entity;

public class Link_Book_Author {

    private String author_id;
    private String isbn;

    public Link_Book_Author(String  author_id, String isbn) {
        this.author_id = author_id;
        this.isbn = isbn;
    }

    public Link_Book_Author() {
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
