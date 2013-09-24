public class Round {
  
  private int player;//defining integer to say which player's turn it is; 1= west, 2= north, 3= east, 4= south (user)
  
  public Round (int t)    //player to play first when initialized
  {
    player = t;
  }
  
  public void set(int n)   //set method to say who's turn
  {
    player = n; 
  }
  
  public int findWinner(HDeck middle)//Method to find the winner of a trick 
  {
    int big, n=1, s2, r2, s, p=player;
    Card first, compare;
    
    first = middle.get(0);   //get the card that was played first in the trick (lead)
    
    big = first.getHeartsRank();   //get rank of 1st Card played, initialize the biggest rank
    s = first.getHeartsSuit();   //get suit of 1st Card played
    
    do
    {
      compare = middle.get(n);//gets the card that was played
      r2 = compare.getHeartsRank();//gets the rank
      s2 = compare.getHeartsSuit();//gets the suit
      
      if (r2 > big && s == s2)//if the rank  of the card is greater than the existing greatest rank and the suits are equal
      {
        big = r2;//change the greatest rank to this card's rank
        p = player + n;//the new position of the player who won the trick is hte original player 
      }
      
      n++; //increment n by 1 to check the next card played
    }
    while (n<4);//n must be less than 4 because it checks the card from positions 1-3 in the array
    
    if(p > 4)//position p must be between 1 and 4, so if it's greater than 4 when p is incremented by n, then wrap-around 
      p = p-4;
    
    return p;//return the position
  }
  
  public void next()//method that advances the player integer
  {
    if (player !=4)//advance 1, unless it's 4
      player++;
    else if (player==4)//if it's 4, wrap-around to 1
      player=1;
  }
  
  public int get()//method that return the position of the player
  {
    return player;
  }
}
