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
            bg_color=None,
            **kw
            ):
        super().__init__(
            master,
            fg_color=bg_color,
            **kw
            )

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
            fg_color="transparent"
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
            fg_color=COLORS[ctk.get_appearance_mode()]["border"],
            hover_color=COLORS[ctk.get_appearance_mode()]["border-muted"],
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
            hover_color=COLORS[ctk.get_appearance_mode()]["button-primary-hover"]
        )
        self.plus_btn.pack(
            side="left",
            padx=10
            )


class ButtonGrid(
    ctk.CTkFrame
    ):

    def __init__(
            self,
            master,
            bg_color=None,
            **kw
            ):
        super().__init__(
            master,
            fg_color=bg_color,
            **kw
            )

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
            **button_neutral()
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
            **button_neutral()
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
            bg_color=None,
            **kw
            ):
        super().__init__(
            master,
            fg_color="transparent",
            **kw
            )

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
            **button_neutral()
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
            **button_secondary()
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
            bg_color=None,
            **kw
            ):
        super().__init__(
            master,
            fg_color=bg_color,
            **kw
            )

        edit_nick = title_icon_editable(
            self,
            title=nickname,
            icon_text="🖉",
            title_size=44,
            icon_size=32
        )
        edit_nick.pack(
            fill="x",
            pady=(14, 0),
            padx=(20, 15)
        )

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