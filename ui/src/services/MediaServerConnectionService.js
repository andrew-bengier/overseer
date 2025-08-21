import {PlexOauth} from "plex-oauth"

let clientInformation = {
    clientIdentifier: "0198be0a-5736-730f-a6cc-d172fba3f663", // This is a unique identifier used to identify your app with Plex.
    product: "overseer",              // Name of your application
    device: "web",            // The type of device your application is running on
    version: "1.0",                               // Version of your application
    forwardUrl: "http://localhost:8080/Settings/Connections",       // Optional - Url to forward back to after signing in.
    platform: "Web",                            // Optional - Platform your application runs on - Defaults to 'Web'
    urlencode: true                             // Optional - If set to true, the output URL is url encoded, otherwise if not specified or 'false', the output URL will return as-is
}

const plexOauth = new PlexOauth(clientInformation);

export async function plexLogin(auth, newWindow = false) {
    try {
        return plexOauth.requestHostedLoginURL().then(data => {
            let [hostedUILink, pinId] = data;
            const url = new URL(hostedUILink);
            url.searchParams.append("plexPin", pinId.toString());
            url.searchParams.append("plexName", auth.name);
            url.searchParams.append("plexUrl", auth.url);
            let link = url.toString() + '?' + url.searchParams.toString();
            clientInformation.forwardUrl = link;

            // [TEST]
            console.log(link);
            if (newWindow) {
                // console.log('Plex login url:', hostedUILink);
                // const width = 500;
                // const height = 600;
                // const left = window.screenX + (window.outerWidth - width) / 2;
                // const top = window.screenY + (window.outerHeight - height) / 2.5;
                // const title = `Plex Login`;
                // return window.open(hostedUILink, title, `width=${width},height=${height},left=${left},top=${top}`);
                return link;
            } else {
                // window.location.href = link;
            }
        });
    } catch (error) {
        throw new Error(`Error initiating Plex login: ${error}`);
    }
}

export const handlePlexCallback = async (pinId) => {
    try {
        return await plexOauth.checkForAuthToken(pinId);
    } catch (error) {
        throw new Error(`Error getting Plex auth token: ${error}`);
    }
}

