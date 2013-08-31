/*

Name: Jeff Quinn

Course: Programming Fundamentals: CS111B

CRN: 71755 

Assignment: 2

Date: 8/30/13

*/
package blackjack;
import java.util.Scanner;
import java.util.Stack;
import java.util.Collections;
import java.awt.Color;

enum Suits {
    heart, club, diamond, spade
};

enum Ranks {
    // Enum values are the string that is rendered for that card
    deuce("2"), three("3"), four("4"), five("5"), six("6"),
    seven("7"), eight("8"), nine("9"), ten("10"), jack("J"),
    queen("Q"), king("K"), ace("A");

    public String value;

    private Ranks(String value) {
	this.value = value;
    }
};



class Card {
    /*
    * A card object, holds the suit and rank
    */
    private Ranks rank;
    private Suits suit;
    public Card( Ranks rank, Suits suit ) {
	this.rank = rank;
	this.suit = suit;
    };

    public String toString() {
	return ("" + rank.value);
    };

    public Suits getSuit() {
	return suit;
    };
    public Ranks getRank() {
	return rank;
    };
};

class Deck {
    /*
    * Deck of cards using stack data structure
    */
    private java.util.Stack<Card> cards;
    private int n_decks;
    public Deck( int n_decks ) {
	Card card;
	this.n_decks = n_decks;
	shuffle_up();
    };

    private void shuffle_up() {
	cards = new java.util.Stack<Card>();
	for( int i = 0; i < n_decks; i++ ) {
	    for( Ranks rank: Ranks.values() ) {
		for( Suits suit: Suits.values() ) {
		    cards.push( new Card(rank, suit));
		};
	    };
	};
	Collections.shuffle(cards); // Shuffle cards
    };

    public Card draw() {
	if ( ! cards.empty() ) {
	    return cards.pop();
	} else {
	    // If the deck is exhausted, reshuffle
	    shuffle_up();
	    return cards.pop();
	}
    };
};
