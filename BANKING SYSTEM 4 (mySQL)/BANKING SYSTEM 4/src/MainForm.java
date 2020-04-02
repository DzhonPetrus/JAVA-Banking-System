
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dzhon
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();
        
        InitFormComponents();
        
        setDate();
        loadAccounts();
        ResetToLogin();
    }

    // COLORS INIT
    final static Color BGMAINGREEN = new Color(155,197,61); // GREEN
    final static Color BGMAINYELLOW = new Color(253,231,76); //YELLOW
    final static Color BGMAINBLUE = new Color(91,192,235); // BLUE
    final static Color BGMAINRED = new Color(229,89,52); // RED
    final static Color FGMAIN = new Color(191,191,191);
    final static Color FGHOVER = new Color(255,255,255);
    
    
    // BANK VARIABLES
    final static int MAINTAININGBALANCE = 5000, INITIALDEPOSIT = 5000, MINIMUMAMOUNT = 100;
    final static double INTEREST = 0.05;
    
    // VALIDATION & INIT VARIABLES
    
    Timer timer = null;
    Account account = null;
    Database db = new Database();
    List<Account> accounts = new ArrayList<>();
    
    private JButton Buttons[] = null;
    private JTextField FormTF[] = null, NumericFields[] = null;
    private String placeholder[] = new String[] {"INITIAL DEPOSIT", "FIRST NAME", "LAST NAME", "CONTACT NUMBER"};
    
    
    private void InitFormComponents() {
        ((JTextField) dcBirthday.getDateEditor().getUiComponent()).setText("BIRTHDAY");
        panelMenu.setVisible(false);
        
        
        // BUTTON PROP INIT
        Buttons = new JButton[]{ loginButtonAction, loginButtonActionSec, ButtonBalance, ButtonCloseAccount, ButtonDeposit, ButtonLogout, ButtonProfile, ButtonWithdraw, newAccButtonAction, newAccButtonActionSec, ButtonRightAction };
        NumericFields = new JTextField[]{ tfPIN , tfContactNumber , tfInitialDeposit, tfRightInput};
        FormTF = new JTextField[]{ tfInitialDeposit, tfFirstName, tfLastName, tfContactNumber};
        
        for(JButton btn : Buttons){
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e){
                    if(btn.isEnabled()){ 
                        btn.setBackground(BGMAINYELLOW);
                        if(btn != loginButtonAction && btn != loginButtonActionSec && btn != newAccButtonAction && btn != newAccButtonActionSec)
                            btn.setBackground(BGMAINRED);
                        btn.setForeground(FGHOVER);
                        if(btn == ButtonRightAction){
                            btn.setBackground(FGHOVER);
                            btn.setForeground(BGMAINRED);
                        }
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e){
                    if(btn.isFocusable()){
                            btn.setBackground(BGMAINGREEN);
                        if(btn != loginButtonAction && btn != loginButtonActionSec && btn != newAccButtonAction && btn != newAccButtonActionSec)
                            btn.setBackground(BGMAINBLUE);
                        
                        btn.setForeground(FGMAIN);
                        
                        if(btn == ButtonRightAction){
                            btn.setBackground(BGMAINRED);
                            btn.setForeground(FGMAIN);
                        }
                    }
                }
                
                @Override
                public void mouseClicked(MouseEvent e){
                    if(btn.isEnabled()){
                        for(JButton btn2 : Buttons){
                            if(!btn2.isFocusable())
                                btn2.setEnabled(true);
                                
                            btn2.setFocusable(true);
                            btn2.setBackground(BGMAINGREEN);
                            btn2.setForeground(FGMAIN);
                                
                            if(btn2 != loginButtonAction && btn2 != loginButtonActionSec && btn2 != newAccButtonAction && btn2 != newAccButtonActionSec)
                                btn2.setBackground(BGMAINBLUE);
                            
                            if(btn2 == ButtonRightAction){
                                btn2.setBackground(BGMAINRED);
                                btn2.setForeground(FGMAIN);
                            }
                        }
                        if(btn != loginButtonAction && btn != loginButtonActionSec && btn != newAccButtonAction && btn != newAccButtonActionSec && btn != ButtonRightAction){
                            btn.setBackground(BGMAINRED);
                            btn.setFocusable(false);
                            btn.setEnabled(false);
                        }else{
                            btn.setBackground(BGMAINYELLOW);
                        }
                        btn.setForeground(FGHOVER);
                        
                        if(btn == ButtonRightAction){
                            btn.setBackground(BGMAINRED);
                            btn.setForeground(FGMAIN);
                        }
                        
                        /* BUTTON CLICK ACTIONS */
                        
                        //LOGIN BUTTONS
                        // upper button on login form
                        if(btn == loginButtonAction){
                            if(btn.getText().equals("YES")){
                                System.exit(0);
                            }else{
                                // SHOW NEW ACCOUNT PANEL
                                FormValidation();
                                FormReset();
                                panelNewAccount.setVisible(true);
                                panelLogin.setVisible(false);
                                tfAccountNumber.setText("" + GenerateAccountNumber());
                            }
                        }
                        
                        // bottom button on login form
                        if(btn == loginButtonActionSec){
                            if(btn.getText().equals("EXIT")){
                                loginButtonAction.setText("YES");
                                btn.setText("NO");
                                tfPIN.setVisible(false);
                                loginActionTitle.setText("<html><center>ARE YOU SURE YOU WANT TO <br/>EXIT THE PROGRAM?</center></html>");
                            }else{
                                loginButtonAction.setText("CREATE NEW ACCOUNT");
                                btn.setText("EXIT");
                                tfPIN.setVisible(true);
                                loginActionTitle.setText("Enter your 4 DIGIT PIN");
                            }
                        }
                        
                        
                        // NEW ACCOUNT BUTTONS
                        //upper button
                        if(btn == newAccButtonAction){
                            String val[] = new String[7];
                            int index = 1;
                            val[0] = tfAccountNumber.getText();
                            for(JTextField jtf : FormTF){
                                val[index] = jtf.getText();
                                index++;
                            }
                            val[5] = ((JTextField) dcBirthday.getDateEditor().getUiComponent()).getText();
                            val[6] = taAddress.getText();
                            
                            account = new Account(Integer.parseInt(val[0]), Integer.parseInt(val[1]), 1, val[2], val[3], val[6], val[5], val[4]);
                            try {
                                db.insertAccount(account);
                                ValidateLogin(account.getAccountNumber());
                            } catch (SQLException ex) {
                                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        
                        //lower button
                        if(btn == newAccButtonActionSec){
                            if(btn.getText().equals("BACK")){
                                timer.stop();
                                ResetToLogin();
                            }
                        }
                        
                        
                        // MENU BUTTONS
                        if(btn == ButtonBalance){
                            String out = "<html><center> CURRENT BALANCE <br/><br/>"+ FormatCurrency(account.getBalance()) +" </center></html>";
                            MenuState(out, "OK", true, false);
                        }
                        
                        if(btn == ButtonDeposit)
                            MenuState("Enter Amount (PHP)", "DEPOSIT", true, true);
                        
                        if(btn == ButtonWithdraw)
                            MenuState("Enter Amount (PHP)", "WITHDRAW", true, true);
                        
                        if(btn == ButtonProfile){
                            String out = "<html><h1>MY PROFILE</h1><p>Account Number: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + account.getAccountNumber() + "</p>\n" + "<p>Current Balance: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ FormatCurrency(account.getBalance()) +"</p>\n" + "<p>First Name:        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ account.getFirstName() +"</p>\n" + "<p>Last Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;      "+ account.getLastName() +"</p>\n" + "<p>Contact Number: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ account.getContactNumber() + "</p>\n" + "<p>Birthday: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ account.getBirthday() +"</p>\n" + "<p>Address: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ account.getAddress() +"</p></html>";
                            MenuState(out, "OK", true, false);
                        }
                        
                        if(btn == ButtonCloseAccount)
                            MenuState("<HTML><CENTER>ARE YOU SURE YOU WANT <BR/> TO CLOSE YOUR ACCOUNT?</CENTER></HTML>", "CONFIRM CLOSE ACCOUNT", true, false);
                        
                        if(btn == ButtonLogout)
                            MenuState("<HTML><CENTER>ARE YOU SURE YOU WANT <BR/> TO LOGOUT?</CENTER></HTML>", "CONFIRM LOGOUT", true, false);
                        
                        
                        // INPUT BUTTON ACTION
                        if(btn == ButtonRightAction){
                            switch(btn.getText()){
                                case "CONFIRM LOGOUT":
                                    ResetToLogin();
                                    break;
                                    
                                case "CONFIRM CLOSE ACCOUNT":
                                    MenuState("<HTML><CENTER><H1>YOUR LAST BALANCE IS "+ FormatCurrency(account.getBalance()) +"</H1><H1>IT IS NOW PHP 0.00</H1><BR/><H1>YOUR ACCOUNT IS NOW CLOSED!</H1></CENTER></HTML>", "BACK TO LOGIN", true, false);
                                    for(JButton b : Buttons)
                                        if(b != ButtonRightAction){
                                            b.setEnabled(false);
                                            b.setFocusable(false);
                                        }
                                    break;
                                    
                                case "OK":
                                    ResetToMenu();
                                    break;
                                
                                case "BACK TO LOGIN":
                                    try {
                                        db.updateAccountStatus(account, 0);
                                    } catch (SQLException ex) {
                                        Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    ResetToLogin();
                                    break;
                                    
                                case "DEPOSIT":
                                   String Out = "<html><div style=\"text-align:right\"><h1>COMPUTATION</h1>Balance: <strong>" + FormatCurrency(account.getBalance()) + "<br/>+ &nbsp;" + FormatCurrency(Integer.valueOf(tfRightInput.getText())) + "</strong>";
                                    account.setBalance(account.getBalance() + Integer.valueOf(tfRightInput.getText()));
                                    Out += "<br/>= &nbsp;<strong>" + FormatCurrency(account.getBalance()) + "</strong><br/>Interest:&nbsp;" + FormatCurrency((int)(account.getBalance() * INTEREST)) + "\n";
                                    account.setBalance(account.getBalance() + (int)(account.getBalance() * INTEREST));
                                    Out += "<br/>New Balance:&nbsp;<strong>" + FormatCurrency(account.getBalance()) + "</strong></html>";
                                    try {
                                        db.updateAccountBalance(account, account.getBalance());
                                    } catch (SQLException ex) {
                                        Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                    MenuState(Out, "OK", true, false);
                                    break;                                                              
                                
                                case "WITHDRAW":
                                    Out = "<html><div style=\"text-align:right\"> Balance: <strong>" + FormatCurrency(account.getBalance()) + "</strong><br/>" + "<strong>- &nbsp;Php " + Integer.valueOf(tfRightInput.getText()) + "</strong><br/>";
                                    account.setBalance(account.getBalance() - Integer.valueOf(tfRightInput.getText()));
                                    Out += "New Balance: <strong>Php " + FormatCurrency(account.getBalance()) + "</strong></div></html>";
                                    try {
                                        db.updateAccountBalance(account, account.getBalance());
                                    } catch (SQLException ex) {
                                        Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    MenuState(Out, "OK", true, false);
                                    break;
                                
                            }
                        }
                        
                        
                    }
                }
            });
        }
        
        // END OF BUTTON PROP
        
        
        // NUMERIC TEXT FIELDS INIT
        for(JTextField tf : NumericFields){
            tf.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent evt){
                    
                try {
                    int input;
                    long cont;
                    // IF PIN INPUT
                    if(tf == tfPIN){
                        input = Integer.parseInt(tf.getText());
                        if(tf.getText().length() > 3){
                            // PIN Validation
                            if(tf.getText().length() == 4){
                                ValidateLogin(Integer.parseInt(tf.getText()));
                                tf.setText("");
                            }
                            tf.setText("");
                        }
                    }
                    
                    // IF CONTACT NUMBER ( max 11 numbers)
                    if(tf == tfContactNumber){
                        cont = Long.parseLong(tf.getText());
                        if(tf.getText().length() >= 12){
                            tf.setText("");
                        }
                    }
            
                    
                    // INITIAL DEPOSIT
                    if(tf == tfInitialDeposit){
                        input = Integer.parseInt(tf.getText());
                    }
                    
                    // AMOUNT w/ VALIDATION
                    if(tf == tfRightInput){
                        input = Integer.parseInt(tf.getText());
                        
                        if(!tf.getText().isEmpty()){
                            LabelRightOut.setText("Enter Amount (PHP)");
                            tf.setForeground(Color.black);
                            ButtonRightAction.setEnabled(true);
                            if(input < MINIMUMAMOUNT){
                                LabelRightOut.setText("MINIMUM AMOUNT IS PHP " + MINIMUMAMOUNT);
                                tf.setForeground(Color.red);
                                ButtonRightAction.setEnabled(false);
                            }
                            
                            // WITHDRAWAL CONDITION
                            if(ButtonRightAction.getText().equals("WITHDRAW")){
                                if(account.getBalance() < input){
                                    LabelRightOut.setText("INSUFFICIENT BALANCE");
                                    tf.setForeground(Color.red);
                                    ButtonRightAction.setEnabled(false);
                                }
                                if((account.getBalance() - input) < MAINTAININGBALANCE){
                                    LabelRightOut.setText("MAINTAINING BALANCE IS PHP " + MAINTAININGBALANCE);
                                    tf.setForeground(Color.red);
                                    ButtonRightAction.setEnabled(false);
                                }
                            }
                        }
                        
                    }
                    
                    
                } catch (NumberFormatException nfe) {
                        tf.setText("");
                        
                }
                // END OF TRY-CATCH
                }
            });
        }
        
        // END NUMERIC TEXT FIELDS INIT
        
        // TEXT FIELD FOCUS FOR PLACE HOLDER
        for(JTextField jtf : FormTF){
            jtf.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e){
                    for(String s : placeholder)
                        if(jtf.getText().equals(s))
                            jtf.setText("");
                }
                
                @Override
                public void focusLost(FocusEvent e){
                    int index = 0;
                    for(JTextField tf : FormTF){
                        if(tf.getText().isEmpty())
                            tf.setText(placeholder[index]);
                        index++;
                    }
                        
                }
            });
        }
        // END TEXT FIELD FOCUS FOR PLACE HOLDER
        
        
        
    }
    
    // DB DATA FUNCTIONS
    private void loadAccounts(){
        accounts = db.getAllAccounts();
    }
    
    private void ValidateLogin(int PIN){
        account = db.validateActiveAccount(PIN);
        if(account != null)
            ResetToMenu();
    }
    
    private void FormValidation(){
        timer = new Timer(500, new FormValidationActionListener());
        timer.start();
    }
    class FormValidationActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            boolean isValid = false;
            int index = 0;
            
            isValid = (!taAddress.getText().equals("ADDRESS") && !taAddress.getText().isEmpty()) && (!((JTextField) dcBirthday.getDateEditor().getUiComponent()).getText().equals("BIRTHDAY") && !((JTextField) dcBirthday.getDateEditor().getUiComponent()).getText().equals(""));
            
            for(JTextField jtf : FormTF){
                if(!jtf.getText().equals(placeholder[index]) && !jtf.getText().isEmpty()){
                    if(jtf == tfInitialDeposit)
                        if(Integer.parseInt(jtf.getText()) < INITIALDEPOSIT){
                            isValid = false;
                            jtf.setToolTipText("INITIAL DEPOSIT MUST BE 5000 AND ABOVE");
                            jtf.setBackground(Color.red);
                        }else{
                            isValid = true;
                            jtf.setToolTipText("");
                            jtf.setBackground(Color.white);
                        }
                }else{
                    isValid = false;
                }
                index++;
            }
                
            newAccButtonAction.setEnabled(isValid);
        }
    }
    
    private void FormReset() {
        int index = 0;
        for(JTextField jtf : FormTF){
            jtf.setText(placeholder[index]);
            index++;
        }
        taAddress.setText("ADDRESS");
        ((JTextField) dcBirthday.getDateEditor().getUiComponent()).setText("BIRTHDAY");
    }
    
    private int GenerateAccountNumber(){
        int AccountNumber = 0;
        do {
            Random rand = new Random();
            AccountNumber = Integer.valueOf(String.format("%04d", rand.nextInt(10000)));
        } while(db.isExistingAccount(AccountNumber) != null);
        return AccountNumber;
    }
    
    private void MenuState(String label, String button, boolean buttonVisible, boolean inputVisible){
        LabelRightOut.setText(label);
        ButtonRightAction.setText(button);
        ButtonRightAction.setVisible(buttonVisible);
        ButtonRightAction.setEnabled(buttonVisible);
        tfRightInput.setVisible(inputVisible);
        tfRightInput.setText("");
        
        if(tfRightInput.isVisible()){
            ButtonRightAction.setEnabled(!buttonVisible);
            tfRightInput.grabFocus();
        }
    }
    
    private void ResetToLogin(){
        account = null;
        panelLogin.setVisible(true);
        panelMain.setVisible(true);
        panelMenu.setVisible(false);
        panelNewAccount.setVisible(false);
    }
    
    private void ResetToMenu(){
        panelMain.setVisible(false);
        panelMenu.setVisible(true);
        LabelUser.setText("<html>Welcome<br/><strong>" + account.getFirstName().concat(" ").concat(account.getLastName()) + "</strong></html>");
        MenuState("<html><center><br/><br/><br/>WHAT WOULD YOU LIKE <br/>TO DO TODAY?</center></html>", "", false, false);
    }
    
    private void setDate(){
        dcBirthday.setMaxSelectableDate(new Date());
        Timer timer = new Timer(1000, new setDateActionListener());
        timer.start();
    }
    class setDateActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LabelRightTime.setText("<html>DATE | TIME  :  <strong>" + new SimpleDateFormat("dd-MM-yyyy  |  HH:mm:ss").format(Calendar.getInstance().getTime()) + "</strong></html>");
        }
    }
        
    private String FormatCurrency(int amount){
        return "PHP " + String.format("%,.2f", (double) amount);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMenu = new javax.swing.JPanel();
        ButtonLogout = new javax.swing.JButton();
        ButtonBalance = new javax.swing.JButton();
        ButtonDeposit = new javax.swing.JButton();
        ButtonWithdraw = new javax.swing.JButton();
        ButtonProfile = new javax.swing.JButton();
        ButtonCloseAccount = new javax.swing.JButton();
        panelRight = new javax.swing.JPanel();
        tfRightInput = new javax.swing.JTextField();
        ButtonRightAction = new javax.swing.JButton();
        LabelRightTime = new javax.swing.JLabel();
        LabelRightOut = new javax.swing.JLabel();
        SYSTEMTITLE2 = new javax.swing.JLabel();
        LabelUser = new javax.swing.JLabel();
        panelMain = new javax.swing.JPanel();
        panelLogin = new javax.swing.JPanel();
        tfPIN = new javax.swing.JPasswordField();
        SYSTEMTITLE = new javax.swing.JLabel();
        loginButtonActionSec = new javax.swing.JButton();
        loginButtonAction = new javax.swing.JButton();
        loginActionTitle = new javax.swing.JLabel();
        panelNewAccount = new javax.swing.JPanel();
        SYSTEMTITLE1 = new javax.swing.JLabel();
        newAccButtonActionSec = new javax.swing.JButton();
        newAccButtonAction = new javax.swing.JButton();
        newAccActionTitle = new javax.swing.JLabel();
        tfAccountNumber = new javax.swing.JTextField();
        tfContactNumber = new javax.swing.JTextField();
        tfInitialDeposit = new javax.swing.JTextField();
        tfFirstName = new javax.swing.JTextField();
        tfLastName = new javax.swing.JTextField();
        txtAddressSP = new javax.swing.JScrollPane();
        taAddress = new javax.swing.JTextArea();
        dcBirthday = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(null);

        panelMenu.setBackground(new java.awt.Color(91, 192, 235));
        panelMenu.setLayout(null);

        ButtonLogout.setBackground(new java.awt.Color(91, 192, 235));
        ButtonLogout.setFont(new java.awt.Font("Nirmala UI", 1, 16)); // NOI18N
        ButtonLogout.setForeground(new java.awt.Color(191, 191, 191));
        ButtonLogout.setText("  LOG-OUT");
        ButtonLogout.setBorder(null);
        ButtonLogout.setContentAreaFilled(false);
        ButtonLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonLogout.setFocusPainted(false);
        ButtonLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonLogout.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ButtonLogout.setOpaque(true);
        ButtonLogout.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ButtonLogout.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        panelMenu.add(ButtonLogout);
        ButtonLogout.setBounds(0, 530, 230, 70);

        ButtonBalance.setBackground(new java.awt.Color(91, 192, 235));
        ButtonBalance.setFont(new java.awt.Font("Nirmala UI", 1, 16)); // NOI18N
        ButtonBalance.setForeground(new java.awt.Color(191, 191, 191));
        ButtonBalance.setText("  BALANCE INQUIRY");
        ButtonBalance.setBorder(null);
        ButtonBalance.setContentAreaFilled(false);
        ButtonBalance.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonBalance.setFocusPainted(false);
        ButtonBalance.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonBalance.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ButtonBalance.setOpaque(true);
        ButtonBalance.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ButtonBalance.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        panelMenu.add(ButtonBalance);
        ButtonBalance.setBounds(0, 180, 230, 70);

        ButtonDeposit.setBackground(new java.awt.Color(91, 192, 235));
        ButtonDeposit.setFont(new java.awt.Font("Nirmala UI", 1, 16)); // NOI18N
        ButtonDeposit.setForeground(new java.awt.Color(191, 191, 191));
        ButtonDeposit.setText("  DEPOSIT");
        ButtonDeposit.setBorder(null);
        ButtonDeposit.setContentAreaFilled(false);
        ButtonDeposit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonDeposit.setFocusPainted(false);
        ButtonDeposit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonDeposit.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ButtonDeposit.setOpaque(true);
        ButtonDeposit.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ButtonDeposit.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        panelMenu.add(ButtonDeposit);
        ButtonDeposit.setBounds(0, 250, 230, 70);

        ButtonWithdraw.setBackground(new java.awt.Color(91, 192, 235));
        ButtonWithdraw.setFont(new java.awt.Font("Nirmala UI", 1, 16)); // NOI18N
        ButtonWithdraw.setForeground(new java.awt.Color(191, 191, 191));
        ButtonWithdraw.setText("  WITHDRAW");
        ButtonWithdraw.setBorder(null);
        ButtonWithdraw.setContentAreaFilled(false);
        ButtonWithdraw.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonWithdraw.setFocusPainted(false);
        ButtonWithdraw.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonWithdraw.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ButtonWithdraw.setOpaque(true);
        ButtonWithdraw.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ButtonWithdraw.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        panelMenu.add(ButtonWithdraw);
        ButtonWithdraw.setBounds(0, 320, 230, 70);

        ButtonProfile.setBackground(new java.awt.Color(91, 192, 235));
        ButtonProfile.setFont(new java.awt.Font("Nirmala UI", 1, 16)); // NOI18N
        ButtonProfile.setForeground(new java.awt.Color(191, 191, 191));
        ButtonProfile.setText("  PROFILE");
        ButtonProfile.setBorder(null);
        ButtonProfile.setContentAreaFilled(false);
        ButtonProfile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonProfile.setFocusPainted(false);
        ButtonProfile.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonProfile.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ButtonProfile.setOpaque(true);
        ButtonProfile.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ButtonProfile.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        panelMenu.add(ButtonProfile);
        ButtonProfile.setBounds(0, 390, 230, 70);

        ButtonCloseAccount.setBackground(new java.awt.Color(91, 192, 235));
        ButtonCloseAccount.setFont(new java.awt.Font("Nirmala UI", 1, 16)); // NOI18N
        ButtonCloseAccount.setForeground(new java.awt.Color(191, 191, 191));
        ButtonCloseAccount.setText("  CLOSE ACCOUNT");
        ButtonCloseAccount.setBorder(null);
        ButtonCloseAccount.setContentAreaFilled(false);
        ButtonCloseAccount.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonCloseAccount.setFocusPainted(false);
        ButtonCloseAccount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonCloseAccount.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ButtonCloseAccount.setOpaque(true);
        ButtonCloseAccount.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ButtonCloseAccount.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        panelMenu.add(ButtonCloseAccount);
        ButtonCloseAccount.setBounds(0, 460, 230, 70);

        panelRight.setBackground(new java.awt.Color(229, 89, 52));
        panelRight.setLayout(null);

        tfRightInput.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        panelRight.add(tfRightInput);
        tfRightInput.setBounds(80, 150, 420, 55);

        ButtonRightAction.setBackground(new java.awt.Color(229, 89, 52));
        ButtonRightAction.setFont(new java.awt.Font("Nirmala UI", 1, 21)); // NOI18N
        ButtonRightAction.setForeground(new java.awt.Color(191, 191, 191));
        ButtonRightAction.setText("<INSERT ACTION>");
        ButtonRightAction.setBorder(null);
        ButtonRightAction.setContentAreaFilled(false);
        ButtonRightAction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonRightAction.setFocusPainted(false);
        ButtonRightAction.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ButtonRightAction.setOpaque(true);
        panelRight.add(ButtonRightAction);
        ButtonRightAction.setBounds(100, 450, 370, 80);

        LabelRightTime.setFont(new java.awt.Font("Nirmala UI", 0, 24)); // NOI18N
        LabelRightTime.setForeground(new java.awt.Color(255, 255, 255));
        LabelRightTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        LabelRightTime.setText("TIME");
        LabelRightTime.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelRight.add(LabelRightTime);
        LabelRightTime.setBounds(60, 10, 500, 40);

        LabelRightOut.setFont(new java.awt.Font("Nirmala UI", 1, 24)); // NOI18N
        LabelRightOut.setForeground(new java.awt.Color(255, 255, 255));
        LabelRightOut.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelRightOut.setText("<INSERT OUTPUT>");
        LabelRightOut.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelRight.add(LabelRightOut);
        LabelRightOut.setBounds(40, 90, 500, 450);

        panelMenu.add(panelRight);
        panelRight.setBounds(230, 0, 570, 600);

        SYSTEMTITLE2.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        SYSTEMTITLE2.setForeground(new java.awt.Color(255, 255, 255));
        SYSTEMTITLE2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SYSTEMTITLE2.setText("<HTML>BANKING<BR/>SYSTEM</HTML>");
        SYSTEMTITLE2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelMenu.add(SYSTEMTITLE2);
        SYSTEMTITLE2.setBounds(10, 0, 170, 100);

        LabelUser.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        LabelUser.setForeground(new java.awt.Color(255, 255, 255));
        LabelUser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LabelUser.setText("<html>Welcome<br/><strong>Juan Pedro Dela Cruz</strong></html>");
        LabelUser.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelMenu.add(LabelUser);
        LabelUser.setBounds(10, 90, 220, 70);

        getContentPane().add(panelMenu);
        panelMenu.setBounds(0, 0, 800, 600);

        panelMain.setBackground(new java.awt.Color(91, 192, 235));
        panelMain.setLayout(null);

        panelLogin.setBackground(new java.awt.Color(91, 192, 235));
        panelLogin.setLayout(null);

        tfPIN.setFont(new java.awt.Font("Nirmala UI", 0, 36)); // NOI18N
        tfPIN.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        panelLogin.add(tfPIN);
        tfPIN.setBounds(130, 160, 290, 55);

        SYSTEMTITLE.setFont(new java.awt.Font("SansSerif", 1, 27)); // NOI18N
        SYSTEMTITLE.setForeground(new java.awt.Color(255, 255, 255));
        SYSTEMTITLE.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SYSTEMTITLE.setText("  BANKING SYSTEM");
        SYSTEMTITLE.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelLogin.add(SYSTEMTITLE);
        SYSTEMTITLE.setBounds(0, 10, 550, 70);

        loginButtonActionSec.setBackground(new java.awt.Color(155, 197, 61));
        loginButtonActionSec.setFont(new java.awt.Font("Nirmala UI", 1, 16)); // NOI18N
        loginButtonActionSec.setForeground(new java.awt.Color(191, 191, 191));
        loginButtonActionSec.setText("EXIT");
        loginButtonActionSec.setBorder(null);
        loginButtonActionSec.setContentAreaFilled(false);
        loginButtonActionSec.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginButtonActionSec.setFocusPainted(false);
        loginButtonActionSec.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        loginButtonActionSec.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        loginButtonActionSec.setOpaque(true);
        loginButtonActionSec.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        loginButtonActionSec.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        panelLogin.add(loginButtonActionSec);
        loginButtonActionSec.setBounds(130, 340, 210, 50);

        loginButtonAction.setBackground(new java.awt.Color(155, 197, 61));
        loginButtonAction.setFont(new java.awt.Font("Nirmala UI", 1, 16)); // NOI18N
        loginButtonAction.setForeground(new java.awt.Color(191, 191, 191));
        loginButtonAction.setText("CREATE NEW ACCOUNT");
        loginButtonAction.setBorder(null);
        loginButtonAction.setContentAreaFilled(false);
        loginButtonAction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginButtonAction.setFocusPainted(false);
        loginButtonAction.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        loginButtonAction.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        loginButtonAction.setOpaque(true);
        loginButtonAction.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        loginButtonAction.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        panelLogin.add(loginButtonAction);
        loginButtonAction.setBounds(130, 260, 210, 70);

        loginActionTitle.setFont(new java.awt.Font("Nirmala UI", 1, 24)); // NOI18N
        loginActionTitle.setForeground(new java.awt.Color(255, 255, 255));
        loginActionTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginActionTitle.setText("Enter your 4 DIGIT PIN");
        panelLogin.add(loginActionTitle);
        loginActionTitle.setBounds(0, 80, 550, 100);

        panelMain.add(panelLogin);
        panelLogin.setBounds(150, 60, 550, 450);

        panelNewAccount.setBackground(new java.awt.Color(91, 192, 235));
        panelNewAccount.setLayout(null);

        SYSTEMTITLE1.setFont(new java.awt.Font("SansSerif", 1, 27)); // NOI18N
        SYSTEMTITLE1.setForeground(new java.awt.Color(255, 255, 255));
        SYSTEMTITLE1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SYSTEMTITLE1.setText("  BANKING SYSTEM");
        SYSTEMTITLE1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelNewAccount.add(SYSTEMTITLE1);
        SYSTEMTITLE1.setBounds(0, 10, 550, 70);

        newAccButtonActionSec.setBackground(new java.awt.Color(155, 197, 61));
        newAccButtonActionSec.setFont(new java.awt.Font("Nirmala UI", 1, 16)); // NOI18N
        newAccButtonActionSec.setForeground(new java.awt.Color(191, 191, 191));
        newAccButtonActionSec.setText("BACK");
        newAccButtonActionSec.setBorder(null);
        newAccButtonActionSec.setContentAreaFilled(false);
        newAccButtonActionSec.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        newAccButtonActionSec.setFocusPainted(false);
        newAccButtonActionSec.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        newAccButtonActionSec.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        newAccButtonActionSec.setOpaque(true);
        newAccButtonActionSec.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        newAccButtonActionSec.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        panelNewAccount.add(newAccButtonActionSec);
        newAccButtonActionSec.setBounds(0, 490, 170, 50);

        newAccButtonAction.setBackground(new java.awt.Color(155, 197, 61));
        newAccButtonAction.setFont(new java.awt.Font("Nirmala UI", 1, 16)); // NOI18N
        newAccButtonAction.setForeground(new java.awt.Color(191, 191, 191));
        newAccButtonAction.setText("CREATE ACCOUNT");
        newAccButtonAction.setBorder(null);
        newAccButtonAction.setContentAreaFilled(false);
        newAccButtonAction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        newAccButtonAction.setEnabled(false);
        newAccButtonAction.setFocusPainted(false);
        newAccButtonAction.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        newAccButtonAction.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        newAccButtonAction.setOpaque(true);
        newAccButtonAction.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        newAccButtonAction.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        panelNewAccount.add(newAccButtonAction);
        newAccButtonAction.setBounds(0, 415, 170, 70);

        newAccActionTitle.setFont(new java.awt.Font("Nirmala UI", 1, 24)); // NOI18N
        newAccActionTitle.setForeground(new java.awt.Color(255, 255, 255));
        newAccActionTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newAccActionTitle.setText("CREATE NEW ACCOUNT");
        panelNewAccount.add(newAccActionTitle);
        newAccActionTitle.setBounds(0, 50, 660, 60);

        tfAccountNumber.setFont(new java.awt.Font("Nirmala UI", 0, 16)); // NOI18N
        tfAccountNumber.setText("ACCOUNT NUMBER");
        tfAccountNumber.setToolTipText("ACCOUNT NUMBER");
        tfAccountNumber.setEnabled(false);
        panelNewAccount.add(tfAccountNumber);
        tfAccountNumber.setBounds(190, 110, 290, 45);

        tfContactNumber.setFont(new java.awt.Font("Nirmala UI", 0, 16)); // NOI18N
        tfContactNumber.setText("CONTACT NUMBER");
        panelNewAccount.add(tfContactNumber);
        tfContactNumber.setBounds(190, 310, 290, 45);

        tfInitialDeposit.setFont(new java.awt.Font("Nirmala UI", 0, 16)); // NOI18N
        tfInitialDeposit.setText("INITIAL DEPOSIT");
        panelNewAccount.add(tfInitialDeposit);
        tfInitialDeposit.setBounds(190, 160, 290, 45);

        tfFirstName.setFont(new java.awt.Font("Nirmala UI", 0, 16)); // NOI18N
        tfFirstName.setText("FIRST NAME");
        panelNewAccount.add(tfFirstName);
        tfFirstName.setBounds(190, 210, 290, 45);

        tfLastName.setFont(new java.awt.Font("Nirmala UI", 0, 16)); // NOI18N
        tfLastName.setText("LAST NAME");
        panelNewAccount.add(tfLastName);
        tfLastName.setBounds(190, 260, 290, 45);

        txtAddressSP.setBorder(null);
        txtAddressSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        txtAddressSP.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        txtAddressSP.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N

        taAddress.setBackground(new java.awt.Color(219, 219, 219));
        taAddress.setColumns(20);
        taAddress.setFont(new java.awt.Font("Nirmala UI", 0, 16)); // NOI18N
        taAddress.setForeground(new java.awt.Color(19, 19, 19));
        taAddress.setLineWrap(true);
        taAddress.setRows(5);
        taAddress.setText("ADDRESS");
        taAddress.setWrapStyleWord(true);
        taAddress.setBorder(null);
        taAddress.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                taAddressFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                taAddressFocusLost(evt);
            }
        });
        txtAddressSP.setViewportView(taAddress);

        panelNewAccount.add(txtAddressSP);
        txtAddressSP.setBounds(190, 410, 290, 90);

        dcBirthday.setBackground(new java.awt.Color(219, 219, 219));
        dcBirthday.setForeground(new java.awt.Color(19, 19, 19));
        dcBirthday.setToolTipText("");
        dcBirthday.setDateFormatString("yyyy-MM-dd");
        dcBirthday.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        dcBirthday.setName("dcBday"); // NOI18N
        panelNewAccount.add(dcBirthday);
        dcBirthday.setBounds(190, 360, 290, 45);

        panelMain.add(panelNewAccount);
        panelNewAccount.setBounds(90, 20, 660, 540);

        getContentPane().add(panelMain);
        panelMain.setBounds(0, 0, 800, 600);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void taAddressFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_taAddressFocusGained
        // TODO add your handling code here:
        if(taAddress.getText().equals("ADDRESS"))
            taAddress.setText("");
            
    }//GEN-LAST:event_taAddressFocusGained

    private void taAddressFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_taAddressFocusLost
        // TODO add your handling code here:
        if(taAddress.getText().isEmpty())
            taAddress.setText("ADDRESS");
    }//GEN-LAST:event_taAddressFocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonBalance;
    private javax.swing.JButton ButtonCloseAccount;
    private javax.swing.JButton ButtonDeposit;
    private javax.swing.JButton ButtonLogout;
    private javax.swing.JButton ButtonProfile;
    private javax.swing.JButton ButtonRightAction;
    private javax.swing.JButton ButtonWithdraw;
    private javax.swing.JLabel LabelRightOut;
    private javax.swing.JLabel LabelRightTime;
    private javax.swing.JLabel LabelUser;
    private javax.swing.JLabel SYSTEMTITLE;
    private javax.swing.JLabel SYSTEMTITLE1;
    private javax.swing.JLabel SYSTEMTITLE2;
    private com.toedter.calendar.JDateChooser dcBirthday;
    private javax.swing.JLabel loginActionTitle;
    private javax.swing.JButton loginButtonAction;
    private javax.swing.JButton loginButtonActionSec;
    private javax.swing.JLabel newAccActionTitle;
    private javax.swing.JButton newAccButtonAction;
    private javax.swing.JButton newAccButtonActionSec;
    private javax.swing.JPanel panelLogin;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JPanel panelNewAccount;
    private javax.swing.JPanel panelRight;
    private javax.swing.JTextArea taAddress;
    private javax.swing.JTextField tfAccountNumber;
    private javax.swing.JTextField tfContactNumber;
    private javax.swing.JTextField tfFirstName;
    private javax.swing.JTextField tfInitialDeposit;
    private javax.swing.JTextField tfLastName;
    private javax.swing.JPasswordField tfPIN;
    private javax.swing.JTextField tfRightInput;
    private javax.swing.JScrollPane txtAddressSP;
    // End of variables declaration//GEN-END:variables
}
