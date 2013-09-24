import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

public class HeartsGUI extends JPanel implements MouseListener, MouseMotionListener {
 private HDeck cpu1, cpu2, cpu3, player;     //players and cpu
 private HDeck northDeck, eastDeck, southDeck, westDeck, mDeck;      //keep track of cards that player played
 private HDeck[] cardsWon; //keep track of cards that players have won from a trick
 private HeartsAI c3, c2, c1;
 private JPanel playArea, inst, btnPanel;
 private JLabel help;   //displays instructions and warnings
 private JButton passBtn;
 private DrawCardsPlayed[] middleArea;  //used to display played cards
 private HDrawDeckPanel south, west, north, east;
 public static boolean isPassing, firstTurn, endHand;       //decides which phase of game it's in 
 private boolean canClick, newGameReset;
 private int whichCard, prevCard;          //stores the card number that is selected or highlighted and the previous card
 private int posCard, counter, lead; //a counter to keep track of who have played
 private final Dimension hSize = new Dimension (300, 150), vSize = new Dimension (150, 320), mSize = new Dimension (300, 300);  //panel size
 private final static int hStart = 250,  gap = 20;
 private Round turn;  //chooses who leads and whose turn it is
 private Timer t;
 private Points pointFinal;
 private JFrame loading;
 private int[] p;
 private int[] pTot;
 private String pointsDisplay;

 public HeartsGUI()
 {
  playArea = new JPanel();
  inst = new JPanel();
  help = new JLabel("Choose 3 cards to pass to your left-hand player.");
  btnPanel = new JPanel();
  passBtn = new JButton(" Pass ");

  cpu1 = new HDeck(false);        //initialize the decks and AIs
  cpu2 = new HDeck(false);
  cpu3 = new HDeck(false);
  c3 = new HeartsAI();
  c2 = new HeartsAI();
  c1 = new HeartsAI();
  cpu1.turnFaceUp(true);
  cpu2.turnFaceUp(true);
  cpu3.turnFaceUp(true);
  mDeck = new HDeck(true);
  player = new HDeck(false);
  northDeck = new HDeck(false);
  westDeck = new HDeck(false);
  southDeck = new HDeck(false);
  eastDeck = new HDeck(false);
  cardsWon = new HDeck[4];
  for (int i = 0; i<4; i++)
   cardsWon[i] = new HDeck(false);
  west = new HDrawDeckPanel(vSize, cpu1, "vertical");             //(x, y, width, height, deck, displaytype)
  north = new HDrawDeckPanel(hSize, cpu2, "horizontal");
  east = new HDrawDeckPanel(vSize, cpu3, "vertical");
  south = new HDrawDeckPanel(hSize, player, "horizontal");
  pointFinal = new Points(); //initialize the points
  p = new int[4];
  pTot = new int[4];

  whichCard = -1;             //initalize vars
  isPassing = false;  

  setLayout(new BorderLayout());   //sets the properties of this jpanel
  add(playArea, "North");
  add(inst, "South");

  Color bkgrndCol = new Color(100, 255, 100);
  btnPanel.setBackground(bkgrndCol);  //set background to green
  east.setBackground(bkgrndCol);
  north.setBackground(bkgrndCol);
  west.setBackground(bkgrndCol);
  south.setBackground(bkgrndCol);
  setBackground(bkgrndCol);
  inst.setBackground(bkgrndCol);
  help.setFont(new Font("ARIAL", Font.BOLD, 20));

  middleArea = new DrawCardsPlayed[5];
  middleArea[1] = new DrawCardsPlayed(new Dimension(150,100), westDeck, "w");       //panels to show cards played; h = horizontal
  middleArea[2] = new DrawCardsPlayed(new Dimension(300, 100), northDeck, "h");           
  middleArea[4] = new DrawCardsPlayed(new Dimension(300, 120), southDeck, "h");
  middleArea[3] = new DrawCardsPlayed(new Dimension(150, 100), eastDeck, "e");
  middleArea[0] = new DrawCardsPlayed(mSize);
  middleArea[0].setLayout(new BorderLayout());
  middleArea[0].add(btnPanel, "South");
  for (int i=0; i<5; i++)
   middleArea[i].setBackground(bkgrndCol);
  btnPanel.add(passBtn);
  passBtn.setPreferredSize(new Dimension(80, 30));
  passBtn.addActionListener(new PassBtnListener());

  playArea.setLayout(new BorderLayout());         //creates playarea
  playArea.add(south, "South");           //creates the four hands and center deck 
  playArea.add(west, "West");            //for cards played
  playArea.add(north, "North");
  playArea.add(east, "East");
  playArea.add(middleArea[0], "Center");
  inst.setLayout(new BoxLayout(inst, BoxLayout.X_AXIS));
  inst.add(help);

  south.addMouseListener (this);    //add listener
  playTimer cpuPlay = new playTimer ();    //ActionListener
  t = new Timer (500, cpuPlay);       //set up timer
  passPhase();
 }



