#include "drawing.h"

void tower(int width, int height);
void windows(int width, int height);
void resetLeft(int width);
void resetRight(int width);
void blackLine(int width);
void plane();
void lineLeft(int width);
void lineRight(int width);
void explosion(int height, int width, int stage);
void tower_destruction(int width, int height, int stage);

int main()
{
    int building_width = 35;
    int building_space = 10;
    int stage = 0;
    int currentpos;
    clear_screen();
    set_blue_color();
    for (int i = 0; i < canvas_height; i++)
    {
        move_to(i, 1);
        for (int j = 0; j < 175; j++)
        {
            draw_pixel();
            move_right();
        }
    }

    move_to(building_space / 2, building_space / 2 + 1);
    tower(building_width, canvas_height - 5);

    move_to(building_space / 2, building_space + 1 + building_width);
    // tower_destruction(building_width, canvas_height - 5, 23);
    tower(building_width, canvas_height - 5);

    for (int i = 0; i < 175; i++)
    {
        currentpos = 176 - 10 - i;
        animate_ms(200);
        if (i < 87)
        {
            move_to(10, currentpos);
            plane();
        }
        if (i > 52) // if the current position of plane is smaller or equal to ending position of second building, the explostion should start
        {
            // animate_ms(200);
            stage++;
            move_to(building_space / 2, building_space + 1 + building_width);
            tower_destruction(building_width, canvas_height - 5, stage);
        }
    }

    // move_to(10, 176 - 50);
    // plane();

    // Keep this line here
    end_drawing();

    return 0;
}

void lineRight(int width)
{
    for (int i = 0; i < width; i++)
    {
        draw_pixel();
        move_right();
    }
}

void lineLeft(int width)
{
    for (int i = 0; i < width; i++)
    {
        draw_pixel();
        move_left();
    }
}

void tower(int width, int height)
{
    blackLine(width);
    resetLeft(width);
    move_down();

    windows(width, height - 1);
}

