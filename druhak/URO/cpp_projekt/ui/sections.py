from customtkinter import CTkFrame, CTkButton

from ui.components import *


class NavBar(
    ctk.CTkFrame
    ):
    HEIGHT = 60

    def __init__(
            self,
            master,
            bg_color=None,
            **kw
            ):
        super().__init__(
            master,
            height=self.HEIGHT,
            fg_color=bg_color,
            **kw
            )
        self.grid_columnconfigure(
            0,
            weight=1
            )
        self.grid_columnconfigure(
            1,
            weight=0
            )
        self.grid_propagate(
            False
            )
        self.back_btn = None
        self.cog_btn = None
        self.muted_lbl = None

    @classmethod
    def with_back_and_title(
            cls,
            parent,
            title: str,
            bg_color=None
            ):
        bar = cls(
            parent,
            bg_color=bg_color
            )

        left = ctk.CTkFrame(
            bar,
            fg_color="transparent"
            )
        left.grid(
            row=0,
            column=0,
            sticky="w",
            padx=20,
            pady=14
            )

        bar.back_btn = icon_button(
            left,
            "←"
            )
        bar.back_btn.pack(
            side="left",
            fill="y"
            )

        title_lbl = ctk.CTkLabel(
            left,
            text=title,
            font=ctk.CTkFont(
                size=24,
                weight="bold"
                )
            )
        title_lbl.pack(
            side="left",
            padx=(20, 0),
            expand=True,
            fill="both"
            )

        return bar

    @classmethod
    def with_title_and_cog(
            cls,
            parent,
            title: str,
            bg_color=None
            ):
        bar = cls(
            parent,
            bg_color=bg_color
            )

        title_lbl = ctk.CTkLabel(
            bar,
            text=title,
            font=ctk.CTkFont(
                size=24,
                weight="bold"
                )
            )
        title_lbl.grid(
            row=0,
            column=0,
            sticky="w",
            padx=20,
            pady=0
            )

        bar.cog_btn = icon_button(
            bar,
            "⚙",
            is_muted=True
            )
        bar.cog_btn.grid(
            row=0,
            column=1,
            sticky="e",
            padx=20,
            pady=14
            )

        return bar

    @classmethod
    def with_stacked_titles_and_cog(
            cls,
            parent,
            title: str,
            muted: str,
            bg_color=None
            ):
        bar = cls(
            parent,
            bg_color=bg_color
            )

        text_wrap = ctk.CTkFrame(
            bar,
            fg_color="transparent"
            )
        text_wrap.grid(
            row=0,
            column=0,
            sticky="w",
            padx=20,
            pady=0
            )

        title_lbl = ctk.CTkLabel(
            text_wrap,
            text=title,
            font=ctk.CTkFont(
                size=24,
                weight="bold"
                )
            )
        title_lbl.pack(
            anchor="w"
            )

        bar.muted_lbl = ctk.CTkLabel(
            text_wrap,
            text=muted,
            text_color=COLORS[ctk.get_appearance_mode()]["text-muted"],
            font=ctk.CTkFont(
                size=16,
                weight="normal"
                ),
        )
        bar.muted_lbl.pack(
            anchor="w"
            )

        bar.cog_btn = icon_button(
            bar,
            "⚙",
            is_muted=True
            )
        bar.cog_btn.grid(
            row=0,
            column=1,
            sticky="e",
            padx=20,
            pady=14
            )

        return bar

    def update_theme_colors(
            self
            ):
        if self.back_btn is not None:
            self.back_btn.configure(
                text_color=COLORS[ctk.get_appearance_mode()]["text"],
                hover_color=COLORS[ctk.get_appearance_mode()]["bg-light"]
            )
        if self.cog_btn is not None:
            self.cog_btn.configure(
                text_color=COLORS[ctk.get_appearance_mode()]["text-muted"],
                hover_color=COLORS[ctk.get_appearance_mode()]["bg-light"]
            )
        if self.muted_lbl is not None:
            self.muted_lbl.configure(
                text_color=COLORS[ctk.get_appearance_mode()]["text-muted"]
            )


