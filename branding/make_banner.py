"""ThaumJS's CurseForge banner and icon in the Thaumaturge addons' family look (_toolkit/logokit/thaumfamily.py).
ThaumJS has no textures of its own, so the relief ramp is a violet vis ramp set here and the icon's subject is a
16x16 script scroll painted below.
Run from anywhere: python branding/make_banner.py  -> branding/banner_*.png, branding/icon_*.png"""
import os
import sys

sys.path.insert(0, "H:/AI_MC_Modding/_toolkit/logokit")
from PIL import Image  # noqa: E402
from thaumfamily import banner, icon, steps, save_all  # noqa: E402

HERE = os.path.dirname(os.path.abspath(__file__))
KEYS = [(6, 3, 14), (20, 10, 40), (44, 22, 84), (74, 40, 132), (110, 66, 186), (150, 100, 232), (196, 150, 255),
        (236, 214, 255)]
COLS = steps(KEYS, 13)

SCROLL = [
    "................",
    "..oooooooooooo..",
    ".oPPpppppppppmo.",
    ".odmmmmmmmmmmdo.",
    "..oooooooooooo..",
    "...oPpppppppmo..",
    "...oPVvvpvVvmo..",
    "...oPpppppppmo..",
    "...oPvVvpVvpmo..",
    "...oPpppppppmo..",
    "...oPVvpvvVvmo..",
    "...oPpppppppmo..",
    "..oooooooooooo..",
    ".oPPpppppppppmo.",
    ".odmmmmmmmmmmdo.",
    "..oooooooooooo..",
]
PAL = {"o": (58, 34, 22), "P": (248, 232, 196), "p": (232, 210, 162), "m": (198, 170, 120), "d": (150, 118, 78),
       "v": (128, 78, 214), "V": (186, 140, 255)}


def scroll_png(path):
    im = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    for y, row in enumerate(SCROLL):
        assert len(row) == 16, (y, row)
        for x, ch in enumerate(row):
            if ch in PAL:
                im.putpixel((x, y), PAL[ch] + (255,))
    im.save(path)
    return path


if __name__ == "__main__":
    sprite = scroll_png(os.path.join(HERE, "scroll_16.png"))
    light = tuple(min(255, int(v * 1.2) + 30) for v in COLS[9])
    save_all(banner("ThaumJS", COLS, 11), icon(sprite, COLS, light), HERE)
