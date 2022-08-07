// Logbook.cpp: implementation of the Logbook class.
//
//////////////////////////////////////////////////////////////////////

#define _CRT_SECURE_NO_WARNINGS
#include "Logbook.h"
#include <iostream>
#include <stdlib.h>
#include <stdio.h>

using namespace std;


//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

Logbook::Logbook()
{
	setCurrentTime();
	initEntry();
}

Logbook::~Logbook()
{

}

Logbook::Logbook(int month, int year)
{
	if (month >= 0 && month <= 11) {
		setCurrentTime();

		logMonth = month;
		logYear = year;
		// pre-lab

		currentTime->tm_year = year - 1900;
		currentTime->tm_mon = month - 1;




		initEntry();
	}
	else {
		cout << "----invalid month value----\n";
	}
}

void Logbook::putEntry(int day, int value)
{
	entries[day] = value;
}

int Logbook::getEntry(int day) const
{
	return entries[day];
}

int Logbook::getMonth() const
{
	return logMonth;
}

int Logbook::getYear() const
{
	return logYear;
}

int Logbook::getDaysInMonth() const
{
	return DaysOfMonth[isLeapYear(logYear)][logMonth - 1];
}

int Logbook::isLeapYear(int year) const
{
	if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
		return 1;		// Leap year
	else
		return 0;		// Normal years
}


void Logbook::putEntry(int value)
{
	entries[(currentTime->tm_mday - 1)] = value;
}

void Logbook::setCurrentTime()
{
	// tm structure Used by asctime, gmtime, localtime, mktime, 
	// and strftime to store and retrieve time information.  

	// tm�� ����ü�� �ð� ������ �����ϰ� �ҷ����� ���� asctime, gmtime, localtime, mktime,
	// �׸��� strftime �� ���ؼ� ���˴ϴ�.

	time_t		now;
	time(&now);
	currentTime = localtime(&now);

	logYear = currentTime->tm_year + 1900;
	logMonth = currentTime->tm_mon + 1;
}

void Logbook::initEntry()
{
	int iDays = getDaysInMonth();
	for (int i = 0; i <= iDays; i++)
		entries[i] = 0;
}


void Logbook::displayCalendar() const
{
	if (currentTime->tm_year + 1900 >= MIN_YEAR && currentTime->tm_year + 1900 <= MAX_YEAR) {

		cout << "\t\t\t\t" << currentTime->tm_mon + 1 << " / " << currentTime->tm_year + 1900;
		cout << endl;

		cout << "\t Sun \t Mon \t Tue \t Wed \t Thu \t Fri \t Sat";
		cout << endl;

		int iDays = getDaysInMonth();

		int flag = getDayOfWeek(1) + 1; // �� �ѱ� Ȯ�� �÷���
		// �ش� ��/���� 1���� ���� ���� �÷��� ��(0-6)���� �����Ѵ�.

		if (flag != 1) {
			for (int p = 1; p < flag; p++)
				cout << "\t";
		}

		for (int i = 0; i < iDays; i++) {
			cout << "\t" << i + 1 << " " << entries[i+1];
			flag++;

			if (flag % 8 == 0) {
				flag = 1;
				cout << endl;
			}
		}
	}
}

int Logbook::getDayOfWeek(int day) const
{
	tm	when;
	time_t	result;

	when = *currentTime;
	when.tm_mday = day;

	if ((result = mktime(&when)) != (time_t)-1)
	{
		return when.tm_wday;
	}
	else
	{
		return 0;
	}
}
