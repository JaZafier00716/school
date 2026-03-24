from ui.sections import *


class TestScreen(
    ctk.CTk
):
    def __init__(
            self
    ):
        super().__init__()
        self.title(
            "NavBar Variants"
        )
        self.geometry(
            "360x900"
        )
        self.configure(
            fg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"]
        )

        self.container = ctk.CTkFrame(
            self,
            fg_color="transparent"
        )
        self.container.pack(
            fill="both",
            expand=True,
            padx=12,
            pady=12
        )

        # nav1 = NavBar.with_back_and_title(self.container, "Game", bg_color=COLORS[ctk.get_appearance_mode()]["bg"])
        # nav1.pack(fill="x", pady=(0, 10))
        #
        # nav2 = NavBar.with_title_and_cog(self.container, "Settings", bg_color=COLORS[ctk.get_appearance_mode()]["bg-light"])
        # nav2.pack(fill="x", pady=(0, 10))
        #
        # nav3 = NavBar.with_stacked_titles_and_cog(self.container, "Profile", "Online", bg_color=COLORS[ctk.get_appearance_mode()]["bg"])
        # nav3.pack(fill="x", pady=(0, 10))

        self.statistics = StatisticsBar(
            self.container,
            f"Hole: {2} / {18}",
            f"Score: {+3}",
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
        )
        self.statistics.pack(
            fill="x",
            pady=(0, 10)
        )

        self.stat_row = StatsRow(
            self.container,
            [("Par", "4"), ("Distance", "350m")],
            bg_color=COLORS[ctk.get_appearance_mode()]["bg-light"]
        )
        self.stat_row.pack(
            fill="x",
            pady=(0, 10)
        )

        self.throw_counter = ThrowCounter(
            self.container,
            0,
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
        )
        self.throw_counter.pack(
            fill="x",
            pady=(0, 10)
        )

        self.button_grid = ButtonGrid(
            self.container,
            bg_color=COLORS[ctk.get_appearance_mode()]["bg-light"]
        )
        self.button_grid.pack(
            fill="x",
            pady=(0, 10)
        )

        self.label_switch = LabelSwitch(
            self.container,
            "Dark Mode",
            on_click=self.toggle_theme,
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"],
        )
        self.label_switch.pack(
            fill="x",
            pady=(0, 10)
        )

        self.title_buttons = TitleButtons(
            self.container,
            "Distance Units",
            "Meters",
            "Feet",
            units=Units.Metric,
            update_units_callback=self._update_units,
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
        )
        self.title_buttons.pack(
            fill="x",
            pady=(0, 10)
        )

        self.edit_nick = EditNick(
            self.container,
            "Nickname",
            "#playerID",
            update_nick_callback=self._update_nickname,
            bg_color=COLORS[ctk.get_appearance_mode()]["bg-light"]
        )
        self.edit_nick.pack(
            fill="x",
            pady=(0, 10)
        )

    def _update_nickname(self, new_nickname):
        """Handle nickname update"""
        print(f"Nickname updated to: {new_nickname}")
        # TODO: Implement actual nickname save logic (e.g., API call, database update)

    def _update_units(self, new_units):
        """Handle units update"""
        print(f"Units updated to: {new_units}")

    def toggle_theme(self):
        # Toggle appearance mode
        new_mode = "Dark" if ctk.get_appearance_mode() == "Light" else "Light"
        ctk.set_appearance_mode(new_mode)

        # Update all custom colors
        self.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"])
        self.statistics.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg"])
        self.stat_row.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg-light"])
        self.throw_counter.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg"])
        self.button_grid.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg-light"])
        self.label_switch.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg"])
        self.title_buttons.container.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg"])
        self.title_buttons.update_theme_colors()
        self.edit_nick.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg-light"])

        # Update button colors in ThrowCounter
        self.throw_counter.minus_btn.configure(
            fg_color=COLORS[ctk.get_appearance_mode()]["border"],
            hover_color=COLORS[ctk.get_appearance_mode()]["border-muted"]
        )
        self.throw_counter.plus_btn.configure(
            fg_color=COLORS[ctk.get_appearance_mode()]["button-primary"],
            text_color=COLORS[ctk.get_appearance_mode()]["highlight"],
            hover_color=COLORS[ctk.get_appearance_mode()]["button-primary-hover"]
        )

        # Update button colors in ButtonGrid
        neutral_colors = button_neutral()
        self.button_grid.prev_button.configure(**neutral_colors)
        self.button_grid.next_button.configure(**neutral_colors)
        self.button_grid.finish_button.configure(**button_primary())


