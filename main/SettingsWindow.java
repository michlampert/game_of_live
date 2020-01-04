package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SettingsWindow extends JFrame implements ActionListener {

    JTextField width = new JTextField("50");
    JTextField height = new JTextField("50");
    JTextField time = new JTextField("100");
    JButton applyButton = new JButton("apply");
    JButton quitButton = new JButton("quit");
    ButtonGroup modeGroup = new ButtonGroup();
    JRadioButton torusMode;
    JRadioButton boundedMode;
    JLabel size = new JLabel();
    JLabel mode = new JLabel();
    JLabel rules1 = new JLabel();
    JLabel rules2 = new JLabel();
    JLabel timeInfo = new JLabel();

    ArrayList<JCheckBox> rulesAliveArray = new ArrayList<>();
    ArrayList<JCheckBox> rulesNotAliveArray = new ArrayList<>();

    public boolean flag = false;


    public static void main(String[] args) throws InterruptedException {
        SettingsWindow settingsWindow = new SettingsWindow();

        ArrayList<Integer> rulesForAlive = new ArrayList<>();
        ArrayList<Integer> rulesForNotAlive = new ArrayList<>();
        int w = 1, h = 1;
        boolean modeFlag;

        while(true){
            TimeUnit.MILLISECONDS.sleep(1);
            if(settingsWindow.flag == true) {
                for(int i = 0; i < 8; i++){
                    if(settingsWindow.rulesAliveArray.get(i).isSelected()) rulesForAlive.add(i);
                    if(settingsWindow.rulesNotAliveArray.get(i).isSelected()) rulesForNotAlive.add(i);
                }
                w = Integer.valueOf(settingsWindow.width.getText()) - 1;
                h = Integer.valueOf(settingsWindow.height.getText()) - 1;
                modeFlag = settingsWindow.torusMode.isSelected();
                settingsWindow.dispose();
                break;
            }
        }

        TorusMap torusMap = new TorusMap(new Vector2d(w, h), rulesForAlive, rulesForNotAlive);
        BoundedMap boundedMap = new BoundedMap(new Vector2d(w, h), rulesForAlive, rulesForNotAlive);
        if(modeFlag){
            SimulationWindow view = new SimulationWindow(torusMap);
            view.run();
        }
        else{
            SimulationWindow view = new SimulationWindow(boundedMap);
            view.run();
        }

    }

    public SettingsWindow() {
        this.setSize(400, 320);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Settings");
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        size.setBounds(10, 10, 120, 20);
        this.add(size);
        size.setText("Set map size (w, h): ");

        width.setBounds(140, 10, 70, 20);
        this.add(width);
        height.setBounds(220, 10, 70, 20);
        this.add(height);


        mode.setBounds(10, 40, 120, 20);
        this.add(mode);
        mode.setText("Choose mode: ");

        torusMode = new JRadioButton("torus", true);
        torusMode.setBounds(140, 40, 60, 20);
        this.add(torusMode);
        boundedMode = new JRadioButton("bounded", false);
        boundedMode.setBounds(220, 40, 80, 20);
        this.add(boundedMode);
        modeGroup.add(torusMode);
        modeGroup.add(boundedMode);

        rules1.setBounds(10,70,200,20);
        this.add(rules1);
        rules1.setText("Choose when cell should stay alive");
        rules2.setBounds(10,130,200,20);
        this.add(rules2);
        rules2.setText("Choose when cell should recover");

        for(int i = 0; i < 9; i++){
            JCheckBox chb = new JCheckBox(String.valueOf(i));
            if(i == 2 || i == 3) chb.setSelected(true);
            rulesAliveArray.add(chb);
            this.add(chb);
            chb.setBounds(10 + i * 40, 100, 40, 20);
        }

        for(int i = 0; i < 9; i++){
            JCheckBox chb = new JCheckBox(String.valueOf(i));
            if(i == 3) chb.setSelected(true);
            rulesNotAliveArray.add(chb);
            this.add(chb);
            chb.setBounds(10 + i * 40, 160, 40, 20);
        }

        timeInfo.setBounds(10,190,120,20);
        this.add(timeInfo);
        timeInfo.setText("Set time delay (ms):");

        time.setBounds(140,190,70,20);
        this.add(time);

        applyButton.setBounds(300,250,70,20);
        this.add(applyButton);
        applyButton.addActionListener(this);

        quitButton.setBounds(220,250,70,20);
        this.add(quitButton);
        quitButton.addActionListener(this);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == applyButton) {
            this.flag = true;
        }
        else if(source == quitButton){
            this.dispose();
        }

    }
}
