import { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { useToast } from '../contexts/ToastContext'
import Input from '../components/ui/Input'
import Button from '../components/ui/Button'
import ErrorMessage from '../components/ui/ErrorMessage'
import styles from './Auth.module.css'

export default function Register() {
  const { register, loading, error, clearError, isAuthenticated } = useAuth()
  const { toast } = useToast()
  const navigate = useNavigate()

  const [form, setForm] = useState({
    userName: '',
    email: '',
    password: '',
    firstName: '',
    lastName: '',
  })
  const [fieldErrors, setFieldErrors] = useState({})

  useEffect(() => {
    if (isAuthenticated) navigate('/dashboard', { replace: true })
  }, [isAuthenticated, navigate])

  useEffect(() => {
    return () => clearError()
  }, [clearError])

  const validate = () => {
    const errs = {}
    if (!form.firstName.trim()) errs.firstName = 'First name is required'
    if (!form.userName.trim()) errs.userName = 'Username is required'
    if (!form.email.trim()) errs.email = 'Email is required'
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) errs.email = 'Enter a valid email'
    if (!form.password) errs.password = 'Password is required'
    else if (form.password.length < 8) errs.password = 'Password must be at least 8 characters'
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
      await register(form)
      toast.success('Account created! Please sign in.')
      navigate('/login')
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
          <h1 className={styles.title}>Create your journal</h1>
          <p className={styles.subtitle}>Start writing in seconds</p>
        </div>

        {error && <ErrorMessage message={error} />}

        <form onSubmit={handleSubmit} className={styles.form} noValidate>
          <div className={styles.row}>
            <Input
              label="First name"
              name="firstName"
              type="text"
              value={form.firstName}
              onChange={handleChange}
              placeholder="Jane"
              error={fieldErrors.firstName}
              autoFocus
            />
            <Input
              label="Last name"
              name="lastName"
              type="text"
              value={form.lastName}
              onChange={handleChange}
              placeholder="Doe"
              error={fieldErrors.lastName}
            />
          </div>
          <Input
            label="Username"
            name="userName"
            type="text"
            value={form.userName}
            onChange={handleChange}
            placeholder="jane_doe"
            error={fieldErrors.userName}
            autoComplete="username"
          />
          <Input
            label="Email"
            name="email"
            type="email"
            value={form.email}
            onChange={handleChange}
            placeholder="jane@example.com"
            error={fieldErrors.email}
            autoComplete="email"
          />
          <Input
            label="Password"
            name="password"
            type="password"
            value={form.password}
            onChange={handleChange}
            placeholder="At least 8 characters"
            error={fieldErrors.password}
            autoComplete="new-password"
            hint="Minimum 8 characters"
          />
          <Button type="submit" loading={loading} size="lg" className={styles.submitBtn}>
            Create account
          </Button>
        </form>

        <p className={styles.footer}>
          Already have an account?{' '}
          <Link to="/login">Sign in</Link>
        </p>
      </div>
    </div>
  )
}
