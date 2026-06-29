import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'
import { useUser } from '../../contexts/UserContext'
import styles from './Navbar.module.css'

export default function Navbar() {
  const { isAuthenticated, logout } = useAuth()
  const { user } = useUser()
  const navigate = useNavigate()
  const location = useLocation()

  const handleLogout = () => {
    logout()
    navigate('/')
  }

  return (
    <header className={styles.header}>
      <nav className={styles.nav}>
        <Link to={isAuthenticated ? '/dashboard' : '/'} className={styles.brand}>
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" className={styles.brandIcon}>
            <rect x="3" y="3" width="14" height="18" rx="2" stroke="currentColor" strokeWidth="1.8"/>
            <line x1="7" y1="8" x2="13" y2="8" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round"/>
            <line x1="7" y1="12" x2="13" y2="12" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round"/>
            <line x1="7" y1="16" x2="11" y2="16" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round"/>
            <path d="M17 6l3.5 3.5L13 17l-3.5.5.5-3.5L17 6z" stroke="currentColor" strokeWidth="1.6" strokeLinejoin="round"/>
          </svg>
          <span>Inkwell</span>
        </Link>

        <div className={styles.right}>
          {isAuthenticated ? (
            <>
              <Link
                to="/dashboard"
                className={[styles.navLink, location.pathname === '/dashboard' ? styles.active : ''].join(' ')}
              >
                Journal
              </Link>
              <Link
                to="/profile"
                className={[styles.navLink, location.pathname === '/profile' ? styles.active : ''].join(' ')}
              >
                Profile
              </Link>
              <button className={styles.logoutBtn} onClick={handleLogout}>
                Sign out
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className={styles.navLink}>Sign in</Link>
              <Link to="/register" className={styles.cta}>Get started</Link>
            </>
          )}
        </div>
      </nav>
    </header>
  )
}
