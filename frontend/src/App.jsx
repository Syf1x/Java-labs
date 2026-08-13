import { NavLink, Route, Routes } from 'react-router-dom'
import EmployeesPage from './pages/EmployeesPage.jsx'
import DepartmentsPage from './pages/DepartmentsPage.jsx'
import ProjectsPage from './pages/ProjectsPage.jsx'
import TasksPage from './pages/TasksPage.jsx'
import './App.css'

function App() {
  return (
    <div className="app">
      <nav className="nav">
        <span className="brand">Employee Management</span>
        <NavLink to="/" end>Сотрудники</NavLink>
        <NavLink to="/departments">Департаменты</NavLink>
        <NavLink to="/projects">Проекты</NavLink>
        <NavLink to="/tasks">Задачи</NavLink>
      </nav>
      <main className="content">
        <Routes>
          <Route path="/" element={<EmployeesPage />} />
          <Route path="/departments" element={<DepartmentsPage />} />
          <Route path="/projects" element={<ProjectsPage />} />
          <Route path="/tasks" element={<TasksPage />} />
        </Routes>
      </main>
    </div>
  )
}

export default App
