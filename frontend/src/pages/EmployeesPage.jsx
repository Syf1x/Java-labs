import { useEffect, useState } from 'react'
import { employeesApi, departmentsApi, tasksApi } from '../api.js'

const emptyForm = { id: null, firstName: '', lastName: '', bio: '', phoneNumber: '', departmentId: '' }

export default function EmployeesPage() {
  const [data, setData] = useState({ content: [], page: { number: 0, size: 10, totalElements: 0, totalPages: 0 } })
  const [departments, setDepartments] = useState([])
  const [tasks, setTasks] = useState([])
  const [form, setForm] = useState(emptyForm)
  const [error, setError] = useState(null)
  const [deptFilter, setDeptFilter] = useState('')
  const [nameFilter, setNameFilter] = useState('')
  const [page, setPage] = useState(0)
  const [assignTaskId, setAssignTaskId] = useState('')

  const loadEmployees = () => {
    const params = { page, size: 10 }
    if (deptFilter) params.dept = deptFilter
    if (nameFilter) params.name = nameFilter
    employeesApi.list(params).then(setData).catch((e) => setError(e.message))
  }

  useEffect(loadEmployees, [page, deptFilter, nameFilter])
  useEffect(() => {
    departmentsApi.list().then(setDepartments).catch((e) => setError(e.message))
    tasksApi.list().then(setTasks).catch((e) => setError(e.message))
  }, [])

  const taskTitle = (id) => tasks.find((t) => t.id === id)?.title ?? `#${id}`

  const submit = async (e) => {
    e.preventDefault()
    setError(null)
    const dto = {
      firstName: form.firstName,
      lastName: form.lastName,
      bio: form.bio || null,
      phoneNumber: form.phoneNumber || null,
      departmentId: form.departmentId ? Number(form.departmentId) : null,
      departmentName: null,
      taskIds: null,
    }
    try {
      if (form.id) {
        await employeesApi.update(form.id, dto)
      } else {
        await employeesApi.create(dto)
      }
      setForm(emptyForm)
      loadEmployees()
    } catch (err) {
      setError(err.message)
    }
  }

  const remove = async (id) => {
    setError(null)
    try {
      await employeesApi.remove(id)
      loadEmployees()
    } catch (err) {
      setError(err.message)
    }
  }

  const edit = (emp) => setForm({
    id: emp.id,
    firstName: emp.firstName,
    lastName: emp.lastName,
    bio: emp.bio === 'Нет' ? '' : emp.bio,
    phoneNumber: emp.phoneNumber === 'Нет' ? '' : emp.phoneNumber,
    departmentId: emp.departmentId ?? '',
  })

  const assignTask = async () => {
    if (!form.id || !assignTaskId) return
    setError(null)
    try {
      const updated = await employeesApi.assignTask(form.id, assignTaskId)
      setAssignTaskId('')
      loadEmployees()
      edit({ ...updated, bio: updated.bio, phoneNumber: updated.phoneNumber })
    } catch (err) {
      setError(err.message)
    }
  }

  const unassignTask = async (taskId) => {
    if (!form.id) return
    setError(null)
    try {
      await employeesApi.unassignTask(form.id, taskId)
      loadEmployees()
    } catch (err) {
      setError(err.message)
    }
  }

  const editingEmployee = data.content.find((e) => e.id === form.id)

  return (
    <div>
      <h2 className="section-title">Сотрудники</h2>
      {error && <div className="error-banner">{error}</div>}

      <div className="toolbar">
        <input placeholder="Фильтр по департаменту" value={deptFilter} onChange={(e) => { setPage(0); setDeptFilter(e.target.value) }} />
        <input placeholder="Фильтр по фамилии" value={nameFilter} onChange={(e) => { setPage(0); setNameFilter(e.target.value) }} />
      </div>

      <form className="form-card" onSubmit={submit}>
        <h3>{form.id ? 'Редактировать сотрудника' : 'Новый сотрудник'}</h3>
        <div className="field">
          <label>Имя</label>
          <input value={form.firstName} onChange={(e) => setForm({ ...form, firstName: e.target.value })} required minLength={2} />
        </div>
        <div className="field">
          <label>Фамилия</label>
          <input value={form.lastName} onChange={(e) => setForm({ ...form, lastName: e.target.value })} required minLength={2} />
        </div>
        <div className="field">
          <label>Биография</label>
          <textarea value={form.bio} onChange={(e) => setForm({ ...form, bio: e.target.value })} />
        </div>
        <div className="field">
          <label>Телефон</label>
          <input value={form.phoneNumber} onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })} />
        </div>
        <div className="field">
          <label>Департамент</label>
          <select value={form.departmentId} onChange={(e) => setForm({ ...form, departmentId: e.target.value })}>
            <option value="">— без департамента —</option>
            {departments.map((d) => <option key={d.id} value={d.id}>{d.name}</option>)}
          </select>
        </div>

        {form.id && (
          <div className="field">
            <label>Задачи (ManyToMany)</label>
            <div>
              {(editingEmployee?.taskIds ?? []).length === 0 && <span className="muted">Задач нет</span>}
              {(editingEmployee?.taskIds ?? []).map((tid) => (
                <span key={tid} className="badge removable" onClick={() => unassignTask(tid)} title="Нажми, чтобы снять задачу">
                  {taskTitle(tid)} ✕
                </span>
              ))}
            </div>
            <div className="form-actions">
              <select value={assignTaskId} onChange={(e) => setAssignTaskId(e.target.value)}>
                <option value="">— выбрать задачу —</option>
                {tasks.map((t) => <option key={t.id} value={t.id}>{t.title}</option>)}
              </select>
              <button className="btn" type="button" onClick={assignTask}>Назначить</button>
            </div>
          </div>
        )}

        <div className="form-actions">
          <button className="btn primary" type="submit">{form.id ? 'Сохранить' : 'Создать'}</button>
          {form.id && <button className="btn" type="button" onClick={() => setForm(emptyForm)}>Отмена</button>}
        </div>
      </form>

      <table>
        <thead>
          <tr><th>ID</th><th>Имя</th><th>Департамент</th><th>Задачи</th><th></th></tr>
        </thead>
        <tbody>
          {data.content.map((emp) => (
            <tr key={emp.id}>
              <td>{emp.id}</td>
              <td>{emp.firstName} {emp.lastName}</td>
              <td>{emp.departmentName}</td>
              <td>{(emp.taskIds ?? []).map((tid) => <span key={tid} className="badge">{taskTitle(tid)}</span>)}</td>
              <td className="row-actions">
                <button className="btn" onClick={() => edit(emp)}>Изменить</button>
                <button className="btn danger" onClick={() => remove(emp.id)}>Удалить</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="pagination">
        <button className="btn" disabled={page === 0} onClick={() => setPage((p) => p - 1)}>← Назад</button>
        <span>Страница {data.page.number + 1} из {Math.max(data.page.totalPages, 1)} (всего {data.page.totalElements})</span>
        <button className="btn" disabled={page + 1 >= data.page.totalPages} onClick={() => setPage((p) => p + 1)}>Вперёд →</button>
      </div>
    </div>
  )
}
