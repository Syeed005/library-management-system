import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddBookController implements ActionListener {
    private AddBookView addBookView;
    private DataAccess dataAccess;

    public AddBookController(AddBookView addBookView, DataAccess dataAccess) {
        this.dataAccess = dataAccess;
        this.addBookView = addBookView;

        addBookView.getBtnLoad().addActionListener(this);
        //borrowerView.getBtnUpdate().addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBookView.getBtnLoad())
            addBook();
    }

    private void addBook() {
        BookModel newBook = new BookModel();
        int bookId = 0;
        try {
            bookId = Integer.parseInt(addBookView.getTxtBookID().getText());
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid product ID! Please provide a valid product ID!");
            return;
        }
        BookModel book = dataAccess.loadBook(bookId);
        if (book != null) {
            JOptionPane.showMessageDialog(null, "This book ID already exist in the database!");
            return;
        }

        newBook.BookId = bookId;

        String title = addBookView.getTxtBookName().getText();
        if (title.isEmpty()){
            JOptionPane.showMessageDialog(null, "Book Title is empty");
            return;
        }

        newBook.Title = title;
        String authors = addBookView.getTxtAuthors().getText();
        if (authors.isEmpty()){
            JOptionPane.showMessageDialog(null, "Book authors is empty");
            return;
        }
        List<String> auth = Arrays.stream(authors.split(",")).toList();
        newBook.Author = auth;
        newBook.ISBN = addBookView.getTxtISBN().getText();

        int counts = Integer.parseInt(addBookView.getTxtCount().getText());
        List<Integer> cc = new ArrayList<>();

        for (int i=1;i<=counts;i++){
            cc.add(i);
        }
        newBook.Copies = cc;

        dataAccess.addBook(newBook);
    }
}
