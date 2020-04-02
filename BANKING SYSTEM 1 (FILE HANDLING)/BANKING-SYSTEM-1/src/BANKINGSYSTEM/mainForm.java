/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BANKINGSYSTEM;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author dzhon
 */
public class mainForm extends javax.swing.JFrame {

    /**
     * Creates new form mainForm
     */
    public mainForm() {
        initComponents();
        
        init();
    }

    // COLOR PROP
    final static Color MAINBGCOLOR = new Color(26,188,156);
    final static Color MAINFGCOLOR = new Color(219,219,219);
    final static Color HOVERBGCOLOR = new Color(46,204,113);
    // MENU DISPLAY
    public String currDate = "";
    public String currUser = "ADMIN";
    
    //BANKING OPERATIONS
    final static int MAINTAINING_BALANCE = 5000;
    final static int MINIMUM_AMOUNT = 100;
    final static double INTEREST_RATE = 0.05;
    public int amount = 0;
    public int interest = 0;
    public int newBalance = 0;
    public String[] flow = new String[]{"","",""};
    
    //DATA 
    public dataFile df = new dataFile("Accounts.txt");
    public Account account, tempAccount = null;
    public List<Account> accounts = new ArrayList<>();
    
    public boolean isLoggedIn = false;
    
    public formFields ff = new formFields();
    public appStates appS = new appStates();
    
