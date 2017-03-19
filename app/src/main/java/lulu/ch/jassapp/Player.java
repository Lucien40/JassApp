package lulu.ch.jassapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by huber on 19-Mar-17.
 */

public class Player {


    private boolean isAnalysable;

    public boolean isFourcheDef() {
        return isFourcheDef;
    }

    public void setFourcheDef(boolean fourcheDef) {
        isFourcheDef = fourcheDef;
    }

    private boolean isFourcheDef = false;
    private boolean hastrump = true;//added true
    private boolean isDealer;
    private boolean hasHearts = true;
    private boolean hasClubs = true;
    private boolean hasSpades = true;
    private boolean hasDiamonds = true;
    private int cardsLeft = 9;

    private String fourcheColor;
    private String name;
    private CardSet cardSet;
    private ArrayList<Card> possibleCard = new ArrayList<>();

    public ArrayList<Card> getDefourche() {
        return defourche;
    }

    public void setDefourche(ArrayList<Card> defourche) {
        this.defourche = defourche;
    }

    private ArrayList<Card> defourche = new ArrayList<>();
    private ArrayList<Card> knownCards = new ArrayList<>();

    public ArrayList<Card> getAnalysisPossibleCard() {
        return analysisPossibleCard;
    }

    public void setAnalysisPossibleCard(ArrayList<Card> analysisPossibleCard) {
        this.analysisPossibleCard = analysisPossibleCard;
    }

    public ArrayList<Card> getAnalysisKnownCards() {
        return analysisKnownCards;
    }

    public void setAnalysisKnownCards(ArrayList<Card> analysisKnownCards) {
        this.analysisKnownCards = analysisKnownCards;
    }

    private ArrayList<Card> analysisPossibleCard = new ArrayList<>();
    private ArrayList<Card> analysisKnownCards = new ArrayList<>();
    private ArrayList<Card> hand = new ArrayList<>();
    private ArrayList<Card> currentHand = new ArrayList<>();
    private ArrayList<Card> playedCards = new ArrayList<>();

    public boolean isDealer() {
        return isDealer;
    }

    public void setDealer(boolean dealer) {
        isDealer = dealer;
    }


    int order;
    int role;

    Player(String name, boolean isAnalysable) {
        this.name = name;
        this.isAnalysable = isAnalysable;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
        currentHand.add(card);
        Log.i("Player" + name, "Added card to hand:" + card.getName() + "size of hand:" + hand.size());
    }

    public void clearHand() {
        hand.clear();
        currentHand.clear();
    }

    public void setCardSet(CardSet cardSet) {
        Log.i("SEttinng the cardset", "HI");
        this.cardSet = cardSet;
        possibleCard = cardSet.getCards();
    }

    public void update() {
        Log.i("LEAD COLOR:", "HI " + cardSet.getLeadColor());

        if (!hastrump) {
            ArrayList<Card> found = new ArrayList<>();
            for (Card card : possibleCard) {
                if (card.isTrump() && !card.isBourg()) {
                    found.add(card);
                }
            }
            possibleCard.removeAll(found);
        }
        if (!hasDiamonds) {
            ArrayList<Card> found = new ArrayList<>();
            for (Card card : possibleCard) {
                if (Objects.equals(card.getColor(), "d")) {
                    found.add(card);
                }
            }
            possibleCard.removeAll(found);
        }
        if (!hasSpades) {
            ArrayList<Card> found = new ArrayList<>();
            for (Card card : possibleCard) {
                if (card.getColor() == "s") {
                    found.add(card);
                }
            }
            possibleCard.removeAll(found);
        }
        if (!hasClubs) {
            ArrayList<Card> found = new ArrayList<>();
            for (Card card : possibleCard) {
                if (card.getColor() == "c") {
                    found.add(card);
                }
            }
            possibleCard.removeAll(found);
        }
        if (!hasHearts) {
            ArrayList<Card> found = new ArrayList<>();
            for (Card card : possibleCard) {
                if (card.getColor() == "h") {
                    found.add(card);
                }
            }
            possibleCard.removeAll(found);
        }
        for (Card card : cardSet.getCards()) {
            if (possibleCard.contains(card) && card.isPlayed()) {
                possibleCard.remove(card);
            }
        }
        if (cardsLeft == knownCards.size()) {
            ArrayList<Card> found = new ArrayList<>();
            for (Card card : possibleCard) {
                if (!knownCards.contains(card)) {
                    found.add(card);
                }
            }
            possibleCard.removeAll(found);
        }
        if (cardsLeft == knownCards.size() + analysisKnownCards.size()) {
            for (Card card : possibleCard) {
                if (!knownCards.contains(card) && !analysisKnownCards.contains(card)) {
                    analysisPossibleCard.add(card);
                }
            }
        }
    }

