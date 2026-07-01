import styles from './ErrorMessage.module.css'

export default function ErrorMessage({ message, onRetry }) {
  if (!message) return null

  return (
    <div className={styles.wrapper} role="alert">
      <svg className={styles.icon} width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <circle cx="12" cy="12" r="10"/>
        <line x1="12" y1="8" x2="12" y2="12"/>
        <line x1="12" y1="16" x2="12.01" y2="16"/>
      </svg>
      <span className={styles.message}>{message}</span>
      {onRetry && (
        <button className={styles.retry} onClick={onRetry}>
          Try again
        </button>
      )}
    </div>
  )
}