 //start playing
 public static boolean isPassPhase()           //returns if it's in passing phase of game
 {
  if(isPassing)
   return true;
  else
   return false;
 }

 private void passPhase()            //pass cards
 { 
  t.stop();      //stops the timer to pass cards
  dealDeck(mDeck, cpu1, cpu2, cpu3, player);        //deal the cards
  player.combSort();
  cpu1.combSort();
  cpu2.combSort();
  cpu3.combSort();
  south.repaint();
  east.repaint();
  north.repaint();
  west.repaint();

  player.turnFaceUp(true);             //turns player's cards face up
  isPassing = true;
  endHand = false;     //true when a hand ends and a new one is initialized
  passBtn.setEnabled(false);           //disables passing btn

  //Computer finds best cards to pass
  for (int q=1; q<=3; q++)
  {
   int i, i2, i3;

   i = c1.passPos(cpu1);  //Find highest Card to pass
   cpu1.selectCard(i);
   i2 = c2.passPos(cpu2);  //Find highest Card to pass
   cpu2.selectCard(i2);
   i3 = c3.passPos(cpu3);  //Find highest Card to pass
   cpu3.selectCard(i3);
  }
  south.requestFocusInWindow();
 }

 private void passCards()
 {
  player.passThreeCards(cpu1);           //passes three cards
  cpu1.passThreeCards(cpu2);
  cpu2.passThreeCards(cpu3);
  cpu3.passThreeCards(player);

  cpu1.turnFaceUp(false);                //turn all cards face up for player and face down for cpu1
  player.turnFaceUp(true);
  cpu1.combSort();                       //sort all the hands
  cpu2.combSort();
  cpu3.combSort();
  player.combSort();

  east.repaint();        //resets orientation
  north.repaint();
  west.repaint();
  south.repaint();

  startGame();
 }

 private void startGame()            //play cards
 {
  isPassing = false;
  canClick = false;
  HeartsAI.setBroken(false);
  middleArea[0].remove(btnPanel);
  middleArea[0].add(middleArea[2], "North");      //adds the panels to display the cards
  middleArea[0].add(middleArea[4], "South");
  middleArea[0].add(middleArea[3], "East");
  middleArea[0].add(middleArea[1], "West");

  middleArea[0].revalidate();
  middleArea[0].repaint();
  south.addMouseMotionListener(this);   
  south.requestFocusInWindow();

  help.setText("");

  int f;
  if (findFirst(cpu1))                   //finds 2 of c
   f = 1;
  else if (findFirst(cpu2))
   f = 2;
  else if (findFirst(cpu3))
   f = 3;
  else
   f = 4;
  turn = new Round(f);
  fPlay(f);
 }

 public void startTimer()
 {
  counter = 0;
  t.restart ();  //start simulation
 }

 class playTimer implements ActionListener
 {
  public void actionPerformed (ActionEvent event)  //advances whenever action happens
  {
   counter++;
   if(endHand){
    newGame(newGameReset);
    closeBox();
   }
   else if (counter == 5){   //sets the pause after a round
    counter = -1;     //end round
    endRound();
   }
   else if (counter < 4 && counter >= 0)
    nextPlay();
  }
 }


