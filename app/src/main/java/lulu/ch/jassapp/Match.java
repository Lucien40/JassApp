package lulu.ch.jassapp;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by huber on 19-Mar-17.
 */

public class Match {

    private ArrayList<Trick> tricks = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private Team team1;
    private Team team2;
    private boolean[] hasHeart = new boolean[4];

    public boolean isPassed() {
        return Passed;
    }

    public void setPassed(boolean passed) {
        Passed = passed;
    }

    private boolean Passed = false;
    private int h;
    private int hp;
    private int cp;
    private int sp;
    private int dp;
    private int c;
    private int s;
    private int d;
    private boolean[] hasClubs = new boolean[4];
    private boolean[] hasSpades = new boolean[4];
    private boolean[] hasDiamonds = new boolean[4];
    private int matchScoreTeam1 = 0;
    private int matchScoreTeam2 = 0;
    private Boolean isDone = false;
    private Trick currentTrick;
    private int nextPlayerOrder;
    private Player mNextPlayer;
    private CardSet matchCardSet;
    private String Trump;


    Match(Player firstPlayer, ArrayList<Player> players, ArrayList<Team> teams, CardSet cardSet) {
        matchCardSet = cardSet;
        this.setNextPlayer(firstPlayer);
        this.players = players;
        for (Player player : players) {
            player.newMatch(matchCardSet);
        }
        this.team1 = teams.get(0);
        this.team2 = teams.get(1);
        Log.i("New Match Started", "Next Player.\n  " + mNextPlayer.getName() + " order: " + mNextPlayer.getOrder());
    }

    public boolean playCard(String card) {
        Card playedCard = matchCardSet.getCard(card);
        if (playedCard.isPlayed()) {
            return true;
        } else {
            update(playedCard);
            return false;
        }
    }

    public Boolean isDone() {
        return isDone;
    }

    private void update(Card playedCard) {
        if (currentTrick == null) {
            currentTrick = new Trick(matchCardSet, nextPlayerOrder);
            Log.i("Current Match", "First Trick started \n" +
                    "First Player order:" + nextPlayerOrder);
        }
        playedCard.setPlayed(true);
        currentTrick.playCard(playedCard);
        for (Player player : players) {
            player.setCardSet(matchCardSet);
        }
        mNextPlayer.playCard(playedCard);
        for (Player player : players) {
            player.update();
        }


        updatePossibleCards();


        Log.i("Current Match", "Card Played:\n  " + playedCard.getName() + " of " + playedCard.getColor()
                + "\n  Power:" + playedCard.getPower() + " Color: " + playedCard.getColor() + "\n By:" + mNextPlayer.getOrder());
        setNextPlayerOrder(currentTrick.getNextPlayer());
        Log.i("Current Match", "Next Player:\n  " + mNextPlayer.getName() + " order: " + mNextPlayer.getOrder());
        if (currentTrick.isDone()) {
            tricks.add(currentTrick);
            if (team1.getPlayers().get(0).getOrder() == currentTrick.getWinningPlayerOrder() | team1.getPlayers().get(1).getOrder() == currentTrick.getWinningPlayerOrder()) {
                matchScoreTeam1 += currentTrick.getTrickPoints();
                Log.i("Team1 won:", "matchScore: " + matchScoreTeam1);
            } else if (team2.getPlayers().get(0).getOrder() == currentTrick.getWinningPlayerOrder() | team2.getPlayers().get(1).getOrder() == currentTrick.getWinningPlayerOrder()) {
                matchScoreTeam2 += currentTrick.getTrickPoints();
                Log.i("Team2 won:", "matchScore: " + matchScoreTeam2);
            }
            if (tricks.size() < 9) {
                currentTrick = new Trick(matchCardSet, nextPlayerOrder);
                Log.i("Current Match", tricks.size() + "th Trick started \n" +
                        "First Player order:" + nextPlayerOrder);
            } else {
                isDone = true;
            }
        }

    }

    private void updatePossibleCards() {
        for (Card card : matchCardSet.getCards()) {
            int N = 4;
            if (!card.isPlayed()) {
                for (Player player : players) {
                    if (!player.getPossibleCard().contains(card)) {
                        N--;
                    }
                }
                if (N <= 1) {
                    for (Player player : players) {
                        if (player.getPossibleCard().contains(card) && !player.getKnownCards().contains(card)) {
                            player.addToKnownCards(card);
                            player.update();
                            updatePossibleCards();
                        }
                    }
                }
            }
        }

    }

    public boolean distribute(Player player, Card card) {
        if (!card.isDistributed) {
            player.addCardToHand(card);
            card.isDistributed = true;
            return false;
        } else {
            return true;
        }
    }

    public void cancelDistribute() {
        for (Player player : players) {
            player.clearHand();
        }
        for (Card card : matchCardSet.cards) {
            card.isDistributed = false;
        }
    }

    public void setTrump(String color) {
        matchCardSet.setTrumpColor(color);
        Trump = color;
    }

    public String getTrumpColor() {
        return Trump;
    }

    public int getNextPlayerOrder() {
        return nextPlayerOrder;
    }

    public void setNextPlayerOrder(int nextPlayerOrder) {
        for (Player player : players) {
            if (player.getOrder() == nextPlayerOrder) {
                setNextPlayer(player);
            }
        }
    }


    public Player getNextPlayer() {
        return mNextPlayer;
    }

    public void setNextPlayer(Player pPlayer) {
        mNextPlayer = pPlayer;
        nextPlayerOrder = pPlayer.getOrder();
    }


    public ArrayList<Trick> getTricks() {
        return tricks;
    }

    public void setTricks(ArrayList<Trick> tricks) {
        this.tricks = tricks;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public int getMatchScoreTeam1() {
        return matchScoreTeam1;
    }

    public void setMatchScoreTeam1(int matchScoreTeam1) {
        this.matchScoreTeam1 = matchScoreTeam1;
    }

    public int getMatchScoreTeam2() {
        return matchScoreTeam2;
    }

    public void setMatchScoreTeam2(int matchScoreTeam2) {
        this.matchScoreTeam2 = matchScoreTeam2;
    }

    public Trick getCurrentTrick() {
        return currentTrick;
    }

    public void setCurrentTrick(Trick currentTrick) {
        this.currentTrick = currentTrick;
    }

    public Player getmNextPlayer() {
        return mNextPlayer;
    }

    public void setmNextPlayer(Player mNextPlayer) {
        this.mNextPlayer = mNextPlayer;
    }

    public CardSet getMatchCardSet() {
        return matchCardSet;
    }

    public void setMatchCardSet(CardSet matchCardSet) {
        this.matchCardSet = matchCardSet;
    }

}
