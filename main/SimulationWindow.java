package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class SimulationWindow extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

    //TODO
    // dostosowanie wielkości do ilości pól a nie na odwrót
    // ustawienie stałej proporcji okienka

    //TODO
    // zamiast porownywać strigi ustawić aktywnośc klawiszy

    AbstractMap map;
    MapView panel;

    JButton ssButton = new JButton("start");
    JButton clearButton = new JButton("clear");
    JButton saveButton = new JButton("save");
    JButton addButton = new JButton("add");
    JTextField nameField = new JTextField();

    int leftMargin = 8;
    int upperMargin = 68;
    public int freeSpace = 40;
    int cellSize;

    Vector2d pointerPosition = new Vector2d(0,0);
    ArrayList<Vector2d> additionalCells =  new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        TorusMap map = new TorusMap(new Vector2d(49,49));
        SimulationWindow view = new SimulationWindow(map);
        view.run();
    }

    public SimulationWindow(AbstractMap map){
        this.map = map;
        this.setSize(this.predictDimension());
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("SimulationView");
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        ssButton.setBounds(10,10,100,20);
        ssButton.addActionListener(this);
        this.add(ssButton);

        clearButton.setBounds(120,10,100,20);
        clearButton.addActionListener(this);
        this.add(clearButton);

        saveButton.setBounds(230,10,100,20);
        saveButton.addActionListener(this);
        this.add(saveButton);

        addButton.setBounds(340,10,100,20);
        addButton.addActionListener(this);
        addButton.setSelected(false);
        this.add(addButton);

        nameField.setBounds(450, 10, 200, 20);
        this.add(nameField);

        panel = new MapView(this);
        prepareFrame(this, panel, panel.w, panel.h);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

    }

    private Dimension predictDimension(){
        int w = this.map.upperRight.x + 1;
        int h = this.map.upperRight.y + 1;

        int x = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int y = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 150;

        int cellSize = min((x - 16) / w,(y - 80) / h);
        this.cellSize = cellSize;
        return new Dimension(cellSize * w, cellSize * h);
    }

    public void run() throws InterruptedException {
        while(true){
            TimeUnit.MILLISECONDS.sleep(1);
            while(ssButton.getText().equals("stop")){
                panel.repaint(1,0,this.freeSpace,panel.w * panel.cellSize,panel.h * panel.cellSize + this.freeSpace);
                this.map.nextGeneration();
                TimeUnit.MILLISECONDS.sleep(100);
            }
            while(addButton.isSelected()){
                panel.repaint(1,0,this.freeSpace,panel.w * panel.cellSize,panel.h * panel.cellSize + this.freeSpace);
                TimeUnit.MILLISECONDS.sleep(100);
            }
        }
    }

    private void prepareFrame(JFrame frame, MapView visualization, int width, int height){
        frame.add(visualization);
        frame.setSize(visualization.w * visualization.cellSize + 16 , visualization.w * visualization.cellSize + 80);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == ssButton)
        {
            if(ssButton.getText().equals("start")) {
                ssButton.setText("stop");
//                saveButton.setBackground(new Color(200,200,210));
//                addButton.setBackground(new Color(200,200,210));
                addButton.setSelected(false);
            }
            else{
                ssButton.setText("start");
                saveButton.setBackground(null);
                addButton.setBackground(null);
            }
        }
        else if(source == clearButton){
            this.map.clear();
            panel.repaint(1,0,this.freeSpace,panel.w * panel.cellSize,panel.h * panel.cellSize + this.freeSpace);
            ssButton.setText("start");
        }
        else if(source == saveButton){
            if(nameField.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "figure name should be longer", "saving fail", JOptionPane.ERROR_MESSAGE);
            }
            else {
                this.map.saveAsJSON(nameField.getText());
            }
        }
        else if(source == addButton){
            if(addButton.isSelected()){
                addButton.setSelected(false);
                this.additionalCells.clear();
                panel.repaint(1,0,this.freeSpace,panel.w * panel.cellSize,panel.h * panel.cellSize + this.freeSpace);
            }
            else{
                try {
                    addButton.setSelected(true);
                    this.additionalCells = Figure.JSONToArray(nameField.getText());
                    panel.repaint(1, 0, this.freeSpace, panel.w * panel.cellSize, panel.h * panel.cellSize + this.freeSpace);
                }
                catch (NullPointerException ex){
                    JOptionPane.showMessageDialog(this, "file with this name doesn't exist", "adding fail", JOptionPane.ERROR_MESSAGE);
                }
                finally {
                    addButton.setSelected(true);
                }
            }

        }
    }

    public Vector2d getPointerPosition(int x, int y){
        if(this.leftMargin  <= x && panel.w * panel.cellSize + this.leftMargin >=  x && this.upperMargin < y && panel.h * panel.cellSize + this.upperMargin >= y){
            int vx = (x - this.leftMargin) / this.panel.cellSize;
            int vy = (y - this.upperMargin) / this.panel.cellSize;
            return new Vector2d(vx, vy);
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!addButton.isSelected()) {
            int x = e.getX();
            int y = e.getY();
            if (this.getPointerPosition(x, y) instanceof Vector2d) {
                this.map.flipCell(this.getPointerPosition(x, y));
            }
            panel.repaint(1, 0, this.freeSpace, panel.w * panel.cellSize, panel.h * panel.cellSize + this.freeSpace);
        }
        else{
            this.map.addCells(this.pointerPosition, this.additionalCells);
            this.pointerPosition = new Vector2d(0,0);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(addButton.isSelected() && this.getPointerPosition(e.getX(), e.getY()) instanceof Vector2d){
            this.pointerPosition = this.getPointerPosition(e.getX(), e.getY());
            panel.repaint(1,0,this.freeSpace,panel.w * panel.cellSize,panel.h * panel.cellSize + this.freeSpace);
        }
    }
}
