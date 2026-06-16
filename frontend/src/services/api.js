class ApiError extends Error {
  constructor(message, response) {
    super(message);
    this.name = 'ApiError';
    this.response = response;
  }
}

const BASE_URL = 'http://localhost:8080';

const request = async (url, options = {}) => {
  const token = localStorage.getItem('token');
  
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };
  
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  
  const response = await fetch(`${BASE_URL}${url}`, {
    ...options,
    headers,
  });
  
  if (!response.ok) {
    let errorData = null;
    try {
      errorData = await response.json();
    } catch (e) {
      // Not a JSON response
    }
    throw new ApiError(
      errorData?.message || `Request failed with status ${response.status}`,
      {
        data: errorData,
        status: response.status,
      }
    );
  }
  
  if (response.status === 204) {
    return { data: null };
  }
  
  const json = await response.json();
  return { data: json };
};

// Projects API endpoints
export const projectApi = {
  getProjects: () => request('/projects'),
  createProject: (projectData) => request('/projects', { method: 'POST', body: JSON.stringify(projectData) }),
  deleteProject: (id) => request(`/projects/${id}`, { method: 'DELETE' }),
  getMembers: (projectId) => request(`/projects/${projectId}/members`),
  addMember: (projectId, userId) => request(`/projects/${projectId}/members/${userId}`, { method: 'POST' }),
  removeMember: (projectId, userId) => request(`/projects/${projectId}/members/${userId}`, { method: 'DELETE' }),
};

// Tasks API endpoints
export const taskApi = {
  getTasks: () => request('/tasks'),
  createTask: (taskData) => request('/tasks', { method: 'POST', body: JSON.stringify(taskData) }),
  updateTask: (id, taskData) => request(`/tasks/${id}`, { method: 'PUT', body: JSON.stringify(taskData) }),
  deleteTask: (id) => request(`/tasks/${id}`, { method: 'DELETE' }),
  assignTask: (taskId, userId) => request(`/tasks/${taskId}/assign/${userId}`, { method: 'POST' }),
  unassignTask: (taskId) => request(`/tasks/${taskId}/unassign`, { method: 'POST' }),
  getMyTasks: () => request('/tasks/my-tasks'),
};

// Dashboard API endpoints
export const dashboardApi = {
  getDashboardStats: () => request('/dashboard'),
};

// Auth API endpoints
export const authApi = {
  login: (credentials) => request('/auth/login', { method: 'POST', body: JSON.stringify(credentials) }),
  register: (userData) => request('/auth/register', { method: 'POST', body: JSON.stringify(userData) }),
};

// Users API endpoints
export const userApi = {
  getUsers: () => request('/users'),
  deleteUser: (id) => request(`/users/${id}`, { method: 'DELETE' }),
};

// Centralized fetch client matching Axios interface
const apiClient = {
  get: (url) => request(url),
  post: (url, data) => request(url, { method: 'POST', body: JSON.stringify(data) }),
  put: (url, data) => request(url, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (url) => request(url, { method: 'DELETE' }),
};

export default apiClient;
