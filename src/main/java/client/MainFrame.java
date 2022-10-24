package client;

import client.events.*;
import client.toolbox.ToolboxController;
import client.users.UserController;
import client.users.UserRole;
import client.whiteboard.WhiteboardController;
import client.whiteboard.WhiteboardPanel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class MainFrame extends JFrame {

    public static final int DEFAULT_WIDTH = 1440;
    public static final int DEFAULT_HEIGHT = 1080;
    public static final int MIN_WIDTH = 1024;
    public static final int MIN_HEIGHT = 768;

    // Singleton.
    private static MainFrame instance;

    private Main main;

    public static MainFrame getInstance() {
        return instance;
    }

    // Controllers.
    public WhiteboardController whiteboardController;
    public UserController userController;
    public ToolboxController toolboxController;

    // Main view.
    public JPanel mainPanel;

    // Whiteboard view.
    public WhiteboardPanel whiteboardPanel;
    public JLabel mousePosLabel;

    // Toolbox view.
    public JPanel toolboxPanel;
    public JRadioButton freehandToolBtn;
    public JRadioButton rectToolBtn;
    public JRadioButton ovalToolBtn;
    public JSlider thicknessSlider;
    public JButton clearBtn;
    public JColorChooser colorChooser;

    // Users view.
    public JPanel rightPanel;
    public JPanel selfUserPanel;
    public JLabel usernameLabel;
    public JScrollPane usersScrollPane;
    public JPanel usersPanel;
    public HashMap<Integer, JButton> userBtns = new HashMap<Integer, JButton>();

    // Other.
    private JPanel bottomPanel;
    private JPanel chatPanel;
    private JButton quitBtn;
    private JPanel infoPanel;
    public JLabel uidLabel;

    public MainFrame(String appName) {
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        MainFrame.instance = this;
        main = Main.getInstance();

        // Setup controllers.
        userController = new UserController();
        toolboxController = new ToolboxController();
        whiteboardController = new WhiteboardController(toolboxController, userController);

        // Initialize controllers. Adds listeners, etc.
        userController.init();
        toolboxController.init();
        whiteboardController.init();

        quitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.getInstance().quit();
            }
        });
    }

    //region Callbacks

    public void enableCallbacks() {
        if (main.client != null) {
            main.client.loggedInEvt.addCallback(this::onLoggedIn);
            main.client.joinedSessionEvt.addCallback(this::onJoinedSession);
            main.client.userJoinedEvt.addCallback(this::onUserJoined);
            main.client.userLeavedEvt.addCallback(this::onUserLeaved);
            main.client.addShapeEvt.addCallback(this::onAddShape);
            main.client.clearWhiteboardEvt.addCallback(this::onClearWhiteboard);
            main.client.newWhiteboardDataEvt.addCallback(this::onNewWhiteboardData);
            main.client.disconnectedEvt.addCallback(this::onDisconnected);
        }
    }

    public void disableCallbacks() {
        if (main.client != null) {
            main.client.loggedInEvt.removeCallback(this::onLoggedIn);
            main.client.joinedSessionEvt.removeCallback(this::onJoinedSession);
            main.client.userJoinedEvt.removeCallback(this::onUserJoined);
            main.client.userLeavedEvt.removeCallback(this::onUserLeaved);
            main.client.addShapeEvt.removeCallback(this::onAddShape);
            main.client.clearWhiteboardEvt.removeCallback(this::onClearWhiteboard);
            main.client.newWhiteboardDataEvt.removeCallback(this::onNewWhiteboardData);
            main.client.disconnectedEvt.removeCallback(this::onDisconnected);
        }
    }

    private void onLoggedIn(Object source, LoginEventArgs args) {
        uidLabel.setText(Integer.toString(args.userData.uid));
    }

    private void onJoinedSession(Object source, EventArgs args) {
        usernameLabel.setText(Main.getInstance().userData.username);
    }

    private void onUserJoined(Object source, UserEventArgs args) {
        if (args.userData.uid == Main.getInstance().userData.uid) {
            return;
        }
        userController.addUser(args.userData);
    }

    private void onUserLeaved(Object o, UserEventArgs userEventArgs) {
        // If manager disconnected, exit.
        if (userEventArgs.userData.role == UserRole.Manager) {
            int result = JOptionPane.showConfirmDialog(null, "Disconnected from server.",
                    "Message", JOptionPane.OK_OPTION);
            if (result > -1) {
                System.exit(0);
            }
        }
        // Otherwise, remove the user panel button of this user.
        else {
            userController.removeUser(userEventArgs.userData);
        }
    }

    private void onAddShape(Object source, ShapeEventArgs args) {
        whiteboardPanel.addPendingShape(args.shape);
        whiteboardPanel.repaint();
    }

    private void onClearWhiteboard(Object source, EventArgs args) {
        whiteboardPanel.clearAll();
    }

    private void onNewWhiteboardData(Object source, WhiteboardDataEventArgs args) {
        whiteboardPanel.bufferedImage = args.whiteboardData.bufferedImage;
        whiteboardPanel.repaint();
    }

    private void onDisconnected(Object o, EventArgs eventArgs) {
        int result = JOptionPane.showConfirmDialog(null, "Disconnected from server.",
                "Message", JOptionPane.OK_OPTION);
        if (result > -1) {
            System.exit(0);
        }
    }


    //endregion


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        toolboxPanel = new JPanel();
        toolboxPanel.setLayout(new GridLayoutManager(9, 1, new Insets(0, 0, 0, 0), -1, -1));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 10;
        mainPanel.add(toolboxPanel, gbc);
        toolboxPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Tools");
        toolboxPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        freehandToolBtn = new JRadioButton();
        freehandToolBtn.setSelected(true);
        freehandToolBtn.setText("Freehand");
        toolboxPanel.add(freehandToolBtn, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rectToolBtn = new JRadioButton();
        rectToolBtn.setText("Rectangle");
        toolboxPanel.add(rectToolBtn, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clearBtn = new JButton();
        clearBtn.setText("Clear");
        toolboxPanel.add(clearBtn, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ovalToolBtn = new JRadioButton();
        ovalToolBtn.setText("Oval");
        toolboxPanel.add(ovalToolBtn, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        toolboxPanel.add(separator1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JSeparator separator2 = new JSeparator();
        toolboxPanel.add(separator2, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        toolboxPanel.add(panel1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Thickness");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel1.add(label2, gbc);
        thicknessSlider = new JSlider();
        thicknessSlider.setAlignmentX(0.5f);
        thicknessSlider.setMajorTickSpacing(5);
        thicknessSlider.setMaximum(16);
        thicknessSlider.setMinimum(1);
        thicknessSlider.setMinorTickSpacing(1);
        thicknessSlider.setOrientation(0);
        thicknessSlider.setPaintLabels(true);
        thicknessSlider.setPaintTicks(true);
        thicknessSlider.setPreferredSize(new Dimension(150, 47));
        thicknessSlider.setSnapToTicks(true);
        thicknessSlider.setValue(6);
        thicknessSlider.setValueIsAdjusting(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel1.add(thicknessSlider, gbc);
        final Spacer spacer1 = new Spacer();
        toolboxPanel.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        whiteboardPanel = new WhiteboardPanel();
        whiteboardPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        whiteboardPanel.setBackground(new Color(-1));
        whiteboardPanel.setForeground(new Color(-1));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(whiteboardPanel, gbc);
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rightPanel.setMaximumSize(new Dimension(2147483647, 2147483647));
        rightPanel.setPreferredSize(new Dimension(128, 28));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 10;
        mainPanel.add(rightPanel, gbc);
        rightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        usersScrollPane = new JScrollPane();
        rightPanel.add(usersScrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        usersPanel = new JPanel();
        usersPanel.setLayout(new FormLayout("", ""));
        usersScrollPane.setViewportView(usersPanel);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(bottomPanel, gbc);
        bottomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        chatPanel = new JPanel();
        chatPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        chatPanel.setPreferredSize(new Dimension(300, 50));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        bottomPanel.add(chatPanel, gbc);
        chatPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        bottomPanel.add(panel2, gbc);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        colorChooser = new JColorChooser();
        panel2.add(colorChooser, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(50, 100), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setPreferredSize(new Dimension(150, 67));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        bottomPanel.add(panel3, gbc);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        quitBtn = new JButton();
        quitBtn.setText("Quit");
        panel3.add(quitBtn, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selfUserPanel = new JPanel();
        selfUserPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(selfUserPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        usernameLabel = new JLabel();
        usernameLabel.setText("username");
        selfUserPanel.add(usernameLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(infoPanel, gbc);
        mousePosLabel = new JLabel();
        mousePosLabel.setText("mouse coord");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        infoPanel.add(mousePosLabel, gbc);
        uidLabel = new JLabel();
        uidLabel.setText("10000");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        infoPanel.add(uidLabel, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("UID: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        infoPanel.add(label3, gbc);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(freehandToolBtn);
        buttonGroup.add(rectToolBtn);
        buttonGroup.add(ovalToolBtn);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
