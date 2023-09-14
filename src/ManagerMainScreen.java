import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerMainScreen extends JFrame {
    private JButton btnAdd = new JButton("Add Book");
    private JButton btnUpdate = new JButton("Update Book");
    private JButton btnAllRecord = new JButton("View All Records");

    public ManagerMainScreen() {
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);

        btnAdd.setPreferredSize(new Dimension(120, 50));
        btnUpdate.setPreferredSize(new Dimension(120, 50));
        btnAllRecord.setPreferredSize(new Dimension(120, 50));



        JLabel title = new JLabel("Library Management System");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        JLabel subtitle = new JLabel("Manager Panel");
        subtitle.setFont(new Font("Sans Serif", Font.BOLD, 18));
        JPanel panelTitle = new JPanel();
        panelTitle.add(title);
        panelTitle.add(subtitle);
        this.getContentPane().add(panelTitle);

        JPanel panelButton = new JPanel();
        panelButton.add(btnAdd);
        panelButton.add(btnUpdate);
        panelButton.add(btnAllRecord);



        this.getContentPane().add(panelButton);

        btnAdd.addActionListener(new ActionListener() { // when controller is simple, we can declare it on the fly
            public void actionPerformed(ActionEvent e) {
                Application.getInstance().getAddBookView().setVisible(true);
            }
        });


        btnUpdate.addActionListener(new ActionListener() { // when controller is simple, we can declare it on the fly
            public void actionPerformed(ActionEvent e) {
                Application.getInstance().getUpdateBookView().setVisible(true);
            }
        });
        btnAllRecord.addActionListener(new ActionListener() { // when controller is simple, we can declare it on the fly
            public void actionPerformed(ActionEvent e) {
                Application.getInstance().getAllRecordView().setVisible(true);
            }
        });
    }
}
