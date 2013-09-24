public class Points
{
 private int[] points;//points array

 public Points()
 {
  points = new int[8];//creates 8 integer array
 }

 public int getPoints(int num)//gets the points
 {
  return points[num];//gets the points: 0=west, 1=north, 2=east, 3=south, 4=west Total, 5=north Total, 6=east Total, 7=south Total
 }

 public void countPoints(HDeck[] cardsWon)
 {
  for (int i = 0; i<4; i++)
   points[i] = pointsHand(cardsWon[i]);    //Finds the points for each hand for this round
 }

 private int pointsHand(HDeck hand)    //method to find the points for a hand (HDeck sent in)
 {
  int p=0;   //initialize points of the round

  for (int i=0; i<hand.getSize(); i++)//for loop, go through 13 cards in played deck
  {
   if ((hand.get(i)).getHeartsSuit()==4)//if a card is a heart, add a point
    p++;
   else if ((hand.get(i)).getHeartsSuit()== 3 && (hand.get(i)).getHeartsRank()==12)//if a card is queen of spades, add 13 points
    p+=13;
  }
  return p;//return number of points
 }

 public void total()//counts the total of all rounds
 {
  for (int i=0; i<4; i++)//for every player
   points[i+4]+=points[i];//adds the current round points to the total from previous rounds, but in 4 more positions in the array
 }

 public void newRound()
 {
  for(int i = 0; i<4; i++)
   points[i] = 0; 
 }

 public void reset()   //resets the points
 {
  for(int i = 0; i<8; i++)
   points[i] = 0;
 }

}



