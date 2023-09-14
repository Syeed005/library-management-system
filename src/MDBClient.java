import java.util.*;
import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Sorts.descending;

public class MDBClient {
    public static void main(String[] args) {
        // MongoDB connection string for the Data API
        String connectionString = "mongodb+srv://syeedsat:password1234@cluster0.zod6sqk.mongodb.net/?retryWrites=true&w=majority";

        try {
            // Create MongoClient
//            MongoClientURI uri = new MongoClientURI(connectionString);
//            MongoClient mongoClient = new MongoClient(uri);
//
//            // Connect to the database
//            MongoDatabase database = mongoClient.getDatabase("library_management");
//
//            // Access a collection
//            MongoCollection<Document> collection = database.getCollection("books");

            DataAccess dao = new MongoWebDataAdapter();
            List<BookModel> books = dao.loadAllBooks();

            List<String> us = Arrays.asList("a", "the", "an", "of", "on", "and", "for", "own", "your", "with");
            Set<String> keys = new HashSet<>(Arrays.asList("database", "data"));

            Map<BookSearchModel, Integer> res = new HashMap<BookSearchModel, Integer>();

            for (BookModel book : books){
                List<String> bookWords = new ArrayList<>();
                bookWords.addAll(Arrays.stream(book.Title.toLowerCase().split(" ")).toList());

                for (String author: book.Author){
                    bookWords.addAll(Arrays.stream(author.toLowerCase().split(" ")).toList());
                }

                //Set<String> bookKeys = new HashSet<>(bookWords);
                bookWords.removeAll(us);


                BookSearchModel bb = null;

                for (String key:keys){
                    int co = 0;
                    for (String bookKey:bookWords){
                        if (key.equals(bookKey)){
                            co++;
                        }
                    }

                    bb = new BookSearchModel();
                    bb.key = key;
                    bb.book = book;
                    if (res.containsKey(bb)){
                        res.put(bb,co);
                    }else{
                        res.put(bb,co);
                    }
                }
            }

            // Step 1: Convert the HashMap to a List of Map.Entry objects
            List<Map.Entry<BookSearchModel, Integer>> entryList = new ArrayList<>(res.entrySet());

            // Step 2: Sort the List based on the values using a custom Comparator
            Collections.sort(entryList, new Comparator<Map.Entry<BookSearchModel, Integer>>() {
                @Override
                public int compare(Map.Entry<BookSearchModel, Integer> entry1, Map.Entry<BookSearchModel, Integer> entry2) {
                    // Compare the values in ascending order
                    return entry2.getValue().compareTo(entry1.getValue());
                }
            });

            // Step 3: Create a new LinkedHashMap to maintain the order of sorted entries
            Map<BookSearchModel, Integer> sortedMap = new LinkedHashMap<>();

            // Step 4: Put the sorted entries into the new LinkedHashMap
            for (Map.Entry<BookSearchModel, Integer> entry : entryList) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            // Print the sorted map
            System.out.println("Sorted Map by Values:");
            for (Map.Entry<BookSearchModel, Integer> entry : sortedMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }



            Map<BookModel, List<String>> pro = new HashMap<BookModel, List<String>>();
            String c = "";
            for (Map.Entry<BookSearchModel, Integer> entry : sortedMap.entrySet()){
               int bookId =  entry.getKey().book.BookId;
               String key =  entry.getKey().key;
               int va =  entry.getValue();

               if ((pro.get(entry.getKey().book)) != null){
                   pro.put(entry.getKey().book, pro.get(entry.getKey().book)).add(key+"_"+va);
               }else{
                   List<String> ds = new ArrayList<>();
                   ds.add(key+"_"+va);
                   pro.put(entry.getKey().book, ds);
               }




               //pro.put(entry.getKey().book,c);

               double count = 0.0, total = 0.0;
            }

            List<WebBookModel> allBooks = new ArrayList<>();
            for (Map.Entry<BookModel, List<String>> boo: pro.entrySet()){
                WebBookModel booki = new WebBookModel();
                booki.book = boo.getKey();
                List<String> strs =  boo.getValue();
                int val = 0;
                for (String ss : strs){
                    String[] sss = ss.split("_");

                    int v = Integer.parseInt(sss[1]);
//                    Map<String, Integer> m = new HashMap<>();
//                    m.put(sss[0],v);
                    booki.values.put(sss[0],v);
                    val = val + v;
                }
                booki.weight = val;
                allBooks.add(booki);
                double count = 0.0, total = 0.0;
            }

            allBooks.sort(Comparator.comparingInt(WebBookModel::getInt).reversed());
//            allBooks.sort(new Comparator<WebBookModel>() {
//                @Override
//                public int compare(WebBookModel p1, WebBookModel p2) {
//                    return Integer.compare(p1.weight, p2.weight);
//                }
//            });


           double count = 0.0, total = 0.0;
//
//            while (cursor.hasNext()) {
//                Document doc = cursor.next();
//                System.out.println(doc.toJson());
////                double price = doc.getDouble("price");
//                count = count + 1.0;
//                total = total + price;
            //}

//            System.out.println("Average price: " + total/count);
//
//            cursor = collection.find(eq("_id", 1) ).iterator();
//
//            if (cursor.hasNext()) {
//                System.out.println("Found a product with id = 1: " + cursor.next().toJson());
//            }
//
//            cursor = collection.find(and(gt("price", 2.0), lt("price", 4.0))).iterator();
//
//            while (cursor.hasNext()) {
//                System.out.println("Found a product with price > $2 and < $4: " + cursor.next().toJson());
//            }
            // Close the connection
            //mongoClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
