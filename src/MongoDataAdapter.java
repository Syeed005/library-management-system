import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoDataAdapter implements DataAccess{
    private MongoClient mongoClient = null;
    private static String conStr = "mongodb+srv://syeedsat:password1234@cluster0.zod6sqk.mongodb.net/?";
    @Override
    public void connect(String str) {
        if (mongoClient != null)
            mongoClient.close();
        mongoClient = new MongoClient(new MongoClientURI(str));
    }

    @Override
    public BookModel loadBook(int bookId) {
        this.connect(conStr);
        BookModel book = null;
        try{
            MongoDatabase database = mongoClient.getDatabase("library_management");
            MongoCollection<Document> collection = database.getCollection("books");
            MongoCursor<Document> cursor = collection.find(eq("_id", bookId)).iterator();
            while(cursor.hasNext()) {
                Document doc = cursor.next();
                book = new BookModel();
                book.BookId = doc.getInteger("_id");
                book.Title = doc.getString("Title");
                book.ISBN = doc.getString("ISBN");
                book.LoanValue = doc.getInteger("LoanValue");
                List<Document> authors = doc.getList("Author", Document.class);
                if (authors != null) {
                    book.Author = new ArrayList<>();
                    for (Document author : authors) {
                        book.Author.add(author.getString("Name"));
                    }
                }
                Object copies = doc.get("Copies");

                if (copies != null) {
                    book.Copies = new ArrayList<>();
                    List<?> intArray = (List<?>) copies;
                    // Assuming the elements in the array are integers, you can convert them to an int array
                    int[] intArrayPrimitive = intArray.stream().mapToInt(obj -> (int) obj).toArray();
                    for (Object copy : intArray)
                         //copy.getInteger("_id");
                        if (copy instanceof Integer)
                            book.Copies.add((Integer)copy);
                    }
            }
        }catch (Exception ex){

        }
        return book;
    }

    @Override
    public void updateBookinBooks(BookModel newBook) {
        Document doc =  new Document()
                .append("_id",newBook.BookId)
                .append("Title",newBook.Title)
                .append("ISBN", newBook.ISBN)
                .append("Copies", newBook.Copies);

        BasicDBList dbl = new BasicDBList();
        for(String name:newBook.Author){
            dbl.add(new BasicDBObject("Name",name));
        }
        doc.append("Author", dbl);

        this.connect(conStr);
        MongoDatabase database = mongoClient.getDatabase("library_management");
        MongoCollection<Document> collection = database.getCollection("books");

        Document filter = new Document("_id", newBook.BookId);
        collection.replaceOne(filter, doc);
    }

    @Override
    public List<LoanRecordModel> loadAllLoanRecord() {
        List<LoanRecordModel> loanBooks= new ArrayList<>();
        this.connect(conStr);
        MongoDatabase database = mongoClient.getDatabase("library_management");
        MongoCollection<Document> collection = database.getCollection("loan_records");
        MongoCursor<Document> cursor = collection.find().iterator();

        while (cursor.hasNext()) {
            Document doc = cursor.next();
            LoanRecordModel loan = new LoanRecordModel();
            loan.LoadRecordId = doc.getObjectId("_id");
            loan.BorrowDate = doc.getString("BorrowedDate");
            loan.DueDate = doc.getString("DueDate");
            loan.BorrowerId = loadUser(doc.getInteger("BorrowerId"));
            loan.BookId = loadBook(doc.getInteger("BookId"));
            loanBooks.add(loan);
        }
        return  loanBooks;
    }

    @Override
    public void updateLoadRecord(LoanRecordModel loanReturn) {
        this.connect(conStr);
        MongoDatabase database = mongoClient.getDatabase("library_management");
        MongoCollection<Document> collection = database.getCollection("loan_records");
        Document filter = new Document("_id", loanReturn.LoadRecordId);
        Document update = new Document("$set", new Document("DueDate", loanReturn.DueDate));
        collection.updateOne(filter, update);
    }

    @Override
    public BookModel loadBook(String bookTitle) {
        this.connect(conStr);
        BookModel book = null;
        try{
            MongoDatabase database = mongoClient.getDatabase("library_management");
            MongoCollection<Document> collection = database.getCollection("books");
            MongoCursor<Document> cursor = collection.find(eq("Title", bookTitle)).iterator();
            while(cursor.hasNext()) {
                Document doc = cursor.next();
                book = new BookModel();
                book.BookId = doc.getInteger("_id");
                book.Title = doc.getString("Title");
                book.ISBN = doc.getString("ISBN");
                List<Document> authors = doc.getList("Author", Document.class);
                if (authors != null) {
                    book.Author = new ArrayList<>();
                    for (Document author : authors) {
                        book.Author.add(author.getString("Name"));
                    }
                }
                Object copies = doc.get("Copies");

                if (copies != null) {
                    book.Copies = new ArrayList<>();
                    List<?> intArray = (List<?>) copies;
                    // Assuming the elements in the array are integers, you can convert them to an int array
                    int[] intArrayPrimitive = intArray.stream().mapToInt(obj -> (int) obj).toArray();
                    for (Object copy : intArray)
                        //copy.getInteger("_id");
                        if (copy instanceof Integer)
                            book.Copies.add((Integer)copy);
                }
            }
        }catch (Exception ex){

        }
        return book;
    }

    @Override
    public BookModel loadBookByAuthor(String author) {
        return null;
    }

    @Override
    public List<BookModel> loadAllBooks() {
        return null;
    }

    private UserModel loadUser(int userId) {
        UserModel user = null;
        try {
            this.connect(conStr);
            // Connect to the database
            MongoDatabase database = mongoClient.getDatabase("library_management");

            // Access a collection
            MongoCollection<Document> collection = database.getCollection("users");
            MongoCursor<Document> cursor = collection.find(eq("_id", userId) ).iterator();

            if (cursor.hasNext()) {
                Document doc = cursor.next();
                    user = new UserModel();
                    user.UserId = doc.getInteger("_id");
                    user.UserName = doc.getString("UserName");
                    user.Password = doc.getString("Password");
                    user.DisplayName = doc.getString("DisplayName");
                    user.Address = doc.getString("Address");
                    user.Phone = doc.getString("Phone");
                    user.Email = doc.getString("Email");
                    user.IsManager = doc.getBoolean("IsManager");
            }

        } catch (Exception e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public UserModel loadUser(String username, String password) {
        UserModel user = null;
        try {
            this.connect(conStr);
            // Connect to the database
            MongoDatabase database = mongoClient.getDatabase("library_management");

            // Access a collection
            MongoCollection<Document> collection = database.getCollection("users");
            MongoCursor<Document> cursor = collection.find(eq("UserName", username) ).iterator();

            if (cursor.hasNext()) {
                Document doc = cursor.next();
                if (doc.getString("Password").equals(password)) {
                    user = new UserModel();
                    user.UserId = doc.getInteger("_id");
                    user.UserName = doc.getString("UserName");
                    user.Password = doc.getString("Password");
                    user.DisplayName = doc.getString("DisplayName");
                    user.Address = doc.getString("Address");
                    user.Phone = doc.getString("Phone");
                    user.Email = doc.getString("Email");
                    user.IsManager = doc.getBoolean("IsManager");
                }
            }

        } catch (Exception e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateBook(BookModel book, UserModel currentUser) {

        Document doc =  new Document()
        .append("BorrowedDate","2023/07/23")
                .append("DueDate","2023/08/10")
                .append("BorrowerId",currentUser.UserId)
                .append("BookId", book.BookId);

            this.connect(conStr);
            MongoDatabase database = mongoClient.getDatabase("library_management");
            MongoCollection<Document> collection = database.getCollection("loan_records");
            collection.insertOne(doc);

            MongoCollection<Document> collectionBooks = database.getCollection("books");
            Document filter = new Document("_id", book.BookId);
            Document update = new Document("$set", new Document("LoanValue", book.LoanValue+1));
            collectionBooks.updateOne(filter, update);

    }

    @Override
    public List<LoanRecordModel> loadLoanRecordByBook(int bookId) {
        List<LoanRecordModel> loanBooks= new ArrayList<>();
        this.connect(conStr);
        MongoDatabase database = mongoClient.getDatabase("library_management");
        MongoCollection<Document> collection = database.getCollection("loan_records");
        MongoCursor<Document> cursor = collection.find(eq("BookId", bookId) ).iterator();

        while (cursor.hasNext()) {
            Document doc = cursor.next();
            LoanRecordModel loan = new LoanRecordModel();
            loan.LoadRecordId = doc.getObjectId("_id");
            loan.BorrowDate = doc.getString("BorrowedDate");
            loan.DueDate = doc.getString("DueDate");
            loan.BorrowerId = loadUser(doc.getInteger("BorrowerId"));
            loan.BookId = loadBook(doc.getInteger("BookId"));
            loanBooks.add(loan);
        }

        return  loanBooks;
    }

    @Override
    public List<LoanRecordModel> loadLoanRecordByUser(int userId) {
        List<LoanRecordModel> loanBooks= new ArrayList<>();
        this.connect(conStr);
        MongoDatabase database = mongoClient.getDatabase("library_management");
        MongoCollection<Document> collection = database.getCollection("loan_records");
        MongoCursor<Document> cursor = collection.find(eq("BorrowerId", userId) ).iterator();

        while (cursor.hasNext()) {
            Document doc = cursor.next();
            LoanRecordModel loan = new LoanRecordModel();
            loan.LoadRecordId = doc.getObjectId("_id");
            loan.BorrowDate = doc.getString("BorrowedDate");
            loan.DueDate = doc.getString("DueDate");
            loan.BorrowerId = loadUser(doc.getInteger("BorrowerId"));
            loan.BookId = loadBook(doc.getInteger("BookId"));
            loanBooks.add(loan);
        }

        return  loanBooks;
    }

    @Override
    public void deleteLoadRecord(LoanRecordModel loanReturn) {
        this.connect(conStr);
        MongoDatabase database = mongoClient.getDatabase("library_management");
        MongoCollection<Document> collection = database.getCollection("loan_records");

        Object documentId = loanReturn.LoadRecordId; // Replace with the _id of the document you want to delete
        Document query = new Document("_id", documentId);
        collection.deleteOne(query);
    }

    @Override
    public void addBook(BookModel newBook) {
        Document doc =  new Document()
                .append("_id",newBook.BookId)
                .append("Title",newBook.Title)
                .append("ISBN", newBook.ISBN)
                .append("Copies", newBook.Copies);

        BasicDBList dbl = new BasicDBList();
        for(String name:newBook.Author){
            dbl.add(new BasicDBObject("Name",name));
        }
        doc.append("Author", dbl);

        this.connect(conStr);
        MongoDatabase database = mongoClient.getDatabase("library_management");
        MongoCollection<Document> collection = database.getCollection("books");
        collection.insertOne(doc);
    }
}



