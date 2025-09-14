#include "funkce.h"

bool List::NodeAddStart(string name, int data) {
	Node *new_node = new Node(name, data);
	Node *temp;
	if(!new_node) {
		return false;
	}
	if(this->first == nullptr) {
		this->first = new_node;
	} else {
		for(temp = this->first;temp != nullptr; temp = temp->GetNext()) {
			if(temp->GetName() == name) {
				string edit;
				do{
					cout << "Nazev uz v poli existuje, chcete prepsat jeho data z " << temp->GetData() << " na " << data << "? (ANO, NE): ";
					cin >> edit;
				}while(edit != "ANO" && edit != "ano" && edit != "NE" && edit != "ne");
				if(edit == "ANO" || edit == "ano") {
					temp->SetData(data);
				}
				return true;
			}
		}
		new_node->SetNext(this->first);
		this->first->SetPrev(new_node);
		this->first = new_node;
	}
	
	return true;
}

bool List::NodeAddEnd(string name, int data) {
	Node *new_node = new Node(name, data);
	Node *temp;
	
	if(!new_node) {
		return false;
	}
	if(this->first == nullptr) {
		this->first = new_node;
	} else {
		for(temp = this->first;temp->GetNext() != nullptr; temp = temp->GetNext()) {
			if(temp->GetName() == name) {
				string edit;
				do{
					cout << "Nazev uz v poli existuje, chcete prepsat jeho data z " << temp->GetData() << " na " << data << "? (ANO, NE): ";
					cin >> edit;
				}while(edit != "ANO" && edit != "ano" && edit != "NE" && edit != "ne");
				if(edit == "ANO" || edit == "ano") {
					temp->SetData(data);
				}
				return true;
			}
		}
		if(temp->GetName() == name) {
			string edit;
			do{
				cout << "Nazev uz v poli existuje, chcete prepsat jeho data z " << temp->GetData() << " na " << data << "? (ANO, NE): ";
				cin >> edit;
			}while(edit != "ANO" && edit != "ano" && edit != "NE" && edit != "ne");
			if(edit == "ANO" || edit == "ano") {
				temp->SetData(data);
			}
			return true;
		}
		temp->SetNext(new_node);
		new_node->SetPrev(temp);
	}
	
	return true;
}

void List::printList() {
	Node *temp;
	int i;	
	if(this->first == nullptr) {
		cout << "-------------" << endl;
		cout << "List is empty" << endl;
		cout << "-------------" << endl << endl;
		return;
	}
	
	cout << "-------------" << endl;
	cout << "List: " << endl;
	cout << "-------------" << endl;
	for(temp = this->first, i=0;temp != nullptr;i++, temp = temp->GetNext()) {
		cout << "ID: " << i << endl;
		cout << "Name: " << temp->GetName() << endl;
		cout << "Data: " << temp->GetData() << endl;
		cout << "-------------" << endl;
	}
	cout << endl;
}

bool List::Search(string name) {
	Node *temp;
	
	if(this->first == nullptr) {
		return false;
	}
	
	for(temp = this->first;temp != nullptr; temp = temp->GetNext()) {
		if(temp->GetName() == name) {
			cout << "Data: " << temp->GetData() << endl;
			return true;
		}
	}
	return false;
}

bool List::LoadTxt(string filename) {
	ifstream f;
	string name, data;
	f.open(filename);
	if(f.fail()) {
		return false;
	}
	while(getline(f, name, ';') && getline(f, data, ';')){
		if(name[0] == '\n') {
			name = name.substr(1);
		}
		if(!this->NodeAddEnd(name, stoi(data))) {
			return false;
		}
	}
	f.close();
	return true;
}

bool List::StoreTxt(string filename) {
	ofstream f;
	Node *temp;
	
	f.open(filename);
	if(f.fail()) {
		return false;
	}
	
	for(temp = this->first; temp != nullptr; temp = temp->GetNext()){
		f << temp->GetName() << ";" << temp->GetData() << ";" << endl;
	}
	f.close();
	return true;
}

