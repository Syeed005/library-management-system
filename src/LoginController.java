import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController implements ActionListener {
    private LoginScreen loginScreen;
    private DataAccess dataAdapter;


    public LoginController(LoginScreen loginScreen, DataAccess dataAdapter) {
        this.loginScreen = loginScreen;
        this.dataAdapter = dataAdapter;
        this.loginScreen.getBtnLogin().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginScreen.getBtnLogin()) {
            String username = loginScreen.getTxtUserName().getText().trim();
            String password = loginScreen.getTxtPassword().getText().trim();

            System.out.println("Login with username = " + username + " and password = " + password);
            UserModel user = dataAdapter.loadUser(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(null, "This user does not exist!");
            }
            else {
                Application.getInstance().setCurrentUser(user);
                if (!user.IsManager){
                    this.loginScreen.setVisible(false);
                    Application.getInstance().getMainScreen().setVisible(true);
                }else{
                    this.loginScreen.setVisible(false);
                    Application.getInstance().getManagerMainScreen().setVisible(true);
                }
            }
        }
    }
}
