import com.hp.gagawa.java.elements.*;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map;

public class WebServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
        HttpContext root = server.createContext("/");
        root.setHandler(WebServer::handleRequest);

        HttpContext context = server.createContext("/users");
        context.setHandler(WebServer::handleRequestUser);

        HttpContext product = server.createContext("/products");
        product.setHandler(WebServer::handleRequestOneProduct);

        HttpContext allproducts = server.createContext("/products/all");
        allproducts.setHandler(WebServer::handleRequestAllProducts);

        HttpContext productSearch = server.createContext("/products/search");
        productSearch.setHandler(WebServer::handleRequestProductSearch);

        HttpContext bookSearchBytitle = server.createContext("/books/title");
        bookSearchBytitle.setHandler(WebServer::handleRequestBookSearchByTitle);

        HttpContext bookSearchByAuthor = server.createContext("/books/author");
        bookSearchByAuthor.setHandler(WebServer::handleRequestBookSearchByAuthor);

        HttpContext bookSearch = server.createContext("/books/search");
        bookSearch.setHandler(WebServer::handleRequestBooksSearch);

        server.start();
    }

    private static void handleRequestBooksSearch(HttpExchange exchange) throws IOException {
        String uri =  exchange.getRequestURI().toString();

        String keywords = uri.substring(uri.lastIndexOf('/')+1);
        keywords = keywords.replaceAll("%20", " ");
        System.out.println(keywords);

        Set<String> set = new HashSet<>(List.of(keywords.toLowerCase().split(" ")));

        DataAccess dao = new MongoWebDataAdapter();
        List<BookModel> books = dao.loadAllBooks();

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("Example Page Title") );
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("Example Page Header") );
        body.appendChild( h1 );

        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);

        //BookModel book = dao.loadBookByAuthor(author);
        List<WebBookModel> processedBooks = Processing(books, set);
        int count = 1;
        while(count < 4){
            for (WebBookModel boo : processedBooks){
                    BookModel book = boo.book;
                    if (book != null) {
                        para = new P();
                        para.appendText("==========================================");
                        html.appendChild(para);
                        para = new P();
                        para.appendText("BookId:" + book.BookId);
                        html.appendChild(para);
                        para = new P();
                        para.appendText("Book Title:" + book.Title);
                        html.appendChild(para);
                        para = new P();
                        para.appendText("ISBN:" + book.ISBN);
                        html.appendChild(para);
                        para = new P();
                        para.appendText("Copies:" + book.Copies.size());
                        html.appendChild(para);

                        for (Map.Entry<String, Integer> s: boo.values.entrySet()){
                            para = new P();
                            para.appendText("Search Text:" + s.getKey());
                            para.appendText("&emsp;&emsp;Search Weight:" + s.getValue());
                            html.appendChild(para);
                        }
                        para = new P();
                        para.appendText("BorrowWeight:" + book.LoanValue);
                        html.appendChild(para);
                        para = new P();
                        para.appendText("==========================================");
                        html.appendChild(para);
                    }
                    else {
                        para = new P();
                        para.appendText("Product not found");
                        html.appendChild(para);
                    }
                count++;
            }
        }



        String response = html.write();

        // System.out.println(response);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static List<WebBookModel> Processing(List<BookModel> books,Set<String> keys) {
        try{

        }catch (Exception ex){

        }
        List<String> us = Arrays.asList("a", "the", "an", "of", "on", "and", "for", "own", "your", "with");
        //Set<String> keys = new HashSet<>(Arrays.asList("database", "data"));

        java.util.Map<BookSearchModel, Integer> res = new HashMap<BookSearchModel, Integer>();
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
        List<java.util.Map.Entry<BookSearchModel, Integer>> entryList = new ArrayList<>(res.entrySet());

        // Step 2: Sort the List based on the values using a custom Comparator
        Collections.sort(entryList, new Comparator<java.util.Map.Entry<BookSearchModel, Integer>>() {
            @Override
            public int compare(java.util.Map.Entry<BookSearchModel, Integer> entry1, java.util.Map.Entry<BookSearchModel, Integer> entry2) {
                // Compare the values in ascending order
                return entry2.getValue().compareTo(entry1.getValue());
            }
        });

        // Step 3: Create a new LinkedHashMap to maintain the order of sorted entries
        java.util.Map<BookSearchModel, Integer> sortedMap = new LinkedHashMap<>();

        // Step 4: Put the sorted entries into the new LinkedHashMap
        for (java.util.Map.Entry<BookSearchModel, Integer> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        // Print the sorted map
        System.out.println("Sorted Map by Values:");
        for (java.util.Map.Entry<BookSearchModel, Integer> entry : sortedMap.entrySet()) {
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
                booki.values.put(sss[0],v);
                val = val + v;
            }
            booki.weight = val;
            allBooks.add(booki);
            double count = 0.0, total = 0.0;
        }

        allBooks.sort(Comparator.comparingInt(WebBookModel::getInt).reversed());
        return allBooks;
    }

    private static void handleRequestBookSearchByAuthor(HttpExchange exchange) throws IOException{
        String uri =  exchange.getRequestURI().toString();

        String author = uri.substring(uri.lastIndexOf('/')+1);

        author = author.replaceAll("%20", " ");

        System.out.println(author);

//        RemoteDataAdapter dao = new RemoteDataAdapter();

        DataAccess dao = new MongoWebDataAdapter();

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("Example Page Title") );
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("Example Page Header") );
        body.appendChild( h1 );

        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);

        BookModel book = dao.loadBookByAuthor(author);
        //BookModel book = null;
        if (book != null) {

            para = new P();
            para.appendText("BookId:" + book.BookId);
            html.appendChild(para);
            para = new P();
            para.appendText("Book Title:" + book.Title);
            html.appendChild(para);
            para = new P();
            para.appendText("ISBN:" + book.ISBN);
            html.appendChild(para);
            para = new P();
            para.appendText("Copies:" + book.Copies.size());
            html.appendChild(para);
        }
        else {
            para = new P();
            para.appendText("Product not found");
            html.appendChild(para);
        }


        String response = html.write();

        // System.out.println(response);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestBookSearchByTitle(HttpExchange exchange) throws IOException{
        String uri =  exchange.getRequestURI().toString();

        String bookTitle = uri.substring(uri.lastIndexOf('/')+1);

        bookTitle = bookTitle.replaceAll("%20", " ");

        System.out.println(bookTitle);

