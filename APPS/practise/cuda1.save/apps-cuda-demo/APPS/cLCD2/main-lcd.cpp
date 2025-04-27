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
#include <string>
#include "board.h"
#include "peripherals.h"
#include "pin_mux.h"
#include "clock_config.h"
#include "fsl_debug_console.h"
using namespace std;

/* TODO: insert other include files here. */

#include "fsl_gpio.h"
#include "fsl_port.h"
#include "fsl_mrt.h"
#include "fsl_lpspi.h"
#include "cts_lib.h"
#include "mcxn-kit.h"
//#include "lcd_lib.h"
#include "graph_class.h"

int32_t tab_sin[] = { 0, 17, 34, 52, 69, 87, 104, 121, 139, 156, 173, 190, 207,
		224, 241, 258, 275, 292, 309, 325, 342, 358, 374, 390, 406, 422, 438,
		453, 469, 484, 499, 515, 529, 544, 559, 573, 587, 601, 615, 629, 642,
		656, 669, 681, 694, 707, 719, 731, 743, 754, 766, 777, 788, 798, 809,
		819, 829, 838, 848, 857, 866, 874, 882, 891, 898, 906, 913, 920, 927,
		933, 939, 945, 951, 956, 961, 965, 970, 974, 978, 981, 984, 987, 990,
		992, 994, 996, 997, 998, 999, 999, 1000, 999, 999, 998, 997, 996, 994,
		992, 990, 987, 984, 981, 978, 974, 970, 965, 961, 956, 951, 945, 939,
		933, 927, 920, 913, 906, 898, 891, 882, 874, 866, 857, 848, 838, 829,
		819, 809, 798, 788, 777, 766, 754, 743, 731, 719, 707, 694, 681, 669,
		656, 642, 629, 615, 601, 587, 573, 559, 544, 529, 515, 499, 484, 469,
		453, 438, 422, 406, 390, 374, 358, 342, 325, 309, 292, 275, 258, 241,
		224, 207, 190, 173, 156, 139, 121, 104, 87, 69, 52, 34, 17, 0, -17, -34,
		-52, -69, -87, -104, -121, -139, -156, -173, -190, -207, -224, -241,
		-258, -275, -292, -309, -325, -342, -358, -374, -390, -406, -422, -438,
		-453, -469, -484, -500, -515, -529, -544, -559, -573, -587, -601, -615,
		-629, -642, -656, -669, -681, -694, -707, -719, -731, -743, -754, -766,
		-777, -788, -798, -809, -819, -829, -838, -848, -857, -866, -874, -882,
		-891, -898, -906, -913, -920, -927, -933, -939, -945, -951, -956, -961,
		-965, -970, -974, -978, -981, -984, -987, -990, -992, -994, -996, -997,
		-998, -999, -999, -1000, -999, -999, -998, -997, -996, -994, -992, -990,
		-987, -984, -981, -978, -974, -970, -965, -961, -956, -951, -945, -939,
		-933, -927, -920, -913, -906, -898, -891, -882, -874, -866, -857, -848,
		-838, -829, -819, -809, -798, -788, -777, -766, -754, -743, -731, -719,
		-707, -694, -681, -669, -656, -642, -629, -615, -601, -587, -573, -559,
		-544, -529, -515, -500, -484, -469, -453, -438, -422, -406, -390, -374,
		-358, -342, -325, -309, -292, -275, -258, -241, -224, -207, -190, -173,
		-156, -139, -121, -104, -87, -69, -52, -34, -17, 0 };

