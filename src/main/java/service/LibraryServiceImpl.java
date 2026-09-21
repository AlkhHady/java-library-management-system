package service;

import entity.*;
import repository.LibraryRepository;
import util.DelayUtil;

import java.text.MessageFormat;
import java.util.*;

public class LibraryServiceImpl implements LibraryService{

    private LibraryRepository libraryRepository;

    private Locale locale;

    private ResourceBundle bundle;

    public LibraryServiceImpl(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    @Override
    public Locale showSetLocal(String lenguage, String country) {
        locale = libraryRepository.setLocal(lenguage,country);
        bundle = ResourceBundle.getBundle("message",locale);
        return locale;
    }

    @Override
    public boolean showLogin(String username, String password) throws InterruptedException {
        Account account = new Account(username,password);

        boolean result = libraryRepository.login(account);
        if (result == false) {
            DelayUtil.delay(1000l);
            System.out.println(bundle.getString("falseLogin"));
            return false;
        } else {
            DelayUtil.delay(1000l);
            System.out.println(bundle.getString("trueLogin"));
            return true;
        }
    }

    @Override
    public void showBorrow(String book, Integer date) throws InterruptedException{
        Loan loan = new Loan(book,date);

        MessageFormat format = new MessageFormat(bundle.getString("descBorrow"));
        boolean resutl = libraryRepository.borrow(loan);
        if (resutl == false) {
            DelayUtil.delay(800l);
            System.out.println(bundle.getString("falseBorrow"));
        } else {
            DelayUtil.delay(800l);
            System.out.println(bundle.getString("trueBorrow"));
            System.out.println(format.format(new Object[]{book,date}));

        }

    }

    @Override
    public void showReturnBook(String username, String value) throws InterruptedException {

        boolean result = libraryRepository.returnBook(username,value);
        if (result == false) {
            DelayUtil.delay(800l);
            System.out.println(bundle.getString("falseReturn"));
        } else {
            DelayUtil.delay(800l);
            System.out.println(bundle.getString("trueReturn"));
        }
    }

    @Override
    public void showSearchWithTitle(String title) throws InterruptedException{

        List<String> list = libraryRepository.searchWithTitle(title);
        if (!list.isEmpty()) {
            DelayUtil.delay(500l);
            for (int i = 0; i < 18; i++) {
                System.out.print("-");
            }
            System.out.println();
            for (int i = 0; i < list.size(); i++) {
                if (list.size() > 1) {
                    var number = i + 1;
                    System.out.println(number + ". " + list.get(i));
                } else {
                    System.out.println(list.get(i));
                }
            }
            for (int i = 0; i < 18; i++) {
                System.out.print("-");
            }
            System.out.println();
        } else if (list.isEmpty()){
            DelayUtil.delay(500l);
            System.out.println(bundle.getString("nullListBook"));
        }
    }

    @Override
    public void showSearchWithIsbn(String isbn) throws InterruptedException {

        List<String> list = libraryRepository.searchWithIsbn(isbn);
        if (!list.isEmpty()) {
            DelayUtil.delay(500l);
            for (int i = 0; i < 37; i++) {
                System.out.print("-");
            }
            System.out.println();
            for (int i = 0; i < list.size(); i++) {
                if (list.size() > 1) {
                    var number = i + 1;
                    System.out.println(number + ". " + list.get(i));
                } else {
                    System.out.println(list.get(i));
                }
            }
            for (int i = 0; i < 37; i++) {
                System.out.print("-");
            }
            System.out.println();
        } else if (list.isEmpty()){
            DelayUtil.delay(500l);
            System.out.println(bundle.getString("nullListBook"));
        }
    }

    @Override
    public void showBookDetail(String value) {

        Map<String,String> map = libraryRepository.bookDetail(value);
        if (!map.isEmpty()) {
            for (int i = 0; i < 50; i++) {
                System.out.print("-");
            }
            System.out.println();
            for (var key : map.keySet()) {
                System.out.printf("%-15s: %s%n",key,map.get(key));
            }
            for (int i = 0; i < 50; i++) {
                System.out.print("-");
            }
            System.out.println();
        } else {
            System.out.println(bundle.getString("nullListBook"));
        }
    }

    @Override
    public void showAllBook() {

        List<String> list = libraryRepository.allBook();
        if (!list.isEmpty()) {
            for (int i = 0; i < 18; i++) {
                System.out.print("-");
            }
            System.out.println();
            for (int i = 0; i < list.size(); i++) {
                var num = i + 1;
                System.out.println(num + ". " + list.get(i));
            }
            for (int i = 0; i < 18; i++) {
                System.out.print("-");
            }
            System.out.println();
        } else {
            System.out.println(bundle.getString("listBookEmpty"));
        }
    }

    @Override
    public void showAllBookDetail() throws InterruptedException{

        List<String> list = libraryRepository.allbookDetail();
        if (!list.isEmpty()) {
            DelayUtil.delay(500l);
            for (int j = 0; j < 55; j++) {
                System.out.print("-");
            }
            System.out.println();
            for (var value : list) {
                System.out.println(value);
                Thread.sleep(200l

                );
            }
            for (int j = 0; j < 55; j++) {
                System.out.print("-");
            }
            System.out.println();
        } else {
            DelayUtil.delay(500l);
            System.out.println(bundle.getString("listBookEmpty"));
        }
    }

    @Override
    public void showAddUser(String username, String password, String address, String number, String email) throws InterruptedException{

        Account account = new Account(username,password);
        DataAccount dataAccount = new DataAccount(address,number,email);

        boolean result = libraryRepository.addUser(account,dataAccount);
        if (result == false) {
            DelayUtil.delay(900l);
            System.out.println(bundle.getString("falseUser"));
        } else {
            DelayUtil.delay(900l);
            System.out.println(bundle.getString("trueUser"));
        }
    }

    @Override
    public void showAdd(String pbId, String pbName, String isbn, String title, String date, Integer page, String desc,
                        Integer price, Integer quanntity,String ctName, String atName) throws InterruptedException {

        Publisher publisher = new Publisher(pbId,pbName);
        Book book = new Book(isbn,title,pbId,date,page,desc,price,quanntity);
        Category category = new Category(ctName);
        Author author = new Author(atName);

        boolean result = libraryRepository.add(publisher,book,category,author);
        if (result == false) {
            DelayUtil.delay(1000l);
            System.out.println(bundle.getString("falseAdd"));
        } else {
            DelayUtil.delay(1000l);
            System.out.println(bundle.getString("trueAdd"));
        }
    }

    @Override
    public void showRemove(String title) throws InterruptedException{

        boolean result = libraryRepository.remove(title);
        if (result == false) {
            DelayUtil.delay(500l);
            System.out.println(bundle.getString("falseRemove"));
        } else {
            DelayUtil.delay(500l);
            System.out.println(bundle.getString("trueRemove") + " : " + title);
        }
    }

    @Override
    public void showUpdate(String title, String set, String value) throws InterruptedException{

        boolean result = libraryRepository.update(title,set,value);
        if (result == false) {
            DelayUtil.delay(500l);
            System.out.println(bundle.getString("falseUpdate"));
        } else {
            DelayUtil.delay(500l);
            System.out.println(bundle.getString("trueUpdate"));
        }
    }

    @Override
    public void showAllUser() throws InterruptedException{

        List<String> list = libraryRepository.allUser();
        if (!list.isEmpty()) {
            DelayUtil.delay(500l);
            for (var value : list) {
                for (int i = 0; i < 20; i++) {
                    System.out.print("-");
                }
                System.out.println("\n"+value);
                for (int i = 0; i < 20; i++) {
                    System.out.print("-");
                }
                System.out.println();
                Thread.sleep(300);
            }
        } else {
            DelayUtil.delay(400l);
            System.out.println(bundle.getString("falseSeeUser"));
        }
    }

    @Override
    public void showLoan() throws InterruptedException{
        List<String> list = libraryRepository.loan();
        if (!list.isEmpty()) {
            DelayUtil.delay(800l);
            System.out.println(bundle.getString("loan"));
            for (String value : list) {
                for (int i = 0; i < 50; i++) {
                    System.out.print("-");
                }
                System.out.println();
                System.out.println(value);
                for (int i = 0; i < 50; i++) {
                    System.out.print("-");
                }
                System.out.println();
                Thread.sleep(300);
            }
        } else {
            DelayUtil.delay(400l);
            System.out.println(bundle.getString("noLoan"));
        }
    }

    @Override
    public void showIsAvailable(String title) {
        boolean available = libraryRepository.isAvailable(title);

        if (available == true) {
            System.out.println(bundle.getString("isAvailable"));
        } else {
            System.out.println(bundle.getString("isNotAvailable"));
        }
    }
}
