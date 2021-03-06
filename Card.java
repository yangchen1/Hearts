import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Card
{
	private int rank;                                       //initialize global datafields (rank, suit, face up/down, image)
	private int suit;                                       //spades = 1, hearts = 2, diamonds = 3, clubs = 4;
	private boolean faceDirection;
	private Image cardImage;
	private Image cardBack;
	private boolean hOrientation;                        //horizontal orientation
	private boolean isSelected;
	private int heartsRank;       //rank and suit system in Hearts
	private int heartsSuit;

	public Card ()                                  //Initializing constructor
	{
		this(1,1);
	}

	public Card (int r, int s)                     //retrieve card based on rank and suit
	{
		if(errorCheck(r,s))
		{
			rank = r;
			suit = s;
			convertSuit();
			convertRank();
			faceDirection = false;
			hOrientation = true;
			isSelected = false;
			loadCardImage();
			loadCardBack();
		}
	}

	public Card (int cardNo)                       //retrieve card based on 1-51
	{
		this ((cardNo % 13 + 1), (cardNo / 13 + 1));
	}


	public boolean errorCheck()
	{
		return errorCheck(getRank(), getSuit());
	}

	public boolean errorCheck (int cardNo)             
	{
		return errorCheck((cardNo % 13 + 1), (cardNo / 13 + 1));
	}

	public boolean errorCheck (int r, int s)
	{
		if (r < 1 || r > 13 || s < 1 || s > 4)            //error check if it's a card
		{
			JOptionPane.showMessageDialog (null, "Incorrect Input", "Error", JOptionPane.ERROR_MESSAGE);
			rank = 1;
			suit = 1;
			return false;
		}
		else
			return true;
	}

	public void setH()                   //changes the orientation of card (seen only from the back)
	{
		hOrientation = true;
		loadCardBack();
	}

	public void setV()                   //changes the orientation of card (seen only from the back)
	{
		hOrientation = false;
		loadCardBack();
	}

	public void select()
	{
		isSelected = !isSelected;
	}

	public boolean isSelected()
	{
		if(isSelected)
			return true;
		else
			return false;
	}

	private void convertRank()
	{
		heartsRank = rank;
		if (heartsRank == 1)
			heartsRank = 14;
	}

	private void convertSuit()
	{
		switch(suit)
		{
		case 1:
			heartsSuit = 3;				//spades
			break;
		case 2: 
			heartsSuit = 4;				//hearts
			break;
		case 3:
			heartsSuit = 2;				//diamonds
			break;
		case 4: 
			heartsSuit = 1;				//clubs
			break;
		}
	}

	public int getHeartsRank()
	{
		return heartsRank;
	}

	public int getHeartsSuit()
	{
		return heartsSuit;
	}

	public int getRank ()                         //return 1-13
	{
		return rank;
	}

	public int getSuit ()                        //return shdc
	{
		return suit;
	} 

	public int getHeartsCardRank()    //used for sorts and AIs
	{
		return(heartsRank + (heartsSuit-1)*13); //out of 53, 2 of C = 2, A of H = 53
	}

	public int getCardRank()
	{
		return (rank + (suit-1)*13);	//2 of C = 52, A of S = 1
	}

	public void turnFaceUp (boolean turn)       //turns it to face up
	{
		faceDirection = turn;
	}

	private String getCardImageName ()
	{
		String rankName, suitName;                  //sets the name to call images
		switch (rank)
		{
		case 1:
			rankName = "a";
			break;
		case 10:
			rankName = "t";
			break;
		case 11:
			rankName = "j";
			break;
		case 12:
			rankName = "q";
			break;
		case 13:
			rankName = "k";
			break;
		default:
			rankName = Integer.toString (rank);
			break;
		}
		switch (suit)
		{
		case 1:
			suitName = "s";
			break;
		case 2:
			suitName = "h";
			break;
		case 3:
			suitName = "d";
			break;
		case 4:
			suitName = "c";
			break;
		default:
			suitName = "";
			break;
		}
		return (rankName + suitName);
	}

	private void loadCardImage ()                      //loads a single image
	{
		cardImage = null;
		try
		{
			cardImage = ImageIO.read (new File ("cards\\" + getCardImageName() + ".jpg"));        //load file into Image object, uses file path
		}
		catch (IOException e)
		{
		}
	}

	private void loadCardBack ()
	{
		cardBack = null;
		try
		{
			if (hOrientation)                  //loads the normal back
				cardBack = ImageIO.read (new File ("cards\\b.jpg"));                            //load file into Image object 
			else
				cardBack = ImageIO.read(new File("cards\\br.jpg"));        //loads a rotated back
		}
		catch (IOException e)
		{
		}
	}

	public String toString ()          //converts name to string
	{
		return getCardImageName();
	}

	public int compareHeartsRankTo(Card card1)
	{
		return (getHeartsRank() - card1.getHeartsRank());                                                   //compares rank of two cards
	}

	/*public int compareTo(Card card1)
  {
    return (getCardRank() - card1.getCardRank());
  }*/

	public int compareCardTo(Card card1)								//used for hearts game
	{
		return (getHeartsCardRank() - card1.getHeartsCardRank());
	}

	public boolean equals(Card card1)                                                         //overrides default equals in order to compare two cards
	{
		if ((getHeartsRank() == card1.getHeartsRank()) && (getHeartsSuit() == card1.getHeartsSuit()))
			return true;
		else 
			return false;
	}

	public void showCard (Graphics g, int x, int y)          //Draw image from graphics context passed to show method, draws onto certain panel
	{
		if (faceDirection)
			g.drawImage (cardImage, x, y, null);
		else
			g.drawImage (cardBack, x, y, null);
	}

	public void showPlayedCard(Graphics g, int x, int y)   //draws one card faceup 
	{
		faceDirection = true;
		g.drawImage(cardImage, x, y, null);
	}
}
