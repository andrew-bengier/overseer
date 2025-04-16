import React from "react";
import PropTypes from "prop-types";
import {IntlProvider} from "react-intl";

const ContentIntlProvider = ({children}) => {
    const language = navigator.language.split(/[-_]/)[0];
    const [messages, setMessages] = React.useState(null);

    function loadMessages(locale) {
        return import((`../i18n/translations.${locale}.json`));
    }

    React.useEffect(() => {
        loadMessages(language).then(response => setMessages(response));
    }, [language]);

    return messages ? (
        <IntlProvider locale={language} messages={messages}>
            {children}
        </IntlProvider>
    ) : null;
};

ContentIntlProvider.propTypes = {
    children: PropTypes.oneOfType([
        PropTypes.arrayOf(PropTypes.element),
        PropTypes.element
    ]).isRequired
};

export default ContentIntlProvider;
