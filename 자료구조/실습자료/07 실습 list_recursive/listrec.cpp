//2018213016 ����� 2019/11/04
//recursive_list
#include"listrec.h"
#include<iostream>
#include"stacklnk.cpp"
using namespace std;
template < class LE >
ListNode<LE>::ListNode(const LE& elem, ListNode* nextPtr) {
	element = elem;
	next = nextPtr;
}

template < class LE >
List<LE>::List(int ignored) {
	head = NULL;
	cursor = NULL;
}

template < class LE >
List<LE>::~List() {
	clear();
}

template < class LE >
void List<LE>::insert(const LE& newElement) {
	if (head != NULL) {
		ListNode<LE>* NodeNew = new ListNode<LE>(newElement, cursor->next);
		cursor->next = NodeNew;
		cursor = NodeNew;
	}
	else {
		ListNode<LE>* NodeNew = new ListNode<LE>(newElement, NULL);
		head = NodeNew;
		cursor = NodeNew;
	}
}

template < class LE >
void List<LE>::clear() {
	ListNode<LE>* temp;
	while (head != NULL) {
		temp = head;
		head = head->next;
		delete temp;
	}

}

template < class LE >
void List<LE>::showStructure() const {

	if (head != NULL) {
		ListNode<LE>* temp = head;
		while (temp != NULL) {
			cout << temp->element;
			if (temp->next != NULL) {
				cout << " | ";
			}
			temp = temp->next;

		}
	}
	else {
		cout << "List is Empty" << endl;
	}
}

template < class LE >
void List<LE>::write() const {

	if (head != NULL) {
		ListNode<LE>* temp = head;
		if (temp != NULL) {
			writeSub(temp);
		}
	}
	else {
		cout << "List is Empty" << endl;
	}
}

template < class LE >
void List<LE>::writeSub(ListNode<LE>* p) const {
	if (p != NULL) {
		cout << p->element;
		writeSub(p->next);
	}
}

template < class LE >
void List<LE>::insertEnd(const LE& newElement) {
	if (head != NULL) {
		insertEndSub(head, newElement);
	}
	else {
		ListNode<LE>* NodeNew = new ListNode<LE>(newElement, NULL);
		head = NodeNew;
		cursor = NodeNew;
	}
}

template < class LE >
void List<LE>::insertEndSub(ListNode<LE>*& p, const LE& newElement) {
	if (p->next != NULL) {
		insertEndSub(p->next, newElement);
	}
	else {
		ListNode<LE>* NodeNew = new ListNode<LE>(newElement, NULL);
		p->next = NodeNew;
	}
}

template < class LE >
void List<LE>::writeMirror() const {

	if (head != NULL) {
		write();
		writeMirrorSub(head);
		cout << endl;
	}
	else {
		cout << "List is Empty" << endl;
	}
}

template < class LE >
void List<LE>::writeMirrorSub(ListNode<LE>* p) const {
	if (p->next != NULL) {
		writeMirrorSub(p->next);
	}
	cout << p->element;
}

template < class LE >
void List<LE>::reverse() {
	if (head != NULL) {
		ListNode<LE>* temp = head;
		reverseSub(head, head->next);
		temp->next = NULL;

	}
	else {
		cout << "List is Empty" << endl;
	}
}

template < class LE >
void List<LE>::reverseSub(ListNode<LE>* p, ListNode<LE>* nextP) { //��ũ next�� ������ 2���� �̿��ؼ� ���� ��Ų��
	if (nextP->next != NULL) {
		reverseSub(nextP, nextP->next);
	}
	else {
		head = nextP;
	}
	nextP->next = p;
}

template < class LE >
void List<LE>::deleteEnd() {
	if (head != NULL) {
		deleteEndSub(head);
	}
	else {
		cout << "List is Empty" << endl;
	}
}

template < class LE >
void List<LE>::deleteEndSub(ListNode<LE>*& p) {
	if (p->next->next != NULL) {
		deleteEndSub(p->next);
	}
	else {
		if (cursor == p->next) {
			cursor = p;
		}

		ListNode<LE>* temp = p->next;
		p->next = NULL; //��۸� ���� ����!(���� ��尡 ����Ű�� ���� �������� ����)
		delete temp;
	}
}

template < class LE >
int List<LE>::length() const {

	if (head == NULL) {
		return 0;
	}
	else {
		return lengthSub(head);
	}
}

template < class LE >
int List<LE>::lengthSub(ListNode<LE>* p) const {
	int len = 1;
	if (p->next != NULL) {
		return len += lengthSub(p->next);
	}
	else {
		return len;
	}
}

template < class LE >
void List<LE>::unknown1() const {
	unknown1Sub(head);
	cout << endl;
}

template < class LE >
void List<LE>::unknown1Sub(ListNode<LE>* p) const {
	if (p != NULL) {
		cout << p->element;
		if (p->next != NULL) {
			unknown1Sub(p->next->next);
			cout << p->next->element;
		}
	}
}

template < class LE >
void List<LE>::unknown2() {
	unknown2Sub(head);
}

template < class LE >
void List<LE>::unknown2Sub(ListNode<LE>*& p) {
	ListNode<LE>* temp;
	if (p != NULL && p->next != NULL) {
		temp = p;
		p = p->next;
		temp->next = p->next;
		p->next = temp;
		unknown2Sub(temp->next);
	}
}

template < class LE >
void List<LE>::iterReverse() { //������ 3���� �̿��ؼ� reverse�ϱ�
	if (head != NULL && head->next != NULL) {  //��尡 �Ѱ��̰ų� ���� �� ����ó��
		ListNode<LE>* temp = head;
		ListNode<LE>* temp2 = head->next;
		ListNode<LE>* temp3 = head->next->next;
		head = NULL; //��۸� ó��

		temp->next = NULL; //��ó�� ��� ����ó��
		temp2->next = temp; //��ó�� ��� ����ó��
		if (temp3 == NULL) { //��尡 2�� �϶� ����ó��
			head = temp2;
		}
		else {
			while (temp3->next != NULL) {
				temp = temp2; //���� ���� �̵�
				temp2 = temp3; //���� ���� �̵�
				temp3 = temp3->next; //���� ���� �̵�

				temp2->next = temp; //������� next�����ϱ�
			}
			head = temp3; // ������ ��忡 head����
			head->next = temp2; //������ ��� ������ �������ֱ�(while������ ����� �������ִϱ�)
		}
	}
	else {
		cout << "List is Empty or one element" << endl;
	}
}

template < class LE >
void List<LE>::stackWriteMirror() const { //����Լ����� ������ �̿��Ͽ� ����
	if (head != NULL) {
		ListNode<LE>* temp1 = head;
		Stack<LE> temp2;
		while (temp1 != NULL) {
			cout << temp1->element;
			temp2.push(temp1->element);
			temp1 = temp1->next;
		}
		while (!temp2.empty()) {
			cout << temp2.pop();
		}
		cout << endl;

	}
	else {
		cout << "List is Empty" << endl;
	}
}

template < class LE >
void List<LE>::cRemove() {

}

template < class LE >
void List<LE>::cRemoveSub(ListNode<LE>*& p) {

}

template < class LE >
void List<LE>::aBeforeb() {

}

template < class LE >
void List<LE>::aBeforebSub(ListNode<LE>*& p) {

}