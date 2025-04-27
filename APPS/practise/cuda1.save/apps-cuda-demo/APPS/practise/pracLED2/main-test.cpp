#include <stdio.h>
#include <functional>
#include "board.h"
#include "peripherals.h"
#include "pin_mux.h"
#include "clock_config.h"
#include "fsl_debug_console.h"

#include "fsl_gpio.h"
#include "fsl_port.h"
#include "fsl_mrt.h"

#include "mcxn-kit.h"

// **************************************************************************
//! System initialization. Do not modify it!!!
void _mcu_initialization() __attribute__((constructor(0x100)));

void _mcu_initialization()
{
	BOARD_InitBootPins();
	BOARD_InitBootClocks();
	BOARD_InitBootPeripherals();
	CLOCK_EnableClock(kCLOCK_Gpio0);
	CLOCK_EnableClock(kCLOCK_Gpio1);
	CLOCK_EnableClock(kCLOCK_Gpio2);
	CLOCK_EnableClock(kCLOCK_Gpio3);
	CLOCK_EnableClock(kCLOCK_Gpio4);
}
// **************************************************************************

//! Global data

#define T 20
#define LED_ROW_NUM 8
#define RGB_LEDS_NUM 3
#define BTN_NUM 4
#define SNAKE_LENGTH 8

class LED
{
public:
	DigitalOut m_led;
	uint32_t m_T0; // cas T0
	bool on;

	LED(pin_name_t t_led_pin) : m_led(t_led_pin)
	{
		m_T0 = 0;
		on = false;
	}

	void nastav_jas_proc(uint8_t t_jas_proc)
	{
		m_T0 = T * ((double)t_jas_proc / 100);
	}
};

typedef struct
{
	LED r;
	LED g;
	LED b;
} RGB_LED;

class BTN
{
public:
	DigitalIn m_btn;
	bool clicked;	// Debouncer

	BTN(pin_name_t t_btn_pin) : m_btn(t_btn_pin)
	{
		clicked = false;
	}
};

RGB_LED g_rgb_leds[RGB_LEDS_NUM] ={
	{P0_14, P0_15, P0_22},
	{P0_24, P0_25, P0_26},
	{P0_28, P0_29, P0_30}
};

LED g_red_led[] = {P3_16, P3_17};
LED g_red_arr[LED_ROW_NUM] = {P4_00, P4_01, P4_02, P4_03, P4_12, P4_13, P4_16, P4_20};
BTN g_btn_arr[BTN_NUM] = {P3_18, P3_19, P3_20, P3_21};

void pwm_control()
{
	static unsigned int tick = 0;

	// 0-7 EXTERN LEDS ROW
	for (int i = 0; i < LED_ROW_NUM; i++)
	{
		if (tick < g_red_arr[i].m_T0 && g_red_arr[i].on)
		{
			g_red_arr[i].m_led.write(1);
		}
		else
		{
			g_red_arr[i].m_led.write(0);
		}
	}

	// 2 INTERN LEDS
	if (tick < g_red_led[0].m_T0 && g_red_led[0].on)
	{
		g_red_led[0].m_led.write(1);
	}
	else
	{
		g_red_led[0].m_led.write(0);
	}

	if (tick < g_red_led[1].m_T0 && g_red_led[1].on)
	{
		g_red_led[1].m_led.write(1);
	}
	else
	{
		g_red_led[1].m_led.write(0);
	}

	// 3 RGB LEDS
	for (int i = 0; i < RGB_LEDS_NUM; i++)
	{
		if (tick < g_rgb_leds[i].r.m_T0 && g_rgb_leds[i].r.on) // R
		{
			g_rgb_leds[i].r.m_led.write(1);
		}
		else
		{
			g_rgb_leds[i].r.m_led.write(0);
		}

		if (tick < g_rgb_leds[i].g.m_T0 && g_rgb_leds[i].g.on) // G
		{
			g_rgb_leds[i].g.m_led.write(1);
		}
		else
		{
			g_rgb_leds[i].g.m_led.write(0);
		}

		if (tick < g_rgb_leds[i].b.m_T0 && g_rgb_leds[i].b.on) // B
		{
			g_rgb_leds[i].b.m_led.write(1);
		}
		else
		{
			g_rgb_leds[i].b.m_led.write(0);
		}
	}

	tick++;

	if (tick >= T)
	{
		tick = 0;
	}
}