class StatisticsBar(
    ctk.CTkFrame
    ):

    def __init__(
            self,
            master,
            left_text,
            right_text,
            bg_color=None,
            **kw
            ):
        super().__init__(
            master,
            fg_color=bg_color,
            **kw
            )
        self.grid_columnconfigure(
            0,
            weight=1
            )
        self.grid_columnconfigure(
            1,
            weight=1
            )

        self.hole_num = ctk.CTkLabel(
            self,
            text=left_text,
            text_color=COLORS[ctk.get_appearance_mode()]["text-muted"],
            font=ctk.CTkFont(
                size=14,
                weight="normal"
                )
        )
        self.hole_num.grid(
            row=0,
            column=0,
            sticky="w",
            padx=20,
            pady=5
            )
        self.score = ctk.CTkLabel(
            self,
            text=right_text,
            text_color=COLORS[ctk.get_appearance_mode()]["text-muted"],
            font=ctk.CTkFont(
                size=14,
                weight="normal"
                )
        )
        self.score.grid(
            row=0,
            column=1,
            sticky="e",
            padx=20,
            pady=5
            )


class StatsRow(
    ctk.CTkFrame
    ):

    def __init__(
            self,
            master,
            stats,
            bg_color=None,
            **kw
            ):
        """
        Create a row of vertical stat labels evenly spaced.

        Args:
            master: Parent widget
            stats: List of dicts with 'title' and 'label' keys
                   e.g., [{"title": "5", "label": "Holes"}, {"title": "Par 3", "label": "Current"}]
            bg_color: Background color
        """
        super().__init__(
            master,
            fg_color=bg_color,
            **kw
            )

        # Configure equal weight columns for even spacing
        for i in range(
                len(
                        stats
                        )
                ):
            self.grid_columnconfigure(
                i,
                weight=1,
                uniform="stats"
                )

        # Create a label_title_vertical component for each stat
        for (i, stat) in enumerate(
                stats
                ):
            stat_widget = label_title_vertical(
                self,
                stat[1],
                stat[0]
                )
            stat_widget.grid(
                row=0,
                column=i,
                sticky="ew",
                padx=10,
                pady=10
                )


class ThrowCounter(
    ctk.CTkFrame
    ):

    def __init__(
            self,
            master,
            throw_count,
            update_throws_callback,
            bg_color=None,
            **kw
            ):
        super().__init__(
            master,
            fg_color=bg_color,
            **kw
            )

        self.update_throws_callback = update_throws_callback
        self.throws = throw_count

        self.grid_rowconfigure(
            0,
            weight=0
            )
        self.grid_rowconfigure(
            1,
            weight=1
            )
        self.grid_columnconfigure(
            0,
            weight=1
            )

        throw_counter = label_title_vertical(
            self,
            str(
                throw_count
                ),
            "Throws",
            72,
            16
            )
        throw_counter.grid(
            row=0,
            column=0,
            padx=0,
            pady=14
            )

        button_row = CTkFrame(
            self,
            fg_color="transparent",
            )
        button_row.grid(
            row=1,
            column=0,
            pady=20
            )

        self.minus_btn = icon_button(
            button_row,
            "−",
            size=64,
            radius=20,
            fg_color=COLORS[ctk.get_appearance_mode()]["border"] if throw_count > 0 else COLORS[ctk.get_appearance_mode()]["border-muted"],
            text_color=COLORS[ctk.get_appearance_mode()]["highlight"] if throw_count > 0 else COLORS[ctk.get_appearance_mode()]["text-muted"],
            command=lambda: self.throws_update(-1)
        )
        self.minus_btn.pack(
            side="left",
            padx=10
            )
        self.plus_btn = icon_button(
            button_row,
            "+",
            size=64,
            radius=20,
            fg_color=COLORS[ctk.get_appearance_mode()]["button-primary"],
            text_color=COLORS[ctk.get_appearance_mode()]["highlight"],
            command=lambda: self.throws_update(+1)
        )
        self.plus_btn.pack(
            side="left",
            padx=10
            )

    def throws_update(self, delta):
        new_throws = self.throws + delta
        if new_throws < 0:
            return
        self.throws = new_throws
        self.minus_btn.configure(
            command=lambda: self.throws_update(-1)
        )
        self.plus_btn.configure(
            command=lambda: self.throws_update(+1)
        )
        # Update the throw count label (first child of the throw_counter component)
        throw_counter_title = self.winfo_children()[0].winfo_children()[1]
        throw_counter_title.configure(
            text=str(self.throws)
        )
        # Call the callback to update the main app state
        if self.update_throws_callback:
            self.update_throws_callback(self.throws)


