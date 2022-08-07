#pragma once
//address.h
//������ 1. AddressBook *Node�� �ʱ�ȭ ���� -> new AddressBook�� �ذ�  2. ���Ⱑ ���Ե� ���ڿ� �Է¹ޱ� -> getline(cin, variable)�� �ذ�
#pragma once
#include<iostream>
#include<fstream>
#include<string>
using namespace std;

class Person {
public:
	Person(string name = "!", Friend *head = NULL, Person *next=NULL); //�Ű����� �ʱ�ȭ�� class���ο��� �ѹ��� �ϸ�� ���� �Լ��� �����Ҷ� �ѹ��� ���� �⺻ �μ� �����Ƕ�� ������ �߻�

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

	void Showlist(string month, bool operation = true); //operation���� true�� list��ü ��� false�� month�� �ش��ϴ� ���� ���
private:
	int number; // ����� ����
	AddressBook *top;
	AddressBook *temp; //��带 �߰��ϰų� ���� ������ �������� �ʵ��� �ּҰ��� �ӽ÷� �������ִ� ������
};




