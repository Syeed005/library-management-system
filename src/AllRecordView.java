import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AllRecordView extends JFrame {
    private JTextField txtBookID = new JTextField(10);
    private JTextField txtBookName = new JTextField(40);
    private JTextField txtAuthors = new JTextField(25);
    private JTextField txtISBN = new JTextField(10);
    private JTextField txtCount = new JTextField(10);

    private DefaultTableModel items = new DefaultTableModel(); // store information for the table!

    private JTable tblItems = new JTable(items);

    private JButton btnLoad = new JButton("Load Book");
    private JButton btnExtend = new JButton("Extend Validity");

    public AllRecordView() {
        this.setTitle("All record View");
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.setSize(500, 300);

        items.addColumn("Book ID");
        items.addColumn("Book Title");
        items.addColumn("Borrower Name");
        items.addColumn("Borrowed Date");
        items.addColumn("Due Date");

        JPanel panelOrder = new JPanel();
        panelOrder.setPreferredSize(new Dimension(400, 450));
        panelOrder.setLayout(new BoxLayout(panelOrder, BoxLayout.PAGE_AXIS));
        tblItems.setBounds(0, 0, 400, 350);
        panelOrder.add(tblItems.getTableHeader());
        panelOrder.add(tblItems);
        //panelOrder.add(labTotal);
        tblItems.setFillsViewportHeight(true);
        this.getContentPane().add(panelOrder);

        JPanel panelButton = new JPanel();
        panelButton.add(btnLoad);
        panelButton.add(btnExtend);
        this.getContentPane().add(panelButton);

    }

    public JButton getBtnLoad() {
        return btnLoad;
    }

    public JButton getBtnExtend() {
        return btnExtend;
    }

    public JTextField getTxtBookID() {
        return txtBookID;
    }

    public JTextField getTxtBookName() {
        return txtBookName;
    }

    public JTextField getTxtAuthors() {
        return txtAuthors;
    }

    public JTextField getTxtISBN() {
        return txtISBN;
    }
    public JTextField getTxtCount() {
        return txtCount;
    }
    public void addRow(Object[] row) {
        items.addRow(row);
    }
}
