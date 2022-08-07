#include"wtgraph.h"
#include<iostream>
using namespace std;
int *pathMatrix;
WtGraph::WtGraph(int maxNumber) : maxSize(maxNumber),size (0){
	
	vertexList = new Vertex[maxNumber];

	adjMatrix = new int[maxSize*maxSize];
	pathMatrix = new int[maxSize*maxSize];
	for (int i = 0; i < maxSize*maxSize; i++) {
		pathMatrix[i] = infiniteEdgeWt;
	}
}


WtGraph::~WtGraph() {
	delete[] vertexList;
	
	delete[] adjMatrix;
	delete[] pathMatrix;
}

void WtGraph::insertVertex(Vertex newVertex) {
	if (!full() && index(newVertex.label)==size) {
		vertexList[size++] = newVertex;

		for (int i = 0; i <= size - 1; i++) {
			edge(i,size-1) = infiniteEdgeWt;
			edge(size - 1,i) = infiniteEdgeWt;
		}
		edge(size - 1, size - 1) = 0;
	}
	else {
		for (int i = 0; i < maxSize; i++) {
			edge(i, index(newVertex.label)) = infiniteEdgeWt;
			edge(index(newVertex.label),i) = infiniteEdgeWt;
		}
	}
}

void WtGraph::insertEdge(char *v1, char *v2, int wt) {
	if (!empty()) {
		int i1 = index(v1);
		int i2 = index(v2);

		if(i1 == size || i2 == size){ cout << "Not found" << endl; }
		else {
			edge(i1,i2) = wt;
			edge(i2,i1) = wt;
		}


	}

}

int WtGraph::retrieveVertex(char* v, Vertex& vData) const {
	int i = index(v);

	if (i == size) {	return 0;	}
	else{
		vData = vertexList[i];
		return 1;
	}

}

int WtGraph::edgeWeight(char* v1, char* v2, int& wt) {
	int i1 = index(v1);
	int i2 = index(v2);

	if (i1 == size || i2 == size) { 
		cout << "Not found" << endl; 
		return 0;
	}
	else {
		wt = edge(i1,i2);
		return 1;
	}
}


void WtGraph::removeVertex(char* v) {
	if (!empty()) {
		int i = index(v);
		

		if (i == size) {	cout << "Not found" << v << endl;	}
		else {
			size--;
			for (int j = i; j < size; j++) {
				vertexList[j] = vertexList[j + 1];		
				for (int j2 = 0; j2 < maxSize; j2++) {
					edge(j,j2) = edge(j+1,j2);
				}
			}


			for (int j3 = 0; j3 < maxSize-1; j3++) {
				for (int j4 = i; j4 < maxSize-1; j4++) {
					edge(j3, j4) = edge(j3, j4 + 1);
				}
			}
			
			
		}

		
	}
}


void WtGraph::removeEdge(char* v1, char* v2) {
	int i1 = index(v1);
	int i2 = index(v2);

	if (i1 == size || i2 == size) { cout << "Not found" << endl; }
	else {
		edge(i1,i2) = 0;
	}
}


void WtGraph::clear() {
	size = 0;
}

int WtGraph::empty() const {
	return size == 0;
}

int WtGraph::full() const {
	return size == maxSize;
}


void WtGraph::showStructure() {
	if (!empty()) {
		cout << "vertax" <<endl;
		for (int i = 0; i < size; i++) {
			cout << i << "\t" << vertexList[i].label <<endl;
		}
		cout << endl;


		cout << "Edge" <<endl;
		for (int i = 0; i < size; i++) {
			cout << "\t" << i;
		}
		cout << endl;
		for (int i = 0; i < size; i++) {
			cout << i <<"\t";
			for (int j = 0; j < size; j++) {
				if (edge(i,j) != infiniteEdgeWt && edge(i,j) !=0) {
					cout << edge(i,j) << "\t";
				}
				else {
					cout << "-" << "\t";
				}
			}
			cout << endl;
		}

		cout << "Path" << endl;
		for (int i = 0; i < size; i++) {
			cout << "\t" << i;
		}
		cout << endl;
		for (int i = 0; i < size; i++) {
			cout << i << "\t";
			for (int j = 0; j < size; j++) {
				if (pathMatrix[i*maxSize+j] != infiniteEdgeWt && pathMatrix[i*maxSize + j] != 0) {
					cout << pathMatrix[i*maxSize + j] << "\t";
				}
				else {
					cout << "-" << "\t";
				}
			}
			cout << endl;
		}
	}
}

int WtGraph::index(char* v) const {
	int i = 0;
	for (; i < size; i++) {
		if (strcmp(v, vertexList[i].label)==0) {
			break;
		}
	}
	return i;
}

int& WtGraph::edge(int row, int col) {
	return adjMatrix[row*maxSize + col];
}




void WtGraph::computePaths() {
	if (!empty()) {
		for (int i = 0; i < maxSize*maxSize; i++) {
			pathMatrix[i] = adjMatrix[i];
		}

		for (int m = 0; m < size; m++) {
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < size; k++) {
					if (edge(j, m) != infiniteEdgeWt && edge(m, k) != infiniteEdgeWt && j!=k) {
						if (pathMatrix[j*maxSize + k] > (pathMatrix[j*maxSize + m] + pathMatrix[m*maxSize + k])) {
							pathMatrix[j*maxSize + k] = pathMatrix[j*maxSize + m] + pathMatrix[m*maxSize + k];
						}
					}
				}
			}
		}
	}
}