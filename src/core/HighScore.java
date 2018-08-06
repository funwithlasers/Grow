package core;

import javax.swing.*;
import java.io.*;
import java.util.Collections;
import java.util.Scanner;

public class HighScore extends JPanel {
    private int minHighScore;
    private NameScore[] topPlayers = new NameScore[10];
    private String fileName = "highscore.txt";

    private class NameScore implements Comparable<NameScore>{
        public int score;
        public String name;


        @Override
        public int compareTo(NameScore o) {
            return score - o.score;
        }
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
        sort(topPlayers);
        try {
            saveScores();
        }   catch( IOException e) { System.out.println("IO ERROR"); }
    }

    void sort(NameScore arr[])
    {
        int n = arr.length;

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (arr[j].score < arr[min_idx].score)
                    min_idx = j;

            // Swap the found minimum element with the first
            // element
            int temp = arr[min_idx].score;
            arr[min_idx].score = arr[i].score;
            arr[i].score = temp;
        }
    }


}
