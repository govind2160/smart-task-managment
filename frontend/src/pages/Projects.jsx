import React, { useState, useEffect, useContext } from 'react';
import { projectApi, userApi } from '../services/api';
import { AuthContext } from '../context/AuthContext';

const Projects = () => {
  const { user } = useContext(AuthContext);
  const [projects, setProjects] = useState([]);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [status, setStatus] = useState('PLANNED');
  const [searchQuery, setSearchQuery] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Members management state
  const [managingProjectId, setManagingProjectId] = useState(null);
  const [projectMembers, setProjectMembers] = useState([]);
  const [allUsers, setAllUsers] = useState([]);

  const fetchProjects = () => {
    setLoading(true);
    projectApi.getProjects()
      .then(response => {
        setProjects(response.data);
        setLoading(false);
      })
      .catch(err => {
        console.error('Error fetching projects:', err);
        setError('Failed to fetch projects. Please check your backend connection.');
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchProjects();
    userApi.getUsers()
      .then(response => {
        setAllUsers(response.data);
      })
      .catch(err => console.error('Error fetching all users:', err));
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!name) return;

    const newProject = { name, description, status };

    projectApi.createProject(newProject)
      .then(() => {
        setName('');
        setDescription('');
        setStatus('PLANNED');
        fetchProjects();
      })
      .catch(err => {
        console.error('Error creating project:', err);
        alert('Failed to create project: ' + (err.response?.data?.message || err.message));
      });
  };

  const handleDelete = (id) => {
    if (!window.confirm('Are you sure you want to delete this project? This will delete all associated tasks!')) return;

    projectApi.deleteProject(id)
      .then(() => {
        fetchProjects();
      })
      .catch(err => {
        console.error('Error deleting project:', err);
        alert('Failed to delete project.');
      });
  };

  const handleManageMembers = (projectId) => {
    setManagingProjectId(projectId);
    projectApi.getMembers(projectId)
      .then(res => {
        setProjectMembers(res.data);
      })
      .catch(err => {
        console.error('Error fetching project members:', err);
        alert('Failed to load project members.');
      });
  };

  const handleAddMember = (userId) => {
    if (!userId) return;
    projectApi.addMember(managingProjectId, userId)
      .then(res => {
        setProjectMembers(res.data);
        setProjects(prev => prev.map(p => p.id === managingProjectId ? { ...p, memberCount: res.data.length } : p));
      })
      .catch(err => {
        console.error('Error adding member:', err);
        alert('Failed to add member: ' + (err.response?.data?.message || err.message));
      });
  };

  const handleRemoveMember = (userId) => {
    if (!window.confirm('Are you sure you want to remove this member?')) return;
    projectApi.removeMember(managingProjectId, userId)
      .then(res => {
        setProjectMembers(res.data);
        setProjects(prev => prev.map(p => p.id === managingProjectId ? { ...p, memberCount: res.data.length } : p));
      })
      .catch(err => {
        console.error('Error removing member:', err);
        alert('Failed to remove member: ' + (err.response?.data?.message || err.message));
      });
  };

  const filteredProjects = projects.filter(p => 
    p.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    (p.description && p.description.toLowerCase().includes(searchQuery.toLowerCase()))
  );

  const getStatusBadgeClass = (projStatus) => {
    switch (projStatus) {
      case 'ACTIVE':
        return 'status-active';
      case 'COMPLETED':
        return 'status-completed';
      case 'PLANNED':
      default:
        return 'status-planned';
    }
  };

  return (
    <div className="container">
      <div style={{ marginBottom: '32px' }}>
        <h1>Projects Workspace</h1>
        <p className="subtitle">Create and organize projects to categorize tasks</p>
      </div>

      {error && (
        <div className="card" style={{ borderColor: 'var(--danger)', backgroundColor: 'var(--danger-light)', marginBottom: '24px', padding: '16px' }}>
          <p style={{ color: 'var(--danger)', fontWeight: 500, fontSize: '0.875rem' }}>⚠️ {error}</p>
        </div>
      )}

      <div className="grid-form-split">
        {/* Left Side: Create Form */}
        <div>
          <div className="card" style={{ position: 'sticky', top: '102px' }}>
            <h2 style={{ fontSize: '1rem', marginBottom: '20px' }}>Create Project</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label className="form-label">Project Name</label>
                <input
                  type="text"
                  className="form-control"
                  placeholder="e.g. Website Redesign"
                  value={name}
                  onChange={e => setName(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">Description</label>
                <textarea
                  className="form-control"
                  placeholder="Brief description of project goals..."
                  rows="4"
                  value={description}
                  onChange={e => setDescription(e.target.value)}
                  style={{ resize: 'vertical' }}
                />
              </div>
              <div className="form-group">
                <label className="form-label">Initial Status</label>
                <select
                  className="form-control"
                  value={status}
                  onChange={e => setStatus(e.target.value)}
                >
                  <option value="PLANNED">Planned</option>
                  <option value="ACTIVE">Active</option>
                  <option value="COMPLETED">Completed</option>
                </select>
              </div>
              <button type="submit" className="btn btn-primary" style={{ width: '100%', height: '40px', marginTop: '8px' }}>
                Add Project
              </button>
            </form>
          </div>
        </div>

        {/* Right Side: Projects Table List */}
        <div>
          <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: '16px', flexWrap: 'wrap' }}>
              <h2 style={{ fontSize: '1rem', margin: 0 }}>All Projects</h2>
              <input
                type="text"
                className="form-control"
                placeholder="Search projects..."
                value={searchQuery}
                onChange={e => setSearchQuery(e.target.value)}
                style={{ width: '220px', height: '36px' }}
              />
            </div>

            {loading ? (
              <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Loading projects list...</p>
            ) : filteredProjects.length === 0 ? (
              <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>No projects found. Create a project to begin.</p>
            ) : (
              <div className="table-container">
                <table className="table">
                  <thead>
                    <tr>
                      <th>Project</th>
                      <th>Status</th>
                      <th style={{ textAlign: 'right' }}>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredProjects.map(proj => {
                      const isOwner = user && user.id === proj.ownerId;
                      return (
                        <tr key={proj.id}>
                          <td>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', flexWrap: 'wrap' }}>
                              <span style={{ fontWeight: 600, color: 'var(--primary)', fontSize: '0.9rem' }}>{proj.name}</span>
                              {isOwner ? (
                                <span style={{
                                  fontSize: '0.6875rem', fontWeight: 600, padding: '2px 8px', borderRadius: '50px',
                                  backgroundColor: 'var(--accent-light)', color: 'var(--accent)', border: '1px solid rgba(37,99,235,0.1)'
                                }}>
                                  Owner
                                </span>
                              ) : (
                                <span style={{
                                  fontSize: '0.6875rem', fontWeight: 600, padding: '2px 8px', borderRadius: '50px',
                                  backgroundColor: 'var(--bg-secondary)', color: 'var(--text-secondary)', border: '1px solid var(--border-color)'
                                }}>
                                  Collaborator
                                </span>
                              )}
                              <span style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>
                                👥 {proj.memberCount || 1} member{proj.memberCount !== 1 ? 's' : ''}
                              </span>
                            </div>
                            <div style={{ color: 'var(--text-secondary)', fontSize: '0.8125rem', marginTop: '4px', lineHeight: '1.4' }}>
                              {proj.description || 'No description provided.'}
                            </div>
                          </td>
                          <td>
                            <span className={`badge ${getStatusBadgeClass(proj.status)}`}>
                              {proj.status}
                            </span>
                          </td>
                          <td style={{ textAlign: 'right' }}>
                            <div style={{ display: 'flex', gap: '8px', justifyContent: 'flex-end' }}>
                              {isOwner && (
                                <button 
                                  className="btn btn-secondary" 
                                  onClick={() => handleManageMembers(proj.id)}
                                  style={{ padding: '6px 12px', fontSize: '0.75rem' }}
                                >
                                  Members
                                </button>
                              )}
                              {isOwner && (
                                <button 
                                  className="btn btn-danger-outline" 
                                  onClick={() => handleDelete(proj.id)}
                                  style={{ padding: '6px 12px', fontSize: '0.75rem' }}
                                >
                                  Delete
                                </button>
                              )}
                            </div>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Members Management Modal Overlay */}
      {managingProjectId && (
        <div style={{
          position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
          backgroundColor: 'rgba(15, 23, 42, 0.4)', display: 'flex',
          alignItems: 'center', justifyContent: 'center', zIndex: 1000
        }}>
          <div className="card" style={{ width: '450px', padding: '24px', position: 'relative', boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
              <h2 style={{ margin: 0, fontSize: '1.15rem' }}>Manage Members</h2>
              <button
                onClick={() => setManagingProjectId(null)}
                style={{ background: 'none', border: 'none', fontSize: '1.25rem', cursor: 'pointer', color: 'var(--text-secondary)' }}
              >
                ✕
              </button>
            </div>

            {/* Member list */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', maxHeight: '240px', overflowY: 'auto', marginBottom: '20px', paddingRight: '4px' }}>
              {projectMembers.map(member => (
                <div key={member.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 14px', border: '1px solid var(--border-color)', borderRadius: '8px', backgroundColor: 'var(--bg-secondary)' }}>
                  <div>
                    <div style={{ fontWeight: 600, fontSize: '0.875rem', color: 'var(--text-primary)' }}>{member.name}</div>
                    <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>{member.email}</div>
                  </div>
                  {projects.find(p => p.id === managingProjectId)?.ownerId !== member.id ? (
                    <button
                      onClick={() => handleRemoveMember(member.id)}
                      style={{ background: 'none', border: 'none', color: 'var(--danger)', fontSize: '0.8125rem', fontWeight: 500, cursor: 'pointer' }}
                    >
                      Remove
                    </button>
                  ) : (
                    <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', fontWeight: 600 }}>Owner</span>
                  )}
                </div>
              ))}
            </div>

            {/* Add Member Dropdown */}
            <div style={{ borderTop: '1px solid var(--border-color)', paddingTop: '16px' }}>
              <label className="form-label" style={{ fontSize: '0.8125rem', fontWeight: 600 }}>Invite new member</label>
              <select
                className="form-control"
                defaultValue=""
                onChange={e => {
                  handleAddMember(e.target.value);
                  e.target.value = ""; // Reset dropdown
                }}
              >
                <option value="" disabled>Select user to invite...</option>
                {allUsers
                  .filter(u => !projectMembers.some(pm => pm.id === u.id))
                  .map(u => (
                    <option key={u.id} value={u.id}>{u.name} ({u.email})</option>
                  ))
                }
              </select>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Projects;