 private void fPlay(int n)            //first play
 {
  startTimer();
  firstTurn = true;    //first round has to play 2 of c
  lead = n;
  if (n==1)
  {
   cpu1.playCard(westDeck, middleArea[1], 0); //play the card
   addToMDeck(westDeck); //add it to the middle deck
   west.repaint(); //repaint the hand
  }
  else if (n==2)
  {
   cpu2.playCard(northDeck, middleArea[2], 0);
   addToMDeck(northDeck);
   north.repaint();
  }
  else if (n==3)
  {
   cpu3.playCard(eastDeck, middleArea[3], 0);
   addToMDeck(eastDeck);
   east.repaint(); 
  }
  else 
   canClick();

  if (n < 4)       //splay does not need to advance
   turn.next();
 }  

 private void nextPlay()
 {
  if (turn.get() == 1)
   wPlay();
  else if (turn.get() == 2)
   nPlay();
  else if (turn.get() == 3)
   ePlay();
  else if (turn.get() == 4)
   canClick();
 }

 private void wPlay()                     //play west cpu
 {
  posCard = c1.pos(cpu1, mDeck);               //finds best card to play
  cpu1.playCard(westDeck, middleArea[1], posCard);   //play the card
  addToMDeck(westDeck);     //adds to middle deck
  turn.set(2);  //set the turn to north
  west.repaint(); //repaint hand area
  middleArea[1].repaint();   
 }

 private void nPlay()                     //play north cpu
 {
  posCard = c2.pos(cpu2, mDeck);                       //finds best card to play
  cpu2.playCard(northDeck, middleArea[2], posCard); //play the card
  addToMDeck(northDeck);   //adds to middle deck
  turn.set(3); //set the turn to east
  north.repaint();  //repaint hand area
  middleArea[2].repaint();
 }

 private void ePlay()                          //play east cpu
 {
  posCard = c3.pos(cpu3, mDeck);                       //finds best card to play
  cpu3.playCard(eastDeck, middleArea[3], posCard); //play the card
  addToMDeck(eastDeck); //adds to middle deck
  turn.set(4); //set the turn to east
  east.repaint();    //repaint hand area
  middleArea[3].repaint();
 }

 private void sPlay()   //only runs when clicked
 {
  boolean canPlay;
  int leadSuit;    //suit led
  if (lead != 4)
   leadSuit = mDeck.get(0).getHeartsSuit(); //get the suit of first card led
  else
   leadSuit = -1;

  if (whichCard < player.getSize() && whichCard != -1)   //messages for bottom of screen  
  {
   canPlay = false;
   if (firstTurn == true && lead == 4 && whichCard != 0)   //2 of C in first turn
    help.setText("You must lead the 2 of Clubs on the first turn.");  
   else if (firstTurn == false && lead == 4 && HeartsAI.getBroken() == false && player.get(whichCard).getHeartsSuit() == 4)    //lead and hearts not broken
    help.setText("You cannot lead Hearts until the suit is broken (a Heart is discarded).");
   else if (lead != 4 && player.checkSuit(leadSuit) == true && player.get(whichCard).getHeartsSuit() != leadSuit)  //follow same suit
    help.setText("You must follow suit.");
   else 
    canPlay = true;


   if (canPlay){   //only run if correct choice
    help.setText("");
    player.playCard(southDeck, middleArea[4], whichCard);     //states which player is playing and which card he plays, includes repaint of middle
    addToMDeck(southDeck);
    turn.set(1); //set the turn to west
    t.start();
    south.unHighlight();
    canClick = false;
    south.repaint();
    middleArea[4].repaint();
   }
  }
 }

 private void endRound()     //called when a round ends
 {
  firstTurn = false;

  turn.set(lead);          //sets the turn to original lead player
  lead = turn.findWinner(mDeck);  //finds the position of player who won trick

  addCards(cardsWon[lead-1]);   //add the cards to whoever won

  pointFinal.countPoints(cardsWon);   //Count points
  for (int i=0; i<4; i++)         
   p[i] = pointFinal.getPoints(i);        //Get the points from this round, store into 4 integer array
  System.out.println("" + p[0] + " " + p[1] + " " + p[2] + " " + p[3] + " " + pTot[0] + " " + pTot[1] + " " + pTot[2] + " " + pTot[3]);


  if (player.getSize() <= 0)    //when out of cards
   reset();
  else{   
   for (int i = 0; i < 5; i++)
    middleArea[i].clear();   //clear everything
   mDeck.clear();   //clear the middle deck
   middleArea[0].repaint();
   turn.set(lead);   //sets the lead to the correct player position
  }
 }

