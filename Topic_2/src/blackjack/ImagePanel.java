/*

Name: Jeff Quinn

Course: Programming Fundamentals: CS111B

CRN: 71755 

Assignment: 2

Date: 8/30/13

*/
package blackjack;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
import java.awt.EventQueue;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import blackjack.Card;
import blackjack.Suits;
import blackjack.Ranks;
import blackjack.BlackJackGame;

class ImagePanel extends JPanel {
    /*
    * Top level panel for the blackjack game.
    */
    private Image table;
    private BlackJackGame game = new BlackJackGame();
    private Image suits;
    private JButton hitBtn = new JButton("hit");
    private JButton stayBtn = new JButton("stay");
    private JPanel hitPanel;
    private JPanel stayPanel;
    private Point p1 = new Point(333, 230); //initalized to where hero will be dealt
    private boolean playing = false;        // when this is true, hero will be dealt
    private boolean dealer_playing = false; // when this is true, dealer will be dealt
    private boolean is_new_hand = false;    // when this is true, all buttons will start new hand
    private boolean refresh = false;        // when this is true repaint will refresh board
					    // and start new hand

    public ImagePanel(Image table, Image suits) {
	/*
	* Construct the board
	* Add background image, and side panels for buttons
	* Add listeners for buttons
	*/
	this.table = table;
	this.suits = suits;
	Dimension size = new Dimension(table.getWidth(null), table.getHeight(null));
	setPreferredSize(size);
	setMinimumSize(size);
	setMaximumSize(size);
	setSize(size);
	setLayout(new BorderLayout());
        hitBtn.addActionListener(
	    new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent evt) {
		    hit_me();
		}
	});

        stayBtn.addActionListener(
	    new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent evt) {
		    stay();
		}
	});
	hitPanel = new JPanel( new BorderLayout() );
	hitPanel.add( hitBtn, BorderLayout.SOUTH );
	hitPanel.setOpaque(true);
	stayPanel = new JPanel( new BorderLayout() );
	stayPanel.add( stayBtn, BorderLayout.SOUTH );
	stayPanel.setOpaque(true);
	this.add( hitPanel, BorderLayout.EAST );
	this.add( stayPanel, BorderLayout.WEST );
    }

    private void hit_me() {
	// Method for hit button
	if (is_new_hand) {
	    new_hand();
	    is_new_hand = false;
	} else {
	    playing = true;
	    repaint();
	    increment_deal_position();
	};
    };

    private void stay() {
	// Method for stay button
	if (is_new_hand) {
	    new_hand();
	    is_new_hand = false;
	} else {
	    dealer_playing = true;
	    repaint();
	};
    };

    private void increment_deal_position() {
	p1.x += 23;
    };

    private void reset_deal_position() {
	p1.x = 333; p1.y = 230;
    };

    private void switch_dealer() {
	p1.x = 333; p1.y = 40;
    };

    private void new_hand() {
	// Refresh board, deal new hand
	playing = false;
	dealer_playing = false;
	this.removeAll();
	this.add( hitPanel, BorderLayout.EAST );
	this.add( stayPanel, BorderLayout.WEST );
	validate();
	refresh = true;
	repaint();
	game.new_hand();
	reset_deal_position();
    };

    public void paintComponent(Graphics g) {
	Card card;
	if( refresh ) {
	    //Deal new hand
	    refresh = false;
	    g.drawImage(table, 0, 0, null);
	    card = game.hit_hero();
	    drawCard(g, card);
	    increment_deal_position();
	    card = game.hit_hero();
	    drawCard(g, card);
	    if( game.is_bust() ) {
		g.setColor(Color.white);
		g.setFont(new Font("SansSerif", Font.BOLD, 20));
		g.drawString("BUST" + " " + game.calculate_hero_score(), 333, 150);
		is_new_hand = true;
	    } else if ( game.is_blackjack() ) {
		g.setColor(Color.white);
		g.setFont(new Font("SansSerif", Font.BOLD, 20));
		g.drawString("BLACKJACK", 333, 150);
		is_new_hand = true;
	    };

	} else if ( playing ) {
	    // Hit hero
	    playing = false;
	    card = game.hit_hero();
	    drawCard(g, card);
	    if( game.is_bust() ) {
		g.setColor(Color.white);
		g.setFont(new Font("SansSerif", Font.BOLD, 20));
		g.drawString("BUST" + " " + game.calculate_hero_score(), 333, 150);
		is_new_hand = true;
	    } else if ( game.is_blackjack() ) {
		g.setColor(Color.white);
		g.setFont(new Font("SansSerif", Font.BOLD, 20));
		g.drawString("BLACKJACK", 333, 150);
		is_new_hand = true;
	    };
	} else if( dealer_playing ) {
	    // Hit dealer until bust or hand > 17
	    dealer_playing = false;
	    switch_dealer();
	    while( game.will_dealer_play() ) {
		card = game.hit_dealer();
		drawCard(g, card);
		increment_deal_position();
	    };
	    g.setColor(Color.white);
	    g.setFont(new Font("SansSerif", Font.BOLD, 20));
	    switch ( game.outcome() ) {
		case 0:
		    g.drawString("HOUSE WINS", 333, 150);
		    break;
		case 1:
		    g.drawString("PUSH", 333, 150);
		    break;
		case 2:
		    g.drawString("WINNER", 333, 150);
		    break;
		default:
		    g.drawString("WINNER", 333, 150);
		    break;
	    };
	    is_new_hand = true;
	};
    }

    public void drawCard(Graphics g, Card c) {
	// render a card on the board
	// based on the card rank and suit
	g.setColor(Color.white);
	g.fillRoundRect(p1.x, p1.y, 35, 50, 10, 10);
	g.setColor(Color.black);
	g.drawRoundRect(p1.x, p1.y, 35, 50, 10, 10);
	if( c.getSuit() == Suits.heart || c.getSuit() == Suits.diamond ) {
	    g.setColor(Color.red);
	} else {
	    g.setColor(Color.black);
	};
	g.drawString(c.toString(), p1.x+6, p1.y+13);
	// This is for chopping up the source image
	// Which is a single gif that has the 4 suits on it
	int srcx1, srcx2, srcy1, srcy2;
	switch (c.getSuit()) {
	    case heart:
		srcx1 = 40; srcy1 = 0; srcx2 = 80; srcy2 = 40;
		break;
	    case diamond:
		srcx1 = 0; srcy1 = 40; srcx2 = 40; srcy2 = 80;
		break;
	    case spade:
		srcx1 = 40; srcy1 = 40; srcx2 = 80; srcy2 = 80;
		break;
	    case club:
		srcx1 = 0; srcy1 = 0; srcx2 = 40; srcy2 = 40;
		break;
	    default:
		srcx1 = 40; srcy1 = 40; srcx2 = 80; srcy2 = 80;
		break;
	};
	g.drawImage(this.suits, p1.x+6, p1.y+16, p1.x+17, p1.y+27, srcx1, srcy1, srcx2, srcy2 , null);
    };

    private void display() {
	// Initalize display
	JFrame f = new JFrame("BlackJack");
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.add(this);
	f.pack();
	f.setLocationRelativeTo(null);
	f.setVisible(true);
	new_hand();
    };

    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {

	@Override
	public void run() {
	    URL table = ImagePanel.class
		  .getResource("/images/table.png");
	    URL suits = ImagePanel.class
		  .getResource("/images/suits.gif");
	    new ImagePanel(new ImageIcon(table).getImage(), new ImageIcon(suits).getImage()).display();
	};
	});
    }

}

