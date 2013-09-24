import java.awt.Graphics;
import java.util.ArrayList;

public class Deck  
{ 
	protected ArrayList<Card> deckOfCards;                     //initialize arraylist of cards type Card

	public Deck()                                    //empty constructor
	{
		deckOfCards = new ArrayList<Card>();
	}

	public Deck(boolean initialize)                  //adds 52 cards if true
	{
		this();
		if (initialize)
		{
			for (int i = 0; i < 52; i++)
				deckOfCards.add(new Card(i));
		}
		else 
			deckOfCards.clear();
	}

	public void add(Card newCard)                    //adds a new card to the end of list
	{
		deckOfCards.add(newCard);
	}

	public int search (Card getThisCard, int startPos)             //searches for card in deck and returns first position
	{
		int posNum = -1;
		int i = startPos;                                                       //initialize counting variable
		while (posNum == -1 && i < deckOfCards.size())
		{
			if (getThisCard.equals(deckOfCards.get(i)))
				posNum = i;
			i++;
		}
		return posNum;
	}

	public void shuffle ()                           //removes a card randomly from deck and add it to the end
	{
		for (int i =0; i < deckOfCards.size()*4; i++)
		{
			int rand = (int)(Math.random()*(deckOfCards.size()-1));
			Card temp = deckOfCards.get(rand); 
			deckOfCards.remove(rand);
			deckOfCards.add(temp);
		}
	}

	public Card get(int pos)                        //get position
	{
		return deckOfCards.get(pos);
	}

	public Card deal()                              //deals the first card
	{
		Card dealtCard;         //temp variable of card dealt
		try{
			dealtCard = deckOfCards.get(0); 
			deckOfCards.remove(0);
			return dealtCard;
		}
		catch (Exception e)
		{}
		return null;
	}

	public Card deal(int pos)                       //gets position of card to be removed from
	{                                               //first card is 0
		Card dealtCard;
		try{          //if no card, nothing happens
			dealtCard = deckOfCards.get(pos);
			deckOfCards.remove(pos);                    //arraylist starts at 0
			return dealtCard;
		}
		catch(Exception e){
		}
		return null;
	}

	public void showDeck (Graphics g, int x, int y)  //draws a deck
	{
		for (int i = 0; i < deckOfCards.size(); i++)
			deckOfCards.get(i).showCard(g, x + i*12, y);
	}

	public void turnFaceUp(boolean face)                          //turns entire deck face up or face down
	{
		for (int i = 0; i < deckOfCards.size(); i++)
			deckOfCards.get(i).turnFaceUp(face);
	}

	//sort
	public void swap(int index1, int index2)
	{
		Card tempCard1 = deckOfCards.get(index1);                 //create two temporary cards
		Card tempCard2 = deckOfCards.get(index2);
		deckOfCards.set(index1, tempCard2);                   //swaps the two cards
		deckOfCards.set(index2, tempCard1);
	}

	public void combSort ()                             //sorts the deck using combsort
	{
		int gap = deckOfCards.size();
		int swaps;// = 0;

		do
		{
			gap = (int)(gap / 1.247330950103979);         //set the gap 
			if (gap < 1)                                  //set gap to 1 if it is less than 1
				gap = 1;
			int i = 0;
			swaps = 0;

			do                             
			{
				if ((deckOfCards.get(i)).compareHeartsRankTo(deckOfCards.get(i + gap)) > 0)     //compare pos 0 to pos gap, if greater than 0
				{
					swap(i, i + gap);                                               //swap i and gap
					swaps = 1;                                                     //adds to swap count
				}
				i++;                                        //i increments
			} while (!(i + gap >= deckOfCards.size()));     //repeat if i+gap < deck size
		} while (!(gap == 1 && swaps == 0));                //repeat if gap != 1 or swaps != 0
	}

}//end of deck class