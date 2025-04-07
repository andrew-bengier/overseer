import LightTheme from "./LightTheme";
import DarkTheme from "./DarkTheme";

const themes = [
    LightTheme,
    DarkTheme
];

export default function ThemeSwitcher(darkMode) {
    if (darkMode) {
        document.body.style.backgroundColor = DarkTheme.palette.background;
    } else {
        document.body.style.backgroundColor = LightTheme.palette.background;
    }

    return themes[darkMode ? 1 : 0];
}
