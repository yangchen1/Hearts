import java.awt.*;

import javax.swing.*;

public class DrawCardsPlayed extends JPanel {
  private HDeck middleDeck;               //displays card played
  private String type;
  boolean clear;
  private final static Color bkgrndCol = new Color(100, 255, 100);
  
  public DrawCardsPlayed(Dimension size, HDeck middleDeck, String type)       //draws a single card
  {
    this.middleDeck = new HDeck();
    this.middleDeck = middleDeck;
    this.type = type;
    this.setPreferredSize(size);
    clear = false;
  }
  
  public DrawCardsPlayed(Dimension size)
  {
    this.setPreferredSize(size);
    type = "null";
    clear = false;
  }
  
  public void clear()
  {
    clear = true;
    repaint();
  }
  
  public void paintComponent (Graphics g)                                         //paints the deck
  {
	  if (clear){
		  super.paintComponent(g);
		  setBackground(bkgrndCol);
		  clear = false;
	  }
	  else if (type != null)
	  {
		  try{
			  super.paintComponent(g);            //clears the panel
			  setBackground(bkgrndCol);
			  if (type.equals("h"))
				  middleDeck.get(middleDeck.getSize()-1).showCard(g, 200, 0);               //paints it at different places depending on the position   
			  else if (type.equals("e"))
				  middleDeck.get(middleDeck.getSize()-1).showCard(g, 0, 0);    
			  else if (type.equals("w"))
				  middleDeck.get(middleDeck.getSize()-1).showCard(g, 50, 0);
		  }
		  catch(ArrayIndexOutOfBoundsException e){}
	  }
  }
}
