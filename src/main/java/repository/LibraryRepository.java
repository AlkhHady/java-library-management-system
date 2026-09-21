package repository;

import entity.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface LibraryRepository {

    Locale setLocal(String lenguage, String country); // All -

    boolean login(Account account); // User -

    boolean borrow(Loan loan); // User -

    boolean returnBook(String username,String title); // User -

    List<String> searchWithTitle(String title); // All -

    List<String> searchWithIsbn(String isbn); // All -

    Map<String,String> bookDetail(String title); // All -

    List<String> allBook(); // All -

    List<String> allbookDetail(); // Lib

    boolean addUser(Account account, DataAccount dataAccount); // Lib

    boolean add(Publisher publisher,Book book, Category category, Author author); // Lib

    boolean remove(String title); // Lib

    boolean update(String title,String set,String value); // Lib

    List<String> history(); // Lib

    List<String> allUser(); // Lib

    List<String> loan(); // Lib

    boolean isAvailable(String title);

    List<String> searchWithOption(String option,String value);
}
