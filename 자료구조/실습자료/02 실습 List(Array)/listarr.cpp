//--------------------------------------------------------------------
//
//                                                       listarr.cpp
//
//  SOLUTION: Array implementation of the List ADT
//
//--------------------------------------------------------------------


#include "listarr.h"

using namespace std;


//--------------------------------------------------------------------

List::List(int maxNumber)

// Creates an empty list. Allocates enough memory for maxNumber
// data items (defaults to defMaxListSize).

: maxSize(maxNumber),
size(0),
cursor(-1)
{
	dataItems = new char[maxNumber];
	int loop;
	for (loop = 0; loop < maxNumber; loop++) {
		dataItems[loop] = 0;
	}
}

//--------------------------------------------------------------------

List:: ~List()

// Frees the memory used by a list.

{
	delete dataItems;
}

//--------------------------------------------------------------------

void List::insert(const DataType &newDataItem)
throw (logic_error)

// Inserts newDataItem after the cursor. If the list is empty, then
// newDataItem is inserted as the first (and only) data items in the
// list. In either case, moves the cursor to newDataItem.

{
	if (isFull() == 0) {     //����Ʈ�� �� ���� �ʾ������� insert��
		if (dataItems[cursor + 1] == 0) {  //Ŀ�� ���� ����Ʈ�� ��� ������
			dataItems[++cursor] = newDataItem; //�׳� Ŀ�� �ڿ� ���� ����
			size++; //������ +1
		}
		else { //Ŀ�� ���� ����Ʈ�� ���� �����Ѵٸ�
			int loop;
			for (loop = size-1; loop > cursor; loop--) {
				dataItems[loop + 1] = dataItems[loop]; // �� ���� �� ���� ��ġ�� �ű��
			}
			dataItems[++cursor] = newDataItem; //�׷� ���� Ŀ�� �ڿ� ���� ����
			size++; //������+1
		}
	}
}

//--------------------------------------------------------------------

void List::remove() throw (logic_error)

// Removes the data items  marked by the cursor from a list. Moves the
// cursor to the next data item item in the list. Assumes that the
// first list data items "follows" the last list data item.

{
	
	if (this->isEmpty() == 0 && dataItems[cursor] != 0) { //����Ʈ�� �ϳ��� ���� �����ϰ�, �ش� Ŀ���� ����Ʈ�� ���� �����Ҷ�
		dataItems[cursor] = 0; //�ش� Ŀ���� ����Ʈ ���� 0���� ����(�� ����)
		int loop;
		for (loop = cursor; loop <= maxSize - 1; loop++) { // �߰��� ���� �������� ��ĭ�� ������ �����ִ� �Լ�
			dataItems[loop] = dataItems[loop + 1];
		}
		dataItems[loop + 1] = 0; 
		size--; // ������ -1
		if (cursor >= size) cursor--; //����Ʈ �ǵ��� ���� ���ﶧ ���� ó��
	}
}

//--------------------------------------------------------------------

void List::replace(const DataType &newDataItem)
throw (logic_error)

// Replaces the item marked by the cursor with newDataItem and
// leaves the cursor at newDataItem.

{
	// Requires that the list is not empty
	// pre-lab
	if (this->isEmpty()==0 && dataItems[cursor] != 0) {  //����Ʈ�� ���� �ְ�, �ش� Ŀ���� ����Ʈ ���� �����Ҷ� replace�Լ� �۵�
		dataItems[cursor] = newDataItem;
	}

}

//--------------------------------------------------------------------

void List::clear()

// Removes all the data items from a list.

{
	int loop;
	for (loop = 0; loop < maxSize; loop++) { //����Ʈ�� 0���� �� ����
		dataItems[loop] = 0; 
	}
	cursor = -1; //Ŀ����ġ -1�� ����
	size = 0; //����Ʈ ���� ũ�� 0���� ����

}

//--------------------------------------------------------------------

bool List::isEmpty() const

// Returns 1 if a list is empty. Otherwise, returns 0.

{
	if (size == 0) { // ����Ʈ ���� ũ�Ⱑ 0�϶� ���� �������ִ� �Լ� 
		return true;
	}
	else {
		return false;
	}
}

//--------------------------------------------------------------------

bool List::isFull() const

// Returns 1 if a list is full. Otherwise, returns 0.

{
	if (size == maxSize) { //����Ʈ�� ������ ���� �������ִ� �Լ�
		return true;
	}
	else {
		return false;
	}
}

//--------------------------------------------------------------------
 
int List::gotoBeginning() throw (logic_error)

// Moves the cursor to the beginning of the list.

{
	cursor = 0; // Ŀ���� ����Ʈ ó�� ��ġ�� �̵�
	return cursor;
}



//--------------------------------------------------------------------

int List::gotoEnd() throw (logic_error)

// Moves the cursor to the end of the list.

{
	cursor = size-1; //Ŀ���� ����Ʈ ���������� �̵�(index������ -1����)
	
	return cursor;
}

//--------------------------------------------------------------------

bool List::gotoNext() throw (logic_error)

// If the cursor is not at the end of a list, then moves the
// cursor to the next item in the list and returns true. Otherwise,
// returns false.

{
	if (cursor < size - 1) { //Ŀ���� ����Ʈ�� �ִ� ũ�⸦ ���� �ʾ��� �� 1ĭ �ڷ� �̵�
		cursor++;
	}
	else {
		return false;
	}

}

//--------------------------------------------------------------------

bool List::gotoPrior() throw (logic_error)

// If the cursor is not at the beginning of a list, then moves the
// cursor to the preceeding item in the list and returns true.
// Otherwise, returns false.

{
	if (cursor > 0) { //Ŀ���� ����Ʈ�� ó�� ��ġ���� Ŭ���� 1ĭ ������ �̵�
		cursor--;
	}
	else {
		return false;
	}

}

//--------------------------------------------------------------------

DataType List::getCursor() const throw (logic_error)

// Returns the item marked by the cursor.

{
	if (this->isEmpty()==0 && dataItems[cursor]!=0) { //����Ʈ�� ���� �����ϰ�, �ش� Ŀ���� ���� ���� �Ҷ� �� ���� ��������
		return dataItems[cursor];
	}

}

//--------------------------------------------------------------------

void List::showStructure() const

// Outputs the data items in a list. If the list is empty, outputs
// "Empty list". This operation is intended for testing/debugging
// purposes only.

{
	int loop;
	for (loop = 0; loop < maxSize; loop++) { //����Ʈ ��ü�� �����ִ� �Լ�
			cout << loop << "\t" << dataItems[loop] << endl;
	}


}

void List::moveToNth(int n) {
	if (n < size && n >-1) {
		int value=0;
		int loop1, loop2;
		value = dataItems[cursor];

		if (n > cursor) {
			for (loop1 = cursor; loop1 < n; loop1++) {
				dataItems[loop1] = dataItems[loop1 + 1];
			}
			dataItems[loop1] = value;
			
		}
		else {
			for (loop2 = cursor; loop2 > n; loop2--) {
				dataItems[loop2] = dataItems[loop2-1];
			}
			dataItems[loop2] = value;
			
		}
		cursor = n;
	}

}
 