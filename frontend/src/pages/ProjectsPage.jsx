import { useEffect, useState } from 'react'
import { projectsApi, tasksApi } from '../api.js'

const emptyForm = { id: null, name: '', description: '' }

export default function ProjectsPage() {
  const [projects, setProjects] = useState([])
  const [tasks, setTasks] = useState([])
  const [form, setForm] = useState(emptyForm)
  const [error, setError] = useState(null)
  const [expandedId, setExpandedId] = useState(null)

  const load = () => {
    projectsApi.list().then(setProjects).catch((e) => setError(e.message))
    tasksApi.list().then(setTasks).catch((e) => setError(e.message))
  }

  useEffect(load, [])

  const tasksForProject = (projectId) => tasks.filter((t) => t.projectId === projectId)

  const submit = async (e) => {
    e.preventDefault()
    setError(null)
    const dto = { name: form.name, description: form.description || null }
    try {
      if (form.id) {
        await projectsApi.update(form.id, dto)
      } else {
        await projectsApi.create(dto)
      }
      setForm(emptyForm)
      load()
    } catch (err) {
      setError(err.message)
    }
  }

  const remove = async (id) => {
    setError(null)
    try {
      await projectsApi.remove(id)
      load()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div>
      <h2 className="section-title">Проекты</h2>
      {error && <div className="error-banner">{error}</div>}

      <form className="form-card" onSubmit={submit}>
        <h3>{form.id ? 'Редактировать проект' : 'Новый проект'}</h3>
        <div className="field">
          <label>Название</label>
          <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required minLength={2} />
        </div>
        <div className="field">
          <label>Описание</label>
          <textarea value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
        </div>
        <div className="form-actions">
          <button className="btn primary" type="submit">{form.id ? 'Сохранить' : 'Создать'}</button>
          {form.id && <button className="btn" type="button" onClick={() => setForm(emptyForm)}>Отмена</button>}
        </div>
      </form>

      <table>
        <thead>
          <tr><th>ID</th><th>Название</th><th>Описание</th><th>Задачи (OneToMany)</th><th></th></tr>
        </thead>
        <tbody>
          {projects.map((p) => {
            const projectTasks = tasksForProject(p.id)
            const isExpanded = expandedId === p.id
            return (
              <tr key={p.id}>
                <td>{p.id}</td>
                <td>{p.name}</td>
                <td className="muted">{p.description}</td>
                <td>
                  <button className="btn" onClick={() => setExpandedId(isExpanded ? null : p.id)}>
                    {projectTasks.length} задач{isExpanded ? ' ▲' : ' ▼'}
                  </button>
                  {isExpanded && (
                    projectTasks.length === 0
                      ? <div className="muted">Нет задач</div>
                      : <ul className="task-list">
                          {projectTasks.map((t) => <li key={t.id}>{t.title} — {t.status}</li>)}
                        </ul>
                  )}
                </td>
                <td className="row-actions">
                  <button className="btn" onClick={() => setForm({ id: p.id, name: p.name, description: p.description ?? '' })}>Изменить</button>
                  <button className="btn danger" onClick={() => remove(p.id)}>Удалить</button>
                </td>
              </tr>
            )
          })}
        </tbody>
      </table>
    </div>
  )
}