 private void reset()   //end of a hand
 {
  boolean gameover = false;
  int playerScore = pointFinal.getPoints(7);

  pointFinal.total();        //totals the points from previous hands
  for (int i = 0; i<4; i++)  
   pTot[i] = pointFinal.getPoints(i+4);         //Get the total points so far, store into 4 integer array
  pointsDisplay = "Player   Round   Total" + "\nWest:         "+ p[0] + "          " + pTot[0] + "\nNorth:        "+ p[1] + "           " + pTot[1] + "\nEast:          "+ p[2] + "           " + pTot[2] + "\nSouth:       "+ p[3] + "           " + pTot[3];

  for (int i=4; i<8; i++)     //if reached score limit
   if(pointFinal.getPoints(i) >= 100)
    gameover=true;

  if (gameover == false)//if nobody has reached 100 total points
  {
   JOptionPane.showMessageDialog(this,"This Hand is Over!\n" + pointsDisplay, "Score", JOptionPane.PLAIN_MESSAGE);     //display the points
   newGameReset = false;//do not reset the entire game
  }
  else //someone reached 100 total points; gameover
  {
   if (playerScore < pointFinal.getPoints(4) && playerScore < pointFinal.getPoints(5) && playerScore < pointFinal.getPoints(6))
    JOptionPane.showMessageDialog(this, "Congratulations! You won the game!", "Winner", JOptionPane.PLAIN_MESSAGE);     //display winning or losing message
   else
    JOptionPane.showMessageDialog(this, "You lost the game! Better luck next time!", "Loser", JOptionPane.PLAIN_MESSAGE); 
   JOptionPane.showMessageDialog(this, pointsDisplay, "Score", JOptionPane.PLAIN_MESSAGE);      
   newGameReset = true;
  }
  endHand = true;
  createBox();
  t.start();        //timer restarts the program after loading screen is displayed

 }

 private void addCards(HDeck won) //Add cards from the middle deck to the trick deck that belongs to a player
 {
  for (int n=0; n<=3; n++)//4 cards in a trick
   won.add(mDeck.get(n));//add it to the sent in deck
 }

 public void newGame(boolean resetScore)
 {
  if(resetScore)
   pointFinal.reset();
  else{
   for (int i = 0; i < 4; i++){    //reset score of that round
    cardsWon[i].clear();
    p[i] = 0;
   }
   pointFinal.newRound();
  }

  clearAllDecks();   //clears all decks
  mDeck.addNewDeck();

  for (int i = 1; i<5; i++)
   middleArea[0].remove(middleArea[i]);   //removes everything
  btnPanel.add(passBtn);
  middleArea[0].add(btnPanel, "South");   //adds btn panel
  middleArea[0].validate();
  west.repaint();       //repaints everything
  north.repaint();
  east.repaint();
  south.repaint();
  middleArea[0].repaint();
  passPhase();
 }

 public void createBox(){    //creates a loading screen
  loading = new JFrame();
  JPanel content = new JPanel();
  JLabel text = new JLabel("Loading... Please wait.");
  content.add(text);
  loading.setContentPane(content);
  loading.setVisible(true);
  loading.setPreferredSize(new Dimension(100, 80));
  loading.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  loading.pack();
  loading.setLocationRelativeTo(null);
 }

 public void closeBox(){     //closes loading screen
  loading.setVisible(false);
  loading.dispose();
 }

 public void showScore()        //display in a pop up box the scores
 {
  pointsDisplay = "Player   Round   Total" + "\nWest:         "+ p[0] + "          " + pTot[0] + "\nNorth:        "+ p[1] + "           " + pTot[1] + "\nEast:          "+ p[2] + "           " + pTot[2] + "\nSouth:       "+ p[3] + "           " + pTot[3];
  JOptionPane.showMessageDialog(this,"Score\n" + pointsDisplay, "Score", JOptionPane.PLAIN_MESSAGE);     //display the points
 }

 private void clearAllDecks() //clears all the decks to restart the game
 {
  mDeck.clear();
  westDeck.clear();
  northDeck.clear();
  eastDeck.clear();
  southDeck.clear();
  player.clear();
  cpu1.clear();
  cpu2.clear();
  cpu3.clear();
 }

