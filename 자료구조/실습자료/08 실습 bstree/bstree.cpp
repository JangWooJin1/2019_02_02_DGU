#include "bstree.h"

template < class TE, class KF >																			// TE : tree element, KF : key field
BSTreeNode<TE, KF>::BSTreeNode(const TE &elem, BSTreeNode *leftPtr, BSTreeNode *rightPtr)				// constructor
	: element(elem), left(leftPtr), right(rightPtr)
{
}

template < class TE, class KF >
BSTree <TE, KF>::BSTree()																				// constructor
	: root(NULL)
{
}

template <class TE, class KF>
BSTree<TE, KF>::~BSTree()																				// Destructor
{
	//pre-lab
	clear();
}

template <class TE, class KF>
void BSTree<TE, KF>::insert(const TE &newElement)														// Insert element
{
	//pre-lab
	if (empty()) {
		BSTreeNode<TE, KF>* NEW = new BSTreeNode<TE, KF>(newElement, NULL, NULL);
		root = NEW;
	}
	else {
		if (root->element.key() < newElement.key()) {
			insertSub(root->right, newElement);
		}
		else if(root->element.key() > newElement.key()) {
			insertSub(root->left, newElement);
		}
	}
}

template <class TE, class KF>
int BSTree<TE, KF>::retrieve(KF searchKey, TE &searchElement) const										// Retrieve element
{
	//pre-lab
	return retrieveSub(root, searchKey, searchElement);

}

template <class TE, class KF>
int BSTree<TE, KF>::remove(KF deleteKey)																// Remove element
{
	//pre-lab
	return removeSub(root, deleteKey);

}

template <class TE, class KF>
void BSTree<TE, KF>::writeKeys() const																	// Output keys
{
	//pre-lab
	writeKeysSub(root);
	cout << endl;
}

template <class TE, class KF>
void BSTree<TE, KF>::clear()																			// Clear tree
{
	//pre-lab
	clearSub(root);
	root = NULL;
}

template <class TE, class KF>
int BSTree<TE, KF>::empty() const																		// Tree is empty
{
	//pre-lab
	return root == NULL;
}

template <class TE, class KF>
int BSTree<TE, KF>::full() const																		// Tree is full
{
	return 0;
}

template <class TE, class KF>
void BSTree<TE, KF>::showStructure() const
{
	if (root == NULL)
	{
		cout << "Empty tree" << endl;
	}
	else
	{
		cout << endl;
		showSub(root, 1);
		cout << endl;
	}
}


template <class TE, class KF>
void BSTree<TE, KF>::insertSub(BSTreeNode<TE, KF> *&p, const TE &newElement)
{
	//pre-lab
	if (p == NULL) {
		BSTreeNode<TE,KF>* NEW = new BSTreeNode<TE,KF>(newElement, NULL, NULL);
		p = NEW;
	}
	else if (p->element.key() < newElement.key()) {
		insertSub(p->right, newElement);
	}
	else if (p->element.key() > newElement.key()) {
		insertSub(p->left, newElement);
	}
}

template <class TE, class KF>
int BSTree<TE, KF>::retrieveSub(BSTreeNode<TE, KF> *p, KF searchKey, TE &searchElement) const
{
	//pre-lab
	if (p == NULL) {
		return 0;
	}
	else if (p->element.key() == searchKey) {
		searchElement = p->element;
		return 1;
	}
	else if(p->element.key() > searchKey){
		return retrieveSub(p->left, searchKey, searchElement);
	}
	else if (p->element.key() < searchKey) {
		return retrieveSub(p->right, searchKey, searchElement);
	}

}

