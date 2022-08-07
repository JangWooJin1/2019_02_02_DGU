/*
 ----------------------------
 * Name: Song min pyo
 * Last modified: 2015-10-16
 * content: Calculator
 ----------------------------
*/
#include "stackarr.cpp"
#include <iostream>
#include <cstring>
#include <cctype>
using namespace std;

#define _Maxname 20

int Calcul(char* postfix);  //this is function which calculate postfix
int main(void)
{
   char post[_Maxname];                //this is array that allocated the character of postfix

   cout << "This is calculator" << endl;
   cout << "Please input the postfix: ";
   cin >> post;                         //save the postfix on post array

   cout << "result: " << Calcul(post) << endl;   //Show the result
   return 0;   
}

int Calcul(char* postfix)
{
   return 0;
}