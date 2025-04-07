import React from 'react';

const useWindowResize = () => {
    const [dimensions, setDimensions] = React.useState(() => {
        if (typeof window !== 'undefined') {
            return {height: window.innerHeight, width: window.innerWidth};
        } else {
            return {height: null, width: null};
        }
    });

    const handleWindowResize = React.useCallback(() => {
        setDimensions({height: window.innerHeight, width: window.innerWidth});
    }, []);

    React.useEffect(() => {
        window.addEventListener('resize', handleWindowResize);
        return () => {
            window.removeEventListener('resize', handleWindowResize);
        };
    }, []);

    return dimensions;
};

export default useWindowResize;
