package blackjack;
import blackjack.*;
import blackjack.Ranks;
import java.util.ArrayList;
import java.util.EnumMap;

class BlackJackGame {

    private Deck deck;
    private ArrayList<Card> hero_cards = new ArrayList<Card>();
    private ArrayList<Card> dealer_cards = new ArrayList<Card>();
    private EnumMap<Ranks, Integer> RankMap = new EnumMap<Ranks, Integer>(Ranks.class);

    public BlackJackGame() {
	deck = new Deck(4);
	RankMap.put(Ranks.deuce, 2);
	RankMap.put(Ranks.three, 3);
	RankMap.put(Ranks.four, 4);
	RankMap.put(Ranks.five, 5);
	RankMap.put(Ranks.six, 6);
	RankMap.put(Ranks.seven, 7);
	RankMap.put(Ranks.eight, 8);
	RankMap.put(Ranks.nine, 9);
	RankMap.put(Ranks.ten, 10);
	RankMap.put(Ranks.jack, 10);
	RankMap.put(Ranks.queen, 10);
	RankMap.put(Ranks.king, 10);
	RankMap.put(Ranks.ace, 11);
    };

    public void new_hand() {
	hero_cards.clear();
	dealer_cards.clear();
    };

    private int calculate_score (ArrayList<Card> hand) {
	int score = 0;
	int aces = 0;
	int aces_as_ones = 0;
	int usable_aces_as_ones = 0;
	for (Card card : hand) {
	    if (card.getRank() == Ranks.ace)
		aces++;
	};
	while(true) {
	    usable_aces_as_ones = aces_as_ones;
	    score = 0;
	    for (Card card : hand) {
		if (card.getRank() == Ranks.ace) {
		    if ( usable_aces_as_ones > 0 ) {
			score += 1;
			usable_aces_as_ones--;
		    } else {
			score += RankMap.get(card.getRank());
			continue;
		    };
		} else {
		    score += RankMap.get(card.getRank());
		};
	    };
	    if( score > 21 && aces_as_ones < aces) {
		aces_as_ones++;
		continue;
	    };
	    break;
	};
	return score;
    };

    public int calculate_dealer_score() {
	return calculate_score(dealer_cards);
    };

    public int calculate_hero_score() {
	return calculate_score(hero_cards);
    };

    public boolean will_dealer_play() {
	if (calculate_dealer_score() <= 17)
	    return true;
	else
	    return false;
    };

    public int outcome() {
	if( calculate_dealer_score() > 21 ) {
	    return 2;
	} else if( calculate_dealer_score() < calculate_hero_score() ) {
	    return 2;
	} else if( calculate_dealer_score() > calculate_hero_score() ) {
	    return 0;
	} else {
	    return 1;
	}
    };

    public boolean is_bust() {
	if( calculate_hero_score() > 21 ) {
	    return true;
	} else {
	    return false;
	}
    };

    public boolean is_blackjack() {
	boolean jack = false;
	boolean ace = false;
	if ( hero_cards.size() != 2 )
	    return false;
	for (Card card : hero_cards) {
	    if(RankMap.get(card.getRank()) == 11)
		ace = true;
	    if(RankMap.get(card.getRank()) == 10)
		jack = true;
	};
	if( jack && ace)
	    return true;
	else
	    return false;
    };

    public Card hit_hero() {
	Card new_card = deck.draw();
	hero_cards.add(new_card);
	return new_card;
    };

    public Card hit_dealer() {
	Card new_card = deck.draw();
	dealer_cards.add(new_card);
	return new_card;
    };

};