class ButtonGrid(
    ctk.CTkFrame
    ):

    def __init__(
            self,
            master,
            next_on_click,
            prev_on_click,
            bg_color=None,
            **kw
            ):
        super().__init__(
            master,
            fg_color=bg_color,
            **kw
            )

        # Store callbacks
        self.next_on_click = next_on_click
        self.prev_on_click = prev_on_click
        self.is_prev_enabled = True
        self.is_next_enabled = True

        # Configure grid for 2 columns
        self.grid_columnconfigure(
            0,
            weight=1
            )
        self.grid_columnconfigure(
            1,
            weight=1
            )

        # Store button references for theme updates
        self.prev_button = CTkButton(
            self,
            text="<    Previous",
            border_width=1,
            corner_radius=10,
            height=52,
            width=150,
            **button_neutral(),
            command=self._prev_clicked
        )
        self.prev_button.grid(
            row=0,
            column=0,
            pady=0,
            padx=(10, 5),
            sticky="ew"
        )

        self.next_button = CTkButton(
            self,
            text="Next    >",
            border_width=1,
            corner_radius=10,
            height=52,
            width=150,
            **button_neutral(),
            command=self._next_clicked
        )
        self.next_button.grid(
            row=0,
            column=1,
            pady=0,
            padx=(5, 10),
            sticky="ew"
        )

        self.finish_button = CTkButton(
            self,
            text="Finish Round",
            border_width=0,
            corner_radius=10,
            height=52,
            **button_primary()
        )
        self.finish_button.grid(
            row=1,
            column=0,
            columnspan=2,
            pady=10,
            padx=10,
            sticky="ew"
            )

    def _prev_clicked(self):
        """Handle previous button click - only execute if enabled"""
        if self.is_prev_enabled and self.prev_on_click:
            self.prev_on_click()

    def _next_clicked(self):
        """Handle next button click - only execute if enabled"""
        if self.is_next_enabled and self.next_on_click:
            self.next_on_click()

    def update_button_states(self, current_hole, total_holes):
        """Update button enabled/disabled states based on current hole"""
        is_first_hole = current_hole == 1
        is_last_hole = current_hole == total_holes

        # Update prev button
        self.is_prev_enabled = not is_first_hole
        if is_first_hole:
            self.prev_button.configure(**button_disabled())
        else:
            self.prev_button.configure(**button_neutral())

        # Update next button
        self.is_next_enabled = not is_last_hole
        if is_last_hole:
            self.next_button.configure(**button_disabled())
        else:
            self.next_button.configure(**button_neutral())


class LabelSwitch(
    ctk.CTkFrame
    ):

    def __init__(
            self,
            master,
            label_text,
            on_click,
            bg_color=None,
            **kw
            ):
        super().__init__(
            master,
            fg_color=bg_color,
            **kw
            )

        self.grid_columnconfigure(
            0,
            weight=1
            )
        self.grid_columnconfigure(
            1,
            weight=0
            )

        label = ctk.CTkLabel(
            self,
            text=label_text,
            font=ctk.CTkFont(
                size=20,
                weight="normal"
                )
            )
        label.grid(
            row=0,
            column=0,
            sticky="w",
            padx=20,
            pady=14
            )

        switch = ctk.CTkSwitch(
            self,
            text="",
            width=52,
            height=32,
            command=on_click,
        )
        # Keep switch state in sync with current global appearance mode
        if ctk.get_appearance_mode() == "Dark":
            switch.select()
        else:
            switch.deselect()
        switch.grid(
            row=0,
            column=1,
            sticky="e",
            padx=20,
            pady=14
        )


