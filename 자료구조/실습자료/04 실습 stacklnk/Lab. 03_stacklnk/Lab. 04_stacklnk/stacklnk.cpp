//--------------------------------------------------------------------
//
//  Laboratory 5                                        stacklnk.cpp
//
//  SOLUTION: Linked list implementation of the Stack ADT
// 2018213016 Àå¿ìÁø 2019-10-07
//--------------------------------------------------------------------

#include <assert.h>
// #include <alloc.h>
#include "stacklnk.h"

//--------------------------------------------------------------------

template < class SE >
StackNode<SE>::StackNode(const SE& elem, StackNode<SE>* nextPtr)
// Creates a stack node containing element elem and next pointer
// nextPtr.
	: element(elem),
	next(nextPtr)
{

}

//--------------------------------------------------------------------

template < class SE >
Stack<SE>::Stack(int ignored)
// Creates an empty stack. The parameter is provided for compatability
// with the array implementation and is ignored.
	: top(0)
{
	top = NULL;
}

//--------------------------------------------------------------------

template < class SE >
Stack<SE>:: ~Stack()
// Frees the memory used by a stack.
{
	StackNode<SE>* Temp;
	while (top != NULL) {
		Temp = top;
		top = top->next;
		delete Temp;
	}
	delete top;
}

//--------------------------------------------------------------------

template < class SE >
void Stack<SE>::push(const SE& newElement)
// Inserts newElement onto the top of a stack.
{
	if (this->full() != 1) {
		StackNode<SE>* location;
		location = new StackNode<SE>(newElement, top);
		top = location;
	}

}

//--------------------------------------------------------------------

template < class SE >
SE Stack<SE>::pop()
// Removes the topmost element from a stack and returns it.
{
	if (this->empty() != 1) {
		SE element = top->element;
		StackNode<SE>* Temp = top;
		top = top->next;
		delete Temp;
		return element;
	}
}

//--------------------------------------------------------------------

template < class SE >
void Stack<SE>::clear()
// Removes all the elements from a stack.
{
	StackNode<SE>* Temp;
	while (top != NULL) {
		Temp = top;
		top = top->next;
		delete Temp;
	}
}

//--------------------------------------------------------------------

template < class SE >
int Stack<SE>::empty() const
// Returns 1 if a stack is empty. Otherwise, returns 0.
{
	return (top == NULL) ? 1 : 0;
}

//--------------------------------------------------------------------

template < class SE >
int Stack<SE>::full() const
// Returns 1 if a stack is full. Otherwise, returns 0. Cannot be
// done cleanly in generic C++ because there is sometimes overhead
// associated with a memory allocation.
{
	StackNode<SE>* test=new StackNode<SE>('t', NULL);
	if (test == NULL) {
		return 1;
	}
	else {
		delete test;
		return 0;
	}
}

//--------------------------------------------------------------------

template < class SE >
void Stack<SE>::showStructure()
// Linked list implementation. Outputs the elements in a stack. If
// the stack is empty, outputs "Empty stack". This operation is
// intended for testing and debugging purposes only.
{
	StackNode<SE>* Temp;
	Temp = top;
	int num = 0;
	while (top != NULL) {

		std::cout << num++ << "\t" << top->element << std::endl;
		top = top->next;

	}
	top = Temp;


}