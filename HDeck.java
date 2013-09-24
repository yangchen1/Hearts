import java.awt.*;
import javax.swing.*;

class HDeck extends Deck {

	final static int CARD_GAP = 20;

	public HDeck(){
		super();
	}

	public HDeck(boolean initialize){
		super(initialize);
	}

	public HDeck(HDeck c)
	{
		for(int i=0; i<c.getSize(); i++)
			deckOfCards.add(c.get(i));
	}

	public void addNewDeck()
	{
		for (int i = 0; i < 52; i++)
			deckOfCards.add(new Card(i));
	}

	public void dealTo(HDeck toDeck)      //moves a card from one deck to another
	{              							//used in beginning of game
		toDeck.add(deal());
	}

	public void dealTo(HDeck toDeck, int cardNo)
	{
		toDeck.add(deal(cardNo));
	}

	private int getSelected(int start)        //gets the cards selected
	{
		int count = start, pos = -1;
		while(count<deckOfCards.size() && pos==-1)
			if(deckOfCards.get(count).isSelected())
			{
				pos = count;
				return pos;
			}
			else
				count++;
		return pos;
	}

	public void passThreeCards(HDeck toDeck)      //passes three cards
	{
		int a, b, c;            //initialize 3 temp num vars
		a = getSelected(0);
		b = getSelected(a+1);
		c = getSelected(b+1);

		if (a!=-1 && b!=-1 && c!=-1){
			deckOfCards.get(a).select();        //deselects the cards
			deckOfCards.get(b).select(); 
			deckOfCards.get(c).select(); 

			dealTo(toDeck, a);
			dealTo(toDeck, b-1);          //account for the removed cards
			dealTo(toDeck, c-2);
		}
	}

	public void playCard(HDeck toDeck, DrawCardsPlayed draw, int cardNo)         //moves played card to played card pile
	{
		deckOfCards.get(cardNo).turnFaceUp(true);
		toDeck.add(deal(cardNo));                  //add the card
		draw.repaint();                 //repaints it after you choose which players playing
	}

	public void clear()
	{
		deckOfCards.clear();
	}

	public int getSize()
	{
		return deckOfCards.size();
	}

	public void setOrientation(String type)
	{
		if (type.equals("horizontal"))
			for(int i = 0; i < deckOfCards.size(); i++)
				deckOfCards.get(i).setH();
		else if (type.equals("vertical"))
			for(int i = 0; i < deckOfCards.size(); i++)
				deckOfCards.get(i).setV();
	}

	public boolean checkSuit(int suit)
	{
		for (int i = 0; i < deckOfCards.size(); i++)
			if (deckOfCards.get(i).getHeartsSuit()==suit)
				return true;
		return false;   
	}

	public void combSort()
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
				if ((deckOfCards.get(i)).compareCardTo(deckOfCards.get(i + gap)) > 0)     //compare pos 0 to pos gap, if greater than 0
				{
					swap(i, i + gap);                                               //swap i and gap
					swaps = 1;                                                     //adds to swap count
				}
				i++;                                        //i increments
			} while (!(i + gap >= deckOfCards.size()));     //repeat if i+gap < deck size
		} while (!(gap == 1 && swaps == 0));                //repeat if gap != 1 or swaps != 0
	}

	public void selectCard(int cardNo)      
	{
		if(canSelect() || deckOfCards.get(cardNo).isSelected())   //deselects if it was already selected
			deckOfCards.get(cardNo).select();   
	}
	

	private boolean canSelect()        //checks to see if 3 cards are already selected
	{
		int count = 0;
		for (int i=0; i<deckOfCards.size(); i++)
			if(deckOfCards.get(i).isSelected())
				count++;
		if (count < 3)    
			return true;
		else
			return false;
	}

	public boolean threeSelected()  //checks if three are selected
	{
		int count = 0;
		for (int i=0; i<deckOfCards.size(); i++)
			if(deckOfCards.get(i).isSelected())
				count++;
		if (count == 3)    
			return true;
		else
			return false;
	}

	public void showSelectedCards(Graphics g, int x, int y, int pos, String displayType)
	{  
		setOrientation(displayType);			 //changes all the cards in that hand to one orientation
		if(displayType.equals("horizontal"))
			for (int i = 0; i < deckOfCards.size(); i++)
				if(!deckOfCards.get(i).isSelected())
					deckOfCards.get(i).showCard(g, x + i*CARD_GAP, y);
				else                //draw them higher if selected
					deckOfCards.get(i).showCard(g, x + i*CARD_GAP, y-5);
		else 
			for (int i = 0; i < deckOfCards.size(); i++)
				if(!deckOfCards.get(i).isSelected())
					deckOfCards.get(i).showCard(g, x, y + i*CARD_GAP);
				else                //draw them higher if selected
					deckOfCards.get(i).showCard(g, x + 5, y + i*CARD_GAP);
	}

	public void showDeckVertical(Graphics g, int x, int y)     //vertical orientation
	{        
		setOrientation("vertical");        //changes all the cards in that hand to vertical
		for (int i = 0; i < deckOfCards.size(); i++)
			deckOfCards.get(i).showCard(g, x, y + i*CARD_GAP);      //shows the cards
	}

	public void showDeckHorizontal(Graphics g, int x, int y, int pos)  //draws with a card popping up (only for user)
	{
		setOrientation("horizontal");        //changes all the cards in that hand to horizontal
		if (pos == -1 || pos >= deckOfCards.size())   //this is if cards have been played
			for (int i = 0; i < deckOfCards.size(); i++)
				deckOfCards.get(i).showCard(g, x + i*CARD_GAP, y);
		else
			for (int i = 0; i < deckOfCards.size(); i++)
				if (i != pos)
					deckOfCards.get(i).showCard(g, x + i*CARD_GAP, y);     
				else 
					deckOfCards.get(i).showCard(g, x + i*CARD_GAP, y-5);     //draws selected card
	}
}