void tower_destruction(int width, int height, int stage)
{
    blackLine(width);
    resetLeft(width);
    move_down();

    switch (stage)
    {
    case 1:
        windows(width, 9);
        for (int i = 0; i < 3; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            set_white_color();
            if (i % 2 == 1)
            {
                lineRight(width - 4);

                set_black_color();
                lineRight(3);
                move_left();
            }
            else
            {
                draw_pixel();
                move_right();
                for (int j = 0; j < width - 3; j++) // Windows
                {
                    if (j >= width - 4)
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 0)
                    {
                        set_cyan_color();
                    }
                    else
                    {
                        set_white_color();
                    }
                    draw_pixel();
                    move_right();
                }
            }
            set_black_color();
            draw_pixel();
            resetLeft(width - 1);

            move_down();
        }

        break;
    case 2:
        windows(width, 9);
        for (int i = 0; i < 4; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            set_white_color();
            if (i % 2 == 1)
            {
                if (i < 3)
                {

                    lineRight(width - 4);
                    set_black_color();
                    lineRight(3);
                }
                else
                {
                    lineRight(width - 3);
                    set_black_color();
                    lineRight(2);
                }

                move_left();
            }
            else
            {
                draw_pixel();
                move_right();
                for (int j = 0; j < width - 3; j++) // Windows
                {
                    if (j >= width - 5 && i > 0)
                    {
                        set_black_color();
                    }
                    else if (j >= width - 4)
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 0)
                    {
                        set_cyan_color();
                    }
                    else
                    {
                        set_white_color();
                    }
                    draw_pixel();
                    move_right();
                }
            }
            set_black_color();
            draw_pixel();
            resetLeft(width - 1);

            move_down();
        }
        set_black_color();
        draw_pixel();
        move_right();
        for (int i = 0; i < width - 2; i++)
        {
            if (i % 2 == 0)
            {
                set_white_color();
            }
            else
            {
                set_cyan_color();
            }
            draw_pixel();
            move_right();
        }
        set_black_color();
        draw_pixel();
        move_right();
        move_down();
        resetLeft(width);
        break;
    case 3:
        windows(width, 8);
        for (int i = 0; i < 6; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            set_white_color();
            if (i % 2 == 0)
            {
                if (i > 0)
                {
                    if (i < 4)
                    {
                        lineRight(width - 5);
                        set_black_color();
                        lineRight(4);
                    }
                    else
                    {
                        lineRight(width - 4);
                        set_black_color();
                        lineRight(3);
                    }
                }
                else
                {
                    lineRight(width - 3);
                    set_black_color();
                    lineRight(2);
                }

                move_left();
            }
            else
            {
                draw_pixel();
                move_right();
                for (int j = 0; j < width - 3; j++) // Windows
                {
                    if (j >= width - 6 && i > 1 && i < 4)
                    {
                        set_black_color();
                    }
                    else if (j >= width - 5 && i < 4)
                    {
                        set_black_color();
                    }
                    else if (j >= width - 4)
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 0)
                    {
                        set_cyan_color();
                    }
                    else
                    {
                        set_white_color();
                    }
                    draw_pixel();
                    move_right();
                }
            }
            set_black_color();
            draw_pixel();
            resetLeft(width - 1);

            move_down();
        }
        break;
    case 4:
        windows(width, 7);
        for (int i = 0; i < 7; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            set_white_color();
            if (i % 2 == 1)
            {
                if (i == 3)
                {
                    lineRight(width - 7);
                    set_black_color();
                    lineRight(6);
                    move_left();
                }
                else
                {
                    lineRight(width - 4);
                    set_black_color();
                    lineRight(3);
                    move_left();
                }
            }
            else
            {
                draw_pixel();
                move_right();
                for (int j = 0; j < width - 3; j++) // Windows
                {
                    if ((i == 0 || i > 4) && j >= width - 4)
                    {
                        set_black_color();
                    }
                    else if (i > 0 && j >= width - 6 && i < 6)
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 1)
                    {
                        set_white_color();
                    }
                    else
                    {
                        set_cyan_color();
                    }
                    draw_pixel();
                    move_right();
                }
            }
            set_black_color();
            draw_pixel();
            resetLeft(width - 1);

            move_down();
        }
        break;
    case 5:
        windows(width, 7);
        for (int i = 0; i < 7; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            set_white_color();
            if (i % 2 == 1)
            {
                if (i == 3)
                {
                    lineRight(width - 8);
                    set_black_color();
                    lineRight(7);
                    move_left();
                }
                else
                {
                    lineRight(width - 5);
                    set_black_color();
                    lineRight(4);
                    move_left();
                }
            }
            else
            {
                draw_pixel();
                move_right();
                for (int j = 0; j < width - 3; j++) // Windows
                {
                    if (i == 4 && j >= width - 8)
                    {
                        set_black_color();
                    }
                    else if ((i == 0 || i > 4) && j >= width - 5)
                    {
                        set_black_color();
                    }
                    else if (i > 0 && j >= width - 7 && i < 6)
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 1)
                    {
                        set_white_color();
                    }
                    else
                    {
                        set_cyan_color();
                    }
                    draw_pixel();
                    move_right();
                }
            }
            set_black_color();
            draw_pixel();
            resetLeft(width - 1);

            move_down();
        }
        break;
    case 6:
        windows(width, 7);
        for (int i = 0; i < 7; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            set_white_color();
            if (i % 2 == 1)
            {
                if (i == 3)
                {
                    lineRight(width - 9);
                    set_black_color();
                    lineRight(8);
                    move_left();
                }
                else
                {
                    lineRight(width - 6);
                    set_black_color();
                    lineRight(5);
                    move_left();
                }
            }
            else
            {
                draw_pixel();
                move_right();
                for (int j = 0; j < width - 3; j++) // Windows
                {
                    if (i == 4 && j >= width - 9)
                    {
                        set_black_color();
                    }
                    else if ((i == 0 || i > 4) && j >= width - 6)
                    {
                        set_black_color();
                    }
                    else if (i > 0 && j >= width - 8 && i < 6)
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 1)
                    {
                        set_white_color();
                    }
                    else
                    {
                        set_cyan_color();
                    }
                    draw_pixel();
                    move_right();
                }
            }
            set_black_color();
            draw_pixel();
            resetLeft(width - 1);

            move_down();
        }
        break;
    case 7:
        windows(width, 7);
        for (int i = 0; i < 7; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            set_white_color();
            if (i % 2 == 1)
            {
                if (i == 3)
                {
                    lineRight(width - 10);
                    set_black_color();
                    lineRight(9);
                    move_left();
                }
                else
                {
                    lineRight(width - 7);
                    set_black_color();
                    lineRight(6);
                    move_left();
                }
            }
            else
            {
                draw_pixel();
                move_right();
                for (int j = 0; j < width - 3; j++) // Windows
                {
                    if (i == 4 && j >= width - 10)
                    {
                        set_black_color();
                    }
                    else if ((i == 0 || i > 4) && j >= width - 7)
                    {
                        set_black_color();
                    }
                    else if (i > 0 && j >= width - 9 && i < 6)
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 1)
                    {
                        set_white_color();
                    }
                    else
                    {
                        set_cyan_color();
                    }
                    draw_pixel();
                    move_right();
                }
            }
            set_black_color();
            draw_pixel();
            resetLeft(width - 1);

            move_down();
        }
        break;
    case 8:
        windows(width, 6);
        for (int i = 0; i < 9; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            set_white_color();
            if (i % 2 == 0)
            {
                if (i > 0 && i < 8 && i != 4)
                {
                    lineRight(width - 2 - 7);
                    set_black_color();
                    lineRight(7);
                }
                else if (i == 4)
                {
                    lineRight(width - 2 - 9);
                    set_black_color();
                    lineRight(9);
                }
                else
                {
                    set_white_color();
                    lineRight(width - 2 - 1);
                    set_black_color();
                    lineRight(1);
                }
            }
            else
            {
                for (int j = 0; j < width - 2; j++)
                {
                    if (j >= width - 2 - 8 && i > 1 && i < 7)
                    {
                        set_black_color();
                    }
                    else if (j >= width - 2 - 4 && (i == 1 || i == 7))
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 1)
                    {
                        set_cyan_color();
                    }
                    else
                    {
                        set_white_color();
                    }
                    draw_pixel();
                    move_right();
                }
            }

            set_black_color();
            draw_pixel();
            move_right();
            resetLeft(width);
            move_down();
        }
        break;
    case 9:
        windows(width, 6);
        for (int i = 0; i < 9; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            set_white_color();
            if (i % 2 == 0)
            {
                if (i > 0 && i < 8 && i != 4)
                {
                    lineRight(width - 2 - 8);
                    set_black_color();
                    lineRight(8);
                }
                else if (i == 4)
                {
                    lineRight(width - 2 - 10);
                    set_black_color();
                    lineRight(10);
                }
                else
                {
                    set_white_color();
                    lineRight(width - 2 - 2);
                    set_black_color();
                    lineRight(2);
                }
            }
            else
            {
                for (int j = 0; j < width - 2; j++)
                {
                    if (j >= width - 2 - 9 && i > 1 && i < 7)
                    {
                        set_black_color();
                    }
                    else if (j >= width - 2 - 5 && (i == 1 || i == 7))
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 1)
                    {
                        set_cyan_color();
                    }
                    else
                    {
                        set_white_color();
                    }
                    draw_pixel();
                    move_right();
                }
            }

            set_black_color();
            draw_pixel();
            move_right();
            resetLeft(width);
            move_down();
        }
        break;
    case 10:
        windows(width, 6);
        for (int i = 0; i < 9; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            set_white_color();
            if (i % 2 == 0)
            {
                if (i > 0 && i < 8 && i != 4)
                {
                    lineRight(width - 2 - 8);
                    set_black_color();
                    lineRight(8);
                }
                else if (i == 4)
                {
                    lineRight(width - 2 - 11);
                    set_black_color();
                    lineRight(11);
                }
                else
                {
                    set_white_color();
                    lineRight(width - 2 - 2);
                    set_black_color();
                    lineRight(2);
                }
            }
            else
            {
                for (int j = 0; j < width - 2; j++)
                {
                    if (j >= width - 2 - 10 && i > 1 && i < 7)
                    {
                        set_black_color();
                    }
                    else if (j >= width - 2 - 5 && (i == 1 || i == 7))
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 1)
                    {
                        set_cyan_color();
                    }
                    else
                    {
                        set_white_color();
                    }
                    draw_pixel();
                    move_right();
                }
            }

            set_black_color();
            draw_pixel();
            move_right();
            resetLeft(width);
            move_down();
        }
        break;
    case 11:
        windows(width, 6);
        for (int i = 0; i < 9; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            set_white_color();
            if (i % 2 == 0)
            {
                if (i > 0 && i < 8 && i != 4)
                {
                    if (i > 3)
                    {
                        lineRight(width - 2 - 10);
                        set_black_color();
                        lineRight(10);
                    }
                    else
                    {
                        lineRight(width - 2 - 9);
                        set_black_color();
                        lineRight(9);
                    }
                }
                else if (i == 4)
                {
                    lineRight(width - 2 - 12);
                    set_black_color();
                    lineRight(12);
                }
                else
                {
                    set_white_color();
                    lineRight(width - 2 - 2);
                    set_black_color();
                    lineRight(2);
                }
            }
            else
            {
                for (int j = 0; j < width - 2; j++)
                {
                    if (j >= width - 2 - 7 && i == 7)
                    {
                        set_black_color();
                    }
                    else if (j >= width - 2 - 11 && i > 1 && i < 7)
                    {
                        set_black_color();
                    }
                    else if (j >= width - 2 - 5 && (i == 1 || i == 7))
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 1)
                    {
                        set_cyan_color();
                    }
                    else
                    {
                        set_white_color();
                    }
                    draw_pixel();
                    move_right();
                }
            }

            set_black_color();
            draw_pixel();
            move_right();
            resetLeft(width);
            move_down();
        }
        break;
    case 12:
        windows(width, 6);
        for (int i = 0; i < 9; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            set_white_color();
            if (i % 2 == 0)
            {
                if (i > 0 && i < 8 && i != 4)
                {
                    if (i > 3)
                    {
                        lineRight(width - 2 - 11);
                        set_black_color();
                        lineRight(11);
                    }
                    else
                    {
                        lineRight(width - 2 - 10);
                        set_black_color();
                        lineRight(10);
                    }
                }
                else if (i == 4)
                {
                    lineRight(width - 2 - 13);
                    set_black_color();
                    lineRight(13);
                }
                else
                {
                    set_white_color();
                    lineRight(width - 2 - 3);
                    set_black_color();
                    lineRight(3);
                }
            }
            else
            {
                for (int j = 0; j < width - 2; j++)
                {
                    if (j >= width - 2 - 8 && i == 7)
                    {
                        set_black_color();
                    }
                    else if (j >= width - 2 - 12 && i > 1 && i < 7)
                    {
                        set_black_color();
                    }
                    else if (j >= width - 2 - 6 && (i == 1 || i == 7))
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 1)
                    {
                        set_cyan_color();
                    }
                    else
                    {
                        set_white_color();
                    }
                    draw_pixel();
                    move_right();
                }
            }

            set_black_color();
            draw_pixel();
            move_right();
            resetLeft(width);
            move_down();
        }
        break;
    case 13:
        windows(width, 6);
        for (int i = 0; i < 9; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            set_white_color();
            if (i % 2 == 0)
            {
                if (i > 0 && i < 8 && i != 4)
                {
                    if (i > 3)
                    {
                        lineRight(width - 2 - 12);
                        set_black_color();
                        lineRight(12);
                    }
                    else
                    {
                        lineRight(width - 2 - 11);
                        set_black_color();
                        lineRight(11);
                    }
                }
                else if (i == 4)
                {
                    lineRight(width - 2 - 14);
                    set_black_color();
                    lineRight(14);
                }
                else
                {
                    set_white_color();
                    lineRight(width - 2 - 3);
                    set_black_color();
                    lineRight(3);
                }
            }
            else
            {
                for (int j = 0; j < width - 2; j++)
                {
                    if (j >= width - 2 - 9 && i == 7)
                    {
                        set_black_color();
                    }
                    else if (j >= width - 2 - 13 && i > 1 && i < 7)
                    {
                        set_black_color();
                    }
                    else if (j >= width - 2 - 7 && (i == 1 || i == 7))
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 1)
                    {
                        set_cyan_color();
                    }
                    else
                    {
                        set_white_color();
                    }
                    draw_pixel();
                    move_right();
                }
            }

            set_black_color();
            draw_pixel();
            move_right();
            resetLeft(width);
            move_down();
        }
        break;
    case 29:
        windows(width, 4);
        for (int i = 0; i < 11; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            if (i % 2 == 0)
            {
                set_white_color();
                if (i == 0)
                {
                    lineRight(width - 2 - 1);
                    set_black_color();
                    lineRight(1);
                }
                else if (i == 2)
                {
                    lineRight(width - 2 - 9);
                    set_black_color();
                    lineRight(9);
                }
                else if (i == 6)
                {
                    lineRight(width - 2 - 16);
                    set_black_color();
                    lineRight(16);
                }
                else if (i == 8)
                {
                    lineRight(width - 2 - 13);
                    set_black_color();
                    lineRight(13);
                }
                else if (i == 10)
                {
                    lineRight(width - 2 - 5);
                    set_black_color();
                    lineRight(5);
                }
                else
                {
                    lineRight(width - 2 - 12);
                    set_black_color();
                    lineRight(12);
                }
            }
            else
            {
                for (int j = 0; j < width - 2; j++)
                {
                    if (i == 1 && j >= width - 2 - 6)
                    {
                        set_black_color();
                    }
                    else if (i == 3 && j >= width - 2 - 11)
                    {
                        set_black_color();
                    }
                    else if ((i == 5 || i == 7) && j >= width - 2 - 15)
                    {
                        set_black_color();
                    }
                    else if (i == 9 && j >= width - 2 - 11)
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 0)
                    {
                        set_white_color();
                    }
                    else
                    {
                        set_cyan_color();
                    }

                    draw_pixel();
                    move_right();
                }
            }

            set_black_color();
            draw_pixel();
            move_right();
            resetLeft(width);
            move_down();
        }
        break;
    case 30:
        windows(width, 4);
        for (int i = 0; i < 11; i++)
        {
            set_black_color();
            draw_pixel();
            move_right();

            if (i % 2 == 0)
            {
                set_white_color();
                if (i == 0)
                {
                    lineRight(width - 2 - 3);
                    set_black_color();
                    lineRight(3);
                }
                else if (i == 2)
                {
                    lineRight(width - 2 - 11);
                    set_black_color();
                    lineRight(11);
                }
                else if (i == 6)
                {
                    lineRight(width - 2 - 16);
                    set_black_color();
                    lineRight(16);
                }
                else if (i == 8)
                {
                    lineRight(width - 2 - 13);
                    set_black_color();
                    lineRight(13);
                }
                else if (i == 10)
                {
                    lineRight(width - 2 - 5);
                    set_black_color();
                    lineRight(5);
                }
                else
                {
                    lineRight(width - 2 - 14);
                    set_black_color();
                    lineRight(14);
                }
            }
            else
            {
                for (int j = 0; j < width - 2; j++)
                {
                    if (i == 1 && j >= width - 2 - 8)
                    {
                        set_black_color();
                    }
                    else if (i == 3 && j >= width - 2 - 12)
                    {
                        set_black_color();
                    }
                    else if ((i == 5 || i == 7) && j >= width - 2 - 15)
                    {
                        set_black_color();
                    }
                    else if (i == 9 && j >= width - 2 - 11)
                    {
                        set_black_color();
                    }
                    else if (j % 2 == 0)
                    {
                        set_white_color();
                    }
                    else
                    {
                        set_cyan_color();
                    }

                    draw_pixel();
                    move_right();
                }
            }

            set_black_color();
            draw_pixel();
            move_right();
            resetLeft(width);
            move_down();
        }
        break;
    default:
        if (stage < 29)
        {

            windows(width, 6);
            for (int i = 0; i < 9; i++)
            {
                set_black_color();
                draw_pixel();
                move_right();

                set_white_color();
                if (i % 2 == 0)
                {
                    if (i > 0 && i < 8 && i != 4)
                    {
                        if (i > 3)
                        {
                            lineRight(width - 2 - 13);
                            set_black_color();
                            lineRight(13);
                        }
                        else
                        {
                            lineRight(width - 2 - 12);
                            set_black_color();
                            lineRight(12);
                        }
                    }
                    else if (i == 4)
                    {
                        lineRight(width - 2 - 15);
                        set_black_color();
                        lineRight(15);
                    }
                    else
                    {
                        set_white_color();
                        lineRight(width - 2 - 3);
                        set_black_color();
                        lineRight(3);
                    }
                }
                else
                {
                    for (int j = 0; j < width - 2; j++)
                    {
                        if (j >= width - 2 - 10 && i == 7)
                        {
                            set_black_color();
                        }
                        else if (j >= width - 2 - 14 && i > 1 && i < 7)
                        {
                            set_black_color();
                        }
                        else if (j >= width - 2 - 8 && (i == 1 || i == 7))
                        {
                            set_black_color();
                        }
                        else if (j % 2 == 1)
                        {
                            set_cyan_color();
                        }
                        else
                        {
                            set_white_color();
                        }
                        draw_pixel();
                        move_right();
                    }
                }

                set_black_color();
                draw_pixel();
                move_right();
                resetLeft(width);
                move_down();
            }
        }
        else
        {
            windows(width, 4);
            for (int i = 0; i < 11; i++)
            {
                set_black_color();
                draw_pixel();
                move_right();

                if (i % 2 == 0)
                {
                    set_white_color();
                    if (i == 0)
                    {
                        lineRight(width - 2 - 3);
                        set_black_color();
                        lineRight(3);
                    }
                    else if (i == 2)
                    {
                        lineRight(width - 2 - 11);
                        set_black_color();
                        lineRight(11);
                    }
                    else if (i == 6)
                    {
                        lineRight(width - 2 - 16);
                        set_black_color();
                        lineRight(16);
                    }
                    else if (i == 8)
                    {
                        lineRight(width - 2 - 13);
                        set_black_color();
                        lineRight(13);
                    }
                    else if (i == 10)
                    {
                        lineRight(width - 2 - 5);
                        set_black_color();
                        lineRight(5);
                    }
                    else
                    {
                        lineRight(width - 2 - 14);
                        set_black_color();
                        lineRight(14);
                    }
                }
                else
                {
                    for (int j = 0; j < width - 2; j++)
                    {
                        if (i == 1 && j >= width - 2 - 8)
                        {
                            set_black_color();
                        }
                        else if (i == 3 && j >= width - 2 - 12)
                        {
                            set_black_color();
                        }
                        else if ((i == 5 || i == 7) && j >= width - 2 - 15)
                        {
                            set_black_color();
                        }
                        else if (i == 9 && j >= width - 2 - 11)
                        {
                            set_black_color();
                        }
                        else if (j % 2 == 0)
                        {
                            set_white_color();
                        }
                        else
                        {
                            set_cyan_color();
                        }

                        draw_pixel();
                        move_right();
                    }
                }

                set_black_color();
                draw_pixel();
                move_right();
                resetLeft(width);
                move_down();
            }
        }
        break;
    }
}

