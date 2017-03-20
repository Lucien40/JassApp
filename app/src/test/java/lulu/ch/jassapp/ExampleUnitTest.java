package lulu.ch.jassapp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void distribute() {

        ArrayList<String> playerNames1 = new ArrayList<>();
        playerNames1.add("Player1");
        playerNames1.add("Player3");
        ArrayList<String> playerNames2 = new ArrayList<>();
        playerNames2.add("Player2");
        playerNames2.add("Player4");
        String team1Name = "Team1";
        String team2Name = "Team2";
        Game newGame = new Game(playerNames1, team1Name, playerNames2, team2Name, Calendar.getInstance().getTime());

        newGame.randomDistribute();
        System.out.print(newGame.getPlayer(1).getHand().size());




    }
}