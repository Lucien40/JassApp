package lulu.ch.jassapp;

import java.util.Objects;

/**
 * Created by huber on 19-Mar-17.
 */

class Is {

    Game game;
    CardSet cardSet;
    Match match;
    Trick trick;

    Is(Game game) {
        this.game = game;
        cardSet = game.getCardSet();
        match = game.getCurrentMatch();
        trick = match.getCurrentTrick();
    }


    boolean keeping(Card card) {
        /**
         * A Keeping card is a card stronger than any card of the same color already present in the trick.
         */
        boolean isKeeping = true;
        for (Card c : cardSet.getCardsOfColor(card.getColor())) {
            if (card.getPower() < c.getPower()
                    && trick.getTrick().contains(c)) {
                isKeeping = false;
            }
        }
        return isKeeping;
    }

    boolean keeping(String card) {
        /**
         * A Keeping card is a card stronger than any card of the same color already present in the trick.
         */
        return keeping(cardSet.getCard(card));
    }

    boolean winning(Card card) {
        boolean isWinning = true;
        for (Card c : cardSet.getCardsOfColor(card.getColor())) {
            if (card.getPower() < c.getPower()
                    && (game.getPlayer(1).getPossibleCard().contains(c)
                    || game.getPlayer(3).getPossibleCard().contains(c)
                    || trick.getTrick().contains(c))) {
                isWinning = false;
            }
        }
        return isWinning;
    }

    boolean winning(String card) {
        return winning(cardSet.getCard(card));
    }

    boolean bock(Card card) {
        boolean isBock = true;
        for (Card c : cardSet.getCardsOfColor(card.getColor())) {
            if (card.getPower() <= c.getPower()
                    && (game.getPlayer(1).getPossibleCard().contains(c)
                    || game.getPlayer(3).getPossibleCard().contains(c)
                    || trick.getTrick().contains(c)
                    || game.getPlayer(2).getPossibleCard().contains(c))) {
                isBock = false;
            }
        }
        return isBock;
    }

    boolean bock(String card) {
        return bock(cardSet.getCard(card));
    }

    boolean bockToCome(Card card) {
        boolean isBock = true;
        for (Card c : cardSet.getCardsOfColor(card.getColor())) {
            if (card.getPower() <= c.getPower()
                    && (game.getPlayer(1).getPossibleCard().contains(c)
                    || game.getPlayer(3).getPossibleCard().contains(c)
                    || game.getPlayer(2).getPossibleCard().contains(c))) {
                isBock = false;
            }
        }
        return isBock;
    }

    boolean bockToCome(String card) {
        return bockToCome(cardSet.getCard(card));
    }

    static boolean stillInGame(Card card) {
        return !card.isPlayed();
    }

    boolean stillInGame(String card) {
        return stillInGame(cardSet.getCard(card));
    }

    boolean highestOfHand(Card card) {
        boolean isHighest = true;
        for (Card c : All.color(game.getPlayer(0).getHand(), card.getColor())) {
            if (card.getPower() <= c.getPower()) {
                isHighest = false;
            }
        }
        return isHighest;
    }

    boolean lowestOfHand(Card card) {
        boolean isLowest = false;
        for (Card c : All.color(game.getPlayer(0).getHand(), card.getColor())) {
            if (card.getPower() <= c.getPower()) {
                isLowest = true;
            }
        }
        return isLowest;
    }

    static boolean lowValue(Card card) {
        boolean isLowValue = false;
        if (card.getPoint() < 10) {
            isLowValue = true;
        }
        return isLowValue;
    }

    boolean leastValuableOfColorinHand(Card card) {
        boolean isLeastValuable = false;
        for (Card c : All.color(game.getPlayer(0).getHand(), card.getColor())) {
            if (card.getPoint() <= c.getPoint()) {
                isLeastValuable = true;
            }
        }
        return isLeastValuable;
    }

    boolean leastValuable(Card card) {
        boolean isLeastValuable = true;
        for (Card c : game.getPlayer(0).getHand()) {
            if (card.getPoint() >= c.getPoint()) {
                isLeastValuable = false;
            }
        }
        return isLeastValuable;
    }

    boolean ofGreaterRank(String color, Player player2, Card card) {
        boolean isOfGreaterRank = true;
        for (Card c : All.color(player2.getPossibleCard(), color)) {
            if (card.getPower() <= c.getPower()) {
                isOfGreaterRank = false;
            }
        }
        return isOfGreaterRank;
    }

    boolean mostValuableOfColorofHand(Card card) {

        boolean isMostValuable = false;
        for (Card c : All.color(game.getPlayer(0).getHand(), card.getColor())) {
            if (card.getPoint() <= c.getPoint()) {
                isMostValuable = true;
            }
        }
        return isMostValuable;

    }

    boolean mostValuable(Card card) {

        boolean isMostValuable = false;
        for (Card c : game.getPlayer(0).getHand()) {
            if (card.getPoint() <= c.getPoint()) {
                isMostValuable = true;
            }
        }
        return isMostValuable;

    }

    boolean passed() {
        return match.isPassed();
    }

    boolean playerDefourche(Card card, Player player) {
        boolean isPlayerDefourche = false;
        if (player.getDefourche().contains(card)) {
            isPlayerDefourche = true;
        }
        return isPlayerDefourche;
    }

    boolean trump(Card card) {
        return card.isTrump();
    }

    static boolean color(Card card, String color) {
        return Objects.equals(card.getColor(), color);
    }

    boolean card(Card card, Card c) {
        return Objects.equals(card, c);
    }
}
