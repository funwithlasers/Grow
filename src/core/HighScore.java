package core;

import javax.swing.*;
import java.io.*;
import java.util.Scanner;

public class HighScore extends JPanel {
    private int minHighScore;
    private NameScore[] topPlayers = new NameScore[10];
    private String fileName = "highscore.txt";

    private class NameScore{
        public int score;
        public String name;
    }

    public HighScore() throws FileNotFoundException {
        int i = 1;
        File f = new File(fileName);
        Scanner sc = new Scanner(f);
        if(sc.hasNextInt()) minHighScore = sc.nextInt();
        topPlayers[0].score = minHighScore;
        if(sc.hasNext()) topPlayers[0].name = sc.next();
        while(sc.hasNextInt()){
            topPlayers[i].score = sc.nextInt();
            if(sc.hasNext()) {
                topPlayers[i].name = sc.next();
            }
        }
    }

    public void displayScores(){
        int a = 9;
        do {
            System.out.print(topPlayers[a].name +": ");
            System.out.println(topPlayers[a].score + " points");
        } while (a >= 0);
    }

    public void saveScores() throws IOException {
        BufferedWriter wr = new BufferedWriter(new FileWriter(fileName));
        int a = 0;
        do {
            wr.write(topPlayers[a].score);
            wr.newLine();
            wr.write(topPlayers[a].name);
            wr.newLine();
        } while(a <10);

        wr.close();
    }

    public void manageEndScore(int s){
        if(s > minHighScore) {
            Scanner sc = new Scanner(System.in);
            String temp = sc.next();
            topPlayers[0].name = temp;
            topPlayers[0].score = s;
        }
    }

}
