#ifndef FUNKCE_H
#define FUNKCE_H
#include <iostream>
#include <fstream>
#define MAX 100
using namespace std;


class Node {
	private:
		string name;
		int data;

		Node *Prev;
		Node *Next;
	public:
		Node() {
			this->Prev = nullptr;
			this->Next = nullptr;
		}

		Node(string name, int data) {
			this->name = name;
			this->data = data;
			this->Prev = nullptr;
			this->Next = nullptr;
		}

		void SetPrev(Node *new_node) {
			this->Prev = new_node;
		}
		void SetNext(Node *new_node) {
			this->Next = new_node;
		}
		void SetName(string name) {
			this->name = name;
		}
		void SetData(int data) {
			this->data = data;
		}
		void SetNameData(string name, int data) {
			this->name = name;
			this->data = data;
		}
		Node* GetPrev() {
			return this->Prev;
		}
		Node* GetNext() {
			return this->Next;
		}
		string GetName() {
			return this->name;
		}
		int GetData() {
			return this->data;
		}
};

class List {
	private:
		Node *first;
	public:
		List() {
			first = nullptr;
		}
		~List() {
			Node *temp;

			for (temp = this->first; temp != nullptr; temp = temp->GetNext()) {
				Node *temp2 = temp;
				delete temp2;
			}
		}
		bool NodeAddStart(string name, int data);
		bool NodeAddEnd(string name, int data);
		void printList();
		bool Search(string name);
		bool LoadTxt(string filename);
		bool StoreTxt(string filename);
		void DataSort(bool vzestupne);
		void NameSort(bool vzestupne);
		bool EditName(string name);
		bool RemoveNode(int id);
		bool MinMax();
		int Sum();
		float Avg();
};

#endif
