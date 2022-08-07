//--------------------------------------------------------------------
//
//  Laboratory 1                                           coffee.cpp
//
//  Sample logbook application program from lab book prelab
//
//--------------------------------------------------------------------

#include <iostream>
#include "Logbook.h"   // Include the declaration of the Logbook class

using namespace std;

// Records coffee intake for January 2003.

void main()
{
	Logbook coffee(1, 2003);   // Coffee intake for January 2003
	int day;                  // Day loop counter

	// Record entries for the 1st and 15th of January 2003

	coffee.putEntry(1, 5);
	coffee.putEntry(15, 2);

	// Output the logbook entries.

	cout << "Month/Year : " << coffee.getMonth() << "/"	<< coffee.getYear() << endl;

	for (day = 1; day <= coffee.getDaysInMonth(); day++)
		cout << day << " : " << coffee.getEntry(day) << endl;
}