class CourseScreen(
    ctk.CTk
):
    def __init__(
            self,
            app_state
    ):
        super().__init__()
        self.app_state = app_state
        self.title(
            "Course Screen"
        )
        self.geometry(
            "360x800"
        )
        self.configure(
            fg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"]
        )

        self.current_hole = 1
        self.holes = app_state.round["holes"]
        self.units = app_state.units

        self.container = ctk.CTkFrame(
            self,
            fg_color="transparent"
        )
        self.container.pack(
            fill="both",
            expand=True,
            padx=10,
            pady=10
        )

        self.nav = NavBar.with_stacked_titles_and_cog(
            self.container,
            app_state.round["name"],
            app_state.round["location"],
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
        )
        self.nav.pack(
            fill="x"
        )
        # Wire up cog button to settings
        self.nav.cog_btn.configure(command=self._open_settings)

        self.statistics = StatisticsBar(
            self.container,
            f"Hole: {self.current_hole} / {len(self.holes)}",
            f"Score: {self.calculate_score()}",
            bg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"]
        )
        self.statistics.pack(
            fill="x"
        )

        self.stat_row = StatsRow(
            self.container,
            [
                (
                    "Par",
                    f"{self.holes[self.current_hole - 1]['par']}"
                ), (
                "Distance",
                f"{self.holes[self.current_hole - 1]['distance']['metric']}m"
                if app_state.units == Units.Metric else
                f"{self.holes[self.current_hole - 1]['distance']['imperial']}ft"
            )],
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
        )
        self.stat_row.pack(
            fill="x",
            pady=(0, 10)
        )

        self.throw_counter = ThrowCounter(
            self.container,
            self.holes[self.current_hole - 1]["throws"],
            update_throws_callback=self._update_throws,
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"],
        )
        self.throw_counter.pack(
            fill="x",
            pady=(0, 10),
            expand=True
        )

        self.button_grid = ButtonGrid(
            self.container,
            bg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"],
            next_on_click=self._go_to_next_hole,
            prev_on_click=self._go_to_prev_hole,
        )
        self.button_grid.pack(
            fill="x",
            pady=(0, 10)
        )
        # Set initial button states
        self.button_grid.update_button_states(self.current_hole, len(self.holes))
        # Wire up finish button
        self.button_grid.finish_button.configure(command=self._finish_round)

    def calculate_score(self):
        score = 0
        # Iterate through completed holes (before current hole)
        for i in range(self.current_hole - 1):
            hole = self.holes[i]
            if hole["throws"] > 0:
                score += (hole["throws"] - hole["par"])
        return score

    def _refresh_hole_display(self):
        # Refresh top stats bar
        self.statistics.hole_num.configure(
            text=f"Hole: {self.current_hole} / {len(self.holes)}"
        )
        self.statistics.score.configure(
            text=f"Score: {self.calculate_score()}"
        )

        # Remember button_grid position
        self.button_grid.pack_forget()

        # Rebuild stat row (par/distance) for current hole
        self.stat_row.destroy()
        self.stat_row = StatsRow(
            self.container,
            [
                ("Par", str(self.holes[self.current_hole - 1]["par"])),
                ("Distance",
                 f'{self.holes[self.current_hole - 1]["distance"]["metric"]}m'
                 if self.units == Units.Metric
                 else f'{self.holes[self.current_hole - 1]["distance"]["imperial"]}ft')
            ],
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
        )
        self.stat_row.pack(fill="x", pady=(0, 10))

        # Rebuild throw counter for current hole
        current_throws = self.holes[self.current_hole - 1]["throws"]
        self.throw_counter.destroy()
        self.throw_counter = ThrowCounter(
            self.container,
            current_throws,
            update_throws_callback=self._update_throws,
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
        )
        self.throw_counter.pack(fill="x", pady=(0, 10), expand=True)

        # Update button states based on current hole
        self.button_grid.update_button_states(self.current_hole, len(self.holes))

        # Repack button_grid at the end
        self.button_grid.pack(fill="x", pady=(0, 10))

    def _update_throws(self, throws):
        self.holes[self.current_hole - 1]["throws"] = throws
        self.statistics.score.configure(text=f"Score: {self.calculate_score()}")

    def _go_to_next_hole(self):
        if self.current_hole < len(self.holes):
            self.current_hole += 1
            self._refresh_hole_display()

    def _go_to_prev_hole(self):
        if self.current_hole > 1:
            self.current_hole -= 1
            self._refresh_hole_display()

    def _open_settings(self):
        """Navigate to settings screen"""
        self.withdraw()  # Hide current window
        settings = SettingsScreen(self.app_state, on_back=lambda: self._return_from_settings())
        settings.protocol("WM_DELETE_WINDOW", lambda: self._close_app(settings))
        settings.mainloop()

    def _return_from_settings(self):
        """Return from settings screen"""
        self.deiconify()  # Show this window again
        # Sync units from app_state
        self.units = self.app_state.units
        self._update_theme_colors()  # Update colors after potential theme change
        self._refresh_distance_display()  # Update distance units display

    def _refresh_distance_display(self):
        """Refresh the distance display with current units"""
        # Rebuild stat row with updated units
        self.stat_row.destroy()
        self.stat_row = StatsRow(
            self.container,
            [
                ("Par", str(self.holes[self.current_hole - 1]["par"])),
                ("Distance",
                 f'{self.holes[self.current_hole - 1]["distance"]["metric"]}m'
                 if self.units == Units.Metric
                 else f'{self.holes[self.current_hole - 1]["distance"]["imperial"]}ft')
            ],
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
        )
        # Insert stat_row back in the correct position (after statistics, before throw_counter)
        self.stat_row.pack(fill="x", pady=(0, 10), after=self.statistics)

    def _update_theme_colors(self):
        """Update all theme-dependent colors"""
        # Update main background
        self.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"])

        # Update navbar
        self.nav.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg"])
        self.nav.update_theme_colors()

        # Update statistics bar
        self.statistics.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"])
        self.statistics.hole_num.configure(text_color=COLORS[ctk.get_appearance_mode()]["text-muted"])
        self.statistics.score.configure(text_color=COLORS[ctk.get_appearance_mode()]["text-muted"])

        # Update stat row
        self.stat_row.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg"])

        # Update throw counter
        self.throw_counter.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg"])
        self.throw_counter.minus_btn.configure(
            fg_color=COLORS[ctk.get_appearance_mode()]["border"] if self.throw_counter.throws > 0 else COLORS[ctk.get_appearance_mode()]["border-muted"],
            text_color=COLORS[ctk.get_appearance_mode()]["highlight"] if self.throw_counter.throws > 0 else COLORS[ctk.get_appearance_mode()]["text-muted"],
            hover_color=COLORS[ctk.get_appearance_mode()]["border-muted"]
        )
        self.throw_counter.plus_btn.configure(
            fg_color=COLORS[ctk.get_appearance_mode()]["button-primary"],
            text_color=COLORS[ctk.get_appearance_mode()]["highlight"],
            hover_color=COLORS[ctk.get_appearance_mode()]["button-primary-hover"]
        )

        # Update button grid
        self.button_grid.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"])
        # Update button colors based on their current state
        if self.current_hole == 1:
            self.button_grid.prev_button.configure(**button_disabled())
        else:
            self.button_grid.prev_button.configure(**button_neutral())

        if self.current_hole == len(self.holes):
            self.button_grid.next_button.configure(**button_disabled())
        else:
            self.button_grid.next_button.configure(**button_neutral())

        self.button_grid.finish_button.configure(**button_primary())

    def _finish_round(self):
        """Navigate to round summary screen"""
        self.withdraw()  # Hide current window
        summary = RoundSummaryScreen(self.app_state, on_back=self._return_from_summary)
        summary.protocol("WM_DELETE_WINDOW", lambda: self._close_app(summary))
        summary.mainloop()

    def _return_from_summary(self):
        """Return from round summary screen"""
        self.deiconify()  # Show this window again
        self._update_theme_colors()  # Update colors after potential theme change in summary

    def _close_app(self, window):
        """Handle window close"""
        window.destroy()
        self.destroy()



