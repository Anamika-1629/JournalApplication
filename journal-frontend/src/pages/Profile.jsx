import { useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { useUser } from '../contexts/UserContext'
import Card from '../components/ui/Card'
import Button from '../components/ui/Button'
import styles from './Profile.module.css'

const placeholderCards = [
  {
    icon: (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <rect x="3" y="11" width="18" height="11" rx="2"/>
        <path d="M7 11V7a5 5 0 0110 0v4"/>
      </svg>
    ),
    title: 'Change password',
    desc: 'Update your account password for better security.',
    badge: 'Coming soon',
  },
  {
    icon: (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <circle cx="12" cy="12" r="3"/>
        <path d="M19.07 4.93a10 10 0 010 14.14M4.93 4.93a10 10 0 000 14.14"/>
      </svg>
    ),
    title: 'Preferences',
    desc: 'Customize your writing experience and display settings.',
    badge: 'Coming soon',
  },
  {
    icon: (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <line x1="18" y1="20" x2="18" y2="10"/>
        <line x1="12" y1="20" x2="12" y2="4"/>
        <line x1="6" y1="20" x2="6" y2="14"/>
      </svg>
    ),
    title: 'Statistics',
    desc: 'See your writing streaks, word counts, and entry history.',
    badge: 'Coming soon',
  },
]

export default function Profile() {
  const { logout } = useAuth()
  const { user } = useUser()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/')
  }

  const initials = user?.userName
    ? user.userName.slice(0, 2).toUpperCase()
    : '?'

  return (
    <div className={styles.page}>
      <div className={styles.container}>
        <h1 className={styles.pageTitle}>Profile</h1>

        {/* Identity card */}
        <Card className={styles.profileCard} elevated>
          <div className={styles.identity}>
            <div className={styles.avatar}>{initials}</div>
            <div className={styles.info}>
              <h2 className={styles.name}>{user?.userName || '—'}</h2>
              {user?.email && <p className={styles.email}>{user.email}</p>}
              {user?.roles?.length > 0 && (
                <div className={styles.roles}>
                  {user.roles.map((r) => (
                    <span key={r} className={styles.badge}>
                      {r.replace('ROLE_', '')}
                    </span>
                  ))}
                </div>
              )}
            </div>
          </div>

          <div className={styles.divider} />

          <div className={styles.cardActions}>
            <Button variant="ghost" onClick={() => navigate('/dashboard')}>
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/>
                <polyline points="9 22 9 12 15 12 15 22"/>
              </svg>
              My Journal
            </Button>
            <Button variant="danger" onClick={handleLogout}>
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4"/>
                <polyline points="16 17 21 12 16 7"/>
                <line x1="21" y1="12" x2="9" y2="12"/>
              </svg>
              Sign out
            </Button>
          </div>
        </Card>

        {/* Placeholder feature cards */}
        <h2 className={styles.sectionTitle}>More features</h2>
        <div className={styles.grid}>
          {placeholderCards.map((c) => (
            <div key={c.title} className={styles.featureCard}>
              <div className={styles.featureHeader}>
                <div className={styles.featureIcon}>{c.icon}</div>
                <span className={styles.featureBadge}>{c.badge}</span>
              </div>
              <h3 className={styles.featureTitle}>{c.title}</h3>
              <p className={styles.featureDesc}>{c.desc}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
