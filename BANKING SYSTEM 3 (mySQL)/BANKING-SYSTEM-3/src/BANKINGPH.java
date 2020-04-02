
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
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
public class BANKINGPH extends javax.swing.JFrame {

    /**
     * Creates new form BANKINGPH
     */
    public BANKINGPH() {
        initComponents();
        
        init();
        stateLogin();
    }

    //BANKING OPERATIONS
    final static int MAINTAINING_BALANCE = 5000;
    final static int MINIMUM_AMOUNT = 100;
    final static double INTEREST_RATE = 0.05;
    private List<Customer> customers = new ArrayList<>();
    private Customer customer = null;
    private myDatabase myDB = new myDatabase();
    private int newID = 19;
    
    // COLOR SCHEME HOVER
    private final Color HOVEROUTFGCOLOR = new Color(191,191,191);
    private final Color HOVEROUTBGCOLOR = new Color(162,40,40);
    private final Color HOVERINFGCOLOR = new Color(162,40,40);
    private final Color HOVERINBGCOLOR = new Color(255,255,255);
    
    private JButton topPanelButtons[] = null;
    private JTextField accountFormFields[] = null;
    
    
    private void init(){
        dtBirthday.setMaxSelectableDate(new Date());
        setDate();
        buttonsInit();
        resetMidPanel();
        setEnableTopPanelButtons(true);
        PanelMidAccount.setVisible(false);
        accountFormFields = new JTextField[]{txtAccNo, txtBalance, txtFName, txtLName, txtContactNo};
        resetAccountForm();
    }
    private void buttonsInit(){
        topPanelButtons = new JButton[]{btnTopBalance, btnTopDeposit, btnTopWithdraw, btnTopProfile, btnTopCloseAccount, lblTopX, btnMidAction, btnLogout, btnSideAction};
        for(JButton b : topPanelButtons){
            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e){
                    if(b.isEnabled()){ 
                        b.setBackground(HOVERINBGCOLOR);
                        b.setForeground(HOVERINFGCOLOR);
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e){
                    if(b.isFocusable()){
                        b.setBackground(HOVEROUTBGCOLOR);
                        b.setForeground(HOVEROUTFGCOLOR);
                    }
                }
                
                @Override
                public void mouseClicked(MouseEvent e){
                    if(b.isEnabled()){
                        if(b != btnMidAction && b != lblTopX && b!= btnLogout && b != btnSideAction){
                            for(JButton btn : topPanelButtons){
                                if(!btn.isFocusable())
                                    btn.setEnabled(true);
                                btn.setFocusable(true);
                                btn.setBackground(HOVEROUTBGCOLOR);
                                btn.setForeground(HOVEROUTFGCOLOR);
                            }
                            b.setFocusable(false);
                            b.setEnabled(false);
                            b.setBackground(HOVERINBGCOLOR);
                            b.setForeground(HOVERINFGCOLOR);
                        }
                        
                        if(b.equals(btnTopBalance))
                            stateBalance();
                        
                        if(b.equals(btnTopDeposit))
                            stateDeposit();
                        
                        if(b.equals(btnTopWithdraw))
                            stateWithdraw();
                        
                        if(b.equals(btnTopProfile))
                            stateProfile();
                        
                        if(b.equals(btnTopCloseAccount))
                            stateCloseAccount();
                        
                        if(b.equals(btnMidAction)){
                            switch(b.getText()){
                                case "CREATE NEW ACCOUNT": createNewAccount(); break;
                                case "DEPOSIT AMOUNT": deposit(); break;
                                case "WITHDRAW AMOUNT": withdraw(); break;
                                case "YES": closeAccount(); break;
                                case "CLOSE BALANCE": stateMenu(); break;
                                case "CLOSE PROFILE": stateMenu(); break;
                                case "CLOSE DEPOSIT": stateMenu(); break;
                                case "CLOSE WITHDRAW": stateMenu(); break;
                                case "BACK TO LOGIN": stateLogin();break;
                            }
                        }
                        
                        if(b.equals(btnSideAction))
                            if(b.getText().equals("Create New Account")) 
                                stateNewAccount();
                        
                        if(b.equals(btnLogout)){
                            int con = JOptionPane.showConfirmDialog(PanelTop, "Are you sure you want to Logout?", "BANKING SYSTEM   |   LOGOUT", JOptionPane.YES_NO_OPTION , JOptionPane.WARNING_MESSAGE);
                            if(con == 0)
                                stateLogin();
                            lblTopX.setFocusable(true);
                        }
                        if(b.equals(lblTopX)){
                            int con = JOptionPane.showConfirmDialog(PanelTop, "Are you sure you want to exit?", "BANKING SYSTEM   |   EXIT", JOptionPane.YES_NO_OPTION , JOptionPane.ERROR_MESSAGE);
                            if(con == 0)
                                System.exit(0);
                            lblTopX.setFocusable(true);
                        }
                        
                    }
                }
            });
            
        }
    }
    
    private void setEnableTopPanelButtons(boolean bool){
        for(JButton b : topPanelButtons)
            if(b != lblTopX && b != btnMidAction && b != btnLogout && b != btnSideAction)
                b.setEnabled(bool);
        btnLogout.setVisible(bool);
    }
    
    private void setAccountFormFieldsEnable(boolean bool){
        for(JTextField tf : accountFormFields)
            if(tf != txtAccNo)
                tf.setEnabled(bool);
        dtBirthday.setEnabled(bool);
        txtAddress.setEnabled(bool);
    }
    private void resetAccountForm() {
        for(JTextField tf : accountFormFields)
            tf.setText("");
        dtBirthday.setDate(null);
        txtAddress.setText("");
        
        setAccountFormFieldsEnable(false);
    }
    private void resetMidPanel() {
        PanelMidAccount.setVisible(false);
        lblMidActionTitle.setVisible(false);
        lblMidOut.setVisible(false);
        btnSideAction.setVisible(false);
        txtMidInput.setVisible(false);
        txtMidInput.setText("");
        txtMidInputLogin.setVisible(false);
        txtMidInputLogin.setText("");
        customers = myDB.getAllCustomers();
        newID = customers.size();
    }
    
    
    public void setDate(){
        Timer timer = new Timer(1000, new setDateActionListener());
        timer.start();
        Timer timer2 = new Timer(500, new validationActionListener());
        timer2.start();
    }
    class setDateActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            lblTopTime.setText(new SimpleDateFormat("dd-MM-yyyy  |  HH:mm:ss").format(Calendar.getInstance().getTime()));
        }
    }
    class validationActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(lblMidActionTitle.getText().equals("NEW ACCOUNT"))
                validateAccountForm();
            
            if(btnMidAction.getText().equals("DEPOSIT AMOUNT") || btnMidAction.getText().equals("WITHDRAW AMOUNT"))
                if(!txtMidInput.getText().isEmpty())
                    validateAmount();
        }
    }
    
    private void  validateAccountForm(){
        boolean isValidated = false;
        
        for(JTextField tf : accountFormFields)
            isValidated = !tf.getText().isEmpty();
        
        isValidated = (dtBirthday.getDate() != null && !txtAddress.getText().isEmpty());
        
        if(!txtBalance.getText().isEmpty())
            if(Integer.valueOf(txtBalance.getText()) < MAINTAINING_BALANCE){
                txtBalance.setForeground(Color.red);
                txtBalance.setToolTipText("MINIMUM INITIAL DEPOSIT IS " + MAINTAINING_BALANCE);
                txtBalance.grabFocus();
                isValidated = false;
            }else{
                txtBalance.setForeground(Color.black);
                txtBalance.setToolTipText(null);
            }
        
        btnMidAction.setEnabled(isValidated);
        btnMidAction.setFocusable(isValidated);
    }
    private void validateAmount(){
        boolean isValidated = false;
        lblMidOut.setText("");
        txtMidInput.setForeground(new Color(19,19,19));
        
        if(Integer.valueOf(txtMidInput.getText()) < MINIMUM_AMOUNT){
            lblMidOut.setText("MINIMUM AMOUNT IS PHP "+ MINIMUM_AMOUNT);
            txtMidInput.setForeground(new Color(162,40,40));
        }else{
            isValidated = true;
        }
        
        if(lblMidActionTitle.getText().equals("WITHDRAW")){
            if((customer.getBalance() - Integer.valueOf(txtMidInput.getText())) < MAINTAINING_BALANCE){
                lblMidOut.setText("MAINTAINING BALANCE IS PHP "+ MAINTAINING_BALANCE);   
                
                if((Integer.valueOf(txtMidInput.getText())) > customer.getBalance())
                    lblMidOut.setText(" INSUFFICIENT BALANCE. ");
                    
                txtMidInput.setForeground(new Color(162,40,40));  
                isValidated = false;
            }
        }
        
        btnMidAction.setEnabled(isValidated);
        btnMidAction.setFocusable(isValidated);
    }
    
    
    private void setMidActionTitle(String title, boolean isVisible){
        lblMidActionTitle.setText(title);
        lblMidActionTitle.setVisible(isVisible);
    }
    private void setMidOut(String out, boolean isVisible){
        lblMidOut.setText(out);
        lblMidOut.setVisible(isVisible);
    }
    private void setMidBtnAction(String action, boolean isVisible){
        btnMidAction.setText(action);
        btnMidAction.setVisible(isVisible);
    }
    private void setMidBtnSideAction(String action, boolean isVisible){
        btnSideAction.setText(action);
        btnSideAction.setVisible(isVisible);
    }
    
    
    private int generateAccountNo(){
        int AccNo = 0;
        do {
            Random rand = new Random();
            AccNo = Integer.valueOf(String.format("%04d", rand.nextInt(10000)));
        } while (myDB.getCustomerByAccountNumber(AccNo) != null);
        
        return AccNo;
    }
    
    private void createNewAccount(){
        String val = "", values[];
        
        
        try {
            for(JTextField tf : accountFormFields)
            val += tf.getText() + ",";
            values = val.split(",");
        
            customer = new Customer(newID, Integer.valueOf(values[0]), Integer.valueOf(values[1]), values[2], values[3], values[4], ((JTextField) dtBirthday.getDateEditor().getUiComponent()).getText(), txtAddress.getText(), "ACTIVE");
            myDB.postCustomerData(customer);
            
            stateMenu();
        } catch (SQLException ex) {
            Logger.getLogger(BANKINGPH.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void deposit(){
        
        String Out = "<html><div style=\"text-align:right\">Balance: <strong>Php " + customer.getBalance() + "<br/>+ &nbsp;Php " + Integer.valueOf(txtMidInput.getText()) + "</strong>";
        
        customer.setBalance(customer.getBalance() + Integer.valueOf(txtMidInput.getText()));
        Out += "<br/>= &nbsp;<strong>Php " + customer.getBalance() + "</strong><br/>Interest:&nbsp;Php " + (int)(customer.getBalance() * INTEREST_RATE) + "\n";
        
        customer.setBalance(customer.getBalance() + (int)(customer.getBalance() * INTEREST_RATE));
        Out += "<br/>New Balance:&nbsp;<strong>Php " + customer.getBalance() + "</strong></html>";
        
        
        try {
            myDB.putCustomerNewBalance(customer, customer.getBalance());
        } catch (SQLException ex) {
            Logger.getLogger(BANKINGPH.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        txtMidInput.setVisible(false);
        setMidBtnAction("CLOSE DEPOSIT", true);
        setMidOut(Out, true);
    }
    
    private void withdraw(){
        String Out = "<html><div style=\"text-align:right\"> Balance: <strong>Php " + customer.getBalance() + "</strong><br/>" +
"<strong>- &nbsp;Php " + Integer.valueOf(txtMidInput.getText()) + "</strong><br/>";
        
        customer.setBalance(customer.getBalance() - Integer.valueOf(txtMidInput.getText()));
        Out += "New Balance: <strong>Php " + customer.getBalance() + "</strong></div></html>";
        
        try {
            myDB.putCustomerNewBalance(customer, customer.getBalance());
        } catch (SQLException ex) {
            Logger.getLogger(BANKINGPH.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        txtMidInput.setVisible(false);
        setMidBtnAction("CLOSE WITHDRAW", true);
        setMidOut(Out, true);
    }
    
    private void closeAccount(){
        String Out = "<html><div style=\"text-align:center\"> Your last balance is "+ customer.getBalance() +"<br/> It is now 0. <br/><br/>YOUR BANK ACCOUNT IS NOW CLOSED.</div></html>";
        
        try {
            myDB.putCustomerNewStatus(customer, "CLOSED");
            myDB.putCustomerNewBalance(customer, 0);
        } catch (SQLException ex) {
            Logger.getLogger(BANKINGPH.class.getName()).log(Level.SEVERE, null, ex);
        }
        setEnableTopPanelButtons(false);
        txtMidInput.setVisible(false);
        setMidBtnAction("BACK TO LOGIN", true);
        setMidOut(Out, true);
    }
    
    
    // STATES
    
    private void stateLogin(){
        resetMidPanel();
        setEnableTopPanelButtons(false);
        txtMidInputLogin.setVisible(true);
        setMidActionTitle("LOGIN", true);
        setMidBtnAction("", false);
        setMidOut("PLEASE ENTER YOUR 4 DIGIT ACCOUNT NUMBER", true);
        setMidBtnSideAction("Create New Account", true);
        txtMidInputLogin.grabFocus();
        
        for(JButton jb : topPanelButtons){
            jb.setForeground(HOVEROUTFGCOLOR);
            jb.setBackground(HOVEROUTBGCOLOR);
        }
    }
    
    private void stateNewAccount(){
        resetMidPanel();
        resetAccountForm();
        setEnableTopPanelButtons(false);
        setAccountFormFieldsEnable(true);
        setMidActionTitle("NEW ACCOUNT", true);
        setMidBtnAction("CREATE NEW ACCOUNT", true);
        txtAccNo.setText(generateAccountNo()+"");
        setMidBtnSideAction("Back to Login", true);
        PanelMidAccount.setVisible(true);
        txtBalance.grabFocus();
        lbl3.setText("<html><center>INITIAL <br/>DEPOSIT</html>");
    }
    
    private void stateMenu(){
        String out = "<html>";
        
        out += "<p style=\"text-align: center;\">Hello, <strong><em>"+ customer.getfName()  +" "+ customer.getlName()  +"</em></strong></p>\n" +
"<p style=\"text-align: center;\">\n" +
"  <br>\n" +
"</p>\n" +
"<p style=\"text-align: center;\">What would you like to do today?</p></html>";
        
        resetMidPanel();
        setEnableTopPanelButtons(true);
        setMidActionTitle("", false);
        setMidBtnAction("", false);
        setMidOut(out, true);
        
        for(JButton jb : topPanelButtons){
            jb.setForeground(HOVEROUTFGCOLOR);
            jb.setBackground(HOVEROUTBGCOLOR);
        }   
    }
    
    private void stateBalance() {
        resetMidPanel();
        String out = "<html><center>Your current balance is <br/><br/>PHP " + customer.getBalance() +"</center></html>";
        setMidActionTitle("BALANCE", true);
        setMidBtnAction("CLOSE BALANCE", true);
        setMidOut(out, true);
    }
    
    private void stateDeposit() {
        resetMidPanel();
        setMidActionTitle("DEPOSIT", true);
        setMidBtnAction("DEPOSIT AMOUNT", true);
        setMidOut("Enter Amount (Minimum PHP "+ MINIMUM_AMOUNT +")", true);
        txtMidInput.setVisible(true);
    }
    
    private void stateWithdraw() {
        resetMidPanel();
        setMidActionTitle("WITHDRAW", true);
        setMidBtnAction("WITHDRAW AMOUNT", true);
        setMidOut("Enter Amount (Minimum PHP "+ MINIMUM_AMOUNT +")", true);
        txtMidInput.setVisible(true);
    }
    
    private void stateProfile() {
        resetMidPanel();
        setAccountFormFieldsEnable(false);
        PanelMidAccount.setVisible(true);
        setMidActionTitle("MY PROFILE", true);
        setMidBtnAction("CLOSE PROFILE", true);
        
        txtAccNo.setText(customer.getAccountNo()+"");
        txtBalance.setText(customer.getBalance()+"");
        txtAddress.setText(customer.getAddress()+"");
        txtContactNo.setText(customer.getContactNo()+"");
        txtFName.setText(customer.getfName()+"");
        txtLName.setText(customer.getlName()+"");
        try {
            dtBirthday.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(customer.getBirthday()));
        } catch (ParseException ex) {
            Logger.getLogger(BANKINGPH.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtAccNo.setText(customer.getAccountNo()+"");
        lbl3.setText("BALANCE");
    }
    
    private void stateCloseAccount(){
        resetMidPanel();
        String out = "<html><center>Good day, "+customer.getfName()+"<br/><br/>Are you sure you want<br/> to Close your Bank Account?</center></html>";
        setMidActionTitle("CLOSE ACCOUNT", true);
        setMidBtnAction("YES", true);
        setMidOut(out, true);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelTop = new javax.swing.JPanel();
        lblTopTime = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnTopBalance = new javax.swing.JButton();
        btnTopDeposit = new javax.swing.JButton();
        btnTopWithdraw = new javax.swing.JButton();
        btnTopProfile = new javax.swing.JButton();
        btnTopCloseAccount = new javax.swing.JButton();
        lblTopX = new javax.swing.JButton();
        lblTopTitle = new javax.swing.JButton();
        PanelBody = new javax.swing.JPanel();
        btnSideAction = new javax.swing.JButton();
        PanelMidAccount = new javax.swing.JPanel();
        txtContactNo = new javax.swing.JTextField();
        txtLName = new javax.swing.JTextField();
        txtBalance = new javax.swing.JTextField();
        txtAccNo = new javax.swing.JTextField();
        txtFName = new javax.swing.JTextField();
        dtBirthday = new com.toedter.calendar.JDateChooser();
        txtAddressSP = new javax.swing.JScrollPane();
        txtAddress = new javax.swing.JTextArea();
        lbl1 = new javax.swing.JLabel();
        lbl2 = new javax.swing.JLabel();
        lbl3 = new javax.swing.JLabel();
        lbl4 = new javax.swing.JLabel();
        lbl5 = new javax.swing.JLabel();
        lbl6 = new javax.swing.JLabel();
        lbl7 = new javax.swing.JLabel();
        PanelMid = new javax.swing.JPanel();
        txtMidInputLogin = new javax.swing.JPasswordField();
        txtMidInput = new javax.swing.JTextField();
        lblMidOut = new javax.swing.JLabel();
        btnMidAction = new javax.swing.JButton();
        lblMidActionTitle = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(null);

        PanelTop.setBackground(new java.awt.Color(172, 50, 50));
        PanelTop.setForeground(new java.awt.Color(191, 191, 191));
        PanelTop.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        PanelTop.setLayout(null);

        lblTopTime.setBackground(new java.awt.Color(162, 40, 40));
        lblTopTime.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 19)); // NOI18N
        lblTopTime.setForeground(new java.awt.Color(255, 255, 255));
        lblTopTime.setText("DATE | TIME");
        lblTopTime.setBorder(null);
        lblTopTime.setContentAreaFilled(false);
        lblTopTime.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblTopTime.setEnabled(false);
        lblTopTime.setFocusPainted(false);
        lblTopTime.setFocusable(false);
        lblTopTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTopTime.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        lblTopTime.setOpaque(true);
        PanelTop.add(lblTopTime);
        lblTopTime.setBounds(530, 0, 200, 30);

        btnLogout.setBackground(new java.awt.Color(162, 40, 40));
        btnLogout.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 21)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(191, 191, 191));
        btnLogout.setText("LOGOUT");
        btnLogout.setBorder(null);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.setFocusPainted(false);
        btnLogout.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        btnLogout.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnLogout.setOpaque(true);
        PanelTop.add(btnLogout);
        btnLogout.setBounds(660, 40, 110, 30);

        btnTopBalance.setBackground(new java.awt.Color(162, 40, 40));
        btnTopBalance.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        btnTopBalance.setForeground(new java.awt.Color(191, 191, 191));
        btnTopBalance.setText("BALANCE");
        btnTopBalance.setBorder(null);
        btnTopBalance.setContentAreaFilled(false);
        btnTopBalance.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTopBalance.setEnabled(false);
        btnTopBalance.setFocusPainted(false);
        btnTopBalance.setOpaque(true);
        PanelTop.add(btnTopBalance);
        btnTopBalance.setBounds(0, 80, 155, 50);

        btnTopDeposit.setBackground(new java.awt.Color(162, 40, 40));
        btnTopDeposit.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        btnTopDeposit.setForeground(new java.awt.Color(191, 191, 191));
        btnTopDeposit.setText("DEPOSIT");
        btnTopDeposit.setBorder(null);
        btnTopDeposit.setContentAreaFilled(false);
        btnTopDeposit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTopDeposit.setEnabled(false);
        btnTopDeposit.setFocusPainted(false);
        btnTopDeposit.setOpaque(true);
        PanelTop.add(btnTopDeposit);
        btnTopDeposit.setBounds(160, 80, 155, 50);

        btnTopWithdraw.setBackground(new java.awt.Color(162, 40, 40));
        btnTopWithdraw.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        btnTopWithdraw.setForeground(new java.awt.Color(191, 191, 191));
        btnTopWithdraw.setText("WITHDRAW");
        btnTopWithdraw.setBorder(null);
        btnTopWithdraw.setContentAreaFilled(false);
        btnTopWithdraw.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTopWithdraw.setEnabled(false);
        btnTopWithdraw.setFocusPainted(false);
        btnTopWithdraw.setOpaque(true);
        PanelTop.add(btnTopWithdraw);
        btnTopWithdraw.setBounds(320, 80, 155, 50);

        btnTopProfile.setBackground(new java.awt.Color(162, 40, 40));
        btnTopProfile.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        btnTopProfile.setForeground(new java.awt.Color(191, 191, 191));
        btnTopProfile.setText("PROFILE");
        btnTopProfile.setBorder(null);
        btnTopProfile.setContentAreaFilled(false);
        btnTopProfile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTopProfile.setEnabled(false);
        btnTopProfile.setFocusPainted(false);
        btnTopProfile.setOpaque(true);
        PanelTop.add(btnTopProfile);
        btnTopProfile.setBounds(480, 80, 155, 50);

        btnTopCloseAccount.setBackground(new java.awt.Color(162, 40, 40));
        btnTopCloseAccount.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        btnTopCloseAccount.setForeground(new java.awt.Color(191, 191, 191));
        btnTopCloseAccount.setText("CLOSE ACCOUNT");
        btnTopCloseAccount.setBorder(null);
        btnTopCloseAccount.setContentAreaFilled(false);
        btnTopCloseAccount.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTopCloseAccount.setEnabled(false);
        btnTopCloseAccount.setFocusPainted(false);
        btnTopCloseAccount.setOpaque(true);
        PanelTop.add(btnTopCloseAccount);
        btnTopCloseAccount.setBounds(640, 80, 160, 50);

        lblTopX.setBackground(new java.awt.Color(162, 40, 40));
        lblTopX.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 24)); // NOI18N
        lblTopX.setForeground(new java.awt.Color(191, 191, 191));
        lblTopX.setText("X");
        lblTopX.setBorder(null);
        lblTopX.setContentAreaFilled(false);
        lblTopX.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTopX.setFocusPainted(false);
        lblTopX.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblTopX.setOpaque(true);
        PanelTop.add(lblTopX);
        lblTopX.setBounds(770, 0, 30, 30);

        lblTopTitle.setBackground(new java.awt.Color(162, 40, 40));
        lblTopTitle.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 27)); // NOI18N
        lblTopTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTopTitle.setText("  BANKING SYSTEM");
        lblTopTitle.setActionCommand("   BANKING SYSTEM");
        lblTopTitle.setBorder(null);
        lblTopTitle.setContentAreaFilled(false);
        lblTopTitle.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblTopTitle.setEnabled(false);
        lblTopTitle.setFocusPainted(false);
        lblTopTitle.setFocusable(false);
        lblTopTitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTopTitle.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblTopTitle.setOpaque(true);
        PanelTop.add(lblTopTitle);
        lblTopTitle.setBounds(0, -10, 800, 84);

        getContentPane().add(PanelTop);
        PanelTop.setBounds(0, 0, 800, 130);

        PanelBody.setBackground(new java.awt.Color(170, 70, 70));
        PanelBody.setForeground(new java.awt.Color(191, 191, 191));
        PanelBody.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        PanelBody.setLayout(null);

        btnSideAction.setBackground(new java.awt.Color(162, 40, 40));
        btnSideAction.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 21)); // NOI18N
        btnSideAction.setForeground(new java.awt.Color(191, 191, 191));
        btnSideAction.setText("Create New Account");
        btnSideAction.setBorder(null);
        btnSideAction.setContentAreaFilled(false);
        btnSideAction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSideAction.setFocusPainted(false);
        btnSideAction.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnSideAction.setOpaque(true);
        PanelBody.add(btnSideAction);
        btnSideAction.setBounds(270, 350, 260, 30);

        PanelMidAccount.setBackground(new java.awt.Color(172, 50, 50));
        PanelMidAccount.setLayout(null);

        txtContactNo.setBackground(new java.awt.Color(219, 219, 219));
        txtContactNo.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        txtContactNo.setForeground(new java.awt.Color(19, 19, 19));
        txtContactNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtContactNoKeyReleased(evt);
            }
        });
        PanelMidAccount.add(txtContactNo);
        txtContactNo.setBounds(490, 30, 230, 45);

        txtLName.setBackground(new java.awt.Color(219, 219, 219));
        txtLName.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        txtLName.setForeground(new java.awt.Color(19, 19, 19));
        PanelMidAccount.add(txtLName);
        txtLName.setBounds(140, 210, 230, 45);

        txtBalance.setBackground(new java.awt.Color(219, 219, 219));
        txtBalance.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        txtBalance.setForeground(new java.awt.Color(19, 19, 19));
        txtBalance.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBalanceKeyReleased(evt);
            }
        });
        PanelMidAccount.add(txtBalance);
        txtBalance.setBounds(140, 90, 230, 45);

        txtAccNo.setBackground(new java.awt.Color(219, 219, 219));
        txtAccNo.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        txtAccNo.setForeground(new java.awt.Color(19, 19, 19));
        txtAccNo.setEnabled(false);
        PanelMidAccount.add(txtAccNo);
        txtAccNo.setBounds(140, 30, 230, 45);

        txtFName.setBackground(new java.awt.Color(219, 219, 219));
        txtFName.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        txtFName.setForeground(new java.awt.Color(19, 19, 19));
        PanelMidAccount.add(txtFName);
        txtFName.setBounds(140, 150, 230, 45);

        dtBirthday.setBackground(new java.awt.Color(219, 219, 219));
        dtBirthday.setForeground(new java.awt.Color(19, 19, 19));
        dtBirthday.setDateFormatString("yyyy-MM-dd");
        dtBirthday.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        dtBirthday.setName("dtBday"); // NOI18N
        PanelMidAccount.add(dtBirthday);
        dtBirthday.setBounds(490, 90, 230, 45);

        txtAddressSP.setBorder(null);
        txtAddressSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        txtAddressSP.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        txtAddressSP.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N

        txtAddress.setBackground(new java.awt.Color(219, 219, 219));
        txtAddress.setColumns(20);
        txtAddress.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        txtAddress.setForeground(new java.awt.Color(19, 19, 19));
        txtAddress.setLineWrap(true);
        txtAddress.setRows(5);
        txtAddress.setWrapStyleWord(true);
        txtAddress.setBorder(null);
        txtAddressSP.setViewportView(txtAddress);

        PanelMidAccount.add(txtAddressSP);
        txtAddressSP.setBounds(490, 150, 230, 100);

        lbl1.setBackground(new java.awt.Color(172, 50, 50));
        lbl1.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        lbl1.setForeground(new java.awt.Color(219, 219, 219));
        lbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl1.setText("ADDRESS");
        lbl1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        PanelMidAccount.add(lbl1);
        lbl1.setBounds(370, 150, 130, 30);

        lbl2.setBackground(new java.awt.Color(172, 50, 50));
        lbl2.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        lbl2.setForeground(new java.awt.Color(219, 219, 219));
        lbl2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl2.setText("ACC NO.");
        lbl2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        PanelMidAccount.add(lbl2);
        lbl2.setBounds(0, 40, 130, 30);

        lbl3.setBackground(new java.awt.Color(172, 50, 50));
        lbl3.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        lbl3.setForeground(new java.awt.Color(219, 219, 219));
        lbl3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl3.setText("<html><center>INITIAL <br/>DEPOSIT</html>");
        lbl3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        PanelMidAccount.add(lbl3);
        lbl3.setBounds(0, 80, 140, 60);

        lbl4.setBackground(new java.awt.Color(172, 50, 50));
        lbl4.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        lbl4.setForeground(new java.awt.Color(219, 219, 219));
        lbl4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl4.setText("FNAME");
        lbl4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        PanelMidAccount.add(lbl4);
        lbl4.setBounds(0, 160, 130, 30);

        lbl5.setBackground(new java.awt.Color(172, 50, 50));
        lbl5.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        lbl5.setForeground(new java.awt.Color(219, 219, 219));
        lbl5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl5.setText("LNAME");
        lbl5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        PanelMidAccount.add(lbl5);
        lbl5.setBounds(0, 220, 130, 30);

        lbl6.setBackground(new java.awt.Color(172, 50, 50));
        lbl6.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        lbl6.setForeground(new java.awt.Color(219, 219, 219));
        lbl6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl6.setText("<html><center>CONTACT <br/>NO</html>");
        lbl6.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        PanelMidAccount.add(lbl6);
        lbl6.setBounds(370, 30, 130, 50);

        lbl7.setBackground(new java.awt.Color(172, 50, 50));
        lbl7.setFont(new java.awt.Font("Yu Gothic UI", 0, 19)); // NOI18N
        lbl7.setForeground(new java.awt.Color(219, 219, 219));
        lbl7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl7.setText("BIRTHDAY");
        lbl7.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        PanelMidAccount.add(lbl7);
        lbl7.setBounds(370, 100, 130, 30);

        PanelBody.add(PanelMidAccount);
        PanelMidAccount.setBounds(30, 90, 740, 290);

        PanelMid.setBackground(new java.awt.Color(172, 50, 50));
        PanelMid.setLayout(null);

        txtMidInputLogin.setBackground(new java.awt.Color(219, 219, 219));
        txtMidInputLogin.setColumns(4);
        txtMidInputLogin.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 27)); // NOI18N
        txtMidInputLogin.setForeground(new java.awt.Color(19, 19, 19));
        txtMidInputLogin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMidInputLogin.setBorder(null);
        txtMidInputLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMidInputLoginKeyReleased(evt);
            }
        });
        PanelMid.add(txtMidInputLogin);
        txtMidInputLogin.setBounds(140, 150, 420, 70);

        txtMidInput.setBackground(new java.awt.Color(219, 219, 219));
        txtMidInput.setFont(new java.awt.Font("Yu Gothic UI", 0, 24)); // NOI18N
        txtMidInput.setForeground(new java.awt.Color(19, 19, 19));
        txtMidInput.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMidInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMidInputKeyReleased(evt);
            }
        });
        PanelMid.add(txtMidInput);
        txtMidInput.setBounds(140, 150, 420, 70);

        lblMidOut.setBackground(new java.awt.Color(172, 50, 50));
        lblMidOut.setFont(new java.awt.Font("Segoe UI Semibold", 0, 27)); // NOI18N
        lblMidOut.setForeground(new java.awt.Color(219, 219, 219));
        lblMidOut.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMidOut.setText("<OUTPUT>");
        lblMidOut.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        PanelMid.add(lblMidOut);
        lblMidOut.setBounds(50, 100, 630, 220);

        btnMidAction.setBackground(new java.awt.Color(162, 40, 40));
        btnMidAction.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 27)); // NOI18N
        btnMidAction.setForeground(new java.awt.Color(191, 191, 191));
        btnMidAction.setText("ACTION");
        btnMidAction.setBorder(null);
        btnMidAction.setContentAreaFilled(false);
        btnMidAction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMidAction.setFocusPainted(false);
        btnMidAction.setOpaque(true);
        btnMidAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMidActionActionPerformed(evt);
            }
        });
        PanelMid.add(btnMidAction);
        btnMidAction.setBounds(0, 350, 740, 60);

        lblMidActionTitle.setBackground(new java.awt.Color(162, 40, 40));
        lblMidActionTitle.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 27)); // NOI18N
        lblMidActionTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblMidActionTitle.setText("ACTION TITLE");
        lblMidActionTitle.setBorder(null);
        lblMidActionTitle.setContentAreaFilled(false);
        lblMidActionTitle.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblMidActionTitle.setEnabled(false);
        lblMidActionTitle.setFocusPainted(false);
        lblMidActionTitle.setFocusable(false);
        lblMidActionTitle.setOpaque(true);
        PanelMid.add(lblMidActionTitle);
        lblMidActionTitle.setBounds(0, 0, 740, 60);

        PanelBody.add(PanelMid);
        PanelMid.setBounds(30, 30, 740, 410);

        getContentPane().add(PanelBody);
        PanelBody.setBounds(0, 130, 800, 480);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnMidActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMidActionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMidActionActionPerformed

    private void txtContactNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContactNoKeyReleased
        long input;
        try {
            input = Long.parseLong(txtContactNo.getText());
            if(txtContactNo.getText().length() > 11){
                txtContactNo.setText("");
            }
        } catch (NumberFormatException nfe) {
            txtContactNo.setText("");
        }
    }//GEN-LAST:event_txtContactNoKeyReleased

    private void txtBalanceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBalanceKeyReleased
        int input;
        try {
            input = Integer.parseInt(txtBalance.getText());
        } catch (NumberFormatException nfe) {
            txtBalance.setText("");
        }
    }//GEN-LAST:event_txtBalanceKeyReleased

    private void txtMidInputLoginKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMidInputLoginKeyReleased
        // TODO add your handling code here:
        int inputAccNo;
        try {
            inputAccNo = Integer.parseInt(txtMidInputLogin.getText());
            if(txtMidInputLogin.getText().length() >= 4){
                // validate accNo
                if(txtMidInputLogin.getText().length() == 4){
                    // ENTER VALIDATION
                    customer = myDB.getCustomerByAccountNumber(Integer.parseInt(txtMidInputLogin.getText()));
                    if(customer != null)
                        if(customer.getStatus().equals("ACTIVE"))
                            stateMenu();
                }
                txtMidInputLogin.setText("");
            }
        } catch (NumberFormatException nfe) {
            txtMidInputLogin.setText("");
        }
    }//GEN-LAST:event_txtMidInputLoginKeyReleased

    private void txtMidInputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMidInputKeyReleased
        // TODO add your handling code here:
        int input;
        try {
            input = Integer.parseInt(txtMidInput.getText());
        } catch (NumberFormatException nfe) {
            txtMidInput.setText("");
        }
    }//GEN-LAST:event_txtMidInputKeyReleased

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
            java.util.logging.Logger.getLogger(BANKINGPH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BANKINGPH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BANKINGPH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BANKINGPH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BANKINGPH().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelBody;
    private javax.swing.JPanel PanelMid;
    private javax.swing.JPanel PanelMidAccount;
    private javax.swing.JPanel PanelTop;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMidAction;
    private javax.swing.JButton btnSideAction;
    private javax.swing.JButton btnTopBalance;
    private javax.swing.JButton btnTopCloseAccount;
    private javax.swing.JButton btnTopDeposit;
    private javax.swing.JButton btnTopProfile;
    private javax.swing.JButton btnTopWithdraw;
    private com.toedter.calendar.JDateChooser dtBirthday;
    private javax.swing.JLabel lbl1;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lbl3;
    private javax.swing.JLabel lbl4;
    private javax.swing.JLabel lbl5;
    private javax.swing.JLabel lbl6;
    private javax.swing.JLabel lbl7;
    private javax.swing.JButton lblMidActionTitle;
    private javax.swing.JLabel lblMidOut;
    private javax.swing.JButton lblTopTime;
    private javax.swing.JButton lblTopTitle;
    private javax.swing.JButton lblTopX;
    private javax.swing.JTextField txtAccNo;
    private javax.swing.JTextArea txtAddress;
    private javax.swing.JScrollPane txtAddressSP;
    private javax.swing.JTextField txtBalance;
    private javax.swing.JTextField txtContactNo;
    private javax.swing.JTextField txtFName;
    private javax.swing.JTextField txtLName;
    private javax.swing.JTextField txtMidInput;
    private javax.swing.JPasswordField txtMidInputLogin;
    // End of variables declaration//GEN-END:variables
}
