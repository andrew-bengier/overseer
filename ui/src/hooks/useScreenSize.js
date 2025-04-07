import React from 'react';
import useWindowResize from './useWindowResize';

const DEFAULT_MOBILE_SCREEN_SIZE = 1006;

const useScreenSize = (initialWidth, customSize = DEFAULT_MOBILE_SCREEN_SIZE) => {
    const [isMobile, setIsMobile] = React.useState(initialWidth < customSize);

    const dimensions = useWindowResize();

    React.useEffect(() => {
        if (dimensions.width) {
            setIsMobile(dimensions.width < customSize);
        }
    }, [dimensions.width, customSize]);

    return isMobile;
};

export default useScreenSize;
