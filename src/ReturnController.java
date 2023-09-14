import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReturnController implements ActionListener {
    private  ReturnView returnView;
    private DataAccess dataAdapter;
    List<LoanRecordModel> loans = new ArrayList<>();

    public  ReturnController(ReturnView returnView, DataAccess dataAdapter){
        this.dataAdapter = dataAdapter;
        this.returnView = returnView;

        returnView.getBtnBorrow().addActionListener(this);
        returnView.getBtnLoad().addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == returnView.getBtnLoad())
            loadRecord();
        else if (e.getSource() == returnView.getBtnBorrow())
            returnBook();
    }

    private void returnBook() {
        if (loans.size() < 1 ){
            JOptionPane.showMessageDialog(null, "please load the books first!");
            return;
        }
        String id = JOptionPane.showInputDialog("Enter BookID: ");
        int returnId = Integer.parseInt(id);

        if (returnId > 0){
            if (returnId > loans.size()){
                JOptionPane.showMessageDialog(null, "please enter valid book id");
                return;
            }
            LoanRecordModel loanReturn = loans.get(returnId - 1);
            dataAdapter.deleteLoadRecord(loanReturn);
        }else{
            JOptionPane.showMessageDialog(null, "please enter valid book id");
            return;
        }
    }

    private void loadRecord() {
        UserModel user = Application.getInstance().getCurrentUser();
        loans = dataAdapter.loadLoanRecordByUser(user.UserId);

        Object[] row = new Object[4];
        for (int i=0;i< loans.size();i++) {
            row[0] = i+1;
            row[1] = loans.get(i).BookId.Title;
            row[2] = loans.get(i).BorrowDate;
            row[3] = loans.get(i).DueDate;
            this.returnView.addRow(row);
        }
        this.returnView.invalidate();

    }
}
