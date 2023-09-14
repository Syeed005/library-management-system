import javax.swing.*;

public class AddBookView extends JFrame {
    private JTextField txtBookID = new JTextField(10);
    private JTextField txtBookName = new JTextField(40);
    private JTextField txtAuthors = new JTextField(25);
    private JTextField txtISBN = new JTextField(10);
    private JTextField txtCount = new JTextField(10);

    //private JLabel lblBookname = new JLabel();

    private JButton btnLoad = new JButton("Add Book");
    //private JButton btnBorrow = new JButton("Borrow Book");

    public AddBookView() {
        this.setTitle("Book Add View");
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.setSize(500, 300);

        JPanel panelButton = new JPanel();
        panelButton.add(btnLoad);
        //panelButton.add(btnBorrow);
        this.getContentPane().add(panelButton);

        JPanel panelProductID = new JPanel();
        panelProductID.add(new JLabel("Book ID: "));
        panelProductID.add(txtBookID);
        txtBookID.setHorizontalAlignment(JTextField.RIGHT);
        this.getContentPane().add(panelProductID);

        JPanel panelProductName = new JPanel();
        panelProductName.add(new JLabel("Book Name: "));
        panelProductName.add(txtBookName);
        //txtBookName.setHorizontalAlignment(JTextField.RIGHT);
        this.getContentPane().add(panelProductName);

        JPanel panelProductAuthor = new JPanel();
        panelProductAuthor.add(new JLabel("Authors: "));
        panelProductAuthor.add(txtAuthors);
        //txtAuthors.setHorizontalAlignment(JTextField.RIGHT);
        this.getContentPane().add(panelProductAuthor);

        JPanel panelProductInfo = new JPanel();
        panelProductInfo.add(new JLabel("ISBN: "));
        panelProductInfo.add(txtISBN);
        txtISBN.setHorizontalAlignment(JTextField.RIGHT);

        panelProductInfo.add(new JLabel("Book Copies: "));
        panelProductInfo.add(txtCount);
        txtCount.setHorizontalAlignment(JTextField.RIGHT);

        this.getContentPane().add(panelProductInfo);

    }

    public JButton getBtnLoad() {
        return btnLoad;
    }

//    public JButton getBtnUpdate() {
//        return btnBorrow;
//    }

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

}
