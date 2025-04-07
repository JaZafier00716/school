// **************************************************************************
//
//               Demo program for APPS labs
//
// Subject:      Computer Architectures and Parallel systems
// Author:       Petr Olivka, petr.olivka@vsb.cz, 02/2025
// Organization: Department of Computer Science, FEECS,
//               VSB-Technical University of Ostrava, CZ
//
// File:         Main program for LCD module
//
// **************************************************************************

/**
 * @file    main-lcd.cpp
 * @brief   Application entry point.
 */

#include <stdio.h>
#include <functional>
#include "board.h"
#include "peripherals.h"
#include "pin_mux.h"
#include "clock_config.h"
#include "fsl_debug_console.h"

/* TODO: insert other include files here. */

#include "fsl_gpio.h"
#include "fsl_port.h"
#include "fsl_mrt.h"
#include "fsl_lpspi.h"
#include "cts_lib.h"
#include "mcxn-kit.h"
//#include "lcd_lib.h"
#include "graph_class.h"

// **************************************************************************
//! System initialization. Do not modify it!!!
void _mcu_initialization() __attribute__(( constructor( 0x100 ) ));

void _mcu_initialization() {
	BOARD_InitBootPins();
	BOARD_InitBootClocks();
	BOARD_InitBootPeripherals();
	BOARD_InitDebugConsole();
	CLOCK_EnableClock(kCLOCK_Gpio0);
	CLOCK_EnableClock(kCLOCK_Gpio1);
	CLOCK_EnableClock(kCLOCK_Gpio2);
	CLOCK_EnableClock(kCLOCK_Gpio3);
	CLOCK_EnableClock(kCLOCK_Gpio4);
}
// **************************************************************************

//! Global data

//! LEDs on MCXN-KIT - instances of class DigitalOut
DigitalOut g_led_P3_16(P3_16);
DigitalOut g_led_P3_17(P3_17);

//! Button on MCXN-KIT - instance of class DigitalIn
DigitalIn g_but_P3_18(P3_18);
DigitalIn g_but_P3_19(P3_19);
DigitalIn g_but_P3_20(P3_20);
DigitalIn g_but_P3_21(P3_21);

void DrawRectangle(Point2D p1, Point2D p2, RGB t_fg, RGB t_bg) {
	Line top( { p1.x, p2.y }, p2, t_fg, t_bg);
	Line bottom(p1, { p2.x, p1.y }, t_fg, t_bg);
	Line left( { p1.x, p2.y }, p1, t_fg, t_bg);
	Line right(p2, { p2.x, p1.y }, t_fg, t_bg);
	top.draw();
	bottom.draw();
	left.draw();
	right.draw();
}

RGB pickColor(cts_points_t l_tpoints, int l_num) {

	for (int p = 0; p < l_num; p++) {
		if (l_tpoints.m_points[p].x >= 0 && l_tpoints.m_points[p].x <= 50
				&& l_tpoints.m_points[p].y >= 0
				&& l_tpoints.m_points[p].y <= 50) {
			DrawRectangle( { 0, 0 }, { 50, 50 }, { 0xFF, 0, 0 }, { 0, 0, 0 });
			return {0xFF, 0 ,0}; // RED
		}
		if (l_tpoints.m_points[p].x >= (LCD_WIDTH - 50)
				&& l_tpoints.m_points[p].x <= LCD_WIDTH
				&& l_tpoints.m_points[p].y >= 0
				&& l_tpoints.m_points[p].y <= 50) {
			DrawRectangle(
					{ LCD_WIDTH - 51, 0 },
					{ LCD_WIDTH-1, 50 },
					{ 0, 0xFF, 0 },
					{ 0, 0, 0 }
			);
			return {0, 0xFF ,0}; // Green
		}
		if (l_tpoints.m_points[p].x
				>= 0&& l_tpoints.m_points[p].x <= 50 && l_tpoints.m_points[p].y >= (LCD_HEIGHT-50)
				&& l_tpoints.m_points[p].y <= LCD_HEIGHT) {
			DrawRectangle(
								{ 0, LCD_HEIGHT-51 },
								{ 50, LCD_HEIGHT -1 },
								{ 0, 0, 0xFF },
								{ 0, 0, 0 }
						);
			return {0, 0, 0xFF}; // Blue
		}
		if (l_tpoints.m_points[p].x
				>= (LCD_WIDTH - 50)&& l_tpoints.m_points[p].x <= LCD_WIDTH <= 50 && l_tpoints.m_points[p].y >= (LCD_HEIGHT-50)
				&& l_tpoints.m_points[p].y <= LCD_HEIGHT) {
			DrawRectangle(
											{ LCD_WIDTH - 51, LCD_HEIGHT-51 },
											{ LCD_WIDTH-1, LCD_HEIGHT-1 },
											{ 0xFF, 0xFF, 0xFF},
											{ 0, 0, 0 }
									);
			return {0xFF, 0xFF, 0xFF}; // WHITE
		}
	}
	return {0, 0, 0};
}

int main() {
	PRINTF("LCD demo program started...\n");

	lcd_init();
	if (cts_init() < 0) {
		PRINTF("Touch Screen not detected!\r\n");
	}
	Circle c( { 150, 150 }, 80, { 0, 0, 0xFF }, { 0, 0, 0 });
	c.draw();

	Line l( { 200, 200 }, { 400, 250 }, { 0, 0xFF, 0 }, { 0, 0, 0 });
	l.draw();

	Character r( { 0, 0 }, 'R', { 0xFF, 0, 0 }, { 0, 0, 0 });
	Character g( { LCD_WIDTH - 16, 0 }, 'G', { 0, 0xFF, 0 }, { 0, 0, 0 });
	Character b( { 0, LCD_HEIGHT - 26 }, 'B', { 0, 0, 0xFF }, { 0, 0, 0 });
	Character w( { LCD_WIDTH - 16, LCD_HEIGHT - 26 }, 'W', { 0xFF, 0xFF, 0xFF },
			{ 0, 0, 0 });
	r.draw();
	g.draw();
	b.draw();
	w.draw();

	DrawRectangle( { 10, 10 }, { 240, 100 }, { 0, 0xF0, 0xFF }, { 0, 0, 0 });

	cts_points_t l_tpoints;
	l_tpoints.m_points[0].size = 0;
	RGB set_color = { 0xEA, 0x87, 0x48 };

// read data from Touch Screen
	while (1) {
		int l_num = cts_get_ts_points(&l_tpoints);

		if (l_num > 0) {
			RGB new_color = pickColor(l_tpoints, l_num);
			for (int p = 0; p < l_num; p++) {
				if (new_color.r == 0 && new_color.g == 0 && new_color.b == 0) {
					Point2D center = { (l_tpoints.m_points[p].x
							+ l_tpoints.m_points[p].size / 2),
							(l_tpoints.m_points[p].y
									+ l_tpoints.m_points[p].size / 2) };
					Pixel finger(center, set_color, set_color);
					finger.draw();
				} else {
					set_color = new_color;
				}
			}
		}
	}
}

