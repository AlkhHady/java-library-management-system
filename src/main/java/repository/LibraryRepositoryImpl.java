package repository;

import com.zaxxer.hikari.HikariDataSource;
import entity.*;
import util.ConnectionUtil;

import java.sql.*;
import java.sql.Date;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LibraryRepositoryImpl implements LibraryRepository{

    private HikariDataSource dataSource;

    private Account model;

    private Locale locale;

    public LibraryRepositoryImpl(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Locale setLocal(String lenguage, String country) {
        return locale = new Locale(lenguage,country);
    }

    @Override
    public boolean login(Account account) {
        String sql = """
                SELECT * FROM account WHERE username = ? AND password = ?
                """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,account.getUsername());
            preparedStatement.setString(2,account.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                model = new Account(resultSet.getString("username"),resultSet.getString("password"));
                return true;
            } else {
                return false;
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean isBookAvailable(String isbn) {
        String sql1 = "SELECT TITLE FROM BOOK WHERE ISBN = ?";
        String sql2 = "SELECT quantity FROM BOOK WHERE ISBN = ?";

        try(Connection connection= dataSource.getConnection();
            PreparedStatement statementTitle = connection.prepareStatement(sql1);
            PreparedStatement statementQuantity = connection.prepareStatement(sql2)) {
            statementTitle.setString(1,isbn);
            statementQuantity.setString(1,isbn);

            ResultSet resultTitle = statementTitle.executeQuery();
            ResultSet resultQuantity = statementQuantity.executeQuery();

            if (resultTitle.next() && resultQuantity.next()) {
                if (resultQuantity.getInt("quantity") != 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void minusBook(String isbn) {
        String sql = """
                UPDATE BOOK
                SET quantity = quantity - 1
                WHERE isbn = ?
                """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1,isbn);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String isbnReturn(String value) {
        String sql = "SELECT isbn FROM BOOK WHERE title = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1,value);

            ResultSet resultIsbn = statement.executeQuery();
            if (resultIsbn.next()) {
                return resultIsbn.getString("isbn");
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean borrow(Loan loan) {
        String sql = "INSERT INTO LOAN VALUES(?,?,?,?)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement mainStatement = connection.prepareStatement(sql)) {

            LocalDate localDate = LocalDate.now();
            LocalDate localDateAfter = localDate.plusDays(loan.getLastDate_loan());
            Date date = Date.valueOf(localDateAfter);
            mainStatement.setString(1, model.getUsername());
            mainStatement.setString(2,isbnReturn(loan.getBook()));
            mainStatement.setDate(3, new Date(System.currentTimeMillis()));
            mainStatement.setDate(4, date);

            if (isBookAvailable(isbnReturn(loan.getBook()))) {
                if (mainStatement.executeUpdate() != 0) {
                    minusBook(isbnReturn(loan.getBook()));
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (NumberFormatException e) {
            return false;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean returnBook(String username, String title) {
        String sqlLoan = "DELETE FROM LOAN WHERE isbn = ? AND username = ?";
        String sqlBook = "UPDATE BOOK SET quantity = quantity + 1 WHERE ISBN = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statementLoan = connection.prepareStatement(sqlLoan);
            PreparedStatement statementBook = connection.prepareStatement(sqlBook)) {
            String isbn = isbnReturn(title);

            statementLoan.setString(1,isbn);
            statementLoan.setString(2,username);
            statementBook.setString(1,isbn);

            if (statementLoan.executeUpdate() != 0 && statementBook.executeUpdate() != 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> searchWithTitle(String title) {
        String sql = "SELECT TITLE FROM BOOK WHERE TITLE LIKE ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,"%" + title + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getString("title"));
            }
            if (list != null) {
                return list;
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> searchWithIsbn(String isbn) {
        String sql = "SELECT isbn, title FROM BOOK WHERE isbn LIKE ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,"%" + isbn + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> list = new ArrayList<>();
            while (resultSet.next()) {
                String v1 = resultSet.getString("isbn");
                String v2 = resultSet.getString("title");
                list.add(v1 + " : " + v2);
            }
            if (list != null) {
                return list;
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String,String> bookDetail(String title) {
        String sql = """
                SELECT
                A.ISBN, A.TITLE, A.PUBLIC_DATE, A.NUMBER_PAGE, A.DESCRIPTION, A.PRICE, F.PUBLISHER_NAME AS 'publisher',
                GROUP_CONCAT(D.CATEGORY_NAME) AS 'category',
                GROUP_CONCAT(E.AUTHOR_NAME) AS 'author'
                FROM
                BOOK A, LINK_BOOK_CATEGORY B, LINK_BOOK_AUTHOR C, CATEGORY D, AUTHOR E, PUBLISHER F
                WHERE
                TITLE = ? AND
                A.PUBLISHER_ID = F.PUBLISHER_ID AND
                A.ISBN = B.ISBN AND
                B.CATEGORY_ID = D.CATEGORY_ID AND
                A.ISBN = C.ISBN AND
                C.AUTHOR_ID = E.AUTHOR_ID
                GROUP BY A.ISBN, A.TITLE, A.PUBLIC_DATE, A.NUMBER_PAGE, A.DESCRIPTION, A.PRICE
                """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,title);

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();

            Map<String,String> map = new LinkedHashMap<>();
            while (resultSet.next()) {
                String isbn = resultSet.getString("A.isbn");
                String titles = resultSet.getString("A.title");
                String public_date = String.valueOf(resultSet.getDate("A.public_date"));
                String numberPage = String.valueOf(resultSet.getInt("A.number_page"));
                String desc = resultSet.getString("A.description");
                String price = numberFormat.format(resultSet.getInt("A.price"));
                String publisher = resultSet.getString("publisher");
                String category = resultSet.getString("category");
                String author = resultSet.getString("author");

                map.put(metaData.getColumnName(1), isbn);
                map.put(metaData.getColumnName(2), titles);
                map.put(metaData.getColumnName(3), public_date);
                map.put(metaData.getColumnName(4), numberPage);
                map.put(metaData.getColumnName(5), desc);
                map.put(metaData.getColumnName(6), price);
                map.put(metaData.getColumnName(7), publisher);
                map.put(metaData.getColumnName(8), category);
                map.put(metaData.getColumnName(9), author);
            }

            if (map != null) {
                return map;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> allBook() {
        String sql = "SELECT title FROM BOOK";

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {

            List<String> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getString("title"));
            }
            if (list != null) {
                return list;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> allbookDetail() {
        String sql = "SELECT * FROM BOOK";

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

            List<String> list = new ArrayList<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                String isbn = String.format("%-15s: %s",metaData.getColumnName(1),resultSet.getString("isbn"));
                String title = String.format("%-15s: %s",metaData.getColumnName(2),resultSet.getString("title"));
                String publisherId = String.format("%-15s: %s",metaData.getColumnName(3),resultSet.getString("publisher_id"));
                String public_date = String.format("%-15s: %s",metaData.getColumnName(4),String.valueOf(resultSet.getDate("public_date")));
                String numberPage = String.format("%-15s: %s",metaData.getColumnName(5), String.valueOf(resultSet.getInt("number_page")));
                String desc = String.format("%-15s: %s",metaData.getColumnName(6), resultSet.getString("description"));
                String price = String.format("%-15s: %s",metaData.getColumnName(7), numberFormat.format(resultSet.getInt("price")));
                String quantity = String.format("%-15s: %s%n",metaData.getColumnName(8), String.valueOf(resultSet.getInt("quantity")));

                //list.addAll(List.of(isbn,title,publisherId,public_date,numberPage,desc,price,quantity));
                list.add(isbn+"\n"+title+"\n"+publisherId+"\n"+public_date+"\n"+numberPage+"\n"+desc+"\n"+price+"\n"+quantity+"\n");
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkUser(String username) {
        String sql = "SELECT username FROM account WHERE username = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean addUser(Account account, DataAccount dataAccount) {
        String sql1 = "INSERT INTO account VALUES(?,?)";
        String sql2 = "INSERT INTO data_account(username,address,number,email) VALUES(?,?,?,?)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedAccount = connection.prepareStatement(sql1);
            PreparedStatement preparedData = connection.prepareStatement(sql2)) {
            preparedAccount.setString(1,account.getUsername());
            preparedAccount.setString(2,account.getPassword());

            preparedData.setString(1,account.getUsername());
            preparedData.setString(2,dataAccount.getAddres());
            preparedData.setString(3,dataAccount.getNumber());
            preparedData.setString(4,dataAccount.getEmail());

            if (checkUser(account.getUsername()) == false) {
                if (preparedAccount.executeUpdate() != 0 && preparedData.executeUpdate() != 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    private List<String> checkPublisher() {
        String sql = "SELECT publisher_name FROM publisher";

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {

            List<String> list = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(resultSet.getString("publisher_name"));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean addPublisher(Publisher publisher) {
        String sql = "INSERT INTO publisher VALUES(?,?)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            connection.setAutoCommit(false);

            preparedStatement.setString(1,publisher.getPublisher_id());
            preparedStatement.setString(2,publisher.getPublisher_name());

            if (!checkPublisher().contains(publisher.getPublisher_name())) {
                int result = preparedStatement.executeUpdate();
                if (result > 0) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            } else if (checkPublisher().contains(publisher.getPublisher_name())) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException exception) {
            return false;
        }
    }

    private List<String> checkCategory() {
        String sql = "SELECT category_name FROM category";

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {

            List<String> list = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(resultSet.getString("category_name"));
            }

            if (list.isEmpty()) {
                list.add("Not Found");
                return list;
            } else {
                return list;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getCategoryId(String value) {
        String sql = "SELECT category_id FROM CATEGORY WHERE category_name = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,value);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("category_id");
                } else {
                    return -1;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean addCategory(Category category, String isbn) {
        String sql = "INSERT INTO category(category_name) VALUES(?)";
        String sql2 = "INSERT INTO link_book_category VALUES(?,?)";

        try(Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            if (!checkCategory().contains(category.getCategory_name())) {
                try(PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
                   statement.setString(1,category.getCategory_name());

                   int categoryOne = statement.executeUpdate();
                   if (categoryOne == 0) {
                       connection.rollback();
                       return false;
                   }

                   try(ResultSet resultSet = statement.getGeneratedKeys()) {
                       if (resultSet.next()) {
                           int generateKey = resultSet.getInt(1);

                           try(PreparedStatement statement2 = connection.prepareStatement(sql2)) {
                               statement2.setInt(1,generateKey);
                               statement2.setString(2,isbn);

                               int categoryTwo = statement2.executeUpdate();
                               if (categoryTwo > 0) {
                                   connection.commit();
                                   return true;
                               } else {
                                   connection.rollback();
                                   return false;
                               }
                           }

                       } else {
                           connection.rollback();
                           return false;
                       }
                   }
                } catch (SQLException e) {
                    connection.rollback();
                    throw new RuntimeException(e);
                }
            } else {
                try(PreparedStatement statement = connection.prepareStatement(sql2)) {
                    statement.setInt(1,getCategoryId(category.getCategory_name()));
                    statement.setString(2,isbn);

                    int linkResult = statement.executeUpdate();
                    if (linkResult > 0) {
                        connection.commit();
                        return true;
                    } else {
                        connection.rollback();
                        return false;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    private List<String> checkAuthor() {
        String sql = "SELECT author_name FROM author";

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {

            List<String> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getString("author_name"));
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getAuthorId(String value) {
        String sql = "SELECT author_id FROM AUTHOR WHERE author_name = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1,value);

            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("author_id");
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean addAuthor(String author,String isbn) {
        String sql = "INSERT INTO AUTHOR(author_name) VALUES(?)";
        String sql2 = "INSERT INTO LINK_BOOK_AUTHOR VALUES(?,?)";

        try(Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            if (!checkAuthor().contains(author)) {

                try(PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1,author);

                    int authorOne = statement.executeUpdate();
                    if (authorOne <= 0) {
                        connection.rollback();
                        return false;
                    }

                    try(ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            int generateKey = resultSet.getInt(1);

                            try(PreparedStatement statement2 = connection.prepareStatement(sql2)) {
                                statement2.setInt(1,generateKey);
                                statement2.setString(2,isbn);

                                int authorTwo = statement2.executeUpdate();
                                if (authorTwo > 0) {
                                    connection.commit();
                                    return true;
                                } else {
                                    connection.rollback();
                                    return false;
                                }
                            }
                        } else {
                            connection.rollback();
                            return false;
                        }
                    }
                }

            } else {
                try(PreparedStatement statement = connection.prepareStatement(sql2)) {
                    statement.setInt(1,getAuthorId(author));
                    statement.setString(2,isbn);

                    int finalResult = statement.executeUpdate();
                    if (finalResult > 0) {
                        connection.commit();
                        return true;
                    } else {
                        connection.rollback();
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean add(Publisher publisher,Book book, Category category, Author author) {
        String sql = "INSERT INTO book VALUES(?,?,?,?,?,?,?,?)";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(book.getPublic_date(),formatter);
        Date date = Date.valueOf(localDate);

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql)){

            preparedStatement1.setString(1,book.getIsbn());
            preparedStatement1.setString(2,book.getTitle());
            preparedStatement1.setString(3,publisher.getPublisher_id());
            preparedStatement1.setDate(4,date);
            preparedStatement1.setInt(5,book.getPage());
            preparedStatement1.setString(6,book.getDescription());
            preparedStatement1.setInt(7,book.getPrice());
            preparedStatement1.setInt(8,book.getQuantity());

            addPublisher(publisher);

            int resultBook = preparedStatement1.executeUpdate();

            if (resultBook > 0) {
                addAuthor(author.getAuthor_name(), book.getIsbn());
                addCategory(category,book.getIsbn());
                return true;
            } else {
                return false;
            }
        } catch (SQLException exception) {
            return false;
        }
    }


    @Override
    public boolean remove(String title) {

        String sql = "DELETE FROM BOOK WHERE isbn = ?";
        String sql2 = "DELETE FROM link_book_category WHERE isbn = ?";
        String sql3 = "DELETE FROM link_book_author WHERE isbn = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            PreparedStatement preparedStatement3 = connection.prepareStatement(sql3)) {

            String isbn = isbnReturn(title);
            if (isbn == null) {
                return false;
            }

            preparedStatement.setString(1,isbn);
            preparedStatement2.setString(1,isbn);
            preparedStatement3.setString(1,isbn);

            preparedStatement2.executeUpdate();
            preparedStatement3.executeUpdate();
            if (preparedStatement.executeUpdate() != 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(String title,String set,String value) {
        String sql = """
                UPDATE BOOK 
                SET {0} = ?
                WHERE isbn = ?
                """;
        MessageFormat messageFormatat = new MessageFormat(sql);
        String result = messageFormatat.format(new Object[] {set});

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(result)) {

            String isbn = isbnReturn(title);

            if (set.equals("quantity") || set.equals("number_page") || set.equals("price")) {
                Integer temp = Integer.valueOf(value);
                preparedStatement.setInt(1,temp);
            } else {
                preparedStatement.setString(1,value);
            }
            preparedStatement.setString(2,isbn);

            if (preparedStatement.executeUpdate() != 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLSyntaxErrorException | NumberFormatException e) {
            return false;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> history() {
        return null;
    }

    @Override
    public List<String> allUser() {
        String sql = "SELECT * FROM DATA_ACCOUNT";

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {

            List<String> list = new ArrayList<>();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String address = resultSet.getString("address");
                String number = resultSet.getString("number");
                String email = resultSet.getString("email");
                String date = String.valueOf(resultSet.getDate("date_created"));
                list.add(String.format("%s%n%s%n%s%n%s%n%s",username,address,number,email,date));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> loan() {
        String sql = "SELECT * FROM LOAN";

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {

            ResourceBundle bundle = ResourceBundle.getBundle("message");

            List<String> list = new ArrayList<>();
            while (resultSet.next()) {
                String username = String.format("%-25s: ", "Username") + resultSet.getString("username");
                String isbn = String.format("%-25s: ","Isbn") + resultSet.getString("isbn");
                String firstDate = String.format("%-25s: ",bundle.getString("fLoan")) +
                        resultSet.getDate("date_loan").toLocalDate().toString();
                String lastDate = String.format("%-25s: ",bundle.getString("lLoan")) +
                        resultSet.getDate("lastdate_loan").toLocalDate().toString();
                list.add(username + "\n" + isbn + "\n" + firstDate + "\n" + lastDate);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isAvailable(String title) {
        String sql = "SELECT quantity FROM BOOK WHERE title = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1,title);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getInt("quantity") != 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> searchWithOption(String option, String value) {

        if (option.equals("CATEGORY") || option.equals("category")) {
            String sql = """
                    SELECT A.TITLE
                    FROM BOOK A, LINK_BOOK_CATEGORY B, CATEGORY C
                    WHERE C.CATEGORY_NAME = ? AND
                    A.ISBN = B.ISBN AND 
                    B.CATEGORY_ID = C.CATEGORY_ID                   
                    """;
            try(Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1,value);

                try(ResultSet resultSet = statement.executeQuery()) {
                    List<String> tmp = new ArrayList<>();
                    if (resultSet.next()) {
                        while (resultSet.next()) {
                            tmp.add(resultSet.getString("title"));
                        }
                    } else {
                        return null;
                    }
                    return tmp;
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (option.equals("AUTHOR") || option.equals("author")) {
            String sql = """
                    SELECT A.TITLE
                    FROM BOOK A, LINK_BOOK_AUTHOR B, AUTHOR C
                    WHERE C.AUTHOR_NAME = ? AND
                    A.ISBN = B.ISBN AND 
                    B.AUTHOR_ID = C.AUTHOR_ID                   
                    """;
            try(Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1,value);

                try(ResultSet resultSet = statement.executeQuery()) {
                    List<String> tmp = new ArrayList<>();
                    while (resultSet.next()) {
                        tmp.add(resultSet.getString("title"));
                    }
                    if (tmp != null) {
                        return tmp;
                    } else {
                        return null;
                    }

                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            List<String> emp = Collections.emptyList();
            return emp;
        }
    }
}

