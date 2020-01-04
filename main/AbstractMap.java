package main;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public abstract class AbstractMap implements IWorldMap{

    public Vector2d lowerLeft = new Vector2d(0,0);
    public Vector2d upperRight;

    public HashMap<Vector2d, Cell> cells = new HashMap<>();
    public HashMap<Vector2d, Cell> toProcess = new HashMap<>();

    public ArrayList<Integer> rulesForAlive = new ArrayList<>();
    public ArrayList<Integer> rulesForNotAlive = new ArrayList<>();

    public AbstractMap(Vector2d upperRight){
        this.upperRight = upperRight;
        this.rulesForAlive.add(2);
        this.rulesForAlive.add(3);
        this.rulesForNotAlive.add(3);
    }

    public AbstractMap(Vector2d upperRight, ArrayList<Integer> rulesForAlive,  ArrayList<Integer> rulesForNotAlive){
        this.upperRight = upperRight;
        this.rulesForAlive = rulesForAlive;
        this.rulesForNotAlive = rulesForNotAlive;
    }

    public void nextGeneration(){
        for(Cell cell : this.cells.values()){
            cell.prepareToProcess();
        }
        this.cells.clear();
        for(Cell cell : this.toProcess.values()){
            cell.process();
        }
        this.toProcess.clear();

    }

    public void addCell(Vector2d position){
        if(!this.cells.containsKey(position)) this.cells.put(position, new Cell(this, position, true));
    }

    public void addCells(Vector2d pointer, ArrayList<Vector2d> positions){
        for(Vector2d position : positions){
            this.addCell(position.add(pointer));
        }
    }

    public void removeCell(Vector2d position){
        if(this.cells.containsKey(position)) this.cells.remove(position);
    }

    public void clear(){
        this.cells = new HashMap<>();
    }

    public void flipCell(Vector2d position){
        if(!this.cells.containsKey(position)){
            this.cells.put(position, new Cell(this, position, true));
        }
        else{
            this.cells.remove(position);
        }
    }

    @Override
    public String toString(){
        MapVisualizer mp = new MapVisualizer(this);
        return mp.draw(this.lowerLeft, this.upperRight);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if(this.cells.containsKey(position)) return true;
        return false;
    }

    @Override
    public Object objectAt(Vector2d position) {
        if(isOccupied(position)) return this.cells.get(position);
        return null;
    }

    public TorusMap mapWithFigure(Vector2d position, Figure figure){
        TorusMap merged = new TorusMap(this.upperRight);

        for(Cell cell : this.cells.values()){
            merged.cells.put(cell.position, new Cell(merged, cell.position, true));
        }

        if(figure.upperRight.precedes(this.upperRight)){
            position = position.upperRight(this.lowerLeft);
            position = position.lowerLeft(this.upperRight.subtract(figure.upperRight));

            for(Cell cell : figure.cells.values()){
                merged.cells.put(cell.position.add(position), new Cell(merged, cell.position.add(position), true));
            }
        }
        return merged;
    }

    public ArrayList<Vector2d> neighboursOf(Vector2d position) {
        return new ArrayList<>();
    }

    public static Vector2d calculateUpperRight(ArrayList<Vector2d> positions){
        Vector2d upperRight = null;
        for(Vector2d position : positions){
            if(!(upperRight instanceof Vector2d)){
                upperRight = upperRight.upperRight(position);
            }
        }
        return upperRight;
    }

    public static Vector2d calculateLowerLeft(ArrayList<Vector2d> positions){
        Vector2d lowerLeft = null;
        for(Vector2d position : positions){
            if(!(lowerLeft instanceof Vector2d)){
                lowerLeft = position;
            }
            else{
                lowerLeft = lowerLeft.lowerLeft(position);
            }
        }
        return lowerLeft;
    }

    public static ArrayList<Vector2d> repairedPositions(ArrayList<Vector2d> positions){
        ArrayList<Vector2d> result = new ArrayList<>();
        Vector2d lowerLeft = AbstractMap.calculateLowerLeft(positions);
        for(Vector2d position : positions){
            result.add(position.subtract(lowerLeft));
        }
        return result;
    }

    public void saveAsJSON(String name){
        JSONParser parser = new JSONParser();
        JSONObject all = new JSONObject();

        try (Reader reader = new FileReader("src/figures.json")) {
            all = (JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        JSONArray figure = new JSONArray();
        ArrayList<Vector2d> positions = new ArrayList<>();

        for(Cell cell : this.cells.values()) {
            positions.add(cell.position);
        }
        for(Vector2d position : AbstractMap.repairedPositions(positions)) {
            JSONArray tmp = new JSONArray();
            tmp.add(position.x);
            tmp.add(position.y);
            figure.add(tmp);
        }

        all.put(name, figure);

        try (FileWriter file = new FileWriter("src/figures.json")) {
            file.write(all.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
