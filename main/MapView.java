package main;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class MapView extends JPanel {

    public int w;
    public int h;
    public AbstractMap map;
    public int cellSize;
    public int freeSpace;
    public SimulationWindow simulationWindow;

    public MapView(SimulationWindow simulationWindow){
        this.simulationWindow = simulationWindow;
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.map = this.simulationWindow.map;
        this.freeSpace = this.simulationWindow.freeSpace;
        this.w = map.upperRight.x + 1;
        this.h = map.upperRight.y + 1;
        this.actualizeBounds();
//        this.cellSize = min((int) (this.simulationWindow.getWidth()), (int) (this.simulationWindow.getHeight())) / max(this.w , this.h );
//        this.setSize(this.cellSize * this.w, this.cellSize * this.h);
    }

    @Override
    public void paintComponent(Graphics g) {
        fillMap(g);
        paintAlive(g);
        drawAdditionalCells(g);
        //actualizeBounds();
    }

    public void fillMap(Graphics g){
        this.drawMap(g);
        this.drawGrid(g);
    }

    public void drawMap(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(0,this.freeSpace, (this.w)*this.cellSize, (this.h)*this.cellSize);
    }

    public void drawGrid(Graphics g){
        g.setColor(Color.BLACK);
        for(int i = 0; i < w; i++){
            for(int j = 0 ; j < h; j++){
                g.drawRect(i* this.cellSize, j* this.cellSize + this.freeSpace, this.cellSize, this.cellSize);
            }
        }
    }

    public void paintAlive(Graphics g){
        for (Cell cell : this.map.cells.values()) {
            g.setColor(Color.BLACK);
            g.fillRect(cell.position.x * this.cellSize, cell.position.y * this.cellSize + this.freeSpace, this.cellSize, this.cellSize);
        }
    }

    public void actualizeBounds(){
//        this.freeSpace = this.simulationWindow.freeSpace;
//        this.w = map.upperRight.x + 1;
//        this.h = map.upperRight.y + 1;
//        this.cellSize = min((this.simulationWindow.getWidth() - this.simulationWindow.leftMargin - 8) / this.w ,(this.simulationWindow.getHeight() - this.simulationWindow.upperMargin - 12) / this.h);
//        this.setSize(this.cellSize * this.w + this.simulationWindow.leftMargin * 2, this.cellSize * this.h + this.simulationWindow.upperMargin * 2);
        this.cellSize = this.simulationWindow.cellSize;
        this.setSize(this.cellSize * this.w + 16, this.cellSize * this.h + 40);
    }

    public void drawAdditionalCells(Graphics g){
        try {
            for (Vector2d cell : this.simulationWindow.additionalCells) {
                Vector2d position = cell.add(this.simulationWindow.pointerPosition);
                g.setColor(new Color(100, 150, 255));
                g.fillRect(position.x * this.cellSize, position.y * this.cellSize + this.freeSpace, this.cellSize, this.cellSize);
            }
        }
        catch (Exception e){
            System.out.println("fail");
        }
    }

}