class SettingsScreen(
    ctk.CTk
):
    def __init__(
            self,
            app_state,
            on_back=None
    ):
        super().__init__()
        self.app_state = app_state
        self.on_back = on_back
        self.title(
            "Settings Screen"
        )
        self.geometry(
            "360x800"
        )
        self.configure(
            fg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"]
        )

        self.container = ctk.CTkFrame(
            self,
            fg_color="transparent"
        )
        self.container.pack(
            fill="both",
            expand=True,
            padx=10,
            pady=10
        )

        self.nav = NavBar.with_back_and_title(
            self.container,
            "Settings",
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
        )
        self.nav.pack(
            fill="x",
            pady=(0, 10)
        )
        # Wire up back button
        self.nav.back_btn.configure(command=self._go_back)

        self.edit_nick = EditNick(
            self.container,
            app_state.nickname,
            "#playerID",
            update_nick_callback=self._update_nickname,
            bg_color=COLORS[ctk.get_appearance_mode()]["bg-light"]
        )
        self.edit_nick.pack(
            fill="x",
            pady=(0, 10)

        )

        self.label_switch = LabelSwitch(
            self.container,
            "Dark Mode",
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"],
            on_click=self.toggle_theme
        )
        self.label_switch.pack(
            fill="x",
            pady=(0, 10)
        )

        self.title_buttons = TitleButtons(
            self.container,
            "Distance Units",
            "Meters",
            "Feet",
            units=app_state.units,
            update_units_callback=self._update_units,
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"],
        )
        self.title_buttons.pack(
            fill="x",
            pady=(0, 10)
        )

        self.title_desc = TitleDesc(
            self.container,
            "DiscGolf Tracker",
            "Version 1.0.0",
            title_size=24,
            desc_size=18
        )
        self.title_desc.pack(
            fill="x",
            expand=True,
            pady=(0, 10),
            anchor="s"
        )

    def toggle_theme(self):
        # Toggle appearance mode
        new_mode = "Dark" if ctk.get_appearance_mode() == "Light" else "Light"
        ctk.set_appearance_mode(new_mode)

        # Update all custom colors
        self.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"])
        self.nav.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg"])
        self.nav.update_theme_colors()
        self.edit_nick.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg-light"])
        self.label_switch.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg"])
        self.title_buttons.container.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg"])
        self.title_buttons.update_theme_colors()

        # Update TitleDesc border color
        self.title_desc.top_border.configure(bg=COLORS[ctk.get_appearance_mode()]["border"])

    def _go_back(self):
        """Handle back button - return to previous screen"""
        if self.on_back:
            self.destroy()
            self.on_back()
        else:
            self.destroy()

    def _update_nickname(self, new_nickname):
        """Handle nickname update"""
        self.app_state.update_nickname(new_nickname)

    def _update_units(self, new_units: Units):
        """Handle units update"""
        self.app_state.update_units(new_units)


