#pragma once
#include "Elektronika.h"

class Computer : public Elektronika
{
private:
  string cpu;
  int ram_gb;
  int ssd_gb;
public:
  Computer(string cpu, int ram_gb, int ssd_gb) : cpu(cpu), ram_gb(ram_gb), ssd_gb(ssd_gb){};
  void printParameters() const override {
    cout << "Computer:" << endl;
    cout << " - CPU: " << this->cpu << endl;
    cout << " - ram: " << this->ram_gb << "GB" << endl;
    cout << " - ssd: " << this->ssd_gb << "GB" << endl;
  }
};

