package lulu.ch.jassapp;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void distribute() {
        PowerMockito.mockStatic(Log.class);

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
        System.out.print(newGame.getPlayer(0).getHand().size() + "" + newGame.getPlayer(1).getHand().size() + "" + newGame.getPlayer(2).getHand().size() + "" + newGame.getPlayer(3).getHand().size());


    }


}