//        RemoteDataAdapter dao = new RemoteDataAdapter();

        DataAccess dao = new MongoWebDataAdapter();

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("Example Page Title") );
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("Example Page Header") );
        body.appendChild( h1 );

        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);

        BookModel book = dao.loadBook(bookTitle);
        //BookModel book = null;
        if (book != null) {

            para = new P();
            para.appendText("BookId:" + book.BookId);
            html.appendChild(para);
            para = new P();
            para.appendText("Book Title:" + book.Title);
            html.appendChild(para);
            para = new P();
            para.appendText("ISBN:" + book.ISBN);
            html.appendChild(para);
            para = new P();
            para.appendText("Copies:" + book.Copies.size());
            html.appendChild(para);
        }
        else {
            para = new P();
            para.appendText("Product not found");
            html.appendChild(para);
        }


        String response = html.write();

        // System.out.println(response);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestProductSearch(HttpExchange exchange) throws IOException {
        String uri =  exchange.getRequestURI().toString();

        String keyword = uri.substring(uri.lastIndexOf('/')+1);

        String searchUrl = "https://www.amazon.com/s?k=" + keyword;
        Document document = Jsoup.connect(searchUrl).get();

        // Find all the search result items
        Elements searchResults = document.select(".s-result-item");

        /* Iterate over the search result items and extract relevant information
        for (Element result : searchResults) {
            String title = result.select(".a-text-normal").text();
            String price = result.select(".a-price .a-offscreen").text();
            String rating = result.select(".a-icon-alt").text();

            System.out.println("Title: " + title);
            System.out.println("Price: " + price);
            System.out.println("Rating: " + rating);
            System.out.println("------------------------");
        }*/

        String response = "The total results: " + searchResults.size(); // document.html();

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    private static void handleRequest(HttpExchange exchange) throws IOException {

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendText("Online shopping web server");
        head.appendChild( title );

        Body body = new Body();

        H1 h1 = new H1();
        h1.appendText("Welcome to my online shopping center");

        body.appendChild(h1);

        P para = new P();

        A link = new A("/products/all", "/products/all");
        link.appendText("Product list");

        para.appendChild(link);
        body.appendChild(para);
        html.appendChild( body );


        String response = html.write();
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestUser(HttpExchange exchange) throws IOException {
        String response = "Hi there! I am a simple web server!";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestAllProducts(HttpExchange exchange) throws IOException {

        String url = "jdbc:sqlite:store.db";

        DataAccess dao = new MongoWebDataAdapter();

        //List<ProductModel> list = dao.loadAllProducts();

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendText("Product list");
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendText("Product list");
        body.appendChild( h1 );

        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);

        para = new P();
        //para.appendChild( new Text("The server has " + list.size() + " products." ));
        body.appendChild(para);

        Table table = new Table();
        Tr row = new Tr();
        Th header = new Th(); header.appendText("ProductID"); row.appendChild(header);
        header = new Th(); header.appendText("Product name"); row.appendChild(header);
        header = new Th(); header.appendText("Price"); row.appendChild(header);
        header = new Th(); header.appendText("Quantity"); row.appendChild(header);
        table.appendChild(row);

//        for (ProductModel product : list) {
//            row = new Tr();
//            Td cell = new Td(); cell.appendText(String.valueOf(product.productID)); row.appendChild(cell);
//            cell = new Td(); cell.appendText(product.name); row.appendChild(cell);
//            cell = new Td(); cell.appendText(String.valueOf(product.price)); row.appendChild(cell);
//            cell = new Td(); cell.appendText(String.valueOf(product.quantity)); row.appendChild(cell);
//            table.appendChild(row);
//        }

        table.setBorder("1");

        html.appendChild(table);
        String response = html.write();

        System.out.println(response);


        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    private static void handleRequestOneProduct(HttpExchange exchange) throws IOException {
        String uri =  exchange.getRequestURI().toString();

        int id = Integer.parseInt(uri.substring(uri.lastIndexOf('/')+1));

        System.out.println(id);

//        RemoteDataAdapter dao = new RemoteDataAdapter();

        DataAccess dao = new MongoDataAdapter();

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("Example Page Title") );
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("Example Page Header") );
        body.appendChild( h1 );

        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);

        BookModel book = dao.loadBook(id);

        if (book != null) {

            para = new P();
            para.appendText("BookId:" + book.BookId);
            html.appendChild(para);
            para = new P();
            para.appendText("Book Title:" + book.Title);
            html.appendChild(para);
            para = new P();
            para.appendText("ISBN:" + book.ISBN);
            html.appendChild(para);
            para = new P();
            para.appendText("Copies:" + book.Copies.size());
            html.appendChild(para);
        }
        else {
            para = new P();
            para.appendText("Product not found");
            html.appendChild(para);
        }


        String response = html.write();

        // System.out.println(response);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
