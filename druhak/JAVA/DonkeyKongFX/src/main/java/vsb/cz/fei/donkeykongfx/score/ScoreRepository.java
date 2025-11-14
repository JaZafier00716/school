package vsb.cz.fei.donkeykongfx.score;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreRepository {

    public static void save(List<Score> scores) throws ScoreException {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("high-score.csv"))) { // lze strednikem oddelovat vice prikazu
            // Alternative:
            // BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("high-score2.csv")));
            for(Score score : scores){
                bw.write(String.format("%s;%d",  score.getNickName(), score.getScore()));
                bw.newLine();
            }
        }
        catch (IOException e) {
            throw new ScoreException("Something went wrong: Saving file did in fact not save the file.", e);
        }
    }

    public static List<Score> load() throws ScoreException {
        List<Score> scores = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader("high-score.csv"))) {
            String line = br.readLine();
            while (line != null) {
                String[] parts = line.split(";");
                if(parts.length != 2) {
                    throw new ScoreException("Something went wrong: Bad arguments BOZO");
                }

                try {
                    int points = Integer.parseInt(parts[1]);
                    scores.add(new Score(parts[0], points));
                } catch (NumberFormatException e) {
                    throw new  ScoreException("Something went wrong: Bad arguments BOZO", e);
                }



                line = br.readLine();
            }
        } catch (IOException e) {
            throw new ScoreException("Something went wrong: Loading file did in fact fail to load the file.", e);

        }

        return scores;
    }
}
