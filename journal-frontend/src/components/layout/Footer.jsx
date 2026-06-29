import { Link } from 'react-router-dom'
import styles from './Footer.module.css'

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <div className={styles.inner}>
        <span className={styles.brand}>Inkwell</span>
        <p className={styles.copy}>© {new Date().getFullYear()} Inkwell. Write more, forget less.</p>
        <div className={styles.links}>
          <Link to="/" className={styles.link}>Home</Link>
          <Link to="/login" className={styles.link}>Sign in</Link>
        </div>
      </div>
    </footer>
  )
}
