package main;

import java.util.concurrent.TimeUnit;

public class World {

    public static void main(String[] args) throws InterruptedException {

        TorusMap map = new TorusMap(new Vector2d(10, 10));

//        for(int i = 0; i<=10; i++){
//            map.addCell(new Vector2d(5 ,i));
//        }
//        map.addCell(new Vector2d(1,1));
//        map.addCell(new Vector2d(1,2));
//        map.addCell(new Vector2d(2,1));
//        map.addCell(new Vector2d(2,2));

//        map.addCell(new Vector2d(2,1));
//        map.addCell(new Vector2d(2,2));
//        map.addCell(new Vector2d(2,3));

        map.addCell(new Vector2d(0,9));
        map.addCell(new Vector2d(1,8));
        map.addCell(new Vector2d(2,8));
        map.addCell(new Vector2d(2,9));
        map.addCell(new Vector2d(2,10));

        System.out.print(map);
        while(!map.cells.isEmpty()){
            map.nextGeneration();
            TimeUnit.MILLISECONDS.sleep(1000);
            System.out.print(map);
            //map.toProcess.clear();
        }
    }
}
