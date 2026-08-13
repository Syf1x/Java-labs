const BASE_URL = "http://localhost:8080/api/v1";

async function request(path, options = {}) {
  const response = await fetch(`${BASE_URL}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });

  if (!response.ok) {
    let message = `HTTP ${response.status}`;
    try {
      const body = await response.json();
      message = body.message || message;
    } catch {
      // response has no JSON body
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return null;
  }
  const text = await response.text();
  return text ? JSON.parse(text) : null;
}

export const employeesApi = {
  list: (params) => request(`/employees?${new URLSearchParams(params)}`),
  create: (dto) => request("/employees", { method: "POST", body: JSON.stringify(dto) }),
  update: (id, dto) => request(`/employees/${id}`, { method: "PUT", body: JSON.stringify(dto) }),
  remove: (id) => request(`/employees/${id}`, { method: "DELETE" }),
  assignTask: (id, taskId) => request(`/employees/${id}/tasks/${taskId}`, { method: "POST" }),
  unassignTask: (id, taskId) => request(`/employees/${id}/tasks/${taskId}`, { method: "DELETE" }),
};

export const departmentsApi = {
  list: () => request("/departments"),
  create: (dto) => request("/departments", { method: "POST", body: JSON.stringify(dto) }),
  update: (id, dto) => request(`/departments/${id}`, { method: "PUT", body: JSON.stringify(dto) }),
  remove: (id) => request(`/departments/${id}`, { method: "DELETE" }),
};

export const projectsApi = {
  list: () => request("/projects"),
  get: (id) => request(`/projects/${id}`),
  create: (dto) => request("/projects", { method: "POST", body: JSON.stringify(dto) }),
  update: (id, dto) => request(`/projects/${id}`, { method: "PUT", body: JSON.stringify(dto) }),
  remove: (id) => request(`/projects/${id}`, { method: "DELETE" }),
};

export const tasksApi = {
  list: () => request("/tasks"),
  create: (dto) => request("/tasks", { method: "POST", body: JSON.stringify(dto) }),
  update: (id, dto) => request(`/tasks/${id}`, { method: "PUT", body: JSON.stringify(dto) }),
  remove: (id) => request(`/tasks/${id}`, { method: "DELETE" }),
};