class TitleButtons(
    ctk.CTkFrame
    ):
    def __init__(
            self,
            master,
            title_text,
            button_left_text,
            button_right_text,
            units,
            update_units_callback,
            bg_color=None,
            **kw
            ):
        super().__init__(
            master,
            fg_color="transparent",
            **kw
            )

        self.units = units
        self.update_units_callback = update_units_callback

        self.container = TitleContainer(
            self,
            title_text,
            bg_color=bg_color,
            corner_radius=10
        )
        self.container.pack(
            fill="x"
            )

        self.button_row = ctk.CTkFrame(
            self.container.content_host,
            fg_color="transparent"
        )
        self.button_row.grid_columnconfigure(
            0,
            weight=1
        )
        self.button_row.grid_columnconfigure(
            1,
            weight=1
        )

        self.left_button = CTkButton(
            self.button_row,
            text=button_left_text,
            border_width=1,
            corner_radius=10,
            height=50,
            width=140,
            **button_neutral() if units == Units.Metric else button_secondary(),
            command=lambda: self.units_update(Units.Metric)
        )
        self.left_button.grid(
            row=0,
            column=0,
            sticky="w",
            padx=(0, 10),
            pady=0
        )

        self.right_button = CTkButton(
            self.button_row,
            text=button_right_text,
            border_width=1,
            corner_radius=10,
            height=50,
            width=140,
            **button_secondary() if units == Units.Metric else button_neutral(),
            command=lambda: self.units_update(Units.Imperial)
        )
        self.right_button.grid(
            row=0,
            column=1,
            sticky="e",
            padx=(10, 0),
            pady=0
        )

        self.container.add_content(
            self.button_row
            )

    def units_update(self, new_units):
        if self.units == new_units:
            return

        self.units = new_units

        if new_units == Units.Metric:
            self.left_button.configure(**button_neutral())
            self.right_button.configure(**button_secondary())
        else:
            self.left_button.configure(**button_secondary())
            self.right_button.configure(**button_neutral())

        if self.update_units_callback:
            self.update_units_callback(new_units)


    def update_theme_colors(
            self
            ):
        self.left_button.configure(
            **button_neutral()
            )
        self.right_button.configure(
            **button_secondary()
            )

class EditNick(
    ctk.CTkFrame
    ):

    def __init__(
            self,
            master,
            nickname,
            player_id,
            update_nick_callback=None,
            bg_color=None,
            **kw
            ):
        super().__init__(
            master,
            fg_color=bg_color,
            **kw
            )

        self.nickname = nickname
        self.player_id = player_id
        self.is_editable = False
        self.update_nick_callback = update_nick_callback

        # Container for edit_nick widget (will be replaced when toggling edit mode)
        self.edit_nick_container = ctk.CTkFrame(
            self,
            fg_color="transparent"
        )
        self.edit_nick_container.pack(
            fill="x",
            pady=(14, 0),
            padx=(20, 15)
        )

        self._create_edit_nick_widget()

        player_id_label = ctk.CTkLabel(
            self,
            text=player_id,
            font=ctk.CTkFont(
                size=18,
                weight="normal"
            ),
            text_color=COLORS[ctk.get_appearance_mode()]["text-muted"]
        )
        player_id_label.pack(
            anchor="w",
            padx=20,
            pady=(0, 14),
        )

    def _create_edit_nick_widget(self):
        """Create or recreate the edit_nick widget based on edit mode"""
        # Clear container
        for widget in self.edit_nick_container.winfo_children():
            widget.destroy()

        if self.is_editable:
            # Create editable version with save callback
            edit_nick = title_icon_editable(
                self.edit_nick_container,
                title=self.nickname,
                icon_text="✓",
                title_size=40,
                icon_size=32,
                is_editable=True,
                edit_text="Enter Nickname",
                on_save=self._save_nickname
            )
        else:
            # Create display version with edit callback
            def toggle_edit():
                self.is_editable = True
                self._create_edit_nick_widget()

            edit_nick = title_icon_editable(
                self.edit_nick_container,
                title=self.nickname,
                icon_text="🖉",
                title_size=40,
                icon_size=32,
                is_editable=False,
                is_icon_left=False
            )
            # The icon button is the second child (column 1) in the wrapper grid
            # Find and wire the icon button to toggle edit mode
            for child in edit_nick.winfo_children():
                if isinstance(child, ctk.CTkButton):
                    child.configure(command=toggle_edit)

        edit_nick.pack(fill="x")

    def _save_nickname(self, new_nickname):
        """Handle nickname save and toggle back to display mode"""
        # Enforce 10 character maximum
        new_nickname = new_nickname.strip()
        was_truncated = len(new_nickname) > 10
        new_nickname = new_nickname[:10]

        if new_nickname:
            self.nickname = new_nickname
            if self.update_nick_callback:
                self.update_nick_callback(new_nickname)

            # Show alert if nickname was truncated
            if was_truncated:
                alert = ctk.CTkToplevel()
                alert.title("Nickname Too Long")
                alert.geometry("300x120")
                alert.resizable(False, False)
                alert.configure(
                    fg_color=COLORS[ctk.get_appearance_mode()]["alert-danger"]
                )

                msg_label = ctk.CTkLabel(
                    alert,
                    text=f"Nickname was truncated to 10 characters:\n\"{new_nickname}\"",
                    wraplength=280
                )
                msg_label.pack(padx=20, pady=15)

                ok_btn = ctk.CTkButton(
                    alert,
                    text="OK",
                    command=alert.destroy
                )
                ok_btn.pack(pady=10)

        # Toggle back to display mode
        self.is_editable = False
        self._create_edit_nick_widget()



