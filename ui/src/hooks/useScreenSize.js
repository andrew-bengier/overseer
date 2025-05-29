import React from 'react';
import useWindowResize from './useWindowResize';

const DEFAULT_MOBILE_SCREEN_SIZE = 900;

const useScreenSize = (initialWidth, customSize = DEFAULT_MOBILE_SCREEN_SIZE) => {
    const [isMobile, setIsMobile] = React.useState(initialWidth ? initialWidth < customSize : false);

    const dimensions = useWindowResize();

    React.useEffect(() => {
        // console.log('changing size: ' + dimensions.width);
        // console.log('mobile view current[' + isMobile + '] update [' + (dimensions.width < customSize) + ']');
        if (dimensions.width != null) {
            setIsMobile(dimensions.width < customSize);
        }
    }, [dimensions, customSize]);

    return isMobile;
};

export default useScreenSize;
