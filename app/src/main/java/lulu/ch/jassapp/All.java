package lulu.ch.jassapp;

import java.util.ArrayList;

/**
 * Created by huber on 19-Mar-17.
 */

class All {

    public Is getIs() {
        return is;
    }

    public void setIs(Is is) {
        this.is = is;
    }

    Is is;
    CardSet cardSet;

    All(Game game) {
        is = new Is(game);
        cardSet = game.getCardSet();
    }

    ArrayList<Card> winning(ArrayList<Card> cards) {
        ArrayList<Card> allWinning = new ArrayList<>();
        for (Card card : cards) {
            if (is.winning(card)) {
                allWinning.add(card);
            }
        }
        return allWinning;
    }

    ArrayList<Card> bock(ArrayList<Card> cards) {
        ArrayList<Card> allBockCard = new ArrayList<>();
        for (Card card : cards) {
            if (is.bock(card)) {
                allBockCard.add(card);
            }
        }
        return allBockCard;
    }

    ArrayList<Card> nonBockToComeOption(ArrayList<Card> cards) {
        ArrayList<Card> allBockToCome = new ArrayList<>();
        for (Card card : cards) {
            if (!is.bockToCome(card)) {
                allBockToCome.add(card);
            }
        }
        if (allBockToCome.size() == 0) {
            return cards;
        }
        return allBockToCome;
    }

    ArrayList<Card> nonBockToCome(ArrayList<Card> cards) {
        ArrayList<Card> allBockToCome = new ArrayList<>();
        for (Card card : cards) {
            if (!is.bockToCome(card)) {
                allBockToCome.add(card);
            }
        }
        return allBockToCome;
    }

    ArrayList<Card> bockToCome(ArrayList<Card> cards) {
        ArrayList<Card> allBockToCome = new ArrayList<>();
        for (Card card : cards) {
            if (is.bockToCome(card)) {
                allBockToCome.add(card);
            }
        }
        return allBockToCome;
    }

    ArrayList<Card> nonBockOption(ArrayList<Card> cards) {
        ArrayList<Card> allNonBock = new ArrayList<>();
        for (Card card : cards) {
            if (!is.bock(card)) {
                allNonBock.add(card);
            }
        }

        if (allNonBock.size() == 0) {
            return cards;
        }
        return allNonBock;
    }

    ArrayList<Card> nonBock(ArrayList<Card> cards) {
        ArrayList<Card> allNonBock = new ArrayList<>();
        for (Card card : cards) {
            if (!is.bock(card)) {
                allNonBock.add(card);
            }
        }
        return allNonBock;
    }

    static ArrayList<Card> stillInGame(ArrayList<Card> cards) {
        ArrayList<Card> allStillInGame = new ArrayList<>();
        for (Card card : cards) {
            if (Is.stillInGame(card)) {
                allStillInGame.add(card);
            }
        }
        return allStillInGame;
    }

    ArrayList<Card> highest(ArrayList<Card> cards) {
        ArrayList<Card> allHighest = new ArrayList<>();
        for (Card card : cards) {
            if (is.highestOfHand(card)) {
                allHighest.add(card);
            }
        }
        return allHighest;
    }

    ArrayList<Card> lowest(ArrayList<Card> cards) {
        ArrayList<Card> allLowest = new ArrayList<>();
        for (Card card : cards) {
            if (is.lowestOfHand(card)) {
                allLowest.add(card);
            }
        }
        return allLowest;
    }

    static ArrayList<Card> lowValue(ArrayList<Card> cards) {
        ArrayList<Card> allLowValue = new ArrayList<>();
        for (Card card : cards) {
            if (Is.lowValue(card)) {
                allLowValue.add(card);
            }
        }
        return allLowValue;
    }

    ArrayList<Card> leastValuable(ArrayList<Card> cards) {
        ArrayList<Card> allLeastValuable = new ArrayList<>();
        for (Card card : cards) {
            if (is.leastValuable(card)) {
                allLeastValuable.add(card);
            }
        }
        return allLeastValuable;
    }

