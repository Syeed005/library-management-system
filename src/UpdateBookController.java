import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateBookController implements ActionListener {
    private UpdateBookView updateBookView;
    private DataAccess dataAccess;
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateBookView.getBtnLoad()){
            loadBook();
        }
        else if (e.getSource() == updateBookView.getBtnUpdate()) {
            updateBook();
        }
    }

    private void updateBook() {
        BookModel newBook = new BookModel();
        int bookId = 0;
        try {
            bookId = Integer.parseInt(updateBookView.getTxtBookID().getText());
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid product ID! Please provide a valid product ID!");
            return;
        }

        String title = updateBookView.getTxtBookName().getText();
        if (title.isEmpty()){
            JOptionPane.showMessageDialog(null, "Book Title is empty");
            return;
        }
        newBook.BookId = bookId;
        newBook.Title = title;
        String authors = updateBookView.getTxtAuthors().getText();
        if (authors.isEmpty()){
            JOptionPane.showMessageDialog(null, "Book authors is empty");
            return;
        }
        List<String> auth = Arrays.stream(authors.split(",")).toList();
        newBook.Author = auth;
        newBook.ISBN = updateBookView.getTxtISBN().getText();

        int counts = Integer.parseInt(updateBookView.getTxtCount().getText());
        List<Integer> cc = new ArrayList<>();

        for (int i=1;i<=counts;i++){
            cc.add(i);
        }
        newBook.Copies = cc;

        dataAccess.updateBookinBooks(newBook);
    }

    private void loadBook() {
        int bookId = 0;
        try {
            bookId = Integer.parseInt(updateBookView.getTxtBookID().getText());
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid product ID! Please provide a valid product ID!");
            return;
        }

        BookModel book = dataAccess.loadBook(bookId);

        if (book == null) {
            JOptionPane.showMessageDialog(null, "This book ID does not exist in the database!");
            return;
        }

        String authos="";
        for (String name : book.Author){
            authos += name+", ";
        }
        updateBookView.getTxtBookName().setText(book.Title);
        updateBookView.getTxtAuthors().setText(authos);
        updateBookView.getTxtISBN().setText(book.ISBN);
        updateBookView.getTxtCount().setText(String.valueOf((book.Copies.size())));
    }


    public UpdateBookController(UpdateBookView updateBookView, DataAccess dataAccess) {
        this.dataAccess = dataAccess;
        this.updateBookView = updateBookView;
        updateBookView.getBtnLoad().addActionListener(this);
        updateBookView.getBtnUpdate().addActionListener(this);
    }


}
