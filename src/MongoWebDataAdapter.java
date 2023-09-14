import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoWebDataAdapter implements DataAccess {
    //MongoClientURI uri = new MongoClientURI(connectionString);
    private MongoClient mongoClient = null;
    private static String conStr = "mongodb+srv://syeedsat:password1234@cluster0.zod6sqk.mongodb.net/?retryWrites=true&w=majority";
    @Override
    public void connect(String str) {
        if (mongoClient != null)
            mongoClient.close();
        mongoClient = new MongoClient(new MongoClientURI(str));
    }

    @Override
    public UserModel loadUser(String username, String password) {
        return null;
    }

    @Override
    public void updateBook(BookModel book, UserModel currentUser) {

    }

    @Override
    public List<LoanRecordModel> loadLoanRecordByBook(int bookId) {
        return null;
    }

    @Override
    public List<LoanRecordModel> loadLoanRecordByUser(int userId) {
        return null;
    }

    @Override
    public void deleteLoadRecord(LoanRecordModel loanReturn) {

    }

    @Override
    public void addBook(BookModel newBook) {

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
        //this.mongoClient.close();
        return book;
    }

    @Override
    public void updateBookinBooks(BookModel newBook) {

    }

    @Override
    public List<LoanRecordModel> loadAllLoanRecord() {
        return null;
    }

    @Override
    public void updateLoadRecord(LoanRecordModel loanReturn) {

    }

    @Override
    public BookModel loadBook(String bookTitle) {
        this.connect(conStr);
        System.out.println(bookTitle);
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
        //this.mongoClient.close();
        return book;
    }

    @Override
    public BookModel loadBookByAuthor(String author) {
        this.connect(conStr);
        System.out.println(author);
        BookModel book = null;
        try{
            MongoDatabase database = mongoClient.getDatabase("library_management");
            MongoCollection<Document> collection = database.getCollection("books");

            Document query = new Document("Author", new Document("$elemMatch", new Document("Name", author)));

            for (Document doc : collection.find(query)) {
                System.out.println(doc);
                book = new BookModel();
                book.BookId = doc.getInteger("_id");
                book.Title = doc.getString("Title");
                book.ISBN = doc.getString("ISBN");
                List<Document> authors = doc.getList("Author", Document.class);
                if (authors != null) {
                    book.Author = new ArrayList<>();
                    for (Document authorNew : authors) {
                        book.Author.add(authorNew.getString("Name"));
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
                break;
            }

            //MongoCursor<Document> cursor = collection.find(eq("Title", author)).iterator();
//            while(cursor.hasNext()) {
//                Document doc = cursor.next();
//
//            }
        }catch (Exception ex){

        }
        //this.mongoClient.close();
        return book;
    }

    @Override
    public List<BookModel> loadAllBooks() {
        List<BookModel> books = new ArrayList<>();
        this.connect(conStr);
        BookModel book = null;
        try{
            MongoDatabase database = mongoClient.getDatabase("library_management");
            MongoCollection<Document> collection = database.getCollection("books");
            MongoCursor<Document> cursor = collection.find().iterator();
            int c = 0;
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
                books.add(book);
                c++;
                if (c > 5){
                    break;
                }
            }
        }catch (Exception ex){

        }
        //this.mongoClient.close();
        return books;

    }
}