    ArrayList<Card> ofGreaterRank(String color, ArrayList<Card> cards, Player player2) {
        ArrayList<Card> allOfGreaterRank = new ArrayList<>();
        for (Card card : cards) {
            if (is.ofGreaterRank(color, player2, card)) {
                allOfGreaterRank.add(card);
            }
        }
        return allOfGreaterRank;
    }


    ArrayList<Card> leastValuableOfColor(ArrayList<Card> cards) {
        ArrayList<Card> allLeastValuableOfColor = new ArrayList<>();
        for (Card card : cards) {
            if (is.leastValuableOfColorinHand(card)) {
                allLeastValuableOfColor.add(card);
            }
        }
        return allLeastValuableOfColor;
    }

    ArrayList<Card> mostValuable(ArrayList<Card> cards) {
        ArrayList<Card> allMostValuable = new ArrayList<>();
        for (Card card : cards) {
            if (is.mostValuable(card)) {
                allMostValuable.add(card);
            }
        }
        return allMostValuable;
    }

    ArrayList<Card> mostValuableOfColor(ArrayList<Card> cards) {
        ArrayList<Card> allMostValuableOfColor = new ArrayList<>();
        for (Card card : cards) {
            if (is.mostValuableOfColorofHand(card)) {
                allMostValuableOfColor.add(card);
            }
        }
        return allMostValuableOfColor;
    }

    ArrayList<Card> keeping(ArrayList<Card> cards) {
        ArrayList<Card> allKeeping = new ArrayList<>();
        for (Card card : cards) {
            if (is.keeping(card)) {
                allKeeping.add(card);
            }
        }
        return allKeeping;
    }

    ArrayList<Card> trump(ArrayList<Card> cards) {

        ArrayList<Card> allTrump = new ArrayList<>();
        for (Card card : cards) {
            if (is.trump(card)) {
                allTrump.add(card);
            }
        }
        return allTrump;


    }

    ArrayList<Card> defourche(ArrayList<Card> cards, Player player) {

        ArrayList<Card> allDefourche = new ArrayList<>();
        for (Card card : cards) {
            if (is.playerDefourche(card, player)) {
                allDefourche.add(card);
            }
        }
        return allDefourche;


    }

    static ArrayList<Card> color(ArrayList<Card> cards, String color) {

        ArrayList<Card> allColor = new ArrayList<>();
        for (Card card : cards) {
            if (Is.color(card, color)) {
                allColor.add(card);
            }
        }
        return allColor;


    }

    ArrayList<Card> nonTrump(ArrayList<Card> cards) {

        ArrayList<Card> allTrump = new ArrayList<>();
        for (Card card : cards) {
            if (!is.trump(card)) {
                allTrump.add(card);
            }
        }
        return allTrump;


    }

    ArrayList<Card> non(ArrayList<Card> cards, Card c) {
        ArrayList<Card> allNon = new ArrayList<>();
        for (Card card : cards) {
            if (!is.card(card, c)) {
                allNon.add(card);
            }
        }
        return allNon;

    }

    ArrayList<Card> non(ArrayList<Card> cards, ArrayList<Card> notCard) {
        ArrayList<Card> allNon = new ArrayList<>();
        for (Card card : cards) {
            if (!notCard.contains(cards)) {
                allNon.add(card);
            }
        }
        return allNon;

    }

    ArrayList<Card> card(ArrayList<Card> cards, Card c) {
        ArrayList<Card> allCard = new ArrayList<>();
        for (Card card : cards) {
            if (!is.card(card, c)) {
                allCard.add(card);
            }
        }
        return allCard;

    }

    ArrayList<Card> card(ArrayList<Card> cards, String c) {
        ArrayList<Card> allCard = new ArrayList<>();
        for (Card card : cards) {
            if (!is.card(card, cardSet.getCard(c))) {
                allCard.add(card);
            }
        }
        return allCard;

    }

    ArrayList<Card> non(String c, ArrayList<Card> cards) {
        ArrayList<Card> allNon = new ArrayList<>();
        for (Card card : cards) {
            if (!is.card(card, cardSet.getCard(c))) {
                allNon.add(card);
            }
        }
        return allNon;


    }

}
