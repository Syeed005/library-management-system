import java.util.List;

public interface DataAccess {
    void connect(String str);

//    void saveProduct(ProductModel product);
//
//
//    //    List<ProductModel> loadAllProducts();
//    UserModel loadUser(int userID);
//    OrderModel loadOrder(int orderID);
//
//    void saveOrder(OrderModel order);
//    OrderModel cancelOrder(int orderID);
//    ProductModel updatePrice(int productID);
//    ProductModel updateQuantity(int productID);

    UserModel loadUser(String username, String password);

    void updateBook(BookModel book, UserModel currentUser);

    List<LoanRecordModel> loadLoanRecordByBook(int bookId);

    List<LoanRecordModel> loadLoanRecordByUser(int userId);

    void deleteLoadRecord(LoanRecordModel loanReturn);

    void addBook(BookModel newBook);

    BookModel loadBook(int bookId);

    void updateBookinBooks(BookModel newBook);

    List<LoanRecordModel> loadAllLoanRecord();

    void updateLoadRecord(LoanRecordModel loanReturn);

    BookModel loadBook(String bookTitle);

    BookModel loadBookByAuthor(String author);

    List<BookModel> loadAllBooks();
}
