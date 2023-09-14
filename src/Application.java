import java.sql.Connection;

public class Application {

    private static Application instance;   // Singleton pattern
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    private DataAccess dataAdapter;

    private UserModel currentUser = null;

    public UserModel getCurrentUser() { return currentUser; }

    public void setCurrentUser(UserModel user) {
        this.currentUser = user;
    }

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    public LoginScreen loginScreen = new LoginScreen();

    public LoginScreen getLoginScreen() {
        return loginScreen;
    }

    private BorrowerView bookView = new BorrowerView();
    public BorrowerView getBookView() {
        return bookView;
    }

    private AddBookView addBookView = new AddBookView();
    public AddBookView getAddBookView() {
        return addBookView;
    }

    private UpdateBookView updateBookView = new UpdateBookView();
    public UpdateBookView getUpdateBookView() {
        return updateBookView;
    }

    private AllRecordView allRecordView = new AllRecordView();
    public AllRecordView getAllRecordView() {
        return allRecordView;
    }


    private ReturnView returnView  = new ReturnView();
    public ReturnView getReturnView() {
        return returnView;
    }

    private MainScreen mainScreen = new MainScreen();

    public MainScreen getMainScreen() {
        return mainScreen;
    }

    private ManagerMainScreen managerMainScreen = new ManagerMainScreen();

    public ManagerMainScreen getManagerMainScreen() {
        return managerMainScreen;
    }

    private BorrowerController borrowerController;
    private ReturnController returnController;
    public LoginController loginController;

    private AddBookController addBookController;
    private UpdateBookController updateBookController;
    private AllRecordController allRecordController;

    private Application() {
        // create connection here
       try {
            connection = null;
            dataAdapter = new MongoDataAdapter();
        }
        catch (Exception ex) {
            System.out.println("MongoDB is not configured. System exits with error!");
            ex.printStackTrace();
            System.exit(1);
        }

        // Create data adapter here!

        borrowerController = new BorrowerController(bookView, dataAdapter);

        returnController = new ReturnController(returnView, dataAdapter);

        loginController = new LoginController(loginScreen, dataAdapter);

        addBookController = new AddBookController(addBookView, dataAdapter);

        updateBookController = new UpdateBookController(updateBookView, dataAdapter);
        allRecordController = new AllRecordController(allRecordView, dataAdapter);
    }



    public static void main(String[] args) {
        Application.getInstance().getLoginScreen().setVisible(true);
    }
}