class TitleDesc(
    ctk.CTkFrame
    ):

    def __init__(
            self,
            master,
            title,
            description,
            title_size=24,
            desc_size=18,
            **kw
            ):
        super().__init__(
            master,
            fg_color="transparent",
            **kw
            )

        self.top_border = ctk.CTkCanvas(
            self,
            height=1,
            bg=COLORS[ctk.get_appearance_mode()]["border"],
            highlightthickness=0
        )
        self.top_border.pack(
            fill="x"
        )

        content_container = ctk.CTkFrame(
            self,
            fg_color="transparent"
        )
        content_container.pack(
            fill="both",
            expand=True
        )

        title_desc = label_title_vertical(
            content_container,
            title,
            description,
            title_size=title_size,
            muted_size=desc_size,
            is_muted_above=False,
        )
        title_desc.pack(
            expand=True,
            padx=20,
            pady=14
        )

class HoleByHole(
    ctk.CTkFrame
):
    def __init__(
            self,
            master,
            holes,
            bg_color,
            **kw
    ):
        super().__init__(
            master,
            fg_color="transparent",
            **kw
        )

        self.holes = holes
        self.bg_color = bg_color

        self.container = TitleContainer(
            self,
            "Hole by Hole",
            bg_color=bg_color,
            corner_radius=10,
            gap=0
        )
        self.container.pack(
            fill="both",
            expand=True
        )

        self.hole_list = ctk.CTkScrollableFrame(
            self.container.content_host,
            fg_color="transparent"
        )
        self.hole_list.pack(
            fill="both",
            expand=True
        )

        self._populate_holes()

        save_btn = ctk.CTkButton(
            self,
            text="Save Round",
            border_width=0,
            corner_radius=10,
            height=60,
            font=ctk.CTkFont(
                size=24,
                weight="normal"
            ),
            **button_primary()
        )
        save_btn.pack(
            pady=10,
            padx=0,
            fill="x"
        )

        delete_btn = ctk.CTkButton(
            self,
            text="Delete Round",
            border_width=0,
            corner_radius=10,
            height=60,
            font=ctk.CTkFont(
                size=24,
                weight="normal"
            ),
            **button_secondary()
        )
        delete_btn.pack(
            pady=(0, 10),
            padx=0,
            fill="x"
        )

    def _populate_holes(self):
        """Populate or refresh hole statistics rows"""
        # Clear existing holes
        for widget in self.hole_list.winfo_children():
            widget.destroy()

        hole_items = self.holes.values() if isinstance(self.holes, dict) else self.holes
        for hole in hole_items:
            hole_row = hole_statistics(
                self.hole_list,
                hole.get("id", 0),
                hole.get("par", 0),
                hole.get("throws", 0),
                COLORS[ctk.get_appearance_mode()]["bg-light"]
            )
            hole_row.pack(
                fill="x",
                pady=5
            )

    def update_theme_colors(self):
        """Update theme colors for all hole statistics"""
        self.container.configure(fg_color=COLORS[ctk.get_appearance_mode()]["bg"])
        self._populate_holes()  # Recreate all holes with new theme colors
