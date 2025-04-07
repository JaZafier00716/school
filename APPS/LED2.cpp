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

void _mcu_initialization() {
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

class LED {
public:
	DigitalOut m_led;
	uint32_t m_T0; // cas T0
	bool on;
	uint8_t jas_proc;

	LED(pin_name_t t_led_pin) :
			m_led(t_led_pin) {
		m_T0 = 0;
		on = false;
	}

	void nastav_jas_proc(uint8_t t_jas_proc) {
		m_T0 = T * ((double) t_jas_proc / 100);
		jas_proc = t_jas_proc;

	}
};

typedef struct {
	LED r;
	LED g;
	LED b;
} RGB_LED;

class BTN {
public:
	DigitalIn m_btn;
	bool clicked;	// Debouncer

	BTN(pin_name_t t_btn_pin) :
			m_btn(t_btn_pin) {
		clicked = false;
	}
};

RGB_LED g_rgb_leds[RGB_LEDS_NUM] = { { P0_14, P0_15, P0_22 }, { P0_24, P0_25,
		P0_26 }, { P0_28, P0_29, P0_30 } };

typedef struct {
	BTN btn;
	bool free;
} Gate;

LED g_red_led[] = { P3_16, P3_17 };
LED g_red_arr[LED_ROW_NUM] = { P4_00, P4_01, P4_02, P4_03, P4_12, P4_13, P4_16,
		P4_20 };
BTN g_btn_P3_21 = { P3_21 };
Gate g_gate_arr[3] = { { P3_18, false }, { P3_19, false }, { P3_20, false } };
static int open = 0;	// gate open
static int start = false;
static int current = -1;
static int close_gate_i = 0;

void pwm_control() {
	static unsigned int tick = 0;

	// 0-7 EXTERN LEDS ROW
	for (int i = 0; i < LED_ROW_NUM; i++) {
		if (tick < g_red_arr[i].m_T0 && g_red_arr[i].on) {
			g_red_arr[i].m_led.write(1);
		} else {
			g_red_arr[i].m_led.write(0);
		}
	}

	// 2 INTERN LEDS
	if (tick < g_red_led[0].m_T0 && g_red_led[0].on) {
		g_red_led[0].m_led.write(1);
	} else {
		g_red_led[0].m_led.write(0);
	}

	if (tick < g_red_led[1].m_T0 && g_red_led[1].on) {
		g_red_led[1].m_led.write(1);
	} else {
		g_red_led[1].m_led.write(0);
	}

	// 3 RGB LEDS
	for (int i = 0; i < RGB_LEDS_NUM; i++) {
		if (tick < g_rgb_leds[i].r.m_T0 && g_rgb_leds[i].r.on) // R
				{
			g_rgb_leds[i].r.m_led.write(1);
		} else {
			g_rgb_leds[i].r.m_led.write(0);
		}

		if (tick < g_rgb_leds[i].g.m_T0 && g_rgb_leds[i].g.on) // G
				{
			g_rgb_leds[i].g.m_led.write(1);
		} else {
			g_rgb_leds[i].g.m_led.write(0);
		}

		if (tick < g_rgb_leds[i].b.m_T0 && g_rgb_leds[i].b.on) // B
				{
			g_rgb_leds[i].b.m_led.write(1);
		} else {
			g_rgb_leds[i].b.m_led.write(0);
		}
	}

	tick++;

	if (tick >= T) {
		tick = 0;
	}
}

void vehicle_leave() {	// RGB LEDS + BTN 0-3
	static int led_on = true;
	static int blink = 0;
	if (open) {
		if (g_gate_arr[current].free) {	// current enter
			if (g_rgb_leds[current].r.m_T0 > 0) {
				g_rgb_leds[current].r.nastav_jas_proc(
						g_rgb_leds[current].r.jas_proc - 1);
			} else {
				if (g_rgb_leds[current].g.m_T0 < T / 2) {
					g_rgb_leds[current].g.nastav_jas_proc(
							g_rgb_leds[current].g.jas_proc + 1);

				}
			}
		} else {
			if (g_rgb_leds[current].g.jas_proc > 0) {
				if (blink < 700) {
						g_rgb_leds[current].g.on = led_on;
					if(blink % 200 == 0) {
						led_on = !led_on;
						close_gate_i--;
					}
					blink++;
				} else {
					g_rgb_leds[current].g.on = true;
					g_rgb_leds[current].g.nastav_jas_proc(
						g_rgb_leds[current].g.jas_proc - 1);
				}
			} else {
				g_rgb_leds[current].r.on = true;
				if (g_rgb_leds[current].r.jas_proc < 50) {
					g_rgb_leds[current].r.nastav_jas_proc(g_rgb_leds[current].r.jas_proc + 1);
				} else {
					blink = 0;
				}
			}
	}
}
}

void close_gate() {
if (close_gate_i > 1000) {
start = true;
close_gate_i = 0;
}
if (open) {
	close_gate_i++;
}
}

void snake_led() {	// EXTERN LEDS + BTN 4
static int head_pos = SNAKE_LENGTH - 1;
if (!start) {
if (g_btn_P3_21.m_btn.read() == 0 && !g_btn_P3_21.clicked && !open) {
	for (int i = 0; i < 3; i++) {
		if (g_gate_arr[i].free) {
			current = i;
			break;
		}
	}
	start = true;
} else {

	for (int i = 0; i < 3; i++) {
		if (!g_gate_arr[i].btn.clicked && g_gate_arr[i].btn.m_btn.read() == 0
				&& !open) {
			current = i;
			start = true;
			break;
		}
	}
}
}

if (start) {
// set the head
if (!open) {
	if (head_pos + 1 < LED_ROW_NUM) {
		g_red_arr[head_pos + 1].on = false;
	}
	g_red_arr[head_pos].on = true;
	g_red_arr[head_pos].nastav_jas_proc(100);

	if (head_pos > 0) {
		head_pos--;
	} else {
		g_gate_arr[current].free = !g_gate_arr[current].free;
		open = true;
		start = false;
	}
} else {
	if (head_pos - 1 >= 0) {
		g_red_arr[head_pos - 1].nastav_jas_proc(10);
		g_red_arr[head_pos - 1].on = 1;
	}
	g_red_arr[head_pos].on = true;
	g_red_arr[head_pos].nastav_jas_proc(100);

	if (head_pos + 1 < LED_ROW_NUM) {
		head_pos++;
	} else {
		open = false;
		start = false;
	}
}
}
}

int main() {

Ticker pwm;
Ticker vehicle;
Ticker gate;
Ticker snake;
pwm.attach(pwm_control, 1);						// every 1ms update state
vehicle.attach(vehicle_leave, 1);	// every 20ms update state
gate.attach(close_gate, 1);
snake.attach(snake_led, 100);

	// 2 INTERN LEDS
g_red_led[0].nastav_jas_proc(100);
g_red_led[1].nastav_jas_proc(100);

	// 0-7 EXTERN LEDS ROW
for (int i = 0; i < LED_ROW_NUM; i++) {
g_red_arr[i].nastav_jas_proc(10);
g_red_arr[i].on = true;
}
g_red_arr[LED_ROW_NUM - 1].nastav_jas_proc(100);

	// 3 RGB LEDS
for (int i = 0; i < RGB_LEDS_NUM; i++) {
g_rgb_leds[i].r.nastav_jas_proc(50);
g_rgb_leds[i].g.nastav_jas_proc(0);
g_rgb_leds[i].b.nastav_jas_proc(0);
g_rgb_leds[i].r.on = true;
g_rgb_leds[i].g.on = true;
g_rgb_leds[i].b.on = true;
}

while (1)
__WFI();
}