class RoundSummaryScreen(
    ctk.CTk
):
    def __init__(
            self,
            app_state,
            on_back=None
    ):
        super().__init__()
        self.app_state = app_state
        self.on_back = on_back
        self.title(
            "Round Summary"
        )
        self.geometry(
            "360x800"
        )
        self.configure(
            fg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"]
        )

        self.container = ctk.CTkFrame(
            self,
            fg_color="transparent"
        )
        self.container.pack(
            fill="both",
            expand=True,
            padx=10,
            pady=10
        )

        self.nav = NavBar.with_back_and_title(
            self.container,
            "Round Summary",
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
        )
        self.nav.pack(
            fill="x",
            pady=(0, 10)
        )
        # Always wire up back button
        self.nav.back_btn.configure(command=self._go_back)

        self.statistics = StatsRow(
            self.container,
            [
                ("Par", str(app_state.round["par"])),
                ("Total Score", str(app_state.get_total_score())),
                ("Average", f"{app_state.get_total_score() / len(app_state.round['holes']):.2f}")
            ],
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
        )
        self.statistics.pack(
            fill="x",
            pady=(0, 10)
        )

        self.hole_container = HoleByHole(
            self.container,
            app_state.round["holes"],
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"],
        )
        self.hole_container.pack(
            fill="both",
            expand=True,
            pady=(0, 10),
        )

    def _go_back(self):
        """Handle back button - return to previous screen"""
        self.destroy()
        if self.on_back:
            self.on_back()

    def update_theme_colors(self):
        """Update all theme-dependent colors"""
        # Update main background
        self.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"])

        # Update navbar
        self.nav.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg"])
        self.nav.update_theme_colors()

        # Update statistics
        self.statistics.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg"])

        # Update hole container
        self.hole_container.update_theme_colors()

