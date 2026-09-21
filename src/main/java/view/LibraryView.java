package view;

import service.LibraryService;
import util.InputUtil;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

public class LibraryView {

    private LibraryService libraryService;

    private ResourceBundle bundle;

    public LibraryView(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public void showLibraryApp() throws InterruptedException {

        for (int i = 0; i < 57; i++) {
            System.out.print("-");
        }
        System.out.println();
        System.out.println("\t    " + bundle.getString("libMan"));
        //System.out.println("\t\t" + bundle.getString("libMan"));
        for (int i = 0; i < 57; i++) {
            System.out.print("-");
        }
        System.out.println();

        System.out.println();
        System.out.println(bundle.getString("libFunc"));
        System.out.println();

        //Choice
        System.out.println("1- Login");
        System.out.println("2- " + bundle.getString("libExit"));
        System.out.println("3- " + bundle.getString("libAdm"));
        for (int i = 0; i < 25; i++) {
            System.out.print("-");
        }
        System.out.println();

        var mainInput = InputUtil.input(bundle.getString("libEnter"));
        if (mainInput.equals("1")) {
            for (int i = 0; i < 3; i++) {
                boolean check = viewShowLogin();
                if (check == true) {

                    for (int j = 0; j < 57; j++) {
                        System.out.print("-");
                    }
                    System.out.println();
                    System.out.println(bundle.getString("libLog"));
                    for (int j = 0; j < 57; j++) {
                        System.out.print("-");
                    }
                    System.out.println();
                    break;
                }
                if (i == 2) {
                    System.out.println("Try Again");
                    System.exit(1);
                }
            }
            while (true) {

                System.out.println("\n" + bundle.getString("libFunc") + "\n");

                System.out.println("1- " + bundle.getString("libTitle"));
                System.out.println("2- " + bundle.getString("libIsbn"));
                System.out.println("3- " + bundle.getString("viewBorrow"));
                System.out.println("4- " + bundle.getString("bookDetail"));
                System.out.println("5- " + bundle.getString("listBook"));
                System.out.println("6- " + bundle.getString("available"));
                System.out.println("x- " + bundle.getString("libExit"));

                var inputAfterLog = InputUtil.input(bundle.getString("libEnter"));
                if (inputAfterLog.equals("1")) {
                    viewShowSearchWithTitle();
                } else if (inputAfterLog.equals("2")) {
                    viewShowSearchWithIsbn();
                } else if (inputAfterLog.equals("3")) {
                    viewShowBorrow();
                } else if (inputAfterLog.equals("4")) {
                    viewShowBookDetail();
                } else if (inputAfterLog.equals("5")) {
                    viewShowAllBook();
                } else if (inputAfterLog.equals("6")) {
                    viewShowIsAvailable();
                } else if (inputAfterLog.equals("x")) {
                    showLibraryApp();
                } else {
                    System.out.println(bundle.getString("libNot"));
                }
            }
        } else if (mainInput.equals("2")) {
            System.out.println(bundle.getString("thanks"));
            System.exit(0);
        } else if (mainInput.equals("3")) {
            System.out.print("\n");

            //lib is a password
            var enterPass = InputUtil.input(bundle.getString("libPass"));
            for (int i = 0; i < 10; i++) {
                if (enterPass.equals("lib")) {
                    for (int j = 0; j < 57; j++) {
                        System.out.print("-");
                    }
                    System.out.println();
                    System.out.println(bundle.getString("libAdmin"));
                    for (int j = 0; j < 57; j++) {
                        System.out.print("-");
                    }
                    System.out.println();
                    break;
                }

                if (i == 9) {
                    System.out.println("Erorr");
                    System.exit(2);
                }
            }

            while (true) {
                System.out.println("\n" + bundle.getString("libFunc") + "\n");

                System.out.println("1-  " + bundle.getString("libAllDetail"));
                System.out.println("2-  " + bundle.getString("addUser"));
                System.out.println("3-  " + bundle.getString("addBook"));
                System.out.println("4-  " + bundle.getString("removeBook"));
                System.out.println("5-  " + bundle.getString("updateBook"));
                System.out.println("6-  " + bundle.getString("libSeeAllUser"));
                System.out.println("7-  " + bundle.getString("libSeeLoan"));
                System.out.println("8-  " + bundle.getString("libTitle"));
                System.out.println("9-  " + bundle.getString("libIsbn"));
                System.out.println("10- " + bundle.getString("bookDetail"));
                System.out.println("11- " + bundle.getString("listBook"));
                System.out.println("12- " + bundle.getString("viewReturn"));
                System.out.println("13- " + bundle.getString("available"));
                System.out.println("x-  " + bundle.getString("libExit"));

                System.out.println();
                var inputLib = InputUtil.input(bundle.getString("libEnter"));
                if (inputLib.equals("1")) {
                    viewShowAllBookDetail();
                } else if (inputLib.equals("2")) {
                    viewShowAddUser();
                } else if (inputLib.equals("3")) {
                    viewShowAddBook();
                } else if (inputLib.equals("4")) {
                    viewShowRemoveBook();
                } else if (inputLib.equals("5")) {
                    viewShowUpdateBook();
                }  else if (inputLib.equals("6")) {
                    viewShowAllUser();
                } else if (inputLib.equals("7")) {
                    viewShowLoan();
                }  else if (inputLib.equals("8")) {
                    viewShowSearchWithTitle();
                }  else if (inputLib.equals("9")) {
                    viewShowSearchWithIsbn();
                } else if (inputLib.equals("10")) {
                    viewShowBookDetail();
                } else if (inputLib.equals("11")) {
                    viewShowAllBook();
                } else if (inputLib.equals("12")) {
                    viewShowReturnBook();
                } else if (inputLib.equals("13")) {
                    viewShowIsAvailable();
                } else if (inputLib.equals("x")) {
                    showLibraryApp();
                } else {
                    System.out.println(bundle.getString("libNot"));
                }
            }

        } else {
            System.out.println(bundle.getString("libNot"));
            showLibraryApp();
        }

    }

    public void viewShowSetLocal() {
        System.out.println("1.English");
        System.out.println("2.Indonesia");

        var input = InputUtil.input("Enter Choice");
        if (input.equals("1")) {
            Locale locale = libraryService.showSetLocal("en","US");
            bundle = ResourceBundle.getBundle("message",locale);
        } else if (input.equals("2")) {
            Locale locale = libraryService.showSetLocal("in","ID");
            bundle = ResourceBundle.getBundle("message",locale);
        } else if (input.equals("x")) {
            //cancel
        } else {
            System.out.println("Invalid");
        }
    }

    boolean viewShowLogin() throws InterruptedException {
        for (int i = 0; i < 25; i++) {
            System.out.print("-");
        }
        System.out.println();
        System.out.println("Login\n");

        var username = InputUtil.input("Enter Username");

        if (username.equals("x")) {
            return false;
            //cancel
        } else {
            var password = InputUtil.input("Enter Password");
            System.out.println();
            boolean result = libraryService.showLogin(username,password);
            if (result == false) {
                return false;
            } else {
                return true;
            }

        }
    }

    void viewShowBorrow() throws InterruptedException {
        System.out.println(bundle.getString("viewBorrow").toUpperCase() + "\n");

        System.out.println(bundle.getString("cancel"));
        var title = InputUtil.input(bundle.getString("bookTitle") + "   ");

        if (title.equals("x")) {
            //cancel
        } else {
            var time = InputUtil.input(bundle.getString("viewBorrowTime"));
            libraryService.showBorrow(title,Integer.valueOf(time));
        }
    }

    void viewShowReturnBook() throws InterruptedException {
        System.out.println(bundle.getString("viewReturn").toUpperCase() + "\n");

        System.out.println(bundle.getString("cancel"));
        var user = InputUtil.input("Username  ");

        if (user.equals("x")) {
            //cancel
        } else {
            var bookTitle = InputUtil.input(bundle.getString("bookTitle"));
            libraryService.showReturnBook(user,bookTitle);
        }
    }

    void viewShowSearchWithTitle() throws InterruptedException {
        System.out.println(bundle.getString("listBook").toUpperCase() + "\n");

        System.out.println(bundle.getString("cancel"));
        var input = InputUtil.input(bundle.getString("bookTitle"));
        if (input.equals("x")) {
            //cancel
        } else {
            libraryService.showSearchWithTitle(input);
        }
    }

    void viewShowSearchWithIsbn() throws InterruptedException {
        System.out.println(bundle.getString("listBook").toUpperCase() + "\n");

        System.out.println(bundle.getString("cancel"));
        var input = InputUtil.input(bundle.getString("bookIsbn"));
        if (input.equals("x")) {
            //cancel
        } else {
            libraryService.showSearchWithIsbn(input);
        }
    }

    void viewShowBookDetail() {
        System.out.println(bundle.getString("bookDetail").toUpperCase() + "\n");

        System.out.println(bundle.getString("cancel"));
        var input  = InputUtil.input(bundle.getString("bookTitle"));
        if (input.equals("x")) {
            //cancel
        } else {
            for (int j = 0; j < 50; j++) {
                System.out.print("-");
            }
            System.out.println();
            System.out.println("\t\t" + bundle.getString("bookDetail"));
            //System.out.println("\t\t\t\t" + bundle.getString("bookDetail"));
            libraryService.showBookDetail(input);
        }
    }

    void viewShowAllBook() {
        System.out.println(bundle.getString("listBook").toUpperCase());

        libraryService.showAllBook();
    }

    void viewShowAllBookDetail() throws InterruptedException {
        System.out.println("\t\t" + bundle.getString("bookDetail"));
        //System.out.println("\t\t\t\t" + bundle.getString("bookDetail"));

        libraryService.showAllBookDetail();
    }

    void viewShowAddUser() throws InterruptedException {
        System.out.println(bundle.getString("addUser").toUpperCase() + "\n");

        System.out.println(bundle.getString("cancel"));
        var username = InputUtil.input("username");

        if (username.equals("x")) {
            //cancel
        } else {
            var password = InputUtil.input("password");
            var address = InputUtil.input(bundle.getString("address") + " ");
            var number = InputUtil.input(bundle.getString("number") + "  ");
            var email =InputUtil.input("email   ");
            libraryService.showAddUser(username,password,address,number,email);
        }
    }

    void viewShowAddBook() throws InterruptedException {
        System.out.println(bundle.getString("addBook").toUpperCase() + "\n");

        System.out.println(bundle.getString("cancel"));
        var pbId = InputUtil.input(String.format("%-16s",bundle.getString("pbId")));

        if (pbId.equals("x")) {
            //cancel
        } else {
            var pbName = InputUtil.input(String.format("%-16s",bundle.getString("pbName")));
            var isbn = InputUtil.input(String.format("%-16s","Isbn"));
            var title = InputUtil.input(String.format("%-16s",bundle.getString("bookTitle")));
            var date = InputUtil.input(String.format("%-16s",bundle.getString("date")));
            var page = InputUtil.input(String.format("%-16s",bundle.getString("page")));
            var desc = InputUtil.input(String.format("%-16s",bundle.getString("desc")));
            var price = InputUtil.input(String.format("%-16s",bundle.getString("price")));
            var quantity = InputUtil.input(String.format("%-16s",bundle.getString("quantity")));
            var ctName = InputUtil.input(String.format("%-16s",bundle.getString("ctName")));
            var atName = InputUtil.input(String.format("%-16s",bundle.getString("atName")));

            libraryService.showAdd(pbId,pbName,isbn,title,date,Integer.valueOf(page),desc,Integer.valueOf(price),
                                   Integer.valueOf(quantity),ctName,atName);
        }

    }

    void viewShowRemoveBook() throws InterruptedException {
        System.out.println(bundle.getString("removeBook").toUpperCase() + "\n");

        System.out.println(bundle.getString("cancel"));
        var title = InputUtil.input(bundle.getString("bookTitle"));

        if (title.equals("x")) {
            //cancel
        } else {
            libraryService.showRemove(title);
        }
    }

    void viewShowUpdateBook() throws InterruptedException {
        System.out.println(bundle.getString("updateBook").toUpperCase() + "\n");

        System.out.println(bundle.getString("cancel"));
        System.out.println("(isbn,title,publisher_id,public_date,number_page,description,price,quantity)");
        var title = InputUtil.input(String.format("%-12s",bundle.getString("bookTitle")));

        if (title.equals("x")) {
            //cancel
        } else {
            var column = InputUtil.input(String.format("%-12s",bundle.getString("column")));
            var value = InputUtil.input(String.format("%-12s",bundle.getString("newValue")));

            libraryService.showUpdate(title,column,value);
        }

    }

    void viewShowAllUser() throws InterruptedException {
        System.out.println(bundle.getString("userList").toUpperCase() + "\n");

        libraryService.showAllUser();
    }

    void viewShowLoan() throws InterruptedException {
        System.out.println(bundle.getString("loanList").toUpperCase() + "\n");

        libraryService.showLoan();
    }

    void viewShowIsAvailable() {

        System.out.println(bundle.getString("cancel"));
        var input = InputUtil.input(bundle.getString("bookTitle"));
        libraryService.showIsAvailable(input);
    }

}
