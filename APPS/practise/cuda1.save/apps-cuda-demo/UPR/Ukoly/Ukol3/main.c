#include "drawing.h"
#include <time.h>
#include <stdlib.h>
#include <string.h>

typedef struct
{
    char color[10];
    int percent;
} TColor;

void dashed_line(int length);
void stairs(int size);
void flower(int width, int height);
void meadow(int rows, int cols);
void fire(int width, int stage);
void draw_line(int width, TColor colors[], int length);

int main()
{
    srand((unsigned int)time(NULL));
    // Keep this line here
    clear_screen();

    // Load the input - what should be drawn.
    int drawing = 0;

    scanf("%d", &drawing);

    clear_screen();
    switch (drawing)
    {
    case 0:
        move_to(1, 1); // Moving to the first location
        set_blue_color();
        dashed_line(4);

        move_to(5, 3);
        set_green_color();
        dashed_line(8);

        move_to(7, 2);
        set_yellow_color();
        dashed_line(2);

        move_to(12, 8);
        set_white_color();
        dashed_line(5);

        move_to(16, 4);
        set_black_color();
        dashed_line(2);

        move_to(20, 7);
        set_red_color();
        dashed_line(9);

        move_to(10, 30);
        set_blue_color();
        dashed_line(4);

        move_to(12, 50);
        set_green_color();
        dashed_line(8);

        move_to(1, 65);
        set_yellow_color();
        dashed_line(2);

        move_to(2, 86);
        set_white_color();
        dashed_line(5);

        move_to(7, 25);
        set_black_color();
        dashed_line(2);

        move_to(9, 68);
        set_red_color();
        dashed_line(9);

        move_to(19, 55);
        set_red_color();
        dashed_line(7);
        break;

    case 1:

        move_to(1, 1);
        set_blue_color();
        stairs(8);

        move_to(1, 6);
        set_red_color();
        stairs(6);

        move_to(10, 1);
        set_white_color();
        stairs(10);

        move_to(8, 20);
        set_yellow_color();
        stairs(12);

        move_to(12, 50);
        set_black_color();
        stairs(8);

        move_to(6, 35);
        set_green_color();
        stairs(6);

        move_to(1, 74);
        set_green_color();
        stairs(6);

        move_to(1, 45);
        set_white_color();
        stairs(10);

        move_to(8, 58);
        set_yellow_color();
        stairs(12);
        break;
    case 2:
        move_to(5, 5);
        set_red_color();
        flower(5, 9);

        move_to(15, 27);
        set_red_color();
        flower(5, 2);

        move_to(5, 55);
        set_black_color();
        flower(9, 12);

        move_to(5, 12);
        set_yellow_color();
        flower(5, 6);

        move_to(10, 15);
        set_white_color();
        flower(5, 8);

        move_to(8, 40);
        set_blue_color();
        flower(5, 8);

        move_to(6, 20);
        set_blue_color();
        flower(7, 10);

        move_to(8, 30);
        set_yellow_color();
        flower(5, 2);
        break;
    case 3:
        move_to(1, 1);
        meadow(2, 8);
        break;
    case 4:
        for (int i = 1; i < 1000; i++)
        {
            animate_ms(200);
            fire(100, i);
        }

    default:
        break;
    }

    // Keep this line here
    end_drawing();

    return 0;
}

void dashed_line(int length)
{
    for (int i = 0; i < length; i++)
    {
        draw_pixel();
        move_right();
        move_right();
    }
}

void stairs(int size)
{
    for (int i = 0; i < size; i++)
    {
        draw_pixel();
        move_right(); 
        draw_pixel();
        move_down();
    }
}

void flower(int width, int height)
{
    int tmp;

    if (height <= 2)
    {
        for (int i = 0; i < width; i++) // First row
        {
            draw_pixel();
            move_right();
        }
        tmp = 1;
        move_left();
    }
    else
    {
        move_right();
        for (int i = 0; i < width - 2; i++) // First row
        {
            draw_pixel();
            move_right();
        }
        move_down();
        for (int i = 0; i < height / 2 - 2; i++) // Middle section
        {
            for (int j = 0; j < width; j++)
            {
                draw_pixel();
                if (j != width - 1)
                {
                    if (i % 2 == 0)
                    {
                        tmp = 1;
                        move_left();
                    }
                    else
                    {
                        tmp = 0;
                        move_right();
                    }
                }
            }
            move_down();
        }

        if (tmp == 1)
        {
            tmp = 0;
        }
        else
        {
            tmp = 1;
        }

        for (int i = 0; i < width - 2; i++) // Last row
        {
            if (tmp == 1)
            {
                move_left();
            }
            else
            {
                move_right();
            }
            draw_pixel();
        }
        if (tmp == 1)
        {
            move_left();
            tmp = 0;
        }
        else
        {
            move_right();
            tmp = 1;
        }
    }
    move_down();
    set_green_color();
    for (int i = 0; i < width / 2; i++) // getting to middle
    {
        if (tmp == 1)
        {
            move_left();
        }
        else
        {
            move_right();
        }
    }

    if (tmp == 1)
    {
        tmp = 0;
    }
    else
    {
        tmp = 1;
    }

    for (int i = 0; i < height - height / 2; i++) // Stem
    {

        draw_pixel();
        move_down();
    }
}

