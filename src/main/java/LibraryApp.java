import com.zaxxer.hikari.HikariDataSource;
import repository.LibraryRepository;
import repository.LibraryRepositoryImpl;
import service.LibraryService;
import service.LibraryServiceImpl;
import util.ConnectionUtil;
import view.LibraryView;

public class LibraryApp {
    public static void main(String[] args) {

        HikariDataSource dataSource = ConnectionUtil.getDataSource();
        LibraryRepository repository = new LibraryRepositoryImpl(dataSource);
        LibraryService service = new LibraryServiceImpl(repository);
        LibraryView view = new LibraryView(service);

        view.viewShowSetLocal();
        try {
            view.showLibraryApp();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
