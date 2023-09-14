import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AllRecordController implements ActionListener {
    private AllRecordView allRecordView;
    private DataAccess dataAdapter;
    List<LoanRecordModel> loans = new ArrayList<>();
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == allRecordView.getBtnLoad())
            loadAllRecord();
        else if (e.getSource() == allRecordView.getBtnExtend())
            extendValidity();
    }

    private void extendValidity() {
        if (loans.size() < 1 ){
            JOptionPane.showMessageDialog(null, "please load books first!");
            return;
        }
        String id = JOptionPane.showInputDialog("Enter BookID: ");
        int returnId = Integer.parseInt(id);

        if (returnId > 0){
            if (returnId > loans.size()){
                JOptionPane.showMessageDialog(null, "please enter valid book id");
                return;
            }
            String newDueDate = JOptionPane.showInputDialog("New Due Date: ");

            LoanRecordModel loanReturn = loans.get(returnId - 1);
            loanReturn.DueDate = newDueDate;
            dataAdapter.updateLoadRecord(loanReturn);
            loadAllRecord();
        }else{
            JOptionPane.showMessageDialog(null, "please enter valid book id");
            return;
        }
    }

    private void loadAllRecord() {
        loans.clear();
        loans = dataAdapter.loadAllLoanRecord();
        Object[] row = new Object[5];
        for (int i=0;i< loans.size();i++) {
            row[0] = i+1;
            row[1] = loans.get(i).BookId.Title;
            row[2] = loans.get(i).BorrowerId.DisplayName;
            row[3] = loans.get(i).BorrowDate;
            row[4] = loans.get(i).DueDate;
            this.allRecordView.addRow(row);
        }
        this.allRecordView.invalidate();
    }

    public  AllRecordController(AllRecordView allRecordView, DataAccess dataAdapter){
        this.dataAdapter = dataAdapter;
        this.allRecordView = allRecordView;

        allRecordView.getBtnExtend().addActionListener(this);
        allRecordView.getBtnLoad().addActionListener(this);
    }
}
