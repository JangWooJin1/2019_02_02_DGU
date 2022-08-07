#include "address_lnk.h"
AddressBook::AddressBook(string name, string phone, string birth, AddressBook *next) : name(name), phone(phone), birth(birth), next(next) { //����� �� �ʱ�ȭ
}

AddressManager::AddressManager() {
	top = NULL;
	temp = NULL;
	number = 0;
}

AddressManager::~AddressManager() {
	AddressBook* Temp;
	while (top != NULL) { //���α׷��� �����Ҷ� clear�ϱ�
		Temp = top;
		top = top->next;
		delete Temp;
	}

}

bool AddressManager::readFile(string filePath) {
	std::ifstream in(filePath);

	if (in.is_open()) {
		string value;
		temp = top;
		AddressBook *Node = new AddressBook;
		int n = 0;
		while (getline(in, value)) { // ���Ͽ� �ִ� ���� �Ѷ���(\n)���� value�� �ֱ�

			if (value == "") { //���ϳ����߿� �׳� ���͸� �ִ°� �糢�� 
				continue;
			}
			else if (Node->name == "!") { //���� name�� ���� ������ name�� �ֱ�
				Node->name = value;
				n++;

			}
			else if (Node->phone == "!") { //�״��� phone
				Node->phone = value;
				n++;

			}
			else if (Node->birth == "!") { //������ birth
				Node->birth = value;
				n++;

			}
			if (n == 3) { //�̸� ��ȭ��ȣ ���� 3���� �� �а� ���� �� ���� ���� �̵��ϱ� ���� ���
				temp = top; //��峢�� �������� �ʰ� top���� temp�� ����
				top = Node; //temp�� �����س��� ���ο� node�� �ּҸ� top�� ����
				Node->next = temp; //�״��� node�� temp���� ���������ν� ��峢�� ����
				Node = new AddressBook; //�̰� �� �Ǵ� ���� �������� �������� �������� �������� �������� ��������
				n = 0;
				number++;

			}

		}
		cout << "Total number of entries in the list: " << number << endl;
		cout << "Number of birthdays in" << endl;
		this->ShowMonthNum();
		return true;
	}
	else {
		return false;
	}

}

bool AddressManager::writeFile(string filePath) {
	std::ofstream out(filePath);
	temp = top; //��带 �̵��� ������ ���� temp����
	if (out.is_open()) {
		while (top != NULL) { //�͹̳� ��带 ������ ������ �ݺ�
			out << top->name << endl;
			out << top->phone << endl;
			out << top->birth << endl;
			out << endl;
			top = top->next; //���� ���� �̵�
		}
		top = temp;
		return true;
	}
	else {
		return false;
	}

}

void AddressManager::addEntry() {
	AddressBook *Node = new AddressBook;
	cout << "Add an entry: " << endl;
	cout << "Name: ";
	getline(cin, Node->name); getline(cin, Node->name);
	cout << "Phone: ";
	getline(cin, Node->phone);
	cout << "Birthday: ";
	getline(cin, Node->birth);

	temp = top;
	top = Node;
	Node->next = temp;

	cout << "Total number of entries in the list: " << ++number << endl;
	cout << "Number of birthdays in" << endl;
	this->ShowMonthNum();
}


void AddressManager::removeEntry(string name) {
	AddressBook *search = top;
	AddressBook *Pre = NULL; //Pre�ʱ�ȭ �ؾ��Ѵ� �ؼ� �׳� NULL�� ����(search���� ��带 ����Ű�� ������)
	 //Search������ ��带 ����Ű�� ������
	while (search != NULL) {
		if (search->name == name) {
			break;
		}
		else {
			Pre = search;
			search = search->next;
		}
	}
	if (search == NULL) { cout << "Not found" << endl; } //��ã������ ó�� 
	else if (search == top) { //�Ǿտ� ��带 ���ﶧ ����ó��
		temp = search->next;
		top = temp;
		delete search;
		number--;
	}
	else {
		temp = search->next; //�޳�� �ּҰ� �ֱ�
		Pre->next = temp; //�޳��� �ճ�� ����
		delete search;
		number--;
	}

	cout << "Total number of entries in the list: " << number << endl;
	cout << "Number of birthdays in" << endl;
	this->ShowMonthNum();
}

string AddressManager::Converter(string month) {
	if (month == "January") { return "01"; }
	else if (month == "February") { return "02"; }
	else if (month == "March") { return "03"; }
	else if (month == "April") { return "04"; }
	else if (month == "May") { return "05"; }
	else if (month == "June") { return "06"; }
	else if (month == "July") { return "07"; }
	else if (month == "Augest") { return "08"; }
	else if (month == "Septmeber") { return "09"; }
	else if (month == "October") { return "10"; }
	else if (month == "November") { return "11"; }
	else if (month == "December") { return "12"; }
	else { return "13"; } //����ó��
}

int AddressManager::getMonthNum(string month) {
	int count = 0;
	temp = top; //��带 �̵��� ������ ���� temp����

	string check;
	//string Mon = std::to_string(month);
	while (top != NULL) { //�͹̳� ��带 ������ ������ �ݺ�
		check = top->birth.substr(0, 2); //���ڿ� ���� �Լ�(�տ� 2����)
		if (check == Converter(month)) { count++; }
		top = top->next; //���� ���� �̵�
	}
	top = temp;
	return count;
}

void AddressManager::ShowMonthNum() {
	int loop;
	string Mon[12] = { "January", "February", "March", "April", "May", "June", "July", "Augest", "Septmeber", "October", "November", "December" };
	for (loop = 0; loop < 12; loop++) {
		if (getMonthNum(Mon[loop]) != 0) {
			cout << "\t" << Mon[loop] << ": " << getMonthNum(Mon[loop]) << endl;
		}
	}

}

void AddressManager::Showlist(string month, bool operation) {
	temp = top; //��带 �̵��� ������ ���� temp����
	string check;
	while (top != NULL) { //�͹̳� ��带 ������ ������ �ݺ�
		check = top->birth.substr(0, 2); //���ڿ� ���� �Լ�(�տ� 2����)
		if (check == Converter(month) || operation == true) {
			cout << top->name << endl;
			cout << top->phone << endl;
			cout << top->birth << endl;
			cout << endl;
		}
		top = top->next; //���� ���� �̵�
	}
	top = temp;
}
