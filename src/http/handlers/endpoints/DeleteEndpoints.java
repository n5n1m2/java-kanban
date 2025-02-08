package http.handlers.endpoints;

public enum DeleteEndpoints implements Endpoint {
    DELETE_SUBTASK_BY_ID("/subtasks/{id}"),
    DELETE_TASK_BY_ID("/tasks/{id}"),
    DELETE_EPIC_BY_ID("/epics/{id}"),
    UNKNOWN("");

    private String s;

    DeleteEndpoints(String s) {
        this.s = s;
    }

    public static DeleteEndpoints endpointFromPatch(String path) {
        System.out.println(path);
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        for (DeleteEndpoints endpoint : DeleteEndpoints.values()) {
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