void intensity_rgb() {	// RGB LEDS + BTN 0-3
		for (int j = 0; j < RGB_LEDS_NUM; j++)
		{
			if(g_btn_arr[0].m_btn.read() == 0) {	// BTN - R
				if(g_rgb_leds[j].r.m_T0 < 100) {
					g_rgb_leds[j].r.m_T0++;
				}
			} else {
				if(g_rgb_leds[j].r.m_T0 > 0) {
					g_rgb_leds[j].r.m_T0--;
				}
			}
			if(g_btn_arr[1].m_btn.read() == 0) {	// BTN - G
				if(g_rgb_leds[j].g.m_T0 < 100) {
					g_rgb_leds[j].g.m_T0++;
				}
			} else {
				if(g_rgb_leds[j].g.m_T0 > 0) {
					g_rgb_leds[j].g.m_T0--;
				}
			}
			if(g_btn_arr[2].m_btn.read() == 0) {	// BTN - B
				if(g_rgb_leds[j].b.m_T0 < 100) {
					g_rgb_leds[j].b.m_T0++;
				}
			} else {
				if(g_rgb_leds[j].b.m_T0 > 0) {
					g_rgb_leds[j].b.m_T0--;
				}
			}
		}
}

void inverse_led() {	// INTERN LEDS + BTN 4
	if(!g_btn_arr[BTN_NUM-1].clicked && g_btn_arr[BTN_NUM-1].m_btn.read() == 0) {
		g_red_led[0].on = !g_red_led[0].on;
		g_red_led[1].on = !g_red_led[1].on;
		g_btn_arr[BTN_NUM-1].clicked = true; // Debounce btn
	}
	if(g_btn_arr[BTN_NUM-1].clicked && g_btn_arr[BTN_NUM-1].m_btn.read() == 1) {
		g_btn_arr[BTN_NUM-1].clicked = false; // Debounce btn
	}
}

void snake_led() {	// EXTERN LEDS + BTN 4
	static int head_pos = -1;
	int tail_pos = head_pos - SNAKE_LENGTH;
	
	for (int i = 0; i <= head_pos; i++)
	{
		if(i < LED_ROW_NUM) {
			if(i < tail_pos) {	// check tail position
				g_red_arr[i].on = false;
			} else {
				g_red_arr[i].on = true;
			}

			if(i < head_pos) {	// all leds before head
				g_red_arr[i].nastav_jas_proc(10);
			} else {		// head led
				g_red_arr[head_pos].on = true;
				g_red_arr[head_pos].nastav_jas_proc(100);
			}
		}
	}
	
	if(head_pos > -1 || (!g_btn_arr[BTN_NUM-1].clicked && g_btn_arr[BTN_NUM-1].m_btn.read() == 0)) { // if the snake has already started or if the button was pressed
		if (head_pos - SNAKE_LENGTH < SNAKE_LENGTH) {	// if the snake has not yet finished 
			head_pos++;
		} else {
			head_pos = -1;
		}
		g_btn_arr[BTN_NUM-1].clicked = true;		// disable click
	}
	if(head_pos == -1 && g_btn_arr[BTN_NUM-1].clicked) {	// allow click
		g_btn_arr[BTN_NUM-1].clicked = false;
	}
}


int main()
{

	Ticker pwm;
	Ticker intensity;
	Ticker inverse;
	Ticker snake;
	pwm.attach(pwm_control, 1);						// every 1ms update state
	intensity.attach(intensity_rgb, 20);	// every 20ms update state
	inverse.attach(inverse_led, 1);
	snake.attach(snake_led, 1);

	// 2 INTERN LEDS
	g_red_led[0].nastav_jas_proc(100);
	g_red_led[1].nastav_jas_proc(100);

	// 0-7 EXTERN LEDS ROW
	for (int i = 0; i < LED_ROW_NUM; i++)
	{
		g_red_arr[i].nastav_jas_proc(10);
		g_red_arr[i].on = false;
	}

	// 3 RGB LEDS
	for (int i = 0; i < RGB_LEDS_NUM; i++)
	{
		g_rgb_leds[i].r.nastav_jas_proc(10);
		g_rgb_leds[i].g.nastav_jas_proc(10);
		g_rgb_leds[i].b.nastav_jas_proc(10);
	}

	while (1)
		__WFI();
}
