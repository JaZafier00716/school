from ui.theme import *


def icon_button(
        parent,
        text: str,
        is_muted: bool = False,
        font_size: int = 24,
        size: int = 32,
        radius: int = 10,
        opacity: float = 0.8,
        fg_color: str = "transparent",
        text_color: str = "",
        hover_color: str = "",
        command: callable = None
):
    if text_color == "":
        text_color = COLORS[ctk.get_appearance_mode()]["text-muted"] if is_muted else COLORS[ctk.get_appearance_mode()][
            "text"]

    if hover_color == "" and fg_color != "transparent":
        # Darken the color by applying opacity multiplier
        hover_color = apply_opacity(fg_color, opacity)
    elif hover_color == "":
        hover_color = COLORS[ctk.get_appearance_mode()]["bg-light"]

    return ctk.CTkButton(
        parent,
        font=ctk.CTkFont(
            size=font_size,
            weight="bold"
        ),
        text=text,
        width=size,
        height=size,
        fg_color=fg_color,
        text_color=text_color,
        hover_color=hover_color,
        corner_radius=radius,
        command=command
    )


def label_title_vertical(
        parent,
        title: str,
        muted: str,
        title_size: int = 24,
        muted_size: int = 16,
        is_muted_above: bool = True,
        anchor: str = ""
):
    wrap = ctk.CTkFrame(
        parent,
        fg_color="transparent"
    )

    if is_muted_above:
        muted_lbl = ctk.CTkLabel(
            wrap,
            text=muted,
            text_color=COLORS[ctk.get_appearance_mode()]["text-muted"],
            font=ctk.CTkFont(
                size=muted_size,
                weight="normal"
            ),
        )
        muted_lbl.pack(
            anchor=f"n{anchor}"
        )
        title_lbl = ctk.CTkLabel(
            wrap,
            text=title,
            font=ctk.CTkFont(
                size=title_size,
                weight="bold"
            )
        )
        title_lbl.pack(
            anchor=f"s{anchor}"
        )
    else:
        title_lbl = ctk.CTkLabel(
            wrap,
            text=title,
            font=ctk.CTkFont(
                size=title_size,
                weight="bold"
            )
        )
        title_lbl.pack(
            anchor=f"n{anchor}"
        )
        muted_lbl = ctk.CTkLabel(
            wrap,
            text=muted,
            text_color=COLORS[ctk.get_appearance_mode()]["text-muted"],
            font=ctk.CTkFont(
                size=muted_size,
                weight="normal"
            ),
        )
        muted_lbl.pack(
            anchor=f"s{anchor}"
        )

    return wrap


def buttons_horizontal(
        parent,
        button_left: dict,
        button_right: dict,
        spacing: int = 20,
        pady: int = 10
):
    button_row = ctk.CTkFrame(
        parent,
        fg_color="transparent"
    )

    button_row.grid_columnconfigure(
        0,
        weight=1
    )
    button_row.grid_columnconfigure(
        1,
        weight=1
    )

    button_left = ctk.CTkButton(
        button_row,
        text=button_left.get(
            "text",
            ""
        ),
        fg_color=button_left.get(
            "fg_color",
            "transparent"
        ),
        hover_color=button_left.get(
            "hover_color",
            "gray"
        ),
        border_color=button_left.get(
            "border_color",
            "transparent"
        ),
        text_color=button_left.get(
            "text_color",
            "white"
        ),
        border_width=button_left.get(
            "border_width",
            0
        ),
        corner_radius=button_left.get(
            "corner_radius",
            10
        ),
        height=button_left.get(
            "height",
            40
        ),
        width=button_left.get(
            "width",
            100
        )
    )
    button_left.grid(
        row=0,
        column=0,
        sticky="w",
        padx=(0, spacing / 2),
        pady=pady
    )

    button_right = ctk.CTkButton(
        button_row,
        text=button_right.get(
            "text",
            ""
        ),
        fg_color=button_right.get(
            "fg_color",
            "transparent"
        ),
        hover_color=button_right.get(
            "hover_color",
            "gray"
        ),
        border_color=button_right.get(
            "border_color",
            "transparent"
        ),
        text_color=button_right.get(
            "text_color",
            "white"
        ),
        border_width=button_right.get(
            "border_width",
            0
        ),
        corner_radius=button_right.get(
            "corner_radius",
            10
        ),
        height=button_right.get(
            "height",
            40
        ),
        width=button_right.get(
            "width",
            100
        )
    )
    button_right.grid(
        row=0,
        column=1,
        sticky="e",
        padx=(spacing / 2, 0),
        pady=pady
    )

    return button_row


