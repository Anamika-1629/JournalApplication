import { useState, useEffect } from 'react'
import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import Input from '../components/ui/Input'
import Button from '../components/ui/Button'
import ErrorMessage from '../components/ui/ErrorMessage'
import styles from './Auth.module.css'

export default function Login() {
  const { login, loading, error, clearError, isAuthenticated } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const from = location.state?.from?.pathname || '/dashboard'

  const [form, setForm] = useState({ userName: '', password: '' })
  const [fieldErrors, setFieldErrors] = useState({})

  useEffect(() => {
    if (isAuthenticated) navigate(from, { replace: true })
  }, [isAuthenticated, navigate, from])

  useEffect(() => {
    return () => clearError()
  }, [clearError])

  const validate = () => {
    const errs = {}
    if (!form.userName.trim()) errs.userName = 'Username is required'
    if (!form.password) errs.password = 'Password is required'
    return errs
  }

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm((prev) => ({ ...prev, [name]: value }))
    if (fieldErrors[name]) setFieldErrors((prev) => ({ ...prev, [name]: '' }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const errs = validate()
    if (Object.keys(errs).length) { setFieldErrors(errs); return }
    try {
      await login(form)
      navigate(from, { replace: true })
    } catch {
      // error shown from context
    }
  }

  return (
    <div className={styles.page}>
      <div className={styles.card}>
        <div className={styles.header}>
          <div className={styles.logo}>
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none">
              <rect x="3" y="3" width="14" height="18" rx="2" stroke="#4F46E5" strokeWidth="1.8"/>
              <line x1="7" y1="8" x2="13" y2="8" stroke="#4F46E5" strokeWidth="1.6" strokeLinecap="round"/>
              <line x1="7" y1="12" x2="13" y2="12" stroke="#4F46E5" strokeWidth="1.6" strokeLinecap="round"/>
              <line x1="7" y1="16" x2="11" y2="16" stroke="#4F46E5" strokeWidth="1.6" strokeLinecap="round"/>
              <path d="M17 6l3.5 3.5L13 17l-3.5.5.5-3.5L17 6z" stroke="#4F46E5" strokeWidth="1.6" strokeLinejoin="round"/>
            </svg>
          </div>
          <h1 className={styles.title}>Welcome back</h1>
          <p className={styles.subtitle}>Sign in to your journal</p>
        </div>

        {error && <ErrorMessage message={error} />}

        <form onSubmit={handleSubmit} className={styles.form} noValidate>
          <Input
            label="Username"
            name="userName"
            type="text"
            value={form.userName}
            onChange={handleChange}
            placeholder="your_username"
            error={fieldErrors.userName}
            autoComplete="username"
            autoFocus
          />
          <Input
            label="Password"
            name="password"
            type="password"
            value={form.password}
            onChange={handleChange}
            placeholder="••••••••"
            error={fieldErrors.password}
            autoComplete="current-password"
          />
          <Button type="submit" loading={loading} size="lg" className={styles.submitBtn}>
            Sign in
          </Button>
        </form>

        <p className={styles.footer}>
          Don't have an account?{' '}
          <Link to="/register">Create one</Link>
        </p>
      </div>
    </div>
  )
}