    public void init(){
        
        ((JTextField)dtBDayAcc.getDateEditor().getUiComponent()).setForeground(new Color(26,188,156));
        btnBalInq.setBackground(MAINBGCOLOR);
        btnClose.setBackground(MAINBGCOLOR);
        btnDeposit.setBackground(MAINBGCOLOR);
        btnExit1.setBackground(MAINBGCOLOR);
        btnLogout.setBackground(MAINBGCOLOR);
        btnNewAcc.setBackground(MAINBGCOLOR);
        btnProfile.setBackground(MAINBGCOLOR);
        btnWithdraw.setBackground(MAINBGCOLOR);
        btnBalInq.setForeground(MAINFGCOLOR);
        btnClose.setForeground(MAINFGCOLOR);
        btnDeposit.setForeground(MAINFGCOLOR);
        btnExit1.setForeground(MAINFGCOLOR);
        btnLogout.setForeground(MAINFGCOLOR);
        btnNewAcc.setForeground(MAINFGCOLOR);
        btnProfile.setForeground(MAINFGCOLOR);
        btnWithdraw.setForeground(MAINFGCOLOR);
        
        panelLogin.setBackground(new Color(39,174,96));
        panelMenu.setBackground(new Color(39,174,96));
        panelLogin.setForeground(MAINFGCOLOR);
        panelMenu.setForeground(MAINFGCOLOR);
        panelAccount.setBackground(new Color(39,174,96));
        panelAccount.setForeground(MAINFGCOLOR);
        
        lblDialogCancel.setVisible(false);
        
        appS.loginState();
        setDate();
        
        try {
            accounts = df.loadAllAccounts();
            System.out.println(accounts);
        } catch (IOException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void verifyCredentials() {
        try {
            account = df.findByAccNo(account, Integer.parseInt(txtAccNo.getText()));
            if((account != null) && (account.getStatus().equals("ACTIVE"))){
                lblUSER.setText("Welcome, " + account.getLname() +", " + account.getFname());
                appS.menuState();
            }else{
                txtAccNo.setText("");
            }
        } catch (IOException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public class formFields {
        
        public void accFormFieldsAreEnabled(boolean st){
            txtBalanceAcc.setEnabled(st);
            txtFNameAcc.setEnabled(st);
            txtLNameAcc.setEnabled(st);
            txtAddressAcc.setEnabled(st);
            txtContactNoAcc.setEnabled(st);
            dtBDayAcc.setEnabled(st);
        }

        public void clearFormFields(){
            txtBalanceAcc.setText("");
            txtFNameAcc.setText("");
            txtLNameAcc.setText("");
            txtAddressAcc.setText("");
            txtContactNoAcc.setText("");
            ((JTextField)dtBDayAcc.getDateEditor().getUiComponent()).setText("");
        }

        public void setFormFieldsValue(){
            txtAccNoAcc.setText(Integer.toString(account.getAccountNo()));
            txtBalanceAcc.setText(Integer.toString(account.getBalance()));
            txtFNameAcc.setText(account.getFname());
            txtLNameAcc.setText(account.getLname());
            txtAddressAcc.setText(account.getAddress());
            ((JTextField)dtBDayAcc.getDateEditor().getUiComponent()).setText(account.getbDay());
            txtContactNoAcc.setText(Long.toString(account.getContactNo()));
        }

        public void verifyAccForm(){
            boolean formFilled = false;
            if(btnActionAcc.getText().equals("NEW ACCOUNT")){
                formFilled = !txtAccNoAcc.getText().isEmpty() && !txtBalanceAcc.getText().isEmpty() && !txtFNameAcc.getText().isEmpty() && !txtLNameAcc.getText().isEmpty() && !txtAddressAcc.getText().isEmpty() && !txtContactNoAcc.getText().isEmpty() && (dtBDayAcc.getDate() != null);
            
                if(!txtBalanceAcc.getText().isEmpty() && (Integer.valueOf(txtBalanceAcc.getText()) < 5000)){
                    txtBalanceAcc.setToolTipText("MINIMUM INITIAL DEPOSIT IS 5000");
                    txtBalanceAcc.setForeground(new Color(255,0,0));
                    formFilled = false;
                }else{
                    txtBalanceAcc.setForeground(new Color(26,188,156));
                }
                btnActionAcc.setEnabled(formFilled);
            }else if(btnActionAcc.getText().equals("UPDATE PROFILE")){
                formFilled=true;
            }
            
        }
        
        public void verifyDialogInput(){
            boolean validInput = false;
            int input = 0;
            
            if(txtDialogInput.isVisible()){
                if(!txtDialogInput.getText().isEmpty()){
                    input = Integer.valueOf(txtDialogInput.getText());
                    if(input < MINIMUM_AMOUNT){
                        txtDialogInput.setForeground(Color.red);
                        txtDialogInput.setToolTipText("MINIMUM AMOUNT IS 100");
                    }else{
                        txtDialogInput.setForeground(new Color(26,188,156));
                        validInput = true;
                        if(lblDialogMessage.getText().equals("WITHDRAW")){
                            validInput = false;
                            if(input >= account.getBalance() || (account.getBalance() - input) < MAINTAINING_BALANCE){
                                txtDialogInput.setForeground(Color.red);
                                txtDialogInput.setToolTipText("MAINTAINING BALANCE IS 5000");
                            }else{
                                txtDialogInput.setForeground(new Color(26,188,156));
                                validInput = true;
                            }
                        }
                    }      
                }else{
                    txtDialogInput.setForeground(new Color(26,188,156));
                    txtDialogInput.setToolTipText(null);
                }
            }else{
                validInput=true;
            }
            btnDialogAction.setEnabled(validInput);
            amount = input;
        }
    }
    
     
    public class appStates {
        public void loginState() {
            panelLogin.setVisible(true);
            panelMenu.setVisible(false);
            panelAccount.setVisible(false);
            txtAccNo.setText("");
            isLoggedIn = false;
            try {
                accounts = df.loadAllAccounts();
            } catch (IOException ex) {
                Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void menuState(){
            panelLogin.setVisible(false);
            panelMenu.setVisible(true);    
            panelAccount.setVisible(false);   
            isLoggedIn = true;
            try {
                accounts = df.loadAllAccounts();
            } catch (IOException ex) {
                Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void accountState(){
            txtBalanceAcc.setVisible(true);
            lblTITLE5.setVisible(true);
            lblTITLE4.setLocation(212,115);
            txtAccNoAcc.setLocation(370, 110);
            panelLogin.setVisible(false);
            panelMenu.setVisible(false);    
            panelAccount.setVisible(true);   
            ff.accFormFieldsAreEnabled(true);
            ff.clearFormFields();
            int accNo = 0;
            boolean isExisting = false;
            do {                    
                Random rand = new Random();
                accNo = Integer.valueOf(String.format("%04d", rand.nextInt(10000)));
                try {
                    tempAccount = df.findByAccNo(tempAccount, accNo);
                } catch (IOException ex) {
                    Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while (tempAccount != null);
            txtAccNoAcc.setText(""+accNo);
            btnActionAcc.setText("NEW ACCOUNT");
            btnActionAcc.setEnabled(true);
            try {
                accounts = df.loadAllAccounts();
            } catch (IOException ex) {
                Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        public void profileState(){
            panelLogin.setVisible(false);
            panelMenu.setVisible(false);    
            panelAccount.setVisible(true);   
            ff.accFormFieldsAreEnabled(false);
            ff.setFormFieldsValue();
            btnActionAcc.setVisible(false);
            txtBalanceAcc.setVisible(false);
            lblTITLE5.setVisible(false);
            lblTITLE4.setLocation(212,165);
            txtAccNoAcc.setLocation(370, 160);
        }
    }
    
         
    public void setDialogForm(String action){
        dialogForm.setVisible(false);
        txtDialogInput.setVisible(false);
        lblDialogMessage.setVisible(false);
        lblDialogMessage2.setVisible(false);
        lblDialogCancel.setVisible(false);
        btnDialogAction.setVisible(true);
        lblDialogCancel.setText("Cancel");
        txtDialogInput.setText("");
        lblDialogCancel.setBounds(360, 330, 70, 30);
        lblDialogMessage.setText(action);
        
        switch(action) {
            case "BALANCE": 
                dialogForm.setVisible(true);
                lblDialogMessage.setVisible(true);
                lblDialogMessage2.setVisible(true);
                break;
                
            case "CLOSE ACCOUNT":
                // SHOW COMPONENTS NEED IN CLOSING ACCOUNT
                dialogForm.setVisible(true);
                lblDialogCancel.setVisible(true);
                lblDialogMessage2.setVisible(true);
                lblDialogMessage2.setText("<html><font size=\"6\">Are you sure you want<br> to close your account?</html>");
                break;
                
            default:
                dialogForm.setVisible(true);
                txtDialogInput.setVisible(true);
                lblDialogMessage.setVisible(true);
                lblDialogCancel.setVisible(true);
                break;
        }
    }
    
    
    // BANKING METHODS
    private void getBalance(){
        lblDialogMessage2.setText("PHP "+ account.getBalance());
        btnDialogAction.setText("OK");
    }
    
     private void deposit() {
        newBalance = account.getBalance();
        newBalance += amount;
        flow[0] = "BALANCE: "+ Integer.toString(account.getBalance()) +" <br/>+ AMOUNT: "+ Integer.toString(amount) +" <br/>   = "+Integer.toString(newBalance);
        interest = (int) ((double) newBalance * INTEREST_RATE);
        flow[1] = "+ INTEREST: "+ interest +" <br/><strong>NEW BALANCE = ";
        newBalance += interest;
        flow[1] +=  Integer.toString(newBalance);
        
        String out = "<html><center>"+ flow[0] + "<br/>" + flow[1] +"</center></html>";
        lblDialogMessage2.setText(out);
        lblDialogMessage2.setVisible(true);
        txtDialogInput.setVisible(false);
        btnDialogAction.setVisible(false);
        lblDialogCancel.setText("OK");
        lblDialogCancel.setBounds(428, 367, 70, 30);
        
        account.setBalance(newBalance);
        try {
            df.updateAccount(account);
        } catch (IOException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   private void withdraw() {
        amount = Integer.valueOf(txtDialogInput.getText());
        
        newBalance = account.getBalance() - amount;
        flow[0] = "BALANCE: "+ Integer.toString(account.getBalance()) +" <br/>- AMOUNT: "+ Integer.toString(amount) +" <br/>  NEW BALANCE = "+Integer.toString(newBalance);
        
        String out = "<html><center>"+ flow[0] +"</center></html>";
        lblDialogMessage2.setText(out);
        lblDialogMessage2.setVisible(true);
        txtDialogInput.setVisible(false);
        btnDialogAction.setVisible(false);
        lblDialogCancel.setText("OK");
        lblDialogCancel.setBounds(428, 367, 70, 30);
        
        account.setBalance(newBalance);
        
        try {
            df.updateAccount(account);
        } catch (IOException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void closeAccount() {
        amount = 0;
        flow[0] = "PREVIOUS = "+ account.getBalance() +"<br/>NEW BALANCE = 0<br/> <font size=\"6\">Your account is now Closed!";
        String out = "<html><center>"+ flow[0] +"</center></html>";
        lblDialogMessage2.setText(out);
        account.setBalance(0);
        account.setStatus("CLOSED");
        btnDialogAction.setText("BACK TO LOGIN");
        lblDialogCancel.setVisible(false);
        
        try {
            df.updateAccount(account);
        } catch (IOException ex) {
            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public void setDate(){
        Timer timer = new Timer(1000, new setDateActionListener());
        timer.start();
    }
    class setDateActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            currDate = new SimpleDateFormat("dd-MM-yyyy  |  HH:mm:ss").format(Calendar.getInstance().getTime());      
            lblTIME.setText(currDate);
            
            ff.verifyAccForm();
            ff.verifyDialogInput();
            
                
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogForm = new javax.swing.JDialog();
        panelDialog = new javax.swing.JPanel();
        lblDialogMessage = new javax.swing.JLabel();
        btnDialogAction = new javax.swing.JButton();
        lblDialogTitle = new javax.swing.JLabel();
        txtDialogInput = new javax.swing.JTextField();
        lblDialogMessage2 = new javax.swing.JLabel();
        lblDialogCancel = new javax.swing.JLabel();
        lblTITLE = new javax.swing.JLabel();
        panelLogin = new javax.swing.JPanel();
        btnNewAcc = new javax.swing.JButton();
        txtAccNo = new javax.swing.JPasswordField();
        lblTITLE1 = new javax.swing.JLabel();
        btnExit1 = new javax.swing.JButton();
        panelMenu = new javax.swing.JPanel();
        btnBalInq = new javax.swing.JButton();
        btnDeposit = new javax.swing.JButton();
        btnProfile = new javax.swing.JButton();
        btnWithdraw = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        lblUSER = new javax.swing.JLabel();
        lblTIME = new javax.swing.JLabel();
        lblUSER1 = new javax.swing.JLabel();
        lblUSER2 = new javax.swing.JLabel();
        panelAccount = new javax.swing.JPanel();
        btnActionAcc = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        txtContactNoAcc = new javax.swing.JTextField();
        txtAccNoAcc = new javax.swing.JTextField();
        txtBalanceAcc = new javax.swing.JTextField();
        txtFNameAcc = new javax.swing.JTextField();
        txtAddressAcc = new javax.swing.JTextArea();
        lblTITLE3 = new javax.swing.JLabel();
        lblTITLE4 = new javax.swing.JLabel();
        lblTITLE5 = new javax.swing.JLabel();
        lblTITLE6 = new javax.swing.JLabel();
        lblTITLE7 = new javax.swing.JLabel();
        lblTITLE8 = new javax.swing.JLabel();
        dtBDayAcc = new com.toedter.calendar.JDateChooser();
        lblTITLE9 = new javax.swing.JLabel();
        txtLNameAcc = new javax.swing.JTextField();

        dialogForm.setTitle("FORM");
        dialogForm.setAlwaysOnTop(true);
        dialogForm.setBackground(new java.awt.Color(39, 174, 96));
        dialogForm.setMinimumSize(new java.awt.Dimension(500, 400));
        dialogForm.setName("dialogForm"); // NOI18N
        dialogForm.setUndecorated(true);
        dialogForm.setResizable(false);
        dialogForm.setSize(new java.awt.Dimension(500, 400));
        dialogForm.setType(java.awt.Window.Type.POPUP);
        dialogForm.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                dialogFormWindowOpened(evt);
            }
        });
        dialogForm.getContentPane().setLayout(new java.awt.CardLayout());

        panelDialog.setBackground(new java.awt.Color(19, 179, 211));
        panelDialog.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelDialog.setMinimumSize(new java.awt.Dimension(400, 300));
        panelDialog.setPreferredSize(new java.awt.Dimension(400, 300));
        panelDialog.setLayout(null);

        lblDialogMessage.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 24)); // NOI18N
        lblDialogMessage.setForeground(new java.awt.Color(255, 255, 255));
        lblDialogMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDialogMessage.setText("<MESSAGE>");
        lblDialogMessage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelDialog.add(lblDialogMessage);
        lblDialogMessage.setBounds(0, 140, 500, 32);

        btnDialogAction.setBackground(new java.awt.Color(26, 188, 156));
        btnDialogAction.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        btnDialogAction.setForeground(new java.awt.Color(219, 219, 219));
        btnDialogAction.setText("NEW ACCOUNT");
        btnDialogAction.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDialogAction.setContentAreaFilled(false);
        btnDialogAction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDialogAction.setFocusPainted(false);
        btnDialogAction.setOpaque(true);
        btnDialogAction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDialogActionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDialogActionMouseExited(evt);
            }
        });
        btnDialogAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogActionActionPerformed(evt);
            }
        });
        panelDialog.add(btnDialogAction);
        btnDialogAction.setBounds(160, 310, 190, 60);

        lblDialogTitle.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 24)); // NOI18N
        lblDialogTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblDialogTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDialogTitle.setText("BANKING SYSTEM");
        lblDialogTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelDialog.add(lblDialogTitle);
        lblDialogTitle.setBounds(0, 30, 500, 32);

        txtDialogInput.setBackground(new java.awt.Color(219, 219, 219));
        txtDialogInput.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtDialogInput.setForeground(new java.awt.Color(26, 188, 156));
        txtDialogInput.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDialogInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDialogInputKeyReleased(evt);
            }
        });
        panelDialog.add(txtDialogInput);
        txtDialogInput.setBounds(90, 190, 330, 35);

        lblDialogMessage2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDialogMessage2.setForeground(new java.awt.Color(255, 255, 255));
        lblDialogMessage2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDialogMessage2.setText("<MESSAGE>");
        lblDialogMessage2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblDialogMessage2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelDialog.add(lblDialogMessage2);
        lblDialogMessage2.setBounds(90, 191, 330, 160);

        lblDialogCancel.setBackground(new java.awt.Color(19, 179, 211));
        lblDialogCancel.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 16)); // NOI18N
        lblDialogCancel.setForeground(new java.awt.Color(255, 255, 255));
        lblDialogCancel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDialogCancel.setText("CANCEL");
        lblDialogCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblDialogCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblDialogCancel.setOpaque(true);
        lblDialogCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDialogCancelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblDialogCancelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblDialogCancelMouseExited(evt);
            }
        });
        panelDialog.add(lblDialogCancel);
        lblDialogCancel.setBounds(360, 330, 70, 30);

        dialogForm.getContentPane().add(panelDialog, "card2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("bankingSystem");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(null);

        lblTITLE.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 24)); // NOI18N
        lblTITLE.setForeground(new java.awt.Color(219, 219, 219));
        lblTITLE.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTITLE.setText("BANKING SYSTEM");
        lblTITLE.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblTITLE);
        lblTITLE.setBounds(320, 35, 200, 32);

        panelLogin.setBackground(new java.awt.Color(39, 174, 96));
        panelLogin.setForeground(new java.awt.Color(255, 255, 255));
        panelLogin.setFont(new java.awt.Font("Yu Gothic UI", 0, 16)); // NOI18N
        panelLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNewAcc.setBackground(new java.awt.Color(26, 188, 156));
        btnNewAcc.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        btnNewAcc.setForeground(new java.awt.Color(219, 219, 219));
        btnNewAcc.setText("NEW ACCOUNT");
        btnNewAcc.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnNewAcc.setContentAreaFilled(false);
        btnNewAcc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNewAcc.setFocusPainted(false);
        btnNewAcc.setOpaque(true);
        btnNewAcc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNewAccMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNewAccMouseExited(evt);
            }
        });
        btnNewAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewAccActionPerformed(evt);
            }
        });
        panelLogin.add(btnNewAcc, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 460, 190, 40));

        txtAccNo.setBackground(new java.awt.Color(219, 219, 219));
        txtAccNo.setColumns(4);
        txtAccNo.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 48)); // NOI18N
        txtAccNo.setForeground(new java.awt.Color(26, 188, 156));
        txtAccNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccNoActionPerformed(evt);
            }
        });
        txtAccNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAccNoKeyReleased(evt);
            }
        });
        panelLogin.add(txtAccNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 220, 310, -1));

        lblTITLE1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 24)); // NOI18N
        lblTITLE1.setForeground(new java.awt.Color(219, 219, 219));
        lblTITLE1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTITLE1.setText("INPUT 4-DIGIT ACCOUNT NUMBER");
        lblTITLE1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelLogin.add(lblTITLE1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, -1, -1));

        btnExit1.setBackground(new java.awt.Color(26, 188, 156));
        btnExit1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        btnExit1.setForeground(new java.awt.Color(219, 219, 219));
        btnExit1.setText("EXIT");
        btnExit1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnExit1.setContentAreaFilled(false);
        btnExit1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit1.setFocusPainted(false);
        btnExit1.setOpaque(true);
        btnExit1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExit1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExit1MouseExited(evt);
            }
        });
        btnExit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExit1ActionPerformed(evt);
            }
        });
        panelLogin.add(btnExit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 570, 110, 30));

        getContentPane().add(panelLogin);
        panelLogin.setBounds(0, 0, 800, 600);

        panelMenu.setBackground(new java.awt.Color(39, 174, 96));
        panelMenu.setForeground(new java.awt.Color(255, 255, 255));
        panelMenu.setFont(new java.awt.Font("Yu Gothic UI", 0, 16)); // NOI18N
        panelMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBalInq.setBackground(new java.awt.Color(26, 188, 156));
        btnBalInq.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        btnBalInq.setForeground(new java.awt.Color(219, 219, 219));
        btnBalInq.setText("BALANCE");
        btnBalInq.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBalInq.setContentAreaFilled(false);
        btnBalInq.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBalInq.setFocusPainted(false);
        btnBalInq.setOpaque(true);
        btnBalInq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBalInqMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBalInqMouseExited(evt);
            }
        });
        btnBalInq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBalInqActionPerformed(evt);
            }
        });
        panelMenu.add(btnBalInq, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 270, 200, 85));

        btnDeposit.setBackground(new java.awt.Color(26, 188, 156));
        btnDeposit.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        btnDeposit.setForeground(new java.awt.Color(219, 219, 219));
        btnDeposit.setText("DEPOSIT");
        btnDeposit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDeposit.setContentAreaFilled(false);
        btnDeposit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeposit.setFocusPainted(false);
        btnDeposit.setOpaque(true);
        btnDeposit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDepositMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDepositMouseExited(evt);
            }
        });
        btnDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositActionPerformed(evt);
            }
        });
        panelMenu.add(btnDeposit, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 270, 200, 85));

        btnProfile.setBackground(new java.awt.Color(26, 188, 156));
        btnProfile.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        btnProfile.setForeground(new java.awt.Color(219, 219, 219));
        btnProfile.setText("PROFILE");
        btnProfile.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnProfile.setContentAreaFilled(false);
        btnProfile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProfile.setFocusPainted(false);
        btnProfile.setOpaque(true);
        btnProfile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnProfileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnProfileMouseExited(evt);
            }
        });
        btnProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProfileActionPerformed(evt);
            }
        });
        panelMenu.add(btnProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 430, 200, 85));

        btnWithdraw.setBackground(new java.awt.Color(26, 188, 156));
        btnWithdraw.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        btnWithdraw.setForeground(new java.awt.Color(219, 219, 219));
        btnWithdraw.setText("WITHDRAW");
        btnWithdraw.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnWithdraw.setContentAreaFilled(false);
        btnWithdraw.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnWithdraw.setFocusPainted(false);
        btnWithdraw.setOpaque(true);
        btnWithdraw.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnWithdrawMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnWithdrawMouseExited(evt);
            }
        });
        btnWithdraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWithdrawActionPerformed(evt);
            }
        });
        panelMenu.add(btnWithdraw, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 430, 200, 85));

        btnClose.setBackground(new java.awt.Color(26, 188, 156));
        btnClose.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        btnClose.setForeground(new java.awt.Color(219, 219, 219));
        btnClose.setText("CLOSE ACCOUNT");
        btnClose.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClose.setContentAreaFilled(false);
        btnClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClose.setFocusPainted(false);
        btnClose.setOpaque(true);
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCloseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCloseMouseExited(evt);
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        panelMenu.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 430, 200, 85));

        btnLogout.setBackground(new java.awt.Color(26, 188, 156));
        btnLogout.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(219, 219, 219));
        btnLogout.setText("LOGOUT");
        btnLogout.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnLogout.setContentAreaFilled(false);
        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.setFocusPainted(false);
        btnLogout.setOpaque(true);
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogoutMouseExited(evt);
            }
        });
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        panelMenu.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 560, 140, 40));

        lblUSER.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        lblUSER.setForeground(new java.awt.Color(255, 255, 255));
        lblUSER.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUSER.setText("WELCOME, BLABLA");
        panelMenu.add(lblUSER, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        lblTIME.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        lblTIME.setForeground(new java.awt.Color(255, 255, 255));
        lblTIME.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTIME.setText("TIME");
        panelMenu.add(lblTIME, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, -1, -1));

        lblUSER1.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 18)); // NOI18N
        lblUSER1.setForeground(new java.awt.Color(255, 255, 255));
        lblUSER1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUSER1.setText("DATE          |  TIME");
        panelMenu.add(lblUSER1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        lblUSER2.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 18)); // NOI18N
        lblUSER2.setForeground(new java.awt.Color(255, 255, 255));
        lblUSER2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUSER2.setText("WELCOME");
        panelMenu.add(lblUSER2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        getContentPane().add(panelMenu);
        panelMenu.setBounds(0, 0, 800, 600);

        panelAccount.setBackground(new java.awt.Color(39, 174, 96));
        panelAccount.setForeground(new java.awt.Color(255, 255, 255));
        panelAccount.setFont(new java.awt.Font("Yu Gothic UI", 0, 16)); // NOI18N
        panelAccount.setLayout(null);

        btnActionAcc.setBackground(new java.awt.Color(26, 188, 156));
        btnActionAcc.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        btnActionAcc.setForeground(new java.awt.Color(219, 219, 219));
        btnActionAcc.setText("NEW ACCOUNT");
        btnActionAcc.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnActionAcc.setContentAreaFilled(false);
        btnActionAcc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActionAcc.setFocusPainted(false);
        btnActionAcc.setOpaque(true);
        btnActionAcc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnActionAccMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnActionAccMouseExited(evt);
            }
        });
        btnActionAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActionAccActionPerformed(evt);
            }
        });
        panelAccount.add(btnActionAcc);
        btnActionAcc.setBounds(610, 540, 190, 60);

        btnBack.setBackground(new java.awt.Color(26, 188, 156));
        btnBack.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        btnBack.setForeground(new java.awt.Color(219, 219, 219));
        btnBack.setText("BACK");
        btnBack.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBack.setFocusPainted(false);
        btnBack.setOpaque(true);
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBackMouseExited(evt);
            }
        });
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        panelAccount.add(btnBack);
        btnBack.setBounds(0, 570, 110, 30);

        txtContactNoAcc.setBackground(new java.awt.Color(219, 219, 219));
        txtContactNoAcc.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtContactNoAcc.setForeground(new java.awt.Color(26, 188, 156));
        txtContactNoAcc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtContactNoAccKeyReleased(evt);
            }
        });
        panelAccount.add(txtContactNoAcc);
        txtContactNoAcc.setBounds(370, 310, 219, 35);

        txtAccNoAcc.setBackground(new java.awt.Color(219, 219, 219));
        txtAccNoAcc.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtAccNoAcc.setForeground(new java.awt.Color(26, 188, 156));
        txtAccNoAcc.setEnabled(false);
        panelAccount.add(txtAccNoAcc);
        txtAccNoAcc.setBounds(370, 110, 219, 35);

        txtBalanceAcc.setBackground(new java.awt.Color(219, 219, 219));
        txtBalanceAcc.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtBalanceAcc.setForeground(new java.awt.Color(26, 188, 156));
        txtBalanceAcc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBalanceAccKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBalanceAccKeyReleased(evt);
            }
        });
        panelAccount.add(txtBalanceAcc);
        txtBalanceAcc.setBounds(370, 160, 219, 35);

        txtFNameAcc.setBackground(new java.awt.Color(219, 219, 219));
        txtFNameAcc.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtFNameAcc.setForeground(new java.awt.Color(26, 188, 156));
        panelAccount.add(txtFNameAcc);
        txtFNameAcc.setBounds(370, 210, 219, 35);

        txtAddressAcc.setBackground(new java.awt.Color(219, 219, 219));
        txtAddressAcc.setColumns(20);
        txtAddressAcc.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtAddressAcc.setForeground(new java.awt.Color(26, 188, 156));
        txtAddressAcc.setLineWrap(true);
        txtAddressAcc.setRows(5);
        txtAddressAcc.setWrapStyleWord(true);
        panelAccount.add(txtAddressAcc);
        txtAddressAcc.setBounds(370, 410, 220, 170);

        lblTITLE3.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        lblTITLE3.setForeground(new java.awt.Color(219, 219, 219));
        lblTITLE3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTITLE3.setText("FNAME");
        lblTITLE3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelAccount.add(lblTITLE3);
        lblTITLE3.setBounds(275, 215, 61, 25);

        lblTITLE4.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        lblTITLE4.setForeground(new java.awt.Color(219, 219, 219));
        lblTITLE4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTITLE4.setText("ACCOUNT_NO");
        lblTITLE4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelAccount.add(lblTITLE4);
        lblTITLE4.setBounds(212, 115, 120, 25);

        lblTITLE5.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        lblTITLE5.setForeground(new java.awt.Color(219, 219, 219));
        lblTITLE5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTITLE5.setText("INITIAL DEPOSIT");
        lblTITLE5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelAccount.add(lblTITLE5);
        lblTITLE5.setBounds(198, 165, 137, 25);

        lblTITLE6.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        lblTITLE6.setForeground(new java.awt.Color(219, 219, 219));
        lblTITLE6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTITLE6.setText("BIRTHDAY");
        lblTITLE6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelAccount.add(lblTITLE6);
        lblTITLE6.setBounds(253, 360, 85, 25);

        lblTITLE7.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        lblTITLE7.setForeground(new java.awt.Color(219, 219, 219));
        lblTITLE7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTITLE7.setText("LNAME");
        lblTITLE7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelAccount.add(lblTITLE7);
        lblTITLE7.setBounds(275, 265, 61, 25);

        lblTITLE8.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        lblTITLE8.setForeground(new java.awt.Color(219, 219, 219));
        lblTITLE8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTITLE8.setText("ADDRESS");
        lblTITLE8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelAccount.add(lblTITLE8);
        lblTITLE8.setBounds(262, 420, 78, 25);

        dtBDayAcc.setBackground(new java.awt.Color(219, 219, 219));
        dtBDayAcc.setForeground(new java.awt.Color(26, 188, 156));
        dtBDayAcc.setDateFormatString("yyyy-MM-dd");
        dtBDayAcc.setFont(new java.awt.Font("Yu Gothic UI", 0, 16)); // NOI18N
        dtBDayAcc.setName("dtBday"); // NOI18N
        panelAccount.add(dtBDayAcc);
        dtBDayAcc.setBounds(370, 360, 220, 35);

        lblTITLE9.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        lblTITLE9.setForeground(new java.awt.Color(219, 219, 219));
        lblTITLE9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTITLE9.setText("CONTACT NO");
        lblTITLE9.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panelAccount.add(lblTITLE9);
        lblTITLE9.setBounds(222, 315, 115, 25);

        txtLNameAcc.setBackground(new java.awt.Color(219, 219, 219));
        txtLNameAcc.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtLNameAcc.setForeground(new java.awt.Color(26, 188, 156));
        panelAccount.add(txtLNameAcc);
        txtLNameAcc.setBounds(370, 260, 219, 35);

        getContentPane().add(panelAccount);
        panelAccount.setBounds(0, 0, 800, 600);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewAccMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewAccMouseEntered
        // TODO add your handling code here:
        if(btnNewAcc.isEnabled()){
            btnNewAcc.setBackground(HOVERBGCOLOR);
            btnNewAcc.setForeground(Color.white);
        }
    }//GEN-LAST:event_btnNewAccMouseEntered

    private void btnNewAccMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewAccMouseExited
        // TODO add your handling code here:
        btnNewAcc.setBackground(MAINBGCOLOR);
        btnNewAcc.setForeground(MAINFGCOLOR);
    }//GEN-LAST:event_btnNewAccMouseExited

    private void btnBalInqMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBalInqMouseEntered
        // TODO add your handling code here:
        if(btnBalInq.isEnabled()){
            btnBalInq.setBackground(HOVERBGCOLOR);
            btnBalInq.setForeground(Color.white);
        }
    }//GEN-LAST:event_btnBalInqMouseEntered

    private void btnBalInqMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBalInqMouseExited
        // TODO add your handling code here:
        btnBalInq.setBackground(MAINBGCOLOR);
        btnBalInq.setForeground(MAINFGCOLOR);
    }//GEN-LAST:event_btnBalInqMouseExited

    private void btnDepositMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDepositMouseEntered
        // TODO add your handling code here:
        if(btnDeposit.isEnabled()){
            btnDeposit.setBackground(HOVERBGCOLOR);
            btnDeposit.setForeground(Color.white);
        }
    }//GEN-LAST:event_btnDepositMouseEntered

    private void btnDepositMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDepositMouseExited
        // TODO add your handling code here:
        btnDeposit.setBackground(MAINBGCOLOR);
        btnDeposit.setForeground(MAINFGCOLOR);
    }//GEN-LAST:event_btnDepositMouseExited

    private void btnProfileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProfileMouseEntered
        // TODO add your handling code here:
        if(btnProfile.isEnabled()){
            btnProfile.setBackground(HOVERBGCOLOR);
            btnProfile.setForeground(Color.white);
        }
    }//GEN-LAST:event_btnProfileMouseEntered

    private void btnProfileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProfileMouseExited
        // TODO add your handling code here:
        btnProfile.setBackground(MAINBGCOLOR);
        btnProfile.setForeground(MAINFGCOLOR);
    }//GEN-LAST:event_btnProfileMouseExited

    private void btnWithdrawMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnWithdrawMouseEntered
        // TODO add your handling code here:
        if(btnWithdraw.isEnabled()){
            btnWithdraw.setBackground(HOVERBGCOLOR);
            btnWithdraw.setForeground(Color.white);
        }
    }//GEN-LAST:event_btnWithdrawMouseEntered

    private void btnWithdrawMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnWithdrawMouseExited
        // TODO add your handling code here:
        btnWithdraw.setBackground(MAINBGCOLOR);
        btnWithdraw.setForeground(MAINFGCOLOR);
    }//GEN-LAST:event_btnWithdrawMouseExited

    private void btnCloseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseEntered
        // TODO add your handling code here:
        if(btnClose.isEnabled()){
            btnClose.setBackground(HOVERBGCOLOR);
            btnClose.setForeground(Color.white);
        }
    }//GEN-LAST:event_btnCloseMouseEntered

    private void btnCloseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseExited
        // TODO add your handling code here:   
        btnClose.setBackground(MAINBGCOLOR);
        btnClose.setForeground(MAINFGCOLOR);
    }//GEN-LAST:event_btnCloseMouseExited

    private void btnLogoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseEntered
        // TODO add your handling code here:
            btnLogout.setBackground(HOVERBGCOLOR);
            btnLogout.setForeground(Color.white);
    }//GEN-LAST:event_btnLogoutMouseEntered

    private void btnLogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseExited
        // TODO add your handling code here:
        btnLogout.setBackground(MAINBGCOLOR);
        btnLogout.setForeground(MAINFGCOLOR);
    }//GEN-LAST:event_btnLogoutMouseExited

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        int Confirmation = JOptionPane.showConfirmDialog(panelMenu, "ARE YOU SURE YOU WANT TO LOGOUT?", "CONFIRMATION", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if(Confirmation == 0)
            appS.loginState();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnExit1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExit1MouseEntered
        // TODO add your handling code here:
        btnExit1.setBackground(HOVERBGCOLOR);
        btnExit1.setForeground(Color.white);
    }//GEN-LAST:event_btnExit1MouseEntered

    private void btnExit1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExit1MouseExited
        // TODO add your handling code here:
        btnExit1.setBackground(MAINBGCOLOR);
        btnExit1.setForeground(MAINFGCOLOR);
    }//GEN-LAST:event_btnExit1MouseExited

    private void btnExit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExit1ActionPerformed
        // TODO add your handling code here:
        int Confirmation = JOptionPane.showConfirmDialog(panelMenu, "ARE YOU SURE YOU WANT TO EXIT THE PROGRAM?", "CONFIRMATION", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if(Confirmation == 0)
            System.exit(0);
    }//GEN-LAST:event_btnExit1ActionPerformed

    private void btnActionAccMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActionAccMouseEntered
        // TODO add your handling code here:
        if(btnActionAcc.isEnabled()){
            btnActionAcc.setBackground(HOVERBGCOLOR);
            btnActionAcc.setForeground(Color.white);
            
        }
    }//GEN-LAST:event_btnActionAccMouseEntered

    private void btnActionAccMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActionAccMouseExited
        // TODO add your handling code here:
        btnActionAcc.setBackground(MAINBGCOLOR);
        btnActionAcc.setForeground(MAINFGCOLOR);
    }//GEN-LAST:event_btnActionAccMouseExited

    private void btnBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseEntered
        // TODO add your handling code here:
        btnBack.setBackground(HOVERBGCOLOR);
        btnBack.setForeground(Color.white);
    }//GEN-LAST:event_btnBackMouseEntered

    private void btnBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseExited
        // TODO add your handling code here:
        btnBack.setBackground(MAINBGCOLOR);
        btnBack.setForeground(MAINFGCOLOR);
    }//GEN-LAST:event_btnBackMouseExited

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        if(!isLoggedIn)
            appS.loginState();
        else
            appS.menuState();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnNewAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewAccActionPerformed
        // TODO add your handling code here:
        txtAccNo.setText("");
        appS.accountState();
    }//GEN-LAST:event_btnNewAccActionPerformed

    private void txtAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccNoActionPerformed
        // TODO add your handling code here:
        if(txtAccNo.getText().length() == 4){
            verifyCredentials();
        }
    }//GEN-LAST:event_txtAccNoActionPerformed

    private void btnActionAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActionAccActionPerformed
        // TODO add your handling code here:
        int ID = accounts.size() + 1;
        int accNo = Integer.valueOf(txtAccNoAcc.getText());
        int balance = Integer.valueOf(txtBalanceAcc.getText());
        String fName = txtFNameAcc.getText();
        String lName = txtLNameAcc.getText();
        String address = txtAddressAcc.getText();
        String bDay = ((JTextField)dtBDayAcc.getDateEditor().getUiComponent()).getText();
        long contactNo = Long.parseLong(txtContactNoAcc.getText());
        String status = "ACTIVE";
            
        account = new Account(ID, accNo, balance, fName, lName, address, bDay, contactNo, status);
        df.saveAccount(account);
        appS.loginState();
    }//GEN-LAST:event_btnActionAccActionPerformed

    private void btnDialogActionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDialogActionMouseEntered
        // TODO add your handling code here:
        if(btnDialogAction.isEnabled()){
            btnDialogAction.setBackground(HOVERBGCOLOR);
            btnDialogAction.setForeground(Color.white);
        }
        
    }//GEN-LAST:event_btnDialogActionMouseEntered

    private void btnDialogActionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDialogActionMouseExited
        // TODO add your handling code here:
        btnDialogAction.setBackground(MAINBGCOLOR);
        btnDialogAction.setForeground(MAINFGCOLOR);
        
    }//GEN-LAST:event_btnDialogActionMouseExited

    private void btnDialogActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogActionActionPerformed
        // TODO add your handling code here:
        String action = btnDialogAction.getText();
        switch(action){
            case "OK":  dialogForm.setVisible(false); break;
            case "DEPOSIT": deposit(); break;
            case "WITHDRAW": withdraw(); break;
            case "YES": closeAccount(); break;
            case "BACK TO LOGIN": dialogForm.setVisible(false); appS.loginState(); break;
        }
            
        
    }//GEN-LAST:event_btnDialogActionActionPerformed

    private void btnBalInqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBalInqActionPerformed
        // TODO add your handling code here:
        setDialogForm("BALANCE");
        getBalance();
    }//GEN-LAST:event_btnBalInqActionPerformed

    private void btnDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositActionPerformed
        // TODO add your handling code here:
        setDialogForm("DEPOSIT");
        
        btnDialogAction.setText("DEPOSIT");
    }//GEN-LAST:event_btnDepositActionPerformed

    private void btnWithdrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWithdrawActionPerformed
        // TODO add your handling code here:
        setDialogForm("WITHDRAW");
        btnDialogAction.setText("WITHDRAW");
    }//GEN-LAST:event_btnWithdrawActionPerformed

    private void btnProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfileActionPerformed
        // TODO add your handling code here:
        appS.profileState();
        
    }//GEN-LAST:event_btnProfileActionPerformed

    private void lblDialogCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDialogCancelMouseEntered
        // TODO add your handling code here:
        lblDialogCancel.setBackground(Color.WHITE);
        lblDialogCancel.setForeground(new Color(19,179,211));
    }//GEN-LAST:event_lblDialogCancelMouseEntered

    private void lblDialogCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDialogCancelMouseExited
        // TODO add your handling code here:
        lblDialogCancel.setForeground(Color.WHITE);
        lblDialogCancel.setBackground(new Color(19,179,211));
    }//GEN-LAST:event_lblDialogCancelMouseExited

    private void lblDialogCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDialogCancelMouseClicked
        // TODO add your handling code here:
        dialogForm.setVisible(false);
    }//GEN-LAST:event_lblDialogCancelMouseClicked

    private void txtAccNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAccNoKeyReleased
        // TODO add your handling code here:
        int inputAccNo;
        try {
            inputAccNo = Integer.parseInt(txtAccNo.getText());
            if(txtAccNo.getText().length() >= 4){
                // validate accNo
                if(txtAccNo.getText().length() == 4){
                    verifyCredentials();
                }
            }  
        } catch (NumberFormatException nfe) {
            txtAccNo.setText("");
        }
        
    }//GEN-LAST:event_txtAccNoKeyReleased

    private void txtContactNoAccKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContactNoAccKeyReleased
        // TODO add your handling code here:
        long inputContactNo;
        try {
            inputContactNo = (long) Long.parseLong(txtContactNoAcc.getText());
            if(txtContactNoAcc.getText().length() >= 12){
                txtContactNoAcc.setText("");
            }  
        } catch (NumberFormatException nfe) {
            txtContactNoAcc.setText("");
        }
    }//GEN-LAST:event_txtContactNoAccKeyReleased

    private void dialogFormWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dialogFormWindowOpened
        // TODO add your handling code here:
        dialogForm.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component) evt.getSource()));
    }//GEN-LAST:event_dialogFormWindowOpened

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        setDialogForm("CLOSE ACCOUNT");
        btnDialogAction.setText("YES");
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtDialogInputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDialogInputKeyReleased
        // TODO add your handling code here:
        int inputDialogTxt;
        try {
            inputDialogTxt = Integer.parseInt(txtDialogInput.getText());
        } catch (NumberFormatException nfe) {
            txtDialogInput.setText("");
        }
    }//GEN-LAST:event_txtDialogInputKeyReleased

    private void txtBalanceAccKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBalanceAccKeyReleased
        // TODO add your handling code here:

        int inputBalance;
        try {
            inputBalance = Integer.parseInt(txtBalanceAcc.getText());
        } catch (NumberFormatException nfe) {
            txtBalanceAcc.setText("");
        }
    }//GEN-LAST:event_txtBalanceAccKeyReleased

    private void txtBalanceAccKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBalanceAccKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBalanceAccKeyPressed

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
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActionAcc;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBalInq;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDeposit;
    private javax.swing.JButton btnDialogAction;
    private javax.swing.JButton btnExit1;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnNewAcc;
    private javax.swing.JButton btnProfile;
    private javax.swing.JButton btnWithdraw;
    private javax.swing.JDialog dialogForm;
    private com.toedter.calendar.JDateChooser dtBDayAcc;
    private javax.swing.JLabel lblDialogCancel;
    private javax.swing.JLabel lblDialogMessage;
    private javax.swing.JLabel lblDialogMessage2;
    private javax.swing.JLabel lblDialogTitle;
    private javax.swing.JLabel lblTIME;
    private javax.swing.JLabel lblTITLE;
    private javax.swing.JLabel lblTITLE1;
    private javax.swing.JLabel lblTITLE3;
    private javax.swing.JLabel lblTITLE4;
    private javax.swing.JLabel lblTITLE5;
    private javax.swing.JLabel lblTITLE6;
    private javax.swing.JLabel lblTITLE7;
    private javax.swing.JLabel lblTITLE8;
    private javax.swing.JLabel lblTITLE9;
    private javax.swing.JLabel lblUSER;
    private javax.swing.JLabel lblUSER1;
    private javax.swing.JLabel lblUSER2;
    private javax.swing.JPanel panelAccount;
    private javax.swing.JPanel panelDialog;
    private javax.swing.JPanel panelLogin;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JPasswordField txtAccNo;
    private javax.swing.JTextField txtAccNoAcc;
    private javax.swing.JTextArea txtAddressAcc;
    private javax.swing.JTextField txtBalanceAcc;
    private javax.swing.JTextField txtContactNoAcc;
    private javax.swing.JTextField txtDialogInput;
    private javax.swing.JTextField txtFNameAcc;
    private javax.swing.JTextField txtLNameAcc;
    // End of variables declaration//GEN-END:variables
}
