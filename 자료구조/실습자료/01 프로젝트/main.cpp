//mainfuction.cpp
#include "address_lnk.h"
int main() {

	AddressManager op;
	char cmd;
	string file;
	string data;
	do
	{

		cout << endl << "Commands:" << endl;
		cout << "  R  : read from a file" << endl;
		cout << "  +  : add a new enrty" << endl;
		cout << "  -  : remove an entry" << endl;
		cout << "  W  : write to a file" << endl;
		cout << "  M  : select a month" << endl;
		cout << "  Q  : Quit the program" << endl;
		cout << endl;


		cout << endl << "Command: ";                  // Read command
		cin >> cmd;


		switch (cmd)
		{
		case 'R': case 'r':
			cout << "Enter the name of the file: ";
			getline(cin, file); getline(cin, file);
			if (!op.readFile(file)) {
				cout << "error" << endl;
			}

			break;

		case '+':
			op.addEntry();
			break;

		case '-':
			cout << "Remove an entry: " << endl;
			cout << "Name: ";
			getline(cin, data); getline(cin, data);
			op.removeEntry(data);
			break;

		case 'W': case 'w':
			cout << "Enter the name of the file: ";
			getline(cin, file); getline(cin, file);
			if (!op.writeFile(file)) {
				cout << "error" << endl;
			}
			else {
				cout << "The list is written into " << file << endl;
			}

			break;

		case 'M': case 'm':

			cout << "Enter the selected month: ";
			cin >> data;
			cout << "Total number of birthdays in " << data << ": " << op.getMonthNum(data) << endl;
			op.Showlist(data, false);

			break;


		case 'Q': case 'q':                   // Quit test program
			break;

		default:                               // Invalid command
			cout << "Inactive or invalid command" << endl;
		}
		//op.Showlist("test");
	} while (cmd != 'Q' && cmd != 'q');

}



