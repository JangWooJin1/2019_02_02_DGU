#include"heap.h"
#include<iostream>
using namespace std;
template<class HE>
Heap<HE>::Heap(int maxNumber) {
	maxSize = maxNumber;
	size = 0;
	element = new HE[maxSize];
}

template<class HE>
Heap<HE>::~Heap() {
	delete[] element;
}

template<class HE>
void Heap<HE>::insert(const HE & newElement) {
	if (!full()) {
		element[size++] = newElement;
		int i = size - 1;
		int pindex;
		HE temp;
		while (i > 0) {
			pindex = (i - 1) / 2;
			//if (pindex >= 0) {
				if (element[pindex].pty() < element[i].pty()) {
					swap(element[pindex], element[i]);
					i = pindex;
				}
				else { break; }
			//}
		}			
	}
}

template<class HE>
HE Heap<HE>::removeMax() {

	if (!empty()) {
		HE re = element[0];
		element[0].setPty(-1);

		int leftchild, rightchild, maxchild;
		HE temp;
		int i = 0;
		while (i < size) {
			leftchild = 2 * i + 1;
			rightchild = 2 * i + 2;
			if (leftchild < size) {
				if (leftchild == size - 1) { maxchild = leftchild; }
				else { maxchild = element[leftchild].pty() > element[rightchild].pty() ? leftchild : rightchild; }


				if (element[maxchild].pty() > element[i].pty()) {
					swap(element[i], element[maxchild]);
					i = maxchild;
				}
				else { break; }
			}
			else {	break;	}
		}
		size--;
		return re;
	}
	HE re;
	re.setPty(-1);
	return re;
	
}

template<class HE>
void Heap<HE>::clear() {
	size = 0;
}

template<class HE>
int Heap<HE>::empty() const {
	return size == 0;
}

template<class HE>
int Heap<HE>::full() const {
	return size == maxSize;
}

template<class HE>
void Heap<HE>::showStructure() const {
	if (!empty()) {
		std::cout << "size=" << size << std::endl;
		for (int i = 0; i < maxSize; i++) {
			std::cout << i << "    ";
		}
		std::cout << std::endl;
		for (int i = 0; i < size; i++) {
			std::cout << element[i].pty() << "    ";
		}
		std::cout << std::endl << std::endl;
		showSubtree(0, 0);
	}
}

template<class HE>
void Heap<HE>::showSubtree(int index, int level) const {
	if (index < size) {
		showSubtree(index * 2 + 2, level + 1);
		for (int i = 0; i < level; i++) {
			std::cout << "    ";
		}
		std::cout << element[index].pty();
		if (2 * index + 2 < size) {	std::cout << "<";	}
		else if(2*index +1 < size){	std::cout<<"\\";	}
		std::cout << std::endl;
		showSubtree(index * 2 + 1, level + 1);
		

	}
}