class TitleContainer(
    ctk.CTkFrame
):
    def __init__(
            self,
            master,
            title_text,
            bg_color=None,
            gap=20,
            corner_radius=10,
            **kw
    ):
        super().__init__(
            master,
            fg_color=bg_color,
            corner_radius=corner_radius,
            **kw
        )

        self.gap = gap
        self.grid_columnconfigure(
            0,
            weight=1
        )
        self.grid_rowconfigure(
            1,
            weight=1
        )

        self.title = ctk.CTkLabel(
            self,
            text=title_text,
            font=ctk.CTkFont(
                size=20,
                weight="bold"
            )
        )
        self.title.grid(
            row=0,
            column=0,
            sticky="w",
            padx=20,
            pady=(20, 0)
        )

        # content host keeps spacing/padding logic in one place
        self.content_host = ctk.CTkFrame(
            self,
            fg_color="transparent"
        )
        self.content_host.grid(
            row=1,
            column=0,
            sticky="nsew",
            padx=20,
            pady=(self.gap, 20)
        )
        self.content_host.grid_columnconfigure(
            0,
            weight=1
        )
        self.content_host.grid_rowconfigure(
            0,
            weight=1
        )

    def add_content(
            self,
            widget
    ):
        # place any CTk widget/component under the title
        widget.grid(
            in_=self.content_host,
            row=0,
            column=0,
            sticky="nsew"
        )
        return widget


def title_icon_editable(
        parent,
        title: str,
        icon_text: str,
        title_size: int = 24,
        icon_size: int = 32,
        icon_font_size: int = 24,
        spacing: int = 10,
        is_icon_left: bool = False,
        is_editable: bool = False,
        edit_text: str = "",
        on_save: callable = None
):
    if is_editable:
        wrap = ctk.CTkFrame(
            parent,
            fg_color="transparent"
        )
        wrap.grid_columnconfigure(0, weight=1)
        wrap.grid_columnconfigure(1, weight=0)

        entry = ctk.CTkEntry(
            wrap,
            font=ctk.CTkFont(
                size=24,
                weight="bold"
            ),
            placeholder_text=edit_text,
            height=50
        )
        # Limit to 10 characters
        entry.insert(0, title[:10] if len(title) > 10 else title)
        entry.grid(
            row=0,
            column=0,
            sticky="ew",
            padx=(0, spacing)
        )

        save_btn = icon_button(
            wrap,
            "✓",
            font_size=icon_font_size,
            size=icon_size,
            is_muted=False
        )
        save_btn.grid(
            row=0,
            column=1,
            sticky="e"
        )

        if on_save:
            save_btn.configure(
                command=lambda: on_save(entry.get())
            )

        return wrap

    wrap = ctk.CTkFrame(
        parent,
        fg_color="transparent"
    )

    if is_icon_left:
        wrap.grid_columnconfigure(
            0,
            weight=0
        )
        wrap.grid_columnconfigure(
            1,
            weight=1
        )
    else:
        wrap.grid_columnconfigure(
            0,
            weight=1
        )
        wrap.grid_columnconfigure(
            1,
            weight=0
        )

    icon = icon_button(
        wrap,
        text=icon_text,
        font_size=icon_font_size,
        size=icon_size,
        is_muted=True
    )
    icon.grid(
        row=0,
        column=0 if is_icon_left else 1,
        sticky="w" if is_icon_left else "e",
        padx=(0, spacing/2) if is_icon_left else (spacing/2, 0)
    )

    title = ctk.CTkLabel(
        wrap,
        text=title,
        font=ctk.CTkFont(
            size=title_size,
            weight="bold"
        )
    )
    title.grid(
        row=0,
        column=1 if is_icon_left else 0,
        sticky="e" if is_icon_left else "w",
        padx=(spacing/2, 0) if is_icon_left else (0, spacing/2)
    )

    return wrap

def hole_statistics(
        parent,
        hole_number: int,
        par: int,
        throws: int,
        bg_color: str = None
):
    wrap = ctk.CTkFrame(
        parent,
        fg_color= bg_color if bg_color else "transparent"
    )
    wrap.grid_columnconfigure(0, weight=0)
    wrap.grid_columnconfigure(1, weight=1)
    wrap.grid_columnconfigure(2, weight=0)

    hole_number_circle = ctk.CTkFrame(
        wrap,
        fg_color=apply_opacity(COLORS[ctk.get_appearance_mode()]["button-primary"], 0.4),
        corner_radius=20,
        width=40,
        height=40
    )
    hole_number_circle.grid(
        row=0,
        column=0,
        sticky="w",
        padx=(10, 10),
        pady=10
    )
    hole_number_circle.grid_propagate(False)

    hole_lbl = ctk.CTkLabel(
        hole_number_circle,
        text=str(hole_number),
        font=ctk.CTkFont(
            size=24,
            weight="bold"
        ),
        text_color=COLORS[ctk.get_appearance_mode()]["button-primary"]
    )
    hole_lbl.place(relx=0.5, rely=0.5, anchor="center")

    par_lbl = ctk.CTkLabel(
        wrap,
        text=f"Par: {par}",
        font=ctk.CTkFont(
            size=16,
            weight="normal"
        ),
    )
    par_lbl.grid(
        row=0,
        column=1,
        sticky="w",
        pady=10
    )

    score = throws - par
    text_color = COLORS[ctk.get_appearance_mode()]["text-muted"]
    if score > 0:
        text_color = COLORS[ctk.get_appearance_mode()]["alert-warning"]
    if score < 0:
        text_color = COLORS[ctk.get_appearance_mode()]["alert-info"]

    score_lbl = ctk.CTkLabel(
        wrap,
        text=f"Score: {"+" if score > 0 else ""}{score}",
        font=ctk.CTkFont(
            size=16,
            weight="normal"
        ),
        text_color=text_color
    )
    score_lbl.grid(
        row=0,
        column=1,
        sticky="e",
        padx=(10, 10),
    )

    return wrap



