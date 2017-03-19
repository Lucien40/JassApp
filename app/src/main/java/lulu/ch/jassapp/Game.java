package lulu.ch.jassapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by huber on 18-Mar-17.
 */

class Game {

    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Match> matches = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private int team1GameScore = 0;
    private int team2GameScore = 0;
    private int lastTeam1Score = 0;
    private int lastTeam2Score = 0;
    private Player firstPlayer;
    private Player relativeFirstPlayer;
    private Player relativeSecondPlayer;
    private Player relativeThirdPlayer;
    private Player relativeFourthPlayer;
    private Match currentMatch;
    private CardSet cards;
    private String name;

    Game(ArrayList<String> team1PlayerNames, String team1Name, ArrayList<String> team2PlayerNames, String team2Name, Date name) {

        //Create Team 1
        Player team1Player1 = new Player(team1PlayerNames.get(0), false);
        Player team1Player2 = new Player(team1PlayerNames.get(1), false);

        ArrayList<Player> team1players = new ArrayList<>();
        team1players.add(team1Player1);
        team1players.add(team1Player2);
        Team team1 = new Team(team1Name, team1players);

        //Create Team 2
        Player team2Player1 = new Player(team2PlayerNames.get(0), false);
        Player team2Player2 = new Player(team2PlayerNames.get(1), false);

        ArrayList<Player> team2players = new ArrayList<>();
        team2players.add(team2Player1);
        team2players.add(team2Player2);
        Team team2 = new Team(team2Name, team2players);

        players.add(0, team1Player1);
        players.add(1, team2Player1);
        players.add(2, team1Player2);
        players.add(3, team2Player2);

        team1Player1.setOrder(players.indexOf(team1Player1));
        team2Player1.setOrder(players.indexOf(team1Player2));
        team1Player2.setOrder(players.indexOf(team2Player1));
        team2Player2.setOrder(players.indexOf(team2Player2));

        teams.add(team1);
        teams.add(team2);

        //Set Name
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        this.name = df.format(name);
        /*Log.i("New Game Started", " Name " + name
                + "\n   Team 1: " + team1.Name
                + "\n    Players:"
                + "\n      " + team1.getPlayers().get(0).getName() + " order: " + team1.getPlayers().get(0).getOrder()
                + "\n      " + team1.getPlayers().get(1).getName() + " order: " + team1.getPlayers().get(1).getOrder()
                + "\n   Team 2: " + team2.Name
                + "\n    Players:"
                + "\n      " + team2.getPlayers().get(0).getName() + " order: " + team2.getPlayers().get(0).getOrder()
                + "\n      " + team2.getPlayers().get(1).getName() + " order: " + team2.getPlayers().get(1).getOrder());

            */
        cards = new CardSet();
    }

    CardSet getCardSet() {
        return cards;
    }

    Player getPlayer(int number) {
        switch (number) {
            case 0:
                return relativeFirstPlayer;
            case 1:
                return relativeSecondPlayer;
            case 2:
                return relativeThirdPlayer;
            case 3:
                return relativeFourthPlayer;
            default:
                return relativeFirstPlayer;
        }
    }

    void randomDistribute() {
        //Comment
        //This is a change in Gui

    }

    private void updatePlayers(Player relativeFirstPlayer) {
        this.relativeFirstPlayer = relativeFirstPlayer;
        relativeSecondPlayer = players.get((relativeFirstPlayer.getOrder() + 1) % 4);
        relativeThirdPlayer = players.get((relativeFirstPlayer.getOrder() + 2) % 4);
        relativeFourthPlayer = players.get((relativeFirstPlayer.getOrder() + 3) % 4);
    }


    public void startNewMatch() throws Exception {
        if (firstPlayer == null) {
            throw new Exception("No first player designated");
        } else {
            cards.reSetCardMap();
            for (Player player : players) {
                firstPlayer.setDealer(true);
                if (player.getOrder() == ((firstPlayer.getOrder() + 1)) % 4) {
                    firstPlayer = player;
                }
            }
            currentMatch = new Match(firstPlayer, players, teams, cards);
            matches.add(currentMatch);
        }
    }

    public void addToGameScore(int score, int team) {
        if (team == 0) {
            this.team1GameScore += score;
        } else {
            this.team2GameScore += score;
        }
    }

    public Player getPlayerFromOrder(int order) {
        order = order % 4;
        Player player = null;
        for (Player iPlayer : players) {
            if (iPlayer.getOrder() == order) {
                player = iPlayer;
            }
        }
        return player;
    }

    public Player getDealer() {
        Player player = null;
        for (Player iPlayer : players) {
            if (iPlayer.isDealer()) {
                player = iPlayer;
            }
        }
        return player;
    }

    public ArrayList<Player> getAnalysablePlayers() {
        ArrayList player = new ArrayList();
        for (Player iplayer : players) {
            if (iplayer.isAnalysable()) {
                player.add(iplayer);
            }
        }
        return player;
    }

    public void setFirstPlayer(Player player) {
        firstPlayer = player;
        getPlayerFromOrder((firstPlayer.getOrder() + 3) % 4).setDealer(true);
        //Start a new Match
        Match startMatch = new Match(firstPlayer, players, teams, cards);
        currentMatch = startMatch;

    }

    public boolean playCard(String card) throws Exception {
        boolean hasBeenPlayed = currentMatch.playCard(card);
        setTeam1GameScore(lastTeam1Score + currentMatch.getMatchScoreTeam1());
        setTeam2GameScore(lastTeam2Score + currentMatch.getMatchScoreTeam2());
        if (currentMatch.isDone()) {
            lastTeam1Score = team1GameScore;
            lastTeam2Score = team2GameScore;
            startNewMatch();
        }
        updatePlayers(currentMatch.getNextPlayer());
        return hasBeenPlayed;
    }

    public boolean distribute(Player player, Card card) {
        return currentMatch.distribute(player, card);
    }

    public void cancelDistribute() {
        currentMatch.cancelDistribute();
    }


    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public String getName() {
        return name;
    }

    public Match getCurrentMatch() {
        return currentMatch;
    }

    public int getTeam2GameScore() {
        return team2GameScore;
    }

    public int getTeam1GameScore() {
        return team1GameScore;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setTeam1GameScore(int team1GameScore) {
        this.team1GameScore = team1GameScore;
    }

    public void setTeam2GameScore(int team2GameScore) {
        this.team2GameScore = team2GameScore;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setCurrentMatch(Match currentMatch) {
        this.currentMatch = currentMatch;
    }
}
