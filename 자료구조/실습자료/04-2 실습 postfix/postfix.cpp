/*
 ----------------------------
 * Name: JangWooJin
 * Last modified: 2019-10-07
 * content: Calculator
 파일include가 안되서 그냥 stacklnk.h과 stacklnk.cpp파일 소스를 복붙해서 가져왔습니다.
 ----------------------------
*/

#include <assert.h>
// #include <alloc.h>
#include <iostream>
#include <cstring>
#include <cctype>
using namespace std;

#define _Maxname 20
//--------------------------------------------------------------------
//--------------------------------------------------------------------
//
//  Laboratory 5                                          stacklnk.h
//
//  Class declarations for the linked list implementation of the
//  Stack ADT
//
//--------------------------------------------------------------------


const int defMaxStackSize = 10;

template <class SE>
class Stack;

template < class SE >
class StackNode               // Facilitator class for the Stack class
{
private:

	// Constructor
	StackNode(const SE &elem, StackNode *nextPtr);

	// Data members
	SE element;         // Stack element
	StackNode *next;    // Pointer to the next element

	friend class Stack<SE>;
};

//--------------------------------------------------------------------

template < class SE >
class Stack
{
public:

	// Constructor
	Stack(int ignored = 0);

	// Destructor
	~Stack();

	// Stack manipulation operations
	void push(const SE &newElement);    // Push element
	SE pop();                             // Pop element
	void clear();                         // Clear stack

	// Stack status operations
	int empty() const;                    // Stack is empty
	int full() const;                     // Stack is full

	// Output the stack structure -- used in testing/debugging
	void showStructure();

	// In-lab operation
	Stack(const Stack &valueStack);     // Copy constructor

private:

	// Data member
	StackNode<SE> *top;   // Pointer to the top element
};

// --------------------------------------------------------------------
//
//  Laboratory 5                                        stacklnk.cpp
//
//  SOLUTION: Linked list implementation of the Stack ADT
//
//--------------------------------------------------------------------



template < class SE >
StackNode<SE>::StackNode(const SE &elem, StackNode<SE> *nextPtr)
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
void Stack<SE>::push(const SE &newElement)
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
	StackNode<SE>* test;
	test = new StackNode<SE>('t', NULL);
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



//--------------------------------------------------------------------
//
//  Laboratory 5                                          postfix.cpp
//--------------------------------------------------------------------



int Calcul(char* postfix);  //this is function which calculate postfix
int main(void)
{
   char post[_Maxname];                //this is array that allocated the character of postfix

   cout << "This is calculator" << endl;
   cout << "Please input the postfix: ";
   cin >> post;                         //save the postfix on post array



   cout << "result: " << Calcul(post) << endl;   //Show the result
   return 0;   
}

int Calcul(char* postfix)
{
	Stack<int> op(10);
	int num1, num2;
	int result;
	int loop;
	for (loop = 0; postfix[loop] != '\0'; loop++) {
		if (postfix[loop] == '+') {
			num1 = op.pop();
			num2 = op.pop();
			cout << num2 << "\t" << num1 << endl;
			result = (num2 + num1);
			op.push(result);
		}
		else if (postfix[loop] == '-') {
			num1 = op.pop();
			num2 = op.pop();
			cout << num2 << "\t" << num1 << endl;
			result = (num2 - num1);
			op.push(result);
		}
		else if (postfix[loop] == '*') {
			num1 =op.pop();
			num2 =op.pop();
			cout << num2 << "\t" << num1 << endl;
			result = (num2 * num1);
			op.push(result);
		}
		else if (postfix[loop] == '/') {
			num1 =op.pop();
			num2 = op.pop();
			if (num1 != 0) {
				cout << num2 << "\t" << num1 << endl;
				result = (num2 / num1);
				op.push(result);
			}
			else {
				cout << "나눌 수 없습니다!" << endl;
				return 0;
			}
		}
		else if(postfix[loop]=='0'|| postfix[loop] == '1' || postfix[loop] == '2'|| postfix[loop] == '3'|| postfix[loop] == '4'|| postfix[loop] == '5'||
			postfix[loop] == '6'|| postfix[loop] == '7'|| postfix[loop] == '8'|| postfix[loop] == '9'){
			
			op.push(result=int(postfix[loop]-'0'));
			
		}
	}
   return result;
}