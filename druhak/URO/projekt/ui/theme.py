import customtkinter as ctk
from enum import Enum

ctk.set_default_color_theme(
    "./theme.json"
)
ctk.set_appearance_mode(
    "Dark"
)  # "Light" or "Dark" or "System"

COLORS = {
    "Light": {
        "bg-dark": "#E5E5E5",
        "bg": "#F5F5F5",
        "bg-light": "#FFFFFF",
        "text": "#262626",
        "text-muted": "#666666",
        "highlight": "#FFFFFF",
        "border": "#999999",
        "border-muted": "#B3B3B3",
        "button-primary": "#3A7F4A",
        "button-primary-hover": "#2E6B3A",
        "button-secondary": "#7F3A6B",
        "button-secondary-hover": "#6B2E5A",
        "alert-danger": "#8C5C4A",
        "alert-warning": "#7A8C4A",
        "alert-success": "#4A8C66",
        "alert-info": "#4A6A8C"
    },
    "Dark": {
        "bg-dark": "#121212",
        "bg": "#1A1A1A",
        "bg-light": "#262626",
        "text": "#F5F5F5",
        "text-muted": "#C2C2C2",
        "highlight": "#FFFFFF",
        "border": "#666666",
        "border-muted": "#4D4D4D",
        "button-primary": "#6FCF8C",
        "button-primary-hover": "#5CB06B",
        "button-secondary": "#CF6FB3",
        "button-secondary-hover": "#B35C9B",
        "alert-danger": "#B0897A",
        "alert-warning": "#A6B07A",
        "alert-success": "#7AB096",
        "alert-info": "#7A95B0"
    }
}

type Button = {
    "text": str,
    "fg_color": str,
    "hover_color": str,
    "border_color": str,
    "text_color": str,
    "border_width": int,
    "corner_radius": int,
    "height": int,
    "width": int
}


def button_primary():
    return {
        "fg_color": COLORS[ctk.get_appearance_mode()]["button-primary"],
        "hover_color": COLORS[ctk.get_appearance_mode()]["button-primary-hover"],
        "border_color": COLORS[ctk.get_appearance_mode()]["button-primary"],
        "text_color": COLORS[ctk.get_appearance_mode()]["highlight"],
    }


def button_secondary():
    return {
        "fg_color": COLORS[ctk.get_appearance_mode()]["bg-light"],
        "hover_color": COLORS[ctk.get_appearance_mode()]["bg-dark"],
        "border_color": COLORS[ctk.get_appearance_mode()]["border"],
        "text_color": COLORS[ctk.get_appearance_mode()]["text"],
    }


def button_neutral():
    return {
        "fg_color": COLORS[ctk.get_appearance_mode()]["bg-light"],
        "hover_color": COLORS[ctk.get_appearance_mode()]["bg-dark"],
        "border_color": COLORS[ctk.get_appearance_mode()]["button-primary"],
        "text_color": COLORS[ctk.get_appearance_mode()]["button-primary"],
    }


def button_disabled():
    return {
        "fg_color": COLORS[ctk.get_appearance_mode()]["bg-dark"],
        "hover_color": COLORS[ctk.get_appearance_mode()]["bg-dark"],
        "border_color": COLORS[ctk.get_appearance_mode()]["border-muted"],
        "text_color": COLORS[ctk.get_appearance_mode()]["text-muted"],
    }


class Units(Enum):
    Metric = "Meters"
    Imperial = "Feet"


def apply_opacity(color: str, opacity: float) -> str:
    """
    Apply opacity to a hex color.
    In Dark mode: darkens the color (multiplies by opacity)
    In Light mode: lightens the color (interpolates toward white)
    """
    hex_color = color.lstrip("#")
    r = int(hex_color[0:2], 16)
    g = int(hex_color[2:4], 16)
    b = int(hex_color[4:6], 16)

    if ctk.get_appearance_mode() == "Dark":
        # Darken: multiply each component by opacity
        r = max(0, min(255, int(r * opacity)))
        g = max(0, min(255, int(g * opacity)))
        b = max(0, min(255, int(b * opacity)))
    else:
        # Lighten: interpolate toward white
        # opacity of 0.4 means 40% of original color + 60% white
        r = max(0, min(255, int(r + (255 - r) * (1 - opacity))))
        g = max(0, min(255, int(g + (255 - g) * (1 - opacity))))
        b = max(0, min(255, int(b + (255 - b) * (1 - opacity))))

    return f"#{r:02x}{g:02x}{b:02x}"