 private void addToMDeck(HDeck deck) //add the played card to the middle deck
 {
  mDeck.add(deck.get(deck.getSize()-1));
 }

 private void canClick()   //called when it's the player's turn
 {
  t.stop();
  help.setText("Your turn.");
  canClick = true;
 }

 public boolean findFirst (HDeck compare)//Find the 2 of clubs to play first in a round
 {
  Card clubs2 = new Card(2, 4);//creates a 2 of clubs card
  boolean ret;
  ret = compare.get(0).equals(clubs2) ? true : false;
  return ret;
 }

 private void dealDeck (HDeck from, HDeck a, HDeck b, HDeck c, HDeck d)  //deals the deck evenly to four players
 {
  from.shuffle();
  for (int i=0; i<13; i++){
   from.dealTo(a);
   from.dealTo(b);
   from.dealTo(c);
   from.dealTo(d);
  }
 }

 private void getCard(MouseEvent e)          //converts mouse position to card num
 {
  if (e.getX() >= hStart && e.getX() < 300+(player.getSize())*gap && e.getY() >= 15 && e.getY() <= 115)
  {                  //limits x y coordinates, limit changes depending on size of deck
   if(e.getX() >= hStart && e.getX() < hStart+gap)      //change x coordinates to number (whichCard) and then calls play based on 
    whichCard = 0;              //that card
   else if (e.getX() >= hStart+gap && e.getX() < hStart+2*gap)
    whichCard = 1;
   else if (e.getX() >= hStart+2*gap && e.getX() < hStart+3*gap)
    whichCard = 2;
   else if (e.getX() >= hStart+3*gap && e.getX() < hStart+4*gap)
    whichCard = 3;
   else if (e.getX() >= hStart+4*gap && e.getX() < hStart+5*gap)
    whichCard = 4;
   else if (e.getX() >= hStart+5*gap && e.getX() < hStart+6*gap)
    whichCard = 5;
   else if (e.getX() >= hStart+6*gap && e.getX() < hStart+7*gap)
    whichCard = 6;
   else if (e.getX() >= hStart+7*gap && e.getX() < hStart+8*gap)
    whichCard = 7;
   else if (e.getX() >= hStart+8*gap && e.getX() < hStart+9*gap)
    whichCard = 8;
   else if (e.getX() >= hStart+9*gap && e.getX() < hStart+10*gap)
    whichCard = 9;
   else if (e.getX() >= hStart+10*gap && e.getX() < hStart+11*gap)
    whichCard = 10;
   else if (e.getX() >= hStart+11*gap && e.getX() < hStart+12*gap)
    whichCard = 11;
   else if (e.getX() >= hStart+12*gap && e.getX() < hStart+13*gap)
    whichCard = 12;
   if (e.getX() >= hStart+(player.getSize())*gap)      //check if it is the last card getting clicked
    whichCard = player.getSize()-1;
  }
  else 
   south.unHighlight();
 }


 public class PassBtnListener implements ActionListener            //pass the cards
 {                       //and starts second phase of game
  public void actionPerformed (ActionEvent e)
  {
   passCards();
  }
 }

 public void mouseClicked (MouseEvent e)     //used to get user's input
 {
  getCard(e);
  if (isPassing)       //distinguish phase
  {
   if (whichCard != -1)    //limits it to make it more efficient
   {
    player.selectCard(whichCard);     //selects card and repaint 
    south.repaint();
   }
   if(player.threeSelected()==true)
    passBtn.setEnabled(true);      //enables button
   else
    passBtn.setEnabled(false);
  }
  else if (canClick)
   sPlay();
 }

 public void mouseMoved (MouseEvent e)     //used to highlight the cards
 { 
  if (!isPassing && canClick)       //used only in playing phase
  {
   getCard(e);
   south.highlight(whichCard);           //highlights a card or not
   if (whichCard != -1 && whichCard != prevCard)
    south.repaint();
   prevCard = whichCard;
  }
 }


 public void mouseDragged (MouseEvent e)                                                       //mouse listener events not used
 {}

 public void mousePressed (MouseEvent e)
 {}

 public void mouseReleased (MouseEvent e)
 {}

 public void mouseEntered (MouseEvent e)
 {}

 public void mouseExited (MouseEvent e)
 {}
}

