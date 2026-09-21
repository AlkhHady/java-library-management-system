package entity;

public class Publisher {

    private String publisher_id;
    private String publisher_name;

    public Publisher(String publisher_id, String publishet_name) {
        this.publisher_id = publisher_id;
        this.publisher_name = publishet_name;
    }

    public Publisher(String publishet_name) {
        this.publisher_name = publishet_name;
    }

    public Publisher() {
    }

    public String getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }
}