    public boolean hasColor(String color) {
        if (color == cardSet.getTrumpColor()) {
            return hastrump();
        } else {
            switch (color) {
                case "h":
                    return hasHearts;
                case "c":
                    return hasClubs;
                case "d":
                    return hasDiamonds;
                case "s":
                    return hasSpades;
                default:
                    return true;
            }
        }
    }


    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public String getName() {
        return name;
    }

    public boolean isAnalysable() {
        return isAnalysable;
    }

    public void setAnalysable(boolean analysable) {
        isAnalysable = analysable;
    }

    public ArrayList<Card> getPossibleCard() {
        return possibleCard;
    }

    public void setPossibleCard(ArrayList<Card> possibleCard) {
        this.possibleCard = possibleCard;
    }

    public ArrayList<Card> getKnownCards() {
        return knownCards;
    }

    public void setKnownCards(ArrayList<Card> knownCards) {
        this.knownCards = knownCards;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
        this.currentHand = hand;
    }

    public ArrayList<Card> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(ArrayList<Card> playedCards) {
        this.playedCards = playedCards;
    }

    public boolean hastrump() {
        boolean trump = false;
        for (Card card : possibleCard) {
            if (card.isTrump()) {
                trump = true;
            }
        }
        return trump;
    }

    public void setHastrump(boolean hastrump) {
        this.hastrump = hastrump;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }


    public void addToPossibleCards(Card card) {
        possibleCard.add(card);
    }

    public void addToKnownCards(Card card) {
        knownCards.add(card);
    }

    public String getFourcheColor() {
        return fourcheColor;
    }

    public void setFourcheColor(String fourcheColor) {
        this.fourcheColor = fourcheColor;
    }

    public void playCard(Card card) {

        if (!card.isLegal()) {
            if (false) {
                hastrump = false; //This excludes the buur
            } else {
                Log.i("Player " + name, "Lead color gotten" + cardSet.getLeadColor() + "card played" + card.getCard());
                switch (cardSet.getLeadColor()) {
                    case "h":
                        hasHearts = false;
                        break;
                    case "c":
                        hasClubs = false;
                        break;
                    case "d":
                        hasDiamonds = false;
                        break;
                    case "s":
                        hasSpades = false;
                        break;
                    default:
                        break;
                }
            }
            if (card.getPoint() <= 4 && !isFourcheDef) {
                isFourcheDef = true;
                fourcheColor = card.getColor();
            }
        }
        if (isAnalysable) {
            currentHand.remove(card);
        } else if (!currentHand.contains(card)) {
            hand.add(card);
        }
        //card.setOwner(this);
        possibleCard.remove(card);
        playedCards.add(card);
        cardsLeft--;

    }

    public ArrayList<Card> getCardsOfHand(String color) {
        ArrayList<Card> colorCards = new ArrayList<>();
        for (Card card : hand) {
            if (card.getColor() == color) {
                colorCards.add(card);
            }
        }
        return colorCards;
    }

    public ArrayList<Card> getPossibleCardofColor(String color) {
        ArrayList<Card> colorCards = new ArrayList<>();
        for (Card card : possibleCard) {
            if (card.getColor() == color) {
                colorCards.add(card);
            }
        }
        return colorCards;
    }

    public void newMatch(CardSet matchCardSet) {

        clearHand();
        playedCards.clear();
        knownCards.clear();
        possibleCard.clear();
        setCardSet(matchCardSet);
        cardsLeft = 9;


    }
}

