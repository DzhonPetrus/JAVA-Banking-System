
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class BankingSystem extends javax.swing.JFrame {

    /**
     * Creates new form BankingSystem
     */
    public BankingSystem() {
        initComponents();
        
        
        FormTextFields = new JTextField[]{TxtAccNo, TxtBal, TxtFname, TxtLname, TxtContactNo};
        
        DtBirthday.setMaxSelectableDate(new Date());
        
        ApplicationState(0);
    }

    private final int MINIMUM_AMOUNT = 100;
    private final int MAINTAINING_BALANCE = 5000;
    private final double INTEREST_RATE = .05;
    
    //VARIABLES
    private int NewID = 0;
    private int AccNo = 0;
    private Client client = null;
    private boolean FormFilled = false;
    private boolean ValidAmount = false;
    private Timer timer, timer2;
    private List<Client> clients = new ArrayList<>();
    private final Client_DAO client_DAO = new Client_DAO();
    
    private JTextField FormTextFields[] = null;
    
    
    //Client Account Action
    private void Deposit(int Amount){
        String Out = "<html>";
        Out += "<p style=\"text-align: left; margin-left: 15px;\">Balance:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>Php " + client.getBalance() + "</strong></p>\n" +
"<p style=\"text-align: left; margin-left: 150px;\"><strong>+ &nbsp;Php " + Amount + "</strong></p>\n";
        
        client.setBalance(client.getBalance() + Amount);
        Out += "<p style=\"text-align: left; margin-left: 150px;\">= &nbsp;<strong>Php " + client.getBalance() + "</strong></p>\n" +
"<p style=\"text-align: left; margin-left: 15px;\">Interest:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Php " + (int)(client.getBalance() * INTEREST_RATE) + "</p>\n";
        
        client.setBalance(client.getBalance() + (int)(client.getBalance() * INTEREST_RATE));
        Out += "<p style=\"text-align: left; margin-left: 15px;\">New Balance:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   <strong>Php " + client.getBalance() + "</strong></p></html>";
        
        LabelMenuDisplayOut.setText(Out);
        TxtMenuDisplayInput.setVisible(false);
        BtnMenuDisplayAction.setText("OK");
        
        try {
            client_DAO.UpdateClientBalance(client, client.getBalance());
        } catch (SQLException ex) {
            Logger.getLogger(BankingSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void Withdrawal(int Amount){
        String Out = "<html>";
        Out += "<p style=\"text-align: left; margin-left: 15px;\">Balance:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>Php " + client.getBalance() + "</strong></p>\n" +
"<p style=\"text-align: left; margin-left: 150px;\"><strong>- &nbsp;Php " + Amount + "</strong></p>\n";
        
        client.setBalance(client.getBalance() - Amount);
        Out += "<p style=\"text-align: left; margin-left: 15px;\">New Balance:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   <strong>Php " + client.getBalance() + "</strong></p></html>";
        
        LabelMenuDisplayOut.setText(Out);
        TxtMenuDisplayInput.setVisible(false);
        BtnMenuDisplayAction.setText("OK");
        
        try {
            client_DAO.UpdateClientBalance(client, client.getBalance());
        } catch (SQLException ex) {
            Logger.getLogger(BankingSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void CloseAccount(){
        
        String Out = "<html> <p style=\"text-align: center;\">Last balance is <strong>"+ client.getBalance() +"</strong></p>\n" + 
                "<p style=\"text-align: center;\">It is now 0</p>\n" + 
                "<p style=\"text-align: center;font-size: 120%\"><strong>Your Account is now closed!</strong></p> </html>";
        
        LabelMenuDisplayOut.setText(Out);
        BtnMenuDisplayAction.setText("OK");
        
        try {
            client_DAO.UpdateClientStatus(client, "Closed");
        } catch (SQLException ex) {
            Logger.getLogger(BankingSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    // DB
    private void LoadClients(){
        clients = client_DAO.GetAllClients();
        if(!clients.isEmpty())
            NewID = (clients.get(clients.size() - 1).getID()) + 1;
        else 
            NewID = 1;
    }
    
    private boolean VerifyAccNoExist(int AccNo) throws SQLException{
        client = client_DAO.GetClientByAccNo(AccNo);
        return (client != null);
    }
    
    
    // UI
    private void ApplicationState(int state){
        LoadClients();
         switch(state) {
            case 0: // Login State
                PanelLogin.setVisible(true);
                PanelAccount.setVisible(false);
                PanelMenu.setVisible(false);
                PassPin.setText("");
                BtnEnter.setEnabled(false);
                client = null;
                break;
            case 1: // New Account OR Profile State
                LabelAccTitle.setText("Please fill out all the details:");
                PanelLogin.setVisible(false);
                PanelAccount.setVisible(true);
                PanelMenu.setVisible(false);
                BtnAccAction.setEnabled(false);
                ClearAccountForm();
                VerifyAccountForm();
                break;
            case 2: // Menu State
                PanelLogin.setVisible(false);
                PanelAccount.setVisible(false);
                PanelMenuDisplay.setVisible(false);
                PanelMenu.setVisible(true);
                LabelMenuUser.setText(client.getFName() + " " + client.getLName() + "'s Account");
                break;
         }
    }
    
    private void ClearAccountForm(){
        for(JTextField txt: FormTextFields)
                txt.setText("");
        DtBirthday.setDate(null);
        TxtAddress.setText("");
    }
    
    private void SetMenuDisplay(boolean IsVisible, String ButtonAction, String Title){
        TxtMenuDisplayInput.setVisible(IsVisible);
        LabelMenuDisplayAction.setVisible(IsVisible);
        LabelMenuDisplayOut.setVisible(IsVisible);
        BtnMenuDisplayAction.setVisible(IsVisible);
        BtnMenuDisplayActionMini.setVisible(IsVisible);
        
        LabelMenuDisplayAction.setText(Title);
        BtnMenuDisplayAction.setText(ButtonAction);
        
        PanelMenuDisplay.setVisible(true);
    }
    
    private void VerifyAccountForm(){
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                    LabelAccBal.setText("Initial Deposit");
                    FormFilled = false;
                
                    for(JTextField curr : FormTextFields)
                        FormFilled = (!curr.getText().isEmpty());
                    FormFilled = (!(DtBirthday.getDate() == null || TxtAddress.getText().isEmpty()));

                    if((TxtBal.getText().length() < 4) || Integer.valueOf(TxtBal.getText()) < MAINTAINING_BALANCE){
                        FormFilled = false;
                        TxtBal.setBackground(Color.red);
                        TxtBal.setToolTipText("MINIMUM INITIAL DEPOSIT MUST BE 5000 AND ABOVE");
                    }else{
                        TxtBal.setBackground(Color.white);
                        TxtBal.setToolTipText(null);
                    }
                    
                    BtnAccAction.setEnabled(FormFilled); 
            }
        });
        if(client == null){
            TxtAccNo.setText(""+GenerateAccNo());
            timer.start();
        }else{
            //IF LOGGED IN
            for(JTextField curr: FormTextFields)
            curr.setEnabled(false);
            TxtAddress.setEnabled(false);
            DtBirthday.setEnabled(false);
            BtnAccAction.setVisible(false);
            LabelAccBal.setText("Balance");

            TxtAccNo.setText(Integer.toString(client.getAccountNo()));
            TxtBal.setText(Integer.toString(client.getBalance()));
            TxtFname.setText(client.getFName());
            TxtLname.setText(client.getLName());
            TxtContactNo.setText(client.getContactNo());
            TxtAddress.setText(client.getAddress());
            try {
            DtBirthday.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(client.getBirthday()));
            } catch (ParseException ex) {
            Logger.getLogger(BankingSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private int GenerateAccNo() {
        
        try {
            do {                
                Random rand = new Random();
                AccNo = Integer.valueOf(String.format("%04d", rand.nextInt(10000)));
            } while (VerifyAccNoExist(AccNo));
            
        } catch (SQLException ex) {
            Logger.getLogger(BankingSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return AccNo;
    }
    
    private void PinInput(int Input) {
        if(PassPin.getText().length() < 4){
            PassPin.setText((String) PassPin.getText().concat(Integer.toString(Input)));
            BtnEnter.setEnabled(false);
        }
        
        if (PassPin.getText().length() == 4)
            BtnEnter.setEnabled(true); 
    }
    
    private void PinDel(){
        if(!PassPin.getText().isEmpty())
            PassPin.setText(PassPin.getText().substring(0, PassPin.getText().length() - 1));   
        BtnEnter.setEnabled(false);
    }
    
    private void PinValidate() throws SQLException {
        AccNo = Integer.valueOf(PassPin.getText());
        
        if((VerifyAccNoExist(AccNo)) && client.getStatus().equals("ACTIVE"))
            ApplicationState(2);
        else
            ApplicationState(0);
    }
    
    private void VerifyMenuDisplayInput(){
        timer2 = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ValidAmount = false;
                JTextField Txt = TxtMenuDisplayInput;
                Txt.setToolTipText("MINIMUM AMOUNT IS 100");
                Txt.setForeground(Color.red);
                
                if(!Txt.getText().isEmpty())
                    if(Integer.valueOf(Txt.getText()) >= MINIMUM_AMOUNT){
                        ValidAmount = true;
                        Txt.setToolTipText(null);
                        Txt.setForeground(Color.BLACK);
                        if(BtnMenuDisplayAction.getText().equals("Withdraw"))
                            if((client.getBalance() - Integer.valueOf(Txt.getText())) < MAINTAINING_BALANCE){
                                Txt.setToolTipText("MAINTAINING BALANCE IS 5000");
                                Txt.setForeground(Color.red);
                                ValidAmount = false;
                            }
                                
                        
                    }
                
                
                BtnMenuDisplayAction.setEnabled(ValidAmount);
            }
        });
        timer2.start();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelLogin = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        LblBank = new javax.swing.JLabel();
        PassPin = new javax.swing.JPasswordField();
        Btn7 = new javax.swing.JButton();
        Btn4 = new javax.swing.JButton();
        Btn1 = new javax.swing.JButton();
        Btn2 = new javax.swing.JButton();
        Btn5 = new javax.swing.JButton();
        Btn8 = new javax.swing.JButton();
        Btn9 = new javax.swing.JButton();
        Btn3 = new javax.swing.JButton();
        Btn6 = new javax.swing.JButton();
        BtnDel = new javax.swing.JButton();
        Btn0 = new javax.swing.JButton();
        BtnEnter = new javax.swing.JButton();
        BtnNewAcc = new javax.swing.JButton();
        BtnExit = new javax.swing.JButton();
        PanelAccount = new javax.swing.JPanel();
        LabelAccAccNo = new javax.swing.JLabel();
        LabelAccAddress = new javax.swing.JLabel();
        LabelAccBirthday = new javax.swing.JLabel();
        LabelAccContactNo = new javax.swing.JLabel();
        LabelAccLName = new javax.swing.JLabel();
        LabelAccFName = new javax.swing.JLabel();
        LabelAccBal = new javax.swing.JLabel();
        BtnAccAction = new javax.swing.JButton();
        BtnAccBack = new javax.swing.JButton();
        TxtAccNo = new javax.swing.JTextField();
        TxtBal = new javax.swing.JTextField();
        TxtFname = new javax.swing.JTextField();
        TxtLname = new javax.swing.JTextField();
        TxtContactNo = new javax.swing.JTextField();
        DtBirthday = new com.toedter.calendar.JDateChooser();
        SPAddressScroller = new javax.swing.JScrollPane();
        TxtAddress = new javax.swing.JTextArea();
        LblBank2 = new javax.swing.JLabel();
        LabelAccTitle = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        PanelMenu = new javax.swing.JPanel();
        PanelMenuDisplay = new javax.swing.JPanel();
        BtnMenuDisplayActionMini = new javax.swing.JButton();
        TxtMenuDisplayInput = new javax.swing.JTextField();
        LabelMenuDisplayAction = new javax.swing.JLabel();
        BtnMenuDisplayAction = new javax.swing.JButton();
        LabelMenuDisplayOut = new javax.swing.JLabel();
        BtnMenuWithdraw = new javax.swing.JButton();
        LblBank3 = new javax.swing.JLabel();
        LabelMenuUser = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        BtnMenuDeposit = new javax.swing.JButton();
        BtnMenuProfile = new javax.swing.JButton();
        BtnMenuBalance = new javax.swing.JButton();
        BtnMenuCloseAcc1 = new javax.swing.JButton();
        BtnMenuLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(null);

        PanelLogin.setBackground(new java.awt.Color(36, 47, 65));
        PanelLogin.setAlignmentX(0.0F);
        PanelLogin.setAlignmentY(0.0F);
        PanelLogin.setPreferredSize(new java.awt.Dimension(800, 600));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 0, 27)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setText("Please enter your 4 digit PIN to continue");
        jLabel1.setPreferredSize(new java.awt.Dimension(500, 35));

        LblBank.setFont(new java.awt.Font("Century Gothic", 1, 30)); // NOI18N
        LblBank.setForeground(new java.awt.Color(204, 204, 204));
        LblBank.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        LblBank.setText("BANKO-PH");

        PassPin.setEditable(false);
        PassPin.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        PassPin.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        PassPin.setFocusable(false);

        Btn7.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        Btn7.setText("7");
        Btn7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Btn7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Btn7MouseReleased(evt);
            }
        });

        Btn4.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        Btn4.setText("4");
        Btn4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Btn4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Btn4MouseReleased(evt);
            }
        });

        Btn1.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        Btn1.setText("1");
        Btn1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Btn1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Btn1MouseReleased(evt);
            }
        });

        Btn2.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        Btn2.setText("2");
        Btn2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Btn2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Btn2MouseReleased1(evt);
            }
        });

        Btn5.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        Btn5.setText("5");
        Btn5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Btn5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Btn5MouseReleased(evt);
            }
        });

        Btn8.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        Btn8.setText("8");
        Btn8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Btn8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Btn8MouseReleased(evt);
            }
        });

        Btn9.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        Btn9.setText("9");
        Btn9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Btn9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Btn9MouseReleased(evt);
            }
        });

        Btn3.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        Btn3.setText("3");
        Btn3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Btn3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Btn3MouseReleased(evt);
            }
        });

        Btn6.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        Btn6.setText("6");
        Btn6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Btn6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Btn6MouseReleased(evt);
            }
        });
        Btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rmed(evt);
            }
        });

        BtnDel.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        BtnDel.setText("<--");
        BtnDel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnDel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                BtnDelMouseReleased(evt);
            }
        });

        Btn0.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        Btn0.setText("0");
        Btn0.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Btn0.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Btn0MouseReleased(evt);
            }
        });

        BtnEnter.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        BtnEnter.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnEnter.setLabel("Enter");
        BtnEnter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                BtnEnterMouseReleased(evt);
            }
        });

        BtnNewAcc.setFont(new java.awt.Font("Candara", 1, 12)); // NOI18N
        BtnNewAcc.setText("New Account");
        BtnNewAcc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnNewAcc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                BtnNewAccMouseReleased(evt);
            }
        });

        BtnExit.setFont(new java.awt.Font("Candara", 1, 12)); // NOI18N
        BtnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnExit.setLabel("Exit");
        BtnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                BtnExitMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout PanelLoginLayout = new javax.swing.GroupLayout(PanelLogin);
        PanelLogin.setLayout(PanelLoginLayout);
        PanelLoginLayout.setHorizontalGroup(
            PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelLoginLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LblBank)
                .addGap(35, 35, 35))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelLoginLayout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PanelLoginLayout.createSequentialGroup()
                        .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PassPin, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(PanelLoginLayout.createSequentialGroup()
                                .addComponent(BtnEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(BtnNewAcc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BtnExit))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                        .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelLoginLayout.createSequentialGroup()
                                .addComponent(Btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(Btn5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(Btn6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PanelLoginLayout.createSequentialGroup()
                                .addComponent(Btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(Btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(Btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PanelLoginLayout.createSequentialGroup()
                                .addComponent(Btn0, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addComponent(BtnDel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PanelLoginLayout.createSequentialGroup()
                                .addComponent(Btn7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(Btn8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(Btn9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(126, 126, 126))
        );
        PanelLoginLayout.setVerticalGroup(
            PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelLoginLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(LblBank)
                .addGap(118, 118, 118)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PassPin, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Btn7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Btn8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Btn9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelLoginLayout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(BtnEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelLoginLayout.createSequentialGroup()
                        .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Btn5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Btn6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Btn0, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BtnDel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(PanelLoginLayout.createSequentialGroup()
                                .addComponent(BtnNewAcc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BtnExit)))))
                .addGap(75, 75, 75))
        );

        getContentPane().add(PanelLogin);
        PanelLogin.setBounds(0, 0, 800, 600);

        PanelAccount.setBackground(new java.awt.Color(36, 47, 65));
        PanelAccount.setAlignmentX(0.0F);
        PanelAccount.setAlignmentY(0.0F);
        PanelAccount.setPreferredSize(new java.awt.Dimension(800, 600));
        PanelAccount.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LabelAccAccNo.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        LabelAccAccNo.setForeground(new java.awt.Color(204, 204, 204));
        LabelAccAccNo.setText("Account No.");
        LabelAccAccNo.setPreferredSize(new java.awt.Dimension(500, 35));
        PanelAccount.add(LabelAccAccNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 220, 50));

        LabelAccAddress.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        LabelAccAddress.setForeground(new java.awt.Color(204, 204, 204));
        LabelAccAddress.setText("Address");
        LabelAccAddress.setPreferredSize(new java.awt.Dimension(500, 35));
        PanelAccount.add(LabelAccAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 440, 220, 50));

        LabelAccBirthday.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        LabelAccBirthday.setForeground(new java.awt.Color(204, 204, 204));
        LabelAccBirthday.setText("Birthday");
        LabelAccBirthday.setPreferredSize(new java.awt.Dimension(500, 35));
        PanelAccount.add(LabelAccBirthday, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 390, 220, 50));

        LabelAccContactNo.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        LabelAccContactNo.setForeground(new java.awt.Color(204, 204, 204));
        LabelAccContactNo.setText("Contact No.");
        LabelAccContactNo.setPreferredSize(new java.awt.Dimension(500, 35));
        PanelAccount.add(LabelAccContactNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, 220, 50));

        LabelAccLName.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        LabelAccLName.setForeground(new java.awt.Color(204, 204, 204));
        LabelAccLName.setText("Last Name");
        LabelAccLName.setPreferredSize(new java.awt.Dimension(500, 35));
        PanelAccount.add(LabelAccLName, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 290, 220, 50));

        LabelAccFName.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        LabelAccFName.setForeground(new java.awt.Color(204, 204, 204));
        LabelAccFName.setText("First Name");
        LabelAccFName.setPreferredSize(new java.awt.Dimension(500, 35));
        PanelAccount.add(LabelAccFName, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 240, 220, 50));

        LabelAccBal.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        LabelAccBal.setForeground(new java.awt.Color(204, 204, 204));
        LabelAccBal.setText("Initial Deposit");
        LabelAccBal.setPreferredSize(new java.awt.Dimension(500, 35));
        PanelAccount.add(LabelAccBal, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 220, 50));

        BtnAccAction.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        BtnAccAction.setText("Register");
        BtnAccAction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnAccAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAccActionActionPerformed(evt);
            }
        });
        PanelAccount.add(BtnAccAction, new org.netbeans.lib.awtextra.AbsoluteConstraints(629, 344, 140, 60));

        BtnAccBack.setFont(new java.awt.Font("Candara", 1, 12)); // NOI18N
        BtnAccBack.setText("Back");
        BtnAccBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnAccBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAccBackActionPerformed(evt);
            }
        });
        PanelAccount.add(BtnAccBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(709, 410, 60, -1));

        TxtAccNo.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        TxtAccNo.setEnabled(false);
        PanelAccount.add(TxtAccNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 140, 270, 40));

        TxtBal.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        TxtBal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TxtBalKeyReleased(evt);
            }
        });
        PanelAccount.add(TxtBal, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 190, 270, 40));

        TxtFname.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        PanelAccount.add(TxtFname, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 240, 270, 40));

        TxtLname.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        PanelAccount.add(TxtLname, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 290, 270, 40));

        TxtContactNo.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        TxtContactNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TxtContactNoKeyReleased(evt);
            }
        });
        PanelAccount.add(TxtContactNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 340, 270, 40));

        DtBirthday.setDateFormatString("yyyy-MM-dd");
        DtBirthday.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        DtBirthday.setMaxSelectableDate(new java.util.Date(253370739668000L));
        PanelAccount.add(DtBirthday, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 390, 270, 40));

        TxtAddress.setColumns(20);
        TxtAddress.setFont(new java.awt.Font("Candara", 0, 16)); // NOI18N
        TxtAddress.setRows(5);
        SPAddressScroller.setViewportView(TxtAddress);

        PanelAccount.add(SPAddressScroller, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 440, 270, 140));

        LblBank2.setFont(new java.awt.Font("Century Gothic", 1, 30)); // NOI18N
        LblBank2.setForeground(new java.awt.Color(204, 204, 204));
        LblBank2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        LblBank2.setText("BANKO-PH");
        PanelAccount.add(LblBank2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 470, 50));

        LabelAccTitle.setFont(new java.awt.Font("Century Gothic", 0, 27)); // NOI18N
        LabelAccTitle.setForeground(new java.awt.Color(204, 204, 204));
        LabelAccTitle.setText("Please fill out all the details:");
        LabelAccTitle.setPreferredSize(new java.awt.Dimension(500, 35));
        PanelAccount.add(LabelAccTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 470, 50));

        jSeparator1.setBackground(new java.awt.Color(191, 191, 191));
        PanelAccount.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 510, 10));

        getContentPane().add(PanelAccount);
        PanelAccount.setBounds(0, 0, 800, 600);

        PanelMenu.setBackground(new java.awt.Color(36, 47, 65));
        PanelMenu.setPreferredSize(new java.awt.Dimension(800, 600));
        PanelMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelMenuDisplay.setBackground(new java.awt.Color(36, 47, 65));
        PanelMenuDisplay.setFocusable(false);
        PanelMenuDisplay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PanelMenuDisplayMouseClicked(evt);
            }
        });
        PanelMenuDisplay.setLayout(null);

        BtnMenuDisplayActionMini.setFont(new java.awt.Font("Candara", 1, 12)); // NOI18N
        BtnMenuDisplayActionMini.setText("Cancel");
        BtnMenuDisplayActionMini.setToolTipText("");
        BtnMenuDisplayActionMini.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnMenuDisplayActionMini.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnMenuDisplayActionMiniActionPerformed(evt);
            }
        });
        PanelMenuDisplay.add(BtnMenuDisplayActionMini);
        BtnMenuDisplayActionMini.setBounds(0, 0, 70, 31);

        TxtMenuDisplayInput.setFont(new java.awt.Font("Century Gothic", 0, 24)); // NOI18N
        TxtMenuDisplayInput.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TxtMenuDisplayInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TxtMenuDisplayInputKeyReleased(evt);
            }
        });
        PanelMenuDisplay.add(TxtMenuDisplayInput);
        TxtMenuDisplayInput.setBounds(130, 140, 450, 50);

        LabelMenuDisplayAction.setBackground(new java.awt.Color(36, 47, 65));
        LabelMenuDisplayAction.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        LabelMenuDisplayAction.setForeground(new java.awt.Color(204, 204, 204));
        LabelMenuDisplayAction.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelMenuDisplayAction.setText("<ACTION>");
        LabelMenuDisplayAction.setPreferredSize(new java.awt.Dimension(500, 35));
        PanelMenuDisplay.add(LabelMenuDisplayAction);
        LabelMenuDisplayAction.setBounds(0, 0, 710, 60);

        BtnMenuDisplayAction.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        BtnMenuDisplayAction.setText("ACTION");
        BtnMenuDisplayAction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnMenuDisplayAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnMenuDisplayActionActionPerformed(evt);
            }
        });
        PanelMenuDisplay.add(BtnMenuDisplayAction);
        BtnMenuDisplayAction.setBounds(0, 340, 710, 70);

        LabelMenuDisplayOut.setBackground(new java.awt.Color(36, 47, 65));
        LabelMenuDisplayOut.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        LabelMenuDisplayOut.setForeground(new java.awt.Color(204, 204, 204));
        LabelMenuDisplayOut.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelMenuDisplayOut.setText("<ACTION>");
        LabelMenuDisplayOut.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        LabelMenuDisplayOut.setPreferredSize(new java.awt.Dimension(500, 35));
        PanelMenuDisplay.add(LabelMenuDisplayOut);
        LabelMenuDisplayOut.setBounds(0, 100, 710, 200);

        PanelMenu.add(PanelMenuDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 710, 410));

        BtnMenuWithdraw.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        BtnMenuWithdraw.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnMenuWithdraw.setLabel("Withdraw");
        BtnMenuWithdraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnMenuWithdrawActionPerformed(evt);
            }
        });
        PanelMenu.add(BtnMenuWithdraw, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 270, 300, 70));

        LblBank3.setFont(new java.awt.Font("Century Gothic", 1, 30)); // NOI18N
        LblBank3.setForeground(new java.awt.Color(204, 204, 204));
        LblBank3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        LblBank3.setText("BANKO-PH");
        PanelMenu.add(LblBank3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 470, 50));

        LabelMenuUser.setFont(new java.awt.Font("Century Gothic", 0, 24)); // NOI18N
        LabelMenuUser.setForeground(new java.awt.Color(204, 204, 204));
        LabelMenuUser.setText("Hello, ");
        LabelMenuUser.setPreferredSize(new java.awt.Dimension(500, 35));
        PanelMenu.add(LabelMenuUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 470, 50));

        jSeparator2.setBackground(new java.awt.Color(191, 191, 191));
        PanelMenu.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 680, 10));

        BtnMenuDeposit.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        BtnMenuDeposit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnMenuDeposit.setLabel("Deposit");
        BtnMenuDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnMenuDepositActionPerformed(evt);
            }
        });
        PanelMenu.add(BtnMenuDeposit, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 270, 300, 70));

        BtnMenuProfile.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        BtnMenuProfile.setText("Profile");
        BtnMenuProfile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnMenuProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnMenuProfileActionPerformed(evt);
            }
        });
        PanelMenu.add(BtnMenuProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, 300, 70));

        BtnMenuBalance.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        BtnMenuBalance.setText("Balance Inquiry");
        BtnMenuBalance.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnMenuBalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnMenuBalanceActionPerformed(evt);
            }
        });
        PanelMenu.add(BtnMenuBalance, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, 660, 70));

        BtnMenuCloseAcc1.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        BtnMenuCloseAcc1.setText("Close Account");
        BtnMenuCloseAcc1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnMenuCloseAcc1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnMenuCloseAcc1ActionPerformed(evt);
            }
        });
        PanelMenu.add(BtnMenuCloseAcc1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 380, 300, 70));

        BtnMenuLogout.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        BtnMenuLogout.setText("Log Out");
        BtnMenuLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnMenuLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnMenuLogoutActionPerformed(evt);
            }
        });
        PanelMenu.add(BtnMenuLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 480, 660, 70));

        getContentPane().add(PanelMenu);
        PanelMenu.setBounds(0, 0, 800, 600);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void rmed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rmed

    }//GEN-LAST:event_rmed

    private void Btn0MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn0MouseReleased
        // TODO add your handling code here:
        PinInput(0);
    }//GEN-LAST:event_Btn0MouseReleased

    private void BtnDelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnDelMouseReleased
        // TODO add your handling code here:
        PinDel();
    }//GEN-LAST:event_BtnDelMouseReleased

    private void Btn1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn1MouseReleased
        // TODO add your handling code here:
        PinInput(1);
    }//GEN-LAST:event_Btn1MouseReleased

    private void Btn2MouseReleased1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn2MouseReleased1
        // TODO add your handling code here:
        PinInput(2);
    }//GEN-LAST:event_Btn2MouseReleased1

    private void Btn3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn3MouseReleased
        // TODO add your handling code here:
        PinInput(3);
    }//GEN-LAST:event_Btn3MouseReleased

    private void Btn4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn4MouseReleased
        // TODO add your handling code here:
        PinInput(4);
    }//GEN-LAST:event_Btn4MouseReleased

    private void Btn5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn5MouseReleased
        // TODO add your handling code here:
        PinInput(5);
    }//GEN-LAST:event_Btn5MouseReleased

    private void Btn6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn6MouseReleased
        // TODO add your handling code here:
        PinInput(6);
    }//GEN-LAST:event_Btn6MouseReleased

    private void Btn7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn7MouseReleased
        // TODO add your handling code here:
        PinInput(7);
    }//GEN-LAST:event_Btn7MouseReleased

    private void Btn8MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn8MouseReleased
        // TODO add your handling code here:
        PinInput(8);
    }//GEN-LAST:event_Btn8MouseReleased

    private void Btn9MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Btn9MouseReleased
        // TODO add your handling code here:
        PinInput(9);
    }//GEN-LAST:event_Btn9MouseReleased

    private void BtnEnterMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnEnterMouseReleased
        try {
            PinValidate();
        } catch (SQLException ex) {
            Logger.getLogger(BankingSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_BtnEnterMouseReleased

    private void BtnNewAccMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnNewAccMouseReleased
        // TODO add your handling code here:
        ApplicationState(1);
    }//GEN-LAST:event_BtnNewAccMouseReleased

    private void BtnExitMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnExitMouseReleased
        // TODO add your handling code here:
        int Confirmation = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to exit?", "BANKO-PH   |   EXIT", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (Confirmation == 0)
            System.exit(0);
    }//GEN-LAST:event_BtnExitMouseReleased

    private void BtnAccBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAccBackActionPerformed
        // TODO add your handling code here:
        if(client == null)
            ApplicationState(0);
        else 
            ApplicationState(2);
        
    }//GEN-LAST:event_BtnAccBackActionPerformed

    private void BtnMenuLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnMenuLogoutActionPerformed
        // TODO add your handling code here:
        int Confirmation = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to Log-out?", "BANKO-PH   |   LOG-OUT", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (Confirmation == 0)
            ApplicationState(0);
    }//GEN-LAST:event_BtnMenuLogoutActionPerformed

    private void BtnAccActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAccActionActionPerformed
        // TODO add your handling code here:
        timer.stop();
        
        try {
            
            client = new Client(NewID, Integer.valueOf(FormTextFields[0].getText()), Integer.valueOf(FormTextFields[1].getText()), FormTextFields[2].getText(), FormTextFields[3].getText(), FormTextFields[4].getText(), ((JTextField) DtBirthday.getDateEditor().getUiComponent()).getText(), TxtAddress.getText(), "ACTIVE");
            client_DAO.AddClient(client);
            ApplicationState(2);
        } catch (SQLException ex) {
            Logger.getLogger(BankingSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_BtnAccActionActionPerformed

    private void TxtBalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtBalKeyReleased
        // TODO add your handling code here:
        
        int inputBalance;
        try {
            inputBalance = Integer.parseInt(TxtBal.getText());
        } catch (NumberFormatException nfe) {
            TxtBal.setText("");
        }
    }//GEN-LAST:event_TxtBalKeyReleased

    private void TxtContactNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtContactNoKeyReleased
        // TODO add your handling code here:
        long inputContactNo;
        try {
            inputContactNo = (long) Long.parseLong(TxtContactNo.getText());
            if(TxtContactNo.getText().length() >= 12){
                TxtContactNo.setText("");
            }  
        } catch (NumberFormatException nfe) {
            TxtContactNo.setText("");
        }
    }//GEN-LAST:event_TxtContactNoKeyReleased

    private void BtnMenuProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnMenuProfileActionPerformed
        // TODO add your handling code here:
        ApplicationState(1);
    }//GEN-LAST:event_BtnMenuProfileActionPerformed

    private void BtnMenuBalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnMenuBalanceActionPerformed
        // TODO add your handling code here:
        SetMenuDisplay(true, "OK", "Balance");
        TxtMenuDisplayInput.setVisible(false);
        BtnMenuDisplayActionMini.setVisible(false);
        LabelMenuDisplayOut.setText("Php "+client.getBalance());
    }//GEN-LAST:event_BtnMenuBalanceActionPerformed

    private void BtnMenuDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnMenuDepositActionPerformed
        // TODO add your handling code here:
        SetMenuDisplay(true, "Deposit", "Deposit");
        TxtMenuDisplayInput.setText("");
        LabelMenuDisplayOut.setText("Enter Amount (Minimum Php 100)");
        VerifyMenuDisplayInput();
    }//GEN-LAST:event_BtnMenuDepositActionPerformed

    private void BtnMenuWithdrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnMenuWithdrawActionPerformed
        // TODO add your handling code here:
        SetMenuDisplay(true, "Withdraw", "Withdraw");
        TxtMenuDisplayInput.setText("");
        LabelMenuDisplayOut.setText("Enter Amount (Minimum Php 100)");
        VerifyMenuDisplayInput();
    }//GEN-LAST:event_BtnMenuWithdrawActionPerformed

    private void BtnMenuCloseAcc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnMenuCloseAcc1ActionPerformed
        // TODO add your handling code here:
        SetMenuDisplay(true, "YES", "Close Account");
        TxtMenuDisplayInput.setVisible(false);
        LabelMenuDisplayOut.setText("<html><font size=\"6\"><br/>Are you sure you want <br/>to close your account?</font></html>");
    }//GEN-LAST:event_BtnMenuCloseAcc1ActionPerformed

    private void BtnMenuDisplayActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnMenuDisplayActionActionPerformed
        // TODO add your handling code here:
        timer2.stop();
        int input = 0;
        if(!TxtMenuDisplayInput.getText().isEmpty())
            input = Integer.valueOf(TxtMenuDisplayInput.getText());
        switch(BtnMenuDisplayAction.getText()){
            case "OK": 
                if(LabelMenuDisplayAction.getText().equals("Close Account"))
                    ApplicationState(0);
                PanelMenuDisplay.setVisible(false);
                break;
            case "Deposit": Deposit(input); break;
            case "Withdraw": Withdrawal(input); break;
            case "YES": CloseAccount(); break;
        }
    }//GEN-LAST:event_BtnMenuDisplayActionActionPerformed

    private void BtnMenuDisplayActionMiniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnMenuDisplayActionMiniActionPerformed
        // TODO add your handling code here:
        switch(BtnMenuDisplayActionMini.getText()){
            case "Cancel": PanelMenuDisplay.setVisible(false); break;
        }
    }//GEN-LAST:event_BtnMenuDisplayActionMiniActionPerformed

    private void PanelMenuDisplayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelMenuDisplayMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_PanelMenuDisplayMouseClicked

    private void TxtMenuDisplayInputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtMenuDisplayInputKeyReleased
        // TODO add your handling code here:
        long inputMenuDisplay;
        try {
            inputMenuDisplay = (long) Long.parseLong(TxtMenuDisplayInput.getText());
        } catch (NumberFormatException nfe) {
            TxtMenuDisplayInput.setText("");
        }
    }//GEN-LAST:event_TxtMenuDisplayInputKeyReleased

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
            java.util.logging.Logger.getLogger(BankingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BankingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BankingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BankingSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BankingSystem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn0;
    private javax.swing.JButton Btn1;
    private javax.swing.JButton Btn2;
    private javax.swing.JButton Btn3;
    private javax.swing.JButton Btn4;
    private javax.swing.JButton Btn5;
    private javax.swing.JButton Btn6;
    private javax.swing.JButton Btn7;
    private javax.swing.JButton Btn8;
    private javax.swing.JButton Btn9;
    private javax.swing.JButton BtnAccAction;
    private javax.swing.JButton BtnAccBack;
    private javax.swing.JButton BtnDel;
    private javax.swing.JButton BtnEnter;
    private javax.swing.JButton BtnExit;
    private javax.swing.JButton BtnMenuBalance;
    private javax.swing.JButton BtnMenuCloseAcc1;
    private javax.swing.JButton BtnMenuDeposit;
    private javax.swing.JButton BtnMenuDisplayAction;
    private javax.swing.JButton BtnMenuDisplayActionMini;
    private javax.swing.JButton BtnMenuLogout;
    private javax.swing.JButton BtnMenuProfile;
    private javax.swing.JButton BtnMenuWithdraw;
    private javax.swing.JButton BtnNewAcc;
    private com.toedter.calendar.JDateChooser DtBirthday;
    private javax.swing.JLabel LabelAccAccNo;
    private javax.swing.JLabel LabelAccAddress;
    private javax.swing.JLabel LabelAccBal;
    private javax.swing.JLabel LabelAccBirthday;
    private javax.swing.JLabel LabelAccContactNo;
    private javax.swing.JLabel LabelAccFName;
    private javax.swing.JLabel LabelAccLName;
    private javax.swing.JLabel LabelAccTitle;
    private javax.swing.JLabel LabelMenuDisplayAction;
    private javax.swing.JLabel LabelMenuDisplayOut;
    private javax.swing.JLabel LabelMenuUser;
    private javax.swing.JLabel LblBank;
    private javax.swing.JLabel LblBank2;
    private javax.swing.JLabel LblBank3;
    private javax.swing.JPanel PanelAccount;
    private javax.swing.JPanel PanelLogin;
    private javax.swing.JPanel PanelMenu;
    private javax.swing.JPanel PanelMenuDisplay;
    private javax.swing.JPasswordField PassPin;
    private javax.swing.JScrollPane SPAddressScroller;
    private javax.swing.JTextField TxtAccNo;
    private javax.swing.JTextArea TxtAddress;
    private javax.swing.JTextField TxtBal;
    private javax.swing.JTextField TxtContactNo;
    private javax.swing.JTextField TxtFname;
    private javax.swing.JTextField TxtLname;
    private javax.swing.JTextField TxtMenuDisplayInput;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables

    
    
}
