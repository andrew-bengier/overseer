export default function colorForGitReleaseTag(tag) {
    switch (tag) {
        case "draft":
            return "warning";
        case "prerelease":
            return "success";
        default:
            return "primary";
    }
}