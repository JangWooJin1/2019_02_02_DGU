//--------------------------------------------------------------------
//
//  Laboratory 5                                        stackarr.cpp
//
//  SOLUTION: Array implementation of the Stack ADT
//
//--------------------------------------------------------------------

#include <assert.h>
#include <iostream>
#include "stackarr.h"
using namespace std;


//--------------------------------------------------------------------

template < class SE >
Stack<SE>::Stack(int maxNumber)
// Creates an empty stack. Allocates enough memory for maxNumber
// elements (defaults to defMaxStackSize).
: maxSize(maxNumber), top(-1)
{
	element = new SE[maxSize]; //�迭ũ�� �������ֱ�
}

//--------------------------------------------------------------------

template < class SE >
Stack<SE>:: ~Stack()
// Frees the memory used by a stack.
{
	delete [] element; //�޸� �����ϱ�
}

//--------------------------------------------------------------------

template < class SE >
void Stack<SE>::push(const SE &newElement)
// Inserts newElement onto the top of a stack.
{
	if (full()) {
		std::cout << "list is full" << std::endl;
	}
	else { //�迭�� �������� �������� �� ����
		element[++top] = newElement;
	}



}

//--------------------------------------------------------------------

template < class SE >
SE Stack<SE>::pop()
// Removes the topmost element from a stack and returns it.
{
	if (this->empty() == 0) { //�迭�� ������� ���� ���� �� ����
		int value = element[top];
		element[top--] = 0;
		return value;
	}
	else {
		std::cout << "list is empty" << std::endl;
	}



}

//--------------------------------------------------------------------

template < class SE >
void Stack<SE>::clear()
// Removes all the elements from a stack.
{
	int loop;
	for (loop = 0; loop < maxSize; loop++) { //�迭 �ʱ�ȭ
		element[loop] = 0;
	}
	top = -1; //top �ʱ�ȭ

}

//--------------------------------------------------------------------

template < class SE >
int Stack<SE>::empty() const
// Returns 1 if a stack is empty. Otherwise, returns 0.
{
	if (top == -1) { return 1; }
	else { return 0; }
}

//--------------------------------------------------------------------

template < class SE >
int Stack<SE>::full() const
// Returns 1 if a stack is full. Otherwise, returns 0.
{
	if (top == maxSize - 1)  return 1; 
	else  return 0; 
}

//--------------------------------------------------------------------

template < class SE >
void Stack<SE>::showStructure() const
// Array implementation. Outputs the elements in a stack. If the
// stack is empty, outputs "Empty stack". This operation is intended
// for testing and debugging purposes only.
{
	int loop;
	for (loop = 0; loop < maxSize; loop++) {
		std::cout << loop << "\t" << element[loop] << std::endl;
	}

}