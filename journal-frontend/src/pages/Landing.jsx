import { Link } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import styles from './Landing.module.css'

const features = [
  {
    icon: (
      <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M12 20h9"/><path d="M16.5 3.5a2.121 2.121 0 013 3L7 19l-4 1 1-4L16.5 3.5z"/>
      </svg>
    ),
    title: 'Write freely',
    desc: 'Distraction-free writing that keeps your thoughts front and center.',
  },
  {
    icon: (
      <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/>
        <line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/>
      </svg>
    ),
    title: 'Every entry, dated',
    desc: 'Automatic timestamps let you look back through your own timeline.',
  },
  {
    icon: (
      <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
      </svg>
    ),
    title: 'Find anything',
    desc: 'Search across all entries instantly. Nothing gets lost.',
  },
  {
    icon: (
      <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <rect x="3" y="11" width="18" height="11" rx="2"/><path d="M7 11V7a5 5 0 0110 0v4"/>
      </svg>
    ),
    title: 'Private & secure',
    desc: 'JWT-protected. Your journal belongs only to you.',
  },
]

export default function Landing() {
  const { isAuthenticated } = useAuth()

  return (
    <div className={styles.page}>
      {/* Hero */}
      <section className={styles.hero}>
        <div className={styles.heroInner}>
          <div className={styles.badge}>Personal journaling, reimagined</div>
          <h1 className={styles.headline}>
            Your thoughts,<br />
            <em>beautifully kept.</em>
          </h1>
          <p className={styles.subhead}>
            Inkwell gives you a calm, distraction-free space to write, reflect, and remember. No noise — just you and your words.
          </p>
          <div className={styles.ctas}>
            {isAuthenticated ? (
              <Link to="/dashboard" className={styles.ctaPrimary}>Go to my journal</Link>
            ) : (
              <>
                <Link to="/register" className={styles.ctaPrimary}>Start writing free</Link>
                <Link to="/login" className={styles.ctaSecondary}>Sign in</Link>
              </>
            )}
          </div>
        </div>

        <div className={styles.heroVisual}>
          <div className={styles.notebook}>
            <div className={styles.notebookLine} />
            <div className={styles.notebookLine} />
            <div className={styles.notebookLine} />
            <div className={styles.notebookEntry}>
              <span className={styles.entryDate}>Today</span>
              <span className={styles.entryTitle}>A quiet morning walk</span>
              <span className={styles.entryText}>The air was cool and the streets were empty. I had time to think...</span>
            </div>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className={styles.features}>
        <div className={styles.container}>
          <h2 className={styles.sectionTitle}>Everything you need to journal well</h2>
          <div className={styles.grid}>
            {features.map((f) => (
              <div key={f.title} className={styles.featureCard}>
                <div className={styles.featureIcon}>{f.icon}</div>
                <h3 className={styles.featureTitle}>{f.title}</h3>
                <p className={styles.featureDesc}>{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA strip */}
      <section className={styles.ctaStrip}>
        <div className={styles.container}>
          <h2 className={styles.ctaTitle}>Ready to start?</h2>
          <p className={styles.ctaDesc}>Join thousands of people who journal with Inkwell every day.</p>
          {!isAuthenticated && (
            <Link to="/register" className={styles.ctaPrimary}>Create your journal</Link>
          )}
        </div>
      </section>
    </div>
  )
}
