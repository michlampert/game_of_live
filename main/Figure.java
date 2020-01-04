package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

public class Figure extends AbstractMap{

    public Figure(){
        super(new Vector2d(0,0));
    }

    public Figure(Vector2d[] positions){
        super(new Vector2d(0,0));
        for(Vector2d position : positions){
            this.upperRight = this.upperRight.upperRight(position);
            this.lowerLeft = this.lowerLeft.lowerLeft(position);
        }

        for(Vector2d position : positions){
            this.addCell(position.subtract(this.lowerLeft));
        }
        this.upperRight = this.upperRight.subtract(this.lowerLeft);
        this.lowerLeft = new Vector2d(0,0);
    }

    public static ArrayList<Vector2d> JSONToArray(String name){
        ArrayList<Vector2d> result = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try (Reader reader = new FileReader("src/figures.json")) {
            jsonObject = (JSONObject) parser.parse(reader);

            JSONArray vArray = (JSONArray) jsonObject.get(name);
            Iterator<JSONArray> iterator = vArray.iterator();
            while (iterator.hasNext()) {
                JSONArray position = iterator.next();
                long x = (long) position.get(0);
                long y = (long) position.get(1);
                result.add(new Vector2d((int) x,(int) y));
            }

        } catch (IOException | org.json.simple.parser.ParseException e) {
            System.out.println("Exception caught during loading settings.");
            e.printStackTrace();
        }

        return result;
    }


}
