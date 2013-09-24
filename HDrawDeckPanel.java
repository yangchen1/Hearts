import java.awt.*;

import javax.swing.*;

public class HDrawDeckPanel extends JPanel{
	private HDeck hand;
	private String type;                                   							  //sets the type of display
	private int pos;
	private static final int borderSpace = 27;
	private final static  Color bkgrndCol = new Color(100, 255, 100);

	public HDrawDeckPanel()                                                         //separate panel for drawing
	{
		this.setPreferredSize (new Dimension(300,300));                            //sets size for default constructor
		type = "horizontal";
		pos = -1;
	}

	public HDrawDeckPanel(HDeck hand)									
	{        
		this(hand, "horizontal");
	}

	public HDrawDeckPanel(Dimension size, HDeck hand, String type)
	{
		this(hand, type);
		this.setPreferredSize (size);                       			//sets size                                                            //gets the deck
	}

	public HDrawDeckPanel(HDeck hand, String type)						//all the other constructors passes on to this one
	{
		this.hand = new HDeck();
		this.hand = hand;
		this.type = type;
		pos = -1;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void highlight(int pos)
	{
		this.pos = pos;
	}
	
	public void unHighlight()
	{
		pos = -1;
	}

	public void paintComponent (Graphics g)                                         //paints the deck
	{
		super.paintComponent(g);
		setBackground(bkgrndCol);
		if (HeartsGUI.isPassPhase())												//cards stay up during getPhase
			if (type.equals("horizontal"))
				hand.showSelectedCards(g, 250, borderSpace, pos, type);
			else
				hand.showSelectedCards(g, borderSpace, 0, pos, type);
		else
			if (type.equals("horizontal"))
				hand.showDeckHorizontal (g, 250, borderSpace, pos);						//-1 means no card pops up
			else
				hand.showDeckVertical(g, borderSpace, 0);
	}
}
