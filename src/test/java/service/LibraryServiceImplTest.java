package service;

import com.zaxxer.hikari.HikariDataSource;
import entity.Author;
import entity.Book;
import entity.Category;
import entity.Publisher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.LibraryRepository;
import repository.LibraryRepositoryImpl;
import util.ConnectionUtil;

import java.util.Locale;

public class LibraryServiceImplTest {

    private LibraryRepository libraryRepository;

    private LibraryService libraryService;

    private HikariDataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = ConnectionUtil.getDataSource();

        libraryRepository = new LibraryRepositoryImpl(dataSource);

        libraryService = new LibraryServiceImpl(libraryRepository);
    }

    @Test
    void testGetLocal() {

        Locale locale = libraryService.showSetLocal("en","US");
        System.out.println(locale.getDisplayCountry());
        System.out.println(locale.getCountry());
        System.out.println(locale.getLanguage());
    }

    @Test
    void testShowLogin() {

        libraryService.showSetLocal("in","ID");
        try {
            libraryService.showLogin("ser","user");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testShowBorrowBook() throws InterruptedException {
        libraryService.showSetLocal("en","US");
        libraryService.showLogin("Alkhalifi","Alkh");

        libraryService.showBorrow("Filosofi teras",10);
    }

    @Test
    void testShowReturnBook() {
        libraryService.showSetLocal("en","US");
        try {
            libraryService.showReturnBook("Alkhalifi","Filosofi Teras");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testShowSearchTitle() throws InterruptedException {
        libraryService.showSetLocal("en","US");
        libraryService.showLogin("Alkhalifi","Alkh");

        String title = "Filosofi Teras";
        libraryService.showSearchWithTitle(title);
    }
    @Test
    void testShowSearchIsbn() throws InterruptedException {
        libraryService.showSetLocal("in","ID");
        libraryService.showLogin("Alkhalifi","Alkh");

        String isbn = "978-";
        libraryService.showSearchWithIsbn(isbn);
    }

    @Test
    void testShowBookDetail() throws InterruptedException {
        libraryService.showSetLocal("in","ID");
        libraryService.showLogin("Alkhalifi","Alkh");

        String value = "Filosofi Teras";
        libraryService.showBookDetail(value);
    }

    @Test
    void testShowAllBook() throws InterruptedException {
        libraryService.showSetLocal("en","US");
        libraryService.showLogin("Alkhalifi","Alkh");

        libraryService.showAllBook();
    }

    @Test
    void testShowAllBookDetail() throws InterruptedException {
        libraryService.showSetLocal("en","US");
        libraryService.showLogin("Alkhalifi","Alkh");

        libraryService.showAllBookDetail();
    }

    @Test
    void testShowAddUser() throws InterruptedException {
        libraryService.showSetLocal("en","US");
        libraryService.showAddUser("Alif","alif_0707","Padang","08120812","alif@gmail.com");
    }

    @Test
    void testShowAddBooks() throws InterruptedException {

        libraryService.showSetLocal("en","US");

        String pbId = "PB01";
        String pbName = "GRAMEDIA";

        String isbn = "978-602-06-3318-2";
        String title = "Atomic Habits";
        String date = "16-10-2018";
        Integer page = 352;
        String desc = "Perubahan Kecil yang Memberikan Hasil";
        Integer price = 108_000;
        Integer quantity = 1;

        String ctName = "Self Development";
        String atName = "Ary Ginanjar";

        libraryService.showAdd(pbId,pbName,isbn,title,date,page,desc,price,quantity,ctName,atName);
    }

    @Test
    void testShowRemoveBook() throws InterruptedException {

        libraryService.showSetLocal("en","US");

        String title = "Aldebaran";
        libraryService.showRemove(title);
    }

    @Test
    void testShowUpdateBook() throws InterruptedException {

        libraryService.showSetLocal("en","US");

        String isbn = "978-623-346-303-4";
        String set = "description";
        String value = "Filsafat Yunani-Romawi Kuno";
        libraryService.showUpdate(isbn,set,value);
    }

    @Test
    void testShowAllUser() throws InterruptedException {

        libraryService.showSetLocal("en","US");

        libraryService.showAllUser();
    }

    @Test
    void testShowLoan() throws InterruptedException {

        libraryService.showSetLocal("en","US");

        libraryService.showLoan();
    }

    @Test
    void testShowBookIsAvailable() {

        libraryService.showSetLocal("en","US");

        String title = "esq";
        libraryService.showIsAvailable(title);
    }

    @AfterEach
    void tearDown() {
        dataSource.close();
    }
}
