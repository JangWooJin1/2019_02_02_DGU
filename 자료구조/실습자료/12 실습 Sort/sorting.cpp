#include<iostream>
using namespace std;
#include "sorting.h"
//2018213016 �����
template<class LE>
Sorting<LE>::Sorting(LE *original) 
	:list(original), num_compare(0)
{

}

template<class LE>
Sorting<LE>::~Sorting() {

}

template<class LE>
int Sorting<LE>::insertion_sort(int n) { 
	//in-lab, pre-lab
	int i,j,k=0;
	for (i = 1; i < n; i++) { //������ ������ �Ѱ��� �÷���

		for ( j = 0; j <= i; j++){ //������ �������� ���� �־���� ��ġ�� ���� ����
			int temp = list[i];
			if (list[i] < list[j]) {
				num_compare += j;
				for (k = i; k >= j; k--) { //���ϴ� ��ġ�� ���� �ֱ� ���� ��ĭ�� �ڷ� �о��ֱ�
					list[k] = list[k-1];
				}
				list[j] = temp;
				break;
			}
		}
		
		print_list(0, n-1);
	
	}
	return num_compare-1;
}


template<class LE>
int Sorting<LE>::quick_sort(int left, int right) {
	//in-lab, pre-lab
	
	if (left < right) {
		int splitPoint = quickPartition(list, left,right); //quickPartition���� ���� �ǹ�������
		quick_sort(left, splitPoint-1); //�ǹ��� ���� ����Ʈ
		quick_sort(splitPoint+1, right); //�ǹ��� ������ ����Ʈ �̷��� �и��ؼ� �����ذ�
	}
	return num_compare+1;
	
}

template<class LE>
int Sorting<LE>::quickPartition(LE keyList[], int left, int right) {
	//in-lab, pre-lab
	int pivot;
	int low, high;
	low = left;
	high = right+1;
	pivot = list[left]; //����Ʈ�� ���� ���� �����͸� �ǹ����� ����
	 do{
		 do {
			low++; //���ʿ��� �ǹ������� ū ���� ����Ű�� low���� ����
		 } while (low <= right && list[low] < pivot);
		
		do { 
			high--; //�����ʿ��� �ǹ������� ���� ���� ����Ű�� high���� ����
		} while (high >= left && list[high] > pivot);

		if (low < high) { //low�� high���� ������ 
			swap(list[low], list[high]); //���� ���� �ٲ���
			num_compare++;
		}
	}while (low < high); //high�� low�� ������ ������ ����

	swap(list[left], list[high]); //�״��� �ǿ��ʿ� �ִ� �ǹ����� �߰��� �־��ֱ�
	num_compare++;

	for (int i = 0; i < left; i++) {
		cout << "    ";
	}
	print_list(left, right);
	return high;
}

template<class LE>
void Sorting<LE>::merge(LE keyList[], int left, int mid, int right) {
	//in-lab, pre-lab
	LE templist[MAX_SIZE]; //mergesort����� �ӽ� �����ϴ� ����Ʈ�����
	/*
	int leftlist = left; //�߰� �������� ���� ����Ʈ ������ ����Ʈ ������
	int rightlist = mid + 1;
	int tempindex = left;
	
	while (leftlist <= mid && rightlist <= right) { //���� ������ ����Ʈ ���ؼ� �����Ÿ� �ӽ� ����Ʈ�� �ֱ�
		templist[tempindex++] = list[leftlist] < list[rightlist] ? list[leftlist++] : list[rightlist++];
		num_compare++;
	}

	if (leftlist > mid) { //���� ������ ����Ʈ ���� ����������
		for (int loop = rightlist; loop <= right; loop++) {
			templist[tempindex++] = list[loop];
		}
	}
	else { //���� ���� ����Ʈ ���� ����������
		for (int loop = leftlist; loop <= mid; loop++) {
			templist[tempindex++] = list[loop];
		}
	}

	for (int loop = left; loop <= right; loop++) { //�ӽø���Ʈ�� ��������Ʈ�� ���� �Ҵ�
		list[loop] = templist[loop];
	}
	*/
	int tempindex = left;
	int leftlist = left;
	int rightlist = mid + 1;
	while (leftlist <= mid && rightlist <= right) {
		templist[tempindex++] = list[leftlist] < list[rightlist] ? list[leftlist++] : list[rightlist++];
	}

	if (leftlist > mid) { //���� ������ ����Ʈ ���� ����������
		for (int loop = rightlist; loop <= right; loop++) {
			templist[tempindex++] = list[loop];
		}
	}
	else { //���� ���� ����Ʈ ���� ����������
		for (int loop = leftlist; loop <= mid; loop++) {
			templist[tempindex++] = list[loop];
		}
	}


	for (int i = left; i <= right; i++) {
		list[i] = templist[i];
	}
	for (int i = 0; i < left; i++) {
		cout << "    ";
	}
	print_list(left, right);
}

template<class LE>
int Sorting<LE>::merge_sort(int left, int right) {
	//in-lab, pre-lab
	if (left < right) {
		int middle = (left + right) / 2; //����Ʈ�� �߰� ��ġ�� ��� �߰��� ��������
		merge_sort(left, middle);  //���ʿ� �ִ� ����Ʈ
		merge_sort(middle+1, right); //�����ʿ� �ִ� ����Ʈ�� �и�
		merge(list, left, middle, right); //�и��� ����Ʈ�� ������ merge�Լ� ����
	}
	return num_compare;
}

template<class LE>
void Sorting<LE>::copy_list(LE newList[], int n, int compare) {
	num_compare = compare;

	for (int i = 0; i < MAX_SIZE; i++)
		list[i] = newList[i];

}

template<class LE>
void Sorting<LE>::print_list(int left, int right) {
	for (int i = left; i < right+1 ; i++)
		cout << "|" << list[i] << "|";
	cout << endl;
}

