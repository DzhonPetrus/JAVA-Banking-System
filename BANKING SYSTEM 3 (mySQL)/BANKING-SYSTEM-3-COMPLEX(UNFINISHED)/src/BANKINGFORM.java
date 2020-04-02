
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalButtonUI;
import sun.security.util.ArrayUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dzhon
 */
public class BANKINGFORM extends javax.swing.JFrame {

    /**
     * Creates new form BANKINGFORM
     */
    public BANKINGFORM() {
        initComponents();
        
        
        ButtonsInit();
        Questions = new String[]{"First Name", "Last Name", "Initial Deposit", "Contact No", "Birthday", "Address"};
        stateLogin();
        btnBotActionLeft.setEnabled(false);
        btnBotActionLeft.setFocusable(false);
        
    }

    
   
    //BANKING OPERATIONS
    final static int MAINTAINING_BALANCE = 5000;
    final static int MINIMUM_AMOUNT = 100;
    final static double INTEREST_RATE = 0.05;
    public int amount = 0;
    public int interest = 0;
    public int newBalance = 0;
    
    // COLOR SCHEME HOVER
    private final Color HOVEROUTFGCOLOR=new Color(191,191,191);
    private final Color HOVEROUTBGCOLOR=new Color(162,40,40);
    private final Color HOVERINFGCOLOR=new Color(162,40,40);
    private final Color HOVERINBGCOLOR=new Color(255,255,255);
    
    
    
    // VARIABLES
    private String Answers[], Questions[] = null;
    private JButton topPanelButtons[], allButtons[] = null;
    private Customer currCustomer = null;
    
    
    private void init(){
        btnBotActionLeft.setEnabled(true);
        btnBotActionLeft.setFocusable(true);
        btnBotActionRight.setEnabled(false);
        btnBotActionRight.setFocusable(false);
        setEnableTopPanelButtons(true);
        btnMidAction.setVisible(true);
        lblMidActionTitle.setVisible(true);
        txtMidInputSP.setVisible(true);
        txtMidInputLogin.setVisible(false);
        txtMidInput.setText("");
        txtMidInputLogin.setText("");
        setMidOut("");
        setMidFlag("");
        Answers = new String[10];
    }
    
