import { useEffect, useState } from 'react'
import { tasksApi, projectsApi } from '../api.js'

const STATUSES = ['TODO', 'IN_PROGRESS', 'DONE', 'CANCELLED']
const emptyForm = { id: null, title: '', description: '', status: 'TODO', projectId: '' }

export default function TasksPage() {
  const [tasks, setTasks] = useState([])
  const [projects, setProjects] = useState([])
  const [form, setForm] = useState(emptyForm)
  const [error, setError] = useState(null)

  const load = () => {
    tasksApi.list().then(setTasks).catch((e) => setError(e.message))
    projectsApi.list().then(setProjects).catch((e) => setError(e.message))
  }

  useEffect(load, [])

  const projectName = (id) => projects.find((p) => p.id === id)?.name ?? '—'

  const submit = async (e) => {
    e.preventDefault()
    setError(null)
    const dto = {
      title: form.title,
      description: form.description || null,
      status: form.status,
      projectId: form.projectId ? Number(form.projectId) : null,
    }
    try {
      if (form.id) {
        await tasksApi.update(form.id, dto)
      } else {
        await tasksApi.create(dto)
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
      await tasksApi.remove(id)
      load()
    } catch (err) {
      setError(err.message)
    }
  }

  const edit = (t) => setForm({
    id: t.id,
    title: t.title,
    description: t.description ?? '',
    status: t.status,
    projectId: t.projectId ?? '',
  })

  return (
    <div>
      <h2 className="section-title">Задачи</h2>
      {error && <div className="error-banner">{error}</div>}

      <form className="form-card" onSubmit={submit}>
        <h3>{form.id ? 'Редактировать задачу' : 'Новая задача'}</h3>
        <div className="field">
          <label>Заголовок</label>
          <input value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} required minLength={2} />
        </div>
        <div className="field">
          <label>Описание</label>
          <textarea value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
        </div>
        <div className="field">
          <label>Статус</label>
          <select value={form.status} onChange={(e) => setForm({ ...form, status: e.target.value })}>
            {STATUSES.map((s) => <option key={s} value={s}>{s}</option>)}
          </select>
        </div>
        <div className="field">
          <label>Проект</label>
          <select value={form.projectId} onChange={(e) => setForm({ ...form, projectId: e.target.value })}>
            <option value="">— без проекта —</option>
            {projects.map((p) => <option key={p.id} value={p.id}>{p.name}</option>)}
          </select>
        </div>
        <div className="form-actions">
          <button className="btn primary" type="submit">{form.id ? 'Сохранить' : 'Создать'}</button>
          {form.id && <button className="btn" type="button" onClick={() => setForm(emptyForm)}>Отмена</button>}
        </div>
      </form>

      <table>
        <thead>
          <tr><th>ID</th><th>Заголовок</th><th>Статус</th><th>Проект</th><th></th></tr>
        </thead>
        <tbody>
          {tasks.map((t) => (
            <tr key={t.id}>
              <td>{t.id}</td>
              <td>{t.title}</td>
              <td>{t.status}</td>
              <td>{projectName(t.projectId)}</td>
              <td className="row-actions">
                <button className="btn" onClick={() => edit(t)}>Изменить</button>
                <button className="btn danger" onClick={() => remove(t.id)}>Удалить</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
