package http.handlers.endpoints;

public enum GetEndpoints implements Endpoint {
    GET_TASKS("/tasks"),
    GET_TASKS_BY_ID("/tasks/{id}"),
    GET_SUBTASKS("/subtasks"),
    GET_SUBTASKS_BY_ID("/subtasks/{id}"),
    GET_EPIC("/epics"),
    GET_EPIC_BY_ID("/epics/{id}"),
    GET_EPIC_SUBTASKS("/epics/{id}/subtasks"),
    GET_HISTORY("/history"),
    GET_PRIORITIZED_TASKS("/prioritized"),
    UNKNOWN("");

    private String s;

    GetEndpoints(String s) {
        this.s = s;
    }

    public static GetEndpoints endpointFromPatch(String path) {
        System.out.println(path);
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        for (GetEndpoints endpoint : GetEndpoints.values()) {
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