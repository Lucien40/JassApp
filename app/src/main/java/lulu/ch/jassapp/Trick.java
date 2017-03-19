package lulu.ch.jassapp;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by huber on 19-Mar-17.
 */

public class Trick {

    private ArrayList<Card> trick = new ArrayList<>(4);
    private int trickPoints = 0;
    private int cardsPlayed = 0;
    private String mInitColor;
    private Boolean isDone = false;
    private int[] players = new int[4];
    private int nextPlayer;
    private int mInitPlayer;
    private Analysis analysis = new Analysis();
    private CardSet cardSet;
    private int winningPlayerOrder;
    private int winningCardIndex;

    Trick(CardSet mcardSet, int initPlayerOrder) {
        this.mInitPlayer = initPlayerOrder;
        this.cardSet = mcardSet;
        nextPlayer = mInitPlayer;
        this.mInitColor = "";
        Log.i("New trick started", "First Player Order: " + mInitPlayer);
    }


    public Boolean isDone() {
        return isDone;
    }

    public void playCard(Card card) {
        trick.add(card);
        int point = card.getPoint();
        card.setPlayed(true);
        trickPoints += point;
        players[cardsPlayed] = nextPlayer;
        cardsPlayed++;
        nextPlayer = ((nextPlayer + 1) % 4);

        if (trick.size() == 1) {
            mInitColor = card.getColor();
            Log.i("Set lead color", "this one: " + mInitColor);
            cardSet.setLeadColor(mInitColor);
            Log.i("Check:", "this color: " + cardSet.getLeadColor());
        } else if (trick.size() == 4) {
            winningCardIndex = analysis.getBestCardIndex(trick);
            //cardIndex 0-3, mInitPlayer 0-3, thus c + m < 6
            winningPlayerOrder = players[winningCardIndex];
            nextPlayer = winningPlayerOrder;
            isDone = true;
            Log.i("Trick WinningPlayer", "" + winningPlayerOrder + "and the starting player" + mInitPlayer + "Points: " + trickPoints);
        }

    }

    public int getNextPlayer() {
        return nextPlayer;
    }

    public int getCardsPlayed() {
        return cardsPlayed;
    }

    public int getTrickPoints() {
        return trickPoints;
    }

    public int getInitPlayer() {
        return mInitPlayer;
    }

    public ArrayList<Card> getTrick() {
        return trick;
    }

    public int getWinningPlayerOrder() {
        return winningPlayerOrder;
    }


}