int32_t tab_cos[] = { 1000, 999, 999, 998, 997, 996, 994, 992, 990, 987, 984,
		981, 978, 974, 970, 965, 961, 956, 951, 945, 939, 933, 927, 920, 913,
		906, 898, 891, 882, 874, 866, 857, 848, 838, 829, 819, 809, 798, 788,
		777, 766, 754, 743, 731, 719, 707, 694, 681, 669, 656, 642, 629, 615,
		601, 587, 573, 559, 544, 529, 515, 500, 484, 469, 453, 438, 422, 406,
		390, 374, 358, 342, 325, 309, 292, 275, 258, 241, 224, 207, 190, 173,
		156, 139, 121, 104, 87, 69, 52, 34, 17, 0, -17, -34, -52, -69, -87,
		-104, -121, -139, -156, -173, -190, -207, -224, -241, -258, -275, -292,
		-309, -325, -342, -358, -374, -390, -406, -422, -438, -453, -469, -484,
		-499, -515, -529, -544, -559, -573, -587, -601, -615, -629, -642, -656,
		-669, -681, -694, -707, -719, -731, -743, -754, -766, -777, -788, -798,
		-809, -819, -829, -838, -848, -857, -866, -874, -882, -891, -898, -906,
		-913, -920, -927, -933, -939, -945, -951, -956, -961, -965, -970, -974,
		-978, -981, -984, -987, -990, -992, -994, -996, -997, -998, -999, -999,
		-1000, -999, -999, -998, -997, -996, -994, -992, -990, -987, -984, -981,
		-978, -974, -970, -965, -961, -956, -951, -945, -939, -933, -927, -920,
		-913, -906, -898, -891, -882, -874, -866, -857, -848, -838, -829, -819,
		-809, -798, -788, -777, -766, -754, -743, -731, -719, -707, -694, -681,
		-669, -656, -642, -629, -615, -601, -587, -573, -559, -544, -529, -515,
		-500, -484, -469, -453, -438, -422, -406, -390, -374, -358, -342, -325,
		-309, -292, -275, -258, -241, -224, -207, -190, -173, -156, -139, -121,
		-104, -87, -69, -52, -34, -17, 0, 17, 34, 52, 69, 87, 104, 121, 139,
		156, 173, 190, 207, 224, 241, 258, 275, 292, 309, 325, 342, 358, 374,
		390, 406, 422, 438, 453, 469, 484, 500, 515, 529, 544, 559, 573, 587,
		601, 615, 629, 642, 656, 669, 681, 694, 707, 719, 731, 743, 754, 766,
		777, 788, 798, 809, 819, 829, 838, 848, 857, 866, 874, 882, 891, 898,
		906, 913, 920, 927, 933, 939, 945, 951, 956, 961, 965, 970, 974, 978,
		981, 984, 987, 990, 992, 994, 996, 997, 998, 999, 999, 1000 };

Point2D g_scroll_bar = { 20, LCD_HEIGHT - 30 };
int bar_w = LCD_WIDTH - 40;
int bar_h = 10;
int circle_r = 10;
Point2D center = { g_scroll_bar.x, g_scroll_bar.y + 0.5 * bar_h };
int percent = 10;
uint32_t g_angle = 230;
bool rising = true;
int period = 20;
bool g_redraw = false;

Point2D line_end = { 0, 0 };
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

void fillRectangle(Point2D start_pos, int w, int h, RGB t_fg, RGB t_bg) {
//    Point2D start = start_pos;
//    Point2D end = {
//    		start_pos.x + w, start_pos.y
//    };
	for (int i = start_pos.y; i < start_pos.y + h; i++) {
		for (int j = start_pos.x; j < start_pos.x + w; j++) {
			Pixel p( { j, i }, t_fg, t_bg);
			p.draw();
		}
	}
}

void drawText(std::string text, Point2D pos, RGB t_fg, RGB t_bg) {
	Point2D new_pos = pos;

	for (long unsigned int i = 0; i < text.length(); i++) {
		fillRectangle(new_pos, FONT_W, FONT_H, { 0, 0, 0 }, { 0, 0, 0 });
		if (new_pos.x + FONT_W < LCD_WIDTH) {
			new_pos.x += FONT_W;
		} else {
			new_pos.x = pos.x;
			new_pos.y += FONT_H;
		}
		Character c(new_pos, text[i], t_fg, t_bg);
		c.draw();
	}
}

void drawScrollBar(RGB t_fg, RGB t_bg) {
	fillRectangle(g_scroll_bar, bar_w, bar_h, t_fg, t_bg);
}

