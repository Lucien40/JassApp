package lulu.ch.jassapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Double.valueOf;

/**
 * Created by huber on 19-Mar-17.
 */

class Analysis {

    private Trick mCurrentTrick;
    private Player lastEnemy;
    private Player nextEnemy;
    private Player ally;
    private String bourg;
    private String nell;
    private String trump10;
    private String asking10;
    private String askingAce;
    String trumpAce;
    String trump;
    String askedColor;
    private Match match;
    private Card cardToCut;
    boolean analysisPlayerFourche;
    Game game;
    Player nextTrueEnemy;
    int playerRole;
    All all;
    Is is;
    Player analysisPlayer;
    ArrayList<Trick> tricks;
    ArrayList<Player> players;
    CardSet cardSet;
    private int remainingPoints;
    private int remainingTricks;


    public Card getBestCard(ArrayList<Card> cards) {
        Card BestCard = cards.get(0);
        for (Card card : cards) {
            if (card.getPower() > BestCard.getPower()) {
                BestCard = card;
            }
        }
        return BestCard;
    }

    public int getBestCardIndex(ArrayList<Card> cards) {
        Card bestCard = getBestCard(cards);
        int index = 0;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) == bestCard) {
                index = i;
            }
        }
        Log.i("Index of Best Card", "index: " + index);
        return index;
    }

    Card getBestCard(Game game) {

        this.game = game;
        this.match = game.getCurrentMatch();
        mCurrentTrick = match.getCurrentTrick();
        analysisPlayer = match.getNextPlayer();
        playerRole = Math.abs(mCurrentTrick.getInitPlayer() - analysisPlayer.getOrder());
        Log.i("Analysis", this + "Role:" + playerRole);
        tricks = match.getTricks();
        analysisPlayerFourche = false;

        players = match.getPlayers();
        cardSet = match.getMatchCardSet();
        trump = cardSet.getTrumpColor();
        remainingPoints = 157 - (match.getMatchScoreTeam1() + match.getMatchScoreTeam2());
        remainingTricks = 9 - match.getTricks().size();
        askedColor = cardSet.getLeadColor();
        bourg = "j of " + trump;
        nell = "9 of " + trump;
        trumpAce = "a of " + trump;
        trump10 = "1 of " + trump;
        asking10 = "1 of " + askedColor;
        askingAce = "a of " + askedColor;
        all = new All(game);
        is = all.getIs();

        lastEnemy = players.get((players.indexOf(analysisPlayer) + 3) % 4);
        ally = players.get((players.indexOf(analysisPlayer) + 2) % 4);
        nextEnemy = players.get((players.indexOf(analysisPlayer) + 1) % 4);

        if (nextEnemy == game.getFirstPlayer()) {
            nextTrueEnemy = new Player("bla", false);
        } else {
            nextTrueEnemy = nextEnemy;
        }

        update();


        switch (playerRole) {
            case 0:
                return firstAnalysis();
            case 1:
                return secondAnalysis();
            case 2:
                return thirdAnalysis();
            case 3:
                return fourthAnalysis();
            default:
                return all.lowest(All.color(analysisPlayer.getHand(), askedColor)).get(0);
        }


    }

    private Card firstAnalysis() {
        if (((possiblyOwns(analysisPlayer, bourg)
                && analysisPlayerOwns(nell)
                && analysisPlayerOwns("a of " + trump)
                && analysisPlayerOwns("1 of " + trump))
                || analysisPlayerNumberOf(trump) > 1)
                && mCurrentTrick.getTrick().size() == 1
                && is.passed()) {

            return all.highest(all.trump(analysisPlayer.getHand())).get(0);

        } else if (analysisPlayerOwns(bourg)
                && Is.stillInGame(cardSet.getCard(nell))
                && analysisPlayerOwns(nell)
                && (!is.passed() || choseTrump(analysisPlayer) || choseTrump(ally))
                && !certainlyOwns(ally, nell)) {

            return cardSet.getCard(bourg);

        } else if ((opponentMaxNumberOfTrump() > 0
                || all.winning(all.nonTrump(analysisPlayer.getHand())).size() == 0)
                && anyOpponentMaxNumberOfTrump() > 1
                && analysisPlayerOwns(nell)
                && !is.winning(cardSet.getCard(nell))
                && analysisPlayerNumberOf(trump) == 2) {

            return giveOver();

        } else if ((opponentMaxNumberOfTrump() > 0
                || all.winning(all.nonTrump(analysisPlayer.getHand())).size() == 0)
                && anyOpponentMaxNumberOfTrump() > 1
                && ((certainlyOwns(nextEnemy, nell) && getPossibleNumberOf(trump, nextEnemy) == 1) && (certainlyOwns(lastEnemy, nell) && getPossibleNumberOf(trump, lastEnemy) == 1))
                && analysisPlayerOwns("a of " + trump)
                && !is.winning(cardSet.getCard("a of " + trump))
                && analysisPlayerNumberOf(trump) == 2) {

            return giveOver();

        } else if ((opponentMaxNumberOfTrump() > 0
                || all.winning(all.nonTrump(analysisPlayer.getHand())).size() == 0)
                && anyOpponentMaxNumberOfTrump() > 1
                && analysisPlayerOwns("1 of " + trump)
                && !is.winning(cardSet.getCard("1 of " + trump))
                && analysisPlayerNumberOf(trump) == 2) {

            return giveOver();

        } else {

            return playBock();

        }
    }

    private Card giveOver() {
        if (opponentMaxNumberOfTrump() == 0
                & isDefourcheDefined(ally)
                & playerOwnAllyDefourcheCard()) {

            return all.mostValuable(all.defourche(analysisPlayer.getHand(), ally)).get(0);

        } else if (opponentMaxNumberOfTrump() > 0
                & isDefourcheDefined(ally)
                & playerOwnAllyDefourcheCard()) {

            return all.leastValuable(all.defourche(analysisPlayer.getHand(), ally)).get(0);

        } else {
            return cardToDiscard();
        }
    }

    private Card cardToDiscard() {

        if (isInTrick(trump)
                && !isDefourcheDefined(analysisPlayer)) {
            return all.leastValuable(all.nonBockOption(all.defourche(analysisPlayer.getHand(), analysisPlayer))).get(0);
        } else if (isInTrick(trump)
                && isTrick()
                && all.nonTrump(analysisPlayer.getHand()).size() > 0
                && all.keeping(All.color(getPossibleCards(nextTrueEnemy), askedColor)).size() == 0
                && all.keeping(all.trump(getPossibleCards(nextTrueEnemy))).size() == 0) {
            return all.mostValuable(all.nonBockOption(all.nonTrump(analysisPlayer.getHand()))).get(0);
        } else if (isInTrick(trump)) {
            return All.color(analysisPlayer.getHand(), discardSelection()).get(0);
        } else if (isDefourcheDefined(ally)
                && all.defourche(analysisPlayer.getHand(), ally).size() > 0
                && MaxNumberOfTrump(nextTrueEnemy) == 0
                && (playerRole == 0 || playerRole == 1)) {
            return all.mostValuable(all.defourche(analysisPlayer.getHand(), ally)).get(0);
        } else if (isDefourcheDefined(ally)
                && all.defourche(analysisPlayer.getHand(), ally).size() > 0
                && (playerRole == 0 || playerRole == 1)) {
            return all.leastValuable(all.defourche(analysisPlayer.getHand(), ally)).get(0);
        } else if (analysisPlayerNumberOf(askedColor) == 0
                && !isDefourcheDefined(analysisPlayer)) {
            try {
                return all.leastValuable(all.nonBockOption(all.defourche(analysisPlayer.getHand(), analysisPlayer))).get(0);
            } finally {
                return cardSet.getCard(askingAce);
            }


        } else {
            try {
                return All.color(analysisPlayer.getHand(), discardSelection()).get(0);
            } finally {
                return cardSet.getCard(askingAce);
            }

        }

    }

    private String discardSelection() {
        String[] colors = new String[4];
        int[] dis = new int[4];
        colors[0] = "s";
        colors[1] = "c";
        colors[2] = "d";
        colors[3] = "h";
        for (int i = 0; i < 4; i++) {
            if (All.color(analysisPlayer.getHand(), colors[i]).size() > 0
                    && !Objects.equals(colors[i], cardSet.getTrumpColor())) {

                dis[i] = (All.color(analysisPlayer.getHand(), colors[i]).size() * 10)
                        + (all.card(analysisPlayer.getHand(), "1 of " + colors[i]).size() * 20)
                        + (all.card(analysisPlayer.getHand(), "j of " + colors[i]).size() * 8)
                        + (all.card(analysisPlayer.getHand(), "q of " + colors[i]).size() * 11)
                        + (all.card(analysisPlayer.getHand(), "k of " + colors[i]).size() * 15)
                        + (all.card(analysisPlayer.getHand(), "a of " + colors[i]).size() * 30)
                        + (All.color(all.bock(analysisPlayer.getHand()), colors[i]).size() * 10);

                if (analysisPlayerOwns("a of " + colors[i])
                        && analysisPlayerNumberOf(colors[i]) == 1) {
                    dis[i] += 80;
                }
                if (analysisPlayerOwns("a of " + colors[i])
                        && analysisPlayerNumberOf(colors[i]) == 2
                        && opponentMaxNumberOfTrump() > 0) {
                    dis[i] += 40;
                }
                if (analysisPlayerOwns("1 of " + colors[i])
                        && analysisPlayerNumberOf(colors[i]) == 1) {
                    dis[i] += 60;
                }
                if (analysisPlayerOwns("1 of " + colors[i])
                        && analysisPlayerNumberOf(colors[i]) == 2) {
                    dis[i] += 30;
                }
                if (analysisPlayerOwns("k of " + colors[i])
                        && all.bock(all.card(analysisPlayer.getHand(), "k of " + colors[i])).size() == 0
                        && analysisPlayerNumberOf(colors[i]) == 2) {
                    dis[i] += 25;
                }
                if (analysisPlayerOwns("q of " + colors[i])
                        && all.bock(all.card(analysisPlayer.getHand(), "q of " + colors[i])).size() == 0
                        && analysisPlayerNumberOf(colors[i]) == 3) {
                    dis[i] += 20;
                }
                if (analysisPlayerOwns("j of " + colors[i])
                        && all.bock(all.card(analysisPlayer.getHand(), "j of " + colors[i])).size() == 0
                        && analysisPlayerNumberOf(colors[i]) == 3) {
                    dis[i] += 15;
                }
            }
        }

        if (dis[0] >= dis[1]
                && dis[0] >= dis[2]
                && dis[0] >= dis[3]) {
            return "s";
        } else if (dis[1] >= dis[0]
                && dis[1] >= dis[2]
                && dis[1] >= dis[3]) {
            return "c";
        } else if (dis[2] >= dis[1]
                && dis[2] >= dis[0]
                && dis[2] >= dis[3]) {
            return "d";
        } else if (dis[3] >= dis[1]
                && dis[3] >= dis[2]
                && dis[3] >= dis[0]) {
            return "h";
        } else return "";


    }

    private String fourchSelection(Player analysisPlayer) {
        int DefS = analysisPlayerNumberOf("s") + (all.bock(All.color(analysisPlayer.getHand(), "s")).size() * 2);
        int DefC = analysisPlayerNumberOf("c") + (all.bock(All.color(analysisPlayer.getHand(), "c")).size() * 2);
        int DefD = analysisPlayerNumberOf("d") + (all.bock(All.color(analysisPlayer.getHand(), "d")).size() * 2);
        int DefH = analysisPlayerNumberOf("h") + (all.bock(All.color(analysisPlayer.getHand(), "h")).size() * 2);

        if (Objects.equals("s", cardSet.getTrumpColor())) {
            DefS = 0;
        } else if (Objects.equals("c", cardSet.getTrumpColor())) {
            DefC = 0;
        } else if (Objects.equals("d", cardSet.getTrumpColor())) {
            DefD = 0;
        } else if (Objects.equals("h", cardSet.getTrumpColor())) {
            DefH = 0;
        }

        if (DefS >= DefC
                && DefS >= DefD
                && DefS >= DefH) {
            return "s";
        } else if (DefC >= DefS
                && DefC >= DefD
                && DefC >= DefH) {
            return "c";
        } else if (DefD >= DefC
                && DefD >= DefS
                && DefD >= DefH) {
            return "d";
        } else if (DefH >= DefC
                && DefH >= DefD
                && DefH >= DefS) {
            return "h";
        } else return "";
    }

    private boolean isDefourcheDefined(Player player) {
        if (player == analysisPlayer && !analysisPlayer.isFourcheDef()) {
            return analysisPlayerFourche;
        } else {
            return player.isFourcheDef();
        }
    }

    private boolean playerOwnAllyDefourcheCard() {
        analysisPlayerOwns(All.color(analysisPlayer.getHand(), ally.getFourcheColor()));
        return false;
    }

    private Card playBock() {
        if (opponentMaxNumberOfTrump() == 0
                && all.nonTrump(all.bock(analysisPlayer.getHand())).size() > 0) {

            return all.highest(all.nonTrump(all.bock(analysisPlayer.getHand()))).get(0);

        } else if (opponentMaxNumberOfTrump() == 0
                && all.winning(all.nonTrump(analysisPlayer.getHand())).size() > 0) {

            return all.highest(all.nonTrump(all.winning(analysisPlayer.getHand()))).get(0);

        } else {

            return playTrump();

        }
    }

    private Card secondAnalysis() {

        if (Objects.equals(askedColor, trump)) {
            if (analysisPlayerNumberOf(trump) == 0) {
                return secondAnalysisTrumpAskedNo();
            } else {
                return secondAnalysisTrumpAskedYes();
            }
        } else {
            if (analysisPlayerNumberOf(askedColor) == 0) {
                return secondAnalysisnonTrumpAskedNo();
            } else {
                return secondAnalysisnonTrumpAskedYes();
            }
        }

    }

    private Card secondAnalysisnonTrumpAskedYes() {

        if (analysisPlayerNumberOf(trump) == 0
                && analysisPlayerNumberOf(askedColor) == 1) {
            return All.color(analysisPlayer.getHand(), askedColor).get(0);
        } else if (MaxNumberOfTrump(nextEnemy) == 0
                && MaxNumberOfTrump(lastEnemy) >= analysisPlayerNumberOf(trump)
                && analysisPlayerOwns(askingAce)
                && analysisPlayerNumberOf(askedColor) == 2) {
            return cardSet.getCard(askingAce);
        } else return normalSecondAnalysisnonTrumpAskedYes();

    }

    private Card normalSecondAnalysisnonTrumpAskedYes() {

        if (all.ofGreaterRank(askedColor, (All.color(all.keeping(analysisPlayer.getHand()), askedColor)), nextEnemy).size() > 0
                && MaxNumberOfTrump(nextEnemy) == 0) {

            return all.mostValuable(all.nonBockOption(all.ofGreaterRank(askedColor, all.keeping(All.color(analysisPlayer.getHand(), askedColor)), nextEnemy))).get(0);

        } else if (worthCutting()) {
            return cardToCut();
        } else if (all.non(asking10, all.non((askingAce), all.keeping(All.color(analysisPlayer.getHand(), askedColor)))).size() > 0) {
            return all.leastValuable(all.non(asking10, all.non((askingAce), all.keeping(All.color(analysisPlayer.getHand(), askedColor))))).get(0);
        } else {
            return all.leastValuable(all.nonBockToComeOption(All.color(analysisPlayer.getHand(), askedColor))).get(0);
        }
    }

    private Card cardToCut() {
        return cardToCut;
    }

    private Card secondAnalysisnonTrumpAskedNo() {

        if (worthCutting()) {
            return cardToCut();
        } else {
            return cardToDiscard();
        }

    }

    private Card secondAnalysisTrumpAskedYes() {

        if (analysisPlayerNumberOf(trump) == 1 &&
                !analysisPlayerOwns(bourg)) {

            return all.trump(analysisPlayer.getHand()).get(0);

        } else if (!analysisPlayerOwns(bourg) &&
                !worthCutting() &&
                analysisPlayerNumberOf(trump) == 1) {

            return cardToDiscard();

        } else if (MaxNumberOfTrump(nextEnemy) == 0
                && analysisPlayerOwns(nell)
                && analysisPlayerOwns(bourg)
                && is.winning(cardSet.getCard(nell))
                && all.non(all.keeping(all.trump(analysisPlayer.getHand())), cardSet.getCard(nell)).size() > 0
                && Is.stillInGame(cardSet.getCard(trumpAce))
                && !isInTrick(cardSet.getCard(trumpAce))
                && !analysisPlayerOwns(trumpAce)) {

            return all.mostValuable(all.nonBockOption(all.non(nell, all.keeping(all.trump(analysisPlayer.getHand()))))).get(0);
        } else if (!possiblyOwns(nextEnemy, trumpAce)
                && nobodyOwn(bourg)
                && isInTrick(cardSet.getCard(bourg))
                && analysisPlayerOwns(nell)
                && all.non(nell, all.keeping(all.trump(analysisPlayer.getHand()))).size() == 0
                && all.non(nell, all.non(trump10, all.trump(analysisPlayer.getHand()))).size() > 0
                && Is.stillInGame(cardSet.getCard(trumpAce))
                && isInTrick(cardSet.getCard(trumpAce))) {

            return all.leastValuable(all.nonBockOption(all.non(nell, all.non(trump10, all.trump(analysisPlayer.getHand()))))).get(0);

        } else return normalSecondAnalysisTrumpAskedYes();

    }

    private boolean nobodyOwn(String card) {
        return !possiblyOwns(nextEnemy, card) && !possiblyOwns(lastEnemy, card) && !possiblyOwns(analysisPlayer, card) && !possiblyOwns(ally, card);
    }

    private Card normalSecondAnalysisTrumpAskedYes() {

        if (all.ofGreaterRank(trump, all.trump(all.non(bourg, all.keeping(analysisPlayer.getHand()))), nextEnemy).size() > 0) {

            return all.mostValuable(all.nonBockOption(all.ofGreaterRank(trump, all.trump(all.non(bourg, all.keeping(analysisPlayer.getHand()))), nextEnemy))).get(0);

        } else if (analysisPlayerOwns(bourg)
                && worthCutting()) {
            return cardSet.getCard(bourg);
        } else if (all.non(trump10, all.non(trumpAce, all.non(nell, all.keeping(all.trump(all.non(bourg, analysisPlayer.getHand())))))).size() > 0) {

            return all.leastValuable(all.non(trump10, all.non(trumpAce, all.non(nell, all.keeping(all.trump(all.non(bourg, analysisPlayer.getHand()))))))).get(0);
        } else {
            return all.leastValuable(all.nonBockToComeOption(all.trump(analysisPlayer.getHand()))).get(0);
        }

    }

    private boolean isInTrick(Card card) {
        return mCurrentTrick.getTrick().contains(card);
    }

    private boolean isInTrick(String color) {
        boolean isInTrick = false;
        for (Card card : mCurrentTrick.getTrick()) {
            if (Objects.equals(card.getColor(), color)) {
                isInTrick = true;
            }
        }
        return isInTrick;
    }

    private boolean worthCutting() {

        if (Objects.equals(askedColor, trump)
                && analysisPlayerOwns(bourg)
                && withoutBourgGuessValueOfTheTrick() > specialBourgThreshold()) {
            cardToCut = cardSet.getCard(bourg);
            return true;
        } else if (!Objects.equals(askedColor, trump)
                && all.ofGreaterRank(trump, all.keeping(all.trump(analysisPlayer.getHand())), nextTrueEnemy).size() > 0
                && guessValueOfTrick(all.mostValuable(all.nonBockToComeOption(all.ofGreaterRank(trump, all.keeping(all.trump(analysisPlayer.getHand())), nextTrueEnemy))).get(0)) > Threshold()
                && all.keeping(all.trump(nextTrueEnemy.getHand())).size() > 0) {

            cardToCut = all.mostValuable(all.nonBockToComeOption(all.ofGreaterRank(trump, all.keeping(all.trump(analysisPlayer.getHand())), nextTrueEnemy))).get(0);
            return true;
        } else if (!Objects.equals(askedColor, trump)
                && all.non(trump10, all.non(trumpAce, all.non(nell, all.keeping(all.trump(analysisPlayer.getHand()))))).size() > 0
                && guessValueOfTrick(all.lowest(all.non(trump10, all.non(trumpAce, all.non(nell, all.keeping(all.trump(analysisPlayer.getHand())))))).get(0)) > (Threshold() + 2)
                ) {

            cardToCut = all.lowest(all.non(trump10, all.non(trumpAce, all.non(nell, all.keeping(all.trump(analysisPlayer.getHand())))))).get(0);
            return true;
        } else {
            return false;
        }

    }

    private int specialBourgThreshold() {
        Double threshold;
        if (analysisPlayerNumberOf(trump) > 0) {
            threshold = (valueOf(remainingPoints - 20) / remainingTricks) - 1 - all.leastValuable(all.trump(analysisPlayer.getHand())).get(0).getPoint();
        } else {
            threshold = (valueOf(remainingPoints - 20) / remainingTricks) - 1 - cardToDiscard().getPoint();
        }
        return threshold.intValue();

    }

    private int withoutBourgGuessValueOfTheTrick() {
        switch (playerRole) {

            case 0:
                return 0;
            case 1:
                return all.leastValuable(All.color(getPossibleCards(nextEnemy), askedColor)).get(0).getPoint()
                        + averageValueOf(All.color(getPossibleCards(ally), askedColor))
                        + mCurrentTrick.getTrickPoints();
            case 2:
                return all.leastValuable(All.color(getPossibleCards(nextEnemy), askedColor)).get(0).getPoint()
                        + mCurrentTrick.getTrickPoints();
            case 3:
                return mCurrentTrick.getTrickPoints();
            default:
                return 0;
        }

    }

    private int averageValueOf(ArrayList<Card> cards) {
        int tot = 0;
        for (Card card : cards) {
            tot += card.getPoint();
        }
        return Double.valueOf(tot / cards.size()).intValue();
    }

    private Card secondAnalysisTrumpAskedNo() {

        return cardToDiscard();

    }

    private Card thirdAnalysis() {
        if (Objects.equals(askedColor, trump)) {
            if (analysisPlayerNumberOf(trump) == 0) {
                return thirdAnalysisTrumpAskedNo();
            } else {
                return thirdAnalysisTrumpAskedYes();
            }
        } else {
            if (analysisPlayerNumberOf(askedColor) == 0) {
                return thirdAnalysisnonTrumpAskedNo();
            } else {
                return thirdAnalysisnonTrumpAskedYes();
            }
        }
    }

    private Card thirdAnalysisnonTrumpAskedNo() {

        if (worthCutting()) {
            return cardToCut();
        } else {
            return cardToDiscard();
        }
    }

    private Card thirdAnalysisnonTrumpAskedYes() {

        if (analysisPlayerNumberOf(trump) == 0
                && analysisPlayerNumberOf(askedColor) == 1) {

            return All.color(analysisPlayer.getHand(), askedColor).get(0);

        } else if (isTrick()
                && all.keeping(all.trump(getPossibleCards(nextEnemy))).size() == 0
                && all.keeping(All.color(getPossibleCards(nextEnemy), askedColor)).size() == 0
                && analysisPlayerOwns(asking10)
                && !is.winning(asking10)) {

            return cardSet.getCard(asking10);

        } else if (all.trump(mCurrentTrick.getTrick()).size() == 0
                && all.trump(getPossibleCards(nextEnemy)).size() == 0
                && allGreater(asking10, getPossibleCards(nextEnemy)).size() == 0
                && analysisPlayerOwns(asking10)) {

            return cardSet.getCard(asking10);

        } else if ((all.trump(mCurrentTrick.getTrick()).size() == 0 || isTrick())
                && all.keeping(all.trump(getPossibleCards(nextEnemy))).size() == 0
                && analysisPlayerOwns(askingAce)
                && opponentMaxNumberOfTrump() >= analysisPlayerNumberOf(trump)
                && analysisPlayerNumberOf(askedColor) == 2) {

            return cardSet.getCard(askingAce);

        } else {
            return normalThirdAnalysisnonTrumpAskedYes();
        }
    }

    private Card normalThirdAnalysisnonTrumpAskedYes() {

        if (isTrick()
                && all.keeping(All.color(getPossibleCards(nextEnemy), askedColor)).size() == 0
                && all.keeping(all.trump(getPossibleCards(nextEnemy))).size() == 0) {

            return all.lowest(All.color(analysisPlayer.getHand(), askedColor)).get(0);

        } else if (MaxNumberOfTrump(nextEnemy) == 0
                && isInTrick(trump)
                && all.ofGreaterRank(askedColor, All.color(analysisPlayer.getHand(), askedColor), nextEnemy).size() > 0
                && guessValueOfTrick(all.mostValuable(all.nonBockOption(all.ofGreaterRank(askedColor, analysisPlayer.getHand(), nextEnemy))).get(0)) > (Threshold() - 3)) {

            return all.mostValuable(all.nonBockOption(all.ofGreaterRank(askedColor, analysisPlayer.getHand(), nextEnemy))).get(0);

        } else if (worthCutting()) {
            return cardToCut();
        } else if (all.non(asking10, all.non(askingAce, all.keeping(All.color(analysisPlayer.getHand(), askedColor)))).size() > 0
                && guessValueOfTrick(all.leastValuable(all.non(asking10, all.non(askingAce, all.keeping(All.color(analysisPlayer.getHand(), askedColor))))).get(0)) > (Threshold() - 3)) {

            return all.leastValuable(all.non(asking10, all.non(askingAce, all.keeping(All.color(analysisPlayer.getHand(), askedColor))))).get(0);

        } else {
            return all.leastValuable(all.nonBockToComeOption(All.color(analysisPlayer.getHand(), askedColor))).get(0);
        }

    }

    private Card thirdAnalysisTrumpAskedNo() {
        return cardToDiscard();
    }

    private Card thirdAnalysisTrumpAskedYes() {

        if (analysisPlayerNumberOf(trump) == 1 &&
                !analysisPlayerOwns(bourg)) {

            return all.trump(analysisPlayer.getHand()).get(0);

        } else if ((isTrick() && all.keeping(all.trump(getPossibleCards(nextEnemy))).size() == 0)
                || (is.keeping(cardSet.getCard(trump10)) && allGreater(trump10, all.trump(getPossibleCards(nextEnemy))).size() == 0)
                && analysisPlayerOwns(trump10)
                && !is.winning(cardSet.getCard(trump10))) {

            return cardSet.getCard(trump10);

        } else if (analysisPlayerOwns(bourg)
                && worthCutting()
                && analysisPlayerNumberOf(trump) == 1) {

            return cardToDiscard();

        } else if (MaxNumberOfTrump(nextEnemy) == 0
                && analysisPlayerOwns(nell)
                && !analysisPlayerOwns(bourg)
                && is.winning(cardSet.getCard(nell))
                && all.non(nell, all.keeping(getPossibleOf(trump, analysisPlayer))).size() > 0
                && Is.stillInGame(cardSet.getCard(trumpAce))
                && !isInTrick(cardSet.getCard(trumpAce))
                && !analysisPlayerOwns(trumpAce)) {

            return all.mostValuable(all.nonBockOption(all.non(nell, all.keeping(all.trump(analysisPlayer.getHand()))))).get(0);

        } else if (!possiblyOwns(nextEnemy, trumpAce)
                && nobodyOwn(bourg)
                && analysisPlayerOwns(nell)
                && all.non(nell, all.keeping(all.trump(analysisPlayer.getHand()))).size() == 0
                && all.non(nell, all.non(trump10, all.trump(analysisPlayer.getHand()))).size() > 0
                && Is.stillInGame(cardSet.getCard(trumpAce))
                && !isInTrick(cardSet.getCard(trumpAce))) {

            return all.leastValuable(all.nonBockOption(all.non(nell, all.non(trump10, all.trump(analysisPlayer.getHand()))))).get(0);

        } else if (analysisPlayerOwns(nell)
                && !is.winning(nell)
                && is.keeping(nell)
                && analysisPlayerNumberOf(trump) == 2
                && !possiblyOwns(nextEnemy, bourg)) {
            return cardSet.getCard(nell);
        } else if ((isTrick()
                && is.keeping(trumpAce))
                && analysisPlayerOwns(trumpAce)
                && !is.winning(trumpAce)
                && all.ofGreaterRank(trump, getPossibleCards(nextEnemy), analysisPlayer).size() == 0
                && all.trump(allGreater(trumpAce, analysisPlayer.getHand())).size() == 0
                && all.ofGreaterRank(trump, getPossibleCards(lastEnemy), analysisPlayer).size() == analysisPlayerNumberOf(trump)) {
            return cardSet.getCard(trumpAce);
        } else {
            return normalThirdAnalysisTrumpAskedYes();
        }
    }

    private boolean isTrick() {

        if (playerRole == 2) {
            return is.winning(mCurrentTrick.getTrick().get(0));
        } else if (playerRole == 3) {
            return is.winning(mCurrentTrick.getTrick().get(1));
        } else {
            return false;
        }

    }

    private Card normalThirdAnalysisTrumpAskedYes() {
        if (isTrick()
                && all.keeping(getPossibleOf(trump, nextEnemy)).size() == 0) {
            return all.lowest(all.trump(analysisPlayer.getHand())).get(0);
        } else if (all.non(bourg, all.trump(all.keeping(all.ofGreaterRank(trump, analysisPlayer.getHand(), nextEnemy)))).size() > 0) {
            return all.mostValuable(all.nonBockOption(all.non(bourg, all.trump(all.keeping(all.ofGreaterRank(trump, analysisPlayer.getHand(), nextEnemy)))))).get(0);
        } else if (analysisPlayerOwns(bourg)
                && worthCutting()) {
            return cardSet.getCard(bourg);
        } else if (all.non(trump10, all.non(trumpAce, all.non(nell, all.non(bourg, all.keeping(all.trump(analysisPlayer.getHand())))))).size() > 0
                && guessValueOfTrick(all.leastValuable(all.non(trump10, all.non(trumpAce, all.non(nell, all.non(bourg, all.keeping(all.trump(analysisPlayer.getHand()))))))).get(0)) > (Threshold() - 3)) {
            return all.leastValuable(all.non(trump10, all.non(trumpAce, all.non(nell, all.non(bourg, all.keeping(all.trump(analysisPlayer.getHand()))))))).get(0);
        } else {
            return all.leastValuable(all.nonBockToComeOption(all.trump(analysisPlayer.getHand()))).get(0);
        }
    }

    private int Threshold() {
        Double threshold;
        if (analysisPlayerNumberOf(askedColor) > 0) {
            threshold = (double) (remainingPoints / remainingTricks) - 1 - all.leastValuable(All.color(analysisPlayer.getHand(), askedColor)).get(0).getPoint();
        } else {
            threshold = (double) (remainingPoints / remainingTricks) - 1 - cardToDiscard().getPoint();
        }
        return threshold.intValue();

    }

    private int guessValueOfTrick(Card card) {

        switch (playerRole) {

            case 0:
                return 0;
            case 1:
                return all.leastValuable(All.color(getPossibleCards(nextEnemy), askedColor)).get(0).getPoint()
                        + averageValueOf(All.color(getPossibleCards(ally), askedColor))
                        + mCurrentTrick.getTrickPoints()
                        + card.getPoint();
            case 2:
                return all.leastValuable(All.color(getPossibleCards(nextEnemy), askedColor)).get(0).getPoint()
                        + mCurrentTrick.getTrickPoints()
                        + card.getPoint();
            case 3:
                return mCurrentTrick.getTrickPoints()
                        + card.getPoint();
            default:
                return 0;
        }
    }

    private ArrayList<Card> allGreater(Card card, ArrayList<Card> b) {

        ArrayList<Card> allGreater = new ArrayList<>();

        for (Card c : b) {
            if (c.getPower() > card.getPower()) {
                allGreater.add(c);
            }
        }

        return allGreater;

    }

    private ArrayList<Card> allGreater(String card, ArrayList<Card> b) {
        return allGreater(cardSet.getCard(card), b);
    }

    private Card fourthAnalysis() {
        if (Objects.equals(askedColor, trump)) {
            if (analysisPlayerNumberOf(trump) == 0) {
                return fourthAnalysisTrumpAskedNo();
            } else {
                return fourthAnalysisTrumpAskedYes();
            }
        } else {
            if (analysisPlayerNumberOf(askedColor) == 0) {
                return fourthAnalysisnonTrumpAskedNo();
            } else {
                return fourthAnalysisnonTrumpAskedYes();
            }
        }
    }

    private Card fourthAnalysisnonTrumpAskedNo() {

        if (worthCutting()) {
            return cardToCut();
        } else {
            return cardToDiscard();
        }

    }

    private Card fourthAnalysisnonTrumpAskedYes() {

        if (analysisPlayerNumberOf(trump) == 0
                && analysisPlayerNumberOf(askedColor) == 1) {

            return All.color(analysisPlayer.getHand(), askedColor).get(0);

        } else if (isTrick()
                && !is.winning(asking10)
                && analysisPlayerOwns(asking10)) {

            return cardSet.getCard(asking10);

        } else if (is.keeping(asking10)
                && !is.winning(asking10)
                && analysisPlayerOwns(asking10)) {

            return cardSet.getCard(asking10);

        } else if (is.keeping(askingAce)
                && analysisPlayerOwns(askingAce)
                && opponentMaxNumberOfTrump() >= analysisPlayerNumberOf(trump)
                && analysisPlayerNumberOf(askedColor) == 2) {

            return cardSet.getCard(askingAce);

        } else {
            return normalFourthAnalysisnonTrumpAskedYes();
        }

    }

    private Card normalFourthAnalysisnonTrumpAskedYes() {
        if (isTrick()) {
            return all.lowest(All.color(analysisPlayer.getHand(), askedColor)).get(0);
        } else if (!isInTrick(trump)
                && all.keeping(All.color(analysisPlayer.getHand(), askedColor)).size() > 0
                && guessValueOfTrick(all.mostValuable(all.nonBockToComeOption(all.keeping(All.color(analysisPlayer.getHand(), askedColor)))).get(0)) > (Threshold() - 4)) {

            return all.mostValuable(all.nonBockToComeOption(all.keeping(All.color(analysisPlayer.getHand(), askedColor)))).get(0);

        } else if (worthCutting()) {
            return cardToCut();
        } else {
            return all.leastValuable(all.nonBockToComeOption(All.color(analysisPlayer.getHand(), askedColor))).get(0);
        }
    }

    private Card fourthAnalysisTrumpAskedNo() {
        return cardToDiscard();
    }

    private Card fourthAnalysisTrumpAskedYes() {

        if (analysisPlayerNumberOf(trump) == 1
                && analysisPlayerOwns(bourg)) {

            return all.trump(analysisPlayer.getHand()).get(0);

        } else if ((isTrick() || is.keeping(trump10))
                && analysisPlayerOwns(trump10)
                && !is.winning(trump10)) {

            return cardSet.getCard(trump10);

        } else if (is.keeping(nell)
                && analysisPlayerOwns(nell)
                && is.winning(nell)
                && analysisPlayerNumberOf(trump) == 2) {

            return cardSet.getCard(nell);

        } else if ((isTrick() || is.keeping(trumpAce))
                && analysisPlayerOwns(trumpAce)
                && is.winning(trumpAce)
                && all.trump(allGreater(trumpAce, analysisPlayer.getHand())).size() == 0
                && (all.ofGreaterRank(trump, getPossibleCards(nextEnemy), analysisPlayer).size() + all.ofGreaterRank(trump, getPossibleCards(lastEnemy), analysisPlayer).size()) >= analysisPlayerNumberOf(trump)) {

            return cardSet.getCard(trumpAce);

        } else if (analysisPlayerOwns(bourg)
                && !worthCutting()
                && analysisPlayerNumberOf(trump) == 1) {
            return cardToDiscard();
        } else if (!analysisPlayerOwns(bourg)
                && analysisPlayerOwns(nell)
                && is.winning(nell)
                && all.non(nell, all.keeping(all.trump(analysisPlayer.getHand()))).size() > 0
                && is.stillInGame(trumpAce)
                && !isInTrick(trumpAce)
                && !analysisPlayerOwns(trumpAce)) {

            return all.mostValuable(all.nonBockOption(all.non(nell, all.keeping(all.trump(analysisPlayer.getHand()))))).get(0);

        } else if (nobodyOwn(bourg)
                && analysisPlayerOwns(nell)
                && all.non(nell, all.keeping(all.trump(analysisPlayer.getHand()))).size() == 0
                && all.non(nell, all.non(trump10, all.trump(analysisPlayer.getHand()))).size() > 0
                && is.stillInGame(trumpAce)
                && !isInTrick(trumpAce)) {

            return all.leastValuable(all.nonBockOption(all.non(nell, all.non(trump10, all.trump(analysisPlayer.getHand()))))).get(0);

        } else {
            return normalForthAnalysisTrumpAskedYes();
        }

    }

    private Card normalForthAnalysisTrumpAskedYes() {

        if (isTrick()) {
            return all.lowest(all.trump(analysisPlayer.getHand())).get(0);

        } else if (all.keeping(all.trump(all.non(bourg, analysisPlayer.getHand()))).size() > 0

                && guessValueOfTrick(all.mostValuable(all.nonBockOption(all.keeping(all.trump(all.non(bourg, analysisPlayer.getHand()))))).get(0)) > (Threshold() - 2)) {

            return all.mostValuable(all.nonBockOption(all.keeping(all.trump(all.non(bourg, analysisPlayer.getHand()))))).get(0);

        } else if (analysisPlayerOwns(bourg)
                && worthCutting()) {

            return cardSet.getCard(bourg);

        } else {
            return all.leastValuable(all.nonBockToComeOption(all.trump(analysisPlayer.getHand()))).get(0);
        }

    }

    private Card playTrump() {

        if (MaxNumberOfTrump(nextEnemy) > 0
                && MaxNumberOfTrump(lastEnemy) > 0
                && all.winning(getPossibleOf(trump, analysisPlayer)).size() > 0) {

            return all.highest(all.winning(all.trump(analysisPlayer.getHand()))).get(0);

        } else if (opponentMaxNumberOfTrump() > 0
                && MaxNumberOfTrump(ally) > 0
                && all.winning(getPossibleOf(trump, analysisPlayer)).size() > 0) {

            return all.highest(all.winning(all.trump(analysisPlayer.getHand()))).get(0);

        } else if (MaxNumberOfTrump(nextEnemy) > 0
                && MaxNumberOfTrump(lastEnemy) > 0
                && analysisPlayerNumberOf(trump) >= opponentMaxNumberOfTrump()
                && analysisPlayerOwns(All.lowValue(analysisPlayer.getHand()))) {

            return all.leastValuable(All.lowValue(all.trump(analysisPlayer.getHand()))).get(0);
        } else {
            return giveOver();
        }

    }

    private int getPossibleNumberOf(String color, Player player) {
        return getPossibleOf(color, player).size();
    }

    private int getKnownNumberOf(String color, Player player) {
        return getKnownOf(color, player).size();
    }


    ////////////////////OWN////////////////////////

    private boolean possiblyOwns(Player player, Card card) {
        return getPossibleCards(player).contains(card);
    }

    private boolean possiblyOwns(Player player, ArrayList<Card> cards) {
        for (Card card : cards) {
            if (possiblyOwns(player, card)) {
                return true;
            }
        }
        return false;
    }

    private boolean possiblyOwns(Player player, String card) {
        return getPossibleCards(player).contains(cardSet.getCard(card));
    }

    private boolean certainlyOwns(Player player, Card card) {
        return getKnownCards(player).contains(card);
    }

    private boolean certainlyOwns(Player player, String card) {
        return certainlyOwns(player, cardSet.getCard(card));
    }

    /////IS CHECK/////////////////////////////////////////////


    String getFourcheColor(Player player) {
        if (Objects.equals(player, game.getPlayer(0)) && !game.getPlayer(0).isFourcheDef()) {
            return fourchSelection(analysisPlayer);
        } else {
            return player.getFourcheColor();
        }
    }

    private boolean choseTrump(Player player) {
        return Objects.equals(player, game.getFirstPlayer());
    }

    //////////TODO get real possible cards///

    private ArrayList<Card> getPossibleCards(Player player) {
        if (player == analysisPlayer) {
            return analysisPlayer.getHand();
        } else {
            ArrayList<Card> possible = new ArrayList<>();
            for (Card card : player.getPossibleCard()) {
                if (!analysisPlayer.getHand().contains(card) && !player.getAnalysisPossibleCard().contains(card)) {
                    possible.add(card);
                }
            }
            return possible;
        }
    }

    private ArrayList<Card> getKnownCards(Player player) {
        if (player == analysisPlayer) {
            return analysisPlayer.getHand();
        } else {
            ArrayList<Card> known = new ArrayList<>();
            ArrayList<Card> knownCard = player.getKnownCards();
            knownCard.addAll(player.getAnalysisKnownCards());
            for (Card card : knownCard) {
                if (!analysisPlayer.getHand().contains(card)) {
                    known.add(card);
                }
            }
            return known;
        }
    }

    private ArrayList<Card> getPossibleOf(String color, Player player) {
        ArrayList<Card> possibleColor = new ArrayList<>();
        for (Card card : getPossibleCards(player)) {
            if (Objects.equals(card.getColor(), color)) {
                possibleColor.add(card);
            }
        }
        return possibleColor;
    }

    private ArrayList<Card> getKnownOf(String color, Player player) {
        ArrayList<Card> knownColor = new ArrayList<>();
        for (Card card : getKnownCards(player)) {
            if (Objects.equals(card.getColor(), color)) {
                knownColor.add(card);
            }
        }
        return knownColor;
    }

    private void update() {
        Log.i("analysis update", "hi");
        for (Card card : cardSet.getCards()) {
            int N = 4;
            if (!card.isPlayed()) {
                for (Player player : players) {
                    if (!getPossibleCards(player).contains(card)) {
                        N--;
                    }
                }
                if (N <= 1) {
                    for (Player player : players) {
                        if (getPossibleCards(player).contains(card) && !getKnownCards(player).contains(card)) {
                            player.getAnalysisKnownCards().add(card);
                            player.update();
                        }
                    }
                }
            }
        }

    }

    //////TODO create sets of compares


    //////////////////////HELPERS/////////////////


    private boolean analysisPlayerOwns(Card card) {
        return possiblyOwns(analysisPlayer, card);
    }

    private boolean analysisPlayerOwns(String name) {
        return analysisPlayerOwns(cardSet.getCard(name));
    }

    private boolean analysisPlayerOwns(ArrayList<Card> cards) {
        for (Card card : cards) {
            if (possiblyOwns(analysisPlayer, card)) {
                return true;
            }
        }
        return false;
    }

    private int analysisPlayerNumberOf(String color) {
        return getPossibleNumberOf(color, analysisPlayer);
    }

    private int trickSize() {
        return mCurrentTrick.getTrick().size();
    }

    private Card analysisPlayerHighestTrump() {
        return all.highest(all.trump(analysisPlayer.getHand())).get(0);
    }

    private int opponentMaxNumberOfTrump() {
        return all.trump(getPossibleCards(nextEnemy)).size() + all.trump(all.non(getPossibleCards(lastEnemy), all.trump(getPossibleCards(nextEnemy)))).size();

    }

    private int anyOpponentMaxNumberOfTrump() {
        if (MaxNumberOfTrump(nextEnemy) >= MaxNumberOfTrump(lastEnemy)) {
            return MaxNumberOfTrump(nextEnemy);
        } else return MaxNumberOfTrump(lastEnemy);

    }

    private int MaxNumberOfTrump(Player player) {

        return getPossibleNumberOf(trump, player);

    }

}