void resetLeft(int width)
{
    for (int j = 0; j < width; j++)
    {
        move_left();
    }
}

void resetRight(int width)
{
    for (int j = 0; j < width; j++)
    {
        move_right();
    }
}

void windows(int width, int height)
{
    for (int i = 0; i < height; i++)
    {
        set_black_color(); // Building edge
        draw_pixel();
        move_right();

        set_white_color();
        if (i % 2 == 0)
        {
            lineRight(width - 2);
        }
        else
        {
            draw_pixel();
            move_right();
            for (int j = 0; j < ((width / 2) - 1); j++) // Windows
            {
                set_cyan_color();
                draw_pixel();
                move_right();

                set_white_color();
                draw_pixel();
                move_right();
            }
        }

        set_black_color(); // Building edge
        draw_pixel();
        move_right();

        resetLeft(width);
        move_down();
    }
}

void blackLine(int width)
{
    set_black_color();
    lineRight(width);
}

void plane()
{
    /* --- Line 1 --- */
    set_blue_color();
    draw_pixel();

    move_left();

    set_black_color();
    lineLeft(3);

    /* --- Reset 1 --- */
    resetRight(4);
    move_down();

    /* --- Line 2 --- */
    set_blue_color();
    draw_pixel();

    move_left();

    set_black_color();
    draw_pixel();

    move_left();

    set_white_color();
    lineLeft(2);

    set_black_color();
    draw_pixel();

    /* --- Reset 2 --- */
    resetRight(4);
    move_down();

    /* --- Line 3-4 --- */
    for (int i = 0; i < 2; i++)
    {
        set_blue_color();
        draw_pixel();

        move_left();

        set_black_color();
        draw_pixel();

        move_left();

        set_white_color();
        lineLeft(3);

        set_black_color();
        draw_pixel();

        /* --- Reset 3-4 --- */
        resetRight(5);
        move_down();
    }

    /* --- Line 5 --- */
    set_blue_color();
    draw_pixel();

    move_left();

    set_black_color();
    draw_pixel();

    move_left();

    set_white_color();
    lineLeft(4);

    set_black_color();
    lineLeft(23); // Edit this

    /* --- Reset 5 --- */
    resetRight(35);
    move_down();

    /* --- Line 6 --- */
    set_blue_color();
    draw_pixel();

    move_left();

    set_black_color();
    lineLeft(7);

    set_white_color();
    lineLeft(26); // Edit this

    set_black_color();
    lineLeft(3);

    /* --- Reset 6 --- */
    resetRight(34);
    move_down();

    /* --- Line 7 --- */
    set_blue_color();
    draw_pixel();

    move_left();

    set_black_color();
    lineLeft(5);

    set_white_color();
    lineLeft(5);

    for (int i = 0; i < 9; i++)
    {
        if (i % 2 == 0)
        {
            set_black_color();
        }
        else
        {
            set_white_color();
        }
        lineLeft(2);
    }

    set_white_color();
    lineLeft(4);

    set_black_color();
    lineLeft(4);

    /* --- Reset 7 --- */
    resetRight(32);
    move_down();

    /* --- Line 8 --- */
    set_blue_color();
    draw_pixel();

    move_left();

    set_black_color();
    draw_pixel();

    move_left();

    set_white_color();
    lineLeft(9);

    set_black_color();
    lineLeft(5);

    set_white_color(9);
    lineLeft(14);

    set_black_color();
    draw_pixel();

    /* --- Reset 8 --- */
    resetRight(29);
    move_down();

    /* --- Line 9 --- */
    set_blue_color();
    draw_pixel();

    move_left();

    set_black_color();
    lineLeft(9);

    set_white_color();
    lineLeft(5);

    set_black_color();
    lineLeft(14);

    /* --- Reset 9 --- */
    resetRight(20);
    move_down();
    /* --- Line 10 --- */
    set_blue_color();
    draw_pixel();

    move_left();

    set_black_color();
    lineLeft(6);
}