    private void ButtonsInit(){
        topPanelButtons = new JButton[]{btnTopBalance, btnTopDeposit, btnTopWithdraw, btnTopProfile, btnTopCloseAccount};
        allButtons = new JButton[]{btnTopBalance, btnTopDeposit, btnTopWithdraw, btnTopProfile, btnTopCloseAccount, btnBotActionLeft, btnBotActionRight, btnMidAction, lblTopX};
        
        for(JButton b : allButtons){
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
                    if(b.isEnabled())
                        if(b != btnMidAction){
                            for(JButton btn : allButtons){
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
                }
            });
            
        }
    }
    
    
    // APPLICATION STATE
    private void stateLogin(){
        init();
        setEnableTopPanelButtons(false);
        btnBotActionRight.setEnabled(true);
        btnBotActionRight.setFocusable(true);
        txtMidInputSP.setVisible(false);
        txtMidInputLogin.setVisible(true);
        setBottomButtonText("LOGIN", "NEW ACCOUNT");
        setMidPanelAction("LOGIN", "", false);
        setMidOut("<html><h1>PLEASE INPUT YOUR 4 DIGIT ACCOUNT NUMBER</h1></html>");
    }
    
    private void stateNewAccount(){
        init();
        setEnableTopPanelButtons(false);
        btnBotActionRight.setEnabled(true);
        btnBotActionRight.setFocusable(true);
        
        setBottomButtonText("LOGIN", "NEW ACCOUNT");
        setMidPanelAction("NEW ACCOUNT", "NEXT", true);
        
    }
    
    private void stateBalance(){
        init();
        txtMidInputSP.setVisible(false);
        setMidPanelAction("BALANCE", "OK", true);
        setMidOut("<html><br/><br/><br/><h1>PHP <span style=\"font-size:23px\">BALANCE</span></h1></html>");
        
    }
    
    private void stateDeposit(){
        init();
        setMidPanelAction("DEPOSIT", "DEPOSIT", true);
        setMidOut("ENTER AMOUNT");
        
    }
    
    private void stateWithdraw(){
        init();
        setMidPanelAction("WITHDRAW", "WITHDRAW", true);
        setMidOut("ENTER AMOUNT");
        
    }
    
    private void stateProfile(){
        init();
        txtMidInputSP.setVisible(false);
        setMidPanelAction("PROFILE", "OK", true);
        setMidOut("OUTPUT PROFILE DETAILS");
        
    }
    
    private void stateCloseAccount(){
        init();
        txtMidInputSP.setVisible(false);
        setMidPanelAction("CLOSE ACCOUNT", "YES", true);
        setMidOut("<html><br/><center><span style=\"font-size:19px\">ARE YOU SURE YOU WANT <br/>TO CLOSE YOUR ACCOUNT?</span></center></html>");
    }
    
    private void stateMenu(){
        init();
        
        for(JButton b : allButtons){
            b.setForeground(HOVEROUTFGCOLOR);
            b.setBackground(HOVEROUTBGCOLOR);
        }
        setBottomButtonText("LOGOUT", "");
        String out = "<html>", lastname = "Narzoles";
        out += "<p style=\"text-align: center;\">Hello, <strong><em>"+ lastname +"</em></strong></p>\n" +
"<p style=\"text-align: center;\">\n" +
"  <br>\n" +
"</p>\n" +
"<p style=\"text-align: center;\">What would you like to do today?</p></html>";
        btnMidAction.setVisible(false);
        lblMidActionTitle.setVisible(false);
        lblMidOut.setText(out);
        txtMidInputSP.setVisible(false);
        
    }
    
    
    // PROCESS
    private void newAccount(){
        
    }
    
    private void balance(){
        
    }
    
    private void deposit(){
        
    }
    
    private void withdraw(){
        
    }
    
    private void profile(){
        
    }
    
    private void closeAccount(){
        
    }
    
    // UI
    private void setEnableTopPanelButtons(boolean bool){
        for(JButton b : topPanelButtons)
            b.setEnabled(bool);
    }
    
    private void setMidPanelAction(String title, String action, boolean btnVisible){
        lblMidActionTitle.setText(title);
        btnMidAction.setText(action);
        btnMidAction.setVisible(btnVisible);
    }
    
    private void setBottomButtonText(String left, String right){
        btnBotActionLeft.setText(left);
        btnBotActionRight.setText(right);
    }
    
    
    private void setButtonBGColor(JButton btn, Color color){
        btn.setBackground(color);
    }
    private void setButtonFGColor(JButton btn, Color color){
        btn.setForeground(color);
    }
    
    private void setMidFlag(String flag){
        lblMidFlag.setText(flag);
    }
    private void setMidOut(String out){
        lblMidOut.setText(out);
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
        btnTopBalance = new javax.swing.JButton();
        btnTopDeposit = new javax.swing.JButton();
        btnTopWithdraw = new javax.swing.JButton();
        btnTopProfile = new javax.swing.JButton();
        btnTopCloseAccount = new javax.swing.JButton();
        lblTopX = new javax.swing.JButton();
        lblTopTitle2 = new javax.swing.JButton();
        lblTopTime = new javax.swing.JButton();
        lblTopTitle = new javax.swing.JButton();
        PanelMid = new javax.swing.JPanel();
        lblMidFlag = new javax.swing.JLabel();
        PanelMidMid = new javax.swing.JPanel();
        txtMidInputSP = new javax.swing.JScrollPane();
        txtMidInput = new javax.swing.JTextArea();
        txtMidInputLogin = new javax.swing.JPasswordField();
        btnMidAction = new javax.swing.JButton();
        lblMidActionTitle = new javax.swing.JButton();
        lblMidOut = new javax.swing.JLabel();
        PanelBot = new javax.swing.JPanel();
        btnBotActionLeft = new javax.swing.JButton();
        btnBotActionRight = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(null);

        PanelTop.setBackground(new java.awt.Color(172, 50, 50));
        PanelTop.setForeground(new java.awt.Color(191, 191, 191));
        PanelTop.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        PanelTop.setLayout(null);

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
        btnTopBalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTopBalanceActionPerformed(evt);
            }
        });
        PanelTop.add(btnTopBalance);
        btnTopBalance.setBounds(0, 70, 155, 50);

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
        btnTopDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTopDepositActionPerformed(evt);
            }
        });
        PanelTop.add(btnTopDeposit);
        btnTopDeposit.setBounds(160, 70, 155, 50);

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
        btnTopWithdraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTopWithdrawActionPerformed(evt);
            }
        });
        PanelTop.add(btnTopWithdraw);
        btnTopWithdraw.setBounds(320, 70, 155, 50);

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
        btnTopProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTopProfileActionPerformed(evt);
            }
        });
        PanelTop.add(btnTopProfile);
        btnTopProfile.setBounds(480, 70, 155, 50);

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
        btnTopCloseAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTopCloseAccountActionPerformed(evt);
            }
        });
        PanelTop.add(btnTopCloseAccount);
        btnTopCloseAccount.setBounds(640, 70, 160, 50);

        lblTopX.setBackground(new java.awt.Color(162, 40, 40));
        lblTopX.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 24)); // NOI18N
        lblTopX.setForeground(new java.awt.Color(191, 191, 191));
        lblTopX.setText("X");
        lblTopX.setBorder(null);
        lblTopX.setContentAreaFilled(false);
        lblTopX.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTopX.setFocusPainted(false);
        lblTopX.setFocusable(false);
        lblTopX.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblTopX.setOpaque(true);
        lblTopX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblTopXActionPerformed(evt);
            }
        });
        PanelTop.add(lblTopX);
        lblTopX.setBounds(770, 0, 30, 30);

        lblTopTitle2.setBackground(new java.awt.Color(162, 40, 40));
        lblTopTitle2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 19)); // NOI18N
        lblTopTitle2.setForeground(new java.awt.Color(255, 255, 255));
        lblTopTitle2.setText("BLABLABLA");
        lblTopTitle2.setBorder(null);
        lblTopTitle2.setContentAreaFilled(false);
        lblTopTitle2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblTopTitle2.setEnabled(false);
        lblTopTitle2.setFocusPainted(false);
        lblTopTitle2.setFocusable(false);
        lblTopTitle2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTopTitle2.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        lblTopTitle2.setOpaque(true);
        PanelTop.add(lblTopTitle2);
        lblTopTitle2.setBounds(480, 30, 300, 30);

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
        lblTopTime.setBounds(460, 0, 300, 30);

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
        lblTopTitle.setBounds(0, -10, 800, 75);

        getContentPane().add(PanelTop);
        PanelTop.setBounds(0, 0, 800, 120);

        PanelMid.setBackground(new java.awt.Color(170, 70, 70));
        PanelMid.setForeground(new java.awt.Color(191, 191, 191));
        PanelMid.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        PanelMid.setLayout(null);

        lblMidFlag.setBackground(new java.awt.Color(170, 70, 70));
        lblMidFlag.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        lblMidFlag.setForeground(new java.awt.Color(219, 219, 219));
        lblMidFlag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMidFlag.setText("<FLAG>");
        lblMidFlag.setOpaque(true);
        PanelMid.add(lblMidFlag);
        lblMidFlag.setBounds(130, 390, 540, 40);

        PanelMidMid.setBackground(new java.awt.Color(172, 50, 50));
        PanelMidMid.setLayout(null);

        txtMidInputSP.setBorder(null);
        txtMidInputSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        txtMidInputSP.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        txtMidInputSP.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N

        txtMidInput.setBackground(new java.awt.Color(219, 219, 219));
        txtMidInput.setColumns(20);
        txtMidInput.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 27)); // NOI18N
        txtMidInput.setForeground(new java.awt.Color(172, 50, 50));
        txtMidInput.setLineWrap(true);
        txtMidInput.setRows(5);
        txtMidInput.setWrapStyleWord(true);
        txtMidInput.setBorder(null);
        txtMidInputSP.setViewportView(txtMidInput);

        PanelMidMid.add(txtMidInputSP);
        txtMidInputSP.setBounds(120, 130, 410, 70);

        txtMidInputLogin.setBackground(new java.awt.Color(219, 219, 219));
        txtMidInputLogin.setColumns(4);
        txtMidInputLogin.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 27)); // NOI18N
        txtMidInputLogin.setForeground(new java.awt.Color(172, 50, 50));
        txtMidInputLogin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMidInputLogin.setBorder(null);
        txtMidInputLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMidInputLoginActionPerformed(evt);
            }
        });
        txtMidInputLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMidInputLoginKeyReleased(evt);
            }
        });
        PanelMidMid.add(txtMidInputLogin);
        txtMidInputLogin.setBounds(120, 130, 410, 70);

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
        PanelMidMid.add(btnMidAction);
        btnMidAction.setBounds(0, 300, 640, 60);

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
        PanelMidMid.add(lblMidActionTitle);
        lblMidActionTitle.setBounds(0, 0, 640, 60);

        lblMidOut.setBackground(new java.awt.Color(172, 50, 50));
        lblMidOut.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        lblMidOut.setForeground(new java.awt.Color(219, 219, 219));
        lblMidOut.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMidOut.setText("<OUTPUT>");
        lblMidOut.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        PanelMidMid.add(lblMidOut);
        lblMidOut.setBounds(30, 80, 570, 190);

        PanelMid.add(PanelMidMid);
        PanelMidMid.setBounds(80, 30, 640, 360);

        getContentPane().add(PanelMid);
        PanelMid.setBounds(0, 120, 800, 430);

        PanelBot.setBackground(new java.awt.Color(162, 0, 37));
        PanelBot.setForeground(new java.awt.Color(191, 191, 191));
        PanelBot.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        PanelBot.setLayout(null);

        btnBotActionLeft.setBackground(new java.awt.Color(255, 255, 255));
        btnBotActionLeft.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        btnBotActionLeft.setForeground(new java.awt.Color(162, 40, 40));
        btnBotActionLeft.setText("LEFT ACTION");
        btnBotActionLeft.setBorder(null);
        btnBotActionLeft.setContentAreaFilled(false);
        btnBotActionLeft.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBotActionLeft.setFocusPainted(false);
        btnBotActionLeft.setOpaque(true);
        btnBotActionLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBotActionLeftActionPerformed(evt);
            }
        });
        PanelBot.add(btnBotActionLeft);
        btnBotActionLeft.setBounds(0, 20, 400, 50);

        btnBotActionRight.setBackground(new java.awt.Color(162, 40, 40));
        btnBotActionRight.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        btnBotActionRight.setForeground(new java.awt.Color(191, 191, 191));
        btnBotActionRight.setText("RIGHT ACTION");
        btnBotActionRight.setBorder(null);
        btnBotActionRight.setContentAreaFilled(false);
        btnBotActionRight.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBotActionRight.setFocusPainted(false);
        btnBotActionRight.setOpaque(true);
        btnBotActionRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBotActionRightActionPerformed(evt);
            }
        });
        PanelBot.add(btnBotActionRight);
        btnBotActionRight.setBounds(400, 20, 400, 50);

        getContentPane().add(PanelBot);
        PanelBot.setBounds(0, 530, 800, 70);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtMidInputLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMidInputLoginActionPerformed
        // TODO add your handling code here:
        if(txtMidInputLogin.getText().length() == 4){
            
        }
    }//GEN-LAST:event_txtMidInputLoginActionPerformed

    private void txtMidInputLoginKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMidInputLoginKeyReleased
        // TODO add your handling code here:
        int inputAccNo;
        try {
            inputAccNo = Integer.parseInt(txtMidInputLogin.getText());
            if(txtMidInputLogin.getText().length() >= 4){
                // validate accNo
                if(txtMidInputLogin.getText().length() == 4){
                    // ENTER VALIDATION
                    if(Integer.parseInt(txtMidInputLogin.getText()) == 1916)
                        stateMenu();
                }
                txtMidInputLogin.setText("");
            }
        } catch (NumberFormatException nfe) {
            txtMidInputLogin.setText("");
        }

    }//GEN-LAST:event_txtMidInputLoginKeyReleased

    private void lblTopXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblTopXActionPerformed
        // TODO add your handling code here:
        int con = JOptionPane.showConfirmDialog(PanelTop, "Are you sure you want to exit?", "BANKING SYSTEM   |   EXIT", JOptionPane.YES_NO_OPTION , JOptionPane.ERROR_MESSAGE);
        if(con == 0)
            System.exit(0);
    }//GEN-LAST:event_lblTopXActionPerformed

    private void btnBotActionLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBotActionLeftActionPerformed
        // TODO add your handling code here:
        switch(btnBotActionLeft.getText()){
            case "LOGIN":
                stateLogin();
                break;
            case "LOGOUT":
                int con = JOptionPane.showConfirmDialog(PanelTop, "Are you sure you want to Log-out?", "BANKING SYSTEM   |   LOGOUT", JOptionPane.YES_NO_OPTION , JOptionPane.QUESTION_MESSAGE);
                if(con == 0){
                    stateLogin();
                    btnBotActionLeft.setEnabled(false);
                    btnBotActionLeft.setFocusable(false);
                }
                break;
        }
    }//GEN-LAST:event_btnBotActionLeftActionPerformed

    private void btnBotActionRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBotActionRightActionPerformed
        // TODO add your handling code here:
        switch(btnBotActionRight.getText()){
            case "NEW ACCOUNT":
                stateNewAccount();
                break;
        }
    }//GEN-LAST:event_btnBotActionRightActionPerformed

    private void btnTopBalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTopBalanceActionPerformed
        // TODO add your handling code here:
        stateBalance();
    }//GEN-LAST:event_btnTopBalanceActionPerformed

    private void btnTopDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTopDepositActionPerformed
        // TODO add your handling code here:
        stateDeposit();
    }//GEN-LAST:event_btnTopDepositActionPerformed

    private void btnTopWithdrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTopWithdrawActionPerformed
        // TODO add your handling code here:
        stateWithdraw();
    }//GEN-LAST:event_btnTopWithdrawActionPerformed

    private void btnTopProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTopProfileActionPerformed
        // TODO add your handling code here:
        stateProfile();
    }//GEN-LAST:event_btnTopProfileActionPerformed

    private void btnTopCloseAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTopCloseAccountActionPerformed
        // TODO add your handling code here:
        stateCloseAccount();
    }//GEN-LAST:event_btnTopCloseAccountActionPerformed

    private void btnMidActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMidActionActionPerformed
        // TODO add your handling code here:
        switch(lblMidActionTitle.getText()){
            case "NEW ACCOUNT": 
                newAccount(); 
                break;
                
            case "BALANCE": 
                balance(); 
                break;
                
            case "DEPOSIT": 
                deposit(); 
                break;
                
            case "WITHDRAW": 
                withdraw();
                break;
                
            case "PROFILE": 
                profile();
                break;
                
            case "CLOSE ACCOUNT": 
                closeAccount(); 
                break;
        }
        
    }//GEN-LAST:event_btnMidActionActionPerformed

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
            java.util.logging.Logger.getLogger(BANKINGFORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BANKINGFORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BANKINGFORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BANKINGFORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BANKINGFORM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelBot;
    private javax.swing.JPanel PanelMid;
    private javax.swing.JPanel PanelMidMid;
    private javax.swing.JPanel PanelTop;
    private javax.swing.JButton btnBotActionLeft;
    private javax.swing.JButton btnBotActionRight;
    private javax.swing.JButton btnMidAction;
    private javax.swing.JButton btnTopBalance;
    private javax.swing.JButton btnTopCloseAccount;
    private javax.swing.JButton btnTopDeposit;
    private javax.swing.JButton btnTopProfile;
    private javax.swing.JButton btnTopWithdraw;
    private javax.swing.JButton lblMidActionTitle;
    private javax.swing.JLabel lblMidFlag;
    private javax.swing.JLabel lblMidOut;
    private javax.swing.JButton lblTopTime;
    private javax.swing.JButton lblTopTitle;
    private javax.swing.JButton lblTopTitle2;
    private javax.swing.JButton lblTopX;
    private javax.swing.JTextArea txtMidInput;
    private javax.swing.JPasswordField txtMidInputLogin;
    private javax.swing.JScrollPane txtMidInputSP;
    // End of variables declaration//GEN-END:variables
}
