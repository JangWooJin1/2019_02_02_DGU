#include "exprtree.h"
#include <iostream>
using namespace std;

ExprTreeNode::ExprTreeNode(char elem, ExprTreeNode *leftPtr, ExprTreeNode *rightPtr) : element(elem), left(leftPtr), right(rightPtr) {}


ExprTree::ExprTree()
{
	root = NULL;
}
ExprTree::~ExprTree()
{
	clear();
}


void ExprTree::build()
{
	char *expr = new char[20];
	cin >> expr;
	buildSub(root,expr);

}
/*
void ExprTree::buildSub(ExprTreeNode*& p)
{
	if (expr[index] == '\0') { return; }

	
	else {
		if (expr[index] == '+' || expr[index] == '-' || expr[index] == '*' || expr[index] == '/') {
			ExprTreeNode* NewNode = new ExprTreeNode(expr[index++], NULL, NULL);
			p = NewNode;
			buildSub(p->left);
			buildSub(p->right);
		}
		else {
			ExprTreeNode* NewNode = new ExprTreeNode(expr[index++], NULL, NULL);
			p = NewNode;
		}
	}
}
*/

void ExprTree::buildSub(ExprTreeNode*& p, char *&c)
{
	//if (*c == '\0') { return; }
	 
		ExprTreeNode* NewNode = new ExprTreeNode(*c, NULL, NULL);
		p = NewNode;
		if (*c == '+' || *c == '-' || *c == '*' || *c == '/') {
			buildSub(p->left, ++c);
			buildSub(p->right, ++c);
		}
		
	
}
void ExprTree::expression() const
{
	exprSub(root);
}
void ExprTree::exprSub(ExprTreeNode* p) const
{
	if (p != NULL) {
		
		if (p->left != NULL) { cout << "("; }
		exprSub(p->left);
		cout << p->element;
		exprSub(p->right);
		if (p->right != NULL) { cout << ")"; }
	}
}

float ExprTree::evaluate() const
{
	return evaluateSub(root);
}
float ExprTree::evaluateSub(ExprTreeNode* p) const
{
	if (p == NULL) {
		return 0;
	}
	else if(p->element == '+'){
		return evaluateSub(p->left) + evaluateSub(p->right);
	}
	else if (p->element == '-') {
		return evaluateSub(p->left) - evaluateSub(p->right);
	}
	else if (p->element == '*') {
		return evaluateSub(p->left) * evaluateSub(p->right);
	}
	else if (p->element == '/') {
		return evaluateSub(p->left) / evaluateSub(p->right);
	}
	else {
		return float(p->element-'0');
	}
}

void ExprTree::clear()
{
	clearSub(root);
	root = NULL;
}
void ExprTree::clearSub(ExprTreeNode* p)
{
	if (p != NULL) {
		clearSub(p->left);
		clearSub(p->right);
		delete p;
	}
}

void ExprTree::showStructure() const
{
	showSub(root,1);
}
void ExprTree::showSub(ExprTreeNode* p, int level) const
{
	if (p != NULL) {
		showSub(p->left, level + 1);
		for (int i = 0; i <= level; i++) {
			cout << "    ";
		}
		cout << p->element;
		if (p->left != NULL && p->right != NULL) { cout << "<"; }
		cout << endl;
		showSub(p->right, level + 1);
	}
}