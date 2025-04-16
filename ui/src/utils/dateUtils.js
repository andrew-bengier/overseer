import {useLocalizationContext} from "@mui/x-date-pickers/internals";
import {format, parseISO} from "date-fns";

export const formatUptime = (milliseconds) => {
    const seconds = Math.floor((milliseconds / 1000) % 60);
    const minutes = Math.floor((milliseconds / (1000 * 60)) % 60);
    const hours = Math.floor((milliseconds / (1000 * 60 * 60)) % 24);
    const days = Math.floor(milliseconds / (1000 * 60 * 60 * 24));

    let formatted = `${seconds}s`;
    if (minutes > 0) {
        formatted = `${minutes}m ${seconds}s`;
    }
    if (hours > 0) {
        formatted = `${hours}h ${minutes}m ${seconds}s`;
    }
    if (days > 0) {
        formatted = `${days}d ${hours}h ${minutes}m ${seconds}s`;
    }

    return formatted
};

export const formatDate = (requestedFormat, date, override) => {
    const {locale} = useLocalizationContext();

    const options = {
        year: (requestedFormat.includes('YYYY') || requestedFormat.includes('yyyy')) ? 'numeric' : undefined,
        month: requestedFormat.includes('MMMM') ? 'long' : requestedFormat.includes('MM') ? '2-digit' : requestedFormat.includes('M') ? 'numeric' : undefined,
        day: requestedFormat.includes('DD') ? '2-digit' : requestedFormat.includes('D') ? 'numeric' : undefined,
        weekday: requestedFormat.includes('ddd') ? 'long' : requestedFormat.includes('dd') ? 'short' : requestedFormat.includes('d') ? 'narrow' : undefined,
        hour: requestedFormat.includes('HH') ? '2-digit' : requestedFormat.includes('H') ? 'numeric' : undefined,
        minute: requestedFormat.includes('mm') ? '2-digit' : undefined,
        second: requestedFormat.includes('ss') ? '2-digit' : undefined,
        hour12: requestedFormat.includes('a'),
    };

    const referenceDate = (date == null) ? new Date() : (date instanceof String) ? parseISO(date) : date;

    if (override) {
        return format(referenceDate, requestedFormat);
    } else {
        return new Intl.DateTimeFormat(locale, options).format(referenceDate);
    }
};

export const listSplitDateFormats = (formats) => {
    return formats.map(format => {
        return {display: formatDate(format, null, false).replace(',', ''), value: format}
    });
};
