package lulu.ch.jassapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by huber on 19-Mar-17.
 */

class CardSet {

    private HashMap<String, Card> cardMap = new HashMap<>();
    private ArrayList<Card> cards = new ArrayList<>();
    private static final String[] color = {"h", "d", "s", "c"};
    private static final String[] name = {"a", "k", "q", "j", "1", "9", "8", "7", "6"};

    private String leadColor;
    private String trumpColor;
    private static final int[] power = {9, 8, 7, 6, 5, 4, 3, 2, 1};
    private static final int[] points = {11, 4, 3, 2, 10, 0, 0, 0, 0};
    private static final int[] trumpPower = {29, 28, 27, 39, 26, 38, 25, 24, 23};
    private static final int[] trumpPoint = {11, 4, 3, 20, 10, 14, 0, 0, 0};

    CardSet() {
        setCardMap();
    }

    private void setCardMap() {
        for (int i = 0; i < name.length; i++) {
            for (String aColor : color) {
                String cardName = name[i] + " of " + aColor;
                Card c = new Card(cardName);
                c.setColor(aColor);
                c.setPoint(points[i]);
                c.setPower(power[i]);
                c.setName(name[i]);
                c.setPlayed(false);
                c.setLead(false);
                c.setTrump(false);
                cards.add(c);
                cardMap.put(cardName, c);
            }
        }
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void reSetCardMap() {

        cardMap.clear();
        cards.clear();
        setCardMap();
    }

    public Card getCard(String name) {
        return cardMap.get(name);
    }

    public void setLeadColor(String color) {
        Log.i("setting", "this card is lead " + color);
        leadColor = color;
        for (String key : cardMap.keySet()) {
            Card card = cardMap.get(key);
            if (card.getColor() == color) {
                card.setLead(true);
                card.setLegal(true);
            } else if (card.isTrump()) {
                card.setLegal(true);
                card.setLead(false);
            } else {
                card.setLead(false);
                card.setLegal(false);
            }
        }
    }

    public void setTrumpColor(String color) {
        trumpColor = color;
        for (int i = 0; i < name.length; i++) {
            String cardName = name[i] + " of " + color;

            Card c = this.getCard(cardName);
            if (name[i] == "j") {
                c.setBourg();
            }
            c.setPower(trumpPower[i]);
            c.setPoint(trumpPoint[i]);
            cardMap.put(cardName, c);
        }
    }

    public ArrayList<Card> getCardsOfColor(String color) {
        ArrayList<Card> colorCards = new ArrayList<>();
        for (Card card : cards) {
            if (card.getColor() == color) {
                colorCards.add(card);
            }
        }
        return colorCards;
    }

    public ArrayList<Card> getNonPlayedCardsOfColor(String color) {
        ArrayList<Card> colorCards = new ArrayList<>();
        for (Card card : cards) {
            if (card.getColor() == color && !card.isPlayed()) {
                colorCards.add(card);
            }
        }
        return colorCards;
    }

    public String getLeadColor() {
        return leadColor;
    }

    public String getTrumpColor() {
        return trumpColor;
    }

    public HashMap getCardMap() {
        return cardMap;
    }


}
