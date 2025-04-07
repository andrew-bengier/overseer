import {useLocalizationContext} from "@mui/x-date-pickers/internals";

export const formatCurrentDate = (format) => {
    const {locale} = useLocalizationContext();

    const options = {
        year: format.includes('YYYY') ? 'numeric' : undefined,
        month: format.includes('MMMM') ? 'long' : format.includes('MM') ? '2-digit' : format.includes('M') ? 'numeric' : undefined,
        day: format.includes('DD') ? '2-digit' : format.includes('D') ? 'numeric' : undefined,
        weekday: format.includes('ddd') ? 'long' : format.includes('dd') ? 'short' : format.includes('d') ? 'narrow' : undefined,
        hour: format.includes('HH') ? '2-digit' : format.includes('H') ? 'numeric' : undefined,
        minute: format.includes('mm') ? '2-digit' : undefined,
        second: format.includes('ss') ? '2-digit' : undefined,
        hour12: format.includes('a'),
    };

    return new Intl.DateTimeFormat(locale, options).format(new Date());
};

export const listSplitDateFormats = (formats) => {
    return formats.map(format => {
        return {display: formatCurrentDate(format).replace(',', ''), value: format}
    });
};
