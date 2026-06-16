import React, { useState, useEffect, useContext } from 'react';
import { taskApi, projectApi } from '../services/api';
import { AuthContext } from '../context/AuthContext';

const Tasks = () => {
  const { user } = useContext(AuthContext);
  const [tasks, setTasks] = useState([]);
  const [projects, setProjects] = useState([]);
  const [title, setTitle] = useState('');
  const [status, setStatus] = useState('PENDING');
  const [projectId, setProjectId] = useState('');
  const [assignedToId, setAssignedToId] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Track project members to allow assignee options
  const [projectMembersMap, setProjectMembersMap] = useState({});

  const fetchProjectMembersMap = async (projectsList) => {
    const membersMap = {};
    for (const p of projectsList) {
      try {
        const res = await projectApi.getMembers(p.id);
        membersMap[p.id] = res.data;
      } catch (err) {
        console.error('Error fetching members for project:', p.id, err);
      }
    }
    return membersMap;
  };

  const fetchData = () => {
    setLoading(true);
    Promise.all([
      taskApi.getTasks(),
      projectApi.getProjects()
    ])
      .then(async ([tasksRes, projectsRes]) => {
        setTasks(tasksRes.data);
        setProjects(projectsRes.data);
        
        let initialProjId = '';
        if (projectsRes.data.length > 0) {
          initialProjId = projectsRes.data[0].id.toString();
          if (!projectId) {
            setProjectId(initialProjId);
          }
        }
        
        const membersMap = await fetchProjectMembersMap(projectsRes.data);
        setProjectMembersMap(membersMap);
        
        setLoading(false);
      })
      .catch(err => {
        console.error('Error fetching data:', err);
        setError('Failed to fetch task workspace data. Make sure Spring Boot is active.');
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchData();
  }, []);

  // Sync assignedToId option when project selection changes in Create Task form
  useEffect(() => {
    setAssignedToId('');
  }, [projectId]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!title || !projectId) return;

    const newTask = {
      title,
      status,
      projectId: parseInt(projectId, 10),
      assignedToId: assignedToId ? parseInt(assignedToId, 10) : null,
      dueDate: dueDate || null
    };

    taskApi.createTask(newTask)
      .then(() => {
        setTitle('');
        setStatus('PENDING');
        setAssignedToId('');
        setDueDate('');
        fetchData();
      })
      .catch(err => {
        console.error('Error creating task:', err);
        alert('Failed to create task: ' + (err.response?.data?.message || err.message));
      });
  };

  const handleStatusChange = (task, newStatus) => {
    const updatedTask = {
      title: task.title,
      status: newStatus,
      projectId: task.projectId,
      assignedToId: task.assignedToId,
      dueDate: task.dueDate
    };

    taskApi.updateTask(task.id, updatedTask)
      .then(() => {
        fetchData();
      })
      .catch(err => {
        console.error('Error updating task status:', err);
        alert('Failed to update status: ' + (err.response?.data?.message || err.message));
      });
  };

  const handleAssigneeChange = (taskId, newAssigneeId) => {
    if (newAssigneeId === '') {
      taskApi.unassignTask(taskId)
        .then(() => fetchData())
        .catch(err => {
          console.error('Error unassigning task:', err);
          alert('Failed to unassign task: ' + (err.response?.data?.message || err.message));
        });
    } else {
      taskApi.assignTask(taskId, parseInt(newAssigneeId, 10))
        .then(() => fetchData())
        .catch(err => {
          console.error('Error assigning task:', err);
          alert('Failed to assign task: ' + (err.response?.data?.message || err.message));
        });
    }
  };

  const handleDelete = (id) => {
    if (!window.confirm('Are you sure you want to delete this task?')) return;

    taskApi.deleteTask(id)
      .then(() => {
        fetchData();
      })
      .catch(err => {
        console.error('Error deleting task:', err);
        alert('Failed to delete task.');
      });
  };

  const getTaskPriority = (id) => {
    if (!id) return { label: 'LOW', className: 'priority-low' };
    if (id % 3 === 0) return { label: 'HIGH', className: 'priority-high' };
    if (id % 2 === 0) return { label: 'MEDIUM', className: 'priority-medium' };
    return { label: 'LOW', className: 'priority-low' };
  };

  // Group tasks by statuses
  const pendingTasks = tasks.filter(t => t.status === 'PENDING');
  const inProgressTasks = tasks.filter(t => t.status === 'IN_PROGRESS');
  const completedTasks = tasks.filter(t => t.status === 'COMPLETED');

  const activeProjectMembers = projectMembersMap[projectId] || [];

  const renderTaskCard = (task) => {
    const project = projects.find(p => p.id === task.projectId);
    const priority = getTaskPriority(task.id);
    const membersOfTaskProject = projectMembersMap[task.projectId] || [];
    
    const isProjectOwner = project && user && project.ownerId === user.id;
    const isAssignee = user && task.assignedToId === user.id;
    const isCreator = user && task.createdById === user.id;
    const canUpdateStatus = isProjectOwner || isAssignee || isCreator;
    const canDelete = isProjectOwner;

    const assignedName = task.assignedToName || 'Unassigned';
    const initials = task.assignedToName 
      ? task.assignedToName.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase() 
      : '??';

    return (
      <div className="kanban-card" key={task.id}>
        <div className="kanban-card-title" style={task.status === 'COMPLETED' ? { textDecoration: 'line-through', color: 'var(--text-muted)' } : {}}>{task.title}</div>
        <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', marginBottom: '8px' }}>
          📁 {project ? project.name : `Project ID: ${task.projectId}`}
        </div>
        <div style={{ display: 'flex', gap: '6px', alignItems: 'center', flexWrap: 'wrap', marginBottom: '8px' }}>
          <span className={`badge ${priority.className}`}>
            {priority.label}
          </span>
          <span style={{ fontSize: '0.6875rem', color: 'var(--text-muted)' }}>
            by {task.createdByName || 'Unknown'}
          </span>
        </div>
        {task.dueDate && (
          <div style={{ 
            fontSize: '0.725rem', 
            color: task.status !== 'COMPLETED' && new Date(task.dueDate) < new Date(new Date().setHours(0,0,0,0)) 
              ? 'var(--danger)' 
              : 'var(--text-secondary)',
            fontWeight: task.status !== 'COMPLETED' && new Date(task.dueDate) < new Date(new Date().setHours(0,0,0,0)) 
              ? '600' 
              : 'normal',
            marginBottom: '8px',
            display: 'flex',
            alignItems: 'center',
            gap: '4px'
          }}>
            📅 Due: {task.dueDate} 
            {task.status !== 'COMPLETED' && new Date(task.dueDate) < new Date(new Date().setHours(0,0,0,0)) && (
              <span className="badge priority-high" style={{ padding: '1px 4px', fontSize: '0.6rem', marginLeft: '4px' }}>OVERDUE</span>
            )}
          </div>
        )}

        {/* Assignee Selection */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '6px', margin: '12px 0 8px 0', borderTop: '1px solid #f1f5f9', paddingTop: '8px' }}>
          <span style={{
            width: '22px', height: '22px', borderRadius: '50%', fontSize: '0.6rem',
            backgroundColor: task.assignedToId ? 'var(--accent-light)' : 'var(--bg-secondary)',
            color: task.assignedToId ? 'var(--accent)' : 'var(--text-muted)',
            border: '1px solid var(--border-color)', display: 'inline-flex',
            alignItems: 'center', justifyContent: 'center', fontWeight: 600
          }} title={assignedName}>
            {task.assignedToId ? initials : '👤'}
          </span>
          <select
            style={{ border: 'none', background: 'none', color: 'var(--text-secondary)', fontSize: '0.75rem', padding: 0, cursor: 'pointer', outline: 'none', width: '100%' }}
            value={task.assignedToId || ''}
            onChange={e => handleAssigneeChange(task.id, e.target.value)}
          >
            <option value="">Assignee: None</option>
            {membersOfTaskProject.map(m => (
              <option key={m.id} value={m.id}>{m.name}</option>
            ))}
          </select>
        </div>

        <div className="kanban-card-footer">
          <select
            className="form-control"
            style={{ padding: '2px 4px', fontSize: '0.75rem', width: '100px', height: '26px' }}
            value={task.status}
            onChange={(e) => handleStatusChange(task, e.target.value)}
            disabled={!canUpdateStatus}
            title={!canUpdateStatus ? 'Only assignee, creator or project owner can change status' : ''}
          >
            <option value="PENDING">To Do</option>
            <option value="IN_PROGRESS">In Progress</option>
            <option value="COMPLETED">Completed</option>
          </select>
          {canDelete && (
            <button 
              className="btn btn-danger-outline"
              onClick={() => handleDelete(task.id)}
              style={{ padding: '4px 8px', fontSize: '0.7rem' }}
            >
              Delete
            </button>
          )}
        </div>
      </div>
    );
  };

  return (
    <div className="container">
      <div style={{ marginBottom: '32px' }}>
        <h1>Tasks Workspace</h1>
        <p className="subtitle">Manage tasks and update progress statuses</p>
      </div>

      {error && (
        <div className="card" style={{ borderColor: 'var(--danger)', backgroundColor: 'var(--danger-light)', marginBottom: '24px', padding: '16px' }}>
          <p style={{ color: 'var(--danger)', fontWeight: 500, fontSize: '0.875rem' }}>⚠️ {error}</p>
        </div>
      )}

      {projects.length === 0 && !loading && (
        <div className="card" style={{ borderColor: 'var(--warning)', backgroundColor: 'var(--warning-light)', marginBottom: '24px', padding: '16px' }}>
          <p style={{ color: 'var(--warning)', fontWeight: 500, fontSize: '0.875rem' }}>
            ⚠️ No projects exist! You must create at least one Project before adding Tasks.
          </p>
        </div>
      )}

      <div className="grid-form-split">
        {/* Left Side: Create Form */}
        <div>
          <div className="card" style={{ position: 'sticky', top: '102px' }}>
            <h2 style={{ fontSize: '1rem', marginBottom: '20px' }}>Create Task</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label className="form-label">Task Title</label>
                <input
                  type="text"
                  className="form-control"
                  placeholder="e.g. Design Entity Models"
                  value={title}
                  onChange={e => setTitle(e.target.value)}
                  disabled={projects.length === 0}
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">Project Assignment</label>
                <select
                  className="form-control"
                  value={projectId}
                  onChange={e => setProjectId(e.target.value)}
                  disabled={projects.length === 0}
                >
                  {projects.map(p => (
                    <option key={p.id} value={p.id}>{p.name}</option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label className="form-label">Assign To</label>
                <select
                  className="form-control"
                  value={assignedToId}
                  onChange={e => setAssignedToId(e.target.value)}
                  disabled={projects.length === 0}
                >
                  <option value="">Unassigned</option>
                  {activeProjectMembers.map(m => (
                    <option key={m.id} value={m.id}>{m.name}</option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label className="form-label">Initial Status</label>
                <select
                  className="form-control"
                  value={status}
                  onChange={e => setStatus(e.target.value)}
                  disabled={projects.length === 0}
                >
                  <option value="PENDING">Pending</option>
                  <option value="IN_PROGRESS">In Progress</option>
                  <option value="COMPLETED">Completed</option>
                </select>
              </div>
              <div className="form-group">
                <label className="form-label">Due Date</label>
                <input
                  type="date"
                  className="form-control"
                  value={dueDate}
                  onChange={e => setDueDate(e.target.value)}
                  disabled={projects.length === 0}
                />
              </div>
              <button 
                type="submit" 
                className="btn btn-primary" 
                style={{ width: '100%', height: '40px', marginTop: '8px' }}
                disabled={projects.length === 0}
              >
                Add Task
              </button>
            </form>
          </div>
        </div>

        {/* Right Side: Kanban Board */}
        <div>
          {loading ? (
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Loading tasks board...</p>
          ) : (
            <div className="kanban-board">
              {/* PENDING Column */}
              <div className="kanban-column">
                <div className="kanban-column-header">
                  <div className="kanban-column-title">
                    <span style={{ color: 'var(--warning)', marginRight: '6px' }}>●</span>
                    <span>To Do</span>
                  </div>
                  <span className="kanban-column-count">{pendingTasks.length}</span>
                </div>
                <div className="kanban-list">
                  {pendingTasks.map(renderTaskCard)}
                </div>
              </div>

              {/* IN_PROGRESS Column */}
              <div className="kanban-column">
                <div className="kanban-column-header">
                  <div className="kanban-column-title">
                    <span style={{ color: 'var(--accent)', marginRight: '6px' }}>●</span>
                    <span>In Progress</span>
                  </div>
                  <span className="kanban-column-count">{inProgressTasks.length}</span>
                </div>
                <div className="kanban-list">
                  {inProgressTasks.map(renderTaskCard)}
                </div>
              </div>

              {/* COMPLETED Column */}
              <div className="kanban-column">
                <div className="kanban-column-header">
                  <div className="kanban-column-title">
                    <span style={{ color: 'var(--success)', marginRight: '6px' }}>●</span>
                    <span>Completed</span>
                  </div>
                  <span className="kanban-column-count">{completedTasks.length}</span>
                </div>
                <div className="kanban-list">
                  {completedTasks.map(renderTaskCard)}
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Tasks;
