﻿// Fig. 4.9: AccountTest.cs
// Using the Account constructor to set an Account's name
// when an Account object is created.
using System;

class AccountTest
{
   static void Main()
   {
      // create two Account objects
      Account account1 = new Account("Jane Green");
      Account account2 = new Account("John Blue");

      // display initial value of name for each Account
      Console.WriteLine($"account1 name is: {account1.Name}");
      Console.WriteLine($"account2 name is: {account2.Name}");
   }
}


/**************************************************************************
 * (C) Copyright 1992-2017 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/