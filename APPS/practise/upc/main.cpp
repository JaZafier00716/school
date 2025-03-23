#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <thread>
#define T 50

static int P3_16, P3_17;

class BTN
{
  private:
  int state;
  bool clicked;
  public:
  BTN(int state) : state(state){
    clicked = false;
  }
  
  int read() const {
    return this->state;
  }
  
  void write(int value) {
    this->state = value;
  }

  bool getClick() {
    return this->clicked;
  }
  void setClick(bool click) {
    this->clicked = click;
  }
};


class LED
{
  public:
  int m_led;
  uint32_t m_T0; // cas T0
  bool unlocked_m_led;
  
  LED(uint32_t t_led_pin) : m_led(t_led_pin)
  {
    m_T0 = 0;
    unlocked_m_led = 1;
  }
  
  void nastav_jas_proc(uint8_t t_jas_proc)
  {
    m_T0 = (double)T * ((double)t_jas_proc / 100);
  }
  
  void lock_m_led() {
    unlocked_m_led = 0;
  }
  void unlock_m_led() {
    unlocked_m_led = 1;
  }
  
  void write(int value) {
    this->m_led = value;
  }
};

LED g_red_led[] = {P3_16, P3_17};
BTN g_sw_P3_18(1), g_sw_P3_19(1);

void pwm_control()
{
  static int tick = 0;
  if(tick < g_red_led[0].m_T0) {
    g_red_led[0].write(1);
  } else {
    g_red_led[0].write(0);
  }
  if(tick < g_red_led[1].m_T0) {
    g_red_led[1].write(1);
  } else {
    g_red_led[1].write(0);
  }
  tick++;
  if(tick >= T) {
    tick = 0;
  }
}

void set_leds() {
  if(g_sw_P3_18.read() == 0 && !g_sw_P3_18.getClick()) {
    g_red_led[0].lock_m_led();
    g_red_led[1].lock_m_led();
    printf("|");
    g_sw_P3_18.setClick(true);
  }

  if(g_sw_P3_18.read() == 1 && g_sw_P3_18.getClick()) {
    g_sw_P3_18.setClick(false);
  }

  if(g_sw_P3_19.read() == 0 && !g_sw_P3_19.getClick()) {
    g_red_led[0].unlock_m_led();
    g_red_led[1].unlock_m_led();
    g_sw_P3_19.setClick(true);
  }

  if(g_sw_P3_19.read() == 1 && g_sw_P3_19.getClick()) {
    g_sw_P3_19.setClick(false);
  }
}

void print_led_state(int i) {
  if(g_red_led[i].m_led == 1 && g_red_led[i].unlocked_m_led) {
    printf("|");
  } else {
    printf("_");
  }
}

void ticker()
{
  g_red_led[0].nastav_jas_proc(5);
  g_red_led[1].nastav_jas_proc(50);

  printf("l1:\t%d\n", g_red_led[0].m_T0);

  printf("l2:\t%d\n", g_red_led[1].m_T0);
  while (1)
  {
    set_leds();
    pwm_control();
    print_led_state(0);
    usleep(1000);
  }
}

void ticker2() {
  while(1) {
    g_sw_P3_18.write(0);
    usleep(500000);  //500ms
    g_sw_P3_18.write(1);
    usleep(5000000); // every 5 sec
    g_sw_P3_19.write(0);
    usleep(500000);  //500ms
    g_sw_P3_19.write(1);
    usleep(10000000); // every 5 sec
  }
}

int main()
{
  std::thread pwm1(ticker);
  std::thread pwm2(ticker2);
  pwm1.join();
  pwm2.join();
}