void List::DataSort(bool vzestupne) {
	Node *temp;
	bool swap = false;
	
	if(this->first == nullptr || this->first->GetNext() == nullptr) {
		return;
	}
	if(vzestupne) {
		do {
			swap = false;
			for(temp = this->first; temp->GetNext() != nullptr; temp = temp->GetNext()) {
				if(temp->GetData() > temp->GetNext()->GetData()) {
					swap = true;
					Node x = *temp;
					temp->SetNameData(temp->GetNext()->GetName(), temp->GetNext()->GetData());
					temp->GetNext()->SetNameData(x.GetName(), x.GetData());
				}
			}
		}while(swap);
	} else {
		do {
			swap = false;
			for(temp = this->first; temp->GetNext() != nullptr; temp = temp->GetNext()) {
				if(temp->GetData() < temp->GetNext()->GetData()) {
					swap = true;
					Node x = *temp;
					temp->SetNameData(temp->GetNext()->GetName(), temp->GetNext()->GetData());
					temp->GetNext()->SetNameData(x.GetName(), x.GetData());
				}
			}
		}while(swap);
	}
}

void List::NameSort(bool vzestupne) {
	Node *temp;
	bool swap = false;
	
	if(this->first == nullptr || this->first->GetNext() == nullptr) {
		return;
	}
	if(vzestupne) {
		do {
			swap = false;
			for(temp = this->first; temp->GetNext() != nullptr; temp = temp->GetNext()) {
				if(temp->GetName() > temp->GetNext()->GetName()) {
					swap = true;
					Node x = *temp;
					temp->SetNameData(temp->GetNext()->GetName(), temp->GetNext()->GetData());
					temp->GetNext()->SetNameData(x.GetName(), x.GetData());
				}
			}
		}while(swap);
	} else {
		do {
			swap = false;
			for(temp = this->first; temp->GetNext() != nullptr; temp = temp->GetNext()) {
				if(temp->GetName() < temp->GetNext()->GetName()) {
					swap = true;
					Node x = *temp;
					temp->SetNameData(temp->GetNext()->GetName(), temp->GetNext()->GetData());
					temp->GetNext()->SetNameData(x.GetName(), x.GetData());
				}
			}
		}while(swap);
	}
}

bool List::EditName(string name) {
	Node *temp;
	string new_name;
	
	for(temp = this->first;temp != nullptr; temp = temp->GetNext()) {
		if(temp->GetName() == name) {
			cout << "Zadejte novy nazev: ";
			cin >> new_name;
			
			temp->SetName(new_name);
			return true;
		}
	}
	return false;
}

bool List::RemoveNode(int id) {
	Node *temp;
	int i;
	// Prazdne pole
	if(this->first == nullptr) {
		return false;
	}
	// Jediny zaznam a prvni zaznam
	if(id == 0) {
		this->first = this->first->GetNext();
		return true;
	}
	for(i=0, temp = this->first;temp != nullptr;temp = temp->GetNext(), i++){
//		cout << "id: " << id << endl;
//		cout << "i: " << i << endl;
		if(i == id) {
			// Odstraneni posledniho zaznamu
			if(temp->GetNext() == nullptr) {
				temp->GetPrev()->SetNext(nullptr);
				return true;
			}
			temp->GetNext()->SetPrev(temp->GetPrev());
			temp->GetPrev()->SetNext(temp->GetNext());
			return true;
		}
	}
	return false;
}

bool List::MinMax() {
	Node *temp;
	int min, max;
	
	if(this->first == nullptr) {
		return false;
	}
	
	min = this->first->GetData();
	max = this->first->GetData();
	for(temp = this->first; temp != nullptr; temp = temp->GetNext()) {
		if(temp->GetData() < min) {
			min = temp->GetData();
		}
		if(temp->GetData() > max) {
			max = temp->GetData();
		}
	}
	cout << "-----------" << endl;
	cout << "min: " << min << endl;
	cout << "max: " << max << endl;
	cout << "-----------" << endl << endl;
	
	return true;
}

int List::Sum() {
	Node *temp;
	int sum = 0;
	if(this->first == nullptr) {
		return false;
	}
	for(temp = this->first; temp != nullptr; temp = temp->GetNext()) {
		sum +=temp->GetData();
	}
	return sum;
}

float List::Avg() {
	Node *temp;
	float sum = 0;
	int i;
	if(this->first == nullptr) {
		return false;
	}
	
	for(i=0, temp = this->first; temp != nullptr; temp = temp->GetNext(), i++) {
		sum +=temp->GetData();
	}
	return sum/i;
}
