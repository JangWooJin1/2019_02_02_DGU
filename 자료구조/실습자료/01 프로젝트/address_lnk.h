#pragma once
//address.h
//문제점 1. AddressBook *Node의 초기화 문제 -> new AddressBook로 해결  2. 띄어쓰기가 포함된 문자열 입력받기 -> getline(cin, variable)로 해결
#pragma once
#include<iostream>
#include<fstream>
#include<string>
using namespace std;

class Person {
public:
	Person(string name = "!", Friend *head = NULL, Person *next=NULL); //매개변수 초기화는 class내부에서 한번만 하면됨 만약 함수를 정의할때 한번더 쓰면 기본 인수 재정의라는 오류가 발생

private:
	string name;
	Friend* head;
	Person *next;
	friend class AddressManager;
	friend class Friend;

};

class Friend {
	Person name;
	Friend* next;
	friend class Person;

	Friend(Person name, Friend* next);
};

class AddressManager {
public:
	AddressManager();
	~AddressManager();
	bool readFile(string filePath);
	bool writeFile(string filePath);

	void addEntry();
	void removeEntry(string name);

	string Converter(string month);
	int getMonthNum(string month);
	void ShowMonthNum();

	void Showlist(string month, bool operation = true); //operation값이 true면 list전체 출력 false면 month에 해당하는 값만 출력
private:
	int number; // 노드의 개수
	AddressBook *top;
	AddressBook *temp; //노드를 추가하거나 뺄때 연결이 끊어지지 않도록 주소값을 임시로 저장해주는 포인터
};




