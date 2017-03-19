package lulu.ch.jassapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by huber on 19-Mar-17.
 */

class CardSet {

    HashMap<String, Card> cardMap = new HashMap<>();
    ArrayList<Card> cards = new ArrayList<>();
    String[] color = {"h", "d", "s", "c"};
    String[] name = {"a", "k", "q", "j", "1", "9", "8", "7", "6"};

    String leadColor;
    String trumpColor;
    int[] power = {9, 8, 7, 6, 5, 4, 3, 2, 1};
    int[] points = {11, 4, 3, 2, 10, 0, 0, 0, 0};
    int[] trumpPower = {29, 28, 27, 39, 26, 38, 25, 24, 23};
    int[] trumpPoint = {11, 4, 3, 20, 10, 14, 0, 0, 0};

    CardSet() {
        setCardMap();
    }

    public String getColor(int color) {
        return this.color[color];
    }


    private void setCardMap() {
        for (int i = 0; i < name.length; i++) {
            for (int j = 0; j < color.length; j++) {
                String cardName = name[i] + " of " + color[j];
                Card c = new Card(cardName);
                c.setColor(color[j]);
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
