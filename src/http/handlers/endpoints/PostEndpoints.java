package http.handlers.endpoints;

public enum PostEndpoints implements Endpoint {
    ADD_TASK("/tasks"),
    ADD_EPIC("/epics"),
    ADD_SUBTASK("/subtasks"),
    UNKNOWN("");

    private String s;

    PostEndpoints(String s) {
        this.s = s;
    }

    public static PostEndpoints endpointFromPatch(String path) {
        System.out.println(path);
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        for (PostEndpoints endpoint : PostEndpoints.values()) {
            if (path.matches(endpoint.getPath().replace("{id}", "\\d+"))) {
                System.out.println("Возвращен эндпойт " + endpoint);
                return endpoint;
            }
        }
        System.out.println("Совпадений не найдено");
        return UNKNOWN;
    }

    private String getPath() {
        return s;
    }
}
