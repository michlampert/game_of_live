package main;

import java.util.ArrayList;

public class TorusMap extends AbstractMap{

    public TorusMap(Vector2d upperRight){
        super(upperRight);
    }

    public TorusMap(Vector2d upperRight, ArrayList<Integer> rulesForAlive,  ArrayList<Integer> rulesForNotAlive){
        super(upperRight, rulesForAlive, rulesForNotAlive);
    }

    @Override
    public ArrayList<Vector2d> neighboursOf(Vector2d position){
        ArrayList<Vector2d> result = new ArrayList<>();
        for(int i : new int[]{-1, 0, 1}){
            for(int j : new int[]{-1, 0, 1}){
                if(i == 0 && j == 0) continue;
                result.add(position.add(new Vector2d(i,j)).mod(this.upperRight));
            }
        }
        return result;
    }

}
