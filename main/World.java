package main;

import java.util.concurrent.TimeUnit;

public class World {

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
}
