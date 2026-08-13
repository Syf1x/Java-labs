import { useEffect, useState } from 'react'
import { departmentsApi } from '../api.js'

const emptyForm = { id: null, name: '' }

export default function DepartmentsPage() {
  const [departments, setDepartments] = useState([])
  const [form, setForm] = useState(emptyForm)
  const [error, setError] = useState(null)

  const load = () => {
    departmentsApi.list().then(setDepartments).catch((e) => setError(e.message))
  }

  useEffect(load, [])

  const submit = async (e) => {
    e.preventDefault()
    setError(null)
    try {
      if (form.id) {
        await departmentsApi.update(form.id, { name: form.name })
      } else {
        await departmentsApi.create({ name: form.name })
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
      await departmentsApi.remove(id)
      load()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div>
      <h2 className="section-title">Департаменты</h2>
      {error && <div className="error-banner">{error}</div>}

      <form className="form-card" onSubmit={submit}>
        <h3>{form.id ? 'Редактировать департамент' : 'Новый департамент'}</h3>
        <div className="field">
          <label>Название</label>
          <input
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            required
            minLength={2}
          />
        </div>
        <div className="form-actions">
          <button className="btn primary" type="submit">{form.id ? 'Сохранить' : 'Создать'}</button>
          {form.id && <button className="btn" type="button" onClick={() => setForm(emptyForm)}>Отмена</button>}
        </div>
      </form>

      <table>
        <thead>
          <tr><th>ID</th><th>Название</th><th></th></tr>
        </thead>
        <tbody>
          {departments.map((d) => (
            <tr key={d.id}>
              <td>{d.id}</td>
              <td>{d.name}</td>
              <td className="row-actions">
                <button className="btn" onClick={() => setForm({ id: d.id, name: d.name })}>Изменить</button>
                <button className="btn danger" onClick={() => remove(d.id)}>Удалить</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
