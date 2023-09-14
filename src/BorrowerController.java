import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BorrowerController implements ActionListener {
    private BorrowerView borrowerView;
    private DataAccess dataAdapter;

    public BorrowerController(BorrowerView borrowerView, DataAccess dataAdapter) {
        this.dataAdapter = dataAdapter;
        this.borrowerView = borrowerView;

        borrowerView.getBtnLoad().addActionListener(this);
        borrowerView.getBtnBorrow().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == borrowerView.getBtnLoad())
            loadBook();
        else if (e.getSource() == borrowerView.getBtnBorrow())
            saveProduct();
    }

    private void saveProduct() {
        int bookId = 0;
        try {
            bookId = Integer.parseInt(borrowerView.getTxtBookID().getText());
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid product ID! Please provide a valid product ID!");
            return;
        }

        BookModel book = dataAdapter.loadBook(bookId);
        List<LoanRecordModel> loanAll = dataAdapter.loadLoanRecordByBook(bookId);

        if (book == null) {
            JOptionPane.showMessageDialog(null, "This book ID does not exist in the database!");
            return;
        }

        UserModel currentUser = Application.getInstance().getCurrentUser();

        for (LoanRecordModel loan: loanAll){
            if (loan.BorrowerId.UserId == currentUser.UserId){
                JOptionPane.showMessageDialog(null, "You have already borrowed a copy of this book");
                return;
            }
        }

        if ((book.Copies.size() - loanAll.size()) < 1){
            JOptionPane.showMessageDialog(null, "No copy available.");
            return;
        }

        dataAdapter.updateBook(book, currentUser);
        JOptionPane.showMessageDialog(null, "You have successfully borrowed the book");
        loanAll = dataAdapter.loadLoanRecordByBook(bookId);
        borrowerView.getTxtCount().setText(String.valueOf((book.Copies.size() - loanAll.size())));
    }

    private void loadBook() {
        int bookId = 0;
        try {
            bookId = Integer.parseInt(borrowerView.getTxtBookID().getText());
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid product ID! Please provide a valid product ID!");
            return;
        }

        BookModel book = dataAdapter.loadBook(bookId);
        List<LoanRecordModel> loan = dataAdapter.loadLoanRecordByBook(bookId);


        if (book == null) {
            JOptionPane.showMessageDialog(null, "This book ID does not exist in the database!");
            return;
        }

        String authos="";
        for (String name : book.Author){
            authos += name+", ";
        }
        borrowerView.getTxtBookName().setText(book.Title);
        borrowerView.getTxtAuthors().setText(authos);
        borrowerView.getTxtISBN().setText(book.ISBN);
        borrowerView.getTxtCount().setText(String.valueOf((book.Copies.size() - loan.size())));
    }
}
