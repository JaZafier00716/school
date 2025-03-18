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

#define T 100

class LED {
public:
    DigitalOut m_led;
    uint32_t m_T0; // čas T0
    uint32_t m_default_T0; // výchozí hodnota pro obnovení

    LED(uint32_t t_led_pin) : m_led(t_led_pin), m_T0(0), m_default_T0(0) {}

    void nastav_jas_proc(uint8_t t_jas_proc) {
        m_T0 = static_cast<uint32_t>(t_jas_proc * T / 100);
        m_default_T0 = m_T0;
    }

    void vypnout() {
        m_T0 = 0;
    }

    void obnovit() {
        m_T0 = m_default_T0;
    }
};

LED g_red_led[] = { P3_16, P3_17 };
DigitalIn g_button_off(P3_18);
DigitalIn g_button_on(P3_19);

void pwm_control() {
    static uint32_t ticks = 0;

    for (auto &led : g_red_led) {
        led.m_led = (ticks < led.m_T0) ? 0 : 1;
    }

    ticks = (ticks + 1) % T;
}

void check_buttons() {
    if (g_button_off.read() == 0) { // Tlačítko zhasnutí
        for (auto &led : g_red_led) {
            led.vypnout();
        }
    }

    if (g_button_on.read() == 0) { // Tlačítko obnovení
        for (auto &led : g_red_led) {
            led.obnovit();
        }
    }
}

int main() {
    g_red_led[0].nastav_jas_proc(5);
    g_red_led[1].nastav_jas_proc(40);

    Ticker pwm1;
    pwm1.attach(pwm_control, 1);

    while (1) {
        check_buttons();
        __WFI();
    }
}
