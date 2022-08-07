#include "address_lnk.h"
AddressBook::AddressBook(string name, string phone, string birth, AddressBook *next) : name(name), phone(phone), birth(birth), next(next) { //노드의 값 초기화
}

AddressManager::AddressManager() {
	top = NULL;
	temp = NULL;
	number = 0;
}

AddressManager::~AddressManager() {
	AddressBook* Temp;
	while (top != NULL) { //프로그램을 종료할때 clear하기
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
		while (getline(in, value)) { // 파일에 있는 값을 한라인(\n)마다 value에 넣기

			if (value == "") { //파일내용중에 그냥 엔터만 있는건 재끼기 
				continue;
			}
			else if (Node->name == "!") { //먼저 name에 값이 없으면 name에 넣기
				Node->name = value;
				n++;

			}
			else if (Node->phone == "!") { //그다음 phone
				Node->phone = value;
				n++;

			}
			else if (Node->birth == "!") { //마지막 birth
				Node->birth = value;
				n++;

			}
			if (n == 3) { //이름 전화번호 생일 3개를 다 읽고 나서 그 다음 노드로 이동하기 위한 제어문
				temp = top; //노드끼리 끊어지지 않게 top값을 temp에 저장
				top = Node; //temp에 저장해놓고 새로운 node의 주소를 top에 저장
				Node->next = temp; //그다음 node에 temp값을 저장함으로써 노드끼리 연결
				Node = new AddressBook; //이게 왜 되는 거지 공부하자 공부하자 공부하자 공부하자 공부하자 공부하자
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
	temp = top; //노드를 이동할 포인터 변수 temp생성
	if (out.is_open()) {
		while (top != NULL) { //터미널 노드를 만나기 전까지 반복
			out << top->name << endl;
			out << top->phone << endl;
			out << top->birth << endl;
			out << endl;
			top = top->next; //다음 노드로 이동
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
	AddressBook *Pre = NULL; //Pre초기화 해야한다 해서 그냥 NULL값 넣음(search전의 노드를 가르키는 포인터)
	 //Search다음의 노드를 가르키는 포인터
	while (search != NULL) {
		if (search->name == name) {
			break;
		}
		else {
			Pre = search;
			search = search->next;
		}
	}
	if (search == NULL) { cout << "Not found" << endl; } //못찾았을때 처리 
	else if (search == top) { //맨앞에 노드를 지울때 예외처리
		temp = search->next;
		top = temp;
		delete search;
		number--;
	}
	else {
		temp = search->next; //뒷노드 주소값 넣기
		Pre->next = temp; //뒷노드와 앞노드 연결
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
	else { return "13"; } //예외처리
}

int AddressManager::getMonthNum(string month) {
	int count = 0;
	temp = top; //노드를 이동할 포인터 변수 temp생성

	string check;
	//string Mon = std::to_string(month);
	while (top != NULL) { //터미널 노드를 만나기 전까지 반복
		check = top->birth.substr(0, 2); //문자열 추출 함수(앞에 2개만)
		if (check == Converter(month)) { count++; }
		top = top->next; //다음 노드로 이동
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
	temp = top; //노드를 이동할 포인터 변수 temp생성
	string check;
	while (top != NULL) { //터미널 노드를 만나기 전까지 반복
		check = top->birth.substr(0, 2); //문자열 추출 함수(앞에 2개만)
		if (check == Converter(month) || operation == true) {
			cout << top->name << endl;
			cout << top->phone << endl;
			cout << top->birth << endl;
			cout << endl;
		}
		top = top->next; //다음 노드로 이동
	}
	top = temp;
}
