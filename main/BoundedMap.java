package main;

import java.util.ArrayList;

public class BoundedMap extends AbstractMap{

    public BoundedMap(Vector2d upperRight){
        super(upperRight);
    }

    public BoundedMap(Vector2d upperRight, ArrayList<Integer> rulesForAlive,  ArrayList<Integer> rulesForNotAlive){
        super(upperRight, rulesForAlive, rulesForNotAlive);
    }

    @Override
    public ArrayList<Vector2d> neighboursOf(Vector2d position){
        ArrayList<Vector2d> result = new ArrayList<>();
        for(int i : new int[]{-1, 0, 1}){
            for(int j : new int[]{-1, 0, 1}){
                if(i == 0 && j == 0) continue;
                if(position.add(new Vector2d(i,j)).precedes(this.upperRight) && position.add(new Vector2d(i,j)).follows(this.lowerLeft)){
                    result.add(position.add(new Vector2d(i,j)).mod(this.upperRight));
                }
            }
        }
        return result;
    }
}
