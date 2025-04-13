#pragma once
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

#include "lcd_lib.h"
#include "./fonts/font16x26_lsb.h"
#define FONT_W 16
#define FONT_H 26

struct Point2D {
    int32_t x, y;
};

struct RGB {
    uint8_t r, g, b;
};

class GraphElement {
public:
    // foreground and background color
    RGB m_fg_color, m_bg_color;

    // constructor
    GraphElement(RGB t_fg_color, RGB t_bg_color) :
            m_fg_color(t_fg_color), m_bg_color(t_bg_color) {
    }

    // ONLY ONE INTERFACE WITH LCD HARDWARE!!!
    void drawPixel(int32_t t_x, int32_t t_y) {
        lcd_put_pixel(t_x, t_y, convert_RGB888_to_RGB565(m_fg_color));
    }

    // Draw graphics element
    virtual void draw() = 0;

    // Hide graphics element
    virtual void hide() {
        swap_fg_bg_color();
        draw();
        swap_fg_bg_color();
    }
private:
    // swap foreground and backgroud colors
    void swap_fg_bg_color() {
        RGB l_tmp = m_fg_color;
        m_fg_color = m_bg_color;
        m_bg_color = l_tmp;
    }

    // IMPLEMENT!!!
    // conversion of 24-bit RGB color into 16-bit color format
    uint16_t convert_RGB888_to_RGB565(RGB t_color) {
        uint16_t color16b;
        t_color.r = (t_color.r & 0b11111000)>> 3;	// Lose 3 bottom bits
        t_color.g = (t_color.g & 0b11111100)>> 2;	// lose 2 bottom bits
        t_color.b = (t_color.b & 0b11111000)>> 3;	// lose 3 bottom bits

        color16b = (t_color.r) << 11;
        color16b |= (t_color.g << 5);
        color16b |= t_color.b;

        // return 0x0EA0;
        return color16b;

    }
};

class Pixel: public GraphElement {
public:
    // constructor
    Pixel(Point2D t_pos, RGB t_fg_color, RGB t_bg_color) :
            GraphElement(t_fg_color, t_bg_color), m_pos(t_pos) {
    }
    // Draw method implementation
    virtual void draw() {
        drawPixel(m_pos.x, m_pos.y);
    }
    // Position of Pixel
    Point2D m_pos;
};

class Circle: public GraphElement {
private:
    void drawCircle(Point2D p) {
        drawPixel(m_center.x + p.x, m_center.y + p.y);
        drawPixel(m_center.x - p.x, m_center.y + p.y);
        drawPixel(m_center.x + p.x, m_center.y - p.y);
        drawPixel(m_center.x - p.x, m_center.y - p.y);
        drawPixel(m_center.x + p.y, m_center.y + p.x);
        drawPixel(m_center.x - p.y, m_center.y + p.x);
        drawPixel(m_center.x + p.y, m_center.y - p.x);
        drawPixel(m_center.x - p.y, m_center.y - p.x);
    }
public:
    // Center of circle
    Point2D m_center;
    // Radius of circle
    int32_t m_radius;

    Circle(Point2D t_center, int32_t t_radius, RGB t_fg, RGB t_bg) :
            GraphElement(t_fg, t_bg), m_center(t_center), m_radius(t_radius) {
    }

    void draw() {
        Point2D p;
        p.x = 0;
        p.y = m_radius;
        int d = 3 - 2 * m_radius;
        drawCircle(p);
        while (p.y >= p.x) {

            // check for decision parameter
            // and correspondingly
            // update d, y
            if (d > 0) {
                p.y--;
                d = d + 4 * (p.x - p.y) + 10;
            } else
                d = d + 4 * p.x + 6;

            // Increment x after updating decision parameter
            p.x++;

            // Draw the circle using the new coordinates
            drawCircle(p);
        }

    } // IMPLEMENT!!!

    void drawFilled() {
        Point2D p;
        p.x = 0;
        p.y = m_radius;
        int d = 3 - 2 * m_radius;
    
        auto drawHorizontalLine = [&](int y, int x1, int x2) {
            for (int x = x1; x <= x2; ++x) {
                drawPixel(x, y);
            }
        };
    
        while (p.y >= p.x) {
            // For each pair of symmetric scanlines, draw horizontal lines between the mirrored Xs
            drawHorizontalLine(m_center.y + p.y, m_center.x - p.x, m_center.x + p.x);
            drawHorizontalLine(m_center.y - p.y, m_center.x - p.x, m_center.x + p.x);
            drawHorizontalLine(m_center.y + p.x, m_center.x - p.y, m_center.x + p.y);
            drawHorizontalLine(m_center.y - p.x, m_center.x - p.y, m_center.x + p.y);
    
            if (d > 0) {
                p.y--;
                d = d + 4 * (p.x - p.y) + 10;
            } else {
                d = d + 4 * p.x + 6;
            }
    
            p.x++;
        }
    }
};

class Character: public GraphElement {
public:
    // position of character
    Point2D m_pos;
    // character
    char m_character;

    Character(Point2D t_pos, char t_char, RGB t_fg, RGB t_bg) :
            GraphElement(t_fg, t_bg), m_pos(t_pos), m_character(t_char) {
    }
    ;

    void draw() {
        for(int y=0; y < 26; y++) {
            for(int x =0; x < 16; x++) {
                if(font[(int)m_character][y] & (1 << x)) {
                    drawPixel(x + m_pos.x, y + m_pos.y);
                }
            }
        }
    }
    ;
    // IMPLEMENT!!!
};

class Line: public GraphElement {
public:
    // the first and the last point of line
    Point2D m_pos1, m_pos2;

    Line(Point2D t_pos1, Point2D t_pos2, RGB t_fg, RGB t_bg) :
            GraphElement(t_fg, t_bg), m_pos1(t_pos1), m_pos2(t_pos2) {
    }

    void draw() {
        Point2D p0 = m_pos1, p1 = m_pos2;

        bool steep = std::abs(p1.y - p0.y) > std::abs(p1.x - p0.x);

        if(steep) {
        	std::swap(p0.x, p0.y);
        	std::swap(p1.x, p1.y);
        }

        if(p0.x > p1.x) {
        	std::swap(p0.x, p1.x);
        	std::swap(p0.y, p1.y);
        }

        Point2D distance;
        distance.x = p1.x - p0.x;
        distance.y = std::abs(p1.y - p0.y);

        int error = distance.x / 2;
        int ystep = (p0.y < p1.y) ? 1 : -1;
        int y = p0.y;

        for (int x = p0.x; x <= p1.x; ++x) {
        	if(steep) {
        		drawPixel(y, x);
        	} else {
        		drawPixel(x, y);
        	}

        	error -= distance.y;
        	if(error < 0) {
        		y += ystep;
        		error += distance.x;
        	}

		}


    }
    ;
};
