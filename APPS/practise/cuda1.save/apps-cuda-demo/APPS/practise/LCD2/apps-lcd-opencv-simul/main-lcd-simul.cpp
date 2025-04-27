// **************************************************************************
//
//               Demo program for labs
//
// Subject:      Computer Architectures and Parallel systems
// Author:       Petr Olivka, petr.olivka@vsb.cz, 09/2019
// Organization: Department of Computer Science, FEECS,
//               VSB-Technical University of Ostrava, CZ
//
// File:         OpenCV simulator of LCD
//
// **************************************************************************

#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <unistd.h>
#include "cts_lib.h"
#include "lcd_lib.h"
#include "font8x8.h"
#include "graph_class.hpp"


void drawText(std::string text, Point2D pos, RGB t_fg, RGB t_bg) {
    Point2D new_pos = pos;

    for (long unsigned int i = 0; i < text.length(); i++)
    {
        if(new_pos.x + FONT_W < LCD_WIDTH) {
            new_pos.x += FONT_W;
        } else {
            new_pos.x = pos.x;
            new_pos.y += FONT_H;
        }
        Character c(new_pos ,text[i], t_fg, t_bg);
        c.draw();
    }
} 

void fillRectangle(Point2D pos, int w, int h, RGB t_fg, RGB t_bg) {
    Point2D start_pos = pos;
    Point2D end_pos = {
        pos.x + w, pos.y
    };
    for (int i = 0; i < h; i++)
    {
        Line l(start_pos, end_pos, t_fg, t_bg);
        l.draw();
        end_pos.y+=1;
        start_pos.y+=1;
    }
}

void drawScrollBar(Point2D pos, int w, int h, RGB t_fg, RGB t_bg, RGB c_fg,int circle_percent, int circle_r) {
    fillRectangle(pos, w, h, t_fg, t_bg);



    Circle c({pos.x + w*(circle_percent/100), pos.y + 0.5 * h}, circle_r, c_fg, t_bg);
    c.drawFilled();
}

int main()
{
    lcd_init();                     // LCD initialization

    drawScrollBar({20, LCD_HEIGHT-30}, LCD_WIDTH-40, 10, {0xFF, 0xFF, 0xFF}, {0, 0, 0}, {0, 0xFF, 0}, 0, 10);
    drawText("Ahoj :D", {5,5}, {0xFF, 0xFF, 0xFF}, {0,0,0});

    cv::imshow( LCD_NAME, g_canvas );   // refresh content of "LCD"
    cv::waitKey( 0 );                   // wait for key 
}


