import { Link } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import styles from './NotFound.module.css'

export default function NotFound() {
  const { isAuthenticated } = useAuth()

  return (
    <div className={styles.page}>
      <div className={styles.content}>
        <div className={styles.illustration}>
          <svg width="120" height="120" viewBox="0 0 120 120" fill="none">
            <circle cx="60" cy="60" r="56" fill="#EEF2FF" stroke="#C7D2FE" strokeWidth="1.5"/>
            <text x="60" y="72" textAnchor="middle" fontSize="40" fontFamily="Georgia, serif" fill="#4F46E5" fontWeight="600">?</text>
          </svg>
        </div>
        <h1 className={styles.code}>404</h1>
        <h2 className={styles.title}>Page not found</h2>
        <p className={styles.desc}>
          This page doesn't exist. It may have been moved or the link was typed incorrectly.
        </p>
        <div className={styles.actions}>
          <Link to={isAuthenticated ? '/dashboard' : '/'} className={styles.primaryLink}>
            {isAuthenticated ? 'Back to my journal' : 'Go home'}
          </Link>
          {isAuthenticated && (
            <Link to="/journal/new" className={styles.secondaryLink}>
              Write a new entry
            </Link>
          )}
        </div>
      </div>
    </div>
  )
}
