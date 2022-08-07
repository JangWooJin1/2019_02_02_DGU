#include<iostream>
using namespace std;
#include "sorting.h"
//2018213016 장우진
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
	for (i = 1; i < n; i++) { //정렬할 영역을 한개씩 늘려감

		for ( j = 0; j <= i; j++){ //정렬할 영역에서 값이 있어야할 위치에 값을 삽입
			int temp = list[i];
			if (list[i] < list[j]) {
				num_compare += j;
				for (k = i; k >= j; k--) { //원하는 위치에 값을 넣기 위해 한칸씩 뒤로 밀어주기
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
		int splitPoint = quickPartition(list, left,right); //quickPartition으로 나온 피벗값으로
		quick_sort(left, splitPoint-1); //피벗값 왼쪽 리스트
		quick_sort(splitPoint+1, right); //피벗값 오른쪽 리스트 이렇게 분리해서 정렬해감
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
	pivot = list[left]; //리스트의 가장 왼쪽 데이터를 피벗으로 선택
	 do{
		 do {
			low++; //왼쪽에서 피벗값보다 큰 값을 가르키게 low값을 증가
		 } while (low <= right && list[low] < pivot);
		
		do { 
			high--; //오른쪽에서 피벗값보다 작은 값을 가르키게 high값을 감소
		} while (high >= left && list[high] > pivot);

		if (low < high) { //low가 high보다 작으면 
			swap(list[low], list[high]); //둘의 값을 바꿔줌
			num_compare++;
		}
	}while (low < high); //high와 low가 교차할 때까지 진행

	swap(list[left], list[high]); //그다음 맨왼쪽에 있는 피벗값을 중간에 넣어주기
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
	LE templist[MAX_SIZE]; //mergesort결과를 임시 저장하는 리스트만들기
	/*
	int leftlist = left; //중간 기준으로 왼쪽 리스트 오른쪽 리스트 나누기
	int rightlist = mid + 1;
	int tempindex = left;
	
	while (leftlist <= mid && rightlist <= right) { //왼쪽 오른쪽 리스트 비교해서 작은거를 임시 리스트에 넣기
		templist[tempindex++] = list[leftlist] < list[rightlist] ? list[leftlist++] : list[rightlist++];
		num_compare++;
	}

	if (leftlist > mid) { //아직 오른쪽 리스트 값이 남아있을때
		for (int loop = rightlist; loop <= right; loop++) {
			templist[tempindex++] = list[loop];
		}
	}
	else { //아직 왼쪽 리스트 값이 남아있을때
		for (int loop = leftlist; loop <= mid; loop++) {
			templist[tempindex++] = list[loop];
		}
	}

	for (int loop = left; loop <= right; loop++) { //임시리스트를 원래리스트에 값을 할당
		list[loop] = templist[loop];
	}
	*/
	int tempindex = left;
	int leftlist = left;
	int rightlist = mid + 1;
	while (leftlist <= mid && rightlist <= right) {
		templist[tempindex++] = list[leftlist] < list[rightlist] ? list[leftlist++] : list[rightlist++];
	}

	if (leftlist > mid) { //아직 오른쪽 리스트 값이 남아있을때
		for (int loop = rightlist; loop <= right; loop++) {
			templist[tempindex++] = list[loop];
		}
	}
	else { //아직 왼쪽 리스트 값이 남아있을때
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
		int middle = (left + right) / 2; //리스트의 중간 위치를 잡고 중간을 기준으로
		merge_sort(left, middle);  //왼쪽에 있는 리스트
		merge_sort(middle+1, right); //오른쪽에 있는 리스트를 분리
		merge(list, left, middle, right); //분리된 리스트를 가지고 merge함수 실행
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

