package main;

import java.util.ArrayList;

public class Cell {

    public Vector2d position;
    public AbstractMap map;
    public boolean alive;

    public Cell(AbstractMap map, Vector2d position, boolean alive){
        this.map = map;
        this.position = position;
        this.alive = alive;
    }

    public Cell(AbstractMap map){
        this.map = map;
        this.position = this.map.upperRight.random();
        this.alive = true;
    }

    @Override
    public String toString(){
        if(alive) return "#";
        else{
            return Integer.toString(this.aliveNeighboursCount());
        }
    }

    public ArrayList<Vector2d> getNeighbours(){
        return this.map.neighboursOf(this.position);
    }

    public int aliveNeighboursCount(){
        int result = 0;
        for(Vector2d position : this.getNeighbours()){
            if((this.map.toProcess.containsKey(position) && this.map.toProcess.get(position).alive)){
                result++;
            }
        }
        return result;
    }

    public void prepareToProcess(){
        for(Vector2d nPosition : this.getNeighbours()){
            if(this.map.toProcess.containsKey(nPosition) == false){
                this.map.toProcess.put(nPosition, new Cell(this.map, nPosition, false));
            }
        }

        if(this.map.toProcess.containsKey(this.position)){
            this.map.toProcess.remove(this.position);
            this.map.toProcess.put(this.position, this);
        }
        else{
            this.map.toProcess.put(this.position, this);
        }
    }

    public void process(){
        if(this.alive){
            if(this.map.rulesForAlive.contains(this.aliveNeighboursCount()) && !this.map.cells.containsKey(this.position)){
                this.map.cells.put(this.position, this);
            }
        }
        else{
            if(this.map.rulesForNotAlive.contains( this.aliveNeighboursCount()) && !this.map.cells.containsKey(this.position)){
                this.map.cells.put(this.position, new Cell(this.map, this.position, true));
            }
        }
    }

}
