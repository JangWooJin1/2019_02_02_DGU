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
	if (isFull() == 0) {     //리스트가 꽉 차지 않았을때만 insert함
		if (dataItems[cursor + 1] == 0) {  //커서 다음 리스트가 비어 있으면
			dataItems[++cursor] = newDataItem; //그냥 커서 뒤에 값을 삽입
			size++; //사이즈 +1
		}
		else { //커서 다음 리스트에 값이 존재한다면
			int loop;
			for (loop = size-1; loop > cursor; loop--) {
				dataItems[loop + 1] = dataItems[loop]; // 그 값을 그 다음 위치에 옮기고
			}
			dataItems[++cursor] = newDataItem; //그런 다음 커서 뒤에 값을 삽입
			size++; //사이즈+1
		}
	}
}

//--------------------------------------------------------------------

void List::remove() throw (logic_error)

// Removes the data items  marked by the cursor from a list. Moves the
// cursor to the next data item item in the list. Assumes that the
// first list data items "follows" the last list data item.

{
	
	if (this->isEmpty() == 0 && dataItems[cursor] != 0) { //리스트에 하나라도 값이 존재하고, 해당 커서의 리스트에 값이 존재할때
		dataItems[cursor] = 0; //해당 커서의 리스트 값을 0으로 변경(값 제거)
		int loop;
		for (loop = cursor; loop <= maxSize - 1; loop++) { // 중간에 값이 지워지면 한칸씩 앞으로 땡겨주는 함수
			dataItems[loop] = dataItems[loop + 1];
		}
		dataItems[loop + 1] = 0; 
		size--; // 사이즈 -1
		if (cursor >= size) cursor--; //리스트 맨뒤의 값을 지울때 예외 처리
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
	if (this->isEmpty()==0 && dataItems[cursor] != 0) {  //리스트에 값이 있고, 해당 커서의 리스트 값이 존재할때 replace함수 작동
		dataItems[cursor] = newDataItem;
	}

}

//--------------------------------------------------------------------

void List::clear()

// Removes all the data items from a list.

{
	int loop;
	for (loop = 0; loop < maxSize; loop++) { //리스트값 0으로 다 변경
		dataItems[loop] = 0; 
	}
	cursor = -1; //커서위치 -1로 변경
	size = 0; //리스트 실제 크기 0으로 변경

}

//--------------------------------------------------------------------

bool List::isEmpty() const

// Returns 1 if a list is empty. Otherwise, returns 0.

{
	if (size == 0) { // 리스트 현재 크기가 0일때 참을 리턴해주는 함수 
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
	if (size == maxSize) { //리스트가 꽉차면 참을 리턴해주는 함수
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
	cursor = 0; // 커서를 리스트 처음 위치로 이동
	return cursor;
}



//--------------------------------------------------------------------

int List::gotoEnd() throw (logic_error)

// Moves the cursor to the end of the list.

{
	cursor = size-1; //커서를 리스트 마지막으로 이동(index값때매 -1해줌)
	
	return cursor;
}

//--------------------------------------------------------------------

bool List::gotoNext() throw (logic_error)

// If the cursor is not at the end of a list, then moves the
// cursor to the next item in the list and returns true. Otherwise,
// returns false.

{
	if (cursor < size - 1) { //커서가 리스트의 최대 크기를 넘지 않았을 때 1칸 뒤로 이동
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
	if (cursor > 0) { //커서가 리스트의 처음 위치보다 클때만 1칸 앞으로 이동
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
	if (this->isEmpty()==0 && dataItems[cursor]!=0) { //리스트에 값이 존재하고, 해당 커서의 값이 존재 할때 그 값을 리턴해줌
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
	for (loop = 0; loop < maxSize; loop++) { //리스트 전체를 보여주는 함수
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
 