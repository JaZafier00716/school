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
            bg_color=COLORS[ctk.get_appearance_mode()]["bg-light"]
        )
        self.edit_nick.pack(
            fill="x",
            pady=(0, 10)
            )

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
            self
            ):
        super().__init__()
        self.title(
            "Course Screen"
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

        self.nav = NavBar.with_stacked_titles_and_cog(
            self.container,
            "Sunnydale Park",
            "Location: Springfield",
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
        )
        self.nav.pack(
            fill="x"
            )

        self.statistics = StatisticsBar(
            self.container,
            f"Hole: {2} / {18}",
            f"Score: {+3}",
            bg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"]
        )
        self.statistics.pack(
            fill="x"
            )

        self.stat_row = StatsRow(
            self.container,
            [("Par", "4"), ("Distance", "350m")],
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
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
            pady=(0, 10),
            expand=True
            )

        self.button_grid = ButtonGrid(
            self.container,
            bg_color=COLORS[ctk.get_appearance_mode()]["bg-dark"]
        )
        self.button_grid.pack(
            fill="x",
            pady=(0, 10)
            )

class SettingsScreen(
    ctk.CTk
    ):
    def __init__(
            self
            ):
        super().__init__()
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

        self.edit_nick = EditNick(
            self.container,
            "Nickname",
            "#playerID",
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
            bg_color=COLORS[ctk.get_appearance_mode()]["bg"]
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
