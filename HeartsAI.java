public class HeartsAI
{
 private int p;//integer of position of card in HDeck hand of the CPU that is to be played
 private static boolean hBroken;//variable to determine if hearts has been broekn. true= hearts has been played

 public HeartsAI()
 {
  p = 0;
  hBroken = false;
 }

 public int pos(HDeck h, HDeck m)//main method to find card to play
 {
  int suit;

  if (h.getSize() == 1)//if there's only one card left, return it
   return 0;

  try
  {
   if (m.getSize()==0)//if the middle deck has no cards (nobody has played yet)
   {
    p = lead(h);//use the lead method to find the best card
   }
   else
   {
    suit = (m.get(0)).getSuit();//get the suit of the first card played
    if (centerPoints(m)>0)//if there are points 
    {
     p = lowest(suit,h);//finds lowest card of that suit
    }
    else if (centerPoints(m)==0)
    {
     p = highest(suit,h);//finds highest of that suit
    }
   }
  }
  catch(Exception e)
  {
  }


  if (p!=-1)//if such card exists in that suit, then return the position
   return p;

  else //player has no cards of suit led
  {
   p = qSpades(h);//finds queen of spades to discard
   if (p!=-1)
    return p;
   else
   {
    p = highest(2,h);//finds highest heart to discard
    if (p!=-1)
    {
     setBroken(true);
     return p;
    }
    else
    {
     p = highest(1,h);//finds highest spade to discard
     if (p!=-1)
      return p;
     else
     {

      p = highest(3,h);//finds highest diamond to discard
      if (p!=-1)
       return p;
      else
      {
       p = highest(4,h);//finds club diamond
       if (p!=-1)
        return p;
       else
        return -1;
      }
     }
    }
   }
  }
 }

 public int highest(int s, HDeck deck) //finds highest card in suit s
 {
  int posFound=0;

  for (int i=14; i>=2; i--)//start at the highest card Ace, decrement
  {
   if (i==14)//if the rank is 14, change it to 1 (1=ace)
    i=1;
   Card compare = new Card (i, s); //make a new card object with the suit and rank
   posFound = deck.search(compare,0); //search the card within the CPU's hand
   if (posFound != -1 && qSpCheck(posFound,deck))//if it's found and it isn't the Q of Spades, return the position integer
    return posFound;
   if (i==1)
    i=14;
  }
  Card compare = new Card (12,1);
  posFound = deck.search(compare,0);
  if (posFound != -1 && s==1)//If there is no other choice, then play Queen of Spades
   return (posFound);
  return -1;//if no card of that suit found, then return -1

 }
 public int lowest(int s, HDeck deck) //finds lowest card in suit s
 {
  int posFound=0;

  for (int i=2; i<=14; i++) //start at rank of 2, increment up
  {
   if (i==14)//if the number is 14, change it to 1 (1=ace)
    i=1;
   Card compare = new Card (i, s);//create a new card with the suit and rank
   posFound = deck.search(compare,0); //search of the card
   if (posFound != -1 && qSpCheck(posFound,deck))//if it's found and 
    return posFound;
   if (i==1)//if the number is 1, change to 15 the out the loop
    i=15;
  }
  Card compare = new Card (12,1);
  posFound = deck.search(compare,0);
  if (posFound != -1 && s==1)//If there is no other choice, then play Queen of Spades
   return (posFound);
  return -1;//if no card of that suit found, then return -1
 }

 public int qSpades(HDeck deck)//method to find the queen of spades
 {
  Card queenS = new Card(12, 1);//make new queen of spades card
  int p = deck.search(queenS, 0);//search the sent in hand for the card
  if (p!=-1)//if found, return the position
   return p;
  else
   return -1;//otherwise return -1
 }

 public int lead(HDeck hand)//method to find best lead
 {

  int l = lowest(1,hand);//finds lowest spade

  if (l != -1 && (hand.get(l)).getHeartsRank() != 12)//if the lead isn't the queen of spades
  {
   return l;//return the position
  }
  else if (getBroken())//if hearts has been broker
  {
   l = lowest(2,hand);//find the lowest heart
   if (l!=-1)
    return l;//return the position
  }
  else
  {
   l = lowest(3,hand);//find the lowest diamond to play
   if (l!=-1)
    return l;
   else
   {
    l = lowest(4,hand); //find the lowest club to play
    if (l!=-1)
     return l;
   }
  }

  return 0; // return 0 is there is only one card in hand left, and nothing else was found
 }

 public boolean qSpCheck(int p, HDeck hand)//check if card is NOT Queen of Spades
 {
  if ((hand.get(p)).getHeartsSuit()==3 && (hand.get(p)).getHeartsRank()== 12)
   return false;//if it is queen of spades, then returns false
  else
   return true;
 }

 public int passPos(HDeck deck)//Finds card to be passed, finds highest cards regardless of suit
 {
  for (int r = 14; r>4; r--)//for loop to find the acesm then kinds, then queens, etc.
  {
   if (r==14)
    r=1;
   for (int s = 1; s<4; s++)//for loop to go through every suit to find the high cards
   {
    Card compare = new Card (r,s);//makes to card to compare
    int p = deck.search(compare,0);//searches for that high card
    if (p !=-1 && !(deck.get(p)).isSelected())//if it isn't already selected and it's found, then return position
     return p;
   }
   if (r==1)//
    r=14;
  }
  return 0;
 }

 public static boolean getBroken()//method to find if hearts has been broken
 {
  return hBroken;//return the hBroken boolean value
 }

 public static void setBroken(boolean h)//method to set if hearts has been broken
 {
  hBroken = h;//sets hBroken to ture or false
 }

 public int centerPoints(HDeck mid)//centerPoints method

 {
  //finds the number of game points within the middle Deck (eg Hearts of Q of Spades that have been played)

  int Centerpoints=0;//initialize the center points

  for (int i=0; i<(mid.getSize()-1); i++)//loops for every card in the middle Deck (played cards)
  {
   Card card = mid.get(i);//gets the card
   if (card.getHeartsSuit() == 4)//if the suit is Hearts, then add a point
    Centerpoints++;

   if (card.getHeartsSuit() == 3 && card.getRank() == 12)//it it's the Queen of Spades, add 13 points
    Centerpoints+=13;
  }

  return Centerpoints;//return the points

 }

}



