package service;

import java.util.Locale;

public interface LibraryService {

    Locale showSetLocal(String lenguage, String country);

    boolean showLogin(String username, String password) throws InterruptedException;

    void showBorrow(String isbn, Integer date) throws InterruptedException;

    void showReturnBook(String username, String value) throws InterruptedException;

    void showSearchWithTitle(String title) throws InterruptedException;

    void showSearchWithIsbn(String isbn) throws InterruptedException;

    void showBookDetail(String value) ;

    void showAllBook();

    void showAllBookDetail() throws InterruptedException;

    void showAddUser(String username, String password, String address, String number, String email) throws InterruptedException;

    void showAdd(String pbId,String pbName,String isbn,String title,String date,Integer page,String desc,Integer price,
                 Integer quanntity,String ctName,String atName) throws InterruptedException;

    void showRemove(String title) throws InterruptedException;

    void showUpdate(String title, String set, String value) throws InterruptedException;

    void showAllUser() throws InterruptedException;

    void showLoan() throws InterruptedException;

    void showIsAvailable(String title);
}
