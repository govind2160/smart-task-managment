import React, { useState, useEffect, useContext } from 'react';
import { projectApi, taskApi, dashboardApi } from '../services/api';
import { AuthContext } from '../context/AuthContext';

const Dashboard = () => {
  const { user } = useContext(AuthContext);
  
  // Data States
  const [tasks, setTasks] = useState([]);
  const [projects, setProjects] = useState([]);
  const [projectMembersMap, setProjectMembersMap] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Filters State
  const [selectedProject, setSelectedProject] = useState('all');
  const [selectedAssignee, setSelectedAssignee] = useState('all');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  
  // Interactive UI state
  const [hoveredSegment, setHoveredSegment] = useState(null);

  const fetchData = async () => {
    try {
      setLoading(true);
      setError(null);
      const [projectsRes, tasksRes] = await Promise.all([
        projectApi.getProjects(),
        taskApi.getTasks()
      ]);

      const projectsData = projectsRes.data || [];
      const tasksData = tasksRes.data || [];

      setProjects(projectsData);
      setTasks(tasksData);

      // Fetch members map for each project
      const membersMap = {};
      for (const p of projectsData) {
        try {
          const res = await projectApi.getMembers(p.id);
          membersMap[p.id] = res.data || [];
        } catch (err) {
          console.error(`Error fetching members for project ${p.id}:`, err);
        }
      }
      setProjectMembersMap(membersMap);
      setLoading(false);
    } catch (err) {
      console.error('Error fetching dashboard data:', err);
      setError('Could not fetch workspace data from Spring Boot. Make sure the backend server is running on port 8080!');
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  // Sync assignee filter selection when project filter changes
  useEffect(() => {
    setSelectedAssignee('all');
  }, [selectedProject]);

  // Extract valid assignees based on selected project
  const getFilterAssignees = () => {
    if (selectedProject !== 'all') {
      return projectMembersMap[selectedProject] || [];
    }
    // All unique members across all projects
    const allMembers = [];
    Object.values(projectMembersMap).forEach(members => {
      members.forEach(m => {
        if (!allMembers.some(existing => existing.id === m.id)) {
          allMembers.push(m);
        }
      });
    });
    return allMembers;
  };

  // Perform reactive client-side filtering
  const filteredTasks = tasks.filter(task => {
    // 1. Project Filter
    if (selectedProject !== 'all' && task.projectId !== parseInt(selectedProject, 10)) {
      return false;
    }
    // 2. Assignee Filter
    if (selectedAssignee !== 'all' && task.assignedToId !== parseInt(selectedAssignee, 10)) {
      return false;
    }
    // 3. Date Range Filters (by Task Due Date)
    if (startDate && (!task.deadline || task.deadline < startDate)) {
      return false;
    }
    if (endDate && (!task.deadline || task.deadline > endDate)) {
      return false;
    }
    return true;
  });

  // Calculate Metrics
  const totalTasks = filteredTasks.length;
  const completedTasks = filteredTasks.filter(t => t.status === 'COMPLETED').length;
  const inProgressTasks = filteredTasks.filter(t => t.status === 'IN_PROGRESS').length;
  const pendingTasks = filteredTasks.filter(t => t.status === 'PENDING').length;
  
  // Overdue = Not completed, has due date, and due date is prior to today
  const todayStr = new Date().toISOString().split('T')[0];
  const overdueTasks = filteredTasks.filter(t => 
    t.status !== 'COMPLETED' && 
    t.deadline && 
    t.deadline < todayStr
  ).length;

  const completionPercentage = totalTasks > 0 ? Math.round((completedTasks / totalTasks) * 100) : 0;

  // Determine progress bar color based on percentage
  const getProgressColorClass = (pct) => {
    if (pct <= 30) return 'progress-red';
    if (pct <= 70) return 'progress-yellow';
    return 'progress-green';
  };

  const getProgressColor = (pct) => {
    if (pct <= 30) return 'var(--danger)';
    if (pct <= 70) return 'var(--warning)';
    return 'var(--success)';
  };

  // Reset all filters
  const handleClearFilters = () => {
    setSelectedProject('all');
    setSelectedAssignee('all');
    setStartDate('');
    setEndDate('');
  };

  if (loading) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '400px', gap: '16px' }}>
        <div className="spinner" style={{ width: '40px', height: '40px', border: '3px solid var(--border-color)', borderTopColor: 'var(--accent)', borderRadius: '50%', animation: 'spin 1s linear infinite' }}></div>
        <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Loading workspace dashboard metrics...</p>
      </div>
    );
  }

  // Set up chart data
  const segments = [
    { label: 'Completed', count: completedTasks, color: 'var(--success)', key: 'COMPLETED' },
    { label: 'In Progress', count: inProgressTasks, color: 'var(--accent)', key: 'IN_PROGRESS' },
    { label: 'Pending', count: pendingTasks, color: 'var(--warning)', key: 'PENDING' },
    { label: 'Overdue', count: overdueTasks, color: 'var(--danger)', key: 'OVERDUE' },
  ].filter(s => s.count > 0);

  // Math for SVG Doughnut: Radius = 50, Circumference = 314.16
  const R = 50;
  const Circ = 2 * Math.PI * R; // ~314.16
  let prevSum = 0;

  return (
    <div className="container">
      {/* Header */}
      <div style={{ marginBottom: '24px', display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', flexWrap: 'wrap', gap: '16px' }}>
        <div>
          <h1>Dashboard Overview</h1>
          <p className="subtitle">Track project progress, task completions, and team performance metrics</p>
        </div>
        <button className="btn btn-secondary" onClick={fetchData} style={{ height: '38px', marginBottom: '24px' }}>
          Refresh Data
        </button>
      </div>

      {error && (
        <div className="card" style={{ borderColor: 'var(--danger)', backgroundColor: 'var(--danger-light)', marginBottom: '24px', padding: '16px' }}>
          <p style={{ color: 'var(--danger)', fontWeight: 500, fontSize: '0.875rem' }}>{error}</p>
        </div>
      )}

      {/* Interactive Filters Panel */}
      <div className="card" style={{ marginBottom: '32px', padding: '20px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px', borderBottom: '1px solid var(--border-color)', paddingBottom: '12px' }}>
          <h3 style={{ fontSize: '0.925rem', fontWeight: 600, color: 'var(--primary)', display: 'flex', alignItems: 'center', gap: '6px' }}>
            Dynamic Filters
          </h3>
          {(selectedProject !== 'all' || selectedAssignee !== 'all' || startDate || endDate) && (
            <button className="btn btn-danger-outline" onClick={handleClearFilters} style={{ padding: '4px 10px', fontSize: '0.75rem', height: '26px' }}>
              Clear Filters
            </button>
          )}
        </div>

        <div className="filters-grid">
          <div className="form-group" style={{ marginBottom: 0 }}>
            <label className="form-label" style={{ fontSize: '0.75rem', fontWeight: 600 }}>Project Workspace</label>
            <select className="form-control" value={selectedProject} onChange={e => setSelectedProject(e.target.value)}>
              <option value="all">All Projects ({projects.length})</option>
              {projects.map(p => (
                <option key={p.id} value={p.id}>{p.name}</option>
              ))}
            </select>
          </div>

          <div className="form-group" style={{ marginBottom: 0 }}>
            <label className="form-label" style={{ fontSize: '0.75rem', fontWeight: 600 }}>Team Member (Assignee)</label>
            <select className="form-control" value={selectedAssignee} onChange={e => setSelectedAssignee(e.target.value)}>
              <option value="all">All Members ({getFilterAssignees().length})</option>
              {getFilterAssignees().map(m => (
                <option key={m.id} value={m.id}>{m.name}</option>
              ))}
            </select>
          </div>

          <div className="form-group" style={{ marginBottom: 0 }}>
            <label className="form-label" style={{ fontSize: '0.75rem', fontWeight: 600 }}>Start Date</label>
            <input type="date" className="form-control" value={startDate} onChange={e => setStartDate(e.target.value)} />
          </div>

          <div className="form-group" style={{ marginBottom: 0 }}>
            <label className="form-label" style={{ fontSize: '0.75rem', fontWeight: 600 }}>End Date</label>
            <input type="date" className="form-control" value={endDate} onChange={e => setEndDate(e.target.value)} />
          </div>
        </div>
      </div>

      {/* Prominent Project Completion KPI Section */}
      <div className="card completion-card" style={{ marginBottom: '32px', borderLeft: `6px solid ${getProgressColor(completionPercentage)}` }}>
        <div className="completion-card-header">
          <div>
            <h2 style={{ fontSize: '1.1rem', marginBottom: '4px' }}>Project Completion Rate</h2>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.8rem', margin: 0 }}>Calculated from the currently filtered active task list</p>
          </div>
          <div className="completion-badge" style={{ color: getProgressColor(completionPercentage), backgroundColor: `${getProgressColor(completionPercentage)}15` }}>
            {completionPercentage}%
          </div>
        </div>

        <div className="progress-bar-container">
          <div 
            className={`progress-bar-fill ${getProgressColorClass(completionPercentage)}`} 
            style={{ width: `${completionPercentage}%` }}
          />
        </div>

        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '12px', fontSize: '0.825rem' }}>
          <span style={{ color: 'var(--text-secondary)' }}>
            <strong>{completedTasks}</strong> of <strong>{totalTasks}</strong> tasks completed
          </span>
          {overdueTasks > 0 ? (
            <span style={{ color: 'var(--danger)', fontWeight: 600, display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
              {overdueTasks} Overdue Task{overdueTasks > 1 ? 's' : ''}
            </span>
          ) : (
            <span style={{ color: 'var(--success)' }}>No overdue tasks</span>
          )}
        </div>
      </div>

      {/* KPI Cards Grid */}
      <div className="grid-4" style={{ marginBottom: '32px' }}>
        <div className="card kpi-card" style={{ borderLeft: '4px solid var(--accent)' }}>
          <div className="kpi-label">Active Tasks</div>
          <div className="kpi-value">{totalTasks}</div>
        </div>

        <div className="card kpi-card" style={{ borderLeft: '4px solid var(--success)' }}>
          <div className="kpi-label">Completed</div>
          <div className="kpi-value" style={{ color: 'var(--success)' }}>{completedTasks}</div>
        </div>

        <div className="card kpi-card" style={{ borderLeft: '4px solid var(--accent)' }}>
          <div className="kpi-label">In Progress</div>
          <div className="kpi-value" style={{ color: 'var(--accent)' }}>{inProgressTasks}</div>
        </div>

        <div className="card kpi-card" style={{ borderLeft: '4px solid var(--warning)' }}>
          <div className="kpi-label">Pending / To Do</div>
          <div className="kpi-value" style={{ color: 'var(--warning)' }}>{pendingTasks}</div>
        </div>

        <div className="card kpi-card" style={{ borderLeft: overdueTasks > 0 ? '4px solid var(--danger)' : '4px solid var(--text-muted)' }}>
          <div className="kpi-label">Overdue Tasks</div>
          <div className="kpi-value" style={{ color: overdueTasks > 0 ? 'var(--danger)' : 'var(--text-muted)' }}>{overdueTasks}</div>
        </div>
      </div>

      {/* Visual Charts and Detailed Breakdown Section */}
      <div className="grid-2" style={{ gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '32px', marginBottom: '32px' }}>
        
        {/* Task Progress Chart Card */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column' }}>
          <div style={{ marginBottom: '24px' }}>
            <h3 style={{ fontSize: '1rem', fontWeight: 600, color: 'var(--primary)', margin: 0 }}>Task Progress Breakdown</h3>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.8rem', margin: '4px 0 0 0' }}>Hover over the doughnut sections to inspect details</p>
          </div>

          {totalTasks === 0 ? (
            <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: '40px 0', textAlign: 'center' }}>
              <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="var(--text-muted)" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
                <circle cx="12" cy="12" r="10" />
                <line x1="8" y1="12" x2="16" y2="12" />
              </svg>
              <h4 style={{ fontSize: '0.9rem', color: 'var(--text-primary)', marginTop: '12px', marginBottom: '4px' }}>No Tasks Found</h4>
              <p style={{ color: 'var(--text-secondary)', fontSize: '0.75rem', maxWidth: '240px', margin: 0 }}>Create tasks in the workspace or adjust filters to compile chart data.</p>
            </div>
          ) : (
            <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
              {/* SVG Doughnut */}
              <div style={{ position: 'relative', width: '180px', height: '180px', marginBottom: '24px' }}>
                <svg width="100%" height="100%" viewBox="0 0 140 140">
                  <circle cx="70" cy="70" r="50" fill="transparent" stroke="var(--border-color)" strokeWidth="12" />
                  
                  {segments.map((seg) => {
                    const percent = (seg.count / totalTasks) * 100;
                    const strokeLength = (percent / 100) * Circ;
                    const strokeOffset = -prevSum;
                    prevSum += strokeLength;

                    const isHovered = hoveredSegment === seg.key;

                    return (
                      <circle
                        key={seg.key}
                        cx="70"
                        cy="70"
                        r="50"
                        fill="transparent"
                        stroke={seg.color}
                        strokeWidth={isHovered ? 16 : 12}
                        strokeDasharray={`${strokeLength} ${Circ - strokeLength}`}
                        strokeDashoffset={strokeOffset}
                        transform="rotate(-90 70 70)"
                        style={{ cursor: 'pointer', transition: 'stroke-width 0.2s ease, stroke-dashoffset 0.5s ease' }}
                        onMouseEnter={() => setHoveredSegment(seg.key)}
                        onMouseLeave={() => setHoveredSegment(null)}
                      />
                    );
                  })}
                </svg>

                {/* Doughnut Middle Text */}
                <div style={{
                  position: 'absolute', top: 0, left: 0, right: 0, bottom: 0,
                  display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
                  pointerEvents: 'none', textAlign: 'center', padding: '10px'
                }}>
                  {hoveredSegment ? (
                    (() => {
                      const seg = segments.find(s => s.key === hoveredSegment);
                      if (!seg) return null;
                      const pct = Math.round((seg.count / totalTasks) * 100);
                      return (
                        <>
                          <span style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', textTransform: 'uppercase' }}>
                            {seg.label}
                          </span>
                          <span style={{ fontSize: '1.25rem', fontWeight: 700, color: seg.color }}>
                            {seg.count} ({pct}%)
                          </span>
                        </>
                      );
                    })()
                  ) : (
                    <>
                      <span style={{ fontSize: '1.65rem', fontWeight: 700, color: 'var(--primary)' }}>
                        {completionPercentage}%
                      </span>
                      <span style={{ fontSize: '0.65rem', color: 'var(--text-secondary)', textTransform: 'uppercase', fontWeight: 600, letterSpacing: '0.05em' }}>
                        Completion
                      </span>
                    </>
                  )}
                </div>
              </div>

              {/* Chart Legend */}
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px 24px', width: '100%', borderTop: '1px solid var(--border-color)', paddingTop: '16px' }}>
                {segments.map((seg) => {
                  const pct = Math.round((seg.count / totalTasks) * 100);
                  return (
                    <div 
                      key={seg.key} 
                      style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.75rem', color: 'var(--text-primary)', cursor: 'pointer', opacity: hoveredSegment && hoveredSegment !== seg.key ? 0.5 : 1, transition: 'opacity 0.2s ease' }}
                      onMouseEnter={() => setHoveredSegment(seg.key)}
                      onMouseLeave={() => setHoveredSegment(null)}
                    >
                      <span style={{ width: '10px', height: '10px', borderRadius: '2px', backgroundColor: seg.color, display: 'inline-block' }}></span>
                      <span style={{ fontWeight: 500 }}>{seg.label}:</span>
                      <span style={{ color: 'var(--text-secondary)', marginLeft: 'auto', fontWeight: 600 }}>{seg.count} ({pct}%)</span>
                    </div>
                  );
                })}
              </div>
            </div>
          )}
        </div>

        {/* Task Breakdown and Timeline Card */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column' }}>
          <div style={{ marginBottom: '16px' }}>
            <h3 style={{ fontSize: '1rem', fontWeight: 600, color: 'var(--primary)', margin: 0 }}>Filtered Tasks List</h3>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.8rem', margin: '4px 0 0 0' }}>Showing tasks matching active filters ({filteredTasks.length})</p>
          </div>

          <div style={{ flex: 1, overflowY: 'auto', maxHeight: '280px', paddingRight: '4px' }}>
            {filteredTasks.length === 0 ? (
              <div style={{ height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--text-muted)', fontSize: '0.8rem', padding: '40px 0' }}>
                No tasks match the selected filter parameters.
              </div>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                {filteredTasks.map(task => {
                  let badgeColorClass = 'status-planned';
                  if (task.status === 'COMPLETED') badgeColorClass = 'status-completed';
                  else if (task.status === 'IN_PROGRESS') badgeColorClass = 'status-active';

                  const isOverdue = task.status !== 'COMPLETED' && task.deadline && task.deadline < todayStr;

                  return (
                    <div key={task.id} style={{ display: 'flex', alignItems: 'center', gap: '12px', padding: '10px 12px', borderRadius: '8px', border: '1px solid var(--border-color)', backgroundColor: 'var(--bg-secondary)', fontSize: '0.78rem' }}>
                      <div style={{ display: 'flex', flexDirection: 'column', flex: 1, gap: '2px', minWidth: 0 }}>
                        <span style={{ fontWeight: 600, color: 'var(--primary)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }} title={task.title}>
                          {task.title}
                        </span>
                        <span style={{ fontSize: '0.68rem', color: 'var(--text-secondary)' }}>
                          {task.assignedToName || 'Unassigned'}
                        </span>
                      </div>
                      
                      {task.deadline && (
                        <span style={{ 
                          fontSize: '0.68rem', 
                          color: isOverdue ? 'var(--danger)' : 'var(--text-secondary)', 
                          fontWeight: isOverdue ? 600 : 'normal',
                          backgroundColor: isOverdue ? 'var(--danger-light)' : 'transparent',
                          padding: isOverdue ? '2px 6px' : '0',
                          borderRadius: isOverdue ? '4px' : '0',
                          whiteSpace: 'nowrap'
                        }}>
                          Due: {task.deadline}
                        </span>
                      )}

                      <span className={`badge ${badgeColorClass}`} style={{ padding: '2px 8px', fontSize: '0.65rem', whiteSpace: 'nowrap' }}>
                        {task.status === 'IN_PROGRESS' ? 'In Progress' : task.status === 'PENDING' ? 'Pending' : 'Completed'}
                      </span>
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        </div>

      </div>
    </div>
  );
};

export default Dashboard;
