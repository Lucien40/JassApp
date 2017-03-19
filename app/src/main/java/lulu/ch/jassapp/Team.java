package lulu.ch.jassapp;

import java.util.ArrayList;

/**
 * Created by huber on 19-Mar-17.
 */

public class Team {

    String Name;
    ArrayList<Player> players;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    Team(String name, ArrayList<Player> players) {
        this.Name = name;
        this.players = players;
    }
}