template <class TE, class KF>
int BSTree<TE, KF>::removeSub(BSTreeNode<TE, KF>*& p, KF deleteKey)
{
	//pre-lab

	if (p == NULL) {	return 0;	}

	else if (p->element.key() > deleteKey) {	return removeSub(p->left, deleteKey); 	}

	else if (p->element.key() < deleteKey) {	return removeSub(p->right, deleteKey); 	}

	else  { 

		BSTreeNode<TE, KF>* del = p;

		if (p->right == NULL) { //���� ����� �ڽ��� �Ѱ� �ΰ��(����) clear
			p = del->left;
			delete del;
		}
		else if (p->left == NULL) { //���� ����� �ڽ��� �Ѱ� �ΰ��(������) clear
			p = del->right;
			delete del;
		}

		else { //���� ����� �ڽ��� 2���� ��� (+200 +1000 +100 +110 +20 +10 +80 +60 ���� 100�� ����� 60�� ������� ����)
				
			BSTreeNode<TE, KF>* r = del->left; //��ü�� ���(���� ��忡�� ���� ū ���)
			while (r->right != NULL) { //�������� �ѹ����� �״������� ���������� ���� ���ʿ��� ���� ū ��� ��
				r = r->right;
			}
			p->element = r->element; //������ ��� ���� ��ü�� ��� ������ �ֱ�
			removeSub(p->left, r->element.key());
		}
		return 1;
	}

}

template <class TE, class KF>
void BSTree<TE, KF>::cutRightmost(BSTreeNode<TE, KF> *&r, BSTreeNode<TE, KF> *&delPtr)
{
	//pre-lab
	
	
	
}

template <class TE, class KF>
void BSTree<TE, KF>::writeKeysSub(BSTreeNode<TE, KF> *p) const
{
	//pre-lab
	if (p != NULL) {
		writeKeysSub(p->left);
		cout << p->element.key() << " ";
		writeKeysSub(p->right);
	}
}

template <class TE, class KF>
void BSTree<TE, KF>::clearSub(BSTreeNode<TE, KF> *p)
{
	//pre-lab
	if (p != NULL) {
		clearSub(p->left);
		clearSub(p->right);
		delete p;
	}
}

template <class TE, class KF>
void BSTree<TE, KF>::showSub(BSTreeNode<TE, KF> *p, int level) const
{
	int i;

	if (p != NULL)
	{
		showSub(p->right, level + 1);
		for (i = 0; i < level; i++)
		{
			cout << '\t';
		}
		cout << " " << p->element.key();
		if ((p->left != 0) && (p->right != 0))
		{
			cout << "<";
		}
		else if (p->right != 0)
		{
			cout << "/";
		}
		else if (p->left != 0)
		{
			cout << "\\";
		}
		cout << endl;
		showSub(p->left, level + 1);
	}
}

 /*********************************************************************/
 /*                           In-Lab                                  */ 
 /*********************************************************************/
// In-lab operations
template <class TE, class KF>
int BSTree<TE, KF>::height() const																		// Height of tree
{
	//in-lab
	return heightSub(root);
}

template <class TE, class KF>
void BSTree<TE, KF>::writeLessThan(KF searchKey) const													// Output keys
{
	//in-lab
	writeLTSub(root, searchKey);
}


template <class TE, class KF>
int BSTree<TE, KF>::heightSub(BSTreeNode<TE, KF> *p) const
{
	//in-lab
	int i1 = 1;
	int i2 = 1;
	if (p != NULL) {
		
		i1 += heightSub(p->left);
		i2 += heightSub(p->right);
		if (i1 < i2) {
			return i2;
		}
		else {
			return i1;
		}
	}
	else {
		return 0;
	}
	
}

template <class TE, class KF>
void BSTree<TE, KF>::writeLTSub(BSTreeNode<TE, KF> *p, KF searchKey) const
{
	//in-lab
	if (p != NULL) {
		writeLTSub(p->left, searchKey);
		if (p->element.key() <= searchKey) { cout << p->element.key() << " "; }
		writeLTSub(p->right, searchKey);
	}
};



template <class TE, class KF>
void BSTree<TE, KF>::printKth(BSTreeNode<TE, KF>* p, int k) {
	if (p != NULL) {
		count++;
		int max = count;
		printKth(p->right, k);
		
		cout << max;
		count--;
		if (max - count == k)
			cout << p->element.key() << endl;
	}
}
