package repository;

import com.zaxxer.hikari.HikariDataSource;
import entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.ConnectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryRepositoryTest {

    private LibraryRepository libraryRepository;

    private HikariDataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = ConnectionUtil.getDataSource();

        libraryRepository = new LibraryRepositoryImpl(dataSource);
    }

    @Test
    void testLogin() {
        Account account = new Account("user","user");
        boolean result = libraryRepository.login(account);
        Assertions.assertTrue(result);

        System.out.println(account.getUsername());
    }

    @Test
    void testAddBook() {
        Book book = new Book("978-623-346-303-4","Filosofi Teras","19-08-2020",
                        298,"Filsafat Yunani",98_000,1);
        Publisher publisher = new Publisher("PB02","KOMPAS");
        Category category = new Category("Self Development");
        Author author = new Author("Henry Manampiring");

        boolean result = libraryRepository.add(publisher,book,category,author);
        Assertions.assertTrue(result);
    }


    @Test
    void testRemoveBoook() {

        String isbn = "979-97542-2-4";
        boolean result = libraryRepository.remove(isbn);

        List<String> list = new ArrayList<>();
        System.out.println(list);
        Assertions.assertFalse(result);
    }

    @Test
    void testUpdateBook() {

        String isbn = "978-602-06-3318-2";
        String set = "description";
        String value = "Perubahan Kecil Memberikan Hasil";
        boolean result = libraryRepository.update(isbn,set,value);

        Assertions.assertTrue(result);
    }

    @Test
    void testAllBook() {

        List<String> list = libraryRepository.allBook();
        list.forEach(System.out::println);
    }

    @Test
    void testAllDetailBook() {

        libraryRepository.setLocal("in","ID");

        List<String> list = libraryRepository.allbookDetail();

        list.forEach(System.out::println);
    }

    @Test
    void testBookDetail() {
        libraryRepository.setLocal("ar","SA");

        Map<String, String> map = libraryRepository.bookDetail("Atomic Habits");

        for (var value : map.keySet()) {
            System.out.println(value + " : " + map.get(value));
        }
    }

    @Test
    void testSearchWithTitle() {
        List<String> list = libraryRepository.searchWithTitle("i");
        list.forEach(System.out::println);
    }


    @Test
    void testSearchWithIsbn() {
        List<String> list = libraryRepository.searchWithIsbn("978");
        list.forEach(System.out::println);
    }

    @Test
    void testAddUser() {
        Account account = new Account("user","user");
        DataAccount dataAccount = new DataAccount("Sql","120120","user@gmail.com");

        boolean lib = libraryRepository.addUser(account,dataAccount);
        Assertions.assertFalse(lib);
    }

    @Test
    void testAddUserCorrect() {
        Account account = new Account("Alkhalifi","Alkh");
        DataAccount dataAccount = new DataAccount("Sawahlunto","167500","alkhalifi@gmail.com");

        boolean lib = libraryRepository.addUser(account,dataAccount);
        Assertions.assertTrue(lib);
    }

    @Test
    void testShowUser() {
        List<String> list = libraryRepository.allUser();

        list.forEach(System.out::println);
    }

    @Test
    void testBorrow() {
        Loan loan = new Loan("Filosofi Teras",10);
        Account account = new Account("Alkhalifi","Alkh");

        libraryRepository.login(account);
        boolean result = libraryRepository.borrow(loan);
        System.out.println(result);
    }

    @Test
    void testReturnBook() {

        String title = "Filosofi Teras";
        boolean result = libraryRepository.returnBook("Alkhalifi",title);
        System.out.println(result);
    }

    @Test
    void testSelectLoan() {

        List<String> list = libraryRepository.loan();
        list.forEach(System.out::println);
    }

    @Test
    void testBookIsAvailable() {

        String title = "bumi";
        boolean available = libraryRepository.isAvailable(title);

        Assertions.assertFalse(available);
    }

    @Test
    void testSearchWithOption() {

        String option = "author";
        String value = "Ary ";
        List<String> list = libraryRepository.searchWithOption(option, value);

        if (list != null) {
            list.forEach(System.out::println);
        } else if (list.isEmpty()){
            System.out.println("Wrong value");
        }
    }

    @AfterEach
    void tearDown() {
        dataSource.close();
    }
}