void meadow(int rows, int cols)
{
    int flower_height = 6;
    int flower_width = 5;
    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < cols; j++)
        {
            move_to(i * flower_height + i + 1, j * flower_width + j + 1);
            set_white_color();
            flower(flower_width, flower_height);
        }
    }
}

void fire(int width, int stage)
{
    TColor colors[4];
    strcpy(colors[0].color, "white");
    strcpy(colors[1].color, "yellow");
    strcpy(colors[2].color, "red");
    strcpy(colors[3].color, "blank");

    for (int i = 20, j = 1; i > 0 && j < stage * 2; i--, j++)
    {
        move_to(i, 1);

        for (int i = 0; i < 4; i++) // reset percentages
        {
            colors[i].percent = 0;
        }

        switch (i)
        {
        case 20:
            colors[0].percent = 100;
            break;
        case 19:
            colors[0].percent = 75;
            colors[1].percent = 25;
            break;
        case 18:
            colors[0].percent = 60;
            colors[1].percent = 40;
            break;
        case 17:
            colors[0].percent = 20;
            colors[1].percent = 50;
            colors[2].percent = 30;
            break;
        case 16:
            colors[0].percent = 20;
            colors[1].percent = 40;
            colors[2].percent = 40;
            break;
        case 15:
            colors[0].percent = 10;
            colors[1].percent = 40;
            colors[2].percent = 50;
            break;
        case 14:
            colors[0].percent = 5;
            colors[1].percent = 35;
            colors[2].percent = 60;
            break;
        case 13:
            colors[0].percent = 2;
            colors[1].percent = 18;
            colors[2].percent = 70;
            colors[3].percent = 10;
            break;
        case 12:
            colors[0].percent = 1;
            colors[1].percent = 9;
            colors[2].percent = 70;
            colors[3].percent = 20;
            break;
        case 11:
            colors[1].percent = 5;
            colors[2].percent = 55;
            colors[3].percent = 40;
            break;
        case 10:
            colors[1].percent = 2;
            colors[2].percent = 58;
            colors[3].percent = 40;
            break;
        case 9:
            colors[2].percent = 30;
            colors[3].percent = 70;
            break;
        case 8:
            colors[2].percent = 25;
            colors[3].percent = 75;
            break;
        case 7:
            colors[2].percent = 20;
            colors[3].percent = 80;
            break;
        case 6:
            colors[2].percent = 15;
            colors[3].percent = 85;
            break;
        case 5:
            colors[2].percent = 10;
            colors[3].percent = 90;
            break;
        case 4:
            colors[2].percent = 7;
            colors[3].percent = 93;
            break;
        case 3:
            colors[2].percent = 4;
            colors[3].percent = 96;
            break;
        case 2:
            colors[2].percent = 1;
            colors[3].percent = 99;
            break;
        case 1:
            colors[3].percent = 100;
            break;
        default:
            break;
        }
        draw_line(width, colors, 4);
    }
}

void draw_line(int width, TColor colors[], int length)
{
    int choose_color;

    for (int i = 0; i < width; i++)
    {
        int total_percent = 0;
        choose_color = rand() % 100;
        for (int j = 0; j < length; j++)
        {
            total_percent += colors[j].percent;
            if (choose_color < total_percent)
            {
                switch (colors[j].color[0])
                {
                case 'w': // White color
                    set_white_color();
                    break;
                case 'y': // Yellow color
                    set_yellow_color();
                    break;
                case 'r': // Red color
                    set_red_color();
                    break;
                case 'b': // blank -> reset color
                    reset_color();
                    break;
                default:
                    reset_color();
                    break;
                }
                break;
            }
        }

        draw_pixel();
        move_right();
    }
}