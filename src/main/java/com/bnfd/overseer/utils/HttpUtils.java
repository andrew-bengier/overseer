package com.bnfd.overseer.utils;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    private static final String EQUALS = "=";
    private static final String NOT_EQUALS = "?";
    private static final String ADD = "&";
    private static final String QUERY = "?";

    public static final String URL_SEPARATOR = "/";

    public static StringBuilder generateUrlWithParams(StringBuilder url, Map<String, String> requestParams, List<String> paramsToInvert) throws UnsupportedEncodingException {
        if (MapUtils.isNotEmpty(requestParams)) {
            for (Map.Entry<String, String> param : requestParams.entrySet()) {
                if (StringUtils.isNotBlank(param.getValue())) {
                    String operator = paramsToInvert.contains(param.getKey()) ? NOT_EQUALS : EQUALS;
                    if (url.toString().contains(QUERY)) {
                        url.append(ADD).append(param.getKey()).append(operator).append(param.getValue());
                    } else {
                        url.append(QUERY).append(param.getKey()).append(operator).append(param.getValue());
                    }
                }
            }
        }

        return url;
    }

    public static StringBuilder generateUrl(String... pathPieces) {
        StringBuilder url = new StringBuilder();
        for (String pathPiece : pathPieces) {
            if (StringUtils.isBlank(url.toString()) || url.toString().endsWith(URL_SEPARATOR)) {
                url.append(pathPiece);
            } else {
                url.append(URL_SEPARATOR).append(pathPiece);
            }
        }

        return url;
    }
}
