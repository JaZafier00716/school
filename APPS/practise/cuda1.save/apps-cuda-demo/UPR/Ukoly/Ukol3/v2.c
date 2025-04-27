#include "drawing.h"
#include <stdlib.h>

void flower(int height, int width);
void reset_left(int width);

int main()
{
    clear_screen();

    set_blue_color();
    move_to(6, 6);
    flower(6, 4);

    reset_color();

    // Keep this line here
    end_drawing();

    return 0;
}

void flower(int height, int width)
{
    for (int i = 0; i < height; i++)
    {
        if (width == 1)
        {
            draw_pixel();
            move_right();
        }
        else
        {
            if (width == 2)
            {
                if (i == 0)
                {
                    draw_pixel();
                    move_right();
                }
                else
                {

                    for (int j = 0; j < width; j++)
                    {
                        draw_pixel();
                        move_right();
                    }
                }
            }
            else
            {
                for (int j = 0; j < width; j++)
                {
                    if (i == 0)
                    {
                        if (width % 2 == 0 && (j == width / 2 || j == width / 2 + 1))
                        {
                            move_right();
                            continue;
                        }
                        if (j > width / 2)
                        {
                            if (j % 2 == 1)
                            {
                                draw_pixel();
                            }
                        }
                        else
                        {
                            if (j % 2 == 0)
                            {
                                draw_pixel();
                            }
                        }
                    }
                    else
                    {
                        if (i >= height - 1 && (j < 2 || j >= height - 3))
                        {
                            move_right();
                            continue;
                        }
                        else
                        {
                            draw_pixel();
                        }
                    }
                    move_right();
                }
            }
        }
        reset_left(width);
        move_down();
    }
}

void reset_left(int width)
{
    for (int i = 0; i < width; i++)
    {
        move_left();
    }
}