void oscilate() {
	static int ticker = 0;

	if (ticker < period*((double)percent/100) == 0) {
		if (g_angle <= 230) {
			rising = true;
		}
		if (g_angle >= 310) {
			rising = false;
		}
		if (rising) {
			g_angle++;
		} else {
			g_angle--;
		}
		g_redraw = true;
	}

	ticker++;

	if (ticker >= period) {
		ticker = 0;
	}
}

Point2D isTouchPointInScrollBar(const cts_points_t &touchPoints) {
	int barLeft = g_scroll_bar.x;
	int barTop = g_scroll_bar.y;
	int barRight = barLeft + bar_w;
	int barBottom = barTop + bar_h;
	int barCenterY = barTop + bar_h / 2;

	for (int i = 0; i < touchPoints.m_num_points; ++i) {
		int px = touchPoints.m_points[i].x;
		int py = touchPoints.m_points[i].y;
		int size = touchPoints.m_points[i].size;

		// Define a small square around the touch point
		int pointLeft = px - size / 2;
		int pointTop = py - size / 2;
		int pointRight = px + size / 2;
		int pointBottom = py + size / 2;

		// Check if this point rectangle intersects the scroll bar rectangle
		if (!(pointRight < barLeft || pointLeft > barRight
				|| pointBottom < barTop || pointTop > barBottom)) {
			// Touch is on the scrollbar, return adjusted Point2D
			return {px, barCenterY};
		}
	}

	// No touch points intersect the scrollbar
	return {-1, -1};
}

int main() {
	lcd_init();                    // LCD initialization
	if (cts_init() < 0) {
		printf("Touch Screen not detected!\r\n");
	}

	Ticker pwm;
	pwm.attach(oscilate, 10);

	Circle c(center, circle_r, { 0, 0xFF, 0 }, { 0, 0, 0 });
	drawScrollBar( { 0xFF, 0xFF, 0xFF }, { 0, 0, 0 });
	c.draw();

	cts_points_t l_tpoints;
	l_tpoints.m_points[0].size = 0;

	int l_xc = 240; // cirle center point
	int l_yc = 130; // circle center point
	int l_r_hand = 110; // hand radius

	Line l_hand( { l_xc, l_yc - l_r_hand }, { l_xc, l_yc },
			{ 0xff, 0xff, 0xff }, { 0, 0, 0 });
	Point2D line_point = { tab_cos[g_angle] * l_r_hand* ((double)percent / 100) / 1000
			+ l_xc, tab_sin[g_angle] * l_r_hand * ((double)percent / 100) / 1000
			+ l_yc };

	Circle line_circle(line_point, circle_r, { 0, 0xFF, 0 }, { 0, 0, 0 });
	uint32_t new_center = center.x;

	l_hand.draw();
	line_circle.draw();
	bool g_redraw_line = false;

	while (1) {
		cts_points_t l_tpoints;

		// read data from Touch Screen
		int l_num = cts_get_ts_points(&l_tpoints);

		if (l_num > 0) {
			if (l_tpoints.m_points[0].y > g_scroll_bar.y) {
				// if touch position is detected on the bottom of LCD, then the detected x touch position
				// will be used for new angle position
				new_center = l_tpoints.m_points[0].x;

				percent = ((double)new_center / (LCD_WIDTH)) * 100;
				g_redraw_line = 1;
			}
		}

		if(g_redraw) {
			l_hand.hide();
			line_circle.hide();
			if(g_redraw_line) {
				c.hide();
				c.m_center.x = new_center;
				drawScrollBar( { 0xFF, 0xFF, 0xFF }, { 0, 0, 0 });
				c.draw();
				g_redraw_line = false;
			}
			l_hand.m_pos1.x = tab_cos[g_angle] * l_r_hand / 1000 + l_xc;
			l_hand.m_pos1.y = tab_sin[g_angle] * l_r_hand / 1000 + l_yc;
			line_circle.m_center.x = tab_cos[g_angle] * l_r_hand
					* ((double) percent / 100) / 1000 + l_xc;
			line_circle.m_center.y = tab_sin[g_angle] * l_r_hand
					* ((double) percent / 100) / 1000 + l_yc;
			l_hand.draw();
			line_circle.draw();
		}
	}

}
